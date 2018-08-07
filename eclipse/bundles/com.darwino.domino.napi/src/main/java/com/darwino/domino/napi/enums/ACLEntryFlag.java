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

package com.darwino.domino.napi.enums;

import com.darwino.domino.napi.DominoAPI;

/**
 * @author Jesse Gallagher
 * @since 2.0.0
 */
public enum ACLEntryFlag implements INumberEnum<Short> {
	AUTHOR_NOCREATE(DominoAPI.ACL_FLAG_AUTHOR_NOCREATE), SERVER(DominoAPI.ACL_FLAG_SERVER),
	NODELETE(DominoAPI.ACL_FLAG_NODELETE), CREATE_PRAGENT(DominoAPI.ACL_FLAG_CREATE_PRAGENT),
	CREATE_PRFOLDER(DominoAPI.ACL_FLAG_CREATE_PRFOLDER), PERSON(DominoAPI.ACL_FLAG_PERSON),
	GROUP(DominoAPI.ACL_FLAG_GROUP), CREATE_FOLDER(DominoAPI.ACL_FLAG_CREATE_FOLDER),
	CREATE_LOTUSSCRIPT(DominoAPI.ACL_FLAG_CREATE_LOTUSSCRIPT), PUBLICREADER(DominoAPI.ACL_FLAG_PUBLICREADER),
	PUBLICWRITER(DominoAPI.ACL_FLAG_PUBLICWRITER), MONITORS_DISALLOWED(DominoAPI.ACL_FLAG_MONITORS_DISALLOWED),
	NOREPLICATE(DominoAPI.ACL_FLAG_NOREPLICATE);
	
	private final short value;
	
	private ACLEntryFlag(short value) {
		this.value = value;
	}
	
	@Override
	public Short getValue() {
		return value;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
}
