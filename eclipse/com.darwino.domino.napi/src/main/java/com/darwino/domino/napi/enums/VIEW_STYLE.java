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
 * (viewfmt.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 *
 */
public enum VIEW_STYLE implements INumberEnum<Byte> {
	TABLE(DominoAPI.VIEW_STYLE_TABLE), DAY(DominoAPI.VIEW_STYLE_DAY),
	WEEK(DominoAPI.VIEW_STYLE_WEEK), VIEW_STYLE_MONTH(DominoAPI.VIEW_STYLE_MONTH);
	
	byte value;
	
	private VIEW_STYLE(byte value) {
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.enums.INumberEnum#getValue()
	 */
	@Override
	public Byte getValue() {
		return value;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
}
