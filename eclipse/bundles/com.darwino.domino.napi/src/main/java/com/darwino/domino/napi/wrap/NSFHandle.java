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

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.ibm.commons.util.StringUtil;

public abstract class NSFHandle extends NSFBase {

	/**
	 * This {@link Recycler} implementation uses {@link DominoAPI#OSMemFree(long)}
	 * to free the provided handle, and so is useful generally for back-end entities
	 * that are meant to be freed in that generic way.
	 *  
	 * @author Jesse Gallagher
	 * @since 2.2.0
	 */
	protected static class OSMemFreeRecycler extends Recycler {
		final DominoAPI api;
		long handle;
		
		public OSMemFreeRecycler(DominoAPI api, long handle) {
			this.api = api;
			this.handle = handle;
		}

		@Override
		void doFree() {
			if(handle != 0) {
				try {
					api.OSMemFree(handle);
					handle = 0;
				} catch (DominoException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private NSFSession session;
	private long handle;

	public NSFHandle(NSFSession session, long handle) {
		super(session.getAPI());
		this.handle = handle;
		this.session = session;
	}
	/**
	 * This constructor is used by non-handle variants of child classes, which do not
	 * necessarily need a parent.
	 * 
	 * @param api the {@link DominoAPI} instance to use for operations
	 */
	public NSFHandle(DominoAPI api) {
		super(api);
		this.handle = 0;
		this.session = null;
	}

	public long getHandle() {
		return handle;
	}
	public void setHandle(long handle) {
		this.handle = handle;
	}
	
	/**
	 * @return the session
	 */
	public NSFSession getSession() {
		return session;
	}
	
	@Override
	protected void doFree() {
		this.handle = 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof NSFHandle)) {
			return false;
		}
		if(this.session == null) {
			if(((NSFHandle)obj).session != null) {
				return false;
			} else {
				// Then just do object comparison
				return this == obj;
			}
		}
		return getHandle() == ((NSFHandle)obj).getHandle();
	}
	
	@Override
	public int hashCode() {
		return getClass().getSimpleName().hashCode() + (int)getHandle();
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#isRefValid()
	 */
	@Override
	public boolean isRefValid() {
		return handle != 0;
	}
	
	@Override
	protected final void _checkRefValidity() {
		if(!isRefValid()) {
			throw new IllegalStateException(StringUtil.format("Object of type {0} (handle {1}) is no longer valid.", getClass().getSimpleName(), handle)); //$NON-NLS-1$
		}
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#toString()
	 */
	@Override
	public String toString() {
		return StringUtil.format("[{0}: Handle={1}]", getClass().getSimpleName(), getHandle()); //$NON-NLS-1$
	}
}
