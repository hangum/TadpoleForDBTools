/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object;

import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.rdb.core.Activator;
import com.swtdesigner.ResourceManager;

/**
 * Object explorer을 표시하기 위한 기본 상수를 정의합니다.
 * 
 * @author hangum
 *
 */
public class ObjectExploreDefine {

	public static final Image IMAGE_PRIMARY_KEY = ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/primary_key_column.png"); //$NON-NLS-1$
	public static final Image IMAGE_FOREIGN_KEY = ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/foreign_key_column.png"); //$NON-NLS-1$
	public static final Image IMAGE_MULTI_KEY 	= ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/multi_key_column.png"); //$NON-NLS-1$
	public static final Image IMAGE_COLUMN 		= ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/column.png"); //$NON-NLS-1$


}
