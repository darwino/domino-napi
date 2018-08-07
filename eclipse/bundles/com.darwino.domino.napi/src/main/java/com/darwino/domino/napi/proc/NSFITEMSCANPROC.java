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

/**
 * Proc for enumerating over the items in a note (nsfnote.h)
 * 
 * @author Jesse Gallagher
 *
 */
public abstract class NSFITEMSCANPROC extends BaseProc {
	static {
		initNative();
	}
	private static final native void initNative();
	
	/**
	 * <p>A method to be called for each item in a note.</p>
	 * 
	 * <p>Returning {@link com.darwino.domino.napi.DominoAPI#ERR_CANCEL} from the callback will
	 * cancel out of the iteration without generating an exception.</p>
	 * 
	 * @param itemFlags the current item's flags; see ITEM_xxx
	 * @param itemName the name of the current item
	 * @param valuePtr a pointer to the value of the item in memory
	 * @param valueLength the length of the item's value in memory, as an unsigned integer
	 * @return a status code that maps to a Domino error code, ERR_CANCEL to cancel, or 0 on success. 
	 */
	public abstract short callback(short itemFlags, String itemName, long valuePtr, int valueLength) throws Exception;
}
