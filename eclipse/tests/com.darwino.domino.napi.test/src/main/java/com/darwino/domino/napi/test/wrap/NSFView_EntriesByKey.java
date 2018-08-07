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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.test.samples.Views_Findbykeyextend;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.NSFSession;
import com.darwino.domino.napi.wrap.NSFView;
import com.darwino.domino.napi.wrap.NSFViewEntry;
import com.darwino.domino.napi.wrap.NSFViewEntryCollection;
import com.darwino.domino.napi.wrap.NSFViewEntryCollection.ViewEntryVisitor;

/**
 * 
 * @author Jesse Gallagher
 *
 */
public class NSFView_EntriesByKey {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", NSFView_EntriesByKey.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", NSFView_EntriesByKey.class.getSimpleName()); //$NON-NLS-1$
	}
	
	@Test
	public void testEntriesByKey() throws Exception {
		NSFSession session = new NSFSession(DominoAPI.get());
		NSFDatabase database = null;
		NSFView view = null;
		NSFViewEntryCollection entries = null;
		try {
			database = session.getDatabase(AllTests.TWOKEY_DB_NAME);
			
			view = database.getView(Views_Findbykeyextend.VIEW_NAME);
			assertNotNull(view);
			
			entries = view.getAllEntriesByKey(Views_Findbykeyextend.KEY_1, false);
			assertEquals(entries.getCount(), 5);
			
			entries.eachEntry(new ViewEntryVisitor() {
				@Override public void visit(NSFViewEntry entry) {
					assertFalse(entry.getUniversalID().isEmpty());
				}
			});
			
		} finally {
			if(entries != null) { entries.free(); }
			if(view != null) { view.free(); }
			if(database != null) { database.free(); }
			session.free();
		}
	}
}
