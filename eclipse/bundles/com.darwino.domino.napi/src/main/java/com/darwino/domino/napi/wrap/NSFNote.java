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
package com.darwino.domino.napi.wrap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import lotus.domino.Document;
import lotus.domino.NotesException;

import com.ibm.commons.log.LogMgr;
import com.ibm.commons.util.NotImplementedException;
import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.c.IntRef;
import com.darwino.domino.napi.c.ShortRef;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.ValueType;
import com.darwino.domino.napi.proc.NSFITEMSCANPROC;
import com.darwino.domino.napi.struct.BLOCKID;
import com.darwino.domino.napi.struct.BaseStruct;
import com.darwino.domino.napi.struct.FILEOBJECT;
import com.darwino.domino.napi.struct.LIST;
import com.darwino.domino.napi.struct.OID;
import com.darwino.domino.napi.struct.RANGE;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.struct.UNIVERSALNOTEID;
import com.darwino.domino.napi.util.DominoNativeUtils;
import com.darwino.domino.napi.util.DominoUtils;
import com.darwino.domino.napi.wrap.item.NSFCompositeData;
import com.darwino.domino.napi.wrap.item.NSFCompositeDataItem;
import com.darwino.domino.napi.wrap.item.NSFFileItem;
import com.darwino.domino.napi.wrap.item.NSFMimeItem;
import com.darwino.domino.napi.wrap.item.NSFObjectItem;

public class NSFNote extends NSFHandle {
	
	@SuppressWarnings("unused")
	private static final LogMgr log = DominoNativeUtils.NAPI_LOG;
	
	public static interface NSFNoteItemProc {
		void callback(String itemName, Object[] value) throws Exception;
	}
	
	public static NSFNote fromLotus(NSFDatabase parent, Document lotusDoc) throws NotesException, DominoException {
		int noteID = Integer.parseInt(lotusDoc.getNoteID(), 16);
		return parent.getNoteByID(noteID);
	}

	NSFDatabase parent;
	
	public NSFNote(NSFDatabase parent, long handle) {
		super(parent.getSession(), handle);
		this.parent = parent;
	}
	
	@Override
	public NSFDatabase getParent() {
		return parent;
	}
	
	public void save() throws DominoException {
		save(0);
	}
	public void save(int flags) throws DominoException {
		_checkRefValidity();
		
		api.NSFNoteCheck(this.getHandle());
		api.NSFNoteUpdateExtended(this.getHandle(), flags);
	}
	
	public int getNoteID() throws DominoException {
		_checkRefValidity();
		
		long idPtr = C.malloc(C.sizeOfNOTEID);
		try {
			api.NSFNoteGetInfo(getHandle(), DominoAPI._NOTE_ID, idPtr);
			int result = C.getNOTEID(idPtr, 0);
			return result;
		} finally {
			C.free(idPtr);
		}
	}
	/**
	 * Retrieves the note's OID. This structure is freed when the parent note is freed.
	 * 
	 * @return the note's OID
	 * @throws DominoException if there is a problem retrieving the OID
	 */
	public OID getOID() throws DominoException {
		_checkRefValidity();
		
		OID oid = new OID();
		api.NSFNoteGetInfo(this.getHandle(), DominoAPI._NOTE_OID, oid.getDataPtr());
		return addChildStruct(oid);
	}
	public String getUniversalID() throws DominoException {
		_checkRefValidity();
		
		OID oid = getOID();
		try {
			return oid.getUNID();
		} finally {
			oid.free();
		}
	}
	
	public void setSequence(int id) throws DominoException {
		_checkRefValidity();
		
		OID oid = new OID();
		try {
			api.NSFNoteGetInfo(this.getHandle(), DominoAPI._NOTE_OID, oid.getDataPtr());
			oid.setSequence(id);
			api.NSFNoteSetInfo(this.getHandle(), DominoAPI._NOTE_OID, oid.getDataPtr());
		} finally {
			oid.free();
		}
	}
	
	/**
	 * Returns the note ID of this note's parent, as an <code>int</code>, or <code>0</code> if
	 * this note has no parent.
	 * 
	 * @return the note ID of this note's parent, as an <code>int</code>, or <code>0</code> if
	 * this note has no parent.
	 */
	public int getParentDocumentID() {
		_checkRefValidity();
		
		long pintRef = C.malloc(C.sizeOfNOTEID);
		try {
			api.NSFNoteGetInfo(this.getHandle(), DominoAPI._NOTE_PARENT_NOTEID, pintRef);
			int parentId = C.getNOTEID(pintRef, 0);
			return parentId;
		} finally {
			C.free(pintRef);
		}
	}
	
