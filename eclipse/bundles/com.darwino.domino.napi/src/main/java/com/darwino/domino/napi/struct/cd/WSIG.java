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
package com.darwino.domino.napi.struct.cd;

import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.struct.BaseStruct;

/**
 * (ods.h)
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public class WSIG extends BaseStruct implements CDHeader<Short, Short> {

	static {
		int[] sizes = new int[3];
		initNative(sizes);
		sizeOf = sizes[0];
		_Signature = sizes[1];
		_Length = sizes[2];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Signature;
	public static final int _Length;
	
	public WSIG() {
		super(C.malloc(sizeOf),true);
	}
	public WSIG(long data) {
		super(data, false);
	}
	public WSIG(long data, boolean owned) {
		super(data, owned);
	}

	@Override
	public Short getSignature() { return _getWORD(_Signature); }
	@Override
	public void setSignature(Short signature) { _setWORD(_Signature, signature); }
	
	@Override
	public Short getLength() { return _getWORD(_Length); }
	@Override
	public void setLength(Short length) { _setWORD(_Length, length); }
	
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	if(data != 0) {
	    	return StringUtil.format("[{0}: Signature={1}, Length={2}]", //$NON-NLS-1$
	    			getClass().getSimpleName(),
	    			getSignature(),
	    			getLength()
	    	);
    	} else {
    		return super.toString();
    	}
    }
}
