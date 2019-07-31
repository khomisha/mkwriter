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

/**
 * View interface
 */
public interface View {
	
	/**
	 * Creates view, i.e. handles the layout, 
	 * creates view elements and puts them in containers
	 */
	public void createView( ); 
    
	/**
	 * Uses to synchronize the view elements with the presenter data
	 */
	public default void bindPresenter( ) { };
	
	/**
	 * Sets presenter
	 * 
	 * @param presenter the presenter to set
	 */
	public default void setPresenter( Presenter presenter ) { } ;
}
