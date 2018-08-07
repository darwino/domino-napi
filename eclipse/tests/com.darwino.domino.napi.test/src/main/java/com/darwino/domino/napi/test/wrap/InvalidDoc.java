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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.NSFSession;

/**
 * These tests cover the NAPI's behavior when dealing with an invalid document (one with
 * summary data exceeding 32k).
 * 
 * @author Jesse Gallagher
 *
 */
public class InvalidDoc {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", InvalidDoc.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", InvalidDoc.class.getSimpleName()); //$NON-NLS-1$
	}
	
	DominoAPI api;
	NSFSession session;
	NSFDatabase database;
	
	@Before
	public void beforeTests() throws DominoException {
		api = DominoAPI.get();
		session = new NSFSession(api);
		database = session.getDatabaseByHandle(AllTests.getEdgeCasesDatabase(), ""); //$NON-NLS-1$
	}
	@After
	public void afterTests() throws DominoException {
		database.free();
		session.free();
	}
	
	@Test(expected=DominoException.class)
	@Ignore("This document likely no longer triggers this exception in 9.0.1FP8")
	public void openNote() throws DominoException {
		NSFNote note = null;
		try {
			note = database.getNoteByUNID(AllTests.INVALID_DOC_UNID);
		} catch(DominoException e) {
			if(e.getStatus() == DominoAPI.ERR_SUMMARY_TOO_BIG) {
				Platform.getInstance().log("Got expected DominoException \"{0}\"", e.getMessage()); //$NON-NLS-1$
				throw e;
			} else {
				throw new RuntimeException("Unexpected DominoException", e); //$NON-NLS-1$
			}
		} finally {
			if(note != null) {
				note.free();
			}
		}
	}
}
