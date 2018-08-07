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

package com.darwino.domino.napi.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.StreamUtil;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;

import com.darwino.domino.napi.test.basics.CreateDatabase;
import com.darwino.domino.napi.test.basics.DeleteDatabase;
import com.darwino.domino.napi.test.basics.Documents;
import com.darwino.domino.napi.test.basics.Folders;
import com.darwino.domino.napi.test.basics.Fonts;
import com.darwino.domino.napi.test.basics.MIMETest;
import com.darwino.domino.napi.test.basics.ModifiedTableTest;
import com.darwino.domino.napi.test.basics.NAPIBasics;
import com.darwino.domino.napi.test.basics.NAPITimeTest;
import com.darwino.domino.napi.test.basics.NSFSearchTest;
import com.darwino.domino.napi.test.basics.NamesListTest;
import com.darwino.domino.napi.test.samples.Dbdesign_Makeview;
import com.darwino.domino.napi.test.samples.Views_Findbykeyextend;
import com.darwino.domino.napi.test.wrap.ACLTest;
import com.darwino.domino.napi.test.wrap.AltNames;
import com.darwino.domino.napi.test.wrap.Attachments;
import com.darwino.domino.napi.test.wrap.ChildCountTest;
import com.darwino.domino.napi.test.wrap.CompositeDataItem;
import com.darwino.domino.napi.test.wrap.Evaluate;
import com.darwino.domino.napi.test.wrap.FormsTest;
import com.darwino.domino.napi.test.wrap.ForumDocs;
import com.darwino.domino.napi.test.wrap.InvalidDoc;
import com.darwino.domino.napi.test.wrap.Item;
import com.darwino.domino.napi.test.wrap.MIMEItem;
import com.darwino.domino.napi.test.wrap.NSFView_EntriesByKey;
import com.darwino.domino.napi.test.wrap.NSFView_Fakenames;
import com.darwino.domino.napi.test.wrap.Note;
import com.darwino.domino.napi.test.wrap.NoteIDCollection;
import com.darwino.domino.napi.test.wrap.TestFileResource;
import com.darwino.domino.napi.test.wrap.TimeTest;
import com.darwino.domino.napi.test.wrap.ViewTests;
import com.darwino.domino.napi.test.wrap.WrapBasics;
import com.darwino.domino.napi.test.wrap.WrapFolders;
import com.darwino.domino.napi.test.wrap.WrapNamesTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	// Basics
	Fonts.class,
	NAPIBasics.class,
	CreateDatabase.class,
	Documents.class,
	NAPITimeTest.class,
	ModifiedTableTest.class,
	NamesListTest.class,
	Folders.class,
	
	// Samples
	Dbdesign_Makeview.class,
	Views_Findbykeyextend.class,
	
	// Wrappers
	NSFView_EntriesByKey.class,
	NSFView_Fakenames.class,
	WrapBasics.class,
	Evaluate.class,
	TimeTest.class,
	Note.class,
	Item.class,
	Attachments.class,
	MIMEItem.class,
	NoteIDCollection.class,
	NSFSearchTest.class,
	MIMETest.class,
	CompositeDataItem.class,
	InvalidDoc.class,
	ForumDocs.class,
	ViewTests.class,
	WrapNamesTest.class,
	WrapFolders.class,
	FormsTest.class,
	ACLTest.class,
	TestFileResource.class,
	AltNames.class,
	
	ChildCountTest.class,
	
	// Cleanup
	DeleteDatabase.class
})
@SuppressWarnings("nls")
public class AllTests {
	
	public static String TEST_DB_NAME;
	
	public static String TWOKEY_DB_NAME;

	public static String MIMEDOCS_DB_NAME;
	/** A known UNID contained in MIMEDocs.nsf with a single MIME entity for Body */
	public static String MIMEDOCS_DOC_NORMAL_UNID = "32FBA276E5E18BFA85257ED100428E3E"; //$NON-NLS-1$
	/** A known UNID contained in MIMEDocs.nsf with a large file attachment in Body */
	public static String MIMEDOCS_DOC_LARGEATT_UNID = "3E79C0A175E0A3A605257F1400499AFB"; //$NON-NLS-1$
	/** A known UNID contained in MIMEDocs.nsf with a multipart Body with an embedded image */
	public static String MIMEDOCS_DOC_MULTIPART_UNID = "C293E202703950CA85257ED100428E3F"; //$NON-NLS-1$
	/** A known UNID of a document that contains a date/time in a half-hour time zone */
	public static String MIMEDOCS_HALF_HOUR_TZ_UNID = "69301A18540DE5DB05257F52007FFB6F"; //$NON-NLS-1$
	
