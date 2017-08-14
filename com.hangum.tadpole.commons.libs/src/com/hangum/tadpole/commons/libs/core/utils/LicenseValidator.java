package com.hangum.tadpole.commons.libs.core.utils;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ApplicationContext;

import com.hangum.tadpole.commons.libs.core.dao.LicenseDAO;

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
				return licenseDAO.getCustomer();
			}
		} catch(Exception e) {
			logger.error("get licenseinfo", e);
		}
		
		return "License is GNU Lesser General Public License v.3";
	}
	
	public static String getTerm() {
		LicenseDAO licenseDAO = LicenseValidator.getLicense();
		return licenseDAO.getTerm();
	}
	
	/**
	 * 엔터프라이즈 여부
	 * @return
	 */
	public static boolean isEnterprise() {
		LicenseDAO licenseDAO = LicenseValidator.getLicense();
		return licenseDAO.isEnterprise();
	}
}
