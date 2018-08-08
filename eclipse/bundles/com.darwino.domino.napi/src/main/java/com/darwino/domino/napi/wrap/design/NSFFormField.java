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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.enums.CDKEYWORD_Flag;
import com.darwino.domino.napi.enums.EC_STYLE;
import com.darwino.domino.napi.enums.EMBEDDEDCTL_TYPE;
import com.darwino.domino.napi.enums.FEXT1;
import com.darwino.domino.napi.enums.FEXT2;
import com.darwino.domino.napi.enums.FieldFlag;
import com.darwino.domino.napi.enums.LDDELIM;
import com.darwino.domino.napi.enums.LDELIM;
import com.darwino.domino.napi.enums.ValueType;
import com.darwino.domino.napi.struct.cd.CDDATAFLAGS;
import com.darwino.domino.napi.struct.cd.CDEMBEDDEDCTL;
import com.darwino.domino.napi.struct.cd.CDEXTFIELD;
import com.darwino.domino.napi.struct.cd.CDFIELD;
import com.darwino.domino.napi.struct.cd.CDFIELDHINT;
import com.darwino.domino.napi.struct.cd.CDFieldStruct;
import com.darwino.domino.napi.struct.cd.CDKEYWORD;
import com.darwino.domino.napi.wrap.NSFBase;
import com.darwino.domino.napi.wrap.NSFFormula;

/**
 * This class represents the properties of a field defined in an {@link NSFForm}.
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 *
 */
public class NSFFormField extends NSFBase {
	private final NSFForm parent;
	
	/**
	 * This enum represents the various display types of form fields, which are more specific than
	 * the underlying {@link ValueType}.
	 * 
	 * @author Jesse Gallagher
	 * @since 1.5.0
	 *
	 */
	public static enum DisplayType {
		TEXT, DATETIME, NUMBER, DIALOG_LIST, CHECKBOX, RADIO_BUTTON, LISTBOX, COMBOBOX, RICH_TEXT,
		AUTHORS, NAMES, READERS, PASSWORD, FORMULA, TIME_ZONE, RICH_TEXT_LITE, COLOR,
		
		/* This doesn't match a Designer type, but is implied by style */
		TEXTAREA
	}
	
	private CDFIELD cdField;
	private CDFIELDHINT cdFieldHint;
	private CDEXTFIELD cdExtField;
	private CDDATAFLAGS cdDataFlags;
	private CDKEYWORD cdKeyword;
	private CDEMBEDDEDCTL cdEmbeddedCtl;
	
	/**
	 * Constructs an <code>NSFFormField</code> using the provided collection of field-related structs.
	 * The collection must contain at least a {@link CDFIELD} object.
	 * 
	 * @param parent the {@link NSFForm} that contains the field
	 * @param structs a {@link Collection} of {@link CDFieldStruct} objects representing the field on the form
	 * @throws IllegalArgumentException if <code>structs</code> does not contain a {@link CDFIELD}
	 */
	public NSFFormField(NSFForm parent, Collection<CDFieldStruct> structs) {
		super(parent.getAPI());
		
		this.parent = parent;
		
		if(structs != null) {
			// Retain any structures that aren't going to be kept, to be safe
			List<CDFieldStruct> burners = new ArrayList<CDFieldStruct>();
			
			for(CDFieldStruct struct : structs) {
				if(struct instanceof CDFIELD && this.cdField == null) {
					this.cdField = (CDFIELD)struct;
				} else if(struct instanceof CDFIELDHINT && this.cdFieldHint == null) {
					this.cdFieldHint = (CDFIELDHINT)struct;
				} else if(struct instanceof CDEXTFIELD && this.cdExtField == null) {
					this.cdExtField = (CDEXTFIELD)struct;
				} else if(struct instanceof CDDATAFLAGS && this.cdDataFlags == null) {
					this.cdDataFlags = (CDDATAFLAGS)struct;
				} else if(struct instanceof CDKEYWORD && this.cdKeyword == null) {
					this.cdKeyword = (CDKEYWORD)struct;
				} else if(struct instanceof CDEMBEDDEDCTL && this.cdEmbeddedCtl == null) {
					this.cdEmbeddedCtl = (CDEMBEDDEDCTL)struct;
				} else {
					burners.add(struct);
				}
			}
			
			for(CDFieldStruct struct : burners) {
				struct.free();
			}
		}
		
		if(cdField == null) {
			throw new IllegalArgumentException("structs collection must contain a CDFIELD. Collection: " + structs);
		}
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return cdField.getName();
	}
	/**
	 * @return the type
	 */
	public ValueType getType() {
		return cdField.getDataType();
	}
	/**
	 * @return the flags
	 */
	public Set<FieldFlag> getFlags() {
		return Collections.unmodifiableSet(EnumSet.copyOf(cdField.getFlags()));
	}
	
