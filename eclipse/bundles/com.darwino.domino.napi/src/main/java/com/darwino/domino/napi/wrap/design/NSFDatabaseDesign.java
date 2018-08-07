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

import java.util.ArrayList;
import java.util.List;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.util.DominoNativeUtils;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.NSFView;
import com.darwino.domino.napi.wrap.NSFViewEntry;
import com.darwino.domino.napi.wrap.NSFViewEntryCollection;
import com.ibm.commons.util.StringUtil;

/**
 * Provides a wrapper view to an {@link NSFDatabase} with convenience methods to access
 * design elements.
 * 
 * @author Jesse Gallagher
 * @since 2.0.0
 */
public class NSFDatabaseDesign {

	private final NSFDatabase database;
	
	public NSFDatabaseDesign(NSFDatabase database) {
		this.database = database;
	}
	
	/**
	 * Returns a {@link List} of the outlines in the database.
	 * 
	 * <p>The caller is responsible for freeing the returned objects.</p>
	 * 
	 * @return a <code>List</code> of {@link NSFOutline} objects
	 * @throws DominoException if there is a lower-level-API problem retrieving the outlines
	 */
	public List<NSFOutline> getOutlines() throws DominoException {
		List<NSFOutline> result = new ArrayList<NSFOutline>();
		List<Integer> ids = findDesignNotes(DominoAPI.NOTE_CLASS_ALLNONDATA, DominoAPI.DFLAGPAT_SITEMAP);
		for(Integer noteId : ids) {
			result.add(database.addChild(new NSFOutline(database, noteId)));
		}
		return result;
	}
	
	/**
	 * Returns a {@link List} of the forms in the database.
	 * 
	 * <p>The caller is responsible for freeing the returned objects.</p>
	 * 
	 * @return a <code>List</code> of {@link NSFForm} objects
	 * 
	 * @throws DominoException if there is a lower-level-API problem retrieving the forms
	 */
	public List<NSFForm> getForms() throws DominoException {
		List<NSFForm> result = new ArrayList<NSFForm>();
		List<Integer> formIds = findDesignNotes(DominoAPI.NOTE_CLASS_FORM, DominoAPI.DFLAGPAT_FORM);
		for(Integer noteId : formIds) {
			result.add(database.addChild(new NSFForm(database, noteId)));
		}
		
		return result;
	}
	
	/**
	 * Returns a {@link List} of the file resources in the database.
	 * 
	 * <p>The caller is responsible for freeing the returned objects.</p>
	 * 
	 * @return a <code>List</code> of {@link NSFFileResource} objects
	 * 
	 * @throws DominoException if there is a lower-level-API problem retrieving the file resources
	 */
	public List<NSFFileResource> getFileResources() throws DominoException {
		List<NSFFileResource> result = new ArrayList<NSFFileResource>();
		List<Integer> formIds = findDesignNotes(DominoAPI.NOTE_CLASS_FORM, DominoAPI.DFLAGPAT_FILE);
		for(Integer noteId : formIds) {
			result.add(database.addChild(new NSFFileResource(database, noteId)));
		}
		return result;
	}
	
