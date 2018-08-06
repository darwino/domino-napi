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
 * Java Long reference.
 * 
 * @author priand
 */
public class LongRefPtr extends BaseLongRef {

	public long ptr;
	
	public LongRefPtr(long ptr) {
		this.ptr = ptr;
	}
	
	@Override
	public long get() {
		return C.getLong(ptr, 0);
	}
	
	@Override
	public void set(long value) {
		C.setLong(ptr, 0, value);
	}	
}
