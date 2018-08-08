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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.struct.BaseStruct;
import com.darwino.domino.napi.struct.COLLECTIONPOSITION;
import com.darwino.domino.napi.struct.ITEM_TABLE;
import com.darwino.domino.napi.struct.ITEM_VALUE_TABLE;
import com.darwino.domino.napi.struct.UNIVERSALNOTEID;
import com.darwino.domino.napi.util.DominoNativeUtils;

/**
 * <p>The entry data is not read until one of the entry-data getters is called, so if the native in-memory
 * object is closed before getting any data, all getters will act as if the read mask was empty.</p>
 * 
 * <p>This object is externally immutable.</p>
 * 
 * @author Jesse Gallagher
 *
 */
public class NSFViewEntry extends NSFBase {

	private final NSFViewEntryCollection collection;
	private final NSFView view;
	@SuppressWarnings("unused")
	private final int index;
	private final int readMask;
	private final long finalPointer;
	
	private final int noteId;
	private final String unid;
	private final short noteClass;
	private final int siblingCount;
	private final int childCount;
	private final int descendantCount;
	private final boolean anyUnread;
	private final short indentLevel;
	private final short searchScore;
	private final boolean unread;
	
	// Store collection position info in its constituent parts
	private final short positionLevel;
	private final byte positionMinLevel;
	private final byte positionMaxLevel;
	private final int[] positionTumbler;
	
	// Store item table info in its constituent parts
	private final String[] itemTableNames;
	private final Object[] itemTableValues;
	private final Object conflict;
	private final Object ref;
	
