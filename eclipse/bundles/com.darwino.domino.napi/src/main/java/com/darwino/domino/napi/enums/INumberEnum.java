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
package com.darwino.domino.napi.enums;

/**
 * @author Jesse Gallagher
 * @since 0.8.0
 *
 */
public abstract interface INumberEnum<T extends Number> {
	/**
	 * @return the C-level value of the enum 
	 */
	T getValue();
	
	/**
	 * @return the C-level value of the enum as a <code>long</code>
	 */
	long getLongValue();
}
