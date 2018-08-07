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
package com.darwino.domino.napi.struct.cd;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.ODSType;

/**
 * CD record used to house a component of a "file resource" type element. The documentation refers to all
 * of these as CSS files. (editods.h)
 * 
 * @author Jesse Gallagher
 * @since 2.0.0
 * @since Notes/Domino 6.0
 *
 */
public class CDFILESEGMENT extends BaseCDStruct<LSIG> {

	static {
		int[] sizes = new int[6];
		initNative(sizes);
		sizeOf = sizes[0];
		_Header = sizes[1];
		_DataSize = sizes[2];
		_SegSize = sizes[3];
		_Flags = sizes[4];
		_Reserved = sizes[5];
	}
	private static final native void initNative(int[] sizes);
	

	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Header;
	public static final int _DataSize;
	public static final int _SegSize;
	public static final int _Flags;
	public static final int _Reserved;
	
	private byte[] fileData;

	public CDFILESEGMENT() {
		super(C.malloc(sizeOf),true);
	}
	public CDFILESEGMENT(long data) {
		super(data, false);
	}
	public CDFILESEGMENT(long data, boolean owned) {
		super(data, owned);
	}

	@Override
	public ODSType getODSType() {
		return ODSType.CDFILESEGMENT;
	}
	
	@Override
	protected void _readODSVariableData(DominoAPI api, long pData) {
		int dataSize = getDataSize() & 0xFFFF;
		this.fileData = new byte[dataSize];
		C.readByteArray(this.fileData, 0, pData, 0, dataSize);
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
	
	public short getDataSize() {
		return _getWORD(_DataSize);
	}
	
	public short getSegSize() {
		return _getWORD(_SegSize);
	}
	
	// *******************************************************************************
	// * Encapsulated getters/setters
	// *******************************************************************************
	
	/**
	 * Returns the byte array used to store the copied data from the file segment.
	 * 
	 * <p>Warning: this is the internal byte array from this object and should not
	 * be modified. Modifications will not be written back to the underlying
	 * composite data structure.</p>
	 * 
	 * @return the byte array of the file segments data
	 */
	public byte[] getFileData() {
		return fileData;
	}
}
