/*
*
* Copyright 2015 CRS4 (http://www.crs4.it) All rights reserved.
*
* This file is part of the Inno Data Management Portal based on 
* Entando Software  (http://www.entando.com).
* The  Inno Data Management Portal is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) 
* as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
*
*/
package org.crs4.entando.innomanager.aps.system.services.inno;


import java.util.Properties;

/**
 * InnoConfig object that manage properties of couchbase connections
 * @author Roberto Demontis (CRS4)
 * @email demontis@crs4.it
 */
public class InnoConfig implements Cloneable   {
		
        public void setConnectorProp(Properties properties){
            try {
                getConnectorProp().clear();
                getConnectorProp().putAll(properties);
            } catch (Exception e) {
            }
        }
        
        /**
         * @return the _attributesProp
         */
        public Properties getConnectorProp() {
            if ( _connectorProp == null )
                _connectorProp = new Properties();
            return _connectorProp;
        }
    
        @Override
        public InnoConfig clone(){
            InnoConfig config = new InnoConfig();
            Properties prop;
            config.setConnectorProp((Properties)getConnectorProp().clone());
            return config;
        }

        private Properties _connectorProp ;
}