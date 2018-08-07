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

package com.darwino.domino.napi.wrap.design;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.VIEW_STYLE;
import com.darwino.domino.napi.enums.VIEW_TABLE_Flag;
import com.darwino.domino.napi.enums.VIEW_TABLE_Flag2;
import com.darwino.domino.napi.struct.VIEW_COLUMN_FORMAT;
import com.darwino.domino.napi.struct.VIEW_COLUMN_FORMAT2;
import com.darwino.domino.napi.struct.VIEW_TABLE_FORMAT;
import com.ibm.commons.util.StringUtil;

/**
 * This class represents the contents of a $ViewFormat item - the combined
 * view and column format structures.
 * 
 * <p>This class does not retain any handles to C-side memory and is serializable.</p>
 * 
 * @author Jesse Gallagher
 * @since 1.5.0
 *
 */
public class NSFViewFormat implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private short version;
	private VIEW_STYLE style;
	private int itemSequenceNumber;
	private Set<VIEW_TABLE_Flag> flags;
	private Set<VIEW_TABLE_Flag2> flags2;
	private List<NSFViewColumn> columns;
	
	/**
	 * Reads a view format from the provided data pointer.
	 * 
	 * @param api the {@link DominoAPI} instance to use to read the view format
	 * @param dataPtr the C pointer referencing the data in memory
	 * @param dataLen the total length of the data in memory
	 */
	public NSFViewFormat(DominoAPI api, long dataPtr, long dataLen) {
		long ppData = C.malloc(C.sizeOfPOINTER);
		try {
			C.setPointer(ppData, 0, dataPtr);
			
			// Begins with a VIEW_TABLE_FORMAT
			VIEW_TABLE_FORMAT viewTableFormat = new VIEW_TABLE_FORMAT();
			int colCount;
			try {
				api.ODSReadMemory(ppData, DominoAPI._VIEW_TABLE_FORMAT, viewTableFormat.getDataPtr(), (short)1);
				this.version = (short)(viewTableFormat.getHeader().getVersion() & 0xFF);
				this.style = viewTableFormat.getHeader().getViewStyle();
				colCount = viewTableFormat.getColumns() & 0xFFFF;
				this.itemSequenceNumber = viewTableFormat.getItemSequenceNumber() & 0xFFFFFFFF;
				this.flags = viewTableFormat.getFlags();
				this.flags2 = viewTableFormat.getFlags2();
			} finally {
				viewTableFormat.free();
			}
			
			// Read in the name/formula/title set for each column
			this.columns = new ArrayList<NSFViewColumn>(colCount);
			// First, read in each of the VIEW_COLUMN_FORMAT structures
			VIEW_COLUMN_FORMAT[] formats = new VIEW_COLUMN_FORMAT[colCount];
			VIEW_COLUMN_FORMAT2[] format2s = new VIEW_COLUMN_FORMAT2[colCount];
			try {
				
				// Per docs: "The item value of a $VIEWFORMAT item consists of a single VIEW_TABLE_FORMAT structure,
				// followed by one VIEW_COLUMN_FORMAT structure for each column, followed by an item name/formula/column
				// title set for each column, followed by a  VIEW_TABLE_FORMAT2 structure, followed by one VIEW_COLUMN_FORMAT2
				// structure for each column, followed by a VIEW_TABLE_FORMAT3 structure."
				String[] names = new String[colCount];
				byte[][] formulas = new byte[colCount][];
				String[] titles = new String[colCount];
				
				for(int i = 0; i < colCount; i++) {
					formats[i] = new VIEW_COLUMN_FORMAT();
					api.ODSReadMemory(ppData, DominoAPI._VIEW_COLUMN_FORMAT, formats[i].getDataPtr(), (short)1);
				}
				
				// Now, read in the column data, using the ppData pointer as the starting point
				long pData = C.getPointer(ppData, 0);
				for(int i = 0; i < colCount; i++) {
					int itemNameSize = formats[i].getItemNameSize() & 0xFFFFFFFF;
					int formulaSize = formats[i].getFormulaSize() & 0xFFFFFFFF;
					int titleSize = formats[i].getTitleSize() & 0xFFFFFFFF;
					int constantSize = formats[i].getConstantValueSize() & 0xFFFFFFFF;
					
					// Read in the name
					if(itemNameSize > 0) {
						names[i] = C.getLMBCSString(pData, 0, itemNameSize);
						pData = C.ptrAdd(pData, itemNameSize);
					} else {
						names[i] = StringUtil.EMPTY_STRING;
					}
					
					// Read in the title
					if(titleSize > 0) {
						titles[i] = C.getLMBCSString(pData, 0, titleSize);
						pData = C.ptrAdd(pData, titleSize);
					} else {
						titles[i] = StringUtil.EMPTY_STRING;
					}
					
					// Read in the compiled formula
					formulas[i] = new byte[formulaSize];
					C.readByteArray(formulas[i], 0, pData, 0, formulaSize);
					pData = C.ptrAdd(pData, formulaSize);
					
					// Also skip over any constant value that may be present
					pData = C.ptrAdd(pData, constantSize);
				}
				
				// Now, an array of VIEW_COLUMN_FORMAT2
				for(int i = 0; i < colCount; i++) {
					format2s[i] = new VIEW_COLUMN_FORMAT2();
					if(C.getPointer(ppData, 0) - dataPtr < dataLen) {
						api.ODSReadMemory(ppData, DominoAPI._VIEW_COLUMN_FORMAT2, format2s[i].getDataPtr(), (short)1);
					}
				}
				
				// Finally, build the columns
				for(int i = 0; i < colCount; i++) {
					this.columns.add(new NSFViewColumn(names[i], formulas[i], titles[i], formats[i].getFlags1(), formats[i].getFlags1Raw(), format2s[i].getFlags3(), format2s[i].getFlags3Raw()));
				}
			} finally {
				for(VIEW_COLUMN_FORMAT format : formats) {
					if(format != null) {
						format.free();
					}
				}
				for(VIEW_COLUMN_FORMAT2 format2 : format2s) {
					if(format2 != null) {
						format2.free();
					}
				}
			}
			
			// TODO read the rest of the format structures
		} finally {
			C.free(ppData);
		}
	}
	
	/**
	 * @return the view format version
	 */
	public short getVersion() {
		return version;
	}
	
	/**
	 * @return the view's style type
	 */
	public VIEW_STYLE getStyle() {
		return style;
	}
	
	/**
	 * @return the sequence number for unique item names
	 */
	public int getItemSequenceNumber() {
		return itemSequenceNumber;
	}
	
	/**
	 * @return the flags from the Flags field of the struct
	 */
	public Set<VIEW_TABLE_Flag> getFlags() {
		return Collections.unmodifiableSet(flags);
	}
	
	/**
	 * @return the flags from the Flags2 field of the struct
	 */
	public Set<VIEW_TABLE_Flag2> getFlags2() {
		return Collections.unmodifiableSet(flags2);
	}
	
	/**
	 * @return the view's columns
	 */
	public List<NSFViewColumn> getColumns() {
		return Collections.unmodifiableList(columns);
	}
}
