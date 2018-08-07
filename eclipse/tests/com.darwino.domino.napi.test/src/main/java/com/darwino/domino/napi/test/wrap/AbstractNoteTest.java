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

import org.junit.After;
import org.junit.Before;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.NSFSession;

/**
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
@SuppressWarnings("nls")
public abstract class AbstractNoteTest {
	protected NSFSession session;
	protected NSFDatabase database;
	protected NSFNote note;
	protected NSFDatabase mimeDocs;
	protected NSFDatabase forumDocs;
	protected NSFDatabase edgeCases;
	protected NSFDatabase designTestbed;
	
	@Before
	public void createNote() throws DominoException {
		Platform.getInstance().log("createNote start"); //$NON-NLS-1$
		
		this.session = new NSFSession(DominoAPI.get());
		this.database = session.getDatabase(AllTests.TEST_DB_NAME);
		this.note = database.createNote();
		this.mimeDocs = session.getDatabase(AllTests.MIMEDOCS_DB_NAME);
		this.forumDocs = session.getDatabase(AllTests.FORUM_DOC_DB_NAME);
		this.edgeCases = session.getDatabase(AllTests.EDGE_CASE_DB_NAME);
		this.designTestbed = session.getDatabase(AllTests.DESIGN_DB_NAME);

		Platform.getInstance().log("createNote end"); //$NON-NLS-1$
	}
	
	@After
	public void destroyNote() throws DominoException {
		Platform.getInstance().log("destroyNote start, handle {0}, class name {1}", note.getHandle(), getClass().getName()); //$NON-NLS-1$

		System.out.println("going to free designTestbed");
		designTestbed.free();
		System.out.println("going to free edgeCases");
		edgeCases.free();
		System.out.println("going to free forumDocs");
		forumDocs.free();
		System.out.println("going to free mimeDocs");
		mimeDocs.free();
		System.out.println("going to free note");
		note.free();
		System.out.println("going to free database");
		database.free();
		System.out.println("going to free session");
		session.free();
		
		Platform.getInstance().log("destroyNote end"); //$NON-NLS-1$
	}
}
