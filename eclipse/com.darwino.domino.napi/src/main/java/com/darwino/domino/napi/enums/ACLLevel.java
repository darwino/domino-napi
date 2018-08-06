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
 * @since 1.5.0
 *
 */
public enum ACLLevel implements INumberEnum<Short> {
	NOACCESS(DominoAPI.ACL_LEVEL_NOACCESS), DEPOSITOR(DominoAPI.ACL_LEVEL_DEPOSITOR),
	READER(DominoAPI.ACL_LEVEL_READER), AUTHOR(DominoAPI.ACL_LEVEL_AUTHOR),
	EDITOR(DominoAPI.ACL_LEVEL_EDITOR), DESIGNER(DominoAPI.ACL_LEVEL_DESIGNER),
	MANAGER(DominoAPI.ACL_LEVEL_MANAGER);
	
	private final short value;
	
	private ACLLevel(short value) {
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
	
	public boolean isAtLeast(ACLLevel other) {
		return value >= other.value;
	}
	
	public boolean isGreaterThan(ACLLevel other) {
		return value > other.value;
	}
}
