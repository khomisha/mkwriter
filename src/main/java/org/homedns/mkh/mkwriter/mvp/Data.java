/* 
 * Copyright 2018 Mikhail Khodonov.
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

package org.homedns.mkh.mkwriter.mvp;

import com.google.gson.JsonElement;

/**
 * Data interface
 *
 */
public interface Data {
	public static final String DATA_KEY = "data";
	public static final String FILE_KEY = "file";
	public static final String FILE_EXT_KEY = "file_ext";
	
	/**
	 * Sets the specified value with the specified key 
	 * 
	 * @param sKey whose associated value is to be returned
	 * @param value the value to set
	 */
	public void setValue( String sKey, Object value );
	
	/**
	 * Returns specified value
	 * 
	 * @param sKey the key whose associated value is to be returned
	 * 
	 * @return the specified value
	 */
	public < T > T getValue( String sKey );
	
	/**
	 * Returns data as xml string
	 * 
	 * @return the xml string
	 */
	public default String getXML( ) { 
		return( null ); 
	}

	/**
	 * Puts data from xml string to the data object
	 * 
	 * @param sXml the xml string to put
	 */
	public default void putXML( String sXml ) { }

	/**
	 * Returns data as json string
	 * 
	 * @return the data as json string
	 * 
	 * @throws Exception
	 */
	public default String getJSON( ) throws Exception {
		return( null );
	}

	/**
	 * Puts data from json element to the data object
	 * 
	 * @param element the source json element to put
	 * 
	 * @throws Exception
	 */
	public default void putJSON( JsonElement element ) throws Exception { }
}
