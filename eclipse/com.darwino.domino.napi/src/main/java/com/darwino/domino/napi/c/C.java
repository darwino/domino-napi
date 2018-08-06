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

package com.darwino.domino.napi.c;

import java.io.OutputStream;
import java.io.PrintStream;

import com.ibm.commons.Platform;



/**
 * Basic "C" API utilities.
 * 
 * @author priand
 */
public class C {
	
	private C() {}

	static {
		try {
			// Call the initialization routine
			int[] sizes = new int[27];
			initSizes(sizes);
			
			// Store the different type sizes
			sizeOfBYTE		  = sizes[1];
			sizeOfWORD		  = sizes[2];
			sizeOfSWORD		  = sizes[3];
			sizeOfDWORD		  = sizes[4];
			sizeOfLONG		  = sizes[5];
			sizeOfUSHORT	  = sizes[6];
			sizeOfBOOL		  = sizes[7];
			sizeOfShort		  = sizes[8];
			sizeOfInt		  = sizes[9];
			sizeOfLong		  = sizes[10];
			sizeOfDHANDLE	  = sizes[11];
			sizeOfPOINTER	  = sizes[12];
			sizeOfNUMBER	  = sizes[13];
			sizeOfNOTEID	  = sizes[14];
			sizeOfChar        = sizes[15];
			sizeOfMEMHANDLE   = sizes[16];
			sizeOfHCOLLECTION = sizes[17];
			sizeOfNOTEHANDLE  = sizes[18];
			sizeOfHANDLE      = sizes[19];
			sizeOfDBHANDLE    = sizes[20];
			sizeOfDouble      = sizes[21];
			sizeOfFONTID      = sizes[22];
			sizeOfHTMLAPI_REF_TYPE = sizes[23];
			sizeOfCmdArgValueType = sizes[24];
			sizeOfCmdArgID = sizes[25];
			sizeOfLICENSEID = sizes[26];
		} catch(Throwable ex) {
			Platform.getInstance().log(ex);
		}
	}
	private static final native void initSizes(int[] sizes);
	
	
	//////////////////////////////////////////////////////////////////////////
	// Some statically initialized fields
	//////////////////////////////////////////////////////////////////////////

	// Machine-independent sizes
	public static int sizeOfBYTE;
	public static int sizeOfWORD;
	public static int sizeOfSWORD;
	public static int sizeOfDWORD;
	public static int sizeOfLONG;
	public static int sizeOfUSHORT;
	public static int sizeOfDouble;
	
	// Machine-dependent sizes
	public static int sizeOfBOOL;
	public static int sizeOfShort;
	public static int sizeOfInt;	
	public static int sizeOfLong;
	public static int sizeOfChar;
	
	public static int sizeOfDHANDLE;
	public static int sizeOfPOINTER;
	public static int sizeOfNUMBER;
	public static int sizeOfNOTEID;
	public static int sizeOfMEMHANDLE;
	
	public static int sizeOfHCOLLECTION;
	public static int sizeOfNOTEHANDLE;
	public static int sizeOfHANDLE;
	public static int sizeOfDBHANDLE;
	public static int sizeOfFONTID;
	public static int sizeOfLICENSEID;
	
	// Enum sizes (machine/compiler-dependent)
	public static int sizeOfHTMLAPI_REF_TYPE;
	public static int sizeOfCmdArgValueType;
	public static int sizeOfCmdArgID;
	
	//////////////////////////////////////////////////////////////////////////
	// Memory allocation.
	//////////////////////////////////////////////////////////////////////////

	public static final native /*pointer*/long malloc(int size);
	public static final native /*pointer*/long calloc(int count, int size);
	public static final native void free(/*pointer*/long ptr);
	

	//////////////////////////////////////////////////////////////////////////
	// Pointer arithmetic
	//////////////////////////////////////////////////////////////////////////

	public static final native /*pointer*/long ptrAdd(/*pointer*/long ptr, int offset);
	public static final long ptrAdd(long ptr, short offset) { return ptrAdd(ptr, offset & 0xFFFF); }
	public static final long ptrAdd(/*pointer*/long ptr, byte offset) { return ptrAdd(ptr, offset & 0xFF); }
	
