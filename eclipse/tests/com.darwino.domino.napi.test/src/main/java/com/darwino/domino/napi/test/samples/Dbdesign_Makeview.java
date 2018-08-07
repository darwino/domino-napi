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
package com.darwino.domino.napi.test.samples;

import static com.darwino.domino.napi.DominoAPI.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.EnumSet;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.c.IntRef;
import com.darwino.domino.napi.c.LMBCSString;
import com.darwino.domino.napi.enums.LDDELIM;
import com.darwino.domino.napi.enums.VCF1;
import com.darwino.domino.napi.enums.VIEW_COL;
import com.darwino.domino.napi.enums.VIEW_STYLE;
import com.darwino.domino.napi.enums.VIEW_TABLE_Flag;
import com.darwino.domino.napi.enums.VIEW_TABLE_Flag2;
import com.darwino.domino.napi.struct.COLLATE_DESCRIPTOR;
import com.darwino.domino.napi.struct.COLLATION;
import com.darwino.domino.napi.struct.VIEW_COLUMN_FORMAT;
import com.darwino.domino.napi.struct.VIEW_COLUMN_FORMAT2;
import com.darwino.domino.napi.struct.VIEW_TABLE_FORMAT;
import com.darwino.domino.napi.struct.VIEW_TABLE_FORMAT2;
import com.darwino.domino.napi.struct.VIEW_TABLE_FORMAT3;
import com.darwino.domino.napi.test.AllTests;

/**
 * This test is meant to be a Java port of the makeview.c sample from the Domino API documentation
 * 
 * @author Jesse Gallagher
 *
 */
public class Dbdesign_Makeview {
	@BeforeClass
	public static void start() {
		Platform.getInstance().log("{0} start", Dbdesign_Makeview.class.getSimpleName()); //$NON-NLS-1$
	}
	@AfterClass
	public static void after() {
		Platform.getInstance().log("{0} end", Dbdesign_Makeview.class.getSimpleName()); //$NON-NLS-1$
	}
	
