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

package com.darwino.domino.napi.wrap.io;

import java.io.IOException;
import java.io.InputStream;

import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.c.IntRef;
import com.darwino.domino.napi.enums.ObjectType;

/**
 * This class provides an <code>InputStream</code> for the data stored in OBJECT type items in an NSF.
 * 
 * <p>Note, however, that it does not decompress the data in those objects - if the object is stored with LZ1
 * or Huffman coding, the data returned by this <code>InputStream</code> will also be compressed.</p>
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 */
public class NSFObjectInputStream extends InputStream {
	private final DominoAPI api;
	private final long hDb;
	private final int objectId;
	
	private static final int BUFFER_SIZE = 1024 * 1024;
	private byte[] buffer;
	private int bufferPos = 0;
	private int size;
	private int totalRead = 0;
	
	
	public NSFObjectInputStream(DominoAPI api, long hDb, int objectId, ObjectType objectType) throws DominoException {
		this.api = api;
		this.hDb = hDb;
		this.objectId = objectId;
		
		IntRef retSize = new IntRef();
		api.NSFDbGetObjectSize(hDb, objectId, objectType.getValue(), retSize, null, null);
		this.size = retSize.get();
	}
	
	@Override
	public int read() throws IOException {
		int avail = available();
		if(avail == 0) {
			return -1;
		}
		if(buffer == null || bufferPos == buffer.length) {
			int bufferSize = avail < BUFFER_SIZE ? avail : BUFFER_SIZE;
			buffer = new byte[bufferSize];
			try {
				long hResultBuffer = api.NSFDbReadObject(hDb, objectId, totalRead, bufferSize);
				if(hResultBuffer == DominoAPI.NULLHANDLE) {
					throw new DominoException(null, "Received NULLHANDLE from NSFDbReadObject");
				}
				long dataPtr = api.OSLockObject(hResultBuffer);
				try {
					C.readByteArray(buffer, 0, dataPtr, 0, bufferSize);
				} finally {
					api.OSUnlockObject(hResultBuffer);
				}
				
			} catch(DominoException e) {
				throw new IOException(e);
			}
		}
		
		totalRead++;
		return buffer[bufferPos++];
	}
	
	/* (non-Javadoc)
	 * @see java.io.InputStream#available()
	 */
	@Override
	public int available() throws IOException {
		return (int)(size - totalRead);
	}
	
	/* (non-Javadoc)
	 * @see java.io.InputStream#skip(long)
	 */
	@Override
	public long skip(long n) throws IOException {
		long startingPos = totalRead;
		totalRead += n;
		if(totalRead > size) {
			totalRead = size;
		}
		return totalRead - startingPos;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return StringUtil.format("[{0}: hDb={1}, objectId={2}, bufferPos={3}, size={4}, totalRead={5}]", //$NON-NLS-1$
				getClass().getSimpleName(),
				hDb,
				objectId,
				bufferPos,
				size,
				totalRead
		);
	}
}