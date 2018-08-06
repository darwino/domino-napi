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

package com.darwino.domino.napi.test.wrap;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.enums.ItemFlag;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.wrap.*;
import com.darwino.domino.napi.wrap.NSFViewEntryCollection.ViewEntryVisitor;

@SuppressWarnings("nls")
public class WrapNamesTest {
	public static final String TEST_NAME = "CN=Jesse Gallagher/O=Frost";
	
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", WrapNamesTest.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", WrapNamesTest.class.getSimpleName()); //$NON-NLS-1$
	}
	
	
	@Test
	public void testSessionNames() throws Exception {
		NSFSession session = new NSFSession(DominoAPI.get(), TEST_NAME, false, false);
		NSFSession serverSession = new NSFSession(DominoAPI.get());
		try {
			assertEquals(serverSession.getUserName(), session.getUserName());
			assertEquals(serverSession.getUserName(), serverSession.getEffectiveUserName());
			assertNotEquals(session.getUserName(), session.getEffectiveUserName());
			assertEquals(TEST_NAME, session.getEffectiveUserName());
		} finally {
			serverSession.free();
			session.free();
		}
	}
	
	@Test
	public void testDbName() throws Exception {
		NSFSession session = new NSFSession(DominoAPI.get(), TEST_NAME, false, false);
		try {
			NSFDatabase database = session.getDatabase(AllTests.TEST_DB_NAME);
			Platform.getInstance().log("DB was opened as {0}", database.getEffectiveUserName());
			assertEquals(TEST_NAME, database.getEffectiveUserName());
		} finally {
			session.free();
		}
	}
	
	@Test
	public void testReaders() throws Exception {
		String unid;
		
		NSFSession session = new NSFSession(DominoAPI.get());
		NSFDatabase database = session.getDatabaseByHandle(AllTests.getTestDatabase(), "");
		try {
			NSFNote note = database.createNote();
			note.set("Foo", "Bar");
			note.set("Readers", "nobody");
			unid = note.getUniversalID();
			NSFItem readers = note.getFirstItem("Readers");
			readers.addFlag(ItemFlag.READERS);
			readers.addFlag(ItemFlag.NAMES);
			note.save();
			note.free();
			
			// Make sure the server session can get the document
			note = database.getNoteByUNID(unid);
			assertNotEquals(null, note);
			NSFItem item = note.getFirstItem("Readers");
			assertTrue(item.getFlags().contains(ItemFlag.READERS));
			assertEquals("nobody", item.getValue(String.class));
			
			// Make sure the server session sees it in the test view
			final ThreadLocal<Boolean> found = new ThreadLocal<Boolean>() {
				@Override protected Boolean initialValue() { return false; }
			};
			final String finalUnid = unid;
			NSFView testView = database.getView("Test View");
			testView.getAllEntries().eachEntry(new ViewEntryVisitor() {
				@Override public void visit(NSFViewEntry entry) throws Exception {
					if(entry.getUniversalID().equals(finalUnid)) {
						found.set(true);
					}
				}
			});
			assertTrue("Should have found the doc in the view", found.get());
		} finally {
			database.free();
			session.free();
		}
		
		// Make sure a user session can't retrieve the document
		{
			session = new NSFSession(DominoAPI.get(), "CN=Some Guy", false, false);
			try {
				database = session.getDatabase(AllTests.TEST_DB_NAME);
				assertEquals("CN=Some Guy", database.getEffectiveUserName());
				
				// Make sure it can't be found by UNID
				try {
					database.getNoteByUNID(unid);
				} catch(DominoException e) {
					if(e.getStatus() == DominoAPI.ERR_NOACCESS) {
						// great! That's what we want
					} else {
						throw e;
					}
				}

				// Make sure it's not in the test view
				final ThreadLocal<Boolean> found = new ThreadLocal<Boolean>() {
					@Override protected Boolean initialValue() { return false; }
				};
				final String finalUnid = unid;
				NSFView testView = database.getView("Test View");
				testView.getAllEntries().eachEntry(new ViewEntryVisitor() {
					@Override public void visit(NSFViewEntry entry) throws Exception {
						if(entry.getUniversalID().equals(finalUnid)) {
							found.set(true);
						}
					}
				});
				assertFalse("Should not have found the doc in the view", found.get());
			} finally {
				session.free();
			}
		}
		
	}
	
	@Test
	public void testCurrentUserNamesList() throws Exception {
		NSFSession session = new NSFSession(DominoAPI.get());
		try {
			String[] names = session.getUserNamesList();
			Collection<String> namesList = Arrays.asList(names);
			Platform.getInstance().log("Current user names list: {0}", namesList);
			assertTrue("namesList should contain user name", namesList.contains(session.getEffectiveUserName()));
			assertTrue("namesList should contain *", namesList.contains("*"));
		} finally {
			session.free();
		}
	}
	
	@Test
	public void testArbitraryUserNamesList() throws Exception {
		NSFSession session = new NSFSession(DominoAPI.get());
		try {
			String name = "CN=Joe Schmoe/O=Imaginary";
			String[] names = session.getUserNamesList(name);
			Collection<String> namesList = Arrays.asList(names);
			Platform.getInstance().log("Arbitrary user names list: {0}", namesList);
			assertTrue("namesList should contain " + name, namesList.contains(name));
			assertTrue("namesList should contain *", namesList.contains("*"));
			assertTrue("namesList should contain */O=Imaginary", namesList.contains("*/O=Imaginary"));
		} finally {
			session.free();
		}
	}
}
