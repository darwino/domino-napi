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
public enum CDGRAPHIC_FLAG implements INumberEnum<Byte> {
	/** Indicates that the DestSize field contains pixel values instead of twips */
	DESTSIZE_IS_PIXELS(DominoAPI.CDGRAPHIC_FLAG_DESTSIZE_IS_PIXELS),
	/** Spans lines and allows wrapping */
	SPANSLINES(DominoAPI.CDGRAPHIC_FLAG_SPANSLINES);
	
	private final byte value;
	
	private CDGRAPHIC_FLAG(byte value) {
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
