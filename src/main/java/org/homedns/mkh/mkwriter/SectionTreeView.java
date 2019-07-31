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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import org.apache.log4j.Logger;
import org.homedns.mkh.mkwriter.mvp.ButtonBaseConfig;
import org.homedns.mkh.mkwriter.mvp.ControlConfig;
import org.homedns.mkh.mkwriter.mvp.ControlFactory;
import org.homedns.mkh.mkwriter.mvp.Data;
import org.homedns.mkh.mkwriter.mvp.MenuItemConfig;
import org.homedns.mkh.mkwriter.mvp.Presenter;
import org.homedns.mkh.mkwriter.mvp.Util;
import org.homedns.mkh.mkwriter.mvp.View;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

/**
 * Document sections tree view
 *
 */
public class SectionTreeView extends VBox implements View {
	private static final Logger LOG = Logger.getLogger( SectionTreeView.class );
	private static final String[][] FILES_FILTER = new String[][] { 
		{ "All Files", "*.*" }, { "MKW", "*.mkw" }, { "PDF", "*.pdf" }
	};
	private static final String HOME_DIR = System.getProperty( "user.home" );

	private Presenter presenter;
	private TreeView< Section > sectionsTree;
	private EditorView editor;
	private TreeItem< Section > copied = null;
	private TreeItem< Section > selected = null;
	private boolean bCut = false;
	private final FileChooser fileChooser;
	private BooleanProperty bpDisable;
	private BooleanProperty bpNoCopied;
	private BooleanProperty bpChanged;
	private String sFileName;
    private ProgressIndicator progressIndicator;
    private ToolBar sectionTreeTBar;
	
	public SectionTreeView( ) {
		createView( );
		fileChooser = new FileChooser( );
	    Util.configureFileChooser( fileChooser, HOME_DIR, FILES_FILTER );
	}
	
	/**
	 * @see org.homedns.mkh.mkwriter.mvp.View#createView( )
	 */
	@Override
	public void createView( ) {
		bpDisable = new SimpleBooleanProperty( true );
		bpNoCopied = new SimpleBooleanProperty( true );
		bpChanged = new SimpleBooleanProperty( false );

        MenuButton btnSave = getMenuButton( "/fxml/save.png", "Save" );
        btnSave.getItems( ).add( getMenuItem( "Save", this::save, null ) );
        btnSave.getItems( ).add( getMenuItem( "SaveAs", this::saveAs, null ) );

        MenuButton btnTree = getMenuButton( "/fxml/tree.png", "Sections tree" );
        btnTree.getItems( ).add( getMenuItem( "Cut", this::cutSection, bpDisable ) );
        btnTree.getItems( ).add( getMenuItem( "Copy", this::copySection, bpDisable ) );
        btnTree.getItems( ).add( getMenuItem( "Paste", this::pasteSection, bpNoCopied ) );
        btnTree.getItems( ).add( new SeparatorMenuItem( ) );
        btnTree.getItems( ).add( getMenuItem( "Move up", this::moveUpSection, bpDisable ) );
        btnTree.getItems( ).add( getMenuItem( "Move down", this::moveDownSection, bpDisable ) );
        btnTree.getItems( ).add( new SeparatorMenuItem( ) );
        btnTree.getItems( ).add( getMenuItem( "Add section", this::addSection, null ) );
        btnTree.getItems( ).add( getMenuItem( "Delete section", this::deleteSection, bpDisable ) );
        btnTree.getItems( ).add( getMenuItem( "Rename", this::renameSection, null ) );
        
		ContextMenu contextMenu = new ContextMenu( );
		contextMenu.getItems( ).add( getMenuItem( "Cut", this::cutSection, bpDisable ) );
		contextMenu.getItems( ).add( getMenuItem( "Copy", this::copySection, bpDisable ) );
		contextMenu.getItems( ).add( getMenuItem( "Paste", this::pasteSection, bpNoCopied ) );
		contextMenu.getItems( ).add( new SeparatorMenuItem( ) );
		contextMenu.getItems( ).add( getMenuItem( "Move up", this::moveUpSection, bpDisable ) );
		contextMenu.getItems( ).add( getMenuItem( "Move down", this::moveDownSection, bpDisable ) );
		contextMenu.getItems( ).add( new SeparatorMenuItem( ) );
		contextMenu.getItems( ).add( getMenuItem( "Add section", this::addSection, null ) );
		contextMenu.getItems( ).add( getMenuItem( "Delete section", this::deleteSection, bpDisable ) );
		contextMenu.getItems( ).add( getMenuItem( "Rename", this::renameSection, null ) );
		
        TreeItem< Section > rootItem = new TreeItem< >( new Section( "New document" ) );
        sectionsTree = new TreeView< >( rootItem );
        sectionsTree.setId( getId( ) );
        sectionsTree.getSelectionModel( ).selectedItemProperty( ).addListener( this::onSelectTreeItem );
        sectionsTree.setContextMenu( contextMenu );
        sectionsTree.setEditable( true );
        sectionsTree.setCellFactory( TextFieldTreeCell.forTreeView( new StrConverter( ) ) );
        VBox.setVgrow( sectionsTree, Priority.ALWAYS );
        
    	ControlConfig config = new ControlConfig( );
    	config.setAttribute( ControlConfig.BACKGROUD_COLOR, "-fx-background-color:white;" );
    	config.setAttribute( ControlConfig.PREF_HEIGHT, 38.0 );
    	sectionTreeTBar = ControlFactory.create( ToolBar.class, config );
        sectionTreeTBar.getItems( ).add( getButton( "/fxml/open.png", "Open", this::open ) );
        sectionTreeTBar.getItems( ).add( btnSave );
        sectionTreeTBar.getItems( ).add( getButton( "/fxml/add.png", "New", this::createDocument ) );
        sectionTreeTBar.getItems( ).add( btnTree );
        sectionTreeTBar.getItems( ).add( getButton( "/fxml/search.png", "Find", this::find ) );
        sectionTreeTBar.getItems( ).add( getButton( "/fxml/spellcheck.png", "Spellcheck", this::spellcheck ) );
        sectionTreeTBar.getItems( ).add( getButton( "/fxml/print.png", "Print", this::print ) );
        sectionTreeTBar.getItems( ).add( getButton( "/fxml/tune.png", "Settings", this::setSettings ) );
        sectionTreeTBar.getItems( ).add( getButton( "/fxml/help.png", "Help", this::help ) );
        sectionTreeTBar.getItems( ).add( getButton( "/fxml/close.png", "Close", e -> Platform.exit( ) ) );
       
        AnchorPane anchorPane = new AnchorPane( );
        progressIndicator = new ProgressIndicator( );
        progressIndicator.visibleProperty( ).set( false );
        AnchorPane.setBottomAnchor( progressIndicator, 2.0 );
        AnchorPane.setTopAnchor( progressIndicator, 2.0 );
        progressIndicator.setPrefHeight( 19.0 );
        progressIndicator.setPrefWidth( 45.0 );
        anchorPane.getChildren( ).add( progressIndicator );

        setId( getId( ) );
        setPrefHeight( 200.0 );
        setPrefWidth( 100.0 );
        getChildren( ).addAll( sectionTreeTBar, sectionsTree, anchorPane );
	}
			
