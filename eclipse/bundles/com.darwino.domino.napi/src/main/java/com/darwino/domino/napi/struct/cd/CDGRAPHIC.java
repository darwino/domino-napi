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

package com.darwino.domino.napi.struct.cd;

import com.ibm.commons.util.StringUtil;

import java.util.Collection;
import java.util.Set;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.CDGRAPHIC_FLAG;
import com.darwino.domino.napi.enums.CDGRAPHIC_VERSION;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.ODSType;

/**
 * (editods.h)
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public class CDGRAPHIC extends BaseCDStruct<LSIG> {
	

	static {
		int[] sizes = new int[9];
		initNative(sizes);
		sizeOf = sizes[0];
		_Header = sizes[1];
		_DestSize = sizes[2];
		_CropSize = sizes[3];
		_CropOffset = sizes[4];
		_fResize = sizes[5];
		_Version = sizes[6];
		_bFlags = sizes[7];
		_wReserved = sizes[8];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Header;
	public static final int _DestSize;
	public static final int _CropSize;
	public static final int _CropOffset;
	public static final int _fResize;
	public static final int _Version;
	public static final int _bFlags;
	public static final int _wReserved;

	// Structure-related fields stored internally due to canonical format
	private String fileInternalName;
	private String fileOriginalName;
	
	public CDGRAPHIC() {
		super(C.malloc(sizeOf),true);
	}
	public CDGRAPHIC(long data) {
		super(data, false);
	}
	public CDGRAPHIC(long data, boolean owned) {
		super(data, owned);
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.ODSStruct#getODSType()
	 */
	@Override
	public ODSType getODSType() {
		return ODSType.CDGRAPHIC;
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.cd.CDStruct#loadVariableData(com.darwino.domino.napi.DominoAPI, long)
	 */
	@Override
	public void _readODSVariableData(DominoAPI api, long dataPtr) {
		// TODO Auto-generated method stub
		
	}
	
	/* ******************************************************************************
	 * Struct field getters/setters
	 ********************************************************************************/

	@Override
	public LSIG getHeader() {
		_checkRefValidity();
		return new LSIG(C.ptrAdd(data, _Header));
	}
	@Override
	public void setHeader(LSIG signature) {
		_checkRefValidity();
		C.memcpy(data, _Header, signature.getDataPtr(), 0, LSIG.sizeOf);
	}
	
	public short getFResize() {
		return _getWORD(_fResize);
	}
	public void setFResize(short fResize) {
		_setWORD(_fResize, fResize);
	}
	
	public byte getVersionRaw() {
		return _getBYTE(_Version);
	}
	public void setVersionRaw(byte version) {
		_setBYTE(_Version, version);
	}
	
	public byte getFlagsRaw() {
		return _getBYTE(_bFlags);
	}
	public void setFlagsRaw(byte flags) {
		_setBYTE(_bFlags, flags);
	}
	
	/* ******************************************************************************
	 * Encapsulated getters/setters
	 ********************************************************************************/
	
	public boolean isResize() {
		return getFResize() > 0;
	}
	public void setResize(boolean resize) {
		setFResize((short)(resize ? 1 : 0));
	}
	
	public CDGRAPHIC_VERSION getVersion() {
		return DominoEnumUtil.valueOf(CDGRAPHIC_VERSION.class, getVersionRaw());
	}
	public void setVersion(CDGRAPHIC_VERSION version) {
		setVersionRaw(version.getValue());
	}
	
	public Set<CDGRAPHIC_FLAG> getFlags() {
		return DominoEnumUtil.valuesOf(CDGRAPHIC_FLAG.class, getFlagsRaw());
	}
	public void setFlags(Collection<CDGRAPHIC_FLAG> flags) {
		setFlagsRaw(DominoEnumUtil.toBitField(CDGRAPHIC_FLAG.class, flags));
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
	    	return StringUtil.format("[{0}: Header={1}, Resize={2}, Version={3}, Flags={4} (raw={5})]", //$NON-NLS-1$
	    			getClass().getSimpleName(),
	    			getHeader(),
	    			isResize(),
	    			getVersion(),
	    			getFlags(),
	    			getFlagsRaw()
	    			
	    	);
    	} else {
    		return super.toString();
    	}
    }

}
