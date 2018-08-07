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

import com.darwino.domino.napi.DominoAPI;

/**
 * (acl.h)
 * 
 * @author Jesse Gallagher
 * @since 2.0.0
 * @since Notes/Domino 4.0
 */
public enum ACLFlag implements INumberEnum<Integer> {
	UNIFORM_ACCESS(DominoAPI.ACL_UNIFORM_ACCESS), ADMIN_READERAUTHOR(DominoAPI.ACL_FLAG_ADMIN_READERAUTHOR);
	
	private final int value;
	
	private ACLFlag(int value) {
		this.value = value;
	}
	
	@Override
	public Integer getValue() {
		return value;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
}
