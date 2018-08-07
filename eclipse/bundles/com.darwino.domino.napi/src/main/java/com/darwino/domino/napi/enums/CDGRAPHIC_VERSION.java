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
public enum CDGRAPHIC_VERSION implements INumberEnum<Byte> {
	/** Graphics Format 1 - created by Notes version 2 */
	VERSION1(DominoAPI.CDGRAPHIC_VERSION1),
	/** Graphics Format 2 - created by Notes version 3 */
	VERSION2(DominoAPI.CDGRAPHIC_VERSION2),
	/** Graphics Format 3 - created by Notes version 4.5 */
	VERSION3(DominoAPI.CDGRAPHIC_VERSION3);
	
	private final byte value;
	
	private CDGRAPHIC_VERSION(byte value) {
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
