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
 * Java Long reference.
 * 
 * @author priand
 */
public class LongRef extends BaseLongRef {

	public long value;
	
	public LongRef() {
	}

	public LongRef(long value) {
		this.value = value;
	}
	
	@Override
	public long get() {
		return value;
	}
	
	@Override
	public void set(long value) {
		this.value = value;
	}	
}
