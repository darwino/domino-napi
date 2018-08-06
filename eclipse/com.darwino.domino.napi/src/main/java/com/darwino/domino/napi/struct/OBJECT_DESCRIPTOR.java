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

import java.util.Collection;
import java.util.Set;

import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.ObjectFlag;
import com.darwino.domino.napi.enums.ObjectType;

/**
 * (nsfdata.h)
 * 
 * @author Jesse Gallagher
 *
 */
public class OBJECT_DESCRIPTOR extends BaseStruct {
	static {
		int[] sizes = new int[3];
		initNative(sizes);
		sizeOf = sizes[0];
		_ObjectType = sizes[1];
		_RRV = sizes[2];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _ObjectType;
	public static final int _RRV;
	
	public OBJECT_DESCRIPTOR() {
		super(C.malloc(sizeOf), true);
	}
	public OBJECT_DESCRIPTOR(long data) {
		super(data, false);
	}

	public OBJECT_DESCRIPTOR(long data, boolean owned) {
		super(data, owned);
	}

	/* ******************************************************************************
	 * Struct field getters/setters
	 ********************************************************************************/
	
	public short getObjectTypeRaw() { return _getWORD(_ObjectType); }
	public void setObjectTypeRaw(short objectType) { _setWORD(_ObjectType, objectType); }
	
	public int getRRV() { return _getDWORD(_RRV); }
	public void setRRV(int rrv) { _setDWORD(_RRV, rrv); }
	
	/* ******************************************************************************
	 * Encapsulated getters/setters
	 ********************************************************************************/

	public ObjectType getObjectType() {
		return DominoEnumUtil.valueOf(ObjectType.class, (short)(getObjectTypeRaw() & 0x00FF));
	}
	public void setObjectType(ObjectType objectType) {
		// Clear out any existing type value while retaining flags
		short unmasked = (short)(getObjectTypeRaw() & ~0x00FF);
		short newValue = (short)(unmasked | objectType.getValue());
		setObjectTypeRaw(newValue);
	}
	
	public Set<ObjectFlag> getFlags() {
		return DominoEnumUtil.valuesOf(ObjectFlag.class, getObjectTypeRaw());
	}
	public void setFlags(Collection<ObjectFlag> flags) {
		// Clear out any existing flag value while retaining type
		short unmasked = (short)(getObjectTypeRaw() & 0x00FF);
		short newValue = (short)(unmasked | DominoEnumUtil.toBitField(ObjectFlag.class, flags));
		setObjectTypeRaw(newValue);
	}
	
	
	/* ******************************************************************************
	 * Misc.
	 ********************************************************************************/
	
	@Override
	public String toString() {
		if(isRefValid()) {
			return StringUtil.format("[{0}: ObjectType={1}, Flags={2}, RRV={3}]", //$NON-NLS-1$
					getClass().getSimpleName(),
					getObjectType(),
					getFlags(),
					getRRV()
			);
		} else {
			return super.toString();
		}
	}
}
