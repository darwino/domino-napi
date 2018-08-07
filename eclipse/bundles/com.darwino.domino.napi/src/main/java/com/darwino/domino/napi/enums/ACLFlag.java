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
 * (acl.h)
 * 
 * @author Jesse Gallagher
 * @since 2.0.0
 * @since Notes/Domino 4.0
 */
public enum ACLFlag implements INumberEnum<Integer> {
	UNIFORM_ACCESS(DominoAPI.ACL_UNIFORM_ACCESS), ADMIN_READERAUTHOR(DominoAPI.ACL_FLAG_ADMIN_READERAUTHOR);
	
	private final int value;
	
	private ACLFlag(int value) {
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
