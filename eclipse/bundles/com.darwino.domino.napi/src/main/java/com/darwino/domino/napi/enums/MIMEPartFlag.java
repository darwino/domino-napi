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
public enum MIMEPartFlag implements INumberEnum<Integer> {
	HAS_BOUNDARY(DominoAPI.MIME_PART_HAS_BOUNDARY), HAS_HEADERS(DominoAPI.MIME_PART_HAS_HEADERS),
	BODY_IN_DBOBJECT(DominoAPI.MIME_PART_BODY_IN_DBOBJECT), SHARED_DBOBJECT(DominoAPI.MIME_PART_SHARED_DBOBJECT),
	SKIP_FOR_CONVERSION(DominoAPI.MIME_PART_SKIP_FOR_CONVERSION);
	
	private int value;
	
	private MIMEPartFlag(int value) {
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
