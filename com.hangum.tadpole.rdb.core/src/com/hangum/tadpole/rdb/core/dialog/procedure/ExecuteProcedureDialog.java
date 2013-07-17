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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

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
	
	private Label[] labelInput;
	private Text[] textInputs;
	private Button btnExecute;
	

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
		newShell.setText(procedureDAO.getName() + " Procedure");
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
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;

		// input value가 몇개가 되어야 하는지 조사하여 입력값으로 보여줍니다.
		Composite compositeInput = new Composite(container, SWT.NONE);
		GridLayout gl_compositeInput = new GridLayout(2, false);
		gl_compositeInput.verticalSpacing = 2;
		gl_compositeInput.horizontalSpacing = 2;
		gl_compositeInput.marginHeight = 2;
		gl_compositeInput.marginWidth = 2;
		compositeInput.setLayout(gl_compositeInput);
		compositeInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		try {
			this.parameterList = getInParameter();
			if(this.parameterList == null) parameterList = new ArrayList<InOutParameterDAO>();
		} catch(Exception e) {
			MessageDialog.openError(null, "Error", procedureDAO.getName() + " 프로시저의 Parameter를 가져오는 중에 오류가 발생했습니다.");
			
			super.okPressed();
		}

		//////[ input values ]////////////////////////////////////////////////////////////////////////
		labelInput = new Label[parameterList.size()];
		textInputs = new Text[parameterList.size()];
		
		for(int i=0; i<labelInput.length; i++) {
			InOutParameterDAO inParameters = parameterList.get(i);
				
			labelInput[i] = new Label(compositeInput, SWT.NONE);
			labelInput[i].setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
			labelInput[i].setText(inParameters.getName());
			
			textInputs[i] = new Text(compositeInput, SWT.BORDER);
			textInputs[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));			
		}
		
		Composite compositeBtn = new Composite(container, SWT.NONE);
		GridLayout gl_compositeBtn = new GridLayout(2, false);
		gl_compositeBtn.verticalSpacing = 2;
		gl_compositeBtn.horizontalSpacing = 2;
		gl_compositeBtn.marginHeight = 2;
		gl_compositeBtn.marginWidth = 2;
		compositeBtn.setLayout(gl_compositeBtn);
		compositeBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		
		Button btnValidation = new Button(compositeBtn, SWT.NONE);
		btnValidation.setEnabled(false);
		btnValidation.setText("Validation");
		
		btnExecute = new Button(compositeBtn, SWT.NONE);
		btnExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				executeProcedure();
			}
		});
		btnExecute.setText("Execute");
		
		//////[ input values ]////////////////////////////////////////////////////////////////////////
		
		Group grpTables = new Group(container, SWT.NONE);
		GridLayout gl_grpTables = new GridLayout(1, false);
		gl_grpTables.horizontalSpacing = 2;
		gl_grpTables.verticalSpacing = 2;
		gl_grpTables.marginHeight = 2;
		gl_grpTables.marginWidth = 2;
		grpTables.setLayout(gl_grpTables);
		grpTables.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpTables.setText("Result");

		tableViewer = new TableViewer(grpTables, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		createTaleColumn();

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ExecuteProcedureLabelProvider());
		
		initUI();

		return container;
	}
	
	/**
	 * 프로시저를 실행합니다.
	 * 
	 */
	private void executeProcedure() {
		for(int i=0; i<parameterList.size(); i++) {
			InOutParameterDAO inParam = parameterList.get(i);
			inParam.setValue(textInputs[i].getText());
		}
		boolean ret = executor.exec(parameterList);
	}
	
	/**
	 * init ui
	 */
	private void initUI() {
		if(textInputs != null && textInputs.length > 0) {
			textInputs[0].setFocus();
		} else {
			btnExecute.setFocus();
		}
	}
	
	/**
	 * initialize procedure information
	 */
	private List<InOutParameterDAO> getInParameter() throws Exception {
		executor = new ProcedureExecutor(procedureDAO, userDB);
		return executor.getInParameters();
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

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(900, 600);
	}

}
