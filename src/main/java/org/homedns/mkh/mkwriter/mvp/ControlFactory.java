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

import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;

/**
 * GUI Control factory
 *
 */
public class ControlFactory {	
	
	/**
	 * Creates new control.
	 * 
	 * @param type
	 *            the control type
	 * @param config
	 *            the control configuration object
	 * 
	 * @return the control
	 */
	@SuppressWarnings( "unchecked" )
	public static < V extends T, T > V create( Class< V > type, Config< T > config ) {
		Object c = null;
		if( type ==  Button.class) {
			c = new Button( );
		} else if( type == MenuButton.class ) {
			c = new MenuButton( );			
		} else if( type == MenuItem.class ) {
			c = new MenuItem( );
		} else if( type == ToolBar.class ) {
			c = new ToolBar( );
		} else {
			throw new IllegalArgumentException( type.getName( ) );
		}
		config.apply( ( T )c );
		return( ( V )c );
	}
}
