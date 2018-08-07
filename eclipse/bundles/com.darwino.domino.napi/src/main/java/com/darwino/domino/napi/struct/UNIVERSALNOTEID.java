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
