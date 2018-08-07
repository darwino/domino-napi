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
public enum ACLLevel implements INumberEnum<Short> {
	NOACCESS(DominoAPI.ACL_LEVEL_NOACCESS), DEPOSITOR(DominoAPI.ACL_LEVEL_DEPOSITOR),
	READER(DominoAPI.ACL_LEVEL_READER), AUTHOR(DominoAPI.ACL_LEVEL_AUTHOR),
	EDITOR(DominoAPI.ACL_LEVEL_EDITOR), DESIGNER(DominoAPI.ACL_LEVEL_DESIGNER),
	MANAGER(DominoAPI.ACL_LEVEL_MANAGER);
	
	private final short value;
	
	private ACLLevel(short value) {
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
	
	public boolean isAtLeast(ACLLevel other) {
		return value >= other.value;
	}
	
	public boolean isGreaterThan(ACLLevel other) {
		return value > other.value;
	}
}
