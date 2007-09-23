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
package com.mcdermottroe.exemplar.output.java.binding;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.model.XMLElement;
import com.mcdermottroe.exemplar.utils.Strings;

import static com.mcdermottroe.exemplar.Constants.Character.COLON;

/** The default {@link ElementNameConverter}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class DefaultElementNameConverter
extends ElementNameConverter<DefaultElementNameConverter>
{
	/** {@inheritDoc} */
	@Override protected String generateClassName(XMLElement element) {
		String elementName = element.getName();
		String className;
		int indexOfColon = elementName.indexOf((int)COLON);
		if (indexOfColon > 0) {
			className = Strings.upperCaseFirst(
				elementName.substring(indexOfColon + 1)
			);
		} else {
			className = Strings.upperCaseFirst(elementName);
		}
		return className.replaceAll("\\W", "_");
	}

	/** {@inheritDoc} */
	public DefaultElementNameConverter getCopy()
	throws CopyException
	{
		return new DefaultElementNameConverter();
	}
}