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

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.ODSType;

/**
 * This class denotes a struct that is stored in canonical format (and thus needs to go through ODSReadMemory),
 * and which may have variable data stored after it.
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public abstract class BaseODSStruct extends BaseStruct {
	
	/**
	 * @param data
	 * @param owned
	 */
	public BaseODSStruct(long data, boolean owned) {
		super(data, owned);
	}

	public abstract ODSType getODSType();
	
	/**
	 * Reads the structure from a canonical ODS format, including any variable
	 * data after the fixed struct.
	 * 
	 * @param api the {@link DominoAPI} instance to use for ODS operations
	 * @param pData a pointer to the start of the data
	 */
	public final void readODSVariableData(DominoAPI api, long pData) {
		long ppData = C.malloc(C.sizeOfPOINTER);
		try {
			C.setPointer(ppData, 0, pData);
			api.ODSReadMemory(ppData, getODSType().getValue(), getDataPtr(), (short)1);

			_readODSVariableData(api, C.getPointer(ppData, 0));
		} finally {
			C.free(ppData);
		}
		
	}
	
	protected abstract void _readODSVariableData(DominoAPI api, long pData);
}
