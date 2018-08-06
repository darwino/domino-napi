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
 * DBCLASS_xxx (nsfdb.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 *
 */
public enum DBClass implements INumberEnum<Short> {
	BY_EXTENSION(DominoAPI.DBCLASS_BY_EXTENSION), NSFTESTFILE(DominoAPI.DBCLASS_NSFTESTFILE),
	NOTEFILE(DominoAPI.DBCLASS_NOTEFILE), DESKTOP(DominoAPI.DBCLASS_DESKTOP),
	NOTECLIPBOARD(DominoAPI.DBCLASS_NOTECLIPBOARD), TEMPLATEFILE(DominoAPI.DBCLASS_TEMPLATEFILE),
	GIANTNOTEFILE(DominoAPI.DBCLASS_GIANTNOTEFILE), HUGENOTEFILE(DominoAPI.DBCLASS_HUGENOTEFILE),
	ONEDOCFILE(DominoAPI.DBCLASS_ONEDOCFILE), V2NOTEFILE(DominoAPI.DBCLASS_V2NOTEFILE),
	ENCAPSMAILFILE(DominoAPI.DBCLASS_ENCAPSMAILFILE), LRGENCAPSMAILFILE(DominoAPI.DBCLASS_LRGENCAPSMAILFILE),
	OBJSTORE(DominoAPI.DBCLASS_OBJSTORE), V3NOTEFILE(DominoAPI.DBCLASS_V3NOTEFILE),
	V4NOTEFILE(DominoAPI.DBCLASS_V4NOTEFILE), V5NOTEFILE(DominoAPI.DBCLASS_V5NOTEFILE),
	V6NOTEFILE(DominoAPI.DBCLASS_V6NOTEFILE), V8NOTEFILE(DominoAPI.DBCLASS_V8NOTEFILE),
	V85NOTEFILE(DominoAPI.DBCLASS_V85NOTEFILE), V9NOTEFILE(DominoAPI.DBCLASS_V9NOTEFILE);
	
	private short value;
	
	private DBClass(short value) {
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
	
	/**
	 * @return whether the database class represents a normal (non-template, non-special-case)
	 * 		database
	 */
	public boolean isDatabase() {
		switch(this) {
		case BY_EXTENSION:
			// Has to be determined by the caller
			return false;
		case GIANTNOTEFILE:
		case HUGENOTEFILE:
		case NOTEFILE:
		case V2NOTEFILE:
		case V3NOTEFILE:
		case V4NOTEFILE:
		case V5NOTEFILE:
		case V6NOTEFILE:
		case V85NOTEFILE:
		case V8NOTEFILE:
		case V9NOTEFILE:
			return true;
		case TEMPLATEFILE:
			return false;
		case DESKTOP:
		case ENCAPSMAILFILE:
		case LRGENCAPSMAILFILE:
		case NOTECLIPBOARD:
		case NSFTESTFILE:
		case OBJSTORE:
		case ONEDOCFILE:
		default:
			return false;
		}
	}
}
