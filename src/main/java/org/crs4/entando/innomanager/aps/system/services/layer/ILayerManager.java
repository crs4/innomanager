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
import org.crs4.entando.innomanager.aps.system.services.layer.model.WorkLayer;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.crs4.entando.innomanager.aps.system.services.layer.model.Infos;
import org.crs4.entando.innomanager.aps.system.services.layer.model.Tile;
import org.entando.entando.aps.system.services.api.model.ApiException;


/**
 * Interfaccia base per i servizi gestori dei layer
 * @author R.Demontis
 */
public interface ILayerManager {
       
    /** 
     *  INIZIO METODI PER LA GESTIONE DEGLI STRATI INFORMATIVI DI LAVORO 
     */
    
    /**
     * Restituisce il layer di lavoro.
     * @param name il nome del layer.
     * @return il layer con il nome indicato o null  .
     */
    public WorkLayer getWorkLayer(String name) throws ApsSystemException;
    
    /**
     * Restituisce la List dei layer di lavoro.
     * @return Una lista di oggetti WorkLayer.
     */
    public List<WorkLayer> getWorkLayers() throws ApsSystemException;

    
    /**
     * Restituisce gli oggetti WorkLayer filtrati in base ai valori degli attributi.
     * @param name L'identificativo del WorkLayer .
     * @param operator L'identificativo dell'operatore che gestisce il WorkLayer.
     * @return Una lista di oggetti WorkLayer.
     * @throws ApsSystemException
     */
    public List<WorkLayer> searchWorkLayers( String name, String operator )  throws ApsSystemException;

    /**
     * cerca di recuperare uno stato consistente del work layer in caso di errori
     * @param layer il WorkLayer da verificare.
     * @return lo stesso WorkLayer in uno stato consistente
     * @throws ApsSystemException
     */
    public WorkLayer resetWorkLayerStatus(WorkLayer layer) throws ApsSystemException;
        
    /**
     * Retituisce gli attributi del layer sulla base della tabella ordinata degli
     * elementi dello shapefile importato
     * @param name L'identificativo del WorkLayer da verificare.
     * @return il nome degli attributi e il loro tipo
     * @throws ApsSystemException
     */
    public HashMap<String,String> getWorkLayerAttributes(String name) throws ApsSystemException;
    
    /**
     * Crea ed aggiunge un WorkLayer.
     * @param name il nome del layer da aggiungere.
     * @param descrizione la descrizione del layer da aggiungere.
     * @param operator il proprietario del  layer da aggiungere.
     * @return il nuovo oggetto WorkLayer
     * @throws ApsSystemException
     */
    public WorkLayer createWorkLayer(String name, String description, String operator) throws ApsSystemException ;    
    
    /**
     * Cancella un WorkLayer.
     * @param name il nome del layer da cancellare.
     * @throws ApsSystemException
     */
    public void deleteWorkLayer(String name) throws ApsSystemException;
    
    /**
     * Aggiorna i metadati di un WorkLayer.
     * @param layer l'oggetto WorkLayer da aggiornare.
     * @throws ApsSystemException
     */
    public void updateWorkLayer(WorkLayer layer) throws ApsSystemException;

    /** 
      *  FINE  METODI PER LA GESTIONE DEGLI STRATI INFORMATIVI DI LAVORO 
      */
    
    /** 
     *  METODI PER LA GESTIONE DELLE ELABORAZIONI SUGLI STRATI INFORMATIVI DI LAVORO 
     */
    
    /**
     * Cancella un WorkLayer e le risorse di sistema allocate 
     * cioe' le tabelle di lavoro ed i shapefiles.
     * @param th il thread WorkLayerThread che cancella i dati.
     * @throws ApsSystemException
     */
    public void deleteWorkLayer(WorkLayerActionThread th) throws ApsSystemException;
   
