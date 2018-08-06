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
public enum MIMEPartFlag implements INumberEnum<Integer> {
	HAS_BOUNDARY(DominoAPI.MIME_PART_HAS_BOUNDARY), HAS_HEADERS(DominoAPI.MIME_PART_HAS_HEADERS),
	BODY_IN_DBOBJECT(DominoAPI.MIME_PART_BODY_IN_DBOBJECT), SHARED_DBOBJECT(DominoAPI.MIME_PART_SHARED_DBOBJECT),
	SKIP_FOR_CONVERSION(DominoAPI.MIME_PART_SKIP_FOR_CONVERSION);
	
	private int value;
	
	private MIMEPartFlag(int value) {
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
