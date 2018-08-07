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

package com.darwino.domino.napi.util;

/**
 * Utility class to pass objects by reference.
 */
public class Ref<T> {

	private T t;
	
	public Ref() {
	}
	
	public Ref(T t) {
		this.t= t;
	}
	
	public T get() {
		return t;
	}
	
	public void set(T t) {
		this.t = t;
	}
}