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

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.ContentDisposition;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;

import com.ibm.commons.log.LogMgr;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.StreamUtil;
import com.darwino.domino.napi.DominoException;
import com.darwino.domino.napi.c.C;
import com.darwino.domino.napi.enums.DominoEnumUtil;
import com.darwino.domino.napi.enums.MIMEPartFlag;
import com.darwino.domino.napi.enums.MIMEPartType;
import com.darwino.domino.napi.util.DominoNativeUtils;

/**
 * <p>Notes MIME_PART struct (mimeods.h)</p>
 * 
 * <p>This structure consists of the fixed portion accessed here as well as a data
 * portion, which follows it immediately in memory. Accordingly, any new-object allocation
 * will need to include both this structure and enough memory to house the actual data.</p>
 * 
 * @author Jesse Gallagher
 *
 */
public class MIME_PART extends BaseStruct {
	
	private static final LogMgr log = DominoNativeUtils.NAPI_LOG;

	// Statically initialized fields
	public static final int sizeOf;
	public static final int _wVersion;
	public static final int _dwFlags;
	public static final int _cPartType;
	public static final int _cSpare;
	public static final int _wByteCount;
	public static final int _wBoundaryLen;
	public static final int _wHeadersLen;
	public static final int _wSpare;
	public static final int _dwSpare;
	
	/**
	 * This is a virtual member representing the start of the post-struct variable data
	 */
	public static final int variableOffset;
	
	static {
		// For some reason, the values from initNative indicate an extra couple bytes
		// It's not ideal to have these values hard-coded, but it works
		sizeOf = 20;
		_wVersion = 0;
		_dwFlags = _wVersion + C.sizeOfWORD;
		_cPartType = _dwFlags + C.sizeOfDWORD;
		_cSpare = _cPartType + C.sizeOfBYTE;
		_wByteCount = _cSpare + C.sizeOfBYTE;
		_wBoundaryLen = _wByteCount + C.sizeOfWORD;
		_wHeadersLen = _wBoundaryLen + C.sizeOfWORD;
		_wSpare = _wHeadersLen + C.sizeOfWORD;
		_dwSpare = _wSpare + C.sizeOfWORD;
		variableOffset = _dwSpare + C.sizeOfDWORD;
//		int[] sizes = new int[10];
//		initNative(sizes);
//		sizeOf = sizes[0];
//		_wVersion = sizes[1];
//		_dwFlags = sizes[2];
//		_cPartType = sizes[3];
//		_cSpare = sizes[4];
//		_wByteCount = sizes[5];
//		_wBoundaryLen = sizes[6];
//		_wHeadersLen = sizes[7];
//		_wSpare = sizes[8];
//		_dwSpare = sizes[9];
//		
//		variableOffset = sizes[9] + C.sizeOfDWORD;
	}
	private static final native void initNative(int[] sizes);

	
	

	/**
	 * Because this structure also contains variable data afterward, the
	 * self-allocating structure will likely have little use.
	 */
	public MIME_PART() {
		super(C.malloc(sizeOf),true);
	}
	public MIME_PART(long data) {
		super(data, false);
	}
	public MIME_PART(long data, boolean owned) {
		super(data, owned);
	}
	
	/* ******************************************************************************
	 * Struct field getters/setters
	 ********************************************************************************/

	public short getVersion() { return _getWORD(_wVersion); }
	public void setVersion(short version) { _setWORD(_wVersion, version); }
	
	public int getFlagsRaw() { return _getDWORD(_dwFlags); }
	public void setFlagsRaw(int flags) { _setDWORD(_dwFlags, flags); }
	
	public byte getPartTypeRaw() { return _getBYTE(_cPartType); }
	public void setPartTypeRaw(byte partType) { _setBYTE(_cPartType, partType); }
	
	public short getByteCount() { return _getWORD(_wByteCount); }
	public void setByteCount(short byteCount) { _setWORD(_wByteCount, byteCount); }
	
	public short getBoundaryLen() { return _getWORD(_wBoundaryLen); }
	public void setBoundaryLen(short boundaryLen) { _setWORD(_wBoundaryLen, boundaryLen); }
	
	public short getHeadersLen() { return C.getWORD(data, _wHeadersLen); }
	public void setHeadersLen(short headersLen) { C.setWORD(data, _wHeadersLen, headersLen); }
	
