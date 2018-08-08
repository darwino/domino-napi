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

import java.io.File;

import com.darwino.domino.napi.DominoAPI;
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
	
	static class UserIDRecycler extends Recycler {
		private final DominoAPI api;
		private long handle;
		private File file;
		
		public UserIDRecycler(DominoAPI api, long handle, File file) {
			this.api = api;
			this.handle = handle;
			this.file = file;
		}
		
		@Override
		void doFree() {
			if(handle != 0) {
				try {
					api.SECKFMClose(handle, 0);
				} catch (DominoException e) {
				}
				handle = 0;
			}
			if(file != null) {
				file.delete();
				file = null;
			}
		}
	}
	@Override
	protected Recycler createRecycler() {
		return new UserIDRecycler(api, getHandle(), file);
	}
}
