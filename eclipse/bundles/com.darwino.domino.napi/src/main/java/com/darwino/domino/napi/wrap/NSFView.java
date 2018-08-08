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

import static com.darwino.domino.napi.DominoAPI.*;

import java.util.Collection;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.c.IntRef;
import com.darwino.domino.napi.enums.ValueType;
import com.darwino.domino.napi.struct.COLLECTIONDATA;
import com.darwino.domino.napi.struct.COLLECTIONPOSITION;
import com.darwino.domino.napi.struct.ITEM_TABLE;
import com.darwino.domino.napi.util.DominoNativeUtils;
import com.darwino.domino.napi.wrap.design.NSFDesignNoteBase;
import com.darwino.domino.napi.wrap.design.NSFViewFormat;

/**
 * This object represents a view or folder within an NSF.
 * 
 * @author Jesse Gallagher
 */
// TODO decide whether these semantics are wise (being passed a note ID, the FetchCollectionData bit, the way ViewEntryCollections work)
public class NSFView extends NSFDesignNoteBase {
	/* this is stored as a byte in order to account for the "not yet determined" state */
	private byte folder = -1;
	
	
	public NSFView(NSFDatabase parent, int noteId) throws DominoException {
		super(parent, noteId);
	}
	
	/**
	 * @return the API-level handle for the open collection
	 */
	public short getCollectionHandle() throws DominoException {
		return this.getCollection();
	}
	
	public boolean isFolder() throws DominoException {
		if(this.folder == -1) {
			NSFNote note = getNote();
			String flags = note.get(DominoAPI.DESIGN_FLAGS, String.class);
			if(flags != null && flags.indexOf(DominoAPI.DESIGN_FLAG_FOLDER_VIEW) > -1) {
				this.folder = 1;
			} else {
				this.folder = 0;
			}
		}
		return this.folder == 1;
	}
	
	public int getDocCount() throws DominoException {
		return getCollectionData().getDocCount();
	}
	public int getEntrySize() throws DominoException {
		return getCollectionData().getDocTotalSize();
	}
	public int getEntryCount() throws DominoException {
		// TODO modify for FT search when implemented
		IntRef entriesSkipped = new IntRef();
		IntRef entriesReturned = new IntRef();
		COLLECTIONPOSITION position = new COLLECTIONPOSITION();
		try {
			position.setLevel((short)0);
			position.setTumbler(0, 1);
			
			api.NIFReadEntries(
					getCollectionHandle(),
					position,
					(short)(NAVIGATE_NEXT | NAVIGATE_CONTINUE),
					MAXDWORD,
					NAVIGATE_CURRENT,
					1,
					0,
					null,
					entriesSkipped,
					entriesReturned,
					null
					);
			
			return entriesSkipped.get() + entriesReturned.get();
		} finally {
			position.free();
		}
	}
	
	public NSFViewEntryCollection getAllEntries() throws DominoException {
		_checkRefValidity();
		
		COLLECTIONPOSITION position = new COLLECTIONPOSITION();
		position.setLevel((short)0);
		position.setTumbler(0, 0);
		return addChild(new NSFViewEntryCollection(this, position, -1, -1, -1));
	}
	
	/**
	 * Returns a {@link NSFViewEntryCollection} for the specified entry <code>count</code>, starting at
	 * the offset <code>start</code>.
	 * 
	 * <p>This method deals only with the first tumbler level.</p>
	 * 
	 * @param start the 0-based index of the first entry to retrieve
	 * @param count the number of entries to retrieve
	 * @return a <code>NIFViewEntryCollection</code> of the specified entries
	 * @throws DominoException if there is a problem retrieving the entries
	 */
	public NSFViewEntryCollection getEntries(int start, int count) throws DominoException {
		_checkRefValidity();
		
		COLLECTIONPOSITION position = new COLLECTIONPOSITION();
		position.setLevel((short)0);
		position.setTumbler(0, start);
		return addChild(new NSFViewEntryCollection(this, position, count, -1, -1));
	}
	
	public NSFViewEntryCollection getAllEntriesByKey(Object key, boolean exactMatch) throws DominoException {
		_checkRefValidity();
		
		if(key == null) {
			throw new IllegalArgumentException("key cannot be null"); //$NON-NLS-1$
		}
		
		COLLECTIONPOSITION position = new COLLECTIONPOSITION();
		position.setLevel((short)0);
		position.setTumbler(0, 0);
		IntRef retNumMatches = new IntRef();
		
		ITEM_TABLE table = DominoNativeUtils.toItemTable(toObjectArray(key));
		try {
			api.NIFFindByKey(
					this.getCollectionHandle(),
					table,
					(short)(FIND_FIRST_EQUAL |
						FIND_CASE_INSENSITIVE |
						FIND_ACCENT_INSENSITIVE |
						FIND_RETURN_DWORD |
						(exactMatch ? 0 : FIND_PARTIAL)),
					position,
					retNumMatches
					);
			int numMatches = retNumMatches.get();
			
			return addChild(new NSFViewEntryCollection(this, position, numMatches, numMatches, -1));
		} catch(DominoException e) {
			if(e.getStatus() == DominoAPI.ERR_NOT_FOUND) {
				// Then it's better to return null
				return null;
			} else {
				throw e;
			}
		} finally {
			table.free();
		}
	}
	
