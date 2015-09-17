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

import org.crs4.entando.innomanager.aps.system.services.layer.model.WorkLayer; 
import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * @author R.Demontis
 */

public class WorkLayerDAO extends AbstractSearcherDAO implements IWorkLayerDAO {
	
	@Override
	public List<WorkLayer> loadLayers() {
                return this.searchWorkLayers(null,null);
	}
	
        @Override 
	public List<WorkLayer> searchWorkLayers(String name, String operator) {
		List<WorkLayer> layers = new ArrayList<WorkLayer>();
                FieldSearchFilter[] filters = {};
                FieldSearchFilter filter2, filter1;
		if (null != name && name.trim().length() > 0  ) {
	            filter1 = new FieldSearchFilter("name", name, true);
                    filter1.setOrder(FieldSearchFilter.ASC_ORDER);
		    filters = addFilter(filters, filter1);
		}
		if ( null != operator  && "admin" != operator) {
	            filter2 = new FieldSearchFilter("operator", operator, false);
		    filter2.setOrder(FieldSearchFilter.ASC_ORDER);
		    filters = addFilter(filters, filter2);
		}
                ApsSystemUtils.getLogger().info("searchLayers: filter size"  + filters.length );
                List<String>  ids = super.searchId(filters);
                ApsSystemUtils.getLogger().info("searchLayers: ids size"  + ids.size() );
		for (Iterator<String> it = ids.iterator(); it.hasNext();)   {
		    WorkLayer layer = this.loadLayer(it.next());
		    layers.add(layer);
                    ApsSystemUtils.getLogger().info("searchLayers: layer"  + layer.getName() );
		}
		return layers;
	}
        
