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
public enum HOTSPOTREC_RUNFLAG implements INumberEnum<Integer> {
	BEGIN(DominoAPI.HOTSPOTREC_RUNFLAG_BEGIN), END(DominoAPI.HOTSPOTREC_RUNFLAG_END), BOX(DominoAPI.HOTSPOTREC_RUNFLAG_BOX),
	NOBORDER(DominoAPI.HOTSPOTREC_RUNFLAG_NOBORDER), FORMULA(DominoAPI.HOTSPOTREC_RUNFLAG_FORMULA),
	MOVIE(DominoAPI.HOTSPOTREC_RUNFLAG_MOVIE), IGNORE(DominoAPI.HOTSPOTREC_RUNFLAG_IGNORE), ACTION(DominoAPI.HOTSPOTREC_RUNFLAG_ACTION),
	SCRIPT(DominoAPI.HOTSPOTREC_RUNFLAG_SCRIPT), INOTES(DominoAPI.HOTSPOTREC_RUNFLAG_INOTES), ISMAP(DominoAPI.HOTSPOTREC_RUNFLAG_ISMAP),
	INOTES_AUTO(DominoAPI.HOTSPOTREC_RUNFLAG_INOTES_AUTO), ISMAP_INPUT(DominoAPI.HOTSPOTREC_RUNFLAG_ISMAP_INPUT), SIGNED(DominoAPI.HOTSPOTREC_RUNFLAG_SIGNED),
	ANCHOR(DominoAPI.HOTSPOTREC_RUNFLAG_ANCHOR), COMPUTED(DominoAPI.HOTSPOTREC_RUNFLAG_COMPUTED), TEMPLATE(DominoAPI.HOTSPOTREC_RUNFLAG_TEMPLATE),
	HIGHLIGHT(DominoAPI.HOTSPOTREC_RUNFLAG_HIGHLIGHT), EXTACTION(DominoAPI.HOTSPOTREC_RUNFLAG_EXTACTION), NAMEDELEM(DominoAPI.HOTSPOTREC_RUNFLAG_NAMEDELEM),
	WEBJAVASCRIPT(DominoAPI.HOTSPOTREC_RUNFLAG_WEBJAVASCRIPT);
	
	private final int value;
	
	private HOTSPOTREC_RUNFLAG(int value) {
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.enums.INumberEnum#getValue()
	 */
	@Override
	public Integer getValue() {
		return value;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}

}
