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
package org.crs4.entando.innomanager.aps.system.services.layer;

import org.crs4.entando.innomanager.aps.system.services.layer.model.InnoLayer;
import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.internal.HttpFuture;
import com.couchbase.client.protocol.views.ComplexKey;
import com.couchbase.client.protocol.views.OnError;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.SpatialView;
import com.couchbase.client.protocol.views.Stale;
import com.couchbase.client.protocol.views.View;
import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.crs4.entando.innomanager.aps.system.InnoManagerSystemConstants;
import org.crs4.entando.innomanager.aps.system.services.inno.InnoConfigManager;
import org.crs4.entando.innomanager.aps.system.services.layer.helper.InnoHelper;
import org.crs4.entando.innomanager.aps.system.services.layer.model.Infos;
import org.crs4.entando.innomanager.aps.system.services.layer.model.Tile;

/**
 * Gestisce il caricamento dei dati dai nodi couchbase
 * 
 * @author R.Demontis
 */

public class CouchbaseLayerDAO extends AbstractService implements ICouchbaseDAO { 
	
    
    @Override  
    public void init() throws Exception {
        try {
            createCouchbaseClient();
        } catch (Exception e) {
            ApsSystemUtils.getLogger().warn( this.getClass().getName() + ": Error Initializing CouchbaseClient " + e.getMessage() );
        }
        ApsSystemUtils.getLogger().debug(this.getClass().getName() + ": initialized ");
    }

    @Override
    public void destroy() {
           /**
            * Disconnect from Couchbase when the Service shuts down.
            *
            */
         ApsSystemUtils.getLogger().debug(this.getClass().getName() + ": Disconnecting from Couchbase Cluster.");
         if ( _couchbaseClient != null ) 
              _couchbaseClient.shutdown();
         super.destroy();
    }
    
    @Override
    public List<InnoLayer> loadLayers()  {
        return loadLayers(null,null);
    }
    
    @Override
    public List<InnoLayer> loadLayers(Integer page, Integer pagesize)  {
        List<InnoLayer> layers = new ArrayList<InnoLayer>();
        try {
            String document;
            InnoLayer layer = null;
            View view = getClientInstance().getView("innospatial", "byname" );
            
            Query query = new Query();
            if ( pagesize == null ) pagesize = 100; 
            if ( page == null ) page = 0;
                 query.setSkip(page*pagesize);
            query.setIncludeDocs(true).setLimit(pagesize).setOnError(OnError.STOP).setStale(Stale.FALSE);
            ApsSystemUtils.getLogger().info("try query");
            HttpFuture<ViewResponse> future = getClientInstance().asyncQuery(view, query);
            ViewResponse response = future.get(3000, TimeUnit.MILLISECONDS);
            if ( future.isDone() )                     
            for( ViewRow row : response ) {
                 document = (String)row.getDocument();
                 ApsSystemUtils.getLogger().info(document);
                 layer = InnoLayer.createLayer(document, gson);
                 if ( layer != null ) 
                     layers.add(layer);
            }
           
        } catch (Exception e) {
            ApsSystemUtils.getLogger().warn(this.getClass().getName() + ": CouchbaseClient - " + e.getMessage());
        }
        return layers;
    }

    @Override
    public List<InnoLayer> searchCouchbaseLayerByName( String name ){
           List<InnoLayer> layers = new ArrayList<InnoLayer>();
        try {
            String document;
            String startKey;
            InnoLayer layer;
            View view = getClientInstance().getView("innospatial", "byname" );
            /* the view "innospatial"."byname" in the inno bucket (couchbase) is:
               function(doc,meta) {
                    if( doc._innoname_ )
                        emit(doc._innoname_, null);
                }
            */
            Query query = new Query();
            if ( name != null ){
                startKey = name.toLowerCase();
                query.setRangeStart(ComplexKey.of(startKey)).setRangeEnd(ComplexKey.of(startKey + "\uefff"));
            }
            /// To remove
            query = query.setIncludeDocs(true).setLimit(50).setOnError(OnError.STOP).setStale(Stale.FALSE);
            ViewResponse result = getClientInstance().query(view, query);
            for( ViewRow row : result ) {
                 document = (String)row.getDocument();
                 layer = InnoLayer.createLayer(document, gson);
                 if ( layer != null ) 
                     layers.add(layer);
            } 
        } catch (Exception e) {
            ApsSystemUtils.getLogger().warn(this.getClass().getName() + ": CouchbaseClient - " + e.getLocalizedMessage());
        }
        return layers;	
    }
    
