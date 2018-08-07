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
package com.darwino.domino.napi.wrap.design;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.item.NSFCompositeData;

/**
 * Represents a file-resource-type note in the database. This can be an actual file resource,
 * a CSS file, an image resource, or an XPage-related artifact (Java classes, Jars, xsp.properties,
 * etc.)
 *  
 * @author Jesse Gallagher
 * @since 2.0.0
 */
public class NSFFileResource extends NSFDesignNoteBase {

	public NSFFileResource(NSFDatabase parent, int noteId) {
		super(parent, noteId);
	}

	/**
	 * Returns the file data for this file resource as a byte array.
	 * 
	 * @return a byte array containing the file data, or <code>null</code> if the note
	 * 			does not contain file data
	 * @throws DominoException if there is a lower-level-API exception extracting the file data 
	 */
	public byte[] getFileData() throws DominoException {
		if(!getNote().hasItem(DominoAPI.ITEM_NAME_FILE_DATA)) {
			return null;
		}
		
		NSFCompositeData cd = getNote().getCompositeData(DominoAPI.ITEM_NAME_FILE_DATA);
		try {
			return cd.getFileResourceData();
		} finally {
			cd.free();
		}
	}
}