	/**
	 * @return the listDelim
	 */
	public Set<LDELIM> getListDelim() {
		return Collections.unmodifiableSet(EnumSet.copyOf(cdField.getListDelim()));
	}
	
	/**
	 * @return the listDisplayDelim
	 */
	public LDDELIM getListDisplayDelim() {
		return cdField.getListDisplayDelim();
	}
	
	/**
	 * @return the defaultValueFormula
	 * @throws DominoException 
	 */
	public NSFFormula getDefaultValueFormula() throws DominoException {
		return cdField.getDefaultValueFormula();
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return cdField.getDescription();
	}
	
	/**
	 * @return the inputTranslationFormula
	 * @throws DominoException 
	 */
	public NSFFormula getInputTranslationFormula() throws DominoException {
		return cdField.getInputTranslationFormula();
	}
	
	/**
	 * @return the inputValidationFormula
	 * @throws DominoException 
	 */
	public NSFFormula getInputValidationFormula() throws DominoException {
		return cdField.getInputValidationFormula();
	}
	
	/**
	 * @return the valueFormula
	 * @throws DominoException 
	 */
	public NSFFormula getValuesFormula() throws DominoException {
		return cdField.getValuesFormula();
	}
	
	public String getHintText() {
		if(cdFieldHint != null) {
			return cdFieldHint.getHintText();
		} else {
			return null;
		}
	}
	
	public DisplayType getDisplayType() {
		Set<FieldFlag> flags = cdField.getFlags();
		
		ValueType type = this.getType();
		if(type == null) {
			return null;
		}
		
		switch(type) {
		case COMPOSITE:
			// This should cover both Rich Text and Rich Text Lite
			return DisplayType.RICH_TEXT;
		case FORMULA:
			return DisplayType.FORMULA;
		case MIME_PART:
			// TODO check if this actually happens
			return DisplayType.RICH_TEXT;
		case NUMBER:
		case NUMBER_RANGE:
			return DisplayType.NUMBER;
		case USERID:
			// V1/V2 "Authors" type is still used for field indicators
			if(flags.contains(FieldFlag.READERS)) {
				return DisplayType.READERS;
			} else {
				return DisplayType.AUTHORS;
			}
		case TEXT:
		case TEXT_LIST:
			// There may be a CDEMBEDDEDCTL
			if(cdEmbeddedCtl != null) {
				EMBEDDEDCTL_TYPE ctlType = cdEmbeddedCtl.getCtlType();
				if(ctlType != null) {
					switch(ctlType) {
					case COMBO:
					case KEYGEN:
						return DisplayType.COMBOBOX;
					case LIST:
						return DisplayType.LISTBOX;
					case TIMEZONE:
						return DisplayType.TIME_ZONE;
					case COLOR:
						return DisplayType.COLOR;
					case EDIT:
					case FILE:
					case TIME:
						break;
					}
				}
				
				EC_STYLE style = cdEmbeddedCtl.getCtlStyle();
				if(style != null) {
					switch(style) {
					case EDITCOMBO:
						return DisplayType.COMBOBOX;
					case EDITPASSWORD:
						return DisplayType.PASSWORD;
					case LISTMULTISEL:
						return DisplayType.LISTBOX;
					case TIMEZONE:
						return DisplayType.TIME_ZONE;
					case EDITMULTILINE:
						return DisplayType.TEXTAREA;
					case TIME:
						return DisplayType.DATETIME;
					case CALENDAR:
					case DURATION:
					case EDITVSCROLL:
						// No special support for these yet
						break;
					}
				}
			}
			// Check any CDEXTFIELD
			if(cdExtField != null) {
				Set<FEXT1> flags1 = cdExtField.getFlags1();
				Set<FEXT2> flags2 = cdExtField.getFlags2();
				if(flags2.contains(FEXT2.PASSWORD)) {
					return DisplayType.PASSWORD;
				} else if(flags2.contains(FEXT2.TIMEZONE)) {
					return DisplayType.TIME_ZONE;
				} else if(flags1.contains(FEXT1.KEYWORDS_UI_COMBO)) {
					return DisplayType.COMBOBOX;
				} else if(flags1.contains(FEXT1.KEYWORDS_UI_LIST)) {
					return DisplayType.LISTBOX;
				}
			}

			// Check a CDKEYWORD struct if present - this may indicate radio buttons
			if(cdKeyword != null) {
				Set<CDKEYWORD_Flag> keywordFlags = cdKeyword.getFlags();
				if(keywordFlags.contains(CDKEYWORD_Flag.RADIO)) {
					return DisplayType.RADIO_BUTTON;
				}
			}
			// Finally, fall back to the base field
			if(flags.contains(FieldFlag.KEYWORDS)) {
				if(flags.contains(FieldFlag.KEYWORDS_UI_CHECKBOX)) {
					return DisplayType.CHECKBOX;
				} else if(flags.contains(FieldFlag.KEYWORDS_UI_RADIOBUTTON)) {
					return DisplayType.RADIO_BUTTON;
				} else {
					return DisplayType.DIALOG_LIST;
				}
			}
			
			if(flags.contains(FieldFlag.READERS)) {
				return DisplayType.READERS;
			} else if(flags.contains(FieldFlag.READWRITERS)) {
				return DisplayType.AUTHORS;
			} else if(flags.contains(FieldFlag.NAMES)) {
				return DisplayType.NAMES;
			} else {
				return DisplayType.TEXT;
			}
		case TIME:
		case TIME_RANGE:
			return DisplayType.DATETIME;
		case ACTION:
		case ASSISTANT_INFO:
		case CALENDAR_FORMAT:
		case COLLATION:
		case ERROR:
		case HIGHLIGHTS:
		case HTML:
		case ICON:
		case INVALID_OR_UNKNOWN:
		case LSOBJECT:
		case NOTELINK_LIST:
		case NOTEREF_LIST:
		case OBJECT:
		case QUERY:
		case RFC822_TEXT:
		case SCHED_LIST:
		case SEAL:
		case SEALDATA:
		case SEAL_LIST:
		case SIGNATURE:
		case UNAVAILABLE:
		case USERDATA:
		case VIEWMAP_DATASET:
		case VIEWMAP_LAYOUT:
		case VIEW_FORMAT:
		case WORKSHEET_DATA:
		default:
			// These have no UI representation
			return null;
		}
	}
	
