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
 * HOST_xxx
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public enum FileObjectHost implements INumberEnum<Short> {
	MSDOS(DominoAPI.HOST_MSDOS), OLE(DominoAPI.HOST_OLE), MAC(DominoAPI.HOST_MAC), UNKNOWN(DominoAPI.HOST_UNKNOWN),
	HPFS(DominoAPI.HOST_HPFS), OLELIB(DominoAPI.HOST_OLELIB), BYTEARRAY_EXT(DominoAPI.HOST_BYTEARRAY_EXT),
	BYTEARRAY_PAGE(DominoAPI.HOST_BYTEARRAY_PAGE), CDSTORAGE(DominoAPI.HOST_CDSTORAGE), STREAM(DominoAPI.HOST_STREAM),
	LINK(DominoAPI.HOST_LINK);
	
	private final short value;
	
	private FileObjectHost(short value) {
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
