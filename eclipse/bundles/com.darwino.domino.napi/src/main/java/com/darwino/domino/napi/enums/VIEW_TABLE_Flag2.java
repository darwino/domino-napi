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
 * VIEW_TABLE_FORMAT Flags2 field (viewfmt.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 *
 */
public enum VIEW_TABLE_Flag2 implements INumberEnum<Short> {
	FLAT_HEADINGS(DominoAPI.VIEW_TABLE_FLAT_HEADINGS), COLORIZE_ICONS(DominoAPI.VIEW_TABLE_COLORIZE_ICONS),
	HIDE_SB(DominoAPI.VIEW_TABLE_HIDE_SB), HIDE_CAL_HEADER(DominoAPI.VIEW_TABLE_HIDE_CAL_HEADER),
	NOT_CUSTOMIZED(DominoAPI.VIEW_TABLE_NOT_CUSTOMIZED), SHOW_PARTIAL_THREADS(DominoAPI.VIEW_TABLE_SHOW_PARITAL_THREADS),
	PARTIAL_FLATINDEX(DominoAPI.VIEW_TABLE_FLAG_PARTIAL_FLATINDEX);
	
	short value;
	
	private VIEW_TABLE_Flag2(short value) {
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.enums.INumberEnum#getValue()
	 */
	@Override
	public Short getValue() {
		return value;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
}
