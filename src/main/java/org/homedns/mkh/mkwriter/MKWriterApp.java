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

import org.apache.log4j.Logger;
import org.homedns.mkh.mkwriter.mvp.Executor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MKWriterApp extends Application {
	private static final Logger LOG = Logger.getLogger( MKWriterApp.class );

	private static Stage stage;
	private MKWriterView writerView;
	
    /**
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start( Stage stage ) throws Exception {
    	try{
			Runtime.getRuntime( ).addShutdownHook( new ShutdownHook( ) );
			MKWriterApp.stage = stage;
	        stage.setTitle( "MKWriter" );
	        Rectangle2D screenBounds = Screen.getPrimary( ).getBounds( );
	        stage.setX( screenBounds.getMinX( ) );
	        stage.setY( screenBounds.getMinY( ) );
	        stage.setWidth( screenBounds.getWidth( ) );
	        stage.setHeight( screenBounds.getHeight( ) );
	        
	        LOG.trace( "dpi: " + Screen.getPrimary( ).getDpi( ) );
	        LOG.trace( "font pt: " + Font.getDefault( ).getName( ) + " size: " + Font.getDefault( ).getSize( ) );
	        LOG.trace( "font px: " + Math.rint( new Text( "" ).getLayoutBounds( ).getHeight( ) ) );
	        LOG.trace( 
	        	"x: " + stage.getX( ) + " y: " + stage.getY( ) + 
	        	" width: " + stage.getWidth( ) + " height: " + stage.getHeight( ) 
	        );
	        
	        writerView = new MKWriterView( );
	        SectionTreePresenter presenter = new SectionTreePresenter( );
	        presenter.addModel( new SectionTreeModel( ) );
	        writerView.getSectionTree( ).setPresenter( presenter );
	        Scene scene = new Scene( writerView );
	        scene.getStylesheets( ).add( getClass( ).getResource( "/styles/Styles.css" ).toExternalForm( ) );
	        stage.setScene( scene );
	        stage.setOnCloseRequest( e -> Platform.exit( ) );
	        stage.show( );
    	}
    	catch( Exception e ) {
			LOG.error( e.getMessage( ), e );
    	}
    }

	/**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main( String[] args ) {
        launch( args );
    }

	/**
	 * Returns main stage
	 * 
	 * @return the stage
	 */
	public static Stage getStage( ) {
		return( stage );
	}

	/**
	 * @see javafx.application.Application#stop()
	 */
	@Override
	public void stop( ) throws Exception {
		writerView.getSectionTree( ).onDocumentClose( null );
		Executor.INSTANCE.getExecutor( ).shutdownNow( );
		System.exit( 0 );
	}

	private class ShutdownHook extends Thread {
		public void run( ) {
			try {
				// some actions on application close
			}
			catch( Exception e ) {
			}
		}
	}
}
