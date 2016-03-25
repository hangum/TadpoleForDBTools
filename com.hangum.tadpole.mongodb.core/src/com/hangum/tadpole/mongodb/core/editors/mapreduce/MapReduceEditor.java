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
package com.hangum.tadpole.mongodb.core.editors.mapreduce;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.widgets.TadpoleEditorWidget;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.composite.result.MongodbResultComposite;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.mongodb.core.utils.CollectionUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceCommand.OutputType;
import com.mongodb.util.JSON;

/**
 * MapReduce editor
 * 
 * @author hangum
 *
 */
public class MapReduceEditor extends EditorPart {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MapReduceEditor.class);

	public static final String ID = "com.hangum.tadpole.mongodb.core.ext.editor.mapreduce";
	
	private String TEMPLATE_MAP_SRC 	= "function() {\r" + TadpoleWidgetUtils.TAB_CONETNT + " emit(this.user_id, 1); \r}";
	private String TEMPLATE_REDUCE_SRC 	= "function(k,values) {\r" + TadpoleWidgetUtils.TAB_CONETNT + " return 1; \r}";
	private String TEMPLATE_FINALIZE_SRC = "";//"function Finalize(key, reduced) {\r" + TadpoleWidgetUtils.TAB_CONETNT + " return reduced; \r}";
	
	/** 초기에 선택된 collection name */
	private UserDBDAO userDB;
	private String initColName = "";
	
	private TadpoleEditorWidget textMap;
	private TadpoleEditorWidget textReduce;
	private TadpoleEditorWidget textFinalize;
	
	// output
	private Combo comboOutputType;
	private TadpoleEditorWidget textQuery;
	private Text textLimit;
	private TadpoleEditorWidget textSort;
	private Text textOutputTarget;
	
	private Button btnSharded;
	private Button btnNoneAtomic; 
	private Button btnJsMode;
	
	/** 쿼리 결과 출력 */
	private MongodbResultComposite compositeResult ;

	public MapReduceEditor() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 0;
		gl_parent.horizontalSpacing = 0;
		gl_parent.marginHeight = 0;
		gl_parent.marginWidth = 0;
		parent.setLayout(gl_parent);
		
		SashForm sashFormMain = new SashForm(parent, SWT.VERTICAL);
		sashFormMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeMapReduceFinalize = new Composite(sashFormMain, SWT.NONE);
		GridLayout gl_compositeMapReduceFinalize = new GridLayout(1, false);
		gl_compositeMapReduceFinalize.verticalSpacing = 1;
		gl_compositeMapReduceFinalize.horizontalSpacing = 1;
		gl_compositeMapReduceFinalize.marginHeight = 1;
		gl_compositeMapReduceFinalize.marginWidth = 1;
		compositeMapReduceFinalize.setLayout(gl_compositeMapReduceFinalize);
		
		SashForm sashFormMRF = new SashForm(compositeMapReduceFinalize, SWT.NONE);
		sashFormMRF.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpMap = new Group(sashFormMRF, SWT.NONE);
		grpMap.setText("Map");
		GridLayout gl_grpMap = new GridLayout(1, false);
		gl_grpMap.verticalSpacing = 1;
		gl_grpMap.horizontalSpacing = 1;
		gl_grpMap.marginHeight = 1;
		gl_grpMap.marginWidth = 1;
		grpMap.setLayout(gl_grpMap);
		
		String strAssist = CollectionUtils.getAssistList(userDB, initColName);
		
		textMap = new TadpoleEditorWidget(grpMap, SWT.BORDER, EditorDefine.EXT_JSON, "", strAssist);
		textMap.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpReduce = new Group(sashFormMRF, SWT.NONE);
		grpReduce.setText("Reduce");
		GridLayout gl_grpReduce = new GridLayout(1, false);
		gl_grpReduce.verticalSpacing = 1;
		gl_grpReduce.horizontalSpacing = 1;
		gl_grpReduce.marginHeight = 1;
		gl_grpReduce.marginWidth = 1;
		grpReduce.setLayout(gl_grpReduce);
		
		textReduce = new TadpoleEditorWidget(grpReduce, SWT.BORDER, EditorDefine.EXT_JSON, "", strAssist);
		textReduce.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpFinalize = new Group(sashFormMRF, SWT.NONE);
		grpFinalize.setText("Finalize");
		GridLayout gl_grpFinalize = new GridLayout(1, false);
		gl_grpFinalize.verticalSpacing = 1;
		gl_grpFinalize.horizontalSpacing = 1;
		gl_grpFinalize.marginHeight = 1;
		gl_grpFinalize.marginWidth = 1;
		grpFinalize.setLayout(gl_grpFinalize);
		
		textFinalize = new TadpoleEditorWidget(grpFinalize, SWT.BORDER, EditorDefine.EXT_JSON, "", strAssist);
		textFinalize.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		sashFormMRF.setWeights(new int[] {4, 4, 2});
		
		Composite compositeInOut = new Composite(sashFormMain, SWT.NONE);
		GridLayout gl_compositeInOut = new GridLayout(1, false);
		gl_compositeInOut.verticalSpacing = 1;
		gl_compositeInOut.horizontalSpacing = 1;
		gl_compositeInOut.marginHeight = 1;
		gl_compositeInOut.marginWidth = 1;
		compositeInOut.setLayout(gl_compositeInOut);
		
		SashForm sashForm = new SashForm(compositeInOut, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpInput = new Group(sashForm, SWT.NONE);
		grpInput.setText("Input");
		GridLayout gl_grpInput = new GridLayout(2, false);
		gl_grpInput.verticalSpacing = 2;
		gl_grpInput.horizontalSpacing = 2;
		gl_grpInput.marginHeight = 2;
		gl_grpInput.marginWidth = 2;
		grpInput.setLayout(gl_grpInput);
		
		Label lblQuery = new Label(grpInput, SWT.NONE);
		lblQuery.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblQuery.setText("Query");
		
		textQuery = new TadpoleEditorWidget(grpInput, SWT.BORDER, EditorDefine.EXT_JSON, "", strAssist);
		textQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblSort = new Label(grpInput, SWT.NONE);
		lblSort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSort.setText("Sort");
		
		textSort = new TadpoleEditorWidget(grpInput, SWT.BORDER, EditorDefine.EXT_JSON, "", strAssist);
		textSort.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblLimit = new Label(grpInput, SWT.NONE);
		lblLimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLimit.setText("Limit");
		
		textLimit = new Text(grpInput, SWT.BORDER);
		textLimit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpOutput_1 = new Group(sashForm, SWT.NONE);
		grpOutput_1.setText("Output");
		GridLayout gl_grpOutput_1 = new GridLayout(2, false);
		gl_grpOutput_1.verticalSpacing = 2;
		gl_grpOutput_1.horizontalSpacing = 2;
		gl_grpOutput_1.marginHeight = 2;
		gl_grpOutput_1.marginWidth = 2;
		grpOutput_1.setLayout(gl_grpOutput_1);
		
		Label lblType = new Label(grpOutput_1, SWT.NONE);
		lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblType.setText("Type");
		
		comboOutputType = new Combo(grpOutput_1, SWT.READ_ONLY);
		comboOutputType.setEnabled(false);
		comboOutputType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for (OutputType outputType : MapReduceCommand.OutputType.values()) {
			comboOutputType.add(outputType.toString());
			comboOutputType.setData(outputType.toString(), outputType);
		}
		comboOutputType.select(3);
		
		Label lblOutput = new Label(grpOutput_1, SWT.NONE);
		lblOutput.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOutput.setText("Output");
		
		textOutputTarget = new Text(grpOutput_1, SWT.BORDER);
		textOutputTarget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(grpOutput_1, SWT.NONE);
		
		btnSharded = new Button(grpOutput_1, SWT.CHECK);
		btnSharded.setText("Sharded ");
		new Label(grpOutput_1, SWT.NONE);
		
		btnNoneAtomic = new Button(grpOutput_1, SWT.CHECK);
		btnNoneAtomic.setText("None Atomic");
		new Label(grpOutput_1, SWT.NONE);
		
		btnJsMode = new Button(grpOutput_1, SWT.CHECK);
		btnJsMode.setText("JS Mode");
		new Label(grpOutput_1, SWT.NONE);
		
		Button btnExecute = new Button(grpOutput_1, SWT.NONE);
		btnExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					executeMapReduce();
				} catch (Exception e1) {
					logger.error("MapReduce Error", e1); //$NON-NLS-1$
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e1.getMessage(), e1); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "MapReduce Search exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		});
		btnExecute.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnExecute.setText("Search");
		
		sashForm.setWeights(new int[] {1, 1});
		
		compositeResult = new MongodbResultComposite(sashFormMain, SWT.NONE, userDB, initColName, false);
		compositeResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 0;
		gl_compositeResult.horizontalSpacing = 0;
		gl_compositeResult.marginHeight = 0;
		gl_compositeResult.marginWidth = 0;
		compositeResult.setLayout(gl_compositeResult);
		
		sashFormMain.setWeights(new int[] {40, 30, 30});
		
		initUI();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
	}
	
	/**
	 * execute map reduce
	 */
	private void executeMapReduce() throws Exception {
		String strMap 		= textMap.getText();
		String strReduce 	= textReduce.getText();
		String strFinilize 	= textFinalize.getText();
		String strOutputTarget = textOutputTarget.getText();
		MapReduceCommand.OutputType outputType = (MapReduceCommand.OutputType)comboOutputType.getData(comboOutputType.getText());
		
		DBObject dbQuery = null;
		if(!"".equals(textQuery.getText())) dbQuery = (DBObject) JSON.parse(textQuery.getText());
		
		DBObject dbSort = null;
		if(!"".equals(textSort.getText())) dbSort = (DBObject) JSON.parse(textSort.getText());
		
		// 쿼리 합니다.
		DBCollection dbCol = MongoDBQuery.findCollection(userDB, initColName);		
		MapReduceCommand mrCmd = new MapReduceCommand(dbCol, strMap, strReduce, strOutputTarget, outputType, dbQuery);
		if(!"".equals(strFinilize)) mrCmd.setFinalize(strFinilize);
		if(dbSort != null) mrCmd.setSort(dbSort);		
		if(getLimit() > 0) mrCmd.setLimit(getLimit());
		if(btnJsMode.getSelection()) {
			mrCmd.setJsMode(true);
		}
		
		final BasicDBObject searchObj = (BasicDBObject)mrCmd.toDBObject();   
		if(btnSharded.getSelection()) 	((BasicDBObject)searchObj.get("out")).put("sharded", true);
		if(btnNoneAtomic.getSelection()) ((BasicDBObject)searchObj.get("out")).put("nonAtomic", true);
		
		goMapReduce(dbCol, searchObj, outputType);
	}	
	
	/**
	 * 검색합니다.
	 * 
	 * @param dbCollection
	 * @param cmdSearchObj
	 * @param outputType
	 */
	CommandResult 	cmdResult = null;
