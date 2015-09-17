/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.crs4.entando.innomanager.aps.system.services.layer;

import org.crs4.entando.innomanager.aps.system.services.layer.model.WorkLayer;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author root
 */
public interface IWorkLayerDAO {

    /**
     * 
     * @param layer il WorkLayer da aggiunger
     */
    void addLayer(WorkLayer layer);

    /**
     * 
     * @param name il nome del WorkLayer da cancellare
     * @return true se il layer è stato cancellato
     */
    boolean deleteLayer(String name);

    /**
     * 
     * @param name il nome del WorkLayer da cancellare
     * @return true se le tabelle e ifile associati al layer sono state cancellate
     */
    boolean deleteLayerExtraResource(String name);

    /**
     * restituisce il nome e tipo degli attributi del WorkLayer con il nome indicato
     * @param name il nome del WorkLayer 
     * @return il nome e tipo degli attributi del WorkLayer con il nome indicato
     */
    HashMap<String, String> getWorkLayerAttributes(String name) throws ApsSystemException;

    /**
     * restituisce una lista di WorkLayer di un utente che hanno nome simile a 
     * quello indicato
     * @param name chiave di ricerca testuale 
     * @param operator il nome di uno user, può essere null 
     * 
     * @return  una lista di WorkLayer 
     */
    List<WorkLayer> searchWorkLayers(String name, String operator);
    
    /**
     * carica un WorkLayer
     * @param name il nome del WorkLayer
     * @return il WorkLayer
     */
    WorkLayer loadLayer(String name);

    /**
     * restituisce la lista dei WorkLayer  
     * 
     * @return  la lista di WorkLayer 
     */
    List<WorkLayer> loadLayers();
    
    /**
     * modifica un WorkLayer
     * @param layer il WorkLayer
     */
    void updateLayer(WorkLayer layer);
    
    /**
     * crea l'ambiente di lavoro (schema e tabele nel database)
     * @param layer il WorkLayer
     */
    void prepareWorkLayerArea(WorkLayer layer);

    /**
     * crea l'ambiente di lavoro (schema e tabele nel database)
     * @param layer il WorkLayer
     * @return  un log risultato dell'azione 
     */
    String prepareLayer(String name);

    /**
     * esegue il tassellamento
     * @param layer il WorkLayer
     * @return  un log risultato dell'azione 
     */
    String tiling(String name, Integer zoom);
    
    /**
     * estrae le informazioni 
     * @param layer il WorkLayer
     * @return  un log risultato dell'azione 
     */
    boolean extractInfo(String name);
    
    /**
     * Restituisce le proprietà delle tabelle nel database relative al WorkLayer
     * @param name il il nome del WorkLayer
     * @return le tabelle presenti con associato il numero di righe
     */
    Map<String,String>  getTablesReport (String name) ;
    
    /**
     * esegue la scrittura su disco de documenti json
     * @param layer il WorkLayer
     * @return  un log risultato dell'azione 
     */
    String writeToDisk(String name, String path);
    
    /**
     * 
     * @return le proprietà della connessione ai nodi couchbase 
     */
    public Properties getConnectionParameters();
    
    
    /**
     * le funzioni su postgresql richiamate dall'ETL
     */
    
    
    /**
     * crea i tasselli per un layer ad un livello di zoom
     */
    public static final String CREATE_TILE  = "_inno_create_tiles ";
        
    /**
     * aggiunge la priorità di inserimento nel tassello agli elementi di un layer
     */
    public static final String PREPARE_LAYER = "_inno_prepare_layer "; 
       
    /**
     * prepara i dati alfanumerici 
     */
    public static final String CREATE_INFOS = "_inno_prepare_info ";
        
    /**
     * scrive i documenti JSON su disco 
     */
    public static final String WRITE_TO_DISK = "_inno_write_to_disk";
}
