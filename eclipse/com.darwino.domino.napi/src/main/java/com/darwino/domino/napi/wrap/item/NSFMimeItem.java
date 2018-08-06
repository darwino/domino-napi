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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.mail.internet.ContentType;
import javax.mail.internet.MimeUtility;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.StreamUtil;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.enums.MIMEPartFlag;
import com.darwino.domino.napi.enums.ValueType;
import com.darwino.domino.napi.struct.BLOCKID;
import com.darwino.domino.napi.struct.MIME_PART;
import com.darwino.domino.napi.util.DominoNativeUtils;
import com.darwino.domino.napi.wrap.NSFItem;
import com.darwino.domino.napi.wrap.NSFNote;

/**
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public class NSFMimeItem extends NSFItem {

	/**
	 * Constructs a new <code>NSFMIMEItem</code> object for the provided note and item name. The item will be the
	 * first item in the note with the provided name.
	 * 
	 * @param parent the <code>NSFNote</code> containing the item
	 * @param itemName the name of the item
	 * @param itemBlockId the <code>itemBlockId</code> returned by <code>NSFItemInfo</code>
	 * @param dataType the {@link ValueType} enum corresponding to the type returned by <code>NSFItemInfo</code>
	 * @param valueBlockId the <code>valueBlockId</code> returned by <code>NSFItemInfo</code>
	 * @param valueLen the <code>valueLen</code> returned by <code>NSFItemInfo</code>
	 */
	public NSFMimeItem(NSFNote parent, String name, BLOCKID itemBlockId, ValueType dataType, BLOCKID valueBlockId, int valueLen) {
		super(parent, name, itemBlockId, dataType, valueBlockId, valueLen);
	}
	
	/**
	 * Returns the value of this item as a {@link MIME_PART} struct. It is the responsibility of the caller
	 * to free this struct when done with it.
	 */
	public MIME_PART getMimePart() throws DominoException {
		return getValue(MIME_PART.class);
	}

	/**
	 * This method will return the data as a byte array.
	 * 
	 * <p>Note that this will return the data from a referenced attachment if applicable,
	 * which could create a very large byte array in memory.</p>
	 * 
	 * @return the data
	 * @throws DominoException 
	 * @throws IOException if the MIME item is backed by an attachment and there is a problem extracting
	 * 		the attachment data
	 * @throws IllegalStateException if the MIME_PART indicates that its data is in a DB object,
	 * 		but lacks information to retrieve it
	 */
	public byte[] getDataBytes() throws DominoException, IOException {
		MIME_PART part = getMimePart();
		if(part.getFlags().contains(MIMEPartFlag.BODY_IN_DBOBJECT)) {
			InputStream is = getInputStream();
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				StreamUtil.copyStream(is, baos);
				return baos.toByteArray();
			} finally {
				StreamUtil.close(is);
			}
		} else {
			return part.getBodyData();
		}
	}
	
	/**
	 * This method will return an {@link InputStream} of the MIME part's data.
	 * 
	 * <p>Note that this may be backed by a temporary file in the case of attachment-stored MIME data.
	 * If it is, closing the input stream will delete the file.</p>
	 * 
	 * @return the data
	 * @throws DominoException 
	 * @throws IOException if the MIME item is backed by an attachment and there is a problem extracting
	 * 		the attachment data
	 * @throws IllegalStateException if the MIME_PART indicates that its data is in a DB object,
	 * 		but lacks information to retrieve it
	 */
	public InputStream getInputStream() throws DominoException, IOException {
		MIME_PART part = getMimePart();
		
		// TODO change to NSFIsMimePartInFile(?)
		if(part.getFlags().contains(MIMEPartFlag.BODY_IN_DBOBJECT)) {
			// Then we'll need to find the right attachment from the parent note
			
			// Read the file name from the item content
			String fileName = part.getBodyLMBCS();
			
			// Now find the appropriate file item
			NSFItem fileItem = getParent().getFirstItem("$FILE"); //$NON-NLS-1$
			if(!(fileItem instanceof NSFFileItem)) {
				throw new IllegalStateException(StringUtil.format("Item $FILE in note {0} is not a file item", getParent().getUniversalID())); //$NON-NLS-1$
			}
			int breaker = 0;
			while(fileItem != null) {
				String itemFileName = ((NSFFileItem)fileItem).getFileName();
				if(StringUtil.equals(itemFileName, fileName)) {
					break;
				}
				
				NSFItem tempItem = fileItem;
				fileItem = getParent().getNextItem(fileItem);
				tempItem.free();
				
				if(breaker++ > 1000) {
					throw new RuntimeException("Probable infinite loop detected");
				}
			}
			
			if(fileItem == null) {
				throw new IllegalStateException(StringUtil.format("Note {0} does not contain a $FILE item for name {1}", getParent().getUniversalID(), fileName)); //$NON-NLS-1$
			}
			
			return ((NSFFileItem)fileItem).getTempFileInputStream();
		} else {
			byte[] bodyData = part.getBodyData();
			return new ByteArrayInputStream(bodyData);
		}
	}
	
	/**
	 * This method will return the body of the MIME entity as a String, using the in-header encoding
	 * if present and UTF-8 otherwise.
	 * 
	 * @return the body as a String, using the in-header encoding if present and UTF-8 otherwise
	 * @throws IOException if the MIME item is backed by an attachment and there is a problem extracting
	 * 		the attachment data
	 */
	public String getDataAsString() throws DominoException, IOException {
		MIME_PART part = getMimePart();
		
		ContentType contentType = part.getContentType();
		Charset charset = null;
		if(contentType != null) {
			String charsetParam = contentType.getParameter("charset"); //$NON-NLS-1$
			if(StringUtil.isNotEmpty(charsetParam)) {
				charset = Charset.forName(MimeUtility.javaCharset(charsetParam));
			}
		}
		if(charset == null) {
			charset = DominoNativeUtils.UTF_8;
		}
		
		// Convert any Windows-isms to Unix-style for consistency
		String result = new String(getDataBytes(), charset);
		return result.replace("\r\n", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
