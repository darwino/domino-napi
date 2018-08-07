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

import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.c.C;

/**
 * 
 * @author Jesse Gallagher
 *
 */
public class TIMEDATE_PAIR extends BaseStruct implements TimeStruct  {

	static {
		int[] sizes = new int[3];
		initNative(sizes);
		sizeOf = sizes[0];
		_Lower = sizes[1];
		_Upper = sizes[2];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Lower;
	public static final int _Upper;
	
	public TIMEDATE_PAIR() {
		super(C.malloc(sizeOf), true);
	}
	public TIMEDATE_PAIR(long data) {
		super(data, false);
	}
	public TIMEDATE_PAIR(long data, boolean owned) {
		super(data, owned);
	}

	public TIMEDATE getLower() {
		long ptr = getField(_Lower);
		return new TIMEDATE(ptr);
	}
	public TIMEDATE getUpper() {
		long ptr = getField(_Upper);
		return new TIMEDATE(ptr);
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.BaseStruct#toString()
	 */
	@Override
	public String toString() {
		return StringUtil.format("[{0}: Lower={1}, Upper={2}]", //$NON-NLS-1$
				getClass().getSimpleName(),
				getLower(),
				getUpper()
		);
	}
}
