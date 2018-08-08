package com.darwino.domino.napi.runtime;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * A {@link PhantomReference} implementation with specific knowledge of {@link IdentifiableFreeable} objects.
 * 
 * @author Jesse Gallagher
 * @since 2.2.0
 */
public class IdentifiableFreeableReference extends PhantomReference<IdentifiableFreeable> {

	public IdentifiableFreeableReference(IdentifiableFreeable referent, ReferenceQueue<? super IdentifiableFreeable> q) {
		super(referent, q);
		
	}
}
