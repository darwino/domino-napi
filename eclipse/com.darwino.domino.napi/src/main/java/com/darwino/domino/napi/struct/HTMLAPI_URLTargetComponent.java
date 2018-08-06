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

package com.darwino.domino.napi.struct;

import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.UAT;
import com.darwino.domino.napi.enums.URT;
import com.ibm.commons.util.StringUtil;

/**
 * 
 * @author Jesse Gallagher
 *
 */
public class HTMLAPI_URLTargetComponent extends BaseStruct {
	
	static {
		int[] sizes = new int[4];
		initNative(sizes);
		sizeOf = sizes[0];
		_AddressableType = sizes[1];
		_ReferenceType = sizes[2];
		_Value = sizes[3];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _AddressableType;
	public static final int _ReferenceType;
	public static final int _Value;

	public HTMLAPI_URLTargetComponent() {
		super(C.malloc(sizeOf),true);
	}
	public HTMLAPI_URLTargetComponent(long data) {
		super(data, false);
	}
	public HTMLAPI_URLTargetComponent(long data, boolean owned) {
		super(data, owned);
	}

	public int getAddressableTypeRaw() { return _getInt(_AddressableType); }
	public UAT getAddressableType() {
		int type = getAddressableTypeRaw();
		try {
			return DominoEnumUtil.valueOf(UAT.class, type);
		} catch(IllegalArgumentException e) {
			return null;
		}
	}
	
	public int getReferenceTypeRaw() { return _getInt(_ReferenceType); }
	public URT getReferenceType() {
		int type = getReferenceTypeRaw();
		try {
			return DominoEnumUtil.valueOf(URT.class, type);
		} catch(IllegalArgumentException e) {
			return null;
		}
	}
	
	public int getValueUSV() { return _getInt(_Value); }
	public long getValueNOTEID() { return _getLong(_Value); }
	public UNIVERSALNOTEID getValueUNID() {
		long valuePtr = getField(_Value);
		return new UNIVERSALNOTEID(valuePtr);
	}
	public String getValueName() {
		_checkRefValidity();
		if(getReferenceType() != URT.Name) {
			return ""; //$NON-NLS-1$
		}
		
		long stringPtr = C.getPointer(data, _Value);
		int strlen = C.strlen(stringPtr, 0);
		byte[] chars = new byte[strlen];
		C.readByteArray(chars, 0, stringPtr, 0, strlen);
		return new String(chars);
	}
	public TIMEDATE getValueDBID() {
		long valuePtr = getField(_Value);
		return new TIMEDATE(valuePtr);
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
			
			result.append(StringUtil.format("[{0}: AddressableType={1}, ReferenceType={2}", //$NON-NLS-1$
					getClass().getSimpleName(),
					getAddressableType(),
					getReferenceType()
					));
			switch(getReferenceType()) {
			case Name:
				result.append(", Value=" + getValueName()); //$NON-NLS-1$
				break;
			case None:
				break;
			case NoteId:
				result.append(", Value=" + getValueNOTEID()); //$NON-NLS-1$
				break;
			case RepId:
				result.append(", Value=" + getValueDBID()); //$NON-NLS-1$
				break;
			case Special:
				result.append(", Value=" + getValueUSV()); //$NON-NLS-1$
				break;
			case Unid:
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
