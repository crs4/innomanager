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
package org.crs4.entando.innomanager.aps.system.services.layer.test;

import com.couchbase.client.CouchbaseClient;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import org.crs4.entando.innomanager.aps.InnoManagerBaseTestCase;
import org.crs4.entando.innomanager.aps.system.InnoManagerSystemConstants;
import org.crs4.entando.innomanager.aps.system.services.inno.InnoConfigManager;
import org.crs4.entando.innomanager.aps.system.services.layer.CouchbaseLayerDAO;

/**
 * @author R.Demontis
 */
public class TestCouchbaseLayerDAO extends InnoManagerBaseTestCase {
	
        @Override
	protected void setUp() throws Exception {
		super.setUp();
		        //LayerManager manager = (LayerManager) this.getApplicationContext().getBean("LayerManager");
                //this._LayerDAO = (CouchbaseLayerDAO)manager.getCouchbaseDAO();
                //this._LayerDAO.setClientInstance(manager.getClientIstance());
	}
	
	/**
	 * Esegue il test sul corretto funzionamento del metodo 'loadLayers'.
	 
	public void testLoadLayers() {
		assertFalse(this._LayerDAO == null );
                assertFalse(this._LayerDAO.getClientInstance() == null );
                assertEquals(1, this._LayerDAO.loadLayers().size() );
	}
	*/
	/**
	 * Esegue il test sul corretto funzionamento del metodo 'searchLayers'.
	 
	public void testSearchLayers() {
		
	}
	*/
	/**
	 * Esegue il test sul corretto funzionamento del metodo 'loadLayer'.
	
	public void testLoadLayer() {
	
	}
	 */
	/**
	 * Esegue il test sul corretto funzionamento dei metodi 'addLayer' e 'deleteLayer' per l'aggiunta e la cancellazione di una scheda.
	 
	public void testAddDeleteLayer() throws Exception {
		
	}
	*/
	/**
	 * Esegue il test sul corretto funzionamento del metodo 'updateLayer'.
	 
	public void testUpdateLayer() {
		
		
	}
	*/
	
	
	private CouchbaseLayerDAO _LayerDAO;
	
}