	/**
	 * Returns the first note in the view by the provided key with the current collation, or
	 * <code>null</code> if the key does not match any entries.
	 * 
	 * @param key an object, {@link Collection}, or array to use as a key.
	 * @param exactMatch whether the lookup should match the key exactly or allow for partial matches
	 * @return The first note in the view by the provided key, or <code>null</code> if the key
	 * 	does not match any entries.
	 * @throws DominoException
	 */
	public NSFNote getFirstNoteByKey(Object key, boolean exactMatch) throws DominoException {
		_checkRefValidity();
		
		COLLECTIONPOSITION position = new COLLECTIONPOSITION();
		try {
			ITEM_TABLE table = null;
			position.setLevel((short)0);
			position.setTumbler(0, 0);
			IntRef retNumMatches = new IntRef();
			
			try {
				table = DominoNativeUtils.toItemTable(toObjectArray(key));
				
				api.NIFFindByKey(
						this.getCollectionHandle(),
						table,
						(short)(FIND_FIRST_EQUAL |
							FIND_CASE_INSENSITIVE |
							FIND_ACCENT_INSENSITIVE |
							FIND_RETURN_DWORD |
							(exactMatch ? 0 : FIND_PARTIAL)),
						position,
						retNumMatches
						);
				int numMatches = retNumMatches.get();
				
				table.free();
				table = null;
				
				if(numMatches < 1) {
					return null;
				} else {
					long hBuffer = api.NIFReadEntries(
						getCollectionHandle(),
						position,
						NAVIGATE_CURRENT,
						0,
						NAVIGATE_NEXT,
						1,
						READ_MASK_NOTEID,
						null,
						null,
						null,
						null
						);
					
					if(hBuffer == NULLHANDLE) {
						// Then there are no entries
						throw new DominoException(null, "NIFReadEntries returned a null handle"); //$NON-NLS-1$
					}
					
					long buffer = api.OSLockObject(hBuffer);
					
					int noteId = C.getNOTEID(buffer, 0);
					
					api.OSUnlockObject(hBuffer);
					api.OSMemFree(hBuffer);
					
					return getParent().getNoteByID(noteId);
				}
			} catch(DominoException e) {
				if(e.getStatus() == DominoAPI.ERR_NOT_FOUND) {
					// In this case, it's better to return null
					return null;
				} else {
					throw e;
				}
			} finally {
				if(table != null) { table.free(); }
			}
		} finally {
			position.free();
		}
	}
	
	/**
	 * If the current object is a folder, returns an {@link NSFFolder} view of it with
	 * specialized operations. Otherwise, returns <code>null</code>
	 * 
	 * @return an {@link NSFFolder} view of this folder, or <code>null</code> if it is
	 * 			not a folder
	 * @throws DominoException if there is a lower-level-API problem retrieving the folder
	 */
	public NSFFolder asFolder() throws DominoException {
		if(this.isFolder()) {
			return new NSFFolder(this);
		} else {
			return null;
		}
	}
	
	private static Object[] toObjectArray(Object obj) {
		Object[] keyArray;
		if(obj.getClass().isArray()) {
			keyArray = (Object[])obj;
		} else if(obj instanceof Collection) {
			keyArray = ((Collection<?>)obj).toArray();
		} else {
			keyArray = new Object[] { obj };
		}
		return keyArray;
	}
	
	static class ViewRecycler extends Recycler {
		private final DominoAPI api;
		Short hCollection = null;
		long collectionDataHandle;
		COLLECTIONDATA collectionData;
		
		public ViewRecycler(DominoAPI api) {
			this.api = api;
		}
		
		boolean hasCollection() {
			return hCollection != null && hCollection != 0;
		}
		