    @Override
    public List<InnoLayer> searchCouchbaseLayerByLocation( double latitude, double longitude ){
        List<InnoLayer> layers = new ArrayList<InnoLayer>();
        try {
            String document;
            InnoLayer layer;
            SpatialView view = getClientInstance().getSpatialView("innospatial", "bybbox" );
            /* the spatial view "innospatial"."bybbox" in the inno bucket (couchbase) is:
               function(doc,meta) {
                    if( doc._bbox_ )
                        emit(doc._bbox_, null);
                }
            */
            Query query = new Query();
            query.setBbox(latitude - 0.0005, longitude - 0.0005, latitude + 0.0005 , longitude + 0.0005)
                 .setIncludeDocs(true).setLimit(50).setOnError(OnError.STOP).setStale(Stale.FALSE);
            ViewResponse result = getClientInstance().query(view, query);
            for( ViewRow row : result ) {
                 document = (String)row.getDocument();
                 layer = InnoLayer.createLayer(document, gson);
                 if ( layer != null ) 
                     layers.add(layer);
            } 
        } catch (Exception e) {
            ApsSystemUtils.getLogger().warn(this.getClass().getName() + ": CouchbaseClient - " + e.getLocalizedMessage());
        }
        return layers;	
    }
            
    @Override
    public InnoLayer loadLayer(String layername){
        InnoLayer layer = null;
        try {
            if ( layername != null ) {
                String document = (String) getClientInstance().get(layername); 
                layer = InnoLayer.createLayer(document, gson);
            }        
        } catch (Exception e) {
            ApsSystemUtils.getLogger().warn(this.getClass().getName() + ": CouchbaseClient - " + e.getLocalizedMessage());
        }
        return layer;
    }
    
    @Override
    public List<Tile> getGeomTiles(String layername, int x, int y, int zoom ) throws ApsSystemException{ 
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        String document;
        String tile_id = layername + ":" + x + ":" + y + ":" + zoom;
        document = (String) getClientInstance().get(tile_id);
        int i = 2; 
        while ( document != null )
        { // gestisce la paginazione
             Tile tile = Tile.createTile(document, gson);
             if ( tile != null )
                  tiles.add(tile);
             document = (String) getClientInstance().get(tile_id + ":" + i);
             i++;
        }
        if ( tiles.isEmpty() )
        {
            // If the document isn't present as simple tile. Search it in the index of the macro tile. 
            // Se il documento non è presente effettua una ricerca nell'indice spaziale delle macro-tile
            SpatialView view = getClientInstance().getSpatialView(layername+"views", "bymacrobbox"+zoom);
            /* example of the spatial view for layername "layer" e zoom 10 in the inno bucket (couchbase):
               - name of spatial views "layerviews"."bymacrobbox10"
                function(doc,meta) {
                    if( doc._layermacro10bbox_ )
                        emit(doc._layermacro10bbox_, null);
                }
            */
            Double[] bbox = null;
            try { 
                // It creates the bbox of the tile
                bbox = InnoHelper.getTileBbx(x, y, zoom);
                if ( view != null && bbox != null ){
                    Query query = new Query();
                    query.setBbox(bbox[0], bbox[1], bbox[2], bbox[3])
                                .setIncludeDocs(true).setStale(Stale.OK).setOnError(OnError.STOP);
                    ViewResponse result = getClientInstance().query(view, query);
                    // If the document isn't the false document that manage request with empty result 
                    // return an empty document, otherwise return the correct JSON document
                    // Se il documento non è il documento che gestisce le richieste senza risposta 
                    // ritorna un documento vuoto, altrimenti ritorna il documento JSON trovato
                    for( ViewRow row : result ) {
                       if ( !row.getId().contains(":false") ){
                           document = (String)row.getDocument();
                           if ( document != null ) {
                                Tile tile = Tile.createTile(document, gson);
                                if ( tile != null )
                                   tiles.add(tile);
                           }     
                       }    
                    }
               }
            } catch ( Exception exc ) {
                // manage error!!!
                return tiles;
            }
            
        }    
        return tiles;
    }
    
