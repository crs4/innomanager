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

import org.crs4.entando.innomanager.aps.system.services.layer.model.WorkLayer;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;
import static junit.framework.Assert.assertEquals;
import org.crs4.entando.innomanager.aps.InnoManagerBaseTestCase;
import org.crs4.entando.innomanager.aps.system.InnoManagerSystemConstants;
import org.crs4.entando.innomanager.aps.system.services.layer.WorkLayerDAO;
import org.crs4.entando.innomanager.aps.system.services.layer.IWorkLayerDAO;
/**
 * 
 */
public class TestWorkLayerDAO extends InnoManagerBaseTestCase {
	
        @Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	/**
	 * Esegue il test sul corretto funzionamento del metodo 'loadLayers'.
	 */
	public void testLoadLayers() {
		List<WorkLayer> layers = this._LayerDAO.loadLayers();
		assertEquals(4, layers.size());
	}
	
	/**
	 * Esegue il test sul corretto funzionamento del metodo 'searchLayers'.
	 */
	public void testSearchLayers() {
		List<WorkLayer> Layers = this._LayerDAO.searchWorkLayers("omun",null);
		assertEquals(1, Layers.size());
		WorkLayer layer = (WorkLayer) Layers.get(0);
		String name = layer.getName();
		assertTrue(name.equals("comuni"));
	}
	
	/**
	 * Esegue il test sul corretto funzionamento del metodo 'loadLayer'.
	 */
	public void testLoadLayer() {
		WorkLayer layer = this._LayerDAO.loadLayer("comuni");
		assertEquals("comuni",layer.getName());
		layer = this._LayerDAO.loadLayer("strade");
		assertEquals("strade", layer.getName());
		layer = this._LayerDAO.loadLayer("punti");
		assertEquals("punti", layer.getName());
	}
	
	/**
	 * Esegue il test sul corretto funzionamento dei metodi 'addLayer' e 'deleteLayer' per l'aggiunta e la cancellazione di una scheda.
	 */
	public void testAddDeleteLayer() throws Exception {
		assertNull(this._LayerDAO.loadLayer("world2"));
		WorkLayer newLayer = new WorkLayer();
		try {
			newLayer.setName("world2");
                        newLayer.setStatus(InnoManagerSystemConstants.WORK_LAYER_STATUS_CREATE);
                        newLayer.setOperator("innouser");
                        newLayer.setDescription("the wole world");
                        newLayer.setLastUpdate(new Date());
                        this._LayerDAO.addLayer(newLayer);
			WorkLayer layer = this._LayerDAO.loadLayer("world2");
			assertEquals("world2", layer.getName());
			assertEquals("the wole world", layer.getDescription());
		} catch (Exception e) {
			throw e;
		} finally {
			this._LayerDAO.deleteLayer("world2");
		}
		assertNull(this._LayerDAO.loadLayer("world2"));
	}
	
	/**
	 * Esegue il test sul corretto funzionamento del metodo 'updateLayer'.
	 */
	public void testUpdateLayer() {
		WorkLayer layer = this._LayerDAO.loadLayer("comuni");
		String oldDescrizione = layer.getDescription();
		Date oldUpDate = layer.getLastUpdate();
		layer.setName("comuni");
		layer.setDescription("test Main Project Manager");
		layer.setLastUpdate(new Date());
		
		this._LayerDAO.updateLayer(layer);
		
		WorkLayer updatedLayer = this._LayerDAO.loadLayer("comuni");
		assertEquals("comuni", updatedLayer.getName());
		assertEquals("test Main Project Manager", updatedLayer.getDescription());
		
		layer.setDescription(oldDescrizione);
		layer.setLastUpdate(oldUpDate);
		
		this._LayerDAO.updateLayer(layer);
		
		updatedLayer = this._LayerDAO.loadLayer("comuni");
		assertEquals("comuni", updatedLayer.getName());
		assertEquals("I comuni", updatedLayer.getDescription());
		
	}
	
	/**
	 * Esegue le operazioni di inizializzazione della classe di test. Crea il dao da testare impostandone il datasource.
	 */
	private void init() {
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
		WorkLayerDAO LayerDAO = new WorkLayerDAO();
		LayerDAO.setDataSource(dataSource);
		this._LayerDAO = LayerDAO;
	}
	
	private IWorkLayerDAO _LayerDAO;
	
}