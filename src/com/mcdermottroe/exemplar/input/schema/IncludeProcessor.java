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
import com.mcdermottroe.exemplar.generated.schema.element.Include;
import com.mcdermottroe.exemplar.generated.schema.support.W3CSchemaException;
import com.mcdermottroe.exemplar.generated.schema.support.W3CSchemaTreeOp;
import com.mcdermottroe.exemplar.generated.schema.support.XMLComponent;
import com.mcdermottroe.exemplar.utils.Files;

/**	A {@link W3CSchemaTreeOp} to process include directives in a schema.

	@author Conor McDermottroe
	@since	0.2
*/
public class IncludeProcessor
implements Copyable<IncludeProcessor>, W3CSchemaTreeOp
{
	/** The directory relative to which includes are processed. */
	protected final File baseDirectory;

	/** Create a new {@link IncludeProcessor} and specify the directory which
		should be considered the "current directory" for the purposes of
		resolving relative references.

		@param	baseDir	The "current directory".
	*/
	public IncludeProcessor(File baseDir) {
		baseDirectory = baseDir;
	}

	/** {@inheritDoc} */
	public boolean shouldApply(XMLComponent<?> treeComponent) {
		return Include.class.isAssignableFrom(treeComponent.getClass());
	}

	/** {@inheritDoc} */
	public void execute(XMLComponent<?> treeComponent)
	throws W3CSchemaException
	{
		Include inc = Include.class.cast(treeComponent);

		// If we've already imported this, we should remove it from the tree
		if (IncludeManager.alreadyIncluded(inc)) {
			XMLComponent<?> incParent = inc.getParent();
			List<XMLComponent<?>> incParentChildren = incParent.getChildren();
			for (int i = 0; i < incParentChildren.size(); i++) {
				if (inc.equals(incParentChildren.get(i))) {
					incParentChildren.remove(i);
				}
			}
			incParent.setChildren(incParentChildren);
			return;
		}

		// Get an InputStream for the schema location
		XMLComponent<?> incRoot;
		try {
			InputStream incInput = Files.readFile(
				inc.getSchemaLocation(),
				baseDirectory
			);
			incRoot = W3CSchema.read(incInput);
		} catch (IOException e) {
			throw new W3CSchemaException(e);
		} catch (SAXException e) {
			throw new W3CSchemaException(e);
		}

		// Replace the instance of this import in the parent
		XMLComponent<?> incParent = treeComponent.getParent();
		incRoot.setParent(incParent);
		List<XMLComponent<?>> incParentChildren = incParent.getChildren();
		for (int i = 0; i < incParentChildren.size(); i++) {
			if (incParentChildren.get(i).equals(treeComponent)) {
				incParentChildren.set(i, incRoot);
			}
		}
		incParent.setChildren(incParentChildren);

		IncludeManager.markAsIncluded(inc);
	}

	/** Get the base directory of this {@link IncludeProcessor}.

		@return	The base directory for this {@link IncludeProcessor}.
	*/
	protected File getBaseDirectory() {
		return baseDirectory;
	}

	/** {@inheritDoc} */
	public IncludeProcessor getCopy()
	throws CopyException
	{
		return new IncludeProcessor(baseDirectory);
	}

	/** {@inheritDoc} */
	public int compareTo(IncludeProcessor other) {
		return Utils.compare(baseDirectory, other.getBaseDirectory());
	}

	/** Generage a hash code for this {@link IncludeProcessor}.

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
		if (!(o instanceof IncludeProcessor)) {
			return false;
		}

		return compareTo(IncludeProcessor.class.cast(o)) == 0;
	}
}
