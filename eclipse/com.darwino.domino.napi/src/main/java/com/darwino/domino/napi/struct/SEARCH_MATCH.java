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
 * Notes SEARCH_MATCH struct (nsfsearc.h)
 *
 * @author Jesse Gallagher
 */
public class SEARCH_MATCH extends BaseStruct {

	static {
		int[] sizes = new int[7];
		initNative(sizes);
		sizeOf = sizes[0];
		_ID = sizes[1];
		_OriginatorID = sizes[2];
		_NoteClass = sizes[3];
		_SERetFlags = sizes[4];
		_Privileges = sizes[5];
		_SummaryLength = sizes[6];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _ID;
	public static final int _OriginatorID;
	public static final int _NoteClass;
	public static final int _SERetFlags;
	public static final int _Privileges;
	public static final int _SummaryLength;
	
	public SEARCH_MATCH() {
		super(C.malloc(sizeOf),true);
	}
	public SEARCH_MATCH(long data) {
		super(data, false);
	}
	public SEARCH_MATCH(long data, boolean owned) {
		super(data, owned);
	}

	/**
	 * Returns the "ID" field of the struct as a {@link GLOBALINSTANCEID} object. This object is not a
	 * copy of the data; it references the same memory as the surrounding struct.
	 */
	public GLOBALINSTANCEID getId() {
		long ptr = getField(_ID);
		return new GLOBALINSTANCEID(ptr);
	}
	public void setId(GLOBALINSTANCEID id) {
		_checkRefValidity();
		if(id == null) { throw new NullPointerException("id cannot be null"); }
		long ptr = getField(_ID);
		C.memcpy(ptr, 0, id.getDataPtr(), 0, GLOBALINSTANCEID.sizeOf);
	}
	
	/**
	 * Returns the "OriginatorID" field of the struct as a {@link OID} object. This object is not a
	 * copy of the data; it references the same memory as the surrounding struct.
	 */
	public OID getOriginatorId() {
		long ptr = getField(_OriginatorID);
		return new OID(ptr);
	}
	public void setOriginatorId(OID oid) {
		if(oid == null) { throw new NullPointerException("oid cannot be null"); }
		long ptr = getField(_OriginatorID);
		C.memcpy(ptr, 0, oid.getDataPtr(), 0, OID.sizeOf);
	}
	
	public short getNoteClass() { return _getWORD(_NoteClass); }
	public void setNoteClass(short noteClass) { _setWORD(_NoteClass, noteClass); }
	
	public byte getSERetFlags() { return _getBYTE(_SERetFlags); }
	public void setSERetFlags(byte seRetFlags) { _setBYTE(_SERetFlags, seRetFlags); }
	
	public byte getPrivileges() { return _getBYTE(_Privileges); }
	public void setPrivileges(byte privileges) { _setBYTE(_Privileges, privileges); }
	
	public short getSummaryLength() { return _getWORD(_SummaryLength); }
	public void setSummaryLength(short summaryLength) { _setWORD(_SummaryLength, summaryLength); }
	
	/**
	 * Returns the TUALength value of the struct. <strong>Warning:</strong> This value is only present when
	 * explicitly requested and will most often reach beyond the length of the struct instead.
	 * 
	 * <p>This value must be explicitly requested with SEARCH1_RETURN_THREAD_UNID_ARRAY, which appears to be
	 * externally undocumented.</p>
	 */
	public short getTUALength() {
		_checkRefValidity();
		long ptr = C.ptrAdd(C.ptrAdd(data, _SummaryLength), C.sizeOfWORD);
		return C.getWORD(ptr, 0);
	}
	
	@Override
	public String toString() {
		if(isRefValid()) {
			return StringUtil.format("[{0}: ID={1}, OriginatorID={2}, NoteClass={3}, SERetFlags={4}, Privileges={5}, SummaryLength={6}]",
					getClass().getSimpleName(),
					getId(),
					getOriginatorId(),
					getNoteClass(),
					getSERetFlags(),
					getPrivileges(),
					getSummaryLength()
			);
		} else {
			return super.toString();
		}
	}
}
