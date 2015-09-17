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
 * Rappresentazione di una tile o macro tile  
 * @author R.Demontis
 */
public class Tile { 

    /**
     * Restituisce l'oggetto Tile presente nel documento JSON con le geometrie
     * degli elementi che vi ricadono 
     * Esempio tile 
     * { 
     *        "id":"comuni:8596+0:6227+2:14", 
     *        "page":1, 
     *        "pages":1, 
     *       "_bbox_":{
     *                    "type": "Polygon",
     *                    "coordinates":[[
     *                          [8.87695,39.53794],[8.87695,39.58876],
     *                          [8.89893,39.58876],[8.89893,39.53794],
     *                          [8.87695,39.53794]]]}, 
     * 
     *        "objs": [{ "id":"094", "g":"3((ff00ffff00ff0000ff00))"} ]
     *      }
     * @param tile_json_document
     * @param gson
     * @return 
     */
    public static Tile createTile( String tile_json_document, Gson gson){ 
        if ( tile_json_document == null || gson == null ) 
            return null;
        Tile tile = new Tile();
        HashMap parsedDoc = gson.fromJson( tile_json_document, HashMap.class );
        tile.setId((String)parsedDoc.get("id"));
        try {
            tile.setPage((Double)parsedDoc.get("page"));
        } catch (Exception e) {
            ApsSystemUtils.getLogger().warn("Errors creating Tile from json: field page");
        }
        try {
            tile.setPages((Double)parsedDoc.get("pages"));
        } catch (Exception e) {
            ApsSystemUtils.getLogger().warn("Errors creating Tile from json: field pages");
        }
        try {
            Collection<StringMap> coll = (Collection<StringMap>)parsedDoc.get("objs");
            for (Iterator<StringMap> it = coll.iterator(); it.hasNext();) {
                StringMap sm = it.next();
                tile.addObj( (String)sm.get("id"), (String)sm.get("g"));
            }
        } catch (Exception e) {
           ApsSystemUtils.getLogger().warn("Errors creating Tile from json: field objs");    
        
        }    
        return tile; 
    }
    
    /**
     * @return the UTF8 json document
     */
    public String toJSON() {
        String document = "";
        
        return document;
    }
    
    /**
     * @return the _attributes
     */
    public ArrayList<Element> getObjs() {
        if ( _objs ==null )
            _objs = new ArrayList<Element>();
        return _objs;
    }

    /**
     * @param attributes the _attributes to set
     */
    public void addObj(String id, String compact_geometry) {
        getObjs().add(new Element(id,compact_geometry));
    }
    
    /**
     * @param attributes the _attributes to set
     */
    public void setObjs(ArrayList<Element> elements) {
        this._objs = elements;
    }
    
    public class Element {
        private String _id;
        private String _g;
        private Element(String id, String g) {
            this._id = id;
            this._g = g;
        }
    }
    
    /**
     * @return the _pages
     */
    public int getPages() {
        return _pages;
    }

    /**
     * @param pages the _pages to set
     */
    public void setPages(double pages) {
        this._pages = (int)pages;
    }

    /**
     * @return the _page
     */
    public int getPage() {
        return _page;
    }

    /**
     * @param page the _page to set
     */
    public void setPage(double page) {
        this._page = (int)page;
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
    private int      _page;
    private int      _pages;
    private ArrayList<Element>  _objs;
}
