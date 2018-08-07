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
 * @author Jesse Gallagher
 * @since 1.5.0
 *
 */
public enum FieldType implements INumberEnum<Short> {
	ERROR(DominoAPI.FIELD_TYPE_ERROR), NUMBER(DominoAPI.FIELD_TYPE_NUMBER),
	TIME(DominoAPI.FIELD_TYPE_TIME), RICH_TEXT(DominoAPI.FIELD_TYPE_RICH_TEXT),
	AUTHORS(DominoAPI.FIELD_TYPE_AUTHORS), READERS(DominoAPI.FIELD_TYPE_READERS),
	NAMES(DominoAPI.FIELD_TYPE_NAMES), KEYWORDS(DominoAPI.FIELD_TYPE_KEYWORDS),
	TEXT(DominoAPI.FIELD_TYPE_TEXT), SECTION(DominoAPI.FIELD_TYPE_SECTION),
	PASSWORD(DominoAPI.FIELD_TYPE_PASSWORD), FORMULA(DominoAPI.FIELD_TYPE_FORMULA),
	TIMEZONE(DominoAPI.FIELD_TYPE_TIMEZONE), COLORCTL(DominoAPI.FIELD_TYPE_COLORCTL);
	
	private short value;
	
	private FieldType(short value) {
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
