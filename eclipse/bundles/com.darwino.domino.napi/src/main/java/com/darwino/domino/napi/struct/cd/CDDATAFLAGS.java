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
import com.darwino.domino.napi.enums.CD_ELEMENT;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.ODSType;

/**
 * (editods.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 * @since Notes/Domino 6.0
 *
 */
public class CDDATAFLAGS extends BaseCDStruct<BSIG> implements CDFieldStruct {
	

	static {
		int[] sizes = new int[5];
		initNative(sizes);
		sizeOf = sizes[0];
		_Header = sizes[1];
		_nFlags = sizes[2];
		_elemType = sizes[3];
		_dwReserved = sizes[4];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Header;
	public static final int _nFlags;
	public static final int _elemType;
	public static final int _dwReserved;
	
	private int[] flags;
	
	public CDDATAFLAGS() {
		super(C.malloc(sizeOf),true);
	}
	public CDDATAFLAGS(long data) {
		super(data, false);
	}
	public CDDATAFLAGS(long data, boolean owned) {
		super(data, owned);
	}
	
	@Override
	public ODSType getODSType() {
		return ODSType.CDDATAFLAGS;
	}
	
	@Override
	protected void _readODSVariableData(DominoAPI api, long dataPtr) {
		int nFlags = this.getNFlags();
		this.flags = new int[nFlags];
		for(int i = 0; i < nFlags; i++) {
			this.flags[i] = C.getDWORD(dataPtr, C.sizeOfDWORD * i);
		}
	}
	
	/* ******************************************************************************
	 * Struct field getters/setters
	 ********************************************************************************/

	@Override
	public BSIG getHeader() {
		_checkRefValidity();
		return new BSIG(C.ptrAdd(data, _Header));
	}
	@Override
	public void setHeader(BSIG signature) {
		_checkRefValidity();
		C.memcpy(data, _Header, signature.getDataPtr(), 0, BSIG.sizeOf);
	}
	
	public short getNFlags() {
		return _getWORD(_nFlags);
	}
	
	public short getElemTypeRaw() {
		return _getWORD(_elemType);
	}
	
	public int[] getFlags() {
		int[] result = new int[flags.length];
		System.arraycopy(this.flags, 0, result, 0, flags.length);
		return result;
	}
	
	/* ******************************************************************************
	 * Encapsulated getters/setters
	 ********************************************************************************/
	
	public CD_ELEMENT getElemType() {
		return DominoEnumUtil.valueOf(CD_ELEMENT.class, getElemTypeRaw());
	}
	
}
