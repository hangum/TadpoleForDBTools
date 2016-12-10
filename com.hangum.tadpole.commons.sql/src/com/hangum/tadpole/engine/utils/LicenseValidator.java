package com.hangum.tadpole.engine.utils;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ApplicationContext;

/**
 * Get user license info
 * 
 * @author hangum
 *
 */
public class LicenseValidator {
	private static final Logger logger = Logger.getLogger(LicenseValidator.class);
	
	/**
	 * license validation
	 * @return
	 */
	public static LicenseDAO getLicense() {
		ApplicationContext context = RWT.getApplicationContext();
		Object objLicense = context.getAttribute("LicenseValidation");
		if(objLicense == null) {
			return new LicenseDAO();
		} else {
			return (LicenseDAO)objLicense;
		}
	}
	
	/**
	 * get customer info
	 * @return
	 */
	public static String getCustomerInfo() {
		try {
			LicenseDAO licenseDAO = getLicense();
			if(!"".equals(licenseDAO.getCustomer())) {
				return "Enterprise Ver. " + licenseDAO.getCustomer();
			}
		} catch(Exception e) {
			logger.error("get licenseinfo", e);
		}
		
		return "License is GNU Lesser General Public License v.3";
	}
}
