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
 * Notes GLOBALINSTANCEID struct (nsfdata.h)
 *
 * @author Jesse Gallagher
 */
public class GLOBALINSTANCEID extends BaseStruct {

	static {
		int[] sizes = new int[4];
		initNative(sizes);
		sizeOf = sizes[0];
		_File = sizes[1];
		_Note = sizes[2];
		_NoteID = sizes[3];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _File;
	public static final int _Note;
	public static final int _NoteID;
	
	public GLOBALINSTANCEID() {
		super(C.malloc(sizeOf),true);
	}
	public GLOBALINSTANCEID(long data) {
		super(data, false);
	}
	public GLOBALINSTANCEID(long data, boolean owned) {
		super(data, owned);
	}

	/**
	 * Returns the "File" field of the struct as a {@link TIMEDATE} object. This object is not a
	 * copy of the data; it references the same memory as the surrounding struct.
	 * 
	 * <p>The field's official type is "DBID", which is an alias of TIMEDATE.</p>
	 */
	public TIMEDATE getFile() {
		return new TIMEDATE(getField(_File));
	}
	public void setFile(TIMEDATE timeDate) {
		if(timeDate == null) { throw new NullPointerException("timeDate cannot be null"); }
		long ptr = getField(_File);
		C.memcpy(ptr, 0, timeDate.getDataPtr(), 0, TIMEDATE.sizeOf);
	}
	
	/**
	 * Returns the "Note" field of the struct as a {@link TIMEDATE} object. This object is not a
	 * copy of the data; it references the same memory as the surrounding struct.
	 */
	public TIMEDATE getNote() {
		return new TIMEDATE(getField(_Note));
	}
	public void setNote(TIMEDATE note) {
		if(note == null) { throw new NullPointerException("note cannot be null"); }
		long ptr = getField(_Note);
		C.memcpy(ptr, 0, note.getDataPtr(), 0, TIMEDATE.sizeOf);
	}
	
	public int getNoteId() { return _getNOTEID(_NoteID); }
	public void setNoteId(int noteId) { _setNOTEID(_NoteID, noteId); }
}
