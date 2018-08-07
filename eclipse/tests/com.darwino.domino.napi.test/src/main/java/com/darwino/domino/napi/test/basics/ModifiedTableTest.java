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
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.proc.IDENUMERATEPROC;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.test.AllTests;

public class ModifiedTableTest {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", ModifiedTableTest.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", ModifiedTableTest.class.getSimpleName()); //$NON-NLS-1$
	}
	
	@Test
	public void testDarwinoDiscussionChanges() throws DominoException {
		String dbPath = AllTests.FAKENAMES_DB_NAME;
		String apiPath = DominoAPI.get().OSPathNetConstruct(null, "", dbPath); //$NON-NLS-1$
		long hDB = DominoAPI.get().NSFDbOpen(apiPath);
		try {
			
			TIMEDATE start = new TIMEDATE();
			TIMEDATE until = new TIMEDATE();
			try {
				{
					// Initial list of changes
					DominoAPI.get().TimeConstant(DominoAPI.TIMEDATE_WILDCARD, start);
					long hTable = DominoAPI.get().NSFDbGetModifiedNoteTable(hDB, DominoAPI.NOTE_CLASS_DOCUMENT, start, until);
					try {
						int nEntries = hTable!=0 ? DominoAPI.get().IDEntries(hTable) : 0;
						Platform.getInstance().log("Initial Table #entries={0}",nEntries); //$NON-NLS-1$
						if(hTable!=0) {
							DominoAPI.get().IDEnumerate(hTable, new IDENUMERATEPROC() {
								int i = 0;
								@Override
								public short callback(int id) {
									if(i++ % 1000 == 0) {
										Platform.getInstance().log("  ID#{0}={1}",i,id); //$NON-NLS-1$
									}
									return 0;
								}
							});
						}
					} finally {
						DominoAPI.get().IDDestroyTable(hTable);
					}
				}
				
				// Next list of changes
				{
					C.memcpy(start.getDataPtr(), 0, until.getDataPtr(), 0, TIMEDATE.sizeOf);
					long hTable = DominoAPI.get().NSFDbGetModifiedNoteTable(hDB, DominoAPI.NOTE_CLASS_DOCUMENT, start, until);
					try {
						int nEntries = hTable!=0 ? DominoAPI.get().IDEntries(hTable) : 0;
						Platform.getInstance().log("Next Table #entries={0}",nEntries); //$NON-NLS-1$
						if(hTable!=0) {
							DominoAPI.get().IDEnumerate(hTable, new IDENUMERATEPROC() {
								int i = 0;
								@Override
								public short callback(int id) {
									Platform.getInstance().log("  ID#{0}={1}",i++,id); //$NON-NLS-1$
									return 0;
								}
							});
						}
					} finally {
						DominoAPI.get().IDDestroyTable(hTable);
					}
				}
			} finally {
				start.free();
				until.free();
			}
		} finally {
			DominoAPI.get().NSFDbClose(hDB);
		}
	}
}
