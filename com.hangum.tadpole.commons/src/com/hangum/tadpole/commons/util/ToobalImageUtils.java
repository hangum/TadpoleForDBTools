/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.util;

import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.commons.Activator;
import com.swtdesigner.ResourceManager;

/**
 * Common Images utils
 * 
 * @author hangum
 *
 */
public class ToobalImageUtils {
	public static final String IMAGE_Activator_ID = Activator.ID;
	
	/**
	 * add database 
	 * @return
	 */
	public static final Image getAddDatabase() {
		return ResourceManager.getPluginImage(ToobalImageUtils.IMAGE_Activator_ID, "resources/icons/add_database.png");
	}
	
	/**
	 * Other information
	 * @return
	 */
	public static final Image getOtherInformation() {
		return ResourceManager.getPluginImage(ToobalImageUtils.IMAGE_Activator_ID, "resources/icons/Info-16.png");
	}
	
	/**
	 * Filtering
	 * @return
	 */
	public static final Image getFiltering() {
		return ResourceManager.getPluginImage(ToobalImageUtils.IMAGE_Activator_ID, "resources/icons/filled_filter.png");
	}
	
	/**
	 * configuration database
	 * @return
	 */
	public static final Image getConfigurationDatabase() {
		return ResourceManager.getPluginImage(ToobalImageUtils.IMAGE_Activator_ID, "resources/icons/configuration_database.png");
	}

	
	/**
	 * refresh img
	 * @return
	 */
	public static final Image getRefresh() {
		return ResourceManager.getPluginImage(ToobalImageUtils.IMAGE_Activator_ID, "resources/icons/refresh.png");
	}
	
	/**
	 * add image
	 * @return
	 */
	public static final Image getAdd() {
		return ResourceManager.getPluginImage(ToobalImageUtils.IMAGE_Activator_ID, "resources/icons/add.png");
	}
	
	/**
	 * user add image
	 * @return
	 */
	public static final Image getUserAdd() {
		return ResourceManager.getPluginImage(ToobalImageUtils.IMAGE_Activator_ID, "resources/icons/user-add-0.56.png");
	}
	
	/**
	 * delete image
	 * @return
	 */
	public static Image getSave() {
		return ResourceManager.getPluginImage(ToobalImageUtils.IMAGE_Activator_ID, "resources/icons/save.png");
	}

	/**
	 * delete image
	 * @return
	 */
	public static Image getDelete() {
		return ResourceManager.getPluginImage(ToobalImageUtils.IMAGE_Activator_ID, "resources/icons/delete.png");
	}

	/**
	 * moidfy image
	 * @return
	 */
	public static Image getModify() {
		return ResourceManager.getPluginImage(ToobalImageUtils.IMAGE_Activator_ID, "resources/icons/modify.png");
	}

	/**
	 * export image
	 * @return
	 */
	public static Image getExport() {
		return ResourceManager.getPluginImage(ToobalImageUtils.IMAGE_Activator_ID, "resources/icons/export.png");
	}

	/**
	 * import image
	 * @return
	 */
	public static Image getImport() {
		return ResourceManager.getPluginImage(ToobalImageUtils.IMAGE_Activator_ID, "resources/icons/import.png");
	}

	public static Image getQueryHistory() {
		return ResourceManager.getPluginImage(ToobalImageUtils.IMAGE_Activator_ID, "resources/icons/queryhistory.png");
	}
	
	public static Image getSQLEditor() {
		return ResourceManager.getPluginImage(ToobalImageUtils.IMAGE_Activator_ID, "resources/icons/sql-query.png");
	}
}
