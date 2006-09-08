// vim:filetype=java:ts=4
/*
	Copyright (c) 2004, 2005, 2006
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
package com.mcdermottroe.exemplar.output.java.sax2;

import com.mcdermottroe.exemplar.output.XMLParserGeneratorException;
import com.mcdermottroe.exemplar.output.java.XMLJavaSourceGenerator;

/** A class which generates Java parsers that implement the SAX 2 XML parser
	API.

	@author	Conor McDermottroe
	@since	0.1
*/
public class Generator extends XMLJavaSourceGenerator {
	/**	Creates a source generator which produces parsers in the Java language
		which implement the SAX version 2 API.

		@throws XMLParserGeneratorException	if the super-class constructor
											throws one.
	*/
	public Generator()
	throws XMLParserGeneratorException
	{
		// The parent does all the work.
		super();
	}

	/** {@inheritDoc} */
	public String describeAPI() {
		return "The SAX2 API";
	}
}
