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
package com.hangum.tadpole.monitoring.core.editors.monitoring.manage;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.ManagerListDTO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.session.manager.SessionManager;
import com.swtdesigner.ResourceManager;

/**
 * manager view label provider
 * 
 * @author hangum
 *
 */
public class UserDBLabelProvider extends LabelProvider {
	public static final String PLUGIN_ID = "com.hangum.tadpole.rdb.core"; //$NON-NLS-1$
	
	/** production markup start tag */
	public static String PRODUCTION_SERVER_START_TAG = "<em style='color:rgb(255, 0, 0)'>"; //$NON-NLS-1$
	/** development markup start tag */
	public static String DEVELOPMENT_SERVER_START_TAG = "<em style='color:rgb(224, 224, 224)'>"; //$NON-NLS-1$
	
	/** Markup end tag */
	public static String END_TAG = "</em>"; //$NON-NLS-1$
	
	@Override
	public Image getImage(Object element) {

		if(element instanceof ManagerListDTO) {
			
			return ResourceManager.getPluginImage(PLUGIN_ID, "resources/icons/server_database.png"); //$NON-NLS-1$
			
		} else if(element instanceof UserDBDAO) {
			
			UserDBDAO dto = (UserDBDAO)element;
			DBDefine dbType = dto.getDBDefine();
			
			if(DBDefine.MYSQL_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(PLUGIN_ID, "resources/icons/mysql-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.MARIADB_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(PLUGIN_ID, "resources/icons/mariadb-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.ORACLE_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(PLUGIN_ID, "resources/icons/oracle-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.SQLite_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(PLUGIN_ID, "resources/icons/sqlite-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.MSSQL_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(PLUGIN_ID, "resources/icons/mssql-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.CUBRID_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(PLUGIN_ID, "resources/icons/cubrid-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.POSTGRE_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(PLUGIN_ID, "resources/icons/postgresSQL-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.MONGODB_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(PLUGIN_ID, "resources/icons/mongodb-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.HIVE_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(PLUGIN_ID, "resources/icons/hive-add.png"); //$NON-NLS-1$
			
			else
				return ResourceManager.getPluginImage(PLUGIN_ID, "resources/icons/database-add.png"); //$NON-NLS-1$
			
		} else if(element instanceof UserDBResourceDAO) {
			UserDBResourceDAO dao = (UserDBResourceDAO)element;
			if(PublicTadpoleDefine.RESOURCE_TYPE.ERD.toString().equals( dao.getResource_types())) {
				return ResourceManager.getPluginImage(PLUGIN_ID, "resources/icons/erd.png"); //$NON-NLS-1$
			} else {
				return ResourceManager.getPluginImage(PLUGIN_ID, "resources/icons/sql-query.png"); //$NON-NLS-1$
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
			UserDBDAO dao = (UserDBDAO)element;
			
			String retText = "";
			if(PublicTadpoleDefine.DBOperationType.PRODUCTION.toString().equals(dao.getOperation_type())) {
				retText = PRODUCTION_SERVER_START_TAG + "[" + StringUtils.substring(dao.getOperation_type(), 0, 1) + "] " + END_TAG;
			} else {
				retText = DEVELOPMENT_SERVER_START_TAG + "[" + StringUtils.substring(dao.getOperation_type(), 0, 1) + "] " + END_TAG;
			}
			
			// 자신의 디비만 보이도록 수정
			if(dao.getUser_seq() == SessionManager.getUserSeq()) {
				retText += dao.getDisplay_name() + " (" + dao.getUsers() + "@" + dao.getDb() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} else {
				retText += dao.getDisplay_name(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			
			return retText;
		} else if(element instanceof UserDBResourceDAO) {
			UserDBResourceDAO dao = (UserDBResourceDAO)element;
			String strComment = "".equals(dao.getDescription())?"":" (" + dao.getDescription() + ")";
			
			return "[" + dao.getShared_type() + "] " + dao.getName() + strComment;
		}
		
		return "## not set ##"; //$NON-NLS-1$
	}
}