        @Override
        public Properties getConnectionParameters() {
               Connection conn = null;
               Properties properties = new Properties();
               try {
                    DatabaseMetaData dsm = this.getConnection().getMetaData();
                    properties.setProperty("User", dsm.getUserName());
                    properties.setProperty("Url", dsm.getURL());
                    
               } catch (Throwable tr) {
	            
               } finally {
                    closeDaoResources(null, null, conn);
	       }
               return properties;
	}
        
        
	@Override
	public WorkLayer loadLayer(String name) {
		Connection conn = null;
		WorkLayer layer = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
                        stat = conn.prepareStatement(SELECT);
			stat.setString(1, name);
			res = stat.executeQuery();
			if (res.next()) {
				layer = new WorkLayer();
				layer.setName(name);
				layer.setDescription(res.getString(1));
				layer.setStatus(res.getInt(2));
                                layer.setOperator(res.getString(3));
                                layer.setLastUpdate(res.getDate(4));
                                layer.setLog(res.getString(5));
                                
                        }
		} catch (Throwable tr) {
	             ApsSystemUtils.getLogger().error("Error loading layer by name " + name, tr);
                } finally {
			closeDaoResources(res, stat, conn);
		}
		return layer;
	}
        
        @Override 
        public HashMap<String,String> getWorkLayerAttributes(String name) throws ApsSystemException { 
            return new HashMap<String,String>();
        }
	
	@Override 
	public void prepareWorkLayerArea(WorkLayer layer) {
                Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
                        if (layer.getName().contains(";") ) return;
                        stat = conn.prepareStatement("DROP SCHEMA IF EXISTS \"" + layer.getName() + "\" CASCADE; CREATE SCHEMA \"" + layer.getName() + "\"");
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
		        ApsSystemUtils.getLogger().error("Error dropping schema ", t);
                } finally {
			closeDaoResources(null, stat, conn);
		}
        }
        
        @Override
	public void addLayer(WorkLayer layer) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(INSERT);
			stat.setString(1, layer.getName());
                        if (null != layer.getDescription()) {
                            stat.setString(2, layer.getDescription());
			} else {
			    stat.setNull(2, Types.VARCHAR);
			}
                        stat.setInt(3, layer.getStatus());
                        stat.setString(4, layer.getOperator());
			if (null != layer.getLastUpdate()) {
				stat.setDate(5, new java.sql.Date(layer.getLastUpdate().getTime()));
			} else {
				stat.setNull(5, Types.DATE);
			}
                        stat.setString(6, layer.getLog());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
		        ApsSystemUtils.getLogger().error("Error adding new layer ", t);
                } finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void updateLayer(WorkLayer layer) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE);
			stat.setString(6, layer.getName());
                        stat.setString(1, layer.getDescription());
			stat.setString(2, layer.getOperator());
                        stat.setInt(3, layer.getStatus());
			if (null != layer.getLastUpdate()) {
				stat.setDate(4, new java.sql.Date(layer.getLastUpdate().getTime()));
			} else {
				stat.setNull(4, Types.DATE);
			}
                        stat.setString(5, layer.getLog());
			
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
	  	        ApsSystemUtils.getLogger().error("Error updating layer ", t);
                
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public boolean deleteLayer(String name) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE);
			stat.setString(1, name);
			stat.executeUpdate();
			conn.commit();
                        return deleteLayerExtraResource(name);
                        
		} catch (Throwable t) {
			this.executeRollback(conn);
	                ApsSystemUtils.getLogger().error("Error deleting layer" + name, t);
                
		} finally {
			closeDaoResources(null, stat, conn);
		}
                return false;
	}
        
        @Override
	public boolean deleteLayerExtraResource(String name) {
                Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
                        stat = conn.prepareStatement("DROP SCHEMA IF EXISTS \"" + name + "\" CASCADE;" );
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
		        ApsSystemUtils.getLogger().error("Error dropping schema ", t);
                } finally {
			closeDaoResources(null, stat, conn);
		}
                return true;
	}
        
        @Override
        public Map<String,String>  getTablesReport (String layername) {
            HashMap<String,String>  reports = new HashMap<String,String> ();
            Connection conn = null;
            PreparedStatement stat = null;
            ResultSet res = null;
            String report ="";
            try {
                conn = this.getConnection();
                stat = conn.prepareStatement(TABLES_REPORT);
                stat.setString(1, layername);
                res = stat.executeQuery();
                // report = "name_table1 = num_rows:name_table2=num_rows....."
                if (res.next()) {
                    report = res.getString(1);
                }
                ApsSystemUtils.getLogger().info(report);
                if ( report != null ) {
                    String[] tables = report.split(";");
                    String[] values;
                    for ( int i = 0; i < tables.length ; i++ ){
                        values =  tables[i].split("=");
                        if ( values.length == 2 )
                            reports.put(values[0], values[1]);
                    }
                }    
                
            } catch (Throwable t) {
                 ApsSystemUtils.getLogger().error("Error doing report " + layername, t);
            } finally {
                 closeDaoResources(res, stat, conn);
            }
            return reports;
        }
        
        @Override
	public String prepareLayer(String name) { 
                Connection conn = null;
                PreparedStatement stat = null;
                ResultSet res = null;
                String log ="";
                try {
                    conn = this.getConnection();
                    stat = conn.prepareStatement(PREPARE_LAYER);
                    stat.setString(1, name);
                    res = stat.executeQuery();
                    if (res.next()) {
                        log = res.getString(1);
                    }
		} catch (Throwable t) {
		     ApsSystemUtils.getLogger().error("Error doing prepare layer " + name, t);
                } finally {
			closeDaoResources(res, stat, conn);
		}
		return log;
	
        } 

        @Override
        public boolean extractInfo(String name){ 
               ///!!! inside Thread
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
                boolean result = false;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(CREATE_INFOS);
			stat.setString(1, name);
			res = stat.executeQuery();
			if (res.next()) {
			    result = true;
                        }
		} catch (Throwable t) {
		     ApsSystemUtils.getLogger().error("Error extarcting info for layer " + name, t);
        	} finally {
			closeDaoResources(res, stat, conn);
		}
		return result;
        } 

        @Override
        public String tiling(String name, Integer zoom ){ 
              ///!!! inside Thread ..
        	Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
                String log = "Error";
                try {
			conn = this.getConnection();
			stat = conn.prepareStatement(CREATE_TILE);
			stat.setString(1, name);
                        stat.setInt(2, zoom);
			res = stat.executeQuery();
			if (res.next()) {
			    log = res.getString(1);
                        }
		} catch (Throwable t) {
        	        ApsSystemUtils.getLogger().error("Error tiling for layer " + name, t);
        	} finally {
			closeDaoResources(res, stat, conn);
		}
		return log;
        } 
        
        @Override 
        public String writeToDisk(String name, String path ){ 
              ///!!! inside Thread ...to do async?
        	Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
                String log = "Error";
                try {
			conn = this.getConnection();
			stat = conn.prepareStatement(WRITE_TO_DISK); 
			stat.setString(1, name);
                        stat.setString(2, path);
                        res = stat.executeQuery();
			if (res.next()) {
			    log = res.getString(1);
                        }
		} catch (Throwable t) {
        	        ApsSystemUtils.getLogger().error("Error writing json for layer " + name, t);
        	} finally {
			closeDaoResources(res, stat, conn);
		}
		return log;
        } 
        
	
	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "innomanager_layers";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "name";
	}
	
	@Override
	protected boolean isForceCaseInsensitiveLikeSearch() {
		return true;
	}
	
	private static final String SELECT = 
			"SELECT description, status, operator, lastupdate, log FROM innomanager_layers  WHERE name = ?";
	
	private static final String INSERT = 
			"INSERT INTO innomanager_layers(name, description, status, operator, lastupdate, log ) VALUES (?, ?, ?, ?, ?, ?)";
	
	private static final String UPDATE = 
			"UPDATE innomanager_layers SET description = ?, operator = ?, status = ?, lastupdate = ?, log = ?  WHERE name = ?";
	
	private static final String DELETE = 
			"DELETE from innomanager_layers WHERE name = ? ";
        
        private static final String TABLES_REPORT = "SELECT _inno_tables_report(?) " ;
        
       
        
}