	/**
	 * Returns the UNID of this note's parent, or <code>""</code> if this note has no parent.
	 * 
	 * @return the UNID of this note's parent, or <code>""</code> if this note has no parent.
	 */
	public String getParentDocumentUNID() throws DominoException {
		Object[] ref = get(DominoAPI.FIELD_LINK);
		if(ref.length == 0 || !(ref[0] instanceof String)) {
			return ""; //$NON-NLS-1$
		} else {
			return (String)ref[0];
		}
	}
	
	public void makeResponse(NSFNote parentNote) throws DominoException {
		setNoteRef(DominoAPI.FIELD_LINK, parentNote.getUniversalID());
	}

	/**
	 * Sets the document's parent UNID to the provided UNID string. This is equivalent to calling
	 * {@link #makeResponse(NSFNote)} with the parent note, but does not require that the
	 * parent note be open (or, for that matter, exist).
	 * 
	 * @param parentUnid the new parent UNID. This must be a 32-character hex string
	 * @throws DominoException if there is a problem setting the parent UNID
	 * @throws IllegalArgumentException if <code>parentUnid</code> is not a legal UNID
	 */
	public void setParentDocumentUNID(String parentUnid) throws DominoException {
		_checkRefValidity();
		
		if(StringUtil.isEmpty(parentUnid) || !DominoUtils.isUnid(parentUnid)) {
			throw new IllegalArgumentException(StringUtil.format("\"{0}\" is not a legal UNID", parentUnid)); //$NON-NLS-1$
		}
		
		setNoteRef(DominoAPI.FIELD_LINK, parentUnid.toUpperCase());
	}
	
	
	/**
	 * Attaches the provided file to an item named $FILE in the note.
	 * 
	 * @param attachmentName the name of the file when attached to the note (e.g. "foo.txt")
	 * @param filePath the path to the file on the filesystem (e.g. "c:\foo.txt")
	 * @throws DominoException if there is a problem attaching the file to the note
	 */
	public void attachFile(String filePath, String attachmentName) throws DominoException {
		_checkRefValidity();
		
		api.NSFNoteAttachFile(getHandle(), DominoAPI.ITEM_NAME_ATTACHMENT, filePath, attachmentName, DominoAPI.COMPRESS_LZ1);
	}
	

	public void detachFile(String fileName) {
		// TODO implement
		// Should look through items of type file for one with the right file name (using NSFItemScan and NSFItemInfo)
		// Then call NSFNoteDetachFile
		throw new NotImplementedException();
	}
	
	/**
	 * Iterates through each item in the note using <code>NSFItemScan</code>.
	 * 
	 * <p>This method does <strong>not</strong> free any structs generated from within
	 * the scanning code.</p>
	 * 
	 * @param proc the handler to be called for each item in the note
	 * @throws DominoException if there is a problem iterating over the note's items
	 */
	public void eachItem(NSFITEMSCANPROC proc) throws DominoException {
		_checkRefValidity();
		
		api.NSFItemScan(getHandle(), proc);
	}
	
	/**
	 * Iterates through each item and its value in the note, providing the {@link NSFNoteItemProc}
	 * with the item name and value.
	 * 
	 * <p>Domino struct values are freed by this method and do not need to be freed by the proc.</p>
	 * 
	 * @param proc the handler to be called for each item name/value pair
	 * @throws DominoException if there is a problem iterating over the note's values
	 */
	public void eachItemValue(final NSFNoteItemProc proc) throws DominoException {
		_checkRefValidity();
		
		eachItem(new NSFITEMSCANPROC() {
			@Override public short callback(short itemFlags, String itemName, long valuePtr, int valueLength) throws Exception {
				Object[] value;
				try {
					value = DominoNativeUtils.readItemValueArray(api, valuePtr, valueLength, getHandle());
				} catch(NotImplementedException e) {
					// Ignore
					value = new Object[0];
				} catch(IllegalArgumentException e) {
					value = new Object[0];
				} catch(UnsupportedOperationException e) {
					value = new Object[0];
				}
				proc.callback(itemName, value);
				DominoNativeUtils.free(value);
				return 0;
			}
		});
	}
	
