/* 
 * Copyright 2019 Mikhail Khodonov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.homedns.mkh.mkwriter;

import org.homedns.mkh.mkwriter.mvp.GenericModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import org.homedns.mkh.mkwriter.mvp.Data;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 * Section tree model
 *
 */
public class SectionTreeModel extends GenericModel {
	
	public SectionTreeModel( ) {
	}

	/**
	 * @see org.homedns.mkh.mkwriter.mvp.Model#execute(java.lang.String, org.homedns.mkh.mkwriter.mvp.Data)
	 */
	@Override
	public Data execute( String sCommand, Data params ) throws Exception {
		Data result = null;
		String sExt = params.getValue( Data.FILE_EXT_KEY );
		switch( sCommand ) {
		case Parameters.OPEN:
			switch( sExt ) {
			case "pdf":
				result = importFromPDF( params );
				break;
			case "rtf":
				result = importFromRTF( params );
				break;
			case "mkw":
				result = open( params );
				break;
			default:
				break;
			}
			break;
		case Parameters.SAVE:
			result = save( params );
			break;
		case Parameters.SAVE_AS:
			switch( sExt ) {
			case "pdf":
				result = export2PDF( params );
				break;
			case "rtf":
				result = export2RTF( params );
				break;
			case "mkw":
				result = save( params );
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		return( result );
	}
	
	/**
	 * Saves data to the file. Data and file name are specified in parameters object
	 * 
	 * @param params the parameters object
	 * 
	 * @return the empty data object
	 * 
	 * @throws Exception
	 */
	protected Data save( Data params ) throws Exception {
		Data result = new Parameters( );
		File file = params.getValue( Data.FILE_KEY );
		try( FileWriter writer = new FileWriter( file ) ) {
			writer.write( params.getJSON( ) );
		}
		return( result );
	}

	/**
	 * Opens and read data from the file which specified in parameters object. 
	 * Returns result object
	 * 
	 * @param params the parameters object
	 * 
	 * @return the result object
	 * 
	 * @throws Exception
	 */
	protected Data open( Data params ) throws Exception {
		Data result = new Parameters( );
		File file = params.getValue( Data.FILE_KEY );
		try( JsonReader in = new JsonReader( new FileReader( file ) ) ) {
	        JsonParser parser = new JsonParser( );
	        JsonElement element = parser.parse( in );
			result.putJSON( element );			
		}
		return( result );
	}

	/**
	 * Saves data to the pdf file. Data and file name are specified in parameters object
	 * 
	 * @param params the parameters object
	 * 
	 * @return the empty data object
	 * 
	 * @throws Exception
	 */
	protected Data export2PDF( Data params ) throws Exception {
		Data result = new Parameters( );
		ITextRenderer renderer = new ITextRenderer( );
		renderer.setDocumentFromString( getHTML( params.getValue( Data.DATA_KEY ) ) );
		renderer.layout( );
		File file = params.getValue( Data.FILE_KEY );
		try( FileOutputStream stream = new FileOutputStream( file ) ) {
			renderer.createPDF( stream );
		}
		return( result );
	}

	/**
	 * Saves data to the rtf file. Data and file name are specified in parameters object
	 * 
	 * @param params the parameters object
	 * 
	 * @return the empty data object
	 * 
	 * @throws Exception
	 */
	protected Data export2RTF( Data params ) throws Exception {
		Data result = new Parameters( );
		return( result );
	}

	protected Data importFromPDF( Data params ) throws Exception {
		Data result = new Parameters( );
		return( result );
	}

	protected Data importFromRTF( Data params ) throws Exception {
		Data result = new Parameters( );
		return( result );
	}
	
	private String getHTML( String sContent ) {
		Document document = Jsoup.parse( sContent );
		document.outputSettings( ).syntax( Document.OutputSettings.Syntax.xml );
		return( document.html( ) );
	}
}
