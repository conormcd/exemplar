// vim:filetype=jflex:ts=4
/*
	Copyright (c) 2003-2007
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
package com.mcdermottroe.exemplar.input.dtd;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import java_cup.runtime.Symbol;

import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.input.LexerException;
import com.mcdermottroe.exemplar.ui.Log;
import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.utils.XML;

import static com.mcdermottroe.exemplar.Constants.BASE_DECIMAL;
import static com.mcdermottroe.exemplar.Constants.BASE_HEXADECIMAL;
import static com.mcdermottroe.exemplar.Constants.XMLExternalIdentifier.PUBLIC;
import static com.mcdermottroe.exemplar.Constants.XMLExternalIdentifier.SYSTEM;

%%
%class Lexer
%{
	/** The path to where the DTD is stored. This is used for resolving 
		relative URIs within parameter entity declarations.
	*/
	private File dtdPath;

	/** A table for tracking parameter entity declarations. */
	private PEDeclTable pedeclTable;

	/** A stack to keep track of nested conditional statements. */
	private Stack<String> conditionalSectionState;

	/** Allow other classes to inform us of what the path to the DTD is.

		@param	value	The path to the DTD.
	*/
	public void setDtdPath(File value) {
		DBC.REQUIRE(value.isDirectory());
		dtdPath = value;
	}

	/** Given a string containing a parameter entity declaration, parse it and
		add the parameter entity to the symbol table.

		@param	peDeclString				A String containing a parameter 
											entity declaration
		@throws ParameterEntityException	if the input is not a valid
											parameter entity declaration.
	*/
	private void processPEDecl(String peDeclString)
	throws ParameterEntityException
	{
		DBC.REQUIRE(peDeclString != null);

		String name;		// The name of the parameter entity
		String contents;	// The replacement text for the entity

		// Get the text that makes up this token
		String text = peDeclString;

		// Get the name of the parameter entity
		name = text.substring(text.indexOf("%") + 1).trim();
		// Find the first whitespace character after the name
		int fWI = Integer.MAX_VALUE;
		if (name.contains(" ") && name.indexOf(" ") < fWI) {
			fWI = name.indexOf(" ");
		}
		if (name.contains("\t") && name.indexOf("\t") < fWI) {
			fWI = name.indexOf("\t");
		}
		if (name.contains("\n") && name.indexOf("\n") < fWI) {
			fWI = name.indexOf("\n");
		}
		if (name.contains("\r") && name.indexOf("\r") < fWI) {
			fWI = name.indexOf("\r");
		}
		// Extract the name.
		name = name.substring(0, fWI + 1).trim();

		// Process the rest of the parameter entity
		String rest = text.substring(	text.indexOf("%") + fWI + 2,
										text.length() - 1
									).trim();

		if	(
				(rest.startsWith("\"") && rest.endsWith("\"")) ||
				(rest.startsWith("'") && rest.endsWith("'"))
			)
		{
			// This is straightforward PEDecl with no URI reference
			String value;
			try {
				value = XML.resolveCharacterReferences(rest.substring(1,rest.length() - 1));
			} catch (ParseException e) {
				throw new ParameterEntityException(e);
			}
			pedeclTable.addNewPE(name, value, ParameterEntityType.VALUE);
		} else if (rest.startsWith(PUBLIC) || rest.startsWith(SYSTEM)) {
			// PUBLIC PEs have a PubidLiteral
			boolean hasPubidLiteral = false;
			if (rest.startsWith(PUBLIC)) {
				hasPubidLiteral = true;
			}

			// Strip off PUBLIC/SYSTEM
			rest = rest.substring(6, rest.length()).trim();

			if (hasPubidLiteral) {
				// Now remove the PubidLiteral, I'm not going to use it
				if (rest.startsWith("\"")) {
					rest =	rest.substring	(		
												rest.indexOf("\"", 1) + 1,
												rest.length()
											).trim();
				} else if (rest.startsWith("'")) {
					rest =	rest.substring	(
												rest.indexOf("'", 1) + 1,
												rest.length()
											).trim();
				} else {
					throw new ParameterEntityException(Message.DTDPEEXCEPTION(Message.DTDPE_INVALID_PUBIDLITERAL(), rest));
				}
			}

			// The next portion is the SystemLiteral giving the location of the
			// content for the entity.
			String systemLiteral = "";
			if (rest.startsWith("\"")) {
				systemLiteral = rest.substring(1, rest.indexOf("\"", 1));
			} else if (rest.startsWith("'")) {
				systemLiteral = rest.substring(1, rest.indexOf("'", 1));
			} else {
				throw new ParameterEntityException(Message.DTDPEEXCEPTION(Message.DTDPE_INVALID_SYSTEMLITERAL(), rest));
			}

			// Check for trailing garbage after the SystemLiteral
			if (systemLiteral.length() + 2 != rest.length()) {
				throw new ParameterEntityException(Message.DTDPEEXCEPTION(Message.DTDPE_GARBAGE_AFTER_SYSTEMLITERAL(), rest));
			}

			// The content can be gotten from the URI
			pedeclTable.addNewPE(name, systemLiteral, ParameterEntityType.URI);
		} else {
			throw new ParameterEntityException(Message.DTDPE_INVALID_PEDECL());
		}
	}

	/** Implement {@link Comparable#compareTo(Object)}. */
	public int compareTo(Lexer other) {
		int dtdPathCmp = Utils.compare(dtdPath, other.dtdPath);
		if (dtdPathCmp != 0) {
			return dtdPathCmp;
		}
		int pedeclTableCmp = Utils.compare(pedeclTable, other.pedeclTable);
		if (pedeclTableCmp != 0) {
			return pedeclTableCmp;
		}
		return Utils.compare(
			conditionalSectionState,
			other.conditionalSectionState
		);
	}

	/** {@inheritDoc} */
	public Lexer getCopy() {
		Lexer copy = new Lexer(zzReader);
		copy.conditionalSectionState = new Stack<String>();
		for (String element : conditionalSectionState) {
			copy.conditionalSectionState.push(element);
		}
		copy.dtdPath = dtdPath;
		copy.pedeclTable = pedeclTable.getCopy();
		copy.yychar = yychar;
		copy.yycolumn = yycolumn;
		copy.yyline = yyline;
		copy.zzAtBOL = zzAtBOL;
		copy.zzAtEOF = zzAtEOF;
		copy.zzBuffer = new char[zzBuffer.length];
		System.arraycopy(zzBuffer, 0, copy.zzBuffer, 0, zzBuffer.length);
		copy.zzCurrentPos = zzCurrentPos;
		copy.zzEndRead = zzEndRead;
		copy.zzEOFDone = zzEOFDone;
		copy.zzLexicalState = zzLexicalState;
		copy.zzMarkedPos = zzMarkedPos;
		copy.zzPushbackPos = zzPushbackPos;
		copy.zzStartRead = zzStartRead;
		copy.zzState = zzState;
		copy.zzStreams = new Stack<ZzFlexStreamInfo>();
		for (ZzFlexStreamInfo stream : zzStreams) {
			zzStreams.push(stream);
		}
		return copy;
	}

	/** Implement {@link Object#equals(Object)}.

		@param	o	The other {@link Object} to test against.
		@return		True if <code>this</code> is equal to <code>o</code>.
	*/
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof Lexer)) {
			return false;
		}

		Lexer other = (Lexer)o;
		return	Utils.areDeeplyEqual(dtdPath, other.dtdPath) &&
				Utils.areDeeplyEqual(pedeclTable, other.pedeclTable) &&
				Utils.areDeeplyEqual(
					conditionalSectionState,
					other.conditionalSectionState
				);
	}

	/** Implement {@link Object#hashCode()}.

		@return	A hash code for this object.
	*/
	public int hashCode() {
		return Utils.genericHashCode(
			dtdPath,
			pedeclTable,
			conditionalSectionState
		);
	}
