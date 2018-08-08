package com.darwino.domino.napi.runtime;

/**
 * A variant of {@link Freeable} that also defines a method for returning a
 * uniquely-identifying value (such as a handle or memory address) to distinguish
 * this object from others in a consistent manner.
 * 
 * @author Jesse Gallagher
 * @since 2.2.0
 */
public interface IdentifiableFreeable extends Freeable {
	/**
	 * Provides a unique back-end identifier for this object, such as its address in
	 * memory or a handle, to distinguish it from others in a consistent manner.
	 * 
	 * @return a unique identifier for the back-end memory
	 */
	long getBackendIdentifier();
}
