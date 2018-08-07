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
 * 
 * @author Jesse Gallagher
 *
 */
public class COLLATE_DESCRIPTOR extends BaseStruct {
	static {
		int[] sizes = new int[6];
		initNative(sizes);
		sizeOf = sizes[0];
		_Flags = sizes[1];
		_signature = sizes[2];
		_keytype = sizes[3];
		_NameOffset = sizes[4];
		_NameLength = sizes[5];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Flags;
	public static final int _signature;
	public static final int _keytype;
	public static final int _NameOffset;
	public static final int _NameLength;
	
	public COLLATE_DESCRIPTOR() {
		super(C.malloc(sizeOf), true);
	}
	public COLLATE_DESCRIPTOR(long data) {
		super(data, false);
	}

	public COLLATE_DESCRIPTOR(long data, boolean owned) {
		super(data, owned);
	}
	
	public byte getFlags() { return _getBYTE(_Flags); }
	public void setFlags(byte flags) { _setBYTE(_Flags, flags); }
	
	public byte getSignature() { return _getBYTE( _signature); }
	public void setSignature(byte signature) { _setBYTE(_signature, signature); }
	
	public byte getKeyType() { return _getBYTE(_keytype); }
	public void setKeyType(byte keyType) { _setBYTE(_keytype, keyType); }
	
	public short getNameOffset() { return _getWORD(_NameOffset); }
	public void setNameOffset(short nameOffset) { _setWORD(_NameOffset, nameOffset); }
	
	public short getNameLength() { return _getWORD(_NameLength); }
	public void setNameLength(short nameLength) { _setWORD(_NameLength, nameLength); }
}
