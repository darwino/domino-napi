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
 * (editods.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 * @since Notes/Domino 4.0
 */
public enum CDKEYWORD_Flag implements INumberEnum<Short> {
	RADIO(DominoAPI.CDKEYWORD_RADIO), FRAME_3D(DominoAPI.CDKEYWORD_FRAME_3D),
	FRAME_STANDARD(DominoAPI.CDKEYWORD_FRAME_STANDARD), FRAME_NONE(DominoAPI.CDKEYWORD_FRAME_NONE),
	KEYWORD_RTL(DominoAPI.CDKEYWORD_KEYWORD_RTL), RO_ACTIVE(DominoAPI.CDKEYWORD_RO_ACTIVE);
	
	private final short value;
	
	private CDKEYWORD_Flag(short value) {
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
