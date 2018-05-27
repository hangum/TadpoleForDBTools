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
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.dialogs.MessageDialog;

import com.hangum.tadpole.commons.libs.core.message.CommonMessages;

/**
 * License extension handler
 * 
 * @author hangum
 *
 */
public class LicenseExtensionHandler {
	private static final Logger logger = Logger.getLogger(LicenseExtensionHandler.class);
	private static final String LICENSE_EXTENSION_ID = "com.hangum.tadpole.engine.license.extension";

	/**
	 * initialize license
	 * 
	 * @return
	 */
	public ILicenseExtension[] license(final File file) {
		final LinkedList listReturn = new LinkedList();
		
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(LICENSE_EXTENSION_ID);
		try {
			for (IConfigurationElement e : config) {
				final Object licenseExtension = e.createExecutableExtension("class");
				if (licenseExtension instanceof ILicenseExtension) {
					ISafeRunnable runnable = new ISafeRunnable() {

						@Override
						public void handleException(Throwable exception) {
							logger.error("Exception license extension", exception);
						}

						@Override
						public void run() throws Exception {
							ILicenseExtension compositeExt = (ILicenseExtension) licenseExtension;
							compositeExt.initExtension(file);
						}
					};
					SafeRunner.run(runnable);
				}
			}
		} catch (CoreException ex) {
			logger.error("Create License extension exception", ex);
		}
		
		return (ILicenseExtension[]) listReturn.toArray(new ILicenseExtension[listReturn.size()]);
	}
	
	/**
	 * initialize license
	 * 
	 * @return
	 */
	public ILicenseExtension[] license(final String strLicense) {
		final LinkedList listReturn = new LinkedList();
		
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(LICENSE_EXTENSION_ID);
		try {
			for (IConfigurationElement e : config) {
				final Object licenseExtension = e.createExecutableExtension("class");
				if (licenseExtension instanceof ILicenseExtension) {
					ISafeRunnable runnable = new ISafeRunnable() {

						@Override
						public void handleException(Throwable exception) {
							logger.error("Exception license extension", exception);
						}

						@Override
						public void run() throws Exception {
							ILicenseExtension compositeExt = (ILicenseExtension) licenseExtension;
							compositeExt.initExtension(strLicense);
						}
					};
					SafeRunner.run(runnable);
				}
			}
		} catch (CoreException ex) {
			logger.error("Create License extension exception", ex);
		}
		
		return (ILicenseExtension[]) listReturn.toArray(new ILicenseExtension[listReturn.size()]);
	}
	
	/**
	 * initialize license
	 * 
	 * @return
	 */
	public ILicenseExtension[] liveChange(final String strLicense) {
		final LinkedList listReturn = new LinkedList();
		
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(LICENSE_EXTENSION_ID);
		try {
			for (IConfigurationElement e : config) {
				final Object licenseExtension = e.createExecutableExtension("class");
				if (licenseExtension instanceof ILicenseExtension) {
					ISafeRunnable runnable = new ISafeRunnable() {

						@Override
						public void handleException(Throwable exception) {
							logger.error("Exception license extension", exception);
						}

						@Override
						public void run() throws Exception {
							ILicenseExtension compositeExt = (ILicenseExtension) licenseExtension;
							compositeExt.liveChage(strLicense);
							
							MessageDialog.openInformation(null, CommonMessages.get().Confirm, CommonMessages.get().ChangingValue);
						}
					};
					SafeRunner.run(runnable);
				}
			}
		} catch (CoreException ex) {
			logger.error("license chage extension exception", ex);
		}
		
		return (ILicenseExtension[]) listReturn.toArray(new ILicenseExtension[listReturn.size()]);
	}
}
