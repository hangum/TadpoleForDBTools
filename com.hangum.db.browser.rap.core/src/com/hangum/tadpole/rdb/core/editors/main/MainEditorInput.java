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
package com.hangum.tadpole.rdb.core.editors.main;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.rdb.core.util.QueryTemplateUtils;
import com.hangum.tadpole.system.TadpoleSystem_UserDBResource;

/**
 * main editor의 input
 * 
 * @author hangumNote
 *
 */
public class MainEditorInput implements IEditorInput {
	private static final Logger logger = Logger.getLogger(MainEditorInput.class);
	
	/** 에디터의 오픈 타입 정의 */
	private Define.EDITOR_OPEN_TYPE OPEN_TYPE = Define.EDITOR_OPEN_TYPE.NONE;
	
	private UserDBDAO userDB;
	private Define.DB_ACTION action;
	private String defaultStr = ""; //$NON-NLS-1$
	private UserDBResourceDAO resourceDAO;
	
	
	/**
	 * query창에을 공백으로
	 * 
	 * @param userDB
	 */
	public MainEditorInput(UserDBDAO userDB) {
		this.userDB = userDB;
		
		this.OPEN_TYPE = Define.EDITOR_OPEN_TYPE.NONE;
	}
	
	/**
	 * query창에 기본텍스트 출력된 체로
	 * 
	 * @param userDB
	 * @param defaultStr
	 */
	public MainEditorInput(UserDBDAO userDB, String defaultStr) {
		this.userDB = userDB;
		this.defaultStr = defaultStr;
		
		this.OPEN_TYPE = Define.EDITOR_OPEN_TYPE.STRING;
	}
	
	/**
	 * 기존 리소스 호출
	 * @param userDB
	 * @param dao
	 */
	public MainEditorInput(UserDBResourceDAO dao) throws Exception {
		this.userDB = dao.getParent();
		this.resourceDAO = dao;
		
		this.OPEN_TYPE = Define.EDITOR_OPEN_TYPE.FILE;
		
		this.defaultStr = TadpoleSystem_UserDBResource.getResourceData(dao);
	}
	
	/**
	 * query창에 action 타입에 따른 기본 텍스트 출력
	 * @param userDB
	 * @param action
	 */
	public MainEditorInput(UserDBDAO userDB, Define.DB_ACTION initAction) {
		this.userDB = userDB;
		this.action = initAction;
		this.defaultStr = QueryTemplateUtils.getQuery(userDB, initAction);
		
		this.OPEN_TYPE = Define.EDITOR_OPEN_TYPE.STRING;
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
		return null;
	}

	@Override
	public String getName() {
		return userDB.getDisplay_name();//User() + "@" + userDB.getDatabase();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return userDB.getDisplay_name();// + "@" + userDB.getDatabase();
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}

	public String getDefaultStr() {
		return defaultStr;
	}
	
	public Define.EDITOR_OPEN_TYPE getOPEN_TYPE() {
		return OPEN_TYPE;
	}

	public UserDBResourceDAO getResourceDAO() {
		return resourceDAO;
	}
}
