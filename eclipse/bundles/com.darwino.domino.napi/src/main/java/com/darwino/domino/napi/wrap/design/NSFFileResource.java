/*!COPYRIGHT HEADER! - CONFIDENTIAL 
 *
 * Darwino Inc Confidential.
 *
 * (c) Copyright Darwino Inc. 2014-2018.
 *
 * Notice: The information contained in the source code for these files is the property 
 * of Darwino Inc. which, with its licensors, if any, owns all the intellectual property 
 * rights, including all copyright rights thereto.  Such information may only be used 
 * for debugging, troubleshooting and informational purposes.  All other uses of this information, 
 * including any production or commercial uses, are prohibited. 
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
