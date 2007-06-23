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
import java.util.List;

import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.utils.Strings;

import static com.mcdermottroe.exemplar.Constants.Character.COMMA;
import static com.mcdermottroe.exemplar.Constants.Character.LEFT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.RIGHT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.SPACE;

/** An option data type.

	@author		Conor McDermottroe
	@since		0.2
	@param	<T>	The type of {@link Option} this is.
*/
public abstract class Option<T extends Option<T>>
implements Copyable<T>
{
	/** The name of the option. */
	protected final String name;

	/** The current value(s) of the option. */
	protected List<Object> value;

	/** A textual description of the option. */
	protected final String description;

	/** Whether the option must be present or not. */
	protected final boolean mandatory;

	/** Whether or not the option takes multiple values. */
	protected final boolean multiValue;

	/** Whether or not the option is case sensitive. */
	protected final boolean caseSensitive;

	/** Simple constructor which initialises all of the member variables
		with given values.

		@param	optionName			The value for the member name.
		@param	optionDesc			The value for the member description.
		@param	optionMandatory		The value for the member mandatory.
		@param	optionMultiValue	The value for the member multiValue.
		@param	optionCaseSensitive	The value for the member caseSensitive.
	*/
	protected Option(
						String optionName,
						String optionDesc,
						boolean optionMandatory,
						boolean optionMultiValue,
						boolean optionCaseSensitive
					)
	{
		name = optionName;
		description = optionDesc;
		mandatory = optionMandatory;
		multiValue = optionMultiValue;
		caseSensitive = optionCaseSensitive;
		value = null;
	}

	/** Accessor for the name member.

		@return The value of the name member.
	*/
	public String getName() {
		return name;
	}

	/** Accessor for the value member.

		@return A copy of the value of the name member.
	*/
	public List<Object> getValue() {
		return new ArrayList<Object>(value);
	}

	/** Setter for the value member.

		@param newValue The list to copy into the value member.
	*/
	public void setValue(List<Object> newValue) {
		value = new ArrayList<Object>(newValue);
	}

	/** Accessor for the description member.

		@return The value of the description member.
	*/
	public String getDescription() {
		return description;
	}

	/** Accessor for the mandatory member.

		@return The value of the mandatory member.
	*/
	public boolean isMandatory() {
		return mandatory;
	}

	/** Accessor for the multiValue member.

		@return The value of the multiValue member.
	*/
	public boolean isMultiValue() {
		return multiValue;
	}

	/** Accessor for the caseSensitive member.

		@return The value of the caseSensitive member.
	*/
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/** See {@link Object#equals(Object)}.

		@param	o	The other object to compare with.
		@return		True if <code>this</code> equals <code>o</code>.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!o.getClass().equals(getClass())) {
			return false;
		}

		T other = (T)o;
		if (!Utils.areDeeplyEqual(getName(), other.getName())) {
			return false;
		}
		if (!Utils.areDeeplyEqual(getDescription(), other.getDescription())) {
			return false;
		}
		if (!Utils.areDeeplyEqual(isMandatory(), other.isMandatory())) {
			return false;
		}
		if (!Utils.areDeeplyEqual(isMultiValue(), other.isMultiValue())) {
			return false;
		}
		if (!Utils.areDeeplyEqual(isCaseSensitive(), other.isCaseSensitive())) {
			return false;
		}
		if (!Utils.areDeeplyEqual(getValue(), other.getValue())) {
			return false;
		}
		return true;
	}

	/** See {@link Object#hashCode()}.

		@return A hash code for this object.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(
			name,
			description,
			mandatory,
			multiValue,
			caseSensitive,
			value
		);
	}

	/** See {@link Object#toString()}.

		@return	A descriptive {@link String}
	*/
	@Override public String toString() {
		StringBuilder desc = new StringBuilder(getClass().getName());
		desc.append(LEFT_PAREN);
		desc.append(
			Strings.join(
				String.valueOf(COMMA) + SPACE,
				name,
				description,
				mandatory,
				multiValue,
				caseSensitive,
				value
			)
		);
		desc.append(RIGHT_PAREN);
		return desc.toString();
	}
}
