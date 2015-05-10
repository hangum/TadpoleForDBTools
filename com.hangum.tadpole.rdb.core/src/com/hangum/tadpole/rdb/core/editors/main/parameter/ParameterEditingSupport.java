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

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;

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
	private final String[] types;

	public ParameterEditingSupport(TableViewer viewer, int columnIndex, UserDBDAO userDB) {
		super(viewer);

		this.viewer = viewer;
		this.columnIndex = columnIndex;
		this.userDB = userDB;
		this.types = RDBTypeToJavaTypeUtils.supportParameterTypes(userDB);
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		HashMap<Integer, Object> map = (HashMap<Integer, Object>) element;

		if (columnIndex == 2) {
			return new ComboBoxCellEditor(viewer.getTable(), types);
		} else if (columnIndex == 3) {
			if (RDBTypeToJavaTypeUtils.isNumberType((String)map.get(2)) || RDBTypeToJavaTypeUtils.isCharType((String)map.get(2)) ) {
				return new TextCellEditor(viewer.getTable());
			} else {
				return new TextCellEditor(viewer.getTable());
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
		HashMap<Integer, Object> map = (HashMap<Integer, Object>) element;
		if (columnIndex == 2) {
			map.put(2, this.types[(Integer) value]);
		} else if (columnIndex == 3) {
			map.put(3, value);
		}

		viewer.update(element, null);
	}

}
