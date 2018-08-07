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
