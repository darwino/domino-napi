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
 * (htmlapi.h)
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public enum FIELD_LIMIT_TYPE implements INumberEnum<Integer> {
	PICTURE(DominoAPI.FIELD_LIMIT_TYPE_PICTURE), APPLET(DominoAPI.FIELD_LIMIT_TYPE_APPLET),
	SHAREDIMAGE(DominoAPI.FIELD_LIMIT_TYPE_SHAREDIMAGE), OBJECT(DominoAPI.FIELD_LIMIT_TYPE_OBJECT),
	TEXTONLY(DominoAPI.FIELD_LIMIT_TYPE_TEXTONLY), VIEW(DominoAPI.FIELD_LIMIT_TYPE_VIEW),
	CALENDAR(DominoAPI.FIELD_LIMIT_TYPE_CALENDAR), INBOX(DominoAPI.FIELD_LIMIT_TYPE_INBOX),
	ATTACHMENT(DominoAPI.FIELD_LIMIT_TYPE_ATTACHMENT), DATEPICKER(DominoAPI.FIELD_LIMIT_TYPE_DATEPICKER),
	GRAPHIC(DominoAPI.FIELD_LIMIT_TYPE_GRAPHIC), HELP(DominoAPI.FIELD_LIMIT_TYPE_HELP),
	CLEAR(DominoAPI.FIELD_LIMIT_TYPE_CLEAR), LINK(DominoAPI.FIELD_LIMIT_TYPE_LINK),
	THUMBNAIL(DominoAPI.FIELD_LIMIT_TYPE_THUMBNAIL);
	
	private final int value;
	
	private FIELD_LIMIT_TYPE(int value) {
		this.value = value;
	}
	
	@Override
	public Integer getValue() {
		return value;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
}
