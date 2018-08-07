/**
 * Copyright © 2014-2018 Darwino, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.darwino.domino.napi.wrap.design;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.HOTSPOTREC_RUNFLAG;
import com.darwino.domino.napi.enums.HOTSPOTREC_TYPE;
import com.darwino.domino.napi.enums.SIG_CD;
import com.darwino.domino.napi.proc.ActionRoutinePtr;
import com.darwino.domino.napi.struct.cd.CDBEGINRECORD;
import com.darwino.domino.napi.struct.cd.CDDATAFLAGS;
import com.darwino.domino.napi.struct.cd.CDEMBEDDEDCTL;
import com.darwino.domino.napi.struct.cd.CDENDRECORD;
import com.darwino.domino.napi.struct.cd.CDEXTFIELD;
import com.darwino.domino.napi.struct.cd.CDFIELD;
import com.darwino.domino.napi.struct.cd.CDFIELDHINT;
import com.darwino.domino.napi.struct.cd.CDFieldStruct;
import com.darwino.domino.napi.struct.cd.CDHOTSPOTBEGIN;
import com.darwino.domino.napi.struct.cd.CDKEYWORD;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.NSFItem;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.item.NSFCompositeDataItem;

/**
 * @author Jesse Gallagher
 * @since 1.5.0
 *
 */
public class NSFForm extends NSFDesignNoteBase {

	public NSFForm(NSFDatabase parent, int noteId) {
		super(parent, noteId);
	}

