/*******************************************************************************
 * Copyright (c) 2017 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main.composite.direct;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.engine.sql.util.resultset.ResultSetUtilDTO;
import com.hangum.tadpole.engine.utils.EditorDefine;
import com.hangum.tadpole.engine.utils.EditorDefine.QUERY_MODE;
import com.swtdesigner.SWTResourceManager;

/**
 * Ledger SQLResult의 LabelProvider
 * 
 * @author hangum
 *
 */
public class LedgerSQLResultLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(LedgerSQLResultLabelProvider.class);
	public static final String STR_NEW_LABEL_COLUMN = "_NEW_";
	private EditorDefine.QUERY_MODE queryMode = QUERY_MODE.QUERY;
	private ResultSetUtilDTO rsDAO;
	private Map<Integer, Integer> _showColumnIndex;
	
	public LedgerSQLResultLabelProvider() {
	}

	public LedgerSQLResultLabelProvider(EditorDefine.QUERY_MODE queryMode, final ResultSetUtilDTO rsDAO, Map<Integer, Integer> _showColumnIndex) {
		this.queryMode = queryMode;
		this.rsDAO = rsDAO;
		this._showColumnIndex = _showColumnIndex;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		int realColumnIndex = _showColumnIndex.get(columnIndex);
		String strColumnName = rsDAO.getColumnLabelName().get(realColumnIndex);
		if(StringUtils.startsWithIgnoreCase(strColumnName, STR_NEW_LABEL_COLUMN)) {
			return SWTResourceManager.getColor(SWT.COLOR_GRAY);
		}
		
		return null;
	}
	
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
	

	@SuppressWarnings("unchecked")
	public String getColumnText(Object element, int columnIndex) {
		HashMap<Integer, Object> rsResult = (HashMap<Integer, Object>)element;
		int realColumnIndex = _showColumnIndex.get(columnIndex);
		Object obj = rsResult.get(realColumnIndex);
		if(obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}

}
