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
public class ShortRefPtr extends BaseShortRef {

	public long ptr;
	
	public ShortRefPtr(long ptr) {
		this.ptr = ptr;
	}
	
	@Override
	public short get() {
		return C.getShort(ptr, 0);
	}
	
	@Override
	public void set(short value) {
		C.setShort(ptr, 0, value);
	}	
}
