package com.hangum.tadpole.engine.utils;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ApplicationContext;

public class LicenseValidator {
	
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
}
