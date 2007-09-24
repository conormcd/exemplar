// vim:filetype=java:ts=4
/*
	Copyright (c) 2006, 2007
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
package junit.com.mcdermottroe.exemplar.ui;

import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.ui.MessageException;

import junit.com.mcdermottroe.exemplar.UtilityClassTestCase;


/** Test class for {@link Message}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class MessageTest
extends UtilityClassTestCase<Message>
{
	/** Test {@link Message#localise()}. */
	public void testLocalise() {
		try {
			Message.localise();
		} catch (MessageException e) {
			assertNotNull("MessageException was null", e);
			fail("Message.localise threw an exception");
		}
	}

	/** Test {@link Message#ANT_LOCALISATION_ERROR()}. */
	public void testANT_LOCALISATION_ERROR() {
		String message = Message.ANT_LOCALISATION_ERROR();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#ASSERTION_MESSAGE(String, String)}. */
	public void testASSERTION_MESSAGE() {
		String message = Message.ASSERTION_MESSAGE("foo", "bar");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#ATTEMPTING_TO_PARSE(String, String)}. */
	public void testATTEMPTING_TO_PARSE() {
		String message = Message.ATTEMPTING_TO_PARSE("foo", "bar");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#COPYRIGHT()}. */
	public void testCOPYRIGHT() {
		String message = Message.COPYRIGHT();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#DEBUG_CLASS_AND_METHOD(String, String)}. */
	public void testDEBUG_CLASS_AND_METHOD() {
		String message = Message.DEBUG_CLASS_AND_METHOD("foo", "bar");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#DTDLEXER_ILLEGAL_CHARACTER()}. */
	public void testDTDLEXER_ILLEGAL_CHARACTER() {
		String message = Message.DTDLEXER_ILLEGAL_CHARACTER();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#DTDLEXER_INPUT_NOT_FOUND(String)}. */
	public void testDTDLEXER_INPUT_NOT_FOUND() {
		String message = Message.DTDLEXER_INPUT_NOT_FOUND("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#DTDLEXER_INVALID_CONDITIONAL_SECTION()}. */
	public void testDTDLEXER_INVALID_CONDITIONAL_SECTION() {
		String message = Message.DTDLEXER_INVALID_CONDITIONAL_SECTION();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#DTDLEXER_INVALID_TEXTDECL()}. */
	public void testDTDLEXER_INVALID_TEXTDECL() {
		String message = Message.DTDLEXER_INVALID_TEXTDECL();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#DTDLEXER_UNKNOWN_CHARACTER()}. */
	public void testDTDLEXER_UNKNOWN_CHARACTER() {
		String message = Message.DTDLEXER_UNKNOWN_CHARACTER();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#DTDPARSER_THREW_EXCEPTION()}. */
	public void testDTDPARSER_THREW_EXCEPTION() {
		String message = Message.DTDPARSER_THREW_EXCEPTION();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#DTDPEDECLTABLE(int, int)}. */
	public void testDTDPEDECLTABLE() {
		String message = Message.DTDPEDECLTABLE(0, 1);
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#DTDPEEXCEPTION(String, String)}. */
	public void testDTDPEEXCEPTION() {
		String message = Message.DTDPEEXCEPTION("foo", "bar");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#DTDPE_GARBAGE_AFTER_SYSTEMLITERAL()}. */
	public void testDTDPE_GARBAGE_AFTER_SYSTEMLITERAL() {
		String message = Message.DTDPE_GARBAGE_AFTER_SYSTEMLITERAL();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#DTDPE_INVALID_PEDECL()}. */
	public void testDTDPE_INVALID_PEDECL() {
		String message = Message.DTDPE_INVALID_PEDECL();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#DTDPE_INVALID_PUBIDLITERAL()}. */
	public void testDTDPE_INVALID_PUBIDLITERAL() {
		String message = Message.DTDPE_INVALID_PUBIDLITERAL();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#DTDPE_INVALID_SYSTEMLITERAL()}. */
	public void testDTDPE_INVALID_SYSTEMLITERAL() {
		String message = Message.DTDPE_INVALID_SYSTEMLITERAL();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#DTDPE_UNDECLARED_PE(String)}. */
	public void testDTDPE_UNDECLARED_PE() {
		String message = Message.DTDPE_UNDECLARED_PE("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#DTDPE_UNRESOLVED_PE_REF()}. */
	public void testDTDPE_UNRESOLVED_PE_REF() {
		String message = Message.DTDPE_UNRESOLVED_PE_REF();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#EXCEPTION_NO_MESSAGE()}. */
	public void testEXCEPTION_NO_MESSAGE() {
		String message = Message.EXCEPTION_NO_MESSAGE();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#FILE_WRITE_FAILED(String)}. */
	public void testFILE_WRITE_FAILED() {
		String message = Message.FILE_WRITE_FAILED("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#FILE_WRITE_IO_EXCEPTION(String)}. */
	public void testFILE_WRITE_IO_EXCEPTION() {
		String message = Message.FILE_WRITE_IO_EXCEPTION("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#GEN_CLASS_FROM_ELEMENT_FAILED(String)}. */
	public void testGEN_CLASS_FROM_ELEMENT_FAILED() {
		String message = Message.GEN_CLASS_FROM_ELEMENT_FAILED("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#GEN_GETTER_FROM_ATT_FAILED(String)}. */
	public void testGEN_GETTER_FROM_ATT_FAILED() {
		String message = Message.GEN_GETTER_FROM_ATT_FAILED("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#GEN_NO_SUCH_DIRECTORY(String)}. */
	public void testGEN_NO_SUCH_DIRECTORY() {
		String message = Message.GEN_NO_SUCH_DIRECTORY("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#GEN_SETTER_FROM_ATT_FAILED(String)}. */
	public void testGEN_SETTER_FROM_ATT_FAILED() {
		String message = Message.GEN_SETTER_FROM_ATT_FAILED("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#GEN_VARNAME_FROM_ATT_FAILED(String)}. */
	public void testGEN_VARNAME_FROM_ATT_FAILED() {
		String message = Message.GEN_VARNAME_FROM_ATT_FAILED("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#GENERIC_SECURITY_EXCEPTION(String, String)}. */
	public void testGENERIC_SECURITY_EXCEPTION() {
		String message = Message.GENERIC_SECURITY_EXCEPTION("foo", "bar");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#IGNORING_ERROR()}. */
	public void testIGNORING_ERROR() {
		String message = Message.IGNORING_ERROR();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#IGNORING_EXCEPTION()}. */
	public void testIGNORING_EXCEPTION() {
		String message = Message.IGNORING_EXCEPTION();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#LANGUAGE_DTD()}. */
	public void testLANGUAGE_DTD() {
		String message = Message.LANGUAGE_DTD();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#LANGUAGE_JAVA()}. */
	public void testLANGUAGE_JAVA() {
		String message = Message.LANGUAGE_JAVA();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#LANGUAGE_XSLT()}. */
	public void testLANGUAGE_XSLT() {
		String message = Message.LANGUAGE_XSLT();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#LEXEREXCEPTION(String, String)}. */
	public void testLEXEREXCEPTION() {
		String message = Message.LEXEREXCEPTION("foo", "bar");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#LOCALISATION_ERROR(String)}. */
	public void testLOCALISATION_ERROR() {
		String message = Message.LOCALISATION_ERROR("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#MALFORMED_CHAR_REF(CharSequence)}. */
	public void testMALFORMED_CHAR_REF() {
		String message = Message.MALFORMED_CHAR_REF("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#MANDATORY_OPTIONS_NOT_SET()}. */
	public void testMANDATORY_OPTIONS_NOT_SET() {
		String message = Message.MANDATORY_OPTIONS_NOT_SET();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#MESSAGE_EXTRA_BUNDLE_ENTRY(String, String)}. */
	public void testMESSAGE_EXTRA_BUNDLE_ENTRY() {
		String message = Message.MESSAGE_EXTRA_BUNDLE_ENTRY("foo", "bar");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#MESSAGE_MISSING(String)}. */
	public void testMESSAGE_MISSING() {
		String message = Message.MESSAGE_MISSING("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#MISSING_MANDATORY_OPTION(String)}. */
	public void testMISSING_MANDATORY_OPTION() {
		String message = Message.MISSING_MANDATORY_OPTION("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#NO_GENERATOR(String, String)}. */
	public void testNO_GENERATOR() {
		String message = Message.NO_GENERATOR("foo", "bar");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#OPTION_DEFAULT(String)}. */
	public void testOPTION_DEFAULT() {
		String message = Message.OPTION_DEFAULT("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#OPTION_ENUM_ARGS_HEADER()}. */
	public void testOPTION_ENUM_ARGS_HEADER() {
		String message = Message.OPTION_ENUM_ARGS_HEADER();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#OPTION_IS_MANDATORY()}. */
	public void testOPTION_IS_MANDATORY() {
		String message = Message.OPTION_IS_MANDATORY();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#OPTION_LANGUAGE_OF_API(String)}. */
	public void testOPTION_LANGUAGE_OF_API() {
		String message = Message.OPTION_LANGUAGE_OF_API("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#OPTION_LANGUAGE_REQUIRES_API()}. */
	public void testOPTION_LANGUAGE_REQUIRES_API() {
		String message = Message.OPTION_LANGUAGE_REQUIRES_API();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#OPTIONS_INITIALISING()}. */
	public void testOPTIONS_INITIALISING() {
		String message = Message.OPTIONS_INITIALISING();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#OPTIONS_NO_SUCH_OPTION(String)}. */
	public void testOPTIONS_NO_SUCH_OPTION() {
		String message = Message.OPTIONS_NO_SUCH_OPTION("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#SOURCE_GENERATOR_DOCTYPE_NULL()}. */
	public void testSOURCE_GENERATOR_DOCTYPE_NULL() {
		String message = Message.SOURCE_GENERATOR_DOCTYPE_NULL();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#SOURCE_GENERATOR_LANGUAGE_NULL()}. */
	public void testSOURCE_GENERATOR_LANGUAGE_NULL() {
		String message = Message.SOURCE_GENERATOR_LANGUAGE_NULL();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#SOURCE_GENERATOR_THREW_EXCEPTION()}. */
	public void testSOURCE_GENERATOR_THREW_EXCEPTION() {
		String message = Message.SOURCE_GENERATOR_THREW_EXCEPTION();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#UI_PROGRESS_DONE()}. */
	public void testUI_PROGRESS_DONE() {
		String message = Message.UI_PROGRESS_DONE();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#UI_PROGRESS_FAILED_TO_CREATE_OUTPUT()}. */
	public void testUI_PROGRESS_FAILED_TO_CREATE_OUTPUT() {
		String message = Message.UI_PROGRESS_FAILED_TO_CREATE_OUTPUT();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#UI_PROGRESS_FINISHED_TIME(double)}. */
	public void testUI_PROGRESS_FINISHED_TIME() {
		String message = Message.UI_PROGRESS_FINISHED_TIME(1.0);
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#UI_PROGRESS_GENERATING_PARSER()}. */
	public void testUI_PROGRESS_GENERATING_PARSER() {
		String message = Message.UI_PROGRESS_GENERATING_PARSER();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#UI_PROGRESS_INPUT_PARSE_FAILED()}. */
	public void testUI_PROGRESS_INPUT_PARSE_FAILED() {
		String message = Message.UI_PROGRESS_INPUT_PARSE_FAILED();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#UI_PROGRESS_OPTIONS()}. */
	public void testUI_PROGRESS_OPTIONS() {
		String message = Message.UI_PROGRESS_OPTIONS();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#UI_PROGRESS_PARSING_INPUT(String)}. */
	public void testUI_PROGRESS_PARSING_INPUT() {
		String message = Message.UI_PROGRESS_PARSING_INPUT("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#UNREACHABLE_CODE_REACHED()}. */
	public void testUNREACHABLE_CODE_REACHED() {
		String message = Message.UNREACHABLE_CODE_REACHED();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#UNSUPPORTED_INPUT_TYPE()}. */
	public void testUNSUPPORTED_INPUT_TYPE() {
		String message = Message.UNSUPPORTED_INPUT_TYPE();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#UNTERMINATED_REF()}. */
	public void testUNTERMINATED_REF() {
		String message = Message.UNTERMINATED_REF();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#XMLDOCTYPE_ORPHAN_ATTLIST(String)}. */
	public void testXMLDOCTYPE_ORPHAN_ATTLIST() {
		String message = Message.XMLDOCTYPE_ORPHAN_ATTLIST("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#XMLDOCTYPE_UNSUPPORTED_FEATURE()}. */
	public void testXMLDOCTYPE_UNSUPPORTED_FEATURE() {
		String message = Message.XMLDOCTYPE_UNSUPPORTED_FEATURE();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#XMLDOCTYPE_XMLOBJECT_IN_MARKUPDECLS(String)}. */
	public void testXMLDOCTYPE_XMLOBJECT_IN_MARKUPDECLS() {
		String message = Message.XMLDOCTYPE_XMLOBJECT_IN_MARKUPDECLS("foo");
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#XMLOBJECT_NOT_CONFIGURED()}. */
	public void testXMLOBJECT_NOT_CONFIGURED() {
		String message = Message.XMLOBJECT_NOT_CONFIGURED();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}

	/** Test {@link Message#XMLPARSER_LOAD_CODE_FRAGMENT_FAILED()}. */
	public void testXMLPARSER_LOAD_CODE_FRAGMENT_FAILED() {
		String message = Message.XMLPARSER_LOAD_CODE_FRAGMENT_FAILED();
		assertNotNull("Message was null", message);
		assertNotSame("Message was a zero-length String", 0,  message.length());
	}
}
