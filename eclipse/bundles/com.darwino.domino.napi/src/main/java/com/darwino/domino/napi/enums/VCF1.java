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