    /**
     * Importa i dati dei shapefiles nel database di lavoro 
     * @param th il thread WorkLayerThread che carica i dati.
     * @throws ApsSystemException
     */
    public void importWorkLayer(WorkLayerActionThread th) throws ApsSystemException;

     
    /**
     * Elaborazione dei dati per il tassellamento di un layer 
     * @param th il thread WorkLayerThread che estare le info.
     * @throws ApsSystemException
     */
    public void elaborateWorkLayer(WorkLayerActionThread th) throws ApsSystemException;
    /**
     * Scrive i documenti json nel bucket di couchbase 
     * @param th il thread WorkLayerThread che carica i dati.
     * @throws ApsSystemException
     */
    public void exportWorkLayer(WorkLayerActionThread th) throws ApsSystemException;
    
    
    /**
     * Elaborazione dei dati alfanumerici di un worklayer 
     * @param name il nome del  WorkLayer.
     * @throws ApsSystemException
     */
    public void generateInfo(String name) throws ApsSystemException;
    
    /**
     * Elaborazione dei dati geometrici di un worklayer per la generazione dei tasselli
     * @param name il nome del  WorkLayer.
     * @param zoom il livello di zoom .
     * @throws ApsSystemException
     */
    public void generateTiles(String name, int zoom) throws ApsSystemException;
            
    /**
     * Elaborazione dei dati di un worklayer per aggiungere la priorità di visualizzazione 
     * @param name il nome del  WorkLayer.
     * @throws ApsSystemException
     */
    public void addPriority(String name) throws ApsSystemException;
    
    /**
     * Scrittura su disco dei documenti JSON  
     * @param th il thread WorkLayerThread che scrive i documenti 
     * @throws ApsSystemException
     */
    public boolean  writeToDisk (String name) throws ApsSystemException;
    
    /**
     * verifica le tabelle con i dati nel database di lavoro per il layer indicato 
     * @param layername il nome del  WorkLayer.
     * @return le informazioni sulle tabelle di lavoro del layer (name,size)
     */
    public Map<String,String> getTablesReport(String layername) throws ApsSystemException;
    
    /** 
      *  FINE METODI PER LA GESTIONE DELLE ELABORAZIONI SUGLI STRATI INFORMATIVI DI LAVORO 
      */
     
    /**
     * API SUPPORT PER LA GESTIONE LAYER DI LAVORO
     * Not yet implemented
     */
            
    /**
     * restituisce una lista di WorkLayer di un utente che hanno nome simile a 
     * quello indicato
     * @param name chiave di ricerca testuale 
     * @param operator il nome di uno user, può essere null 
     * @return  una lista di WorkLayer
     * @throws Throwable 
     */
    public List<WorkLayer> getWorkLayersForApi(  String name, String operator ) throws Throwable;
     
     /**
      * Aggiunge un nuovo oggetto WorkLayer .
      * @param layer: l'oggetto WorkLayer da aggiungere
      * @throws  ApiException, ApsSystemException
     */
     public void addWorkLayerForApi(String name, String description, String operator) throws ApiException, ApsSystemException;
     
     /**
      * Modifica un oggetto WorkLayer .
      * @param layer: l'oggetto WorkLayer da modificare
      * @throws  ApiException, ApsSystemException
     */
     public void updateWorkLayerForApi(WorkLayer layer) throws ApiException, ApsSystemException ;
     
     /**
      * Cancella un oggetto WorkLayer .
      * @param name: il nome dell'oggetto WorkLayer da rimuovere
      * @throws Throwable 
     */
     public void deleteWorkLayerForApi(String name) throws Throwable;
    
     /**
     * FINE API SUPPORT PER LA GESTIONE LAYER DI LAVORO
     * Not yet implemented
     */
     
     /** 
      *  INIZIO  METODI PER LA GESTIONE DEI SHAPEFILES 
      */
     
     /**
      * verifica se è presente il file dei dati nelle risorse  
      *    
      * @param layer il WorkLayer da verificare
      * @throws ApsSystemException
      */
      public void verifyFile ( WorkLayer layer ) throws ApsSystemException;
      
     /**
      * esegue l'unzip di uno shapefile e ne verifica il contenuto
      *    
      * @param layername il nome dello strato informativo di lavoro
      * @param zipFile lo shapefile zippato
      * @param zipFileName il nome dello shapefile zippato
      * @return File l'oggetto file riferito al .shp
      * @throws ApsSystemException
      */
      public File unzipShapeFile ( String layername, File zipFile, String zipFileName );
    
