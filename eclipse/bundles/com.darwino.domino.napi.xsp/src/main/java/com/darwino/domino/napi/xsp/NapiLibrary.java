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
	
	@Override
	public String getPluginId() {
		return NapiActivator.instance.getBundle().getSymbolicName();
	}
	
	@Override
	public boolean isGlobalScope() {
		return false;
	}

}
