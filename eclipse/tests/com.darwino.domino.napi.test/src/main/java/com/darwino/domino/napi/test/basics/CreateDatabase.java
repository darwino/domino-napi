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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.test.AllTests;

public class CreateDatabase {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", CreateDatabase.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", CreateDatabase.class.getSimpleName()); //$NON-NLS-1$
	}
	
	@Test
	public void testCreateDatabase() throws DominoException {
		try {
			String path = DominoAPI.get().OSPathNetConstruct(null, "", AllTests.TEST_DB_NAME); //$NON-NLS-1$
			DominoAPI.get().NSFDbCreate(path, DominoAPI.DBCLASS_BY_EXTENSION, false);
			
			// Crack open the DB to enable uniform access for reader tests
			long hDb = DominoAPI.get().NSFDbOpen(path);
			long hACL = DominoAPI.get().ACLCreate();
			try {
				DominoAPI.get().ACLSetFlags(hACL, DominoAPI.ACL_UNIFORM_ACCESS);
				DominoAPI.get().NSFDbStoreACL(hDb, hACL, 0, (short)0);
			} finally {
				DominoAPI.get().OSMemFree(hACL);
			}
			DominoAPI.get().NSFDbClose(hDb);
		} catch(DominoException e) {
			if(e.getStatus() == 260) {
				// This means it already exists, which is fine
				return;
			} else {
				throw e;
			}
		}
	}
}
