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
