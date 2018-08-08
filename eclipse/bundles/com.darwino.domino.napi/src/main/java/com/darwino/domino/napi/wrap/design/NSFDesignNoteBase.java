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
package com.darwino.domino.napi.wrap.design;

import java.util.Collections;
import java.util.List;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.util.DominoNativeUtils;
import com.darwino.domino.napi.wrap.NSFBase;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.NSFNote;

/**
 * This is a common base class for objects that represent a backing design note, such as
 * views and forms.
 * 
 * @author Jesse Gallagher
 * @since 1.1.0
 */
public abstract class NSFDesignNoteBase extends NSFBase {
	
	private NSFDatabase parent;
	private int noteId;

	/** an internal cached note to use for operations that require reading it */
	private NSFNote note = null;

	public NSFDesignNoteBase(NSFDatabase parent, int noteId) {
		super(parent.getAPI());
		
		this.noteId = noteId;
		this.parent = parent;
	}

	@Override
	protected void doFree() {
		this.note = null;
		this.noteId = 0;
	}

	@Override
	public boolean isRefValid() {
		return noteId != 0;
	}

	public int getNoteID() {
		return noteId;
	}
	
	@Override
	public NSFDatabase getParent() {
		return parent;
	}
	
	public NSFNote getNote() throws DominoException {
		return getParent().getNoteByID(getNoteID());
	}
	

	
	/**
	 * Returns the title of the design note. The title is the first value in the list
	 * of titles in the $TITLE item of the note.
	 * 
	 * @return the design note's title
	 * @throws DominoException if there is a lower-level-API problem retrieving the $TITLE item
	 */
	public String getTitle() throws DominoException {
		String[] namesVal = _getInternalNote().get(DominoAPI.FIELD_TITLE, String[].class);
		List<String> names = DominoNativeUtils.splitDesignNames(namesVal);
		if(names.isEmpty()) {
			return ""; //$NON-NLS-1$
		} else {
			return names.get(0);
		}
	}
	
	/**
	 * Returns the aliases of the design note. The aliases are the values in the list
	 * of titles in the $TITLE item of the note after the first.
	 * 
	 * @return the design note's aliases
	 * @throws DominoException if there is a lower-level-API problem retrieving the $TITLE item
	 */
	public List<String> getAliases() throws DominoException {
		String[] namesVal = _getInternalNote().get(DominoAPI.FIELD_TITLE, String[].class);
		List<String> names = DominoNativeUtils.splitDesignNames(namesVal);
		if(names.size() < 2) {
			return Collections.emptyList();
		} else {
			return names.subList(1, names.size());
		}
	}
	
	/**
	 * Returns the programmatic title of the design note. The programmatic title is the <em>last</em>
	 * value in the list of titles in the $TITLE item of the note.
	 * 
	 * @return the design note's programmatic title
	 * @throws DominoException if there is a lower-level-API problem retrieving the $TITLE item
	 */
	public String getProgrammaticTitle() throws DominoException {
		String[] namesVal = _getInternalNote().get(DominoAPI.FIELD_TITLE, String[].class);
		List<String> names = DominoNativeUtils.splitDesignNames(namesVal);
		if(names.isEmpty()) {
			return ""; //$NON-NLS-1$
		} else {
			return names.get(names.size()-1);
		}
	}
	
	public String getFlagsString() throws DominoException {
		return _getInternalNote().getAsString(DominoAPI.DESIGN_FLAGS, ',');
	}
	

	protected  NSFNote _getInternalNote() throws DominoException {
		if(this.note == null) {
			this.note = getNote();
			// Take responsibility for the internal note
			getParent().removeChild(this.note);
		}
		return this.note;
	}
}
