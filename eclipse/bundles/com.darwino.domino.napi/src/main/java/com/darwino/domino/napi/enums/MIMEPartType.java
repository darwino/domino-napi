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
public enum MIMEPartType implements INumberEnum<Byte> {
	PROLOG(DominoAPI.MIME_PART_PROLOG), BODY(DominoAPI.MIME_PART_BODY),
	EPILOG(DominoAPI.MIME_PART_EPILOG), RETRIEVE_INFO(DominoAPI.MIME_PART_RETRIEVE_INFO),
	MESSAGE(DominoAPI.MIME_PART_MESSAGE);
	
	private final byte value;
	
	private MIMEPartType(byte value) {
		this.value = value;
	}
	
	@Override
	public Byte getValue() {
		return value;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
}
