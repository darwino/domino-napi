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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.wrap.NSFDateTime;
import com.darwino.domino.napi.wrap.NSFFolder;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.NSFNoteIDCollection;
import com.darwino.domino.napi.wrap.NSFView;

@SuppressWarnings("nls")
public class WrapFolders extends AbstractNoteTest {

	@Test
	public void testFolderLifecycle() throws DominoException {
		System.out.println("testFolderLifecycle start");
		
		NSFView folder = forumDocs.createFolder("Wrap Test Folder", false);
		assertNotEquals("folder should not be null", null, folder);
		assertTrue("folder should be a folder", folder.isFolder());
		assertEquals("folder title should be Wrap Test Folder", "Wrap Test Folder", folder.getTitle());
		
		NSFView copied = folder.asFolder().copy("Wrap Test Folder Copy");
		assertNotEquals("copied should not be null", null, copied);
		assertTrue("copied should be a folder", copied.isFolder());
		
		copied.asFolder().rename("Wrap Test Folder Copied Rename");
		NSFView renamed = forumDocs.getView("Wrap Test Folder Copied Rename");
		assertNotEquals("renamed should not be null", null, renamed);
		assertEquals("renamed ID should be copied ID", copied.getNoteID(), renamed.getNoteID());
		
		copied.asFolder().move(folder);
		NSFView moved = forumDocs.getView("Wrap Test Folder\\Wrap Test Folder Copied Rename");
		assertNotEquals("moved should not be null", null, moved);
		assertEquals("moved ID should be copied ID", copied.getNoteID(), moved.getNoteID());

		// Create a new note for testing
		NSFNote note = forumDocs.createNote();
		note.save();
		
		// Add the note to the folder
		folder.asFolder().add(note);
		assertTrue("folder should contain note", folder.asFolder().contains(note));
		assertEquals("folder doc count should be 1", 1, folder.asFolder().size());
		assertEquals("folder note ID count should be 1", 1, folder.asFolder().getNoteCount());
		
		
		// Ensure that the added changed contain this note
		NSFFolder.FolderChanges changes = folder.asFolder().getChanges(null);
		try {
			NSFNoteIDCollection added = changes.getAdded();
			NSFNoteIDCollection removed = changes.getRemoved();
			assertEquals("No docs should have been removed", 0, removed.size());
			assertEquals("Added should have 1 entry", 1, added.size());
			assertTrue("Added should contain the note ID", added.contains(note.getNoteID()));
		} finally {
			changes.free();
		}
		NSFDateTime now = NSFDateTime.now();
		
		// Remove the note from the folder
		folder.asFolder().remove(note);
		assertFalse("folder should not contain note", folder.asFolder().contains(note));
		assertEquals("folder doc count should be 0", 0, folder.asFolder().size());
		assertEquals("folder note ID count should be 0", 0, folder.asFolder().getNoteCount());
		
		// Ensure that the removed changed contain this note
		changes = folder.asFolder().getChanges(now);
		try {
			NSFNoteIDCollection added = changes.getAdded();
			NSFNoteIDCollection removed = changes.getRemoved();
			assertEquals("No docs should have been added", 0, added.size());
			assertEquals("Removed should have 1 entry", 1, removed.size());
			assertTrue("Removed should contain the note ID", removed.contains(note.getNoteID()));
		} finally {
			changes.free();
		}
		now = NSFDateTime.now();
		
		// Re-add the note to the folder
		folder.asFolder().add(note);
		
		// Remove all notes from the folder
		folder.asFolder().clear();
		assertFalse("folder should not contain note", folder.asFolder().contains(note));
		assertEquals("folder doc count should be 0", 0, folder.asFolder().size());
		assertEquals("folder note ID count should be 0", 0, folder.asFolder().getNoteCount());
		
		folder.asFolder().delete();
		folder.free();
		
		folder = null;
		try {
			folder = forumDocs.getView("Wrap Test Folder");
		} catch(DominoException e) {
			if(e.getStatus() == DominoAPI.ERR_NOT_FOUND) {
				// Expected
			} else {
				throw e;
			}
		}
		assertEquals("folder should be null", null, folder);

		System.out.println("testFolderLifecycle end");
	}

	@Test
	public void testAliases() throws Exception {
		System.out.println("testAliases start");
		
		NSFView folder = database.createFolder("Foo|Bar|Baz", false);
		assertNotEquals("folder should not be null", null, folder);
		assertEquals("folder name should be Foo", "Foo", folder.getTitle());
		List<String> aliases = Arrays.asList("Bar", "Baz");
		assertEquals("folder aliases should be Bar, Baz", aliases, folder.getAliases());
		
		for(String name : new String[] { "Foo", "Bar", "Baz" }) {
			NSFView namedFolder = database.getView(name);
			assertNotEquals("namedFolder should not be null for " + name, null, namedFolder);
			assertEquals("nameFolder should be the same note as folder for " + name, folder.getNoteID(), namedFolder.getNoteID());
		}
		
		
		System.out.println("testAliases end");
	}
	
	@Test
	public void testFolderContainment() throws Exception {
		System.out.println("testFolderContainment start");

		NSFView one = database.createFolder("View One", false);
		NSFView two = database.createFolder("View Two", false);
		NSFView three = database.createFolder("View Three", false);
		
		NSFNote note = database.createNote();
		note.save();
		NSFNote note2 = database.createNote();
		note2.save();
		
		one.asFolder().add(note);
		two.asFolder().add(note2);
		three.asFolder().add(note2);
		
		{
			List<NSFView> folders = note.getContainingFolders();
			List<String> names = new ArrayList<String>(folders.size());
			for(NSFView folder : folders) {
				names.add(folder.getTitle());
			}
			assertEquals("names should contain one entry", 1, names.size());
			assertEquals("names[0] should be View One", "View One", names.get(0));
		}
		{
			List<NSFView> folders = note2.getContainingFolders();
			List<String> names = new ArrayList<String>(folders.size());
			for(NSFView folder : folders) {
				names.add(folder.getTitle());
			}
			assertEquals("names should contain two entries", 2, names.size());
			assertTrue("names should contain View Two", names.contains("View Two"));
			assertTrue("names should contain View Three", names.contains("View Three"));
		}
		
		
		System.out.println("testFolderContainment end");
	}
	
	@Test
	public void findNewFolder() throws Exception {
		System.out.println("findNewFolder end");
		
		forumDocs.createFolder("findNewFolder", false);
		List<NSFView> folders = forumDocs.getFolders();
		boolean found = false;
		for(NSFView view : folders) {
			if(view.getTitle().equals("findNewFolder") && view.isFolder()) {
				found = true;
				break;
			}
		}
		assertTrue("findNewFolder should be found", found);
		
		System.out.println("findNewFolder end");
	}
	
	@Test
	public void findNewFolderViewCache() throws Exception {
		System.out.println("findNewFolderViewCache end");
		
		forumDocs.createFolder("findNewFolderViewCache", false);
		forumDocs.getViews();
		List<NSFView> folders = forumDocs.getFolders();
		boolean found = false;
		for(NSFView view : folders) {
			if(view.getTitle().equals("findNewFolderViewCache") && view.isFolder()) {
				found = true;
				break;
			}
		}
		assertTrue("findNewFolderViewCache should be found", found);
		
		System.out.println("findNewFolderViewCache end");
	}
}
