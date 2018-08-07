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
 * @since 1.5.0
 *
 */
public enum VCF1 implements INumberEnum<Short> {
	Sort(DominoAPI.VCF1_M_Sort), SortCategorize(DominoAPI.VCF1_M_SortCategorize),
	SortDescending(DominoAPI.VCF1_M_SortDescending), Hidden(DominoAPI.VCF1_M_Hidden),
	Response(DominoAPI.VCF1_M_Response), HideDetail(DominoAPI.VCF1_M_HideDetail),
	Icon(DominoAPI.VCF1_M_Icon), NoResize(DominoAPI.VCF1_M_NoResize),
	ResortAscending(DominoAPI.VCF1_M_ResortAscending), ResortDescending(DominoAPI.VCF1_M_ResortDescending),
	Twistie(DominoAPI.VCF1_M_Twistie), ResortToView(DominoAPI.VCF1_M_ResortToView),
	SecondResort(DominoAPI.VCF1_M_SecondResort), SecondResortDescending(DominoAPI.VCF1_M_SecondResortDescending),
	/** @deprecated replaced by {@link VCF3#CaseSensitiveSortInV5} */
	CaseInsensitiveSort(DominoAPI.VCF1_M_CaseInsensitiveSort),
	/** @deprecated replaced by {@link VCF3#AccentSensitiveSortInV5} */
	AccentInsensitiveSort(DominoAPI.VCF1_M_AccentInsensitiveSort);
	
	short value;
	
	private VCF1(short value) {
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
