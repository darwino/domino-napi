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
 *
 */
public enum LDDELIM implements INumberEnum<Short> {
	SPACE(DominoAPI.LDDELIM_SPACE), COMMA(DominoAPI.LDDELIM_COMMA),
	SEMICOLON(DominoAPI.LDDELIM_SEMICOLON), NEWLINE(DominoAPI.LDDELIM_NEWLINE),
	BLANKLINE(DominoAPI.LDDELIM_BLANKLINE);
	
	private short value;
	
	private LDDELIM(short value) {
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
