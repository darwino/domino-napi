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
package com.darwino.domino.napi.struct;

import com.darwino.domino.napi.c.C;

/**
 * (nsfdata.h)
 * 
 * @author Jesse Gallagher
 *
 */
public class ITEM extends BaseStruct {
	static {
		int[] sizes = new int[3];
		initNative(sizes);
		sizeOf = sizes[0];
		_NameLength = sizes[1];
		_ValueLength = sizes[2];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _NameLength;
	public static final int _ValueLength;
	
	public ITEM() {
		super(C.malloc(sizeOf), true);
	}
	public ITEM(long data) {
		super(data, false);
	}

	public ITEM(long data, boolean owned) {
		super(data, owned);
	}
	
	public short getNameLength() { return _getUSHORT(_NameLength); }
	public void setNameLength(short nameLength) { _setUSHORT(_NameLength, nameLength); }

	public short getValueLength() { return _getUSHORT(_ValueLength); }
	public void setValueLength(short valueLength) { _setUSHORT(_ValueLength, valueLength); }
}
