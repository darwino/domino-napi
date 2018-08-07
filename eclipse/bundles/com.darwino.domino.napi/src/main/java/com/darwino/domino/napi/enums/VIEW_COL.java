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
