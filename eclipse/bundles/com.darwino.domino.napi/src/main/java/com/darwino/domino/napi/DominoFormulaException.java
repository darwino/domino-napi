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

package com.darwino.domino.napi;

import com.ibm.commons.util.StringUtil;


/**
 * <p>A generic exception representing an underlying error code from the Domino API. The
 * exception's message will be the error message returned by the underlying API and the
 * original error code can be accessed via {@link #getStatus()}.
 * 
 * @author priand
 */
public class DominoFormulaException extends DominoException {

	private static final long serialVersionUID = 1L;
	
	private final int formulaError;
	private final String formulaMessage;
	private final int errorLine;
	private final int errorColumn;
	private final int errorOffset;
	private final int errorLength;
	
    public DominoFormulaException(Throwable nextException, int status, String formulaMessage, int formulaError, int errorLine, int errorColumn, int errorOffset, int errorLength, String msg, Object...params) {
        super(nextException, status, msg, params);
        this.formulaError = formulaError;
        this.formulaMessage = formulaMessage;
    	this.errorLine = errorLine;
    	this.errorColumn = errorColumn;
    	this.errorOffset = errorOffset;
    	this.errorLength = errorLength;
    }
    
    @Override
    public String getMessage() {
    	return StringUtil.format("{0}: [Error={1}, Line={2}, Column={3}, Offset={4}, Length={5}] {7}", super.getMessage(), formulaError, errorLine, errorColumn, errorOffset, errorLength, formulaMessage); //$NON-NLS-1$
    }
    @Override
    public String getLocalizedMessage() {
    	return getMessage();
    }
}
