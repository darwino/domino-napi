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