	public static final native /*pointer*/long ptrSub(/*pointer*/long ptr, int offset);
	public static final long ptrSub(long ptr, short offset) { return ptrSub(ptr, offset & 0xFFFF); }
	public static final long ptrSub(long ptr, byte offset) { return ptrSub(ptr, offset & 0xFF); }
	
	
	
	//////////////////////////////////////////////////////////////////////////
	// Memory buffer access
	//////////////////////////////////////////////////////////////////////////

	public static final native void memset(/*pointer*/long buffer, int offset, byte value, int size);
	public static final native void memcpy(/*pointer*/long dest, int offdest,/*pointer*/long src, int offsrc, int size);


	public static final native byte getBYTE(/*pointer*/long ptr, int offset);
	public static final native short getWORD(/*pointer*/long ptr, int offset);
	public static final native short getSWORD(/*pointer*/long ptr, int offset);
	public static final native int getDWORD(/*pointer*/long ptr, int offset);
	public static final native int getLONG(/*pointer*/long ptr, int offset);
	public static final native short getUSHORT(/*pointer*/long ptr, int offset);
	public static final native double getNUMBER(/*pointer*/long ptr, int offset);
	public static final native int getFONTID(/*pointer*/long ptr, int offset);
	public static final native int getHTMLAPI_REF_TYPE(/*pointer*/long ptr, int offset);
	public static final native int getCmdArgValueType(/*pointer*/long ptr, int offset);
	public static final native int getCmdArgID(/*pointer*/long ptr, int offset);
	
	public static final native boolean getBOOL(/*pointer*/long ptr, int offset);
	
	public static final native byte getByte(/*pointer*/long ptr, int offset);
	public static final native short getShort(/*pointer*/long ptr, int offset);
	public static final native int getInt(/*pointer*/long ptr, int offset);
	public static final native long getLong(/*pointer*/long ptr, int offset);
	public static final native float getFloat(/*pointer*/long ptr, int offset);
	public static final native double getDouble(/*pointer*/long ptr, int offset);

	public static final native long getHandle(/*pointer*/long ptr, int offset);
	public static final native long getDHandle(/*pointer*/long ptr, int offset);
	public static final native long getPointer(/*pointer*/long ptr, int offset);
	
	// Misc specific-type getters for explicitness
	public static final long getNOTEHANDLE(long ptr, int offset) { return getDHandle(ptr, offset); }
	public static final short getHCOLLECTION(long ptr, int offset) { return getWORD(ptr, offset); }
	public static final int getNOTEID(long ptr, int offset) { return getDWORD(ptr, offset); }
	
	/**
	 * Reads a null-terminated LMBCS string from the given memory location and returns it as a Unicode Java String.
	 */
	public static final native String getLMBCSString(/*pointer*/long ptr, int offset);
	/**
	 * Reads am LMBCS string of the given length from the given memory location and returns it as a Unicode Java String.
	 */
	public static final native String getLMBCSString(/*pointer*/long ptr, int offset, int length);
	/**
	 * Converts a Java String to a null-terminated LMBCS string and returns a pointer to
	 * its start. The caller must free this memory when done.
	 */
	public static final native long toLMBCSString(String value);

	public static final native void setBYTE(/*pointer*/long ptr, int offset, byte value);
	public static final native void setWORD(/*pointer*/long ptr, int offset, short value);
	public static final native void setSWORD(/*pointer*/long ptr, int offset, short value);
	public static final native void setDWORD(/*pointer*/long ptr, int offset, int value);
	public static final native void setLONG(/*pointer*/long ptr, int offset, int value);
	public static final native void setUSHORT(/*pointer*/long ptr, int offset, short value);
	public static final native void setBOOL(/*pointer*/long ptr, int offset, boolean value);
	public static final native void setNUMBER(/*pointer*/long ptr, int offset, double value);
	public static final native void setFONTID(/*pointer*/long ptr, int offset, int value);
	public static final native void setHTMLAPI_REF_TYPE(long ptr, int offset, int value);
	
