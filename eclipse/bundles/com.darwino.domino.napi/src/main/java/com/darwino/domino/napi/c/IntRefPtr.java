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

package com.darwino.domino.napi.c;


/**
 * Java Int reference.
 * 
 * @author priand
 */
public class IntRefPtr extends BaseIntRef {

	public long ptr;
	
	public IntRefPtr(long ptr) {
		this.ptr = ptr;
	}
	
	@Override
	public int get() {
		return C.getInt(ptr, 0);
	}
	
	@Override
	public void set(int value) {
		C.setInt(ptr, 0, value);
	}	
}
