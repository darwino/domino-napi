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

/**
 * This marker interface is used to classify collections of TIMEDATEs and TIMEDATE_PAIRs, but does not provide
 * any behavioral specification of its own.
 * 
 * @author Jesse Gallagher
 *
 */
public interface TimeStruct {
	void free();
}
