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

import java.text.NumberFormat;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
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
	
	/**
	 * RDB Character shown in the column
	 * @return
	 */
	public static String getRDBShowInTheColumn() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN, PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN_VALUE);
		return userInfo.getValue0();
	}

	@SuppressWarnings("unchecked")
	public String getColumnText(Object element, int columnIndex) {
		HashMap<Integer, Object> rsResult = (HashMap<Integer, Object>)element;
		
		Object obj = rsResult.get(columnIndex);
		if(rsDAO != null) {
			if(isPretty & RDBTypeToJavaTypeUtils.isNumberType(rsDAO.getColumnType().get(columnIndex))) return addComma(obj);
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
	 * table의 Column을 생성한다.
	 */
	public static void createTableColumn(final TableViewer tableViewer,
										final ResultSetUtilDTO rsDAO,
										final SQLResultSorter tableSorter) {
		// 기존 column을 삭제한다.
		Table table = tableViewer.getTable();
		int columnCount = table.getColumnCount();
		for(int i=0; i<columnCount; i++) {
			table.getColumn(0).dispose();
		}
		
		if(rsDAO.getColumnName() == null) return;
			
		try {			
			for(int i=0; i<rsDAO.getColumnName().size(); i++) {
				final int index = i;
				final int columnAlign = RDBTypeToJavaTypeUtils.isNumberType(rsDAO.getColumnType().get(i))?SWT.RIGHT:SWT.LEFT;
				String strColumnName = rsDAO.getColumnName().get(i);
		
				/** 표시 되면 안되는 컬럼을 제거 합니다 */
				if(StringUtils.startsWithIgnoreCase(strColumnName, PublicTadpoleDefine.SPECIAL_USER_DEFINE_HIDE_COLUMN)) continue;
				
				final TableViewerColumn tv = new TableViewerColumn(tableViewer, columnAlign);
				final TableColumn tc = tv.getColumn();
				
				tc.setText(strColumnName);
				tc.setResizable(true);
				tc.setMoveable(true);
				
				tc.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						tableSorter.setColumn(index);
						int dir = tableViewer.getTable().getSortDirection();
						if (tableViewer.getTable().getSortColumn() == tc) {
							dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
						} else {
							dir = SWT.DOWN;
						}
						tableViewer.getTable().setSortDirection(dir);
						tableViewer.getTable().setSortColumn(tc);
						tableViewer.refresh();
					}
				});
//				tc.setEditingSupport(new FirstNameEditingSupport(viewer));
				
			}	// end for
			
		} catch(Exception e) { 
			logger.error("SQLResult TableViewer", e);
		}		
	}
	
	/**
	 * 숫자일 경우 ,를 찍어보여줍니다.
	 * 
	 * @param value
	 * @return
	 */
	private String addComma(Object value) {
		if(value==null) return strNullValue;
		
		try{
			NumberFormat nf = NumberFormat.getNumberInstance();
			return nf.format(value);
		} catch(Exception e){
			// ignore exception
		}

		return value.toString();
	}
}
