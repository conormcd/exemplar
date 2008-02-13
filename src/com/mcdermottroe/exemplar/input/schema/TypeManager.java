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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mcdermottroe.exemplar.generated.schema.element.ComplexType;
import com.mcdermottroe.exemplar.generated.schema.element.SimpleType;
import com.mcdermottroe.exemplar.input.ParserException;
import com.mcdermottroe.exemplar.input.schema.type.AtomicType;
import com.mcdermottroe.exemplar.input.schema.type.Finality;
import com.mcdermottroe.exemplar.input.schema.type.ListType;
import com.mcdermottroe.exemplar.input.schema.type.Type;
import com.mcdermottroe.exemplar.input.schema.type.UnionType;
import com.mcdermottroe.exemplar.input.schema.type.facet.Facet;

/** This is a manager for the types declared in a schema.

	@author Conor McDermottroe
	@since	0.2
*/
public final class TypeManager {
	/** Types indexed by name. */
	private static final Map<String, Type> typesByName =
		new HashMap<String, Type>();

	/** Types indexed by simple type. */
	private static final Map<SimpleType, Type> typesBySimpleType =
		new HashMap<SimpleType, Type>();

	/** Types indexed by complex type. */
	private static final Map<ComplexType, Type> typesByComplexType =
		new HashMap<ComplexType, Type>();

	/** Prevent instantiation of this class. */
	private TypeManager() {
	}

