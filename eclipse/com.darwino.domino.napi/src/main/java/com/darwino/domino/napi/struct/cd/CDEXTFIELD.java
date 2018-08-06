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

import java.util.Set;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.CDEXTFIELD_HELPER;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.FEXT1;
import com.darwino.domino.napi.enums.FEXT2;
import com.darwino.domino.napi.enums.ODSType;

/**
 * (editods.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 * @since Notes/Domino 4.0
 *
 */
public class CDEXTFIELD extends BaseCDStruct<WSIG> implements CDFieldStruct {
	static {
		int[] sizes = new int[8];
		initNative(sizes);
		sizeOf = sizes[0];
		_Header = sizes[1];
		_Flags1 = sizes[2];
		_Flags2 = sizes[3];
		_EntryHelper = sizes[4];
		_EntryDBNameLen = sizes[5];
		_EntryViewNameLen = sizes[6];
		_EntryColumnNumber = sizes[7];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Header;
	public static final int _Flags1;
	public static final int _Flags2;
	public static final int _EntryHelper;
	public static final int _EntryDBNameLen;
	public static final int _EntryViewNameLen;
	public static final int _EntryColumnNumber;
	
	private String entryDbName;
	private String entryViewName;
	
	public CDEXTFIELD() {
		super(C.malloc(sizeOf),true);
	}
	public CDEXTFIELD(long data) {
		super(data, false);
	}
	public CDEXTFIELD(long data, boolean owned) {
		super(data, owned);
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.ODSStruct#getODSType()
	 */
	@Override
	public ODSType getODSType() {
		return ODSType.CDEXTFIELD;
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.cd.CDStruct#loadVariableData(com.darwino.domino.napi.DominoAPI, long)
	 */
	@Override
	public void _readODSVariableData(DominoAPI api, long dataPtr) {
		int dbNameLen = this.getEntryDBNameLen() & 0xFFFF;
		if(dbNameLen > 0) {
			this.entryDbName = C.getLMBCSString(dataPtr, 0, dbNameLen);
		} else {
			this.entryDbName = null;
		}
		int viewNameLen = this.getEntryViewNameLen() & 0xFFFF;
		if(viewNameLen > 0) {
			this.entryViewName = C.getLMBCSString(dataPtr, dbNameLen, viewNameLen);
		} else {
			this.entryViewName = null;
		}
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
	
	public int getFlags1Raw() {
		return _getDWORD(_Flags1);
	}
	public int getFlags2Raw() {
		return _getDWORD(_Flags2);
	}
	public short getEntryHelperTypeRaw() {
		return _getWORD(_EntryHelper);
	}
	public short getEntryDBNameLen() {
		return _getWORD(_EntryDBNameLen);
	}
	public short getEntryViewNameLen() {
		return _getWORD(_EntryViewNameLen);
	}
	public short getEntryColumnNumber() {
		return _getWORD(_EntryColumnNumber);
	}
	
	/* ******************************************************************************
	 * Encapsulated getters/setters
	 ********************************************************************************/
	
	public Set<FEXT1> getFlags1() {
		return DominoEnumUtil.valuesOf(FEXT1.class, getFlags1Raw());
	}
	
	public Set<FEXT2> getFlags2() {
		return DominoEnumUtil.valuesOf(FEXT2.class, getFlags2Raw());
	}
	
	public CDEXTFIELD_HELPER getEntryHelperType() {
		return DominoEnumUtil.valueOf(CDEXTFIELD_HELPER.class, getEntryHelperTypeRaw());
	}
	
	public String getEntryDbName() {
		return entryDbName;
	}
	
	public String getEntryViewName() {
		return entryViewName;
	}
}
