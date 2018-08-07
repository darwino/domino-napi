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

import com.darwino.domino.napi.DominoAPI;

/**
 * @author Jesse Gallagher
 * @since 2.1.0
 */
public enum VCF3 implements INumberEnum<Short> {
	FlatInV5(DominoAPI.VCF3_M_FlatInV5),
	CaseSensitiveSortInV5(DominoAPI.VCF3_M_CaseSensitiveSortInV5),
	AccentSensitiveSortInV5(DominoAPI.VCF3_M_AccentSensitiveSortInV5),
	HideWhenFormula(DominoAPI.VCF3_M_HideWhenFormula),
	TwistieResource(DominoAPI.VCF3_M_TwistieResource),
	Color(DominoAPI.VCF3_M_Color),
	ExtDate(DominoAPI.VCF3_ExtDate),
	NumberFormat(DominoAPI.VCF3_NumberFormat),
	IsColumnEditable(DominoAPI.VCF3_M_IsColumnEditable),
	UserDefinableColor(DominoAPI.VCF3_M_UserDefinableColor),
	HideInR5(DominoAPI.VCF3_M_HideInR5),
	NamesFormat(DominoAPI.VCF3_M_NamesFormat),
	HideColumnTitle(DominoAPI.VCF3_M_HideColumnTitle),
	IsSharedColumn(DominoAPI.VCF3_M_IsSharedColumn),
	UseSharedColumnFormulaOnly(DominoAPI.VCF3_M_UseSharedColumnFormulaOnly);
	
	short value;
	
	private VCF3(short value) {
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
