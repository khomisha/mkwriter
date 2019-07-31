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

/**
 * Model interface
 */
public interface Model {
	
	/**
	 * Executes specified command
	 * 
	 * @param sCommand the command to execute
	 * @param params the parameters required for command execution
	 * 
	 * @return the result
	 * 
	 * @throws Exception
	 */
	public Data execute( String sCommand, Data params ) throws Exception;
}