	public void convertCDToMIME() throws DominoException {
		_checkRefValidity();
		
		api.MIMEConvertCDParts(this.getHandle(), false, hasMime(), 0);
	}
	
	public boolean hasMime() {
		_checkRefValidity();
		
		return api.NSFNoteHasMIME(this.getHandle());
	}
	
	/**
	 * @return whether or not the note contains any object items, such as file attachments
	 */
	public boolean hasEmbedded() {
		_checkRefValidity();
		
		return api.NSFNoteHasObjects(getHandle(), null);
	}
	
	/**
	 * Returns an {@link NSFNoteIDCollection} of the responses to this note. Warning: though those collections
	 * are usually tied to the session, collections created this way will become invalid when the creating
	 * <code>NSFNote</code> is freed.
	 * 
	 * <p>If the collection should survive past the lifetime of the <code>NSFNote</code>, then it should be
	 * merged into an independently-created collection.</p>
	 * 
	 * @return a collection of the response notes
	 * @throws DominoException if there is a problem retrieving this collection
	 */
	public NSFNoteIDCollection getResponses() throws DominoException {
		_checkRefValidity();
		
		long pHandleRef = C.malloc(C.sizeOfDHANDLE);
		try {
			api.NSFNoteGetInfo(this.getHandle(), DominoAPI._NOTE_RESPONSES, pHandleRef);
			long handle = C.getDHandle(pHandleRef, 0);
			if(handle == DominoAPI.NULLHANDLE) {
				return null;
			} else {
				return addChild(new NSFNoteIDCollection(getParent().getParent(), handle, false));
			}
		} finally {
			C.free(pHandleRef);
		}
	}
	
	/**
	 * Returns the count of responses to the document. This is mildly more efficient than getting a count
	 * from {@link #getResponses()}.
	 */
	public int getResponseCount() throws DominoException {
		_checkRefValidity();
		
		long pintRef = C.malloc(C.sizeOfDWORD);
		try {
			api.NSFNoteGetInfo(this.getHandle(), DominoAPI._NOTE_RESPONSE_COUNT, pintRef);
			int count = C.getDWORD(pintRef, 0) & 0xFFFFFFFF;
			return count;
		} finally {
			C.free(pintRef);
		}
	}
	
	/* ******************************************************************************
	 * Creation/modification times
	 ********************************************************************************/
	
	/**
	 * Returns the last modification time of the note globally (as opposed to just in this file).
	 * 
	 * @return a {@link NSFDateTime} of the note's modification time
	 * @throws DominoException if there is a lower-level-API problem retrieving the modification time
	 */
	public NSFDateTime getSequenceModified() throws DominoException {
		OID oid = getOID();
		try {
			return new NSFDateTime(oid.getSequenceTime());
		} finally {
			oid.free();
		}
	}
	public void setSequenceModified(long timeMillis) throws DominoException {
		_checkRefValidity();
		
		TIMEDATE modified = new TIMEDATE();
		try {
			modified.fromJavaMillis(timeMillis);
			// Modification time is set in the OID (which matches to the sequence number), not in _NOTE_MODIFIED, which "in this file"
			OID oid = new OID();
			try {
				api.NSFNoteGetInfo(this.getHandle(), DominoAPI._NOTE_OID, oid.getDataPtr());
				oid.setSequenceTime(modified);
				api.NSFNoteSetInfo(this.getHandle(), DominoAPI._NOTE_OID, oid.getDataPtr());
			} finally {
				oid.free();
			}
		} finally {
			modified.free();
		}
	}
	public void setModified(long timeMillis) throws DominoException {
		_checkRefValidity();
		
		TIMEDATE modified = new TIMEDATE();
		try {
			modified.fromJavaMillis(timeMillis);
			api.NSFNoteSetInfo(this.getHandle(), DominoAPI._NOTE_MODIFIED, modified.getDataPtr());
		} finally {
			modified.free();
		}
	}
	public void setAddedToFile(long timeMillis) throws DominoException {
		_checkRefValidity();
		
		TIMEDATE modified = new TIMEDATE();
		try {
			modified.fromJavaMillis(timeMillis);
			api.NSFNoteSetInfo(this.getHandle(), DominoAPI._NOTE_ADDED_TO_FILE, modified.getDataPtr());
		} finally {
			modified.free();
		}
	}
	public NSFDateTime getAddedToFile() throws DominoException {
		_checkRefValidity();
		
		TIMEDATE addedToFile = new TIMEDATE();
		try {
			api.NSFNoteGetInfo(this.getHandle(), DominoAPI._NOTE_ADDED_TO_FILE, addedToFile.getDataPtr());
			return new NSFDateTime(addedToFile);
		} finally {
			addedToFile.free();
		}
	}
	
