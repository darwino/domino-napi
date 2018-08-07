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
import java.util.Set;

import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.LDDELIM;
import com.darwino.domino.napi.enums.VCF1;
import com.darwino.domino.napi.enums.VIEW_COL;

/**
 * 
 * @author Jesse Gallagher
 *
 */
public class VIEW_COLUMN_FORMAT extends BaseStruct {
	static {
		int[] sizes = new int[14];
		initNative(sizes);
		sizeOf = sizes[0];
		_Signature = sizes[1];
		_Flags1 = sizes[2];
		_ItemNameSize = sizes[3];
		_TitleSize = sizes[4];
		_FormulaSize = sizes[5];
		_ConstantValueSize = sizes[6];
		_DisplayWidth = sizes[7];
		_FontID = sizes[8];
		_Flags2 = sizes[9];
		_NumberFormat = sizes[10];
		_TimeFormat = sizes[11];
		_FormatDataType = sizes[12];
		_ListSep = sizes[13];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Signature;
	public static final int _Flags1;
	public static final int _ItemNameSize;
	public static final int _TitleSize;
	public static final int _FormulaSize;
	public static final int _ConstantValueSize;
	public static final int _DisplayWidth;
	public static final int _FontID;
	public static final int _Flags2;
	public static final int _NumberFormat;
	public static final int _TimeFormat;
	public static final int _FormatDataType;
	public static final int _ListSep;
	
	public VIEW_COLUMN_FORMAT() {
		super(C.malloc(sizeOf), true);
	}
	public VIEW_COLUMN_FORMAT(long data) {
		super(data, false);
	}

	public VIEW_COLUMN_FORMAT(long data, boolean owned) {
		super(data, owned);
	}
	
	// ******************************************************************************
	// * Raw getters/setters
	// ******************************************************************************
	
	public short getSignature() { return _getWORD(_Signature); }
	public void setSignature(short signature) { _setWORD(_Signature, signature); }
	
	public short getFlags1Raw() { return _getWORD(_Flags1); }
	public void setFlags1Raw(short flags1) { _setWORD(_Flags1, flags1); }
	
	public short getItemNameSize() { return _getWORD(_ItemNameSize); }
	public void setItemNameSize(short itemNameSize) { _setWORD(_ItemNameSize, itemNameSize); }
	
	public short getTitleSize() { return _getWORD(_TitleSize); }
	public void setTitleSize(short titleSize) { _setWORD(_TitleSize, titleSize); }
	
	public short getFormulaSize() { return _getWORD(_FormulaSize); }
	public void setFormulaSize(short formulaSize) { _setWORD(_FormulaSize, formulaSize); }
	
	public short getConstantValueSize() { return _getWORD(_ConstantValueSize); }
	public void setConstantValueSize(short constantValueSize) { _setWORD(_ConstantValueSize, constantValueSize); }
	
	public short getDisplayWidth() { return _getWORD(_DisplayWidth); }
	public void setDisplayWidth(short displayWidth) { _setWORD(_DisplayWidth, displayWidth); }
	
	public int getFontID() { return _getFONTID(_FontID);}
	public void setFontID(int fontId) { _setFONTID(_FontID, fontId); }
	
	public short getFlags2() { return _getWORD(_Flags2); }
	public void setFlags2(short flags2) { _setWORD(_Flags2, flags2); }
	
	public NFMT getNumberFormat() {
		long structPtr = getField(_NumberFormat);
		return new NFMT(structPtr);
	}
	
	public TFMT getTimeFormat() {
		long structPtr = getField(_TimeFormat);
		return new TFMT(structPtr);
	}
	
	public short getFormatDataTypeRaw() { return _getWORD(_FormatDataType); }
	public void setFormatDataTypeRaw(short formatDataType) { _setWORD(_FormatDataType, formatDataType); }
	
	public short getListSepRaw() { return _getWORD(_ListSep); }
	public void setListSepRaw(short listSep) { _setWORD(_ListSep, listSep); }
	
	// ******************************************************************************
	// * Encapsulated getters/setters
	// ******************************************************************************
	
	public Set<VCF1> getFlags1() {
		return DominoEnumUtil.valuesOf(VCF1.class, getFlags1Raw());
	}
	public void setFlags1(Collection<VCF1> flags1) {
		setFlags1Raw(DominoEnumUtil.toBitField(VCF1.class, flags1));
	}
	
	public VIEW_COL getFormatDataType() {
		return DominoEnumUtil.valueOf(VIEW_COL.class, getFormatDataTypeRaw());
	}
	public void setFormatDataType(VIEW_COL type) {
		setFormatDataTypeRaw(type.getValue());
	}
	
	public LDDELIM getListSep() {
		return DominoEnumUtil.valueOf(LDDELIM.class, getListSepRaw());
	}
	public void setListSep(LDDELIM listSep) {
		setListSepRaw(listSep.getValue());
	}
}
