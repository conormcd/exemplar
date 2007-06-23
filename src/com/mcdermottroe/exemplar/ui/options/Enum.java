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
package com.mcdermottroe.exemplar.ui.options;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/** Option type for dealing with enumerated options. These are options
	that take one or more values from a fixed set.

	@author	Conor McDermottroe
	@since	0.2
*/
public class Enum
extends Option<Enum>
{
	/** The allowed values. */
	protected final Map<String, String> enumValues;

	/** Constructor that just initializes the member variables.

		@param enumName			The name of this enumerated option.
		@param enumDesc			A description of what this option is for.
		@param enumVals			The values that this option may take. This
								is a map where the keys are the values that
								the Enum may take and the values are the
								descriptions of what those values do.
		@param isMandatory		Set if this option must be set by the user.
		@param isMultiValue		Set if more than one value from the fixed
								set can be passed to this option.
		@param isCaseSensitive	Set if the value(s) given are case
								sensitive.
		@param defaultValues	Default value(s) to set this option to.
	*/
	public Enum	(
						String enumName,
						String enumDesc,
						Map<String, String> enumVals,
						boolean isMandatory,
						boolean isMultiValue,
						boolean isCaseSensitive,
						Set<String> defaultValues
					)
	{
		super	(
					enumName,
					enumDesc,
					isMandatory,
					isMultiValue,
					isCaseSensitive
				);
		if (defaultValues != null) {
			value = new ArrayList<Object>(defaultValues);
		} else {
			value = new ArrayList<Object>();
		}
		enumValues = new HashMap<String, String>(enumVals);
	}

	/** Accessor for the allowed values of this Enum.

		@return A copy of the {@link Map} where the keys are the allowed
				values for this Enum and the values are the descriptions of
				what the values do.
	*/
	public Map<String, String> getEnumValues() {
		return new TreeMap<String, String>(enumValues);
	}

	/** {@inheritDoc} */
	public Enum getCopy() {
		Map<String, String> enumValuesCopy = new TreeMap<String, String>();
		enumValuesCopy.putAll(enumValues);
		Set<String> valueCopy = new HashSet<String>();
		for (Object o : value) {
			valueCopy.add(o.toString());
		}
		return new Enum(
			name,
			description,
			enumValuesCopy,
			mandatory,
			multiValue,
			caseSensitive,
			valueCopy
		);
	}
}
