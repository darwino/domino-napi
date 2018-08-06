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

import java.util.Collection;
import java.util.Set;

import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.VCF3;

/**
 * 
 * @author Jesse Gallagher
 *
 */
public class VIEW_COLUMN_FORMAT2 extends BaseStruct {
	static {
		int[] sizes = new int[12];
		initNative(sizes);
		sizeOf = sizes[0];
		_Signature = sizes[1];
		_HeaderFontID = sizes[2];
		_ResortToViewUNID = sizes[3];
		_wSecondResortColumnIndex = sizes[4];
		_Flags3 = sizes[5];
		_wHideWhenFormulaSize = sizes[6];
		_wTwistieResourceSize = sizes[7];
		_wCustomOrder = sizes[8];
		_wCustomHiddenFlags = sizes[9];
		_ColumnColor = sizes[10];
		_HeaderFontColor = sizes[11];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Signature;
	public static final int _HeaderFontID;
	public static final int _ResortToViewUNID;
	public static final int _wSecondResortColumnIndex;
	public static final int _Flags3;
	public static final int _wHideWhenFormulaSize;
	public static final int _wTwistieResourceSize;
	public static final int _wCustomOrder;
	public static final int _wCustomHiddenFlags;
	public static final int _ColumnColor;
	public static final int _HeaderFontColor;
	
	public VIEW_COLUMN_FORMAT2() {
		super(C.malloc(sizeOf), true);
	}
	public VIEW_COLUMN_FORMAT2(long data) {
		super(data, false);
	}

	public VIEW_COLUMN_FORMAT2(long data, boolean owned) {
		super(data, owned);
	}
	
	public short getSignature() { return _getWORD(_Signature); }
	public void setSignature(short signature) { _setWORD(_Signature, signature); }

	// ******************************************************************************
	// * Raw getters/setters
	// ******************************************************************************
	
	public short getFlags3Raw() {
		return _getWORD(_Flags3);
	}
	public void setFlags3Raw(short flags3) {
		_setWORD(_Flags3, flags3);
	}

	// ******************************************************************************
	// * Encapsulated getters/setters
	// ******************************************************************************
	
	public Set<VCF3> getFlags3() {
		return DominoEnumUtil.valuesOf(VCF3.class, getFlags3Raw());
	}
	public void setFlags1(Collection<VCF3> flags1) {
		setFlags3Raw(DominoEnumUtil.toBitField(VCF3.class, flags1));
	}
	
}
