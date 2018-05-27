/*******************************************************************************
 * Copyright (c) 2018 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.elasticsearch.core.editos.main;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * Elasticsearch editor input
 * 
 * @author hangum
 *
 */
public class ElasticsearchEditorInput implements IEditorInput {
	private UserDBDAO userDB;
	private UserDBResourceDAO resourceDAO;
	
	/** 에디터의 오픈 타입 정의 */
	private PublicTadpoleDefine.EDITOR_OPEN_TYPE OPEN_TYPE = PublicTadpoleDefine.EDITOR_OPEN_TYPE.NONE;
	private String defaultStr = ""; //$NON-NLS-1$

	public ElasticsearchEditorInput(UserDBDAO userDB) {
		this.userDB = userDB;
	}
	
	public ElasticsearchEditorInput(UserDBDAO userDB, String lowSQL) {
		this.userDB = userDB;
		
		this.OPEN_TYPE = PublicTadpoleDefine.EDITOR_OPEN_TYPE.STRING;
		this.defaultStr = lowSQL;
	}
	
	/**
	 * 기존 리소스 호출
	 * @param userDB
	 * @param dao
	 */
	public ElasticsearchEditorInput(UserDBResourceDAO dao) throws Exception {
		this.userDB = dao.getParent();
		this.resourceDAO = dao;
		
		this.OPEN_TYPE = PublicTadpoleDefine.EDITOR_OPEN_TYPE.FILE;
		this.defaultStr = TadpoleSystem_UserDBResource.getResourceData(dao);
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
	}

	@Override
	public String getName() {
		if(PublicTadpoleDefine.DBOperationType.PRODUCTION.toString().equals(userDB.getOperation_type())) {
			return String.format("[%s]%s", StringUtils.substring(userDB.getOperation_type(), 0, 1), userDB.getDisplay_name());
		}  else {
			return userDB.getDisplay_name();
		}
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {

		if(PermissionChecker.isShow(userDB.getRole_id())) {
			return String.format(userDB.getDbms_type() + " - %s:%s", userDB.getHost(), userDB.getUsers());
		}
		
		return userDB.getDisplay_name();
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	public UserDBResourceDAO getResourceDAO() {
		return resourceDAO;
	}
	
	public String getDefaultStr() {
		return defaultStr;
	}
}
