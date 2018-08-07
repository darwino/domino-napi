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
 * FEXT_xxx for the Flags1 field of CDEXTFIELD (editods.h)
 * 
 * <p>This enum does not cover the mask and shift values.</p>
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 * @since Notes/Domino 4.0
 */
public enum FEXT1 implements INumberEnum<Integer> {
	LOOKUP_EACHCHAR(DominoAPI.FEXT_LOOKUP_EACHCHAR), KWSELRECALC(DominoAPI.FEXT_KWSELRECALC),
	KWHINKYMINKY(DominoAPI.FEXT_KWHINKYMINKY), AFTERVALIDATION(DominoAPI.FEXT_AFTERVALIDATION),
	ACCEPT_CARET(DominoAPI.FEXT_ACCEPT_CARET), KEYWORD_FRAME_3D(DominoAPI.FEXT_KEYWORD_FRAME_3D),
	KEYWORD_FRAME_STANDARD(DominoAPI.FEXT_KEYWORD_FRAME_STANDARD),
	KEYWORD_FRAME_NONE(DominoAPI.FEXT_KEYWORD_FRAME_NONE), KEYWORDS_UI_COMBO(DominoAPI.FEXT_KEYWORDS_UI_COMBO),
	KEYWORDS_UI_LIST(DominoAPI.FEXT_KEYWORDS_UI_LIST);
	
	private final int value;
	
	private FEXT1(int value) {
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
