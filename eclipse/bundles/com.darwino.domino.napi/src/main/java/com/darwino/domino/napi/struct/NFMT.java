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
public class NFMT extends BaseStruct {
	static {
		int[] sizes = new int[5];
		initNative(sizes);
		sizeOf = sizes[0];
		_Digits = sizes[1];
		_Format = sizes[2];
		_Attributes = sizes[3];
		_Unused = sizes[4];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Digits;
	public static final int _Format;
	public static final int _Attributes;
	public static final int _Unused;
	
	public NFMT() {
		super(C.malloc(sizeOf), true);
	}
	public NFMT(long data) {
		super(data, false);
	}

	public NFMT(long data, boolean owned) {
		super(data, owned);
	}
	
	public byte getDigits() { return _getBYTE(_Digits); }
	public void setDigits(byte digits) { _setBYTE(_Digits, digits); }
	
	public byte getFormat() { return _getBYTE(_Format); }
	public void setFormat(byte format) { _setBYTE(_Format, format); }
	
	public byte getAttributes() { return _getBYTE(_Attributes); }
	public void setAttributes(byte attributes) { _setBYTE(_Attributes, attributes); }
}
