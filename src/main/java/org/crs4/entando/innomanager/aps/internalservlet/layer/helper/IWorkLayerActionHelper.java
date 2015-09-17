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

import com.agiletec.aps.system.services.user.UserDetails;
import org.crs4.entando.innomanager.aps.system.services.layer.model.WorkLayer;
import javax.servlet.http.HttpServletRequest;

/**
 * L'interfaccia della classe helper WorkLayerActionHelper .
 * @author Roberto Demontis (CRS4) 
 * @email demontis@crs4.it
 */
public interface IWorkLayerActionHelper  {	
	
	/**
	 * Setta il nome dell'esecutore dell'azione sul WorkLayer
	 * @param request The request.
	 * @param layer l'oggetto WorkLayer.
	 */
	public void setOperator(WorkLayer layer, HttpServletRequest request);
	
	/**
	 * Verifica se l'utente ha i permessi di accesso al WorkLayer.
	 * @param request La richiesta.
         * @param currentUser L'utente che ha generato la richiesta.
	 * @return true se l'utente ha i permessi altrimenti ritorna false
         * */
	public boolean isUserAllowed(WorkLayer layer, UserDetails currentUser);
        
        /**
	 * Verifica se l'utente corrente è nel gruppo abilitato a lavorare sui WorkLayer.
	 * @param request The request.
	 * @return Vero se l'utente è nel gruppo altrimenti Falso
         * */
	public boolean isUserOnInnoGroup(UserDetails currentUser);
	
			
}