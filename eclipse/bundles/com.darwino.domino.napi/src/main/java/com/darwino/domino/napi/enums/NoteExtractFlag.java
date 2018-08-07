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
 * @since 0.8.0
 *
 */
public enum NoteExtractFlag implements INumberEnum<Short> {
	RESONLY(DominoAPI.NTEXT_RESONLY), FTYPE_MASK(DominoAPI.NTEXT_FTYPE_MASK), FTYPE_FLAT(DominoAPI.NTEXT_FTYPE_FLAT),
	FTYPE_MACBIN(DominoAPI.NTEXT_FTYPE_MACBIN), RAWMIME(DominoAPI.NTEXT_RAWMIME), IGNORE_HUFF2(DominoAPI.NTEXT_IGNORE_HUFF2);
	
	private final short value;
	
	private NoteExtractFlag(short value) {
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
