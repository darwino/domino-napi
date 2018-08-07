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

/**
 * @author Jesse Gallagher
 * @since 0.8.0
 */
public enum UAT implements INumberEnum<Integer> {
	None(0), Server(1), Database(2), View(3), Form(4), Navigator(5), Agent(6),
	Document(7), Filename(8), ActualFilename(9), Field(10), FieldOffset(11),
	FieldSuboffset(12), Page(13), FrameSet(14), ImageResource(15),
	CssResource(16), JavascriptLib(17), FileResource(18), About(19),
	Help(20), Icon(21), SearchForm(22), SearchSiteForm(23), Outline(24);
	
	private int value;
	
	private UAT(int value) {
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
