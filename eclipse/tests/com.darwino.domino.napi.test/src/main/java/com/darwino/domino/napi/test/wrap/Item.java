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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.enums.ItemFlag;
import com.darwino.domino.napi.enums.ValueType;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.wrap.NSFItem;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.NSFUserData;

/**
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
@SuppressWarnings("nls")
public class Item extends AbstractNoteTest {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", Item.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", Item.class.getSimpleName()); //$NON-NLS-1$
	}
	
	@Test
	public void testTextItem() throws Exception {
		note.set("TestItem", "Foo"); //$NON-NLS-1$ //$NON-NLS-2$
		
		assertTrue("note should have item 'TestItem'", note.hasItem("TestItem")); //$NON-NLS-1$ //$NON-NLS-2$
		
		NSFItem item = note.getFirstItem("TestItem"); //$NON-NLS-1$
		assertFalse("item should not be null", item == null); //$NON-NLS-1$
		
		try {
			assertTrue("item should be of type TEXT", item.getType() == ValueType.TEXT); //$NON-NLS-1$
			
			assertEquals("Foo", item.getValue(String.class)); //$NON-NLS-1$
		} finally {
			item.free();
		}
	}
	
	@Test
	public void testIntItem() throws Exception {
		note.set("TestInt", 1); //$NON-NLS-1$
		
		assertTrue("note should have item 'TestInt'", note.hasItem("TestInt")); //$NON-NLS-1$ //$NON-NLS-2$
		
		NSFItem item = note.getFirstItem("TestInt"); //$NON-NLS-1$
		assertFalse("item should not be null", item == null); //$NON-NLS-1$
		
		try {
			assertTrue("item should be of type NUMBER", item.getType() == ValueType.NUMBER); //$NON-NLS-1$
			
			assertEquals(new Integer(1), item.getValue(Integer.class));
		} finally {
			item.free();
		}
	}
	
	@Test
	public void testMimeItem() throws Exception {
		try {
			
		} finally {
			database.free();
		}
	}
	
	@Test
	public void testReaders() throws Exception {
		note.set("Readers", "CN=Joe Schmoe/O=Foo");
		assertTrue("note should have item 'Readers'", note.hasItem("Readers"));
		
		NSFItem item = note.getFirstItem("Readers");
		assertNotEquals("item should not be null", null, item);
		
		item.addFlag(ItemFlag.READERS);
		
		assertTrue("item should now have flag READERS", item.getFlags().contains(ItemFlag.READERS));
	}
	
	@Test
	public void testArbitraryData() throws Exception {
		NSFNote note = edgeCases.getNoteByUNID(AllTests.ARBITRARY_DATA_DOC_UNID);
		try {
			assertNotEquals("note should not be null", null, note);
			NSFItem item = note.getFirstItem("LipSum");
			assertNotEquals("LipSum item should not be null", null, item);
			assertEquals("item type should be USERDATA", ValueType.USERDATA, item.getType());
			NSFUserData value = item.getValue(NSFUserData.class);
			assertNotEquals("value should not be null", null, value);
			assertEquals("data format should be 'ODAChunk'", "ODAChunk", value.getFormatName());
			byte[] data = value.getData();
			assertEquals("data length should be 49152", 49152, data.length);
			String valueString = new String(data, "UTF-8");
			assertTrue("valueString should contain lipsum", valueString.contains("Lorem ipsum dolor sit amet"));
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testArbitraryDataReadWrite() throws Exception {
		NSFNote note = edgeCases.createNote();
		try {
			String exampleData = "éxample data ı 😏";
			
			NSFUserData userData = new NSFUserData("Heyaç", exampleData.getBytes("UTF-8"));
			note.set("Foo", userData);
			
			NSFUserData readData = note.get("Foo", NSFUserData.class);
			assertNotEquals("readData should not be null", null, readData);
			assertEquals("readData type should be Heyaç", "Heyaç", readData.getFormatName());
			byte[] readDataBytes = readData.getData();
			String readExampleData = new String(readDataBytes, "UTF-8");
			assertEquals("readData should equal exampleData", exampleData, readExampleData);
		} finally {
			note.free();
		}
	}
	
	@Test
	public void testTextNonSummary() throws Exception {
		NSFNote note = database.createNote();
		try {
			note.set("SomeText", "Foo bar", (short)0);
			
			NSFItem item = note.getFirstItem("SomeText");
			assertNotEquals("item should not be null", null, item);
			assertEquals("item should be type TEXT", ValueType.TEXT, item.getType());
			assertEquals("item value should be 'Foo bar'", "Foo bar", item.getValue()[0]);
			assertFalse("item should not be summary", item.getFlags().contains(ItemFlag.SUMMARY));
		} finally {
			note.free();
		}
	}
}
