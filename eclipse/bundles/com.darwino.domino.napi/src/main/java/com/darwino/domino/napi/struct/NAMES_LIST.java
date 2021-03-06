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
package com.darwino.domino.napi.struct;

import com.ibm.commons.util.StringUtil;

import java.util.Collection;
import java.util.EnumSet;

import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.NamesListAuth;

/**
 * (acl.h)
 * 
 * @author Jesse Gallagher
 *
 */
public class NAMES_LIST extends BaseStruct {
	static {
		int[] sizes = new int[4];
		initNative(sizes);
		sizeOf = sizes[0];
		_NumNames = sizes[1];
		_License = sizes[2];
		_Authenticated = sizes[3];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _NumNames;
	public static final int _License;
	public static final int _Authenticated;
	
	public NAMES_LIST() {
		super(C.malloc(sizeOf), true);
	}
	public NAMES_LIST(long data) {
		super(data, false);
	}

	public NAMES_LIST(long data, boolean owned) {
		super(data, owned);
	}
	
	/* ******************************************************************************
	 * Struct field getters/setters
	 ********************************************************************************/
	
	public short getNumNames() {
		return _getWORD(_NumNames);
	}
	public void setNumNames(short numNames) {
		_setWORD(_NumNames, numNames);
	}
	public int getAuthenticatedRaw() {
		// This potentiallydiffers based on platform settings - find if it's a DWORD or a WORD
		if(sizeOf - _Authenticated == 2) {
			return _getWORD(_Authenticated);
		} else {
			return _getDWORD(_Authenticated);
		}
	}
	public void setAuthenticatedRaw(int authenticated) {
		if(sizeOf - _Authenticated == 2) {
			_setWORD(_Authenticated, (short)authenticated);
		} else {
			_setDWORD(_Authenticated, authenticated);
		}
	}

	
	/* ******************************************************************************
	 * Variable data
	 ********************************************************************************/
	
	public String[] getNames() {
		int count = getNumNames() & 0xFFFFFFFF;
		String[] result = new String[count];
		
		long dataPtr = C.ptrAdd(getDataPtr(), sizeOf);
		for(int i = 0; i < count; i++) {
			result[i] = C.getLMBCSString(dataPtr, 0);
			
			// Now find the next null
			int breaker = 0;
			while(C.getByte(dataPtr, 0) != '\0') {
				dataPtr = C.ptrAdd(dataPtr, 1);
				if(breaker++ > 65000) {
					throw new RuntimeException("Probable infinite loop detected");
				}
			}
			// Move one past it
			dataPtr = C.ptrAdd(dataPtr, 1);
		}
		
		return result;
	}
	
	/* ******************************************************************************
	 * Encapsulated getters/setters
	 ********************************************************************************/
	public EnumSet<NamesListAuth> getAuthenticated() {
		return DominoEnumUtil.valuesOf(NamesListAuth.class, (short)getAuthenticatedRaw());
	}
	public void setAuthenticated(Collection<NamesListAuth> authenticated) {
		setAuthenticatedRaw(DominoEnumUtil.toBitField(NamesListAuth.class, authenticated));
	}

	/* ******************************************************************************
	 * Misc.
	 ********************************************************************************/
	
	@Override
	public String toString() {
		if(isRefValid()) {
			return StringUtil.format("[{0}: NumNames={1}, Authenticated={2}]", //$NON-NLS-1$
					getClass().getSimpleName(),
					getNumNames(),
					getAuthenticated()
			);
		} else {
			return super.toString();
		}
	}
}
