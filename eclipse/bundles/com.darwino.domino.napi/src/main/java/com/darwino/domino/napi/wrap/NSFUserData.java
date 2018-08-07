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

package com.darwino.domino.napi.wrap;

/**
 * This class represents the data pair contained within an item of type USERDATA:
 * the user-defined format name and the data as a byte array
 * 
 * @author Jesse Gallagher
 * @since 1.0.1
 */
public class NSFUserData {

	private final String formatName;
	private final byte[] data;
	
	/**
	 * Constructs a new <code>NSFUserData</code> using the provided format name and byte array
	 * of data. The byte array is referenced directly; it is <strong>not</strong> copied in memory.
	 */
	public NSFUserData(String formatName, byte[] data) {
		this.formatName = formatName;
		this.data = data;
	}

	public String getFormatName() {
		return formatName;
	}
	
	/**
	 * @return the data contained in this <code>NSFUserData</code> object. The internal byte array is
	 * 		returned directly; it is <strong>not</strong> copied in memory
	 */
	public byte[] getData() {
		return data;
	}
}
