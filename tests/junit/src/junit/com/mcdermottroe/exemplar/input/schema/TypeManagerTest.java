// vim:filetype=java:ts=4
/*
	Copyright (c) 2008
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
package junit.com.mcdermottroe.exemplar.input.schema;

import java.util.EnumSet;

import com.mcdermottroe.exemplar.generated.schema.element.ComplexType;
import com.mcdermottroe.exemplar.generated.schema.element.SimpleType;
import com.mcdermottroe.exemplar.input.ParserException;
import com.mcdermottroe.exemplar.input.schema.TypeManager;
import com.mcdermottroe.exemplar.input.schema.type.AtomicType;
import com.mcdermottroe.exemplar.input.schema.type.Finality;
import com.mcdermottroe.exemplar.input.schema.type.ListType;
import com.mcdermottroe.exemplar.input.schema.type.Type;
import com.mcdermottroe.exemplar.input.schema.type.UnionType;
import com.mcdermottroe.exemplar.input.schema.type.facet.PatternFacet;

import junit.com.mcdermottroe.exemplar.UtilityClassTestCase;

/** Test class for {@link TypeManager}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class TypeManagerTest
extends UtilityClassTestCase<TypeManager>
{
	/** Test {@link TypeManager#addType(Type)}. */
	public void testAddType() {
		AtomicType namedType = new AtomicType(
			"namedType",
			false,
			EnumSet.of(Finality.NONE)
		);
		try {
			TypeManager.addType(namedType);
		} catch (ParserException e) {
			assertNotNull("ParserException was null", e);
			fail("addType(Type) threw a parser exception");
			return;
		}

		try {
			Type<?> retrievedType = TypeManager.getType("namedType");
			assertNotNull("getType(String) returned null", retrievedType);
			assertEquals(
				"Round-tripped object was modified",
				namedType,
				retrievedType
			);
		} catch (ParserException e) {
			assertNotNull("ParserException was null", e);
			fail("getType(String) threw a parser exception");
		}
	}

	/** Test {@link TypeManager#addType(SimpleType, Type)}. */
	public void testAddTypeBySimpleType() {
		AtomicType namedType = new AtomicType(
			"namedType",
			false,
			EnumSet.of(Finality.NONE)
		);
		SimpleType simpleType = new SimpleType();
		simpleType.setId("simpleType");

		try {
			TypeManager.addType(simpleType, namedType);
		} catch (ParserException e) {
			assertNotNull("ParserException was null", e);
			fail("addType(SimpleType, Type) threw a parser exception");
			return;
		}

		try {
			Type<?> retrievedType = TypeManager.getType(simpleType);
			assertNotNull("getType(SimpleType) returned null", retrievedType);
			assertEquals(
				"Round-tripped object was modified",
				namedType,
				retrievedType
			);
		} catch (ParserException e) {
			assertNotNull("ParserException was null", e);
			fail("getType(SimpleType) threw a parser exception");
		}
	}

	/** Test {@link TypeManager#addType(ComplexType, Type)}. */
	public void testAddTypeByComplexType() {
		AtomicType namedType = new AtomicType(
			"namedType",
			false,
			EnumSet.of(Finality.NONE)
		);
		ComplexType complexType = new ComplexType();
		complexType.setId("complexType");

		try {
			TypeManager.addType(complexType, namedType);
		} catch (ParserException e) {
			assertNotNull("ParserException was null", e);
			fail("addType(ComplexType, Type) threw a parser exception");
			return;
		}

		try {
			Type<?> retrievedType = TypeManager.getType(complexType);
			assertNotNull("getType(ComplexType) returned null", retrievedType);
			assertEquals(
				"Round-tripped object was modified",
				namedType,
				retrievedType
			);
		} catch (ParserException e) {
			assertNotNull("ParserException was null", e);
			fail("getType(ComplexType) threw a parser exception");
		}
	}

	/** Test {@link TypeManager#extendByList(String, java.util.Set, Type)}. */
	public void testExtendByList() {
		AtomicType typeToList = new AtomicType(
			"typeToList",
			false,
			EnumSet.of(Finality.NONE)
		);

		Type<ListType<AtomicType>> listType;
		try {
			listType = TypeManager.extendByList(
				"listType",
				EnumSet.of(Finality.NONE),
				typeToList
			);
		} catch (ParserException e) {
			assertNotNull("ParserException was null", e);
			fail("extendByList threw a parser exception");
			return;
		}
		assertNotNull("extendByList returned null", listType);
	}

	/** Test {@link TypeManager#extendByRestriction(String, Type, java.util.Set,
		com.mcdermottroe.exemplar.input.schema.type.facet.Facet[])}.
	*/
	public void testExtendByRestriction() {
		AtomicType typeToExtend = new AtomicType(
			"typeToExtend",
			false,
			EnumSet.of(Finality.NONE)
		);

		AtomicType extendedType;
		try {
			extendedType = TypeManager.extendByRestriction(
				"extendedType",
				typeToExtend,
				EnumSet.of(Finality.NONE),
				new PatternFacet("aab*")
			);
		} catch (ParserException e) {
			assertNotNull("ParserException was null", e);
			fail("extendByRestriction threw a parser exception");
			return;
		}
		assertNotNull("extendByRestriction returned null", extendedType);
	}

	/** Test {@link TypeManager#extendByUnion(String, java.util.Set, Type[])}.
	*/
	public void testExtendByUnion() {
		AtomicType typeToUnionA = new AtomicType(
			"typeToUnionA",
			false,
			EnumSet.of(Finality.NONE)
		);
		AtomicType typeToUnionB = new AtomicType(
			"typeToUnionA",
			false,
			EnumSet.of(Finality.NONE)
		);

		UnionType unionedType;
		try {
			unionedType = TypeManager.extendByUnion(
				"unionedType",
				EnumSet.of(Finality.NONE),
				typeToUnionA,
				typeToUnionB
			);
		} catch (ParserException e) {
			assertNotNull("ParserException was null", e);
			fail("extendByUnion threw a parser exception");
			return;
		}
		assertNotNull("extendByUnion returned null", unionedType);
	}

	/** Test {@link TypeManager#getType(String)}. */
	public void testGetTypeByName() {
		assertTrue("See testAddType()", true);
	}

	/** Test {@link TypeManager#getType(SimpleType)}. */
	public void testGetTypeBySimpleType() {
		assertTrue("See testAddTypeBySimpleType()", true);
	}

	/** Test {@link TypeManager#getType(ComplexType)}. */
	public void testGetTypeByComplexType() {
		assertTrue("See testAddTypeByComplexType()", true);
	}
}