	public NSFDateTime getModifiedInFile() throws DominoException {
		_checkRefValidity();
		
		TIMEDATE addedToFile = new TIMEDATE();
		try {
			api.NSFNoteGetInfo(this.getHandle(), DominoAPI._NOTE_MODIFIED, addedToFile.getDataPtr());
			return new NSFDateTime(addedToFile);
		} finally {
			addedToFile.free();
		}
	}
	
	/**
	 * Returns the creation date of the note. This may be either the value of "$Created" (if present) or the
	 * creation date as stored in the OID.
	 * 
	 * @return the creation date of the note
	 * @throws DominoException if there is a problem determining the creation date
	 */
	public Date getCreated() throws DominoException {
		if(hasItem(DominoAPI.FIELD_CREATED)) {
			Object[] created = get(DominoAPI.FIELD_CREATED);
			if(created != null && created.length > 0 && created[0] instanceof NSFDateTime) {
				return ((NSFDateTime)created[0]).toDate();
			}
		}
		OID oid = getOID();
		try {
			return oid.getNote().toJavaDate();
		} finally {
			oid.free();
		}
	}
	
	/**
	 * Delete the note from its database. This object will be invalid if the document is purged.
	 * 
	 * @param force whether to force deletion
	 * @param purge whether to delete without leaving a replication stub
	 * @throws DominoException if there is a problem deleting the note
	 */
	public void delete(boolean force, boolean purge) throws DominoException {
		_checkRefValidity();
		
		getParent().deleteNoteByID(getNoteID(), force, purge);
		if(purge) {
			setHandle(0);
		}
	}
	
	/* ******************************************************************************
	 * Item value operations
	 ********************************************************************************/
	
