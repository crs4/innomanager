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
package org.crs4.entando.innomanager.aps.system.services.layer.model;

import java.util.Date;
import org.crs4.entando.innomanager.aps.system.InnoManagerSystemConstants;

/**
 * Rappresentazione di uno strato informativo di lavoro
 * @author R.Demontis
 */
public class WorkLayer { 
    
    /**
     * Restituisce l'identificativo del layer.
     * @return L'identificativo del layer.
     */
    public String getName() {
        return _name;
    }
    
    /**
     * Imposta l'identificativo del layer.
     * @param id L'identificativo del layer.
     */
    public void setName(String name) {
        if (name != null )
            this._name = name;
    }
    
    /**
     * Restituisce la descrizione del layer.
     * @return La descrizione del layer.
     */
    public String getDescription() {
        return _descr;
    }
    
    /**
     * Imposta la descrizione del layer.
     * @param descr La descrizione del layer.
     */
    public void setDescription(String descr) {
        this._descr = descr;
    }
    
    /**
     * Restituisce la data dell'ultima modifica dello stato del layer.
     * @return La data dell'ultima modifica dello stato del layer.
     */
    public Date getLastUpdate() {
        return _lastupdate;
    }
    
    /**
     * Imposta la data dell'ultima modifica dello stato del layer .
     * @param date La data dell'ultima modifica dello stato del layer.
     */
    public void setLastUpdate(Date date) {
        this._lastupdate = date;
    }
    
    /**
     * Restituisce il nickname di chi sta eseguendo l'operazione sul layer.
     * @return Il nickname di chi sta eseguendo l'operazione sul layer.
     */
    public String getOperator() {
        return _operator;
    }
    
    /**
     * Imposta il nickname di chi sta eseguendo l'operazione sul layer.
     * @param Il nickname di chi sta eseguendo l'operazione sul layer.
     */
    public void setOperator(String operator) {
        this._operator = operator;
    }
    
    /**
     * Restituisce il nickname di chi sta eseguendo l'operazione sul layer.
     * @return Il nickname di chi sta eseguendo l'operazione sul layer.
     */
    public String getLog() {
        return _log;
    }
    
    /**
     * Imposta il nickname di chi sta eseguendo l'operazione sul layer.
     * @param Il nickname di chi sta eseguendo l'operazione sul layer.
     */
    public void setLog(String log) {
        this._log = log;
    }
    
    
    
    
    /**
     * Restituisce lo status del worklayr.
     * @return lo status del worklayr.
     */
    public String getStatusText() {
        if ( _status == InnoManagerSystemConstants.WORK_LAYER_STATUS_CREATE)
            return  "innomanager_WORK_LAYER_STATUS_CREATE";
        else if ( _status == InnoManagerSystemConstants.WORK_LAYER_STATUS_UPLOADED)
            return  "innomanager_WORK_LAYER_STATUS_UPLOADED";
        else if ( _status == InnoManagerSystemConstants.WORK_LAYER_STATUS_IMPORTING)
            return  "innomanager_WORK_LAYER_STATUS_IMPORTING";
        else if ( _status == InnoManagerSystemConstants.WORK_LAYER_STATUS_IMPORTED)
            return  "innomanager_WORK_LAYER_STATUS_IMPORTED";
        else if ( _status == InnoManagerSystemConstants.WORK_LAYER_STATUS_ELABORATING)
            return  "innomanager_WORK_LAYER_STATUS_ELABORATING";
        else if ( _status == InnoManagerSystemConstants.WORK_LAYER_STATUS_READY)
            return  "innomanager_WORK_LAYER_STATUS_READY";
        else if ( _status == InnoManagerSystemConstants.WORK_LAYER_STATUS_EXPORTING)
            return  "innomanager_WORK_LAYER_STATUS_EXPORTING";
        else if ( _status == InnoManagerSystemConstants.WORK_LAYER_STATUS_COMPLETED)
            return  "innomanager_WORK_LAYER_STATUS_COMPLETED";
        else if ( _status == InnoManagerSystemConstants.WORK_LAYER_STATUS_DELETING)
            return  "innomanager_WORK_LAYER_STATUS_DELETING";
        else return  "innomanager_WORK_LAYER_WRONG_STATUS";
    }
    
    /**
     * Restituisce lo status del worklayr.
     * @return lo status del worklayr.
     */
    public boolean isWrongStatus() {
        if ( _status < InnoManagerSystemConstants.WORK_LAYER_STATUS_CREATE 
          || _status > InnoManagerSystemConstants.WORK_LAYER_STATUS_DELETING )
            return  true;
        return false;
    }
   
    /**
     * Restituisce lo status del worklayr.
     * @return lo status del worklayr.
     */
    public int getStatus() {
        return _status;
    }
    
    /**
     * Imposta  lo status del worklayr.
     * @param  lo status del worklayr
     */
    public void setStatus(int status) {
        this._status = status;
    }
    
    private int     _status = 0;
    private String  _name;
    private String  _log;
    private String  _descr;
    private String  _operator;
    private Date    _lastupdate;
    
}
