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

package com.darwino.domino.napi.struct;

import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.c.C;

/**
 * 
 * @author Jesse Gallagher
 *
 */
public class TIMEDATE_PAIR extends BaseStruct implements TimeStruct  {

	static {
		int[] sizes = new int[3];
		initNative(sizes);
		sizeOf = sizes[0];
		_Lower = sizes[1];
		_Upper = sizes[2];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Lower;
	public static final int _Upper;
	
	public TIMEDATE_PAIR() {
		super(C.malloc(sizeOf), true);
	}
	public TIMEDATE_PAIR(long data) {
		super(data, false);
	}
	public TIMEDATE_PAIR(long data, boolean owned) {
		super(data, owned);
	}

	public TIMEDATE getLower() {
		long ptr = getField(_Lower);
		return new TIMEDATE(ptr);
	}
	public TIMEDATE getUpper() {
		long ptr = getField(_Upper);
		return new TIMEDATE(ptr);
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.BaseStruct#toString()
	 */
	@Override
	public String toString() {
		return StringUtil.format("[{0}: Lower={1}, Upper={2}]", //$NON-NLS-1$
				getClass().getSimpleName(),
				getLower(),
				getUpper()
		);
	}
}
