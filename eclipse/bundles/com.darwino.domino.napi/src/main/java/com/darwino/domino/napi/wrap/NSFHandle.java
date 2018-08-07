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

import com.darwino.domino.napi.DominoAPI;
import com.ibm.commons.util.StringUtil;

public abstract class NSFHandle extends NSFBase {
	
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