    @Override
    public Tile getGeomTile(String layername, int x, int y, int zoom, int page ) throws ApsSystemException{
        String document;
        Tile tile;
        
        String tile_id = layername + ":" + x + ":" + y + ":" + zoom;
        if ( page > 0 && page > 1 ) 
             tile_id += ":" + page; 
        document = (String) getClientInstance().get(tile_id);
        if ( document != null )
        { // gestisce la paginazione
             tile = Tile.createTile(document, gson);
             if ( tile != null )
                  return tile;
        }
        else if ( page < 2 ) { 
            // se la ricerca riguarda la prima pagina e non è presente come 
            // indirizzamento diretto allora cerca nella macro-tile
            SpatialView view = getClientInstance().getSpatialView(layername+"views", "bymacrobbox"+zoom);
            /* example of the spatial view for layername "layer" e zoom 10 in the inno bucket (couchbase):
               - name of spatial views "layerviews"."bymacrobbox10"
                function(doc,meta) {
                    if( doc._layermacro10bbox_ )
                        emit(doc._layermacro10bbox_, null);
                }
            */
            Double[] bbox = null;
            try { 
                // It creates the bbox of the tile
                bbox = InnoHelper.getTileBbx(x, y, zoom);
                if ( view != null && bbox != null ){
                    Query query = new Query();
                    query.setBbox(bbox[0], bbox[1], bbox[2], bbox[3])
                                .setIncludeDocs(true).setStale(Stale.OK).setOnError(OnError.STOP);
                    ViewResponse result = getClientInstance().query(view, query);
                    // If the document isn't the false document that manage request with empty result 
                    // return an empty document, otherwise return the correct JSON document
                    // Se il documento non è il documento che gestisce le richieste senza risposta 
                    // ritorna un documento vuoto, altrimenti ritorna il documento JSON trovato
                    for( ViewRow row : result ) {
                       if ( !row.getId().contains(":false") ){
                           document = (String)row.getDocument();
                           if ( document != null ) {
                                tile = Tile.createTile(document, gson);
                                return tile;
                           }     
                       }    
                    }
               }
            } catch ( Exception exc ) {
                // manage error!!!
                return null;
            }
            
        }    
        return null;
    }
    
    @Override
    public List<Tile> getGeomTiles(String layername, double lat, double lon, int zoom ) throws ApsSystemException{
        String[] tile_coord = InnoHelper.getTileId(lat, lon, zoom).split(":");
        int x = Integer.decode(tile_coord[0]);
        int y = Integer.decode(tile_coord[1]);
        return getGeomTiles( layername, x, y, zoom ) ;
    }
    
    @Override
    public Tile getGeomTile(String layername, double lat, double lon, int zoom, int page ) throws ApsSystemException{
        String[] tile_coord = InnoHelper.getTileId(lat, lon, zoom).split(":");
        int x = Integer.decode(tile_coord[0]);
        int y = Integer.decode(tile_coord[1]);
        return getGeomTile( layername, x, y, zoom, page ) ;
    }
    
    @Override
    public List<Infos> getInfoTiles(String layername, int x, int y, int zoom ) throws ApsSystemException{
        ArrayList<Infos> infos = new ArrayList<Infos>();
        String document;
        Infos info;
        Double[] bbox = null;
        try { 
            // It creates the bbox of the tile
            bbox = InnoHelper.getTileBbx(x, y, zoom);
        } catch ( Exception exc ) {
            // manage error!!!
            return infos;
        } 
        // Se il documento non è presente effettua una ricerca nell'indice spaziale delle macro-tile
        SpatialView view = getClientInstance().getSpatialView(layername+"views", "bybbox");
        /* example of the spatial view for layer "layername" in the inno bucket (couchbase):
               - name of spatial views "layernameviews"."bybbox"
                function(doc,meta) {
                    if( doc._layernamebbox_ )
                        emit(doc._layernamebbox_, null);
                }
        */   
        if ( view != null && bbox != null ){
            Query query = new Query();
            query.setBbox(bbox[0], bbox[1], bbox[2], bbox[3])
                        .setIncludeDocs(true).setStale(Stale.OK).setOnError(OnError.STOP);
            ViewResponse result = getClientInstance().query(view, query);
            // If the document isn't the false document that manage request with empty result 
            // return an empty document, otherwise return the correct JSON document
            // Se il documento non è il documento che gestisce le richieste senza risposta 
            // ritorna un documento vuoto, altrimenti ritorna il documento JSON trovato
            for( ViewRow row : result ) {
               if ( !row.getId().contains(":false") ){
                   document = (String)row.getDocument();
                   if ( document != null ) {
                        info = Infos.loadInfos(document, gson);
                        if ( info != null )
                           infos.add(info);
                   }     
               }    
            }
        }    
        return infos;
        
        
    }
    
    
    
