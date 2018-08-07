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

import com.ibm.commons.util.AbstractException;


/**
 * <p>A generic exception representing an underlying error code from the Domino API. The
 * exception's message will be the error message returned by the underlying API and the
 * original error code can be accessed via {@link #getStatus()}.
 * 
 * @author priand
 */
public class DominoException extends AbstractException {

	private static final long serialVersionUID = 1L;
	
	public static class DominoExceptionWithoutStackTrace extends DominoException {
		private static final long serialVersionUID = 1L;

		public DominoExceptionWithoutStackTrace(Throwable nextException, int status, String msg, Object... params) {
			super(nextException, status, msg, params);
		}
		
		@Override
		public synchronized Throwable fillInStackTrace() {
			return this;
		}
		
		@Override
		public String toString() {
			String str1 = DominoException.class.getName();
			String str2 = getLocalizedMessage();
			return str2 != null ? str1 + ": " + str2 : str1; //$NON-NLS-1$
		}
	}
	
	private int status = -1;
	
    public DominoException(Throwable nextException) {
        super(nextException);
    }

    public DominoException(Throwable nextException, String msg, Object...params) {
        super(nextException, msg, params);
    }
    
    public DominoException(Throwable nextException, int status, String msg, Object...params) {
        super(nextException, msg, params);
    	this.status = status;
    }
    
    /**
     * @return the underlying Domino API status code. See ERR_ in the API documentation for specific
     * 	codes.
     */
    public int getStatus() {
		return status;
	}
}
