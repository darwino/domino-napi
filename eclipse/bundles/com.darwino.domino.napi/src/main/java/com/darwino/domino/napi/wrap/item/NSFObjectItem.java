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
