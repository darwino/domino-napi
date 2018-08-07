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

import static com.darwino.domino.napi.DominoAPI.*;

import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.struct.LIST.Type;

/**
 * (nsfdata.h)
 * 
 * @author Jesse Gallagher
 *
 */
public class ITEM_VALUE_TABLE extends BaseStruct {
	static {
		int[] sizes = new int[3];
		initNative(sizes);
		sizeOf = sizes[0];
		_Length = sizes[1];
		_Items = sizes[2];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Length;
	public static final int _Items;
	
	public ITEM_VALUE_TABLE() {
		super(C.malloc(sizeOf), true);
	}
	public ITEM_VALUE_TABLE(long data) {
		super(data, false);
	}

	public ITEM_VALUE_TABLE(long data, boolean owned) {
		super(data, owned);
	}
	
	public short getLength() { return _getUSHORT(_Length); }
	public void setLength(short length) { _setUSHORT(_Length, length); }
	
	public short getItems() { return _getUSHORT(_Items); }
	public void setItems(short items) { _setUSHORT(_Items, items); }
	

	public Object[] readItemValues() throws DominoException {
		_checkRefValidity();
		
		Object[] result = new Object[getItems()];
		
		long summaryPtr = C.ptrAdd(data, sizeOf);
		
		int[] lengths = new int[result.length];
		for(int i = 0; i < lengths.length; i++) {
			lengths[i] = C.getWORD(summaryPtr, 0);
			summaryPtr = C.ptrAdd(summaryPtr, C.sizeOfWORD);
		}
		
		Integer[] lengths2 = new Integer[lengths.length];
		for(int i = 0; i < lengths.length; i++) {
			lengths2[i] = lengths[i];
		}
		
		for(int i = 0; i < result.length; i++) {
			if(lengths[i] > C.sizeOfUSHORT) {
				short itemType = C.getUSHORT(summaryPtr, 0);
				switch(itemType) {
				case TYPE_TEXT:
					result[i] = C.getLMBCSString(summaryPtr, C.sizeOfUSHORT, lengths[i] - C.sizeOfUSHORT);
					break;
				case TYPE_TEXT_LIST:
					LIST list = new LIST(C.ptrAdd(summaryPtr, C.sizeOfUSHORT), Type.TEXT_LIST);
					switch(list.getType()) {
					case NOTEREF:
						UNIVERSALNOTEID[] values = list.getNoteRefValues();
						String[] stringVals = new String[values.length];
						for(int unidIndex = 0; unidIndex < values.length; unidIndex++) {
							stringVals[unidIndex] = values[unidIndex].getUNID();
						}
						break;
					case TEXT_LIST:
						result[i] = list.getStringValues();
						break;
					}
					break;
				case TYPE_NUMBER:
					result[i] = C.getNUMBER(summaryPtr, C.sizeOfUSHORT);
					break;
				case TYPE_TIME:
					long tdPtr = C.ptrAdd(summaryPtr, C.sizeOfUSHORT);
					TIMEDATE td = new TIMEDATE(tdPtr);
					result[i] = td;
					break;
				case TYPE_COMPOSITE:
					// Ignore, at least for now
					result[i] = null;
					break;
				default:
					Platform.getInstance().log("Unhandled type {0} on column {1}", itemType, i); //$NON-NLS-1$
					Platform.getInstance().log(new Exception());
					result[i] = null;
				}
			} else {
				result[i] = null;
			}
			// The length may either be 0 or C.sizeOfUSHORT even if there's no data - make sure to skip ahead either way
			summaryPtr = C.ptrAdd(summaryPtr, lengths[i]);
		}
		
		return result;
	}
}
