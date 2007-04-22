// vim:filetype=java:ts=4
/*
	Copyright (c) 2007
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
package com.mcdermottroe.exemplar;

/** This interface is designed to be a replacement for {@link Cloneable} and
	should rectify the problems that exist with it.

	<p>The following problems exist with {@link Cloneable}:</p>

	<ul>
 		<li>It does not enforce that a {@link Object#clone()} method is public,
 			so you can't clone instances of {@link Cloneable} without knowing
			the concrete class type.
		 </li>
 		<li>(Great-)grandchildren of {@link Object} are hard to clone properly
 			if the intervening (grand)children do not implement {@link
			Cloneable} (and actually publicise {@link Object#clone()}).
 		</li>
 		<li>{@link Object#clone()} predates generics, so it returns an {@link
			Object} rather than the correct type. Coupled with the fact that
			casting a generified collection from Object is nigh-on impossible to
 			do correctly, this makes cloning collection classes hard.
		 </li>
	</ul>

	@author		Conor McDermottroe
	@since		0.2
	@param	<T>	The type of the implementing object. For example, if {@link
				String} implemented this interface it would do so as {@link
				Copyable}&lt;{@link String}&gt;.
*/
public interface Copyable<T> {
	/** Creates a deep copy of the current object and returns it.
	
		@return					A deep copy of the current object.
		@throws	CopyException	if the copying encountered an exception.
	*/
	T getCopy() throws CopyException;
}
