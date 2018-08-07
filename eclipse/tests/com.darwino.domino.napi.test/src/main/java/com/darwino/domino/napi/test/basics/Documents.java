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
