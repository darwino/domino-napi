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
