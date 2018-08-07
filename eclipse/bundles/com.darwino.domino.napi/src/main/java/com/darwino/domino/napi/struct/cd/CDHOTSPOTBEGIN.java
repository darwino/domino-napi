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
package com.darwino.domino.napi.struct.cd;

import com.ibm.commons.util.StringUtil;

import java.util.Set;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.HOTSPOTREC_RUNFLAG;
import com.darwino.domino.napi.enums.HOTSPOTREC_TYPE;
import com.darwino.domino.napi.enums.ODSType;

/**
 * (editods.h)
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public class CDHOTSPOTBEGIN extends BaseCDStruct<WSIG> {
	static {
		int[] sizes = new int[5];
		initNative(sizes);
		sizeOf = sizes[0];
		_Header = sizes[1];
		_Type = sizes[2];
		_Flags = sizes[3];
		_DataLength = sizes[4];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Header;
	public static final int _Type;
	public static final int _Flags;
	public static final int _DataLength;

	// Structure-related fields stored internally due to canonical format
	private String fileInternalName;
	private String fileOriginalName;
	private String subformName;
	
	public CDHOTSPOTBEGIN() {
		super(C.malloc(sizeOf),true);
	}
	public CDHOTSPOTBEGIN(long data) {
		super(data, false);
	}
	public CDHOTSPOTBEGIN(long data, boolean owned) {
		super(data, owned);
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.ODSStruct#getODSType()
	 */
	@Override
	public ODSType getODSType() {
		return ODSType.CDHOTSPOTBEGIN;
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.cd.CDStruct#loadVariableData(com.darwino.domino.napi.DominoAPI, long)
	 */
	@Override
	protected void _readODSVariableData(DominoAPI api, long dataPtr) {
		HOTSPOTREC_TYPE type = this.getType();
		if(type != null) {
			switch(type) {
			case FILE: {
				String internalName = C.getLMBCSString(dataPtr, 0);
				
				int offset = 0;
				int max = this.getDataLength();
				while(C.getByte(dataPtr, offset) != 0 && offset < max) {
					offset++;
				}
				String originalName = C.getLMBCSString(dataPtr, offset+1);
				this.setFileInternalName(internalName);
				this.setFileOriginalName(originalName);
				
				break;
			}
			case SUBFORM: {
				String subformName = C.getLMBCSString(dataPtr, 0, this.getDataLength());
				this.subformName = subformName;
				break;
			}
			default:
				// TODO implement others
				//throw new NotImplementedException("Type {0} not yet implemented", result.getType()); //$NON-NLS-1$
				break;
			}
		}
	}
	
	/* ******************************************************************************
	 * Struct field getters/setters
	 ********************************************************************************/

	@Override
	public WSIG getHeader() {
		_checkRefValidity();
		return new WSIG(C.ptrAdd(data, _Header));
	}
	@Override
	public void setHeader(WSIG signature) {
		_checkRefValidity();
		C.memcpy(data, _Header, signature.getDataPtr(), 0, WSIG.sizeOf);
	}
	
	public short getTypeRaw() { return _getWORD(_Type); }
	public void setTypeRaw(short type) { _setWORD(_Type, type); }
	
	public int getFlagsRaw() { return _getDWORD(_Flags); }
	public void setFlagsRaw(int flags) { _setDWORD(_Flags, flags); }
	
	public short getDataLength() { return _getWORD(_DataLength); }
	public void setDataLength(short dataLength) { _setWORD(_DataLength, dataLength); }
	
	/* ******************************************************************************
	 * Variable data
	 ********************************************************************************/
	
	/**
	 * Applicable only when type is HOTSPOTREC_TYPE_FILE
	 * 
	 * @return the internal Domino name of the file
	 */
	public String getFileInternalName() {
		return fileInternalName;
	}
	
	/**
	 * Sets the internal file name stored in this Java object.
	 * 
	 * <p>This is <strong>not</strong> stored in C-side memory, as <code>CDHOTSPOTBEGIN</code>
	 * structs must be read with <code>ODSReadMemory</code> and are no longer adjacent to
	 * their name strings.</p>
	 * 
	 * @param fileInternalName the new file name
	 */
	public void setFileInternalName(String fileInternalName) {
		this.fileInternalName = fileInternalName;
	}
	
	/**
	 * Applicable only when type is HOTSPOTREC_TYPE_FILE
	 * 
	 * @return the internal Domino name of the file
	 */
	public String getFileOriginalName() {
		return fileOriginalName;
	}
	
	/**
	 * Sets the original file name stored in this Java object.
	 * 
	 * <p>This is <strong>not</strong> stored in C-side memory, as <code>CDHOTSPOTBEGIN</code>
	 * structs must be read with <code>ODSReadMemory</code> and are no longer adjacent to
	 * their name strings.</p>
	 * 
	 * @param fileInternalName the new file name
	 */
	public void setFileOriginalName(String fileOriginalName) {
		this.fileOriginalName = fileOriginalName;
	}
	
	/**
	 * Applicable only when type is HOTSPOTREC_TYPE_SUBFORM
	 * 
	 * @return the referenced subform name
	 */
	public String getSubformName() {
		return subformName;
	}
	
	/* ******************************************************************************
	 * Encapsulated getters/setters
	 ********************************************************************************/
	
	public HOTSPOTREC_TYPE getType() {
		return DominoEnumUtil.valueOf(HOTSPOTREC_TYPE.class, getTypeRaw());
	}
	public void setType(HOTSPOTREC_TYPE type) {
		setTypeRaw(type.getValue());
	}
	
	public Set<HOTSPOTREC_RUNFLAG> getFlags() {
		return DominoEnumUtil.valuesOf(HOTSPOTREC_RUNFLAG.class, getFlagsRaw());
	}
	
	/* ******************************************************************************
	 * Misc.
	 ********************************************************************************/
	
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	if(isRefValid()) {
	    	return StringUtil.format("[{0}: Header={1}, Type={2}, Flags={3}, DataLength={4}]", //$NON-NLS-1$
	    			getClass().getSimpleName(),
	    			getHeader(),
	    			getType(),
	    			getFlagsRaw(),
	    			getDataLength()
	    	);
    	} else {
    		return super.toString();
    	}
    }

}
