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
 * _xxx (ODS Type) (odstypes.h)
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 *
 */
public enum ODSType implements INumberEnum<Short> {
	LIST(DominoAPI._LIST), OBJECT_DESCRIPTOR(DominoAPI._OBJECT_DESCRIPTOR), VIEW_TABLE_FORMAT(DominoAPI._VIEW_TABLE_FORMAT),
	VIEW_COLUMN_FORMAT(DominoAPI._VIEW_COLUMN_FORMAT), VIEW_TABLE_FORMAT2(DominoAPI._VIEW_TABLE_FORMAT2),
	FILEOBJECT(DominoAPI._FILEOBJECT), COLLATION(DominoAPI._COLLATION), COLLATE_DESCRIPTOR(DominoAPI._COLLATE_DESCRIPTOR),
	CDTEXT(DominoAPI._CDTEXT), CDFIELD(DominoAPI._CDFIELD), CDGRAPHIC(DominoAPI._CDGRAPHIC), CDHOTSPOTBEGIN(DominoAPI._CDHOTSPOTBEGIN),
	VIEW_COLUMN_FORMAT2(DominoAPI._VIEW_COLUMN_FORMAT2), CDBEGINRECORD(DominoAPI._CDBEGINRECORD), CDENDRECORD(DominoAPI._CDENDRECORD),
	CDEXT2FIELD(DominoAPI._CDEXT2FIELD), MIME_PART(DominoAPI._MIME_PART), VIEW_TABLE_FORMAT3(DominoAPI._VIEW_TABLE_FORMAT3),
	CDFIELDHINT(DominoAPI._CDFIELDHINT), CDEXTFIELD(DominoAPI._CDEXTFIELD), CDDATAFLAGS(DominoAPI._CDDATAFLAGS),
	CDKEYWORD(DominoAPI._CDKEYWORD), EMBEDDEDCTL(DominoAPI._CDEMBEDDEDCTL), CDFILEHEADER(DominoAPI._CDFILEHEADER),
	CDFILESEGMENT(DominoAPI._CDFILESEGMENT);
	
	private final short value;
	
	private ODSType(short value) {
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.enums.INumberEnum#getValue()
	 */
	@Override
	public Short getValue() {
		return value;
	}
	
	@Override
	public long getLongValue() {
		return value;
	}
}
