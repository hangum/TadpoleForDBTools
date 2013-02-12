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
import com.hangum.tadpole.importdb.core.dialog.importdb.mongodb.QueryToMongoDBImport;
import com.hangum.tadpole.importdb.core.dialog.importdb.mongodb.RDBTableToMongoDBImport;
import com.hangum.tadpole.system.TadpoleSystem_UserDBQuery;

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
		
		Composite compositeHead = new Composite(parent, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(3, false));
		
		Label lblSource = new Label(compositeHead, SWT.NONE);
		lblSource.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSource.setText("Source"); //$NON-NLS-1$
		
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
		lblTarget.setText("Target"); //$NON-NLS-1$
		
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
		btnImport.setText("Import"); //$NON-NLS-1$
		
		Composite compositeBody = new Composite(parent, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tabFolderQuery = new CTabFolder(compositeBody, SWT.NONE);
		tabFolderQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		CTabItem tabItemTable = new CTabItem(tabFolderQuery, SWT.NONE);
		tabItemTable.setText("Table"); //$NON-NLS-1$
		
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
		btnSelectAll.setText("Select All"); //$NON-NLS-1$
		
		CTabItem tabItemQuery = new CTabItem(tabFolderQuery, SWT.NONE);
		tabItemQuery.setText("Query"); //$NON-NLS-1$
		
		Composite compositeQuery = new Composite(tabFolderQuery, SWT.NONE);
		tabItemQuery.setControl(compositeQuery);
		compositeQuery.setLayout(new GridLayout(1, false));
		
		textQuery = new TadpoleOrionHubEditor(compositeQuery, SWT.BORDER);// | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeQueryTail = new Composite(compositeQuery, SWT.NONE);
		compositeQueryTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeQueryTail.setLayout(new GridLayout(3, false));
		
		Label lblCollectionName = new Label(compositeQueryTail, SWT.NONE);
		lblCollectionName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCollectionName.setBounds(0, 0, 56, 15);
		lblCollectionName.setText("Collection Name"); //$NON-NLS-1$
		
		textCollectionName = new Text(compositeQueryTail, SWT.BORDER);
		textCollectionName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeQueryTail, SWT.NONE);
		
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
			logger.error(Messages.MongoDBImportEditor_8, e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.MongoDBImportEditor_8, errStatus); //$NON-NLS-1$
		}
	}
	
	/**
	 * data import
	 */
	private void importData() {
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
		// TODO Auto-generated method stub

	}

}
