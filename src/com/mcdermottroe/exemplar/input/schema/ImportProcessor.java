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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.xml.sax.SAXException;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.generated.schema.W3CSchema;
import com.mcdermottroe.exemplar.generated.schema.element.Import;
import com.mcdermottroe.exemplar.generated.schema.support.W3CSchemaException;
import com.mcdermottroe.exemplar.generated.schema.support.W3CSchemaTreeOp;
import com.mcdermottroe.exemplar.generated.schema.support.XMLComponent;
import com.mcdermottroe.exemplar.utils.Files;

/** A {@link W3CSchemaTreeOp} to process all import declarations.

	@author Conor McDermottroe
	@since	0.2
*/
public class ImportProcessor
implements Copyable<ImportProcessor>, W3CSchemaTreeOp
{
	/** The directory from which relative paths should be resolved. */
	protected File baseDirectory;

	/** Create a new {@link ImportProcessor}.

		@param	baseDir	The directory from which relative paths should be
						resolved.
	*/
	public ImportProcessor(File baseDir) {
		baseDirectory = baseDir;
	}

	/** {@inheritDoc} */
	public boolean shouldApply(XMLComponent<?> treeComponent) {
		return Import.class.isAssignableFrom(treeComponent.getClass());
	}

	/** {@inheritDoc} */
	public void execute(XMLComponent<?> treeComponent)
	throws W3CSchemaException
	{
		Import imp = Import.class.cast(treeComponent);

		// If we've already imported this, we should remove it from the tree
		if (IncludeManager.alreadyImported(imp)) {
			XMLComponent<?> impParent = imp.getParent();
			List<XMLComponent<?>> impParentChildren = impParent.getChildren();
			for (int i = 0; i < impParentChildren.size(); i++) {
				if (imp.equals(impParentChildren.get(i))) {
					impParentChildren.remove(i);
				}
			}
			impParent.setChildren(impParentChildren);
			return;
		}

		XMLComponent<?> impRoot;
		try {
			InputStream impInput = Files.readFile(
				imp.getSchemaLocation(),
				baseDirectory
			);
			impRoot = W3CSchema.read(impInput);
		} catch (IOException e) {
			throw new W3CSchemaException(e);
		} catch (SAXException e) {
			throw new W3CSchemaException(e);
		}

		// Walk this new tree and add the namespace
		applyNamespace(impRoot, imp.getNamespace());

		// Attach the new tree to the parent
		XMLComponent<?> impParent = treeComponent.getParent();
		impRoot.setParent(impParent);
		List<XMLComponent<?>> impParentChildren = impParent.getChildren();
		for (int i = 0; i < impParentChildren.size(); i++) {
			if (impParentChildren.get(i).equals(treeComponent)) {
				impParentChildren.set(i, impRoot);
			}
		}
		impParent.setChildren(impParentChildren);

		IncludeManager.markAsImported(imp);
	}

	/** Apply a namespace to a subtree of the parse tree.

		@param	treeElement	The root of the subtree.
		@param	namespace	The namespace to apply.
	*/
	private void applyNamespace(XMLComponent<?> treeElement, String namespace) {
		// SCHEMATASK
	}

	/** Get the base directory for this processor.

		@return	The base directory for this processor
	*/
	protected File getBaseDirectory() {
		return baseDirectory;
	}

	/** {@inheritDoc} */
	public ImportProcessor getCopy()
	throws CopyException
	{
		return new ImportProcessor(baseDirectory);
	}

	/** {@inheritDoc} */
	public int compareTo(ImportProcessor other) {
		return Utils.compare(baseDirectory, other.getBaseDirectory());
	}

	/** Generage a hash code for this {@link ImportProcessor}.

		@return	A hashcode for this object.
	*/
	@Override public int hashCode() {
		return baseDirectory.hashCode();
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
		if (!(o instanceof ImportProcessor)) {
			return false;
		}

		return compareTo(ImportProcessor.class.cast(o)) == 0;
	}
}
