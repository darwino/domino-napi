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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.NSFFileAttachment;
import com.darwino.domino.napi.wrap.NSFItem;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.item.NSFCompositeDataItem;

/**
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
@SuppressWarnings("nls")
public class CompositeDataItem extends AbstractNoteTest {
	
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", CompositeDataItem.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", CompositeDataItem.class.getSimpleName()); //$NON-NLS-1$
	}

	@Test
	public void testSimpleCDItem() throws Exception {
		Platform.getInstance().log("testSimpleCDItem start");
		
		NSFDatabase database = session.getDatabaseByHandle(AllTests.getForumDocDatabase(), ""); //$NON-NLS-1$
		NSFNote note = database.getNoteByUNID(AllTests.FORUM_DOC_BASICCD_UNID);
		assertFalse("note should not be null", note == null); //$NON-NLS-1$

		try {
			assertTrue("note should have item 'Body'", note.hasItem("Body")); //$NON-NLS-1$ //$NON-NLS-2$
			
			NSFItem item = note.getFirstItem("Body"); //$NON-NLS-1$
			assertFalse("item should not be null", item == null); //$NON-NLS-1$
			try {
				assertEquals("item should be a NSFCompositeDataItem", NSFCompositeDataItem.class, item.getClass()); //$NON-NLS-1$
				
			} finally {
				item.free();
			}
		} finally {
			note.free();
		}

		Platform.getInstance().log("testSimpleCDItem end");
	}
	
	@Test
	public void testSingleAttachmentCDItem() throws Exception {
		Platform.getInstance().log("testSingleAttachmentCDItem start");
		
		NSFDatabase database = session.getDatabaseByHandle(AllTests.getForumDocDatabase(), ""); //$NON-NLS-1$
		NSFNote note = database.getNoteByUNID(AllTests.FORUM_DOC_ATTACHMENT_UNID);
		assertFalse("note should not be null", note == null); //$NON-NLS-1$

		try {
			assertTrue("note should have item 'Body'", note.hasItem("Body")); //$NON-NLS-1$ //$NON-NLS-2$
			
			NSFItem item = note.getFirstItem("Body"); //$NON-NLS-1$
			assertFalse("item should not be null", item == null); //$NON-NLS-1$
			try {
				assertEquals("item should be a NSFCompositeDataItem", NSFCompositeDataItem.class, item.getClass()); //$NON-NLS-1$
				
				List<NSFFileAttachment> attachments = ((NSFCompositeDataItem)item).getAttachments();
				assertNotEquals("Attachment list should not be null", null, attachments); //$NON-NLS-1$
				assertEquals("Attachment list should have one entry", 1, attachments.size()); //$NON-NLS-1$
				
				NSFFileAttachment att = attachments.get(0);
				try {
					assertNotEquals("Attachment should not be null", null, att); //$NON-NLS-1$
					assertEquals("Attachment internal name should be 'twokey.nsf'", "twokey.nsf", att.getInternalFileName()); //$NON-NLS-1$ //$NON-NLS-2$
					assertEquals("Attachment original name should be 'twokey.nsf'", "twokey.nsf", att.getOriginalFileName()); //$NON-NLS-1$ //$NON-NLS-2$
				} finally {
					att.free();
				}
				
			} finally {
				item.free();
			}
		} finally {
			note.free();
		}
		
		Platform.getInstance().log("testSingleAttachmentCDItem end");
	}
	
	@Test
	public void testDoubleAttachmentCDItem() throws Exception {
		Platform.getInstance().log("testDoubleAttachmentCDItem start");
		
		NSFDatabase database = session.getDatabaseByHandle(AllTests.getForumDocDatabase(), ""); //$NON-NLS-1$
		NSFNote note = database.getNoteByUNID(AllTests.FORUM_DOC_DOUBLE_ATTACHMENT_UNID);
		assertFalse("note should not be null", note == null); //$NON-NLS-1$

		try {
			assertTrue("note should have item 'Body'", note.hasItem("Body")); //$NON-NLS-1$ //$NON-NLS-2$
			
			NSFItem item = note.getFirstItem("Body"); //$NON-NLS-1$
			assertFalse("item should not be null", item == null); //$NON-NLS-1$
			try {
				assertEquals("item should be a NSFCompositeDataItem", NSFCompositeDataItem.class, item.getClass()); //$NON-NLS-1$
				
				List<NSFFileAttachment> attachments = ((NSFCompositeDataItem)item).getAttachments();
				assertNotEquals("Attachment list should not be null", null, attachments); //$NON-NLS-1$
				assertEquals("Attachment list should have two entries", 2, attachments.size()); //$NON-NLS-1$
				
				NSFFileAttachment att = attachments.get(0);
				try {
					assertNotEquals("Attachment should not be null", null, att); //$NON-NLS-1$
					assertEquals("Attachment internal name should be 'twokey.nsf'", "twokey.nsf", att.getInternalFileName()); //$NON-NLS-1$ //$NON-NLS-2$
					assertEquals("Attachment original name should be 'twokey.nsf'", "twokey.nsf", att.getOriginalFileName()); //$NON-NLS-1$ //$NON-NLS-2$
				} finally {
					att.free();
				}
				
				NSFFileAttachment att2 = attachments.get(1);
				try {
					assertNotEquals("Attachment 2 should not be null", null, att2); //$NON-NLS-1$
					assertEquals("Attachment 2 internal name should be 'twokey.002.nsf'", "twokey.002.nsf", att2.getInternalFileName()); //$NON-NLS-1$ //$NON-NLS-2$
					assertEquals("Attachment 2 original name should be 'twokey.nsf'", "twokey.nsf", att2.getOriginalFileName()); //$NON-NLS-1$ //$NON-NLS-2$
				} finally {
					att.free();
				}
				
			} finally {
				item.free();
			}
		} finally {
			note.free();
		}
		
		Platform.getInstance().log("testDoubleAttachmentCDItem end");
	}
}
