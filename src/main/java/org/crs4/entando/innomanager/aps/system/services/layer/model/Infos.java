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

import com.agiletec.aps.system.ApsSystemUtils;
import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import java.util.ArrayList;
import java.util.Collection;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Rappresentazione di un tassello con le info per un particolare campo 
 * degli elementi di uno strato informativo
 * @author R.Demontis
 */
public class Infos { 

    /**
     * Restituisce l'oggetto Infos presente nel documento JSON con le 
     * informazioni alfanumeriche degli elementi in una tile 
     * Esempio tile      
     *    { 
     *        "id": "comuni:subregione:535:384:10",
     *        "values":[
     *         {"id":"comuni:001","v":"Sassarese"},
     *         {"id":"comuni:507","v":"Nurra"},
     *         {"id":"comuni:137","v":"Nurra"},
     *         {"id":"comuni:467","v":"Nurra"}]
     *    }
     * @param infos_json_document il documento JSON 
     * @param gson il parser del JSON
     * @return l'oggetto Infos con le informazioni alfanumeriche degli elementi 
     * in una tile
     */
    public static Infos loadInfos( String infos_json_document, Gson gson){ 
       
        if ( infos_json_document == null || gson == null ) 
            return null;
        Infos infos = new Infos();
        HashMap parsedDoc = gson.fromJson( infos_json_document, HashMap.class );
        infos.setId((String)parsedDoc.get("id"));
        try {
            Collection<StringMap> coll = (ArrayList)parsedDoc.get("values");
            for (Iterator<StringMap> it = coll.iterator(); it.hasNext();) {
                StringMap sm = it.next();
                infos.addValue( (String)sm.get("id"), (String)sm.get("v"));
            }
        } catch (Exception e) {
           ApsSystemUtils.getLogger().warn("Errors creating Tile from json: field objs");    
        
        }    
        return infos; 
        
    }
    
    /**
     * @return the UTF8 json document
     */
    public String toJSON() {
        String document = "";
        
        return document;
    }
    
    /**
     * @return the _values
     */
    public ArrayList<Value> getValues() {
        if ( _values ==null )
            _values = new ArrayList<Value>();
        return _values;
    }

    /**
     * @param id: the identificator of the feature
     * @param value: the value of the field 
     */
    public void addValue(String id, String value) {
        getValues().add(new Value(id,value));
    }
    
    /**
     * @param values the _valus to set
     */
    public void setValues(ArrayList<Value> values) {
        this._values = values;
    }
    
    static class Value {
        private String _id;
        private String _v;
        private Value(String id, String v) {
            this._id = id;
            this._v = v;
        }
    }
    
    /**
     * Restituisce l'identificativo del tassello
     * @return L'identificativo del tassello.
     */
    public String getId() {
        return _id;
    }
    
    /**
     * Imposta l'identificativo del tassello.
     * @param id L'identificativo del tassello.
     */
    public void setId(String id) {
        this._id = id;
    }
        
    private String   _id;
    private ArrayList<Value>  _values;
}
