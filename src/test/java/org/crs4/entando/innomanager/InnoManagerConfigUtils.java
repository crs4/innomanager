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

import com.agiletec.ConfigTestUtils;

/**
 * @author R.Demontis
 */
public class InnoManagerConfigUtils extends ConfigTestUtils {
    
    @Override
    protected String[] getSpringConfigFilePaths() {
    	String[] filePaths = new String[8];
        filePaths[0] = "classpath:spring/propertyPlaceholder.xml";
		filePaths[1] = "classpath:spring/baseSystemConfig.xml";
		filePaths[2] = "classpath*:spring/aps/**/**.xml";
		filePaths[3] = "classpath*:spring/apsadmin/**/**.xml";
		filePaths[4] = "classpath*:spring/plugins/**/aps/**/**.xml";
		filePaths[5] = "classpath*:spring/plugins/**/apsadmin/**/**.xml";
                filePaths[6] = "classpath*:spring/innomanager/aps/**/**.xml";
		filePaths[7] = "classpath*:spring/innomanager/apsadmin/**/**.xml";
        return filePaths;
    }
    
}
