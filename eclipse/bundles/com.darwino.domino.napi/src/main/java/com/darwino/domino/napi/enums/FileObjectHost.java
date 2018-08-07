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
 * HOST_xxx
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public enum FileObjectHost implements INumberEnum<Short> {
	MSDOS(DominoAPI.HOST_MSDOS), OLE(DominoAPI.HOST_OLE), MAC(DominoAPI.HOST_MAC), UNKNOWN(DominoAPI.HOST_UNKNOWN),
	HPFS(DominoAPI.HOST_HPFS), OLELIB(DominoAPI.HOST_OLELIB), BYTEARRAY_EXT(DominoAPI.HOST_BYTEARRAY_EXT),
	BYTEARRAY_PAGE(DominoAPI.HOST_BYTEARRAY_PAGE), CDSTORAGE(DominoAPI.HOST_CDSTORAGE), STREAM(DominoAPI.HOST_STREAM),
	LINK(DominoAPI.HOST_LINK);
	
	private final short value;
	
	private FileObjectHost(short value) {
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