%}
%public
%init{
pedeclTable = new PEDeclTable();
conditionalSectionState = new Stack<String>();
conditionalSectionState.push("INCLUDE");
%init}
%yylexthrow{
LexerException, ParameterEntityException
%yylexthrow}
%implements Comparable<Lexer>, Copyable<Lexer>
%unicode
%cup
%state YYIGNORE
%%

<YYINITIAL>"<!ENTITY"[\u0009\u000A\u000D\u0020]+%[\u0009\u000A\u000D\u0020]+[^\u0009\u000A\u000D\u0020]+[\u0009\u000A\u000D\u0020]+\u0022[^\u0022]+\u0022">"
			{
				processPEDecl(yytext());
			}
<YYINITIAL>"<!ENTITY"[\u0009\u000A\u000D\u0020]+%[\u0009\u000A\u000D\u0020]+[^\u0009\u000A\u000D\u0020]+[\u0009\u000A\u000D\u0020]+'[^']+'">"
			{
				processPEDecl(yytext());
			}
<YYINITIAL>"<!ENTITY"[\u0009\u000A\u000D\u0020]+%[\u0009\u000A\u000D\u0020]+[^\u0009\u000A\u000D\u0020]+[\u0009\u000A\u000D\u0020]+\u0022[^\u0022]+\u0022[\u0009\u000A\u000D\u0020]+">"
			{
				processPEDecl(yytext());
			}
<YYINITIAL>"<!ENTITY"[\u0009\u000A\u000D\u0020]+%[\u0009\u000A\u000D\u0020]+[^\u0009\u000A\u000D\u0020]+[\u0009\u000A\u000D\u0020]+'[^']+'[\u0009\u000A\u000D\u0020]+">"
			{
				processPEDecl(yytext());
			}
