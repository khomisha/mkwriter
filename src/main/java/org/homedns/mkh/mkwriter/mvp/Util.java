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

import java.io.File;
import javafx.stage.FileChooser;

/**
 * Utility object
 *
 */
public class Util {

    /**
     * Configures file chooser dialog
     * 
     * @param fileChooser the file chooser dialog
     * @param sTitle the dialog title
     * @param sInitialDir the initial directory
     * @param asExts the file extensions array
     */
    public static void configureFileChooser( 
    	FileChooser fileChooser, 
    	String sInitialDir, 
    	String[][] asExts 
    ) {      
        fileChooser.setInitialDirectory( new File( sInitialDir ) );
        if( fileChooser.getExtensionFilters( ).size( ) > 0 ) {
        	return;
        }
        for( String[] asExt : asExts ) {
        	fileChooser.getExtensionFilters( ).add( new FileChooser.ExtensionFilter( asExt[ 0 ], asExt[ 1 ] ) );
        }
    }
    
    /**
     * Returns specified file extension
     * 
     * @param file the target file
     * 
     * @return the file extension
     */
    public static String getFileExt( File file ) {
    	String[] as = file.getName( ).split( "\\." );
    	return( as[ as.length - 1 ] );
    }
}
