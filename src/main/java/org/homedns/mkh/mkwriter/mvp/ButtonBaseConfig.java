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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Button base configuration object
 *
 */
public class ButtonBaseConfig extends ControlConfig {
	/**
	* Image path, default null
	*/
	public static final Integer IMAGE_PATH = 15;
	/**
	* Image width, default 24.0
	*/
	private static final Integer IMAGE_WIDTH = 16;
	/**
	* Image height, default 24.0
	*/
	private static final Integer IMAGE_HEIGHT = 17;
	
	public ButtonBaseConfig( ) {
		super( );
		setAttribute( TEXT, null );
		setAttribute( IMAGE_PATH, null );
		setAttribute( ACTION_HANDLER, null );
		setAttribute( IMAGE_WIDTH, 24.0 );
		setAttribute( IMAGE_HEIGHT, 24.0 );
		setAttribute( MNEMONIC_PARSING, false );
	}

	/**
	 * @see org.homedns.mkh.mkwriter.mvp.ControlConfig#apply(javafx.scene.control.Control)
	 */
	@SuppressWarnings( "unchecked" )
	@Override
	public void apply( Control control ) {
		super.apply( control );
		ButtonBase bbutton = ( ButtonBase )control;
		String sText = ( String )getAttribute( TEXT );
        if( sText != null ) {
        	bbutton.setText( sText );
        }
		String sImagePath = ( String )getAttribute( IMAGE_PATH );
        if( sImagePath != null ) {
        	bbutton.setGraphic( 
        		getImage( ( Double )getAttribute( IMAGE_HEIGHT ), ( Double )getAttribute( IMAGE_WIDTH ), sImagePath ) 
        	);
        }
	    EventHandler< ActionEvent > handlerAction = ( EventHandler< ActionEvent > )getAttribute( ACTION_HANDLER );
	    if( handlerAction != null ) {
	    	bbutton.setOnAction( handlerAction );
	    }
	    bbutton.setMnemonicParsing( ( Boolean )getAttribute( MNEMONIC_PARSING ) );
	}

	/**
	 * Returns image view with specified height, width and image
	 * 
	 * @param dbHeight the height
	 * @param dbWidth the width
	 * @param sImagePath the path to image
	 * 
	 * @return the image view
	 */
	private ImageView getImage( double dbHeight, double dbWidth, String sImagePath ) {
        ImageView image = new ImageView( );
        image.setFitHeight( dbHeight );
        image.setFitWidth( dbWidth );
        image.setPickOnBounds( true );
        image.setPreserveRatio( true );
        image.setImage( new Image( getClass( ).getResource( sImagePath ).toExternalForm( ) ) );
        return( image );
	}
}
