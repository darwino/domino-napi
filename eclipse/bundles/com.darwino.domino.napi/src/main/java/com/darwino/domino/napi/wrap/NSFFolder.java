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

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.ibm.commons.util.StringUtil;

/**
 * This class represents a specialized view of an {@link NSFView} object - it shares the same back-end
 * structures, but provides access to folder-specific operations.
 * 
 * <p>This object is considered a child of the parent {@link NSFView} object.</p>
 * 
 * @author Jesse Gallagher
 */
public class NSFFolder {
	
	public static final class FolderChanges {
		private final NSFNoteIDCollection added;
		private final NSFNoteIDCollection removed;
		
		private FolderChanges(NSFNoteIDCollection added, NSFNoteIDCollection removed) {
			this.added = added;
			this.removed = removed;
		}
		public NSFNoteIDCollection getAdded() {
			return added;
		}
		public NSFNoteIDCollection getRemoved() {
			return removed;
		}
		
		public void free() {
			added.free();
			removed.free();
		}
	}
	
	private NSFView parent;
	private DominoAPI api;

	public NSFFolder(NSFView parent) {
		this.api = parent.getAPI();
		this.parent = parent;
	}
	
	/**
	 * Returns a {@link NSFNoteIDCollection} of the notes contained within this folder.
	 * 
	 * @return a {@link NSFNoteIDCollection} of the folder contents
	 * @throws DominoException if there is a lower-level-API problem retrieving the contents
	 */
	public NSFNoteIDCollection getContents() throws DominoException {
		parent._checkRefValidity();
		
		NSFView view = this.getParent();
		NSFDatabase database = view.getParent();
		NSFSession session = database.getParent();
		long hDb = database.getHandle();
		long hTable = api.NSFFolderGetIDTable(hDb, hDb, view.getNoteID(), 0);
		if(hTable == 0) {
			throw new DominoException(null, "NSFFolderGetIDTable returned 0");
		}
		return session.addChild(new NSFNoteIDCollection(session, hTable, true));
	}
	
	/**
	 * Returns the number of entries in this folder's index. If the folder is categorized,
	 * this includes category entries.
	 * 
	 * <p>To retrieve the count of document specifically, use {@link #getNoteCount()}.</p>
	 * 
	 * @return the number of entries in this folder's index
	 * @throws DominoException if there is a lower-level-API problem retrieving the entry count
	 */
	public int size() throws DominoException {
		parent._checkRefValidity();
		
		long hDb = this.getParent().getParent().getHandle();
		return api.FolderDocCount(hDb, hDb, this.getParent().getNoteID(), 0);
	}
	
	/**
	 * Returns the number of notes contained in this folder.
	 * 
	 * <p>This is equivalent to calling {@link #getContents} and calling
	 * {@link NSFNoteIDCollection#size} on that.</p>
	 * 
	 * @return the number of notes contained in the folder
	 * @throws DominoException if there is a lower-level-API problem retrieving the entry count
	 */
	public int getNoteCount() throws DominoException {
		NSFNoteIDCollection contents = getContents();
		try {
			return contents.size();
		} finally {
			contents.free();
		}
	}
	
	public void add(int noteId) throws DominoException {
		parent._checkRefValidity();
		
		long hTable = api.IDCreateTable(C.sizeOfNOTEID);
		try {
			api.IDInsert(hTable, noteId);
			
			long hDb = this.getParent().getParent().getHandle();
			api.FolderDocAdd(hDb, hDb, this.getParent().getNoteID(), hTable, 0);
		} finally {
			api.IDDestroyTable(hTable);
		}
	}
	
	/**
	 * @throws DominoException if there is a lower-level-API problem adding the ID
	 * @throws IllegalArgumentException if <code>note</code> is null
	 */
	public void add(NSFNote note) throws DominoException {
		if(note == null) {
			throw new IllegalArgumentException("note cannot be null");
		}
		add(note.getNoteID());
	}
	
	/**
	 * @throws DominoException if there is a lower-level-API problem adding the IDs
	 * @throws IllegalArgumentException if <code>notes</code> is null
	 */
	public void addAll(NSFNoteIDCollection notes) throws DominoException {
		if(notes == null) {
			throw new IllegalArgumentException("notes cannot be null");
		}
		parent._checkRefValidity();
		
		long hTable = notes.getHandle();
		long hDb = this.getParent().getParent().getHandle();
		api.FolderDocAdd(hDb, hDb, this.getParent().getNoteID(), hTable, 0);
	}
	
	/**
	 * @throws DominoException if there is a lower-level-API problem removing the ID
	 */
	public void remove(int noteId) throws DominoException {
		parent._checkRefValidity();
		
		long hTable = api.IDCreateTable(C.sizeOfNOTEID);
		try {
			api.IDInsert(hTable, noteId);
			
			long hDb = this.getParent().getParent().getHandle();
			api.FolderDocRemove(hDb, hDb, this.getParent().getNoteID(), hTable, 0);
		} finally {
			api.IDDestroyTable(hTable);
		}
	}
	
