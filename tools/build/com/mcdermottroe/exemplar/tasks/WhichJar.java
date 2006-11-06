// vim:filetype=java:ts=4
/*
	Copyright (c) 2006
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
package build.com.mcdermottroe.exemplar.tasks;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.WhichResource;

/**	A way of getting the name of the JAR that a resource has been loaded from
	that isn't in the bizarre format used by the {@link WhichResource} task.

	@author	Conor McDermottroe
	@since	0.1
*/
public class WhichJar extends WhichResource {
	/** Since no-one thought that someone would want to extend {@link
		WhichResource}, the property field was declared private, so it has to
		be mirrored here.
	*/
	private String resultPropertyName;

	/** Redo the constructor to make sure that {@link #resultPropertyName} is
		always initialised.
	*/
	public WhichJar() {
		super();
		resultPropertyName = "";
	}

	/** Overload the {@link WhichResource#setProperty} method to allow us to
		intercept the name of the property that will hold the result of the
		{@link WhichResource} task.

		@param property The property that will be set.
	*/
	@Override public void setProperty(String property) {
		resultPropertyName = property;
		super.setProperty(property);
	}

	/** Execute the parent task and massage the result into the form we want.
		If the result was a resource starting with "jar:file:" then the path to
		the JAR file is returned. In all other cases a BuildException is thrown
		to indicate failure.

		@throws	BuildException	if the resource was not found or if it was
								found but was not in a JAR file.
	*/
	@Override public void execute() {
		super.execute();
		String resultPropertyValue = getProject().getProperty(resultPropertyName);
		if (resultPropertyValue != null && resultPropertyValue.startsWith("jar:file:")) {
			resultPropertyValue = resultPropertyValue.replaceFirst("^jar:file:", "");
			resultPropertyValue = resultPropertyValue.replaceFirst("!.*$", "");
			getProject().setProperty(resultPropertyName, resultPropertyValue);
		} else {
			throw new BuildException("Failed to locate JAR file.");
		}
	}
}
