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
package com.darwino.domino.napi.proc;

import com.darwino.domino.napi.DominoException;

/**
 * (ods.h)
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public abstract class ActionRoutinePtr extends BaseProc {

	static {
		initNative();
	}
	private static final native void initNative();

	/**
	 * @param recordPtr a pointer to the current composite-data record
	 * @param recordType the SIG_CD_xxx signature WORD of the record
	 * @param recordLength the length (in bytes) of the record
	 * @return a status code that maps to a Domino error code, ERR_CANCEL to cancel, or 0 on success 
	 */
	public abstract short callback(long recordPtr, short recordType, int recordLength) throws DominoException; 

}
