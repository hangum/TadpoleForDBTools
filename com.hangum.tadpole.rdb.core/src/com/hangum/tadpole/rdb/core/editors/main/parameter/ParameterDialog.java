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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.PARAMETER_TYPE;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.ParameterUtils;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.hangum.tadpole.mongodb.core.dialogs.msg.TadpoleSQLDialog;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.composite.ResultSetComposite;

/**
 * 
 * @author nilriri
 */
public class ParameterDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(ParameterDialog.class);
	/** 쿼리 실행 후 닫기 버튼 정의 */
	private int EXECUTE_AND_CLOSE = IDialogConstants.CLIENT_ID + 1;
	
	private ResultSetComposite resultSetComposite;
	private PARAMETER_TYPE parameterType;
	private RequestQuery reqQuery;
	private String strSQL;
	private UserDBDAO userDB;
	private Map<Integer, String> mapIndex;
	private List<Map<Integer, Object>> parameters;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param parameterType 
	 * @param reqQuery 
	 * @wbp.parser.constructor
	 */
	public ParameterDialog(Shell parentShell, ResultSetComposite resultSetComposite, PARAMETER_TYPE parameterType, final RequestQuery reqQuery, final UserDBDAO userDB, final String strSQL, int paramCount) {
		super(parentShell);
		setBlockOnOpen(false);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.resultSetComposite = resultSetComposite;
		this.parameterType = parameterType;
		this.reqQuery = reqQuery;
		this.userDB = userDB;
		
		this.strSQL = strSQL;
		this.makeParamCount(paramCount);
	}

	public ParameterDialog(Shell parentShell, ResultSetComposite resultSetComposite, PARAMETER_TYPE parameterType, final RequestQuery reqQuery, final UserDBDAO userDB, final String strSQL, final Map<Integer, String> mapIndex) {
		super(parentShell);
		setBlockOnOpen(false);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.resultSetComposite = resultSetComposite;
		this.parameterType = parameterType;
		this.reqQuery = reqQuery;
		this.userDB = userDB;
		
		this.strSQL = strSQL;
		this.mapIndex = mapIndex;
		this.makeParamCount(mapIndex);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().ParameterDialog_0);
	}
	
	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginWidth = 5;
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginWidth = 5;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		container.setLayout(new GridLayout(1, false));
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(compositeHead, SWT.FLAT | SWT.RIGHT);
		
		ToolItem toolItem = new ToolItem(toolBar, SWT.NONE);
		toolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TadpoleSQLDialog dialog = new TadpoleSQLDialog(getShell(), Messages.get().ViewQuery, strSQL);
				dialog.open();
			}
		});
		toolItem.setText(Messages.get().ViewQuery);

		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		TableColumnLayout tcl_compositeBody = new TableColumnLayout();
		compositeBody.setLayout(tcl_compositeBody);

		TableViewer tableViewer = new TableViewer(compositeBody, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		createTableColumn(tableViewer, tcl_compositeBody);

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ParamLabelProvider());
		tableViewer.setInput(parameters);
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		tableViewer.getTable().setFocus();

		return container;
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == EXECUTE_AND_CLOSE) {
			executeQuery();
			return;
		}
		super.buttonPressed(buttonId);
	}
	
	
	@Override
	protected void okPressed() {
		executeQuery();
		
		super.okPressed();
	}
	
	/**
	 * 쿼리 실행
	 */
	private void executeQuery() {
		reqQuery.setSqlStatementType(PublicTadpoleDefine.SQL_STATEMENT_TYPE.PREPARED_STATEMENT);
		reqQuery.setSql(strSQL);
		
		ParameterObject paramObj = getParameterObject();
		String repSQL = ParameterUtils.fillParameters(reqQuery.getSql(), paramObj.getParameter());
		
		reqQuery.setSqlAddParameter(repSQL);
		
		if(PARAMETER_TYPE.JAVA_BASIC == parameterType) {
			reqQuery.setStatementParameter(getParameterObject().getParameter());
		} else if(PARAMETER_TYPE.ORACLE == parameterType ||
				PARAMETER_TYPE.MYBATIS_SHARP == parameterType ||
				PARAMETER_TYPE.MYBATIS_DOLLAR == parameterType
		) {
			reqQuery.setStatementParameter(getOracleParameterObject(mapIndex).getParameter());
		}
		
		resultSetComposite._executeQuery(reqQuery);
	}

	/**
	 * create table column
	 * 
	 * @param tableViewer
	 * @param tcl_composite
	 */
	private void createTableColumn(TableViewer tableViewer, TableColumnLayout tcl_composite) {
//		Not support Eclipse RAP.
//		This class table course. course is keyboard controls
//		final TableCursor cursor = new TableCursor(table, SWT.NONE);
		
		TableViewerColumn tvcSeq = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcSeq = tvcSeq.getColumn();
		tcl_composite.setColumnData(tcSeq, new ColumnPixelData(30, true, true));
		tcSeq.setText(Messages.get().ParameterDialog_1);

		TableViewerColumn tvcName = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcName = tvcName.getColumn();
		tcl_composite.setColumnData(tcName, new ColumnPixelData(150, true, true));
		tcName.setText(Messages.get().ParameterDialog_2);

		TableViewerColumn tvcType = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcType = tvcType.getColumn();
		tcl_composite.setColumnData(tcType, new ColumnPixelData(80, true, true));
		tcType.setText(Messages.get().DataType);
		tvcType.setEditingSupport(new ParameterEditingSupport(tableViewer, 2, this.userDB, parameters));

		TableViewerColumn tvcValue = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcValue = tvcValue.getColumn();
		tcl_composite.setColumnData(tcValue, new ColumnPixelData(150, true, true));
		tcValue.setText(Messages.get().ParameterDialog_4);
		tvcValue.setEditingSupport(new ParameterEditingSupport(tableViewer, 3, this.userDB, parameters));
	}
	
	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, EXECUTE_AND_CLOSE, Messages.get().ExecuteQuery, true);
		createButton(parent, IDialogConstants.OK_ID, Messages.get().ExecuteQueryAndClose, false);
		createButton(parent, IDialogConstants.CANCEL_ID,  CommonMessages.get().Close, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 350);
	}

	/**
	 * Return java style parameter
	 * 
	 * @return
	 */
	private ParameterObject getParameterObject() {
		ParameterObject param = new ParameterObject();

		for (Map<Integer, Object> paramE : parameters) {
			switch (RDBTypeToJavaTypeUtils.getJavaType((String) paramE.get(2))) {
			case java.sql.Types.INTEGER:
				param.setObject(Integer.valueOf(paramE.get(3).toString()));
				break;
			default:
				param.setObject(paramE.get(3));
				break;
			}
		}
		return param;
	}
	
	/**
	 * Returns oracle styled parameter object
	 * 
	 * @param mapIndex
	 * @return
	 */
	private ParameterObject getOracleParameterObject(Map<Integer, String> mapIndex) {
		ParameterObject param = new ParameterObject();
		
		for(Integer intKey : mapIndex.keySet()) {
			String strParamName = mapIndex.get(intKey);
			
			for (Map<Integer, Object> mapParam : parameters) {
				String strTmpParamName = ""+mapParam.get(1); //$NON-NLS-1$
				
				if(StringUtils.equals(strParamName, strTmpParamName)) {
					switch (RDBTypeToJavaTypeUtils.getJavaType((String) mapParam.get(2))) {
					case java.sql.Types.INTEGER:
						param.setObject(Integer.valueOf(mapParam.get(3).toString()));
						break;
					default:
						param.setObject(mapParam.get(3));
						break;
					}
					
					// 파라미터 이름이 동일할 경우 하나만 설정되도록 수정.
					break;
				}

			}
		}
			
		return param;
	}
	

	/**
	 * java type
	 * 
	 * @param paramCount
	 */
	private void makeParamCount(int paramCount) {
		parameters = new ArrayList<Map<Integer, Object>>();
		for (int i = 0; i < paramCount; i++) {
			Map<Integer, Object> map = new HashMap<Integer, Object>();
			map.put(0, (i + 1));
			map.put(1, "Param" + (i + 1)); //$NON-NLS-1$
			map.put(2, RDBTypeToJavaTypeUtils.supportParameterTypes(userDB)[0]);
			map.put(3, ""); //$NON-NLS-1$
			
			parameters.add(map);
		}
	}
	
	/**
	 * oracel type 
	 * 
	 * @param mapIndex
	 */
	private void makeParamCount(Map<Integer, String> mapIndex) {
		
		parameters = new ArrayList<Map<Integer, Object>>();
//		int i = 0;
//		for (String strKey : mapIndex.keySet()) {
		for(int i=0; i<mapIndex.size(); i++) {
			String strKey = mapIndex.get(i+1);
			Map<Integer, Object> map = new HashMap<Integer, Object>();
			map.put(0, (i+ + 1));
			map.put(1, strKey);
			map.put(2, RDBTypeToJavaTypeUtils.supportParameterTypes(userDB)[0]);
			map.put(3, ""); //$NON-NLS-1$
			
			parameters.add(map);
		}
		
	}
	
	/**
	 * table dao
	 * 
	 * @return
	 */
	private List<Map<Integer, Object>> getParameters() {
		return parameters;
	}

}
