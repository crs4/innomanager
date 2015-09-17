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
package org.crs4.entando.innomanager.aps.tags;

import org.crs4.entando.innomanager.aps.system.InnoManagerSystemConstants;
import org.crs4.entando.innomanager.aps.system.services.layer.model.WorkLayer;
import org.crs4.entando.innomanager.aps.system.services.layer.ILayerManager;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import java.util.Iterator;
import javax.servlet.http.HttpSession;

/**
 * Tag erogatore della lista degli strati informativi di lavoro (worklayer) .
 * @author R.Demontis
 */
public class WorkLayerListTag extends TagSupport {
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		try {
			HttpSession session = this.pageContext.getSession();
                        UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			ILayerManager layerManager = (ILayerManager) ApsWebApplicationUtils.getBean(InnoManagerSystemConstants.INNO_DATAMANAGER, this.pageContext);
			String name = request.getParameter("name");
                        String operator = currentUser.getUsername();
                        if ( operator.equals("admin") ) operator = null;
                        List<WorkLayer> layers = layerManager.searchWorkLayers(name, operator);
                        WorkLayer layer;
                        for ( Iterator<WorkLayer> iter = layers.iterator(); iter.hasNext();) {
                            layer = iter.next();
                            if ( layer.getStatus() == InnoManagerSystemConstants.WORK_LAYER_STATUS_UPLOADED ) 
                            layerManager.verifyFile(layer);
                        }    
                        this.pageContext.setAttribute(this.getListName(), layers);
		} catch (Throwable e) {
			ApsSystemUtils.logThrowable(e, this, "doStartTag");
			throw new JspException("Errore in doStartTag", e);
		}
		return super.doStartTag();
	}
	
        @Override
	public void release() {
		this._listName = null;
	}
	
	/**
	 * Restituisce il nome con il quale viene inserita nel pageContext la lista dei work layers
	 * @return Il nome della variabile.
	 */
	public String getListName() {
		return _listName;
	}
	
	/**
	 * Setta il nome con il quale viene inserita nel pageContext la lista dei work layers
	 * @param listName Il nome della variabile.
	 */
	public void setListName(String listName) {
		this._listName = listName;
	}
	
	private String _listName;
	
}