	/**
	 * @throws DominoException if there is a lower-level-API problem removing the ID
	 * @throws IllegalArgumentException if <code>note</code> is null
	 */
	public void remove(NSFNote note) throws DominoException {
		if(note == null) {
			throw new IllegalArgumentException("note cannot be null");
		}
		
		remove(note.getNoteID());
	}
	
	/**
	 * @throws DominoException if there is a lower-level-API problem removing the IDs
	 * @throws IllegalArgumentException if <code>notes</code> is null
	 */
	public void removeAll(NSFNoteIDCollection notes) throws DominoException {
		if(notes == null) {
			throw new IllegalArgumentException("notes cannot be null");
		}
		parent._checkRefValidity();
		
		long hTable = notes.getHandle();
		long hDb = this.getParent().getParent().getHandle();
		api.FolderDocRemove(hDb, hDb, this.getParent().getNoteID(), hTable, 0);
	}
	
	public void clear() throws DominoException {
		parent._checkRefValidity();
		
		long hDb = this.getParent().getParent().getHandle();
		api.FolderDocRemoveAll(hDb, hDb, this.getParent().getNoteID(), 0);
	}
	
	public boolean contains(int noteId) throws DominoException {
		return getContents().contains(noteId);
	}
	/**
	 * @throws DominoException if there is a lower-level-API problem checking the folder contents
	 * @throws IllegalArgumentException if <code>note</code> is null
	 */
	public boolean contains(NSFNote note) throws DominoException {
		if(note == null) {
			throw new IllegalArgumentException("note cannot be null");
		}
		return contains(note.getNoteID());
	}
	
	/**
	 * @throws DominoException if there is a lower-level-API problem moving the folder
	 * @throws IllegalArgumentException if <code>parent</code> is null or not a folder
	 */
	public void move(NSFView parent) throws DominoException {
		if(parent == null) {
			throw new IllegalArgumentException("parent cannot be null");
		}
		if(!parent.isFolder()) {
			throw new IllegalArgumentException("parent must be a folder");
		}
		parent._checkRefValidity();
		
		long hDb = this.getParent().getParent().getHandle();
		long parentDb = parent.getParent().getHandle();
		api.FolderMove(hDb, hDb, this.getParent().getNoteID(), parentDb, parent.getNoteID(), 0);
	}
	
	/**
	 * @throws DominoException if there is a lower-level-API problem renaming the view
	 * @throws IllegalArgumentException if <code>name</code> is null or empty
	 */
	public void rename(String name) throws DominoException {
		if(StringUtil.isEmpty(name)) {
			throw new IllegalArgumentException("name cannot be empty");
		}
		parent._checkRefValidity();
		
		long hDb = this.getParent().getParent().getHandle();
		api.FolderRename(hDb, hDb, this.getParent().getNoteID(), name, 0);
	}
	
	/**
	 * @throws DominoException if there is a lower-level-API problem copying the view
	 * @throws IllegalArgumentException if <code>name</code> is null or empty
	 */
	public NSFView copy(String name) throws DominoException {
		if(StringUtil.isEmpty(name)) {
			throw new IllegalArgumentException("name cannot be empty");
		}
		parent._checkRefValidity();
		
		long hDb = this.getParent().getParent().getHandle();
		int copyId = api.FolderCopy(hDb, hDb, this.getParent().getNoteID(), name, 0);
		NSFDatabase db = this.getParent().getParent();
		return db.addChild(new NSFView(db, copyId));
	}
	
	/**
	 * @param since the starting time for the change list, or <code>null</code> to retrieve
	 * 		all historical changes
	 * @throws DominoException if there is a lower-level-API problem retrieving the changes
	 */
	public FolderChanges getChanges(NSFDateTime since) throws DominoException {
		TIMEDATE timeDate = null;
		if(since == null) {
			timeDate = new TIMEDATE();
			api.TimeDateClear(timeDate);
		} else {
			timeDate = since.toTIMEDATE();
		}
		long addedPtr = C.malloc(C.sizeOfDHANDLE);
		long removedPtr = C.malloc(C.sizeOfDHANDLE);
		try {
			NSFDatabase database = this.getParent().getParent();
			long hDb = database.getHandle();
			api.NSFGetFolderChanges(hDb, hDb, this.getParent().getNoteID(), timeDate, 0, addedPtr, removedPtr);
			long hAdded = C.getDHandle(addedPtr, 0);
			long hRemoved = C.getDHandle(removedPtr, 0);
			
			NSFSession session = database.getParent();
			NSFNoteIDCollection added = session.addChild(new NSFNoteIDCollection(session, hAdded, true));
			NSFNoteIDCollection removed = session.addChild(new NSFNoteIDCollection(session, hRemoved, true));
			
			return new FolderChanges(added, removed);
		} finally {
			C.free(addedPtr);
			C.free(removedPtr);
			timeDate.free();
		}
	}
	
	/**
	 * Deletes the folder.
	 * 
	 * @throws DominoException if there is a lower-level-API problem deleting the folder
	 */
	public void delete() throws DominoException {
		long hDb = this.getParent().getParent().getHandle();
		int noteId = this.getParent().getNoteID();
		api.FolderDelete(hDb, hDb, noteId, 0);
	}
	
	public NSFView getParent() {
		return parent;
	}

}
