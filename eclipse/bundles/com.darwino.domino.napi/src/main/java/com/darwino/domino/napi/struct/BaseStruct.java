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

import com.ibm.commons.Platform;
import com.ibm.commons.util.StringUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.runtime.IdentifiableFreeable;
import com.darwino.domino.napi.runtime.StructReferenceCache;




/**
 * Base Struct.
 * 
 * @author priand
 */
public abstract class BaseStruct implements IdentifiableFreeable {

	static {
		initNative();
	}
	private static final native void initNative();
	

    protected long data;
    protected boolean owned;
    private boolean trackedInSet;
    
    // Properties used for memory tracking/debugging
	private static boolean TRACE_CREATION = false;
	private static final Set<BaseStruct> ALLOCATED_SET = Collections.synchronizedSet(new HashSet<BaseStruct>());
    private Throwable creationTrace;
    public static final int getAllocatedStructCount() {
    	if(!TRACE_CREATION) {
    		throw new IllegalStateException("Struct creation tracing is not currently enabled"); //$NON-NLS-1$
    	}
    	return ALLOCATED_SET.size();
    }
    
    /**
     * Configures whether the stack trace at creation should be stored inside each struct, for memory-
     * debugging needs.
     * 
     * @param traceCreation the mode to set
     */
	public static void setTraceCreation(boolean traceCreation) {
		TRACE_CREATION = traceCreation;
	}
	
	/**
	 * @return whether stack traces are stored internally in allocated structs, for memory-debugging needs.
	 */
	public static boolean isTraceCreation() {
		return TRACE_CREATION;
	}

    public BaseStruct(long data, boolean owned) {
        this.data = data;
        this.owned = owned;
        if(TRACE_CREATION) {
        	creationTrace = new Exception();
        	
        	if(owned) {
        		ALLOCATED_SET.add(this);
        		trackedInSet = true;
        	}
        }
        
        if(owned) {
        	StructReferenceCache.get().register(this);
        }
    }
    
    // This should be removed in production to use the garbage explorer
    // this method is for test only.
    @Override
	protected void finalize() throws Throwable {
    	if(owned && data!=0) {
    		if(TRACE_CREATION) {
	    		Platform.getInstance().getErrorStream().println(StringUtil.format("Native Domino Struct of class {0} is not properly freed. Stack trace at creation:", getClass()));
	    		creationTrace.printStackTrace(Platform.getInstance().getErrorStream());
    		} else {
    			Platform.getInstance().getErrorStream().println(StringUtil.format("Native Domino Struct of class {0} is not properly freed.", getClass()));
    		}
    		free(true);
    	}
		super.finalize();
	}

    @Override
	public final void free() {
		free(false);
    }
    @Override
	public final void free(boolean force) {
		if(data!=0 && (force||owned)) {
            //C.free(data);
            data = 0;
            if(trackedInSet && owned) {
            	ALLOCATED_SET.remove(this);
            }
        }
    }

    public final long getField(int offset) {
    	_checkRefValidity();
        return C.ptrAdd(data, offset);
    }

    public final long getDataPtr() {
        return data;
    }
    
    @Override
    public long getBackendIdentifier() {
    	return getDataPtr();
    }
    
    public void setDataPtr(long data) {
        this.data = data;
    }
    
    /* ******************************************************************************
	 * Internal accessor assistance
	 ********************************************************************************/
    
    protected final long _getDHandle(int offset) {
    	_checkRefValidity();
    	return C.getDHandle(data, offset);
    }
    protected final void _setDHandle(int offset, long value) {
    	_checkRefValidity();
    	C.setDHandle(data, offset, value);
    }
    protected final byte _getBYTE(int offset) {
    	_checkRefValidity();
    	return C.getBYTE(data, offset);
    }
    protected final void _setBYTE(int offset, byte value) {
    	_checkRefValidity();
    	C.setBYTE(data, offset, value);
    }
    protected final short _getWORD(int offset) {
    	_checkRefValidity();
    	return C.getWORD(data, offset);
    }
    protected final void _setWORD(int offset, short value) {
    	_checkRefValidity();
    	C.setWORD(data, offset, value);
    }
    protected final int _getDWORD(int offset) {
    	_checkRefValidity();
    	return C.getDWORD(data, offset);
    }
    protected final void _setDWORD(int offset, int value) {
    	_checkRefValidity();
    	C.setDWORD(data, offset, value);
    }
    protected final short _getUSHORT(int offset) {
    	_checkRefValidity();
    	return C.getUSHORT(data, offset);
    }
    protected final void _setUSHORT(int offset, short value) {
    	_checkRefValidity();
    	C.setUSHORT(data, offset, value);
    }
    protected final int _getNOTEID(int offset) {
    	_checkRefValidity();
    	return C.getNOTEID(data, offset);
    }
    protected final void _setNOTEID(int offset, int value) {
    	_checkRefValidity();
    	C.setNOTEID(data, offset, value);
    }
    protected final int _getInt(int offset) {
    	_checkRefValidity();
    	return C.getInt(data, offset);
    }
    protected final void _setInt(int offset, int value) {
    	_checkRefValidity();
    	C.setInt(data, offset, value);
    }
    protected final long _getLong(int offset) {
    	_checkRefValidity();
    	return C.getLong(data, offset);
    }
    protected final void _setLong(int offset, long value) {
    	_checkRefValidity();
    	C.setLong(data, offset, value);
    }
    protected final long _getPointer(int offset) {
    	_checkRefValidity();
    	return C.getPointer(data, offset);
    }
    protected final int _getFONTID(int offset) {
    	_checkRefValidity();
    	return C.getFONTID(data, offset);
    }
    protected final void _setFONTID(int offset, int value) {
    	_checkRefValidity();
    	C.setFONTID(data, offset, value);
    }
    protected final int _getHTMLAPI_REF_TYPE(int offset) {
    	_checkRefValidity();
    	return C.getHTMLAPI_REF_TYPE(data, offset);
    }
    
    
    /* ******************************************************************************
	 * Misc.
	 ********************************************************************************/
    
    @Override
    public boolean isRefValid() {
    	return data != 0;
    }
	
	protected void _checkRefValidity() {
		if(!isRefValid()) {
			throw new IllegalStateException(StringUtil.format("Struct of type {0} is no longer valid.", getClass().getSimpleName())); //$NON-NLS-1$
		}
	}
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	if(data != 0) {
	    	return StringUtil.format("[{0}]", //$NON-NLS-1$
	    			getClass().getSimpleName()
	    	);
    	} else {
    		return StringUtil.format("[{0} (deallocated)]", //$NON-NLS-1$
	    			getClass().getSimpleName()
	    	);
    	}
    }
}
