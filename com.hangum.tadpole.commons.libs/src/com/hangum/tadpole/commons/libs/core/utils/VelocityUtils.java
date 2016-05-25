/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.libs.core.utils;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.junit.validator.PublicClassValidator;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;

/**
 * Velocity utils
 * 
 * @author hangum
 *
 */
public class VelocityUtils {
	private static final Logger logger = Logger.getLogger(VelocityUtils.class);
	
	/**
	 * template 
	 * 
	 * @param strName
	 * @param strSQL
	 * @param mapParameter
	 * @return
	 */
	public static String getTemplate(String strName, String strSQL, Map<String, Object> mapParameter) throws Exception {
		Writer writerReturn = new StringWriter();
		
		VelocityContext velocityCtx = new VelocityContext();
		Velocity.setProperty(RuntimeConstants.RUNTIME_LOG, PublicTadpoleDefine.DEFAULT_VELOCITY_LOG_FILE);//"../logs/tadpole/tadpoleVelocity.log");
		
		for(String strKey :mapParameter.keySet()) {
			velocityCtx.put(strKey, mapParameter.get(strKey));
		}

//		long stTme = System.currentTimeMillis();
		Velocity.evaluate(velocityCtx, writerReturn, strName, new StringReader(strSQL));			
//		if(logger.isDebugEnabled()) {
//			logger.debug("Name is " + strName + "[totaly time is ] " + (System.currentTimeMillis() - stTme));
//			logger.debug(writerReturn);
//		}
		
		return writerReturn.toString();
	}
}
