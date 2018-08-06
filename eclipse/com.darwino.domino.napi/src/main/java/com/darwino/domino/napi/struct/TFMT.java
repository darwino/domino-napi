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
