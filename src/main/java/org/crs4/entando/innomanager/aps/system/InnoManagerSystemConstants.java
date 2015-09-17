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
package org.crs4.entando.innomanager.aps.system;

/**
 * Interfaccia con le principali costanti del progetto InnoManager Portal.
 * @author R.Demontis
 */
public interface InnoManagerSystemConstants {
	
	/**
	 * Nome del servizio che gestisce gli oggetti WorkLayer . 
	 */
	public static final String INNO_DATAMANAGER = "LayerManager";
        
        // CODICI di stato degli oggetti WorkLayer 
	
        /* WORK_LAYER_STATUS_CREATE: layer creato con i soli campi nome e 
         * descrizione, possibile azione: upload
	 */
        public static final int WORK_LAYER_STATUS_CREATE = 0;
        
        /** WORK_LAYER_STATUS_UPLOADING: layer in fase di upload,
	 */
	public static final int WORK_LAYER_STATUS_UPLOADING = 1;
        
        /** WORK_LAYER_STATUS_UPLOADED: layer con almeno uno shapefile su disco,
         * possibile azione: prepare, upload another
	 */
	public static final int WORK_LAYER_STATUS_UPLOADED = 2;
        
        /** WORK_LAYER_STATUS_IMPORTING: scrittura su database 
         * dei dati dello shapefile
         */
	public static final int WORK_LAYER_STATUS_IMPORTING = 3;
        
        /** WORK_LAYER_STATUS_IMPORTED: i dati dello shapefile sono su database, 
         * 
	 */
	public static final int WORK_LAYER_STATUS_IMPORTED = 4;
        
        /** WORK_LAYER_STATUS_ELABORATING: elaborazione su database 
         * dei dati per la generazione dei tasselli  
         */
	public static final int WORK_LAYER_STATUS_ELABORATING = 5;
        
        /** WORK_LAYER_STATUS_READY: i dati sono pronti per la generazione
         * dei docuenti JSON su disco, 
         * 
	 */
	public static final int WORK_LAYER_STATUS_READY = 6;
        
        
        /**WORK_LAYER_STATUS_EXPORTING: il layer e' in fase di importazione su couchbase  
	 */
	public static final int WORK_LAYER_STATUS_EXPORTING = 7;
        
        /**WORK_LAYER_STATUS_COMPLETED: il layer e' su couchbase, 
         * possibile azione: cancellazione dei dati di lavoro
	 */
	public static final int WORK_LAYER_STATUS_COMPLETED = 8;
        
        /** WORK_LAYER_STATUS_DELETING: layer in fase di cancellazione,
	 */
	public static final int WORK_LAYER_STATUS_DELETING = 9;
        
        /** WORK_LAYER_WRONG_STATUS: Errore generico,
	 */
	public static final int WORK_LAYER_WRONG_STATUS = -1;
        
        /**  INNO_LAYER_ACTION_NONE: nessuna operazione in esecuzione
        */
        public static final int WORK_LAYER_ACTION_NONE = 0;
        
        /**  INNO_LAYER_WRITE_ACTION: operazione di import sul database di lavoro
         *   dei dati nei shapefiles shp2psql 
         */
        public static final int WORK_LAYER_ACTION_IMPORT = 1;
        
        /**  INNO_LAYER_WRITE_ACTION: operazione di elaborazione dei dati 
         */
        public static final int WORK_LAYER_ACTION_ELABORATE = 2;
        
        /**  INNO_LAYER_IMPORT_ACTION: operazione di scrittura su couchbase 
         * cbdbloader  
         */
        public static final int WORK_LAYER_ACTION_EXPORT = 3;
        
        /**  INNO_LAYER_DELETE_ACTION: operazione di cancellazione dati di lavoro
        */
        public static final int WORK_LAYER_ACTION_DELETE = 4;
        
        /**  WORK_LAYER_ACTION_NEW: operazione di creazione dati di lavoro
        */
        public static final int WORK_LAYER_ACTION_NEW = 5;
        
        
        /**  INNO_LAYER_DO_ACTION_PATH path azione apertura form gestione WorkLayer
        */
        public static final String WORK_LAYER_EDIT_ACTION_PATH = "/ExtStr2/do/Frontend/WorkLayer/entryEditAction.action";
        
        /**  WORK_LAYER_ELABORATE_ACTION_PATH path azione apertura form elaborazioni WorkLayer
        */
        public static final String WORK_LAYER_ELABORATE_ACTION_PATH = "/ExtStr2/do/Frontend/WorkLayer/entryElaborateAction.action";
        
        /** INNO_USERGROUP: gruppo degli utenti abilitati a creare e gestire un layer 
	 */
    	public static final String INNO_USERGROUP = "innousers";
        
        public static final String FINAL_DEST_PAGE_PARAM_NAME = "finalDestPage";	
        
        /** nomi dei tag e degli attributi del DOM dell'oggetto xml
         * innomanager_config contenente i parametri di connessione 
         * a couchbase  
         */ 
        public static final String INNOCONFIG_TAG_ROOT  = "innoConnectorConfig";
        public static final String CONFIG_TAG_BUCKET    = "bucket";
        public static final String CONFIG_TAG_NODES      = "nodes";
        public static final String CONFIG_ATTRIBUTE_NAME   = "name";
        public static final String CONFIG_ATTRIBUTE_PASSWORD   = "password";
        public static final String CBDOCLOADER_PATH   = "/opt/couchbase/bin/tools/cbdocloader";
        public static final String WORK_LAYER_EXPORT_PATH     = "/tmp/_inno_data";
        
    
}

