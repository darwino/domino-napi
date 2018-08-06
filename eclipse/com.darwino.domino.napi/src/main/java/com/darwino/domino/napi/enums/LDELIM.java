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
public enum LDELIM implements INumberEnum<Short> {
	SPACE(DominoAPI.LDELIM_SPACE), COMMA(DominoAPI.LDELIM_COMMA),
	SEMICOLON(DominoAPI.LDELIM_SEMICOLON), NEWLINE(DominoAPI.LDELIM_NEWLINE),
	BLANKLINE(DominoAPI.LDELIM_BLANKLINE);
	
	private short value;
	
	private LDELIM(short value) {
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
