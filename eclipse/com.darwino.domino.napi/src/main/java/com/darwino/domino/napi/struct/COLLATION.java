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

import com.darwino.domino.napi.c.C;

/**
 * 
 * @author Jesse Gallagher
 *
 */
public class COLLATION extends BaseStruct {
	static {
		int[] sizes = new int[5];
		initNative(sizes);
		sizeOf = sizes[0];
		_BufferSize = sizes[1];
		_Items = sizes[2];
		_Flags = sizes[3];
		_signature = sizes[4];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _BufferSize;
	public static final int _Items;
	public static final int _Flags;
	public static final int _signature;
	
	public COLLATION() {
		super(C.malloc(sizeOf), true);
	}
	public COLLATION(long data) {
		super(data, false);
	}

	public COLLATION(long data, boolean owned) {
		super(data, owned);
	}
	
	public short getBufferSize() { return _getUSHORT(_BufferSize); }
	public void setBufferSize(short bufferSize) { _setUSHORT(_BufferSize, bufferSize); }
	
	public short getItems() { return _getUSHORT(_Items); }
	public void setItems(short items) { _setUSHORT(_Items, items); }
	
	public byte getFlags() { return _getBYTE(_Flags); }
	public void setFlags(byte flags) { _setBYTE(_Flags, flags); }
	
	public byte getSignature() { return _getBYTE(_signature); }
	public void setSignature(byte signature) { _setBYTE(_signature, signature); }
}
