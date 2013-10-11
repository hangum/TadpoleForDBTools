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
package com.hangum.tadpole.rdb.core.editors.main;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

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
	private static final String[] types = { "String", "Long", "Integer" };

	private final TableViewer viewer;
	private int columnIndex;

	public ParameterEditingSupport(TableViewer viewer, int columnIndex) {
		super(viewer);

		this.viewer = viewer;
		this.columnIndex = columnIndex;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		HashMap<Integer, Object> map = (HashMap<Integer, Object>) element;

		if (columnIndex == 2) {
			return new ComboBoxCellEditor(viewer.getTable(), types);
		} else {
			if (map.get(2).equals("String")) {
				return new TextCellEditor(viewer.getTable());
			} else if (map.get(2).equals("Long")) {
				return new TextCellEditor(viewer.getTable());
			} else if (map.get(2).equals("Integer")) {
				return new TextCellEditor(viewer.getTable());
			} else {
				return new TextCellEditor(viewer.getTable());
			}
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
			if (map.get(2).equals("String")) {
				return 0;
			} else if ("Long".equals(map.get(2))) {
				return 1;
			} else if ("Integer".equals(map.get(2))) {
				return 2;
			} else {
				return 0;
			}

		} else if (columnIndex == 3) {
			return (String) map.get(3);
		}

		return null;
	}

	@Override
	protected void setValue(Object element, Object value) {
		HashMap<Integer, Object> map = (HashMap<Integer, Object>) element;
		if (columnIndex == 2) {
			if (Integer.valueOf(value.toString()) == 0) {
				map.put(2, "String");
			} else if (Integer.valueOf(value.toString()) == 1){
				map.put(2, "Long");
			} else if (Integer.valueOf(value.toString()) == 2){
				map.put(2, "Integer");
			}
		} else if (columnIndex == 3) {
			map.put(3, value);
		}

		viewer.update(element, null);
	}

}
