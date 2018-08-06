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
import static com.darwino.domino.napi.DominoAPI.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.ibm.commons.util.io.StreamUtil;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.ObjectType;
import com.darwino.domino.napi.enums.ValueType;
import com.darwino.domino.napi.proc.NSFITEMSCANPROC;
import com.darwino.domino.napi.struct.FILEOBJECT;
import com.darwino.domino.napi.struct.OBJECT_DESCRIPTOR;
import com.darwino.domino.napi.struct.OID;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.NSFDateTime;
import com.darwino.domino.napi.wrap.NSFItem;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.NSFNoteIDCollection;

@SuppressWarnings("nls")
public class Note extends AbstractNoteTest {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", Note.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", Note.class.getSimpleName()); //$NON-NLS-1$
	}
	
	
	@Test
	public void setString() throws DominoException {
		Platform.getInstance().log("setString start"); //$NON-NLS-1$
		
		note.set("Foo", "bar"); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue("Item Foo should exist", note.hasItem("Foo"));
		Object[] result = note.get("Foo"); //$NON-NLS-1$
		assertEquals(1, result.length);
		assertEquals("bar", result[0]); //$NON-NLS-1$

		Platform.getInstance().log("setString end"); //$NON-NLS-1$
	}
	
	@Test
	public void setStringArray() throws DominoException {
		Platform.getInstance().log("setStringArray start"); //$NON-NLS-1$
		
		note.set("Foo1", Arrays.asList("foo", "bar", "baz")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		Object[] result = note.get("Foo1"); //$NON-NLS-1$
		assertEquals(3, result.length);
		assertEquals("foo", result[0]); //$NON-NLS-1$
		assertEquals("bar", result[1]); //$NON-NLS-1$
		assertEquals("baz", result[2]); //$NON-NLS-1$

		Platform.getInstance().log("setStringArray end"); //$NON-NLS-1$
	}
	
	@Test
	public void setNumber() throws DominoException {
		Platform.getInstance().log("setNumber start"); //$NON-NLS-1$
		
		note.set("Foo2", 1); //$NON-NLS-1$
		Object[] result = note.get("Foo2"); //$NON-NLS-1$
		assertEquals(1, result.length);
		assertEquals(1d, result[0]);

		Platform.getInstance().log("setNumber end"); //$NON-NLS-1$
	}
	
	@Test
	public void setNumberArray() throws DominoException {
		Platform.getInstance().log("setNumberArray start"); //$NON-NLS-1$
		
		note.set("Foo3", Arrays.asList(1, 2, 3)); //$NON-NLS-1$
		Platform.getInstance().log("done set; now get"); //$NON-NLS-1$
		Object[] result = note.get("Foo3"); //$NON-NLS-1$
		assertEquals(1d, result[0]);
		assertEquals(2d, result[1]);
		assertEquals(3d, result[2]);

		Platform.getInstance().log("setNumberArray end"); //$NON-NLS-1$
	}
	
	@Test
	public void setDate() throws DominoException {
		Platform.getInstance().log("setDate start"); //$NON-NLS-1$
		
		Date now = new Date();
		note.set("Foo4", now); //$NON-NLS-1$
		Object[] result = note.get("Foo4"); //$NON-NLS-1$
		assertEquals(1, result.length);
		assertTrue(result[0] instanceof NSFDateTime);
		assertTrue(11 > Math.abs(now.getTime() - ((NSFDateTime)result[0]).toDate().getTime()));

		Platform.getInstance().log("setDate end"); //$NON-NLS-1$
	}
	
	@Test
	public void setDateArray() throws DominoException {
		Platform.getInstance().log("setDateArray start"); //$NON-NLS-1$
		
		Date now = new Date();
		Calendar later = Calendar.getInstance();
		later.setTime(now);
		later.add(Calendar.HOUR, 1);
		note.set("Foo5", new Object[] { now, later }); //$NON-NLS-1$
		Object[] result = note.get("Foo5"); //$NON-NLS-1$
		assertEquals(2, result.length);
		assertTrue(result[0] instanceof NSFDateTime);
		// Check that it's within 10 ms, since Domino times only go to hundredths
		
		long diff = Math.abs(now.getTime() - ((NSFDateTime)result[0]).toDate().getTime());
		assertTrue("Time of " + diff + " should be within 10ms", 11 > diff); //$NON-NLS-1$ //$NON-NLS-2$
		diff = Math.abs(later.getTime().getTime() - ((NSFDateTime)result[1]).toDate().getTime());
		assertTrue("Time of " + diff + " should be within 10ms", 11 > diff); //$NON-NLS-1$ //$NON-NLS-2$
		
		Platform.getInstance().log("setDateArray end"); //$NON-NLS-1$
	}
	
	@Test
	public void setUNID() throws DominoException {
		Platform.getInstance().log("setUNID start"); //$NON-NLS-1$
		
		note.setNoteRef("Foo6", note.getUniversalID()); //$NON-NLS-1$
		Object[] result = note.get("Foo6"); //$NON-NLS-1$
		assertEquals(1, result.length);
		assertEquals(String.class, result[0].getClass());
		assertEquals(note.getUniversalID(), result[0]);

		Platform.getInstance().log("setUNID end"); //$NON-NLS-1$
	}
	
	@Test
	public void setUNIDs() throws DominoException {
		Platform.getInstance().log("setUNIDs start"); //$NON-NLS-1$
		
		note.setNoteRef("Foo7", note.getUniversalID(), "8178B1C14B1E9B6B8525624F0062FE9F", "12BE305AE05245308FDC6BD48518A181"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		Object[] result = note.get("Foo7"); //$NON-NLS-1$
		assertEquals(3, result.length);
		assertEquals(String.class, result[0].getClass());
		assertEquals(note.getUniversalID(), result[0]);
		assertEquals("8178B1C14B1E9B6B8525624F0062FE9F", result[1]); //$NON-NLS-1$
		assertEquals("12BE305AE05245308FDC6BD48518A181", result[2]); //$NON-NLS-1$

		Platform.getInstance().log("setUNIDs end"); //$NON-NLS-1$
	}
	
	@Test
	public void makeResponse() throws DominoException {
		Platform.getInstance().log("makeResponse start"); //$NON-NLS-1$
		
		NSFNote child = note.getParent().createNote();
		child.makeResponse(note);
		try {
			Object[] ref = child.get("$REF"); //$NON-NLS-1$
			assertEquals(1, ref.length);
			assertEquals(note.getUniversalID(), ref[0]);
			
			assertEquals(note.getUniversalID(), child.getParentDocumentUNID());
		} finally {
			child.free();
		}
		Platform.getInstance().log("makeResponse end"); //$NON-NLS-1$
	}
	
	@Test
	public void save() throws DominoException {
		Platform.getInstance().log("save start"); //$NON-NLS-1$
		int id = note.getNoteID();
		assertEquals(0, id);
		note.save();
		id = note.getNoteID();
		assertNotEquals(0, id);
		Platform.getInstance().log("save end"); //$NON-NLS-1$
	}
	
	@Test
	public void itemScan() throws DominoException {
		Platform.getInstance().log("itemScan start"); //$NON-NLS-1$
		NSFDatabase fakeNames = session.getDatabase("", AllTests.FAKENAMES_DB_NAME); //$NON-NLS-1$
		try {
			NSFNote iconNote = fakeNames.getNoteByID(NOTE_ID_SPECIAL | NOTE_CLASS_ICON);
			assertNotNull(iconNote);
			try {
				iconNote.eachItem(new NSFITEMSCANPROC() {
					@Override public short callback(short itemFlags, String itemName, long valuePtr, int valueLength) {
						Platform.getInstance().log("Processing item {0}, flags {1}", itemName, itemFlags); //$NON-NLS-1$
						return 0;
					}
				});
			} finally {
				iconNote.free();
			}
		} finally {
			fakeNames.free();
		}
		Platform.getInstance().log("itemScan end"); //$NON-NLS-1$
	}
	
	@Test
	public void itemScanValues() throws DominoException {
		Platform.getInstance().log("itemScanValues start"); //$NON-NLS-1$
		NSFDatabase fakeNames = session.getDatabase("", AllTests.FAKENAMES_DB_NAME); //$NON-NLS-1$
		try {
			NSFNote iconNote = fakeNames.getNoteByID(NOTE_ID_SPECIAL | NOTE_CLASS_ICON);
			assertNotNull(iconNote);
			try {
				iconNote.eachItemValue(new NSFNote.NSFNoteItemProc() {
					@Override public void callback(String itemName, Object[] value) throws Exception {
						Platform.getInstance().log("Processing item {0}, values {1}", itemName, Arrays.asList(value)); //$NON-NLS-1$
					}
				});
			} finally {
				iconNote.free();
			}
		} finally {
			fakeNames.free();
		}
		Platform.getInstance().log("itemScan end"); //$NON-NLS-1$
	}
	
	@Test
	public void itemScanCancel() throws DominoException {
		Platform.getInstance().log("itemScanCancel start"); //$NON-NLS-1$
		NSFDatabase fakeNames = session.getDatabase("", AllTests.FAKENAMES_DB_NAME); //$NON-NLS-1$
		try {
			NSFNote iconNote = fakeNames.getNoteByID(NOTE_ID_SPECIAL | NOTE_CLASS_ICON);
			assertNotNull(iconNote);
			try {
				final ThreadLocal<Integer> counter = new ThreadLocal<Integer>() { @Override protected Integer initialValue() { return 0; } };
				iconNote.eachItem(new NSFITEMSCANPROC() {
					@Override public short callback(short itemFlags, String itemName, long valuePtr, int valueLength) {
						if(counter.get() == 1) {
							Platform.getInstance().log("Canceling proc on index 1"); //$NON-NLS-1$
							return ERR_CANCEL;
						} else {
							Platform.getInstance().log("Processing item {0}, flags {1}", itemName, itemFlags); //$NON-NLS-1$
							counter.set(counter.get() + 1);
						}
						return 0;
					}
				});
				assertEquals(1, (int)counter.get());
			} finally {
				iconNote.free();
			}
		} finally {
			fakeNames.free();
		}
		Platform.getInstance().log("itemScanCancel end"); //$NON-NLS-1$
	}
	
	@Test
	public void attachFileAndScan() throws DominoException, IOException {
		Platform.getInstance().log("attachFileAndScan start"); //$NON-NLS-1$
		
		File tempFile = File.createTempFile("napi", ".txt"); //$NON-NLS-1$ //$NON-NLS-2$
		FileOutputStream fos = new FileOutputStream(tempFile);
		try {
			fos.write("hey there".getBytes()); //$NON-NLS-1$
			fos.close();
			
			note.attachFile(tempFile.getAbsolutePath(), "napi.txt"); //$NON-NLS-1$
			note.save();
			
			final ThreadLocal<Boolean> found = new ThreadLocal<Boolean>() { @Override protected Boolean initialValue() { return false; } };
			note.eachItem(new NSFITEMSCANPROC() {
				@Override public synchronized short callback(short itemFlags, String itemName, long valuePtr, int valueLength) {
					short itemType = C.getWORD(valuePtr, 0);
					if(itemType == TYPE_OBJECT) {
						Platform.getInstance().log("found object field {0}", itemName); //$NON-NLS-1$
						
						// The content is in canonical format, so we'll have to pass through ODSReadMemory
						long ppValuePtr = C.malloc(C.sizeOfPOINTER);
						Platform.getInstance().log("malloced size {0}", C.sizeOfPOINTER); //$NON-NLS-1$
						Platform.getInstance().log("valuePtr is {0}, length is {1}", valuePtr, valueLength); //$NON-NLS-1$
						C.setPointer(ppValuePtr, 0, C.ptrAdd(valuePtr, C.sizeOfWORD));
						
						// First, read the object descriptor
						OBJECT_DESCRIPTOR desc = new OBJECT_DESCRIPTOR();
						try {
							note.getAPI().ODSReadMemory(ppValuePtr, _OBJECT_DESCRIPTOR, desc.getDataPtr(), (short)1);
							if(desc.getObjectType() == ObjectType.FILE) {
								// Then we know it's a file - reset the pointer and read the FILEOBJECT
								C.setPointer(ppValuePtr, 0, C.ptrAdd(valuePtr, C.sizeOfWORD));
								FILEOBJECT obj = new FILEOBJECT();
								try {
									note.getAPI().ODSReadMemory(ppValuePtr, _FILEOBJECT, obj.getDataPtr(), (short)1);
									Platform.getInstance().log("File name length is {0}", obj.getFileNameLength()); //$NON-NLS-1$
									Platform.getInstance().log("struct size is {0}", FILEOBJECT.sizeOf); //$NON-NLS-1$
									String fileName = C.getLMBCSString(C.getPointer(ppValuePtr, 0), 0, obj.getFileNameLength());
									assertEquals("napi.txt", fileName); //$NON-NLS-1$
									Platform.getInstance().log("is a file: {0}", fileName); //$NON-NLS-1$
									
									found.set(true);
								} finally {
									Platform.getInstance().log("about to free obj"); //$NON-NLS-1$
									obj.free();
									Platform.getInstance().log("freed obj"); //$NON-NLS-1$
								}
							} else {
								Platform.getInstance().log("object type was {0}", desc.getObjectType()); //$NON-NLS-1$
							}
						} finally {
							Platform.getInstance().log("about to free desc"); //$NON-NLS-1$
							desc.free();
							Platform.getInstance().log("freed desc"); //$NON-NLS-1$
							Platform.getInstance().log("going to free ppValuePtr, which is {0}", ppValuePtr); //$NON-NLS-1$
							C.free(ppValuePtr);
							Platform.getInstance().log("freed ppValuePtr"); //$NON-NLS-1$
						}
					}
					
					return 0;
				}
			});
			assertTrue(found.get());
		} finally {
			StreamUtil.close(fos);
			tempFile.delete();
		}
		Platform.getInstance().log("attachFileAndScan end"); //$NON-NLS-1$
	}
	
	@Test
	public void itemNotFound() throws DominoException {
		assertFalse(note.hasItem("ITEM NOT FOUND")); //$NON-NLS-1$
	}
	
	@Test
	public void itemCast() throws DominoException { 
		note.set("Foo", "Bar"); //$NON-NLS-1$ //$NON-NLS-2$
		note.get("Foo", String.class); //$NON-NLS-1$
		note.get("Foo", Object.class); //$NON-NLS-1$
	}
	
	public void invalidItemCast() throws DominoException {
		note.set("Foo", "Bar"); //$NON-NLS-1$ //$NON-NLS-2$
		Number result = note.get("Foo", Number.class); //$NON-NLS-1$
		assertEquals(null, result);
	}
	
	@Test
	public void normalCreated() throws DominoException {
		NSFNote note = database.createNote();
		OID oid = note.getOID();
		try {
			assertEquals(oid.getNote().toJavaDate(), note.getCreated());
		} finally {
			oid.free();
			note.free();
		}
	}
	
	@Test
	public void brokenCreatedField() throws DominoException {
		NSFNote note = database.createNote();
		OID oid = note.getOID();
		try {
			note.set("$Created", "foo"); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals(oid.getNote().toJavaDate(), note.getCreated());
		} finally {
			oid.free();
			note.free();
		}
	}
	
	@Test
	public void overriddenCreated() throws DominoException {
		NSFNote note = database.createNote();
		OID oid = note.getOID();
		try {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2014);
			cal.set(Calendar.MILLISECOND, 0);
			note.set("$Created", cal); //$NON-NLS-1$
			assertNotEquals(oid.getSequenceTime().toJavaDate(), note.getCreated());
			assertEquals(cal.getTime(), note.getCreated());
		} finally {
			oid.free();
			note.free();
		}
	}
	
	@Test
	public void responses() throws DominoException {
		NSFNote note = forumDocs.getNoteByUNID(AllTests.FORUM_DOC_DOUBLE_ATTACHMENT_UNID);
		try {
			int count = note.getResponseCount();
			assertEquals("Response count should be 1", 1, count);
			
			NSFNoteIDCollection responses = note.getResponses();
			assertNotEquals("Responses should not be null", null, responses); //$NON-NLS-1$
			assertEquals("Responses should contain one entry", 1, responses.size()); //$NON-NLS-1$
			assertEquals("First note ID should be 2326", 2326, (int)responses.iterator().next()); //$NON-NLS-1$
		} finally {
			note.free();
		}
	}
	
	@Test
	public void hasItemPerformance() throws DominoException {
		Platform.getInstance().log("hasItemPerformance start");
		NSFNote note = forumDocs.getNoteByUNID(AllTests.FORUM_DOC_DOUBLE_ATTACHMENT_UNID);
		try {
			long start = System.currentTimeMillis();
			int iterations = 10000;
			
			for(int i = 0; i < iterations; i++) {
				note.hasItem("NOTPRESENT");
			}
			
			long end = System.currentTimeMillis();
			
			Platform.getInstance().log("{0} iterations took {1}ms", iterations, end - start);
		} finally {
			note.free();
		}
		Platform.getInstance().log("hasItemPerformance end");
	}
	
	@Test
	public void testArbitraryParentUNID() throws DominoException {
		NSFNote note = forumDocs.createNote();
		try {
			String parentId = "0123456789ABCDEF0123456789ABCDEF";
			note.setParentDocumentUNID(parentId);
			
			assertEquals(parentId, note.getParentDocumentUNID());
			
			NSFItem item = note.getFirstItem(DominoAPI.FIELD_LINK);
			assertEquals(ValueType.NOTEREF_LIST, item.getType());
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testSingleValueArray() throws DominoException {
		NSFNote note = forumDocs.createNote();
		try {
			note.set("Foo", "Bar");
			
			String[] foo = note.get("Foo", String[].class);
			assertNotEquals("foo should not be null", null, foo);
			assertEquals("foo should have one element", 1, foo.length);
			assertEquals("foo[0] should be Bar", "Bar", foo[0]);
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testMultiValueArray() throws DominoException {
		NSFNote note = forumDocs.createNote();
		try {
			note.set("Foo", Arrays.asList("Bar", "Baz"));
			
			String[] foo = note.get("Foo", String[].class);
			assertNotEquals("foo should not be null", null, foo);
			assertEquals("foo should have two element", 2, foo.length);
			assertEquals("foo[0] should be Bar", "Bar", foo[0]);
			assertEquals("foo[0] should be Baz", "Baz", foo[1]);
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testZeroValueArray() throws DominoException {
		NSFNote note = forumDocs.createNote();
		try {
			String[] foo = note.get("Foo", String[].class);
			assertEquals("foo should be null", null, (Object)foo);
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testSingleValue() throws DominoException {
		NSFNote note = forumDocs.createNote();
		try {
			note.set("Foo", "Bar");
			
			String foo = note.get("Foo", String.class);
			assertNotEquals("foo should not be null", null, foo);
			assertEquals("foo should be Bar", "Bar", foo);
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testUserIDField() throws DominoException {
		NSFDatabase database = session.getDatabaseByHandle(AllTests.getV2TesterDatabase(), "");
		try {
			NSFNote note = database.getNoteByUNID(AllTests.V2TESTER_USERID_UNID);
			String username = note.get("Author", String.class);
			assertEquals("Author name should be Test User", "Test User", username);
		} finally {
			database.free();
		}
	}
	
	@Test
	public void testConvertToString() throws DominoException {
		NSFNote note = forumDocs.createNote();
		try {
			note.set("Foo", 1);
			
			String result = note.getAsString("Foo", ',');
			assertEquals("result should be '1'", "1", result);
			
			note.set("Bar", new int[] { 1, 2, 3 });
			
			result = note.getAsString("Bar", ',');
			assertEquals("result should be '1,2,3'", "1,2,3", result);
			
			result = note.getAsString("NotSet", ',');
			assertEquals("result should be blank", "", result);
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testDbModified() throws DominoException {
		NSFNote note = forumDocs.createNote();
		try {
			note.save();
			NSFDateTime mod = note.getSequenceModified();
			NSFDateTime dbMod = forumDocs.getLastModified().getDataModified();
			assertTrue("DB mod time should be at least doc mod time", dbMod.compareTo(mod) >= 0);
		} finally {
			note.free();
		}
	}
}
