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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.darwino.domino.napi.DominoException;

/**
 * This class provides operations to deal with one or more {@link NSFCompositeDataItem}s in a document
 * in a unified way. 
 * 
 * @author Jesse Gallagher
 * @since 2.0.0
 */
public class NSFCompositeData {

	private final List<NSFCompositeDataItem> items;
	private boolean freed = false;
	
	public NSFCompositeData(Collection<NSFCompositeDataItem> items) {
		if(items == null || items.isEmpty()) {
			throw new IllegalArgumentException("Cannot create a Composite Data view with an empty item list");
		}
		this.items = new ArrayList<NSFCompositeDataItem>(items);
	}

	/**
	 * Returns the file data for a <code>CDFILEHEADER</code>+<code>CDFILESEGMENT</code>-type design element.
	 * 
	 * <p>This will <strong>only</strong> work with those types of elements and will not return e.g. file attachment
	 * data.</p>
	 * 
	 * @return a byte array of the file resource data
	 * @throws DominoException if there is an underlying API problem reading the file data
	 */
	public byte[] getFileResourceData() throws DominoException {
		if(isFreed()) {
			throw new IllegalStateException("Cannot perform operations on a freed object");
		}
		
		NSFCompositeDataItem first = items.get(0);
		long size = first.getFileResourceSize();
		if(size > Integer.MAX_VALUE) {
			// Probably not possible for an NSF, but better cover this base just in case
			throw new IndexOutOfBoundsException("Cannot allocate a buffer to fit file of size " + size);
		}
		
		byte[] result = new byte[(int)size];
		int written = 0;
		for(NSFCompositeDataItem item : items) {
			byte[] slice = item.getFileResourceData();
			System.arraycopy(slice, 0, result, written, slice.length);
			written += slice.length;
		}
		
		return result;
	}
	
	public void free() {
		for(NSFCompositeDataItem item : items) {
			item.free();
		}
		freed = true;
	}
	
	public boolean isFreed() {
		return freed;
	}
}
