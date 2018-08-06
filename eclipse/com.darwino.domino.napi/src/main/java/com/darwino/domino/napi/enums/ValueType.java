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

import static com.darwino.domino.napi.DominoAPI.*;

/**
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public enum ValueType implements INumberEnum<Short> {
	ERROR(TYPE_ERROR), UNAVAILABLE(TYPE_UNAVAILABLE), TEXT(TYPE_TEXT), TEXT_LIST(TYPE_TEXT_LIST),
	NUMBER(TYPE_NUMBER), NUMBER_RANGE(TYPE_NUMBER_RANGE), TIME(TYPE_TIME), TIME_RANGE(TYPE_TIME_RANGE),
	FORMULA(TYPE_FORMULA), USERID(TYPE_USERID), INVALID_OR_UNKNOWN(TYPE_INVALID_OR_UNKNOWN),
	COMPOSITE(TYPE_COMPOSITE), COLLATION(TYPE_COLLATION), OBJECT(TYPE_OBJECT), NOTEREF_LIST(TYPE_NOTEREF_LIST),
	VIEW_FORMAT(TYPE_VIEW_FORMAT), ICON(TYPE_ICON), NOTELINK_LIST(TYPE_NOTELINK_LIST), SIGNATURE(TYPE_SIGNATURE),
	SEAL(TYPE_SEAL), SEALDATA(TYPE_SEALDATA), SEAL_LIST(TYPE_SEAL_LIST), HIGHLIGHTS(TYPE_HIGHLIGHTS),
	WORKSHEET_DATA(TYPE_WORKSHEET_DATA), USERDATA(TYPE_USERDATA), QUERY(TYPE_QUERY), ACTION(TYPE_ACTION),
	ASSISTANT_INFO(TYPE_ASSISTANT_INFO), VIEWMAP_DATASET(TYPE_VIEWMAP_DATASET), VIEWMAP_LAYOUT(TYPE_VIEWMAP_LAYOUT),
	LSOBJECT(TYPE_LSOBJECT), HTML(TYPE_HTML), SCHED_LIST(TYPE_SCHED_LIST), CALENDAR_FORMAT(TYPE_CALENDAR_FORMAT),
	MIME_PART(TYPE_MIME_PART), RFC822_TEXT(TYPE_RFC822_TEXT);
	
	private short value;
	
	private ValueType(short value) {
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