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
package com.darwino.domino.napi.struct;

import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.util.DominoNativeUtils;

/**
 * (nsfdata.h)
 * 
 * @author Jesse Gallagher
 *
 */
public class ITEM_TABLE extends BaseStruct {
	static {
		int[] sizes = new int[3];
		initNative(sizes);
		sizeOf = sizes[0];
		_Length = sizes[1];
		_Items = sizes[2];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Length;
	public static final int _Items;
	
	public ITEM_TABLE() {
		super(C.malloc(sizeOf), true);
	}
	public ITEM_TABLE(long data) {
		super(data, false);
	}

	public ITEM_TABLE(long data, boolean owned) {
		super(data, owned);
	}
	
	public short getLength() { return _getUSHORT(_Length); }
	public void setLength(short length) { _setUSHORT(_Length, length); }
	
	public short getItems() { return _getUSHORT(_Items); }
	public void setItems(short items) { _setUSHORT(_Items, items); }
	
	/**
	 * Returns the name/value pairs of the item table as a two-entry array of arrays of equal length.
	 * 
	 * <p>This returns the array structure instead of a {@link java.util.Map} because there may be
	 * multiple table entries with the same name and that may be important to retain (e.g. for view entries).</p>
	 * 
	 * @return a two-entry array of equal-length arrays representing the name/value pairs in the table
	 * @throws DominoException if there is a problem reading the values from memory and converting to Java formats
	 */
	public Object[][] readItemValues() throws DominoException {
		_checkRefValidity();
		
		Object[][] result = new Object[2][];
		long summaryPtr = C.ptrAdd(data, sizeOf);
		
		// First is an array of ITEMs
		int itemCount = getItems();
		ITEM[] items = new ITEM[itemCount];
		for(int i = 0; i < itemCount; i++) {
			items[i] = new ITEM(summaryPtr);
			summaryPtr = C.ptrAdd(summaryPtr, ITEM.sizeOf);
		}
		
		result[0] = new Object[itemCount];
		result[1] = new Object[itemCount];
		
		// Now is a packed array of item name/value pairs
		for(int i = 0; i < itemCount; i++) {
			// Read in the name
			short nameLength = items[i].getNameLength();
			String name = C.getLMBCSString(summaryPtr, 0, nameLength);
			summaryPtr = C.ptrAdd(summaryPtr, nameLength);
			
			// Read in the value, with its data type
			short valueLength = items[i].getValueLength();
			Object value = DominoNativeUtils.readItemValue(DominoAPI.get(), summaryPtr, valueLength, 0);
			summaryPtr = C.ptrAdd(summaryPtr, valueLength);
			
			result[0][i] = name;
			result[1][i] = value;
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		if(isRefValid()) {
			return StringUtil.format("[{0}: Length={1}, Items={2}]", //$NON-NLS-1$
					getClass().getSimpleName(),
					getLength(),
					getItems()
			);
		} else {
			return super.toString();
		}
	}
}
