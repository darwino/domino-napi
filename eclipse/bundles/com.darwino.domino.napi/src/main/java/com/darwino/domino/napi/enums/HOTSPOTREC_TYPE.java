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
public enum HOTSPOTREC_TYPE implements INumberEnum<Short> {
	POPUP(DominoAPI.HOTSPOTREC_TYPE_POPUP), HOTREGION(DominoAPI.HOTSPOTREC_TYPE_HOTREGION),
	BUTTON(DominoAPI.HOTSPOTREC_TYPE_BUTTON), FILE(DominoAPI.HOTSPOTREC_TYPE_FILE),
	SECTION(DominoAPI.HOTSPOTREC_TYPE_SECTION), ANY(DominoAPI.HOTSPOTREC_TYPE_ANY),
	HOTLINK(DominoAPI.HOTSPOTREC_TYPE_HOTLINK), BUNDLE(DominoAPI.HOTSPOTREC_TYPE_BUNDLE),
	V4_SECTION(DominoAPI.HOTSPOTREC_TYPE_V4_SECTION), SUBFORM(DominoAPI.HOTSPOTREC_TYPE_SUBFORM),
	ACTIVEOBJECT(DominoAPI.HOTSPOTREC_TYPE_ACTIVEOBJECT), OLERICHTEXT(DominoAPI.HOTSPOTREC_TYPE_OLERICHTEXT),
	EMBEDDEDVIEW(DominoAPI.HOTSPOTREC_TYPE_EMBEDDEDVIEW), EMBEDDEDFPANE(DominoAPI.HOTSPOTREC_TYPE_EMBEDDEDFPANE),
	EMBEDDEDNAV(DominoAPI.HOTSPOTREC_TYPE_EMBEDDEDNAV), MOUSEOVER(DominoAPI.HOTSPOTREC_TYPE_MOUSEOVER),
	FILEUPLOAD(DominoAPI.HOTSPOTREC_TYPE_FILEUPLOAD), EMBEDDEDOUTLINE(DominoAPI.HOTSPOTREC_TYPE_EMBEDDEDOUTLINE),
	EMBEDDEDCTL(DominoAPI.HOTSPOTREC_TYPE_EMBEDDEDCTL), EMBEDDEDCALENDARCTL(DominoAPI.HOTSPOTREC_TYPE_EMBEDDEDCALENDARCTL),
	SCHEDCTL(DominoAPI.HOTSPOTREC_TYPE_EMBEDDEDSCHEDCTL), RCLINK(DominoAPI.HOTSPOTREC_TYPE_RCLINK),
	EMBEDDEDITCTL(DominoAPI.HOTSPOTREC_TYPE_EMBEDDEDEDITCTL), CONTACTLISTCTL(DominoAPI.HOTSPOTREC_TYPE_CONTACTLISTCTL);
	
	private final short value;
	
	private HOTSPOTREC_TYPE(short value) {
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
