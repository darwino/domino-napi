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
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.ODSType;
import com.darwino.domino.napi.enums.SIG_CD;

/**
 * (editods.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 * @since Notes/Domino 5.0
 *
 */
public class CDBEGINRECORD extends BaseCDStruct<BSIG> {
	

	static {
		int[] sizes = new int[4];
		initNative(sizes);
		sizeOf = sizes[0];
		_Header = sizes[1];
		_Version = sizes[2];
		_Signature = sizes[3];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Header;
	public static final int _Version;
	public static final int _Signature;
	
	public CDBEGINRECORD() {
		super(C.malloc(sizeOf),true);
	}
	public CDBEGINRECORD(long data) {
		super(data, false);
	}
	public CDBEGINRECORD(long data, boolean owned) {
		super(data, owned);
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.ODSStruct#getODSType()
	 */
	@Override
	public ODSType getODSType() {
		return ODSType.CDBEGINRECORD;
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.cd.CDStruct#loadVariableData(com.darwino.domino.napi.DominoAPI, long)
	 */
	@Override
	public void _readODSVariableData(DominoAPI api, long dataPtr) {
		// NOP
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
	public void setHeader(BSIG header) {
		_checkRefValidity();
		C.memcpy(data, _Header, header.getDataPtr(), 0, BSIG.sizeOf);
	}
	
	public short getVersion() {
		return _getWORD(_Version);
	}
	public short getSignatureRaw() {
		return _getWORD(_Signature);
	}
	
	/* ******************************************************************************
	 * Encapsulated getters/setters
	 ********************************************************************************/
	
	public SIG_CD getSignature() {
		return DominoEnumUtil.valueOf(SIG_CD.class, getSignatureRaw());
	}
}
