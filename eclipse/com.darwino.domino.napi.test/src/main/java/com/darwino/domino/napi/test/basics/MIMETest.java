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

package com.darwino.domino.napi.test.basics;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.Platform;
import com.ibm.commons.util.io.StreamUtil;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.c.IntRef;
import com.darwino.domino.napi.c.ShortRef;
import com.darwino.domino.napi.struct.BLOCKID;
import com.darwino.domino.napi.struct.MIME_PART;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.test.AllTests;
import com.darwino.domino.napi.util.DominoNativeUtils;
import com.darwino.domino.napi.wrap.NSFDatabase;
import com.darwino.domino.napi.wrap.NSFNote;
import com.darwino.domino.napi.wrap.NSFNoteIDCollection;
import com.darwino.domino.napi.wrap.NSFSession;

public class MIMETest {
	public static final String BODY_ITEM = "Body"; //$NON-NLS-1$
	
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", MIMETest.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", MIMETest.class.getSimpleName()); //$NON-NLS-1$
	}
	
	@Test
	public void testMimeItems() throws DominoException, MessagingException, IOException {
		DominoAPI api = DominoAPI.get();
		
		NSFSession session = new NSFSession(api);
		NSFDatabase database = session.getDatabase(AllTests.MIMEDOCS_DB_NAME);
		TIMEDATE forever = new TIMEDATE();
		api.TimeConstant(DominoAPI.TIMEDATE_WILDCARD, forever);
		try {
			NSFNoteIDCollection notes = database.getModifiedNotes(DominoAPI.NOTE_CLASS_DATA, forever);
			for(int noteId : notes) {
				Platform.getInstance().log("checking note ID {0}", Integer.toString(noteId, 16)); //$NON-NLS-1$
				NSFNote note = database.getNoteByID(noteId);
				BLOCKID itemBlockId = new BLOCKID();
				ShortRef valueDataType = new ShortRef();
		    	BLOCKID valueBlockId = new BLOCKID();
		    	IntRef valueLen = new IntRef();
				try {
					// We'll need the itemBlockId value here for getting subsequent items
		    		// We know that valueDataType is going to say it's MIME, but hey
		    		api.NSFItemInfo(note.getHandle(), BODY_ITEM, itemBlockId, valueDataType, valueBlockId, valueLen);
		    		readMimeItem(api, note, valueBlockId, valueLen);
		    		
		    		int breaker = 0;
					while(true) {
						try {
							api.NSFItemInfoNext(note.getHandle(), itemBlockId, BODY_ITEM, itemBlockId, valueDataType, valueBlockId, valueLen);
							readMimeItem(api, note, valueBlockId, valueLen);
						} catch(DominoException e) {
							if(e.getStatus() == DominoAPI.ERR_ITEM_NOT_FOUND) {
								break;
							} else {
								throw e;
							}
						}
						if(breaker++ > 1000) {
							throw new RuntimeException("Probable infinite loop detected");
						}
					}
				} finally {
		    		itemBlockId.free();
		    		valueBlockId.free();
					note.free();
				}
			}
		} finally {
			forever.free();
			database.free();
			session.free();
		}
	}
	
	private void readMimeItem(DominoAPI api, NSFNote note, BLOCKID valueBlockId, IntRef valueLen) throws DominoException, IOException, MessagingException {
		int length = valueLen.get();
		if(length == 0 || length == C.sizeOfUSHORT) {
			// Then don't do anything
		} else {
			Platform.getInstance().log("Got data length {0}", length); //$NON-NLS-1$
    		long valuePtr = api.OSLockBlock(valueBlockId);
			try {
				// This will come back as raw bytes
				MIME_PART result = (MIME_PART)DominoNativeUtils.readItemValue(api, valuePtr, length, note.getHandle());
				try {
				
					MimeBodyPart part = result.toMimeBodyPart();
					Platform.getInstance().log("===headers:"); //$NON-NLS-1$
					@SuppressWarnings("unchecked")
					List<Header> headers = Collections.list(part.getAllHeaders());
					boolean printable = false;
					for(Header header : headers) {
						String name = header.getName();
						String value = header.getValue();
						Platform.getInstance().log("\t{0}: {1}", name, value); //$NON-NLS-1$
						if(name.equalsIgnoreCase("content-type") && (value.startsWith("multipart") || value.startsWith("text"))) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							printable = true;
						}
					}
					if(printable) {
						Platform.getInstance().log("===Content"); //$NON-NLS-1$
						Platform.getInstance().log("{0}", StreamUtil.readString(part.getInputStream())); //$NON-NLS-1$
					}
					Platform.getInstance().log("done."); //$NON-NLS-1$
				} finally {
					result.free();
				}
			} finally {
				api.OSUnlockBlock(valueBlockId);
			}
		}
	}
}
