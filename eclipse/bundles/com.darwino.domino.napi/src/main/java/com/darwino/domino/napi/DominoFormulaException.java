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
