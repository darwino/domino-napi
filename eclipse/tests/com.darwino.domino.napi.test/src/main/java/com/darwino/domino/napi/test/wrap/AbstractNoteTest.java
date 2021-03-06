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
