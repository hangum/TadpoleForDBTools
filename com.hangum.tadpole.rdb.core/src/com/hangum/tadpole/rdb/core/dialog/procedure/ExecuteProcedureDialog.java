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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.dao.ResultSetTableViewerDAO;
import com.hangum.tadpole.sql.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.sql.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.util.executer.ProcedureExecuterManager;
import com.hangum.tadpole.sql.util.executer.procedure.ProcedureExecutor;
import com.hangum.tadpole.sql.util.tables.SQLResultContentProvider;
import com.hangum.tadpole.sql.util.tables.SQLResultLabelProvider;
import com.hangum.tadpole.sql.util.tables.SQLResultSorter;
import com.hangum.tadpole.sql.util.tables.TableUtil;

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
	private ProcedureExecutor procedureExecutor;

	private TableViewer[] sqlResultTableViewer;
	private SQLResultSorter sqlSorter;

	private UserDBDAO userDB;
	private ProcedureFunctionDAO procedureDAO;
	private List<InOutParameterDAO> parameterList = new ArrayList<InOutParameterDAO>();

	private Label[] labelInput;
	private Text[] textInputs;
	private Label[] labelType;

	private Group grpTables;
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
		newShell.setText(procedureDAO.getName() + " Procedure Dialog");
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
		GridLayout gl_compositeInput = new GridLayout(3, false);
		gl_compositeInput.verticalSpacing = 2;
		gl_compositeInput.horizontalSpacing = 2;
		gl_compositeInput.marginHeight = 2;
		gl_compositeInput.marginWidth = 2;
		compositeInput.setLayout(gl_compositeInput);
		compositeInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		try {
			initProcedureExecuter();
			this.parameterList = getInParameter();
		} catch (Exception e) {
			logger.error("get in parameter", e);
			MessageDialog.openError(null, "Error", e.getMessage());

			super.okPressed();
		}

		// ////[ input values
		// ]////////////////////////////////////////////////////////////////////////
		labelInput = new Label[parameterList.size()];
		textInputs = new Text[parameterList.size()];
		labelType = new Label[parameterList.size()];

		for (int i = 0; i < labelInput.length; i++) {
			InOutParameterDAO inParameters = parameterList.get(i);

			labelInput[i] = new Label(compositeInput, SWT.NONE);
			labelInput[i].setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
			labelInput[i].setText(inParameters.getName());

			textInputs[i] = new Text(compositeInput, SWT.BORDER);
			textInputs[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			// Parameter default value set.
			textInputs[i].setText(inParameters.getValue());

			labelType[i] = new Label(compositeInput, SWT.NONE);
			labelType[i].setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));

			String tmpLength = StringUtils.isEmpty(inParameters.getLength()) ? "" : "(" + inParameters.getLength() + ")";
			labelType[i].setText(inParameters.getRdbType() + " " + tmpLength);
		}

		Composite compositeBtn = new Composite(container, SWT.NONE);
		GridLayout gl_compositeBtn = new GridLayout(1, false);
		gl_compositeBtn.verticalSpacing = 2;
		gl_compositeBtn.horizontalSpacing = 2;
		gl_compositeBtn.marginHeight = 2;
		gl_compositeBtn.marginWidth = 2;
		compositeBtn.setLayout(gl_compositeBtn);
		compositeBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));

		btnExecute = new Button(compositeBtn, SWT.NONE);
		btnExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				executeProcedure();
			}
		});
		btnExecute.setText("Execute");

		grpTables = new Group(container, SWT.NONE);
		GridLayout gl_grpTables = new GridLayout(1, false);
		gl_grpTables.horizontalSpacing = 2;
		gl_grpTables.verticalSpacing = 2;
		gl_grpTables.marginHeight = 2;
		gl_grpTables.marginWidth = 2;
		grpTables.setLayout(gl_grpTables);
		grpTables.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpTables.setText("Result Set view");

		initUI();

		return container;
	}

	/**
	 * initialize procedure executer
	 * 
	 * @throws Exception
	 */
	private void initProcedureExecuter() throws Exception {
		ProcedureExecuterManager executorManager = new ProcedureExecuterManager(userDB, procedureDAO);
		procedureExecutor = executorManager.getExecuter();
	}

	/**
	 * 프로시저를 실행합니다.
	 * 
	 */
	private void executeProcedure() {
		if (sqlResultTableViewer != null) {
			for (int i = 0; i < sqlResultTableViewer.length; i++) {
				TableViewer tv = sqlResultTableViewer[i];
				tv.getTable().dispose();
			}
		}

		for (int i = 0; i < parameterList.size(); i++) {
			InOutParameterDAO inParam = parameterList.get(i);
			inParam.setValue(textInputs[i].getText());
		}

		try {
			boolean ret = procedureExecutor.exec(parameterList);
			if (ret) {
				List<ResultSetTableViewerDAO> listResultDao = procedureExecutor.getResultDAO();
				sqlResultTableViewer = new TableViewer[listResultDao.size()];

				for (int i = 0; i < listResultDao.size(); i++) {
					ResultSetTableViewerDAO resultDao = listResultDao.get(i);

					sqlResultTableViewer[i] = new TableViewer(grpTables, SWT.BORDER | SWT.FULL_SELECTION);
					Table table = sqlResultTableViewer[i].getTable();
					table.setHeaderVisible(true);
					table.setLinesVisible(true);
					table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

					sqlSorter = new SQLResultSorter(-999);

					SQLResultLabelProvider.createTableColumn(sqlResultTableViewer[i], resultDao.getMapColumns(), resultDao.getMapColumnType(), sqlSorter);
					sqlResultTableViewer[i].setLabelProvider(new SQLResultLabelProvider());
					sqlResultTableViewer[i].setContentProvider(new SQLResultContentProvider(resultDao.getSourceDataList()));

					sqlResultTableViewer[i].setInput(resultDao.getSourceDataList());
					sqlResultTableViewer[i].setSorter(sqlSorter);

					TableUtil.packTable(sqlResultTableViewer[i].getTable());
				}
			}

			grpTables.layout();
		} catch (Exception e) {
			logger.error("Procedure execute Result view", e);
			MessageDialog.openError(null, "Error", e.getMessage());
		}
	}

	/**
	 * init ui
	 */
	private void initUI() {
		if (textInputs != null && textInputs.length > 0) {
			textInputs[0].setFocus();
		} else {
			btnExecute.setFocus();
		}
	}

	/**
	 * initialize procedure IN information
	 */
	private List<InOutParameterDAO> getInParameter() throws Exception {
		return procedureExecutor.getInParameters();
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Close", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(900, 600);
	}

}
