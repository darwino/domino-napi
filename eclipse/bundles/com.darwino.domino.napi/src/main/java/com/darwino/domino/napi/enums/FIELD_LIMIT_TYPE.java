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
 * (htmlapi.h)
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public enum FIELD_LIMIT_TYPE implements INumberEnum<Integer> {
	PICTURE(DominoAPI.FIELD_LIMIT_TYPE_PICTURE), APPLET(DominoAPI.FIELD_LIMIT_TYPE_APPLET),
	SHAREDIMAGE(DominoAPI.FIELD_LIMIT_TYPE_SHAREDIMAGE), OBJECT(DominoAPI.FIELD_LIMIT_TYPE_OBJECT),
	TEXTONLY(DominoAPI.FIELD_LIMIT_TYPE_TEXTONLY), VIEW(DominoAPI.FIELD_LIMIT_TYPE_VIEW),
	CALENDAR(DominoAPI.FIELD_LIMIT_TYPE_CALENDAR), INBOX(DominoAPI.FIELD_LIMIT_TYPE_INBOX),
	ATTACHMENT(DominoAPI.FIELD_LIMIT_TYPE_ATTACHMENT), DATEPICKER(DominoAPI.FIELD_LIMIT_TYPE_DATEPICKER),
	GRAPHIC(DominoAPI.FIELD_LIMIT_TYPE_GRAPHIC), HELP(DominoAPI.FIELD_LIMIT_TYPE_HELP),
	CLEAR(DominoAPI.FIELD_LIMIT_TYPE_CLEAR), LINK(DominoAPI.FIELD_LIMIT_TYPE_LINK),
	THUMBNAIL(DominoAPI.FIELD_LIMIT_TYPE_THUMBNAIL);
	
	private final int value;
	
	private FIELD_LIMIT_TYPE(int value) {
		this.value = value;
	}
	
	@Override
	public Integer getValue() {
		return value;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
}