	public NSFViewEntry(NSFViewEntryCollection parent, int index, long infoPtr, int readMask) throws DominoException {
		super(parent.getAPI());
		
		this.collection = parent;
		this.view = parent.getParent();
		this.index = index;
		this.readMask = readMask;
		
		// Read in the NOTEID and advance past it
		long data = infoPtr;
		if((readMask & READ_MASK_NOTEID) != 0) {
			this.noteId = C.getDWORD(data, 0);
			data = C.ptrAdd(data, C.sizeOfDWORD);
		} else {
			this.noteId = -1;
		}
		
		// Read in the UNID and advance past it
		if((readMask & READ_MASK_NOTEUNID) != 0) {
			// Make a local copy of the UNID structure so the parent can be released
			UNIVERSALNOTEID unidStruct = new UNIVERSALNOTEID(data);
			this.unid = unidStruct.getUNID();
			data = C.ptrAdd(data, UNIVERSALNOTEID.sizeOf);
		} else {
			this.unid = null;
		}
		
		// Note class, a WORD
		if((readMask & READ_MASK_NOTECLASS) != 0) {
			this.noteClass = C.getWORD(data, 0);
			data = C.ptrAdd(data, C.sizeOfWORD);
		} else {
			this.noteClass = -1;
		}
		
		// Siblings, a DWORD
		if((readMask & READ_MASK_INDEXSIBLINGS) != 0) {
			this.siblingCount = C.getDWORD(data, 0);
			data = C.ptrAdd(data, C.sizeOfDWORD);
		} else {
			this.siblingCount = -1;
		}
		
		// Children, a DWORD
		if((readMask & READ_MASK_INDEXCHILDREN) != 0) {
			this.childCount = C.getDWORD(data, 0);
			data = C.ptrAdd(data, C.sizeOfDWORD);
		} else {
			this.childCount = -1;
		}
		
		// Descendants, a DWORD
		if((readMask & READ_MASK_INDEXDESCENDANTS) != 0) {
			this.descendantCount = C.getDWORD(data, 0);
			data = C.ptrAdd(data, C.sizeOfDWORD);
		} else {
			this.descendantCount = -1;
		}
		
		// Any Unread, a WORD
		if((readMask & READ_MASK_INDEXANYUNREAD) != 0) {
			this.anyUnread = C.getWORD(data, 0) == 1;
			data = C.ptrAdd(data, C.sizeOfWORD);
		} else {
			this.anyUnread = false;
		}
		
		// Indent level, a WORD
		if((readMask & READ_MASK_INDENTLEVELS) != 0) {
			this.indentLevel = C.getWORD(data, 0);
			data = C.ptrAdd(data, C.sizeOfWORD);
		} else {
			this.indentLevel = -1;
		}
		
		// Search score, if the parent was FTSearch'd
		if((readMask & READ_MASK_SCORE) != 0) {
			this.searchScore = C.getWORD(data, 0);
			data = C.ptrAdd(data, C.sizeOfWORD);
		} else {
			this.searchScore = -1;
		}
		
		// Unread, a WORD
		if((readMask & READ_MASK_INDEXUNREAD) != 0) {
			this.unread = C.getWORD(data, 0) == 1;
			data = C.ptrAdd(data, C.sizeOfWORD);
		} else {
			this.unread = false;
		}
		
		// Collection position, which is truncated (the Tumbler array at the end is only as large as need be)
		if((readMask & READ_MASK_INDEXPOSITION) != 0) {
			COLLECTIONPOSITION truncatedPos = new COLLECTIONPOSITION(data);
			
			this.positionLevel = truncatedPos.getLevel();
			this.positionMinLevel = truncatedPos.getMinLevel();
			this.positionMaxLevel = truncatedPos.getMaxLevel();
			this.positionTumbler = truncatedPos.getTumblerTruncated();
			
			data = C.ptrAdd(data, COLLECTIONPOSITION.size(truncatedPos));
		} else {
			this.positionLevel = -1;
			this.positionMinLevel = -1;
			this.positionMaxLevel = -1;
			this.positionTumbler = null;
		}
		
		Object[] itemTableValues = null;
		String[] itemTableNames = null;
		Object conflict = null;
		Object ref = null;
		if((readMask & READ_MASK_SUMMARYVALUES) != 0) {
			ITEM_VALUE_TABLE table = new ITEM_VALUE_TABLE(data);
			
			Object[] nativeValues = table.readItemValues();
			Object[] values = DominoNativeUtils.wrapNativeStructs(nativeValues, true);
			for(Object obj : values) {
				if(obj instanceof BaseStruct) {
					addChildStruct((BaseStruct)obj);
				}
			}
			

			// Document entries come with two extra columns for conflict and red
			if(isDocument()) {
				conflict = values[values.length-2];
				ref = values[values.length-1];
				
				itemTableValues = new Object[values.length-2];
				System.arraycopy(values, 0, itemTableValues, 0, itemTableValues.length);
			} else {
				itemTableValues = values;
			}
			
			data = C.ptrAdd(data, table.getLength());
		}
		
		if((readMask & READ_MASK_SUMMARY) != 0) {
			ITEM_TABLE table = new ITEM_TABLE(data);
			
			Object[][] itemTable = table.readItemValues();
			itemTable[1] = DominoNativeUtils.wrapNativeStructs(itemTable[1], true);
			for(Object obj : itemTable[1]) {
				if(obj instanceof BaseStruct) {
					addChildStruct((BaseStruct)obj);
				} else if(obj instanceof NSFBase) {
					addChild((NSFBase)obj);
				}
			}

			int itemCount = itemTable[0].length;
			
			// Document entries come with two extra columns for conflict and red
			if(isDocument()) {
				conflict = itemTable[1][itemCount-2];
				ref = itemTable[1][itemCount-1];
				
				itemTableNames = new String[itemCount-2];
				System.arraycopy(itemTable[0], 0, itemTableNames, 0, itemCount-2);
				itemTableValues = new Object[itemCount-2];
				System.arraycopy(itemTable[1], 0, itemTableValues, 0, itemCount-2);
			} else {
				conflict = null;
				ref = null;
				
				itemTableNames = new String[itemCount];
				System.arraycopy(itemTable[0], 0, itemTableNames, 0, itemTableNames.length);
				itemTableValues = itemTable[1];
			}
			
			data = C.ptrAdd(data, table.getLength());
		}
		
		this.itemTableValues = itemTableValues;
		this.itemTableNames = itemTableNames;
		this.conflict = conflict;
		this.ref = ref;
		
		finalPointer = data;
	}

	/**
	 * @return the parent {@link NSFViewEntryCollection} of this entry, or <code>null</code> if the entry did not come from a collection
	 */
	@Override
	public NSFViewEntryCollection getParent() {
		return collection;
	}
	
	public NSFView getParentView() {
		return view;
	}
	
	/**
	 * @return the note ID of the entry, or -1 if the data was not read from the view
	 */
	public int getNoteId() {
		return noteId;
	}
	
	/**
	 * @return the UNID of the entry, or <code>null</code> if the data was not read from the view
	 */
	public String getUniversalID() {
		if(unid == null) {
			return null;
		} else {
			return unid;
		}
	}
	
	/**
	 * @return the note class of the entry, or -1 if the data was not read from the view
	 */
	public short getNoteClass() {
		return noteClass;
	}
	
	/**
	 * @return the number of sibling entries to this entry, or -1 if the data was not read from the view
	 */
	public long getSiblingCount() {
		if((readMask & READ_MASK_INDEXSIBLINGS) != 0) {
			return siblingCount & 0xFFFFFFFF;
		} else {
			return -1;
		}
	}
	
	/**
	 * @return the number of child entries of this entry, or -1 if the data was not read from the view
	 */
	public long getChildCount() {
		if((readMask & READ_MASK_INDEXCHILDREN) != 0) {
			return childCount & 0xFFFFFFFF;
		} else {
			return -1;
		}
	}
	
