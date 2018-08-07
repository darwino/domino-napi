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
 * Java Int reference.
 * 
 * @author priand
 */
public class IntRefPtr extends BaseIntRef {

	public long ptr;
	
	public IntRefPtr(long ptr) {
		this.ptr = ptr;
	}
	
	@Override
	public int get() {
		return C.getInt(ptr, 0);
	}
	
	@Override
	public void set(int value) {
		C.setInt(ptr, 0, value);
	}	
}
