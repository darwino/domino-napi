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
package com.darwino.domino.napi.c;

import java.io.Closeable;

public class LMBCSString implements Closeable {

	private String value;
	private long data;
	private int length = -1;
	
	public LMBCSString(String value) {
		this.value = value;
		data = C.toLMBCSString(this.value);
	}
	
	public int length() {
		if(length < 0) {
			length = C.strlen(data, 0);
		}
		return length;
	}
	
	public long getDataPtr() {
		return data;
	}
	
	/**
	 * Returns the original String value used to construct this object.
	 */
	@Override
	public String toString() {
		return this.value;
	}
	
	@Override
	public void close() {
		if(data != 0) {
			C.free(data);
			data = 0;
		}
	}

}
