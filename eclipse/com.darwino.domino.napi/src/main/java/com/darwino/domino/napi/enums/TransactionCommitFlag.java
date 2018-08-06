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

public enum TransactionCommitFlag implements INumberEnum<Integer> {
	SKIP_AUTO_ABORT(DominoAPI.TRANCOMMIT_SKIP_AUTO_ABORT);
	
	private final int value;
	
	private TransactionCommitFlag(int value) {
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
