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
 * This structure contains view format information for views saved in Domino Release 5 and later. (viewfmt.h)
 * 
 * @author Jesse Gallagher
 *
 */
public class VIEW_TABLE_FORMAT3 extends BaseStruct {

	static {
		int[] sizes = new int[18];
		initNative(sizes);
		sizeOf = sizes[0];
		_Length = sizes[1];
		_Flags = sizes[2];
		_BackgroundColor = sizes[3];
		_AlternateBackgroundColor = sizes[4];
		_GridColorValue = sizes[5];
		_wViewMarginTop = sizes[6];
		_wViewMarginLeft = sizes[7];
		_wViewMarginRight = sizes[8];
		_wViewMarginBottom = sizes[9];
		_MarginBackgroundColor = sizes[10];
		_HeaderBackgroundColor = sizes[11];
		_wViewMarginTopUnder = sizes[12];
		_UnreadColor = sizes[13];
		_TotalsColor = sizes[14];
		_wMaxRows = sizes[15];
		_wThemeSetting = sizes[16];
		_dwReserved = sizes[17];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Length;
	public static final int _Flags;
	public static final int _BackgroundColor;
	public static final int _AlternateBackgroundColor;
	public static final int _GridColorValue;
	public static final int _wViewMarginTop;
	public static final int _wViewMarginLeft;
	public static final int _wViewMarginRight;
	public static final int _wViewMarginBottom;
	public static final int _MarginBackgroundColor;
	public static final int _HeaderBackgroundColor;
	public static final int _wViewMarginTopUnder;
	public static final int _UnreadColor;
	public static final int _TotalsColor;
	public static final int _wMaxRows;
	public static final int _wThemeSetting;
	public static final int _dwReserved;
	
	public VIEW_TABLE_FORMAT3() {
		super(C.malloc(sizeOf), true);
	}
	public VIEW_TABLE_FORMAT3(long data) {
		super(data, false);
	}

	public VIEW_TABLE_FORMAT3(long data, boolean owned) {
		super(data, owned);
	}
	
	public short getLength() { return _getWORD(_Length); }
	public void setLength(short length) { _setWORD(_Length, length); }
}
