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


/**
 * Java Short reference.
 */
public class ByteRefPtr extends BaseByteRef {

	public long ptr;
	
	public ByteRefPtr(long ptr) {
		this.ptr = ptr;
	}
	
	@Override
	public byte get() {
		return C.getByte(ptr, 0);
	}
	
	@Override
	public void set(byte value) {
		C.setByte(ptr, 0, value);
	}	
}
