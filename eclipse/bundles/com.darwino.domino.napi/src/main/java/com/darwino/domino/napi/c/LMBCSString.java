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

import java.io.Closeable;

public class LMBCSString implements Closeable {

	private String value;
	private long data;
	private int length = -1;
	
	public LMBCSString(String value) {
		this.value = value;
		data = C.toLMBCSString(this.value);
	}
	
	public int length() {
		if(length < 0) {
			length = C.strlen(data, 0);
		}
		return length;
	}
	
	public long getDataPtr() {
		return data;
	}
	
	/**
	 * Returns the original String value used to construct this object.
	 */
	@Override
	public String toString() {
		return this.value;
	}
	
	@Override
	public void close() {
		if(data != 0) {
			C.free(data);
			data = 0;
		}
	}

}
