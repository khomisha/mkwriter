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

import java.time.Duration;
import java.time.LocalDateTime;
import org.apache.log4j.Logger;
import org.homedns.mkh.mkwriter.mvp.View;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Pair;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Editor view
 *
 */
public class EditorView implements View {
	private static final Logger LOG = Logger.getLogger( EditorView.class );

	private WebView webView;
	private WebEngine engine;

	public EditorView( ) {
		createView( );
	}
	
	/**
	 * @see org.homedns.mkh.mkwriter.mvp.View#createView()
	 */
	@Override
	public void createView( ) {
		try {
			webView = new WebView( );
			engine = webView.getEngine( );
//			WebConsoleListener.setDefaultListener(
//				( v, message, lineNumber, sourceId ) -> {
//					LOG.debug( message + "[" + sourceId + ": " + lineNumber + "]" );
//				}
//			);
			webView.setContextMenuEnabled( false );
			webView.addEventHandler( MouseEvent.MOUSE_CLICKED, this::onMouseClicked );
			Section.setGetContentFunc( this::getSectionContent );
			Section.setSetContentFunc( this::setSectionContent );
	        engine.load( getClass( ).getResource( "/config.html" ).toExternalForm( ) );
		}
		catch( Exception e ) {
			LOG.error( e.getMessage( ), e );
		}
	}
	
	/**
	 * Returns web view object
	 * 
	 * @return the web view
	 */
	public WebView getWebView( ) {
		return webView;
	}
	
	/**
	 * Returns editor content
	 * 
	 * @return the content
	 */
	public String getContent( ) {
		String s = ( String )executeScript( "document.getElementById( 'mystore' ).innerHTML;");
		return( s );
	}
	
	/**
	 * Sets editor content
	 * 
	 * @param sContent the content to set
	 */
	public void setContent( String sContent ) {
		executeScript( "document.getElementById( 'mystore' ).innerHTML='" + sContent + "';");
	}

	/**
	 * Moves section from editable textarea to the specified paragraph in hidden buffer.   
	 * 
	 * @param sSectionId the section id
	 */
	public void moveFromEdit( String sSectionId ) {
		add2DOM( sSectionId );
		executeScript( 
			"document.getElementById('" + sSectionId + "').innerHTML=tinymce.activeEditor.getBody().innerHTML;" 
		);
	}
	
	/**
	 * Moves specified section to the editable textarea
	 * 
	 * @param sSectionId the section id
	 */
	public void move2Edit( String sSectionId ) {		
		add2DOM( sSectionId );
		executeScript( 
			"tinymce.activeEditor.getBody().innerHTML=document.getElementById('" + sSectionId + "').innerHTML;" 
		);
	}
	
	/**
	 * If html element doesn't exist for specified section it adds to DOM otherwise do nothing 
	 * 
	 * @param sSectionId the section id
	 */
	protected void add2DOM( String sSectionId ) {
		if( engine.getDocument( ).getElementById( sSectionId ) == null ) {
			executeScript( 
				"tinymce.activeEditor.dom.add(document.getElementById('mystore'), 'div', {id: '" + sSectionId + "', style: 'page-break-after: always'});" 
			);
		}		
	}
		
	/**
	 * Returns specified section content from the tinymce html page
	 * 
	 * @param sSectionId the section id
	 * 
	 * @return the section content
	 */
	private String getSectionContent( String sSectionId ) {
		String sContent = "";
		if( engine.getDocument( ).getElementById( sSectionId ) != null ) {
			sContent = ( String )executeScript( "document.getElementById('" + sSectionId + "').innerHTML;" );
		}
		return( sContent );  
	}
	
	/**
	 * Sets specified section content to the tinymce html page
	 * 
	 * @param content the content to set
	 * 
	 * @return true
	 */
	private boolean setSectionContent( Pair< String, String > content) {
		add2DOM( content.getKey( ) );
		executeScript( 
			"document.getElementById('" + content.getKey( ) + "').innerHTML='" + content.getValue( ) + "';" 
		);
		return( true );
	}
	
	/**
	 * Handles mouse event
	 * 
	 * @param event the event to handle
	 */
	private void onMouseClicked( MouseEvent event ) {
        if( event.getButton( ) == MouseButton.SECONDARY ) {
            JSObject clientRect = ( JSObject )executeScript( 
            	"tinymce.activeEditor.getContentAreaContainer().getBoundingClientRect()" 
            );
            int x = ( Integer )clientRect.getMember( "left" );
            int y = ( Integer )clientRect.getMember( "top" );
            y = ( int )( event.getY( ) - y );
            x = ( int )( event.getX( ) - x );
            if( y >= 0 && x >= 0 ) {
            	executeScript( 
            		"tinymce.activeEditor.fire('contextmenu', { bubbles: true, cancelable: false, view: window, button: 2, buttons: 0, " + 
            		"clientX: " + String.valueOf( x ) + ", clientY: " + String.valueOf( y ) + " }, true);" 
            	);
            }
        }
    }
	
	/**
	 * @see javafx.scene.web.WebEngine#executeScript(String)
	 */
	private Object executeScript( String script ) {
		LocalDateTime dtStart = LocalDateTime.now( );
		Object result = null;
		try {
			result = engine.executeScript( script );
		}
		catch( JSException e ){
			LOG.error( e.getMessage( ), e );
		}
		finally {
			LOG.debug( script + ": " + Duration.between( dtStart, LocalDateTime.now( ) ) );
		}
		return( result );
	}
}
