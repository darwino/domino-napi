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

import java.io.File;

import com.darwino.domino.napi.DominoException;

/**
 * @author Jesse Gallagher
 * @since 1.5.0
 *
 */
public class NSFUserID extends NSFHandle {

	private String serverName;
	private File file;
	
	/**
	 * @param session
	 * @param handle
	 */
	public NSFUserID(NSFSession session, long handle, String serverName, File file) {
		super(session, handle);
		
		this.serverName = serverName;
		this.file = file;
	}
	
	/**
	 * @return the server that housed the ID file, if retrieved from an ID Vault
	 */
	public String getServerName() {
		return serverName;
	}

	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#getParent()
	 */
	@Override
	protected NSFBase getParent() {
		return getSession();
	}

	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFHandle#doFree()
	 */
	@Override
	protected void doFree() {
		if(getHandle() != 0) {
			try {
				api.SECKFMClose(getHandle(), 0);
			} catch(DominoException e) {
				// This should be very unlikely
				throw new RuntimeException(e);
			}
		}
		if(file != null) {
			file.delete();
		}
		
		super.doFree();
	}
}
