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
package com.hangum.tadpole.rdb.core.extensionpoint.definition;

import org.eclipse.swt.SWT;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;

/**
 * MainEditor extension
 * 
 * @author hangum
 *
 */
public abstract class AMainEditorExtension implements IMainEditorExtension {
	
	/** 올챙이 메인 에디터 */
	protected MainEditor mainEditor;

	/**
	 * 이 익스텐션을 동작가능 한지?
	 */
	public boolean enableExtension = false;
	
	/**
	 * 현재 보여지고 있는 데이터베이스.
	 */
	public UserDBDAO editorUserDB = null;
	
	/**
	 * 메인 에디터에서 UI가 위치할 위치 지정.
	 * 
	 *  SWT.LEFT
	 *	SWT.RIGHT
	 *	SWT.TOP
	 *	SWT.BOTTOM
	 */
	public int location = SWT.RIGHT;
	
	/**
	 * 화면을 초기화 합니다.
	 * 1. 초기 화면이 보여야 하는지 설정합니다. 
	 */
	public void initExtension(UserDBDAO userDB) {
		this.editorUserDB = userDB;
		this.enableExtension = true;
	}
	
	/**
	 * 현재 에디터에서 사용하고 있는 extension
	 * @return
	 */
	public UserDBDAO getEditorUserDB() {
		return editorUserDB;
	}

	/**
	 * extension을 활성화 할것인지?
	 * 
	 * @return the enableExtension
	 */
	public boolean isEnableExtension() {
		return enableExtension;
	}

	/**
	 * @param enableExtension the enableExtension to set
	 */
	public void setEnableExtension(boolean isEnable) {
		this.enableExtension = isEnable;
	}

	/**
	 * @return the location
	 */
	public int getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(int location) {
		this.location = location;
	}
	
}
