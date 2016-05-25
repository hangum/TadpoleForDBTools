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
package com.hangum.tadpole.rdb.core.viewers.connections;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.security.TadpoleSecurityManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.extensionpoint.handler.ConnectionDecorationContributionsHandler;
import com.swtdesigner.ResourceManager;

/**
 *  db icons utils
 * 
 * @author hangum
 *
 */
public class DBIconsUtils {
	private static final Logger logger = Logger.getLogger(DBIconsUtils.class);
	
	/**
	 * Get db iamge url
	 * 
	 * @param userDB
	 * @return
	 */
	public static String getDBImageUrl(UserDBDAO userDB) {
		String strBaseImage = "";
		
		DBDefine dbType = userDB.getDBDefine();
		if(DBDefine.MYSQL_DEFAULT == dbType) 		strBaseImage = "mysql-add.png";
		else if(DBDefine.MARIADB_DEFAULT == dbType) strBaseImage = "mariadb-add.png";
		else if(DBDefine.ORACLE_DEFAULT == dbType) 	strBaseImage = "oracle-add.png";
		else if(DBDefine.SQLite_DEFAULT == dbType) 	strBaseImage = "sqlite-add.png";
		else if(DBDefine.MSSQL_DEFAULT == dbType || DBDefine.MSSQL_8_LE_DEFAULT == dbType) 	strBaseImage = "mssql-add.png";
		else if(DBDefine.CUBRID_DEFAULT == dbType) 	strBaseImage = "cubrid-add.png";
		else if(DBDefine.POSTGRE_DEFAULT == dbType) strBaseImage = "postgresSQL-add.png";
		else if(DBDefine.MONGODB_DEFAULT == dbType) strBaseImage = "mongodb-add.png";
		else if(DBDefine.HIVE_DEFAULT == dbType || DBDefine.HIVE2_DEFAULT == dbType) strBaseImage = "hive-add.png";
		else if(DBDefine.TAJO_DEFAULT == dbType) strBaseImage = "tajo-add.jpg";
		else if(DBDefine.TIBERO_DEFAULT == dbType) strBaseImage = "tibero_add.png";
		else if(DBDefine.ALTIBASE_DEFAULT == dbType) strBaseImage = "altibase_add.png";
		else  strBaseImage = "database-add.png";
		
		return "resources/icons/dbs/" + strBaseImage;
	}
	
//	/**
//	 * get db image
//	 * 
//	 * @param userDB
//	 * @return
//	 */
//	public static Image getDBNormalImage(UserDBDAO userDB) {
//		return ResourceManager.getPluginImage(Activator.PLUGIN_ID, getDBImageUrl(userDB));
//	}
	
	/**
	 * 	
	 * 		https://bugs.eclipse.org/bugs/show_bug.cgi?id=468362
	 * 
	 * @param userDB
	 * @return
	 */
	public static Image getEditorImage(UserDBDAO userDB) {
		return getPluginImage(Activator.PLUGIN_ID, getDBImageUrl(userDB));
	}
	
	/**
	 * get procedure image
	 * 
	 * @param userDB
	 * @return
	 */
	public static Image getProcedureImage(UserDBDAO userDB) {
		Image baseImage = getEditorImage(userDB);
		// 이미지 캐쉬에 문제가 있어서 주석처리.
//		try {
//			return getDecorateImage(baseImage, "resources/icons/editor/compile_0.28.png", ResourceManager.BOTTOM_RIGHT);
//		} catch(Exception e) {
//			return baseImage;
//		}
		return baseImage;
	}
	
	/**
	 * get db image
	 * 
	 * @param userDB
	 * @return
	 */
	public static Image getDBConnectionImage(UserDBDAO userDB) {
		Image baseImage = getEditorImage(userDB);
		// 이미지 캐쉬에 문제가 있어서 주석처리.
//		try {
//			if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getIs_lock())) {
//				if(!TadpoleSecurityManager.getInstance().isLock(userDB)) {
//					baseImage = getDecorateImage(baseImage, "resources/icons/lock_0.28.png", ResourceManager.BOTTOM_RIGHT);
//				} else {
//					baseImage = getDecorateImage(baseImage, "resources/icons/unlock_0.28.png", ResourceManager.BOTTOM_RIGHT);
//				}
//			}
//		} catch(Exception e) {
//			logger.error("Image decoration", e);
//		}
//		
//		// extension image decoration
//		try {
//			ConnectionDecorationContributionsHandler handler = new ConnectionDecorationContributionsHandler();
//			Image extensionImage = handler.getImage(userDB);
//			if(extensionImage != null) {
//				return ResourceManager.decorateImage(baseImage, extensionImage, ResourceManager.BOTTOM_LEFT);
//			}
//		} catch(Exception e) {
//			logger.error("extension point exception", e);
//		}
		
		return baseImage;
	}
	
//				// 이미지 캐쉬에 문제가 있어서 주석처리.
//	/**
//	 * lock image
//	 * 
//	 * @param baseImage
//	 * @return
//	 */
//	public static Image getDecorateImage(Image baseImage, String strDecorateImage, int conor) throws Exception {
//		return ResourceManager.decorateImage(baseImage, 
//				ResourceManager.getPluginImage(Activator.PLUGIN_ID, strDecorateImage), 
//				conor);
//	}
	
	/**
	 * get plugin image
	 * 
	 * @param pluginId
	 * @param imgURL
	 * @return
	 */
	public static Image getPluginImage(String pluginId, String imgURL) {
		ImageDescriptor descriptor = getPluginImageDescriptor(pluginId, imgURL);
		if(descriptor != null) return descriptor.createImage();
		else return null;
	}
	
	/**
	 * 
	 * @param pluginId
	 * @param imgURL
	 * @return
	 */
	public static ImageDescriptor getPluginImageDescriptor(String pluginId, String imgURL) {
		String imgKey = pluginId + imgURL;
		
		ImageDescriptor descriptor = JFaceResources.getImageRegistry().getDescriptor(imgKey);
		if(descriptor == null) {
//			if(logger.isDebugEnabled()) logger.debug(String.format("==[image][new][key] %s]", imgKey));
			try {
				descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, imgURL);
			    JFaceResources.getImageRegistry().put(pluginId, descriptor);
			} catch(Exception e) {
				logger.error("create image exception", e);
			}
		    return descriptor;
		} else {
			if(logger.isDebugEnabled()) logger.debug(String.format("==[image][registery] %s]", imgKey));
			
			return descriptor;
		}
	}
}
