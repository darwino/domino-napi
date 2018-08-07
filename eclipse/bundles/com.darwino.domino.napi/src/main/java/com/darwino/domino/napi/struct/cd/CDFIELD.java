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

import java.util.Collection;
import java.util.Set;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.FieldFlag;
import com.darwino.domino.napi.enums.LDDELIM;
import com.darwino.domino.napi.enums.LDELIM;
import com.darwino.domino.napi.enums.ODSType;
import com.darwino.domino.napi.enums.ValueType;
import com.darwino.domino.napi.struct.LIST;
import com.darwino.domino.napi.wrap.NSFFormula;

/**
 * This represents a <code>CDFIELD</code> struct in memory. However, due to the way
 * these structs are usually stored, the variable data is pre-loaded using the
 * {@link #fromPointer(DominoAPI, long)} method and not referenced live in memory.
 * 
 * <p>(editods.h)</p>
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 *
 */
public class CDFIELD extends BaseCDStruct<WSIG> implements CDFieldStruct {
	static {
		int[] sizes = new int[15];
		initNative(sizes);
		sizeOf = sizes[0];
		_Header = sizes[1];
		_Flags = sizes[2];
		_DataType = sizes[3];
		_ListDelim = sizes[4];
		_NumberFormat = sizes[5];
		_TimeFormat = sizes[6];
		_FontID = sizes[7];
		_DVLength = sizes[8];
		_ITLength = sizes[9];
		_TabOrder = sizes[10];
		_IVLength = sizes[11];
		_NameLength = sizes[12];
		_DescLength = sizes[13];
		_TextValueLength = sizes[14];
	}
	private static final native void initNative(int[] sizes);

	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Header;
	public static final int _Flags;
	public static final int _DataType;
	public static final int _ListDelim;
	public static final int _NumberFormat;
	public static final int _TimeFormat;
	public static final int _FontID;
	public static final int _DVLength;
	public static final int _ITLength;
	public static final int _TabOrder;
	public static final int _IVLength;
	public static final int _NameLength;
	public static final int _DescLength;
	public static final int _TextValueLength;
	
	// Outside-struct fields stored internally due to canonical format
	private byte[] defaultValueFormula;
	private byte[] inputTranslationFormula;
	private byte[] inputValidationFormula;
	private String name;
	private String description;
	private String[] values;
	private byte[] valuesFormula;
	
