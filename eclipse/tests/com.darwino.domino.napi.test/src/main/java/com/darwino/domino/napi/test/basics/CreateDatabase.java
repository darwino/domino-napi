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
