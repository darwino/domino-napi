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
