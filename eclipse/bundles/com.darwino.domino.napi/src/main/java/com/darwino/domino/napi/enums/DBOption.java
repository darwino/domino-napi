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
 * DBOPTION_xxx (nsfdb.h)
 * 
 * @author Jesse Gallagher
 * @since 2.1.0
 *
 */
public enum DBOption implements INumberEnum<Integer> {
	FT_INDEX(DominoAPI.DBOPTION_FT_INDEX),
	IS_OBJSTORE(DominoAPI.DBOPTION_IS_OBJSTORE),
	USES_OBJSTORE(DominoAPI.DBOPTION_USES_OBJSTORE),
	OBJSTORE_NEVER(DominoAPI.DBOPTION_OBJSTORE_NEVER),
	IS_LIBRARY(DominoAPI.DBOPTION_IS_LIBRARY),
	UNIFORM_ACCESS(DominoAPI.DBOPTION_UNIFORM_ACCESS),
	OBJSTORE_ALWAYS(DominoAPI.DBOPTION_OBJSTORE_ALWAYS),
	NO_BGAGENT(DominoAPI.DBOPTION_NO_BGAGENT),
	OUT_OF_SERVICE(DominoAPI.DBOPTION_OUT_OF_SERVICE),
	IS_PERSONALJOURNAL(DominoAPI.DBOPTION_IS_PERSONALJOURNAL),
	MARKED_FOR_DELETE(DominoAPI.DBOPTION_MARKED_FOR_DELETE),
	HAS_CALENDAR(DominoAPI.DBOPTION_HAS_CALENDAR),
	IS_CATALOG_INDEX(DominoAPI.DBOPTION_IS_CATALOG_INDEX),
	IS_ADDRESS_BOOK(DominoAPI.DBOPTION_IS_ADDRESS_BOOK),
	IS_SEARCH_SCOPE(DominoAPI.DBOPTION_IS_SEARCH_SCOPE),
	IS_UA_CONFIDENTIAL(DominoAPI.DBOPTION_IS_UA_CONFIDENTIAL),
	RARELY_USED_NAMES(DominoAPI.DBOPTION_RARELY_USED_NAMES),
	IS_SITEDB(DominoAPI.DBOPTION_IS_SITEDB);
	
	private int value;
	
	private DBOption(int value) {
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