	public static final native void setByte(/*pointer*/long ptr, int offset, byte value);
	public static final native void setShort(/*pointer*/long ptr, int offset, short value);
	public static final native void setInt(/*pointer*/long ptr, int offset, int value);
	public static final native void setLong(/*pointer*/long ptr, int offset, long value);
	public static final native void setFloat(/*pointer*/long ptr, int offset, float value);
	public static final native void setDouble(/*pointer*/long ptr, int offset, double value);
	
	public static final native void setDHandle(/*pointer*/long ptr, int offset, long value);
	public static final native void setHandle(/*pointer*/long ptr, int offset, long value);
	public static final native void setPointer(/*pointer*/long ptr, int offset, long value);
	
	// Misc specific-type setters for explicitness
	public static final void setNOTEHANDLE(long ptr, int offset, long value) { setDHandle(ptr, offset, value); }
	public static final void setHCOLLECTION(long ptr, int offset, short value) { setWORD(ptr, offset, value); }
	public static final void setNOTEID(long ptr, int offset, int value) { setDWORD(ptr, offset, value); }

	
	public static final native void writeByteArray(/*pointer*/long dest, int offset, byte[] src, int offsrc, int size);
	public static final native void readByteArray(byte[] dest, int offdest, /*pointer*/long src, int offset, int size);
	
	public static final native void writeShortArray(/*pointer*/long dest, int offset, short[] src, int offsrc, int size);
	public static final native void readShortArray(short[] dest, int offdest, /*pointer*/long src, int offset, int size);
	
	public static final native void writeIntArray(/*pointer*/long dest, int offset, int[] src, int offsrc, int size);
	public static final native void readIntArray(int[] dest, int offdest, /*pointer*/long src, int offset, int size);
	
	public static final native void writeLongArray(/*pointer*/long dest, int offset, long[] src, int offsrc, int size);
	public static final native void readLongArray(long[] dest, int offdest, /*pointer*/long src, int offset, int size);
	
	public static final native void writeFloatArray(/*pointer*/long dest, int offset, float[] src, int offsrc, int size);
	public static final native void readFloatArray(float[] dest, int offdest, /*pointer*/long src, int offset, int size);
	
	public static final native void writeDoubleArray(/*pointer*/long dest, int offset, double[] src, int offsrc, int size);
	public static final native void readDoubleArray(double[] dest, int offdest, /*pointer*/long src, int offset, int size);
	
	public static final native void writePtrArray(/*pointer*/long dest, int offset, long[] src, int offsrc, int size);
	public static final native void readPtrArray(long[] dest, int offdest, /*pointer*/long src, int offset, int size);

	
	//////////////////////////////////////////////////////////////////////////
	// String functions
	//////////////////////////////////////////////////////////////////////////
	
	public static final native int strlen(/*pointer*/long ptr, int offset);
	public static final native void strcpy(/*pointer*/long ptr, int offdest, /*pointer*/long src, int offsrc);
	

	//////////////////////////////////////////////////////////////////////////
	// Debug utilities
	//////////////////////////////////////////////////////////////////////////
	
	public static final void dump(OutputStream os, /*pointer*/long ptr, int offset, int length) {
		if(os==null) {
			os = System.out;
		}
		PrintStream ps = (os instanceof PrintStream) ? (PrintStream)os : new PrintStream(os);
		int lineLength = 16;
		for(int i=offset; i<offset+length; i+=lineLength) {
			StringBuilder b = new StringBuilder();
			String hx = Integer.toHexString(i);
			b.append("0000".substring(0,4-hx.length())); //$NON-NLS-1$
			b.append(hx);
			b.append(":"); //$NON-NLS-1$
			for( int j=i; j<(i+16) && j<(offset+length); j++ ) {
				int v = getByte(ptr, j) & 0xFF;
				String s = Integer.toHexString(v);
				b.append("00".substring(0,2-s.length())); //$NON-NLS-1$
				b.append(s);
				b.append(" "); //$NON-NLS-1$
			}
			for( int j=i; j<(i+lineLength) && j<(offset+length); j++ ) {
				int v = getByte(ptr, j) & 0xFF;
				if(Character.isDefined(v) && v!=13 && v!=10) {
					b.append((char)v);
				} else {
					b.append("."); //$NON-NLS-1$
				}
			}
			ps.println(b.toString());
		}
	}
}
