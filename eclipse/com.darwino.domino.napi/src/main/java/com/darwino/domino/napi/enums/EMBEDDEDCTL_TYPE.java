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
 * (editods.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 * @since Notes/Domino 5.0
 */
public enum EMBEDDEDCTL_TYPE implements INumberEnum<Short> {
	EDIT(DominoAPI.EMBEDDEDCTL_EDIT), COMBO(DominoAPI.EMBEDDEDCTL_COMBO),
	LIST(DominoAPI.EMBEDDEDCTL_LIST), TIME(DominoAPI.EMBEDDEDCTL_TIME),
	KEYGEN(DominoAPI.EMBEDDEDCTL_KEYGEN), FILE(DominoAPI.EMBEDDEDCTL_FILE),
	TIMEZONE(DominoAPI.EMBEDDEDCTL_TIMEZONE), COLOR(DominoAPI.EMBEDDEDCTL_COLOR);
	
	private final short value;
	
	private EMBEDDEDCTL_TYPE(short value) {
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
