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

import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.CmdArgID;
import com.darwino.domino.napi.enums.CmdArgValueType;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.ibm.commons.util.StringUtil;

/**
 * 
 * @author Jesse Gallagher
 *
 */
public class HTMLAPI_URLArg extends BaseStruct {
	static {
		int[] sizes = new int[4];
		initNative(sizes);
		sizeOf = sizes[0];
		_Id = sizes[1];
		_Type = sizes[2];
		_Value = sizes[3];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Id;
	public static final int _Type;
	public static final int _Value;

	public HTMLAPI_URLArg() {
		super(C.malloc(sizeOf),true);
	}
	public HTMLAPI_URLArg(long data) {
		super(data, false);
	}
	public HTMLAPI_URLArg(long data, boolean owned) {
		super(data, owned);
	}

	public int getIdRaw() {
		_checkRefValidity();
		return C.getCmdArgID(getDataPtr(), _Id);
	}
	public CmdArgID getId() {
		return DominoEnumUtil.valueOf(CmdArgID.class, getIdRaw());
	}
	
	public int getTypeRaw() {
		_checkRefValidity();
		return C.getCmdArgValueType(getDataPtr(), _Type);
	}
	
	public CmdArgValueType getType() {
		return DominoEnumUtil.valueOf(CmdArgValueType.class, getTypeRaw());
	}
	
	public int getValueInt() { return _getInt(_Value); }
	public long getValueNOTEID() { return _getLong(_Value); }
	public UNIVERSALNOTEID getValueUNID() {
		_checkRefValidity();
		
		long valuePtr = C.ptrAdd(data, _Value);
		return new UNIVERSALNOTEID(valuePtr);
	}
	public String getValueString() {
		_checkRefValidity();
		
		if(getType() != CmdArgValueType.String) {
			return ""; //$NON-NLS-1$
		}
		
		long stringPtr = C.getPointer(data, _Value);
		int strlen = C.strlen(stringPtr, 0);
		byte[] chars = new byte[strlen];
		C.readByteArray(chars, 0, stringPtr, 0, strlen);
		return new String(chars);
	}
	public long getValueListPtr() {
		_checkRefValidity();
		return C.ptrAdd(data, _Value);
	}
	
	/* ******************************************************************************
	 * Misc.
	 ********************************************************************************/
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.BaseStruct#toString()
	 */
	@Override
	public String toString() {
		try {
			StringBuilder result = new StringBuilder();
			
			result.append(StringUtil.format("[{0}: ID={1}, Type={2}", //$NON-NLS-1$
					getClass().getSimpleName(),
					getId(),
					getType()
					));
			switch(getType()) {
			case Int:
				result.append(", Value=" + getValueInt()); //$NON-NLS-1$
				break;
			case NoteId:
				result.append(", Value=" + getValueNOTEID()); //$NON-NLS-1$
				break;
			case String:
				result.append(", Value=" + getValueString()); //$NON-NLS-1$
				break;
			case StringList:
				// NYI
				break;
			case UNID:
				result.append(", Value=" + getValueUNID()); //$NON-NLS-1$
				break;
			default:
				break;
			}
			result.append("]"); //$NON-NLS-1$
			
			return result.toString();
		} catch(Throwable t) {
			return StringUtil.format("{0}: (exception {1})]", getClass().getSimpleName(), t.toString()); //$NON-NLS-1$
		}
	}
}
