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

package com.darwino.domino.napi.proc;

import com.darwino.domino.napi.DominoException;

/**
 * (ods.h)
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public abstract class ActionRoutinePtr extends BaseProc {

	static {
		initNative();
	}
	private static final native void initNative();

	/**
	 * @param recordPtr a pointer to the current composite-data record
	 * @param recordType the SIG_CD_xxx signature WORD of the record
	 * @param recordLength the length (in bytes) of the record
	 * @return a status code that maps to a Domino error code, ERR_CANCEL to cancel, or 0 on success 
	 */
	public abstract short callback(long recordPtr, short recordType, int recordLength) throws DominoException; 

}
