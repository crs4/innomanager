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
package org.crs4.entando.innomanager;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.crs4.entando.innomanager.aps.system.services.layer.test.TestCouchbaseLayerDAO;
import org.crs4.entando.innomanager.aps.system.services.layer.test.TestWorkLayerDAO;
import org.crs4.entando.innomanager.aps.system.services.layer.test.TestLayerManager;

public class AllTests {
	
    public static Test suite() {
		TestSuite suite = new TestSuite("Test for InnoManager");
		System.out.println("Test for InnoManager");
		
		//suite.addTestSuite(TestCouchbaseLayerDAO.class);
		suite.addTestSuite(TestWorkLayerDAO.class);
		suite.addTestSuite(TestLayerManager.class);
		
		
		return suite;
	}
    
}
