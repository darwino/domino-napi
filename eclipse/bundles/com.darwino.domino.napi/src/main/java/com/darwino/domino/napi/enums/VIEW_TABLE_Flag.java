/**
 * Copyright © 2014-2018 Darwino, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
