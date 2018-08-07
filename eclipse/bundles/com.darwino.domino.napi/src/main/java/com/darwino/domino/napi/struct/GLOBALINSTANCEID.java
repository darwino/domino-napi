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
