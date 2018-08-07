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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.wrap.HTMLExporter;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.NSFSession;
import com.darwino.domino.napi.wrap.NSFView;
import com.darwino.domino.napi.wrap.NSFViewEntry;
import com.darwino.domino.napi.wrap.NSFViewEntryCollection.ViewEntryVisitor;

import java.nio.ByteBuffer;

/**
 * The forum docs database contains documents from public forum DBs that have proven problematic
 * during conversion
 * 
 * @author Jesse Gallagher
 */
@SuppressWarnings("nls")
public class ForumDocs {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", ForumDocs.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", ForumDocs.class.getSimpleName()); //$NON-NLS-1$
	}
	
	DominoAPI api;
	NSFSession session;
	NSFDatabase database;
	
	@Before
	public void beforeTests() throws DominoException {
		api = DominoAPI.get();
		session = new NSFSession(api);
		database = session.getDatabaseByHandle(AllTests.getForumDocDatabase(), ""); //$NON-NLS-1$
	}
	@After
	public void afterTests() throws DominoException {
		database.free();
		session.free();
	}
	
	@Test
	public void readUpdatedByNormal() throws DominoException {
		NSFNote note = database.getNoteByUNID(AllTests.FORUM_DOC_HTMLAPI_UNID);
		Platform.getInstance().log("Opened {0} note {1}", DominoAPI.FIELD_UPDATED_BY, note.getUniversalID()); //$NON-NLS-1$
		
		Object[] value = note.get(DominoAPI.FIELD_UPDATED_BY);
		Platform.getInstance().log("Read value {0}", value); //$NON-NLS-1$
		
		assertTrue("Updated By should have at least one entry", value != null && value.length > 0); //$NON-NLS-1$
	}
	
	/**
	 * This test reflects the case where reading the $UpdatedBy field from a specific doc
	 * would either crash or return invalid data. The problem turned out to be reading a
	 * DWORD as a short and then using that as the offset in getDWORD.
	 */
	@Test
	public void readUpdatedByProblematic() throws DominoException {
		NSFNote note = database.getNoteByUNID(AllTests.FORUM_DOC_UPDATEDBY_UNID);
		Platform.getInstance().log("Opened {0} note {1}", DominoAPI.FIELD_UPDATED_BY, note.getUniversalID()); //$NON-NLS-1$
		
		Object[] value = note.get(DominoAPI.FIELD_UPDATED_BY);
		Platform.getInstance().log("Read value {0}", value); //$NON-NLS-1$
		
		assertTrue("Updated By should have at least one entry", value != null && value.length > 0); //$NON-NLS-1$
	}
	
	/**
	 * This test reflects the case where a document's rich-text body field throws an "HTMLAPI"
	 * error from C during conversion
	 */
	@Test
	public void exportHTMLProblematic() throws DominoException {
		NSFNote note = database.getNoteByUNID(AllTests.FORUM_DOC_HTMLAPI_UNID);
		
		try {
			HTMLExporter.InlineImageHandler imageHandler = new HTMLExporter.InlineImageHandler() {
				@Override public String handle(String elemOffset, String format, ByteBuffer data) {
					Platform.getInstance().log("Handing image at offset {0}", elemOffset); //$NON-NLS-1$
					return "#"; //$NON-NLS-1$
				}
			};
			
			@SuppressWarnings("unused")
			String result = ""; //$NON-NLS-1$
			try {
				result = note.createHTMLExporter("BodyR2R") //$NON-NLS-1$
						.inlineImageHandler(imageHandler)
						.export();
			} catch(DominoException e) {
				if(e.getStatus() == DominoAPI.ERR_HTMLAPI_GENERATING_HTML) {
					// This is what we expect
				} else {
					throw e;
				}
			}
		} finally {
			if(note != null) { note.free(); }
		}
	}
	
	@Test
	public void exportForumHTML() throws Exception {
		System.out.println("exportForumHTML start");
		
		NSFView view = database.getView("All Documents");
		view.getAllEntries().eachEntry(new ViewEntryVisitor() {
			@Override public void visit(NSFViewEntry entry) throws Exception {
				final NSFNote note = database.getNoteByUNID(entry.getUniversalID());
				final String documentId = note.getUniversalID();
				try {
					if(note.hasItem("Body")) {
						HTMLExporter.InlineImageHandler imageHandler = new HTMLExporter.InlineImageHandler() {
							@Override public String handle(String elemOffset, String format, ByteBuffer data) {
								return "/" + documentId + "/Body/" + elemOffset + "?format=" + format;
							}
						};
						String result = note.createHTMLExporter("Body")
								.inlineImageHandler(imageHandler)
								.export();
						System.out.println(result);
					}
				} finally {
					note.free();
				}
			}
		});

		System.out.println("exportForumHTML end");
	}
}
