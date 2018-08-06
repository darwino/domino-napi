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
 * FEXT_xxx for the Flags1 field of CDEXTFIELD (editods.h)
 * 
 * <p>This enum does not cover the mask and shift values.</p>
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 * @since Notes/Domino 4.0
 */
public enum FEXT1 implements INumberEnum<Integer> {
	LOOKUP_EACHCHAR(DominoAPI.FEXT_LOOKUP_EACHCHAR), KWSELRECALC(DominoAPI.FEXT_KWSELRECALC),
	KWHINKYMINKY(DominoAPI.FEXT_KWHINKYMINKY), AFTERVALIDATION(DominoAPI.FEXT_AFTERVALIDATION),
	ACCEPT_CARET(DominoAPI.FEXT_ACCEPT_CARET), KEYWORD_FRAME_3D(DominoAPI.FEXT_KEYWORD_FRAME_3D),
	KEYWORD_FRAME_STANDARD(DominoAPI.FEXT_KEYWORD_FRAME_STANDARD),
	KEYWORD_FRAME_NONE(DominoAPI.FEXT_KEYWORD_FRAME_NONE), KEYWORDS_UI_COMBO(DominoAPI.FEXT_KEYWORDS_UI_COMBO),
	KEYWORDS_UI_LIST(DominoAPI.FEXT_KEYWORDS_UI_LIST);
	
	private final int value;
	
	private FEXT1(int value) {
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
