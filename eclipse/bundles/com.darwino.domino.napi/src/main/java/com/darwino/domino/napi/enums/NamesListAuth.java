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
public enum NamesListAuth implements INumberEnum<Short> {
	/** Set if names list has been authenticated via Notes */
	AUTHENTICATED(DominoAPI.NAMES_LIST_AUTHENTICATED),
	/** Set if names list has been authenticated using external password -- Triggers "Maximum Internet name & password" (set in the database ACL) access level allowed */
	PASSWORD_AUTHENTICATED(DominoAPI.NAMES_LIST_PASSWORD_AUTHENTICATED),
	/** Set if user requested full admin access and it was granted */
	FULL_ADMIN_ACCESS(DominoAPI.NAMES_LIST_FULL_ADMIN_ACCESS);
	
	private final short value;
	
	private NamesListAuth(short value) {
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