	public static String FAKENAMES_DB_NAME;
	/** A known UNID contained in FakeNames.nsf */
	public static String FAKENAMES_DOC_UNID = "5996EEDC6D56DE9205257838007ABA01"; //$NON-NLS-1$
	
	public static String EDGE_CASE_DB_NAME;
	/** A known UNID of an invalid document in 32k.nsf */
	public static String INVALID_DOC_UNID = "B98406A76B7E3A9485257EFF004CB0C1"; //$NON-NLS-1$
	/** A known UNID of a doc with arbitrary data in ODA's "chunk" format. */
	public static String ARBITRARY_DATA_DOC_UNID = "8E16DAE2F1603A6405257F6B00592252"; //$NON-NLS-1$
	
	public static String FORUM_DOC_DB_NAME;
	public static String FORUM_DOC_DB_REPLICAID = "85257F06007A13ED"; //$NON-NLS-1$
	/** A known UNID of a document that causes a crash when reading $UpdatedBy */
	public static String FORUM_DOC_UPDATEDBY_UNID = "CA5AA91172026A9A85257F06007A17C4"; //$NON-NLS-1$
	/** A known UNID of a document that causes an HTMLAPI exception in nd6forum.nsf */
	public static String FORUM_DOC_HTMLAPI_UNID = "7EBA5013A67A675A85257F06007A7A68"; //$NON-NLS-1$
	/** A known UNID of a document that contains basic composite data */
	public static String FORUM_DOC_BASICCD_UNID = "153235EDCD640E2A05257F19005215EB"; //$NON-NLS-1$
	/** A known UNID of a document that contains an attachment in the body */
	public static String FORUM_DOC_ATTACHMENT_UNID = "6605FE15EA9AA77A05257F1900522F02"; //$NON-NLS-1$
	/** A known UNID of a document that contains two attachments with the same original name */
	public static String FORUM_DOC_DOUBLE_ATTACHMENT_UNID = "32713EA860B5453105257F19005A0793"; //$NON-NLS-1$
	/** A known UNID of a document that contains double-wildcard date/time values */
	public static String FORUM_DOC_WILDCARD_DTS_UNID = "893FDA7E345CB2F405257F530080EA01"; //$NON-NLS-1$
	
	public static String DARWINOSYNC_DB_NAME;
	
	public static String V2TESTER_DB_NAME;
	/** A known UNID of a document that contains an old-style username field */
	public static String V2TESTER_USERID_UNID = "0000038734BCEA0785257F8E00003F79";
	
	public static String DESIGN_DB_NAME;
	/** A known UNID of a file resource note */
	public static String DESIGN_DOC_FILERES_UNID = "714061170AE3D229042580FE006AAA31";
	/** A known UNID of an image stored as a file resource note */
	public static String DESIGN_DOC_IMAGE_UNID = "95C33B09BBBFD7CE852580FF004B4B28";
	
	public static long getTestDatabase() throws DominoException {
		return DominoAPI.get().NSFDbOpen(AllTests.TEST_DB_NAME);
	}
	public static long getFakeNamesDatabase() throws DominoException {
		return DominoAPI.get().NSFDbOpen(AllTests.FAKENAMES_DB_NAME);
	}
	public static long getTwokeyDatabase() throws DominoException {
		return DominoAPI.get().NSFDbOpen(AllTests.TWOKEY_DB_NAME);
	}
	public static long getMimeDocsDatabase() throws DominoException {
		return DominoAPI.get().NSFDbOpen(MIMEDOCS_DB_NAME);
	}
	public static long getEdgeCasesDatabase() throws DominoException {
		return DominoAPI.get().NSFDbOpen(AllTests.EDGE_CASE_DB_NAME);
	}
	public static long getForumDocDatabase() throws DominoException {
		return DominoAPI.get().NSFDbOpen(AllTests.FORUM_DOC_DB_NAME);
	}
	public static long getDarwinoSyncDatabase() throws DominoException {
		return DominoAPI.get().NSFDbOpen(AllTests.DARWINOSYNC_DB_NAME);
	}
	public static long getV2TesterDatabase() throws DominoException {
		return DominoAPI.get().NSFDbOpen(AllTests.V2TESTER_DB_NAME);
	}
	
