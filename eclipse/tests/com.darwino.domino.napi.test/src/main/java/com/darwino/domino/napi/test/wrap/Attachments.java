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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.ibm.commons.util.io.StreamUtil;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.NSFItem;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.item.NSFFileItem;

/**
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public class Attachments extends AbstractNoteTest {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", Attachments.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", Attachments.class.getSimpleName()); //$NON-NLS-1$
	}

	@Test
	public void testHTMLAttachment() throws Exception {
		NSFDatabase database = session.getDatabaseByHandle(AllTests.getMimeDocsDatabase(), ""); //$NON-NLS-1$
		// This note is generally used for MIME testing, but it has the side effect of having
		// a single $FILE item containing an HTML file
		NSFNote note = database.getNoteByUNID(AllTests.MIMEDOCS_DOC_LARGEATT_UNID);
		try {
			System.out.println("got note"); //$NON-NLS-1$
			NSFItem attachment = note.getFirstItem("$FILE"); //$NON-NLS-1$
			assertNotEquals("attachment should not be null", null, attachment); //$NON-NLS-1$
			assertEquals("attachment should be an NSFFileItem", NSFFileItem.class, attachment.getClass()); //$NON-NLS-1$
			System.out.println("got attachment item " + attachment); //$NON-NLS-1$
			
			File tempFile = File.createTempFile("extracted", ".html"); //$NON-NLS-1$ //$NON-NLS-2$
			try {
				((NSFFileItem)attachment).extractFile(tempFile);
				@SuppressWarnings("resource")
				FileInputStream fos = new FileInputStream(tempFile);
				String content = null;
				try {
					content = StreamUtil.readString(fos);
				} finally {
					StreamUtil.close(fos);
				}
				
				assertTrue("content should contain 'Lorem ipsum dolor sit amet'", content.contains("Lorem ipsum dolor sit amet")); //$NON-NLS-1$ //$NON-NLS-2$
				assertEquals("content should be 72095 characters", 72095, content.length()); //$NON-NLS-1$
			} finally {
				tempFile.delete();
			}
		} finally {
			note.free();
		}
	}

	@Test
	public void testHTMLAttachmentInputStream() throws Exception {
		NSFDatabase database = session.getDatabaseByHandle(AllTests.getMimeDocsDatabase(), ""); //$NON-NLS-1$
		// This note is generally used for MIME testing, but it has the side effect of having
		// a single $FILE item containing an HTML file
		NSFNote note = database.getNoteByUNID(AllTests.MIMEDOCS_DOC_LARGEATT_UNID);
		try {
			System.out.println("got note"); //$NON-NLS-1$
			NSFItem attachment = note.getFirstItem("$FILE"); //$NON-NLS-1$
			assertNotEquals("attachment should not be null", null, attachment); //$NON-NLS-1$
			assertEquals("attachment should be an NSFFileItem", NSFFileItem.class, attachment.getClass()); //$NON-NLS-1$
			System.out.println("got attachment item " + attachment); //$NON-NLS-1$
			
			@SuppressWarnings("resource")
			InputStream is = ((NSFFileItem)attachment).getTempFileInputStream();
			try {
				String content = StreamUtil.readString(is);
				
				assertTrue("content should contain 'Lorem ipsum dolor sit amet'", content.contains("Lorem ipsum dolor sit amet")); //$NON-NLS-1$ //$NON-NLS-2$
				assertEquals("content should be 72095 characters", 72095, content.length()); //$NON-NLS-1$
			} finally {
				StreamUtil.close(is);
			}
		} finally {
			note.free();
		}
	}
}
