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
package com.hangum.tadpole.rdb.core.viewers.connections;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.ManagerListDTO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.security.TadpoleSecurityManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.extensionpoint.handler.ConnectionDecorationContributionsHandler;
import com.hangum.tadpole.session.manager.SessionManager;
import com.swtdesigner.ResourceManager;

/**
 * manager view label provider
 * 
 * @author hangum
 *
 */
public class ManagerLabelProvider extends LabelProvider {
	private static final Logger logger = Logger.getLogger(ManagerLabelProvider.class);
	
	/** production markup start tag */
	public static String PRODUCTION_SERVER_START_TAG = "<em style='color:rgb(255, 0, 0)'>"; //$NON-NLS-1$
	/** development markup start tag */
	public static String DEVELOPMENT_SERVER_START_TAG = "<em style='color:rgb(224, 224, 224)'>"; //$NON-NLS-1$
	
	/** Markup end tag */
	public static String END_TAG = "</em>"; //$NON-NLS-1$
	
	/**
	 * get group image
	 * 
	 * @return
	 */
	public static Image getGroupImage() {
		return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/server_database.png"); //$NON-NLS-1$
	}
	/**
	 * get db image
	 * 
	 * @param userDB
	 * @return
	 */
	public static Image getDBImage(UserDBDAO userDB) {
		String strBaseImage = "";
		
		DBDefine dbType = DBDefine.getDBDefine(userDB);
		if(DBDefine.MYSQL_DEFAULT == dbType) 		strBaseImage = "resources/icons/mysql-add.png";
		else if(DBDefine.MARIADB_DEFAULT == dbType) strBaseImage = "resources/icons/mariadb-add.png";
		else if(DBDefine.ORACLE_DEFAULT == dbType) 	strBaseImage = "resources/icons/oracle-add.png";
		else if(DBDefine.SQLite_DEFAULT == dbType) 	strBaseImage = "resources/icons/sqlite-add.png";
		else if(DBDefine.MSSQL_DEFAULT == dbType) 	strBaseImage = "resources/icons/oracle-add.png";
		else if(DBDefine.CUBRID_DEFAULT == dbType) 	strBaseImage = "resources/icons/cubrid-add.png";
		else if(DBDefine.POSTGRE_DEFAULT == dbType) strBaseImage = "resources/icons/postgresSQL-add.png";
		else if(DBDefine.MONGODB_DEFAULT == dbType) strBaseImage = "resources/icons/mongodb-add.png";
		else if(DBDefine.HIVE_DEFAULT == dbType || DBDefine.HIVE2_DEFAULT == dbType) strBaseImage = "resources/icons/hive-add.png";
		else if(DBDefine.TAJO_DEFAULT == dbType) strBaseImage = "resources/icons/tajo-add.png";
		else  strBaseImage = "resources/icons/database-add.png";
		
		Image baseImage = ResourceManager.getPluginImage(Activator.PLUGIN_ID, strBaseImage);
		
		try {
			if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDB.getIs_lock())) {
				if(!TadpoleSecurityManager.getInstance().isLock(userDB)) {				
					baseImage = ResourceManager.decorateImage(baseImage, 
													ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/lock_0.28.png"), 
													ResourceManager.BOTTOM_LEFT);
				} else {
					baseImage = ResourceManager.decorateImage(baseImage, 
							ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/unlock_0.28.png"), 
							ResourceManager.BOTTOM_LEFT);
				}
			}
		} catch(Exception e) {
			logger.error("Image decoration", e);
		}
		
		// extension image decoration
		try {
			ConnectionDecorationContributionsHandler handler = new ConnectionDecorationContributionsHandler();
			Image extensionImage = handler.getImage(userDB);
			if(extensionImage != null) {
				return ResourceManager.decorateImage(baseImage, extensionImage, ResourceManager.BOTTOM_RIGHT);
			}
		} catch(Exception e) {
			logger.error("extension point exception", e);
		}
		
		return baseImage;
	}
	
	/**
	 * user label text
	 * 
	 * @param userDB
	 * @return
	 */
	public static String getDBText(UserDBDAO userDB) {
		String retText = "";
		if(PublicTadpoleDefine.DBOperationType.PRODUCTION.toString().equals(userDB.getOperation_type())) {
			retText = PRODUCTION_SERVER_START_TAG + "[" + StringUtils.substring(userDB.getOperation_type(), 0, 3) + "] " + END_TAG;
		} else {
			retText = DEVELOPMENT_SERVER_START_TAG + "[" + StringUtils.substring(userDB.getOperation_type(), 0, 3) + "] " + END_TAG;
		}
		
		// 자신의 디비만 보이도록 수정
		if(userDB.getUser_seq() == SessionManager.getUserSeq()) {
			retText += userDB.getDisplay_name() + " (" + userDB.getUsers() + "@" + userDB.getDb() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} else {
			retText += userDB.getDisplay_name(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		
		return retText;
	}
	
	@Override
	public Image getImage(Object element) {

		if(element instanceof ManagerListDTO) {
			return getGroupImage();

		} else if(element instanceof UserDBDAO) {
			return getDBImage((UserDBDAO)element);			
		
		} else if(element instanceof UserDBResourceDAO) {
			UserDBResourceDAO dao = (UserDBResourceDAO)element;
			if(PublicTadpoleDefine.RESOURCE_TYPE.ERD.toString().equals( dao.getResource_types())) {
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/erd.png"); //$NON-NLS-1$
			} else {
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/sql-query.png"); //$NON-NLS-1$
			}
		}
		
		return super.getImage(element);
	}
	
	@Override
	public String getText(Object element) {
		if(element instanceof ManagerListDTO) {
			ManagerListDTO dto = (ManagerListDTO)element;
			return dto.getName();
			
		} else if(element instanceof UserDBDAO) {
			return getDBText((UserDBDAO)element);
		} else if(element instanceof UserDBResourceDAO) {
			UserDBResourceDAO dao = (UserDBResourceDAO)element;
			String strComment = "".equals(dao.getDescription())?"":" (" + dao.getDescription() + ")";
			
			return "[" + StringUtils.substring(dao.getShared_type(), 0, 3) + "] " + dao.getName() + strComment;
		}
		
		return "## not set ##"; //$NON-NLS-1$
	}
}
