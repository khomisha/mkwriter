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

import java.io.Serializable;
import java.util.function.Function;
import com.google.gson.annotations.SerializedName;
import javafx.util.Pair;

/**
 * Document section
 *
 */
public class Section implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;

	@SerializedName( "title" ) private String sTitle;
	@SerializedName( "content" ) private String sContent = "";
	@SerializedName( "id" ) private String sId;
	private static Function< String, String > getContentFunc;
	private static Function< Pair< String, String >, Boolean > setContentFunc;
	private static int iCount;

	public Section( ) {
		sId = String.valueOf( iCount++ );
		setTitle( "Section " + sId );
	}

	public Section( String sTitle ) {
		this( );
		setTitle( sTitle );
	}
	
	public Section( String sTitle, String sId, String sContent ) {
		this( sTitle );
		setId( sId );
		setContent( new Pair< String, String >( sId, sContent ) );
	}
	
	/**
	 * Returns section title
	 * 
	 * @return the title
	 */
	public String getTitle( ) {
		return( sTitle );
	}

	/**
	 * Sets section title
	 * 
	 * @param sTitle the title to set
	 */
	public void setTitle( String sTitle ) {
		this.sTitle = sTitle;
	}

	/**
	 * Returns section ID
	 * 
	 * @return the ID
	 */
	public String getId( ) {
		return( sId );
	}

	/**
	 * Sets section ID
	 * 
	 * @param sId the ID to set
	 */
	public void setId( String sId ) {
		this.sId = sId;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString( ) {
		return( sTitle );
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Section clone( ) throws CloneNotSupportedException {
		return( new Section( sTitle, sId, sContent ) );
	}

	/**
	 * Extracts section content from tinymce html page
	 */
	public void getContent( ) {
		if( getContentFunc != null ) {
			sContent = getContentFunc.apply( sId );
		}
	}
	
	/**
	 * Puts content to tinymce html page
	 * 
	 * @param content the content pair id and html content
	 * 
	 * @return true if succeed and false otherwise
	 */
	protected boolean setContent( Pair< String, String > content ) {
		boolean bResult = false;
		if( setContentFunc != null ) {
			bResult = setContentFunc.apply( content );
		}
		return( bResult );
	}
	
	/**
	 * Sets extract content from tinymce html page function
	 * 
	 * @param extractContentFunc the function to set
	 */
	public static void setGetContentFunc( Function< String, String > getContentFunc ) {
		Section.getContentFunc = getContentFunc;
	}

	/**
	 * Sets put content to the tinymce html page function
	 * 
	 * @param putContentFunc the function to set
	 */
	public static void setSetContentFunc( Function< Pair< String, String >, Boolean > setContentFunc ) {
		Section.setContentFunc = setContentFunc;
	}
}