//	MapReduceOutput mrOutput = null;
	private void goMapReduce(final DBCollection dbCol, final BasicDBObject basicObj, final MapReduceCommand.OutputType mrOType) {
		Job job = new Job("MapReduce search job") { //$NON-NLS-1$
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Starting JSON query...", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
				try {
				
//					if(mrOType == MapReduceCommand.OutputType.INLINE) {
//						cmdResult = dbCol.getDB().command(basicObj, dbCol.getOptions());
//						cmdResult.throwOnError();
//						mrOutput = new MapReduceOutput(dbCol, basicObj, cmdResult);
//					} else {
						cmdResult = dbCol.getDB().command(basicObj);
//						cmdResult.throwOnError();
//						mrOutput = new MapReduceOutput(dbCol, basicObj, cmdResult);
//					}
						if(!cmdResult.ok()) {
							throw cmdResult.getException();
						}
					
				} catch (Exception e) {
					logger.error("mapreduce exception", e); //$NON-NLS-1$
					return new Status(Status.WARNING,Activator.PLUGIN_ID, "mapreduce " + e.getMessage()); //$NON-NLS-1$
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
								Iterable<DBObject> iteResult = (Iterable<DBObject>)cmdResult.get("results");
								compositeResult.refreshDBView(iteResult, 0);
								compositeResult.setResult();
							} catch(Exception e) {
								logger.error("MapReduce Error", e); //$NON-NLS-1$
								Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
								ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "MapReduce execute exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
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
	
	private int getLimit() {
		try {
			return Integer.parseInt(textLimit.getText());
		} catch(Exception e) {
			return 0;
		}
	}
	
	/**
	 * ui초기화 작업을 합니다.
	 */
	private void initUI() {
//		textMap.setText(TEMPLATE_MAP_SRC);
//		textReduce.setText(TEMPLATE_REDUCE_SRC);
//		textFinalize.setText(TEMPLATE_FINALIZE_SRC);
		
		textMap.setFocus();
		
		// google analytic
		AnalyticCaller.track(MapReduceEditor.ID);
	}

	@Override
	public void setFocus() {
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		
		MapReduceEditorInput editInput = (MapReduceEditorInput)input;
		setPartName(editInput.getName());
		
		this.initColName = editInput.getColname();
		this.userDB = editInput.getUserDB();		
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}
