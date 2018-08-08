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
package com.darwino.domino.napi.wrap;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.c.IntRef;
import com.ibm.commons.util.StringUtil;

/**
 * This class represents a compiled formula in memory, either newly compiled or read from
 * a stored item.
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 *
 */
public class NSFFormula extends NSFHandle {
	
	static class FormulaRecycler extends OSMemFreeRecycler {
		private long compiledFormulaPtr;
		
		public FormulaRecycler(DominoAPI api, long handle) {
			super(api, handle);
		}
		
		@Override
		void doFree() {
			if(compiledFormulaPtr != 0 && handle != 0) {
				api.OSUnlockObject(handle);
				compiledFormulaPtr = 0;
			}
			
			super.doFree();
		}
		
	}
	
	private int length;
	private byte[] formulaData;

	public NSFFormula(NSFSession session, String formulaText) throws FormulaException, DominoException {
		super(session, 0);
		
		IntRef retFormulaLength = new IntRef();
		IntRef retCompileError = new IntRef();
		IntRef retCompileErrorLine = new IntRef();
		IntRef retCompileErrorColumn = new IntRef();
		IntRef retCompileErrorOffset = new IntRef();
		IntRef retCompileErrorLength = new IntRef();
		
		long hFormula = 0;
		try {
			hFormula = api.NSFFormulaCompile(null, formulaText, retFormulaLength, retCompileError, retCompileErrorLine, retCompileErrorColumn, retCompileErrorOffset, retCompileErrorLength);
		} catch(DominoException ex) {
			if(ex.getStatus() == DominoAPI.ERR_FORMULA_COMPILATION) {
				throw new FormulaException(retCompileError.get(), retCompileErrorLine.get(), retCompileErrorColumn.get(), retCompileErrorOffset.get(), retCompileErrorLength.get());
			} else {
				throw ex;
			}
		}
		
		this.length = retFormulaLength.get();
		
		setHandle(hFormula);
	}
	public NSFFormula(NSFSession session, long handle, int length) {
		super(session, handle);
		this.length = length;
	}
	/**
	 * Constructs a formula object using the provided compiled formula data in memory. This copies
	 * the <code>data</code> parameter's contents into a new array in memory.
	 * 
	 * <p>Unlike the other constructors, this does not actually use a native Domino handle.</p>
	 * 
	 * @param api the {@link DominoAPI} instance to use for operations
	 * @param data the in-memory compiled formula data
	 */
	public NSFFormula(DominoAPI api, byte[] data) {
		// In this case, it's not truly a handle-based object
		super(api);
		this.formulaData = new byte[data.length];
		System.arraycopy(data, 0, this.formulaData, 0, data.length);
		setNeedsFree(false);
	}
	
	public long getCompiledFormulaPtr() {
		_checkRefValidity();
		
		if(((FormulaRecycler)recycler).compiledFormulaPtr == 0) {
			if(this.formulaData != null) {
				// Then write the data to C memory
				((FormulaRecycler)recycler).compiledFormulaPtr = C.malloc(formulaData.length);
				C.writeByteArray(((FormulaRecycler)recycler).compiledFormulaPtr, 0, formulaData, 0, formulaData.length);
			} else {
				// Then use the handle
				((FormulaRecycler)recycler).compiledFormulaPtr = api.OSLockObject(getHandle());
			}
		}
		return ((FormulaRecycler)recycler).compiledFormulaPtr;
	}
	
	public int getLength() {
		return length;
	}
	
	/**
	 * @param selectionFormula whether or not the formula represents a view selection formula
	 * @return the decompiled text content of the formula
	 * @throws DominoException if there is a problem decompiling the formula
	 */
	public String getFormulaText(boolean selectionFormula) throws DominoException {
		_checkRefValidity();
		
		// Early check for formula data without writing out a pointer
		if(formulaData != null && formulaData.length == 0) {
			return StringUtil.EMPTY_STRING;
		}
		
		long compiledFormulaPtr = getCompiledFormulaPtr();
		return api.NSFFormulaDecompile(compiledFormulaPtr, selectionFormula);
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFHandle#isRefValid()
	 */
	@Override
	public boolean isRefValid() {
		if(formulaData != null) {
			// Then it's a "faux" handle
			return true;
		} else {
			return super.isRefValid();
		}
	}

	@Override
	protected void doFree() {
		if(getHandle() != 0) {
			
			try {
				api.OSMemFree(getHandle());
			} catch(DominoException e) {
				// This should be very unlikely
				throw new RuntimeException(e);
			}
		}
		super.doFree();
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.wrap.NSFBase#getParent()
	 */
	@Override
	public NSFBase getParent() {
		return getSession();
	}
	
	@Override
	protected Recycler createRecycler() {
		return new FormulaRecycler(api, getHandle());
	}
}
