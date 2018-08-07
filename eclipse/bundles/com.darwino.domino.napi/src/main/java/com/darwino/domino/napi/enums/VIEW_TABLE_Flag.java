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
 * VIEW_TABLE_FORMAT Flags field (viewfmt.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 */
public enum VIEW_TABLE_Flag implements INumberEnum<Short> {
	COLLAPSED(DominoAPI.VIEW_TABLE_FLAG_COLLAPSED), FLATINDEX(DominoAPI.VIEW_TABLE_FLAG_FLATINDEX),
	DISP_ALLUNREAD(DominoAPI.VIEW_TABLE_FLAG_DISP_ALLUNREAD), CONFLICT(DominoAPI.VIEW_TABLE_FLAG_CONFLICT),
	DISP_UNREADDOCS(DominoAPI.VIEW_TABLE_FLAG_DISP_UNREADDOCS), GOTO_TOP_ON_OPEN(DominoAPI.VIEW_TABLE_GOTO_TOP_ON_OPEN),
	GOTO_BOTTOM_ON_OPEN(DominoAPI.VIEW_TABLE_GOTO_TOP_ON_OPEN), ALTERNATE_ROW_COLORING(DominoAPI.VIEW_TABLE_ALTERNATE_ROW_COLORING),
	HIDE_HEADINGS(DominoAPI.VIEW_TABLE_HIDE_HEADINGS), HIDE_LEFT_MARGIN(DominoAPI.VIEW_TABLE_HIDE_LEFT_MARGIN),
	SIMPLE_HEADINGS(DominoAPI.VIEW_TABLE_SIMPLE_HEADINGS), VARIABLE_LINE_COUNT(DominoAPI.VIEW_TABLE_VARIABLE_LINE_COUNT),
	GOTO_TOP_ON_REFRESH(DominoAPI.VIEW_TABLE_GOTO_TOP_ON_REFRESH), GOTO_BOTTOM_ON_REFRESH(DominoAPI.VIEW_TABLE_GOTO_BOTTOM_ON_REFRESH),
	EXTEND_LAST_COLUMN(DominoAPI.VIEW_TABLE_EXTEND_LAST_COLUMN), RTLVIEW(DominoAPI.VIEW_TABLE_RTLVIEW);
	
	short value;
	
	private VIEW_TABLE_Flag(short value) {
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
