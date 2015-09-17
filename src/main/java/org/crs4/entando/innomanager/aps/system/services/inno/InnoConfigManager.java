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

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import org.crs4.entando.innomanager.aps.system.InnoManagerSystemConstants;


/**
 * The InnoConfigManager the manager of the InnoConfig object
 * @author Roberto Demontis (CRS4)
 */
public class InnoConfigManager extends AbstractService   {
	
    @Override
    public void init() throws Exception {
        InnoConfigDOM dom;
	try {
            dom = this.loadConfig(InnoManagerSystemConstants.INNOCONFIG_TAG_ROOT);
            InnoConfig config = new InnoConfig();
            config.setConnectorProp(dom.getConnectorProperties());
            setInnoConnectorConfig(config);
            ApsSystemUtils.getLogger().info(this.getClass().getName() + ": initialized ");
	}catch (Throwable t) {
            ApsSystemUtils.getLogger().error("InnoConnectorConfigManager", "init", t);
            throw new ApsSystemException("Errore in fase di aggiornamento caricamento del connettore a Couchbase", t);
	}
    }

    /**
     * 
     * @param item il testo nel database contenente la rappresentazione xml della 
     * configurazione
     * @return l'oggetto InnoConfigDOM per il parsing della configurazione
     * @throws ApsSystemException 
     */
    private InnoConfigDOM loadConfig(String item ) throws ApsSystemException {
    try {
        ConfigInterface configManager = this.getConfigManager();
        String xml = configManager.getConfigItem(item);
        if (xml == null) {
            throw new ApsSystemException("Configuration Item not present: " + InnoManagerSystemConstants.INNOCONFIG_TAG_ROOT );
        }
        ApsSystemUtils.getLogger().info(InnoManagerSystemConstants.INNOCONFIG_TAG_ROOT + ":  " + xml);
        InnoConfigDOM dom = new InnoConfigDOM(xml);
        return dom;
    } catch (Throwable t) {
        ApsSystemUtils.getLogger().error("InnoConnectorConfigManager", "loadConfig", t);
        throw new ApsSystemException("Error on initialization", t);
    }
    }
    
    
    /**
     * restituisce il manager che permetta l'accesso alla configurazione presente  
     * sul database
     * @return il manager che permetta l'accesso alla configurazione presente  
     * sul database
     */ 
    protected ConfigInterface getConfigManager() {
    	return _configManager;
    }
 
    /**
     * setta il manager che permetta l'accesso alla configurazione presente  
     * sul database 
     * @param configManager 
     */
    public void setConfigManager(ConfigInterface configManager) { 
    	this._configManager = configManager; 
    }
    
    
    /*
     * set the Couchbase Connector Configuration 
     * @param config the InnoConfig Object containing the configuration of the Gis Attribute
     */
    public void setInnoConnectorConfig(InnoConfig config)  {
        _innoconfig = config;
    }

    /*
     * get the copy of the Couchbase Connector configuration Object 
     * @return a InnoConfig Object containing the configuration of the Gis Attribute
     */
    public InnoConfig getInnoConnectorConfig()  {
    try {
        return (InnoConfig) this._innoconfig.clone();
    } catch (Throwable t) {
        ApsSystemUtils.logThrowable(t, this, "getInnoConnectorConfig");
        return null;
    }
    }

    /*
     * update the Couchbase Connector configuration Object
     * @param config the InnoConfig Object containing the new configuration for 
     * couchbase connections 
     */
    public void updateInnoConnectorConfig(InnoConfig config) throws ApsSystemException {
    try {
    	String xml = new InnoConfigDOM().encodeDOM(config);
        this.getConfigManager().updateConfigItem(InnoManagerSystemConstants.INNOCONFIG_TAG_ROOT, xml);
        this.setInnoConnectorConfig(config);
    } catch (Throwable t) {
	ApsSystemUtils.logThrowable(t, this, "updateInnoConnectorConfig");
        throw new ApsSystemException("Errore in fase di aggiornamento configurazione InnoConnector", t);
    }
    }

    private ConfigInterface  _configManager;
    private InnoConfig _innoconfig;
    
}
