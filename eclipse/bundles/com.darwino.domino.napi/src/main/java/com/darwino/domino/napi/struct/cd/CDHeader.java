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

package com.darwino.domino.napi.struct.cd;

/**
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 * @param S the size of the signature field
 * @param L the size of the data-length field
 */
public interface CDHeader<S extends Number, L extends Number> {
	public S getSignature();
	public void setSignature(S signature);
	
	public L getLength();
	public void setLength(L length);
}
