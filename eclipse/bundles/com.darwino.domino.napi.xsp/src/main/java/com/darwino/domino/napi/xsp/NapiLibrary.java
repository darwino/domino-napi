package com.darwino.domino.napi.xsp;

import com.ibm.xsp.library.AbstractXspLibrary;

/**
 * XPages library contributor for the Darwino Domino NAPI.
 * 
 * @author Jesse Gallagher
 * @since 2.2.0
 */
public class NapiLibrary extends AbstractXspLibrary {
	public static final String LIBRARY_ID = NapiLibrary.class.getPackage().getName();

	@Override
	public String getLibraryId() {
		return LIBRARY_ID;
	}

}