<YYINITIAL>"<!ENTITY"[\u0009\u000A\u000D\u0020]+%[\u0009\u000A\u000D\u0020]+[^\u0009\u000A\u000D\u0020]+[\u0009\u000A\u000D\u0020]+[^>]+">"
			{
				processPEDecl(yytext());
			}
<YYINITIAL>%[^;]+;
			{
				// Ensure that calling methods on the result of yytext() will
				// not cause an NPE
				DBC.ASSERT(yytext() != null);

				// Strip off the leading % and the trailing ;
				String peRefKey = yytext().substring(1, yytext().length() - 1);

				// Create a new reader object and push it as a new input
				yypushStream(pedeclTable.peRefReader(peRefKey, dtdPath));
			}
<YYINITIAL>"<!["[^\[]+"["
			{
				// Replace any parameter entity references
				String text = pedeclTable.replacePERefs(yytext());
				DBC.ASSERT(text != null);

				// Strip off the '<![' and '[' and remove leading and trailing
				// space.
				text = text.substring(3, text.length() - 1).trim();

				if (text.equals("IGNORE") || text.equals("INCLUDE")) {
					DBC.ASSERT(conditionalSectionState != null);
					conditionalSectionState.push(text);
					if (conditionalSectionState.search("IGNORE") == -1) {
						yybegin(YYINITIAL);
					} else {
						yybegin(YYIGNORE);
					}
				} else {
					throw new LexerException(Message.LEXEREXCEPTION(Message.DTDLEXER_INVALID_CONDITIONAL_SECTION(), yytext()));
				}
			}
<YYIGNORE>"<!["[^\[]+"["
			{
				// This conditional section could be either INCLUDE or IGNORE
				// but it doesn't matter because an outer one is IGNORE.
				DBC.ASSERT(conditionalSectionState != null);
				conditionalSectionState.push("IGNORE");
			}
<YYINITIAL,YYIGNORE>"]]>"
			{
				String text = conditionalSectionState.pop();
				DBC.ASSERT(text != null);
				DBC.ASSERT(conditionalSectionState != null);
				if (conditionalSectionState.search("IGNORE") == -1) {
					yybegin(YYINITIAL);
				} else {
					yybegin(YYIGNORE);
				}
			}
<YYINITIAL>"<?"~"?>"
			{
				// Ensure that calling methods on the result of yytext() will
				// not cause an NPE
				DBC.ASSERT(yytext() != null);

				// Strip the leading and trailing <? ?> from the PI
				String text = yytext().substring(2, yytext().length() - 2);

				// Find the index of the first whitespace character so that the
				// name of the processing instruction can be extracted.
				int fWI = Integer.MAX_VALUE;
				if (text.contains(" ") && text.indexOf(" ") < fWI) {
					fWI = text.indexOf(" ");
				}
				if (text.contains("\t") && text.indexOf("\t") < fWI) {
					fWI = text.indexOf("\t");
				}
				if (text.contains("\n") && text.indexOf("\n") < fWI) {
					fWI = text.indexOf("\n");
				}
				if (text.contains("\r") && text.indexOf("\r") < fWI) {
					fWI = text.indexOf("\r");
				}

				// Now extract the name
				String name = text.substring(0, fWI);

				// If the name of the PI is [Xx][Mm][Ll] then this is a text
				// declaration. All other PIs are ignored.
				if (	(name.charAt(0) == 'x' || name.charAt(0) == 'X') &&
						(name.charAt(1) == 'm' || name.charAt(1) == 'M') &&
						(name.charAt(2) == 'l' || name.charAt(2) == 'L'))
				{
					// This is a text declaration
					String textDeclText = text.substring(fWI, text.length()).trim();

					// Process the version
					if (textDeclText.startsWith("version")) {
						// Strip off version and whitespace immediately after
						// it
						textDeclText = textDeclText.substring(7, textDeclText.length()).trim();

						// Now textDeclText should start with an '='
						if (textDeclText.charAt(0) == '=') {
							// Strip off the '=' and whitespace immediately
							// after it.
							textDeclText = textDeclText.substring(1, textDeclText.length()).trim();

							// Get the version number
							String versionNum;
							char quotChar = textDeclText.charAt(0);
							if (quotChar == '\'' || quotChar == '\"') {
								versionNum = textDeclText.substring(1, textDeclText.indexOf(quotChar, 1));

								// Now remove the version number
								textDeclText = textDeclText.substring(versionNum.length() + 2, textDeclText.length()).trim();
							} else {
								throw new LexerException(Message.DTDLEXER_INVALID_TEXTDECL());
							}
						} else {
							throw new LexerException(Message.DTDLEXER_INVALID_TEXTDECL());
						}
					}

					// Process the encoding
					if (textDeclText.startsWith("encoding")) {
						// Strip off encoding and whitespace immediately after
						// it
						textDeclText = textDeclText.substring(8, textDeclText.length()).trim();

						// Now textDeclText should start with an '='
						if (textDeclText.charAt(0) == '=') {
							// Strip off the '=' and whitespace immediately
							// after it.
							textDeclText = textDeclText.substring(1, textDeclText.length()).trim();

							// Get the encoding name
							char quotChar = textDeclText.charAt(0);
							if (quotChar == '\'' || quotChar == '\"') {
								String encName = textDeclText.substring(1, textDeclText.indexOf(quotChar, 1));

								// Now remove the version number
								textDeclText = textDeclText.substring(encName.length() + 2, textDeclText.length()).trim();
							} else {
								throw new LexerException(Message.DTDLEXER_INVALID_TEXTDECL());
							}
						} else {
							throw new LexerException(Message.DTDLEXER_INVALID_TEXTDECL());
						}
					}

					// If there's anything left, it's an error
					if (! textDeclText.trim().equals("")) {
						throw new LexerException(Message.DTDLEXER_INVALID_TEXTDECL());
					}
				} else {
					// Ignore
					Log.debug("Ignoring processing instruction: " + name);
				}
			}
<YYINITIAL>"<!--"~"-->"
			{
				return new Symbol(ParserSymbols.COMMENT);
			}
<YYINITIAL>"<!ATTLIST"
			{
				return new Symbol(ParserSymbols.ATTLIST_HEAD, yytext());
			}
<YYINITIAL>"<!ELEMENT"
			{
				return new Symbol(ParserSymbols.ELEMENT_HEAD, yytext());
			}
<YYINITIAL>"<!ENTITY"
			{
				return new Symbol(ParserSymbols.ENTITY_HEAD, yytext());
			}
<YYINITIAL>"<!NOTATION"
			{
				return new Symbol(ParserSymbols.NOTATION_HEAD, yytext());
			}
<YYINITIAL>"#PCDATA"
			{
				return new Symbol(ParserSymbols.HASH_PCDATA, yytext());
			}
<YYINITIAL>"#FIXED"
			{
				return new Symbol(ParserSymbols.HASH_FIXED, yytext());
			}
<YYINITIAL>"#IMPLIED"
			{
				return new Symbol(ParserSymbols.HASH_IMPLIED, yytext());
			}
<YYINITIAL>"#REQUIRED"
			{
				return new Symbol(ParserSymbols.HASH_REQUIRED, yytext());
			}
<YYINITIAL>"CDATA"
			{
				return new Symbol(ParserSymbols.CDATA, yytext());
			}
<YYINITIAL>"SYSTEM"
			{
				return new Symbol(ParserSymbols.SYSTEM, yytext());
			}
<YYINITIAL>"PUBLIC"
			{
				return new Symbol(ParserSymbols.PUBLIC, yytext());
			}
<YYINITIAL>[\u0009\u000A\u000D\u0020]+"NDATA"[\u0009\u000A\u000D\u0020]+
			{
				return new Symbol(ParserSymbols.NDATA_SURROUNDED_BY_S, yytext());
			}
<YYINITIAL>"NOTATION"
			{
				return new Symbol(ParserSymbols.NOTATION, yytext());
			}
<YYINITIAL>"EMPTY"
			{
				return new Symbol(ParserSymbols.EMPTY, yytext());
			}
<YYINITIAL>"ANY"
			{
				return new Symbol(ParserSymbols.ANY, yytext());
			}
<YYINITIAL>"ID"
			{
				return new Symbol(ParserSymbols.ID, yytext());
			}
<YYINITIAL>"IDREF"
			{
				return new Symbol(ParserSymbols.IDREF, yytext());
			}
<YYINITIAL>"IDREFS"
			{
				return new Symbol(ParserSymbols.IDREFS, yytext());
			}
<YYINITIAL>"ENTITY"
			{
				return new Symbol(ParserSymbols.ENTITY, yytext());
			}
<YYINITIAL>"ENTITIES"
			{
				return new Symbol(ParserSymbols.ENTITIES, yytext());
			}
<YYINITIAL>"NMTOKEN"
			{
				return new Symbol(ParserSymbols.NMTOKEN, yytext());
			}
<YYINITIAL>"NMTOKENS"
			{
				return new Symbol(ParserSymbols.NMTOKENS, yytext());
			}
<YYINITIAL>"&#"[0-9]+";"
			{
				try {
					return new Symbol(ParserSymbols.CHARREF, XML.resolveCharacterReferences(yytext()));
				} catch (ParseException e) {
					throw new LexerException(e);
				}
			}
<YYINITIAL>"&#x"[0-9A-Fa-f]+";"
			{
				try {
					return new Symbol(ParserSymbols.CHARREF, XML.resolveCharacterReferences(yytext()));
				} catch (ParseException e) {
					throw new LexerException(e);
				}
			}
<YYINITIAL>[\u0009\u000D\u000A\u0020]+,[\u0009\u000D\u000A\u0020]+
			{
				return new Symbol(ParserSymbols.COMMA_SURROUNDED_BY_S, yytext());
			}
<YYINITIAL>[\u0009\u000D\u000A\u0020]+,
			{
				return new Symbol(ParserSymbols.COMMA_PRECEDED_BY_S, yytext());
			}
<YYINITIAL>,[\u0009\u000D\u000A\u0020]+
			{
				return new Symbol(ParserSymbols.COMMA_FOLLOWED_BY_S, yytext());
			}
<YYINITIAL>[\u0009\u000D\u000A\u0020]+"|"[\u0009\u000D\u000A\u0020]+
			{
				return new Symbol(ParserSymbols.PIPE_SURROUNDED_BY_S, yytext());
			}
<YYINITIAL>[\u0009\u000D\u000A\u0020]+"|"
			{
				return new Symbol(ParserSymbols.PIPE_PRECEDED_BY_S, yytext());
			}
<YYINITIAL>"|"[\u0009\u000D\u000A\u0020]+
			{
				return new Symbol(ParserSymbols.PIPE_FOLLOWED_BY_S, yytext());
			}
<YYINITIAL>[\u0009\u000D\u000A\u0020]+
			{
				return new Symbol(ParserSymbols.S, yytext());
			}
<YYINITIAL>"&"
			{
				return new Symbol(ParserSymbols.AMP, yytext());
			}
<YYINITIAL>"'"
			{
				return new Symbol(ParserSymbols.APOS, yytext());
			}
<YYINITIAL>"@"
			{
				return new Symbol(ParserSymbols.AT, yytext());
			}
<YYINITIAL>"`"
			{
				return new Symbol(ParserSymbols.BACKTICK, yytext());
			}
<YYINITIAL>[\u0041-\u005A\u0061-\u007A\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u00FF\u0100-\u0131\u0134-\u013E\u0141-\u0148\u014A-\u017E\u0180-\u01C3\u01CD-\u01F0\u01F4-\u01F5\u01FA-\u0217\u0250-\u02A8\u02BB-\u02C1\u0386\u0388-\u038A\u03AC\u038E-\u03A1\u03A3-\u03CE\u03D0-\u03D6\u03DA\u03DC\u03DE\u03E0\u03E2-\u03F3\u0401-\u040C\u040E-\u044F\u0451-\u045C\u045E-\u0481\u0490-\u04C4\u04C7\u04C8\u04CB\u04CC\u04D0-\u04EB\u04EE-\u04F5\u04F8\u04F9\u0531-\u0556\u0559\u0561-\u0586\u05D0-\u05EA\u05F0-\u05F2\u0621-\u063A\u0641-\u064A\u0671-\u06B7\u06BA-\u06BE\u06C0-\u06CE\u06D0-\u06D3\u06D5\u06E5\u06E6\u0905-\u0939\u093D\u0958-\u0961\u0985-\u098C\u098F-\u0990\u0993-\u09A8\u09AA-\u09B0\u09B2\u09B6-\u09B9\u09DC\u09DD\u09DF-\u09E1\u09F0\u09F1\u0A05-\u0A0A\u0A0F-\u0A10\u0A13-\u0A28\u0A2A-\u0A30\u0A32\u0A33\u0A35\u0A36\u0A38\u0A39\u0A59-\u0A5C\u0A5E\u0A72-\u0A74\u0A85-\u0A8B\u0A8D\u0A8F-\u0A91\u0A93-\u0AA8\u0AAA-\u0AB0\u0AB2\u0AB3\u0AB5-\u0AB9\u0ABD\u0AE0\u0B05-\u0B0C\u0B0F-\u0B10\u0B13-\u0B28\u0B2A-\u0B30\u0B32\u0B33\u0B36-\u0B39\u0B3D\u0B5C\u0B5D\u0B5F-\u0B61\u0B85-\u0B8A\u0B8E-\u0BB5\u0BB7-\u0BB9\u0C05-\u0C0C\u0C0E-\u0C10\u0C12-\u0C28\u0C2A-\u0C33\u0C35-\u0C39\u0C60\u0C61\u0C85-\u0C8C\u0C8E-\u0C90\u0C92-\u0CA8\u0CAA-\u0CB3\u0CB5-\u0CB9\u0CDE\u0CE0\u0CE1\u0D05-\u0D0C\u0D0E-\u0D10\u0D12-\u0D28\u0D2A-\u0D39\u0D60\u0D61\u0E01-\u0E2E\u0E30\u0E32\u0E33\u0E40-\u0E45\u0E81\u0E82\u0E84\u0E87\u0E88\u0E8A\u0E8D\u0E94-\u0E97\u0E99-\u0E9F\u0EA1-\u0EA3\u0EA5\u0EA7\u0EAA\u0EAB\u0EAD\u0EAE\u0EB0\u0EB2\u0EB3\u0EBD\u0EC0-\u0EC4\u0F40-\u0F47\u0F49-\u0F69\u10A9-\u10C5\u10D0-\u10F6\u1100\u1102\u1103\u1105-\u1107\u1109\u110B\u110C\u110E-\u1112\u113C\u113E\u1140\u114C\u114E\u1150\u1154\u1155\u1159\u115F-\u1161\u1163\u1165\u1167\u1169\u116D\u116E\u1172\u1173\u1175\u119E\u11A8\u11AB\u11AE\u11AF\u11B7\u11B8\u11BA\u11BC-\u11C2\u11EB\u11F0\u11F9\u1E00-\u1E9B\u1EA0-\u1EF9\u1F00-\u1F15\u1F18-\u1F1D\u1F20-\u1F45\u1F48-\u1F4D\u1F50-\u1F57\u1F59\u1F5B\u1F5D\u1F5F-\u1F7D\u1F80-\u1FB4\u1FB6-\u1FBC\u1FBE\u1FC2-\u1FC4\u1FC6-\u1FCC\u1FD0-\u1FD3\u1FD6-\u1FDB\u1FE0-\u1FEC\u1FF2-\u1FF4\u1FF6-\u1FFC\u2126\u212A\u212B\u212E\u2180-\u2182\u3041-\u3094\u30A1-\u30FA\u3105-\u312C\uAC00-\uD7A3]
			{
				return new Symbol(ParserSymbols.BASECHAR, yytext());
			}
<YYINITIAL>"\\"
			{
				return new Symbol(ParserSymbols.BSLASH, yytext());
			}
<YYINITIAL>"^"
			{
				return new Symbol(ParserSymbols.CARAT, yytext());
			}
<YYINITIAL>[\u007F-\u00B6\u00B8-\u00BF\u00D7\u00F7\u0132\u0133\u013F\u0140\u0149\u017F\u01C4-\u01CC\u01F1-\u01F3\u01F6-\u01F9\u0218-\u024F\u02A9-\u02BA\u02C2-\u02CF\u02D2-\u02FF\u0346-\u035F\u0362-\u0385\u038B-\u038D\u03A2\u03CF\u03D7-\u03D9\u03DB\u03DD\u03DF\u03E1\u03F4-\u0400\u040D\u0450\u045D\u0482\u0487-\u048F\u04C5\u04C6\u04C9\u04CA\u04CD-\u04CF\u04EC\u04ED\u04F6\u04F7\u04FA-\u0530\u0557\u0558\u055A-\u0560\u0587-\u0590\u05A2\u05BA\u05BC\u05BE\u05C0\u05C3\u05C5-\u05CF\u05EB-\u05EF\u05F3-\u0620\u063B-\u063F\u0653-\u0659\u065A-\u065F\u066A-\u066F\u06B8\u06B9\u06BF\u06CF\u06D4\u06E9\u06EE\u06EF\u06FA-\u0900\u0904\u093A\u093B\u094E-\u0950\u0955-\u0957\u0964\u0965\u0970-\u0980\u0984\u098D\u098E\u0991\u0992\u09A9\u09B1\u09B3-\u09B5\u09BA\u09BB\u09BD\u09C5\u09C6\u09C9\u09CA\u09CC\u09CE-\u09D6\u09D8-\u09DB\u09DE\u09E4\u09E5\u09F2-\u0A01\u0A03\u0A04\u0A0B-\u0A0E\u0A11\u0A12\u0A29\u0A31\u0A34\u0A37\u0A3A\u0A3B\u0A3D\u0A43-\u0A46\u0A49-\u0A4A\u0A4E-\u0A58\u0A5D\u0A5F-\u0A65\u0A75-\u0A80\u0A84\u0A8C\u0A8E\u0A92\u0AA9\u0AB1\u0AB4\u0ABA\u0ABB\u0AC6\u0ACA\u0ACE-\u0ADF\u0AE1-\u0AE5\u0AF0-\u0B00\u0B04\u0B0D\u0B0E\u0B11\u0B12\u0B29\u0B31\u0B34\u0B35\u0B3A\u0B3B\u0B44-\u0B46\u0B49\u0B4A\u0B4E-\u0B55\u0B58-\u0B5B\u0B5E\u0B62-\u0B65\u0B70-\u0B81\u0B84\u0B8B-\u0B8D\u0BB6\u0BBA-\u0BBD\u0BC3-\u0BC5\u0BC9\u0BCE-\u0BD6\u0BD8-\u0BE6\u0BF0-\u0C00\u0C04\u0C0D\u0C11\u0C29\u0C34\u0C3A-\u0C3D\u0C45\u0C49\u0C4E-\u0C54\u0C57-\u0C5F\u0C62-\u0C65\u0C70-\u0C81\u0C84\u0C8D\u0C91\u0CA9\u0CB4\u0CBA-\u0CBD\u0CC5\u0CC9\u0CCE-\u0CD4\u0CD7-\u0CDD\u0CDF\u0CE2-\u0CE5\u0CF0-\u0D01\u0D04\u0D0D\u0D11\u0D29\u0D3A-\u0D3D\u0D44\u0D45\u0D49\u0D4E-\u0D56\u0D58-\u0D5F\u0D62-\u0D65\u0D70-\u0E00\u0E2F\u0E3B-\u0E3F\u0E4F\u0E5A-\u0E5F\u0E60-\u0E80\u0E83\u0E85\u0E86\u0E89\u0E8B\u0E8C\u0E8E-\u0E93\u0E98\u0EA0\u0EA4\u0EA6\u0EA8\u0EA9\u0EAC\u0EAF\u0EBA\u0EBE\u0EBF\u0EC5\u0EC7\u0ECE\u0ECF\u0EDA-\u0F17\u0F1A-\u0F1F\u0F2A-\u0F2F\u0F30-\u0F34\u0F36\u0F38\u0F3A-\u0F3D\u0F48\u0F6A-\u0F70\u0F85\u0F8C-\u0F8F\u0F96\u0F98\u0FAE-\u0FB0\u0FB8\u0FBA-\u10A8\u10C6-\u10CF\u10F7-\u10FF\u1101\u1104\u1108\u110A\u110D\u1113-\u113B\u113D\u113F\u1141-\u114B\u114D\u114F\u1151-\u1153\u1156-\u1158\u115A-\u115E\u1162\u1164\u1166\u1168\u116A-\u116C\u116F-\u1171\u1174\u1176-\u119D\u119F-\u11A7\u11A9\u11AA\u11AC\u11AD\u11B0-\u11B6\u11B9\u11BB\u11C3-\u11EA\u11EC-\u11EF\u11F1-\u11F8\u11FA-\u1DFF\u1E9C-\u1E9F\u1EFA-\u1EFF\u1F16\u1F17\u1F1E\u1F1F\u1F46\u1F47\u1F4E\u1F4F\u1F58\u1F5A\u1F5C\u1F5E\u1F7E\u1F7F\u1FB5\u1FBD\u1FBF-\u1FC1\u1FC5\u1FCD-\u1FCF\u1FD4\u1FD5\u1FDC-\u1FDF\u1FED-\u1FF1\u1FF5\u1FFD-\u20CF\u20DD-\u20E0\u20E2-\u2125\u2127-\u2129\u212C\u212D\u212F-\u217F\u2183-\u3004\u3006\u3008-\u3020\u3030\u3036-\u3040\u3095-\u3098\u309B\u309C\u309F\u30A0\u30FB\u30FF-\u3104\u312D-\u4DFF\u9FA6-\uABFF\uD7A4-\uD7FF\uE000-\uFFFD]
			{
				return new Symbol(ParserSymbols.CHAR, yytext());
			}
<YYINITIAL>":"
			{
				return new Symbol(ParserSymbols.COLON, yytext());
			}
<YYINITIAL>[\u0300-\u0345\u0360\u0361\u0483-\u0486\u0591-\u05A1\u05A3-\u05B9\u05BB\u05BD\u05BF\u05C1\u05C2\u05C4\u064B-\u0652\u0670\u06D6-\u06DC\u06DD-\u06DF\u06E0-\u06E4\u06E7\u06E8\u06EA-\u06ED\u0901-\u0903\u093C\u093E-\u094D\u0951-\u0954\u0962\u0963\u0981-\u0983\u09BC\u09BE\u09BF\u09C0-\u09C4\u09C7\u09C8\u09CB\u09CD\u09D7\u09E2\u09E3\u0A02\u0A3C\u0A3E\u0A3F\u0A40-\u0A42\u0A47\u0A48\u0A4B-\u0A4D\u0A70\u0A71\u0A81-\u0A83\u0ABC\u0ABE-\u0AC5\u0AC7-\u0AC9\u0ACB-\u0ACD\u0B01-\u0B03\u0B3C\u0B3E-\u0B43\u0B47\u0B48\u0B4B-\u0B4D\u0B56\u0B57\u0B82\u0B83\u0BBE-\u0BC2\u0BC6-\u0BC8\u0BCA-\u0BCD\u0BD7\u0C01-\u0C03\u0C3E-\u0C44\u0C46-\u0C48\u0C4A-\u0C4D\u0C55\u0C56\u0C82\u0C83\u0CBE-\u0CC4\u0CC6-\u0CC8\u0CCA-\u0CCD\u0CD5\u0CD6\u0D02\u0D03\u0D3E-\u0D43\u0D46-\u0D48\u0D4A-\u0D4D\u0D57\u0E31\u0E34-\u0E3A\u0E47-\u0E4E\u0EB1\u0EB4-\u0EB9\u0EBB\u0EBC\u0EC8-\u0ECD\u0F18\u0F19\u0F35\u0F37\u0F39\u0F3E\u0F3F\u0F71-\u0F84\u0F86-\u0F8B\u0F90-\u0F95\u0F97\u0F99-\u0FAD\u0FB1-\u0FB7\u0FB9\u20D0-\u20DC\u20E1\u302A-\u302F\u3099\u309A]
			{
				return new Symbol(ParserSymbols.COMBININGCHAR, yytext());
			}
<YYINITIAL>","
			{
				return new Symbol(ParserSymbols.COMMA, yytext());
			}
<YYINITIAL>[\u0030-\u0039\u0660-\u0669\u06F0-\u06F9\u0966-\u096F\u09E6-\u09EF\u0A66-\u0A6F\u0AE6-\u0AEF\u0B66-\u0B6F\u0BE7-\u0BEF\u0C66-\u0C6F\u0CE6-\u0CEF\u0D66-\u0D6F\u0E50-\u0E59\u0ED0-\u0ED9\u0F20-\u0F29]
			{
				return new Symbol(ParserSymbols.DIGIT, yytext());
			}
<YYINITIAL>"$"
			{
				return new Symbol(ParserSymbols.DOLLAR, yytext());
			}
<YYINITIAL>"."
			{
				return new Symbol(ParserSymbols.DOT, yytext());
			}
<YYINITIAL>"="
			{
				return new Symbol(ParserSymbols.EQUALS, yytext());
			}
<YYINITIAL>"!"
			{
				return new Symbol(ParserSymbols.EXCLAM, yytext());
			}
<YYINITIAL>[\u00B7\u02D0\u02D1\u0387\u0640\u0E46\u0EC6\u3005\u3031-\u3035\u309D\u309E\u30FC-\u30FE]
			{
				return new Symbol(ParserSymbols.EXTENDER, yytext());
			}
<YYINITIAL>">"
			{
				return new Symbol(ParserSymbols.GT, yytext());
			}
<YYINITIAL>"#"
			{
				return new Symbol(ParserSymbols.HASH, yytext());
			}
<YYINITIAL>[\u3007\u3021-\u3029\u4E00-\u9FA5]
			{
				return new Symbol(ParserSymbols.IDEOGRAPHIC, yytext());
			}
<YYINITIAL>[\u0000-\u0008\u000B\u000C\u000E-\u001F]
			{
				throw new LexerException(Message.LEXEREXCEPTION(Message.DTDLEXER_ILLEGAL_CHARACTER(), yytext()));
			}
<YYINITIAL>"{"
			{
				return new Symbol(ParserSymbols.LBRACE, yytext());
			}
<YYINITIAL>"("
			{
				return new Symbol(ParserSymbols.LPAREN, yytext());
			}
<YYINITIAL>"["
			{
				return new Symbol(ParserSymbols.LSQUARE, yytext());
			}
<YYINITIAL>"<"
			{
				return new Symbol(ParserSymbols.LT, yytext());
			}
<YYINITIAL>"-"
			{
				return new Symbol(ParserSymbols.MINUS, yytext());
			}
<YYINITIAL>"%"
			{
				return new Symbol(ParserSymbols.PERCENT, yytext());
			}
<YYINITIAL>"|"
			{
				return new Symbol(ParserSymbols.PIPE, yytext());
			}
<YYINITIAL>"+"
			{
				return new Symbol(ParserSymbols.PLUS, yytext());
			}
<YYINITIAL>"?"
			{
				return new Symbol(ParserSymbols.QMARK, yytext());
			}
<YYINITIAL>"\u0022"
			{
				return new Symbol(ParserSymbols.QUOT, yytext());
			}
<YYINITIAL>"}"
			{
				return new Symbol(ParserSymbols.RBRACE, yytext());
			}
<YYINITIAL>")"
			{
				return new Symbol(ParserSymbols.RPAREN, yytext());
			}
<YYINITIAL>"]"
			{
				return new Symbol(ParserSymbols.RSQUARE, yytext());
			}
<YYINITIAL>";"
			{
				return new Symbol(ParserSymbols.SEMI, yytext());
			}
<YYINITIAL>"/"
			{
				return new Symbol(ParserSymbols.SLASH, yytext());
			}
<YYINITIAL>"*"
			{
				return new Symbol(ParserSymbols.STAR, yytext());
			}
<YYINITIAL>"~"
			{
				return new Symbol(ParserSymbols.TILDE, yytext());
			}
<YYINITIAL>"_"
			{
				return new Symbol(ParserSymbols.UNDERSCORE, yytext());
			}
<<EOF>>
			{
				if (yymoreStreams()) {
					yypopStream();
				} else {
					return null;
				}
			}
<YYINITIAL>[\u0000-\uFFFF]
			{
				throw new LexerException(Message.LEXEREXCEPTION(Message.DTDLEXER_UNKNOWN_CHARACTER(), yytext()));
			}
<YYIGNORE>[\u0000-\uFFFF]
			{/* Do nothing */}
