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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.ValueType;
import com.darwino.domino.napi.struct.BLOCKID;
import com.darwino.domino.napi.struct.FILEOBJECT;
import com.darwino.domino.napi.struct.OBJECT_DESCRIPTOR;
import com.darwino.domino.napi.wrap.NSFItem;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.io.NSFTempFileInputStream;

/**
 * This subclass of {@link NSFItem} represents items of type <code>TYPE_OBJECT</code>, specifically
 * those that are file attachments.
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 */
public class NSFFileItem extends NSFObjectItem {

	/**
	 * @param parent
	 * @param name
	 * @param itemBlockId
	 * @param dataType
	 * @param valueBlockId
	 * @param valueLen
	 */
	public NSFFileItem(NSFNote parent, String name, BLOCKID itemBlockId, ValueType dataType, BLOCKID valueBlockId, int valueLen) {
		super(parent, name, itemBlockId, dataType, valueBlockId, valueLen);
	}
	
	public NSFFileItem(NSFItem existing) {
		super(existing);
	}

	public FILEOBJECT getFileObject() throws DominoException {
		return getValue(FILEOBJECT.class);
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.item.NSFObjectItem#getObjectDescriptor()
	 */
	@Override
	public OBJECT_DESCRIPTOR getObjectDescriptor() throws DominoException {
		FILEOBJECT fileObject = getFileObject();
		OBJECT_DESCRIPTOR result = null;
		try {
			OBJECT_DESCRIPTOR source = fileObject.getHeader();
			result = new OBJECT_DESCRIPTOR();
			C.memcpy(result.getDataPtr(), 0, source.getDataPtr(), 0, OBJECT_DESCRIPTOR.sizeOf);
		} finally {
			fileObject.free();	
		}
		return result;
	}
	
	/**
	 * Returns an {@link InputStream} for the raw data represented by this object, extracted as a temporary file
	 * on the filesystem. When this stream is closed or on clean JVM exit, the temporary file will be deleted.
	 * 
	 * @return an {@link InputStream} for the raw data represented by this object
	 * @throws DominoException if there is a problem retrieving the object information from the database
	 * @throws IOException if there is a problem creating the temporary file
	 */
	public NSFTempFileInputStream getTempFileInputStream() throws DominoException, IOException {
		String fileName = getFileName();
		String ext = null;
		String baseName = null;
		if(fileName.contains(".")) { //$NON-NLS-1$
			ext = fileName.substring(fileName.lastIndexOf(".")); //$NON-NLS-1$
			baseName = fileName.substring(0, fileName.lastIndexOf(".")); //$NON-NLS-1$
		} else {
			ext = ".dat"; //$NON-NLS-1$
			baseName = fileName;
		}
		
		baseName += '-' + this.getParent().getUniversalID() + '-';
		
		File tempFile;
		File baseDir = this.getParent().getParent().getParent().getTempDir();
		if(baseDir == null) {
			tempFile = File.createTempFile(baseName, ext);
		} else {
			tempFile = new File(baseDir, baseName + System.currentTimeMillis() + ext);
		}
		tempFile.deleteOnExit();
		extractFile(tempFile);
		return new NSFTempFileInputStream(tempFile);
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.item.NSFObjectItem#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws DominoException {
		try {
			return getTempFileInputStream();
		} catch(IOException e) {
			throw new DominoException(e);
		}
	}
	
	public String getFileName() throws DominoException {
		FILEOBJECT fileObject = getFileObject();
		try {
			return fileObject.getFileName();
		} finally {
			fileObject.free();
		}
	}
	
	/**
	 * Extracts the file to the path indicated by <code>file</code>.
	 * 
	 * @param filePath the path and name of the file to extract to
	 * @throws DominoException if there is a problem extracting the file
	 */
	public void extractFile(String filePath) throws DominoException {
		api.NSFNoteCipherExtractFile(getParent().getHandle(), getItemBlockId(), 0, 0, filePath);
	}
	
	public void extractFile(File file) throws DominoException {
		if(!(file.isFile() || !file.exists())) {
			throw new IllegalArgumentException(StringUtil.format("File '{0}' is a folder", file.getAbsolutePath())); //$NON-NLS-1$
		}
		extractFile(file.getAbsolutePath());
	}
}
