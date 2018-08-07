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
package com.darwino.domino.napi.wrap;

/**
 * This class represents the data pair contained within an item of type USERDATA:
 * the user-defined format name and the data as a byte array
 * 
 * @author Jesse Gallagher
 * @since 1.0.1
 */
public class NSFUserData {

	private final String formatName;
	private final byte[] data;
	
	/**
	 * Constructs a new <code>NSFUserData</code> using the provided format name and byte array
	 * of data. The byte array is referenced directly; it is <strong>not</strong> copied in memory.
	 */
	public NSFUserData(String formatName, byte[] data) {
		this.formatName = formatName;
		this.data = data;
	}

	public String getFormatName() {
		return formatName;
	}
	
	/**
	 * @return the data contained in this <code>NSFUserData</code> object. The internal byte array is
	 * 		returned directly; it is <strong>not</strong> copied in memory
	 */
	public byte[] getData() {
		return data;
	}
}
