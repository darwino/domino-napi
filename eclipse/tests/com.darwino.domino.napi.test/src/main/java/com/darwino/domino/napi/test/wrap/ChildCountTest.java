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
