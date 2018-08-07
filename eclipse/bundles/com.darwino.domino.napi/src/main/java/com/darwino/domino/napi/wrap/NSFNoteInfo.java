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
package com.darwino.domino.napi.wrap;

import java.io.Serializable;

import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.NoteClass;
import com.darwino.domino.napi.struct.OID;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.util.DominoNativeUtils;
import com.darwino.domino.napi.wrap.NSFOriginatorID;


/**
 * This class represents information retrieved from the database's note header, independent of
 * the actual note. This specifically maps to the data available from <code>NSFDbGetNoteInfoExt</code>.
 * 
 * @author Jesse Gallagher
 * @since 1.5.1
 */
public class NSFNoteInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private final int noteId;
	private boolean deleted;
	private final NSFOriginatorID originatorId;
	private final NSFDateTime modified;
	private final NoteClass noteClass;
	private final NSFDateTime addedToFile;
	private final int responseCount;
	private final int parentId;
	
	public NSFNoteInfo(int noteId, OID oid, TIMEDATE modified, short noteClass, TIMEDATE addedToFile, int responseCount, int parentId) throws DominoException {
		this.noteId = DominoNativeUtils.toNonDeletedId(noteId);
		this.deleted = DominoNativeUtils.isDeletedId(noteId);
		this.originatorId = new NSFOriginatorID(oid);
		this.modified = new NSFDateTime(modified);
		this.noteClass = DominoEnumUtil.valueOf(NoteClass.class, noteClass);
		this.addedToFile = new NSFDateTime(addedToFile);
		this.responseCount = responseCount;
		this.parentId = parentId;
	}
	
	/**
	 * @return the note ID of the original note
	 */
	public int getNoteId() {
		return noteId;
	}
	
	/**
	 * @return whether the note has been deleted, and thus whether this represents a deletion stub
	 */
	public boolean isDeleted() {
		return deleted;
	}
	
	/**
	 * @return the Originator ID of the note
	 */
	public NSFOriginatorID getOriginatorId() {
		return originatorId;
	}
	
	/**
	 * @return the time that the note was modified in the current NSF (as opposed to globally)
	 */
	public NSFDateTime getModified() {
		return modified;
	}
	
	/**
	 * @return the {@link NoteClass} of the note
	 */
	public NoteClass getNoteClass() {
		return noteClass;
	}
	
	/**
	 * @return the time that the note was added to the current NSF
	 */
	public NSFDateTime getAddedToFile() {
		return addedToFile;
	}
	
	/**
	 * @return the count of responses, or <code>0</code> if response counts are not tracked in this NSF
	 */
	public int getResponseCount() {
		return responseCount;
	}
	
	/**
	 * @return the note ID of the note's parent, or <codde>0</code> if the note has no parent
	 */
	public int getParentId() {
		return parentId;
	}
}