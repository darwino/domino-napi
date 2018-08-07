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

import java.util.Collection;
import java.util.Set;

import com.ibm.commons.util.StringUtil;
import com.darwino.domino.napi.DominoAPI;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.FileObjectAttribute;
import com.darwino.domino.napi.enums.FileObjectCompress;
import com.darwino.domino.napi.enums.FileObjectFlag;
import com.darwino.domino.napi.enums.FileObjectHost;

/**
 * (nsfdata.h)
 * 
 * @author Jesse Gallagher
 *
 */
public class FILEOBJECT extends BaseStruct {
	static {
		int[] sizes = new int[10];
		initNative(sizes);
		sizeOf = sizes[0];
		_Header = sizes[1];
		_FileNameLength = sizes[2];
		_HostType = sizes[3];
		_CompressionType = sizes[4];
		_FileAttributes = sizes[5];
		_Flags = sizes[6];
		_FileSize = sizes[7];
		_FileCreated = sizes[8];
		_FileModified = sizes[9];
	}
	private static final native void initNative(int[] sizes);
	
	// Statically initialized fields
	public static final int sizeOf;
	public static final int _Header;
	public static final int _FileNameLength;
	public static final int _HostType;
	public static final int _CompressionType;
	public static final int _FileAttributes;
	public static final int _Flags;
	public static final int _FileSize;
	public static final int _FileCreated;
	public static final int _FileModified;
	
	// Structure-related fields stored internally due to canonical format
	private String fileName;
	
	public FILEOBJECT() {
		super(C.malloc(sizeOf), true);
	}
	public FILEOBJECT(long data) {
		super(data, false);
	}

	public FILEOBJECT(long data, boolean owned) {
		super(data, owned);
	}

	/* ******************************************************************************
	 * Struct field getters/setters
	 ********************************************************************************/
	
	public OBJECT_DESCRIPTOR getHeader() {
		_checkRefValidity();
		return new OBJECT_DESCRIPTOR(getField(_Header));
	}
	public void setHeader(OBJECT_DESCRIPTOR header) {
		_checkRefValidity();
		C.memcpy(data, _Header , header.data, 0, OBJECT_DESCRIPTOR.sizeOf);
	}
	
	public short getFileNameLength() { return _getWORD(_FileNameLength); }
	public void setFileNameLength(short fileNameLength) { _setWORD(_FileNameLength, fileNameLength); }
	
	/**
	 * @return the numeric value of the HostType field
	 */
	public short getHostTypeRaw() { return _getWORD(_HostType); }
	/**
	 * @param hostType the new numeric value for the HostType field
	 */
	public void setHostTypeRaw(short hostType) { _setWORD(_HostType, hostType); }
	
	public short getCompressionTypeRaw() { return _getWORD(_CompressionType); }
	public void setCompressionTypeRaw(short compressionType) { _setWORD(_CompressionType, compressionType); }
	
	public short getFileAttributesRaw() { return _getWORD(_FileAttributes); }
	public void setFileAttributesRaw(short fileAttributes) { _setWORD(_FileAttributes, fileAttributes); }
	
	public short getFlagsRaw() { return _getWORD(_Flags); }
	public void setFlagsRaw(short flags) { _setWORD(_Flags, flags); }
	
	public int getFileSize() { return _getDWORD(_FileSize); }
	public void setFileSize(int fileSize) { _setDWORD(_FileSize, fileSize); }
	
	public TIMEDATE getFileCreated() {
		return new TIMEDATE(getField(_FileCreated));
	}
	public void setFileCreated(TIMEDATE fileCreated) {
		_checkRefValidity();
		C.memcpy(data, _FileCreated, fileCreated.data, 0, TIMEDATE.sizeOf);
	}
	
	public TIMEDATE getFileModified() {
		return new TIMEDATE(getField(_FileModified));
	}
	public void setFileModified(TIMEDATE fileModified) {
		_checkRefValidity();
		C.memcpy(data, _FileModified, fileModified.data, 0, TIMEDATE.sizeOf);
	}

	/* ******************************************************************************
	 * Variable data
	 ********************************************************************************/
	
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * Sets the file name stored in this Java object.
	 * 
	 * <p>This is <strong>not</strong> stored in C-side memory, as <code>FILEOBJECT</code>
	 * structs must be read with <code>ODSReadMemory</code> and are no longer adjacent to
	 * their name strings.</p>
	 * 
	 * @param fileName the new file name
	 */
	public void setInternalFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/* ******************************************************************************
	 * Encapsulated getters/setters
	 ********************************************************************************/
	
	public FileObjectHost getHost() {
		short hostVal = (short)(getHostTypeRaw() & DominoAPI.HOST_MASK);
		return DominoEnumUtil.valueOf(FileObjectHost.class, hostVal);
	}
	
	public void setHost(FileObjectHost host) {
		// Clear out any existing host type value while retaining flags
		short unmaskedHost = (short)(getHostTypeRaw() & ~DominoAPI.HOST_MASK);
		short newValue = (short)(unmaskedHost | host.getValue());
		setHostTypeRaw(newValue);
	}
	
	public FileObjectCompress getCompressionType() {
		return DominoEnumUtil.valueOf(FileObjectCompress.class, getCompressionTypeRaw());
	}
	public void setCompressionType(FileObjectCompress compressionType) {
		setCompressionTypeRaw(compressionType.getValue());
	}

	public Set<FileObjectAttribute> getFileAttributes() {
		return DominoEnumUtil.valuesOf(FileObjectAttribute.class, getFileAttributesRaw());
	}
	public void setFileAttributes(Collection<FileObjectAttribute> fileAttributes) {
		setFileAttributesRaw(DominoEnumUtil.toBitField(FileObjectAttribute.class, fileAttributes));
	}
	
	public Set<FileObjectFlag> getFlags() {
		return DominoEnumUtil.valuesOf(FileObjectFlag.class, getFlagsRaw());
	}
	public void setFlags(Collection<FileObjectFlag> flags) {
		setFlagsRaw(DominoEnumUtil.toBitField(FileObjectFlag.class, flags));
	}
	
	/* ******************************************************************************
	 * Misc.
	 ********************************************************************************/
	
	@Override
	public String toString() {
		if(isRefValid()) {
			return StringUtil.format("[{0}: Header={1}, Host={2}, CompressionType={3}, FileAttributes={4}, Flags={5}, FileSize={6}, FileCreated={7}, FileModified={8}]", //$NON-NLS-1$
				getClass().getSimpleName(),
				getHeader(),
				getHost(),
				getCompressionType(),
				getFileAttributes(),
				getFlags(),
				getFileSize(),
				getFileCreated(),
				getFileModified()
			);
		} else {
			return super.toString();
		}
	}
}
