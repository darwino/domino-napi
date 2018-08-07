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

import java.util.Collection;
import com.darwino.domino.napi.c.C;

/**
 * 
 * @author Jesse Gallagher
 *
 */
// TODO Use generics for LIST<String>, etc?
public class LIST extends BaseStruct {

	static {
		int[] sizes = new int[2];
		initNative(sizes);
		sizeOf = sizes[0];
		_ListEntries = sizes[1];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _ListEntries;
	
	public static enum Type {
		NOTEREF, TEXT_LIST
	}
	
	public static LIST fromStrings(Collection<? extends CharSequence> stringList) {
		if(stringList == null) {
			LIST result = new LIST();
			result.setType(Type.TEXT_LIST);
			return result;
		}
		// First, convert all of the strings to LMBCS, so we can know the full size
		int count = stringList.size();
		long[] lmbcsStrings = new long[count];
		int[] lengths = new int[count];
		int totalLength = 0;
		int i = 0;
		for(CharSequence seq : stringList) {
			String val = seq == null ? "" : seq.toString(); //$NON-NLS-1$
			lmbcsStrings[i] = C.toLMBCSString(val);
			lengths[i] = C.strlen(lmbcsStrings[i], 0);
			totalLength += lengths[i];
			i++;
		}
		
		int listSize = LIST.sizeOf + (C.sizeOfUSHORT * count) + totalLength;
		long dataPtr = C.malloc(listSize);
		LIST result = new LIST(dataPtr, true);
		result.setType(Type.TEXT_LIST);
		result.setListEntries((short)count);
		dataPtr = C.ptrAdd(dataPtr, sizeOf);
		for(i = 0; i < count; i++) {
			C.setUSHORT(dataPtr, 0, (short)lengths[i]);
			dataPtr = C.ptrAdd(dataPtr, C.sizeOfUSHORT);
		}
		for(i = 0; i < count; i++) {
			C.memcpy(dataPtr, 0, lmbcsStrings[i], 0, lengths[i]);
			C.free(lmbcsStrings[i]);
			dataPtr = C.ptrAdd(dataPtr, lengths[i]);
		}
		return result;
	}
	
	public static LIST fromUniversalNoteIDs(Collection<? extends UNIVERSALNOTEID> unidList) {
		if(unidList == null) {
			return new LIST(Type.NOTEREF);
		}
		
		int totalLength = sizeOf + unidList.size() * UNIVERSALNOTEID.sizeOf;
		long dataPtr = C.malloc(totalLength);
		LIST result = new LIST(dataPtr, true, Type.NOTEREF);
		result.setListEntries((short)unidList.size());
		dataPtr = C.ptrAdd(dataPtr, sizeOf);
		for(UNIVERSALNOTEID unidStruct : unidList) {
			C.memcpy(dataPtr, 0, unidStruct.getDataPtr(), 0, UNIVERSALNOTEID.sizeOf);
			dataPtr = C.ptrAdd(dataPtr, UNIVERSALNOTEID.sizeOf);
		}
		return result;
	}
	

	private Type type;
	
	public LIST() {
		super(C.malloc(sizeOf), true);
	}
	public LIST(Type type) {
		this();
		setType(type);
	}

	public LIST(long data) {
		super(data, false);
	}
	public LIST(long data, Type type) {
		this(data);
		setType(type);
	}
	public LIST(long data, boolean owned) {
		super(data, owned);
	}
	public LIST(long data, boolean owned, Type type) {
		this(data, owned);
		setType(type);
	}

	public short getListEntries() { return _getUSHORT(_ListEntries); }
	public void setListEntries(short listEntries) { _setUSHORT(_ListEntries, listEntries); }
	
	public void setType(Type type) { this.type = type; }
	public Type getType() { return type; }
	
	public int getTotalSize() {
		_checkRefValidity();
		
		if(type == null) {
			throw new IllegalStateException("Cannot reliably determine size without a set type");
		}
		switch(type) {
		case NOTEREF:
			// TODO add size of NUMBER_RANGE if we implement that for some reason
			return sizeOf + (getListEntries() * UNIVERSALNOTEID.sizeOf);
		case TEXT_LIST:
			int length = sizeOf;
			long dataPtr = C.ptrAdd(data, sizeOf);
			int count = this.getListEntries();
			length += count * C.sizeOfUSHORT;
			for(int i = 0; i < count; i++) {
				length += C.getUSHORT(dataPtr, 0) & 0xFFFFFFFF;
				dataPtr = C.ptrAdd(dataPtr, C.sizeOfUSHORT);
			}
			return length;
		default:
			// We can't get here
			return sizeOf;
		}
	}
	
	/**
	 * <p>Returns the values of this list as UNIVERSALNOTEIDs.</p>
	 * 
	 * <p>The returned structures are views on the original memory.</p>
	 */
	public UNIVERSALNOTEID[] getNoteRefValues() {
		_checkRefValidity();
		
		int count = getListEntries();
		UNIVERSALNOTEID[] result = new UNIVERSALNOTEID[count];
		
		long valuePtr = C.ptrAdd(data, sizeOf);
		for(int i = 0; i < count; i++) {
			result[i] = new UNIVERSALNOTEID(valuePtr);
			valuePtr = C.ptrAdd(valuePtr, UNIVERSALNOTEID.sizeOf);
		}
		
		return result;
	}
	
	/**
	 * <p>Returns the values of this list as UTF-8 Strings.</p>
	 * 
	 * <p>The resultant data is detached from the original struct's memory.</p>
	 */
	public String[] getStringValues() {
		_checkRefValidity();
		
		
		long valuePtr = C.ptrAdd(data, sizeOf);
		int[] sizes = readSizes(valuePtr, getListEntries());
		// Increment the value ptr by the count times the size of a USHORT
		valuePtr = C.ptrAdd(valuePtr, sizes.length * C.sizeOfUSHORT);
		
		return readStringValues(valuePtr, sizes);
	}
	
	/**
	 * Returns an array of list-element sizes starting from the provided pointer.
	 * This static method can be used when reading from canonical format.
	 * 
	 * @param sizesPtr the pointer to the start of the sizes array
	 * @return the list-element sizes, as integers
	 */
	public static int[] readSizes(long sizesPtr, int listEntries) {
		long ptr = sizesPtr;
		
		int[] sizes = new int[listEntries];
		for(int i = 0; i < sizes.length; i++) {
			sizes[i] = C.getUSHORT(ptr, 0) & 0xFFFFFFFF;
			ptr = C.ptrAdd(ptr, C.sizeOfUSHORT);
		}
		
		return sizes;
	}
	
	/**
	 * Returns an array of list-element string values starting from the provided pointer.
	 * This static method can be used when reading from canonical format.
	 * @param valuesPtr the pointer to the start of the string data
	 * @param sizes an array of string sizes
	 * @return the string list values
	 */
	public static String[] readStringValues(long valuesPtr, int[] sizes) {
		long ptr = valuesPtr;
		
		String[] result = new String[sizes.length];
		
		for(int i = 0; i < sizes.length; i++) {
			if(sizes[i] == 0) {
				result[i] = ""; //$NON-NLS-1$
			} else {
				result[i] = C.getLMBCSString(ptr, 0, sizes[i]).replace('\0', '\n');
			}
			ptr = C.ptrAdd(ptr, sizes[i]);
		}
		
		return result;
	}
}