	/**
	 * Returns a {@link List} of the fields on the form, as {@link NSFFormField}, in their order on the form.
	 * This optionally may include the fields from non-computed subforms.
	 * 
	 * @param includeSubforms whether the result should include fields from non-computed subforms
	 * @return a list of <code>NSFFormField</code>s for the form
	 * @throws DominoException if there is a lower-level-API problem retrieving the list of fields
	 */
	public List<NSFFormField> getFields(final boolean includeSubforms) throws DominoException {
		final List<NSFFormField> result = new ArrayList<NSFFormField>();
		// Establish a stack of related field structs
		final Deque<CDFieldStruct> fieldStructs = new ArrayDeque<CDFieldStruct>();
		
		NSFNote note = getNote();
		try {
			List<NSFItem> bodyItems = note.getAllItems(DominoAPI.ITEM_NAME_TEMPLATE);
			for(NSFItem item : bodyItems) {
				if(item instanceof NSFCompositeDataItem) {
					// This should always be the case
					
					// To keep track of BEGINRECORD/ENDRECORD pairs for fields
					final AtomicBoolean foundFieldBegin = new AtomicBoolean();
					
					((NSFCompositeDataItem)item).eachRecord(new ActionRoutinePtr() {
						@Override public short callback(long recordPtr, short recordType, int recordLength) throws DominoException {
							SIG_CD sig = DominoEnumUtil.valueOf(SIG_CD.class, recordType);
							
							switch(sig) {
							case BEGIN: {
								// Check to see if it's the start of a field
								CDBEGINRECORD beginRecord = new CDBEGINRECORD();
								try {
									beginRecord.readODSVariableData(api, recordPtr);
									if(beginRecord.getSignature() == SIG_CD.FIELD || beginRecord.getSignature() == SIG_CD.EMBEDDEDCTL) {
										foundFieldBegin.set(true);
									}
								} finally {
									beginRecord.free();
								}
								break;
							}
							case FIELD: {
								CDFIELD cdField = new CDFIELD();
								cdField.readODSVariableData(api, recordPtr);
								fieldStructs.add(cdField);
								break;
							}
							case FIELDHINT: {
								CDFIELDHINT cdFieldHint = new CDFIELDHINT();
								cdFieldHint.readODSVariableData(api, recordPtr);
								fieldStructs.add(cdFieldHint);
								break;
							}
							case EXT_FIELD: {
								// TODO figure out if this loop needs to account for a pre-R5 field with a CDHTMLFORMULA afterwards
								CDEXTFIELD cdExtField = new CDEXTFIELD();
								cdExtField.readODSVariableData(api, recordPtr);
								fieldStructs.add(cdExtField);
								break;
							}
							case EXT2_FIELD: {
								// TODO add impl
								break;
							}
							case COLOR: {
								// Don't close fields on COLOR
								break;
							}
							case DATAFLAGS: {
								if(foundFieldBegin.get()) {
									// TODO add impl to field structs
									CDDATAFLAGS cdDataFlags = new CDDATAFLAGS();
									cdDataFlags.readODSVariableData(api, recordPtr);
									fieldStructs.add(cdDataFlags);
								} else if(!fieldStructs.isEmpty() && !foundFieldBegin.get()) {
									flushField(fieldStructs, result);
								}
								break;
							}
							case KEYWORD: {
								CDKEYWORD cdKeyword = new CDKEYWORD();
								cdKeyword.readODSVariableData(api, recordPtr);
								fieldStructs.add(cdKeyword);
								break;
							}
							case EMBEDDEDCTL: {
								CDEMBEDDEDCTL cdEmbeddedCtl = new CDEMBEDDEDCTL();
								cdEmbeddedCtl.readODSVariableData(api, recordPtr);
								fieldStructs.add(cdEmbeddedCtl);
								break;
							}
							case END: {
								// Check to see if it's ending an open field
								CDENDRECORD endRecord = new CDENDRECORD();
								try {
									endRecord.readODSVariableData(api, recordPtr);
									if(endRecord.getSignature() == SIG_CD.FIELD || endRecord.getSignature() == SIG_CD.EMBEDDEDCTL) {
										flushField(fieldStructs, result);
										foundFieldBegin.set(false);
									}
								} finally {
									endRecord.free();
								}
								break;
							}
							case HOTSPOTBEGIN:
							case V4HOTSPOTBEGIN: {
								// First, flush any queued field structs, in the case of pre-R5 fields
								if(!fieldStructs.isEmpty() && !foundFieldBegin.get()) {
									flushField(fieldStructs, result);
								}
								
								if(includeSubforms) {
									// Then read in the hotspot to see if it's a subform type
									
									CDHOTSPOTBEGIN hotspot = new CDHOTSPOTBEGIN();
									try {
										hotspot.readODSVariableData(api, recordPtr);
										if(hotspot.getType() == HOTSPOTREC_TYPE.SUBFORM && !hotspot.getFlags().contains(HOTSPOTREC_RUNFLAG.FORMULA)) {
											// Then load the subform and incorporate its fields
											String subformName = hotspot.getSubformName();
											NSFForm subform = getParent().getDesign().getSubform(subformName);
											if(subform != null) {
												result.addAll(subform.getFields(true));
											}
										}
									} finally {
										hotspot.free();
									}
								}
								
								break;
							}
							default: {
								// First, flush any queued field structs, in the case of pre-R5 fields
								if(!fieldStructs.isEmpty() && !foundFieldBegin.get()) {
									flushField(fieldStructs, result);
								}
								
								// ignore
								break;
							}
							}
							
							return DominoAPI.NOERROR;
						}
					});
					
					// Flush any remaining queued field struct
					if(!fieldStructs.isEmpty()) {
						flushField(fieldStructs, result);
					}
				}
			}
		} finally {
			note.free();
			
			if(!fieldStructs.isEmpty()) {
				// Then there must have been a problem reading in a field - clear the structs
				for(CDFieldStruct struct : fieldStructs) {
					struct.free();
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the field for the provided (case-insensitive) name, or <code>null</code> if
	 * the field does not exist on the form.
	 *   
	 * @param fieldName the case-insensitive field name
	 * @param includeSubforms whether to search fields from non-computed subforms
	 * @return the {@link NSFFormField}, or <code>null</code> if not present
	 * @throws DominoException 
	 */
	public NSFFormField getField(String fieldName, boolean includeSubforms) throws DominoException {
		List<NSFFormField> fields = getFields(includeSubforms);
		NSFFormField result = null;
		for(NSFFormField field : fields) {
			if(field.getName().equalsIgnoreCase(fieldName)) {
				result = field;
			} else {
				field.free();
			}
		}
		return result;
	}
	
	public boolean isSubform() throws DominoException {
		return getFlagsString().indexOf(DominoAPI.DESIGN_FLAG_SUBFORM) > -1;
	}
	
	/**
	 * Creates an NSFFormField out of the structs, if applicable, adds any created field
	 * to the provided pool, and clears the collection.
	 * @throws DominoException 
	 */
	private void flushField(Collection<CDFieldStruct> structs, Collection<NSFFormField> fieldsPool) throws DominoException {
		if(structs != null && !structs.isEmpty()) {
			// Make sure there's a CDFIELD
			boolean foundField = false;
			for(CDFieldStruct struct : structs) {
				if(struct instanceof CDFIELD) {
					foundField = true;
					break;
				}
			}
			if(foundField) {
				NSFFormField field = addChild(new NSFFormField(this, structs));
				fieldsPool.add(field);
			}
			structs.clear();
		}
	}
}
