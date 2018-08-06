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
public enum EC_STYLE implements INumberEnum<Integer> {
	EDITMULTILINE(DominoAPI.EC_STYLE_EDITMULTILINE), EDITVSCROLL(DominoAPI.EC_STYLE_EDITVSCROLL),
	EDITPASSWORD(DominoAPI.EC_STYLE_EDITPASSWORD), EDITCOMBO(DominoAPI.EC_STYLE_EDITCOMBO),
	LISTMULTISEL(DominoAPI.EC_STYLE_LISTMULTISEL), CALENDAR(DominoAPI.EC_STYLE_CALENDAR),
	TIME(DominoAPI.EC_STYLE_TIME), DURATION(DominoAPI.EC_STYLE_DURATION),
	TIMEZONE(DominoAPI.EC_STYLE_TIMEZONE);
	
	private final int value;
	
	private EC_STYLE(int value) {
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
