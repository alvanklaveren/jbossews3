package com.myforum.application;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public final class StringLogics{
	
	static{ new StringLogics(); }

   	private static Logger log = LoggerFactory.getLogger(StringLogics.class);

	private StringLogics(){}
	
	public static String nvl(String aString){
		return StringLogics.nvl(aString, "");
	}
	
	public static String nvl(String aString, String defaultString){
		if( StringLogics.isEmpty(defaultString) ){ defaultString = ""; }
		if( StringLogics.isEmpty(aString) ){ aString = defaultString; }

		return aString;
	}
	
	public static boolean isEmpty( String aString ){
		if( aString == null || aString.trim().equals( "" ) ){
			return true;
		}
		return false;
	}

	public String urlEncode(String message){
		String encodedMessage = ""; 
		
		try {
			encodedMessage = URLEncoder.encode( message, "UTF-8" );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("unable to encode message");
		}
		
		return encodedMessage; 
	}

	public String urlDecode(String message){
		String decodedMessage = ""; 
		
		try {
			decodedMessage = URLDecoder.decode( message, "UTF-8" );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("unable to decode message");
		}
		
		return decodedMessage; 
	}

	/*
	 *  This function does the setEscapeModelStrings( true ) for text that is in HTML. However, because
	 *  I introduced a possibility to present messages in bold, italic etc, we should enforce this by deciding ourselves
	 *  what is allowed and what is not
	 */
	public static String prepareMessage( String messageText ){
		String preparedText = messageText;
		preparedText = preparedText.replaceAll( "<", "&lt" );
		preparedText = preparedText.replaceAll( ">", "&gt" );

		preparedText = convertToHTML( preparedText, "`", "<span style=\"background:#DDD\">", "</span>" );
		preparedText = convertToHTML( preparedText, "***", "<b><i>", "</i></b>" );
		preparedText = convertToHTML( preparedText, "**", "<b>", "</b>" );
		preparedText = convertToHTML( preparedText, "*", "<i>", "</i>" );		
		
		preparedText = setHyperLink( preparedText );
		
		return preparedText;
	}
	
	public static String convertToHTML( String messageText, String source, String targetStart, String targetEnd ){
		boolean firstTag = true;
		StringBuilder buildString = new StringBuilder();
		
		for( int i = 0; i < messageText.length(); i++ ){
			if( i < messageText.length() - ( source.length() - 1 ) && messageText.substring( i, i + source.length() ).equals( source ) ) {
				if(firstTag ){
					buildString.append( targetStart );
					firstTag = false;
				}else{
					buildString.append( targetEnd );				
					firstTag = true;
				}
				i += ( source.length() - 1 );
			}else{
				buildString.append( messageText.charAt( i ) );				
			}
		}	
		return( buildString.toString().trim() );

	}

	public static String setHyperLink( String messageText ){
		String source = "[h:";
		StringBuilder buildString = new StringBuilder();
		
		
		for( int i = 0; i < messageText.length(); i++ ){
			if( i < messageText.length() - ( source.length() - 1 ) && messageText.substring( i, i + source.length() ).equals( source ) ) {
				i = i + source.length();
				int k = i;
				while( k < (messageText.length() - 1) && messageText.charAt( k ) != ']' ){
					k++;
				}
				
				String hyperlink = "<a target=\"_blank\" rel=\"nofollow\" href=\"";
				if(!messageText.substring( i, k ).startsWith("http")){
					hyperlink += "http://"; 
				}
				hyperlink += messageText.substring( i, k ) + "\">" + messageText.substring( i, k ) + "</a>"; 
				buildString.append( hyperlink );		
				i = k;
				
			}else{
				buildString.append( messageText.charAt( i ) );				
			}
		}	
		return( buildString.toString().trim() );

	}

	/*
	 *  This function does the setEscapeModelStrings( true ) for text that is in HTML. 
	 *  To make editing easier, **** will be recognised as <table class=sourcetext>
	 */
	public static String prepareSourceText( String messageText ){
		String preparedText = messageText;
		preparedText = preparedText.replaceAll( " ", "&nbsp;" );
		preparedText = preparedText.replaceAll( "%n", "<br>" );
		preparedText = convertToHTML( preparedText, "****", "<table class=sourcetext><tr><td class=sourcetext>", "</td></tr></table>" );
		
		return preparedText;
	}

	/**
	 * Validate hex with regular expression
	 * 
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public static boolean validateEmailAddress( final String hex ){
		Pattern pattern;
		Matcher matcher;
	 
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	 
		pattern = Pattern.compile( EMAIL_PATTERN );
		matcher = pattern.matcher( hex );
		return matcher.matches();	 
	}

	// translate (roman) numbers at the end of a string, if any. E.g. a 2 becomes II, and a IV becomes 4
	public static String convertVersionNumbers(String str){
		if(str.lastIndexOf(" ", str.length()) <= 0){
			return str;
		}
		log.info("String: " + str);
		String strStart = str.substring(0, str.lastIndexOf(" ", str.length()) + 1);
		log.info("Start: " + strStart);
		String strEnd   = str.substring(str.lastIndexOf(" ", str.length()), str.length());
		log.info("End: " + strEnd);
		
		switch(strEnd.trim()){
	    	case "I": 		strStart += "1"; break;
	    	case "II": 		strStart += "2"; break;
	    	case "III": 	strStart += "3"; break;
	    	case "IV": 		strStart += "4"; break;
	    	case "V": 		strStart += "5"; break;
	    	case "VI": 		strStart += "6"; break;
	    	case "VII": 	strStart += "7"; break;
	    	case "VIII": 	strStart += "8"; break;
	    	case "IX": 		strStart += "9"; break;
	    	case "X": 		strStart += "10"; break;
	    	case "XI": 		strStart += "11"; break;
	    	case "XII": 	strStart += "12"; break;
	    	case "XIII": 	strStart += "13"; break;
	    	case "XIV": 	strStart += "14"; break;
	    	case "XV": 		strStart += "15"; break;
	    	case "XVI": 	strStart += "16"; break;
	    	case "XVII": 	strStart += "17"; break;
	    	case "XVIII": 	strStart += "18"; break;
	    	case "XIX": 	strStart += "19"; break;
	    	case "XX": 		strStart += "20"; break;
	
	    	case "1":		/*strStart += "I"*/; 	break; // this will almost never happen (a version 1). So to prevent find result on II, III, etc. SKIP
	    	case "2":		strStart += "II"; 		break;
	    	case "3":		strStart += "III"; 		break;
	    	case "4":		strStart += "IV"; 		break;
	    	case "5":		strStart += "V"; 		break;
	    	case "6":		strStart += "VI"; 		break;
	    	case "7":		strStart += "VII"; 		break;
	    	case "8":		strStart += "VIII"; 	break;
	    	case "9":		strStart += "IX"; 		break;
	    	case "10":		strStart += "X"; 		break;
	    	case "11":		strStart += "XI"; 		break;
	    	case "12":		strStart += "XII"; 		break;
	    	case "13":		strStart += "XIII"; 	break;
	    	case "14":		strStart += "XIV"; 		break;
	    	case "15":		strStart += "XV"; 		break;
	    	case "16":		strStart += "XVI"; 		break;
	    	case "17":		strStart += "XVII"; 	break;
	    	case "18":		strStart += "XVIII"; 	break;
	    	case "19":		strStart += "XIX"; 		break;
	    	case "20":		strStart += "XX"; 		break;
	    	default:		strStart = str;			break;
		}
		log.info(strStart);
		
		// next, find version numbers that are somewhere in the middle of a string
		strStart = strStart.replace(" II ", 	" 2 ");
		strStart = strStart.replace(" III ", 	" 3 ");
		strStart = strStart.replace(" IV ", 	" 4 ");
		strStart = strStart.replace(" V ", 		" 5 ");
		strStart = strStart.replace(" VI ", 	" 6 ");
		strStart = strStart.replace(" VII ", 	" 7 ");
		strStart = strStart.replace(" VIII ", 	" 8 ");
		strStart = strStart.replace(" IX ", 	" 9 ");
		strStart = strStart.replace(" X ", 		" 10 ");
		strStart = strStart.replace(" XI ", 	" 11 ");
		strStart = strStart.replace(" XII ", 	" 12 ");
		strStart = strStart.replace(" XIII ", 	" 13 ");
		strStart = strStart.replace(" XIV ", 	" 14 ");
		strStart = strStart.replace(" XV ", 	" 15 ");
		strStart = strStart.replace(" XVI ", 	" 16 ");
		strStart = strStart.replace(" XVII ", 	" 17 ");
		strStart = strStart.replace(" XVIII ", 	" 18 ");
		strStart = strStart.replace(" XIX ", 	" 19 ");
		strStart = strStart.replace(" XX ", 	" 20 ");

		// and vice versa
		strStart = strStart.replace(" 2 ", 		" II ");
		strStart = strStart.replace(" 3 ", 		" III ");
		strStart = strStart.replace(" 4 ", 		" IV ");
		strStart = strStart.replace(" 5 ", 		" V ");
		strStart = strStart.replace(" 6 ", 		" VI ");
		strStart = strStart.replace(" 7 ", 		" VII ");
		strStart = strStart.replace(" 8 ", 		" VIII ");
		strStart = strStart.replace(" 9 ", 		" IX ");
		strStart = strStart.replace(" 10 ", 	" X ");
		strStart = strStart.replace(" 11 ", 	" XI ");
		strStart = strStart.replace(" 12 ", 	" XII ");
		strStart = strStart.replace(" 13 ", 	" XIII ");
		strStart = strStart.replace(" 14 ", 	" XIV ");
		strStart = strStart.replace(" 15 ", 	" XV ");
		strStart = strStart.replace(" 16 ", 	" XVI ");
		strStart = strStart.replace(" 17 ", 	" XVII ");
		strStart = strStart.replace(" 18 ", 	" XVIII ");
		strStart = strStart.replace(" 19 ", 	" XIX ");
		strStart = strStart.replace(" 20 ", 	" XX ");
		
		log.info(strStart);
		return strStart;
	}
	
}
