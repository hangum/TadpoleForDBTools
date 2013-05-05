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
package com.hangum.tadpole.importdb.core.dialog.importdb.editor;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.editor.core.widgets.editor.TadpoleOrionHubEditor;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.importdb.Activator;
import com.hangum.tadpole.importdb.core.Messages;
import com.hangum.tadpole.importdb.core.dialog.importdb.composite.TableColumnLIstComposite;
import com.hangum.tadpole.importdb.core.dialog.importdb.mongodb.DBImport;
import com.hangum.tadpole.importdb.core.dialog.importdb.mongodb.MongoDBCollectionToMongodBImport;
import com.hangum.tadpole.importdb.core.dialog.importdb.mongodb.QueryToMongoDBImport;
import com.hangum.tadpole.importdb.core.dialog.importdb.mongodb.RDBTableToMongoDBImport;
import com.hangum.tadpole.system.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.util.TadpoleWidgetUtils;

/**
 * mongodb import
 * 
 * @author hangum
 *
 */
public class MongoDBImportEditor extends EditorPart {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MongoDBImportEditor.class);
	public static final String ID = "com.hangum.tadpole.importdb.editor.mongodb"; //$NON-NLS-1$
	/** 모두 선택 눌렀는지 */
	private boolean isSelectAll = false;
	
	private CTabFolder tabFolderQuery;

	private UserDBDAO userDB = null;
	private TadpoleOrionHubEditor textQuery;
	private Text textCollectionName;
	
	private Combo comboDBList;
	private TableColumnLIstComposite tableColumnListComposite;
	private Button btnExistOnDelete;

	public MongoDBImportEditor() {
		super();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		
		MongoDBImportEditorInput qei = (MongoDBImportEditorInput)input;
		userDB = qei.getUserDB();
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.marginHeight = 2;
		gl_parent.verticalSpacing = 2;
		gl_parent.horizontalSpacing = 2;
		gl_parent.marginWidth = 2;
		parent.setLayout(gl_parent);
		
		Composite compositeHead = new Composite(parent, SWT.BORDER);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeHead = new GridLayout(3, false);
		gl_compositeHead.verticalSpacing = 2;
		gl_compositeHead.horizontalSpacing = 2;
		gl_compositeHead.marginHeight = 2;
		gl_compositeHead.marginWidth = 2;
		compositeHead.setLayout(gl_compositeHead);
		
		Label lblSource = new Label(compositeHead, SWT.NONE);
		lblSource.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSource.setText(Messages.MongoDBImportEditor_0);
		
		comboDBList = new Combo(compositeHead, SWT.READ_ONLY);
		comboDBList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initCombo();
				isSelectAll = false;
			}
		});
		comboDBList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblTarget = new Label(compositeHead, SWT.NONE);
		lblTarget.setText(Messages.MongoDBImportEditor_2);
		
		Label lblTargetDB = new Label(compositeHead, SWT.NONE);
		lblTargetDB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblTargetDB.setText(userDB.getDisplay_name());
		
		Button btnImport = new Button(compositeHead, SWT.NONE);
		btnImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				importData();
			}
		});
		btnImport.setText(Messages.MongoDBImportEditor_4);
		
		Composite compositeBody = new Composite(parent, SWT.NONE);
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 1;
		gl_compositeBody.horizontalSpacing = 1;
		gl_compositeBody.marginHeight = 1;
		gl_compositeBody.marginWidth = 1;
		compositeBody.setLayout(gl_compositeBody);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tabFolderQuery = new CTabFolder(compositeBody, SWT.NONE);
		tabFolderQuery.setBorderVisible(false);		
		tabFolderQuery.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());
		tabFolderQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		CTabItem tabItemTable = new CTabItem(tabFolderQuery, SWT.NONE);
		tabItemTable.setText(Messages.MongoDBImportEditor_5);
		
		tableColumnListComposite = new TableColumnLIstComposite(tabFolderQuery, SWT.NONE);
		tabItemTable.setControl(tableColumnListComposite);
		tableColumnListComposite.setLayout(new GridLayout(1, false));
		
		Button btnSelectAll = new Button(tableColumnListComposite, SWT.NONE);
		btnSelectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!isSelectAll) {
					tableColumnListComposite.selectAll();
					isSelectAll = true;
				} else {
					tableColumnListComposite.selectNotAll();
					isSelectAll = false;
				}
			}
		});
		btnSelectAll.setText(Messages.MongoDBImportEditor_6);
		
		CTabItem tabItemQuery = new CTabItem(tabFolderQuery, SWT.NONE);
		tabItemQuery.setText(Messages.MongoDBImportEditor_7);
		
		Composite compositeQuery = new Composite(tabFolderQuery, SWT.NONE);
		tabItemQuery.setControl(compositeQuery);
		compositeQuery.setLayout(new GridLayout(1, false));
		
		textQuery = new TadpoleOrionHubEditor(compositeQuery, SWT.BORDER);
		textQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeQueryTail = new Composite(compositeQuery, SWT.NONE);
		compositeQueryTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeQueryTail.setLayout(new GridLayout(3, false));
		
		Label lblCollectionName = new Label(compositeQueryTail, SWT.NONE);
		lblCollectionName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCollectionName.setBounds(0, 0, 56, 15);
		lblCollectionName.setText(Messages.MongoDBImportEditor_9);
		
		textCollectionName = new Text(compositeQueryTail, SWT.BORDER);
		textCollectionName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnExistOnDelete = new Button(compositeQueryTail, SWT.CHECK);
		btnExistOnDelete.setText(Messages.MongoDBImportEditor_10);
		