	@Test
	public void testMakeview() throws DominoException {
		DominoAPI api = DominoAPI.get();
		
		VIEW_TABLE_FORMAT ViewTableFormat = null;
		VIEW_COLUMN_FORMAT ViewColumnFormat = null;
		VIEW_TABLE_FORMAT2 ViewTableFormat2 = null;
		VIEW_COLUMN_FORMAT2 ViewColumnFormat2 = null;
		VIEW_TABLE_FORMAT3 ViewTableFormat3 = null;
		COLLATION Collation = null;
		COLLATE_DESCRIPTOR CollateDesc = null;
		COLLATE_DESCRIPTOR CollateDesc2 = null;
		IntRef wdc = new IntRef();
		long hSelFormula = 0;
		IntRef selFormulaLen = new IntRef();
		long hFormula_1 = 0;
		IntRef formula_1_Len = new IntRef();
		long hFormula_2 = 0;
		IntRef formula_2_Len = new IntRef();
		long hFormula_3 = 0;
		IntRef formula_3_Len = new IntRef();
		long ppVFBuf = 0;
		long ppCBuf = 0;
		LMBCSString itemName_1 = null;
		LMBCSString itemName_2 = null;
		LMBCSString title_2 = null;
		LMBCSString itemName_3 = null;
		LMBCSString title_3 = null;
		
		long hDB = 0;
		
		try {
			
			Platform.getInstance().log("begin makeview"); //$NON-NLS-1$
			
			hDB = AllTests.getTestDatabase();
			long hNote;
			String viewName = "Test View"; /* Title of view to be created */ //$NON-NLS-1$
			
			short numColumns = 3; // This view will contain this many columns
			short classView = NOTE_CLASS_VIEW;
			
			// Variables pertaining to the view selection formula.
			
			String selFormula = "@All"; //$NON-NLS-1$
			
			// Variables pertaining to the first column in the view.
			// This will be a category column, sorting by keyword.
			String formula_1 = "KeyWordField"; //$NON-NLS-1$
			itemName_1 = new LMBCSString("ItemName1"); //$NON-NLS-1$
			
			// Variables pertaining to the second column in the view.
			String formula_2 = "NumberField"; //$NON-NLS-1$
			title_2 = new LMBCSString("Number Column"); //$NON-NLS-1$
			itemName_2 = new LMBCSString("ItemName2"); //$NON-NLS-1$
			
			// Variables pertaining to the third column in the view.
			String formula_3 = "TextField"; //$NON-NLS-1$
			title_3 = new LMBCSString("Text Column"); //$NON-NLS-1$
			itemName_3 = new LMBCSString("ItemName3"); //$NON-NLS-1$
			
			// Variables pertaining to the $Conflict and $REF
			// summary item names in the view
			String conflictName = DominoAPI.VIEW_CONFLICT_ITEM;
			String refName = DominoAPI.FIELD_LINK;
			
			// Variables pertaining to the $VIEWFORMAT item.
			
			ViewTableFormat = new VIEW_TABLE_FORMAT();
			ViewColumnFormat = new VIEW_COLUMN_FORMAT();
			ViewTableFormat2 = new VIEW_TABLE_FORMAT2();
			ViewColumnFormat2 = new VIEW_COLUMN_FORMAT2();
			ViewTableFormat3 = new VIEW_TABLE_FORMAT3();
			
			short viewFormatBufLen;
			long hViewFormatBuffer;
			long pViewFormatBuffer;
			
			// Variables pertaining to the Collation buffer ($Collation) item
			Collation = new COLLATION();
			CollateDesc = new COLLATE_DESCRIPTOR();
			CollateDesc2 = new COLLATE_DESCRIPTOR();
			short collationBufLen;
			long hCollationBuffer;
			long pCollationBuffer;
			
			Platform.getInstance().log("IBM Notes API\nmakeview sample program"); //$NON-NLS-1$
			
			// Skip Notes init
			// Skip open database
			
			// Check to see if we can find a view with this name already (disallow duplicates)
			try {
				api.NIFFindView(hDB, viewName);
			} catch(DominoException e) {
				if(e.getStatus() == ERR_NOT_FOUND) {
					// Then we're good
				} else {
					throw e;
				}
			}
			
			// Now create a note in database
			hNote = api.NSFNoteCreate(hDB);
			
			// Set the NOTE_CLASS to NOTE_CLASS_VIEW
			long classViewPtr = C.malloc(C.sizeOfWORD);
			C.setWORD(classViewPtr, 0, classView);
			api.NSFNoteSetInfo(hNote, _NOTE_CLASS, classViewPtr);
			C.free(classViewPtr);
			
			
			// Set the view name. ($TITLE field)
			api.NSFItemSetText(hNote, VIEW_TITLE_ITEM, viewName);
			
			
			// Compile and set the selection formula to show all notes.
			try {
				hSelFormula = api.NSFFormulaCompile(null, selFormula, selFormulaLen, wdc, wdc, wdc, wdc, wdc);
			} catch(DominoException e) {
				Platform.getInstance().log("Got DominoException code {0}, message {1}", e.getStatus(), e.getMessage()); //$NON-NLS-1$
				throw e;
			}
			
			Platform.getInstance().log("Compiled formula length is {0}", selFormulaLen.get()); //$NON-NLS-1$
			
			
			// Compile for formula for column 1.
			hFormula_1 = api.NSFFormulaCompile(itemName_1.toString(), formula_1, formula_1_Len, wdc, wdc, wdc, wdc, wdc);
			
			
			// Set summary item by merging column 1 formula into selection formula
			api.NSFFormulaSummaryItem(hSelFormula, itemName_1.toString());
			
			api.NSFFormulaMerge(hFormula_1, hSelFormula);
			
			
			// Compile the formula for column 2.
			hFormula_2 = api.NSFFormulaCompile(itemName_2.toString(), formula_2, formula_2_Len, wdc, wdc, wdc, wdc, wdc);
			
			
			// Set this item as a summary item by merging the column 2
			// formula into the selection formula.
			api.NSFFormulaSummaryItem(hSelFormula, itemName_2.toString());
			
			api.NSFFormulaMerge(hFormula_2, hSelFormula);
			
			
			// Compile the formula for column 3.
			hFormula_3 = api.NSFFormulaCompile(itemName_3.toString(), formula_3, formula_3_Len, wdc, wdc, wdc, wdc, wdc);
			
			
			// Set this item as a summary item by merging the column 3
			// formula into the selection formula.
			api.NSFFormulaSummaryItem(hSelFormula, itemName_3.toString());
			
			api.NSFFormulaMerge(hFormula_3, hSelFormula);
			
			
			// Add $Conflict and $REF summary item names to selection formula
			api.NSFFormulaSummaryItem(hSelFormula, conflictName);
			
			api.NSFFormulaSummaryItem(hSelFormula, refName);
			
			
			// Get the size of the merged formula
			selFormulaLen.set(api.NSFFormulaGetSize(hSelFormula));
			
			
			// Append the selection formula item to the view note
			try {
				api.NSFItemAppend(hNote, ITEM_SUMMARY, VIEW_FORMULA_ITEM, TYPE_FORMULA, api.OSLockObject(hSelFormula), selFormulaLen.get());
			} finally {
				api.OSUnlockObject(hSelFormula);
			}
			
			
			/*
			 * Create the $VIEWFORMAT item. The $VIEWFORMAT item is an item
			 * of TYPE_VIEW_FORMAT with name VIEW_VIEW_FORMAT_ITEM. 
			 *
			 * The $VIEWFORMAT item for this view will consist of the following 
			 * series of structures converted to Domino and Notes canonical format and packed 
			 * together:
			 *
			 *          VIEW_TABLE_FORMAT
			 *          VIEW_COLUMN_FORMAT (for column 1)
			 *          VIEW_COLUMN_FORMAT (for column 2)
			 *          VIEW_COLUMN_FORMAT (for column 3)
			 *          Item Name for column 1
			 *          formula for column 1
			 *          Item Name for column 2
			 *          Title for column 2
			 *          formula for column 2
			 *          Item Name for column 3
			 *          Title for column 3
			 *          formula for column 3
			 *          VIEW_TABLE_FORMAT2
			 *          VIEW_COLUMN_FORMAT2 (for column 1)
			 *          VIEW_COLUMN_FORMAT2 (for column 2)
			 *          VIEW_COLUMN_FORMAT2 (for column 3)
			 *          VIEW_TABLE_FORMAT3
			 *
			 *
			 * First, allocate a buffer, pViewFormatBuffer, that will contain the 
			 * entire $VIEWFORMAT item. 
			 */
			
			Platform.getInstance().log("Pre viewFormatBufLen"); //$NON-NLS-1$
			
			viewFormatBufLen = (short)(
					api.ODSLength( _VIEW_TABLE_FORMAT )   +
					api.ODSLength( _VIEW_COLUMN_FORMAT )  +
					api.ODSLength( _VIEW_COLUMN_FORMAT )  +
					api.ODSLength( _VIEW_COLUMN_FORMAT )  +
					itemName_1.length()                   +
					formula_1_Len.get()                    +
					itemName_2.length()                   +
					title_2.length()                      +
					formula_2_Len.get()                    +
					itemName_3.length()                   +
					title_3.length()                      +
					formula_3_Len.get()                    +
					api.ODSLength( _VIEW_TABLE_FORMAT2 ) +
					api.ODSLength(_VIEW_COLUMN_FORMAT2) +
					api.ODSLength(_VIEW_COLUMN_FORMAT2) +
					api.ODSLength(_VIEW_COLUMN_FORMAT2) +
					api.ODSLength(_VIEW_TABLE_FORMAT3));
			
			
			hViewFormatBuffer = api.OSMemAlloc((short)0, viewFormatBufLen);
			
			Platform.getInstance().log("allocated {0} bytes of memory", viewFormatBufLen); //$NON-NLS-1$
			
			pViewFormatBuffer = api.OSLockObject(hViewFormatBuffer);
			
			
			// Initialize pVFBuf. pViewFormatBuffer will remain pointing to the top
			// of the buffer. pVFBug will move to point to the next available byte.
			ppVFBuf = C.malloc(C.sizeOfPOINTER);
			C.setPointer(ppVFBuf, 0, pViewFormatBuffer);
			
			
			// Initialize the VIEW_TABLE_FORMAT structure
			
			// init to all zeros
			C.memset(ViewTableFormat.getDataPtr(), 0, (byte)0, VIEW_TABLE_FORMAT.sizeOf);
			
			ViewTableFormat.getHeader().setVersion(VIEW_FORMAT_VERSION);
			ViewTableFormat.getHeader().setViewStyle(VIEW_STYLE.TABLE);
			ViewTableFormat.setColumns(numColumns);
			ViewTableFormat.setItemSequenceNumber((short)0); // Reserved - should be 0
			
			ViewTableFormat.setFlags(EnumSet.of(
					VIEW_TABLE_Flag.FLATINDEX,
					VIEW_TABLE_Flag.DISP_UNREADDOCS,
					VIEW_TABLE_Flag.CONFLICT
					));
			ViewTableFormat.setFlags2(EnumSet.noneOf(VIEW_TABLE_Flag2.class));
	
			Platform.getInstance().log("Pre WriteMemory ViewTableFormat"); //$NON-NLS-1$
			
			/*
			 * Call ODSWriteMemory to convert the VIEW_TABLE_FORMAT structure from
			 * host-specific format to Domino and Notes canonical format, and copy it into the 
			 * buffer at location pVFBuf. ODSWriteMemory increments pVFBuf to point
			 * to the next byte in the buffer after the written data structure.
			 */
			
			// For Java, we'll need to make this double-pointer business
			api.ODSWriteMemory(ppVFBuf, _VIEW_TABLE_FORMAT, ViewTableFormat.getDataPtr(), (short)1);
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppVFBuf, 0) - pViewFormatBuffer, api.ODSLength(_VIEW_TABLE_FORMAT)); //$NON-NLS-1$
			
