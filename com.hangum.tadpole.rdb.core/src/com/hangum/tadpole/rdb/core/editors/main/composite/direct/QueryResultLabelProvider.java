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
package com.hangum.tadpole.rdb.core.editors.main.composite.direct;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.hangum.tadpole.commons.util.NumberFormatUtils;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.resultset.ResultSetUtilDTO;
import com.hangum.tadpole.engine.utils.EditorDefine;
import com.hangum.tadpole.engine.utils.EditorDefine.QUERY_MODE;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.swtdesigner.SWTResourceManager;

/**
 * SQLResult의 LabelProvider
 * 
 * @author hangum
 *
 */
public class QueryResultLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(QueryResultLabelProvider.class);
	
	private EditorDefine.QUERY_MODE queryMode = QUERY_MODE.QUERY;
	private ResultSetUtilDTO rsDAO;
	
	public QueryResultLabelProvider() {
	}

	public QueryResultLabelProvider(EditorDefine.QUERY_MODE queryMode, final ResultSetUtilDTO rsDAO) {
		this.queryMode = queryMode;
		this.rsDAO = rsDAO;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		HashMap<Integer, Object> rsResult = (HashMap<Integer, Object>)element;
		Object obj = rsResult.get(columnIndex);
		if(obj == null) {
			return SWTResourceManager.getColor(152, 118, 137);
		} else {
			String objValue = obj.toString();
			
			if(queryMode == QUERY_MODE.QUERY) {
				if(GetPreferenceGeneral.getRDBShowInTheColumn() != -1 
						&& objValue.length() > GetPreferenceGeneral.getRDBShowInTheColumn()
						&& !RDBTypeToJavaTypeUtils.isNumberType(rsDAO.getColumnType().get(columnIndex))
				) {
					return SWTResourceManager.getColor(152, 118, 137);
				}
			}
		}
		
		return null;
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		if(columnIndex == 0) return SWTResourceManager.getColor(SWT.COLOR_GRAY);
		
		return null;
	}
	
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public String getColumnText(Object element, int columnIndex) {
		HashMap<Integer, Object> rsResult = (HashMap<Integer, Object>)element;
		
		Object obj = rsResult.get(columnIndex);
		if(obj == null) {
			return GetPreferenceGeneral.getResultNull();
		} else if(GetPreferenceGeneral.getISRDBNumberIsComma() && RDBTypeToJavaTypeUtils.isNumberType(rsDAO.getColumnType().get(columnIndex))) {
			return NumberFormatUtils.addCommaLong(obj);
		} else {
			if(GetPreferenceGeneral.getRDBShowInTheColumn() == -1) {
				return obj.toString();
			} 
			return queryMode == QUERY_MODE.QUERY ? 
					StringUtils.abbreviate(obj.toString(), 0, GetPreferenceGeneral.getRDBShowInTheColumn()) :
					obj.toString();
		}
	}
}