	/* ******************************************************************************
	 * Variable data
	 ********************************************************************************/
	
	/**
	 * @return the MIME part's entire data (headers, boundary, and body) as a byte array
	 */
	public byte[] getMimeData() {
		_checkRefValidity();
		byte[] result = new byte[getByteCount() & 0xFFFF];
		C.readByteArray(result, 0, data, sizeOf, result.length);
		return result;
	}
	
	/**
	 * @return the MIME part's boundary as a String
	 */
	public String getBoundary() {
		_checkRefValidity();
		int boundaryLen = getBoundaryLen() & 0xFFFF;
		byte[] result = new byte[boundaryLen];
		C.readByteArray(result, 0, data, sizeOf, result.length);
		return new String(result, Charset.forName("US-ASCII")); //$NON-NLS-1$
	}
	
	/**
	 * @return the MIME part's headers as a String
	 */
	public String getHeaders() {
		byte[] result = getHeaderData();
		return new String(result, Charset.forName("US-ASCII")); //$NON-NLS-1$
	}
	
	public byte[] getHeaderData() {
		_checkRefValidity();
		int headersLen = getHeadersLen() & 0xFFFF;
		int boundaryLen = getBoundaryLen() & 0xFFFF;
		byte[] result = new byte[headersLen];
		C.readByteArray(result, 0, data, sizeOf + boundaryLen, result.length);
		return result;
	}
	
	/**
	 * @return the MIME part's body content as a Unicode String, with the original storage
	 * 			treated as LMBCS
	 */
	public String getHeadersLMBCS() {
		_checkRefValidity();
		int headersLen = getHeadersLen() & 0xFFFF;
		int boundaryLen = getBoundaryLen() & 0xFFFF;
		return C.getLMBCSString(data, sizeOf + boundaryLen, headersLen);
	}
	
	/**
	 * @return the MIME part's body content as a byte array
	 */
	public byte[] getBodyData() {
		_checkRefValidity();
		int headersLen = getHeadersLen() & 0xFFFF;
		int boundaryLen = getBoundaryLen() & 0xFFFF;
		int bodyLen = (getByteCount() & 0xFFFF) - headersLen - boundaryLen;
		byte[] result = new byte[bodyLen];
		C.readByteArray(result, 0, data, sizeOf + headersLen + boundaryLen, result.length);
		return result;
	}
	
	/**
	 * @return the MIME part's body content as a Unicode String, with the original storage
	 * 			treated as LMBCS
	 */
	public String getBodyLMBCS() {
		_checkRefValidity();
		int headersLen = getHeadersLen() & 0xFFFF;
		int boundaryLen = getBoundaryLen() & 0xFFFF;
		int bodyLen = (getByteCount() & 0xFFFF) - headersLen - boundaryLen;
		return C.getLMBCSString(data, sizeOf + headersLen + boundaryLen, bodyLen);
	}
	
	/* ******************************************************************************
	 * Encapsulated getters/setters
	 ********************************************************************************/
	
	public Set<MIMEPartFlag> getFlags() {
		return DominoEnumUtil.valuesOf(MIMEPartFlag.class, getFlagsRaw());
	}
	public void setFlags(Collection<MIMEPartFlag> flags) {
		setFlagsRaw(DominoEnumUtil.toBitField(MIMEPartFlag.class, flags));
	}
	
	public MIMEPartType getPartType() {
		return DominoEnumUtil.valueOf(MIMEPartType.class, getPartTypeRaw());
	}
	public void setPartType(MIMEPartType partType) {
		setPartTypeRaw(partType.getValue());
	}
	
	public MimeBodyPart toMimeBodyPart() throws MessagingException, DominoException {
		InternetHeaders headers = getInternetHeaders();
		
		byte[] body = getBodyData();
		
		return new MimeBodyPart(headers, body);
	}
	
