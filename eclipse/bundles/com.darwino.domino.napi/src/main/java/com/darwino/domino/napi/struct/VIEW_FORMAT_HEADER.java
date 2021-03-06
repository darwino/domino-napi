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
package com.darwino.domino.napi.struct;

import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.VIEW_STYLE;

/**
 * 
 * @author Jesse Gallagher
 *
 */
public class VIEW_FORMAT_HEADER extends BaseStruct {
	static {
		int[] sizes = new int[3];
		initNative(sizes);
		sizeOf = sizes[0];
		_Version = sizes[1];
		_ViewStyle = sizes[2];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Version;
	public static final int _ViewStyle;
	
	public VIEW_FORMAT_HEADER() {
		super(C.malloc(sizeOf), true);
	}
	public VIEW_FORMAT_HEADER(long data) {
		super(data, false);
	}

	public VIEW_FORMAT_HEADER(long data, boolean owned) {
		super(data, owned);
	}
	
	// ******************************************************************************
	// * Raw getters/setters
	// ******************************************************************************
	
	public byte getVersion() { return _getBYTE(_Version); }
	public void setVersion(byte version) { _setBYTE(_Version, version); }
	
	public byte getViewStyleRaw() { return _getBYTE(_ViewStyle); }
	public void setViewStyleRaw(byte viewStyle) { _setBYTE(_ViewStyle, viewStyle); }
	
	// ******************************************************************************
	// * Encapsulated getters/setters
	// ******************************************************************************
	
	public VIEW_STYLE getViewStyle() {
		return DominoEnumUtil.valueOf(VIEW_STYLE.class, getViewStyleRaw());
	}
	public void setViewStyle(VIEW_STYLE style) {
		setViewStyleRaw(style.getValue());
	}
}
