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
package com.hangum.tadpole.rdb.core.editors.main.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.resultset.ResultSetUtilDTO;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultSorter;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.editors.main.composite.resultdetail.ResultTableComposite;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * SQL result table util
 * 
 * @author hangum
 *
 */
public class SQLResultTableUtil extends TableUtil {
	private static final Logger logger = Logger.getLogger(SQLResultTableUtil.class);
	
	/**
	 * table의 Column을 생성한다.
	 */
	public static void createTableColumn(final ResultTableComposite rtComposite,
										final RequestQuery reqQuery,
										final TableViewer tableViewer,
										final ResultSetUtilDTO rsDAO,
										final SQLResultSorter tableSorter,
										final String strResultSetHeadClicks,
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
				String strColumnName = rsDAO.getColumnLabelName().get(i);
		
				/** 표시 되면 안되는 컬럼을 제거 합니다 */
				if(StringUtils.startsWithIgnoreCase(strColumnName, PublicTadpoleDefine.SPECIAL_USER_DEFINE_HIDE_COLUMN)) continue;
				
				final TableViewerColumn tv = new TableViewerColumn(tableViewer, columnAlign);
				final TableColumn tc = tv.getColumn();
				
				tc.setText(strColumnName);
				tc.setResizable(true);
				tc.setMoveable(true);
				
				if(PreferenceDefine.RDB_RESULT_SET_HEAD_CLICK_SORTDATA_VALUE.equals(strResultSetHeadClicks)) {
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
					
				} else {
					tc.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							String strLabel = tc.getText();
							if(!StringUtils.isEmpty(strLabel)) {
								if(GetPreferenceGeneral.getAddComma()) {
									rtComposite.appendTextAtPosition(String.format("%s, ", tc.getText()));
								} else {
									rtComposite.appendTextAtPosition(String.format("%s ", tc.getText()));
								}
							}
						}
					});

				}
			
//				
//				TODO 디비 스키마 명을 사용할 수있어서, 현재로서는 직접 수정하지 못하도록 코드를 막습니다. - hangum(16.07.26)
				// if select statement update
//				if(PublicTadpoleDefine.QUERY_DML_TYPE.SELECT == reqQuery.getSqlDMLType() && isEditable) {
//					if(i != 0) tv.setEditingSupport(new SQLResultEditingSupport(tableViewer, rsDAO, i));
//				}
				
			}	// end for
			
		} catch(Exception e) { 
			logger.error("SQLResult TableViewer", e);
		}		
	}

}
