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

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.c.C;

/**
 * (nif.h)
 * 
 * @author Jesse Gallagher
 *
 */
public class COLLECTIONDATA extends BaseStruct {
	static {
		int[] sizes = new int[7];
		initNative(sizes);
		sizeOf = sizes[0];
		_DocCount = sizes[1];
		_DocTotalSize = sizes[2];
		_BTreeLeafNodes = sizes[3];
		_BTreeDepth = sizes[4];
		_Spare = sizes[5];
		_KeyOffset = sizes[6];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _DocCount;
	public static final int _DocTotalSize;
	public static final int _BTreeLeafNodes;
	public static final int _BTreeDepth;
	public static final int _Spare;
	public static final int _KeyOffset;
	
	public COLLECTIONDATA() {
		super(C.malloc(sizeOf), true);
	}
	public COLLECTIONDATA(long data) {
		super(data, false);
	}

	public COLLECTIONDATA(long data, boolean owned) {
		super(data, owned);
	}
	
	public int getDocCount() { return _getDWORD(_DocCount); }
	public void setDocCount(int docCount) { _setDWORD(_DocCount, docCount); }
	
	public int getDocTotalSize() { return _getDWORD(_DocTotalSize); }
	public void setDocTotalSize(int docTotalSize) { _setDWORD(_DocTotalSize, docTotalSize); }
	
	public int getBTreeLeafNodes() { return _getDWORD(_BTreeLeafNodes); }
	public void setBTreeLeafNodes(int bTreeLeafNodes) { _setDWORD(_BTreeLeafNodes, bTreeLeafNodes); }
	
	public short getBTreeDepth() { return _getWORD(_BTreeDepth); }
	public void setBTreeDepth(short bTreeDepth) { _setDWORD(_BTreeDepth, bTreeDepth); }
	
	/**
	 * This method returns a copy of the key offset data from the struct. Modifications must
	 * be made by using {@link #setKeyOffset(int, int)} or by writing a new array back
	 * using {@link #setKeyOffset(int[])}.
	 */
	public int[] getKeyOffset() {
		_checkRefValidity();
		
		int[] result = new int[DominoAPI.PERCENTILE_COUNT];
		C.readIntArray(result, 0, data, _KeyOffset, DominoAPI.PERCENTILE_COUNT);
		return result;
	}
	public int getKeyOffset(int index) {
		int offset = _KeyOffset + index * C.sizeOfDWORD;
		return _getDWORD(offset);
	}
	
	public void setKeyOffset(int[] tumbler) {
		_checkRefValidity();
		
		// Make sure we have an appropriately-sized array
		int inputSize = tumbler.length < DominoAPI.PERCENTILE_COUNT ? tumbler.length : DominoAPI.PERCENTILE_COUNT;
		int[] storage = new int[DominoAPI.PERCENTILE_COUNT];
		System.arraycopy(tumbler, 0, storage, 0, inputSize);
		C.writeIntArray(data, _KeyOffset, storage, 0, DominoAPI.PERCENTILE_COUNT);
	}
	public void setKeyOffset(int index, int value) {
		int offset = _KeyOffset + index * C.sizeOfDWORD;
		_setDWORD(offset, value);
	}
}
