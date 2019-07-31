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

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.homedns.mkh.mkwriter.mvp.Data;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.scene.control.TreeItem;

/**
 * Command parameters object
 *
 */
public class Parameters implements Data {
	public static final String SAVE = "Save";
	public static final String SAVE_AS = "Save as";
	public static final String OPEN = "Open";

	private Map< String, Object > params;
	private final Gson gson;
	private String sJSON;
	private JsonElement jsonElement;
	
	public Parameters( ) {
		params = new HashMap< >( );
		gson = new GsonBuilder( ).excludeFieldsWithModifiers( Modifier.STATIC ).create( );
	}
	
	/**
	 * @see org.homedns.mkh.mkwriter.mvp.Data#setValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setValue( String sKey, Object value ) {
		params.put( sKey, value );
	}

	/**
	 * @see org.homedns.mkh.mkwriter.mvp.Data#getValue(java.lang.String)
	 */
	@SuppressWarnings( "unchecked" )
	@Override
	public < T > T getValue( String sKey ) {
		Object value = params.get( sKey );
		if( value == null ) {
			return( null );
		}
		return( ( T )value );
	}

	/**
	 * @see org.homedns.mkh.mkwriter.mvp.Data#getJSON()
	 */
	@Override
	public String getJSON( ) throws Exception {
		return( sJSON );
	}

	/**
	 * @see org.homedns.mkh.mkwriter.mvp.Data#putJSON(com.google.gson.JsonElement)
	 */
	@Override
	public void putJSON( JsonElement element ) throws Exception {
		this.jsonElement = element;
	}	
	
	/**
	 * Get data from json element
	 * 
	 * @throws Exception
	 */
	public void getDataFromJson( ) throws Exception {
		setValue( Data.DATA_KEY, getDataFromJson( jsonElement ) );
	}
	
	/**
	 * Gets data from json element to the tree item 
	 * 
	 * @param element the source json element
	 * 
	 * @return the target tree item 
	 * 
	 * @throws Exception
	 */
	private TreeItem< Section > getDataFromJson( JsonElement element ) throws Exception {
		TreeItem< Section > treeItem = new TreeItem< >( );
        if( element.isJsonObject( ) ) {
            JsonObject jsoSection = element.getAsJsonObject( );
            Section section = new Section( 
            	jsoSection.get( "title" ).getAsString( ),
            	jsoSection.get( "id" ).getAsString( ),
            	jsoSection.get( "content" ).getAsString( ) 
            ); 
            treeItem.setValue( section );
            JsonArray childs = jsoSection.getAsJsonArray( "childs" );
            if( childs != null ) {
	            for( JsonElement child : childs ) {
	            	treeItem.getChildren( ).add( getDataFromJson( child ) );
	            }
            }
        }
        return( treeItem );
	}
	
	/**
	 * Puts data from tree item to json string 
	 */
	public void putData2Json( ) {
		sJSON = putData2Json( getValue( Data.DATA_KEY ) ).toString( );
	}
	
	/**
	 * Puts the in json format to destination string buffer from specified tree item
	 * 
	 * @param src the source tree item
	 * 
	 * @return the result json string buffer 
	 */
	private StringBuffer putData2Json( TreeItem< Section > src ) {
		StringBuffer dest = new StringBuffer( );
		Section section = src.getValue( );
		section.getContent( );
		String s = gson.toJson( section ).replaceAll( "\\{", "" );
		s = s.replaceAll( "\\}", "" );
		s = "{ " + s + ",";
		dest.append( s );
		int iSize = src.getChildren( ).size( );
		int iChild = 0;
		dest.append( "\"childs\": [" );				
		for( TreeItem< Section > ti : src.getChildren( ) ) {
			dest.append( putData2Json( ti ) );
			if( iChild < iSize - 1 ) {
				dest.append( "," );
			}
			iChild++;
		}
		dest.append( "] }" );				
		return( dest );
	}
}
