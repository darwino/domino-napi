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
 * (nsfdata.h)
 * 
 * @author Jesse Gallagher
 *
 */
public class ITEM extends BaseStruct {
	static {
		int[] sizes = new int[3];
		initNative(sizes);
		sizeOf = sizes[0];
		_NameLength = sizes[1];
		_ValueLength = sizes[2];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _NameLength;
	public static final int _ValueLength;
	
	public ITEM() {
		super(C.malloc(sizeOf), true);
	}
	public ITEM(long data) {
		super(data, false);
	}

	public ITEM(long data, boolean owned) {
		super(data, owned);
	}
	
	public short getNameLength() { return _getUSHORT(_NameLength); }
	public void setNameLength(short nameLength) { _setUSHORT(_NameLength, nameLength); }

	public short getValueLength() { return _getUSHORT(_ValueLength); }
	public void setValueLength(short valueLength) { _setUSHORT(_ValueLength, valueLength); }
}
