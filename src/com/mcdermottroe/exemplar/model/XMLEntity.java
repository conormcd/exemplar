// vim:filetype=java:ts=4
/*
	Copyright (c) 2004-2007
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
package com.mcdermottroe.exemplar.model;

import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;

import static com.mcdermottroe.exemplar.Constants.Character.COMMA;
import static com.mcdermottroe.exemplar.Constants.Character.LEFT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.RIGHT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.SPACE;
import static com.mcdermottroe.exemplar.Constants.HASHCODE_MAGIC_NUMBER;

/** An {@link XMLObject} which represents general entities.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLEntity
extends XMLNamedObject<XMLEntity>
implements XMLMarkupDeclaration
{
	/** The type of entity this is. */
	private final XMLEntityType entityType;

	/** The immediate value of this entity. This is only set for internal
		entities (entities with an {@link #entityType} of {@link
		XMLEntityType#INTERNAL}).
	*/
	private final String value;

	/**	The external identifier of this entity. This is only set for external
		entities (entities with an {@link #entityType} of {@link
		XMLEntityType#EXTERNAL_PARSED}).
	*/
	private final XMLExternalIdentifier extID;

	/** The notation associated with this entity. This will only be set if the
		entity is an external unparsed entity (entities with an {@link
		#entityType} of {@link XMLEntityType#EXTERNAL_UNPARSED}).
	*/
	private final String notation;

	/** Constructor for internal entities ({@link #entityType} == {@link
		XMLEntityType#INTERNAL}).

		@param	entityName	The name of this entity.
		@param	entityValue	The replacement text for this entity.
	*/
	public XMLEntity(String entityName, String entityValue) {
		// Do the basic initialisation
		super(entityName);
		DBC.REQUIRE(entityValue != null);

		// Mark this entity as internal.
		entityType = XMLEntityType.INTERNAL;

		// Copy in the parameters.
		value = entityValue;

		// There is no notation or external ID associated with this entity
		extID = null;
		notation = null;
	}

	/** Constructor for external parsed entities ({@link #entityType} == {@link
		XMLEntityType#EXTERNAL_PARSED}).

		@param	entityName	The name of this entity.
		@param	externalID	The URI + PublicID of where the content is.
	*/
	public XMLEntity(String entityName, XMLExternalIdentifier externalID) {
		// Do the basic initialisation
		super(entityName);
		DBC.REQUIRE(externalID != null);

		// Mark this entity as an external parsed one
		entityType = XMLEntityType.EXTERNAL_PARSED;

		// Copy in the parameters
		extID = externalID;

		// It doesn't have a notation or an immediate value
		value = null;
		notation = null;
	}

	/** Constructor for external unparsed entities ({@link #entityType} ==
		{@link XMLEntityType#EXTERNAL_UNPARSED}).

		@param	entityName	The name of this entity.
		@param	externalID	The URI + PublicID  of where the content is.
		@param	not			The notation associated with this entity
	*/
	public XMLEntity(
						String entityName,
						XMLExternalIdentifier externalID,
						String not
					)
	{
		// Do the basic initialisation
		super(entityName);
		DBC.REQUIRE(externalID != null);
		DBC.REQUIRE(not != null);

		// Mark this entity as an external unparsed one
		entityType = XMLEntityType.EXTERNAL_UNPARSED;

		// Copy in the parameters
		extID = externalID;
		notation = not;

		// It doesn't have an immediate value
		value = null;
	}

	/** Accessor for {@link #entityType}.

		@return The type of this entity.
	*/
	public XMLEntityType type() {
		return entityType;
	}

	/** Accessor for {@link #value}.

		@return The replacement text for this entity if it is an internal
				entity, null otherwise.
	*/
	public String value() {
		return value;
	}

	/** Accessor for {@link #extID}.

		@return The {@link XMLExternalIdentifier} for this entity if it is an
				external entity, null otherwise.
	*/
	public XMLExternalIdentifier externalID() {
		return extID;
	}

	/** Accessor for {@link #notation}.

		@return	The notation for this entity, if it is an unparsed external
				entity, null otherwise.
	*/
	public String getNotation() {
		return notation;
	}

	/** Tell whether the entity is an internal entity.

		@return	True if the entity is internal, false otherwise.
	*/
	public boolean isInternal() {
		return entityType.equals(XMLEntityType.INTERNAL);
	}

	/** {@inheritDoc} */
	@Override public XMLEntity getCopy() {
		if (notation != null) {
			return new XMLEntity(name, extID.getCopy(), notation);
		} else if (extID != null) {
			return new XMLEntity(name, extID.getCopy());
		} else {
			return new XMLEntity(name, value);
		}
	}

	/** {@inheritDoc} */
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || !(o instanceof XMLEntity)) {
			return false;
		}

		XMLEntity other = (XMLEntity)o;
		if (super.equals(o)) {
			Object[] thisFields = {
				entityType,
				extID,
				notation,
				value,
			};
			Object[] otherFields = {
				other.type(),
				other.externalID(),
				other.getNotation(),
				other.value(),
			};
			return Utils.areAllDeeplyEqual(thisFields, otherFields);
		}

		return false;
	}

	/** {@inheritDoc} */
	@Override public int hashCode() {
		int hashCode = super.hashCode();
		hashCode *= HASHCODE_MAGIC_NUMBER;
		hashCode += Utils.genericHashCode(entityType, extID, notation, value);
		return hashCode;
	}

	/** {@inheritDoc} */
	@Override public String toString() {
		StringBuilder desc = new StringBuilder();
		desc.append(name);
		desc.append(COMMA);
		desc.append(SPACE);
		desc.append(entityType);
		desc.append(COMMA);
		desc.append(SPACE);
		switch (entityType) {
			case INTERNAL:
				desc.append(value());
				break;
			case EXTERNAL_PARSED:
			case EXTERNAL_UNPARSED:
				desc.append(LEFT_PAREN);
				desc.append(externalID().toString());
				desc.append(RIGHT_PAREN);
				break;
		}

		return XMLObject.toStringHelper(getClass().getName(), desc.toString());
	}
}
