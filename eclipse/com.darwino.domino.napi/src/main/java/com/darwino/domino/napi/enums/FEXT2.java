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
 * @since Notes/Domino 4.5
 */
public enum FEXT2 implements INumberEnum<Integer> {
	KW_CHOICE_RECALC(DominoAPI.FEXT_KW_CHOICE_RECALC), HTML_IN_FIELDDEF(DominoAPI.FEXT_HTML_IN_FIELDDEF),
	HIDEDELIMITERS(DominoAPI.FEXT_HIDEDELIMITERS), KW_RTL_READING_ORDER(DominoAPI.FEXT_KW_RTL_READING_ORDER),
	ALLOWTABBINGOUT(DominoAPI.FEXT_ALLOWTABBINGOUT), PASSWORD(DominoAPI.FEXT_PASSWORD),
	USEAPPLETINBROWSER(DominoAPI.FEXT_USEAPPLETINBROWSER), CONTROL(DominoAPI.FEXT_CONTROL),
	LITERALIZE(DominoAPI.FEXT_LITERALIZE), CONTROLDYNAMIC(DominoAPI.FEXT_CONTROLDYNAMIC),
	RUNEXITINGONCHANGE(DominoAPI.FEXT_RUNEXITINGONCHANGE), TIMEZONE(DominoAPI.FEXT_TIMEZONE),
	PROPORTIONALHEIGHT(DominoAPI.FEXT_PROPORTIONALHEIGHT), PROPORTIONALWIDTH(DominoAPI.FEXT_PROPORTIONALWIDTH),
	SHOWIMSTATUS(DominoAPI.FEXT_SHOWIMSTATUS);
	
	private final int value;
	
	private FEXT2(int value) {
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
