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
public class COLLECTIONSTATS extends BaseStruct {
	static {
		int[] sizes = new int[3];
		initNative(sizes);
		sizeOf = sizes[0];
		_TopLevelEntries = sizes[1];
		_LastModifiedTime = sizes[2];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _TopLevelEntries;
	public static final int _LastModifiedTime;
	
	public COLLECTIONSTATS() {
		super(C.malloc(sizeOf), true);
	}
	public COLLECTIONSTATS(long data) {
		super(data, false);
	}

	public COLLECTIONSTATS(long data, boolean owned) {
		super(data, owned);
	}
	
	public int getTopLevelEntries() { return _getDWORD(_TopLevelEntries); }
	public void setTopLevelEntries(int topLevelEntries) { _setDWORD(_TopLevelEntries, topLevelEntries); }
	
	public int getLastModifiedTime() { return _getDWORD(_LastModifiedTime); }
	public void setLastModifiedTime(int lastModifiedTime) { _setDWORD(_LastModifiedTime, lastModifiedTime); }
}
