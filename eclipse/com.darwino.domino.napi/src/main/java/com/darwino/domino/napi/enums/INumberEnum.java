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

package com.darwino.domino.napi.enums;

/**
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public abstract interface INumberEnum<T extends Number> {
	/**
	 * @return the C-level value of the enum 
	 */
	T getValue();
	
	/**
	 * @return the C-level value of the enum as a <code>long</code>
	 */
	long getLongValue();
}
