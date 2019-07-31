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

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Generic service
 *
 */
public class GenericService extends Service< Data > {
	private String sCommand;
	private Model model;
	private Data params;
	private GenericTask task;
	
	public GenericService( String sCommand ) {
		setExecutor( Executor.INSTANCE.getExecutor( ) );
		this.sCommand = sCommand;
	}

	/**
	 * @see javafx.concurrent.Service#createTask()
	 */
	@Override
	protected Task< Data > createTask( ) {
		task = new GenericTask( model, sCommand, params );
		return( task );
	}

	/**
	 * Sets generic task which will be executed
	 * 
	 * @param task the generic task to set
	 */
	public void setTaskParams( Model model, Data params ) {
		this.model = model;
		this.params = params;
	}
}
