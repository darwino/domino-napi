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
import com.darwino.domino.napi.enums.CDKEYWORD_Flag;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.ODSType;
import com.darwino.domino.napi.struct.LIST;

/**
 * (editods.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 *
 */
public class CDKEYWORD extends BaseCDStruct<WSIG> implements CDFieldStruct {
	

	static {
		int[] sizes = new int[5];
		initNative(sizes);
		sizeOf = sizes[0];
		_Header = sizes[1];
		_FontID = sizes[2];
		_Keywords = sizes[3];
		_Flags = sizes[4];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Header;
	public static final int _FontID;
	public static final int _Keywords;
	public static final int _Flags;
	
	private boolean[] onOffStates;
	private String[] keywordValues;
	
	public CDKEYWORD() {
		super(C.malloc(sizeOf),true);
	}
	public CDKEYWORD(long data) {
		super(data, false);
	}
	public CDKEYWORD(long data, boolean owned) {
		super(data, owned);
	}
	
	@Override
	public ODSType getODSType() {
		return ODSType.CDKEYWORD;
	}
	
	@Override
	protected void _readODSVariableData(DominoAPI api, long dataPtr) {
		int keywords = getKeywords();
		
		// Read in the on/off states
		this.onOffStates = new boolean[keywords];
		for(int i = 0; i < keywords; i++) {
			byte val = C.getByte(dataPtr, i);
			this.onOffStates[i] = val == 1;
		}
		
		// Read in the keyword values
		long listPtr = C.ptrAdd(dataPtr, keywords);
		LIST valuesList = new LIST(listPtr);
		this.keywordValues = valuesList.getStringValues();
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
	
	public int getFontID() {
		return _getDWORD(_FontID);
	}
	
	public short getKeywords() {
		return _getWORD(_Keywords);
	}
	
	public short getFlagsRaw() {
		return _getWORD(_Flags);
	}
	
	/* ******************************************************************************
	 * Encapsulated getters/setters
	 ********************************************************************************/
	
	public Set<CDKEYWORD_Flag> getFlags() {
		return DominoEnumUtil.valuesOf(CDKEYWORD_Flag.class, getFlagsRaw());
	}
	
	/**
	 * @return the on/off states for each keyword
	 */
	public boolean[] getOnOffStates() {
		boolean[] result = new boolean[onOffStates.length];
		System.arraycopy(onOffStates, 0, result, 0, onOffStates.length);
		return result;
	}
	/**
	 * @return the keyword values
	 */
	public String[] getKeywordValues() {
		String[] result = new String[keywordValues.length];
		System.arraycopy(keywordValues, 0, result, 0, keywordValues.length);
		return result;
	}
}
