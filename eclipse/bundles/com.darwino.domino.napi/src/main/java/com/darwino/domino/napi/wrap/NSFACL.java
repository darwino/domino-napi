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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.c.LongRef;
import com.darwino.domino.napi.c.ShortRef;
import com.darwino.domino.napi.enums.ACLEntryFlag;
import com.darwino.domino.napi.enums.ACLLevel;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.proc.ACLEnumFunc;

/**
 * Represents the ACL of a database.
 * 
 * @author Jesse Gallagher
 * @since 2.0.0
 */
public class NSFACL extends NSFHandle {
	
	private final NSFDatabase parent;

	public NSFACL(NSFDatabase parent, long handle) {
		super(parent.getSession(), handle);
		this.parent = parent;
	}

	@Override
	protected NSFDatabase getParent() {
		return this.parent;
	}

	/**
	 * Returns a list of entries in the ACL, sorted with -Default- first and then case-insensitively by name (like the ACL dialog).
	 * 
	 * @return a {@link List} of {@link NSFACLEntry} objects
	 * @throws DominoException if there is a problem reading the database ACL
	 */
	public List<NSFACLEntry> getEntries() throws DominoException {
		_checkRefValidity();
		
		final List<NSFACLEntry> result = new ArrayList<NSFACLEntry>();
		final ThreadLocal<List<String>> roles = new ThreadLocal<List<String>>(); // Just to hold the reference
		api.ACLEnumEntries(getHandle(), new ACLEnumFunc() {
			@Override public void callback(String name, short accessLevel, long privileges, short accessFlags) throws DominoException {
				ACLLevel level = DominoEnumUtil.valueOf(ACLLevel.class, accessLevel);
				
				if(roles.get() == null) {
					// Build the role name cache
					roles.set(getRoles());
				}
				List<String> entryRoles = new ArrayList<String>();
				for(int i = 0; i < roles.get().size(); i++) {
					if(api.ACLIsPrivSet(privileges, (short)(i+5))) {
						entryRoles.add(roles.get().get(i));
					}
				}
				
				Set<ACLEntryFlag> flags = DominoEnumUtil.valuesOf(ACLEntryFlag.class, accessFlags);
				
				result.add(new NSFACLEntry(NSFACL.this, name, level, entryRoles, flags));
			}
		});
		
		Collections.sort(result, new Comparator<NSFACLEntry>() {
			@Override
			public int compare(NSFACLEntry o1, NSFACLEntry o2) {
				if(o1.isDefaultEntry()) {
					return -1;
				} else if(o2.isDefaultEntry()) {
					return 1;
				} else {
					return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
				}
			}
		});
		
		return result;
	}
	
	/**
	 * Returns a list of roles in the database, ignoring the pre-R4 roles.
	 * 
	 * @return a {@link List} of {@link String}s
	 * @throws DominoException if there is a problem building the role list
	 */
	public List<String> getRoles() throws DominoException {
		_checkRefValidity();
		
		List<String> result = new ArrayList<String>();
		// Skip slots 0 through 4
		for(int i = 5; i < DominoAPI.ACL_PRIVCOUNT; i++) {
			try {
			String name = api.ACLGetPrivName(getHandle(), (short)i);
			result.add(name);
			} catch(DominoException e) {
				if(e.getStatus() == 1060) {
					// This means the requested index isn't in the list
					break;
				} else {
					throw e;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the ACL update history.
	 * 
	 * <p><strong>Note:</strong> due to the fact that these entries are stored in locale-specific format
	 * with no indication of locale, the date/times are parsed using the server's current locale. If
	 * they're un-parseable that way (for example, a date of "15/1/2017" on a US-locale server),
	 * the entry is skipped.</p>
	 * 
	 * @return a {@link List} of {@link NSFACLHistoryEntry} objects
	 * @throws DominoException if there is a problem retrieving the ACL history
	 */
	public List<NSFACLHistoryEntry> getHistory() throws DominoException {
		_checkRefValidity();
		
		LongRef hHistory = new LongRef();
		ShortRef historyCount = new ShortRef();
		
		api.ACLGetHistory(getHandle(), hHistory, historyCount);
		
		int count = historyCount.get() & 0xFFFFFFFF;
		List<NSFACLHistoryEntry> result = new ArrayList<NSFACLHistoryEntry>(count);
		if(count == 0) {
			return result;
		}
		
		long historyPtr = api.OSLockObject(hHistory.get());
		try {
			// Entries are LMBCS strings separated by NULLs
			// The docs say they're separated by double NULL, but that is a filthy lie
			
			long start = historyPtr;
			long term = historyPtr;
			
			for(int i = 0; i < count; i++) {
				// Find the term
				int len = 0;
				while(true) {
					char termVal = (char)C.getByte(term, 0);
					
					if(termVal == '\0') {
						break;
					} else {
						term = C.ptrAdd(term, 1);
					}
					
					if(len++ > 10000) {
						throw new IllegalStateException("Infinite loop detected");
					}
				}
				
				String entry = C.getLMBCSString(start, 0, len);
				try {
					NSFACLHistoryEntry historyEntry = new NSFACLHistoryEntry(api, entry);
					result.add(historyEntry);
				} catch(DominoException e) {
					if(e.getStatus() == DominoAPI.ERR_TDI_CONV) {
						// The date/time of the entry can't be read in this locale - skip
					} else {
						throw e;
					}
				}
				
				start = C.ptrAdd(term, 1);
				
				// Check for a second null, just in case the docs are sometimes right
				if(C.getBYTE(start, 0) == 0) {
					start = C.ptrAdd(start, 1);
				}
				
				term = start;
			}
		} finally {
			api.OSUnlockObject(hHistory.get());
			api.OSMemFree(hHistory.get());
		}
		
		return result;
	}
	
	/**
	 * Returns the date that the ACL was last modified, according to its note.
	 * 
	 * @return a {@link NSFDateTime} for when the ACL note was last modified
	 * @throws DominoException if there is a problem reading the note
	 */
	public NSFDateTime getLastModified() throws DominoException {
		NSFNote aclNote = getParent().getNoteByID(DominoAPI.NOTE_ID_SPECIAL | DominoAPI.NOTE_CLASS_ACL);
		try {
			return aclNote.getSequenceModified();
		} finally {
			aclNote.free();
		}
	}
}
