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
public enum FieldFlag implements INumberEnum<Short> {
	READWRITERS(DominoAPI.FREADWRITERS), EDITABLE(DominoAPI.FEDITABLE),
	NAMES(DominoAPI.FNAMES), STOREDV(DominoAPI.FSTOREDV), READERS(DominoAPI.FREADERS),
	SECTION(DominoAPI.FSECTION), SPARE3(DominoAPI.FSPARE3), V3FAB(DominoAPI.FV3FAB),
	COMPUTED(DominoAPI.FCOMPUTED), KEYWORDS(DominoAPI.FKEYWORDS), PROTECTED(DominoAPI.FPROTECTED),
	REFERENCE(DominoAPI.FREFERENCE), SIGN(DominoAPI.FSIGN), SEAL(DominoAPI.FSEAL),
	KEYWORDS_UI_STANDARD(DominoAPI.FKEYWORDS_UI_STANDARD),
	KEYWORDS_UI_CHECKBOX(DominoAPI.FKEYWORDS_UI_CHECKBOX),
	KEYWORDS_UI_RADIOBUTTON(DominoAPI.FKEYWORDS_UI_RADIOBUTTON),
	KEYWORDS_UI_ALLOW_NEW(DominoAPI.FKEYWORDS_UI_ALLOW_NEW);
	
	private short value;
	
	private FieldFlag(short value) {
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
