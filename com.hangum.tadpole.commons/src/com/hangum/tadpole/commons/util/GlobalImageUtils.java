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
public class GlobalImageUtils {
	public static final String IMAGE_Activator_ID = Activator.ID;
	
	/**
	 * checked image
	 * @return
	 */
	public static final Image getCheck() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/check/checked.gif");
	}
	
	/**
	 * unchecked image
	 * @return
	 */
	public static final Image getUnCheck() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/check/unchecked.gif");
	}

	/**
	 * history image
	 * @return
	 */
	public static final Image getHistory() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/history.png");
	}
	
	/**
	 * start image
	 * @return
	 */
	public static final Image getStart() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/start.png");
	}
	
	/**
	 * killing image
	 * 
	 * @return
	 */
	public static final Image getKilling() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/kill_process.png");
	}
	
	/**
	 * stop image
	 * @return
	 */
	public static final Image getStop() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/stop.png");
	}
	
	/**
	 * add tadpole db hub
	 * @return
	 */
	public static final Image getTadpoleIcon() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/Tadpole15-15.png");
	}
	
	/**
	 * add database 
	 * @return
	 */
	public static final Image getAddDatabase() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/add_database.png");
	}
	
	/**
	 * Other information
	 * @return
	 */
	public static final Image getOtherInformation() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/Info-16.png");
	}
	
	/**
	 * Filtering
	 * @return
	 */
	public static final Image getFiltering() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/filled_filter.png");
	}
	
	/**
	 * configuration database
	 * @return
	 */
	public static final Image getConfigurationDatabase() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/configuration_database.png");
	}

	
	/**
	 * refresh img
	 * @return
	 */
	public static final Image getRefresh() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/refresh.png");
	}
	
	/**
	 * add image
	 * @return
	 */
	public static final Image getAdd() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/add.png");
	}
	
	/**
	 * user add image
	 * @return
	 */
	public static final Image getUserAdd() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/user-add-0.56.png");
	}
	
	/**
	 * user add image
	 * @return
	 */
	public static final Image getUserInfo() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/user_info-0.56.png");
	}
	
	/**
	 * user add image
	 * @return
	 */
	public static final Image getUserRemove() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/user-remove-0.56.png.png");
	}
	
	/**
	 * delete image
	 * @return
	 */
	public static Image getSave() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/save.png");
	}

	/**
	 * delete image
	 * @return
	 */
	public static Image getDelete() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/delete.png");
	}

	/**
	 * moidfy image
	 * @return
	 */
	public static Image getModify() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/modify.png");
	}

	/**
	 * export image
	 * @return
	 */
	public static Image getExport() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/export.png");
	}

	/**
	 * import image
	 * @return
	 */
	public static Image getImport() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/import.png");
	}

	public static Image getQueryHistory() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/queryhistory.png");
	}
	
	public static Image getSQLEditor() {
		return ResourceManager.getPluginImage(GlobalImageUtils.IMAGE_Activator_ID, "resources/icons/sql-query.png");
	}
}
