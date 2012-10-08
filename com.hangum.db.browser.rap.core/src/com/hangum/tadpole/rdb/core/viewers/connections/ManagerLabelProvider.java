/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.connections;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.ManagerListDTO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.session.manager.SessionManager;
import com.swtdesigner.ResourceManager;

/**
 * manager view label provider
 * 
 * @author hangum
 *
 */
public class ManagerLabelProvider extends LabelProvider {
	
	@Override
	public Image getImage(Object element) {

		if(element instanceof ManagerListDTO) {
			
			return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/server_database.png"); //$NON-NLS-1$
			
		} else if(element instanceof UserDBDAO) {
			
			UserDBDAO dto = (UserDBDAO)element;
			DBDefine dbType = DBDefine.getDBDefine(dto.getTypes());
			
			if(DBDefine.MYSQL_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/mysql-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.ORACLE_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/oracle-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.SQLite_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/sqlite-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.MSSQL_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/mssql-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.CUBRID_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/cubrid-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.POSTGRE_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/postgresSQL-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.MONGODB_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/mongodb-add.png"); //$NON-NLS-1$
			
		} else if(element instanceof UserDBResourceDAO) {
			UserDBResourceDAO dao = (UserDBResourceDAO)element;
			if(Define.RESOURCE_TYPE.ERD.toString().equals( dao.getTypes() )) {
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
			UserDBDAO dao = (UserDBDAO)element;
			
			// 자신의 디비만 보이도록 수정
			if(dao.getUser_seq() == SessionManager.getSeq()) {
				return dao.getDisplay_name() + " (" + dao.getUsers() + "@" + dao.getDb() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} else {
				return dao.getDisplay_name() + " (not visible)"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		} else if(element instanceof UserDBResourceDAO) {
			UserDBResourceDAO dao = (UserDBResourceDAO)element;
			
			return dao.getFilename();
		}
		
		return "## not set ##"; //$NON-NLS-1$
	}
}
