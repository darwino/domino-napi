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

package com.darwino.domino.napi.test.basics;

import static org.junit.Assert.*;
import static com.darwino.domino.napi.DominoAPI.*;

import org.junit.Test;

/**
 * The Font-related methods in DominoAPI are Java implementations of the original C macros,
 * so it's worth having a couple tests to verify that their bitwise logic is intact.
 * 
 * @author Jesse Gallagher
 *
 */
public class Fonts {

	@Test
	public void testFonts() {
		int plain = DEFAULT_FONT_ID;
		
		assertTrue(FontIsPlain(plain));
		assertEquals(FontGetStyle(plain), 0);
		assertEquals(FontGetFaceID(plain), FONT_FACE_SWISS);
		assertEquals(plain, FontClearUnderline(FontSetUnderline(plain)));
		
		int bold = FontSetBold(plain);
		assertTrue(FontIsBold(bold));
		assertEquals(bold, DEFAULT_BOLD_FONT_ID);
		
		int larger = FontSetSize(plain, (byte)20);
		assertEquals(FontGetSize(larger), 20);
		
		assertEquals(FontGetColor(plain), 0);
		assertEquals(FontGetColor(FontSetColor(plain, (byte)1)), 1);
		
		assertEquals(FontGetStyle(FontSetStyle(plain, (byte)1)), 1);
		assertEquals(FontGetFaceID(FontSetFaceID(plain, (byte)FONT_FACE_TYPEWRITER)), FONT_FACE_TYPEWRITER);
		
		assertTrue(FontIsUnderline(UNDERLINE_FONT_ID));
		assertTrue(FontIsItalic(FontSetItalic(plain)));
		
		assertFalse(FontIsItalic(FontClearItalic(FontSetItalic(plain))));
		assertFalse(FontIsBold(FontClearBold(bold)));
		assertFalse(FontIsUnderline(FontClearUnderline(UNDERLINE_FONT_ID)));
		
		assertTrue(FontIsStrikeOut(FontSetStrikeOut(plain)));
		assertFalse(FontIsStrikeOut(FontClearStrikeOut(FontSetStrikeOut(plain))));
		
		assertTrue(FontIsSuperScript(FontSetSuperScript(plain)));
		assertFalse(FontIsSuperScript(FontClearSuperScript(FontSetSuperScript(plain))));
		
		assertTrue(FontIsSubScript(FontSetSubScript(plain)));
		assertFalse(FontIsSubScript(FontClearSubScript(FontSetSubScript(plain))));
		
		assertTrue(FontIsEffect(FontSetEmboss(plain)));
		assertFalse(FontIsEmboss(plain));
		assertTrue(FontIsExtrude(FontSetExtrude(FontSetEmboss(plain))));
		assertTrue(FontIsShadow(FontSetShadow(plain)));
		
		assertEquals(FontGetShadowOffset(plain), 1);
	}
}
