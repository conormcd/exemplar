// vim:filetype=java:ts=4
/*
	Copyright (c) 2006, 2007
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
package com.mcdermottroe.exemplar.input;

import java.io.File;
import java.io.InputStream;

import com.mcdermottroe.exemplar.model.XMLDocumentType;

/** An interface to which all input modules must conform.

	@author	Conor McDermottroe
	@since	0.1
*/
public interface InputModule {
	/** Given an input file path, produce an {@link XMLDocumentType} object
		representing the vocabulary described by the input file.

		@param	inputFilePath	The path to the input file to parse.
		@return					An {@link XMLDocumentType} object which
								describes the vocabulary of XML defined in the
								input file.
		@throws ParserException	If anything fails in the parsing process.
	*/
	XMLDocumentType parse(String inputFilePath) throws ParserException;

	/** Given an input file, produce an {@link XMLDocumentType} object
		representing the vocabulary described by the input file.

		@param	inputFile		The file to parse as input.
		@return					An {@link XMLDocumentType} object which
								describes the vocabulary of XML defined in the
								input file.
		@throws ParserException	If anything fails in the parsing process.
	*/
	XMLDocumentType parse(File inputFile) throws ParserException;

	/** Given an input source, produce an {@link XMLDocumentType} object
		representing the vocabulary described by the input file.

		@param	input			The source material to parse.
		@return					An {@link XMLDocumentType} object which
								describes the vocabulary of XML defined in the
								input file.
		@throws ParserException	If anything fails in the parsing process.
	*/
	XMLDocumentType parse(InputStream input) throws ParserException;

	/** Describe the {@link InputModule} for documentation purposes.

		@return A {@link String} describing the language that the implementing
				class parses.
	*/
	@Override String toString();
}
