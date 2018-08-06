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
 * This enum represents the <code>NOTE_CLASS_xxx</code> values, other than the "mask" ones. (nsfnote.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.1
 */
public enum NoteClass implements INumberEnum<Short> {
	DOCUMENT(DominoAPI.NOTE_CLASS_DOCUMENT), DATA(DominoAPI.NOTE_CLASS_DATA), INFO(DominoAPI.NOTE_CLASS_INFO),
	FORM(DominoAPI.NOTE_CLASS_FORM), VIEW(DominoAPI.NOTE_CLASS_VIEW), ICON(DominoAPI.NOTE_CLASS_ICON),
	/** This use of "design" refers to the special view containing the list of design notes, not a design note mask */
	DESIGN(DominoAPI.NOTE_CLASS_DESIGN),
	ACL(DominoAPI.NOTE_CLASS_ACL), HELP_INDEX(DominoAPI.NOTE_CLASS_HELP_INDEX), HELP(DominoAPI.NOTE_CLASS_HELP),
	FILTER(DominoAPI.NOTE_CLASS_FILTER), FIELD(DominoAPI.NOTE_CLASS_FIELD), REPLFORMULA(DominoAPI.NOTE_CLASS_REPLFORMULA),
	PRIVATE(DominoAPI.NOTE_CLASS_PRIVATE);
	
	private final short value;
	
	private NoteClass(short value) {
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
