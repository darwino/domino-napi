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

public class UNIVERSALNOTEID extends BaseStruct {
	
	static {
		int[] sizes = new int[3];
		initNative(sizes);
		sizeOf = sizes[0];
		_file = sizes[1];
		_note = sizes[2];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _file;
	public static final int _note;
	
	public UNIVERSALNOTEID() {
		super(C.malloc(sizeOf), true);
	}
	public UNIVERSALNOTEID(long data) {
		super(data, false);
	}

	public UNIVERSALNOTEID(long data, boolean owned) {
		super(data, owned);
	}
	
	public String getUNID() {
		return getFile().toHexString() + getNote().toHexString();
	}
	
	
	public void setUNID(String unid) {
		if(StringUtil.isEmpty(unid) || unid.length() != 32) {
			throw new IllegalArgumentException("unid must be a 32-character hexadecimal string");
		}

		int[] ints = new int[4];
		ints[1] = (int)Long.parseLong(unid.substring(0, 8), 16);
		ints[0] = (int)Long.parseLong(unid.substring(8, 16), 16);
		ints[3] = (int)Long.parseLong(unid.substring(16, 24), 16);
		ints[2] = (int)Long.parseLong(unid.substring(24, 32), 16);
		
		C.writeIntArray(data, 0, ints, 0, 4);
	}

	
	public final TIMEDATE getFile() { return new TIMEDATE(getField(_file)); }
	public final void setFile(TIMEDATE value) {
		_checkRefValidity();
		C.memcpy(data,_file,value.data,0,TIMEDATE.sizeOf);
	}
	
	public final TIMEDATE getNote() { return new TIMEDATE(getField(_note)); }
	public final void setNote(TIMEDATE value) {
		_checkRefValidity();
		C.memcpy(data,_note,value.data,0,TIMEDATE.sizeOf);
	}
	
	/* ******************************************************************************
	 * Misc.
	 ********************************************************************************/

	@Override
	public String toString() {
		if(isRefValid()) {
			return StringUtil.format("[{0}: {1}, File={2}, Note={3}", getClass().getSimpleName(), getUNID(), getFile(), getNote()); //$NON-NLS-1$
		} else {
			return super.toString();
		}
	}
}
