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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.rwt.RWT;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.dao.mongodb.CollectionFieldDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dialogs.message.TadpoleSimpleMessageDialog;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.composite.result.MongodbResultComposite;
import com.hangum.tadpole.mongodb.core.define.MongoDBDefine;
import com.hangum.tadpole.mongodb.core.dto.MongodbTreeViewDTO;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.mongodb.core.utils.MongoDBTableColumn;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.util.JSONUtil;
import com.hangum.tadpole.util.TadpoleWidgetUtils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
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

	private Text textBasicFind;
	private Text textBasicField;
	private Text textBasicSort;
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
		lblfind.setText("{Find}"); //$NON-NLS-1$
		
		textBasicFind = new Text(compositeBasicHead, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textBasicFind.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			
				if(e.stateMask == 0 && e.keyCode == SWT.TAB) {
					textBasicFind.insert(TadpoleWidgetUtils.TAB_CONETNT);
				}
			}
		});
		textBasicFind.setData( RWT.CANCEL_KEYS, new String[] { "TAB" } );
		textBasicFind.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		textBasicFind.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		
		Label lblfield = new Label(compositeBasicHead, SWT.NONE);
		lblfield.setText("{Field}"); //$NON-NLS-1$
		
		textBasicField = new Text(compositeBasicHead, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textBasicField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			
				if(e.stateMask == 0 && e.keyCode == SWT.TAB) {
					textBasicField.insert(TadpoleWidgetUtils.TAB_CONETNT);
				}
			}
		});
		textBasicFind.setData( RWT.CANCEL_KEYS, new String[] { "TAB" } );
		textBasicField.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		textBasicField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblsort = new Label(compositeBasicHead, SWT.NONE);
		lblsort.setText("{Sort}"); //$NON-NLS-1$
		
		textBasicSort = new Text(compositeBasicHead, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textBasicSort.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			
				if(e.stateMask == 0 && e.keyCode == SWT.TAB) {
					textBasicSort.insert(TadpoleWidgetUtils.TAB_CONETNT);
				}
			}
		});
		textBasicSort.setData( RWT.CANCEL_KEYS, new String[] { "TAB" } );
		textBasicSort.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		textBasicSort.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeBasicSearch = new Composite(compositeBasic, SWT.NONE);
		GridLayout gl_compositeBasicSearch = new GridLayout(8, false);
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
		
		Button btnBasicLastCommandConsole = new Button(compositeBasicSearch, SWT.NONE);
		btnBasicLastCommandConsole.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				compositeResult.console();
			}
		});
		btnBasicLastCommandConsole.setText("Show Console"); //$NON-NLS-1$
		
		Button btnBasicSearch = new Button(compositeBasicSearch, SWT.NONE);
		btnBasicSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findBasic();
			}
		});
		btnBasicSearch.setText("Search"); //$NON-NLS-1$
		
		Composite composite = new Composite(sashForm, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.horizontalSpacing = 0;
		gl_composite.verticalSpacing = 0;
		gl_composite.marginHeight = 0;
		gl_composite.marginWidth = 0;
		composite.setLayout(gl_composite);
		
		compositeResult = new MongodbResultComposite(composite, SWT.NONE, userDB, initColName);
		compositeResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeResult.setLayout(new GridLayout(1, false));
		
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
		
		findBasic();
	}
	
	/**
	 * 검색 기본
	 */
	public void findBasic() {
		final String strBasicField = textBasicField.getText().trim();
		final String strBasicFind = textBasicFind.getText().trim();
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
		
//			if(!tcDAO.getType().equals("com.mongodb.BasicDBObject")) 
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
