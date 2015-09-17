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
import static junit.framework.Assert.assertEquals;
import org.crs4.entando.innomanager.aps.InnoManagerBaseTestCase;
import org.crs4.entando.innomanager.aps.system.InnoManagerSystemConstants;
import org.crs4.entando.innomanager.aps.system.services.layer.ILayerManager;

/**
 * Classe di test per il servizio gestore degli strati informativi.
 * @author R.Demontis
 */
public class TestLayerManager extends InnoManagerBaseTestCase {
	
        @Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	/**
	 * Esegue il test sul corretto funzionamento del metodo 'getLayers'.
	 */
	public void testGetLayers() throws Throwable {
		List<WorkLayer> layers = this._layerManager.getWorkLayers();
		assertEquals(4, layers.size());
	}
	
	/**
	 * Esegue il test sul corretto funzionamento del metodo 'searchLayers'.
	 */
	public void testSearchLayers() throws Throwable {
		List<WorkLayer> layers = this._layerManager.searchWorkLayers("comuni",null);
		assertEquals(1, layers.size());
		WorkLayer layer = (WorkLayer) layers.get(0);
		String name = layer.getName();
		assertTrue(name.equals("comuni"));
	}
	
	/**
	 * Esegue il test sul corretto funzionamento del metodo 'getLayer'.
	 */
	public void testGetLayer() throws Throwable {
		WorkLayer layer = this._layerManager.getWorkLayer("comuni");
		assertEquals("comuni", layer.getName());
		layer = this._layerManager.getWorkLayer("strade");
		assertEquals("strade", layer.getName());
		layer = this._layerManager.getWorkLayer("punti");
		assertEquals("punti", layer.getName());
	}
	
	/**
	 * Esegue il test sul corretto funzionamento dei metodi 'addLayer' e 'deleteLayer' per l'aggiunta e la cancellazione di una scheda.
	 */
	public void testAddDeleteLayer() throws Throwable {
		assertNull(this._layerManager.getWorkLayer("world2"));
		try {
			WorkLayer newLayer = this._layerManager.createWorkLayer("world2", "the wole world", "pippo");
		        List<WorkLayer> layers = this._layerManager.searchWorkLayers("world2",null);
			assertEquals(1, layers.size());
			WorkLayer layer = layers.get(0);
			assertEquals("world2", layer.getName());
			assertEquals("the wole world", layer.getDescription());
			layers = this._layerManager.searchWorkLayers(null,"pippo");
			assertEquals(1, layers.size());
			layer = layers.get(0);
			assertEquals("world2", layer.getName());
			assertEquals("the wole world", layer.getDescription());
		} catch (Throwable t) {
			throw t;
		} finally {
			List<WorkLayer> layers = this._layerManager.searchWorkLayers("world2",null);
			assertEquals(1, layers.size());
			WorkLayer layer = layers.get(0);
			this._layerManager.deleteWorkLayer(layer.getName());
			assertNull(this._layerManager.getWorkLayer(layer.getName()));
		}
	}
	
	/**
	 * Esegue il test sul corretto funzionamento del metodo 'updateLayer'.
	 */
	public void testUpdateLayer() throws Throwable {
		WorkLayer layer = this._layerManager.getWorkLayer("comuni");
		String oldDescrizione = layer.getDescription();
		Date oldUpDate = layer.getLastUpdate();
		layer.setName("comuni");
		layer.setDescription("test Main Project Manager");
		layer.setLastUpdate(new Date());
		
		this._layerManager.updateWorkLayer(layer);
		
		WorkLayer updatedLayer = this._layerManager.getWorkLayer("comuni");
		assertEquals("comuni", updatedLayer.getName());
		assertEquals("test Main Project Manager", updatedLayer.getDescription());
		
		layer.setDescription(oldDescrizione);
		layer.setLastUpdate(oldUpDate);
		
		this._layerManager.updateWorkLayer(layer);
		
		updatedLayer = this._layerManager.getWorkLayer("comuni");
		assertEquals("comuni", updatedLayer.getName());
		assertEquals("I comuni", updatedLayer.getDescription());
	}
	
	/**
	 * Esegue le operazioni di inizializzazione della classe di test.
	 */
	private void init() {
		this._layerManager = (ILayerManager) this.getService(InnoManagerSystemConstants.INNO_DATAMANAGER);
	}
	
	private ILayerManager _layerManager;
	
}
