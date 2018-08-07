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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.wrap.FormulaException;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.NSFSession;

@SuppressWarnings("nls")
public class Evaluate {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", Evaluate.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", Evaluate.class.getSimpleName()); //$NON-NLS-1$
	}
	
	@Test
	public void testEvaluate() throws Exception {
		NSFSession session = new NSFSession(DominoAPI.get());
		try {
			Object[] result = session.evaluate(" 0 "); //$NON-NLS-1$
			assertEquals(1, result.length);
			assertEquals(0d, result[0]);
			
			result = session.evaluate(" hey "); //$NON-NLS-1$
			assertEquals(1, result.length);
			assertEquals("", result[0]); //$NON-NLS-1$
			
			result = session.evaluate(" 'hey string' "); //$NON-NLS-1$
			assertEquals(1, result.length);
			assertEquals("hey string", result[0]); //$NON-NLS-1$
			
			result = session.evaluate(" 'héy strıng' "); //$NON-NLS-1$
			assertEquals(1, result.length);
			assertEquals("héy strıng", result[0]); //$NON-NLS-1$
			
			result = session.evaluate(" \"foo\":\"bar\":\"baz\" "); //$NON-NLS-1$
			assertEquals(3, result.length);
			assertEquals("foo", result[0]); //$NON-NLS-1$
			assertEquals("bar", result[1]); //$NON-NLS-1$
			assertEquals("baz", result[2]); //$NON-NLS-1$
			
			NSFDatabase database = session.getDatabaseByHandle(AllTests.getTestDatabase(), ""); //$NON-NLS-1$
			try {
				NSFNote note = database.createNote();
				try {
					note.set("Foo", "bar"); //$NON-NLS-1$ //$NON-NLS-2$
					result = session.evaluate("Foo", note); //$NON-NLS-1$
					assertEquals(1, result.length);
					assertEquals("bar", result[0]); //$NON-NLS-1$
				} finally {
					note.free();
				}
			} finally {
				database.free();
			}
		} finally {
			session.free();
		}
	}
	
	@Test(expected=FormulaException.class)
	public void testInvalidFormula() throws Exception {
		NSFSession session = new NSFSession(DominoAPI.get());
		try {
			session.evaluate(" @Text( "); //$NON-NLS-1$
		} finally {
			session.free();
		}
	}
	
	@Test
	public void testEvaluateAsUser() throws Exception {
		String effectiveName = "CN=Jesse Gallagher/O=DARWINO";
		NSFSession session = new NSFSession(DominoAPI.get(), effectiveName, false, false);
		try {
			NSFDatabase database = session.getDatabase(AllTests.FAKENAMES_DB_NAME);
			NSFNote note = database.createNote();
			
			Object[] result = session.evaluate(" @UserName ", note); //$NON-NLS-1$
			System.out.println("testEvaluateAsUser @UserName is " + Arrays.asList(result));
			assertEquals("User names should match", effectiveName, result[0]);
		} finally {
			session.free();
		}
	}
}
