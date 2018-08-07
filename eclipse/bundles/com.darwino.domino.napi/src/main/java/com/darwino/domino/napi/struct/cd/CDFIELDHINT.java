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
 * (editods.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 *
 */
public class CDFIELDHINT extends BaseCDStruct<WSIG> implements CDFieldStruct {

	static {
		int[] sizes = new int[6];
		initNative(sizes);
		sizeOf = sizes[0];
		_Header = sizes[1];
		_HintTextLength = sizes[2];
		_Flags = sizes[3];
		_Spare = sizes[4];
		_Spare2 = sizes[5];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Header;
	public static final int _HintTextLength;
	public static final int _Flags;
	public static final int _Spare;
	public static final int _Spare2;
	
	private String hintText;
	
	public CDFIELDHINT() {
		super(C.malloc(sizeOf),true);
	}
	public CDFIELDHINT(long data) {
		super(data, false);
	}
	public CDFIELDHINT(long data, boolean owned) {
		super(data, owned);
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.cd.CDStruct#getODSType()
	 */
	@Override
	public ODSType getODSType() {
		return ODSType.CDFIELDHINT;
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.cd.CDStruct#loadVariableData(com.darwino.domino.napi.DominoAPI, long)
	 */
	@Override
	protected void _readODSVariableData(DominoAPI api, long dataPtr) {
		this.hintText = C.getLMBCSString(dataPtr, 0, this.getHintTextLength() & 0xFFFF);
	}

	/* ******************************************************************************
	 * Struct field getters/setters
	 ********************************************************************************/

	@Override
	public WSIG getHeader() {
		_checkRefValidity();
		return new WSIG(C.ptrAdd(data, _Header));
	}
	@Override
	public void setHeader(WSIG signature) {
		_checkRefValidity();
		C.memcpy(data, _Header, signature.getDataPtr(), 0, WSIG.sizeOf);
	}
	
	public short getHintTextLength() {
		return _getWORD(_HintTextLength);
	}
	public void setHintTextLength(short hintTextLength) {
		_setWORD(_HintTextLength, hintTextLength);
	}

	// ******************************************************************************
	// * Encapsulated getters/setters
	// ******************************************************************************
	
	/**
	 * @return the hint text
	 */
	public String getHintText() {
		return hintText;
	}
}
