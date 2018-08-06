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
public enum ItemFlag implements INumberEnum<Short> {
	SIGN(DominoAPI.ITEM_SIGN), SEAL(DominoAPI.ITEM_SEAL), SUMMARY(DominoAPI.ITEM_SUMMARY),
	READWRITERS(DominoAPI.ITEM_READWRITERS), NAMES(DominoAPI.ITEM_NAMES),
	PLACEHOLDER(DominoAPI.ITEM_PLACEHOLDER), PROTECTED(DominoAPI.ITEM_PROTECTED),
	READERS(DominoAPI.ITEM_READERS), UNCHANGED(DominoAPI.ITEM_UNCHANGED);
	
	private short value;
	
	private ItemFlag(short value) {
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
