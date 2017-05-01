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
package com.hangum.tadpole.rdb.core.editors.main.parameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.CommentCellEditor;

/**
 * SampleDAtaEditingSupport
 * 
 * @author hangum
 */
public class ParameterEditingSupport extends EditingSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6917152389392816571L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ParameterEditingSupport.class);

	private final TableViewer viewer;
	private int columnIndex;
	private UserDBDAO userDB;

	/** 입력 파라미터 */
	private List<Map<Integer, Object>> parameters = null;
	
	private final String[] types;

	public ParameterEditingSupport(TableViewer viewer, int columnIndex, UserDBDAO userDB, List<Map<Integer, Object>> parameters) {
		super(viewer);

		this.viewer = viewer;
		this.columnIndex = columnIndex;
		this.userDB = userDB;
		this.types = RDBTypeToJavaTypeUtils.supportParameterTypes(userDB);
		this.parameters = parameters;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		HashMap<Integer, Object> map = (HashMap<Integer, Object>) element;

		if (columnIndex == 2) {
			return new KeyEventComboBoxCellEditor(columnIndex, viewer, types);
		} else if (columnIndex == 3) {
			if (RDBTypeToJavaTypeUtils.isNumberType((String)map.get(2)) || RDBTypeToJavaTypeUtils.isCharType((String)map.get(2)) ) {
				return new CommentCellEditor(columnIndex, viewer);
			} else {
				return new CommentCellEditor(columnIndex, viewer);
			}
		}else{
			return null;
		}
	}

	@Override
	protected boolean canEdit(Object element) {
		if (columnIndex == 2 || columnIndex == 3) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected Object getValue(Object element) {
		HashMap<Integer, Object> map = (HashMap<Integer, Object>) element;

		if (columnIndex == 2) {
			return RDBTypeToJavaTypeUtils.getIndex(userDB, (String) map.get(2));
		} else if (columnIndex == 3) {
			return map.get(3);
		}

		return null;
	}

	@Override
	protected void setValue(Object element, Object value) {
		HashMap<Integer, Object> mapChangeElement = (HashMap<Integer, Object>) element;
		if (columnIndex == 2) {
			mapChangeElement.put(2, this.types[(Integer) value]);
			_chageNameChage(mapChangeElement, 2, this.types[(Integer) value]);
		} else if (columnIndex == 3) {
			mapChangeElement.put(3, value);
			_chageNameChage(mapChangeElement, 3, value);
		}

//		viewer.update(element, null);
	}
	
	/**
	 * 파라미터 이름이 같은 경우 모든 이름을 바꾸어준다.
	 * 
	 * @param mapChangeElement
	 * @param changeObject
	 */
	private void _chageNameChage(HashMap<Integer, Object> mapChangeElement, int intIndex, Object changeObject) {
		boolean isUpdate = false;
		
		List<Map<Integer, Object>> parameters = (List<Map<Integer, Object>>)viewer.getInput();
		String strOriKey = ""+mapChangeElement.get(1);
		for(Map<Integer, Object> mapOld : parameters) {
			String _tmpKey = ""+mapOld.get(1);
			if(strOriKey.equals(_tmpKey)) {
				mapOld.put(intIndex, changeObject);
				
//				viewer.update(mapOld, null);
				isUpdate = true;
			}
		}
		
		if(isUpdate) viewer.refresh(parameters, true);
		logger.debug("======================================================================================================================================================");
		logger.debug("========================> " + parameters.size());
		logger.debug("======================================================================================================================================================");
	}

}
