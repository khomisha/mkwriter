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

import org.apache.log4j.Logger;
import javafx.concurrent.Task;

/**
 * Generic task for asynchronous commands execution 
 *
 */
public class GenericTask extends Task< Data > {
	private static final Logger LOG = Logger.getLogger( GenericTask.class );

	private final String sCommand;
	private final Model model;
	private final Data params;
	
	/**
	 * @param model the target model
	 * @param sCommand the command to execute
	 * @param params the command parameters
	 */
	public GenericTask( Model model, String sCommand, Data params ) {
		this.model = model;
		this.sCommand = sCommand;
		this.params = params;
	}
	
	/**
	 * Returns executing command
	 * 
	 * @return the executing command
	 */
	public String getCommand( ) {
		return( sCommand );
	}

	/**
	 * Returns model 
	 * 
	 * @return the model
	 */
	public Model getModel( ) {
		return( model );
	}
	
	/**
	 * @see javafx.concurrent.Task#call()
	 */
	@Override
	protected Data call( ) throws Exception {
//		updateProgress( 0.1d, 1.0d );
		return( model.execute( sCommand, params ) );
	}

	/**
	 * @see javafx.concurrent.Task#succeeded()
	 */
	@Override
	protected void succeeded( ) {
		super.succeeded( );
		updateMessage( "Done." );
	}

	/**
	 * @see javafx.concurrent.Task#cancelled()
	 */
	@Override
	protected void cancelled( ) {
		updateMessage( "Cancelled." );
		super.cancelled( );
	}

	/**
	 * @see javafx.concurrent.Task#failed()
	 */
	@Override
	protected void failed( ) {
		super.failed( );
		LOG.error( getException( ).getMessage( ), getException( ) );
	}
}
