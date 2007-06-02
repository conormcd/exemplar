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
package junit.com.mcdermottroe.exemplar.output.java;

import java.util.ArrayList;
import java.util.Collection;
import java.io.File;

import com.mcdermottroe.exemplar.output.java.XMLJavaSourceGenerator;
import com.mcdermottroe.exemplar.ui.Options;

import static com.mcdermottroe.exemplar.Constants.Format.Filenames.JAVA_PARSER;
import static com.mcdermottroe.exemplar.Constants.Format.Filenames.JFLEX;
import static com.mcdermottroe.exemplar.Constants.Output.Java.ENTITIES_FILE;

import junit.com.mcdermottroe.exemplar.output.XMLParserSourceGeneratorTestCase;

/** Test class for children of {@link XMLJavaSourceGenerator}.

	@param	<T>	The type of {@link XMLJavaSourceGenerator} to test.
	@author	Conor McDermottroe
	@since	0.1
*/
public abstract class
XMLJavaSourceGeneratorTestCase<T extends XMLJavaSourceGenerator<T>>
extends XMLParserSourceGeneratorTestCase<T>
{
	/** {@inheritDoc} */
	@Override public Collection<File> generatedFiles(File outputDir) {
		String vocabulary = Options.getString("vocabulary");
		Collection<File> retVal = new ArrayList<File>();
		retVal.add(new File(outputDir, String.format(JAVA_PARSER, vocabulary)));
		retVal.add(new File(outputDir, String.format(JFLEX, vocabulary)));
		if (Options.isSet("include", "entities")) {
			retVal.add(new File(outputDir, ENTITIES_FILE));
		}
		return retVal;
	}
}
