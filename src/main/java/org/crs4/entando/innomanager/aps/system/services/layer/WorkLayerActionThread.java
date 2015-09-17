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

import org.crs4.entando.innomanager.aps.system.InnoManagerSystemConstants;


/**
 * 
 * @author R.Demontis
 */
public class WorkLayerActionThread extends Thread {
    
/**
* Setup the thread for delete action
* @param layerManager the service that handles the entities
* @param name the service that handles the entities
* @param action the service that handles the entities
* @param forced the service that handles the entities
*/
    public WorkLayerActionThread(LayerManager layerManager, String name, int action, boolean forced ) {
        
        this._layerManager = layerManager;
        this._workLayerName = name;
        this._action = action;
        this._forced = forced;
    }

    @Override
    public void run() { 
        try {
             if ( _layerManager.addToThreadWorking(this, _forced) ){

                if ( getAction() == InnoManagerSystemConstants.WORK_LAYER_ACTION_DELETE )
                    this._layerManager.deleteWorkLayer(this);
                else if ( getAction() == InnoManagerSystemConstants.WORK_LAYER_ACTION_IMPORT)
                    this._layerManager.importWorkLayer(this);
                else if ( getAction() == InnoManagerSystemConstants.WORK_LAYER_ACTION_ELABORATE )
                    this._layerManager.elaborateWorkLayer(this);
                else if ( getAction() == InnoManagerSystemConstants.WORK_LAYER_ACTION_EXPORT )
                    this._layerManager.exportWorkLayer(this); 
             }
             _layerManager.removeFromThreadWorking(this, false);
        } catch (Exception e) {
             this._layerManager.manageThreadError(_workLayerName,e.getLocalizedMessage(), getAction());  
        }
    } 
    
    /**
     * @return the _workLayerName
     */
    public String getWorkLayerName() {
        return _workLayerName;
    }

    /**
     * @param workLayerName the _workLayerName to set
     */
    public void setWorkLayerName(String workLayerName) {
        this._workLayerName = workLayerName;
    }
   
    /**
     * @return the _action
     */
    public int getAction() {
        return _action;
    }

    /**
     * @param action the _action to set
     */
    public void setAction(int action) {
        this._action = action;
    }
    
    /**
     * @return the _action
     */
    public String getLog() {
        return _log;
    }

    /**
     * @param action the _action to set
     */
    public void setLog(String log) {
        this._log = log + "\n" + this._log ; 
    }
    
    private LayerManager _layerManager;
    private String _workLayerName;
    private int _action;
    private String _log = "";
    private boolean _forced;    

}