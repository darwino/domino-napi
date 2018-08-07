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

import org.junit.Test;

import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.wrap.NSFNote;

/**
 * @author Jesse Gallagher
 * @since 1.0.1
 *
 */
@SuppressWarnings("nls")
public class ChildCountTest extends AbstractNoteTest {
	
	/**
	 * Tests the proper operation of the recursive
	 * @throws DominoException 
	 */
	@Test
	public void testChildCounts() throws DominoException {
		assertEquals("Session should initially have six children open (five databases, one note)", 6, session.getChildObjectCount());
		
		NSFNote note = database.createNote();
		try {
			assertEquals("Session should now have seven children", 7, session.getChildObjectCount());
		} finally {
			note.free();
		}
		
		assertEquals("Session should be back to six children", 6, session.getChildObjectCount());
	}
}
