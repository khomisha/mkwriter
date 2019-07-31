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

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.concurrent.Worker;

/**
 * Presenter interface
 */
public interface Presenter {
	
	/**
	 * Adds model
	 * 
	 * @param model the model to add
	 */
	public void addModel( Model model );
	
	/**
	 * Executes specified command
	 * 
	 * @param sCommand the command to execute
	 * @param params the parameters required for command execution
	 */
	public void execute( String sCommand, Data params );
	
	/**
	 * Cancels specified command
	 * 
	 * @param sCommand the command to cancel
	 */
	public void cancel( String sCommand );
	
	/**
	 * Returns model by specified type
	 * 
	 * @param type the model type
	 * 
	 * @return the model
	 */
	public < T extends Model > T getModel( Class< T > type );
	
	/**
	 * Wrapper of @see javafx.concurrent.Worker<V>#exceptionProperty()
	 */
	public default ReadOnlyObjectProperty< Throwable > getExceptionProperty( String sCommand ) {
		return( null );
	}

	/**
	 * Wrapper of @see javafx.concurrent.Worker<V>#messageProperty()
	 */
	public default ReadOnlyStringProperty getMessageProperty( String sCommand ) {
		return( null );
	}

	/**
	 * Wrapper of @see javafx.concurrent.Worker<V>#progressProperty()
	 */
	public default ReadOnlyDoubleProperty getProgressProperty( String sCommand ) {
		return( null );
	}

	/**
	 * Wrapper of @see javafx.concurrent.Worker<V>#runningProperty()
	 */
	public default ReadOnlyBooleanProperty getRunningProperty( String sCommand ) {
		return( null );
	}

	/**
	 * Wrapper of @see javafx.concurrent.Worker<V>#stateProperty()
	 */
	public default ReadOnlyObjectProperty< Worker.State > getStateProperty( String sCommand ) {
		return( null );
	}

	/**
	 * Wrapper of @see javafx.concurrent.Worker<V>#valueProperty()
	 */
	public default ReadOnlyObjectProperty< Data > getValueProperty( String sCommand ) {
		return( null );
	}

	/**
	 * Wrapper of @see javafx.concurrent.Worker<V>#titleProperty()
	 */
	public default ReadOnlyStringProperty getTitleProperty( String sCommand ) {
		return( null );
	}

	/**
	 * Wrapper of @see javafx.concurrent.Worker<V>#totalWorkProperty()
	 */
	public default ReadOnlyDoubleProperty getTotalWorkProperty( String sCommand ) {
		return( null );
	}

	/**
	 * Wrapper of @see javafx.concurrent.Worker<V>#workDoneProperty()
	 */
	public default ReadOnlyDoubleProperty getWorkDoneProperty( String sCommand ) {
		return( null );
	}
	
	/**
	 * Returns the observable boolean value representing the union of the
	 * services running properties.
	 *
	 * @return the the observable boolean value which is the union of the
	 *         existing services running properties
	 */
	public default BooleanBinding servicesRunning( ) {
		return( null );
	}
}