    @Override
    public List<Infos> getInfoTiles(String layername, double lat, double lon, int zoom ) throws ApsSystemException{
        String[] tile_coord = InnoHelper.getTileId(lat, lon, zoom).split(":");
        int x = Integer.decode(tile_coord[0]);
        int y = Integer.decode(tile_coord[1]);
        return getInfoTiles( layername, x, y, zoom ) ;
    }
    
    

    @Override
    public boolean updateLayer(InnoLayer layer){
              // disabled
		return false;
    }
    
    @Override
    public boolean deleteLayer(String name){
    	      // disabled
                return false;
    }
    
    @Override
    public String getDocument(String id){
   	   return (String)getClientInstance().get(id);
    }
    
    @Override
    public boolean updateDocument(String id, String json){ 
          // Set (add or override) the document (converted to JSON with GSON)
         try {
             getClientInstance().set(id,json);
             return true;
        } catch (Exception e) {
             return false;
        }
    }
    
    
    @Override
    public Properties getInnoConnectorConfig(){
         return getInnoConfigManager().getInnoConnectorConfig().getConnectorProp();
    
    }
    
    /**
     * Inizializza il client per l'accesso a Couchbase.
     * I dati di connessione son letti dal database 
     */
    private void createCouchbaseClient() {
         
         if ( this._couchbaseClient == null  )
         try {
                ApsSystemUtils.getLogger().info(this.getClass().getName() + ": Connecting to Couchbase Cluster.");
                Properties configuration = getInnoConfigManager().getInnoConnectorConfig().getConnectorProp();
        	String[] nodes_path = configuration.getProperty(InnoManagerSystemConstants.CONFIG_TAG_NODES).split(",");
                ApsSystemUtils.getLogger().info(this.getClass().getName() + ": NODES->" + configuration.getProperty(InnoManagerSystemConstants.CONFIG_TAG_NODES));
                ArrayList<URI> nodes = new ArrayList<URI>();
                for ( int i = 0; i< nodes_path.length; i++ ){
                     ApsSystemUtils.getLogger().info(this.getClass().getName() + ": adding URI node" + nodes_path[i]);
                     nodes.add(URI.create("http://" + nodes_path[i] + ":8091/pools"));
                }
                _couchbaseClient = new CouchbaseClient( nodes, configuration.getProperty(InnoManagerSystemConstants.CONFIG_ATTRIBUTE_NAME), 
                                 configuration.getProperty(InnoManagerSystemConstants.CONFIG_ATTRIBUTE_PASSWORD) );
                
                ApsSystemUtils.getLogger().info(this.getClass().getName() + ": CouchbaseClient initialized.");
         } catch (IOException ex) {
        	ApsSystemUtils.getLogger().warn(this.getClass().getName() + ": CouchbaseClient not initialized.");
    	 }
         
    }
    
    /**
     * Restituisce il client per l'accesso a Couchbase.
     * @return Il client della configurazione per l'accesso a Couchbase.
     */
    public static CouchbaseClient getClientInstance() {
         return _couchbaseClient;
    }
    
    
    
    
    /**
     * Restituisce il manager della configurazione per l'accesso a Couchbase.
     * @return Il manager della configurazione per l'accesso a Couchbase.
     */
    protected InnoConfigManager getInnoConfigManager() {
        return _innoConfigManager;
    }

    /**
     * Imposta il manager della configurazione per l'accesso a Couchbase.
     * @param _innoConfigManager Il manager della configurazione per l'accesso a Couchbase.
     */
    public void setInnoConfigManager(InnoConfigManager configManager) {
        this._innoConfigManager = configManager;
    }
    
    
    
    final Gson gson = new Gson();
    private static CouchbaseClient _couchbaseClient;
    private InnoConfigManager _innoConfigManager;
    
}