		@Override
		void doFree() {
			try {
				if(hasCollection()) {
					api.NIFCloseCollection(hCollection);
					hCollection = null;
				}
				if(collectionData != null) {
					collectionData.free();
					collectionData = null;
					api.OSUnlockObject(collectionDataHandle);
					api.OSMemFree(collectionDataHandle);
				}
			} catch(DominoException e) {
				
			}
		}
	}
	@Override
	protected Recycler createRecycler() {
		return new ViewRecycler(api);
	}

	
	public COLLECTIONDATA getCollectionData() throws DominoException {
		_checkRefValidity();
		
		if(((ViewRecycler)recycler).collectionData == null && ((ViewRecycler)recycler).hasCollection()) {
			short hCollection = this.getCollection();
			((ViewRecycler)recycler).collectionDataHandle = api.NIFGetCollectionData(hCollection);
			if(((ViewRecycler)recycler).collectionDataHandle == DominoAPI.NULLHANDLE) {
				throw new DominoException(null, "Received NULLHANDLE from NIFGetCollectionData");
			}
			long dataPtr = api.OSLockObject(((ViewRecycler)recycler).collectionDataHandle);
			((ViewRecycler)recycler).collectionData = new COLLECTIONDATA(dataPtr);
		}
		return ((ViewRecycler)recycler).collectionData;
	}
	
	/**
	 * Attempts to update the view to match changes to the database (<code>NIFUpdateCollection</code>).
	 * Calling this will possibly render open positions or collections invalid.
	 */
	public void refresh() throws DominoException {
		_checkRefValidity();
		
		if(((ViewRecycler)recycler).hasCollection()) {
			api.NIFUpdateCollection(((ViewRecycler)recycler).hCollection);
		}
	}
	
	/**
	 * Retrieves the compiled selection formula of the view.
	 * 
	 * @return the compiled selection formula of the view
	 * @throws DominoException if there is an API problem reading the selection formula
	 * @throws IllegalStateException if the view note's {@link DominoAPI#VIEW_FORMULA_ITEM} item is invalid
	 */
	public NSFFormula getSelectionFormula() throws DominoException {
		_checkRefValidity();
		
		NSFNote note = getNote();
		NSFItem formulaItem = note.getFirstItem(DominoAPI.VIEW_FORMULA_ITEM);
		if(formulaItem == null) {
			throw new IllegalStateException(DominoAPI.VIEW_FORMULA_ITEM + " item does not exist");
		} else if(formulaItem.getType() != ValueType.FORMULA) {
			throw new IllegalStateException(DominoAPI.VIEW_FORMULA_ITEM + " item is of unexpected type " + formulaItem.getType());
		}
		return formulaItem.getValue(NSFFormula.class);
	}
	
	/**
	 * Retrieves the visual and data format of the view, as a {@link NSFViewFormat} object.
	 * 
	 * @return the format of the view, or <code>null</code> if the {@link DominoAPI#VIEW_VIEW_FORMAT_ITEM} item does not exist
	 * @throws DominoException if there is an API problem reading the view format
	 * @throws IllegalStateException if the view note's {@link DominoAPI#VIEW_VIEW_FORMAT_ITEM} item is invalid
	 */
	public NSFViewFormat getFormat() throws DominoException {
		_checkRefValidity();
		
		NSFNote note = getNote();
		NSFItem formatItem = note.getFirstItem(DominoAPI.VIEW_VIEW_FORMAT_ITEM);
		if(formatItem == null) {
			return null;
		} else if(formatItem.getType() != ValueType.VIEW_FORMAT) {
			throw new IllegalStateException(DominoAPI.VIEW_VIEW_FORMAT_ITEM + " item if of unexpected type " + formatItem.getType());
		}
		return formatItem.getValue(NSFViewFormat.class);
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#isRefValid()
	 */
	@Override
	public boolean isRefValid() {
		Short hCollection = ((ViewRecycler)recycler).hCollection;
		return super.isRefValid() && (hCollection == null || hCollection != 0);
	}
	
	// ******************************************************************************
	// * Internal utility methods
	// ******************************************************************************
	
	private short getCollection() throws DominoException {
		if(((ViewRecycler)recycler).hCollection == null) {
			_checkRefValidity();
			
			long hNamesList = getParent().getParent().getNamesListHandle();
			if(hNamesList == 0) {
				((ViewRecycler)recycler).hCollection = api.NIFOpenCollection(
						getParent().getHandle(),
						getParent().getHandle(),
						getNoteID(),
						DominoAPI.OPEN_REOPEN_COLLECTION,
						NULLHANDLE,
						NULLHANDLE,
						null,
						NULLHANDLE,
						NULLHANDLE
				);
			} else {
				((ViewRecycler)recycler).hCollection = api.NIFOpenCollectionWithUserNameList(
						getParent().getHandle(),
						getParent().getHandle(),
						getNoteID(),
						DominoAPI.OPEN_REOPEN_COLLECTION,
						NULLHANDLE,
						NULLHANDLE,
						null,
						NULLHANDLE,
						NULLHANDLE,
						hNamesList
				);
			}
			if(((ViewRecycler)recycler).hCollection == 0) {
				throw new DominoException(null, "NIFOpenCollection returned NULLHANDLE"); //$NON-NLS-1$
			}
		}
		return ((ViewRecycler)recycler).hCollection;
	}
}
