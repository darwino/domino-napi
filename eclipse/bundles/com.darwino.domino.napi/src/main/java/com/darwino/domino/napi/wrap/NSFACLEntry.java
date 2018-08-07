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
