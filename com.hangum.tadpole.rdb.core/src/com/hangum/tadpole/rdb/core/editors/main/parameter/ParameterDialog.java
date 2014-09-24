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

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.util.RDBTypeToJavaTypeUtils;
import com.ibatis.sqlmap.client.SqlMapClient;

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
	private int paramCount = 0;
	private List<Map<Integer, Object>> parameters;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ParameterDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Bind Parameters");
	}

	/**
	 * @wbp.parser.constructor
	 */
	public ParameterDialog(Shell parentShell, UserDBDAO userDB, String executeQuery) throws Exception {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);

		this.userDB = userDB;
		this.calcParamCount(executeQuery);
		makeParamCount();
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

		TableViewer tableViewer = new TableViewer(composite, SWT.SINGLE | SWT.BORDER | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		createTableColumn(tableViewer, tcl_composite);

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ParamLabelProvider());
		tableViewer.setInput(parameters);

		tableViewer.refresh();
		
//		table.select(0);
		table.setFocus();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}

	private void createTableColumn(TableViewer tableViewer, TableColumnLayout tcl_composite) {
		TableViewerColumn tvcSeq = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcSeq = tvcSeq.getColumn();
		tcl_composite.setColumnData(tcSeq, new ColumnPixelData(30, true, true));
		tcSeq.setText("Seq");

		TableViewerColumn tvcName = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcName = tvcName.getColumn();
		tcl_composite.setColumnData(tcName, new ColumnPixelData(80, true, true));
		tcName.setText("Param Name");

		TableViewerColumn tvcType = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcType = tvcType.getColumn();
		tcl_composite.setColumnData(tcType, new ColumnPixelData(80, true, true));
		tcType.setText("Data Type");
		tvcType.setEditingSupport(new ParameterEditingSupport(tableViewer, 2, this.userDB));

		TableViewerColumn tvcValue = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcValue = tvcValue.getColumn();
		tcl_composite.setColumnData(tcValue, new ColumnPixelData(150, true, true));
		tcValue.setText("Param Value");
		tvcValue.setEditingSupport(new ParameterEditingSupport(tableViewer, 3, this.userDB));
	}
	
	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Close", false);
//		Button button = createButton(parent, IDialogConstants.CANCEL_ID, "Close", false);
//		button.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				parameters.clear();
//			}
//		});
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(370, 300);
	}

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

	protected void calcParamCount(String executeQuery) throws Exception {

		java.sql.Connection javaConn = null;
		PreparedStatement stmt = null;
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			stmt = javaConn.prepareStatement(executeQuery);
			java.sql.ParameterMetaData pmd = stmt.getParameterMetaData();
			if(pmd != null) {
				paramCount = pmd.getParameterCount();	
			} else {
				paramCount = 0;
			}

		} catch (Exception e) {
			logger.error("Count parameter error", e);
			paramCount = 0;
			throw e;
		} finally {
			try {
				stmt.close();
			} catch (Exception e) {
			}
			try {
				javaConn.close();
			} catch (Exception e) {
			}
		}
	}

	protected void makeParamCount() {
		parameters = new ArrayList<Map<Integer, Object>>();
		for (int i = 0; i < paramCount; i++) {
			Map<Integer, Object> map = new HashMap<Integer, Object>();
			map.put(0, (i + 1));
			map.put(1, "Param" + (i + 1));
			map.put(2, RDBTypeToJavaTypeUtils.supportParameterTypes(userDB)[0]);
			// map.put(2, "VARCHAR");
			map.put(3, "");
			parameters.add(map);
		}
	}

	public int getParamCount() {
		return paramCount;
	}
}
