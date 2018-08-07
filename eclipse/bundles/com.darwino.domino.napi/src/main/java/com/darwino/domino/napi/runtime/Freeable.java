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
package com.darwino.domino.napi.runtime;

/**
 * Represents an object that maintains C- or Domino-side references and must be freed when
 * unneeded.
 * 
 * @author Jesse Gallagher
 * @since 2.2.0
 */
public interface Freeable {
	/**
	 * Frees any back-end resources associated with this object.
	 * 
	 * <p>This method may not actually free the back-end resources if the object
	 * was instantiated as having its memory "owned" by another object.</p>
	 */
	void free();
	
	/**
	 * Frees any back-end resources associated with this object.
	 * 
	 * <p>This method will free the object's back-end resources even if the
	 * object was instantiated as having its memory "owned" by another object.</p>
	 */
	void free(boolean force);
	
	/**
	 * Determines whether the object is still valid ({@code true}) or has
	 * already been freed ({@code false}).
	 * 
	 * @return {@code true} if the object is still valid and usable; {@code false}
	 * 		otherwise
	 */
	boolean isRefValid();
}
