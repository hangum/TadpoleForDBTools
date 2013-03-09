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
package com.hangum.tadpole.mongodb.core.editors.main;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.dao.mongodb.CollectionFieldDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.editor.core.widgets.editor.TadpoleOrionHubEditor;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.composite.result.MongodbResultComposite;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.util.TadpoleWidgetUtils;
import com.swtdesigner.SWTResourceManager;

/**
 * mongodb find editor
 * 
 * @author hangum
 *
 */
public class MongoDBTableEditor extends EditorPart {
	public static final String  ID = "com.hangum.tadpole.mongodb.core.main.editor"; //$NON-NLS-1$
	static Logger logger = Logger.getLogger(MongoDBTableEditor.class);
	
	/** userDB */
	private UserDBDAO userDB;
	/** collectionName */
	private String initColName;
	
	/** search panel */
	private CTabFolder tabFolderSearchPanel;
	
	/** collection column info */
	private Map<String, CollectionFieldDAO> columnInfo;
	/** collection의 전체 컬럼 */
	private String strColumns = ""; //$NON-NLS-1$
	
	/** 쿼리 결과 출력 */
	private MongodbResultComposite compositeResult ;

	private TadpoleOrionHubEditor textBasicFind;
	private TadpoleOrionHubEditor textBasicField;
	private TadpoleOrionHubEditor textBasicSort;
	private Text textBasicSkip;
	private Text textBasicLimit;
	
	/** preference default find page */
	private String defaultFindPage 	= GetPreferenceGeneral.getMongoDefaultFindPage();

	/** preference default limit */
	private String defaultLimit = GetPreferenceGeneral.getMongoDefaultLimit();
	
