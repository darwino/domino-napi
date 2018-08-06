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

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.enums.ACLEntryFlag;
import com.darwino.domino.napi.enums.ACLLevel;
import com.darwino.domino.napi.wrap.NSFACL;
import com.darwino.domino.napi.wrap.NSFACLEntry;
import com.darwino.domino.napi.wrap.NSFACLHistoryEntry;
import com.darwino.domino.napi.wrap.NSFDateTime;

@SuppressWarnings("nls")
public class ACLTest extends AbstractNoteTest {
	@Test
	public void testForumDocsACL() throws Exception {
		NSFACL acl = forumDocs.getACL();
		assertNotEquals("acl should not be null", null, acl);
		
		List<NSFACLEntry> entries = acl.getEntries();
		assertNotEquals("entries should not be null", null, entries);
		
		assertEquals("entry count should match expected", 6, entries.size());
		
		{
			NSFACLEntry entry = entries.get(0);
			assertEquals("entry 0 should have expected name", DominoAPI.ACL_DEFAULT_NAME, entry.getName());
			assertEquals("level should match", ACLLevel.AUTHOR, entry.getAccessLevel());
			
			Set<ACLEntryFlag> flags = entry.getFlags();
			assertNotEquals("flags should not be null", null, flags);
			Set<ACLEntryFlag> expected = EnumSet.of(ACLEntryFlag.CREATE_PRFOLDER, ACLEntryFlag.PUBLICREADER);
			assertEquals("flags should match expected", expected, flags);
			
			Set<String> roles = entry.getRoles();
			assertNotEquals("roles should not be null", null, flags);
			Set<String> expectedRoles = Collections.emptySet();
			assertEquals("roles should match expected", expectedRoles, roles);
		}
		{
			NSFACLEntry entry = entries.get(1);
			assertEquals("entry 1 should have expected name", "Anonymous", entry.getName());
			assertEquals("level should match", ACLLevel.NOACCESS, entry.getAccessLevel());
			
			Set<ACLEntryFlag> flags = entry.getFlags();
			assertNotEquals("flags should not be null", null, flags);
			Set<ACLEntryFlag> expected = EnumSet.of(ACLEntryFlag.AUTHOR_NOCREATE, ACLEntryFlag.NODELETE);
			assertEquals("flags should match expected", expected, flags);
			
			Set<String> roles = entry.getRoles();
			assertNotEquals("roles should not be null", null, flags);
			Set<String> expectedRoles = Collections.emptySet();
			assertEquals("roles should match expected", expectedRoles, roles);
		}
		{
			NSFACLEntry entry = entries.get(2);
			assertEquals("entry 2 should have expected name", "CN=Jesse Gallagher/O=IKSG", entry.getName());
			assertEquals("level should match", ACLLevel.MANAGER, entry.getAccessLevel());
			
			Set<ACLEntryFlag> flags = entry.getFlags();
			assertNotEquals("flags should not be null", null, flags);
			Set<ACLEntryFlag> expected = EnumSet.of(ACLEntryFlag.PERSON);
			assertEquals("flags should match expected", expected, flags);
			
			Set<String> roles = entry.getRoles();
			assertNotEquals("roles should not be null", null, flags);
			Set<String> expectedRoles = Collections.emptySet();
			assertEquals("roles should match expected", expectedRoles, roles);
		}
		{
			NSFACLEntry entry = entries.get(3);
			assertEquals("entry 3 should have expected name", "LocalDomainAdmins", entry.getName());
			assertEquals("level should match", ACLLevel.MANAGER, entry.getAccessLevel());
			
			Set<ACLEntryFlag> flags = entry.getFlags();
			assertNotEquals("flags should not be null", null, flags);
			Set<ACLEntryFlag> expected = EnumSet.of(ACLEntryFlag.CREATE_PRAGENT, ACLEntryFlag.CREATE_PRFOLDER, ACLEntryFlag.CREATE_FOLDER, ACLEntryFlag.CREATE_LOTUSSCRIPT, ACLEntryFlag.PUBLICREADER, ACLEntryFlag.PUBLICWRITER, ACLEntryFlag.GROUP, ACLEntryFlag.PERSON);
			assertEquals("flags should match expected", expected, flags);
			
			Set<String> roles = entry.getRoles();
			assertNotEquals("roles should not be null", null, flags);
			Set<String> expectedRoles = new HashSet<String>(Arrays.asList("[Configuration]"));
			assertEquals("roles should match expected", expectedRoles, roles);
		}
		{
			NSFACLEntry entry = entries.get(4);
			assertEquals("entry 4 should have expected name", "LocalDomainServers", entry.getName());
			assertEquals("level should match", ACLLevel.MANAGER, entry.getAccessLevel());
			
			Set<ACLEntryFlag> flags = entry.getFlags();
			assertNotEquals("flags should not be null", null, flags);
			Set<ACLEntryFlag> expected = EnumSet.of(ACLEntryFlag.CREATE_PRAGENT, ACLEntryFlag.CREATE_PRFOLDER, ACLEntryFlag.CREATE_FOLDER, ACLEntryFlag.CREATE_LOTUSSCRIPT, ACLEntryFlag.PUBLICREADER, ACLEntryFlag.PUBLICWRITER, ACLEntryFlag.GROUP, ACLEntryFlag.SERVER);
			assertEquals("flags should match expected", expected, flags);
			
			Set<String> roles = entry.getRoles();
			assertNotEquals("roles should not be null", null, flags);
			Set<String> expectedRoles = Collections.emptySet();
			assertEquals("roles should match expected", expectedRoles, roles);
		}
		{
			NSFACLEntry entry = entries.get(5);
			assertEquals("entry 5 should have expected name", "OtherDomainServers", entry.getName());
			assertEquals("level should match", ACLLevel.NOACCESS, entry.getAccessLevel());
			
			Set<ACLEntryFlag> flags = entry.getFlags();
			assertNotEquals("flags should not be null", null, flags);
			Set<ACLEntryFlag> expected = EnumSet.of(ACLEntryFlag.SERVER, ACLEntryFlag.GROUP, ACLEntryFlag.NODELETE, ACLEntryFlag.AUTHOR_NOCREATE);
			assertEquals("flags should match expected", expected, flags);
			
			Set<String> roles = entry.getRoles();
			assertNotEquals("roles should not be null", null, flags);
			Set<String> expectedRoles = Collections.emptySet();
			assertEquals("roles should match expected", expectedRoles, roles);
		}
		
		// Now check the history
		List<NSFACLHistoryEntry> history = acl.getHistory();
		assertNotEquals("history should be null", null, history);
		
		List<String> expected = Arrays.asList(
			"Jesse Gallagher/IKSG updated Jesse Gallagher/IKSG",
			"Jesse Gallagher/IKSG added Jesse Gallagher/IKSG",
			"Jesse Gallagher/IKSG updated role [Configuration]",
			"Jesse Gallagher/IKSG added Anonymous",
			"Jesse Gallagher/IKSG added LocalDomainAdmins",
			"Jesse Gallagher/IKSG added LocalDomainServers",
			"Jesse Gallagher/IKSG updated -Default-",
			"Jesse Gallagher/IKSG added OtherDomainServers",
			"Jesse Gallagher/IKSG updated -Default-"
		);
		assertEquals("lengths should be the same", expected.size(), history.size());
		for(int i = 0; i < expected.size(); i++) {
			assertEquals("history text should match at index " + i, expected.get(i), history.get(i).getText());
		}
	}
	
	@Test
	public void testACLModification() throws DominoException {
		NSFACL acl = forumDocs.getACL();
		assertNotEquals("acl should not be null", null, acl);
		NSFDateTime lastModified = acl.getLastModified();
		// Just check to make sure it's not null for now
		assertNotEquals("lastModified should not be null", null, lastModified);
	}
}
