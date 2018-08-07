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
 * Java Short reference.
 */
public class ByteRefPtr extends BaseByteRef {

	public long ptr;
	
	public ByteRefPtr(long ptr) {
		this.ptr = ptr;
	}
	
	@Override
	public byte get() {
		return C.getByte(ptr, 0);
	}
	
	@Override
	public void set(byte value) {
		C.setByte(ptr, 0, value);
	}	
}
