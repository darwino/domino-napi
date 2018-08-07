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
package com.darwino.domino.napi.struct;

import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.c.C;

/**
 * Notes BLOCKID struct (nsfnote.h)
 *
 * @author Jesse Gallagher
 */
public class BLOCKID extends BaseStruct {

	static {
		int[] sizes = new int[3];
		initNative(sizes);
		sizeOf = sizes[0];
		_pool = sizes[1];
		_block = sizes[2];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _pool;
	public static final int _block;
	
	public BLOCKID() {
		super(C.malloc(sizeOf),true);
	}
	public BLOCKID(long data) {
		super(data, false);
	}
	public BLOCKID(long data, boolean owned) {
		super(data, owned);
	}

	public long getPool() { return _getDHandle(_pool); }
	public void setPool(long pool) { _setDHandle(_pool, pool); }
	
	public short getBlock() { return _getWORD(_block); }
	public void setBlock(short block) { _setWORD(_block, block); }
	
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	if(isRefValid()) {
	    	return StringUtil.format("[{0}: pool={1}, block={2}]", //$NON-NLS-1$
	    			getClass().getSimpleName(),
	    			getPool(),
	    			getBlock()
	    	);
    	} else {
    		return super.toString();
    	}
    }
}
