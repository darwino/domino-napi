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
 * Values for the FormatDataType field of VIEW_COLUMN_FORMAT (viewfmt.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 *
 */
public enum VIEW_COL implements INumberEnum<Short> {
	NUMBER(DominoAPI.VIEW_COL_NUMBER), TIMEDTAE(DominoAPI.VIEW_COL_TIMEDATE),
	TEXT(DominoAPI.VIEW_COL_TEXT);
	
	short value;
	
	private VIEW_COL(short value) {
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
