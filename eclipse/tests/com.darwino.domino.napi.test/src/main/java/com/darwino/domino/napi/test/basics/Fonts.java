/**
 * Copyright © 2014-2018 Darwino, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
