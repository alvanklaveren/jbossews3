package com.myforum.application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Scanner;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public final class IoLogics{
	
	static{ new IoLogics(); }

   	private static Logger log = LoggerFactory.getLogger(IoLogics.class);

	private IoLogics(){}
	
	public static byte[] readImageFile( FileUploadField fileUploadField, String URL ){
		byte[] fileContent = null;
		List<FileUpload> uploads = fileUploadField.getFileUploads();
		 
		// in case it becomes multiselect in a later development stage
		if( uploads != null ){
            for (FileUpload upload : uploads){
            	fileContent = upload.getBytes();
            }
        }

        return( fileContent );
	}
	
	public static String convertStreamToString( InputStream is ) {
	    @SuppressWarnings("resource")
		Scanner scanner = new Scanner(is);
	    scanner.useDelimiter("\\A");
	    return scanner.hasNext() ? scanner.next() : "";
	}

	public static String readTextFile( FileUploadField fileUploadField ){
		InputStream inputStream 	= null;
		List<FileUpload> uploads	= fileUploadField.getFileUploads();
		String fileContent 			= "";
		
		if( uploads != null && uploads.size() == 1 ) { 
			try {
					inputStream 	= uploads.get(0).getInputStream();
					fileContent 	= convertStreamToString( inputStream );
			} catch (IOException e) {
				e.printStackTrace();
				log.error("failed to readfile in readTextFile(fileUploadField)");
			}
		}
   
        return( fileContent );
	}

	public static void saveToFile( String fileName, String content ) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter( fileName, "UTF-8" );
		writer.println( content );
		writer.close();
	}

	public static String readFromFile( String fileName ) throws IOException{
	    StringBuilder sb = new StringBuilder();
	    BufferedReader br = new BufferedReader( new FileReader( fileName ) );	        
        String line = br.readLine();

        while ( line != null ) {
            sb.append( line );
            //sb.append( System.getProperty( "line.separator" ) );
            line = br.readLine();
        }
        br.close();

        return sb.toString();

	}

}
