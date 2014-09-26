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
public class ImageUtils {
	public static final String IMAGE_Activator_ID = Activator.ID;
	
	/**
	 * refresh img
	 * @return
	 */
	public static final Image getRefresh() {
		return ResourceManager.getPluginImage(ImageUtils.IMAGE_Activator_ID, "resources/icons/refresh.png");
	}
	
	/**
	 * add image
	 * @return
	 */
	public static final Image getAdd() {
		return ResourceManager.getPluginImage(ImageUtils.IMAGE_Activator_ID, "resources/icons/add.png");
	}
	
	/**
	 * delete image
	 * @return
	 */
	public static Image getSave() {
		return ResourceManager.getPluginImage(ImageUtils.IMAGE_Activator_ID, "resources/icons/save.png");
	}

	/**
	 * delete image
	 * @return
	 */
	public static Image getDelete() {
		return ResourceManager.getPluginImage(ImageUtils.IMAGE_Activator_ID, "resources/icons/delete.png");
	}

	/**
	 * moidfy image
	 * @return
	 */
	public static Image getModify() {
		return ResourceManager.getPluginImage(ImageUtils.IMAGE_Activator_ID, "resources/icons/modify.png");
	}

	/**
	 * export image
	 * @return
	 */
	public static Image getExport() {
		return ResourceManager.getPluginImage(ImageUtils.IMAGE_Activator_ID, "resources/icons/export.png");
	}

	/**
	 * import image
	 * @return
	 */
	public static Image getImport() {
		return ResourceManager.getPluginImage(ImageUtils.IMAGE_Activator_ID, "resources/icons/import.png");
	}

	public static Image getQueryHistory() {
		return ResourceManager.getPluginImage(ImageUtils.IMAGE_Activator_ID, "resources/icons/queryhistory.png");
	}
	
	public static Image getSQLEditor() {
		return ResourceManager.getPluginImage(ImageUtils.IMAGE_Activator_ID, "resources/icons/sql-query.png");
	}
}
