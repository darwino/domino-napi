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
public class NFMT extends BaseStruct {
	static {
		int[] sizes = new int[5];
		initNative(sizes);
		sizeOf = sizes[0];
		_Digits = sizes[1];
		_Format = sizes[2];
		_Attributes = sizes[3];
		_Unused = sizes[4];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Digits;
	public static final int _Format;
	public static final int _Attributes;
	public static final int _Unused;
	
	public NFMT() {
		super(C.malloc(sizeOf), true);
	}
	public NFMT(long data) {
		super(data, false);
	}

	public NFMT(long data, boolean owned) {
		super(data, owned);
	}
	
	public byte getDigits() { return _getBYTE(_Digits); }
	public void setDigits(byte digits) { _setBYTE(_Digits, digits); }
	
	public byte getFormat() { return _getBYTE(_Format); }
	public void setFormat(byte format) { _setBYTE(_Format, format); }
	
	public byte getAttributes() { return _getBYTE(_Attributes); }
	public void setAttributes(byte attributes) { _setBYTE(_Attributes, attributes); }
}
