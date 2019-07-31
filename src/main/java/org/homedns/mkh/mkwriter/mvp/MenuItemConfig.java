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

package org.homedns.mkh.mkwriter.mvp;

import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

/**
 * Menu item configuration object
 *
 */
public class MenuItemConfig extends AttributeMap< Integer, Object > implements Config< MenuItem > {

	public MenuItemConfig( ) {
		setAttribute( TEXT, null );
		setAttribute( ACTION_HANDLER, null );
		setAttribute( MNEMONIC_PARSING, false );		
		setAttribute( VISIBLE_PROPERTY, null );
		setAttribute( DISABLE_PROPERTY, null );
	}
	
	/**
	 * @see org.homedns.mkh.mkwriter.mvp.Config#apply(java.lang.Object)
	 */
	@SuppressWarnings( "unchecked" )
	@Override
	public void apply( MenuItem item ) {
		item.setId( getId( ) );
        BooleanProperty bpDisable = ( BooleanProperty )getAttribute( DISABLE_PROPERTY );
        if( bpDisable != null ) { 
        	item.disableProperty( ).bind( bpDisable );
        }
        BooleanProperty bpVisible = ( BooleanProperty )getAttribute( VISIBLE_PROPERTY );
        if( bpVisible != null ) { 
        	item.disableProperty( ).bind( bpVisible );
        }
		String sText = ( String )getAttribute( TEXT );
        if( sText != null ) {
        	item.setText( sText );
        }
	    EventHandler< ActionEvent > handlerAction = ( EventHandler< ActionEvent > )getAttribute( ACTION_HANDLER );
	    if( handlerAction != null ) {
	    	item.setOnAction( handlerAction );
	    }
        item.setMnemonicParsing( ( Boolean )getAttribute( MNEMONIC_PARSING ) );
	}
}
