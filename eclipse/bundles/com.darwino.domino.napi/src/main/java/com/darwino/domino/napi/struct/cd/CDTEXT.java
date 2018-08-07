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

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.ODSType;
import com.ibm.commons.util.StringUtil;

/**
 * (editods.h)
 * 
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public class CDTEXT extends BaseCDStruct<WSIG> {
	
	static {
		int[] sizes = new int[3];
		initNative(sizes);
		sizeOf = sizes[0];
		_Header = sizes[1];
		_FontID = sizes[2];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Header;
	public static final int _FontID;
	

	// Structure-related fields stored internally due to canonical format
	private String text;
	
	public CDTEXT() {
		super(C.malloc(sizeOf),true);
	}
	public CDTEXT(long data) {
		super(data, false);
	}
	public CDTEXT(long data, boolean owned) {
		super(data, owned);
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.ODSStruct#getODSType()
	 */
	@Override
	public ODSType getODSType() {
		return ODSType.CDTEXT;
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.cd.CDStruct#loadVariableData(com.darwino.domino.napi.DominoAPI, long)
	 */
	@Override
	protected void _readODSVariableData(DominoAPI api, long dataPtr) {
		int textLength = getHeader().getLength() - api.ODSLength(getODSType().getValue());
		if(textLength == 0) {
			this.text = ""; //$NON-NLS-1$
		} else {
			String text = C.getLMBCSString(dataPtr, 0, textLength);
			this.text = text;
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
	
	/* ******************************************************************************
	 * Variable data
	 ********************************************************************************/
	
	public String getText() {
		return text;
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
	    	return StringUtil.format("[{0}: Header={1}, Text={2}]", //$NON-NLS-1$
	    			getClass().getSimpleName(),
	    			getHeader(),
	    			getText()
	    	);
    	} else {
    		return super.toString();
    	}
    }

}