			/*
			 *  Create the VIEW_COLUMN_FORMAT item for column 1. Since this column
			 *  is neither a TIME field or a NUMBER field, we don't need to set the
			 *  TimeFormat or the NumberFormat fields.
			 */
			ViewColumnFormat.setSignature(VIEW_COLUMN_FORMAT_SIGNATURE);
			ViewColumnFormat.setFlags1(EnumSet.of(VCF1.Sort, VCF1.SortCategorize));
			
			ViewColumnFormat.setDisplayWidth((short)8);
			ViewColumnFormat.setFontID(DEFAULT_BOLD_FONT_ID);
			ViewColumnFormat.setFlags2((short)0);
			
			ViewColumnFormat.setFormatDataType(VIEW_COL.TEXT);
			ViewColumnFormat.setListSep(LDDELIM.COMMA);
			
			ViewColumnFormat.setFormulaSize((short)formula_1_Len.get());
			ViewColumnFormat.setItemNameSize((short)itemName_1.length());
			ViewColumnFormat.setTitleSize((short)0);
			ViewColumnFormat.setConstantValueSize((short)0);
			
			
			
			/*
			 * Convert the View Column Format structure for Col 1 to Domino and Notes canonical
			 * format and append it to the buffer. This increments pVFBuf to point to
			 * the next byte in the buffer after the View Column Format.
			 */
			api.ODSWriteMemory(ppVFBuf, _VIEW_COLUMN_FORMAT, ViewColumnFormat.getDataPtr(), (short)1);
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppVFBuf, 0) - pViewFormatBuffer, api.ODSLength(_VIEW_COLUMN_FORMAT)); //$NON-NLS-1$
			
			/*
			 *  Now create the VIEW_COLUMN_FORMAT item for column 2. Since this column
			 *  is not a TIME field, we don't need to set the TimeFormat field.
			 *  Also, since this column will be a sort key, set the Flags1
			 *  structure member appropriately.
			 */
			C.memset(ViewColumnFormat.getDataPtr(), 0, (byte)0, VIEW_COLUMN_FORMAT.sizeOf);
			
			ViewColumnFormat.setSignature(VIEW_COLUMN_FORMAT_SIGNATURE);
			ViewColumnFormat.setFlags1(EnumSet.of(VCF1.Sort, VCF1.Response));
			ViewColumnFormat.setDisplayWidth((short)80);
			ViewColumnFormat.setFontID(DEFAULT_FONT_ID);
			ViewColumnFormat.setFlags2((short)0);
			
			// Set up Number format
			ViewColumnFormat.getNumberFormat().setDigits((byte)0);
			ViewColumnFormat.getNumberFormat().setFormat(NFMT_GENERAL);
			ViewColumnFormat.getNumberFormat().setAttributes((byte)0);
			// Unused is already 0
			
			ViewColumnFormat.setFormatDataType(VIEW_COL.TEXT);
			ViewColumnFormat.setListSep(LDDELIM.COMMA);
			
			// Now set up the lengths of the various strings
			ViewColumnFormat.setFormulaSize((short)formula_2_Len.get());
			ViewColumnFormat.setItemNameSize((short)itemName_2.length());
			ViewColumnFormat.setTitleSize((short)title_2.length());
			ViewColumnFormat.setConstantValueSize((short)0);
	
			Platform.getInstance().log("Pre WriteMemory ViewColumnFormat"); //$NON-NLS-1$
			
			/*
			 * Call ODSWriteMemory to convert the View Column Format structure for
			 * Col 2 to Domino and Notes canonical format and append it to the buffer. This
			 * increments pVFBuf to point to the next byte after the View Column Format
			 * structure.
			 */
			api.ODSWriteMemory(ppVFBuf, _VIEW_COLUMN_FORMAT, ViewColumnFormat.getDataPtr(), (short)1);
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppVFBuf, 0) - pViewFormatBuffer, api.ODSLength(_VIEW_COLUMN_FORMAT)); //$NON-NLS-1$
			
			/*
			 *  Next, create the VIEW_COLUMN_FORMAT item for column 3. Since this column
			 *  is neither a TIME field nor a NUMBER field, we don't need to set the
			 *  TimeFormat or the NumberFormat fields.
			 */
			C.memset(ViewColumnFormat.getDataPtr(), 0, (byte)0, VIEW_COLUMN_FORMAT.sizeOf);
			
			ViewColumnFormat.setSignature(VIEW_COLUMN_FORMAT_SIGNATURE);
			ViewColumnFormat.setFlags1(EnumSet.noneOf(VCF1.class));
			ViewColumnFormat.setDisplayWidth((short)120);
			ViewColumnFormat.setFontID(DEFAULT_FONT_ID);
			ViewColumnFormat.setFlags2((short)0);
			ViewColumnFormat.setFormatDataType(VIEW_COL.TEXT);
			ViewColumnFormat.setListSep(LDDELIM.COMMA);
			
			
			// Now set up lengths of the various strings
			ViewColumnFormat.setFormulaSize((short)formula_3_Len.get());
			ViewColumnFormat.setItemNameSize((short)itemName_3.length());
			ViewColumnFormat.setTitleSize((short)title_3.length());
			ViewColumnFormat.setConstantValueSize((short)0);
			
	
			Platform.getInstance().log("Pre WriteMemory ViewColumnFormat (#2)"); //$NON-NLS-1$
			/*
			 * Convert the View Column Format structure for Col 3 to Domino and Notes canonical 
			 * format and append it to the buffer. This increments pVFBuf to point 
			 * to the next byte after the View Column Format structure.
			 */
			api.ODSWriteMemory(ppVFBuf, _VIEW_COLUMN_FORMAT, ViewColumnFormat.getDataPtr(), (short)1);
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppVFBuf, 0) - pViewFormatBuffer, api.ODSLength(_VIEW_COLUMN_FORMAT)); //$NON-NLS-1$
			
			
			// Append column 1's item name and formula to the buffer.
			C.memcpy(C.getPointer(ppVFBuf, 0), 0, itemName_1.getDataPtr(), 0, itemName_1.length());
			C.setPointer(ppVFBuf, 0, C.ptrAdd(C.getPointer(ppVFBuf, 0), itemName_1.length()));
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppVFBuf, 0) - pViewFormatBuffer, itemName_1.length()); //$NON-NLS-1$
			
			try {
				C.memcpy(C.getPointer(ppVFBuf, 0), 0, api.OSLockObject(hFormula_1), 0, formula_1_Len.get());
			} finally {
				api.OSUnlockObject(hFormula_1);
			}
			C.setPointer(ppVFBuf, 0, C.ptrAdd(C.getPointer(ppVFBuf, 0), formula_1_Len.get()));
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppVFBuf, 0) - pViewFormatBuffer, formula_1_Len.get()); //$NON-NLS-1$
			
			
			// Append column 2's item name, title, and formula to the buffer.
			C.memcpy(C.getPointer(ppVFBuf, 0), 0, itemName_2.getDataPtr(), 0, itemName_2.length());
			C.setPointer(ppVFBuf, 0, C.ptrAdd(C.getPointer(ppVFBuf, 0), itemName_2.length()));
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppVFBuf, 0) - pViewFormatBuffer, itemName_2.length()); //$NON-NLS-1$
			
			C.memcpy(C.getPointer(ppVFBuf, 0), 0, title_2.getDataPtr(), 0, title_2.length());
			C.setPointer(ppVFBuf, 0, C.ptrAdd(C.getPointer(ppVFBuf, 0), title_2.length()));
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppVFBuf, 0) - pViewFormatBuffer, title_2.length()); //$NON-NLS-1$
			
			try {
				C.memcpy(C.getPointer(ppVFBuf, 0), 0, api.OSLockObject(hFormula_2), 0, formula_2_Len.get());
			} finally {
				api.OSUnlockObject(hFormula_2);
			}
			C.setPointer(ppVFBuf, 0, C.ptrAdd(C.getPointer(ppVFBuf, 0), formula_2_Len.get()));
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppVFBuf, 0) - pViewFormatBuffer, formula_2_Len.get()); //$NON-NLS-1$
			
			
			// Append column 3's item name, title, and formula to the buffer.
			C.memcpy(C.getPointer(ppVFBuf, 0), 0, itemName_3.getDataPtr(), 0, itemName_3.length());
			C.setPointer(ppVFBuf, 0, C.ptrAdd(C.getPointer(ppVFBuf, 0), itemName_3.length()));
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppVFBuf, 0) - pViewFormatBuffer, itemName_3.length()); //$NON-NLS-1$
			
			C.memcpy(C.getPointer(ppVFBuf, 0), 0, title_3.getDataPtr(), 0, title_3.length());
			C.setPointer(ppVFBuf, 0, C.ptrAdd(C.getPointer(ppVFBuf, 0), title_3.length()));
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppVFBuf, 0) - pViewFormatBuffer, title_3.length()); //$NON-NLS-1$
			
			try {
				C.memcpy(C.getPointer(ppVFBuf, 0), 0, api.OSLockObject(hFormula_3), 0, formula_3_Len.get());
			} finally {
				api.OSUnlockObject(hFormula_3);
			}
			C.setPointer(ppVFBuf, 0, C.ptrAdd(C.getPointer(ppVFBuf, 0), formula_3_Len.get()));
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppVFBuf, 0) - pViewFormatBuffer, formula_3_Len.get()); //$NON-NLS-1$
			
			
			// Initialize the VIEW_TABLE_FORMAT2 structure.
			C.memset(ViewTableFormat2.getDataPtr(), 0, (byte)0, VIEW_TABLE_FORMAT2.sizeOf);
			
			ViewTableFormat2.setLength(api.ODSLength(_VIEW_TABLE_FORMAT2));
			ViewTableFormat2.setBackgroundColor(NOTES_COLOR_WHITE);
			ViewTableFormat2.setV2BorderColor((short)0);
			ViewTableFormat2.setTitleFont(DEFAULT_BOLD_FONT_ID);
			ViewTableFormat2.setUnreadFont(DEFAULT_FONT_ID);
			ViewTableFormat2.setTotalsFont(DEFAULT_FONT_ID);
			ViewTableFormat2.setAutoUpdateSeconds((short)0);
			
			ViewTableFormat2.setWSig(VALID_VIEW_FORMAT_SIG);
			
	
			Platform.getInstance().log("Pre WriteMemory ViewTableFormat2"); //$NON-NLS-1$
			/*
			 * Convert the View Table Format2 structure to Domino and Notes canonical format 
			 * and append it to the buffer. This increments pVFBuf to point to the
			 * next byte after the View Table Format2 structure.
			 */
			api.ODSWriteMemory(ppVFBuf, _VIEW_TABLE_FORMAT2, ViewTableFormat2.getDataPtr(), (short)1);
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppVFBuf, 0) - pViewFormatBuffer, api.ODSLength(_VIEW_TABLE_FORMAT2)); //$NON-NLS-1$
			
			
			// Write in stubs for the three column format 2s and the view format 3
			C.memset(ViewColumnFormat2.getDataPtr(), 0, (byte)0, VIEW_COLUMN_FORMAT2.sizeOf);
			ViewColumnFormat2.setSignature(VIEW_COLUMN_FORMAT_SIGNATURE2);
			for(int i = 0; i < 3; i++) {
				api.ODSWriteMemory(ppVFBuf, _VIEW_COLUMN_FORMAT2, ViewColumnFormat2.getDataPtr(), (short)1);
				Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppVFBuf, 0) - pViewFormatBuffer, api.ODSLength(_VIEW_COLUMN_FORMAT2)); //$NON-NLS-1$
			}
			C.memset(ViewTableFormat3.getDataPtr(), 0, (byte)0, VIEW_TABLE_FORMAT3.sizeOf);
			ViewTableFormat3.setLength(api.ODSLength(_VIEW_TABLE_FORMAT3));
			api.ODSWriteMemory(ppVFBuf, _VIEW_TABLE_FORMAT3, ViewTableFormat3.getDataPtr(), (short)1);
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppVFBuf, 0) - pViewFormatBuffer, api.ODSLength(_VIEW_TABLE_FORMAT3)); //$NON-NLS-1$

			Platform.getInstance().log("ptr diff {0}", C.getPointer(ppVFBuf, 0) - pViewFormatBuffer); //$NON-NLS-1$
			Platform.getInstance().log("expected length was {0}", viewFormatBufLen); //$NON-NLS-1$
			
			Platform.getInstance().log("Pre NSFItemAppend View format item"); //$NON-NLS-1$
			
			/*
			 * Now append the $VIEWFORMAT item to the note.
			 */
			api.NSFItemAppend(hNote, ITEM_SUMMARY, VIEW_VIEW_FORMAT_ITEM, TYPE_VIEW_FORMAT, pViewFormatBuffer, viewFormatBufLen);
			
			Platform.getInstance().log("Post NSFItemAppend View format item"); //$NON-NLS-1$
			
			api.OSUnlockObject(hViewFormatBuffer);
			Platform.getInstance().log("Post UnlockObject, Pre MemFree"); //$NON-NLS-1$
			api.OSMemFree(hViewFormatBuffer);
			
			Platform.getInstance().log("Post mem free"); //$NON-NLS-1$
			
			/*
			 * Create the View Collation Item. The View Collation Item is an item of 
			 * TYPE_COLLATION with name VIEW_COLLATION_ITEM ($Collation). The View
			 * Collation item consists of a COLLATION structure followed by one 
			 * or more COLLATE_DESCRIPTOR structures, one for each sorted or categorized
			 * column in the view, followed by the item name of each column to be collated. 
			 * 
			 * This view contains two collated columns. The first column in the view is
			 * both sorted and categorized. The second column is sorted. The View Collation
			 * Item therfore consists of the following series of structures converted to 
			 * Domino and Notes canonical format and packed together:
			 *
			 *           COLLATION
			 *           COLLATE_DESCRIPTOR (for column 1)
			 *           COLLATE_DESCRIPTOR (for column 2)
			 *           Item Name for column 1
			 *           Item Name for column 2
			 */
			collationBufLen = (short)(api.ODSLength( _COLLATION )          +
				api.ODSLength( _COLLATE_DESCRIPTOR ) +
				api.ODSLength( _COLLATE_DESCRIPTOR ) +
				itemName_1.length()                  +
				itemName_2.length()                  );
			
			hCollationBuffer = api.OSMemAlloc((short)0, collationBufLen);
			
			Platform.getInstance().log("allocated {0} bytes of memory", collationBufLen); //$NON-NLS-1$
			
			pCollationBuffer = api.OSLockObject(hCollationBuffer);
			C.memset(pCollationBuffer, 0, (byte)0, collationBufLen);
			
			/* 
			 * Initialize pCBuf. Keep pCollationBuffer pointing to the top 
			 * of the buffer, and pCBuf pointing to the next available byte.
			 */
			ppCBuf = C.malloc(C.sizeOfPOINTER);
			C.setPointer(ppCBuf, 0, pCollationBuffer);
			
			
			// Initialize the Collation structure.
			Collation.setItems((short)2);
			Collation.setFlags((byte)0);
			Collation.setSignature(COLLATION_SIGNATURE);
			Collation.setBufferSize(collationBufLen);
			
			assertEquals(Collation.getBufferSize(), collationBufLen);
	
			Platform.getInstance().log("Pre WriteMemory Collation"); //$NON-NLS-1$
			
			/*
			 * Convert the Collation structure to Domino and Notes canonical format and store it
			 * in the ODS buffer. This increments pCBuf to point to the next byte 
			 * after the Collation structure in the buffer.
			 */
			api.ODSWriteMemory(ppCBuf, _COLLATION, Collation.getDataPtr(), (short)1);
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppCBuf, 0) - pCollationBuffer, api.ODSLength(_COLLATION)); //$NON-NLS-1$
			
			/*
			 *  Initialize the collation descriptor for the first column. The
			 *  first column is a category.
			 */
			CollateDesc.setFlags((byte)0);
			CollateDesc.setSignature(COLLATE_DESCRIPTOR_SIGNATURE);
			CollateDesc.setKeyType(COLLATE_TYPE_CATEGORY);
			CollateDesc.setNameOffset((short)0);
			CollateDesc.setNameLength((short)itemName_1.length());
			
	
			Platform.getInstance().log("Pre WriteMemory CollateDesc"); //$NON-NLS-1$
			
			/*
			 * Convert the collation descriptor for the first column to Domino and Notes 
			 * canonical format, and store it in the buffer. This increments
			 * pCBuf to point to the next byte in the buffer after the collation
			 * descriptor.
			 */
			
			api.ODSWriteMemory(ppCBuf, _COLLATE_DESCRIPTOR, CollateDesc.getDataPtr(), (short)1);
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppCBuf, 0) - pCollationBuffer, api.ODSLength(_COLLATE_DESCRIPTOR)); //$NON-NLS-1$
			
			/*
			 * Initialize the collation descriptor for the second column. The
			 * second column is sorted.
			 */
			CollateDesc2.setFlags((byte)0);
			CollateDesc2.setSignature(COLLATE_DESCRIPTOR_SIGNATURE);
			CollateDesc2.setKeyType(COLLATE_TYPE_KEY);
			CollateDesc2.setNameOffset((short)itemName_1.length());
			CollateDesc2.setNameLength((short)itemName_2.length());
			
			Platform.getInstance().log("Pre WriteMemory CollateDesc2"); //$NON-NLS-1$
			
			/*
			 * Convert the collation descriptor for the second column to Domino and Notes 
			 * canonical format, and store it in the buffer. This increments
			 * pCBuf, as before.
			 */
			api.ODSWriteMemory(ppCBuf, _COLLATE_DESCRIPTOR, CollateDesc2.getDataPtr(), (short)1);
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppCBuf, 0) - pCollationBuffer, api.ODSLength(_COLLATE_DESCRIPTOR)); //$NON-NLS-1$
			
			Platform.getInstance().log("appending item name of length {0}: {1}", itemName_1.length(), C.getLMBCSString(itemName_1.getDataPtr(), 0)); //$NON-NLS-1$
			
			// Append the column 1 item name to the buffer.
			C.memcpy(C.getPointer(ppCBuf, 0), 0, itemName_1.getDataPtr(), 0, itemName_1.length());
			C.setPointer(ppCBuf, 0, C.ptrAdd(C.getPointer(ppCBuf, 0), itemName_1.length()));
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppCBuf, 0) - pCollationBuffer, itemName_1.length()); //$NON-NLS-1$
			
			// Append the column 2 item name to the buffer.
			C.memcpy(C.getPointer(ppCBuf, 0), 0, itemName_2.getDataPtr(), 0, itemName_2.length());
			C.setPointer(ppCBuf, 0, C.ptrAdd(C.getPointer(ppCBuf, 0), itemName_2.length()));
			Platform.getInstance().log("Pointer bumped to {0} on write of {1}", C.getPointer(ppCBuf, 0) - pCollationBuffer, itemName_2.length()); //$NON-NLS-1$
			
			Platform.getInstance().log("ptr diff {0}", C.getPointer(ppCBuf, 0) - pCollationBuffer); //$NON-NLS-1$
			Platform.getInstance().log("expected length was {0}", collationBufLen); //$NON-NLS-1$
			
			Platform.getInstance().log("appending item name {0}", C.getLMBCSString(itemName_2.getDataPtr(), 0)); //$NON-NLS-1$
			
			Platform.getInstance().log("Pre NSFItemAppend"); //$NON-NLS-1$
			
			
			// Now append the $COLLATION item to the note.
			api.NSFItemAppend(hNote, ITEM_SUMMARY, VIEW_COLLATION_ITEM, TYPE_COLLATION, pCollationBuffer, collationBufLen);
			
			Platform.getInstance().log("Pre OSUnlockObject"); //$NON-NLS-1$
			
			api.OSUnlockObject(hCollationBuffer);
			
			Platform.getInstance().log("Pre OSMemFree"); //$NON-NLS-1$
			
			api.OSMemFree(hCollationBuffer);
			
			Platform.getInstance().log("Pre NSFNoteCheck"); //$NON-NLS-1$
			api.NSFNoteCheck(hNote);
			
			Platform.getInstance().log("Pre NSFNoteUpdate"); //$NON-NLS-1$
			
			/*
			 *  Done constructing the view note. Now store the view note
			 *  in the database.
			 */
			api.NSFNoteUpdate(hNote, (short)0);
			
			Platform.getInstance().log("Pre NSFNoteClose"); //$NON-NLS-1$
			
			api.NSFNoteClose(hNote);
			
			
			Platform.getInstance().log("Successfully created view note in database."); //$NON-NLS-1$
			Platform.getInstance().log("\nProgram completed successfully."); //$NON-NLS-1$
		
		
		} finally {
			// Clear out dynamically-allocated structs
			if(ppVFBuf != 0) { C.free(ppVFBuf); }
			if(ppCBuf != 0) { C.free(ppCBuf); }
			if(CollateDesc2 != null) { CollateDesc2.free(); }
			if(CollateDesc != null) { CollateDesc.free(); }
			if(Collation != null) { Collation.free(); }
			if(ViewTableFormat2 != null) { ViewTableFormat2.free(); }
			if(ViewColumnFormat != null) { ViewColumnFormat.free(); }
			if(ViewTableFormat != null) { ViewTableFormat.free(); }
			if(itemName_1 != null) { itemName_1.close(); }
			if(itemName_2 != null) { itemName_2.close(); }
			if(title_2 != null) { title_2.close(); }
			if(itemName_3 != null) { itemName_3.close(); }
			if(title_3 != null) { title_3.close(); }
			if(ViewColumnFormat2 != null) { ViewColumnFormat2.free(); }
			if(ViewTableFormat3 != null) { ViewTableFormat3.free(); }
			
			if(hDB != 0) { api.NSFDbClose(hDB); }
		}
	}
}
