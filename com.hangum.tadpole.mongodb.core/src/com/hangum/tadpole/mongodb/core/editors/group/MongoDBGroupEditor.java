package com.hangum.tadpole.mongodb.core.editors.group;

import org.apache.log4j.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.editor.core.widgets.editor.TadpoleOrionHubEditor;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.composite.result.MongodbResultComposite;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * Mongodb Group Editor
 * 
 * @author hangum
 *
 */
public class MongoDBGroupEditor extends EditorPart {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MongoDBGroupEditor.class);

	/** editor id */
	public static String ID = "com.hangum.tadpole.mongodb.core.editor.group";
	
	/** initial collection name */
	private String initColName;
	private UserDBDAO userDB;
	private TadpoleOrionHubEditor textKeys;
	private TadpoleOrionHubEditor textQuery;
	private TadpoleOrionHubEditor textInitialValue;
	private TadpoleOrionHubEditor textReduceFunction;
	private TadpoleOrionHubEditor textFinalizeFunction;

	private MongodbResultComposite compositeResult ;
	
	public MongoDBGroupEditor() {
		super();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		
		MongoDBGroupEditorInput editInput = (MongoDBGroupEditorInput)input;
		setPartName(editInput.getName());
		
		this.initColName = editInput.getColname();
		this.userDB = editInput.getUserDB();
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 1;
		gl_parent.horizontalSpacing = 1;
		gl_parent.marginHeight = 1;
		gl_parent.marginWidth = 1;
		parent.setLayout(gl_parent);
		
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeMainSearch = new Composite(sashForm, SWT.NONE);
		GridLayout gl_compositeMainSearch = new GridLayout(1, false);
		gl_compositeMainSearch.verticalSpacing = 0;
		gl_compositeMainSearch.horizontalSpacing = 0;
		gl_compositeMainSearch.marginHeight = 0;
		gl_compositeMainSearch.marginWidth = 0;
		compositeMainSearch.setLayout(gl_compositeMainSearch);
		
		Composite compositeSearch = new Composite(compositeMainSearch, SWT.NONE);
		compositeSearch.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeSearch = new GridLayout(3, false);
		gl_compositeSearch.marginHeight = 1;
		gl_compositeSearch.verticalSpacing = 1;
		gl_compositeSearch.horizontalSpacing = 1;
		gl_compositeSearch.marginWidth = 1;
		compositeSearch.setLayout(gl_compositeSearch);
		
		Group grpKeys = new Group(compositeSearch, SWT.NONE);
		GridLayout gl_grpKeys = new GridLayout(1, false);
		gl_grpKeys.verticalSpacing = 1;
		gl_grpKeys.horizontalSpacing = 1;
		gl_grpKeys.marginHeight = 1;
		gl_grpKeys.marginWidth = 1;
		grpKeys.setLayout(gl_grpKeys);
		grpKeys.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpKeys.setText("Keys");
		
		textKeys = new TadpoleOrionHubEditor(grpKeys, SWT.BORDER);
		textKeys.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpQuery = new Group(compositeSearch, SWT.NONE);
		grpQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpQuery.setSize(50, 28);
		grpQuery.setText("Query");
		GridLayout gl_grpQuery = new GridLayout(1, false);
		gl_grpQuery.verticalSpacing = 1;
		gl_grpQuery.horizontalSpacing = 1;
		gl_grpQuery.marginHeight = 1;
		gl_grpQuery.marginWidth = 1;
		grpQuery.setLayout(gl_grpQuery);
		
		textQuery = new TadpoleOrionHubEditor(grpQuery, SWT.BORDER);
		textQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpInitialValue = new Group(compositeSearch, SWT.NONE);
		grpInitialValue.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpInitialValue.setText("Initial Value");
		GridLayout gl_grpInitialValue = new GridLayout(1, false);
		gl_grpInitialValue.verticalSpacing = 1;
		gl_grpInitialValue.horizontalSpacing = 1;
		gl_grpInitialValue.marginHeight = 1;
		gl_grpInitialValue.marginWidth = 1;
		grpInitialValue.setLayout(gl_grpInitialValue);
		
		textInitialValue = new TadpoleOrionHubEditor(grpInitialValue, SWT.BORDER);
		textInitialValue.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeFunction = new Composite(compositeMainSearch, SWT.NONE);
		compositeFunction.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeFunction = new GridLayout(2, false);
		gl_compositeFunction.verticalSpacing = 1;
		gl_compositeFunction.horizontalSpacing = 1;
		gl_compositeFunction.marginHeight = 1;
		gl_compositeFunction.marginWidth = 1;
		compositeFunction.setLayout(gl_compositeFunction);
		
		Group grpReductJavascriptFunction = new Group(compositeFunction, SWT.NONE);
		GridLayout gl_grpReductJavascriptFunction = new GridLayout(1, false);
		gl_grpReductJavascriptFunction.verticalSpacing = 1;
		gl_grpReductJavascriptFunction.horizontalSpacing = 1;
		gl_grpReductJavascriptFunction.marginWidth = 1;
		gl_grpReductJavascriptFunction.marginHeight = 1;
		grpReductJavascriptFunction.setLayout(gl_grpReductJavascriptFunction);
		grpReductJavascriptFunction.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpReductJavascriptFunction.setText("Reduce JavaScript Function");
		
		textReduceFunction = new TadpoleOrionHubEditor(grpReductJavascriptFunction, SWT.BORDER);
		textReduceFunction.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpFinalizeJavascriptFunction = new Group(compositeFunction, SWT.NONE);
		GridLayout gl_grpFinalizeJavascriptFunction = new GridLayout(1, false);
		gl_grpFinalizeJavascriptFunction.verticalSpacing = 1;
		gl_grpFinalizeJavascriptFunction.horizontalSpacing = 1;
		gl_grpFinalizeJavascriptFunction.marginHeight = 1;
		gl_grpFinalizeJavascriptFunction.marginWidth = 1;
		grpFinalizeJavascriptFunction.setLayout(gl_grpFinalizeJavascriptFunction);
		grpFinalizeJavascriptFunction.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpFinalizeJavascriptFunction.setText("Finalize JavaScript Function");
		grpFinalizeJavascriptFunction.setBounds(0, 0, 70, 82);
		
		textFinalizeFunction = new TadpoleOrionHubEditor(grpFinalizeJavascriptFunction, SWT.BORDER);
		textFinalizeFunction.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeBtn = new Composite(compositeMainSearch, SWT.NONE);
		compositeBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeBtn = new GridLayout(2, false);
		gl_compositeBtn.verticalSpacing = 0;
		gl_compositeBtn.horizontalSpacing = 0;
		gl_compositeBtn.marginHeight = 0;
		gl_compositeBtn.marginWidth = 0;
		compositeBtn.setLayout(gl_compositeBtn);
		
		Label label = new Label(compositeBtn, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		label.setBounds(0, 0, 56, 15);
		
		Button btnSearch = new Button(compositeBtn, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					search();
				} catch (Exception e1) {
					logger.error("MapReduce Error", e1); //$NON-NLS-1$
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e1.getMessage(), e1); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(null, "Error", "MapReduce Search exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		});
		btnSearch.setText("Search");
		
		compositeResult = new MongodbResultComposite(sashForm, SWT.NONE, userDB, initColName, false);
		compositeResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 0;
		gl_compositeResult.horizontalSpacing = 0;
		gl_compositeResult.marginHeight = 0;
		gl_compositeResult.marginWidth = 0;
		compositeResult.setLayout(gl_compositeResult);
		
		sashForm.setWeights(new int[] {7, 3});
	}

	/**
	 * 검색
	 * 
	 * @throws Exception
	 */

	DBObject resultDBObject = null;	
	private void search() throws Exception {
		final DBCollection dbCol = MongoDBQuery.findCollection(userDB, initColName);
		
		final DBObject dbObjectKey 		= (DBObject)JSON.parse(textKeys.getText());
		final DBObject dbObjectInitial 	= (DBObject)JSON.parse(textInitialValue.getText());
		final DBObject dbObjectCondition 	= (DBObject)JSON.parse(textQuery.getText());
		
		final String strReductFunction 	=  textReduceFunction.getText();
		final String strFinalize 		= textFinalizeFunction.getText();
		
		
		// search job
		Job job = new Job("Group Search job") { //$NON-NLS-1$
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Starting JSON query...", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
				try {
					resultDBObject = dbCol.group(dbObjectKey, dbObjectCondition, dbObjectInitial, strReductFunction, strFinalize);
					
				} catch (Exception e) {
					logger.error("Group exception", e); //$NON-NLS-1$
					return new Status(Status.WARNING,Activator.PLUGIN_ID, "Group " + e.getMessage()); //$NON-NLS-1$
				} finally {
					monitor.done();
				}
				
				return Status.OK_STATUS;
			}
		};
		
		// job의 event를 처리해 줍니다.
		job.addJobChangeListener(new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {
	
				final IJobChangeEvent jobEvent = event; 
				getSite().getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if(jobEvent.getResult().isOK()) {
							try {
								compositeResult.refreshDBView(resultDBObject, 0);
								compositeResult.setResult();
							} catch(Exception e) {
								logger.error("MapReduce Error", e); //$NON-NLS-1$
								Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
								ExceptionDetailsErrorDialog.openError(null, "Error", "MapReduce execute exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
							}
						} else {
//							compositeResult.errorView(jobEvent.getResult().getMessage());
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
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}
	
	@Override
	public void setFocus() {
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}
}