	public boolean isAllowMultiple() {
		// Some types imply multiple
		ValueType type = getType();
		if(type == null) {
			return false;
		}
		
		switch(type) {
		case TEXT_LIST:
		case NUMBER_RANGE:
		case TIME_RANGE:
			return true;
		default:
			break;
		}
		
		// Some display types also do
		DisplayType uiType = getDisplayType();
		if(uiType == null) {
			return false;
		}
		
		switch(uiType) {
		case CHECKBOX:
			return true;
		default:
			break;
		}
		
		return false;
	}
	
	/**
	 * @return if a keyword-type field allows user-added keywords
	 */
	public boolean isAllowUserKeywords() {
		Set<FieldFlag> flags = cdField.getFlags();
		return flags != null && flags.contains(FieldFlag.KEYWORDS) && flags.contains(FieldFlag.KEYWORDS_UI_ALLOW_NEW);
	}
	
	/**
	 * @return the values
	 */
	public String[] getValues() {
		// Values may be in two places
		String[] localValues = cdField.getValues();
		String[] keywordValues = null;
		if(cdKeyword != null) {
			keywordValues = cdKeyword.getKeywordValues();
		}
		if(keywordValues != null && keywordValues.length > 0) {
			return keywordValues;
		} else if(localValues != null && localValues.length > 0) {
			return localValues;
		} else {
			return new String[0];
		}
	}
	
	/**
	 * Determines whether the field is a shared-field reference only, without
	 * locally-stored field information.
	 * 
	 * @return <code>true</code> if the field is only a reference; <code>false</code>
	 * 		otherwise
	 */
	public boolean isReferenceOnly() {
		Set<FieldFlag> flags = this.getFlags();
		if(flags.contains(FieldFlag.REFERENCE)) {
			// Consider it a reference if there is no specified type
			DisplayType type = this.getDisplayType();
			return type == null;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#doFree()
	 */
	@Override
	protected void doFree() {
		cdField = null;
		cdFieldHint = null;
		cdExtField = null;
		cdDataFlags = null;
		cdKeyword = null;
		cdEmbeddedCtl = null;
	}
	
	@Override
	protected Recycler createRecycler() {
		// NOP
		return null;
	}

	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#isRefValid()
	 */
	@Override
	public boolean isRefValid() {
		return cdField != null;
	}

	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#getParent()
	 */
	@Override
	public NSFForm getParent() {
		return parent;
	}
}
