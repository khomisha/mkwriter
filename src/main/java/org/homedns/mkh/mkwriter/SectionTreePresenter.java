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

import org.homedns.mkh.mkwriter.mvp.GenericPresenter;
import org.homedns.mkh.mkwriter.mvp.GenericService;
import org.homedns.mkh.mkwriter.mvp.Data;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.concurrent.Worker.State;

/**
 * Section tree presenter
 *
 */
public class SectionTreePresenter extends GenericPresenter {
	
	public SectionTreePresenter( ) {
	}

	/**
	 * @see org.homedns.mkh.mkwriter.mvp.Presenter#execute(java.lang.String, org.homedns.mkh.mkwriter.mvp.Data)
	 */
	@Override
	public void execute( String sCommand, Data params ) {
        GenericService service = getService( sCommand );
		service.setTaskParams( getModel( SectionTreeModel.class ), params );
		service.restart( );
	}
	
	/**
	 * @see org.homedns.mkh.mkwriter.mvp.Presenter#getExceptionProperty(java.lang.String)
	 */
	@Override
	public ReadOnlyObjectProperty< Throwable > getExceptionProperty( String sCommand ) {
		return( getService( sCommand ).exceptionProperty( ) );
	}

	/**
	 * @see org.homedns.mkh.mkwriter.mvp.Presenter#getValueProperty(java.lang.String)
	 */
	@Override
	public ReadOnlyObjectProperty< Data > getValueProperty( String sCommand ) {
		return( getService( sCommand ).valueProperty( ) );
	}

	/**
	 * @see org.homedns.mkh.mkwriter.mvp.Presenter#getProgressProperty(java.lang.String)
	 */
	@Override
	public ReadOnlyDoubleProperty getProgressProperty( String sCommand ) {
		return( getService( sCommand ).progressProperty( ) );
	}
	
	/**
	 * @see org.homedns.mkh.mkwriter.mvp.Presenter#getStateProperty(java.lang.String)
	 */
	@Override
	public ReadOnlyObjectProperty< State > getStateProperty( String sCommand ) {
		return( getService( sCommand ).stateProperty( ) );
	}
}