     /**
      * restituisce il nome e le dimensioni dei file ".shp" nell'area di lavoro per
      * lo strato informativo.   
      * @param layername il nome dello strato informativo di lavoro
      * @return una lista con le informazioni dei shapefile presenti nell'area di 
      *         lavoro nel formato "nomefile (length)" per lo strato indicato
      */
      public List<String> getShapeFileInfo ( String layername );
     
      /**
       * restituisce il nome e il tipo degli attributi dei shapefile di uno
       * strato informativo.   
       * @param layername il nome dello strato informativo di lavoro
       * @return una lista con le il nome e il tipo degli attributi dei shapefile 
       * di uno strato informativo: "nome:tipo".   
       */
      public List<String> getShapeFilesAttributes ( String layername );

      /**
       * restituisce il nome e il tipo degli attributi dei shapefile di uno
       * strato informativo.   
       * @param dbfFile  l'oggetto File con formato dbf contenente gli attributi
       * @return una lista con le il nome e il tipo degli attributi dei shapefile 
       * di uno strato informativo : "nome:tipo".  
       */
      public List<String> getShapeFileAttributes ( File dbfFile );
      
      /**
       * eseguela rimozione dei file .shp, .dbf, .shx riferiti allo shapefile   
       * @param layername il nome dello strato informativo di lavoro
       * @param shapeFileName il base name dei files da rimuovere 
       * @return true se lo shapefile e' stata cancellato altrimenti false   
       */
      public boolean removeShapeFile ( String layername, String shapeFileName );
    
      /** 
       *  FINE  METODI PER LA GESTIONE DEI SHAPEFILES   
       */
    
      /** 
       *  INIZIO  METODI PER L'ACCESSO AI DATI SU COUCHBASE 
       */
    
       /**
        * 
        * Legge le informazioni su di un layer presente in couchbae 
        * @param name il nome del layer
        * @return l'oggetto InnoLayer richiesto.
        * @throws ApsSystemException
        */
       public InnoLayer getCouchbaseLayer(String name) throws ApsSystemException;

       /**
        * Legge i layers caricati su Couchbase.
        * @return una lista di oggetti InnoLayer.
        * @throws ApsSystemException
        */
       public List<InnoLayer> getCouchbaseLayers() throws ApsSystemException;

       /**
        *  
        * Legge i layers caricati su Couchbase.
        * @param key la chiave di ricerca (viene usato il LIKE)
        * @return una lista di oggetti InnoLayer ricavati in base alla chiave data.
        * @throws ApsSystemException
        */
       public List<InnoLayer> searchCouchbaseLayers(String key) throws ApsSystemException;

       /**
        * fa partire il thread di cancellazione dei documenti relativi ad un layer 
        * presente su Couchbase. (disabled)
        * @param name L'identificativo del layer in Couchbase da  cancellare.
        * @return true se la cancellazione è stata eseguita, false altrimenti.
        * @throws ApsSystemException
        */
       public boolean deleteCouchbaseLayer(String name) throws ApsSystemException;

       /**
        * Scrive o sovrascrive le informazioni su di un layer in couchbae 
        * @param layer L'oggetto InnoLayer che si vuole aggiornare su Couchbase .
        * @return true se l'update è stato eseguito, false altrimenti.
        * @throws ApsSystemException
        */
       public boolean updateCouchbaseLayer(InnoLayer layer) throws ApsSystemException;

       /**
        * Legge le informazioni su di una feature di un layer  su couchbae 
        * @param layer l'identificativo del layer in Couchbase
        * @param feature l'identificativo dell'elemento.
        * @return l'oggetto Infos ricavato dal JSON in couchbase.
        * @throws ApsSystemException
        */
       public Infos getInfoFromCouchbase(String layer, String feature) throws ApsSystemException;

