/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.importdb.core.dialog.importdb.mongodb;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.importdb.Activator;
import com.hangum.tadpole.importdb.core.dialog.importdb.composite.TableColumnLIstComposite;
import com.hangum.tadpole.system.TadpoleSystem_UserDBQuery;

/**
 * 올챙이디비 데이터를 몽고디비로 이관합니다.
 * 
 * @author hangum
 *
 */
public class MongoDBImportDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MongoDBImportDialog.class);
	
	private TabFolder tabFolderQuery;

	private UserDBDAO userDB = null;
	private Text textQuery;
	private Text textCollectionName;
	
	private Combo comboDBList;
	private TableColumnLIstComposite tableColumnListComposite;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public MongoDBImportDialog(Shell parentShell, UserDBDAO userDB) {
		super(parentShell);
		
		this.userDB = userDB;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Imports"); //$NON-NLS-1$
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 3;
		gridLayout.horizontalSpacing = 3;
		gridLayout.marginHeight = 3;
		gridLayout.marginWidth = 3;
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(2, false));
		
		Label lblSource = new Label(compositeHead, SWT.NONE);
		lblSource.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSource.setText("Source");
		
		comboDBList = new Combo(compositeHead, SWT.READ_ONLY);
		comboDBList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initCombo();
			}
		});
		comboDBList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTarget = new Label(compositeHead, SWT.NONE);
		lblTarget.setText("Target");
		
		Label lblTargetDB = new Label(compositeHead, SWT.NONE);
		lblTargetDB.setText(userDB.getDisplay_name());
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tabFolderQuery = new TabFolder(compositeBody, SWT.NONE);
		tabFolderQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabItem tabItemTable = new TabItem(tabFolderQuery, SWT.NONE);
		tabItemTable.setText("Table");
		
		tableColumnListComposite = new TableColumnLIstComposite(tabFolderQuery, SWT.NONE);
		tabItemTable.setControl(tableColumnListComposite);
		tableColumnListComposite.setLayout(new GridLayout(1, false));
		
		Button btnSelectAll = new Button(tableColumnListComposite, SWT.NONE);
		btnSelectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableColumnListComposite.selectAll();
			}
		});
		btnSelectAll.setText("Select All");
		
		TabItem tabItemQuery = new TabItem(tabFolderQuery, SWT.NONE);
		tabItemQuery.setText("Query");
		
		Composite compositeQuery = new Composite(tabFolderQuery, SWT.NONE);
		tabItemQuery.setControl(compositeQuery);
		compositeQuery.setLayout(new GridLayout(1, false));
		
		textQuery = new Text(compositeQuery, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeQueryTail = new Composite(compositeQuery, SWT.NONE);
		compositeQueryTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeQueryTail.setLayout(new GridLayout(3, false));
		
		Label lblCollectionName = new Label(compositeQueryTail, SWT.NONE);
		lblCollectionName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCollectionName.setBounds(0, 0, 56, 15);
		lblCollectionName.setText("Collection Name");
		
		textCollectionName = new Text(compositeQueryTail, SWT.BORDER);
		textCollectionName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeQueryTail, SWT.NONE);
		
//		Button btnPreview = new Button(compositeQueryTail, SWT.NONE);
//		btnPreview.setText("Preview");
		
		init();

		return container;
	}
	
	private void initCombo() {
		tableColumnListComposite.init( (UserDBDAO)comboDBList.getData(comboDBList.getText()) );
		tableColumnListComposite.layout();
	}
	
	/**
	 * 화면을 초기화 합니다.
	 */
	private void init() {
		try {
			int visibleItemCount = 0;
			List<UserDBDAO> userDBS = TadpoleSystem_UserDBQuery.getUserDB();
			for (UserDBDAO userDBDAO : userDBS) {
				if (DBDefine.getDBDefine(userDBDAO.getTypes()) != DBDefine.MONGODB_DEFAULT) {
					comboDBList.add(userDBDAO.getDisplay_name());
					comboDBList.setData(userDBDAO.getDisplay_name(), userDBDAO);
					visibleItemCount++;
				}
			}
			comboDBList.setVisibleItemCount(visibleItemCount);
			comboDBList.select(0);
			initCombo();
			
		} catch (Exception e) {
			logger.error("initialize Mongodb import", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", "initialize Mongodb import", errStatus); //$NON-NLS-1$
		}
	}
	
	@Override
	protected void okPressed() {
		final UserDBDAO exportDBDAO = (UserDBDAO)comboDBList.getData(comboDBList.getText());
		Job job = null;
		
		if(tabFolderQuery.getSelectionIndex() == 0) {
			RDBTableToMongoDBImport importData = new RDBTableToMongoDBImport(userDB, tableColumnListComposite.getSelectListTables(), exportDBDAO);
			job = importData.workTableImport();
			if(job == null) return;
			
		} else if(tabFolderQuery.getSelectionIndex() == 1) {
			QueryToMongoDBImport importData = new QueryToMongoDBImport(userDB, textCollectionName.getText(), textQuery.getText(), exportDBDAO);
			job = importData.workTableImport();
			if(job == null) return;			
		}
		
		job.addJobChangeListener(new JobChangeAdapter() {			
			public void done(IJobChangeEvent event) {
				final IJobChangeEvent jobEvent = event; 
				getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if(jobEvent.getResult().isOK()) {
							MessageDialog.openInformation(null, "Confirm", "complet import data.");
						} else {				
							ExceptionDetailsErrorDialog.openError(null, "Error", "Import error", jobEvent.getResult()); //$NON-NLS-1$
						}						
					}					
				});	// end display.asyncExec
			}	// end done			
		});	// end job
		
		job.setName(userDB.getDisplay_name());
		job.setUser(true);
		job.schedule();		
	}	

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Import", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(573, 490);
	}
}
