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
