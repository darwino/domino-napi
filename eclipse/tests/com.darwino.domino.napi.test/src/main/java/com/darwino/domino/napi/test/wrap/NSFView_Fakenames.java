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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.ibm.commons.util.io.NullOutputStream;
import com.ibm.commons.util.io.StreamUtil;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.wrap.*;
import com.darwino.domino.napi.wrap.NSFViewEntryCollection.ViewEntryVisitor;

import lotus.domino.*;

public class NSFView_Fakenames {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", NSFView_Fakenames.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", NSFView_Fakenames.class.getSimpleName()); //$NON-NLS-1$
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testFakenames() throws Exception {
		
		@SuppressWarnings("resource")
		final PrintWriter out = new PrintWriter(new NullOutputStream());
		
		final List<String> napiUNIDs = new ArrayList<String>();
		List<String> legacyUNIDs = new ArrayList<String>();
		final List<List<Object>> napiValues = new ArrayList<List<Object>>();
		List<List<Object>> legacyValues = new ArrayList<List<Object>>();
		
		long napiTime, legacyTime;
		
		// NAPI test
		{
			long startTime = System.nanoTime();
			
			NSFSession session = new NSFSession(DominoAPI.get());
			NSFDatabase database = session.getDatabase("", AllTests.FAKENAMES_DB_NAME); //$NON-NLS-1$
			NSFView view = database.getView("ByName"); //$NON-NLS-1$
			NSFViewEntryCollection entries = view.getAllEntries();
			entries.setBufferMaxEntries(400);
			final int[] holder = new int[] { 0 };
			entries.eachEntry(new ViewEntryVisitor() {
				@Override
				public void visit(NSFViewEntry entry) {
					if(holder[0]++ % 1000 == 0) {
						String unid = entry.getUniversalID();
						napiUNIDs.add(unid);
						Platform.getInstance().log("Column value map: {0}", entry.getColumnValuesMap()); //$NON-NLS-1$
						napiValues.add(new Vector<Object>(Arrays.asList(entry.getColumnValues())));
					}
				}	
			});
			entries.free();
			view.free();
			database.free();
			session.free();
			
			long endTime = System.nanoTime();
			napiTime = endTime - startTime;
			
			Platform.getInstance().log("NAPI completed in {0}ms", napiTime / 1000 / 1000); //$NON-NLS-1$
		}
		
		// Legacy test
		{
			NotesThread.sinitThread();
			long startTime = System.nanoTime();
		
			Session session = NotesFactory.createSession();
			Database database = session.getDatabase("", AllTests.FAKENAMES_DB_NAME); //$NON-NLS-1$
			View view = database.getView("ByName"); //$NON-NLS-1$
			view.setAutoUpdate(false);
			
			ViewNavigator nav = view.createViewNav();
			nav.setBufferMaxEntries(400);
			nav.setEntryOptions(ViewNavigator.VN_ENTRYOPT_NOCOUNTDATA);
			ViewEntry entry = nav.getFirst();
			int i = 0;
			while(entry != null) {
				entry.setPreferJavaDates(true);
				out.println(entry.getUniversalID());
				if(i++ % 1000 == 0) {
					String unid = entry.getUniversalID();
					legacyUNIDs.add(unid);
					legacyValues.add(entry.getColumnValues());
				}
				
				ViewEntry tempEntry = entry;
				entry = nav.getNext();
				tempEntry.recycle();
			}
			
			long endTime = System.nanoTime();
			legacyTime = endTime - startTime;
			
			Platform.getInstance().log("Legacy completed in {0}ms", legacyTime / 1000 / 1000); //$NON-NLS-1$
			NotesThread.stermThread();
		}
		
		assertEquals(legacyUNIDs, napiUNIDs);
		
		// Don't worry about dates/time in this test - they're fiddly due to having
		// their missing date or time value filled in with the current time
		for(List<Object> vals : napiValues) {
			for(int j = 0; j < vals.size(); j++) {
				Object val = vals.get(j);
				if(val instanceof Date || val instanceof TIMEDATE || val instanceof NSFDateTime) {
					vals.set(j, ""); //$NON-NLS-1$
				}
			}
		}
		for(List<Object> vals : legacyValues) {
			for(int j = 0; j < vals.size(); j++) {
				Object val = vals.get(j);
				if(val instanceof Date) {
					vals.set(j, ""); //$NON-NLS-1$
				}
			}
		}
		assertEquals(legacyValues, napiValues);
		
		
//		assertTrue(napiTime < legacyTime); // we don't want any significant performance regressions
		Platform.getInstance().log("Fakenames completed successfully."); //$NON-NLS-1$
		
		
		StreamUtil.close(out);
	}
	
	@Test
	public void testGetFirst() throws Exception {
		NSFSession session = new NSFSession(DominoAPI.get());
		try {
			NSFDatabase database = session.getDatabase("", AllTests.FAKENAMES_DB_NAME); //$NON-NLS-1$
			NSFView view = database.getView("ByName"); //$NON-NLS-1$
			NSFViewEntryCollection entries = view.getEntries(0, 1);
			try {
				assertEquals(1, entries.getCount());
				
				NSFViewEntry entry = entries.getFirst();
				assertNotNull(entry);
				
				Object[] values = entry.getColumnValues();
				assertNotEquals(0, values.length);
				assertEquals(String.class, values[0].getClass());
			} finally {
				entries.free();
			}
		} finally {
			session.free();
		}
	}
}
