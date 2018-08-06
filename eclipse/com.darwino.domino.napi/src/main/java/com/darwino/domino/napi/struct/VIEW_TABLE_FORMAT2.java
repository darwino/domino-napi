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
 * This structure contains view format information for views saved in Notes 2.0 and later. (viewfmt.h)
 * 
 * @author Jesse Gallagher
 *
 */
public class VIEW_TABLE_FORMAT2 extends BaseStruct {

	static {
		int[] sizes = new int[16];
		initNative(sizes);
		sizeOf = sizes[0];
		_Length = sizes[1];
		_BackgroundColor = sizes[2];
		_V2BorderColor = sizes[3];
		_TitleFont = sizes[4];
		_UnreadFont = sizes[5];
		_TotalsFont = sizes[6];
		_AutoUpdateSeconds = sizes[7];
		_AlternateBackgroundColor = sizes[8];
		_wSig = sizes[9];
		_LineCount = sizes[10];
		_Spacing = sizes[11];
		_BackgroundColorExt = sizes[12];
		_HeaderLineCount = sizes[13];
		_Flags1 = sizes[14];
		_Spare = sizes[15];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Length;
	public static final int _BackgroundColor;
	public static final int _V2BorderColor;
	public static final int _TitleFont;
	public static final int _UnreadFont;
	public static final int _TotalsFont;
	public static final int _AutoUpdateSeconds;
	public static final int _AlternateBackgroundColor;
	public static final int _wSig;
	public static final int _LineCount;
	public static final int _Spacing;
	public static final int _BackgroundColorExt;
	public static final int _HeaderLineCount;
	public static final int _Flags1;
	public static final int _Spare;
	
	public VIEW_TABLE_FORMAT2() {
		super(C.malloc(sizeOf), true);
	}
	public VIEW_TABLE_FORMAT2(long data) {
		super(data, false);
	}

	public VIEW_TABLE_FORMAT2(long data, boolean owned) {
		super(data, owned);
	}
	
	public short getLength() { return _getWORD(_Length); }
	public void setLength(short length) { _setWORD(_Length, length); }
	
	public short getBackgroundColor() { return _getWORD(_BackgroundColor); }
	public void setBackgroundColor(short backgroundColor) { _setWORD(_BackgroundColor, backgroundColor); }
	
	public short getV2BorderColor() { return _getWORD(_V2BorderColor); }
	public void setV2BorderColor(short v2BorderColor) { _setWORD(_V2BorderColor, v2BorderColor); }
	
	public int getTitleFont() { return _getFONTID(_TitleFont); }
	public void setTitleFont(int titleFont) { _setFONTID(_TitleFont, titleFont); }
	
	public int getUnreadFont() { return _getFONTID(_UnreadFont); }
	public void setUnreadFont(int unreadFont) { _setFONTID(_UnreadFont, unreadFont); }
	
	public int getTotalsFont() { return _getFONTID(_TotalsFont); }
	public void setTotalsFont(int totalsFont) { _setFONTID(_TotalsFont, totalsFont); }
	
	public short getAutoUpdateSeconds() { return _getWORD(_AutoUpdateSeconds); }
	public void setAutoUpdateSeconds(short autoUpdateSeconds) { _setWORD(_AutoUpdateSeconds, autoUpdateSeconds); }
	
	public short getAlternateBackgroundColor() { return _getWORD(_AlternateBackgroundColor); }
	public void setAlternateBackgroundColor(short alternateBackgroundColor) { _setWORD(_AlternateBackgroundColor, alternateBackgroundColor); }
	
	public short getWSig() { return _getWORD(_wSig); }
	public void setWSig(short wsig) { _setWORD(_wSig, wsig); }
	
	public byte getLineCount() { return _getBYTE(_LineCount); }
	public void setLineCount(byte lineCount) { _setBYTE(_LineCount, lineCount); }
	
	public byte getSpacing() { return _getBYTE(_Spacing); }
	public void setSpacing(byte spacing) { _setBYTE(_Spacing, spacing); }
	
	public short getBackgroundColorExt() { return _getWORD(_BackgroundColorExt); }
	public void setBackgroundColorExt(short backgroundColorExt) { _setWORD(_BackgroundColorExt, backgroundColorExt); }
	
	public byte getHeaderLineCount() { return _getBYTE(_HeaderLineCount); }
	public void setHeaderLineCount(byte headerLineCount) { _setWORD(_HeaderLineCount, headerLineCount); }
	
	public byte getFlags1() { return _getBYTE(_Flags1); }
	public void setFlags1(byte flags1) { _setBYTE(_Flags1, flags1); }
}
