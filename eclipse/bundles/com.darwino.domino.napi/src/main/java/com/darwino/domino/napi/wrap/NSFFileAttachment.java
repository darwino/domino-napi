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

import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.wrap.item.NSFCompositeDataItem;
import com.darwino.domino.napi.wrap.item.NSFFileItem;

/**
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public class NSFFileAttachment extends NSFBase {

	private final NSFCompositeDataItem parent;
	private final String internalFileName;
	private final String originalFileName;
	
	public NSFFileAttachment(NSFCompositeDataItem parent, String internalFileName, String originalFileName) {
		super(parent.getAPI());
		
		this.parent = parent;
		this.internalFileName = internalFileName;
		this.originalFileName = originalFileName;
	}
	
	/**
	 * @return the parent {@link NSFCompositeDataItem}
	 */
	@Override
	public NSFCompositeDataItem getParent() {
		return parent;
	}

	/**
	 * @return the internal file name Domino uses to store the attachment
	 */
	public String getInternalFileName() {
		return internalFileName;
	}
	
	/**
	 * @return the original file name of the attachment
	 */
	public String getOriginalFileName() {
		return originalFileName;
	}
	
	/**
	 * @return the {@link NSFFileItem} linked by this attachment, or <code>null</code> if the item does not exist in the note
	 * @throws DominoException if there is a problem searching for the file
	 */
	public NSFFileItem getFileItem() throws DominoException {
		// TODO make this not so wasteful
		NSFFileItem result = null;
		for(NSFItem item : getParent().getParent().getAllItems(DominoAPI.ITEM_NAME_ATTACHMENT)) {
			if(item instanceof NSFFileItem) {
				String fileName = ((NSFFileItem)item).getFileName();
				if(StringUtil.equals(fileName, internalFileName)) {
					result = ((NSFFileItem)item);
					break;
				} else {
					item.free();
				}
			}
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#doFree()
	 */
	@Override
	protected void doFree() {
		// NOP
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#isRefValid()
	 */
	@Override
	public boolean isRefValid() {
		return true;
	}
}