	public InternetHeaders getInternetHeaders() throws DominoException {
		try {
			// Domino doesn't ASCII-encode its headers, so we have to do that here
			String headers = getHeadersLMBCS().replace("\r\n", "\n").replace('\r', '\n'); //$NON-NLS-1$ //$NON-NLS-2$
			
			String workingHeaders = headers;
			StringBuilder headersEnc = new StringBuilder();
			int lfIndex = workingHeaders.indexOf('\n');
			int looper = 0;
			while(lfIndex > -1) {
				int nextChar = lfIndex < workingHeaders.length()-1 ? workingHeaders.codePointAt(lfIndex+1) : 0;
				if(nextChar == 0 || !Character.isWhitespace(nextChar)) {
					// Then it's a real break
					String headerLine = workingHeaders.substring(0, lfIndex);
					int colonIndex = headerLine.indexOf(": "); //$NON-NLS-1$
					if(colonIndex > -1) {
						// Encode the right side
						String headerVal = MimeUtility.encodeText(unfold(headerLine.substring(colonIndex+2)));
						headerLine = headerLine.substring(0, colonIndex) + ": " + headerVal; //$NON-NLS-1$
					}
					headersEnc.append(headerLine);
					headersEnc.append('\n');
					workingHeaders = workingHeaders.substring(lfIndex+1);
					lfIndex = -1;
				}
				
				lfIndex = workingHeaders.indexOf('\n', lfIndex+1);
				if(looper++ > 1000) {
					throw new RuntimeException("Probable infinite loop detected"); //$NON-NLS-1$
				}
			}
			
			ByteArrayInputStream bais = new ByteArrayInputStream(headersEnc.toString().getBytes("US-ASCII")); //$NON-NLS-1$
			InternetHeaders result = new InternetHeaders(bais);
			StreamUtil.close(bais);
			return result;
		} catch(UnsupportedEncodingException e) {
			throw new DominoException(e);
		} catch(MessagingException e) {
			throw new DominoException(e);
		}
	}
	
	/**
	 * Performs a basic "unfold" of MIME header values - essentially, just stripping the newline+whitespace combo
	 */
	private static String unfold(String val) {
		if(StringUtil.isEmpty(val)) {
			return val;
		} else {
			return val.replaceAll("\\n\\s+", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * @return the value of the Content-Type header, as a {@link ContentType} object, or <code>null</code>
	 * 			if there is no such header
	 * @throws DominoException if there is a problem determining the content type header. This
	 * 			may wrap a {@link MessagingException}
	 */
	public ContentType getContentType() throws DominoException {
		try {
			InternetHeaders headers = getInternetHeaders();
			
			String[] headerVals = headers.getHeader("Content-Type"); //$NON-NLS-1$
			if(headerVals == null || headerVals.length < 1) { return null; }
			
			return new ContentType(MimeUtility.decodeText(headerVals[0]));
		} catch(MessagingException e) {
			throw new DominoException(e);
		} catch(UnsupportedEncodingException e) {
			throw new DominoException(e);
		}
	}
	
	/**
	 * @return the value of the Content-Disposition header, as a {@link ContentDisposition} object, or <code>null</code>
	 * 			if there is no such header
	 * @throws DominoException if there is a problem determining the content type header. This
	 * 			may wrap a {@link MessagingException}
	 */
	public ContentDisposition getContentDisposition() throws DominoException {
		String headerVal = null;
		try {
			InternetHeaders headers = getInternetHeaders();
			
			String[] headerVals = headers.getHeader("Content-Disposition"); //$NON-NLS-1$
			if(headerVals == null || headerVals.length < 1) { return null; }

			headerVal = MimeUtility.decodeText(headerVals[0]);
			return new ContentDisposition(headerVal);
		} catch(MessagingException e) {
			log.error("Got {0} when working with headerVal: {1}", e.getClass().getSimpleName(), headerVal);
			throw new DominoException(e);
		} catch(UnsupportedEncodingException e) {
			throw new DominoException(e);
		}
	}
	
	/* ******************************************************************************
	 * Misc.
	 ********************************************************************************/
	
	@Override
	public String toString() {
		if(isRefValid()) {
			return StringUtil.format("[{0}: version={1}, flags={2}, partType={3}, byteCount={4}, boundaryLen={5}, headersLen={6}]", //$NON-NLS-1$
					getClass().getSimpleName(),
					getVersion(),
					getFlags(),
					getPartType(),
					getByteCount(),
					getBoundaryLen(),
					getHeadersLen()
			);
		} else {
			return super.toString();
		}
	}
}
