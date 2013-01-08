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
package com.hangum.tadpole.mongodb.core.ext.editors.mapreduce;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.util.TadpoleWidgetUtils;

/**
 * MapReduce editor
 * 
 * @author hangum
 *
 */
public class MapReduceEditor extends EditorPart {
	public static final String ID = "com.hangum.tadpole.mongodb.core.ext.editor.mapreduce";
	
	/** 초기에 선택된 collection name */
	private String initColName = "";
	
	private Text textMap;
	private Text textReduce;
	private Text textFinalize;

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
		
		Group grpInOut = new Group(sashFormMain, SWT.NONE);
		grpInOut.setText("In & Out");
		GridLayout gl_grpInOut = new GridLayout(1, false);
		gl_grpInOut.verticalSpacing = 1;
		gl_grpInOut.horizontalSpacing = 1;
		gl_grpInOut.marginHeight = 1;
		gl_grpInOut.marginWidth = 1;
		grpInOut.setLayout(gl_grpInOut);
		
		Group grpOutput = new Group(sashFormMain, SWT.NONE);
		grpOutput.setText("Output");
		GridLayout gl_grpOutput = new GridLayout(1, false);
		gl_grpOutput.verticalSpacing = 1;
		gl_grpOutput.horizontalSpacing = 1;
		gl_grpOutput.marginHeight = 1;
		gl_grpOutput.marginWidth = 1;
		grpOutput.setLayout(gl_grpOutput);
		
		sashFormMain.setWeights(new int[] {60, 20, 20});
		
		initUI();
	}
	
	/**
	 * ui초기화 작업을 합니다.
	 */
	private void initUI() {

		
	}

	@Override
	public void setFocus() {
		textMap.setFocus();
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
