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
package com.darwino.domino.napi.wrap.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.ibm.commons.util.StringUtil;

/**
 * This <code>InputStream</code> wraps a normal {@link FileInputStream} for all operations, but also
 * deletes the provided file on close.
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 */
public class NSFTempFileInputStream extends InputStream {
	private final File file;
	private final FileInputStream fis;
	private boolean closed = false;
	
	public NSFTempFileInputStream(File file) throws FileNotFoundException {
		this.file = file;
		this.fis = new FileInputStream(file);
	}
	
	/* (non-Javadoc)
	 * @see java.io.InputStream#close()
	 */
	@Override
	public void close() throws IOException {
		fis.close();
		file.delete();
		this.closed = true;
	}
	
	/* (non-Javadoc)
	 * @see java.io.InputStream#available()
	 */
	@Override
	public int available() throws IOException {
		return fis.available();
	}
	
	/* (non-Javadoc)
	 * @see java.io.InputStream#read(byte[])
	 */
	@Override
	public int read(byte[] b) throws IOException {
		return fis.read(b);
	}
	
	/* (non-Javadoc)
	 * @see java.io.InputStream#skip(long)
	 */
	@Override
	public long skip(long n) throws IOException {
		return fis.skip(n);
	}
	
	/* (non-Javadoc)
	 * @see java.io.InputStream#markSupported()
	 */
	@Override
	public boolean markSupported() {
		return fis.markSupported();
	}
	
	/* (non-Javadoc)
	 * @see java.io.InputStream#mark(int)
	 */
	@Override
	public synchronized void mark(int readlimit) {
		fis.mark(readlimit);
	}
	
	/* (non-Javadoc)
	 * @see java.io.InputStream#reset()
	 */
	@Override
	public synchronized void reset() throws IOException {
		fis.reset();
	}
	
	/* (non-Javadoc)
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return fis.read(b, off, len);
	}

	/* (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		return fis.read();
	}
	
	public long getFileSize() throws IOException {
		if(file != null && file.exists()) {
			return file.length();
		}
		return 0;
	}
	public boolean isClosed() {
		return closed;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return StringUtil.format("[{0}: file={1}]", getClass().getName(), file); //$NON-NLS-1$
	}
}
