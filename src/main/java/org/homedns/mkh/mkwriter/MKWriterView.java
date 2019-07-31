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

package org.homedns.mkh.mkwriter;

import org.homedns.mkh.mkwriter.mvp.View;
import javafx.geometry.Pos;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * MKWriter view
 *
 */
public class MKWriterView extends VBox implements View {
    SectionTreeView sectionTree;
    EditorView editor;

	public MKWriterView( ) {
		createView( );
	}
	
	/**
	 * @see org.homedns.mkh.mkwriter.mvp.View#createView()
	 */
	@Override
	public void createView( ) {
        setId( getId( ) );
		setFillWidth( true );
		setAlignment( Pos.CENTER );

		SplitPane splitHPane = new SplitPane( );
        AnchorPane.setBottomAnchor( splitHPane, 0.0 );
        AnchorPane.setLeftAnchor( splitHPane, 0.0 );
        AnchorPane.setRightAnchor( splitHPane, 0.0 );
        AnchorPane.setTopAnchor( splitHPane, 65.0 );
        VBox.setVgrow( splitHPane, javafx.scene.layout.Priority.ALWAYS );
        splitHPane.setDividerPositions( 0.23 );
        splitHPane.setFocusTraversable( true );
        splitHPane.setId( getId( ) );
        
        sectionTree = new SectionTreeView( );
        editor = new EditorView( );
        sectionTree.setEditor( editor );
        
        splitHPane.getItems( ).add( sectionTree );
        splitHPane.getItems( ).add( editor.getWebView( ) );
        getChildren( ).add( splitHPane );
	}

	/**
	 * Returns sections tree view
	 * 
	 * @return the sections tree view
	 */
	public SectionTreeView getSectionTree( ) {
		return( sectionTree );
	}

	/**
	 * Returns editor view
	 * 
	 * @return the editor view
	 */
	public EditorView getEditor( ) {
		return( editor );
	}
}
