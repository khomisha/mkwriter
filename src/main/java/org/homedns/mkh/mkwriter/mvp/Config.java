/*
 * Copyright 2019 Mikhail Khodonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * $Id$
 */

package org.homedns.mkh.mkwriter.mvp;

/**
 * Component configuration interface 
 *
 */
public interface Config< T > {
	/**
	* Action handler, default null
	*/
	public static final Integer ACTION_HANDLER = 1;
	/**
	* Mnemonic parsing, default false
	*/
	public static final Integer MNEMONIC_PARSING = 2;
	/**
	* Text, default null
	*/
	public static final Integer TEXT = 3;
	/**
	* Visible property, default null
	*/
	public static final Integer VISIBLE_PROPERTY = 4;
	/**
	* Disable property, default null
	*/
	public static final Integer DISABLE_PROPERTY = 5;
	
	/**
	 * Sets attribute value
	 * 
	 * @param iKey
	 *            the attribute key
	 * @param value
	 *            the attribute value
	 */
	public void setAttribute( Integer iKey, Object value );

	/**
	 * Returns attribute value
	 * 
	 * @param iKey
	 *            the attribute key
	 * 
	 * @return the attribute value
	 */
	public Object getAttribute( Integer iKey );
	
	/**
	 * Applies configuration to specified object
	 * 
	 * @param object the target object
	 */
	public void apply( T object );
	
	/**
	 * Returns default id for gui elements
	 * 
	 * @return
	 */
	public default String getId( ) {
		Double num = Math.random( ) * 100000;
		return( "app-gen-" + String.valueOf( num.intValue( ) ) );
	}
}
