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
import java.util.List;

/**
 * Rappresentazione di uno strato informativo di lavoro
 * @author R.Demontis
 */
public class InnoLayer { 

    /** crea un oggetto InnoLayer con le informazioni di un Layer a partire
     * da un documento JSON
     *   esempio di documento json con le informazioni di un layer
     * { 
     *    "_innoname_": "comuni",
     *    "description": "I comuni della sardegna fonte istat"
     *    "_bbox_":{
     *                "type":"Polygon",
     *                "coordinates":[[[8.1307698,38.859124],
     *                                [8.1307698,41.3132405],
     *                                [9.8270429,41.3132405],
     *                                [9.8270429,38.859124],
     *                                [8.1307698,38.859124]]]
     *    }, 
     *    "vertices":314610, 
     *    "count": 377, 
     *    "type": "MULTIPOLYGON", 
     *    "attributes": [
     *                   { "name": "gid", "type":"integer"},
     *                   { "name": "istat", "type":"double precision"},
     *                   { "name": "nome", "type":"character varying"},
     *                   { "name": "regione", "type":"character varying"},
     *                   { "name": "subregione", "type":"character varying"}
     *    ],
     *    "levels":[{"zoom":10,"tiles":[535,382,539,391]},
     *              {"zoom":11,"tiles":[1070,765,1079,783]},
     *              {"zoom":12,"tiles":[2140,1530,2159,1567]},
     *              {"zoom":13,"tiles":[4281,3061,4319,3134]},
     *              {"zoom":14,"tiles":[8562,6123,8639,6269]},
     *              {"zoom":15,"tiles":[17124,12247,17278,12539]},
     *              {"zoom":16,"tiles":[34248,24495,34556,25079]},
     *              {"zoom":17,"tiles":[68496,48990,69113,50158]}]
     *  }
     *  @param infos_json_document il documento JSON 
     *  @param gson il parser del JSON
     *  @return l'oggetto InnoLayer con le informazioni del Layer
     *  
     */
    public static InnoLayer createLayer(String innoLayer_json_document, Gson gson){ 
      
        
        if ( innoLayer_json_document == null || gson == null ) 
            return null;
        InnoLayer layer = new InnoLayer();
        HashMap parsedDoc = gson.fromJson( innoLayer_json_document, HashMap.class );
        layer.setName((String)parsedDoc.get("_innoname_"));
        try {
            layer.setCount((Double)parsedDoc.get("count"));
        } catch (Exception e) {
            ApsSystemUtils.getLogger().warn("Errors creating InnoLayer from json: field count");
        }
        try {
            layer.setDescription((String)parsedDoc.get("description"));
        } catch (Exception e) {
            ApsSystemUtils.getLogger().warn("Errors creating InnoLayer from json: field description");
        }
        try {
            layer.setVertices((Double)parsedDoc.get("vertices"));
        } catch (Exception e) {
            ApsSystemUtils.getLogger().warn("Errors creating InnoLayer from json: field vertices");
        }
        layer.setType((String)parsedDoc.get("type"));
        try {
            Collection<StringMap> coll = (Collection<StringMap>)parsedDoc.get("attributes");
            for (Iterator<StringMap> it = coll.iterator(); it.hasNext();) {
                StringMap sm = it.next();
                layer.addAttribute( (String)sm.get("name"), (String)sm.get("type"));
            }
        } catch (Exception e) {
           ApsSystemUtils.getLogger().warn("Errors creating InnoLayer from json: field attributes");    
        }
        try {
            Collection<StringMap> coll = (Collection<StringMap>)parsedDoc.get("levels");
            for (Iterator<StringMap> it = coll.iterator(); it.hasNext();) {
                StringMap sm = it.next();
                String tiles = sm.get("tiles").toString().replace(".0","");
                String zoom  = sm.get("zoom").toString().replace(".0","");
                layer.addLevel( zoom, tiles);
            }
        } catch (Exception e) {
           ApsSystemUtils.getLogger().warn("Errors creating InnoLayer from json: field levels");    
        }
        try {
            String bbox = parsedDoc.get("_bbox_").toString();
            bbox = bbox.substring(bbox.indexOf("[[[")+1,bbox.indexOf("]]]")).replace("[", " ").replace("]", " ");
            String[] txtcoords = bbox.split(",");
            double[] coords = new double[4];
            coords[0] = Double.parseDouble(txtcoords[0]);
            coords[1] = Double.parseDouble(txtcoords[1]);
            if ( coords[0] == Double.parseDouble(txtcoords[2]) )
                coords[2] = Double.parseDouble(txtcoords[4]);
            else coords[2] = Double.parseDouble(txtcoords[2]);
            if ( coords[1] == Double.parseDouble(txtcoords[3]) )
                coords[3] = Double.parseDouble(txtcoords[5]);
            else coords[3] = Double.parseDouble(txtcoords[3]);
            layer.setBbox(coords);
        } catch (Exception e) {
            ApsSystemUtils.getLogger().warn("Errors creating InnoLayer from json: field bbox");
        }
        return layer; 
        
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
    public List<Attribute> getAttributes() {
        if ( _attributes ==null )
            _attributes = new ArrayList<Attribute>();
        return _attributes;
    }
    
    /**
     * @return the _levels
     */
    public List<Level> getLevels() {
        if ( _levels ==null )
            _levels = new ArrayList<Level>();
        return _levels;
    }

    /**
     * @param attributes the _attributes to set
     */
    public void addAttribute(String name, String type) {
        getAttributes().add(new Attribute(name,type));
    }
    
    /**
     * @param attributes the _attributes to set
     */
    public void setAttributes(ArrayList<Attribute> attributes) {
        this._attributes = attributes;
    }
    
    /**
     * @param attributes the _attributes to set
     */
    public void addLevel(String zoom, String tiles) {
        getLevels().add(new Level(zoom,tiles));
    }
    
    /**
     * @param attributes the _attributes to set
     */
    public void setLevels(ArrayList<Level> levels) {
        this._levels = levels;
    }
    
    public class Attribute {
        private String _name;
        private String _type;
        private Attribute(String name, String type) {
            this._name = name;
            this._type = type;
        }
        public String getName(){
            return _name;
        }
        public String getType(){
            return _type;
        }
    }
    
    public class Level {
        private String _zoom;
        private String _tiles;
        private Level(String zoom, String tiles) {
            this._zoom = zoom;
            this._tiles = tiles;
        }
        public String getZoom(){
            return _zoom;
        }
        public String getTiles(){
            return _tiles;
        }
    }
    
    
    /**
     * @return the _bbox
     */
    public double[] getBbox() {
        return _bbox;
    }
    
    
    /**
     * @return the _bbox in text format
     */
    public String getBboxText() {
        if ( _bbox != null && _bbox.length == 4)
            return "["+_bbox[0]+","+_bbox[1]+","+_bbox[2]+","+_bbox[3]+"]";
        return "null";
    }

    /**
     * @param bbox the _bbox to set
     */
    public void setBbox(double[] bbox) {
        this._bbox = bbox;
    }

    /**
     * @return the _vertices
     */
    public int getVertices() {
        return _vertices;
    }

    /**
     * @param vertices the _vertices to set
     */
    public void setVertices(double vertices) {
        this._vertices = (int)vertices;
    }

    /**
     * @return the _type
     */
    public String getType() {
        return _type;
    }

    /**
     * @param type the _type to set
     */
    public void setType(String type) {
        this._type = type;
    }

    /**
     * @return the _count
     */
    public int getCount() {
        return _count;
    }

    /**
     * @param count the _count to set
     */
    public void setCount(double count) {
        this._count = (int)count;
    }
    
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
        
    private String   _name;
    private String   _descr;
    private double[] _bbox;
    private int      _vertices;
    private String   _type;
    private int      _count;
    private ArrayList<Attribute>  _attributes;
    private ArrayList<Level>  _levels;
}
