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
 * Proc for enumerating the IDs in an IDTable
 * 
 * @author priand
 */
public abstract class IDENUMERATEPROC extends BaseProc {
	
	static {
		initNative();
	}
	private static final native void initNative();

	/**
	 * <p>A method to be called for each note ID in the IDTable.</p>
	 * 
	 * <p>Returning {@link com.darwino.domino.napi.DominoAPI#ERR_CANCEL} from the callback will
	 * cancel out of the iteration without generating an exception.</p>
	 * 
	 * @param id the current ID to process
	 * @return a status code that maps to a Domino error code, ERR_CANCEL to cancel, or 0 on success. 
	 */
	public abstract short callback(int id) throws DominoException; 
}
