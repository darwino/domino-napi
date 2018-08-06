/*!COPYRIGHT HEADER! - CONFIDENTIAL 
 *
 * Darwino Inc Confidential.
 *
 * (c) Copyright Darwino Inc. 2014-2018.
 *
 * Notice: The information contained in the source code for these files is the property 
 * of Darwino Inc. which, with its licensors, if any, owns all the intellectual property 
 * rights, including all copyright rights thereto.  Such information may only be used 
 * for debugging, troubleshooting and informational purposes.  All other uses of this information, 
 * including any production or commercial uses, are prohibited. 
 */

package com.darwino.domino.napi.struct;

import java.util.Arrays;

import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.c.C;

/**
 * (nif.h)
 * 
 * @author Jesse Gallagher
 *
 */
public class COLLECTIONPOSITION extends BaseStruct {
	static {
		int[] sizes = new int[5];
		initNative(sizes);
		sizeOf = sizes[0];
		_Level = sizes[1];
		_MinLevel = sizes[2];
		_MaxLevel = sizes[3];
		_Tumbler = sizes[4];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Level;
	public static final int _MinLevel;
	public static final int _MaxLevel;
	public static final int _Tumbler;
	
	/**
	 * This is based on the similarly-named macro in nif.h
	 */
	public static int size(COLLECTIONPOSITION p) {
		return C.sizeOfDWORD * ((int)p.getLevel() + 2);
	}
	
	public COLLECTIONPOSITION() {
		super(C.malloc(sizeOf), true);
	}
	public COLLECTIONPOSITION(long data) {
		super(data, false);
	}

	public COLLECTIONPOSITION(long data, boolean owned) {
		super(data, owned);
	}
	
	
	public short getLevel() { return _getWORD(_Level); }
	public void setLevel(short level) { _setWORD(_Level, level); }
	
	public byte getMinLevel() { return _getBYTE(_MinLevel); }
	public void setMinLevel(byte minLevel) { _setBYTE(_MinLevel, minLevel); }
	
	public byte getMaxLevel() { return _getBYTE(_MaxLevel); }
	public void setMaxLevel(byte maxLevel) { _setBYTE(_MaxLevel, maxLevel); }
	
	/**
	 * This method returns a copy of the tumbler data from the struct. Modifications must
	 * be made by using {@link #setTumbler(int, int)} or by writing a new array back
	 * using {@link #setTumbler(int[])}.
	 */
	public int[] getTumbler() {
		_checkRefValidity();
		
		int[] result = new int[DominoAPI.MAXTUMBLERLEVELS];
		C.readIntArray(result, 0, data, _Tumbler, DominoAPI.MAXTUMBLERLEVELS);
		return result;
	}
	/**
	 * @param index 0-based index into the internal Tumbler array
	 */
	public int getTumbler(int index) {
		int offset = _Tumbler + index * C.sizeOfDWORD;
		return _getDWORD(offset);
	}
	/**
	 * Returns a copy of the tumbler data up to the level, so the size of the array may be less
	 * than {@link DominoAPI#MAXTUMBLERLEVELS}.
	 */
	public int[] getTumblerTruncated() {
		int level = getLevel();
		int[] result = new int[level];
		for(int i = 0; i < level; i++) {
			result[i] = getTumbler(i);
		}
		return result;
	}
	
	public void setTumbler(int[] tumbler) {
		_checkRefValidity();
		
		// Make sure we have an appropriately-sized array
		int inputSize = tumbler.length < DominoAPI.MAXTUMBLERLEVELS ? tumbler.length : DominoAPI.MAXTUMBLERLEVELS;
		int[] storage = new int[DominoAPI.MAXTUMBLERLEVELS];
		System.arraycopy(tumbler, 0, storage, 0, inputSize);
		C.writeIntArray(data, _Tumbler, storage, 0, DominoAPI.MAXTUMBLERLEVELS);
	}
	/**
	 * @param index 0-based index into the internal Tumbler array
	 */
	public void setTumbler(int index, int value) {
		int offset = _Tumbler + index * C.sizeOfDWORD;
		_setDWORD(offset, value);
	}
	
	@Override
	public String toString() {
		if(isRefValid()) {
			return StringUtil.format("[{0}; level={1}, minLevel={2}, maxLevel={3}, tumbler={4}]", getClass().getName(), getLevel(), //$NON-NLS-1$
					getMinLevel(), getMaxLevel(), Arrays.asList(getTumblerTruncated()));
		} else {
			return super.toString();
		}
				
	}
}
