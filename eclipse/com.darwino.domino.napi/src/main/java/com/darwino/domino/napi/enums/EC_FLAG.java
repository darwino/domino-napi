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
public enum EC_FLAG implements INumberEnum<Short> {
	UNITS(DominoAPI.EC_FLAG_UNITS), DIALOGUNITS(DominoAPI.EC_FLAG_DIALOGUNITS), FITTOCONTENTS(DominoAPI.EC_FLAG_FITTOCONTENTS),
	ALWAYSACTIVE(DominoAPI.EC_FLAG_ALWAYSACTIVE), FITTOWINDOW(DominoAPI.EC_FLAG_FITTOWINDOW),
	POSITION_TOP(DominoAPI.EC_FLAG_POSITION_TOP), POSITION_BOTTOM(DominoAPI.EC_FLAG_POSITION_BOTTOM),
	POSITION_ASCENT(DominoAPI.EC_FLAG_POSITION_ASCENT), POSITION_HEIGHT(DominoAPI.EC_FLAG_POSITION_HEIGHT);
	
	private final short value;
	
	private EC_FLAG(short value) {
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
