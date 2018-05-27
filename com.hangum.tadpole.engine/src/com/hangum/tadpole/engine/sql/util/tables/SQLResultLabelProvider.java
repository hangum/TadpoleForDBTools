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
package com.hangum.tadpole.engine.sql.util.tables;

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
import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.resultset.ResultSetUtilDTO;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.session.manager.SessionManager;
import com.swtdesigner.SWTResourceManager;

/**
 * SQLResult의 LabelProvider
 * 
 * @author hangum
 *
 */
public class SQLResultLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SQLResultLabelProvider.class);
	private boolean isPretty = false;
	private String strNullValue = "";
	private ResultSetUtilDTO rsDAO;
	
	public SQLResultLabelProvider() {
	}

	public SQLResultLabelProvider(final boolean isPretty, final String strNullValue) {
		this.isPretty = isPretty;
		this.strNullValue = strNullValue;
	}
	
	public SQLResultLabelProvider(final boolean isPretty, final ResultSetUtilDTO rsDAO, final String strNullValue) {
		this.isPretty = isPretty;
		this.strNullValue = strNullValue;
		this.rsDAO = rsDAO;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
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
		if(rsDAO != null) {
			if(isPretty & RDBTypeToJavaTypeUtils.isNumberType(rsDAO.getColumnType().get(columnIndex))) return NumberFormatUtils.addCommaLong(obj);
		}
		String showValue = "";
		try {
			int intShowWidth = Integer.parseInt(getRDBShowInTheColumn());
			if(intShowWidth != -1) showValue = StringUtils.abbreviate(obj.toString(), 0, intShowWidth);
			else showValue = obj.toString();
		} catch(Exception e) {}
		
		return obj == null ? strNullValue : showValue;
	}
	
	/**
	 * RDB Character shown in the column
	 * @return
	 */
	private static String getRDBShowInTheColumn() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN, PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN_VALUE);
		return userInfo.getValue0();
	}
	
}