	// Initialise the built-in types.
	static {
		try {
			initBuiltInTypes();
		} catch (ParserException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/** Add all of the built-in types to this {@link TypeManager}.

		@throws	ParserException	if any of the types cannot be added.
	*/
	private static void initBuiltInTypes()
	throws ParserException
	{
		// Make sure this only runs once
		if	(
				!(
					typesByName.isEmpty() &&
					typesBySimpleType.isEmpty() &&
					typesByComplexType.isEmpty()
				)
			)
		{
			return;
		}

		// A finality set of NONE
		Set<Finality> finalityNone = EnumSet.of(Finality.NONE);

		// Add the built-in types
		addType(new AtomicType("xs:anytype", true, finalityNone));
		addType(new AtomicType("xs:anySimpleType", false, finalityNone));
		addType(
			extendByRestriction(
				"xs:string",
				getType("xs:anySimpleType"),
				finalityNone
			)
		);
		addType(
			extendByRestriction(
				"xs:normalizedString",
				getType("xs:string"),
				finalityNone
			)
		);
		addType(
			extendByRestriction(
				"xs:token",
				getType("xs:normalizedString"),
				finalityNone
			)
		);
		addType(
			extendByRestriction(
				"xs:language",
				getType("xs:token"),
				finalityNone
			)
		);
		addType(
			extendByRestriction(
				"xs:Name",
				getType("xs:token"),
				finalityNone
			)
		);
		addType(
			extendByRestriction(
				"xs:NMTOKEN",
				getType("xs:token"),
				finalityNone
			)
		);
		addType(
			extendByRestriction(
				"xs:NCName",
				getType("xs:Name"),
				finalityNone
			)
		);
		addType(
			extendByList(
				"xs:NMTOKENS",
				finalityNone,
				getType("xs:NMTOKEN")
			)
		);
		addType(
			extendByRestriction(
				"xs:ID",
				getType("xs:NCName"),
				finalityNone
			)
		);
		addType(
			extendByRestriction(
				"xs:IDREF",
				getType("xs:NCName"),
				finalityNone
			)
		);
		addType(
			extendByRestriction(
				"xs:ENTITY",
				getType("xs:NCName"),
				finalityNone
			)
		);
		addType(
			extendByList(
				"xs:IDREFS",
				finalityNone,
				getType("xs:IDREF")
			)
		);
		addType(
			extendByList(
				"xs:ENTITIES",
				finalityNone,
				getType("xs:ENTITY")
			)
		);
	}

	/** Retrieve a type based on its name.

		@param	name			The name of the type to retrieve.
		@return					The type with the given name.
		@throws	ParserException	if the named type does not exist.
	*/
	public static Type getType(String name)
	throws ParserException
	{
		if (typesByName.containsKey(name)) {
			return typesByName.get(name);
		} else {
			// SCHEMATASK: message
			throw new ParserException("No such type: " + name);
		}
	}

	/** SCHEMATASK.

		@param	type			SCHEMATASK
		@return					SCHEMATASK
		@throws	ParserException	SCHEMATASK
	*/
	public static Type getType(SimpleType type)
	throws ParserException
	{
		if (typesBySimpleType.containsKey(type)) {
			return typesBySimpleType.get(type);
		} else {
			// SCHEMATASK: message
			throw new ParserException("No type for SimpleType: " + type);
		}
	}

	/** SCHEMATASK.

		@param	type			SCHEMATASK
		@return					SCHEMATASK
		@throws	ParserException	SCHEMATASK
	*/
	public static Type getType(ComplexType type)
	throws ParserException
	{
		if (typesByComplexType.containsKey(type)) {
			return typesByComplexType.get(type);
		} else {
			// SCHEMATASK: message
			throw new ParserException("No type for ComplexType: " + type);
		}
	}

	/** Add a new {@link Type}.

		@param	t				The {@link Type} to add.
		@throws	ParserException	if the {@link Type} has no name.
	*/
	public static void addType(Type t)
	throws ParserException
	{
		String typeName = t.getName();
		if (typeName != null) {
			addType(typeName, t);
		} else {
			// SCHEMATASK: message
			throw new ParserException("No name for type");
		}
	}

	/** Add a new named {@link Type}.

		@param	typeName		The name of the type to add.
		@param	t				The {@link Type} to add.
		@throws ParserException	if the {@link Type} already exists.
	*/
	private static void addType(String typeName, Type t)
	throws ParserException
	{
		if (typesByName.containsKey(typeName)) {
			if (!t.equals(typesByName.get(typeName))) {
				// SCHEMATASK: message
				throw new ParserException(
					"Can't redefine type \"" +
						typeName +
						"\" from: " +
						typesByName.get(typeName) +
						" to: " +
						t
				);
			}
		}
		typesByName.put(typeName, t);
	}

	/** SCHEMATASK.

		@param	type			SCHEMATASK
		@param	t				The {@link Type} to add.
		@throws ParserException	if the {@link Type} already exists.
	*/
	public static void addType(SimpleType type, Type t)
	throws ParserException
	{
		if (typesBySimpleType.containsKey(type)) {
			if (!t.equals(typesBySimpleType.get(type))) {
				// SCHEMATASK: message
				throw new ParserException(
					"Can't redefine type for: " +
						type +
						" from: " +
						typesBySimpleType.get(type) +
						" to: " +
						t
				);
			}
		}
		typesBySimpleType.put(type, t);
	}

	/** SCHEMATASK.

		@param	type			SCHEMATASK
		@param	t				The {@link Type} to add.
		@throws ParserException	if the {@link Type} already exists.
	*/
	public static void addType(ComplexType type, Type t)
	throws ParserException
	{
		if (typesByComplexType.containsKey(type)) {
			if (!t.equals(typesByComplexType.get(type))) {
				// SCHEMATASK: message
				throw new ParserException(
					"Can't redefine type for: " +
						type +
						" from: " +
						typesByComplexType.get(type) +
						" to: " +
						t
				);
			}
		}
		typesByComplexType.put(type, t);
	}

	/** Extend a {@link Type} by restriction.

		@param	name			The name of the new {@link Type}.
		@param	baseType		The {@link Type} to extend.
		@param	finality		The finality restrictions to apply to the new
								{@link Type}.
		@param	facets			The {@link Facet}s to add to the new {@link
								Type}.
		@return					The newly created {@link Type}.
		@throws	ParserException	if the base type cannot be extended by
								restriction.
		@param	<T>				The type of {@link Type} the base type is.
	*/
	public static <T extends Type<T>> AtomicType extendByRestriction(
		String name,
		T baseType,
		Set<Finality> finality,
		Facet... facets
	)
	throws ParserException
	{
		if (baseType.isFinal(Finality.RESTRICTION)) {
			// SCHEMATASK: message
			throw new ParserException("Type " + baseType + " is final");
		}
		AtomicType newType = new AtomicType(
			name,
			baseType.isComplexType(),
			finality,
			(Facet[])baseType.getFacets().toArray(new Facet[] {})
		);
		for (Facet f : facets) {
			newType.addFacet(f);
		}
		return newType;
	}

	/** Extend a {@link Type} by restriction.

		@param	name			The name of the new {@link Type}.
		@param	finality		The finality restrictions to apply to the new
								{@link Type}.
		@param	typesToUnion	The {@link Type}s to union to create the new
								{@link Type}.
		@return					The newly created {@link Type}.
		@throws	ParserException	if the base type cannot be extended by
								union.
		@param	<T>				The type of {@link Type} the unioned types are.
	 */
	public static <T extends Type<T>> UnionType extendByUnion(
		String name,
		Set<Finality> finality,
		T... typesToUnion
	)
	throws ParserException
	{
		for (T type : typesToUnion) {
			if (type.isFinal(Finality.UNION)) {
				// SCHEMATASK: message
				throw new ParserException("Type " + type + " is final");
			}
		}
		return new UnionType(name, finality, typesToUnion);
	}

	/** Extend a {@link Type} by creating a list of a type.

		@param	name			The name of the new derived type.
		@param	finality		The finality restrictions to apply to the new
								{@link Type}.
		@param	listType		The type to list.
		@return					The newly created {@link Type}.
		@throws	ParserException	if the base type cannot be extended by list.
		@param	<T>				The type of {@link Type} the listed type is.
	*/
	public static <T extends Type<T>> ListType<T> extendByList(
		String name,
		Set<Finality> finality,
		T listType
	)
	throws ParserException
	{
		if (listType.isFinal(Finality.LIST)) {
			// SCHEMATASK: message
			throw new ParserException("Type " + listType + " is final");
		}
		return new ListType<T>(name, finality, listType);
	}
}
