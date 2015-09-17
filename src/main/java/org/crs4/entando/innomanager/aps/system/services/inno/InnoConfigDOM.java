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

import java.io.StringReader;
import org.slf4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.crs4.entando.innomanager.aps.system.InnoManagerSystemConstants;
import java.util.Properties;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;


/**
 * This is the representation of the InnoConfigDOM that is the support Class
 * for XML parsing of configuration for couchbase nodes connections.
 * To Do admin user interface to change config
 *
 * This is an xml example of the couchbase connector configuration.
 * <innoConnectorConfig>
 *          <bucket name="inno" password="pippo" >
 *          <nodes>my.server1.it,my.server2.it</nodes> 
 *          </bucket>
 * </innoConnectorConfig>
 * 
 * 
 * @author Roberto Demontis (CRS4)
 * @email demontis@crs4.it
 */
public class InnoConfigDOM {

    /**
     * init 
     * @throws ApsSystemException 
     */
    public InnoConfigDOM() throws ApsSystemException {
        _log = ApsSystemUtils.getLogger();
        
    }
    
    /**
     * init with xml configuration
     * @param xmlText the configuration expressed in xml  
     * @throws ApsSystemException 
     */
    public InnoConfigDOM(String xmlText) throws ApsSystemException {
        _log = ApsSystemUtils.getLogger();
        this.decodeDOM(xmlText);
    }

    /**
     * 
     * @return the connection properties
     */
    public Properties getConnectorProperties() {
        Properties connectorProp = new Properties();
        try  {
            
            Element bucket = this._doc.getRootElement().getChild(InnoManagerSystemConstants.CONFIG_TAG_BUCKET);
            connectorProp.put(InnoManagerSystemConstants.CONFIG_ATTRIBUTE_NAME, 
                              bucket.getAttributeValue(InnoManagerSystemConstants.CONFIG_ATTRIBUTE_NAME));
            connectorProp.put(InnoManagerSystemConstants.CONFIG_ATTRIBUTE_PASSWORD, 
                              bucket.getAttributeValue(InnoManagerSystemConstants.CONFIG_ATTRIBUTE_PASSWORD));
            String[] listnodes = (bucket.getChild(InnoManagerSystemConstants.CONFIG_TAG_NODES).getTextTrim()).split(",");
            String correctnodes = "";
            // verifica indirizzi
            for ( int i=0; i < listnodes.length; i++ ) {
                  if ( listnodes[i].matches("^[a-z0-9\\-\\.]+\\.(it|com|org|net|eu|mobi)$")
                       || listnodes[i].matches("^([0-9]{1,3}\\.){3}[0-9]{1,3}$") )
                        correctnodes = correctnodes + "," + listnodes[i];
            }
            if (correctnodes.length() > 0 )
                correctnodes = correctnodes.substring(1);
            _log.info("COUCHBASE NODES: " + correctnodes); 
            connectorProp.put(InnoManagerSystemConstants.CONFIG_TAG_NODES,correctnodes); 
        } catch (Exception exc) { 
            _log.warn("Errore di configurazione innoconnector: " + exc.getMessage());  
        }
        return connectorProp;
    }
  
    /*
     * initialize the jdom document _doc from xml
     * @param xmlText the configuration expressed in xml 
     * 
     */
    private void decodeDOM(String xmlText) throws ApsSystemException {
        try {
            SAXBuilder builder = new SAXBuilder();
            builder.setValidation(false);
            StringReader reader = new StringReader(xmlText);
            _doc = builder.build(reader);
        } catch (Throwable t) {
            _log.error("Errore nel parsing: "+ t.getMessage());
            throw new ApsSystemException("Errore nell'interpretazione dell'xml", t);
        }
    }
    
    /**
     * 
     * @param config l'oggetto InnoConfig con la configurazione
     * @return l'oggetto jdom con l'elemento radice della configurazione 
     * @throws ApsSystemException 
     */
    private Element createConfigElement ( InnoConfig config ) throws ApsSystemException {
    	try {  	
            Element rootElem = new Element(InnoManagerSystemConstants.INNOCONFIG_TAG_ROOT);
            // To Do: add cbdocloader path to config 
            //Element cbdocloaderElem = new Element(InnoManagerSystemConstants.CONFIG_CBDOCLOADER_PATH); 
            Element bucketElem = new Element(InnoManagerSystemConstants.CONFIG_TAG_BUCKET);
            Element nodesElem = new Element(InnoManagerSystemConstants.CONFIG_TAG_NODES);
            bucketElem.setAttribute(InnoManagerSystemConstants.CONFIG_ATTRIBUTE_NAME, 
                         config.getConnectorProp().getProperty(InnoManagerSystemConstants.CONFIG_ATTRIBUTE_NAME));
            bucketElem.setAttribute(InnoManagerSystemConstants.CONFIG_ATTRIBUTE_PASSWORD, 
                         config.getConnectorProp().getProperty(InnoManagerSystemConstants.CONFIG_ATTRIBUTE_PASSWORD));
            bucketElem.setText(config.getConnectorProp().getProperty(InnoManagerSystemConstants.CONFIG_TAG_NODES));
            bucketElem = bucketElem.addContent(nodesElem); 
            _log.info("innoconnector updated");             
            // return rootElem.addContent(cbdocloaderElem).addContent(bucketElem); 
            return rootElem.addContent(bucketElem);  
    	} catch (Exception e) {
            _log.error("Errore di scritture configurazione innoconnector" + e);   
	}
    	return null;
    }

    
    /**
     * 
     * @param config l'oggetto InnoConfig con la configurazione
     * @return la rappresentazione testuale in xml della configurazione
     * @throws ApsSystemException 
     */
    public String encodeDOM(InnoConfig config) throws ApsSystemException {
    try {
        Element root = this.createConfigElement(config);
        Document doc = new Document(root);
	String xml = new XMLOutputter().outputString(doc);
	return xml;
    } catch (Throwable t) {
    	_log.error("Errore nel parsing: " + t.getMessage()); 
        throw new ApsSystemException("Errore nell'interpretazione dell'xml", t);
    }
    }

    private Document _doc;
    private Logger _log; 
}