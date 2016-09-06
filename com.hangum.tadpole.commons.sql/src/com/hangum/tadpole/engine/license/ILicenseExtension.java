/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.license;

import java.io.File;

/**
 * 시스템 메니저의 확장 포인트
 * 
 * @author hangum
 *
 */
public interface ILicenseExtension {
	/**
	 * 
	 * @param strFile
	 */
	public void initExtension(File file);
	
	/**
	 * get host information
	 * 
	 * @return
	 */
	public String getCustomerInformation();
	
	/*
	 * license validation
	 */
	public String getValidation();
	
	public boolean isValid();
	
}
