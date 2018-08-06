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
public enum CmdArgValueType implements INumberEnum<Integer> {
	String(DominoAPI.CAVT_String), Int(DominoAPI.CAVT_Int),
	NoteId(DominoAPI.CAVT_NoteId), UNID(DominoAPI.CAVT_UNID),
	StringList(DominoAPI.CAVT_StringList);
	
	private final int value;
	
	private CmdArgValueType(int value) {
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
