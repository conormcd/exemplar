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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.ui.Message;

import static com.mcdermottroe.exemplar.Constants.Character.LEFT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.RIGHT_PAREN;

/** A class representing an XML DTD/Schema of some sort.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLDocumentType
implements Copyable<XMLDocumentType>
{
	/** A map of all the elements in this DTD/Schema. */
	private final Map<String, XMLMarkupDeclaration> elements;

	/** A map of all the attribute lists in this DTD/Schema. */
	private final Map<String, XMLMarkupDeclaration> attlists;

	/** A map of all the entities in this DTD/Schema. */
	private final Map<String, XMLMarkupDeclaration> entities;

	/** A map of all the notations in this DTD/Schema. */
	private final Map<String, XMLMarkupDeclaration> notations;

	/** Make an {@link XMLDocumentType} out of a {@link Collection} of markup
		declarations.

		@param	markup						A {@link Collection} of markup
											declarations.
		@throws	XMLDocumentTypeException	if any of the initialising methods
											called from here throw an {@link
											XMLDocumentTypeException}.
	*/
	public XMLDocumentType(Collection<XMLMarkupDeclaration> markup)
	throws XMLDocumentTypeException
	{
		DBC.REQUIRE(markup != null);

		// Allocate storage for the various hashes
		int markupSize = 0;
		if (markup != null) {
			markupSize = markup.size();
		}
		attlists = new HashMap<String, XMLMarkupDeclaration>(markupSize);
		elements = new HashMap<String, XMLMarkupDeclaration>(markupSize);
		entities = new HashMap<String, XMLMarkupDeclaration>(markupSize);
		notations = new HashMap<String, XMLMarkupDeclaration>(markupSize);

		// Go through the list of markup declarations and put the objects in
		// the correct hashes.
		if (markup != null) {
			for (XMLMarkupDeclaration xmlObject : markup) {
				if (xmlObject == null) {
					continue;
				}
				Class<? extends XMLMarkupDeclaration> c = xmlObject.getClass();
				if (XMLAttributeList.class.isAssignableFrom(c)) {
					attlists.put(xmlObject.getName(), xmlObject);
				} else if (XMLElement.class.isAssignableFrom(c)) {
					elements.put(xmlObject.getName(), xmlObject);
				} else if (XMLEntity.class.isAssignableFrom(c)) {
					entities.put(xmlObject.getName(), xmlObject);
				} else if (XMLNotation.class.isAssignableFrom(c)) {
					notations.put(xmlObject.getName(), xmlObject);
				}
			}
		}

		// Associate attributes with their elements.
		for (String name : attlists.keySet()) {
			// Get the XMLAttributeList and corresponding XMLElement
			XMLAttributeList attlist = (XMLAttributeList)attlists.get(name);
			XMLElement element = (XMLElement)elements.get(name);

			// Associate the two
			if (element != null) {
				element.setAttlist(attlist);
				elements.put(name, element);
			} else {
				throw new XMLDocumentTypeException(
					Message.XMLDOCTYPE_ORPHAN_ATTLIST(name)
				);
			}
		}
	}

	/** Shorthand for finding out if the current document type declares any
		attribute lists.

		@return	True if the document type declares attribute lists and false in
				every other case, including if errors occur.
	*/
	public boolean hasAttlists() {
		return attlists != null && !attlists.isEmpty();
	}

	/** Accessor for elements.

		@return	A {@link Map} (keyed on the names) of the elements declared in
				this {@link XMLDocumentType}.
	*/
	public Map<String, XMLMarkupDeclaration> elements() {
		return new HashMap<String, XMLMarkupDeclaration>(elements);
	}

	/** Accessor for attribute lists.

		@return	A {@link Map} (keyed on the names) of the attribute lists
				declared in this {@link XMLDocumentType}
	*/
	public Map<String, XMLMarkupDeclaration> attlists() {
		return new HashMap<String, XMLMarkupDeclaration>(attlists);
	}

	/** Accessor for entities.

		@return A {@link Map} (keyed on the names) of the general entities
				declared in this {@link XMLDocumentType}.
	*/
	public Map<String, XMLMarkupDeclaration> entities() {
		return new HashMap<String, XMLMarkupDeclaration>(entities);
	}

	/** Accessor for notations.

		@return	A {@link Map} (keyed on the names) of the notations declared in
				this {@link XMLDocumentType}.
	*/
	public Map<String, XMLMarkupDeclaration> notations() {
		return new HashMap<String, XMLMarkupDeclaration>(notations);
	}

    /** {@inheritDoc} */
    public XMLDocumentType getCopy()
	throws CopyException
	{
		try {
			List<XMLMarkupDeclaration> all;
			all = new ArrayList<XMLMarkupDeclaration>();
			all.addAll(attlists.values());
			all.addAll(elements.values());
			all.addAll(entities.values());
			all.addAll(notations.values());
			return new XMLDocumentType(all);
		} catch (XMLDocumentTypeException e) {
			throw new CopyException(e);
		}
	}

	/** See {@link Object#equals(Object)}.

		@param	o	The object to compare against.
		@return		True if <code>this</code> is equal to <code>o</code>.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || !(o instanceof XMLDocumentType)) {
			return false;
		}

		XMLDocumentType other = (XMLDocumentType)o;
		Object[] thisFields = {
			attlists,
			elements,
			entities,
			notations,
		};
		Object[] otherFields = {
			other.attlists(),
			other.elements(),
			other.entities(),
			other.notations(),
		};
		return Utils.areAllDeeplyEqual(thisFields, otherFields);
	}

	/** See {@link Object#hashCode()}.

		@return	A hash code.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(attlists, elements, entities, notations);
	}

	/** See {@link Object#toString()}.

		@return	A descriptive {@link String}.
	*/
	@Override public String toString() {
		StringBuilder description = new StringBuilder();
		description.append(getClass().getName());
		description.append(LEFT_PAREN);
		description.append(hashCode());
		description.append(RIGHT_PAREN);
		return description.toString();
	}
}
