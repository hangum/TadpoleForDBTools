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

import java.text.DecimalFormat;
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

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.define.EditorDefine.QUERY_MODE;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.resultset.ResultSetUtilDTO;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultSorter;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
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
	
	private EditorDefine.QUERY_MODE queryMode = QUERY_MODE.QUERY;
	private boolean isPretty = false;
	private ResultSetUtilDTO rsDAO;
	
	public SQLResultLabelProvider() {
	}

	public SQLResultLabelProvider(final boolean isPretty) {
		this.isPretty = isPretty;
	}
	
	public SQLResultLabelProvider(EditorDefine.QUERY_MODE queryMode, final boolean isPretty, final ResultSetUtilDTO rsDAO) {
		this.queryMode = queryMode;
		this.isPretty = isPretty;
		this.rsDAO = rsDAO;
	}
	
//	@Override
//	public Font getFont(Object element, int columnIndex) {
//		if(tvQueryResult == null) return null;
//		
//		HashMap<Integer, Object> rsResult = (HashMap<Integer, Object>)element;
//		Object obj = rsResult.get(columnIndex);
//		if(obj == null) {
//			Font font = tvQueryResult.getTable().getFont();
//			if(font != null) return ResourceManager.getItalicFont(font);
//			
//		} else if(queryMode == QUERY_MODE.QUERY) {
//			if(obj.toString().length() > getRDBShowInTheColumn()) {
//				Font font = tvQueryResult.getTable().getFont();
//				if(font != null) return ResourceManager.getItalicFont(font);
//			}
//		}
//		
//		return null;
//	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		HashMap<Integer, Object> rsResult = (HashMap<Integer, Object>)element;
		Object obj = rsResult.get(columnIndex);
		if(obj == null) {
			return SWTResourceManager.getColor(152, 118, 137);
			
		} else if(queryMode == QUERY_MODE.QUERY) {
			if(getRDBShowInTheColumn() != -1 && obj.toString().length() > getRDBShowInTheColumn()) {
				return SWTResourceManager.getColor(152, 118, 137);
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
	
	/**
	 * RDB Character shown in the column
	 * @return
	 */
	private static Integer getRDBShowInTheColumn() {
		UserInfoDataDAO userInfo = SessionManager.getUserInfo(PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN, PreferenceDefine.RDB_CHARACTER_SHOW_IN_THE_COLUMN_VALUE);
		return Integer.parseInt(userInfo.getValue0());
	}

	@SuppressWarnings("unchecked")
	public String getColumnText(Object element, int columnIndex) {
		HashMap<Integer, Object> rsResult = (HashMap<Integer, Object>)element;
		
		Object obj = rsResult.get(columnIndex);
		if(rsDAO != null && rsDAO.getColumnType().get(columnIndex) != null && queryMode == QUERY_MODE.QUERY) {
			if(isPretty && RDBTypeToJavaTypeUtils.isNumberType(rsDAO.getColumnType().get(columnIndex))) return addComma(obj);
		}
		
		if(obj == null) {
			return GetPreferenceGeneral.getResultNull();
		} else {
			if(getRDBShowInTheColumn() == -1) {
				return obj.toString();
			} 
			return queryMode == QUERY_MODE.QUERY ? 
					StringUtils.abbreviate(obj.toString(), 0, getRDBShowInTheColumn()) :
					obj.toString();
		}
	}
	
	/**
	 * table의 Column을 생성한다.
	 */
	public static void createTableColumn(
										final RequestQuery reqQuery,
										final TableViewer tableViewer,
										final ResultSetUtilDTO rsDAO,
										final SQLResultSorter tableSorter, 
										final boolean isEditable) {
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

				// if select statement update
				if(PublicTadpoleDefine.QUERY_DML_TYPE.SELECT == reqQuery.getSqlDMLType() && isEditable) {
					if(i != 0) tv.setEditingSupport(new SQLResultEditingSupport(tableViewer, rsDAO, i));
				}
				
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
	private static String addComma(Object value) {
		if(value==null) return GetPreferenceGeneral.getResultNull();
		
		try{
			DecimalFormat nf = new DecimalFormat("###,###.#############");
			return nf.format(Double.parseDouble(value.toString()));
		} catch(Exception e){
			// ignore exception
		}

		return value.toString();
	}

}
