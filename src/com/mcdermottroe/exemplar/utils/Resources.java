// vim:filetype=java:ts=4
/*
	Copyright (c) 2007
	Conor McDermottroe.  All rights reserved.

	Redistribution and use in source and binary forms, with or without
	modification, are permitted provided that the following conditions
	are met:
	1. Redistributions of source code must retain the above copyright
	   notice, this list of conditions and the following disclaimer.
	2. Redistributions in binary form must reproduce the above copyright
	   notice, this list of conditions and the following disclaimer in the
	   documentation and/or other materials provided with the distribution.
	3. Neither the name of the author nor the names of any contributors to
	   the software may be used to endorse or promote products derived from
	   this software without specific prior written permission.

	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
	"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
	LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
	A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
	HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
	SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
	TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
	OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
	OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
	NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
	SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.mcdermottroe.exemplar.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.mcdermottroe.exemplar.DBC;

/** A collection of XML-specific {@link String} handling methods.

	@author	Conor McDermottroe
	@since	0.2
*/
public final class Resources {
	/** A cache of resources which have already been read from the disk. */
	private static final Map<Class<?>, Map<String, String>> cache =
		new HashMap<Class<?>, Map<String, String>>();

	/** Private constructor to prevent instantiation of this class. */
	private Resources() {
		DBC.UNREACHABLE_CODE();
	}

	/** Get the resources for a class.

		@param	c							The class to fetch the resource
											bundle for.
		@return								A {@link Map} with the keys and
											values from the resource bundle in
											the JAR.
	*/
	public static Map<String, String> get(Class<?> c) {
		// Check the cache
		if (cache.containsKey(c)) {
			return cache.get(c);
		}

		// The return value
		Map<String, String> retVal = new HashMap<String, String>();

		// Get the resource bundle.
		ResourceBundle rb;
		try {
			rb = ResourceBundle.getBundle(c.getName());
		} catch (MissingResourceException e) {
			DBC.IGNORED_EXCEPTION(e);
			return retVal;
		}

		// Read all of the contents of the resource bundle into a map.
		for (Enumeration<String> e = rb.getKeys(); e.hasMoreElements(); ) {
			String key = e.nextElement();
			String value = rb.getString(key);
			if (key != null && value != null) {
				retVal.put(key, value);
			}
		}

		// Cache the result
		cache.put(c, retVal);

		return retVal;
	}

	/** Get a {@link String} from the {@link ResourceBundle} for a particular
		{@link Class}.

		@param	c	The {@link Class} to find the resource for.
		@param	key	The key in the resource bundle.
		@return		The value associated with <code>key</code> in the bundle, or
					null if <code>key</code> was not in the bundle.
	*/
	public static String getString(Class<?> c, String key) {
		Map<String, String> resources = get(c);
		if (resources.containsKey(key)) {
			return resources.get(key);
		} else {
			return null;
		}
	}


	/** Get a <code>boolean</code> from the {@link ResourceBundle} for a
		particular {@link Class}.

		@param	c			The {@link Class} to find the resource for.
		@param	key			The key in the resource bundle.
		@param	defValue	The value to return if <code>key</code> was not in
							the bundle.
		@return				The value associated with <code>key</code> in the
							bundle, or <code>defValue</code> if <code>key</code>
							was not in the bundle.
	*/
	public static boolean getBoolean(Class<?> c, String key, boolean defValue) {
		Map<String, String> resources = get(c);
		if (resources.containsKey(key)) {
			return Boolean.valueOf(resources.get(key));
		} else {
			return defValue;
		}
	}}
