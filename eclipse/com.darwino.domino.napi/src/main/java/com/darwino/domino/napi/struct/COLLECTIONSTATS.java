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

import com.darwino.domino.napi.c.C;

/**
 * 
 * @author Jesse Gallagher
 *
 */
public class COLLECTIONSTATS extends BaseStruct {
	static {
		int[] sizes = new int[3];
		initNative(sizes);
		sizeOf = sizes[0];
		_TopLevelEntries = sizes[1];
		_LastModifiedTime = sizes[2];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _TopLevelEntries;
	public static final int _LastModifiedTime;
	
	public COLLECTIONSTATS() {
		super(C.malloc(sizeOf), true);
	}
	public COLLECTIONSTATS(long data) {
		super(data, false);
	}

	public COLLECTIONSTATS(long data, boolean owned) {
		super(data, owned);
	}
	
	public int getTopLevelEntries() { return _getDWORD(_TopLevelEntries); }
	public void setTopLevelEntries(int topLevelEntries) { _setDWORD(_TopLevelEntries, topLevelEntries); }
	
	public int getLastModifiedTime() { return _getDWORD(_LastModifiedTime); }
	public void setLastModifiedTime(int lastModifiedTime) { _setDWORD(_LastModifiedTime, lastModifiedTime); }
}
