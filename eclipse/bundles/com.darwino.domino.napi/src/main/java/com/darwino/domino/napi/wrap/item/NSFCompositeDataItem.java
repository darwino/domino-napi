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
package com.darwino.domino.napi.wrap.item;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.enums.SIG_CD;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.HOTSPOTREC_TYPE;
import com.darwino.domino.napi.enums.ValueType;
import com.darwino.domino.napi.proc.ActionRoutinePtr;
import com.darwino.domino.napi.struct.BLOCKID;
import com.darwino.domino.napi.struct.cd.CDFILEHEADER;
import com.darwino.domino.napi.struct.cd.CDFILESEGMENT;
import com.darwino.domino.napi.struct.cd.CDHOTSPOTBEGIN;
import com.darwino.domino.napi.util.DominoNativeUtils;
import com.darwino.domino.napi.util.Ref;
import com.darwino.domino.napi.wrap.NSFFileAttachment;
import com.darwino.domino.napi.wrap.NSFItem;
import com.darwino.domino.napi.wrap.NSFNote;
import com.ibm.commons.log.LogMgr;

/**
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public class NSFCompositeDataItem extends NSFItem {
	
	private final LogMgr log = DominoNativeUtils.NAPI_LOG;

	/**
	 * @param parent
	 * @param name
	 * @param itemBlockId
	 * @param dataType
	 * @param valueBlockId
	 * @param valueLen
	 */
	public NSFCompositeDataItem(NSFNote parent, String name, BLOCKID itemBlockId, ValueType dataType, BLOCKID valueBlockId, int valueLen) {
		super(parent, name, itemBlockId, dataType, valueBlockId, valueLen);
	}

	public List<NSFFileAttachment> getAttachments() throws DominoException {
		final List<NSFFileAttachment> result = new ArrayList<NSFFileAttachment>();
		// Maintain a set of used attachment names so as to not add the same attachment multiple times.
		// This can occur when an RT field references the same attachment twice
		final Set<String> usedAttNames = new HashSet<String>();
		
		eachRecord(new ActionRoutinePtr() {
			@Override public short callback(long recordPtr, short recordType, int recordLength) throws DominoException {
				try {
					if(recordType != 0) {
						SIG_CD sig = DominoEnumUtil.valueOf(SIG_CD.class, recordType);
						if(sig == null) {
							// Just skip over the record in this case, since we definitely don't care about it
							return DominoAPI.NOERROR;
						}
						switch(sig) {
						case HOTSPOTBEGIN:
							// Then this could be a file
							CDHOTSPOTBEGIN hotspot = new CDHOTSPOTBEGIN();
							try {
								hotspot.readODSVariableData(api, recordPtr);
								if(hotspot.getType() == HOTSPOTREC_TYPE.FILE) {
									String internalName = hotspot.getFileInternalName();
									if(!usedAttNames.contains(internalName)) {
										result.add(addChild(new NSFFileAttachment(NSFCompositeDataItem.this, internalName, hotspot.getFileOriginalName())));
										usedAttNames.add(internalName);
									}
								}
							} finally {
								hotspot.free();
							}
							break;
						default:
							break;
						}
					}
				} catch(Throwable t) {
					if(log.isErrorEnabled()) {
						log.error(t, "Exception while enumerating composite data buffer");
					}
					return DominoAPI.ERR_CANCEL;
				}
				
				return DominoAPI.NOERROR;
			}
		});
		
		return result;
	}
	
	public void eachRecord(ActionRoutinePtr callback) throws DominoException {
		api.EnumCompositeBuffer(getValueBlockId(), getValueLen(), callback);
	}
	
	/**
	 * Returns the file size from the <code>CDFILEHEADER</code> record in a file resource design element.
	 * 
	 * <p>This will <strong>only</strong> work with those types of elements and will not return e.g. file attachment
	 * sizes.</p>
	 * 
	 * @return the size of the file resource, according to the <code>CDFILEHEADER</code>
	 */
	public long getFileResourceSize() throws DominoException {
		final Ref<Long> size = new Ref<Long>();
		final Ref<Throwable> exception = new Ref<Throwable>();
		
		// Use eachRecord just to get to the first one cleanly
		eachRecord(new ActionRoutinePtr() {
			@Override public short callback(long recordPtr, short recordType, int recordLength) throws DominoException {
				if(recordType != 0) {
					SIG_CD sig = DominoEnumUtil.valueOf(SIG_CD.class, recordType);
					switch(sig) {
					case FILEHEADER:
						// Then this is the start of the file
						CDFILEHEADER header = new CDFILEHEADER();
						try {
							header.readODSVariableData(api, recordPtr);
							size.set(header.getFileDataSize() & 0xFFFFFFFFL);
						} catch(Exception e) {
							exception.set(e);
						} finally {
							header.free();
						}
						return DominoAPI.ERR_CANCEL;
					case FILESEGMENT:
						// Then this must not be the opening item
						exception.set(new UnsupportedOperationException("Must be used on the first Composite Data item in the note"));
						return DominoAPI.ERR_CANCEL;
					default:
						exception.set(new UnsupportedOperationException("Cannot be used on non-file-resource entities"));
						return DominoAPI.ERR_CANCEL;
					}
				}
				
				return DominoAPI.NOERROR;
			}
		});
		
		if(exception.get() instanceof RuntimeException) {
			throw (RuntimeException)exception.get();
		} else if(exception.get() instanceof DominoException) {
			throw (DominoException)exception.get();
		} else if(exception.get() != null) {
			throw new DominoException(exception.get());	
		}
		
		return size.get();
	}
	
	/**
	 * Returns the file data for a <code>CDFILEHEADER</code>+<code>CDFILESEGMENT</code>-type design element.
	 * 
	 * <p>This will <strong>only</strong> work with those types of elements and will not return e.g. file attachment
	 * data.</p>
	 * 
	 * @return a byte array of the file resource data
	 */
	public byte[] getFileResourceData() throws DominoException {
		// First, check the first record to make sure it is indeed a CDFILEHEADER
		// TODO switch to using CompositeDataList when complete
		
		final Ref<ByteArrayOutputStream> baos = new Ref<ByteArrayOutputStream>();
		baos.set(new ByteArrayOutputStream());
		final Ref<Throwable> exception = new Ref<Throwable>();
		eachRecord(new ActionRoutinePtr() {
			@Override public short callback(long recordPtr, short recordType, int recordLength) throws DominoException {
				if(recordType != 0) {
					SIG_CD sig = DominoEnumUtil.valueOf(SIG_CD.class, recordType);
					switch(sig) {
					case FILEHEADER:
						// Then this is the start of the file
						break;
					case FILESEGMENT:
						// Then this is part of the file - read its data
						CDFILESEGMENT segment = new CDFILESEGMENT();
						try {
							segment.readODSVariableData(api, recordPtr);
							baos.get().write(segment.getFileData());
						} catch(Exception e) {
							exception.set(e);
						} finally {
							segment.free();
						}
						
						break;
					default:
						exception.set(new UnsupportedOperationException("Cannot be used on non-file-resource entities"));
						return DominoAPI.ERR_CANCEL;
					}
				}
				
				return DominoAPI.NOERROR;
			}
		});
		
		if(exception.get() instanceof RuntimeException) {
			throw (RuntimeException)exception.get();
		} else if(exception.get() instanceof DominoException) {
			throw (DominoException)exception.get();
		} else if(exception.get() != null) {
			throw new DominoException(exception.get());	
		}
		
		return baos.get().toByteArray();
	}
	
	/**
	 * The Composite Data implementation defers to converting the item to text and returning that,
	 */
	@Override
	public Object[] getValue() throws DominoException {
		return new Object[] { getParent().getAsString(this.getName(), ' ') };
	}
}
