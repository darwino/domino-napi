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
public enum FieldType implements INumberEnum<Short> {
	ERROR(DominoAPI.FIELD_TYPE_ERROR), NUMBER(DominoAPI.FIELD_TYPE_NUMBER),
	TIME(DominoAPI.FIELD_TYPE_TIME), RICH_TEXT(DominoAPI.FIELD_TYPE_RICH_TEXT),
	AUTHORS(DominoAPI.FIELD_TYPE_AUTHORS), READERS(DominoAPI.FIELD_TYPE_READERS),
	NAMES(DominoAPI.FIELD_TYPE_NAMES), KEYWORDS(DominoAPI.FIELD_TYPE_KEYWORDS),
	TEXT(DominoAPI.FIELD_TYPE_TEXT), SECTION(DominoAPI.FIELD_TYPE_SECTION),
	PASSWORD(DominoAPI.FIELD_TYPE_PASSWORD), FORMULA(DominoAPI.FIELD_TYPE_FORMULA),
	TIMEZONE(DominoAPI.FIELD_TYPE_TIMEZONE), COLORCTL(DominoAPI.FIELD_TYPE_COLORCTL);
	
	private short value;
	
	private FieldType(short value) {
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
