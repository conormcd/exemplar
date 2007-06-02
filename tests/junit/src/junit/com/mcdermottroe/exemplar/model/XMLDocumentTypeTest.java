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
package junit.com.mcdermottroe.exemplar.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mcdermottroe.exemplar.model.XMLAttribute;
import com.mcdermottroe.exemplar.model.XMLAttributeList;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.model.XMLDocumentTypeException;
import com.mcdermottroe.exemplar.model.XMLElement;
import com.mcdermottroe.exemplar.model.XMLElementContentType;
import com.mcdermottroe.exemplar.model.XMLEntity;
import com.mcdermottroe.exemplar.model.XMLExternalIdentifier;
import com.mcdermottroe.exemplar.model.XMLMarkupDeclaration;
import com.mcdermottroe.exemplar.model.XMLNotation;
import com.mcdermottroe.exemplar.model.XMLElementContentModel;
import com.mcdermottroe.exemplar.utils.PowerSet;

import junit.com.mcdermottroe.exemplar.NormalClassTestCase;

/** Test class for {@link XMLDocumentType}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLDocumentTypeTest
extends NormalClassTestCase<XMLDocumentType>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		// Create some sample XMLMarkupDeclaration objects
		XMLAttributeList attlist = new XMLAttributeList(
			"testElement",
			new ArrayList<XMLAttribute>()
		);
		XMLElement element = new XMLElement(
			"testElement",
			new XMLElementContentModel(XMLElementContentType.ANY)
		);
		XMLEntity entity = new XMLEntity("testEntityName", "testEntityValue");
		XMLNotation notation = new XMLNotation(
			"testNotationName",
			new XMLExternalIdentifier("publicId", "systemId")
		);

		Set<XMLMarkupDeclaration> sampleMarkup;
		sampleMarkup = new HashSet<XMLMarkupDeclaration>();
		sampleMarkup.add(attlist);
		sampleMarkup.add(element);
		sampleMarkup.add(entity);
		sampleMarkup.add(notation);

		// XMLDocumentType(Collection) constructor
		PowerSet<XMLMarkupDeclaration> perm;
		perm = new PowerSet<XMLMarkupDeclaration>(sampleMarkup);
		for (Set<XMLMarkupDeclaration> m : perm) {
			if (!(m.contains(attlist) && !m.contains(element))) {
				addSample(new XMLDocumentType(m));
			}
		}
	}

	/** Test the constructor of {@link XMLDocumentType}. */
	public void testConstructor() {
		boolean fellThrough = false;
		XMLDocumentType xdt = null;
		try {
			xdt = new XMLDocumentType(null);
			fellThrough = true;
		} catch (XMLDocumentTypeException e) {
			assertNotNull("XMLDocumentTypeException was null", e);
			fail("An unexpected error occurred");
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertFalse("XMLDocumentType(null) did not assert false", fellThrough);
	}

	/** Test {@link XMLDocumentType#attlists()}. */
	public void testAttlists() {
		for (XMLDocumentType sample : samples()) {
			if (sample != null) {
				Map<String, XMLMarkupDeclaration> attlists = sample.attlists();
				assertNotNull("attlists() returned null", attlists);
				for (String name : attlists.keySet()) {
					assertSame(
						"Attlist name was inconsistent",
						name,
						attlists.get(name).getName()
					);
				}
			}
		}
	}

	/** Test {@link XMLDocumentType#elements()}. */
	public void testElements() {
		for (XMLDocumentType sample : samples()) {
			if (sample != null) {
				Map<String, XMLMarkupDeclaration> elements = sample.elements();
				assertNotNull("elements() returned null", elements);
				for (String name : elements.keySet()) {
					assertSame(
						"Element name was inconsistent",
						name,
						elements.get(name).getName()
					);
				}
			}
		}
	}

	/** Test {@link XMLDocumentType#entities()}. */
	public void testEntities() {
		for (XMLDocumentType sample : samples()) {
			if (sample != null) {
				Map<String, XMLMarkupDeclaration> entities = sample.entities();
				assertNotNull("entities() returned null", entities);
				for (String name : entities.keySet()) {
					assertSame(
						"Entities name was inconsistent",
						name,
						entities.get(name).getName()
					);
				}
			}
		}
	}


	/** Test {@link XMLDocumentType#notations()}. */
	public void testNotations() {
		for (XMLDocumentType sample : samples()) {
			if (sample != null) {
				Map<String, XMLMarkupDeclaration> nots = sample.notations();
				assertNotNull("notations() returned null", nots);
				for (String name : nots.keySet()) {
					assertSame(
						"Notation name was inconsistent",
						name,
						nots.get(name).getName()
					);
				}
			}
		}
	}

	/** Test {@link XMLDocumentType#hasAttlists()}. */
	public void testHasAttlists() {
		for (XMLDocumentType sample : samples()) {
			if (sample != null) {
				assertEquals(
					"hasAttlists() does not agree with attlists()",
					sample.attlists() != null && !sample.attlists().isEmpty(),
					sample.hasAttlists()
				);
			}
		}
	}
}
