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

import org.junit.Ignore;
import org.junit.Test;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.proc.NSFGETALLFOLDERCHANGESCALLBACK;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.struct.UNIVERSALNOTEID;
import com.darwino.domino.napi.test.AllTests;

@SuppressWarnings("nls")
public class Folders {

	DominoAPI api = DominoAPI.get();
	
	@Test
	@Ignore("This fails for currently-unknown reasons on Windows")
	// TODO investigate this odd failing
	public void testFolderLifecycle() throws DominoException {
		System.out.println("testFolderLifecycle start");
		
		long hDb = AllTests.getForumDocDatabase();
		TIMEDATE immemorial = new TIMEDATE();
		TIMEDATE now = new TIMEDATE();
		try {
			api.TimeDateClear(immemorial);
			
			int folderId = api.FolderCreate(hDb, hDb, 0, DominoAPI.NULLHANDLE, "Test Folder", DominoAPI.DESIGN_TYPE_SHARED, 0);
			assertNotEquals("folderId should not be 0", 0, folderId);
			
			int copiedId = api.FolderCopy(hDb, hDb, folderId, "Test Folder Copy", 0);
			assertNotEquals("copiedId should not be 0", 0, copiedId);
			
			api.FolderRename(hDb, hDb, copiedId, "Test Folder Copied Rename", 0);
			int renamedId = api.NIFFindView(hDb, "Test Folder Copied Rename");
			assertEquals("renamedId should be copiedId", copiedId, renamedId);
			
			api.FolderMove(hDb, hDb, copiedId, hDb, folderId, 0);
			int movedId = api.NIFFindView(hDb, "Test Folder\\Test Folder Copied Rename");
			assertEquals("movedId should be copiedId", copiedId, movedId);
			
			// Create a new note for testing
			long note = api.NSFNoteCreate(hDb);
			api.NSFNoteUpdate(note, (short)0);
			long noteIdPtr = C.malloc(C.sizeOfNOTEID);
			int noteId = 0;
			try {
				api.NSFNoteGetInfo(note, DominoAPI._NOTE_ID, noteIdPtr);
				noteId = C.getNOTEID(noteIdPtr, 0);
			} finally {
				C.free(noteIdPtr);
			}
			
			// Add the note to the folder
			long hTable = api.IDCreateTable(C.sizeOfNOTEID);
			try {
				api.IDInsert(hTable, noteId);
				
				api.FolderDocAdd(hDb, hDb, folderId, hTable, 0);
			} finally {
				api.IDDestroyTable(hTable);
			}
			
			// Ensure that the folder now contains the note
			hTable = api.NSFFolderGetIDTable(hDb, hDb, folderId, 0);
			try {
				assertTrue("Folder ID table should contain note ID", api.IDIsPresent(hTable, noteId));
			} finally {
				api.IDDestroyTable(hTable);
			}
			
			// Ensure that the note count is 1
			assertEquals("Folder doc count should be 1", 1, api.FolderDocCount(hDb, hDb, folderId, 0));
			
			// Ensure that the added changed contain this note
			long hAddedPtr = C.malloc(C.sizeOfDHANDLE);
			long hRemovedPtr = C.malloc(C.sizeOfDHANDLE);
			try {
				api.NSFGetFolderChanges(hDb, hDb, folderId, immemorial, 0, hAddedPtr, hRemovedPtr);
				
				long hAdded = C.getDHandle(hAddedPtr, 0);
				long hRemoved = C.getDHandle(hRemovedPtr, 0);
				try {
					System.out.println("hAdded is " + hAdded);
					System.out.println("hRemoved is " + hRemoved);
					assertEquals("No docs should have been removed", 0, api.IDEntries(hRemoved));
					assertEquals("Added should have 1 entry", 1, api.IDEntries(hAdded));
					assertTrue("Added should contain the note ID", api.IDIsPresent(hAdded, noteId));
				} finally {
					api.IDDestroyTable(hAdded);
					api.IDDestroyTable(hRemoved);
				}
			} finally {
				C.free(hAddedPtr);
				C.free(hRemovedPtr);
			}
			api.OSCurrentTIMEDATE(now);
			
			
			// Remove the note from the folder
			hTable = api.IDCreateTable(C.sizeOfNOTEID);
			try {
				api.IDInsert(hTable, noteId);
				api.FolderDocRemove(hDb, hDb, folderId, hTable, 0);	
			} finally {
				api.IDDestroyTable(hTable);
			}
			
			// Ensure that the folder now does NOT contain the note
			hTable = api.NSFFolderGetIDTable(hDb, hDb, folderId, 0);
			try {
				assertFalse("Folder ID table should not contain note ID", api.IDIsPresent(hTable, noteId));
			} finally {
				api.IDDestroyTable(hTable);
			}
			
			// Ensure that the removed changed contain this note
			hAddedPtr = C.malloc(C.sizeOfDHANDLE);
			hRemovedPtr = C.malloc(C.sizeOfDHANDLE);
			try {
				api.NSFGetFolderChanges(hDb, hDb, folderId, now, 0, hAddedPtr, hRemovedPtr);
				
				long hAdded = C.getDHandle(hAddedPtr, 0);
				long hRemoved = C.getDHandle(hRemovedPtr, 0);
				try {
					
					assertEquals("No docs should have been added", 0, api.IDEntries(hAdded));
					assertEquals("Removed should have 1 entry", 1, api.IDEntries(hRemoved));
					assertTrue("Removed should contain the note ID", api.IDIsPresent(hRemoved, noteId));
				} finally {
					api.IDDestroyTable(hAdded);
					api.IDDestroyTable(hRemoved);
				}
			} finally {
				C.free(hAddedPtr);
				C.free(hRemovedPtr);
			}
			api.OSCurrentTIMEDATE(now);
			
			// Re-add the note to the folder
			hTable = api.IDCreateTable(C.sizeOfNOTEID);
			try {
				api.IDInsert(hTable, noteId);
				
				api.FolderDocAdd(hDb, hDb, folderId, hTable, 0);
			} finally {
				api.IDDestroyTable(hTable);
			}
			
			// Remove all notes from the folder
			api.FolderDocRemoveAll(hDb, hDb, folderId, 0);
			
			// Ensure that the folder now does NOT contain the note
			hTable = api.NSFFolderGetIDTable(hDb, hDb, folderId, 0);
			try {
				assertFalse("Folder ID table should not contain note ID", api.IDIsPresent(hTable, noteId));
			} finally {
				api.IDDestroyTable(hTable);
			}

			// Ensure that the note count is 0
			assertEquals("Folder doc count should be 0", 0, api.FolderDocCount(hDb, hDb, folderId, 0));
			
			// Scan the changes
			final StringBuilder result = new StringBuilder();
			api.NSFGetAllFolderChanges(hDb, hDb, now, 0, new NSFGETALLFOLDERCHANGESCALLBACK() {
				@Override public short callback(long unidPtr, long addedNoteTable, long removedNoteTable) throws DominoException {
					UNIVERSALNOTEID unid = new UNIVERSALNOTEID(unidPtr);
					int addedCount = api.IDEntries(addedNoteTable);
					int removedCount = api.IDEntries(removedNoteTable);
					
					System.out.println("View UNID " + unid.getUNID() + ": added " + addedCount + ", removed " + removedCount);
					
					result.append("good.");
					
					return DominoAPI.NOERROR;
				}
			}, now);
			
			assertTrue("Callback should have run", result.length() > 0);
			
			api.FolderDelete(hDb, hDb, folderId, 0);
			
			int deletedId = 0;
			try {
				deletedId = api.NIFFindView(hDb, "Test Folder");
			} catch(DominoException e) {
				if(e.getStatus() == DominoAPI.ERR_NOT_FOUND) {
					// Expected
				} else {
					throw e;
				}
			}
			assertEquals("deleted ID should be 0", 0, deletedId);
		} finally {
			now.free();
			immemorial.free();
			api.NSFDbClose(hDb);
		}
		
		System.out.println("testFolderLifecycle end");
	}

}
