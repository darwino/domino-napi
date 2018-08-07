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
 * (editods.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 * @since Notes/Domino 5.0
 */
public enum EC_FLAG implements INumberEnum<Short> {
	UNITS(DominoAPI.EC_FLAG_UNITS), DIALOGUNITS(DominoAPI.EC_FLAG_DIALOGUNITS), FITTOCONTENTS(DominoAPI.EC_FLAG_FITTOCONTENTS),
	ALWAYSACTIVE(DominoAPI.EC_FLAG_ALWAYSACTIVE), FITTOWINDOW(DominoAPI.EC_FLAG_FITTOWINDOW),
	POSITION_TOP(DominoAPI.EC_FLAG_POSITION_TOP), POSITION_BOTTOM(DominoAPI.EC_FLAG_POSITION_BOTTOM),
	POSITION_ASCENT(DominoAPI.EC_FLAG_POSITION_ASCENT), POSITION_HEIGHT(DominoAPI.EC_FLAG_POSITION_HEIGHT);
	
	private final short value;
	
	private EC_FLAG(short value) {
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
