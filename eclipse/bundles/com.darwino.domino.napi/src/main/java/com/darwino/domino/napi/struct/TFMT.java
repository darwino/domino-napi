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

import com.darwino.domino.napi.c.C;

/**
 * 
 * @author Jesse Gallagher
 *
 */
public class TFMT extends BaseStruct {
	static {
		int[] sizes = new int[5];
		initNative(sizes);
		sizeOf = sizes[0];
		_Date = sizes[1];
		_Time = sizes[2];
		_Zone = sizes[3];
		_Structure = sizes[4];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Date;
	public static final int _Time;
	public static final int _Zone;
	public static final int _Structure;
	
	public TFMT() {
		super(C.malloc(sizeOf), true);
	}
	public TFMT(long data) {
		super(data, false);
	}

	public TFMT(long data, boolean owned) {
		super(data, owned);
	}
	
	public byte getDate() { return _getBYTE(_Date); }
	public void setDate(byte date) { _setBYTE(_Date, date); }
	
	public byte getTime() { return _getBYTE(_Time); }
	public void setTime(byte time) { _setBYTE(_Time, time); }
	
	public byte getZone() { return _getBYTE(_Zone); }
	public void setZone(byte zone) { _setBYTE(_Zone, zone); }
	
	public byte getStructure() { return _getBYTE(_Structure); }
	public void setStructure(byte structure) { _setBYTE(_Structure, structure); }
}
