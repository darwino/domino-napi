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
 * COMPRESS_xxx
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 */
public enum FileObjectCompress implements INumberEnum<Short> {
	NONE(DominoAPI.COMPRESS_NONE), HUFF(DominoAPI.COMPRESS_HUFF),
	LZ1(DominoAPI.COMPRESS_LZ1), RECOMPRESS_HUFF(DominoAPI.RECOMPRESS_HUFF);
	
	private final short value;
	
	private FileObjectCompress(short value) {
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
