// vim:filetype=java:ts=4
/*
	Copyright (c) 2007, 2008
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
package com.mcdermottroe.exemplar.input.schema;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.generated.schema.element.Annotation;
import com.mcdermottroe.exemplar.generated.schema.element.ComplexType;
import com.mcdermottroe.exemplar.generated.schema.element.Enumeration;
import com.mcdermottroe.exemplar.generated.schema.element.FractionDigits;
import com.mcdermottroe.exemplar.generated.schema.element.Length;
import com.mcdermottroe.exemplar.generated.schema.element.MaxExclusive;
import com.mcdermottroe.exemplar.generated.schema.element.MaxInclusive;
import com.mcdermottroe.exemplar.generated.schema.element.MaxLength;
import com.mcdermottroe.exemplar.generated.schema.element.MinExclusive;
import com.mcdermottroe.exemplar.generated.schema.element.MinInclusive;
import com.mcdermottroe.exemplar.generated.schema.element.MinLength;
import com.mcdermottroe.exemplar.generated.schema.element.Pattern;
import com.mcdermottroe.exemplar.generated.schema.element.Restriction;
import com.mcdermottroe.exemplar.generated.schema.element.SimpleType;
import com.mcdermottroe.exemplar.generated.schema.element.TotalDigits;
import com.mcdermottroe.exemplar.generated.schema.element.Union;
import com.mcdermottroe.exemplar.generated.schema.element.WhiteSpace;
import com.mcdermottroe.exemplar.generated.schema.support.W3CSchemaException;
import com.mcdermottroe.exemplar.generated.schema.support.W3CSchemaTreeOp;
import com.mcdermottroe.exemplar.generated.schema.support.XMLComponent;
import com.mcdermottroe.exemplar.generated.schema.support.XMLContent;
import com.mcdermottroe.exemplar.input.ParserException;
import com.mcdermottroe.exemplar.input.schema.type.Finality;
import com.mcdermottroe.exemplar.input.schema.type.Type;
import com.mcdermottroe.exemplar.input.schema.type.facet.EnumerationFacet;
import com.mcdermottroe.exemplar.input.schema.type.facet.Facet;
import com.mcdermottroe.exemplar.ui.Message;

/** A {@link W3CSchemaTreeOp} to process type information in the parsed schema
	tree.

	@author Conor McDermottroe
	@since	0.2
*/
public class TypeProcessor
implements Copyable<TypeProcessor>, W3CSchemaTreeOp
{
	/** {@inheritDoc} */
	public boolean shouldApply(XMLComponent<?> treeComponent) {
		return	ComplexType.class.isAssignableFrom(treeComponent.getClass()) ||
				SimpleType.class.isAssignableFrom(treeComponent.getClass());
	}

	/** {@inheritDoc} */
	public void execute(XMLComponent<?> treeComponent)
	throws W3CSchemaException
	{
		Class<?> treeComponentClass = treeComponent.getClass();
		if (ComplexType.class.isAssignableFrom(treeComponentClass)) {
			processComplexType(ComplexType.class.cast(treeComponent));
		} else if (SimpleType.class.isAssignableFrom(treeComponentClass)) {
			processSimpleType(SimpleType.class.cast(treeComponent));
		} else {
			DBC.UNREACHABLE_CODE();
		}
	}

	/** Process a {@link ComplexType} and add it to the {@link TypeManager}.

		@param	complexType				The {@link ComplexType} to process.
		@throws	W3CSchemaException		If an error occurs while processing the
										complex type definition.
	*/
	protected void processComplexType(ComplexType complexType)
	throws W3CSchemaException
	{
		// SCHEMATASK
	}

	/** Process a {@link SimpleType} and add it to the {@link TypeManager}.

		@param	simpleType			The {@link SimpleType} to process.
		@throws	W3CSchemaException	If an error occurs while processing the 
									simple type definition.
	*/
	protected static void processSimpleType(SimpleType simpleType)
	throws W3CSchemaException
	{
		// Process the properties of this simple type
		String name = simpleType.getName();
		Set<Finality> finality = EnumSet.of(Finality.NONE);
		String finalProperty = simpleType.getFinal();
		if (finalProperty == null) {
			// SCHEMATASK: Go to the enclosing schema finalDefault attribute
			finalProperty = "";
		}
		if ("".equals(finalProperty)) {
			finality.add(Finality.NONE);
		} else {
			for (String finalityBit : finalProperty.split("\\s+")) {
				if ("#all".equals(finalityBit)) {
					finality.add(Finality.ALL);
				} else if ("list".equals(finalityBit)) {
					finality.add(Finality.LIST);
				} else if ("restriction".equals(finalityBit)) {
					finality.add(Finality.RESTRICTION);
				} else if ("union".equals(finalityBit)) {
					finality.add(Finality.UNION);
				}
			}
		}
		String id = simpleType.getId();

		// Normalize the finality set
		if (finality.size() >= 2) {
			finality.remove(Finality.NONE);
		}
		if (finality.contains(Finality.ALL)) {
			finality = EnumSet.of(Finality.ALL);
		}

		// Examine the parent
		XMLComponent<?> parent = simpleType.getParent();

		// Get the List class without importing it and clashing with
		// java.util.List
		Class<com.mcdermottroe.exemplar.generated.schema.element.List>
			listClass =
			com.mcdermottroe.exemplar.generated.schema.element.List.class;

		// Get the children
		List<XMLComponent<?>> children = simpleType.getChildren();

		// Examine the content model
		Annotation annotation = null;
		com.mcdermottroe.exemplar.generated.schema.element.List list = null;
		Restriction restriction = null;
		Union union = null;
		for (XMLComponent<?> xc : children) {
			Class<?> xcClass = xc.getClass();
			if (Annotation.class.isAssignableFrom(xcClass)) {
				if (annotation == null) {
					annotation = Annotation.class.cast(xc);
				} else {
					throw new W3CSchemaException(
						Message.SCHEMA_BAD_SIMPLE_TYPE_CONTENT_MODEL(name)
					);
				}
			} else if (listClass.isAssignableFrom(xcClass)) {
				if (list == null && restriction == null && union == null) {
					list = listClass.cast(xc);
				} else {
					throw new W3CSchemaException(
						Message.SCHEMA_BAD_SIMPLE_TYPE_CONTENT_MODEL(name)
					);
				}
			} else if (Restriction.class.isAssignableFrom(xcClass)) {
				if (list == null && restriction == null && union == null) {
					restriction = Restriction.class.cast(xc);
				} else {
					throw new W3CSchemaException(
						Message.SCHEMA_BAD_SIMPLE_TYPE_CONTENT_MODEL(name)
					);
				}
			} else if (Union.class.isAssignableFrom(xcClass)) {
				if (list == null && restriction == null && union == null) {
					union = Union.class.cast(xc); 
				} else {
					throw new W3CSchemaException(
						Message.SCHEMA_BAD_SIMPLE_TYPE_CONTENT_MODEL(name)
					);
				}
			} else if (XMLContent.class.isAssignableFrom(xcClass)) {
				XMLContent content = XMLContent.class.cast(xc);
				if (!content.isAllWhitespace()) {
					throw new W3CSchemaException(
						Message.SCHEMA_BAD_SIMPLE_TYPE_CONTENT_MODEL(name)
					);
				}
			}
		}

		// Now do things based on the type of the child
		if (list != null) {
			if (name != null) {
				try {
					TypeManager.addType(
						TypeManager.extendByList(
							name,
							finality,
							TypeManager.getType(list.getItemType())
						)
					);
				} catch (ParserException e) {
					throw new W3CSchemaException(e);
				}
			} else {
				try {
					TypeManager.addType(
						simpleType,
						TypeManager.extendByList(
							name,
							finality,
							TypeManager.getType(list.getItemType())
						)
					);
				} catch (ParserException e) {
					throw new W3CSchemaException(e);
				}
			}
		} else if (restriction != null) {
			// Read the content model of the restriction
			Annotation a = null;
			Type baseType = null;
			Set<Facet> additionalFacets = new HashSet<Facet>();
			for (XMLComponent<?> rc : restriction.getChildren()) {
				Class<?> rcClass = rc.getClass();
				if (XMLContent.class.isAssignableFrom(rcClass)) {
					XMLContent content = XMLContent.class.cast(rc);
					if (!content.isAllWhitespace()) {
						// SCHEMATASK: message
						throw new W3CSchemaException(
							"Bad content for restriction - rogue CDATA"
						);
					}
				} else if (Annotation.class.isAssignableFrom(rcClass)) {
					if	(
							a == null &&
							baseType == null &&
							additionalFacets.isEmpty()
						)
					{
						a = Annotation.class.cast(rc);
					} else {
						// SCHEMATASK: message
						throw new W3CSchemaException(
							"Bad content for restriction - misplaced annotation"
						);
					}
				} else if (Enumeration.class.isAssignableFrom(rcClass)) {
					Enumeration enumeration = Enumeration.class.cast(rc);
					additionalFacets.add(
						new EnumerationFacet(enumeration.getValue())
					);
				} else if (FractionDigits.class.isAssignableFrom(rcClass)) {
					// SCHEMATASK
				} else if (Length.class.isAssignableFrom(rcClass)) {
					// SCHEMATASK
				} else if (MaxExclusive.class.isAssignableFrom(rcClass)) {
					// SCHEMATASK
				} else if (MaxInclusive.class.isAssignableFrom(rcClass)) {
					// SCHEMATASK
				} else if (MaxLength.class.isAssignableFrom(rcClass)) {
					// SCHEMATASK
				} else if (MinExclusive.class.isAssignableFrom(rcClass)) {
					// SCHEMATASK
				} else if (MinInclusive.class.isAssignableFrom(rcClass)) {
					// SCHEMATASK
				} else if (MinLength.class.isAssignableFrom(rcClass)) {
					// SCHEMATASK
				} else if (Pattern.class.isAssignableFrom(rcClass)) {
					// SCHEMATASK
				} else if (SimpleType.class.isAssignableFrom(rcClass)) {
					SimpleType baseSimpleType = SimpleType.class.cast(rc);
					processSimpleType(baseSimpleType);
					try {
						baseType = TypeManager.getType(baseSimpleType);
					} catch (ParserException e) {
						throw new W3CSchemaException(e);
					}
				} else if (TotalDigits.class.isAssignableFrom(rcClass)) {
					// SCHEMATASK
				} else if (WhiteSpace.class.isAssignableFrom(rcClass)) {
					// SCHEMATASK
				} else {
					// SCHEMATASK
					throw new W3CSchemaException(rcClass.getName());
				}
			}

			// Figure out the base type
			if (baseType == null && restriction.getBase() != null) {
				try {
					baseType = TypeManager.getType(restriction.getBase());
				} catch (ParserException e) {
					throw new W3CSchemaException(e);
				}
			}

			// Now extend the base type
			if (name != null) {
				try {
					TypeManager.addType(
						TypeManager.extendByRestriction(
							name,
							baseType,
							finality,
							additionalFacets.toArray(new Facet[] {})
						)
					);
				} catch (ParserException e) {
					throw new W3CSchemaException(e);
				}
			} else {
				try {
					TypeManager.addType(
						simpleType,
						TypeManager.extendByRestriction(
							name,
							baseType,
							finality,
							additionalFacets.toArray(new Facet[] {})
						)
					);
				} catch (ParserException e) {
					throw new W3CSchemaException(e);
				}
			}
		} else if (union != null) {
			// SCHEMATASK
			System.err.println("\tUnion of " + union.getMemberTypes());
		} else {
			// SCHEMATASK: message
			throw new W3CSchemaException(
				"Bad content for SimpleType - no child"
			);
		}
	}

	/** {@inheritDoc} */
	public TypeProcessor getCopy()
	throws CopyException
	{
		return new TypeProcessor();
	}

	/** {@inheritDoc} */
	public int compareTo(TypeProcessor other) {
		return Utils.compare(getClass().getName(), other.getClass().getName());
	}

	/** Generage a hash code for this {@link TypeProcessor}.

		@return	A hashcode for this object.
	*/
	@Override public int hashCode() {
		return getClass().hashCode();
	}

	/** Test for equality against another object.

		@param	o	The {@link Object} to test against.
		@return		True if this is equal to o, false otherwise.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof TypeProcessor)) {
			return false;
		}

		return compareTo(TypeProcessor.class.cast(o)) == 0;
	}
}
