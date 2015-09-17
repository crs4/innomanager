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
package org.crs4.entando.innomanager.aps.internalservlet.layer.helper;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.entity.EntityActionHelper;
import org.crs4.entando.innomanager.aps.system.InnoManagerSystemConstants;

import org.crs4.entando.innomanager.aps.system.services.layer.model.WorkLayer;

/**
 * La classe helper a supporto della classe WorkLayerAction.
 * @author Roberto Demontis (CRS4) 
 * @email demontis@crs4.it
 */
public class WorkLayerActionHelper  extends EntityActionHelper implements IWorkLayerActionHelper 
{

	@Override
        public void setOperator(WorkLayer layer, HttpServletRequest request) {
            try {
                if (null == layer) return;
        	UserDetails user =  (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
    		if ( getAuthorizationManager().isAuthOnGroup(user, InnoManagerSystemConstants.INNO_USERGROUP) )
                     layer.setOperator(user.getUsername());
    	} catch (Throwable t) {
        	ApsSystemUtils.logThrowable(t, this, "updateLayer");
        	throw new RuntimeException("Errore on updateLayer", t);
        }
	}

	@Override
        public boolean isUserOnInnoGroup(UserDetails currentUser) {
		return (getAuthorizationManager().isAuthOnGroup(currentUser, InnoManagerSystemConstants.INNO_USERGROUP) );
	}
        
        
	@Override
        public boolean isUserAllowed(WorkLayer layer, UserDetails currentUser) {
		return ( currentUser.getUsername().equals(layer.getOperator()) &&
                         isUserOnInnoGroup(currentUser) )
	              || getAuthorizationManager().isAuthOnGroup(currentUser, Group.ADMINS_GROUP_NAME);
	}
        
        
        @Override
        protected IAuthorizationManager  getAuthorizationManager() {
		return _authorizationManager;
	}
        
        @Override
	public void setAuthorizationManager( IAuthorizationManager authorizationManager ) {
		this._authorizationManager = authorizationManager;

	}

        private IAuthorizationManager _authorizationManager;
	

}