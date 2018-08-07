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
public enum CDGRAPHIC_VERSION implements INumberEnum<Byte> {
	/** Graphics Format 1 - created by Notes version 2 */
	VERSION1(DominoAPI.CDGRAPHIC_VERSION1),
	/** Graphics Format 2 - created by Notes version 3 */
	VERSION2(DominoAPI.CDGRAPHIC_VERSION2),
	/** Graphics Format 3 - created by Notes version 4.5 */
	VERSION3(DominoAPI.CDGRAPHIC_VERSION3);
	
	private final byte value;
	
	private CDGRAPHIC_VERSION(byte value) {
		this.value = value;
	}
	
	@Override
	public Byte getValue() {
		return value;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
}
