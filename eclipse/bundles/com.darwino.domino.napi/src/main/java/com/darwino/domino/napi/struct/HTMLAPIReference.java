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

package com.darwino.domino.napi.struct;

import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.CmdId;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.HTMLAPI_REF_TYPE;

/**
 * Notes HTMLAPIReference struct (htmlapi.h)
 * @author Jesse Gallagher
 *
 */
public class HTMLAPIReference extends BaseStruct {

	static {
		int[] sizes = new int[10];
		initNative(sizes);
		sizeOf = sizes[0];
		_RefType = sizes[1];
		_pRefText = sizes[2];
		_CommandId = sizes[3];
		_pTargets = sizes[4];
		_pArgs = sizes[5];
		_NumTargets = sizes[6];
		_NumArgs = sizes[7];
		_pFragment = sizes[8];
		_URLParts = sizes[9];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _RefType;
	public static final int _pRefText;
	public static final int _CommandId;
	public static final int _pTargets;
	public static final int _pArgs;
	public static final int _NumTargets;
	public static final int _NumArgs;
	public static final int _pFragment;
	public static final int _URLParts;
	
	
	public HTMLAPIReference() {
		super(C.malloc(sizeOf),true);
	}
	public HTMLAPIReference(long data) {
		super(data, false);
	}
	public HTMLAPIReference(long data, boolean owned) {
		super(data, owned);
	}
	

	/* ******************************************************************************
	 * Struct field getters/setters
	 ********************************************************************************/

	public int getRefTypeRaw() { return _getHTMLAPI_REF_TYPE(_RefType); }
	
	public long getPRefText() { return _getPointer(_pRefText); }
	
	public int getCommandIdRaw() { return _getInt(_CommandId); }
	
	public long getPTargets() { return _getPointer(_pTargets); }
	
	public long getPArgs() { return _getPointer(_pArgs); }
	
	public int getNumTargets() { return _getDWORD(_NumTargets); }
	
	public int getNumArgs() { return _getDWORD(_NumArgs); }
	
	public long getPFragment() { return _getPointer(_pFragment); }
	
	public long getURLParts() { return _getPointer(_URLParts); }
	

	/* ******************************************************************************
	 * Encapsulated getters/setters
	 ********************************************************************************/
	
	public HTMLAPI_REF_TYPE getRefType() {
		return DominoEnumUtil.valueOf(HTMLAPI_REF_TYPE.class, getRefTypeRaw());
	}
	
	public CmdId getCommandId() {
		return DominoEnumUtil.valueOf(CmdId.class, getCommandIdRaw());
	}
	
	public HTMLAPI_URLTargetComponent[] getTargets() {
		_checkRefValidity();
		
		int numTargets = getNumTargets();
		HTMLAPI_URLTargetComponent[] result = new HTMLAPI_URLTargetComponent[numTargets];
		long targetPtr = getPTargets();
		for(int i = 0; i < numTargets; i++) {
			HTMLAPI_URLTargetComponent target = new HTMLAPI_URLTargetComponent(targetPtr);
			result[i] = target;
			targetPtr = C.ptrAdd(targetPtr, HTMLAPI_URLTargetComponent.sizeOf);
		}
		return result;
	}
	
	public HTMLAPI_URLArg[] getArgs() {
		_checkRefValidity();
		
		int numArgs = getNumArgs();
		HTMLAPI_URLArg[] result = new HTMLAPI_URLArg[numArgs];
		long argPtr = getPArgs();
		for(int i = 0; i < numArgs; i++) {
			HTMLAPI_URLArg arg = new HTMLAPI_URLArg(argPtr);
			result[i] = arg;
			argPtr = C.ptrAdd(argPtr, HTMLAPI_URLArg.sizeOf);
		}
		return result;
	}
	
}