	public void set(String itemName, Object value) throws DominoException {
		set(itemName, value, (short)-1);
	}
	// TODO support date ranges
	// TODO set flags for single values
	@SuppressWarnings("unchecked")
	public void set(String itemName, Object value, short flags) throws DominoException {
		_checkRefValidity();
		
		if(value == null) {
			removeItem(itemName);
		} else if(DominoNativeUtils.isDateTimeType(value)) {
			TIMEDATE timeDate = new TIMEDATE();
			try {
				DominoNativeUtils.fillTimeDate(timeDate, value);
				api.NSFItemSetTime(this.getHandle(), itemName, timeDate);
			} finally {
				timeDate.free();
			}
		} else if(value instanceof TIMEDATE) {
			api.NSFItemSetTime(this.getHandle(), itemName, (TIMEDATE)value);
		} else if(value instanceof CharSequence) {
			
			// Note: this is a workaround until proper flag support is done.
			// Since the most likely flag to be set is whether or not it's summary, though,
			// it warrants temporary explicit support
			if(flags == -1 || (flags & DominoAPI.ITEM_SUMMARY) > 0) {
				// Then it's summary - use the default
				api.NSFItemSetText(this.getHandle(), itemName, value.toString());
			} else {
				api.NSFItemSetTextSummary(this.getHandle(), itemName, value.toString(), false);
			}
		} else if(value instanceof Number) {
			api.NSFItemSetNumber(this.getHandle(), itemName, ((Number)value).doubleValue());
		} else if(value instanceof NSFUserData) {
			NSFUserData userData = (NSFUserData)value;
			long lmbcsPointer = C.toLMBCSString(userData.getFormatName());
			try {
				int nameLen = C.strlen(lmbcsPointer, 0);
				if(nameLen > 255) {
					throw new IllegalArgumentException("NSFUserData format name too long when converted to LMBCS; maximum 255, received " + nameLen);
				}
				byte[] data = userData.getData();
				// Now allocate space to hold the name length, the name, and the data
				int dataLen = C.sizeOfBYTE + nameLen + data.length;
				long dataPtr = C.malloc(C.sizeOfBYTE + nameLen + data.length);
				try {
					C.setBYTE(dataPtr, 0, (byte)nameLen);
					C.memcpy(dataPtr, C.sizeOfBYTE, lmbcsPointer, 0, nameLen);
					long dataStorePtr = C.ptrAdd(C.ptrAdd(dataPtr, C.sizeOfBYTE), nameLen);
					C.writeByteArray(dataStorePtr, 0, data, 0, data.length);
					api.NSFItemAppend(getHandle(), flags == -1 ? 0 : flags, itemName, DominoAPI.TYPE_USERDATA, dataPtr, dataLen);
				} finally {
					C.free(dataPtr);
				}
			} finally {
				C.free(lmbcsPointer);
			}
		} else if(value instanceof Collection || value.getClass().isArray()) {
			Collection<?> listVal;
			if(value instanceof Collection) {
				listVal = (Collection<?>)value;
			} else {
				listVal = Arrays.asList(DominoNativeUtils.toObjectArray(value));
			}
			
			if(listVal.isEmpty()) {
				removeItem(itemName);
				return;
			}
			
			if(!verifyList(listVal)) {
				throw new IllegalArgumentException("List must be a uniform collection of Strings, Dates or Calendars, or Numbers."); //$NON-NLS-1$
			}
			
			removeItem(itemName);
			
			// If we're here, then that means the list has at least one entry and all entries are of compatible classes
			Class<?> baseType = listVal.iterator().next().getClass();
			if(CharSequence.class.isAssignableFrom(baseType)) {
				// Then we want a LIST with strings
				LIST list = LIST.fromStrings((Collection<CharSequence>)listVal);
				try {
					api.NSFItemAppend(this.getHandle(), flags == -1 ? (short)DominoAPI.ITEM_SUMMARY : flags, itemName, DominoAPI.TYPE_TEXT_LIST, list.getDataPtr(), list.getTotalSize());
				} finally {
					list.free();
				}
			} else if(DominoNativeUtils.isDateTimeType(baseType) || DominoNativeUtils.isDateRangeType(baseType)) {
				// Then we want a RANGE of TIMEDATEs
				RANGE range = RANGE.fromDateList(listVal);
				try {
					api.NSFItemAppend(this.getHandle(), flags == -1 ? (short)DominoAPI.ITEM_SUMMARY : flags, itemName, DominoAPI.TYPE_TIME_RANGE, range.getDataPtr(), range.getTotalSize());
				} finally {
					range.free();
				}
			} else if(Number.class.isAssignableFrom(baseType)) {
				// Then we want a RANGE of NUMBERs
				RANGE range = RANGE.fromNumberList((Collection<? extends Number>)listVal);
				try {
					api.NSFItemAppend(this.getHandle(), flags == -1 ? (short)DominoAPI.ITEM_SUMMARY : flags, itemName, DominoAPI.TYPE_NUMBER_RANGE, range.getDataPtr(), range.getTotalSize());
				} finally {
					range.free();
				}
			} else if(UNIVERSALNOTEID.class.isAssignableFrom(baseType)) {
				// Then we want a LIST of UNIVERSALNOTEID
				LIST list = LIST.fromUniversalNoteIDs((Collection<UNIVERSALNOTEID>)listVal);
				try {
					api.NSFItemAppend(this.getHandle(), flags == -1 ? (short)DominoAPI.ITEM_SUMMARY : flags, itemName, DominoAPI.TYPE_NOTEREF_LIST, list.getDataPtr(), list.getTotalSize());
				} finally {
					list.free();
				}
			} else {
				throw new IllegalArgumentException(StringUtil.format("Unsupported value class {0}", baseType.getName())); //$NON-NLS-1$
			}
			
		} else {
			// Try running through the multi-value variant
			set(itemName, new Object[] { value }, flags);
		}
	}
	public void setNoteRef(String itemName, String... unids) throws DominoException {
		UNIVERSALNOTEID[] unidStructs = new UNIVERSALNOTEID[unids.length];
		for(int i = 0; i < unids.length; i++) {
			unidStructs[i] = new UNIVERSALNOTEID();
			unidStructs[i].setUNID(unids[i]);
		}
		set(itemName, unidStructs);
		for(int i = 0; i < unids.length; i++) {
			unidStructs[i].free();
		}
	}
	