       /**
        * Carica le pagine con le geometrie di un tassello di  tassello di un layer con coordinate x e y 
        * riferite al livello di zoom  secondo le specifiche Slippy map 
        * tilenames (http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames)
        * 
        * @param name: L'identificativo del layer su couchbase.
        * @param x: la coordinata x del tassello
        * @param y: la coordinata y del tassello
        * @param zoom: Il livello di Zoom
        * @return la lista degli oggetti Tile con le pagine tassello indicato 
        * @throws ApsSystemException
        */
       public List<Tile> getGeomTilesFromCouchbase(String name, int x, int y, int zoom ) throws ApsSystemException;

       /**
         * Carica un pagina di dati con le geometrie per un tassello di un layer 
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
        public Tile getGeomTileFromCouchbase(String name, int x, int y, int zoom, int page ) throws ApsSystemException;

       /**
        * Restituisce le pagine con le geometrie di un tassello di un layer per 
        * un livello di zoom data una posizione in WGS84
        *  
        * @param name: L'identificativo del layer.
        * @param lat: la latitudine
        * @param lon: la longitudine
        * @param zoom: Il livello di Zoom
        * @return la lista degli oggetti Tile nel tassello indicato 
        */
       public List<Tile> getGeomTilesFromCouchbase(String name, double lat, double lon, int zoom ) throws ApsSystemException;

       /**
         * Legge un pagina con le geometrie di un tassello ad un livello di zoom  
         * di un layer data una posizione in WGS84
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
        public Tile getGeomTileFromCouchbase(String layer, double lat, double lon, int zoom, int page ) throws ApsSystemException;

       /**
        * Carica le pagine con i dati alfanumerici di un tassello ad un livello di zoom
        * riferite al livello di zoom secondo le specifiche Slippy map 
        * tilenames (http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames)
        * 
        * @param name: L'identificativo del layer.
        * @param x: la coordinata x del tassello
        * @param y: la coordinata y del tassello
        * @param zoom: Il livello di Zoom
        * @return la lista degli oggetti Info per il tassello indicato 
        * @throws ApsSystemException
        */
        public List<Infos> getInfoTilesFromCouchbase(String name, int x, int y, int zoom ) throws ApsSystemException;

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
        public List<Infos> getInfoTilesFromCouchbase(String name, double lat, double lon, int zoom ) throws ApsSystemException;

       /** 
        *  FINE METODI PER L'ACCESSO AI DATI SU COUCHBASE 
        */
       
       /** 
        * API SUPPORT PER L'ACCESSO AI STRATI INFORMATIVI NEL BUCKET COUCHBASE 
        */
       
       /**
        * Legge su couchbae il documento con le proprietà degli elementi di un layer 
        * in un specifico tassello  
        * @param layer: il nome dello strato informativo su couchbase
        * @param x:  coordinata x della tassello
        * @param y:  coordinata y della tassello
        * @param zoom: il livello di zoom del tassello
        * @param page: il numero di pagina 
        * @return  il documento in JSON richiesto o null se non esiste.
        * @throws ApsSystemException
        */
       public String getInfoFromCouchbaseForAPI(String layer, int x, int y, int zoom, int page) throws ApsSystemException;

       /**
        * API SUPPORT PER LA GESTIONE STRATI INFORMATIVI SUL BUCKET COUCHBASE 
        * Legge su couchbae il documento con legeometrie degli elementi di un layer 
        * in un specifico tassello  
        * @param layer: il nome dello strato informativo su couchbase
        * @param x:  coordinata x della tassello
        * @param y:  coordinata y della tassello
        * @param zoom: il livello di zoom del tassello
        * @param page: il numero di pagina 
        * @return  il documento in JSON richiesto o null se non esiste.
        * @throws ApsSystemException
        */
        public String getTileFromCouchbaseForAPI(String layer, int x, int y, int zoom, int page ) throws ApsSystemException;

       /**
        * API SUPPORT PER LA GESTIONE STRATI INFORMATIVI SUL BUCKET COUCHBASE 
        * Legge su Couchbae il documento con id specificato  
        * @return il documento in JSON richiesto.
        * @throws ApsSystemException
        */
       public String getDocumentFromCouchbaseForAPI(String ids) throws ApsSystemException;



       
}
