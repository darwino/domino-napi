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
