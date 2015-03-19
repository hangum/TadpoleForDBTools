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

import java.util.Map;

import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;

/**
 * MainEditor extension
 * 
 * @author hangum
 *
 */
public interface IMainEditorExtension {
	
	/**
	 * user create part control
	 * 
	 * @param parent    사용자 컴포넌트를 그릴 위젲.
	 * @param mainEditor 메인 에디터 위젲.
	 */
	public void createPartControl(Composite parent, MainEditor mainEditor);
	
	/**
	 * 화면을 초기화 합니다.
	 * 1. 초기 화면이 보여야 하는지 설정합니다. 
	 */
	public void initExtension(UserDBDAO userDB);
	
	/**
	 * 확장 에디터가 조작하고 싶은 쿼리로 조절한다.
	 * 
	 * @param strSQL
	 * @return
	 */
	public String sqlCostume(String strSQL);
	
	/**
	 * result set one click
	 * 
	 * @param selectIndex  select index
	 * @param mapColumns column <index, value>
	 */
	public void resultSetClick(int selectIndex, Map<Integer, Object> mapColumns);
	
	/**
	 * resultSetDoubleClick
	 * 
	 * @param selectIndex  select index
	 * @param mapColumns column <index, value>
	 */
	public void resultSetDoubleClick(int selectIndex, Map<Integer, Object> mapColumns);
	
	/**
	 * 쿼리 실행이 끝나고 확장하는 포인터에서 실행해 줘야 하는 경우에.
	 * 
	 * 즉, 사용자가 메인 에디터에서 쿼리 실행을 끝나면 실행 됩니다.
	 * 
	 * @param rsDAO
	 */
	public void queryEndedExecute(QueryExecuteResultDTO rsDAO);

	/**
	 * 현재 에디터에서 사용하고 있는 extension
	 * @return
	 */
	public UserDBDAO getEditorUserDB();

	/**
	 * extension을 활성화 할것인지?
	 * 
	 * @return the enableExtension
	 */
	public boolean isEnableExtension();

	/**
	 * @param enableExtension the enableExtension to set
	 */
	public void setEnableExtension(boolean enableExtension);

	/**
	 * @return the location
	 */
	public int getLocation();

	/**
	 * @param location the location to set
	 */
	public void setLocation(int location);
	
}
