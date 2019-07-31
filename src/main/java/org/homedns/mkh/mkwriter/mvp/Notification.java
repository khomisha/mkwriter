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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import java.util.List;

/**
 * Notification object
 *
 */
public class Notification {
    public final static String MODEL_CHANGED = "model_changed";
	public final static Notification INSTANCE = new Notification( );
	
    private final Map< String, Map< Publisher, List< Subscriber > > > subscribers;

    private Notification( ) {
    	subscribers = new HashMap< >( );
	}

    /**
     * Notifies that specified event has happened in specified publisher
     * 
     * @param event the event
     * @param publisher the event source
     */
    public void publish( String event, Publisher publisher ) {
        Platform.runLater( 
        	( ) -> {
                List< Subscriber > list = subscribers.get( event ).get( publisher );
        		if( list != null ) {
        			for( Subscriber s : list ) {
        				s.update( event, publisher );
        			}
         		}
        	} 
        );
    }

    /**
     * Subscribes specified subscriber to the specified event and event source
     * 
     * @param event the event
     * @param publisher the event source
     * @param subscriber the subscriber to subscribe
     */
    public void subscribe( String event, Publisher publisher, Subscriber subscriber ) {
        if( !subscribers.containsKey( event ) ) {
            subscribers.put( event, new HashMap< >( ) );
        }
        Map< Publisher, List< Subscriber > > subscribers4Publisher = subscribers.get( event );
        if( !subscribers4Publisher.containsKey( publisher ) ) {
        	subscribers4Publisher.put( publisher, new ArrayList< >( ) );
        }
        subscribers4Publisher.get( publisher ).add( subscriber );
    }

    /**
     * Unsubscribes specified subscriber from specified event and event source
     * 
     * @param event the event
     * @param publisher the event source
     * @param subscriber the subscriber to unsubscribe
     */
    public void unsubscribe( String event, Publisher publisher, Subscriber subscriber ) {
        List< Subscriber > list = subscribers.get( event ).get( publisher );
        if( list != null ) {
            list.remove( subscriber );
        }
    }
    
    /**
     * Clears all subscribers
     */
    public void clear( ) {
    	subscribers.clear( );
    }
}
