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
 * Callback routine for ACLEnumEntries. Original has no canonical name (acl.h)
 *
 */
public abstract class ACLEnumFunc extends BaseProc {

	static {
		initNative();
	}
	private static final native void initNative();

	/**
	 * @param name
	 * @param accessLevel WORD
	 * @param privileges ACL_PRIVILEGES* (pointer to BYTE[10])
	 * @param accessFlags
	 * @throws DominoException
	 */
	public abstract void callback(String name, short accessLevel, long privileges, short accessFlags) throws DominoException; 

}
