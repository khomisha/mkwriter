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

import java.util.HashMap;
import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Abstract presenter
 *
 */
public abstract class GenericPresenter implements Presenter {
	private Map< Class< ? >, Model > models;
	private Map< String, GenericService > services;
	private BooleanBinding servicesRunning;
	private SimpleBooleanProperty dummy;
	
	public GenericPresenter( ) {
		models = new HashMap< >( );
		services = new HashMap< >( );
		dummy = new SimpleBooleanProperty( false );
		servicesRunning = Bindings.or( dummy, dummy );
	}

	/**
	 * @see org.homedns.mkh.mkwriter.mvp.Presenter#addModel(org.homedns.mkh.mkwriter.mvp.Model)
	 */
	@Override
	public void addModel( Model model ) {
		models.put( model.getClass( ), model );
	}

	/**
	 * @see org.homedns.mkh.mkwriter.mvp.Presenter#getModel(java.lang.Class)
	 */
	@SuppressWarnings( "unchecked" )
	@Override
	public < T extends Model > T getModel( Class< T > type ) {
		return ( T )models.get( type );
	}
	
	/**
	 * @see org.homedns.mkh.mkwriter.mvp.Presenter#cancel(java.lang.String)
	 */
	@Override
	public void cancel( String sCommand ) {
		GenericService service = getService( sCommand );
		if( service.isRunning( ) ) {
			service.cancel( );
		}
	}

	/**
	 * @see org.homedns.mkh.mkwriter.mvp.Presenter#servicesRunning()
	 */
	public BooleanBinding servicesRunning( ) {
		return( servicesRunning );
	}
	
	/**
	 * Returns service by specified command
	 * 
	 * @param sCommand the command
	 * 
	 * @return the service 
	 */
	protected GenericService getService( String sCommand ) {
		GenericService service = services.get( sCommand );
		if( service == null ) {
			service = new GenericService( sCommand );
			services.put( sCommand, service );
			servicesRunning = Bindings.or( servicesRunning, service.runningProperty( ) );
		}
		return( service );
	}
}
