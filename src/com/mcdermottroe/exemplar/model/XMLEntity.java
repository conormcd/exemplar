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
package com.mcdermottroe.exemplar.model;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.ui.Message;

/** An {@link XMLObject} which represents general entities.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLEntity
extends XMLObject
implements Constants.XML.Entity, XMLObject.HasName
{
	/** The type of entity this is. This should be one of the types defined in
		{@link com.mcdermottroe.exemplar.Constants.XML.Entity}.
	*/
	private int entityType;

	/** The immediate value of this entity. This is only set for internal
		entities (entities with an {@link #entityType} of {@link
		com.mcdermottroe.exemplar.Constants.XML.Entity#INTERNAL}).
	*/
	private String value;

	/**	The external identifier of this entity. This is only set for external
		entities (entities with an {@link #entityType} of {@link
		com.mcdermottroe.exemplar.Constants.XML.Entity#EXTERNAL_PARSED}).
	*/
	private XMLExternalIdentifier extID;

	/** The notation associated with this entity. This will only be set if the
		entity is an external unparsed entity (entities with an {@link
		#entityType} of {@link
		com.mcdermottroe.exemplar.Constants.XML.Entity#EXTERNAL_UNPARSED}).
	*/
	private String notation;

	/** Overload the default constructor from {@link XMLObject}. */
	public XMLEntity() {
		// Do the basic initialisation.
		super();

		// Mark this entity as uninitialised.
		entityType = UNINITIALISED;

		// It doesn't have a value
		value = null;
		extID = null;
		notation = null;
	}

	/** Constructor for internal entities ({@link #entityType} == {@link
		com.mcdermottroe.exemplar.Constants.XML.Entity#INTERNAL}).

		@param	entityValue	The replacement text for this entity.
	*/
	public XMLEntity(String entityValue) {
		// Do the basic initialisation
		super();

		DBC.REQUIRE(entityValue != null);

		// Mark this entity as internal.
		entityType = INTERNAL;

		// Copy in the parameters.
		value = entityValue;

		// There is no notation or external ID associated with this entity
		extID = null;
		notation = null;
	}

	/** Constructor for external parsed entities ({@link #entityType} == {@link
		com.mcdermottroe.exemplar.Constants.XML.Entity#EXTERNAL_PARSED}).

		@param externalID	The URI + PublicID of where the content is.
	*/
	public XMLEntity(XMLExternalIdentifier externalID) {
		// Do the basic initialisation
		super();

		DBC.REQUIRE(externalID != null);

		// Mark this entity as an external parsed one
		entityType = EXTERNAL_PARSED;

		// Copy in the parameters
		extID = externalID;

		// It doesn't have a notation or an immediate value
		value = null;
		notation = null;
	}

	/** Constructor for external unparsed entities ({@link #entityType} ==
		{@link
		com.mcdermottroe.exemplar.Constants.XML.Entity#EXTERNAL_UNPARSED}).

		@param externalID	The URI + PublicID  of where the content is.
		@param not			The notation associated with this entity
	*/
	public XMLEntity(XMLExternalIdentifier externalID, String not) {
		// Do the basic initialisation
		super();

		DBC.REQUIRE(externalID != null);
		DBC.REQUIRE(not != null);

		// Mark this entity as an external unparsed one
		entityType = EXTERNAL_UNPARSED;

		// Copy in the parameters
		extID = externalID;
		notation = not;

		// It doesn't have an immediate value
		value = null;
	}

	/** Accessor for {@link #entityType}.

		@return An integer describing the type of entity this is. The integer
				will be one of {@link #UNINITIALISED}, {@link #INTERNAL},
				{@link #EXTERNAL_PARSED} or {@link #EXTERNAL_UNPARSED}.
	*/
	public int type() {
		DBC.ENSURE	(
						entityType == UNINITIALISED ||
						entityType == INTERNAL ||
						entityType == EXTERNAL_PARSED ||
						entityType == EXTERNAL_UNPARSED
					);
		return entityType;
	}

	/** Accessor for {@link #value}.

		@return The replacement text for this entity if it is an internal
				entity, null otherwise.
	*/
	public String value() {
		DBC.REQUIRE	(
						entityType == INTERNAL && value != null ||
						entityType != INTERNAL && value == null
					);
		return value;
	}

	/** Accessor for {@link #extID}.

		@return The {@link XMLExternalIdentifier} for this entity if it is an
				external entity, null otherwise.
	*/
	public XMLExternalIdentifier externalID() {
		DBC.REQUIRE	(
						entityType == EXTERNAL_PARSED && extID != null ||
						entityType == EXTERNAL_UNPARSED && extID != null ||
						entityType == UNINITIALISED && extID == null ||
						entityType == INTERNAL && extID == null
					);
		return extID;
	}

	/** Accessor for {@link #notation}.

		@return	The notation for this entity, if it is an unparsed external
				entity, null otherwise.
	*/
	public String getNotation() {
		DBC.REQUIRE	(
						entityType == EXTERNAL_UNPARSED && notation != null ||
						entityType != EXTERNAL_UNPARSED && notation == null
					);
		return notation;
	}

	/** Tell whether the entity is an internal entity.

		@return	True if the entity is internal, false otherwise.
	*/
	public boolean isInternal() {
		DBC.ENSURE	(
						entityType == INTERNAL ||
						entityType == EXTERNAL_PARSED ||
						entityType == EXTERNAL_UNPARSED
					);
		return entityType == INTERNAL;
	}

	/** {@inheritDoc} */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || !(o instanceof XMLEntity)) {
			return false;
		}

		XMLEntity other = (XMLEntity)o;
		if (super.equals(o)) {
			Object[] thisFields = {
				new Integer(entityType),
				extID,
				notation,
				value,
			};
			Object[] otherFields = {
				new Integer(other.type()),
				other.externalID(),
				other.getNotation(),
				other.value(),
			};
			return Utils.areAllDeeplyEqual(thisFields, otherFields);
		}

		return false;
	}

	/** {@inheritDoc} */
	public int hashCode() {
		int hashCode = super.hashCode();

		hashCode *= Constants.HASHCODE_MAGIC_NUMBER;
		hashCode += entityType;
		hashCode *= Constants.HASHCODE_MAGIC_NUMBER;
		if (extID != null) {
			hashCode += extID.hashCode();
		}
		hashCode *= Constants.HASHCODE_MAGIC_NUMBER;
		if (notation != null) {
			hashCode += notation.hashCode();
		}
		hashCode *= Constants.HASHCODE_MAGIC_NUMBER;
		if (value != null) {
			hashCode += value.hashCode();
		}

		return hashCode;
	}

	/** {@inheritDoc} */
	public String toString() {
		StringBuffer desc = new StringBuffer(
			toStringPrefix(
				getClass().getName()
			)
		);

		desc.append(name);
		desc.append(Constants.Character.COMMA);
		desc.append(Constants.Character.SPACE);
		if (entityType == INTERNAL) {
			desc.append(value());
		} else if	(
						entityType == EXTERNAL_PARSED ||
						entityType == EXTERNAL_UNPARSED
					)
		{
			desc.append(Constants.Character.LEFT_PAREN);
			desc.append(externalID().toString());
			desc.append(Constants.Character.RIGHT_PAREN);
		} else {
			desc.append(Message.XMLOBJECT_NOT_CONFIGURED);
		}

		desc.append(toStringSuffix());

		return desc.toString();
	}
}