	/**
     * @see org.homedns.mkh.mkwriter.mvp.View#setPresenter(org.homedns.mkh.mkwriter.mvp.Presenter)
     */
    @Override
	public void setPresenter( Presenter presenter ) {
    	this.presenter = presenter;
        presenter.getExceptionProperty( Parameters.OPEN ).addListener( this::onCommandError );
        presenter.getExceptionProperty( Parameters.SAVE ).addListener( this::onCommandError );
        presenter.getExceptionProperty( Parameters.SAVE_AS ).addListener( this::onCommandError );
        presenter.getValueProperty( Parameters.OPEN ).addListener( this::onOpenSuccess );
        progressIndicator.visibleProperty( ).bind( presenter.servicesRunning( ) );
	}

	/**
	 * Sets editor
	 * 
	 * @param editor the editor to set
	 */
	public void setEditor( EditorView editor ) {
		this.editor = editor;
	}

	/**
     * Opens file
     * 
     * @param actionEvent the action event
	 */
	protected void open( ActionEvent actionEvent ) {
        fileChooser.setTitle( Parameters.OPEN );
    	File file = fileChooser.showOpenDialog( MKWriterApp.getStage( ) );
    	if( file != null ) {
    		sFileName = file.getPath( );
    		Parameters params = new Parameters( );
	    	params.setValue( Data.FILE_EXT_KEY, Util.getFileExt( file ) );
	    	params.setValue( Data.FILE_KEY, file );
	    	presenter.execute( Parameters.OPEN, params );
    	}
    }

