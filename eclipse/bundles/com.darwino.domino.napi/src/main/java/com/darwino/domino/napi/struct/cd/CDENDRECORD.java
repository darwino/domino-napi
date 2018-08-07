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
public class CDENDRECORD extends BaseCDStruct<BSIG> {
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
	
	public CDENDRECORD() {
		super(C.malloc(sizeOf),true);
	}
	public CDENDRECORD(long data) {
		super(data, false);
	}
	public CDENDRECORD(long data, boolean owned) {
		super(data, owned);
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.ODSStruct#getODSType()
	 */
	@Override
	public ODSType getODSType() {
		return ODSType.CDENDRECORD;
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
	public void setHeader(BSIG signature) {
		_checkRefValidity();
		C.memcpy(data, _Header, signature.getDataPtr(), 0, BSIG.sizeOf);
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
