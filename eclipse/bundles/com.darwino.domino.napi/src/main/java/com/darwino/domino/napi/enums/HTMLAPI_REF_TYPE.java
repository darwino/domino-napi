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
public enum HTMLAPI_REF_TYPE implements INumberEnum<Integer> {
	UNKNOWN(DominoAPI.HTMLAPI_REF_UNKNOWN), HREF(DominoAPI.HTMLAPI_REF_HREF),
	IMG(DominoAPI.HTMLAPI_REF_IMG), FRAME(DominoAPI.HTMLAPI_REF_FRAME),
	APPLET(DominoAPI.HTMLAPI_REF_APPLET), EMBED(DominoAPI.HTMLAPI_REF_EMBED),
	OBJECT(DominoAPI.HTMLAPI_REF_OBJECT), BASE(DominoAPI.HTMLAPI_REF_BASE),
	BACKGROUND(DominoAPI.HTMLAPI_REF_BACKGROUND), CID(DominoAPI.HTMLAPI_REF_CID);
	
	private final int value;
	
	private HTMLAPI_REF_TYPE(int value) {
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
