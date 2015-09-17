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
package org.crs4.entando.innomanager.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * @author R.Demontis
 */
@DatabaseTable(tableName = WorkLayer.TABLE_NAME)
public class WorkLayer {
	
	public WorkLayer() {}
	
	@DatabaseField(columnName = "name", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false,  id = true)
        private String _name;
	
	@DatabaseField(columnName = "description", 
			dataType = DataType.STRING, 
			width = 255, 
			canBeNull = true)
        private String _description;

        @DatabaseField(columnName = "status", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
        private int _status;
        
        @DatabaseField(columnName = "operator", 
			dataType = DataType.STRING, 
			canBeNull = false)
        private String _operator;
        
	@DatabaseField(columnName = "lastupdate", 
			dataType = DataType.DATE, 
			canBeNull = false)
        private Date _lastupdate;
        
        @DatabaseField(columnName = "log", 
			dataType = DataType.STRING, 
			canBeNull = true)
        private String _log;
        
        
	public static final String TABLE_NAME = "innomanager_layers";
	
}

