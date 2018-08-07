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
import com.darwino.domino.napi.util.DominoNativeUtils;



/**
 * Notes OID.
 * 
 */
public class OID extends BaseStruct {

	static {
		int[] sizes = new int[5];
		initNative(sizes);
		sizeOf = sizes[0];
		_file = sizes[1];
		_note = sizes[2];
		_sequence = sizes[3];
		_sequenceTime = sizes[4];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _file;
	public static final int _note;
	public static final int _sequence;
	public static final int _sequenceTime;
			
	public OID() {
		super(C.malloc(sizeOf),true);
	}

	public OID(/*pointer*/long pointer) {
		super(pointer,false);
	}

	public OID(/*pointer*/long pointer, boolean owned) {
		super(pointer,owned);
	}
	
	public String getUNID() {
		return DominoNativeUtils.toUnid(getFile(), getNote());
	}
	
	public final TIMEDATE getFile() {
		return new TIMEDATE(getField(_file));
	}
	public final void setFile(TIMEDATE value) {
		_checkRefValidity();
		C.memcpy(data,_file,value.data,0,TIMEDATE.sizeOf);
	}
	
	public final TIMEDATE getNote() {
		return new TIMEDATE(getField(_note));
	}
	public final void setNote(TIMEDATE value) {
		_checkRefValidity();
		C.memcpy(data,_note,value.data,0,TIMEDATE.sizeOf); 
	}
	
	public final int getSequence() { return _getDWORD(_sequence); }
	public final void setSequence(int value) { _setDWORD(_sequence, value); }
	
	public final TIMEDATE getSequenceTime() {
		return new TIMEDATE(getField(_sequenceTime));
	}
	public final void setSequenceTime(TIMEDATE value) {
		_checkRefValidity();
		C.memcpy(data,_sequenceTime,value.data,0,TIMEDATE.sizeOf);
	}
	
	/* ******************************************************************************
	 * Misc.
	 ********************************************************************************/
	
	@Override
	public String toString() {
		if(isRefValid()) {
			return StringUtil.format("[{0}: File={1}, Note={2}, Sequence={3}, SequenceTime={4}, UNID={5}]", getClass().getSimpleName(), getFile(), getNote(), getSequence(), getSequenceTime(), getUNID()); //$NON-NLS-1$
		} else {
			return super.toString();
		}
	}
}
