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

/** Option type for representing switches. Switched are options that do not
	take a value. Their presence indicates an "on" state and their absence
	indicates an "off" state.

	@author	Conor McDermottroe
	@since	0.2
*/
public class Switch
extends Option<Switch>
{
	/** Constructor that just initializes the member variables.

		@param switchName		The name of this switch.
		@param switchDesc		A description of what this switch controls.
		@param aDefaultValue	A default value for this switch.
	*/
	public Switch(
						String switchName,
						String switchDesc,
						boolean aDefaultValue
					)
	{
		super(switchName, switchDesc, false, false, false);
		value = new ArrayList<String>(1);
		if (aDefaultValue) {
			value.add(String.valueOf(Boolean.TRUE));
		} else {
			value.add(String.valueOf(Boolean.FALSE));
		}
	}

	/** {@inheritDoc} */
	public Switch getCopy() {
		return new Switch(name, description, Boolean.valueOf(value.get(0)));
	}
}