    /**
     * Saves content to file which was specified in {@link this#open(ActionEvent)}
     * 
     * @param actionEvent the action event
     */
    protected void save( ActionEvent actionEvent ) {
    	File file = null;
	    if( sFileName == null ) {
	        fileChooser.setTitle( Parameters.SAVE );
	    	file = fileChooser.showSaveDialog( MKWriterApp.getStage( ) );
	    	if( file != null ) {
	    		sFileName = file.getPath( );
	    	}
	    } else {
	    	file = new File( sFileName );
	    }
    	if( file != null ) {
    		if( selected != null ) {
    			editor.moveFromEdit( selected.getValue( ).getId( ) );
    		}
			Parameters params = new Parameters( );
	    	params.setValue( Data.FILE_EXT_KEY, Util.getFileExt( file ) );
	    	params.setValue( Data.FILE_KEY, file );
	    	params.setValue( Data.DATA_KEY, sectionsTree.getRoot( ) );
	    	params.putData2Json( );
	    	presenter.execute( Parameters.SAVE, params );
			bpChanged.set( false );
    	}
    }

    /**
     * Saves content as will be specified by user using save dialog.
     * 
     * @param actionEvent the action event
     */
    protected void saveAs( ActionEvent actionEvent ) { 
        fileChooser.setTitle( Parameters.SAVE_AS );
    	File file = fileChooser.showSaveDialog( MKWriterApp.getStage( ) );
    	if( file != null ) {
    		sFileName = file.getPath( );
    		if( selected != null ) {
    			editor.moveFromEdit( selected.getValue( ).getId( ) );
    		}
			Parameters params = new Parameters( );
			String sExt = Util.getFileExt( file );
	    	params.setValue( Data.FILE_EXT_KEY, sExt );
	    	params.setValue( Data.FILE_KEY, file );
	    	if( sExt.contains( "mkw" ) ) {
		    	params.setValue( Data.DATA_KEY, sectionsTree.getRoot( ) );
		    	params.putData2Json( );	    		
	    	} else {
		    	params.setValue( Data.DATA_KEY, editor.getContent( ) );	    		
	    	}
	    	presenter.execute( Parameters.SAVE_AS, params );
			bpChanged.set( false );
    	}
    }

    /**
     * Creates new document
     * 
     * @param actionEvent the action event
     */
    protected void createDocument( ActionEvent actionEvent ) {
    	Optional< ButtonType > response = onDocumentClose( actionEvent );
    	if( response.isPresent( ) && response.get( ) == ButtonType.CANCEL ) {
    		return;
    	}
		sectionsTree.getRoot( ).getChildren( ).clear( );
		sectionsTree.getRoot( ).setValue( new Section( "New document" ) );
		sectionsTree.getSelectionModel( ).select( sectionsTree.getRoot( ) );
    }

    /**
     * On document close invokes confirmation dialog to save document
     * 
     * @param actionEvent the action event
     * 
     * @return the button type
     */
    public Optional< ButtonType > onDocumentClose( ActionEvent actionEvent ) {
    	Optional< ButtonType > response = Optional.empty( );
    	if( bpChanged.get( ) ) {
	        Alert alert = new Alert( 
	        	Alert.AlertType.CONFIRMATION, 
	        	"Document was changed. Do you want to save it?", 
	        	ButtonType.YES, ButtonType.NO, ButtonType.CANCEL 
	        );
	        response = alert.showAndWait( );
			if( response.isPresent( ) && response.get( ) == ButtonType.YES ) {
				save( actionEvent );
			}
    	}
    	return( response );
    }

    /**
     * Cuts selected section
     * 
     * @param actionEvent the action event
     */
    protected void cutSection( ActionEvent actionEvent ) { 
    	copySection( actionEvent );
    	bCut = true;
    }

    /**
     * Copies selected section
     * 
     * @param actionEvent the action event
     */
    protected void copySection( ActionEvent actionEvent ) {
    	if( selected != null ) {
	    	copied = selected;
	    	bpNoCopied.set( copied == null );
    	}
    }

    /**
     * Pastes copied section
     * 
     * @param actionEvent the action event 
     */
    protected void pasteSection( ActionEvent actionEvent ) { 
    	try {
			selected.getChildren( ).add( copyTreeItem( copied ) );
			if( bCut ) {
				bCut = false;
				copied.getParent( ).getChildren( ).remove( copied );
			}
			bpChanged.set( true );
		} catch( CloneNotSupportedException e ) {
			LOG.error( e.getMessage( ), e );
		}
    }

    /**
     * Moves up selected tree item in same tree level 
     * 
     * @param actionEvent the input action event
     */
    protected void moveUpSection( ActionEvent actionEvent ) { 
        if( selected.getParent( ) != null ) {
        	List< TreeItem< Section > > childs = selected.getParent( ).getChildren( );
        	int iIndex = childs.indexOf( selected );
        	TreeItem< Section > moving = childs.get( iIndex );
        	if( iIndex > 0 ) {
    			bpChanged.set( true );
        		childs.set( iIndex, childs.set( iIndex - 1, moving ) );
        		sectionsTree.getSelectionModel( ).clearSelection( );
        		sectionsTree.getSelectionModel( ).select( moving );
        	}
        }
    }

