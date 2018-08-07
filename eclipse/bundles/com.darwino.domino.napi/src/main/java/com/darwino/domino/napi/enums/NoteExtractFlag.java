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
 * @since 0.8.0
 *
 */
public enum NoteExtractFlag implements INumberEnum<Short> {
	RESONLY(DominoAPI.NTEXT_RESONLY), FTYPE_MASK(DominoAPI.NTEXT_FTYPE_MASK), FTYPE_FLAT(DominoAPI.NTEXT_FTYPE_FLAT),
	FTYPE_MACBIN(DominoAPI.NTEXT_FTYPE_MACBIN), RAWMIME(DominoAPI.NTEXT_RAWMIME), IGNORE_HUFF2(DominoAPI.NTEXT_IGNORE_HUFF2);
	
	private final short value;
	
	private NoteExtractFlag(short value) {
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
