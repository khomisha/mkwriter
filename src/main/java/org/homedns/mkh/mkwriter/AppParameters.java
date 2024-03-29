/*
 * Copyright 2014 - 2018 Mikhail Khodonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * $Id$
 */

package org.homedns.mkh.mkwriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * The application configuration parameters object
 *
 */
public class AppParameters {
	private static final Logger LOG = Logger.getLogger( AppParameters.class );

	private Properties parameters = new Properties( );

	/**
	 * @param sFileName
	 *            the configuration file name
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public AppParameters( String sFileName ) throws FileNotFoundException, IOException {
		readConfig( sFileName );
	}

	/**
	 * Returns configuration parameters.
	 * 
	 * @return configuration parameters
	 */
	public Properties getParameters( ) {
		return( parameters );
	}

	/**
	 * Reads configuration parameters file.
	 * 
	 * @param sFileName
	 *            the configuration file name
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void readConfig( String sFileName ) throws FileNotFoundException, IOException {
		try( FileInputStream stream = new FileInputStream( sFileName ) ) {
			parameters.load( stream );
			LOG.debug( "Properties file " + sFileName + " is successfully loaded" );
		}
	}
}
