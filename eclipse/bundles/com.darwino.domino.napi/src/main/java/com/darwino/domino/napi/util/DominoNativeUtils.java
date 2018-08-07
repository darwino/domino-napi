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

package com.darwino.domino.napi.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.activation.MimetypesFileTypeMap;

import com.ibm.commons.log.Log;
import com.ibm.commons.log.LogMgr;
import com.ibm.commons.util.NotImplementedException;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.StreamUtil;
import com.ibm.commons.Platform;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.ObjectType;
import com.darwino.domino.napi.enums.ValueType;
import com.darwino.domino.napi.struct.BaseStruct;
import com.darwino.domino.napi.struct.FILEOBJECT;
import com.darwino.domino.napi.struct.ITEM;
import com.darwino.domino.napi.struct.ITEM_TABLE;
import com.darwino.domino.napi.struct.LIST;
import com.darwino.domino.napi.struct.LIST.Type;
import com.darwino.domino.napi.wrap.NSFBase;
import com.darwino.domino.napi.wrap.NSFDateRange;
import com.darwino.domino.napi.wrap.NSFDateTime;
import com.darwino.domino.napi.wrap.NSFFormula;
import com.darwino.domino.napi.wrap.NSFItem;
import com.darwino.domino.napi.wrap.NSFUserData;
import com.darwino.domino.napi.wrap.design.NSFViewFormat;
import com.darwino.domino.napi.struct.MIME_PART;
import com.darwino.domino.napi.struct.OBJECT_DESCRIPTOR;
import com.darwino.domino.napi.struct.RANGE;
import com.darwino.domino.napi.struct.TIMEDATE;
import com.darwino.domino.napi.struct.TIMEDATE_PAIR;
import com.darwino.domino.napi.struct.TimeStruct;
import com.darwino.domino.napi.struct.UNIVERSALNOTEID;

public enum DominoNativeUtils {
	;
	
	private static final String LOG_GROUP = "NAPI.DominoNativeUtils"; //$NON-NLS-1$
	private static final LogMgr log = Log.load(LOG_GROUP);
	static {
		log.setLogLevel(LogMgr.LOG_WARN_LEVEL);
	}
	
	/**
	 * This static constant for the charset allows classes to avoid looking it up each time
	 */
	public static final Charset UTF_8 = Charset.forName("UTF-8"); //$NON-NLS-1$
	
	public static final LogMgr NAPI_LOG = Log.load("DominoNAPI"); //$NON-NLS-1$
	public static final LogMgr NAPI_MEMORY_LOG = Log.load("DominoNAPI.Memory"); //$NON-NLS-1$
	static {
		NAPI_LOG.setLogLevel(LogMgr.LOG_WARN_LEVEL);
		NAPI_MEMORY_LOG.setLogLevel(LogMgr.LOG_INFO_LEVEL);
	}
	
	public static boolean isWinx86() {
		try {
			String os = System.getProperty("os.name"); //$NON-NLS-1$
			if(os.contains("Windows")) { //$NON-NLS-1$
				String arch = System.getProperty("os.arch"); //$NON-NLS-1$
				if(arch.contains("x86")) { //$NON-NLS-1$
					return true;
				}
			}
		} catch(Exception ex) {
			Platform.getInstance().log(ex);
		}
		return false;
	}
	
	public static boolean isWinx64() {
		try {
			String os = System.getProperty("os.name"); //$NON-NLS-1$
			if(os.contains("Windows")) { //$NON-NLS-1$
				String arch = System.getProperty("os.arch"); //$NON-NLS-1$
				if(arch.contains("x64") || arch.contains("amd64")) { //$NON-NLS-1$ //$NON-NLS-2$
					return true;
				}
			}
		} catch(Exception ex) {
			Platform.getInstance().log(ex);
		}
		return false;
	}
	
	public static boolean isMac_x86() {
		try {
			String os = System.getProperty("os.name"); //$NON-NLS-1$
			if(os.contains("Mac OS X")) { //$NON-NLS-1$
				String arch = System.getProperty("os.arch"); //$NON-NLS-1$
				if(arch.contains("x86") && !arch.contains("x86_64")) {  //$NON-NLS-1$//$NON-NLS-2$
					return true;
				}
			}
		} catch(Exception ex) {
			Platform.getInstance().log(ex);
		}
		return false;
	}
	
	public static boolean isMac_x64() {
		try {
			String os = System.getProperty("os.name"); //$NON-NLS-1$
			if(os.contains("Mac OS X")) { //$NON-NLS-1$
				String arch = System.getProperty("os.arch"); //$NON-NLS-1$
				if(arch.contains("x86_64")) { //$NON-NLS-1$
					return true;
				}
			}
		} catch(Exception ex) {
			Platform.getInstance().log(ex);
		}
		return false;
	}
	
	public static boolean isLinux_x86() {
		try {
			String os = System.getProperty("os.name"); //$NON-NLS-1$
			if(os.contains("Linux")) { //$NON-NLS-1$
				String arch = System.getProperty("os.arch"); //$NON-NLS-1$
				if(arch.contains("i386") || arch.contains("x86")) { //$NON-NLS-1$
					return true;
				}
			}
		} catch(Exception ex) {
			Platform.getInstance().log(ex);
		}
		return false;
	}
	
	public static boolean isLinux_x64() {
		try {
			String os = System.getProperty("os.name"); //$NON-NLS-1$
			if(os.contains("Linux")) { //$NON-NLS-1$
				String arch = System.getProperty("os.arch"); //$NON-NLS-1$
				if(arch.contains("amd64")) { //$NON-NLS-1$
					return true;
				}
			}
		} catch(Exception ex) {
			Platform.getInstance().log(ex);
		}
		return false;
	}
	
	/**
	 * Returns the MIME type for the provided extension (without the dot), according to Domino.
	 */
	public static String getMIMETypeForExtension(String ext) {
		if(StringUtil.isEmpty(ext)) {
			return ""; //$NON-NLS-1$
		}
		
		final short bufferSize = 32;
		long typeBufPtr = C.malloc(bufferSize);
		long subtypeBufPtr = C.malloc(bufferSize);
		long descrBufPtr = C.malloc(bufferSize);
		try {
			DominoAPI.get().MimeGetTypeInfoFromExt(ext, typeBufPtr, bufferSize, subtypeBufPtr, bufferSize, descrBufPtr, bufferSize);
			byte[] typeData = new byte[bufferSize];
			C.readByteArray(typeData, 0, typeBufPtr, 0, typeData.length);
			String type = readString(typeData);
			byte[] subtypeData = new byte[bufferSize];
			C.readByteArray(subtypeData, 0, subtypeBufPtr, 0, subtypeData.length);
			String subtype = readString(subtypeData);
			
			String result;
			if(StringUtil.isEmpty(type) || StringUtil.isEmpty(subtype)) {
				result = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType("foo." + ext); //$NON-NLS-1$
			} else {
				result = type + '/' + subtype;
			}
			return result;
		} finally {
			// Shouldn't be an exception, but you can never be too careful
			C.free(typeBufPtr);
			C.free(subtypeBufPtr);
			C.free(descrBufPtr);
		}
	}
	
