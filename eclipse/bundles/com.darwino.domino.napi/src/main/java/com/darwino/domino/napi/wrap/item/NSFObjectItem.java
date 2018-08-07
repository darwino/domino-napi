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
package com.darwino.domino.napi.wrap.item;

import java.io.InputStream;

import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.enums.ValueType;
import com.darwino.domino.napi.struct.BLOCKID;
import com.darwino.domino.napi.struct.OBJECT_DESCRIPTOR;
import com.darwino.domino.napi.wrap.NSFItem;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.io.NSFObjectInputStream;

/**
 * This subclass of {@link NSFItem} represents items of type <code>TYPE_OBJECT</code>.
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public class NSFObjectItem extends NSFItem {

	/**
	 * @param parent
	 * @param name
	 * @param itemBlockId
	 * @param dataType
	 * @param valueBlockId
	 * @param valueLen
	 */
	public NSFObjectItem(NSFNote parent, String name, BLOCKID itemBlockId, ValueType dataType, BLOCKID valueBlockId, int valueLen) {
		super(parent, name, itemBlockId, dataType, valueBlockId, valueLen);
	}
	
	public NSFObjectItem(NSFItem existing) {
		super(existing);
	}
	
	public OBJECT_DESCRIPTOR getObjectDescriptor() throws DominoException {
		return getValue(OBJECT_DESCRIPTOR.class);
	}

	/**
	 * Returns an {@link InputStream} for the raw data represented by this object.
	 * 
	 * <p>Note, however, that it does not decompress the data in those objects - if the object is stored with LZ1
	 * or Huffman coding, the data returned by this <code>InputStream</code> will also be compressed.</p>
	 * 
	 * @return an {@link InputStream} for the raw data represented by this object
	 * @throws DominoException if there is a problem retrieving the object information from the database
	 */
	public InputStream getInputStream() throws DominoException {
		OBJECT_DESCRIPTOR descriptor = getObjectDescriptor();
		try {
			int rrv = descriptor.getRRV();
			return new NSFObjectInputStream(api, getParent().getParent().getHandle(), rrv, descriptor.getObjectType());
		} finally {
			descriptor.free();
		}
	}
}
