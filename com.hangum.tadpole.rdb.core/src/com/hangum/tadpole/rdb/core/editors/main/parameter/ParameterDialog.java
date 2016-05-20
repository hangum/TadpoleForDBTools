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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.rdb.core.Messages;

/**
 * 
 * @author nilriri
 * example code
   1065
		
		// Parameter Object init.
		param = null;
		if(intExecuteQueryType != ALL_QUERY_EXECUTE) {
			ParameterDialog epd = new ParameterDialog(this.getSite().getWorkbenchWindow().getShell(), this.userDB, finalExecuteSQL);
			if (epd.getParamCount() > 0){
				epd.open();
				param = epd.getParameterObject();
				epd.close();
			}
		}



1249				
				if (param != null && param.getParameter().length > 0 ){
					int i = 1;
					for (Object obj : param.getParameter()){
						stmt.setObject(i++, obj);
					}
				}

 */
public class ParameterDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(ParameterDialog.class);
	
	private Table table;
	private UserDBDAO userDB;
	private List<Map<Integer, Object>> parameters;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ParameterDialog(Shell parentShell, final UserDBDAO userDB, int paramCount) {
		super(parentShell);
		
		this.userDB = userDB;
		this.makeParamCount(paramCount);
	}

	public ParameterDialog(Shell parentShell, final UserDBDAO userDB, Map<Integer, String> mapIndex) {
		super(parentShell);
		
		this.userDB = userDB;
		this.makeParamCount(mapIndex);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().ParameterDialog_0);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
	}
	
	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		TableColumnLayout tcl_composite = new TableColumnLayout();
		composite.setLayout(tcl_composite);

		TableViewer tableViewer = new TableViewer(composite, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		createTableColumn(tableViewer, tcl_composite);

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ParamLabelProvider());
		tableViewer.setInput(parameters);
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		tableViewer.getTable().setFocus();

		return container;
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
		tvcType.setEditingSupport(new ParameterEditingSupport(tableViewer, 2, this.userDB));

		TableViewerColumn tvcValue = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcValue = tvcValue.getColumn();
		tcl_composite.setColumnData(tcValue, new ColumnPixelData(150, true, true));
		tcValue.setText(Messages.get().ParameterDialog_4);
		tvcValue.setEditingSupport(new ParameterEditingSupport(tableViewer, 3, this.userDB));
	}
	
	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().ExecuteQuery, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().CANCEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(420, 300);
	}

	/**
	 * Return java style parameter
	 * 
	 * @return
	 */
	public ParameterObject getParameterObject() {
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
	public ParameterObject getOracleParameterObject(Map<Integer, String> mapIndex) {
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
	protected void makeParamCount(int paramCount) {
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
	protected void makeParamCount(Map<Integer, String> mapIndex) {
		
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
	public List<Map<Integer, Object>> getParameters() {
		return parameters;
	}

}