	@BeforeClass
	public static void init() throws Exception {
		Platform.getInstance().log("AllTests.init"); //$NON-NLS-1$
		
		String notesIni = System.getenv("NotesINI"); //$NON-NLS-1$
		if(StringUtil.isNotEmpty(notesIni)) {
			// There should also be a Notes_ExecDirectory
			String exec = System.getenv("Notes_ExecDirectory"); //$NON-NLS-1$
			Platform.getInstance().log("Init ini={0}, exec={1}", notesIni, exec); //$NON-NLS-1$
			DominoAPI.get().NotesInitExtended(exec, "=" + notesIni); //$NON-NLS-1$
		} else {
			DominoAPI.get().NotesInitExtended();
		}
		Platform.getInstance().log("NotesInitExtended complete"); //$NON-NLS-1$
		
		{
			File tempFile = File.createTempFile("testdb", ".nsf"); //$NON-NLS-1$ //$NON-NLS-2$
			Platform.getInstance().log("testdb location is {0}", tempFile.getAbsolutePath()); //$NON-NLS-1$
			TEST_DB_NAME = tempFile.getAbsolutePath();
			tempFile.delete();
		}

		TWOKEY_DB_NAME = instantiateDb("twokey"); //$NON-NLS-1$
		FAKENAMES_DB_NAME = instantiateDb("FakeNames"); //$NON-NLS-1$
		MIMEDOCS_DB_NAME = instantiateDb("MIMEDocs"); //$NON-NLS-1$
		EDGE_CASE_DB_NAME = instantiateDb("32k"); //$NON-NLS-1$
		FORUM_DOC_DB_NAME = instantiateDb("forumdocs"); //$NON-NLS-1$
		DARWINOSYNC_DB_NAME = instantiateDb("darwinosync-cg"); //$NON-NLS-1$
		V2TESTER_DB_NAME = instantiateDb("v2tester");
		DESIGN_DB_NAME = instantiateDb("designtestbed");
	}
	@AfterClass
	public static void term() throws DominoException {
		Platform.getInstance().log("AllTests.term"); //$NON-NLS-1$
		DominoAPI.get().NotesTerm();
		Platform.getInstance().log("NotesTerm complete"); //$NON-NLS-1$
		
		deleteDb(TWOKEY_DB_NAME);
		deleteDb(FAKENAMES_DB_NAME);
		deleteDb(MIMEDOCS_DB_NAME);
		deleteDb(EDGE_CASE_DB_NAME);
		deleteDb(FORUM_DOC_DB_NAME);
		deleteDb(DARWINOSYNC_DB_NAME);
		deleteDb(V2TESTER_DB_NAME);
		deleteDb(DESIGN_DB_NAME);
	}
	
	
	/* ******************************************************************************
	 * Internal utility methods
	 ********************************************************************************/
	
	@SuppressWarnings("resource")
	private static String instantiateDb(String basename) throws IOException {
		File dbFile = File.createTempFile(basename, ".nsf"); //$NON-NLS-1$
		Platform.getInstance().log("{0} location is {1}", basename, dbFile.getAbsolutePath()); //$NON-NLS-1$
		FileOutputStream fos = new FileOutputStream(dbFile);
		InputStream is = AllTests.class.getResourceAsStream("/" + basename + ".nsf"); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			StreamUtil.copyStream(is, fos);
		} finally {
			StreamUtil.close(fos);
			StreamUtil.close(is);
		}
		return dbFile.getAbsolutePath();
	}
	
	private static void deleteDb(String dbName) {
		try {
			File testDB = new File(dbName);
			if(testDB.exists()) {
				testDB.delete();
			}
		} catch(Exception e) { }
	}
}
