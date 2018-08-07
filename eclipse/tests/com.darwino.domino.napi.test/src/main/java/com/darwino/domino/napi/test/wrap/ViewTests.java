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
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.NSFView;
import com.darwino.domino.napi.wrap.NSFViewEntry;
import com.darwino.domino.napi.wrap.NSFViewEntryCollection;
import com.darwino.domino.napi.wrap.NSFViewEntryCollection.ViewEntryVisitor;

/**
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
@SuppressWarnings("nls")
public class ViewTests extends AbstractNoteTest {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", ViewTests.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", ViewTests.class.getSimpleName()); //$NON-NLS-1$
	}
	
	@Test
	public void testViewIterate() throws Exception {
		Platform.getInstance().log("testViewIterate start");
		
		NSFDatabase database = session.getDatabaseByHandle(AllTests.getForumDocDatabase(), ""); //$NON-NLS-1$
		NSFView view = database.getView("ReplicationHistory");
		assertNotEquals("View should not be null", null, view);
		try {
			NSFViewEntryCollection entries = view.getAllEntries();
			assertNotEquals("entries should not be null", null, entries);
			try {
				entries.eachEntry(new ViewEntryVisitor() {
					@Override public void visit(NSFViewEntry entry) throws Exception {
						Platform.getInstance().log("Found entry {0}", entry);
						Platform.getInstance().log("Entry vals are {0}", Arrays.asList(entry.getColumnValues()));
					}
				});
			} finally {
				entries.free();
			}
		} finally {
			view.free();
			database.free();
		}
		
		Platform.getInstance().log("testViewIterate end");
	}

	@Test
	public void testMultiKeyLookup() throws DominoException {
		NSFDatabase database = session.getDatabaseByHandle(AllTests.getForumDocDatabase(), ""); //$NON-NLS-1$
		NSFView view = database.getView("ReplicationHistory");
		assertNotEquals("View should not be null", null, view);
		try {
			Platform.getInstance().log("View note ID is {0}", Integer.toString(view.getNoteID(), 16));
			assertEquals("View should have 5 entries", 5, view.getEntryCount());
			List<String> keys = Arrays.asList("domdisc", "cleantest2.nsf", "CN=Pelias-L/O=Frost", "unid:2683C2D53F72476E9F8437524C377D03", "d41d8cd98f00b204e9800998ecf8427e");
			NSFNote note = view.getFirstNoteByKey(keys, true);
			assertNotEquals("Note should not be null", null, note);
			try {
				assertEquals("Note UNID should be 8B78FFD2B101DB6305257F32007C9B8E", "8B78FFD2B101DB6305257F32007C9B8E", note.getUniversalID());
			} finally {
				note.free();
			}
		} finally {
			view.free();
			database.free();
		}
	}
	
	@Test
	public void testMultiKeyLookupDarwinoSync() throws DominoException {
		NSFDatabase database = session.getDatabaseByHandle(AllTests.getDarwinoSyncDatabase(), ""); //$NON-NLS-1$
		NSFView view = database.getView("ReplicationHistory");
		assertNotEquals("View should not be null", null, view);
		try {
			Platform.getInstance().log("View note ID is {0}", Integer.toString(view.getNoteID(), 16));
			assertEquals("View should have four entries", 4, view.getEntryCount());
			List<String> keys = Arrays.asList("time_effort", "", "CN=CGU_SRV9/O=WGCDEV/C=CH", "unid:C7D6C7983C9349678EE0700F17A8C168", "70a208d86199889b0bf825babb38699b");
			NSFNote note = view.getFirstNoteByKey(keys, true);
			assertNotEquals("Note should not be null", null, note);
			try {
				assertEquals("Note UNID should be 0B1BB57FA7237A81C1257F33002A42DB", "0B1BB57FA7237A81C1257F33002A42DB", note.getUniversalID());
			} finally {
				note.free();
			}
		} finally {
			view.free();
			database.free();
		}
	}
	
	@Test
	public void testEmptyColumns() throws Exception {
		Platform.getInstance().log("testEmptyColumns start");
		
		NSFDatabase database = session.getDatabaseByHandle(AllTests.getEdgeCasesDatabase(), "");
		NSFView view = database.getView("Empty Items View");
		assertNotEquals("View should not be null", null, view);
		try {
			NSFViewEntryCollection entries = view.getAllEntries();
			entries.setReadColumnNames(true);
			NSFViewEntry entry = entries.getFirst();
			assertNotEquals("entry should not be null", null, entry);
			assertEquals("UNID should be 179F4A276F56D16C05257F700002AAA0", "179F4A276F56D16C05257F700002AAA0", entry.getUniversalID());
			Map<String, Object> columnValues = entry.getColumnValuesMap();
			Platform.getInstance().log("columnValues is {0}", columnValues);
			assertTrue("columnValues should contain key notPresentItem", columnValues.containsKey("notPresentItem"));
			assertEquals("notPresentItem should be null", null, columnValues.get("notPresentItem"));
			assertTrue("columnValues should contain key notPresentItem2", columnValues.containsKey("notPresentItem2"));
			assertEquals("notPresentItem2 should be null", null, columnValues.get("notPresentItem2"));
			assertTrue("columnValues should contain key presentItem", columnValues.containsKey("presentItem"));
			assertEquals("presentItem should be Foo", "Foo", columnValues.get("presentItem"));
			assertTrue("columnValues should contain key presentItem2", columnValues.containsKey("presentItem2"));
			assertEquals("presentItem2 should be Bar", "Bar", columnValues.get("presentItem2"));
		} finally {
			view.free();
		}

		Platform.getInstance().log("testEmptyColumns end");
	}
	
	@Test
	public void testEmptyColumnValues() throws Exception {
		Platform.getInstance().log("testEmptyColumnValues start");
		
		NSFDatabase database = session.getDatabaseByHandle(AllTests.getEdgeCasesDatabase(), "");
		NSFView view = database.getView("Empty Items View");
		assertNotEquals("View should not be null", null, view);
		try {
			NSFViewEntryCollection entries = view.getAllEntries();
			entries.setReadColumnNames(false);
			NSFViewEntry entry = entries.getFirst();
			assertNotEquals("entry should not be null", null, entry);
			assertEquals("UNID should be 179F4A276F56D16C05257F700002AAA0", "179F4A276F56D16C05257F700002AAA0", entry.getUniversalID());
			Object[] columnValues = entry.getColumnValues();
			assertEquals("columnValues should have four entries", 4, columnValues.length);
			assertEquals("columnValues[0] should be null", null, columnValues[0]);
			assertEquals("columnValues[1] should be null", null, columnValues[1]);
			assertEquals("columnValues[2] should be Foo", "Foo", columnValues[2]);
			assertEquals("columnValues[3] should be Bar", "Bar", columnValues[3]);
		} finally {
			view.free();
		}

		Platform.getInstance().log("testEmptyColumnValues end");
	}
	
	/**
	 * NSFView.getFirstNoteByKey should return null in the case of an un-found doc,
	 * rather than throwing an exception.
	 */
	@Test
	public void testNotFoundLookup() throws Exception {
		Platform.getInstance().log("testNotFoundLookup start");

		NSFDatabase database = session.getDatabaseByHandle(AllTests.getTwokeyDatabase(), "");
		NSFView view = database.getView("KeyView");
		assertNotEquals("KeyView should not be null", null, view);
		try {
			Object[] keys = new Object[] { "foo", "bar" };
			NSFNote note = view.getFirstNoteByKey(keys, true);
			assertEquals("note should be null", null, note);
		} finally {
			view.free();
		}
		
		Platform.getInstance().log("testNotFoundLookup end");
	}
	
	@Test
	public void testEntriesNotFoundLookup() throws Exception {
		Platform.getInstance().log("testNotFoundLookup start");

		NSFDatabase database = session.getDatabaseByHandle(AllTests.getTwokeyDatabase(), "");
		NSFView view = database.getView("KeyView");
		assertNotEquals("KeyView should not be null", null, view);
		try {
			Object[] keys = new Object[] { "foo", "bar" };
			NSFViewEntryCollection entries = view.getAllEntriesByKey(keys, true);
			assertEquals("entriesshould be null", null, entries);
		} finally {
			view.free();
		}
		
		Platform.getInstance().log("testNotFoundLookup end");
	}
	
	@Test
	public void testUpdateView() throws Exception {
		Platform.getInstance().log("testUpdateView start");

		NSFDatabase database = session.getDatabaseByHandle(AllTests.getTwokeyDatabase(), "");
		NSFView view = database.getView("KeyView");
		assertNotEquals("KeyView should not be null", null, view);
		try {
			String key = "NotYetFound";
			NSFNote note = view.getFirstNoteByKey(key, true);
			assertEquals("note should initially be null", null, note);
			
			NSFNote created = database.createNote();
			created.set("text", key);
			created.save();
			
			note = view.getFirstNoteByKey(key, true);
			assertEquals("note should still be null pre-update", null, note);
			
			view.refresh();
			
			note = view.getFirstNoteByKey(key, true);
			assertNotEquals("note should no longer be null post-update", null, note);
		} finally {
			view.free();
		}
		
		Platform.getInstance().log("testUpdateView end");
	}
}