	public CDFIELD() {
		super(C.malloc(sizeOf),true);
	}
	public CDFIELD(long data) {
		super(data, false);
	}
	public CDFIELD(long data, boolean owned) {
		super(data, owned);
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.cd.CDStruct#getODSType()
	 */
	@Override
	public ODSType getODSType() {
		return ODSType.CDFIELD;
	}
	
	/* (non-Javadoc)
	 * @see com.darwino.domino.napi.struct.cd.CDStruct#loadVariableData(long, int)
	 */
	@Override
	protected void _readODSVariableData(DominoAPI api, long dataPtr) {
		int offset = 0;
		
		// Read in the compiled default-value formula
		{
			int dvLength = getDVLength() & 0xFFFFFFFF;
			this.defaultValueFormula = new byte[dvLength];
			C.readByteArray(this.defaultValueFormula, 0, dataPtr, offset, dvLength);
			offset += dvLength;
		}
		// Read in the compiled input-translation formula
		{
			int itLength = this.getITLength() & 0xFFFFFFFF;
			this.inputTranslationFormula = new byte[itLength];
			C.readByteArray(this.inputTranslationFormula, 0, dataPtr, offset, itLength);
			offset += itLength;
		}
		// Read in the compiled input-validation formula
		{
			int ivLength = this.getIVLength() & 0xFFFFFFFF;
			this.inputValidationFormula = new byte[ivLength];
			C.readByteArray(this.inputValidationFormula, 0, dataPtr, offset, ivLength);
			offset += ivLength;
		}
		// Read in the LMBCS name string
		{
			int nameLength = this.getNameLength() & 0xFFFFFFFF;
			this.name = C.getLMBCSString(dataPtr, offset, nameLength);
			offset += nameLength;
		}
		// Read in the LMBCS description string
		{
			int descLength = this.getDescLength() & 0xFFFFFFFF;
			this.description = C.getLMBCSString(dataPtr, offset, descLength);
			offset += descLength;
		}
		// Read in the value options (may be a text list or formula)
		{
			int valueLen = this.getTextValueLength() & 0xFFFFFFFF;
			if(valueLen > 0) {
				// Read in the LIST structure from memory
				LIST list = new LIST();
				try {
					long listPtr = C.ptrAdd(dataPtr, offset);
					
					long ppData = C.malloc(C.sizeOfPOINTER);
					try {
						C.setPointer(ppData, 0, listPtr);
						api.ODSReadMemory(ppData, DominoAPI._LIST, list.getDataPtr(), (short)1);
						int listEntries = list.getListEntries();
						if(listEntries > 0) {
							// Then it's definitely a list of strings
							// We can't use getStringValues(), due to the in-memory format
							long sizesPtr = C.calloc(listEntries, C.sizeOfUSHORT);
							try {
								// Read in the sizes words
								api.ODSReadMemory(ppData, DominoAPI._WORD, sizesPtr, (short)listEntries);
								int[] sizes = LIST.readSizes(sizesPtr, listEntries);
								// Now read strings starting from the new pointer location
								this.values = LIST.readStringValues(C.getPointer(ppData, 0), sizes);
							} finally {
								C.free(sizesPtr);
							}
							
							this.valuesFormula = new byte[0];
						} else {
							// Then there's likely a formula following it
							// Read from where ODSReadMemory left the pointer
							this.values = new String[0];
							int formulaLen = valueLen - api.ODSLength(DominoAPI._WORD);
							this.valuesFormula = new byte[formulaLen];
							C.readByteArray(this.valuesFormula, 0, C.getPointer(ppData, 0), 0, formulaLen);
						}
					} finally {
						C.free(ppData);
					}
				} finally {
					list.free();
				}
			} else {
				this.values = new String[0];
				this.valuesFormula = new byte[0];
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

	public short getFlagsRaw() {
		return _getWORD(_Flags);
	}
	public void setFlagsRaw(short flags) {
		_setWORD(_Flags, flags);
	}
	
	public short getDataTypeRaw() {
		return _getWORD(_DataType);
	}
	public void setDataTypeRaw(short dataType) {
		_setWORD(_DataType, dataType);
	}
	
	public short getListDelimRaw() {
		return _getWORD(_ListDelim);
	}
	public void setListDelimRaw(short listDelim) {
		_setWORD(_ListDelim, listDelim);
	}
	
	public short getDVLength() {
		return _getWORD(_DVLength);
	}
	public short getITLength() {
		return _getWORD(_ITLength);
	}
	public short getTabOrder() {
		return _getWORD(_TabOrder);
	}
	public short getIVLength() {
		return _getWORD(_IVLength);
	}
	public short getNameLength() {
		return _getWORD(_NameLength);
	}
	public short getDescLength() {
		return _getWORD(_DescLength);
	}
	public short getTextValueLength() {
		return _getWORD(_TextValueLength);
	}
	
	// ******************************************************************************
	// * Encapsulated getters/setters
	// ******************************************************************************
	
	public Set<FieldFlag> getFlags() {
		return DominoEnumUtil.valuesOf(FieldFlag.class, getFlagsRaw());
	}
	public void setFlags(Collection<FieldFlag> flags) {
		setFlagsRaw(DominoEnumUtil.toBitField(FieldFlag.class, flags));
	}
	
	public ValueType getDataType() {
		return DominoEnumUtil.valueOf(ValueType.class, getDataTypeRaw());
	}
	
	public Set<LDELIM> getListDelim() {
		return DominoEnumUtil.valuesOf(LDELIM.class, (short)(getListDelimRaw() & DominoAPI.LD_MASK));
	}
	
	public LDDELIM getListDisplayDelim() {
		return DominoEnumUtil.valueOf(LDDELIM.class, (short)(getListDelimRaw() & DominoAPI.LDD_MASK));
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * @return the defaultValueFormula
	 */
	public byte[] getDefaultValueFormulaRaw() {
		return defaultValueFormula;
	}
	public NSFFormula getDefaultValueFormula() throws DominoException {
		return new NSFFormula(DominoAPI.get(), getDefaultValueFormulaRaw());
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return the inputTranslationFormula
	 */
	public byte[] getInputTranslationFormulaRaw() {
		return inputTranslationFormula;
	}
	public NSFFormula getInputTranslationFormula() throws DominoException {
		return new NSFFormula(DominoAPI.get(), getInputTranslationFormulaRaw());
	}
	
	/**
	 * @return the inputValidationFormula
	 */
	public byte[] getInputValidationFormulaRaw() {
		return inputValidationFormula;
	}
	public NSFFormula getInputValidationFormula() throws DominoException {
		return new NSFFormula(DominoAPI.get(), getInputValidationFormulaRaw());
	}
	
	/**
	 * @return the values
	 */
	public String[] getValues() {
		return values;
	}
	/**
	 * @return the valuesFormula
	 */
	public byte[] getValuesFormulaRaw() {
		return valuesFormula;
	}
	
	public NSFFormula getValuesFormula() throws DominoException {
		return new NSFFormula(DominoAPI.get(), getValuesFormulaRaw());
	}
}
