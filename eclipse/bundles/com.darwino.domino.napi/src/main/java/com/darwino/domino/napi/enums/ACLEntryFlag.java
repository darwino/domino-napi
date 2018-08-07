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
 * @since 2.0.0
 */
public enum ACLEntryFlag implements INumberEnum<Short> {
	AUTHOR_NOCREATE(DominoAPI.ACL_FLAG_AUTHOR_NOCREATE), SERVER(DominoAPI.ACL_FLAG_SERVER),
	NODELETE(DominoAPI.ACL_FLAG_NODELETE), CREATE_PRAGENT(DominoAPI.ACL_FLAG_CREATE_PRAGENT),
	CREATE_PRFOLDER(DominoAPI.ACL_FLAG_CREATE_PRFOLDER), PERSON(DominoAPI.ACL_FLAG_PERSON),
	GROUP(DominoAPI.ACL_FLAG_GROUP), CREATE_FOLDER(DominoAPI.ACL_FLAG_CREATE_FOLDER),
	CREATE_LOTUSSCRIPT(DominoAPI.ACL_FLAG_CREATE_LOTUSSCRIPT), PUBLICREADER(DominoAPI.ACL_FLAG_PUBLICREADER),
	PUBLICWRITER(DominoAPI.ACL_FLAG_PUBLICWRITER), MONITORS_DISALLOWED(DominoAPI.ACL_FLAG_MONITORS_DISALLOWED),
	NOREPLICATE(DominoAPI.ACL_FLAG_NOREPLICATE);
	
	private final short value;
	
	private ACLEntryFlag(short value) {
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
