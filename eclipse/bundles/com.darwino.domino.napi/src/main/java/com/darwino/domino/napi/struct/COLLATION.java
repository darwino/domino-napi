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
public class COLLATION extends BaseStruct {
	static {
		int[] sizes = new int[5];
		initNative(sizes);
		sizeOf = sizes[0];
		_BufferSize = sizes[1];
		_Items = sizes[2];
		_Flags = sizes[3];
		_signature = sizes[4];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _BufferSize;
	public static final int _Items;
	public static final int _Flags;
	public static final int _signature;
	
	public COLLATION() {
		super(C.malloc(sizeOf), true);
	}
	public COLLATION(long data) {
		super(data, false);
	}

	public COLLATION(long data, boolean owned) {
		super(data, owned);
	}
	
	public short getBufferSize() { return _getUSHORT(_BufferSize); }
	public void setBufferSize(short bufferSize) { _setUSHORT(_BufferSize, bufferSize); }
	
	public short getItems() { return _getUSHORT(_Items); }
	public void setItems(short items) { _setUSHORT(_Items, items); }
	
	public byte getFlags() { return _getBYTE(_Flags); }
	public void setFlags(byte flags) { _setBYTE(_Flags, flags); }
	
	public byte getSignature() { return _getBYTE(_signature); }
	public void setSignature(byte signature) { _setBYTE(_signature, signature); }
}
