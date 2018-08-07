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
package com.darwino.domino.napi.test.samples;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import static com.darwino.domino.napi.DominoAPI.*;

import org.junit.Test;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.c.IntRef;
import com.darwino.domino.napi.c.ShortRef;
import com.darwino.domino.napi.struct.COLLECTIONPOSITION;
import com.darwino.domino.napi.struct.ITEM;
import com.darwino.domino.napi.struct.ITEM_TABLE;
import com.darwino.domino.napi.test.AllTests;

/**
 * <p>This test is meant to be a Java port of the findbykeyextend.c sample from the Domino API documentation,
 * modified to use hard-coded keys.</p>
 * 
 * @author Jesse Gallagher
 *
 */
public class Views_Findbykeyextend {
	public static final String VIEW_NAME = "KeyView"; //$NON-NLS-1$
	public static final String KEY_1 = "Elvis"; //$NON-NLS-1$
	public static final double KEY_2 = 99;
	
	public static final int MALLOC_AMOUNT = 6048;
	
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", Views_Findbykeyextend.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", Views_Findbykeyextend.class.getSimpleName()); //$NON-NLS-1$
	}
	
	@Test
	public void testFindbykeyextend() throws DominoException {
		DominoAPI api = DominoAPI.get();
		
		long hDb = 0;
		long phCollection = C.malloc(C.sizeOfHCOLLECTION);
		short hCollection = 0;
		COLLECTIONPOSITION posCollection = new COLLECTIONPOSITION();
		long pNoteID;
		IntRef pNumNotesFound = new IntRef();
		int NoteCount = 0;
		// For Java, the keys are pointers to the memory locations, initialized later
		long Key1 = 0;
		long pTemp = 0, pKey = 0;
		boolean FirstTime = true;
		
		ITEM_TABLE Itemtbl = new ITEM_TABLE();
		ITEM Item = new ITEM();
		
		try {
			// No need to init
			
			// No need to have input keys - these are hard-coded
			
			hDb = api.NSFDbOpen(AllTests.TWOKEY_DB_NAME);
			int ViewID = api.NIFFindView(hDb, VIEW_NAME);
			
			hCollection = api.NIFOpenCollection(
					hDb,          // handle of db with view
					hDb,          // handle of db with data
					ViewID,       // noteID of the view
					(short)0,     // collection open flag
					NULLHANDLE,   // handle to unread ID list (input and return)
					NULLHANDLE,   // handle to open view note (return)
					null,         // universal noteID of view (return)
					NULLHANDLE,   // handle to collapsed list (return)
					NULLHANDLE    // handle to selected list (return)
					);
			
			Key1 = C.toLMBCSString(KEY_1);
			short Key1Len = (short)C.strlen(Key1, 0);
			short Item1ValueLen = (short)(Key1Len + C.sizeOfWORD);
			short Item2ValueLen = (short)(C.sizeOfDouble + C.sizeOfWORD);
			
			pKey = C.malloc(MALLOC_AMOUNT);
			
			pTemp = pKey;
			
			// line 209
			Itemtbl.setLength((short)(
					ITEM_TABLE.sizeOf +
					(2 * ITEM.sizeOf) +
					Item1ValueLen +
					Item2ValueLen
					));
			Itemtbl.setItems((short)2);
			C.memcpy(pTemp, 0, Itemtbl.getDataPtr(), 0, ITEM_TABLE.sizeOf);
			pTemp = C.ptrAdd(pTemp, ITEM_TABLE.sizeOf);
			
			// line 215
			Item.setNameLength((short)0);
			Item.setValueLength(Item1ValueLen);
			C.memcpy(pTemp, 0, Item.getDataPtr(), 0, ITEM.sizeOf);
			pTemp = C.ptrAdd(pTemp, ITEM.sizeOf);
			
			// Line 220
			Item.setNameLength((short)0);
			Item.setValueLength(Item2ValueLen);
			C.memcpy(pTemp, 0, Item.getDataPtr(), 0, ITEM.sizeOf);
			pTemp = C.ptrAdd(pTemp, ITEM.sizeOf);
			
			// Line 225
			// Write the text key indicator and value
			C.setWORD(pTemp, 0, TYPE_TEXT);
			pTemp = C.ptrAdd(pTemp, C.sizeOfWORD);
			
			C.memcpy(pTemp, 0, Key1, 0, Key1Len);
			pTemp = C.ptrAdd(pTemp, Key1Len);
			
			// Line 232
			// Write the number key indicator and value
			C.setWORD(pTemp, 0, TYPE_NUMBER);
			pTemp = C.ptrAdd(pTemp, C.sizeOfWORD);
			
			C.setDouble(pTemp, 0, KEY_2);
			pTemp = C.ptrAdd(pTemp, C.sizeOfDouble);
			
			
			// Line 240
			/* Search through the collection for the notes whose sort 
			   column values match the given search keys: */
			IntRef pNumNotesMatch = new IntRef();
			api.NIFFindByKey(
					hCollection,
					new ITEM_TABLE(pKey),
					FIND_CASE_INSENSITIVE,
					posCollection,
					pNumNotesMatch
				);
			
			// line 268
			int NumNotesMatch = pNumNotesMatch.get();
			ShortRef psignal_flag = new ShortRef();
			
			short signal_flag = 0;
			int breaker = 0;
			do {
				long hBuffer = api.NIFReadEntries(
						hCollection, // handle to this collection
						posCollection, // where to start in collection
						(FirstTime ? NAVIGATE_CURRENT : NAVIGATE_NEXT), // order to use when skipping
						FirstTime ? 0 : 1, // number to skip i
						NAVIGATE_NEXT, // order to use when reading
						NumNotesMatch - NoteCount, // max number to read
						READ_MASK_NOTEID, // info we want
						null, // length of buffer (return)
						null, // entries skipped (return)
						pNumNotesFound, // entries read (return)
						psignal_flag
						);
				assertNotEquals(hBuffer, NULLHANDLE);
				
				// line 290
				pNoteID = api.OSLockObject(hBuffer);
				int NumNotesFound = pNumNotesFound.get();
				for(int i = 0; i < NumNotesFound; i++) {
					int offset = i * C.sizeOfNOTEID;
					Platform.getInstance().log("Note count is {0}. \t noteID is: {1}", ++NoteCount, C.getNOTEID(pNoteID, offset)); //$NON-NLS-1$
				}
				
				api.OSUnlockObject(hBuffer);
				api.OSMemFree(hBuffer);
				
				signal_flag = psignal_flag.get();
				
				if(breaker++ > 100) {
					throw new RuntimeException("too many NIFReadEntries loops!"); //$NON-NLS-1$
				}
				
				FirstTime = false;
			} while((signal_flag & SIGNAL_MORE_TO_DO) != 0);
			
			api.NIFCloseCollection(hCollection);
			
			api.NSFDbClose(hDb);
			
			Platform.getInstance().log("Lookup completed successfully."); //$NON-NLS-1$
			
		} finally {
			if(phCollection != 0) { C.free(phCollection); }
			posCollection.free();
			if(pKey != 0) { C.free(pKey); }
			Itemtbl.free();
			Item.free();
			if(Key1 != 0) { C.free(Key1); }
		}
	}
}
