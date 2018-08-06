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
 * Proc for enumerating the IDs in an IDTable
 * 
 * @author priand
 */
public abstract class IDENUMERATEPROC extends BaseProc {
	
	static {
		initNative();
	}
	private static final native void initNative();

	/**
	 * <p>A method to be called for each note ID in the IDTable.</p>
	 * 
	 * <p>Returning {@link com.darwino.domino.napi.DominoAPI#ERR_CANCEL} from the callback will
	 * cancel out of the iteration without generating an exception.</p>
	 * 
	 * @param id the current ID to process
	 * @return a status code that maps to a Domino error code, ERR_CANCEL to cancel, or 0 on success. 
	 */
	public abstract short callback(int id) throws DominoException; 
}
