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
import com.darwino.domino.napi.enums.VIEW_TABLE_Flag;
import com.darwino.domino.napi.enums.VIEW_TABLE_Flag2;

/**
 * This structure contains the view table format descriptor. (viewfmt.h)
 * 
 * @author Jesse Gallagher
 *
 */
public class VIEW_TABLE_FORMAT extends BaseStruct {

	static {
		int[] sizes = new int[6];
		initNative(sizes);
		sizeOf = sizes[0];
		_Header = sizes[1];
		_Columns = sizes[2];
		_ItemSequenceNumber = sizes[3];
		_Flags = sizes[4];
		_Flags2 = sizes[5];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Header;
	public static final int _Columns;
	public static final int _ItemSequenceNumber;
	public static final int _Flags;
	public static final int _Flags2;
	
	public VIEW_TABLE_FORMAT() {
		super(C.malloc(sizeOf), true);
	}
	public VIEW_TABLE_FORMAT(long data) {
		super(data, false);
	}

	public VIEW_TABLE_FORMAT(long data, boolean owned) {
		super(data, owned);
	}
	
	// ******************************************************************************
	// * Raw getters/setters
	// ******************************************************************************
	
	public VIEW_FORMAT_HEADER getHeader() {
		long headerPtr = getField(_Header);
		return new VIEW_FORMAT_HEADER(headerPtr);
	}
	
	public short getColumns() { return _getWORD(_Columns); }
	public void setColumns(short columns) { _setWORD(_Columns, columns); }
	
	public short getItemSequenceNumber() { return _getWORD(_ItemSequenceNumber); }
	public void setItemSequenceNumber(short itemSequenceNumber) { _setWORD(_ItemSequenceNumber, itemSequenceNumber); }
	
	public short getFlagsRaw() { return _getWORD(_Flags); }
	public void setFlagsRaw(short flags) { _setWORD(_Flags, flags); }
	
	public short getFlags2Raw() { return _getWORD(_Flags2); }
	public void setFlags2Raw(short flags2) { _setWORD(_Flags2, flags2); }
	
	// ******************************************************************************
	// * Encapsulated getters/setters
	// ******************************************************************************
	
	public Set<VIEW_TABLE_Flag> getFlags() {
		return DominoEnumUtil.valuesOf(VIEW_TABLE_Flag.class, getFlagsRaw());
	}
	public void setFlags(Collection<VIEW_TABLE_Flag> flags) {
		setFlagsRaw(DominoEnumUtil.toBitField(VIEW_TABLE_Flag.class, flags));
	}
	
	public Set<VIEW_TABLE_Flag2> getFlags2() {
		return DominoEnumUtil.valuesOf(VIEW_TABLE_Flag2.class, getFlags2Raw());
	}
	public void setFlags2(Collection<VIEW_TABLE_Flag2> flags) {
		setFlags2Raw(DominoEnumUtil.toBitField(VIEW_TABLE_Flag2.class, flags));
	}
}