	/**
	 * Returns the file extension (without the dot) for the provided MIME type, according to Domino.
	 */
	public static String getExtensionForMIMEType(String mimeType) {
		if(StringUtil.isEmpty(mimeType)) {
			return ""; //$NON-NLS-1$
		}
		if(!mimeType.contains("/")) { //$NON-NLS-1$
			throw new IllegalArgumentException("MIME Type must contain type and subtype parts: " + mimeType);
		}
		
		int delimIndex = mimeType.indexOf("/"); //$NON-NLS-1$
		String type = mimeType.substring(0, delimIndex);
		String subtype = mimeType.substring(delimIndex+1);
		
		final short bufferSize = 32;
		long extBufPtr = C.malloc(bufferSize);
		long descrBufPtr = C.malloc(bufferSize);
		try {
			DominoAPI.get().MimeGetExtFromTypeInfo(type, subtype, extBufPtr, bufferSize, descrBufPtr, bufferSize);
			byte[] extData = new byte[bufferSize];
			C.readByteArray(extData, 0, extBufPtr, 0, extData.length);
			String ext = readString(extData);
			
			return ext;
		} finally {
			C.free(extBufPtr);
			C.free(descrBufPtr);
		}
	}
	
	public static int indexOf(byte[] bytes, byte value) {
		for(int i = 0; i < bytes.length; i++) {
			if(bytes[i] == value) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Reads a null-terminated UTF-8 String from the given byte array, stopping at the first null character or the end of the array.
	 */
	public static String readString(byte[] bytes) {
		int nullByte = indexOf(bytes, (byte)0);
		int length = nullByte == -1 ? bytes.length : nullByte;
		return new String(bytes, 0, length);
	}
	
	public static String readLMBCSString(byte[] bytes) {
		long temp = C.malloc(bytes.length);
		try {
			C.writeByteArray(temp, 0, bytes, 0, bytes.length);
			return C.getLMBCSString(temp, 0, bytes.length);
		} finally {
			C.free(temp);
		}
	}
	
	/**
	 * <p>Convert the provided values into an {@link ITEM_TABLE} with no item names.</p>
	 * 
	 * <p>The caller is responsible for freeing the returned object.</p>
	 * 
	 * @return an {@link ITEM_TABLE} containing the provided values with no item names
	 * @throws IllegalArgumentException if the provided values cannot be converted to NSF-friendly formats
	 * 		or if the total size exceeds the capacity of an ITEM_TABLE
	 * @throws NullPointerException if the provided value array contains a <code>null</code> value
	 */
	public static ITEM_TABLE toItemTable(Object... values) throws DominoException {
		if(values.length > 0xFFFF) {
			throw new IllegalArgumentException(StringUtil.format("Values count {0} exceeds capacity of USHORT", values.length));
		}
		
		// Loop through the input values to figure out the final structure size and
		// convert to internal representations if needed
		int totalSize = ITEM_TABLE.sizeOf;
		long[] internalValues = new long[values.length];
		try {
			int[] internalSizes = new int[values.length];
			for(int i = 0; i < values.length; i++) {
				// Each value will be stored with an ITEM header and a data type as a USHORT
				totalSize += ITEM.sizeOf + C.sizeOfUSHORT;
				
				if(values[i] == null) {
					throw new NullPointerException("Cannot add a null value to an ITEM_TABLE");
				} else if(values[i] instanceof Number) {
					// Then it's going to be stored as a double
					totalSize += C.sizeOfDouble;
				} else if(isDateTimeType(values[i])) {
					// Then it'll be a TIMEDATE
					totalSize += TIMEDATE.sizeOf;
				} else if(isDateRangeType(values[i])) {
					// Then it'll be a TIMEDATE_PAIR
					totalSize += TIMEDATE_PAIR.sizeOf;
				} else if(values[i] instanceof CharSequence) {
					// Then it'll be stored as an LMBCS string - convert and get the length
					long lmbcsString = C.toLMBCSString(values[i].toString());
					internalValues[i] = lmbcsString;
					internalSizes[i] = C.strlen(lmbcsString, 0);
					totalSize += internalSizes[i];
				} else {
					throw new IllegalArgumentException(StringUtil.format("Unhandled value type {0}", values[i].getClass().getName()));
				}
			}
			
			if(totalSize > 0xFFFF) {
				throw new IllegalArgumentException(StringUtil.format("Total value size of {0} exceeds capacity of USHORT", totalSize));
			}
			
			// Allocate the total size and initialize the start with an item table structure
			long tablePtr = C.malloc(totalSize);
			ITEM_TABLE result = new ITEM_TABLE(tablePtr, true);
			result.setLength((short)totalSize);
			result.setItems((short)values.length);
			
			// Follow the ITEM_TABLE with a series of ITEM structures and values
			long itemsPtr = C.ptrAdd(tablePtr, ITEM_TABLE.sizeOf);
			
			// The data will start after the ITEMs end
			long valueDataPtr = itemsPtr;
			for (int i = 0; i < values.length; i++) {
				valueDataPtr = C.ptrAdd(valueDataPtr, ITEM.sizeOf);
			}
			
			for(int i = 0; i < values.length; i++) {
				int valueSize = 0;
				
				// For each item, write its ITEM structure and then jump ahead to
				// write its value after the space allocated for ITEMs
				
				ITEM valueItem = new ITEM(itemsPtr);
				valueItem.setNameLength((short)0);
				if(values[i] instanceof Number) {
					// Then store it as a double
					valueSize = C.sizeOfDouble + C.sizeOfUSHORT;
					valueItem.setValueLength((short)valueSize);
					
					
					C.setUSHORT(valueDataPtr, 0, DominoAPI.TYPE_NUMBER);
					valueDataPtr = C.ptrAdd(valueDataPtr, C.sizeOfUSHORT);
					
					C.setDouble(valueDataPtr, 0, ((Number)values[i]).doubleValue());
					valueDataPtr = C.ptrAdd(valueDataPtr, C.sizeOfDouble);
				} else if(isDateTimeType(values[i])) {
					// Then store it as a TIMEDATE
					valueSize = TIMEDATE.sizeOf + C.sizeOfUSHORT;
					valueItem.setValueLength((short)valueSize);
					
					
					C.setUSHORT(valueDataPtr, 0, DominoAPI.TYPE_TIME);
					valueDataPtr = C.ptrAdd(valueDataPtr, C.sizeOfUSHORT);
					
					TIMEDATE timedate = new TIMEDATE(valueDataPtr);
					fillTimeDate(timedate, values[i]);
					valueDataPtr = C.ptrAdd(valueDataPtr, TIMEDATE.sizeOf);
				} else if(isDateRangeType(values[i])) {
					// Then store it as a TIMEDATE_PAIR
					valueSize = TIMEDATE_PAIR.sizeOf + C.sizeOfUSHORT;
					valueItem.setValueLength((short)valueSize);
					
					
					C.setUSHORT(valueDataPtr, 0, DominoAPI.TYPE_TIME);
					valueDataPtr = C.ptrAdd(valueDataPtr, C.sizeOfUSHORT);
					
					TIMEDATE_PAIR timedatepair = new TIMEDATE_PAIR(valueDataPtr);
					fillTimeDatePair(timedatepair, values[i]);
					valueDataPtr = C.ptrAdd(valueDataPtr, TIMEDATE_PAIR.sizeOf);
				} else if(values[i] instanceof CharSequence) {
					// Then store it as an LMBCS string, which will already be converted in internalValues
					valueSize = internalSizes[i] + C.sizeOfUSHORT;
					valueItem.setValueLength((short)valueSize);
					
					
					C.setUSHORT(valueDataPtr, 0, DominoAPI.TYPE_TEXT);
					valueDataPtr = C.ptrAdd(valueDataPtr, C.sizeOfUSHORT);
					
					C.memcpy(valueDataPtr, 0, internalValues[i], 0, internalSizes[i]);
					valueDataPtr = C.ptrAdd(valueDataPtr, internalSizes[i]);
				}
				
				itemsPtr = C.ptrAdd(itemsPtr, ITEM.sizeOf);
			}
			
			return result;
		} finally {
			// Deallocate any temporary internal converted values
			for(long internalPtr : internalValues) {
				if(internalPtr != 0) {
					C.free(internalPtr);
				}
			}
		}
	}
	
	/**
	 * Determines whether the object is of a supported date/time class.
	 * 
	 * <p>Currently supported classes are {@link Date}, {@link Calendar}, {@link TimeOnly},
	 * {@link DateOnly}, and {@link NSFDateTime}.</p>
	 * 
	 * @return whether the provided object is a date/time type known to the NAPI
	 */
	public static boolean isDateTimeType(Object val) {
		if(val instanceof Date) {
			return true;
		} else if(val instanceof Calendar) {
			return true;
		} else if(val instanceof NSFDateTime) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Determines whether the class is a supported date/time class.
	 * 
	 * <p>Currently supported classes are {@link Date}, {@link Calendar}, {@link TimeOnly},
	 * {@link DateOnly}, and {@link NSFDateTime}.</p>
	 * 
	 * @return whether the provided class is a date/time class known to the NAPI
	 */
	public static boolean isDateTimeType(Class<?> valClass) {
		if(Date.class.isAssignableFrom(valClass)) {
			return true;
		} else if(Calendar.class.isAssignableFrom(valClass)) {
			return true;
		} else if(NSFDateTime.class.isAssignableFrom(valClass)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Determines whether the object is of a supported date/time range class.
	 * 
	 * <p>Currently supported classes are {@link DateOnlyRange}, {@link TimeOnlyRange},
	 * and {@link NSFDateRange}.</p>
	 * 
	 * @return whether the provided object is a date/time range type known to the NAPI
	 */
	public static boolean isDateRangeType(Object val) {
		if(val == null) {
			return false;
		} else {
			return isDateRangeType(val.getClass());
		}
	}
	/**
	 * Determines whether the class is a supported date/time range class.
	 * 
	 * <p>Currently supported classes are {@link DateOnlyRange}, {@link TimeOnlyRange},
	 * and {@link NSFDateRange}.</p>
	 * 
	 * @return whether the provided class is a date/time range type known to the NAPI
	 */
	public static boolean isDateRangeType(Class<?> valClass) {
		if(NSFDateRange.class.isAssignableFrom(valClass)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Sets the provided TIMEDATE value to an appropriate value based on the type of val
	 */
	public static void fillTimeDate(TIMEDATE timedate, Object val) throws DominoException {
		if(val instanceof Date) {
			Date dateVal = (Date)val;
			timedate.fromJavaDate(dateVal);
		} else if(val instanceof Calendar) {
			timedate.fromJavaCalendar((Calendar)val);
		} else if(val instanceof NSFDateTime) {
			NSFDateTime dt = (NSFDateTime)val;
			if(dt.isAnyTime()) {
				timedate.fromDateOnly(dt.getYear(), dt.getMonth(), dt.getDay());
			} else if(dt.isAnyDate()) {
				timedate.fromTimeOnly(dt.getHour(), dt.getMinute(), dt.getSecond(), dt.getHundredth());
			} else {
				timedate.fromJavaCalendar(dt.toCalendar());
			}
		} else {
			throw new IllegalArgumentException("Illegal object provided: " + val); //$NON-NLS-1$
		}
	}
	
	/**
	 * Sets the provided TIMEDATE_PAIR value to an appropriate value based on the type of val
	 */
	public static void fillTimeDatePair(TIMEDATE_PAIR timedatepair, Object val) throws DominoException {
		if(val instanceof NSFDateRange) {
			NSFDateRange range = (NSFDateRange)val;
			if(range.isAnyTime()) {
				NSFDateTime lower = range.getLower();
				NSFDateTime upper = range.getUpper();
				timedatepair.getLower().fromDateOnly(lower.getYear(), lower.getMonth(), lower.getDay());
				timedatepair.getUpper().fromDateOnly(upper.getYear(), upper.getMonth(), upper.getDay());
			} else if(range.isAnyDate()) {
				NSFDateTime lower = range.getLower();
				NSFDateTime upper = range.getUpper();
				timedatepair.getLower().fromTimeOnly(lower.getHour(), lower.getMinute(), lower.getSecond(), lower.getHundredth());
				timedatepair.getUpper().fromTimeOnly(upper.getHour(), upper.getMinute(), upper.getSecond(), upper.getHundredth());
			} else {
				timedatepair.getLower().fromJavaCalendar(range.getLower().toCalendar());
				timedatepair.getUpper().fromJavaCalendar(range.getUpper().toCalendar());
			}
		}
	}
	
	/**
	 * Reads the value of the reference item into Java objects when possible and, when not, into copies of
	 * the struct objects in memory. Any objects created this way must be deallocated by the caller.
	 * 
	 * <p>The resultant type and its level of wrapping depends on the data type:</p>
	 * 
	 * <dl>
	 * 	<dt>UNAVAILABLE</dt>
	 *  <dd><code>null</code></dd>
	 *  
	 *  <dt>TEXT</dt>
	 *  <dd>{@link String}</dd>
	 *  
	 *  <dt>TEXT_LIST</dt>
	 *  <dd>{@link LIST}</dd>
	 *  
	 *  <dt>NUMBER</dt>
	 *  <dd>{@link Double}</dd>
	 *  
	 *  <dt>NUMBER_RANGE</dt>
	 *  <dd>{@link RANGE}</dd>
	 *  
	 *  <dt>TIME</dt>
	 *  <dd>{@link TIMEDATE}</dd>
	 *  
	 *  <dt>TIME_RANGE</dt>
	 *  <dd>{@link RANGE}</dd>
	 *  
	 *  <dt>FORMULA</dt>
	 *  <dd>{@link NSFFormula}</dd>
	 *  
	 *  <dt>USERID</dt>
	 *  <dd>{@link String}</dd>
	 *  
	 *  <dt>OBJECT</dt>
	 *  <dd>{@link OBJECT_DESCRIPTOR} or {@link FILEOBJECT}</dd>
	 *  
	 *  <dt>NOTEREF_LIST</dt>
	 *  <dd>{@link UNIVERSALNOTEID}[]</dd>
	 *  
	 *  <dt>USERDATA</dt>
	 *  <dd>{@link NSFUserData}</dd>
	 *  
	 *  <dt>MIME_PART</dt>
	 *  <dd>{@link MIME_PART}</dd>
	 *  
	 *  <dt>VIEW_FORMAT</dt>
	 *  <dd>{@link NSFViewFormat}</dd>
	 * </dl>
	 * 
	 * @throws DominoException in the case of a general problem reading the value
	 * @throws NotImplementedException when reading the specified value type has not yet been implemented but will be
	 * @throws UnsupportedOperationException when reading the specified value type will not be implemented
	 * @throws IllegalArgumentException when the specified value type is illegal
	 */
	public static Object readItemValue(DominoAPI api, long valuePtr, int size, long hNote) throws DominoException {
		// Note: for many of the currently-unsupported operations, it may be best to just return
		// a byte[] of the contents
		// -jesse 2015-08-07
		
		// If the value either doesn't exist or contains only a type identifier, return early
		if(size <= C.sizeOfWORD) {
			return null;
		}
		
		if(NAPI_LOG.isTraceDebugEnabled()) {
			NAPI_LOG.traceDebug("readItemValue: Going to read type of value with size {0}", size); //$NON-NLS-1$
		}
		
		ValueType type = DominoEnumUtil.valueOf(ValueType.class, C.getWORD(valuePtr, 0));
		if(NAPI_LOG.isTraceDebugEnabled()) {
			NAPI_LOG.traceDebug("readItemValue: type is {0}", type); //$NON-NLS-1$
		}
		long pValuePtr = 0; // needed for canonical types
		switch(type) {
		case ERROR:
			// TODO implement
			throw new NotImplementedException("TYPE_ERROR not yet implemented"); //$NON-NLS-1$
		case UNAVAILABLE:
			return null;
		case TEXT:
			return C.getLMBCSString(valuePtr, C.sizeOfWORD, size - C.sizeOfWORD).replace('\0', '\n');
		case TEXT_LIST:
			LIST list = new LIST(C.ptrAdd(valuePtr, C.sizeOfWORD), Type.TEXT_LIST);
			return list.getStringValues();
		case NUMBER:
			return C.getDouble(valuePtr, C.sizeOfWORD);
		case NUMBER_RANGE:
			RANGE range = new RANGE(C.ptrAdd(valuePtr, C.sizeOfWORD), RANGE.Type.NUMBER);
			return range.getNumberValues();
		case TIME: {
			TIMEDATE result = new TIMEDATE();
			C.memcpy(result.getDataPtr(), 0, valuePtr, C.sizeOfWORD, TIMEDATE.sizeOf);
			return result;
		}
		case TIME_RANGE: {
			RANGE timeRange = new RANGE(C.ptrAdd(valuePtr, C.sizeOfWORD), RANGE.Type.TIME);
			// TODO add support for time ranges
			TIMEDATE[] times = timeRange.getTimeDateValues();
			TIMEDATE_PAIR[] pairs = timeRange.getTimeDatePairValues();
			TimeStruct[] result = new TimeStruct[times.length + pairs.length];
			
			for(int i = 0; i < times.length; i++) {
				TIMEDATE time = new TIMEDATE();
				C.memcpy(time.getDataPtr(), 0, times[i].getDataPtr(), 0, TIMEDATE.sizeOf);
				result[i] = time;
			}
			
			for(int i = 0; i < pairs.length; i++) {
				TIMEDATE_PAIR pair = new TIMEDATE_PAIR();
				C.memcpy(pair.getDataPtr(), 0, pairs[i].getDataPtr(), 0, TIMEDATE_PAIR.sizeOf);
				result[times.length + i] = pair;
			}
			
			return result;
		}
		case FORMULA:
			// Read the data and return as a byte array
			byte[] formulaData = new byte[size-C.sizeOfWORD];
			C.readByteArray(formulaData, 0, valuePtr, C.sizeOfWORD, size-C.sizeOfWORD);
			return new NSFFormula(api, formulaData);
		case USERID:
			// This is the textual username followed by an 8-byte license ID.
			// We don't care about the license ID, so just read the name data.
			int userIdSize = size - C.sizeOfWORD - C.sizeOfLICENSEID;
			if(userIdSize < 1) {
				return ""; //$NON-NLS-1$
			}
			if(NAPI_LOG.isTraceDebugEnabled()) {
				NAPI_LOG.traceDebug("Going to read USERID data of size {0}", userIdSize); //$NON-NLS-1$
			}
			return C.getLMBCSString(valuePtr, C.sizeOfWORD, userIdSize);
		case INVALID_OR_UNKNOWN:
			throw new IllegalArgumentException("Item type is TYPE_INVALID_OR_UNKNOWN"); //$NON-NLS-1$
		case COMPOSITE:
			// TODO maybe implement; this is better done at a higher level, though
			// This will be in canonical format
			if(NAPI_LOG.isWarnEnabled()) {
				NAPI_LOG.warn("Attempted to read unimplemented value of TYPE_COMPOSITE"); //$NON-NLS-1$
			}
			return null;
		case COLLATION:
			// TODO implement
			// This will be in canonical format
			throw new NotImplementedException("TYPE_COLLATION not yet implemented"); //$NON-NLS-1$
		case OBJECT:
			// This will be in canonical format
			
			pValuePtr = C.malloc(C.sizeOfPOINTER);
			try {
				C.setPointer(pValuePtr, 0, C.ptrAdd(valuePtr, C.sizeOfWORD));
				// This will always start with an OBJECT_DESCRIPTOR, but may actually be part of a FILEOBJECT
				OBJECT_DESCRIPTOR descriptor = new OBJECT_DESCRIPTOR();
				api.ODSReadMemory(pValuePtr, DominoAPI._OBJECT_DESCRIPTOR, descriptor.getDataPtr(), (short)1);
				ObjectType typeVal = descriptor.getObjectType();
				if(typeVal == ObjectType.FILE) {
					// If it's a file, scrap the descriptor and read the larger structure
					descriptor.free();
					
					FILEOBJECT fileObject = new FILEOBJECT();
					C.setPointer(pValuePtr, 0, C.ptrAdd(valuePtr, C.sizeOfWORD));
					api.ODSReadMemory(pValuePtr,  DominoAPI._FILEOBJECT, fileObject.getDataPtr(), (short)1);
					
					String fileName = C.getLMBCSString(C.getPointer(pValuePtr, 0), 0, fileObject.getFileNameLength());
					fileObject.setInternalFileName(fileName);
					
					return fileObject;
				} else {
					// Otherwise, return the descriptor as-is
					return descriptor;
				}
			} finally {
				C.free(pValuePtr);
			}
		case NOTEREF_LIST: {
			LIST unidList = new LIST(C.ptrAdd(valuePtr, C.sizeOfWORD), LIST.Type.NOTEREF);
			UNIVERSALNOTEID[] values = unidList.getNoteRefValues();
			UNIVERSALNOTEID[] result = new UNIVERSALNOTEID[values.length];
			for(int i = 0; i < values.length; i++) {
				result[i] = new UNIVERSALNOTEID();
				C.memcpy(result[i].getDataPtr(), 0, values[i].getDataPtr(), 0, UNIVERSALNOTEID.sizeOf);
			}
			return result;
		}
		case VIEW_FORMAT: {
			// This will be in canonical format
			long dataPtr = C.ptrAdd(valuePtr, C.sizeOfWORD);
			return new NSFViewFormat(api, dataPtr, size-C.sizeOfWORD);
		}
		case ICON:
			// TODO implement
			// This will be in canonical format
			throw new NotImplementedException("TYPE_ICON not yet implemented"); //$NON-NLS-1$
		case NOTELINK_LIST:
			// TODO implement
			throw new NotImplementedException("TYPE_NOTELINK_LIST not yet implemented"); //$NON-NLS-1$
		case SIGNATURE:
			// TODO implement
			// This will be in canonical format
			throw new NotImplementedException("TYPE_SIGNATURE not yet implemented"); //$NON-NLS-1$
		case SEAL:
			// TODO implement
			// This will be in canonical format
			throw new NotImplementedException("TYPE_SEAL not yet implemented"); //$NON-NLS-1$
		case SEALDATA:
			// TODO implement
			// This will be in canonical format
			throw new NotImplementedException("TYPE_SEALDATA not yet implemented"); //$NON-NLS-1$
		case SEAL_LIST:
			// TODO implement
			// This will be in canonical format
			throw new NotImplementedException("TYPE_SEAL_LIST not yet implemented"); //$NON-NLS-1$
		case HIGHLIGHTS:
			// TODO implement
			throw new NotImplementedException("TYPE_HIGHLIGHTS not yet implemented"); //$NON-NLS-1$
		case WORKSHEET_DATA:
			throw new UnsupportedOperationException("TYPE_WORKSHEET_DATA is not supported"); //$NON-NLS-1$
		case USERDATA:
			// This will be in canonical format
			
			long dataPtr = C.ptrAdd(valuePtr, C.sizeOfWORD);
			short length = (short)(C.getBYTE(dataPtr, 0) & 0xFFFF);
			dataPtr = C.ptrAdd(dataPtr, C.sizeOfBYTE);
			String formatName = C.getLMBCSString(dataPtr, 0, length);
			dataPtr = C.ptrAdd(dataPtr, length);
			int dataLen = size - C.sizeOfWORD - C.sizeOfBYTE - length;
			byte[] data = new byte[dataLen];
			C.readByteArray(data, 0, dataPtr, 0, dataLen);
			
			return new NSFUserData(formatName, data);
		case QUERY:
			// TODO implement
			// This will be in canonical format
			throw new NotImplementedException("TYPE_QUERY not yet implemented"); //$NON-NLS-1$
		case ACTION:
			// TODO implement
			// This will be in canonical format
			throw new NotImplementedException("TYPE_ACTION not yet implemented"); //$NON-NLS-1$
		case ASSISTANT_INFO:
			// TODO implement
			// This will be in canonical format
			throw new NotImplementedException("TYPE_ASSISTANT_INFO not yet implemented"); //$NON-NLS-1$
		case VIEWMAP_DATASET:
			// TODO implement
			// This will be in canonical format
			throw new NotImplementedException("TYPE_VIEWMAP_DATASET not yet implemented"); //$NON-NLS-1$
		case VIEWMAP_LAYOUT:
			// TODO implement
			// This will be in canonical format
			throw new NotImplementedException("TYPE_VIEWMAP not yet implemented"); //$NON-NLS-1$
		case LSOBJECT:
			// TODO implement
			throw new NotImplementedException("TYPE_LSOBJECT not yet implemented"); //$NON-NLS-1$
		case HTML:
			// TODO implement
			// This will be in LMBCS
			throw new NotImplementedException("TYPE_HTML not yet implemented"); //$NON-NLS-1$
		case SCHED_LIST:
			// TODO implement
			throw new NotImplementedException("TYPE_SCHED_LIST not yet implemented"); //$NON-NLS-1$
		case CALENDAR_FORMAT:
			// TODO implement
			// This will be in canonical format
			throw new NotImplementedException("TYPE_CALENDAR_FORMAT not yet implemented"); //$NON-NLS-1$
		case MIME_PART: {
			// This SHOULD be in canonical format, but ODSReadMemory causes problems
//			pValuePtr = C.malloc(C.sizeOfPOINTER);
//			try {
//				C.setPointer(pValuePtr, 0, C.ptrAdd(valuePtr, C.sizeOfWORD));
//				MIME_PART result = new MIME_PART();
//				api.ODSReadMemory(pValuePtr, _MIME_PART, result.getDataPtr(), (short)1);
//				return result;
//			} finally {
//				C.free(pValuePtr);
//			}
			long resultPtr = C.malloc(size - C.sizeOfWORD);
			MIME_PART result = new MIME_PART(resultPtr, true);
			C.memcpy(resultPtr, 0, valuePtr, C.sizeOfWORD, size - C.sizeOfWORD);
			return result;
		}
		case RFC822_TEXT:
			// TODO implement
			// This will be in canonical format
			throw new NotImplementedException("TYPE_RFC822_TEXT not yet implemented"); //$NON-NLS-1$
		}
		// We won't get here, but Java insists
		throw new DominoException(null, "Unhandled type {0}", type);
	}
	public static Object[] readItemValueArray(DominoAPI api, long valuePtr, int size, long hNote) throws DominoException {
		Object result = readItemValue(api, valuePtr, size, hNote);
		return toObjectArray(result);
	}
	public static Object[] toObjectArray(Object value) {
		if(value == null) {
			return new Object[0];
		} else if(value.getClass().isArray()) {
			Class<?> componentType = value.getClass().getComponentType();
			if(componentType.isPrimitive()) {
				int length = Array.getLength(value);
				Object[] wrapped = new Object[length];
				for(int i = 0; i < length; i++) {
					wrapped[i] = Array.get(value, i);
				}
				return wrapped;
			} else {
				return (Object[])value;
			}
		} else {
			return new Object[] { value };
		}
	}
	
	public static <T> T coerceValue(Object[] value, Class<T> clazz) {
		if(value == null) {
			return null;
		} else if(value.length == 0) {
			return null;
		} else if(clazz.isArray()) {
			Object result = Array.newInstance(clazz.getComponentType(), value.length);
			for(int i = 0; i < value.length; i++) {
				Array.set(result, i, coerceValue(value[i], clazz.getComponentType()));
			}
			return clazz.cast(result);
		} else if(value.length == 1 || !clazz.isArray()) {
			return coerceValue(value[0], clazz);
		} else {
			throw new UnsupportedOperationException(StringUtil.format("Could not coerce value to type {0}", clazz.getName())); //$NON-NLS-1$
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T coerceValue(Object value, Class<T> clazz) {
		if(value == null) {
			return null;
		} else if(clazz.isAssignableFrom(value.getClass())) {
			return clazz.cast(value);
		} else if(value instanceof Number) {
			// Special handling for numbers
			if(Byte.class.equals(clazz)) {
				return (T)(Byte)((Number)value).byteValue();
			} else if(Short.class.equals(clazz)) {
				return (T)(Short)((Number)value).shortValue();
			} else if(Integer.class.equals(clazz)) {
				return (T)(Integer)((Number)value).intValue();
			} else if(Long.class.equals(clazz)) {
				return (T)(Long)((Number)value).longValue();
			} else if(Float.class.equals(clazz)) {
				return (T)(Float)((Number)value).floatValue();
			} else if(Double.class.equals(clazz)) {
				return (T)(Double)((Number)value).doubleValue();
			} else {
				throw new ClassCastException(StringUtil.format("Object of class \"{0}\" cannot be cast to \"{1}\"", value.getClass().getName(), clazz.getName())); //$NON-NLS-1$
			}
		} else if(value instanceof NSFDateTime && Date.class.equals(clazz)) {
			return (T)((NSFDateTime)value).toDate();
		} else if(value instanceof NSFDateTime && Calendar.class.equals(clazz)) {
			return (T)((NSFDateTime)value).toCalendar();
		} else {
			if(NAPI_LOG.isInfoEnabled()) {
				NAPI_LOG.info("Object of class \"{0}\" cannot be cast to \"{1}\"", value.getClass().getName(), clazz.getName()); //$NON-NLS-1$
			}
			return null;
		}
	}
	
	/**
	 * Escapes the provided value in a way suitable for inclusion in place of a string in a Notes formula.
	 * 
	 * <p>For example: <code>StringUtil.format("foo={0}", quoteForFormulaString("bar"))</code> will result in a
	 * formula of <code>foo={bar}</code></p>
	 * 
	 * @param value the String value to escape
	 * @return the provided value escaped and quoted for insertion into a Notes formula
	 */
	public static String quoteForFormulaString(final String value) {
		// I wonder if this is sufficient escaping
		return "{" + value.replace("{", "\\{").replace("}", "\\}") + "}"; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$//$NON-NLS-6$
	}
	
	/**
	 * Decompiles a given formula and returns its text as a {@link String}.
	 * 
	 * @param api the {@link DominoAPI} instance to use for decompilation
	 * @param formulaData the compiled formula data
	 * @param selectionFormula whether the provided formula should be treated as a selection formula
	 * @return the decompiled formula as a <code>String</code>
	 */
	public static String decompileFormula(DominoAPI api, byte[] formulaData, boolean selectionFormula) throws DominoException {
		if(formulaData == null || formulaData.length == 0) {
			return StringUtil.EMPTY_STRING;
		}
		
		long pFormulaBuffer = C.malloc(formulaData.length);
		try {
			C.writeByteArray(pFormulaBuffer, 0, formulaData, 0, formulaData.length);
			return DominoAPI.get().NSFFormulaDecompile(pFormulaBuffer, selectionFormula);
		} finally {
			C.free(pFormulaBuffer);
		}
	}
	
	/**
	 * Returns a {@link List} of the names and aliases in the provided array. This checks each element
	 * for the "|" and splits it into multiple elements in the result if needed. It also filters out
	 * empty name values.
	 */
	public static List<String> splitDesignNames(String[] names) {
		if(names == null || names.length == 0) {
			return Collections.emptyList();
		}
		
		List<String> result = new ArrayList<String>();
		for(String name : names) {
			if(StringUtil.isNotEmpty(name)) {
				String[] bits = StringUtil.splitString(name, '|');
				for(String bit : bits) {
					if(StringUtil.isNotEmpty(bit)) {
						result.add(bit);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Frees any {@link BaseStruct} and {@link NSFBase} objects in the provided array.
	 */
	public static void free(Object... objects) {
		if(objects != null) {
			for(Object obj : objects) {
				if(obj instanceof NSFBase) {
					((NSFBase)obj).free();
				} else if(obj instanceof BaseStruct) {
					((BaseStruct)obj).free();
				}
			}
		}
	}
	
	/**
	 * @param flags a design flag value to test
	 * @param pattern a flag pattern to test against (DFLAGPAT_*)
	 * @return whether the flags match the pattern
	 */
	public static boolean matchesFlagsPattern(String flags, String pattern) {
		if(StringUtil.isEmpty(pattern)) {
			return false;
		}
		
		String toTest = flags == null ? StringUtil.EMPTY_STRING : flags;
		
		// Patterns start with one of four characters:
		// "+" (match any)
		// "-" (match none)
		// "*" (match all)
		// "(" (multi-part test)
		String matchers = null;
		String antiMatchers = null;
		String allMatchers = null;
		char first = pattern.charAt(0);
		switch(first) {
		case '+':
			matchers = pattern.substring(1);
			antiMatchers = StringUtil.EMPTY_STRING;
			allMatchers = StringUtil.EMPTY_STRING;
			break;
		case '-':
			matchers = StringUtil.EMPTY_STRING;
			antiMatchers = pattern.substring(1);
			allMatchers = StringUtil.EMPTY_STRING;
			break;
		case '*':
			matchers = StringUtil.EMPTY_STRING;
			antiMatchers = StringUtil.EMPTY_STRING;
			allMatchers = pattern.substring(1);
		case '(':
			// The order is always +-*
			int plusIndex = pattern.indexOf('+');
			int minusIndex = pattern.indexOf('-');
			int starIndex = pattern.indexOf('*');
			
			matchers = pattern.substring(plusIndex+1, minusIndex == -1 ? pattern.length() : minusIndex);
			antiMatchers = minusIndex == -1 ? StringUtil.EMPTY_STRING : pattern.substring(minusIndex+1, starIndex == -1 ? pattern.length() : starIndex);
			allMatchers = starIndex == -1 ? StringUtil.EMPTY_STRING : pattern.substring(starIndex+1);
			break;
		}
		if(matchers == null) { matchers = StringUtil.EMPTY_STRING; }
		if(antiMatchers == null) { antiMatchers = StringUtil.EMPTY_STRING; }
		if(allMatchers == null) { allMatchers = StringUtil.EMPTY_STRING; }
		
		// Test "match against any" and fail if it doesn't
		boolean matchedAny = matchers.isEmpty();
		for(int i = 0; i < matchers.length(); i++) {
			if(toTest.indexOf(matchers.charAt(i)) > -1) {
				matchedAny = true;
				break;
			}
		}
		if(!matchedAny) {
			return false;
		}
		
		// Test "match none" and fail if it does
		for(int i = 0; i < antiMatchers.length(); i++) {
			if(toTest.indexOf(antiMatchers.charAt(i)) > -1) {
				// Exit immediately
				return false;
			}
		}
		
		// Test "match all" and fail if it doesn't
		for(int i = 0; i < allMatchers.length(); i++) {
			if(toTest.indexOf(allMatchers.charAt(i)) == -1) {
				// Exit immediately
				return false;
			}
		}
		
		// If we survived to here, it must match
		return true;
	}
	
	/**
	 * Converts the two provided {@link TIMEDATE}s (typically the "File" and "Note" components
	 * of an OID) to a hex string UNID.
	 * @param td1 the first component ("File")
	 * @param td2 the second component ("Note")
	 * @return a hex UNID string
	 */
	public static String toUnid(TIMEDATE td1, TIMEDATE td2) {
		StringBuilder b = new StringBuilder();
		appendHexInt8(b, td1.getInnards(1));
		appendHexInt8(b, td1.getInnards(0));
		appendHexInt8(b, td2.getInnards(1));
		appendHexInt8(b, td2.getInnards(0));
		return b.toString();
	}
	
	/**
	 * Converts the two provided {@link NSFDateTime}s (typically the "File" and "Note" components
	 * of an OID) to a hex string UNID.
	 * @param dt1 the first component ("File")
	 * @param dt2 the second component ("Note")
	 * @return a hex UNID string
	 */
	public static String toUnid(NSFDateTime dt1, NSFDateTime dt2) {
		StringBuilder b = new StringBuilder();
		appendHexInt8(b, dt1.getInnards2());
		appendHexInt8(b, dt1.getInnards1());
		appendHexInt8(b, dt2.getInnards2());
		appendHexInt8(b, dt2.getInnards1());
		return b.toString();
	}
	
	private static void appendHexInt8(StringBuilder b, int value) {
		for (int i = 7; i >= 0; i--) {
			int v = (value >>> (i * 4)) & 0x0F;
	        char hexChar = (char)((v>=10) ? (v-10+'A') : (v+'0'));
			b.append(hexChar);
		}
	}
	
	/**
	 * @param noteId the note ID to check
	 * @return whether the note ID indicates that it was deleted
	 */
	public static boolean isDeletedId(int noteId) {
		return (noteId & DominoAPI.RRV_DELETED) != 0;
	}
	
	/**
	 * @param noteId the note ID to clean
	 * @return the noteId with any deletion info masked out
	 */
	public static int toNonDeletedId(int noteId) {
		if(isDeletedId(noteId)) {
			return noteId & ~DominoAPI.RRV_DELETED;
		} else {
			return noteId;
		}
	}
	
	public static Object[] concatNSFItemValues(Collection<NSFItem> items) throws DominoException {
		if(items == null) {
			return new Object[0];
		}
		switch(items.size()) {
		case 0:
			return new Object[0];
		case 1:
			return items.iterator().next().getValue();
		default:
			List<Object> resultList = new ArrayList<Object>();
			for(NSFItem item : items) {
				resultList.addAll(Arrays.asList(item.getValue()));
			}
			return resultList.toArray(new Object[resultList.size()]);
		}
	}
	
	public static <T> T concatNSFItemValues(Collection<NSFItem> items, Class<T> clazz) throws DominoException {
		if(items == null) {
			return null;
		}
		switch(items.size()) {
		case 0:
			return null;
		case 1:
			return items.iterator().next().getValue(clazz);
		default:
			List<Object> resultList = new ArrayList<Object>();
			for(NSFItem item : items) {
				resultList.addAll(Arrays.asList(item.getValue()));
			}
			Object[] results = resultList.toArray(new Object[resultList.size()]);
			return DominoNativeUtils.coerceValue(results, clazz);
		}
	}
	
	/**
	 * This method attempts to wrap a native struct object with its wrapper class equivalent,
	 * e.g. {@link TIMEDTAE} &rarr; {@link NSFDateTime}.
	 * 
	 * <p>If the provided object is not a {@link BaseStruct} or does not have a matching wrapper,
	 * it is returned as-is.</p>
	 * 
	 * @param nativeStruct the native struct object to wrap
	 * @param freeOriginal whether the original struct should be freed when a reference-less wrapper is found
	 * @return a wrapped version of the provided object, or the original object if no wrapper applies
	 * @throws DominoException 
	 */
	public static Object wrapNativeStruct(Object nativeStruct, boolean freeOriginal) throws DominoException {
		if(!(nativeStruct instanceof BaseStruct)) {
			return nativeStruct;
		}
		
		if(nativeStruct instanceof UNIVERSALNOTEID) {
			String unid = ((UNIVERSALNOTEID) nativeStruct).getUNID();
			if(freeOriginal) {
				((UNIVERSALNOTEID)nativeStruct).free();
			}
			return unid;
		} else if(nativeStruct instanceof TIMEDATE) {
			NSFDateTime dt = new NSFDateTime((TIMEDATE)nativeStruct);
			if(freeOriginal) {
				((TIMEDATE)nativeStruct).free();
			}
			return dt;
		} else if(nativeStruct instanceof TIMEDATE_PAIR) {
			NSFDateRange dr = new NSFDateRange((TIMEDATE_PAIR)nativeStruct);
			if(freeOriginal) {
				((TIMEDATE_PAIR)nativeStruct).free();
			}
			return dr;
		} else {
			return nativeStruct;
		}
	}
	
	/**
	 * This method attempts to wrap an array of native struct objects with their wrapper class equivalents,
	 * e.g. {@link TIMEDTAE} &rarr; {@link NSFDateTime}.
	 * 
	 * <p>If the provided objects are not {@link BaseStruct}s or do not have a matching wrapper,
	 * they are returned as-is. Each individual element is checked and treated this way.</p>
	 * 
	 * @param nativeStructs the array of native struct objects to wrap
	 * @param freeOriginal whether the original struct should be freed when a reference-less wrapper is found
	 * @return a wrapped version of the provided objects, or the original objects if no wrapper applies
	 * @throws DominoException 
	 */
	public static Object[] wrapNativeStructs(Object[] nativeStructs, boolean freeOriginal) throws DominoException {
		if(nativeStructs == null) {
			return null;
		}
		Object[] result = new Object[nativeStructs.length];
		for(int i = 0; i < nativeStructs.length; i++) {
			result[i] = wrapNativeStruct(nativeStructs[i], freeOriginal);
		}
		return result;
	}
	
	private static final ThreadLocal<Map<TimeZone, Calendar>> REUSABLE_CALENDARS = new ThreadLocal<Map<TimeZone, Calendar>>() {
		@Override protected java.util.Map<TimeZone,Calendar> initialValue() {
			return new HashMap<TimeZone, Calendar>();
		}
	};
	
	/**
	 * Returns a {@link Calendar} instance for the specified {@link TimeZone}. This
	 * differs from {@link Calendar#getInstance()} in that it uses a thread-local pool
	 * of existing calendar objects for efficiency. Accordingly, this should only be used
	 * when the calendar is being used for temporary processing, not kept around or
	 * returned to a caller.
	 * 
	 * @param timeZone the time zone to get the calendar for
	 * @param initTime whether the calendar should be re-initialized to the current time
	 * @return a thread-local reusable <code>Calendar</code> instance
	 */
	public static Calendar getReusableCalendarInstance(TimeZone timeZone, boolean initTime) {
		Map<TimeZone, Calendar> calendars = REUSABLE_CALENDARS.get();
		Calendar cal;
		if(!calendars.containsKey(timeZone)) {
			cal = Calendar.getInstance(timeZone);
			calendars.put(timeZone, cal);
		} else {
			cal = calendars.get(timeZone);
		}
		if(initTime) {
			cal.setTimeInMillis(System.currentTimeMillis());
		}
		return cal;
	}
	
	private static final Map<Integer, String[]> AVAILABLE_TZ_IDS = new HashMap<Integer, String[]>();
	private static final Map<Integer, TimeZone> KNOWN_TZS = new HashMap<Integer, TimeZone>();
	public static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("GMT"); //$NON-NLS-1$
	
	public static synchronized TimeZone getTimeZoneForOffset(int offset) {
		if(!KNOWN_TZS.containsKey(offset)) {
			String[] zones;
			if(!AVAILABLE_TZ_IDS.containsKey(offset)) {
				String[] result = TimeZone.getAvailableIDs(offset);
				AVAILABLE_TZ_IDS.put(offset, result);
				zones = result;
			} else {
				zones = AVAILABLE_TZ_IDS.get(offset);
			}
			
			TimeZone zone;
			if(zones == null || zones.length < 1) {
				if(log.isWarnEnabled()) {
					log.warn("{0}: Could not find time zone for offset {1}; defaulting to UTC with manual offset", NSFDateTime.class.getName(), offset); //$NON-NLS-1$
				}
				zone = TIMEZONE_UTC;
			} else {
				zone = TimeZone.getTimeZone(zones[0]);
			}
			
			KNOWN_TZS.put(offset, zone);
			return zone;
		} else {
			return KNOWN_TZS.get(offset);
		}
	}
	
	public static void loadLibraryFromJar(ClassLoader cl, String path) throws IOException {
		int pos = path.lastIndexOf('/');
		String name = pos >= 0 ? path.substring(pos + 1) : path;

		File tempFile;
		int dot = name.lastIndexOf('.');
		if (dot >= 0) {
			tempFile = File.createTempFile(name.substring(0, dot), name.substring(dot));
		} else {
			tempFile = File.createTempFile(name, null);
		}
		if (!tempFile.exists()) {
			throw new FileNotFoundException(StringUtil.format("Cannot create temporary file {0}", tempFile.getAbsolutePath()));
		}
		tempFile.deleteOnExit();

		@SuppressWarnings("resource")
		InputStream is = cl.getResourceAsStream(path);
		if (is == null) {
			throw new FileNotFoundException(StringUtil.format("Cannot find native library {0}", path));
		}
		try {
			// Open output stream and copy data between source file in JAR and
			// the temporary file
			@SuppressWarnings("resource")
			OutputStream os = new FileOutputStream(tempFile);
			try {
				StreamUtil.copyStream(is, os);
			} finally {
				StreamUtil.close(os);
			}
		} finally {
			StreamUtil.close(is);
		}

		// Ok, load the library from the temporary file
		System.load(tempFile.getAbsolutePath());
	}
}
