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

import org.junit.Test;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.wrap.FormulaException;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.NSFSession;

import static org.junit.Assert.*;

public class AltNames {
	@Test
	public void altSessioName() throws DominoException, FormulaException {
		String customName = "CN=Joe Schmoe/O=SomeOrg";
		NSFSession session = new NSFSession(DominoAPI.get(), customName, false, false);
		try {
			NSFDatabase db = session.getDatabase("", AllTests.DESIGN_DB_NAME);
			Object[] un = session.evaluate("@UserName", db.createNote());
			assertNotEquals("Eval result shoulld not be null", null, un);
			assertEquals("Eval result should have one entry", 1, un.length);
			assertEquals("Name should match", customName, un[0]);
		} finally {
			session.free();
		}
	}
}
