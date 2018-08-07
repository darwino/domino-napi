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
 * DBCLASS_xxx (nsfdb.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 *
 */
public enum DBClass implements INumberEnum<Short> {
	BY_EXTENSION(DominoAPI.DBCLASS_BY_EXTENSION), NSFTESTFILE(DominoAPI.DBCLASS_NSFTESTFILE),
	NOTEFILE(DominoAPI.DBCLASS_NOTEFILE), DESKTOP(DominoAPI.DBCLASS_DESKTOP),
	NOTECLIPBOARD(DominoAPI.DBCLASS_NOTECLIPBOARD), TEMPLATEFILE(DominoAPI.DBCLASS_TEMPLATEFILE),
	GIANTNOTEFILE(DominoAPI.DBCLASS_GIANTNOTEFILE), HUGENOTEFILE(DominoAPI.DBCLASS_HUGENOTEFILE),
	ONEDOCFILE(DominoAPI.DBCLASS_ONEDOCFILE), V2NOTEFILE(DominoAPI.DBCLASS_V2NOTEFILE),
	ENCAPSMAILFILE(DominoAPI.DBCLASS_ENCAPSMAILFILE), LRGENCAPSMAILFILE(DominoAPI.DBCLASS_LRGENCAPSMAILFILE),
	OBJSTORE(DominoAPI.DBCLASS_OBJSTORE), V3NOTEFILE(DominoAPI.DBCLASS_V3NOTEFILE),
	V4NOTEFILE(DominoAPI.DBCLASS_V4NOTEFILE), V5NOTEFILE(DominoAPI.DBCLASS_V5NOTEFILE),
	V6NOTEFILE(DominoAPI.DBCLASS_V6NOTEFILE), V8NOTEFILE(DominoAPI.DBCLASS_V8NOTEFILE),
	V85NOTEFILE(DominoAPI.DBCLASS_V85NOTEFILE), V9NOTEFILE(DominoAPI.DBCLASS_V9NOTEFILE);
	
	private short value;
	
	private DBClass(short value) {
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
	
	/**
	 * @return whether the database class represents a normal (non-template, non-special-case)
	 * 		database
	 */
	public boolean isDatabase() {
		switch(this) {
		case BY_EXTENSION:
			// Has to be determined by the caller
			return false;
		case GIANTNOTEFILE:
		case HUGENOTEFILE:
		case NOTEFILE:
		case V2NOTEFILE:
		case V3NOTEFILE:
		case V4NOTEFILE:
		case V5NOTEFILE:
		case V6NOTEFILE:
		case V85NOTEFILE:
		case V8NOTEFILE:
		case V9NOTEFILE:
			return true;
		case TEMPLATEFILE:
			return false;
		case DESKTOP:
		case ENCAPSMAILFILE:
		case LRGENCAPSMAILFILE:
		case NOTECLIPBOARD:
		case NSFTESTFILE:
		case OBJSTORE:
		case ONEDOCFILE:
		default:
			return false;
		}
	}
}