//		Button btnPreview = new Button(compositeQueryTail, SWT.NONE);
//		btnPreview.setText("Preview");
		
		initEditor();
	}
	
	private void initCombo() {
		tableColumnListComposite.init( (UserDBDAO)comboDBList.getData(comboDBList.getText()) );
		tableColumnListComposite.layout();
	}
	
	/**
	 * 화면을 초기화 합니다.
	 */
	private void initEditor() {
		tabFolderQuery.setSelection(1);
		
		try {
			int visibleItemCount = 0;
			List<UserDBDAO> userDBS = TadpoleSystem_UserDBQuery.getUserDB();
			for (UserDBDAO userDBDAO : userDBS) {
				// 임포트 하려는 자신은 제외 
				if(userDB.getSeq() != userDBDAO.getSeq()) {
					comboDBList.add(userDBDAO.getDisplay_name());
					comboDBList.setData(userDBDAO.getDisplay_name(), userDBDAO);
					visibleItemCount++;
				}
			}
			comboDBList.setVisibleItemCount(visibleItemCount);
			comboDBList.select(0);
			initCombo();
			
		} catch (Exception e) {
			logger.error(Messages.MongoDBImportEditor_8, e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.MongoDBImportEditor_8, errStatus); //$NON-NLS-1$
		}
	}
	
	/**
	 * data import
	 */
	private void importData() {
		// 예외케이스를 리턴합니다.
		if(tabFolderQuery.getSelectionIndex() == 0) {			
			if(tableColumnListComposite.getSelectListTables().isEmpty()) return;
		} else if(tabFolderQuery.getSelectionIndex() == 1) {			
			if("".equals(textCollectionName.getText().trim())) { //$NON-NLS-1$
				MessageDialog.openError(null, Messages.MongoDBImportEditor_14, Messages.MongoDBImportEditor_3);
				return;
			}
		}
		
		// job make
		final UserDBDAO exportDBDAO = (UserDBDAO)comboDBList.getData(comboDBList.getText());
		Job job = null;		
		if(MessageDialog.openConfirm(null, "Confirm", Messages.MongoDBImportEditor_1)) {	 //$NON-NLS-1$
			if(tabFolderQuery.getSelectionIndex() == 0) {
				
				DBImport dbImport = null;
				if(userDB != null && DBDefine.MONGODB_DEFAULT == DBDefine.getDBDefine(userDB.getTypes())) {
					dbImport = new MongoDBCollectionToMongodBImport(userDB, tableColumnListComposite.getSelectListTables(), exportDBDAO);
				} else {
					dbImport = new RDBTableToMongoDBImport(userDB, tableColumnListComposite.getSelectListTables(), exportDBDAO);
				}
				job = dbImport.workTableImport();
				if(job == null) return;
				
				
			} else if(tabFolderQuery.getSelectionIndex() == 1) {	
				if(userDB != null && DBDefine.MONGODB_DEFAULT == DBDefine.getDBDefine(userDB.getTypes())) {
					MessageDialog.openInformation(null, "Confirm", "Not support MongoDB.");
					return;
				} else {
					QueryToMongoDBImport importData = new QueryToMongoDBImport(userDB, textCollectionName.getText(), textQuery.getText(), exportDBDAO, btnExistOnDelete.getSelection());
					job = importData.workTableImport();
					if(job == null) return;
				}
			}
		} else return;
		
		// job listener
		job.addJobChangeListener(new JobChangeAdapter() {			
			public void done(IJobChangeEvent event) {
				final IJobChangeEvent jobEvent = event; 
				getSite().getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if(jobEvent.getResult().isOK()) {
							MessageDialog.openInformation(null, "Confirm", Messages.MongoDBImportEditor_11); //$NON-NLS-1$
						} else {				
							ExceptionDetailsErrorDialog.openError(null, "Error", Messages.MongoDBImportEditor_12, jobEvent.getResult()); //$NON-NLS-1$
						}						
					}					
				});	// end display.asyncExec
			}	// end done			
		});	// end job
		
		job.setName(userDB.getDisplay_name());
		job.setUser(true);
		job.schedule();	
	}

	@Override
	public void setFocus() {
	}

}
