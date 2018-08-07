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

import java.util.Set;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.EC_FLAG;
import com.darwino.domino.napi.enums.EC_STYLE;
import com.darwino.domino.napi.enums.EMBEDDEDCTL_TYPE;
import com.darwino.domino.napi.enums.ODSType;

/**
 * (editods.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 * @since Notes/Domino 5.0
 *
 */
public class CDEMBEDDEDCTL extends BaseCDStruct<WSIG> implements CDFieldStruct {
	static {
		int[] sizes = new int[12];
		initNative(sizes);
		sizeOf = sizes[0];
		_Header = sizes[1];
		_CtlStyle = sizes[2];
		_Flags = sizes[3];
		_Width = sizes[4];
		_Height = sizes[5];
		_Version = sizes[6];
		_CtlType = sizes[7];
		_MaxChars = sizes[8];
		_MaxLines = sizes[9];
		_Percentage = sizes[10];
		_Spare = sizes[11];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Header;
	public static final int _CtlStyle;
	public static final int _Flags;
	public static final int _Width;
	public static final int _Height;
	public static final int _Version;
	public static final int _CtlType;
	public static final int _MaxChars;
	public static final int _MaxLines;
	public static final int _Percentage;
	public static final int _Spare;
	
	public CDEMBEDDEDCTL() {
		super(C.malloc(sizeOf),true);
	}
	public CDEMBEDDEDCTL(long data) {
		super(data, false);
	}
	public CDEMBEDDEDCTL(long data, boolean owned) {
		super(data, owned);
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.ODSStruct#getODSType()
	 */
	@Override
	public ODSType getODSType() {
		return ODSType.EMBEDDEDCTL;
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
	public WSIG getHeader() {
		_checkRefValidity();
		return new WSIG(C.ptrAdd(data, _Header));
	}
	@Override
	public void setHeader(WSIG signature) {
		_checkRefValidity();
		C.memcpy(data, _Header, signature.getDataPtr(), 0, WSIG.sizeOf);
	}
	
	public int getCtlStyleRaw() {
		return _getDWORD(_CtlStyle);
	}
	public short getFlagsRaw() {
		return _getWORD(_Flags);
	}
	public short getWidth() {
		return _getWORD(_Width);
	}
	public short getHeight() {
		return _getWORD(_Height);
	}
	public short getVersion() {
		return _getWORD(_Version);
	}
	public short getCtlTypeRaw() {
		return _getWORD(_CtlType);
	}
	public short getMaxChars() {
		return _getWORD(_MaxChars);
	}
	public short getMaxLines() {
		return _getWORD(_MaxLines);
	}
	public short getPercentage() {
		return _getWORD(_Percentage);
	}
	
	/* ******************************************************************************
	 * Encapsulated getters/setters
	 ********************************************************************************/
	
	public EC_STYLE getCtlStyle() {
		return DominoEnumUtil.valueOf(EC_STYLE.class, getCtlStyleRaw());
	}
	
	public Set<EC_FLAG> getFlags() {
		return DominoEnumUtil.valuesOf(EC_FLAG.class, getFlagsRaw());
	}
	
	public EMBEDDEDCTL_TYPE getCtlType() {
		return DominoEnumUtil.valueOf(EMBEDDEDCTL_TYPE.class, getCtlTypeRaw());
	}
}
