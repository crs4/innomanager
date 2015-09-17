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
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.List;
import java.util.Properties;
import org.crs4.entando.innomanager.aps.system.services.layer.model.Infos;
import org.crs4.entando.innomanager.aps.system.services.layer.model.Tile;

/**
 * Interfaccia base per il Data Access Object degli oggetti InnoLayer 
 * presenti su couchbase
 * TO DO: operazioni CRUD su documenti Tile, Info e oggetti InnoLayer
 * @author R.Demontis
 */
public interface ICouchbaseDAO {
    
        /**
         * Carica la List dei layer nel cluster di Couchbase.
         * @param page: la pagina richiesta.
         * @param pagesize: la dimensione della pagina.
         * @return Una List di oggetti InnoLayer.
         */
        public List<InnoLayer> loadLayers(Integer page, Integer pagesize);

        /**
         * Carica la lista dei layer nel cluster di Couchbase.
         * @return Una List di di oggetti InnoLayer.
         */
        public List<InnoLayer> loadLayers();


        /**
         * Restituisce gli oggetti InnoLayer filtrati in base ad un nome.
         * @param name: L'identificativo dello strato informativo, puo' essere null .
         * @return Una List di oggetti InnoLayer.
         */
        public List<InnoLayer> searchCouchbaseLayerByName(String name);

        /**
         * Restituisce gli oggetti InnoLayer filtrati in base ad ua posizione.
         * @param latitude: latitudine wgs84 in radianti.
         * @param longitude: longitude wgs84 in radianti.
         * @return Una List di oggetti InnoLayer.
          */
        public List<InnoLayer> searchCouchbaseLayerByLocation(double latitudine , double longitudine);

        /**
         * Carica l'oggetto InnoLayer con il nome  dato.
         * @param name: L'identificativo del documento su couchbase.
         * @return L'oggetto InnoLayer layer richiesto.
         */
        public InnoLayer loadLayer(String name);

        /**
         * Carica le pagine con le geometrie di un tassello di un layer con 
         * coordinate x e y riferite al livello di zoom  secondo le specifiche 
         * Slippy map tilenames (http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames)
         * 
         * @param name: L'identificativo del layer su couchbase.
         * @param x: la coordinata x del tassello
         * @param y: la coordinata y del tassello
         * @param zoom: Il livello di Zoom
         * @return la lista degli oggetti Tile con le pagine tassello indicato 
         * @throws ApsSystemException
         */
        public List<Tile> getGeomTiles(String name, int x, int y, int zoom) throws ApsSystemException;

        /**
         * Carica le pagine con i dati alfanumerici di un tassello con coordinate x e y 
         * riferite al livello di zoom indicato secondo le specifiche Slippy map 
         * tilenames (http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames)
         * 
         * @param name: L'identificativo del layer.
         * @param x: la coordinata x del tassello
         * @param y: la coordinata y del tassello
         * @param zoom: Il livello di Zoom
         * @return la lista degli oggetti Infos uno per pagina del il tassello indicato 
         * @throws ApsSystemException
         */
        public List<Infos> getInfoTiles(String name, int x, int y, int zoom) throws ApsSystemException;

        /**
         * Legge un pagina di dati con le geometrie per un tassello di un layer 
         * con coordinate x e y riferite al livello di zoom indicato secondo le specifiche Slippy map 
         * tilenames (http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames)
         * 
         * @param name: L'identificativo del documento su couchbase.
         * @param x: la coordinata x del tassello
         * @param y: la coordinata y del tassello
         * @param zoom: Il livello di Zoom
         * @param page: La pagina del tassello
         * @return l'oggetto Tile con i dati del tassello delle geometrie per la pagina indicata
         * @throws ApsSystemException
         */
        public Tile getGeomTile(String name, int x, int y, int zoom, int page) throws ApsSystemException;



        /**
         * Restituisce le pagine di dati con le geometrie per un tassello 
         * di un layer per un tassello ad un livello di zoom data una posizione 
         * in WGS84
         *  
         * @param name: L'identificativo del layer.
         * @param lat: la latitudine
         * @param lon: la longitudine
         * @param zoom: Il livello di Zoom
         * @return la lista degli oggetti Tile nel tassello indicato 
         * @throws ApsSystemException
         */
        public List<Tile> getGeomTiles(String name, double lat, double lon, int zoom) throws ApsSystemException;

        /**
         * Legge un pagina di un tassello ad un livello di zoom di un layer  
         * data una posizione in WGS84
         *  
         * @param name: L'identificativo del layer.
         * @param lat: la latitudine wgs84
         * @param lon: la longitudine wgs84
         * @param zoom: Il livello di Zoom
         * @param page: il numero di pagina 
         * @return l'oggetto Tile con le geometrie del tassello delle geometrie 
         * per la pagina indicata
         * @throws ApsSystemException
         */
        public Tile getGeomTile(String name, double lat, double lon, int zoom, int page ) throws ApsSystemException;

        /**
         * Carica le pagine con i dati alfanumerici di un tassello ad un livello di zoom 
         * data una posizione in  WGS84
         *  
         * @param name: L'identificativo del layer.
         * @param lat: la latitudine
         * @param lon: la longitudine
         * @param zoom: Il livello di Zoom
         * @return la lista degli oggetti Infos nel tassello indicato 
         */
        public List<Infos> getInfoTiles(String name, double lat, double lon, int zoom) throws ApsSystemException;

        /**
         * Aggiorna le informazioni di un layer in Couchbase.
         * @param layer: l'oggetto InnoLayer del layer su couchbase da aggiornare.
         * @return  true se il layer è stato aggiornato, false altrimenti
         */
        public boolean updateLayer(InnoLayer layer);

        /**
         * Restituisce il documento json data una chiave su Coushbase 
         * @param id l'identificativo del documento su Couchbase.
         * @return il JSON presente su Couchbase
         */
        public String getDocument(String id);

        /**
         * Aggiorna  o inserisce un documento su Couchbase.
         * @param id l'identificativo del documento su Couchbase
         * @param json il documento in formato json
         * @return  true se il layer è stato aggiornato, false altrimenti
         */
        public boolean updateDocument(String id, String json);

        /**
         * Inizializza il thread di cancellazione di un layer su Couchbase 
         * @param name: l'identificativo del layer su couchbase da cancellare.
         * @return  true se il thread di cancellazione è partito, false altrimenti
         */
        public boolean deleteLayer(String name);


        /**
         * Ritorna le prorpietà di connessione al database su couchbase
         * @return un oggetto Properties con le prorpietà di connessione a couchbase
         */
        public Properties getInnoConnectorConfig();
       
}
