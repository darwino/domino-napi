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
 * @since 1.5.0
 *
 */
public enum FieldFlag implements INumberEnum<Short> {
	READWRITERS(DominoAPI.FREADWRITERS), EDITABLE(DominoAPI.FEDITABLE),
	NAMES(DominoAPI.FNAMES), STOREDV(DominoAPI.FSTOREDV), READERS(DominoAPI.FREADERS),
	SECTION(DominoAPI.FSECTION), SPARE3(DominoAPI.FSPARE3), V3FAB(DominoAPI.FV3FAB),
	COMPUTED(DominoAPI.FCOMPUTED), KEYWORDS(DominoAPI.FKEYWORDS), PROTECTED(DominoAPI.FPROTECTED),
	REFERENCE(DominoAPI.FREFERENCE), SIGN(DominoAPI.FSIGN), SEAL(DominoAPI.FSEAL),
	KEYWORDS_UI_STANDARD(DominoAPI.FKEYWORDS_UI_STANDARD),
	KEYWORDS_UI_CHECKBOX(DominoAPI.FKEYWORDS_UI_CHECKBOX),
	KEYWORDS_UI_RADIOBUTTON(DominoAPI.FKEYWORDS_UI_RADIOBUTTON),
	KEYWORDS_UI_ALLOW_NEW(DominoAPI.FKEYWORDS_UI_ALLOW_NEW);
	
	private short value;
	
	private FieldFlag(short value) {
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
