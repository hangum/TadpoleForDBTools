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
package com.hangum.tadpole.rdb.core.dialog.procedure;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.commons.sql.util.executer.ProcedureExecutor;
import com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;

/**
 * procedure 실행 다이얼로그.
 * 
 * @author hangum
 * 
 */
public class ExecuteProcedureDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ExecuteProcedureDialog.class);
	private ProcedureExecutor executor;

	private TableViewer tableViewer;

	private UserDBDAO userDB;
	private ProcedureFunctionDAO procedureDAO;

	private List<InOutParameterDAO> parameterList = new ArrayList<InOutParameterDAO>();

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param userDB
	 * @param procedureDAO
	 */
	public ExecuteProcedureDialog(Shell parentShell, UserDBDAO userDB, ProcedureFunctionDAO procedureDAO) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);

		this.userDB = userDB;
		this.procedureDAO = procedureDAO;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Excute Procedure");
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
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;

		// input value가 몇개가 되어야 하는지 조사하여 입력값으로 보여줍니다.
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		// in out의 갯수 파악한다.
		Group grpTables = new Group(container, SWT.NONE);
		grpTables.setLayout(new GridLayout(1, false));
		grpTables.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpTables.setText(procedureDAO.getName());

		tableViewer = new TableViewer(grpTables, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		createTaleColumn();

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ExecuteProcedureLabelProvider());

		tableViewer.setInput(parameterList);

		try {
			/**
			 * executor를 DB Type별로 따로 처리하도록 해야 할거 같은데..ㅡㅡ; DDL Script 뽑는것처럼...^^;
			 * 
			 * 
			 */
			executor = new ProcedureExecutor(this.getParentShell(), procedureDAO.getName(), parameterList, userDB);
			this.parameterList = executor.getParameters();
		} catch(Exception e) {
			logger.error(procedureDAO.getName() + " Procedure in out parameter.", e);
			
			MessageDialog.openError(null, "Error", procedureDAO.getName() + " 프로시저의 Parameter를 가져오는 중에 오류가 발생했습니다.");
		}

		tableViewer.refresh();

		return container;
	}

	/**
	 * create column
	 */
	private void createTaleColumn() {
		String columnHeader[] = new String[] { "Seq", "Name", "Type", "ParamType", "Length", "Value" };

		for (int i = 0; i < columnHeader.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.LEFT);

			TableColumn tblclmnColumnName = tableViewerColumn.getColumn();
			tblclmnColumnName.setWidth(columnHeader[i].length() * 15);
			tblclmnColumnName.setText(columnHeader[i]);

			tableViewerColumn.setEditingSupport(new ExecuteProcParamInputSupport(tableViewer, i));
		}
	}

	@Override
	protected void okPressed() {

		boolean ret = executor.exec();

		if (ret) {
			this.close();
		}

	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancle", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(900, 600);
	}

}
