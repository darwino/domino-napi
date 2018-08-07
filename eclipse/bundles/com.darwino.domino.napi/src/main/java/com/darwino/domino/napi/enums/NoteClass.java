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
 * This enum represents the <code>NOTE_CLASS_xxx</code> values, other than the "mask" ones. (nsfnote.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.1
 */
public enum NoteClass implements INumberEnum<Short> {
	DOCUMENT(DominoAPI.NOTE_CLASS_DOCUMENT), DATA(DominoAPI.NOTE_CLASS_DATA), INFO(DominoAPI.NOTE_CLASS_INFO),
	FORM(DominoAPI.NOTE_CLASS_FORM), VIEW(DominoAPI.NOTE_CLASS_VIEW), ICON(DominoAPI.NOTE_CLASS_ICON),
	/** This use of "design" refers to the special view containing the list of design notes, not a design note mask */
	DESIGN(DominoAPI.NOTE_CLASS_DESIGN),
	ACL(DominoAPI.NOTE_CLASS_ACL), HELP_INDEX(DominoAPI.NOTE_CLASS_HELP_INDEX), HELP(DominoAPI.NOTE_CLASS_HELP),
	FILTER(DominoAPI.NOTE_CLASS_FILTER), FIELD(DominoAPI.NOTE_CLASS_FIELD), REPLFORMULA(DominoAPI.NOTE_CLASS_REPLFORMULA),
	PRIVATE(DominoAPI.NOTE_CLASS_PRIVATE);
	
	private final short value;
	
	private NoteClass(short value) {
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
