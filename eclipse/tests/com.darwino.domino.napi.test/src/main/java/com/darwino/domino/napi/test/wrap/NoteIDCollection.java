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
package com.darwino.domino.napi.test.wrap;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.util.DominoNativeUtils;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.NSFNoteIDCollection;
import com.darwino.domino.napi.wrap.NSFSession;

public class NoteIDCollection {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", NoteIDCollection.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", NoteIDCollection.class.getSimpleName()); //$NON-NLS-1$
	}
	
	NSFSession session;
	NSFDatabase database;
	
	@Before
	public void beforeTests() throws DominoException {
		session = new NSFSession(DominoAPI.get());
		database = session.getDatabaseByHandle(AllTests.getTestDatabase(), ""); //$NON-NLS-1$
	}
	@After
	public void afterTests() throws DominoException {
		database.free();
		session.free();
	}
	
	@Test
	public void createCollection() throws DominoException {
		NSFNoteIDCollection notes = session.createNoteIDCollection();
		try {
			assertEquals(0, notes.size());
			
			int[] createdIds = populateCollection(notes, 3);
			
			assertEquals(createdIds.length, notes.size());
			assertTrue(notes.contains(createdIds[0]));
			assertTrue(notes.containsAll(Arrays.asList(DominoNativeUtils.toObjectArray(createdIds))));
			
			notes.remove(createdIds[0]);
			assertEquals(createdIds.length-1, notes.size());
			assertFalse(notes.contains(createdIds[0]));
		} finally {
			notes.free();
		}
	}
	
	@Test
	public void iterate() throws DominoException {
		NSFNoteIDCollection notes = session.createNoteIDCollection();
		try {
			assertEquals(0, notes.size());
			
			int[] createdIds = populateCollection(notes, 3);
			
			int i = 0;
			int[] iteratedIds = new int[createdIds.length];
			for(Integer noteId : notes) {
				iteratedIds[i++] = noteId;
			}
			
			assertArrayEquals(createdIds, iteratedIds);
		} finally {
			notes.free();
		}
	}
	
	// Since the underlying ID* functions seem to behave poorly when removing an ID during
	// a scan, the wrapping Iterator should declare that unsupported
	@Test(expected=UnsupportedOperationException.class)
	public void iterateRemove() throws DominoException {
		NSFNoteIDCollection notes = session.createNoteIDCollection();
		try {
			assertEquals(0, notes.size());
			
			populateCollection(notes, 3);
			
			Iterator<Integer> iterator = notes.iterator();
			while(iterator.hasNext()) {
				iterator.next();
				iterator.remove();
			}
		} finally {
			notes.free();
		}
	}
	
	@Test
	public void retainAll() throws DominoException {
		NSFNoteIDCollection notes = session.createNoteIDCollection();
		try {
			assertEquals(0, notes.size());
			
			int[] createdIds = populateCollection(notes, 3);
			List<Integer> toRetain = Arrays.asList(createdIds[0]);
			
			notes.retainAll(toRetain);
			assertEquals(1, notes.size());
			assertTrue(notes.contains(toRetain.get(0)));
		} finally {
			notes.free();
		}
	}
	
	@Test(expected=ConcurrentModificationException.class)
	public void concurrentModification() throws DominoException {
		NSFNoteIDCollection notes = session.createNoteIDCollection();
		try {
			assertEquals(0, notes.size());
			
			populateCollection(notes, 3);
			
			Iterator<Integer> iterator = notes.iterator();
			while(iterator.hasNext()) {
				int noteId = iterator.next();
				notes.remove(noteId);
			}
		} finally {
			notes.free();
		}
	}
	
	public void modifiedNoteCollection() throws DominoException {
		TIMEDATE now = new TIMEDATE();
		try {
			now.fromJavaDate(new Date());
			int[] createdIds = new int[3];
			for(int i = 0; i < 3; i++) {
				NSFNote note = database.createNote();
				createdIds[i] = note.getNoteID();
				note.save();
				note.free();
			}
			
			NSFNoteIDCollection notes = database.getModifiedNotes(DominoAPI.NOTE_CLASS_DOCUMENT, now);
			try {
				assertEquals(3, notes.size());
				assertTrue(notes.containsAll(Arrays.asList(DominoNativeUtils.toObjectArray(createdIds))));
			} finally {
				notes.free();
			}
		} finally {
			now.free();
		}
	}
	
	private int[] populateCollection(NSFNoteIDCollection notes, int count) throws DominoException {
		int[] createdIds = new int[count];
		for(int i = 0; i < createdIds.length; i++) {
			NSFNote note = database.createNote();
			note.save();
			createdIds[i] = note.getNoteID();
			note.free();
			
			notes.add(createdIds[i]);
		}
		return createdIds;
	}
}
