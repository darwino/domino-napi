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

package com.darwino.domino.napi.wrap;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.enums.ACLEntryFlag;
import com.darwino.domino.napi.enums.ACLLevel;

/**
 * Represents an entry within the ACL of a database. Does not maintain any open handles of its own
 * 
 * @author Jesse Gallagher
 * @since 2.0.0
 */
public class NSFACLEntry {
	private final NSFACL parent;
	private String name;
	private ACLLevel accessLevel;
	private Set<String> roles;
	private Set<ACLEntryFlag> flags;
	
	public NSFACLEntry(NSFACL parent, String name, ACLLevel accessLevel, Collection<String> roles, Collection<ACLEntryFlag> flags) {
		this.parent = parent;
		this.name = name;
		this.accessLevel = accessLevel;
		this.roles = new HashSet<String>(roles);
		this.flags = EnumSet.copyOf(flags);
	}
	
	public String getName() {
		return name;
	}
	
	public NSFACL getParent() {
		return parent;
	}
	
	public ACLLevel getAccessLevel() {
		return accessLevel;
	}
	
	public Set<String> getRoles() {
		return new HashSet<String>(roles);
	}
	
	public Set<ACLEntryFlag> getFlags() {
		return EnumSet.copyOf(flags);
	}
	
	/**
	 * @return whether the entry is the "-Default-" entry for the ACL
	 */
	public boolean isDefaultEntry() {
		return DominoAPI.ACL_DEFAULT_NAME.equals(name);
	}
	
	/**
	 * @return whether the entry is for the "Anonymous" user
	 */
	public boolean isAnonymous() {
		return DominoAPI.ACL_ANONYMOUS_NAME.equals(name);
	}
}
