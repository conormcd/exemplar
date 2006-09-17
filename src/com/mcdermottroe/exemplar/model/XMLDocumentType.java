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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.ui.Message;

/** A class representing an XML DTD/Schema of some sort.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLDocumentType {
	/** Enum value for {@link XMLDocumentType}s which define elements with
		attribute lists.
	*/
	private static final Integer ATTLISTS = new Integer(0);

	/** Enum value for {@link XMLDocumentType}s which define elements. */
	private static final Integer ELEMENTS = new Integer(1);

	/** Enum value for {@link XMLDocumentType}s which define entities. */
	private static final Integer ENTITIES = new Integer(2);

	/** Enum value for {@link XMLDocumentType}s which define notations. */
	private static final Integer NOTATIONS = new Integer(3);

	/** A list of all the markup declarations in the document type. */
	private List markupDecls;

	/** A map of all the features that this DTD/Schema uses. */
	private Map feature;

	/** A map of all the elements in this DTD/Schema. */
	private Map elements;

	/** A map of all the attribute lists in this DTD/Schema. */
	private Map attlists;

	/** A map of all the entities in this DTD/Schema. */
	private Map entities;

	/** A map of all the notations in this DTD/Schema. */
	private Map notations;

	/** A no-arg constructor to aid in testing. This effectively creates an
		empty vocabulary description. It's useless for actually generating
		anything.

		@throws	XMLDocumentTypeException	if either {@link
											#separateObjects()} or {@link
											#associateAttlistsWithElements()}
											throws one.
	*/
	public XMLDocumentType()
	throws XMLDocumentTypeException
	{
		markupDecls = new ArrayList();
		feature = null;
		elements = null;
		attlists = null;
		entities = null;
		notations = null;

		separateObjects();
		associateAttlistsWithElements();
		calculateFeatures();
	}

	/** Make an {@link XMLDocumentType} out of a {@link List} of markup
		declarations.

		@param	markup						A {@link List} of markup
											declarations.
		@throws	XMLDocumentTypeException	if any of the initialising methods
											called from here throw an {@link
											XMLDocumentTypeException}.
	*/
	public XMLDocumentType(List markup)
	throws XMLDocumentTypeException
	{
		DBC.REQUIRE(markup != null);

		markupDecls = new ArrayList(markup);
		feature = null;
		elements = null;
		attlists = null;
		entities = null;
		notations = null;

		separateObjects();
		associateAttlistsWithElements();
		calculateFeatures();
	}

	/** Determine what features of DTDs/Schemas the current {@link
		XMLDocumentType} uses.
	*/
	private void calculateFeatures() {
		DBC.REQUIRE(attlists != null);
		DBC.REQUIRE(elements != null);
		DBC.REQUIRE(entities != null);
		DBC.REQUIRE(notations != null);

		// Ensure there is storage for the features
		feature = new HashMap();

		// Calculate some features
		if (attlists.isEmpty()) {
			feature.put(ATTLISTS, Boolean.FALSE);
		} else {
			feature.put(ATTLISTS, Boolean.TRUE);
		}
		if (elements.isEmpty()) {
			feature.put(ELEMENTS, Boolean.FALSE);
		} else {
			feature.put(ELEMENTS, Boolean.TRUE);
		}
		if (entities.isEmpty()) {
			feature.put(ENTITIES, Boolean.FALSE);
		} else {
			feature.put(ENTITIES, Boolean.TRUE);
		}
		if (notations.isEmpty()) {
			feature.put(NOTATIONS, Boolean.FALSE);
		} else {
			feature.put(NOTATIONS, Boolean.TRUE);
		}

		DBC.ENSURE(feature != null);
	}

	/** Rip through the vector of markup declarations and separate them into
		their respective hashes.

		@throws	XMLDocumentTypeException	if any of the objects in {@link
											#markupDecls} cannot be classified
											according to their types.
	*/
	private void separateObjects()
	throws XMLDocumentTypeException
	{
		DBC.REQUIRE(markupDecls != null);

		// Allocate storage for the various hashes
		attlists = new HashMap();
		elements = new HashMap();
		entities = new HashMap();
		notations = new HashMap();

		// Go through the list of markup declarations and put the objects in
		// the correct hashes.
		for (Iterator it = markupDecls.iterator(); it.hasNext(); ) {
			XMLObject xmlObject = (XMLObject)it.next();

			boolean classified = false;
			try {
				if (xmlObject instanceof XMLAttributeList) {
					attlists.put(xmlObject.getName(), xmlObject);
					classified = true;
				} else if (xmlObject instanceof XMLElement) {
					elements.put(xmlObject.getName(), xmlObject);
					classified = true;
				} else if (xmlObject instanceof XMLEntity) {
					entities.put(xmlObject.getName(), xmlObject);
					classified = true;
				} else if (xmlObject instanceof XMLNotation) {
					notations.put(xmlObject.getName(), xmlObject);
					classified = true;
				}
			} catch (XMLObjectException e) {
				// All the above types have names so this exception will never
				// be thrown
				DBC.UNREACHABLE_CODE();
				throw new XMLDocumentTypeException(
					Message.XMLDOCTYPE_XMLOBJECT_IN_MARKUPDECLS(
						xmlObject.toString()
					),
					e
				);
			}

			// If the xmlObject was not classified then it means it wasn't
			// supposed to be there.
			if (!classified) {
				DBC.UNREACHABLE_CODE();
				throw new XMLDocumentTypeException(
					Message.XMLDOCTYPE_XMLOBJECT_IN_MARKUPDECLS(
						xmlObject.toString()
					)
				);
			}
		}
	}

	/** Associate attribute lists with their parent elements.

		@throws	XMLDocumentTypeException	if an attribute is discovered that
											there is no element to attach it
											to.
	*/
	private void associateAttlistsWithElements()
	throws XMLDocumentTypeException
	{
		DBC.REQUIRE(attlists != null);
		DBC.REQUIRE(elements != null);

		for (Iterator it = attlists.keySet().iterator(); it.hasNext(); ) {
			// The keys are Strings containing the name of the elements that
			// the attribute lists belong to.
			String name = (String)it.next();

			// Get the XMLAttributeList and corresponding XMLElement
			XMLAttributeList attlist = (XMLAttributeList)attlists.get(name);
			DBC.ASSERT(attlist != null);
			XMLElement element = (XMLElement)elements.get(name);
			DBC.ASSERT(element != null);

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

		DBC.ENSURE(attlists != null);
		DBC.ENSURE(elements != null);
	}

	/** Given a feature, determine if the current document type uses it.

		@param	featureName					The name of the feature to check.
		@return								true if <code>featureName</code> is
											used by the current document type,
											false otherwise.
		@throws	XMLDocumentTypeException	if <code>featureName</code> is not
											a valid feature name.
	*/
	private boolean hasFeature(Integer featureName)
	throws XMLDocumentTypeException
	{
		DBC.REQUIRE(featureName != null);

		// Require that features have been determined
		if (feature == null) {
			calculateFeatures();
		}
		DBC.ASSERT(feature != null);

		// Make sure that featureName is a valid feature
		if (!feature.containsKey(featureName)) {
			throw new XMLDocumentTypeException(
				Message.XMLDOCTYPE_UNSUPPORTED_FEATURE
			);
		}

		// Actually do the check
		return ((Boolean)feature.get(featureName)).booleanValue();
	}

	/** Shorthand for finding out if the current document type declares any
		attribute lists.

		@return	True if the document type declares attribute lists and false in
				every other case, including if errors occur.
	*/
	public boolean hasAttlists() {
		try {
			return hasFeature(ATTLISTS);
		} catch (XMLDocumentTypeException e) {
			DBC.IGNORED_ERROR();
			return false;
		}
	}

	/** Shorthand for finding out if the current document type declares any
		entities.

		@return	True if the document type declares entities and false in every
				other case, including if errors occur.
	*/
	public boolean hasEntities() {
		try {
			return hasFeature(ENTITIES);
		} catch (XMLDocumentTypeException e) {
			DBC.IGNORED_ERROR();
			return false;
		}
	}

	/** Accessor for elements.

		@return	A {@link Map} (keyed on the names) of the elements declared in
				this {@link XMLDocumentType}.
	*/
	public Map elements() {
		DBC.REQUIRE(elements != null);
		return new HashMap(elements);
	}

	/** Accessor for attribute lists.

		@return	A {@link Map} (keyed on the names) of the attribute lists
				declared in this {@link XMLDocumentType}
	*/
	public Map attlists() {
		DBC.REQUIRE(attlists != null);
		return new HashMap(attlists);
	}

	/** Accessor for entities.

		@return A {@link Map} (keyed on the names) of the general entities
				declared in this {@link XMLDocumentType}.
	*/
	public Map entities() {
		DBC.REQUIRE(entities != null);
		return new HashMap(entities);
	}

	/** Accessor for notations.

		@return	A {@link Map} (keyed on the names) of the notations declared in
				this {@link XMLDocumentType}.
	*/
	public Map notations() {
		DBC.REQUIRE(notations != null);
		return new HashMap(notations);
	}

	/** See {@link Object#equals(Object)}.

		@param	o	The object to compare against.
		@return		True if <code>this</code> is equal to <code>o</code>.
	*/
	public boolean equals(Object o) {
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
	public int hashCode() {
		Object[] hashCodeVars = {
			attlists,
			elements,
			entities,
			notations,
		};
		return Utils.genericHashCode(hashCodeVars);
	}

	/** See {@link Object#toString()}.

		@return	A descriptive {@link String}.
	*/
	public String toString() {
		StringBuffer desc = new StringBuffer(
			XMLObject.toStringPrefix(
				getClass().getName()
			)
		);

		Iterator it = markupDecls.iterator();
		if (it.hasNext()) {
			desc.append(it.next().toString());
			while (it.hasNext()) {
				desc.append(Constants.Character.COMMA);
				desc.append(Constants.Character.SPACE);
				desc.append(it.next().toString());
			}
		}

		return desc.toString();
	}
}