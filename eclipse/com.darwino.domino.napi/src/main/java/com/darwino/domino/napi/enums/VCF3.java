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
