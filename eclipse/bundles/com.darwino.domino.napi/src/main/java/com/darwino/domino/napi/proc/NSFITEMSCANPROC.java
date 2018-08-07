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

/**
 * Proc for enumerating over the items in a note (nsfnote.h)
 * 
 * @author Jesse Gallagher
 *
 */
public abstract class NSFITEMSCANPROC extends BaseProc {
	static {
		initNative();
	}
	private static final native void initNative();
	
	/**
	 * <p>A method to be called for each item in a note.</p>
	 * 
	 * <p>Returning {@link com.darwino.domino.napi.DominoAPI#ERR_CANCEL} from the callback will
	 * cancel out of the iteration without generating an exception.</p>
	 * 
	 * @param itemFlags the current item's flags; see ITEM_xxx
	 * @param itemName the name of the current item
	 * @param valuePtr a pointer to the value of the item in memory
	 * @param valueLength the length of the item's value in memory, as an unsigned integer
	 * @return a status code that maps to a Domino error code, ERR_CANCEL to cancel, or 0 on success. 
	 */
	public abstract short callback(short itemFlags, String itemName, long valuePtr, int valueLength) throws Exception;
}
