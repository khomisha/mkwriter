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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Task executor object
 *
 */
public class Executor {
	public final static Executor INSTANCE = new Executor( );

	private ExecutorService executor;

	private Executor( ) {
		// default executor
		executor = Executors.newCachedThreadPool( );
	}

	/**
	 * Sets the task executor
	 * 
	 * @param executor the executor to set
	 */
	public void setExecutor( ExecutorService executor ) {
		this.executor = executor;
	}
	
	/**
	 * Returns task executor
	 * 
	 * @return the task executor
	 */
	public ExecutorService getExecutor( ) {
		return( executor );
	}
}
