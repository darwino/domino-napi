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

package com.darwino.domino.napi.struct.cd;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.ODSType;

/**
 * CD record used to indicate the start of a "file resource" type element. The documentation refers to all
 * of these as CSS files. (editods.h)
 * 
 * @author Jesse Gallagher
 * @since 2.0.0
 * @since Notes/Domino 6.0
 *
 */
public class CDFILEHEADER extends BaseCDStruct<LSIG> {
	

	static {
		int[] sizes = new int[7];
		initNative(sizes);
		sizeOf = sizes[0];
		_Header = sizes[1];
		_FileExtLen = sizes[2];
		_FileDataSize = sizes[3];
		_SegCount = sizes[4];
		_Flags = sizes[5];
		_Reserved = sizes[6];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Header;
	public static final int _FileExtLen;
	public static final int _FileDataSize;
	public static final int _SegCount;
	public static final int _Flags;
	public static final int _Reserved;
	
	private String fileExt;

	public CDFILEHEADER() {
		super(C.malloc(sizeOf),true);
	}
	public CDFILEHEADER(long data) {
		super(data, false);
	}
	public CDFILEHEADER(long data, boolean owned) {
		super(data, owned);
	}

	@Override
	public ODSType getODSType() {
		return ODSType.CDFILEHEADER;
	}
	

	@Override
	protected void _readODSVariableData(DominoAPI api, long pData) {
		int fileExtLen = getFileExtLen() & 0xFFFF;
		long ptr = C.ptrAdd(getDataPtr(), sizeOf);
		this.fileExt = C.getLMBCSString(ptr, 0, fileExtLen);
	}
	
	// *******************************************************************************
	// * Struct field getters/setters
	// *******************************************************************************
	
	@Override
	public LSIG getHeader() {
		_checkRefValidity();
		return new LSIG(C.ptrAdd(data, _Header));
	}

	@Override
	public void setHeader(LSIG header) {
		_checkRefValidity();
		C.memcpy(data, _Header, header.getDataPtr(), 0, LSIG.sizeOf);
	}

	public short getFileExtLen() {
		return _getWORD(_FileExtLen);
	}
	
	public int getFileDataSize() {
		return _getDWORD(_FileDataSize);
	}
	
	public int getSegCount() {
		return _getDWORD(_SegCount);
	}
	
	// *******************************************************************************
	// * Encapsulated getters
	// *******************************************************************************
	
	public String getFileExt() {
		return fileExt;
	}
}
