/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dml.IndexInformationDialog;
import com.hangum.tadpole.rdb.core.dialog.dml.SelectObjectDialog;
import com.hangum.tadpole.rdb.core.dialog.dml.TableInformationDialog;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;

/**
 * dialog util
 * 
 * @author hangum
 *
 */
public class DialogUtil {
	private static final Logger logger = Logger.getLogger(DialogUtil.class);

	/**
	 * dialog util
	 * 
	 * @param userDB
	 * @param strObject
	 */
	public static void popupObjectInformationDialog(UserDBDAO userDB, Map<String, String> paramMap) {
		//TODO: 디비엔진 종류별로 지원유무에 따라 처리해야 하나?
		
		SelectObjectDialog objectSelector = new SelectObjectDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), userDB, paramMap);

		if (objectSelector.getObjectCount() > 1) {
			//이름으로 검색한 결과가 1개이상이면 선택화면을 띄운다.
			objectSelector.open();
		} else if (objectSelector.getObjectCount() <= 0 || objectSelector.getSelectObject().isEmpty()) {
			//해당 오브젝트를 찾을 수 없습니다.
			MessageDialog.openInformation(null , Messages.get().Information, Messages.get().NotFountObject);
			return;
		}
		Map<String, String> map = objectSelector.getSelectObject();

		if (StringUtils.contains("TABLE,VIEW", map.get("OBJECT_TYPE"))) {

			TableDAO tableDao = new TableDAO();
			tableDao.setSchema_name(map.get("OBJECT_OWNER"));
			
			tableDao.setSysName(map.get("OBJECT_NAME"));
			tableDao.setTab_name(map.get("OBJECT_NAME"));
			tableDao.setTable_name(map.get("OBJECT_NAME"));

			popupTableInformationDialog(userDB, tableDao);
		} else if (StringUtils.contains("INDEX,CONSTRAINTS", map.get("OBJECT_TYPE"))) {

			InformationSchemaDAO indexDao = new InformationSchemaDAO();
			indexDao.setSchema_name(map.get("OBJECT_OWNER"));
			indexDao.setTABLE_SCHEMA(map.get("OBJECT_OWNER"));
			indexDao.setINDEX_NAME(map.get("OBJECT_NAME"));

			popupIndexInformationDialog(userDB, indexDao);
		} else if (!StringUtils.isEmpty( map.get("OBJECT_TYPE"))){

			MessageDialog.openInformation(null , Messages.get().Information, Messages.get().DoNotSupportObject);
		} else if (StringUtils.isEmpty( map.get("OBJECT_TYPE"))){

			MessageDialog.openInformation(null , Messages.get().Information, Messages.get().NotFountObject);
		}

	}

	public static void popupTableInformationDialog(UserDBDAO userDB, TableDAO paramTableDAO) {
		try {
			TableDAO tableDao = null;
			List<TableDAO> listTable = userDB.getListTable();
			if (listTable.isEmpty()) {
				if (DBDefine.POSTGRE_DEFAULT != userDB.getDBDefine()) {
					tableDao = TadpoleObjectQuery.getTable(userDB, paramTableDAO);
				} else {
					tableDao = new TableDAO(paramTableDAO.getName(), "");
				}
			} else {
				//TODO: F4상세정보 조회에서도 테이블필터에 의해서 조회가 제한된 테이블인 경우는 조회하지 못하게 해야 하는가?
				// 스키마가 다른경우는 예외로? 
				for (TableDAO tmpTableDAO : listTable) {
					if (StringUtils.equalsIgnoreCase(paramTableDAO.getName(), tmpTableDAO.getName()) && StringUtils.equalsIgnoreCase(paramTableDAO.getSchema_name(), tmpTableDAO.getSchema_name())) {
						tableDao = tmpTableDAO;
						break;
					}
				}
			}

			if (tableDao == null && (!StringUtils.equalsIgnoreCase(userDB.getSchema(), paramTableDAO.getSchema_name()) | StringUtils.isEmpty(paramTableDAO.getSchema_name()))) {
				// 현재 스키마와 다른경우
				TableInformationDialog dialog = new TableInformationDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), false, userDB, paramTableDAO);
				dialog.open();
			} else {
				TableInformationDialog dialog = new TableInformationDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), false, userDB, tableDao);
				dialog.open();
			}

		} catch (Exception e) {
			logger.error("f4 function", e);
		}
	}

	public static void popupIndexInformationDialog(UserDBDAO userDB, InformationSchemaDAO paramDAO) {
		try {

			IndexInformationDialog dialog = new IndexInformationDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), false, userDB, paramDAO);
			dialog.open();

		} catch (Exception e) {
			logger.error("f4 function", e);
		}
	}

}
