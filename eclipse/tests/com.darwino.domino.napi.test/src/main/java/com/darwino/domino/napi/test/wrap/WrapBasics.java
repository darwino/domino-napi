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
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.enums.TimeConstant;
import com.darwino.domino.napi.struct.UNIVERSALNOTEID;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.wrap.*;

@SuppressWarnings("nls")
public class WrapBasics {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", WrapBasics.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", WrapBasics.class.getSimpleName()); //$NON-NLS-1$
	}
	
	
	@Test
	public void testCreateUNID() throws Exception {
		UNIVERSALNOTEID unid = new UNIVERSALNOTEID();
		try {
			unid.setUNID(AllTests.FAKENAMES_DOC_UNID);
			assertEquals(AllTests.FAKENAMES_DOC_UNID, unid.getUNID());
		} finally {
			unid.free();
		}
	}
	
	@Test
	public void testGetNoteByUNID() throws Exception {
		NSFSession session = new NSFSession(DominoAPI.get());
		NSFDatabase database = null;
		NSFNote note = null;
		try {
			database = session.getDatabase("", AllTests.FAKENAMES_DB_NAME); //$NON-NLS-1$
			note = database.getNoteByUNID(AllTests.FAKENAMES_DOC_UNID);
			assertNotNull(note);
			assertEquals(note.getUniversalID(), AllTests.FAKENAMES_DOC_UNID);
		} finally {
			if(note != null) { note.free(); }
			if(database != null) { database.free(); }
			session.free();
		}
	}
	
	@Test
	public void testGetNoteByKey() throws Exception {
		NSFSession session = new NSFSession(DominoAPI.get());
		NSFDatabase database = null;
		NSFView view = null;
		NSFNote note = null;
		try {
			database = session.getDatabase("", AllTests.FAKENAMES_DB_NAME); //$NON-NLS-1$
			view = database.getView("ByName"); //$NON-NLS-1$
			note = view.getFirstNoteByKey("Adams", false); //$NON-NLS-1$
			assertNotNull(note);
		} finally {
			if(note != null) { note.free(); }
			if(view != null) { view.free(); }
			if(database != null) { database.free(); }
			session.free();
		}
		
	}
	
	@Test
	public void testCurrentUserName() throws DominoException {
		NSFSession session = new NSFSession(DominoAPI.get());
		try {
			String userName = session.getUserName();
			
			Platform.getInstance().log("Username is {0}", userName); //$NON-NLS-1$
			
			assertNotNull("Username should not be null", userName); //$NON-NLS-1$
			assertFalse("Username should not be empty", userName.isEmpty()); //$NON-NLS-1$
		} finally {
			session.free();
		}
	}
	
	@Test
	public void testReplicaId() throws Exception {
		NSFSession session = new NSFSession(DominoAPI.get());
		try {
			NSFDatabase database = new NSFDatabase(session, AllTests.getForumDocDatabase(), "", true); //$NON-NLS-1$
			try {
				assertEquals(AllTests.FORUM_DOC_DB_REPLICAID, database.getReplicaId());
			} finally {
				database.free();
			}
		} finally {
			session.free();
		}
	}
	
	@Test
	public void testDatabaseList() throws Exception {
		System.out.println("testDatabaseList start");
		
		NSFSession session = new NSFSession(DominoAPI.get());
		try {
			long start = System.nanoTime();
			String[] databases = session.getChangedDBPaths(null, NSFDateTime.fromTimeConstant(TimeConstant.MINIMUM));
			long end = System.nanoTime();
			System.out.println("Getting database list took " + ((end - start) / 1000 / 1000) + "ms");
			assertNotEquals("databases should not be null", null, databases);
			assertNotEquals("databases should not be empty", 0, databases.length);
			List<String> databasesList = Arrays.asList(databases);
			System.out.println("Databases list is " + databasesList);
			assertTrue("databases should contain names.nsf", databasesList.contains("names.nsf"));
		} finally {
			session.free();
		}
		
		System.out.println("testDatabaseList start");
	}
	
	@Test
	public void testFormulaCompileDecompile() throws Exception {
		System.out.println("testFormulaCompileDecompile start");
		
		String FORMULA_TEXT_1 = "SELECT Form=\"Form 1\"";
		String FORMULA_TEXT_2 = "@Text(Foo)";

		NSFSession session = new NSFSession(DominoAPI.get());
		try {
			{
				NSFFormula formula = session.compileFormula(FORMULA_TEXT_1);
				assertNotEquals("formula should not be null", null, formula);
				assertEquals("formula text should match", FORMULA_TEXT_1, formula.getFormulaText(true));
			}
			{
				NSFFormula formula = session.compileFormula(FORMULA_TEXT_2);
				assertNotEquals("formula should not be null", null, formula);
				assertEquals("formula text should match", FORMULA_TEXT_2, formula.getFormulaText(false));
			}
		} finally {
			session.free();
		}
		
		System.out.println("testFormulaCompileDecompile end");
	}
	
	@Test
	public void testGetServerList() throws Exception {
		System.out.println("testGetServerList start");
		
		NSFSession session = new NSFSession(DominoAPI.get());
		try {
			Collection<String> servers = session.getServerList(null);
			System.out.println("Known server list: " + servers);
		} finally {
			session.free();
		}
		
		System.out.println("testGetServerList end");
		
	}
}
