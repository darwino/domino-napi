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

package com.darwino.domino.napi.wrap;

import com.ibm.commons.util.StringUtil;

public class FormulaException extends Exception {
	private static final long serialVersionUID = 1L;

	private final int code;
	private final int line;
	private final int column;
	private final int offset;
	private final int length;

	public FormulaException(int code, int line, int column, int offset, int length) {
		this.code = code;
		this.line = line;
		this.column = column;
		this.offset = offset;
		this.length = length;
	}
	
	@Override
	public String getMessage() {
		return StringUtil.format("Formula compilation exception: code={0}, line={1}, column={2}, offset={3}, length={4}", code, line, column, offset, length);
	}
	
	public int getCode() { return code; }
	public int getLine() { return line; }
	public int getColumn() { return column; }
	public int getOffset() { return offset; }
	public int getLength() { return length; }
}
