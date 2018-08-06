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
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public enum NamesListAuth implements INumberEnum<Short> {
	/** Set if names list has been authenticated via Notes */
	AUTHENTICATED(DominoAPI.NAMES_LIST_AUTHENTICATED),
	/** Set if names list has been authenticated using external password -- Triggers "Maximum Internet name & password" (set in the database ACL) access level allowed */
	PASSWORD_AUTHENTICATED(DominoAPI.NAMES_LIST_PASSWORD_AUTHENTICATED),
	/** Set if user requested full admin access and it was granted */
	FULL_ADMIN_ACCESS(DominoAPI.NAMES_LIST_FULL_ADMIN_ACCESS);
	
	private final short value;
	
	private NamesListAuth(short value) {
		this.value = value;
	}
	
	@Override
	public Short getValue() {
		return value;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
}
