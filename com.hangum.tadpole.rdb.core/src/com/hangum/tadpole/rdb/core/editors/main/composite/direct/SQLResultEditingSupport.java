///*******************************************************************************
// * Copyright (c) 2012 - 2015 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.rdb.core.editors.main.composite.direct;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.commons.lang.StringEscapeUtils;
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
//import org.eclipse.jface.dialogs.Dialog;
//import org.eclipse.jface.dialogs.MessageDialog;
//import org.eclipse.jface.viewers.CellEditor;
//import org.eclipse.jface.viewers.EditingSupport;
//import org.eclipse.jface.viewers.TableViewer;
//import org.eclipse.jface.viewers.TextCellEditor;
//
//import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
//import com.hangum.tadpole.engine.sql.util.DataTypeValidate;
//import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
//import com.hangum.tadpole.engine.sql.util.resultset.ResultSetUtilDTO;
//import com.hangum.tadpole.rdb.core.Messages;
//import com.hangum.tadpole.sql.format.SQLFormater;
//
///**
// * SQL Result Editing support
// *
// * @author hangum
// * @version 1.6.1
// * @since 2015. 4. 6.
// *
// */
//public class SQLResultEditingSupport extends EditingSupport {
//	private static Logger logger = Logger.getLogger(SQLResultEditingSupport.class);
//	
//	private TableViewer tvSQLResult;
//	private ResultSetUtilDTO rsDAO;
//	private int intColumnIndex;
//	
//	private CellEditor cellEditor;
//	
//	/**
//	 * @param viewer
//	 */
//	public SQLResultEditingSupport(TableViewer viewer, final ResultSetUtilDTO rsDAO, final int intColumnIndex) {
//		super(viewer);
//		
//		this.tvSQLResult = viewer;
//		this.rsDAO = rsDAO;
//		this.intColumnIndex = intColumnIndex;
//		
//		this.cellEditor = new TextCellEditor(tvSQLResult.getTable());
//	}
//
//	/* (non-Javadoc)
//	 * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
//	 */
//	@Override
//	protected CellEditor getCellEditor(Object element) {
//		return cellEditor;
//	}
//
//	/* (non-Javadoc)
//	 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
//	 */
//	@Override
//	protected boolean canEdit(Object element) {
//		return true;
//	}
//
//	/* (non-Javadoc)
//	 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
//	 */
//	@Override
//	protected Object getValue(Object element) {
//		HashMap<Integer, String> data = (HashMap<Integer, String>)element;
//		return data.get(intColumnIndex)==null?"":data.get(intColumnIndex);
//	}
//
//	/* (non-Javadoc)
//	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
//	 */
//	@Override
//	protected void setValue(Object element, Object value) {
//		HashMap<Integer, String> data = (HashMap<Integer, String>)element;
//		HashMap<Integer, String> oldDataMap = (HashMap<Integer, String>)data.clone();
//		
//		String oldData = data.get(intColumnIndex)==null?"":data.get(intColumnIndex);
//		if(oldData.equals(value.toString())) return;
//
//		// 입력 값이 올바른지 검사합니다.
//		String colType = RDBTypeToJavaTypeUtils.getRDBType(rsDAO.getColumnType().get(intColumnIndex));
//		if(!DataTypeValidate.isValid(rsDAO.getUserDB(), colType, value.toString())) {
//			MessageDialog.openError(getViewer().getControl().getShell(), CommonMessages.get().Confirm, Messages.get().TextViewerEditingSupport_2 + " is " + colType + "."); 
//			return;
//		} 
//
//		String strColumnName = rsDAO.getColumnName().get(intColumnIndex);
//		String strColumnValue = "";
//		if(RDBTypeToJavaTypeUtils.isNumberType(rsDAO.getColumnType().get(intColumnIndex))) strColumnValue = value.toString();
//		else strColumnValue = "'" + StringEscapeUtils.escapeSql(value.toString()) + "'";
//		
//		String tableName = rsDAO.getColumnTableName().get(intColumnIndex);
//		
//		final String strUpdateStatement = String.format("UPDATE %s SET %s=%s WHERE %s", 
//					tableName, 
//					strColumnName, 
//					strColumnValue, 
//					makeWhereStaement(oldDataMap));
//		String strFormatStatement = "";
//		try {
//			strFormatStatement = SQLFormater.format(strUpdateStatement);
//		} catch(Exception e) {
//			strFormatStatement = strUpdateStatement;
//		}
//		if(logger.isDebugEnabled()) logger.debug("Update SQL Statement is " + strFormatStatement);
//		
//		SQLUpdateDialog dialog = new SQLUpdateDialog(getViewer().getControl().getShell(), rsDAO.getUserDB(),  strFormatStatement);
//		if(Dialog.OK == dialog.open()) {
//			// 수정된 데이터 표시
//			data.put(intColumnIndex, value.toString());
//			tvSQLResult.refresh();
//			tvSQLResult.getTable().setFocus();
//		}
//	}
//	
//	/**
//	 * make where statement
//	 * 
//	 * @return
//	 */
//	private String makeWhereStaement(HashMap<Integer, String> data) {
//		StringBuffer sbWhere = new StringBuffer();
//
//		Map<Integer, String> mapTableNames = rsDAO.getColumnTableName();
//		Map<Integer, String> mapColumnNames = rsDAO.getColumnName();
//		Map<Integer, Integer> mapColumnType = rsDAO.getColumnType();
//		
//		String selectTableName = mapTableNames.get(intColumnIndex);
//		
//		for(int i=1;i<mapColumnNames.size(); i++) {
//			if(selectTableName.equals(mapTableNames.get(i))) {
//				sbWhere.append(mapColumnNames.get(i));
//				
//				if(data.get(i) == null) {
//					sbWhere.append( " IS ");
//				} else {
//					sbWhere.append( " = ");
//				}
//				
//				if(data.get(i) == null) {
//					sbWhere.append(" null ");
//				} else {
//					if(RDBTypeToJavaTypeUtils.isNumberType(mapColumnType.get(i))) sbWhere.append(data.get(i));
//					else sbWhere.append("'" + data.get(i) + "'");
//				}
//			
//				sbWhere.append(" AND ");
//			}
//		}
//		
//		String returnWhere = sbWhere.toString();
//		returnWhere = StringUtils.removeEnd(returnWhere, "AND ");
//		
//		if(logger.isDebugEnabled()) logger.debug("make where statement is : " + returnWhere);
//		
//		return returnWhere;
//	}
//
//}
