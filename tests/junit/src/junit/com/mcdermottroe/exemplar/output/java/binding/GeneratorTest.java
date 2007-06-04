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
package junit.com.mcdermottroe.exemplar.output.java.binding;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.output.java.binding.Generator;
import com.mcdermottroe.exemplar.ui.Options;
import com.mcdermottroe.exemplar.utils.Strings;

import static com.mcdermottroe.exemplar.Constants.Format.Filenames.JAVA;

import junit.com.mcdermottroe.exemplar.output.XMLParserSourceGeneratorTestCase;

/** Test class for {@link Generator}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class GeneratorTest
extends XMLParserSourceGeneratorTestCase<Generator>
{
	/** {@inheritDoc} */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();

		Options.set("output-package", "test.output.package");

		addSample(new Generator());
	}

	/** {@inheritDoc} */
	@Override public Collection<File> generatedFiles(
		File outputDir,
		XMLDocumentType docType
	)
	{
		String vocabulary = Options.getString("vocabulary");
		String rootParserClass = Strings.upperCaseFirst(vocabulary);

		File elementDir = new File(outputDir, "element");
		File supportDir = new File(outputDir, "support");

		Collection<File> retVal = new ArrayList<File>();
		retVal.add(new File(outputDir, String.format(JAVA, rootParserClass)));
		retVal.add(elementDir);
		for (String elementName : docType.elements().keySet()) {
			String className;
			if (elementName.contains(":")) {
				className = Strings.upperCaseFirst(
					elementName.substring(elementName.indexOf((int)':') + 1)
				);
			} else {
				className = Strings.upperCaseFirst(elementName);
			}
			retVal.add(
				new File(
					elementDir,
					String.format(JAVA, className)
				)
			);
		}
		retVal.add(supportDir);
		retVal.add(new File(supportDir, String.format(JAVA, "XMLComponent")));
		retVal.add(new File(supportDir, String.format(JAVA, "XMLContent")));
		retVal.add(
			new File(supportDir, String.format(JAVA, "exemplarElement"))
		);
		return retVal;
	}
}