	/**
	 * Returns the named file resource.
	 * 
	 * @param name the name or alias of the file resource to retrieve
	 * @return a {@link FileResource} object, or <code>null</code> if no file resource matched the name
	 * @throws DominoException if there is a lower-level-API problem retrieving the file resource
	 */
	public NSFFileResource getFileResource(String name) throws DominoException {
		int noteId = findDesignNote(DominoAPI.NOTE_CLASS_FORM, DominoAPI.DFLAGPAT_FILE, name, false);
		if(noteId != 0) {
			return database.addChild(new NSFFileResource(database, noteId));
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the {@link NSFForm} matching the provided name or alias.
	 * 
	 * @param formName the name to match against the form name or alias
	 * @return the specified form, or <code>null</code> if not found
	 * @throws DominoException if there is a lower-level-API problem finding the form
	 */
	public NSFForm getForm(String formName) throws DominoException {
		_checkRefValidity();
		
		int noteId = findDesignNote(DominoAPI.NOTE_CLASS_FORM, DominoAPI.DFLAGPAT_FORM, formName, false);
		if(noteId != 0) {
			return database.addChild(new NSFForm(database, noteId));	
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the {@link NSFForm} matching the provided name or alias, if it is a subform.
	 * 
	 * @param formName the name to match against the subform name or alias
	 * @return the specified subform, or <code>null</code> if not found or is not a subform
	 * @throws DominoException if there is a lower-level-API problem finding the form
	 */
	public NSFForm getSubform(String subformName) throws DominoException {
		int noteId = findDesignNote(DominoAPI.NOTE_CLASS_FORM, DominoAPI.DFLAGPAT_SUBFORM, subformName, false);
		if(noteId != 0) {
			return database.addChild(new NSFForm(database, noteId));	
		} else {
			return null;
		}
	}
	
	public NSFFormField getSharedField(String sharedFieldName) throws DominoException {
		int noteId = findDesignNote(DominoAPI.NOTE_CLASS_FIELD, DominoAPI.DFLAGPAT_FIELD, sharedFieldName, false);
		if(noteId != 0) {
			NSFForm form = database.addChild(new NSFForm(database, noteId));
			return form.getField(sharedFieldName, false);
		} else {
			return null;
		}
	}
	
	// ******************************************************************************
	// * Utility methods
	// ******************************************************************************
	
	/**
	 * Queries the design collection for a single design note.
	 * 
	 * @param noteClass the class of note to query (see <code>NOTE_CLASS_*</code> in {@link DominoAPI})
	 * @param pattern the note flag pattern to query (see <code>DFLAGPAT_*</code> in {@link DominoAPI})
	 * @param name the name or alias of the design note
	 * @param partialMatch whether partial matches are allowed
	 * @return the note ID of the specified design note, or <code>0</code> if the note was not found
	 * @throws DominoException if there is a lower-level-API problem querying the design collection
	 */
	public int findDesignNote(short noteClass, String pattern, String name, boolean partialMatch) throws DominoException {
		_checkRefValidity();
		
		long hDb = database.getHandle();
		
		int options = partialMatch ? DominoAPI.FIND_DESIGN_NOTE_PARTIAL : 0;
		try {
			return database.getAPI().NIFFindDesignNoteExt(hDb, name, noteClass, pattern, options);
		} catch(DominoException e) {
			if(e.getStatus() == DominoAPI.ERR_NOT_FOUND) {
				// Then the design element isn't found
				return 0;
			} else {
				throw e;
			}
		}
	}
	
	/**
	 * Queries the design collection of the database for design notes.
	 * 
	 * @param noteClass the class of note to query (see <code>NOTE_CLASS_*</code> in {@link DominoAPI})
	 * @param pattern the note flag pattern to query (see <code>DFLAGPAT_*</code> in {@link DominoAPI})
	 * @return a {@link List} of note IDs matching the query
	 * @throws DominoException if there is a lower-level-API problem querying the design collection
	 */
	public List<Integer> findDesignNotes(final short noteClass, final String pattern) throws DominoException {
		// TODO add structure that (potentially) reads all column data
		/*
		 * Design collection columns:
		 * 	- $TITLE (string)
		 * 	- $FormPrivs
		 * 	- $FormUsers
		 * 	- $Body
		 * 	- $Flags (string)
		 * 	- $Class
		 * 	- $Modified (TIMEDATE)
		 * 	- $Comment (string)
		 * 	- $AssistTrigger
		 * 	- $AssistType
		 * 	- $AssistFlags
		 * 	- $AssistFlags2
		 * 	- $UpdatedBy (string)
		 * 	- $$FormScript_0
		 * 	- $LANGUAGE
		 * 	- $Writers
		 *	- $PWriters
		 *	- $FlagsExt
		 *	- $FileSize (number)
		 *	- $MimeType
		 *	- $DesinerVersion (string)
		 */
		
		final List<Integer> result = new ArrayList<Integer>();
		NSFView designCollection = database.getView(DominoAPI.NOTE_CLASS_DESIGN | DominoAPI.NOTE_ID_SPECIAL);
		try {
			NSFViewEntryCollection entries = designCollection.getAllEntries();
			
			final boolean hasPattern = StringUtil.isNotEmpty(pattern);
			short readMask = DominoAPI.READ_MASK_NOTECLASS | DominoAPI.READ_MASK_NOTEID;
			if(hasPattern) {
				readMask |= DominoAPI.READ_MASK_SUMMARYVALUES;
			}
			
			entries.setReadMask(readMask);
			try {
				entries.eachEntry(new NSFViewEntryCollection.ViewEntryVisitor() {
					@Override public void visit(NSFViewEntry entry) throws DominoException {
						if((entry.getNoteClass() & noteClass) != 0) {
							if(hasPattern) {
								String flags = (String)entry.getColumnValues()[4];
								if(DominoNativeUtils.matchesFlagsPattern(flags, pattern)) {
									result.add(entry.getNoteId());
								}
							} else {
								// Then add as-is
								result.add(entry.getNoteId());
							}
						}
					}
				});
			} catch(DominoException e) {
				throw e;
			} catch(RuntimeException e) {
				throw e;
			} catch(Exception e) {
				throw new DominoException(e, "Exception while finding dsign elements of class {0}, pattern '{1}'", noteClass, pattern);
			}
		} finally {
			if(designCollection != null) {
				designCollection.free();
			}
		}
		return result;
	}
	
	// *******************************************************************************
	// * Internal methods
	// *******************************************************************************
	
	private void _checkRefValidity() {
		if(!database.isRefValid()) {
			throw new IllegalStateException(StringUtil.format("Object of type {0} (handle {1}) is no longer valid.", database.getClass().getSimpleName(), database.getHandle())); //$NON-NLS-1$
		}
	}
}
