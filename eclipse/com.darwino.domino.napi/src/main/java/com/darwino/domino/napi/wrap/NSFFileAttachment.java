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
