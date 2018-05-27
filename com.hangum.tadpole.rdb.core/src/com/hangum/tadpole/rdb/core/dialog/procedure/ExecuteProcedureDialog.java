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
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.executer.ProcedureExecuterManager;
import com.hangum.tadpole.engine.sql.util.executer.procedure.ProcedureExecutor;
import com.hangum.tadpole.engine.sql.util.resultset.ResultSetUtilDTO;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultContentProvider;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultLabelProvider;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultSorter;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.msg.TDBInfoDialog;

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
	private List<InOutParameterDAO> parameterInList = new ArrayList<InOutParameterDAO>();
	private List<InOutParameterDAO> parameterOutList = new ArrayList<InOutParameterDAO>();
	
	private Label[] labelInput;
	private Text[] textInputs;
	private Label[] labelType;
	
	private Group grpTables;
	private Button btnExecute;
	private Text textDBMSOutput;
	private Text textObjectName;
	private Text textObjectType;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param userDB
	 * @param procedureDAO
	 */
	public ExecuteProcedureDialog(Shell parentShell, UserDBDAO userDB, ProcedureFunctionDAO procedureDAO) {
		super(parentShell);
		setBlockOnOpen(false);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);// | SWT.APPLICATION_MODAL);

		this.userDB = userDB;
		this.procedureDAO = procedureDAO;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().ExecuteProcedureDialog_0);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite containerInput = (Composite) super.createDialogArea(parent);
		GridLayout gl_containerInput = (GridLayout) containerInput.getLayout();
		gl_containerInput.verticalSpacing = 1;
		gl_containerInput.horizontalSpacing = 1;
		gl_containerInput.marginHeight = 1;
		gl_containerInput.marginWidth = 1;
		
		Composite compositeHead = new Composite(containerInput, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(2, false));
		
		Label lblObjectType = new Label(compositeHead, SWT.NONE);
		lblObjectType.setText(Messages.get().ObjectType);
		
		textObjectType = new Text(compositeHead, SWT.BORDER);
		textObjectType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblObjectName = new Label(compositeHead, SWT.NONE);
		lblObjectName.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblObjectName.setText(Messages.get().ObjectName);
		
		textObjectName = new Text(compositeHead, SWT.BORDER);
		textObjectName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		// input value가 몇개가 되어야 하는지 조사하여 입력값으로 보여줍니다.
		
		try {
			initProcedureExecuter();
			this.parameterInList = getInParameter();
			this.parameterOutList = getOutParameters();
			
		} catch(Exception e) {
			logger.error("get in parameter", e); //$NON-NLS-1$
			MessageDialog.openError(null,CommonMessages.get().Error, e.getMessage());
			
			super.okPressed();
		}
		
		if(!parameterInList.isEmpty()) {
			Group compositeInput = new Group(containerInput, SWT.NONE);
			GridLayout gl_compositeInput = new GridLayout(3, false);
			gl_compositeInput.verticalSpacing = 2;
			gl_compositeInput.horizontalSpacing = 2;
			gl_compositeInput.marginHeight = 2;
			gl_compositeInput.marginWidth = 2;
			compositeInput.setLayout(gl_compositeInput);
			compositeInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			compositeInput.setText("Input Parameter");
	
			//////[ input values ]////////////////////////////////////////////////////////////////////////
			labelInput 	= new Label[parameterInList.size()];
			textInputs 	= new Text[parameterInList.size()];
			labelType 	= new Label[parameterInList.size()];
			
			for(int i=0; i<labelInput.length; i++) {
				InOutParameterDAO inParameters = parameterInList.get(i);
					
				labelInput[i] = new Label(compositeInput, SWT.NONE);
				labelInput[i].setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
				labelInput[i].setText(inParameters.getName());
				
				textInputs[i] = new Text(compositeInput, SWT.BORDER);
				textInputs[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				textInputs[i].addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						if(e.keyCode == SWT.Selection) {
							executeProcedure();
						}
					}
				});
				// Parameter default value set.
				textInputs[i].setText(inParameters.getValue());
				
				labelType[i] = new Label(compositeInput, SWT.NONE);
				labelType[i].setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
				
				String tmpLength = StringUtils.isEmpty(inParameters.getLength())?"" : "(" + inParameters.getLength() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				labelType[i].setText(inParameters.getRdbType() + " " + tmpLength); //$NON-NLS-1$
			}
		}
		
		Composite compositeBtn = new Composite(containerInput, SWT.NONE);
		GridLayout gl_compositeBtn = new GridLayout(1, false);
		gl_compositeBtn.verticalSpacing = 2;
		gl_compositeBtn.horizontalSpacing = 2;
		gl_compositeBtn.marginHeight = 2;
		gl_compositeBtn.marginWidth = 2;
		compositeBtn.setLayout(gl_compositeBtn);
		compositeBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		
		btnExecute = new Button(compositeBtn, SWT.NONE);
		btnExecute.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		btnExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				executeProcedure();
			}
		});
		btnExecute.setText(Messages.get().Execute);
		
		SashForm sashForm = new SashForm(containerInput, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		grpTables = new Group(sashForm, SWT.NONE);
		GridLayout gl_grpTables = new GridLayout(1, false);
		gl_grpTables.horizontalSpacing = 2;
		gl_grpTables.verticalSpacing = 2;
		gl_grpTables.marginHeight = 2;
		gl_grpTables.marginWidth = 2;
		grpTables.setLayout(gl_grpTables);
		grpTables.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpTables.setText(Messages.get().ExecuteProcedureDialog_8);
		
		if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT || userDB.getDBDefine() == DBDefine.TIBERO_DEFAULT) {
			Group grpDbmsOutput = new Group(sashForm, SWT.NONE);
			grpDbmsOutput.setLayout(new GridLayout(1, false));
			GridData gd_grpDbmsOutput = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			gd_grpDbmsOutput.minimumHeight = 50;
			gd_grpDbmsOutput.heightHint = 50;
			grpDbmsOutput.setLayoutData(gd_grpDbmsOutput);
			grpDbmsOutput.setText(Messages.get().DBMSOutput);
			
			textDBMSOutput = new Text(grpDbmsOutput, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
			textDBMSOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			sashForm.setWeights(new int[] {7, 3});
		}
		
		initUI();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return containerInput;
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
		if(sqlResultTableViewer != null) {
			for(int i=0; i<sqlResultTableViewer.length; i++) {
				TableViewer tv = sqlResultTableViewer[i];
				if(!tv.getTable().isDisposed()) {
					tv.getTable().clearAll();
					tv.getTable().dispose();
				}
			}
		}
		
		for(int i=0; i<parameterInList.size(); i++) {
			InOutParameterDAO inParam = parameterInList.get(i);
			inParam.setValue(textInputs[i].getText());
		}
		
		try {
			boolean ret = procedureExecutor.exec(parameterInList);
			if(ret) {
				if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT || userDB.getDBDefine() == DBDefine.TIBERO_DEFAULT) {
					textDBMSOutput.setText(procedureExecutor.getStrOutput());
				}
				
				List<ResultSetUtilDTO> listResultDao = procedureExecutor.getResultDAO();
				sqlResultTableViewer = new TableViewer[listResultDao.size()];
				
				for(int i=0; i<listResultDao.size(); i++) {
					ResultSetUtilDTO resultDao = listResultDao.get(i);
					
					sqlResultTableViewer[i] = new TableViewer(grpTables, SWT.BORDER | SWT.FULL_SELECTION);
					Table table = sqlResultTableViewer[i].getTable();
					table.setHeaderVisible(true);
					table.setLinesVisible(true);
					table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					
					sqlSorter = new SQLResultSorter(-999);
					
					TableUtil.createTableColumn(sqlResultTableViewer[i], resultDao, sqlSorter);
					sqlResultTableViewer[i].setLabelProvider(new SQLResultLabelProvider(GetPreferenceGeneral.getISRDBNumberIsComma(), resultDao, GetPreferenceGeneral.getResultNull()));
					sqlResultTableViewer[i].setContentProvider(new SQLResultContentProvider(resultDao.getDataList().getData()));
					
					sqlResultTableViewer[i].setInput(resultDao.getDataList());
					sqlResultTableViewer[i].setSorter(sqlSorter);
					
					TableUtil.packTable(sqlResultTableViewer[i].getTable());
				}
			}
			
			grpTables.layout();
		} catch(Exception e) {
			logger.error("Procedure execute Result view", e); //$NON-NLS-1$
			
			TDBInfoDialog dialog = new TDBInfoDialog(null, Messages.get().ObjectExecutionException, e.getMessage());
			dialog.open();
		}
	}
	
	/**
	 * init ui
	 */
	private void initUI() {
		textObjectType.setText(procedureDAO.getType());
		textObjectName.setText(procedureDAO.getName());
		
		if(textInputs != null && textInputs.length > 0) {
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
	
	private List<InOutParameterDAO> getOutParameters() throws Exception {
		return procedureExecutor.getOutParameters();
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, CommonMessages.get().Close, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(900, 600);
	}

}