	/**
	 * @return the number of descendant entries of this entry, or -1 if the data was not read from the view
	 */
	public long getDescendantCount() {
		if((readMask & READ_MASK_INDEXDESCENDANTS) != 0) {
			return descendantCount & 0xFFFFFFFF;
		} else {
			return -1;
		}
	}
	
	/**
	 * @return whether the entry has any unread information. This value is always <code>false</code> if the data was not read from the view
	 */
	public boolean isAnyUnread() {
		return anyUnread;
	}
	
	/**
	 * @return the indent level of this entry, or -1 if the data was not read from the view
	 */
	public int getIndentLevel() {
		if((readMask & READ_MASK_INDENTLEVELS) != 0) {
			return indentLevel & 0xFFFF;
		} else {
			return -1;
		}
	}
	
	/**
	 * @return the FT search score of this entry, or -1 if the data was not read from the view
	 */
	public int getSearchScore() {
		if((readMask & READ_MASK_SCORE) != 0) {
			return searchScore & 0xFFFF;
		} else {
			return -1;
		}
	}
	
	/**
	 * @return whether the entry is unread fot the active user. This value is always <code>false</code> if the data was not read from the view
	 */
	public boolean isUnread() {
		return unread;
	}
	
	/**
	 * @return the position level of this entry, or -1 if the data was not read from the view
	 */
	public int getPositionLevel() {
		if((readMask & READ_MASK_INDEXPOSITION) != 0) {
			return positionLevel & 0xFFFF;
		} else {
			return -1;
		}
	}
	
	/**
	 * @return the position min level of this entry, or -1 if the data was not read from the view
	 */
	public int getPositionMinLevel() {
		if((readMask & READ_MASK_INDEXPOSITION) != 0) {
			return positionMinLevel & 0xFFFF;
		} else {
			return -1;
		}
	}
	
	/**
	 * @return the position max level of this entry, or -1 if the data was not read from the view
	 */
	public int getPositionMaxLevel() {
		if((readMask & READ_MASK_INDEXPOSITION) != 0) {
			return positionMaxLevel & 0xFFFF;
		} else {
			return -1;
		}
	}
	
	/**
	 * @param the 0-based index of the tumbler to return
	 * @return the value of the specified index in the tumbler of this entry, or -1 if the data was not read from the view
	 * @throws IndexOutOfBoundsException if <code>index</code> is higher than level of this entry - 1 
	 */
	public int getPositionTumbler(int index) {
		if((readMask & READ_MASK_INDEXPOSITION) != 0) {
			if(index >= positionTumbler.length || index < 0) {
				throw new IndexOutOfBoundsException(StringUtil.format("Index {0} is outside of the tumbler level {1}", index, getPositionLevel())); //$NON-NLS-1$
			}
			return positionTumbler[index];
		} else {
			return -1;
		}
	}
	
	public boolean isDocument() {
		return noteId > 0 && (noteId & NOTEID_RESERVED) == 0;
	}
	public boolean isCategory() {
		return (noteId & NOTEID_GHOST_ENTRY) != 0 || (noteId & NOTEID_CATEGORY) != 0;
	}
	public boolean isTotal() {
		return (noteId & NOTEID_CATEGORY_TOTAL) != 0;
	}
	
	public Object[] getColumnValues() {
		// TODO This should be a deep copy because the values array may contain arrays
		if(this.itemTableValues != null) {
			Object[] result = new Object[this.itemTableValues.length];
			System.arraycopy(this.itemTableValues, 0, result, 0, result.length);
			return result;
		} else {
			return new Object[0];
		}
	}
	
	public Map<String, Object> getColumnValuesMap() {
		if(this.itemTableNames != null) {
			Map<String, Object> result = new LinkedHashMap<String, Object>();
			for(int i = 0; i < this.itemTableNames.length; i++) {
				// TODO This should be a deep copy because the values array may contain arrays
				result.put(this.itemTableNames[i], this.itemTableValues[i]);
			}
			return result;
		} else {
			return Collections.emptyMap();
		}
	}

	@Override
	protected void doFree() {
		// NOP
	}
	@Override
	protected Recycler createRecycler() {
		// NOP
		return null;
	}
	
	protected long _getFinalPointer() {
		return finalPointer;
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#isRefValid()
	 */
	@Override
	public boolean isRefValid() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#removeFromParent()
	 */
	@Override
	protected void removeFromParent() {
		if(collection != null) {
			collection.removeChild(this);
		} else {
			view.removeChild(this);
		}
	}
	
	@Override
	public String toString() {
		return StringUtil.format("[{0}: unid={1}]", getClass().getName(), getUniversalID()); //$NON-NLS-1$
	}
}
