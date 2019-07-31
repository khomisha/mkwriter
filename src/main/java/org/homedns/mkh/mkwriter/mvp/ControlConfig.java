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

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;

/**
 * Control configuration object
 *
 */
public class ControlConfig extends AttributeMap< Integer, Object > implements Config< Control > {
	private static final String DEFAULT_BKGD_COLOR = "-fx-background-color:transparent;";
			
	/**
	* Max width, default null
	*/
	public static final Integer MAX_WIDTH = 6;
	/**
	* Max height, default null
	*/
	public static final Integer MAX_HEIGHT = 7;
	/**
	* Min width, default null
	*/
	public static final Integer MIN_WIDTH = 8;
	/**
	* Min height, default null
	*/
	public static final Integer MIN_HEIGHT = 9;
	/**
	* Preferred width, defaults to the USE_COMPUTED_SIZE flag, 
	* which means that getPrefWidth(forHeight) will return the region's internally computed preferred width 
	*/
	public static final Integer PREF_WIDTH = 10;
	/**
	* Preferred height, defaults to the USE_COMPUTED_SIZE flag, 
	* which means that getPrefHeight(forHeight) will return the region's internally computed preferred height 
	*/
	public static final Integer PREF_HEIGHT = 11;
	/**
	* Background color on focus, default null
	*/
	public static final Integer BACKGROUD_COLOR_ON_FOCUS = 12;
	/**
	* Background color, default null
	*/
	public static final Integer BACKGROUD_COLOR = 13;
	/**
	* Tooltip, default null
	*/
	public static final Integer TOOLTIP = 14;

	public ControlConfig( ) {
		setAttribute( MAX_WIDTH, null );
		setAttribute( MAX_HEIGHT, null );
		setAttribute( MIN_WIDTH, null );
		setAttribute( MIN_HEIGHT, null );
		setAttribute( PREF_WIDTH, null );
		setAttribute( PREF_HEIGHT, null );
		setAttribute( DISABLE_PROPERTY, null );
		setAttribute( BACKGROUD_COLOR_ON_FOCUS, null );
		setAttribute( BACKGROUD_COLOR, null );
		setAttribute( VISIBLE_PROPERTY, null );
		setAttribute( TOOLTIP, null );
	}
	
	/**
	 * Applies configuration to the specified object
	 * 
	 * @param control the target object
	 */
	public void apply( Control control ) {
		control.setId( getId( ) );
		Double maxHeight = ( Double )getAttribute( MAX_HEIGHT );
        if( maxHeight != null ) {
        	control.setMaxHeight( maxHeight );
        }
		Double minHeight = ( Double )getAttribute( MIN_HEIGHT );
        if( minHeight != null ) {
        	control.setMinHeight( minHeight );
        }
		Double maxWidth = ( Double )getAttribute( MAX_WIDTH );
        if( maxWidth != null ) {
        	control.setMaxWidth( maxWidth );
        }
		Double minWidth = ( Double )getAttribute( MIN_WIDTH );
        if( minWidth != null ) {
        	control.setMinWidth( minWidth );
        }
		Double prefHeight = ( Double )getAttribute( PREF_HEIGHT );
        if( prefHeight != null ) {
        	control.setMaxHeight( Region.USE_PREF_SIZE );
        	control.setMinHeight( Region.USE_PREF_SIZE );
        	control.setPrefHeight( prefHeight );
        }
		Double prefWidth = ( Double )getAttribute( PREF_WIDTH );
        if( prefWidth != null ) {
        	control.setMaxWidth( Region.USE_PREF_SIZE );
        	control.setMinWidth( Region.USE_PREF_SIZE );
        	control.setPrefWidth( prefWidth );
        }
		String sTooltip = ( String )getAttribute( TOOLTIP );
        if( sTooltip != null ) {
        	control.setTooltip( getTooltip( sTooltip ) );
        }
        BooleanProperty bpDisable = ( BooleanProperty )getAttribute( DISABLE_PROPERTY );
        if( bpDisable != null ) { 
        	control.disableProperty( ).bind( bpDisable );
        }
        BooleanProperty bpVisible = ( BooleanProperty )getAttribute( VISIBLE_PROPERTY );
        if( bpVisible != null ) { 
        	control.disableProperty( ).bind( bpVisible );
        }
		String sBkgdColor = ( String )getAttribute( BACKGROUD_COLOR );
        if( sBkgdColor != null ) {
        	control.setStyle( sBkgdColor );
        }
		String sBkgdColorOnFocus = ( String )getAttribute( BACKGROUD_COLOR_ON_FOCUS );
        if( sBkgdColorOnFocus != null ) {
        	control.focusedProperty( ).addListener( 
        		( ov, oldV, newV ) -> {
        			if( newV ) {
        				control.setStyle( sBkgdColorOnFocus );
        			} else {
        				control.setStyle( sBkgdColor == null ? DEFAULT_BKGD_COLOR : sBkgdColor );
        			}
        		} 
        	);
        	control.setOnMouseEntered( e -> control.setStyle( sBkgdColorOnFocus ) );
        	control.setOnMouseExited( e -> control.setStyle( sBkgdColor == null ? DEFAULT_BKGD_COLOR : sBkgdColor ) );
        }
	}
	
	/**
	 * Returns tool tip with specified text
	 * 
	 * @param sText the text
	 * 
	 * @return tool tip
	 */
	private Tooltip getTooltip( String sText ) {	
        Tooltip ttip = new Tooltip( );
        ttip.setText( sText );
        return( ttip );
	}
}
