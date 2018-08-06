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
 * Notes DBREPLICAINFO struct (nsfdata.h)
 *
 * @author Jesse Gallagher
 */
public class DBREPLICAINFO extends BaseStruct {
	static {
		int[] sizes = new int[5];
		initNative(sizes);
		sizeOf = sizes[0];
		_ID = sizes[1];
		_Flags = sizes[2];
		_CutoffInterval = sizes[3];
		_Cutoff = sizes[4];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _ID;
	public static final int _Flags;
	public static final int _CutoffInterval;
	public static final int _Cutoff;
	
	public DBREPLICAINFO() {
		super(C.malloc(sizeOf),true);
	}
	public DBREPLICAINFO(long data) {
		super(data, false);
	}
	public DBREPLICAINFO(long data, boolean owned) {
		super(data, owned);
	}

	/**
	 * Returns the "ID" field of the struct as a {@link TIMEDATE} object. This object is not a
	 * copy of the data; it references the same memory as the surrounding struct.
	 */
	public TIMEDATE getID() {
		return new TIMEDATE(getField(_ID));
	}
	public void setID(TIMEDATE timeDate) {
		if(timeDate == null) { throw new NullPointerException("timeDate cannot be null"); }
		long ptr = getField(_ID);
		C.memcpy(ptr, 0, timeDate.getDataPtr(), 0, TIMEDATE.sizeOf);
	}
	
	public short getFlagsRaw() { return _getWORD(_Flags); }
	public void setFlagsRaw(short flags) { _setWORD(_Flags, flags); }
	
	public short getCutoffInterval() { return _getWORD(_CutoffInterval); }
	public void setCutoffInterval(short cutoffInterval) { _setWORD(_CutoffInterval, cutoffInterval); }
	
	/**
	 * Returns the "ID" field of the struct as a {@link TIMEDATE} object. This object is not a
	 * copy of the data; it references the same memory as the surrounding struct.
	 */
	public TIMEDATE getCutoff() {
		return new TIMEDATE(getField(_Cutoff));
	}
	public void setCutoff(TIMEDATE timeDate) {
		if(timeDate == null) { throw new NullPointerException("timeDate cannot be null"); }
		long ptr = getField(_ID);
		C.memcpy(ptr, 0, timeDate.getDataPtr(), 0, TIMEDATE.sizeOf);
	}
	
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	if(isRefValid()) {
	    	return StringUtil.format("[{0}: ID={1}, Flags={2}, CutoffInterval={3}, Cutoff={4}]", //$NON-NLS-1$
	    			getClass().getSimpleName(),
	    			getID(),
	    			getFlagsRaw(),
	    			getCutoffInterval(),
	    			getCutoff()
	    	);
    	} else {
    		return super.toString();
    	}
    }
}
