/*******************************************************************************
	 * Copyright (c) 2013 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.mongodb.core.editors.mapreduce;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.util.TadpoleWidgetUtils;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceCommand.OutputType;
import com.mongodb.MapReduceOutput;
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
	
	private String strMap = "function() {\r" + TadpoleWidgetUtils.TAB_CONETNT + " emit(this.user_id, 1); \r}";
	private String strReduce = "function(k,values) {\r" + TadpoleWidgetUtils.TAB_CONETNT + " return 1; \r}";
	private String strFinalize = "";//"function Finalize(key, reduced) {\r" + TadpoleWidgetUtils.TAB_CONETNT + " return reduced; \r}";
	
	/** 초기에 선택된 collection name */
	private UserDBDAO userDB;
	private String initColName = "";
	
	private Text textMap;
	private Text textReduce;
	private Text textFinalize;
	
	
	// output
	private Combo comboOutputType;
	private Text textQuery;
	private Text textLimit;
	private Text textSort;
	private Text textOutputTarget;

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
		
		textMap = new Text(grpMap, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textMap.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		textMap.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			
				if(e.stateMask == 0 && e.keyCode == SWT.TAB) {
					textMap.insert(TadpoleWidgetUtils.TAB_CONETNT);
				}
			}
		});
		textMap.setData( RWT.CANCEL_KEYS, new String[] { "TAB" } );
		
		Group grpReduce = new Group(sashFormMRF, SWT.NONE);
		grpReduce.setText("Reduce");
		GridLayout gl_grpReduce = new GridLayout(1, false);
		gl_grpReduce.verticalSpacing = 1;
		gl_grpReduce.horizontalSpacing = 1;
		gl_grpReduce.marginHeight = 1;
		gl_grpReduce.marginWidth = 1;
		grpReduce.setLayout(gl_grpReduce);
		
		textReduce = new Text(grpReduce, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textReduce.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		textReduce.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			
				if(e.stateMask == 0 && e.keyCode == SWT.TAB) {
					textReduce.insert(TadpoleWidgetUtils.TAB_CONETNT);
				}
			}
		});
		textReduce.setData( RWT.CANCEL_KEYS, new String[] { "TAB" } );
		
		Group grpFinalize = new Group(sashFormMRF, SWT.NONE);
		grpFinalize.setText("Finalize");
		GridLayout gl_grpFinalize = new GridLayout(1, false);
		gl_grpFinalize.verticalSpacing = 1;
		gl_grpFinalize.horizontalSpacing = 1;
		gl_grpFinalize.marginHeight = 1;
		gl_grpFinalize.marginWidth = 1;
		grpFinalize.setLayout(gl_grpFinalize);
		
		textFinalize = new Text(grpFinalize, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textFinalize.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		textFinalize.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			
				if(e.stateMask == 0 && e.keyCode == SWT.TAB) {
					textFinalize.insert(TadpoleWidgetUtils.TAB_CONETNT);
				}
			}
		});
		textFinalize.setData( RWT.CANCEL_KEYS, new String[] { "TAB" } );
		
		sashFormMRF.setWeights(new int[] {4, 4, 2});
		
		Composite compositeInOut = new Composite(sashFormMain, SWT.NONE);
		compositeInOut.setLayout(new GridLayout(1, false));
		
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
		
		textQuery = new Text(grpInput, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		textQuery.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			
				if(e.stateMask == 0 && e.keyCode == SWT.TAB) {
					textQuery.insert(TadpoleWidgetUtils.TAB_CONETNT);
				}
			}
		});
		textQuery.setData( RWT.CANCEL_KEYS, new String[] { "TAB" } );
		
		Label lblSort = new Label(grpInput, SWT.NONE);
		lblSort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSort.setText("Sort");
		
		textSort = new Text(grpInput, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		textSort.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		textSort.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			
				if(e.stateMask == 0 && e.keyCode == SWT.TAB) {
					textSort.insert(TadpoleWidgetUtils.TAB_CONETNT);
				}
			}
		});
		textSort.setData( RWT.CANCEL_KEYS, new String[] { "TAB" } );
		
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
//		comboOutputType.setEnabled(false);
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
		
		Button btnExecute = new Button(grpOutput_1, SWT.NONE);
		btnExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				executeMapReduce();
			}
		});
		btnExecute.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnExecute.setText("Execute");
		
		sashForm.setWeights(new int[] {1, 1});
		
		Composite compositeResult = new Composite(sashFormMain, SWT.NONE);
		compositeResult.setLayout(new GridLayout(1, false));
		
		TreeViewer treeViewer = new TreeViewer(compositeResult, SWT.BORDER);
		Tree tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		sashFormMain.setWeights(new int[] {40, 30, 30});
		
		initUI();
	}
	
	/**
	 * execute map reduce
	 */
	private void executeMapReduce() {
		String strMap 		= textMap.getText();
		String strReduce 	= textReduce.getText();
		String strFinilize 	= textFinalize.getText();
		String strOutputTarget = textOutputTarget.getText();
		MapReduceCommand.OutputType outputType = (MapReduceCommand.OutputType)comboOutputType.getData(comboOutputType.getText());
		
		DBObject dbQuery = null;
		if(!"".equals(textQuery.getText())) {
			dbQuery = (DBObject) JSON.parse(textQuery.getText());
			logger.debug("[dbQuery]" + dbQuery.toString());
		}
		
		DBObject dbSort = null;
		if(!"".equals(textSort.getText())) dbSort = (DBObject) JSON.parse(textSort.getText());
		
		int intLimit = 0;
		try {
			intLimit = Integer.parseInt(textLimit.getText());
		} catch(Exception e) {}
		
		try {
			Object outObj = MongoDBQuery.mapReduce(userDB, initColName, strMap, strReduce, strFinilize, strOutputTarget, outputType, dbQuery, dbSort, intLimit);
//			for(DBObject dbObj : out.results()) {
//				System.out.println(dbObj);
//			}

		} catch(Exception e) {
			logger.error("MapReduce execute exception.", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", "An error has occurred.", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * ui초기화 작업을 합니다.
	 */
	private void initUI() {
		textMap.setText(strMap);
		textReduce.setText(strReduce);
		textFinalize.setText(strFinalize);
		
		textMap.setFocus();
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