	private boolean verifyList(Collection<?> value) {
		Class<?> baseType = null;
		for(Object val : value) {
			if(val == null) {
				return false;
			}
			if(!isLegalListValue(val)) {
				throw new IllegalArgumentException("Unable to set a value of type " + val.getClass().getName());
			}
			
			Class<?> valClass = val.getClass();
			if(baseType == null) {
				baseType = valClass;
			} else {
				// Check to see if it's compatible with the first type
				if(CharSequence.class.isAssignableFrom(valClass) && CharSequence.class.isAssignableFrom(baseType)) {
					return true;
				} else if(
					(DominoNativeUtils.isDateTimeType(valClass) || DominoNativeUtils.isDateRangeType(valClass)) &&
					(DominoNativeUtils.isDateTimeType(baseType) || DominoNativeUtils.isDateRangeType(baseType))
					) {
					return true;
				} else if(Number.class.isAssignableFrom(valClass) && Number.class.isAssignableFrom(baseType)) {
					return true;
				} else if(baseType.isAssignableFrom(valClass)) {
					return true;
				} else {
					return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean isLegalListValue(Object value) {
		if(value instanceof Number) {
			return true;
		} else if(DominoNativeUtils.isDateTimeType(value)) {
			return true;
		} else if(DominoNativeUtils.isDateRangeType(value)) {
			return true;
		} else if(value instanceof CharSequence) {
			return true;
		} else if(value instanceof UNIVERSALNOTEID) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the value(s) for the given item. If there are multiple items
	 * with the same name, this concatenates their values into a single array.
	 * 
	 * @see DominoNativeUtils#readItemValueArray(long, int, long)
	 */
	public Object[] get(String itemName) throws DominoException {
		List<NSFItem> items = getAllItems(itemName);
		try {
			return DominoNativeUtils.concatNSFItemValues(items);
		} finally {
			for(NSFItem item : items) {
				item.free();
			}
		}
	}
	
	/**
	 * This method attempts to coerce the item's value into the provided class. If there are multiple items
	 * with the same name, this concatenates their values into a single array before coercion.
	 * 
	 * <p>In the case of supported single-value classes and multi-value items, the item's first value
	 * is returned.</p>
	 * 
	 * @param itemName the item name whose value to retrieve
	 * @param clazz the class to attempt to coerce the item to
	 * @return the item's value as an instance of the provided class, or <code>null</code> if the item doesn't exist or can't be coerced
	 * @throws DominoException if there is a problem retrieving the value
	 */
	// TODO add type coercion from ODA
	public <T> T get(String itemName, Class<T> clazz) throws DominoException {
		List<NSFItem> items = getAllItems(itemName);
		try {
			return DominoNativeUtils.concatNSFItemValues(items, clazz);
		} finally {
			for(NSFItem item : items) {
				item.free();
			}
		}
	}
	
	/**
	 * Returns the specified item's value as a String.
	 * 
	 * <p>This is distinct from calling {@link #get(String, Class)} with <code>String</code> for the class,
	 * as this uses the underlying <code>NSFItemConvertToText</code> function call, which has its own
	 * characteristics.</p>
	 * 
	 * <p>This implementation uses a buffer size of 60K (the maximum allowed by the underlying API).</p>
	 * 
	 * @param itemName the item name whose value to retrieve
	 * @param separator the separator to use to concatenate multiple item values
	 * @return the item's value as a String
	 * @throws DominoException if there is a problem retrieving the value or convering it to a String
	 */
	public String getAsString(String itemName, char separator) throws DominoException {
		_checkRefValidity();
		
		return api.NSFItemConvertToText(getHandle(), itemName, (short)(60 * 1024), separator);
	}
	
	/**
	 * Creates a configurable exporter for the specified item. The item's value must be
	 * composite data or MIME. See {@link HTMLExporter} for usage.
	 * 
	 * @param itemName An item containing composite data or MIME
	 * @return An {@link HTMLExporter} for the specified item
	 */
	public HTMLExporter createHTMLExporter(String itemName) {
		_checkRefValidity();
		
		return new HTMLExporter(this, itemName);
	}
	
	/* ******************************************************************************
	 * Item operations
	 ********************************************************************************/
	
	public boolean hasItem(String itemName) throws DominoException {
		_checkRefValidity();
		
		return api.NSFItemIsPresent(getHandle(), itemName);
	}
	public void removeItem(String itemName) throws DominoException {
		_checkRefValidity();
		
		if(hasItem(itemName)) {
			api.NSFItemDelete(this.getHandle(), itemName);
		}
	}
	
	/**
	 * @param itemName the name of the item to query
	 * @return the {@link ValueType} of the item, or <code>null</code> if the item does not exist
	 * @throws DominoException if there is a problem determining the item type
	 */
	public ValueType getItemType(String itemName) throws DominoException {
		_checkRefValidity();
		
		ShortRef valueDataType = new ShortRef();
		try {
			api.NSFItemInfo(getHandle(), itemName, null, valueDataType, null, null);
		} catch(DominoException de) {
			if(de.getStatus() == DominoAPI.ERR_ITEM_NOT_FOUND) {
				return null;
			} else {
				throw de;
			}
		}
		
		return DominoEnumUtil.valueOf(ValueType.class, valueDataType.get());
	}
	
	public List<NSFItem> getAllItems(String itemName) throws DominoException {
		List<NSFItem> result = new ArrayList<NSFItem>();
		NSFItem item = getFirstItem(itemName);
		int breaker = 0;
		while(item != null) {
			result.add(item);
			
			item = getNextItem(item);
			
			if(breaker++ > 1000) {
				throw new RuntimeException("Probable infinite loop detected");
			}
		}
		return result;
	}
	
	public NSFItem getFirstItem(String itemName) throws DominoException {
		_checkRefValidity();
		
		BLOCKID itemBlockId = new BLOCKID();
		ShortRef valueDataType = new ShortRef();
		BLOCKID valueBlockId = new BLOCKID();
		IntRef valueLenRef = new IntRef();
		try {
			api.NSFItemInfo(getHandle(), itemName, itemBlockId, valueDataType, valueBlockId, valueLenRef);
		} catch(DominoException de) {
			itemBlockId.free();
			valueBlockId.free();
			if(de.getStatus() == DominoAPI.ERR_ITEM_NOT_FOUND) {
				return null;
			} else {
				throw de;
			}
		}
		
		if(itemBlockId.getPool() == 0) {
			itemBlockId.free();
			valueBlockId.free();
			return null;
		}
		if(valueBlockId.getPool() == 0) {
			itemBlockId.free();
			valueBlockId.free();
			return null;
		}
		
		return createItem(valueDataType.get(), itemName, itemBlockId, valueBlockId, valueLenRef.get());
		
	}
	
	/**
	 * @param previous the note's item prior to the one you want to retrieve
	 * @param itemName the name of the item you want to retrieve, or <code>null</code> if you would like the next
	 * 		item regardless of name
	 * @return the next item in the note, or <code>null</code> if there are no more items
	 * @throws DominoException if there is a problem retrieving the item
	 */
	public NSFItem getNextItem(NSFItem previous, String itemName) throws DominoException {
		_checkRefValidity();
		
		BLOCKID itemBlockId = new BLOCKID();
		ShortRef valueDataType = new ShortRef();
		BLOCKID valueBlockId = new BLOCKID();
		IntRef valueLenRef = new IntRef();
		try {
			api.NSFItemInfoNext(getHandle(), previous.getItemBlockId(), itemName, itemBlockId, valueDataType, valueBlockId, valueLenRef);
		} catch(DominoException de) {
			itemBlockId.free();
			valueBlockId.free();
			if(de.getStatus() == DominoAPI.ERR_ITEM_NOT_FOUND) {
				return null;
			} else {
				throw de;
			}
		}
		
		if(itemBlockId.getPool() == 0) {
			itemBlockId.free();
			valueBlockId.free();
			return null;
		}
		if(valueBlockId.getPool() == 0) {
			itemBlockId.free();
			valueBlockId.free();
			return null;
		}
		
		return createItem(valueDataType.get(), itemName, itemBlockId, valueBlockId, valueLenRef.get());
	}
	
	/**
	 * @param previous the note's item prior to the one you want to retrieve
	 * @return the next item in the note, or <code>null</code> if there are no more items
	 * @throws DominoException if there is a problem retrieving the item
	 */
	public NSFItem getNextItem(NSFItem previous) throws DominoException {
		return getNextItem(previous, previous.getName());
	}
	
	public List<NSFItem> getItems() throws DominoException {
		List<NSFItem> result = new ArrayList<NSFItem>();
		
		NSFItem item = getFirstItem(null);
		int breaker = 0;
		while(item != null) {
			result.add(item);
			
			item = getNextItem(item, null);
			
			if(breaker++ > 1000) {
				throw new RuntimeException("Probable infinite loop detected");
			}
		}
		
		return result;
	}
	
	/**
	 * Returns a {@link NSFCompositeData} view of all of the items used to store CD in a note
	 * for the given name.
	 * 
	 * <p>The caller is responsible for freeing the returned object.</p>
	 * 
	 * @param itemName the item name to open
	 * @return a {@link NSFCompositeData} view of the items, or <code>null</code> if the item is not set
	 * @throws DominoException if there is an underlying API problem reading the items
	 */
	public NSFCompositeData getCompositeData(String itemName) throws DominoException {
		if(!hasItem(itemName)) {
			return null;
		}
		
		List<NSFItem> items = getAllItems(itemName);
		if(!(items.get(0) instanceof NSFCompositeDataItem)) {
			throw new IllegalArgumentException("Cannot wrap an item of type " + items.get(0).getType());
		}
		
		// It's safe to assume that all of the items are composite data
		@SuppressWarnings("unchecked")
		List<NSFCompositeDataItem> cdItems = (List<NSFCompositeDataItem>)(List<?>)items;
		return new NSFCompositeData(cdItems);
	}
	
	/**
	 * If the note is a profile note, this returns the form name of the profile
	 * 
	 * @return the form name of the profile note, or <code>null</code> if this is not a profile
	 * @throws DominoException if there is a problem retrieving the name item
	 */
	public String getProfileName() throws DominoException {
		if(!hasItem(DominoAPI.FIELD_NAMED)) {
			return null;
		}
		String name = DominoAPI.FIELD_NAMED;
		return DominoUtils.getProfileForm(name);
	}
	
	/**
	 * If the note is a profile note, this returns the user name of the profile
	 * 
	 * @return the user name of the profile note, or <code>null</code> if this is not a profile
	 * @throws DominoException if there is a problem retrieving the name item
	 */
	public String getProfileUserName() throws DominoException {
		if(!hasItem(DominoAPI.FIELD_NAMED)) {
			return null;
		}
		String name = DominoAPI.FIELD_NAMED;
		return DominoUtils.getProfileUser(name);
	}
	
	/* ******************************************************************************
	 * Misc.
	 ********************************************************************************/
	
	/**
	 * @return a <code>List</code> of the folders in the database that contain this note
	 */
	public List<NSFView> getContainingFolders() throws DominoException {
		return this.getParent().getFoldersContainingNoteID(this.getNoteID());
	}

	@Override
	protected void doFree() {
		if(this.getHandle() > 0) {
			try {
				api.NSFNoteClose(this.getHandle());
			} catch(DominoException e) {
				// This should be very unlikely
				throw new RuntimeException(e);
			}
		}
		super.doFree();
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFHandle#toString()
	 */
	@Override
	public String toString() {
		try {
			return StringUtil.format("[NSFNote: unid={0}, filePath={1}]", getUniversalID(), getParent().getFilePath()); //$NON-NLS-1$
		} catch(DominoException e) {
			return "[NSFNote (details could not be retrieved: " + e.getLocalizedMessage() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/* ******************************************************************************
	 * Internal utility methods
	 ********************************************************************************/
	
	private NSFItem createItem(short valueDataType, String itemName, BLOCKID itemBlockId, BLOCKID valueBlockId, int valueLen) throws DominoException {
		ValueType dataType = DominoEnumUtil.valueOf(ValueType.class, valueDataType);
		switch(dataType) {
		case COMPOSITE:
			return addChild(new NSFCompositeDataItem(this, itemName, itemBlockId, dataType, valueBlockId, valueLen));
		case MIME_PART:
			return addChild(new NSFMimeItem(this, itemName, itemBlockId, dataType, valueBlockId, valueLen));
		case OBJECT:
			// This takes some extra work: identify whether it's a file object or not
			// TODO do this without double-reading the item value and creating the temp item
			NSFItem tempItem = new NSFItem(this, itemName, itemBlockId, dataType, valueBlockId, valueLen);
			BaseStruct descriptor = (BaseStruct)tempItem.getValue()[0];
			NSFItem result = null;
			if(descriptor instanceof FILEOBJECT) {
				result = new NSFFileItem(tempItem);
			} else {
				result = new NSFObjectItem(tempItem);
			}
			descriptor.free();
			tempItem.free();
			
			return addChild(result);
		default:
			return addChild(new NSFItem(this, itemName, itemBlockId, dataType, valueBlockId, valueLen));		
		}
	}
}
