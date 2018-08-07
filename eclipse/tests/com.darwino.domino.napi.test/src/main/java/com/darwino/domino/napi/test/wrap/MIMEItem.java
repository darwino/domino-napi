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

import javax.mail.internet.MimeBodyPart;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.ibm.commons.util.io.StreamUtil;
import com.darwino.domino.napi.enums.ItemFlag;
import com.darwino.domino.napi.enums.MIMEPartType;
import com.darwino.domino.napi.struct.MIME_PART;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.NSFItem;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.item.NSFMimeItem;

/**
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public class MIMEItem extends AbstractNoteTest {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", MIMEItem.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", MIMEItem.class.getSimpleName()); //$NON-NLS-1$
	}
	
	@Test
	public void testSimpleMimeItem() throws Exception {
		NSFDatabase database = session.getDatabaseByHandle(AllTests.getMimeDocsDatabase(), ""); //$NON-NLS-1$
		NSFNote note = database.getNoteByUNID(AllTests.MIMEDOCS_DOC_NORMAL_UNID);
		
		assertFalse("note should not be null", note == null); //$NON-NLS-1$
		
		assertTrue("note should have item 'Body'", note.hasItem("Body")); //$NON-NLS-1$ //$NON-NLS-2$
		
		NSFItem item = note.getFirstItem("Body"); //$NON-NLS-1$
		assertFalse("item should not be null", item == null); //$NON-NLS-1$
		try {
			assertEquals("item should be a NSFMIMEItem", NSFMimeItem.class, item.getClass()); //$NON-NLS-1$
			
			Object[] val = item.getValue();
			assertTrue("val should be a single-element array", val.length == 1); //$NON-NLS-1$
			assertEquals("val[0] should be a MIME_PART", MIME_PART.class, val[0].getClass()); //$NON-NLS-1$
			
			MimeBodyPart part = ((MIME_PART)val[0]).toMimeBodyPart();
			String content = StreamUtil.readString(part.getInputStream());
			assertTrue("content should contain 'doh!'", content.contains("doh!")); //$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			item.free();
		}
	}
	
	@Test
	public void testMultiMimeItem() throws Exception {
		NSFDatabase database = session.getDatabaseByHandle(AllTests.getMimeDocsDatabase(), ""); //$NON-NLS-1$
		NSFNote note = database.getNoteByUNID(AllTests.MIMEDOCS_DOC_MULTIPART_UNID);
		
		assertFalse("note should not be null", note == null); //$NON-NLS-1$
		
		assertTrue("note should have item 'Body'", note.hasItem("Body")); //$NON-NLS-1$ //$NON-NLS-2$
		
		NSFItem header = null, html = null, image = null, footer = null;
		try {
			header = note.getFirstItem("Body"); //$NON-NLS-1$
			{
				assertNotEquals("header should not be null", null, header); //$NON-NLS-1$
				assertEquals("header should be an NSFMimeItem", NSFMimeItem.class, header.getClass()); //$NON-NLS-1$
				MIME_PART part = ((NSFMimeItem)header).getMimePart();
				assertNotEquals("part should not be null", null, part); //$NON-NLS-1$
				assertEquals("part should be type PROLOG", MIMEPartType.PROLOG, part.getPartType()); //$NON-NLS-1$
			}
			
			html = note.getNextItem(header);
			{
				assertNotEquals("html should not be null", null, html); //$NON-NLS-1$
				assertEquals("html should be an NSFMimeItem", NSFMimeItem.class, html.getClass()); //$NON-NLS-1$
				MIME_PART part = ((NSFMimeItem)html).getMimePart();
				assertNotEquals("part should not be null", null, part); //$NON-NLS-1$
				assertEquals("part should be type BODY", MIMEPartType.BODY, part.getPartType()); //$NON-NLS-1$
				
				String content = ((NSFMimeItem)html).getDataAsString();
				assertTrue("content should contain 'XPage application'", content.contains("XPage application")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			image = note.getNextItem(html);
			{
				assertNotEquals("image should not be null", null, image); //$NON-NLS-1$
				assertEquals("image should be an NSFMimeItem", NSFMimeItem.class, image.getClass()); //$NON-NLS-1$
				MIME_PART part = ((NSFMimeItem)image).getMimePart();
				assertNotEquals("part should not be null", null, part); //$NON-NLS-1$
				assertEquals("part should be type BODY", MIMEPartType.BODY, part.getPartType()); //$NON-NLS-1$
			}
			
			footer = note.getNextItem(image);
			{
				assertNotEquals("footer should not be null", null, footer); //$NON-NLS-1$
				assertEquals("footer should be an NSFMimeItem", NSFMimeItem.class, footer.getClass()); //$NON-NLS-1$
				MIME_PART part = ((NSFMimeItem)footer).getMimePart();
				assertNotEquals("part should not be null", null, part); //$NON-NLS-1$
				assertEquals("part should be type EPILOG", MIMEPartType.EPILOG, part.getPartType()); //$NON-NLS-1$
			}
		} finally {
			if(header != null) { header.free(); }
			if(html != null) { html.free(); }
			if(image != null) { image.free(); }
			if(footer != null) { footer.free(); }
		}
	}
	
	@Test
	public void testExternalContentItem() throws Exception {
		NSFDatabase database = session.getDatabaseByHandle(AllTests.getMimeDocsDatabase(), ""); //$NON-NLS-1$
		NSFNote note = database.getNoteByUNID(AllTests.MIMEDOCS_DOC_LARGEATT_UNID);
		
		assertFalse("note should not be null", note == null); //$NON-NLS-1$
		
		assertTrue("note should have item 'Body'", note.hasItem("Body")); //$NON-NLS-1$ //$NON-NLS-2$
		
		NSFItem header = null, html = null, attachment = null, footer = null;
		try {
			header = note.getFirstItem("Body"); //$NON-NLS-1$
			{
				assertNotEquals("header should not be null", null, header); //$NON-NLS-1$
				assertEquals("header should be an NSFMimeItem", NSFMimeItem.class, header.getClass()); //$NON-NLS-1$
				MIME_PART part = ((NSFMimeItem)header).getMimePart();
				assertNotEquals("part should not be null", null, part); //$NON-NLS-1$
				assertEquals("part should be type PROLOG", MIMEPartType.PROLOG, part.getPartType()); //$NON-NLS-1$
				
				// This item also happens to be signed, so do an opportunistic test here
				assertTrue("header should have flag SIGN", header.getFlags().contains(ItemFlag.SIGN)); //$NON-NLS-1$
				assertTrue("header should have flag SEAL", header.getFlags().contains(ItemFlag.SEAL)); //$NON-NLS-1$
			}
			
			html = note.getNextItem(header);
			{
				assertNotEquals("html should not be null", null, html); //$NON-NLS-1$
				assertEquals("html should be an NSFMimeItem", NSFMimeItem.class, html.getClass()); //$NON-NLS-1$
				MIME_PART part = ((NSFMimeItem)html).getMimePart();
				assertNotEquals("part should not be null", null, part); //$NON-NLS-1$
				assertEquals("part should be type BODY", MIMEPartType.BODY, part.getPartType()); //$NON-NLS-1$
				
				String content = ((NSFMimeItem)html).getDataAsString();
				assertTrue("content should contain 'not part of'", content.contains("not part of")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			attachment = note.getNextItem(html);
			{
				assertNotEquals("attachment should not be null", null, attachment); //$NON-NLS-1$
				assertEquals("attachment should be an NSFMimeItem", NSFMimeItem.class, attachment.getClass()); //$NON-NLS-1$
				MIME_PART part = ((NSFMimeItem)attachment).getMimePart();
				assertNotEquals("part should not be null", null, part); //$NON-NLS-1$
				assertEquals("part should be type BODY", MIMEPartType.BODY, part.getPartType()); //$NON-NLS-1$
				
				String content = ((NSFMimeItem)attachment).getDataAsString();
				assertNotEquals("content should not be the attachment file name", "lipsum.html", content); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			footer = note.getNextItem(attachment);
			{
				assertNotEquals("footer should not be null", null, footer); //$NON-NLS-1$
				assertEquals("footer should be an NSFMimeItem", NSFMimeItem.class, footer.getClass()); //$NON-NLS-1$
				MIME_PART part = ((NSFMimeItem)footer).getMimePart();
				assertNotEquals("part should not be null", null, part); //$NON-NLS-1$
				assertEquals("part should be type EPILOG", MIMEPartType.EPILOG, part.getPartType()); //$NON-NLS-1$
			}
		} finally {
			if(header != null) { header.free(); }
			if(html != null) { html.free(); }
			if(attachment != null) { attachment.free(); }
			if(footer != null) { footer.free(); }
		}
	}
}