	public MongoDBTableEditor() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 2;
		gl_parent.horizontalSpacing = 2;
		gl_parent.marginHeight = 2;
		gl_parent.marginWidth = 2;
		parent.setLayout(gl_parent);
		
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setSashWidth(1);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tabFolderSearchPanel = new CTabFolder(sashForm, SWT.NONE);
		tabFolderSearchPanel.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());
		
		CTabItem tabItemBasic = new CTabItem(tabFolderSearchPanel, SWT.NONE);
		tabItemBasic.setText("JSON Search"); //$NON-NLS-1$
		
		Composite compositeBasic = new Composite(tabFolderSearchPanel, SWT.NONE);
		tabItemBasic.setControl(compositeBasic);
		GridLayout gl_compositeBasic = new GridLayout(1, false);
		gl_compositeBasic.verticalSpacing = 0;
		gl_compositeBasic.horizontalSpacing = 0;
		gl_compositeBasic.marginHeight = 0;
		gl_compositeBasic.marginWidth = 0;
		compositeBasic.setLayout(gl_compositeBasic);
		
		Composite compositeBasicHead = new Composite(compositeBasic, SWT.NONE);
		compositeBasicHead.setLayout(new GridLayout(4, false));
		compositeBasicHead.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblfind = new Label(compositeBasicHead, SWT.NONE);
		lblfind.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 2));
		lblfind.setText(Messages.MongoDBTableEditor_0);
		
		textBasicFind = new TadpoleOrionHubEditor(compositeBasicHead, SWT.BORDER);// | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textBasicFind.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		
		Label lblfield = new Label(compositeBasicHead, SWT.NONE);
		lblfield.setText(Messages.MongoDBTableEditor_1);
		
		textBasicField = new TadpoleOrionHubEditor(compositeBasicHead, SWT.BORDER);// | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textBasicField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblsort = new Label(compositeBasicHead, SWT.NONE);
		lblsort.setText(Messages.MongoDBTableEditor_2);
		
		textBasicSort = new TadpoleOrionHubEditor(compositeBasicHead, SWT.BORDER);// | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textBasicSort.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeBasicSearch = new Composite(compositeBasic, SWT.NONE);
		GridLayout gl_compositeBasicSearch = new GridLayout(10, false);
		gl_compositeBasicSearch.verticalSpacing = 2;
		gl_compositeBasicSearch.horizontalSpacing = 2;
		gl_compositeBasicSearch.marginHeight = 2;
		gl_compositeBasicSearch.marginWidth = 2;
		compositeBasicSearch.setLayout(gl_compositeBasicSearch);
		compositeBasicSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label = new Label(compositeBasicSearch, SWT.NONE);
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label.widthHint = 30;
		label.setLayoutData(gd_label);
		
		Label lblSkip_1 = new Label(compositeBasicSearch, SWT.NONE);
		lblSkip_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSkip_1.setText("Skip"); //$NON-NLS-1$
		
		textBasicSkip = new Text(compositeBasicSearch, SWT.BORDER | SWT.RIGHT);
		textBasicSkip.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection)	findBasic();
			}
		});
		textBasicSkip.setText("0"); //$NON-NLS-1$
		textBasicSkip.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		GridData gd_textBasicSkip = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_textBasicSkip.widthHint = 60;
		textBasicSkip.setLayoutData(gd_textBasicSkip);
		
		Label lblLimit_1 = new Label(compositeBasicSearch, SWT.NONE);
		lblLimit_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLimit_1.setText("Limit"); //$NON-NLS-1$
		
		textBasicLimit = new Text(compositeBasicSearch, SWT.BORDER | SWT.RIGHT);
		textBasicLimit.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection)	findBasic();
			}
		});
		textBasicLimit.setText(defaultLimit); //$NON-NLS-1$
		textBasicLimit.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		GridData gd_textBasicLimit = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_textBasicLimit.widthHint = 60;
		textBasicLimit.setLayoutData(gd_textBasicLimit);
		
		Label label_1 = new Label(compositeBasicSearch, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnExecutePlan = new Button(compositeBasicSearch, SWT.NONE);
		btnExecutePlan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				compositeResult.consoleExecutePlan();
			}
		});
		btnExecutePlan.setText(Messages.MongoDBTableEditor_3);
		
		Button btnBasicLastCommandConsole = new Button(compositeBasicSearch, SWT.NONE);
		btnBasicLastCommandConsole.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				compositeResult.consoleError();
			}
		});
		btnBasicLastCommandConsole.setText(Messages.MongoDBTableEditor_4);
		
		Label label_2 = new Label(compositeBasicSearch, SWT.NONE);
		GridData gd_label_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label_2.widthHint = 10;
		label_2.setLayoutData(gd_label_2);
		
		Button btnBasicSearch = new Button(compositeBasicSearch, SWT.NONE);
		btnBasicSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findBasic();
			}
		});
		btnBasicSearch.setText(Messages.MongoDBTableEditor_5);
		
		compositeResult = new MongodbResultComposite(sashForm, SWT.NONE, userDB, initColName, true);
		compositeResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 0;
		gl_compositeResult.horizontalSpacing = 0;
		gl_compositeResult.marginHeight = 0;
		gl_compositeResult.marginWidth = 0;
		compositeResult.setLayout(gl_compositeResult);
		
		sashForm.setWeights(new int[] {40, 60});
		
		initCollection();
	}
	
	/**
	 * collection을 초기화 한다.
	 */
	private void initCollection() {		
		if(PreferenceDefine.MONGO_DEFAULT_FIND_BASIC.equals(defaultFindPage)) {
			tabFolderSearchPanel.setSelection(0);
		}
		
//		findBasic();
		compositeResult.find("", "", "", getCntSkip(), getCntLimit()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		

		textBasicFind.setFocus();
	}
	
	/**
	 * 검색 기본
	 */
	private void findBasic() {
		final String strBasicFind = textBasicFind.getText().trim();
		final String strBasicField = textBasicField.getText().trim();
		final String strBasicSort = textBasicSort.getText().trim();
		final int cntSkip = getCntSkip();
		final int cntLimit = getCntLimit();
		
		compositeResult.find(strBasicField, strBasicFind, strBasicSort, cntSkip, cntLimit);
	}
	
	/**
	 * text cnt skip
	 * @return
	 */
	private int getCntSkip() {
		try {
			return Integer.parseInt(textBasicSkip.getText());
		} catch(Exception e) {
			return 0;
		}

	}
	
	/**
	 * text cnt term
	 * @return
	 */
	private int getCntLimit() {
		try {
			return Integer.parseInt(textBasicLimit.getText());
		} catch(Exception e) {
			return Integer.parseInt(defaultLimit);
		}
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
		
		MongoDBEditorInput moInput = (MongoDBEditorInput)input;
		this.userDB = moInput.getUserDB();
		this.initColName = moInput.getCollectionName();
		this.columnInfo = new HashMap<String, CollectionFieldDAO>();
		for (int i=0; i<moInput.getShowTableColumns().size(); i++) {
			CollectionFieldDAO tcDAO = (CollectionFieldDAO)moInput.getShowTableColumns().get(i);
			columnInfo.put(tcDAO.getField(), tcDAO);

			strColumns += tcDAO.getField() + ", "; //$NON-NLS-1$
		}
		strColumns = StringUtils.chompLast(strColumns, ", "); //$NON-NLS-1$
		
		setPartName(moInput.getName());		 //$NON-NLS-1$ //$NON-NLS-2$
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
