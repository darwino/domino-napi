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

package com.darwino.domino.napi.test.basics;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.test.AllTests;

@SuppressWarnings("nls")
public class Documents {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", Documents.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", Documents.class.getSimpleName()); //$NON-NLS-1$
	}
	
	@Test
	public void testCreateDocument() throws DominoException {
		long hDb = AllTests.getTestDatabase();
		try {
			long hNote = DominoAPI.get().NSFNoteCreate(hDb);
			try {
				DominoAPI.get().NSFItemSetText(hNote, "Form", "Test Doc"); //$NON-NLS-1$ //$NON-NLS-2$
				DominoAPI.get().NSFNoteUpdate(hNote, (short)0);
			} finally {
				DominoAPI.get().NSFNoteClose(hNote);
			}
		} finally {
			DominoAPI.get().NSFDbClose(hDb);
		}
	}
	
	@Test
	public void testSetText() throws DominoException {
		Platform.getInstance().log("testSetText start");
		
		long hDb = AllTests.getTestDatabase();
		try {
			long hNote = DominoAPI.get().NSFNoteCreate(hDb);
			try {
				DominoAPI.get().NSFItemSetText(hNote, "Form", "Test Doc");
				String val = DominoAPI.get().NSFItemConvertToText(hNote, "Form", (short)100, '\n');
				System.out.println("Got text value: " + val);
				assertEquals("val should be 'Test Doc'", "Test Doc", val);
			} finally {
				DominoAPI.get().NSFNoteClose(hNote);
			}
		} finally {
			DominoAPI.get().NSFDbClose(hDb);
		}
		
		Platform.getInstance().log("testSetText end");
	}
}