    /**
     * Moves down selected tree item in same tree level 
     * 
     * @param actionEvent the input action event
     */
    protected void moveDownSection( ActionEvent actionEvent ) { 
        if( selected.getParent( ) != null ) {
        	List< TreeItem< Section > > childs = selected.getParent( ).getChildren( );
        	int iIndex = childs.indexOf( selected );
        	TreeItem< Section > moving = childs.get( iIndex );
        	if( iIndex < childs.size( ) - 1 ) {
    			bpChanged.set( true );
        		childs.set( iIndex, childs.set( iIndex + 1, moving ) );
        		sectionsTree.getSelectionModel( ).clearSelection( );
        		sectionsTree.getSelectionModel( ).select( moving );
        	}
        }    	
    }
    
    /**
     * Adds new section in lower level
     * 
     * @param actionEvent the action event
     */
    protected void addSection( ActionEvent actionEvent ) { 
    	Section s = new Section( );
    	if( selected != null ) {
    		TreeItem< Section > newTreeItem = new TreeItem< >( s );
       		selected.getChildren( ).add( newTreeItem );
			bpChanged.set( true );
       		selected.setExpanded( true );
       		sectionsTree.layout( );
       		sectionsTree.getSelectionModel( ).select( newTreeItem );
       		renameSection( actionEvent );
    	};
    }

    /**
     * Deletes selected section
     * 
     * @param actionEvent the action event
     */
    protected void deleteSection( ActionEvent actionEvent ) { 
         if( selected.getParent( ) != null ) {
        	selected.getParent( ).getChildren( ).remove( selected );
			bpChanged.set( true );
        }
    }

    /**
     * Renames selected section
     * 
     * @param actionEvent the action event
     */
    protected void renameSection( ActionEvent actionEvent ) {
    	if( selected != null ) {
    		sectionsTree.edit( selected );
			bpChanged.set( true );
    	}
    }

    protected void find( ActionEvent actionEvent ) { }

    protected void spellcheck( ActionEvent actionEvent ) { }

    protected void print( ActionEvent actionEvent ) { }
    
    protected void setSettings( ActionEvent actionEvent ) { }

    protected void help( ActionEvent actionEvent ) { }
    
    /**
     * Performs custom actions on select tree item
     * 
     * @param observable the tree view selected item property
     * @param oldTreeItem the previous tree item
     * @param newTreeItem the selected tree item
     */
    private void onSelectTreeItem( 
    	ObservableValue< ? extends TreeItem< Section > > observable, 
    	TreeItem< Section > oldTreeItem, 
    	TreeItem< Section > newTreeItem 
    ) {
    	selected = newTreeItem;
    	bpDisable.set( selected == null || selected.getParent( ) == null );
    	if( oldTreeItem == newTreeItem ) {
    		return;
    	}
    	if( oldTreeItem != null ) {
    		editor.moveFromEdit( oldTreeItem.getValue( ).getId( ) );
    	}
    	if( newTreeItem != null ) {
    		editor.move2Edit( newTreeItem.getValue( ).getId( ) );
    	}
    }
    
    /**
     * Copies specified tree item
     * 
     * @param item the tree item to copy
     * 
     * @return the tree item copy
     * 
     * @throws CloneNotSupportedException
     */
    private TreeItem< Section > copyTreeItem( TreeItem< Section > treeItem ) throws CloneNotSupportedException {
        TreeItem< Section > treeItemCopy = new TreeItem< Section >( treeItem.getValue( ).clone( ) );
        for( TreeItem< Section > child : treeItem.getChildren( ) ) {
        	treeItemCopy.getChildren( ).add( copyTreeItem( child ) );
        }
        return( treeItemCopy );
    }
    
    /**
     * Entirely expands specified tree item 
     * 
     * @param treeItem the tree item to expand
     */
    private void expandAll( TreeItem< Section > treeItem ) {
    	treeItem.setExpanded( true );
        for( TreeItem< Section > child : treeItem.getChildren( ) ) {
        	if( !child.isLeaf( ) ) {
        		expandAll( child );
        	}
        }
    }
    
    /**
     * On command error invokes custom action i.e. shows alert dialog
     * 
     * @param observable the observable value
     * @param oldEx the old value
     * @param newEx the new value
     */
    private void onCommandError( ObservableValue< ? extends Throwable > observable, Throwable oldEx, Throwable newEx ) {
        if( newEx != null ) {
        	String sErr = "";
        	if( newEx.getMessage( ) != null && !newEx.getMessage( ).isEmpty( ) ) {
        		sErr = newEx.getMessage( );
        	} else {
        		try(
        			StringWriter sw = new StringWriter( );
        			PrintWriter pw = new PrintWriter( sw );
        		) {
        			newEx.printStackTrace( pw );
        			sErr = sw.toString( );
        		} 
        		catch( IOException e ) {
					LOG.error( e.getMessage( ), e );
				}
        	}
            Alert alert = new Alert( Alert.AlertType.ERROR, sErr );
            alert.setResizable( true );
            alert.showAndWait( );  
        }
    }
    
    /**
     * On open command success invokes custom action
     * 
     * @param observable the observable value
     * @param oldData the old value
     * @param newData the new value
     */
    private void onOpenSuccess( ObservableValue< ? extends Data > observable, Data oldData, Data newData ) {
    	if( newData != null && newData instanceof Parameters ) {
    		try {
	    		Parameters params = ( Parameters )newData;
	    		params.getDataFromJson( );
	    		TreeItem< Section > root = params.getValue( Data.DATA_KEY );
	    		if( root.getValue( ) != null ) {
		    		sectionsTree.setRoot( root );
		    		expandAll( sectionsTree.getRoot( ) );
		       		sectionsTree.getSelectionModel( ).select( root );
	    		}
    		}
    		catch( Exception e ) {
    			LOG.error( e.getMessage( ), e );
    		}
    	}
    }
       
	/**
	 * Returns button
	 * 
	 * @param sImagePath the image path
	 * @param sTooltip the tool tip text
	 * @param handler the event handler
	 * 
	 * @return the button
	 */
    private Button getButton( String sImagePath, String sTooltip, EventHandler< ActionEvent > handler ) {
    	ButtonBaseConfig config = new ButtonBaseConfig( );
    	config.setAttribute( ButtonBaseConfig.IMAGE_PATH, sImagePath );
    	config.setAttribute( ButtonBaseConfig.TOOLTIP, sTooltip );
    	config.setAttribute( ButtonBaseConfig.BACKGROUD_COLOR, "-fx-background-color:white;" );
    	config.setAttribute( ButtonBaseConfig.BACKGROUD_COLOR_ON_FOCUS, "-fx-background-color:gainsboro;" );
    	config.setAttribute( ButtonBaseConfig.ACTION_HANDLER, handler );
    	Button button = ControlFactory.create( Button.class, config );
    	return( button );
    }
    
	/**
	 * Returns button
	 * 
	 * @param sImagePath the image path
	 * @param sTooltip the tool tip text
	 * 
	 * @return the button
	 */
    private MenuButton getMenuButton( String sImagePath, String sTooltip ) {
    	ButtonBaseConfig config = new ButtonBaseConfig( );
    	config.setAttribute( ButtonBaseConfig.IMAGE_PATH, sImagePath );
    	config.setAttribute( ButtonBaseConfig.TOOLTIP, sTooltip );
    	config.setAttribute( ButtonBaseConfig.BACKGROUD_COLOR, "-fx-background-color:white;" );
    	config.setAttribute( ButtonBaseConfig.BACKGROUD_COLOR_ON_FOCUS, "-fx-background-color:gainsboro;" );
    	MenuButton button = ControlFactory.create( MenuButton.class, config );
    	return( button );
    }

	/**
	 * Returns menu item
	 * 
	 * @param sText the text
	 * @param handler the event handler
	 * @param bpDisable the disable property to bind
	 * 
 	 * @return the menu item
	 */
	private MenuItem getMenuItem( String sText, EventHandler< ActionEvent > handler, BooleanProperty bpDisable ) {
		MenuItemConfig config = new MenuItemConfig( );
    	config.setAttribute( MenuItemConfig.ACTION_HANDLER, handler );
    	config.setAttribute( MenuItemConfig.TEXT, sText );
    	config.setAttribute( MenuItemConfig.DISABLE_PROPERTY, bpDisable );
    	MenuItem item = ControlFactory.create( MenuItem.class, config );
		return( item );
	}

    /**
     * String converter implementation for Section object
     */
    private class StrConverter extends StringConverter< Section > {

		/**
		 * @see javafx.util.StringConverter#toString(java.lang.Object)
		 */
		@Override
		public String toString( Section section ) {
			return( section.getTitle( ) );
		}

		/**
		 * @see javafx.util.StringConverter#fromString(java.lang.String)
		 */
		@Override
		public Section fromString( String sTitle ) {
			return( new Section( sTitle ) );
		}
    }
}
