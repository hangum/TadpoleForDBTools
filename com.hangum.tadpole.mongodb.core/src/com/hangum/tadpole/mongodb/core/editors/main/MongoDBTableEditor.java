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

import com.hangum.tadpole.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dialogs.message.TadpoleSimpleMessageDialog;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.composite.result.MongodbResultComposite;
import com.hangum.tadpole.mongodb.core.connection.MongoConnectionManager;
import com.hangum.tadpole.mongodb.core.define.MongoDBDefine;
import com.hangum.tadpole.mongodb.core.dto.MongodbTreeViewDTO;
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
	private String tableName;
	
	/** search panel */
	private CTabFolder tabFolderSearchPanel;
	
	/** collection column info */
	private Map<String, TableColumnDAO> columnInfo;
	/** collection의 전체 컬럼 */
	private String strColumns = ""; //$NON-NLS-1$
	
//	private Text textSearch;
//	private Text textSort;
//	private Text textField;
//	
//	private Text textCntSkip;
//	private Text textCntLimit;
	
	/** where parser */
//	private MongoSQLParser parser = new MongoSQLParser();
	
	/** 쿼리 결과 출력 */
	private MongodbResultComposite compositeResult ;
	
	/** 헤드 타이틀 */
	private Map<Integer, String> mapColumns = new HashMap<Integer, String>();
	
	/** tree view result */
	List<MongodbTreeViewDTO> listTrees = new ArrayList<MongodbTreeViewDTO>();
	
	/** table query 의 결과 데이터  -- table의 데이터를 표시하는 용도 <column index, Data> */
	private List<HashMap<Integer, Object>> sourceDataList = new ArrayList<HashMap<Integer, Object>>();
	
	/** label count  string */
	private String txtCnt = ""; //$NON-NLS-1$
	
	/** console msg */
	StringBuffer sbConsoleMsg = new StringBuffer();
	private Text textBasicFind;
	private Text textBasicField;
	private Text textBasicSort;
	private Text textBasicSkip;
	private Text textBasicLimit;
	
	/** preference default find page */
	private String defaultFindPage 	= GetPreferenceGeneral.getMongoDefaultFindPage();//Activator.getDefault().getPreferenceStore().getString(PreferenceDefine.MONGO_DEFAULT_FIND);

	/** preference default result page */
	private String defaultResultPage = GetPreferenceGeneral.getMongoDefaultResultPage();//Activator.getDefault().getPreferenceStore().getString(PreferenceDefine.MONGO_DEFAULT_RESULT);
	
	/** preference default limit */
	private String defaultLimit = GetPreferenceGeneral.getMongoDefaultLimit();//Activator.getDefault().getPreferenceStore().getString(PreferenceDefine.MONGO_DEFAULT_LIMIT);
	
	/** preference default max count */
	private int defaultMaxCount = GetPreferenceGeneral.getMongoDefaultMaxCount();//Activator.getDefault().getPreferenceStore().getInt(PreferenceDefine.MONGO_DEFAULT_MAX_COUNT);
	
	/**
	 * 
	 */
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
					textBasicFind.insert("    ");//new Character(SWT.TAB).toString());
				}
			}
		});
//		textBasicFind.addTraverseListener(new TraverseListener() {
//			public void keyTraversed(TraverseEvent e) {
//				if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
//					textBasicFind.setFocus();
//					e.doit = false;
//					e.detail = SWT.TRAVERSE_NONE;
//				}
//			}
//		});
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
					textBasicField.insert("    ");//new Character(SWT.TAB).toString());
				}
			}
		});
//		textBasicField.addTraverseListener(new TraverseListener() {
//			public void keyTraversed(TraverseEvent e) {
//				if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
//					textBasicField.setFocus();
//					e.doit = false;
//					e.detail = SWT.TRAVERSE_NONE;
//				}
//			}
//		});
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
					textBasicSort.insert("    ");//new Character(SWT.TAB).toString());
				}
			}
		});
//		textBasicSort.addTraverseListener(new TraverseListener() {
//			public void keyTraversed(TraverseEvent e) {
//				if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
//					textBasicSort.setFocus();
//					e.doit = false;
//					e.detail = SWT.TRAVERSE_NONE;
//				}
//			}
//		});
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
		gd_textBasicSkip.widthHint = 50;
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
		gd_textBasicLimit.widthHint = 50;
		textBasicLimit.setLayoutData(gd_textBasicLimit);
		
		Label label_1 = new Label(compositeBasicSearch, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnBasicLastCommandConsole = new Button(compositeBasicSearch, SWT.NONE);
		btnBasicLastCommandConsole.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				console();
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
		
//		TabItem tbtmExtension = new TabItem(tabFolderSearchPanel, SWT.NONE);
//		tbtmExtension.setText("Extension Search"); //$NON-NLS-1$
		
//		Composite compositeSearchExtension = new Composite(tabFolderSearchPanel, SWT.NONE);
//		tbtmExtension.setControl(compositeSearchExtension);
//		compositeSearchExtension.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		GridLayout gl_compositeSearchExtension = new GridLayout(1, false);
//		gl_compositeSearchExtension.verticalSpacing = 2;
//		gl_compositeSearchExtension.horizontalSpacing = 2;
//		gl_compositeSearchExtension.marginHeight = 2;
//		gl_compositeSearchExtension.marginWidth = 2;
//		compositeSearchExtension.setLayout(gl_compositeSearchExtension);
		
//		Composite compositeHead = new Composite(compositeSearchExtension, SWT.NONE);
//		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		compositeHead.setLayout(new GridLayout(2, false));
//		
//		Label lblField = new Label(compositeHead, SWT.NONE);
//		lblField.setText("Field"); //$NON-NLS-1$
//		
//		textField = new Text(compositeHead, SWT.BORDER);
//		textField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		textField.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
//		
//		Label lblSearch = new Label(compositeHead, SWT.NONE);
//		lblSearch.setText("Where"); //$NON-NLS-1$
//		
//		textSearch = new Text(compositeHead, SWT.BORDER);
//		textSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		textSearch.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
//		
//		Label lblOrderBy = new Label(compositeHead, SWT.NONE);
//		lblOrderBy.setText("Sort"); //$NON-NLS-1$
//		
//		textSort = new Text(compositeHead, SWT.BORDER);
//		textSort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		textSort.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//				if(e.keyCode == SWT.Selection) {
//					findExtension();
//				}
//			}
//		});
//		textSort.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
//		
//		textSearch.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//				if(e.keyCode == SWT.Selection) {
//					findExtension();
//				}
//			}
//		});
//		textField.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//				if(e.keyCode == SWT.Selection) {
//					findExtension();
//				}
//			}
//		});
//		
//		Composite compositeFindSearch = new Composite(compositeSearchExtension, SWT.NONE);
//		compositeFindSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
//		GridLayout gl_compositeFindSearch = new GridLayout(8, false);
//		gl_compositeFindSearch.verticalSpacing = 1;
//		gl_compositeFindSearch.horizontalSpacing = 1;
//		gl_compositeFindSearch.marginHeight = 1;
//		gl_compositeFindSearch.marginWidth = 1;
//		compositeFindSearch.setLayout(gl_compositeFindSearch);
//		
//		Label lblStart = new Label(compositeFindSearch, SWT.NONE);
//		GridData gd_lblStart = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
//		gd_lblStart.widthHint = 30;
//		lblStart.setLayoutData(gd_lblStart);
//		
//		Label lblSkip = new Label(compositeFindSearch, SWT.NONE);
//		lblSkip.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//		lblSkip.setText("Skip"); //$NON-NLS-1$
//		
//		textCntSkip = new Text(compositeFindSearch, SWT.BORDER | SWT.RIGHT);
//		textCntSkip.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
//		textCntSkip.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//				if(e.keyCode == SWT.Selection) {
//					findExtension();
//				}
//			}
//		});
//		textCntSkip.setText("0"); //$NON-NLS-1$
//		GridData gd_textCntSkip = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
//		gd_textCntSkip.widthHint = 50;
//		textCntSkip.setLayoutData(gd_textCntSkip);
//		
//		Label lblLimit = new Label(compositeFindSearch, SWT.NONE);
//		lblLimit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//		lblLimit.setText("Limit"); //$NON-NLS-1$
//		
//		textCntLimit = new Text(compositeFindSearch, SWT.BORDER | SWT.RIGHT);
//		textCntLimit.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
//		textCntLimit.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//				if(e.keyCode == SWT.Selection) {
//					findExtension();
//				}
//			}
//		});
//		GridData gd_textCntLimit = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
//		gd_textCntLimit.widthHint = 50;
//		textCntLimit.setLayoutData(gd_textCntLimit);
//		textCntLimit.setText(defaultLimit); //$NON-NLS-1$
//		
//		Label lblNewLabel = new Label(compositeFindSearch, SWT.NONE);
//		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		
//		Button btnToApplication = new Button(compositeFindSearch, SWT.NONE);
//		btnToApplication.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				console();
//			}
//		});
//		btnToApplication.setText("Show Console"); //$NON-NLS-1$
//		
//		Button btnSearch = new Button(compositeFindSearch, SWT.NONE);
//		btnSearch.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				findExtension();
//			}
//		});
//		btnSearch.setText("Search"); //$NON-NLS-1$
		
		Composite composite = new Composite(sashForm, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.horizontalSpacing = 0;
		gl_composite.verticalSpacing = 0;
		gl_composite.marginHeight = 0;
		gl_composite.marginWidth = 0;
		composite.setLayout(gl_composite);
		
		compositeResult = new MongodbResultComposite(composite, SWT.NONE, userDB, tableName, this);
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
//		} else {
//			tabFolderSearchPanel.setSelection(1);
		}
		
		findBasic();		
//		textField.setText(strColumns);
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
		
		// job
		Job job = new Job("SQL execute job") { //$NON-NLS-1$
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Starting JSON query...", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
						
				try {			
					// field					
					BasicDBObject basicFields = (BasicDBObject)JSON.parse(strBasicField);
					if(null == basicFields) basicFields = new BasicDBObject();
					
					// search
					DBObject basicWhere = (DBObject)JSON.parse(strBasicFind);
					if(null == basicWhere) basicWhere = new BasicDBObject();
					
					// sort
					BasicDBObject basicSort = (BasicDBObject)JSON.parse(strBasicSort);
					if(null == basicSort) basicSort = new BasicDBObject();
					
					if(logger.isDebugEnabled()) {
						logger.debug("############[text condition]#####################"); //$NON-NLS-1$
						logger.debug("[Fields]" + strBasicField); //$NON-NLS-1$
						logger.debug("[Find]" + strBasicFind); //$NON-NLS-1$
						logger.debug("[Sort]" + strBasicSort); //$NON-NLS-1$
						logger.debug("############[text condition]#####################"); //$NON-NLS-1$
					}
					monitor.setTaskName(basicWhere.toString());
		
					// console 초기화
					sbConsoleMsg.setLength(0);
					// 검색
					find(basicFields, basicWhere, basicSort, cntSkip, cntLimit);
				} catch (Exception e) {
					logger.error("find basic collection exception", e); //$NON-NLS-1$
					return new Status(Status.WARNING,Activator.PLUGIN_ID, "findBasic " + e.getMessage()); //$NON-NLS-1$
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
							compositeResult.setResult(txtCnt, mapColumns, listTrees, sourceDataList);
						} else {
							sbConsoleMsg.append(jobEvent.getResult().getMessage());
							compositeResult.appendMessage(jobEvent.getResult().getMessage());
						}
					}
				});	// end display.asyncExec				
			}	// end done
		});	// end job
		
		job.setName(userDB.getDisplay_name());
		job.setUser(true);
		job.schedule();
	}
	
//	/**
//	 * 검색 확장
//	 */
//	public void findExtension() {
//		final String strBasicField = textField.getText().trim();
//		final String strBasicFind = textSearch.getText().trim();
//		final String strBasicSort = textSort.getText().trim();
//		final int cntSkip = getCntSkip();
//		final int cntLimit = getCntLimit();
//		
//		// job
//		Job job = new Job(Messages.MongoDBTableEditor_4) {
//			@Override
//			public IStatus run(IProgressMonitor monitor) {
//				monitor.beginTask(Messages.MongoDBTableEditor_5, IProgressMonitor.UNKNOWN);
//						
//				try {
//					// field
//					//
//					//	특정 컬럼을 검색하는지.(특정 컬럼을 검색하지 않으면 모든 컬럼을 검색)
//					//	*, 공백이면 모든 컬럼 검색
//					BasicDBObject basicFields = new BasicDBObject();
//					if(!strBasicField.equals("")) { //$NON-NLS-1$
//						if(!"*".equals(strBasicField)) { //$NON-NLS-1$
//							int columnCnt = 0;
//							
//							for(String field : StringUtils.split(strBasicField, ",")) { //$NON-NLS-1$
//								basicFields.append(field.trim(), true);
//								
//								mapColumns.put(columnCnt, field.trim());
//								columnCnt++;
//							}
//						}
//					}
//					
//					// where
//					DBObject basicWhere = new BasicDBObject();
//					if(!strBasicFind.equals("")) { //$NON-NLS-1$
//						basicWhere = parser.query(strBasicFind, columnInfo);
//					}
//					
//					// sort
//					BasicDBObject basicSort = new BasicDBObject();
//					if(!strBasicSort.equals("")) { //$NON-NLS-1$
//						for(String sortStmt : StringUtils.split(strBasicSort, ",")) { //$NON-NLS-1$
//							String[] search = StringUtils.split(sortStmt, "="); //$NON-NLS-1$
//							
//							// aa=-1
//							TableColumnDAO column = null;
//							if(search.length == 2) {
//		
//								// 원래 데이터 타입에 맞게 형변환 합니다.
//								column = columnInfo.get(search[0].trim());
//								// 컬럼이 발견 되지 않았으면..
//								if(column == null) {
//									throw new Exception("[Sort] " + search[0] + Messages.MongoDBTableEditor_23); //$NON-NLS-1$
//								}
//								
//								basicSort.append(search[0].trim(), Integer.parseInt(search[1].trim()));
//							}
//						}
//					}
//					
//					if(logger.isDebugEnabled()) {
//						logger.debug("############[text condition]#####################"); //$NON-NLS-1$
//						logger.debug("[Fields]" + strBasicField); //$NON-NLS-1$
//						logger.debug("[Find]" + strBasicFind); //$NON-NLS-1$
//						logger.debug("[Sort]" + strBasicSort); //$NON-NLS-1$
//						logger.debug("############[text condition]#####################"); //$NON-NLS-1$
//					}
//					monitor.setTaskName(basicWhere.toString());
//					
//					// 초기화
//					sbConsoleMsg.setLength(0);		
//					// 검색
//					find(basicFields, basicWhere, basicSort, cntSkip, cntLimit);
//				} catch (Exception e) {
//					logger.error("find extendsion collection exception", e); //$NON-NLS-1$
//					return new Status(Status.WARNING,Activator.PLUGIN_ID, e.getMessage());
//				} finally {
//					monitor.done();
//				}
//				
//				return Status.OK_STATUS;
//			}
//		};
//		
//		// job의 event를 처리해 줍니다.
//		job.addJobChangeListener(new JobChangeAdapter() {
//			public void done(IJobChangeEvent event) {
//	
//				final IJobChangeEvent jobEvent = event; 
//				getSite().getShell().getDisplay().asyncExec(new Runnable() {
//					public void run() {
//						if(jobEvent.getResult().isOK()) {
//							compositeResult.setResult(txtCnt, mapColumns, listTrees, sourceDataList);
//						} else {
//							sbConsoleMsg.append(jobEvent.getResult().getMessage());
//							compositeResult.appendMessage(jobEvent.getResult().getMessage());
//						}
//					}
//				});	// end display.asyncExec				
//			}	// end done
//		});	// end job
//		
//		job.setName(userDB.getDisplay_name());
//		job.setUser(true);
//		job.schedule();
//	}

	/**
	 * 실제 몽고디비와 접속해서 검색을 수행합니다.
	 * 
	 * @param basicFields
	 * @param basicWhere
	 * @param basicSort
	 */
	private void find(BasicDBObject basicFields, DBObject basicWhere, BasicDBObject basicSort, int cntSkip, int cntLimit) throws Exception {
		if( (cntLimit - cntSkip) >= defaultMaxCount) {
			
//			"검색 수가 " + defaultMaxCount + "를 넘을수 없습니다. Prefernece에서 값을 조절하십시오."
//			Search can not exceed the number 5. Set in Perference.
			throw new Exception(String.format(Messages.MongoDBTableEditor_0, ""+defaultMaxCount));  //$NON-NLS-2$
		}
		
		mapColumns = new HashMap<Integer, String>();
		sourceDataList = new ArrayList<HashMap<Integer, Object>>();
		
		DB mongoDB = MongoConnectionManager.getInstance(userDB);
		DBCollection dbCollection = mongoDB.getCollection(tableName);
		
		// 데이터 검색
		DBCursor dbCursor = null;
		try {
			if(cntSkip > 0 && cntLimit > 0) {
					
				sbConsoleMsg.append("############[query]#####################").append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
				sbConsoleMsg.append("[Fields]" + basicFields.toString()).append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
				sbConsoleMsg.append("[Where]" + basicWhere.toString()).append("\r\n");					 //$NON-NLS-1$ //$NON-NLS-2$
				sbConsoleMsg.append("[Sort] " + basicSort.toString()).append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
				sbConsoleMsg.append("[Skip]" + cntSkip).append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
				sbConsoleMsg.append("[Limit]" + cntLimit).append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
				sbConsoleMsg.append("############[query]#####################"); //$NON-NLS-1$
				
				dbCursor = dbCollection.
									find(basicWhere, basicFields).
									sort(basicSort).
									skip(cntSkip).
									limit(cntLimit)
									;
				
			} else if(cntSkip == 0 && cntLimit > 0) {
				
				sbConsoleMsg.append("############[query]#####################").append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
				sbConsoleMsg.append("[Fields]" + basicFields.toString()).append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
				sbConsoleMsg.append("[Where]" + basicWhere.toString()).append("\r\n");				 //$NON-NLS-1$ //$NON-NLS-2$
				sbConsoleMsg.append("[Sort] " + basicSort.toString()).append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
				sbConsoleMsg.append("[Limit]" + cntLimit).append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
				sbConsoleMsg.append("############[query]#####################").append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
				
				dbCursor = dbCollection.
						find(basicWhere, basicFields).
						sort(basicSort).
						limit(cntLimit);
			} else {
				sbConsoleMsg.append("############[query]#####################").append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
				sbConsoleMsg.append("[Fields]" + basicFields.toString()).append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
				sbConsoleMsg.append("[Where]" + basicWhere.toString()).append("\r\n");				 //$NON-NLS-1$ //$NON-NLS-2$
				sbConsoleMsg.append("[Sort] " + basicSort.toString()).append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
				sbConsoleMsg.append("############[query]#####################").append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
				
				dbCursor = dbCollection.
									find(basicWhere, basicFields).
									sort(basicSort);				
			}
	
			DBObject explainDBObject = dbCursor.explain();
			sbConsoleMsg.append("############[result]#####################").append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
			sbConsoleMsg.append("[query explain]\r\n" + JSONUtil.getPretty(explainDBObject.toString())).append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
			sbConsoleMsg.append("[error]\r\n" + JSONUtil.getPretty(mongoDB.getLastError().toString())).append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
			sbConsoleMsg.append("############[result]#####################").append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
	
			mongoDB.forceError();
	        mongoDB.resetError();
	        
	        if(logger.isDebugEnabled()) logger.debug(sbConsoleMsg);
			
			// 결과 데이터를 출력합니다.
			int totCnt = 0;
			listTrees = new ArrayList<MongodbTreeViewDTO>();
			
			for (DBObject dbObject : dbCursor) {
				// 초기 호출시 컬럼 정보 설정 되어 있지 않을때
				if(mapColumns.size() == 0) mapColumns = MongoDBTableColumn.getTabelColumnView(dbObject);
				
				// append tree text columnInfo.get(key)
				MongodbTreeViewDTO treeDto = new MongodbTreeViewDTO(dbObject, "(" + totCnt + ") {..}", "", "Document");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				parserTreeObject(dbObject, treeDto, dbObject);
				listTrees.add(treeDto);
								
				// append table text
				HashMap<Integer, Object> dataMap = new HashMap<Integer, Object>();				
				for(int i=0; i<mapColumns.size(); i++)	{
					
					Object keyVal = dbObject.get(mapColumns.get(i));
					if(keyVal == null) dataMap.put(i, "");  //$NON-NLS-1$
					else dataMap.put(i, keyVal.toString());
				}
				// 데이터 삭제 및 수정에서 사용하기 위한 id
				dataMap.put(MongoDBDefine.PRIMARY_ID_KEY, dbObject);
				sourceDataList.add(dataMap);
				
				// append row text
				totCnt++;
			}
			txtCnt = dbCursor.count() + "/" + totCnt + Messages.MongoDBTableEditor_69; //$NON-NLS-1$
		} finally {
			if(dbCursor != null) dbCursor.close();
		}
	}
	
	/**
	 * parser tree obejct
	 * 
	 * @param dbObject
	 */
	private void parserTreeObject(final DBObject rootDbObject, final MongodbTreeViewDTO treeDto, final DBObject dbObject) throws Exception {
		List<MongodbTreeViewDTO> listTrees = new ArrayList<MongodbTreeViewDTO>();
		
		Map<Integer, String> tmpMapColumns = MongoDBTableColumn.getTabelColumnView(dbObject);
		for(int i=0; i<tmpMapColumns.size(); i++)	{
			MongodbTreeViewDTO tmpTreeDto = new MongodbTreeViewDTO();
			tmpTreeDto.setDbObject(rootDbObject);
			
			String keyName = tmpMapColumns.get(i);			
			Object keyVal = dbObject.get(keyName);
			
			tmpTreeDto.setRealKey(keyName);
			// is sub document
			if( keyVal instanceof BasicDBObject ) {
				tmpTreeDto.setKey(tmpMapColumns.get(i) + " {..}"); //$NON-NLS-1$
				tmpTreeDto.setType("Document"); //$NON-NLS-1$
				
				parserTreeObject(rootDbObject, tmpTreeDto, (DBObject)keyVal);
			} else if(keyVal instanceof BasicDBList) {
				BasicDBList dbObjectList = (BasicDBList)keyVal;
				
				tmpTreeDto.setKey(tmpMapColumns.get(i) + " [" + dbObjectList.size() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
				tmpTreeDto.setType("Array"); //$NON-NLS-1$
				parseObjectArray(rootDbObject, tmpTreeDto, dbObjectList);
			} else {
				tmpTreeDto.setKey(tmpMapColumns.get(i));
				tmpTreeDto.setType(keyVal != null?keyVal.getClass().getName():"Unknow"); //$NON-NLS-1$
				
				if(keyVal == null) tmpTreeDto.setValue(""); //$NON-NLS-1$
				else tmpTreeDto.setValue(keyVal.toString());
			}
			
			// 컬럼의 데이터를 넣는다.
			listTrees.add(tmpTreeDto);
		}
		
		treeDto.setChildren(listTrees);
	}
	
	/**
	 * object array
	 * 
	 * @param treeDto
	 * @param dbObject
	 * @throws Exception
	 */
	private void parseObjectArray(final DBObject rootDbObject, final MongodbTreeViewDTO treeDto, final BasicDBList dbObjectList) throws Exception {
		List<MongodbTreeViewDTO> listTrees = new ArrayList<MongodbTreeViewDTO>();
		
		for(int i=0; i<dbObjectList.size(); i++) {
			MongodbTreeViewDTO mongodbDto = new MongodbTreeViewDTO();
			
			mongodbDto.setRealKey("" + i ); //$NON-NLS-1$
			mongodbDto.setKey("(" + i + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			mongodbDto.setDbObject(rootDbObject);

			Object keyVal = dbObjectList.get(i);
			if( keyVal instanceof BasicDBObject ) {
				mongodbDto.setType("Document"); //$NON-NLS-1$
				
				parserTreeObject(rootDbObject, mongodbDto, (DBObject)keyVal);
			} else if(keyVal instanceof BasicDBList) {
				BasicDBList tmpDbObjectList = (BasicDBList)keyVal;
				
				mongodbDto.setType("Array"); //$NON-NLS-1$
				parseObjectArray(rootDbObject, mongodbDto, tmpDbObjectList);
			} else {
				mongodbDto.setType(keyVal != null?keyVal.getClass().getName():"Unknow"); //$NON-NLS-1$
				
				if(keyVal == null) mongodbDto.setValue(""); //$NON-NLS-1$
				else mongodbDto.setValue(keyVal.toString());
			}
			
			listTrees.add(mongodbDto);
		}
		
		treeDto.setChildren(listTrees);
	}
	
	/**
	 * text cnt skip
	 * @return
	 */
	private int getCntSkip() {
//		if(tabFolderSearchPanel.getSelectionIndex() == 0) {
			return Integer.parseInt(textBasicSkip.getText());
//		} else {
//			return Integer.parseInt(textCntSkip.getText());
//		}
	}
	
	/**
	 * text cnt term
	 * @return
	 */
	private int getCntLimit() {
//		if(tabFolderSearchPanel.getSelectionIndex() == 0) {
			return Integer.parseInt(textBasicLimit.getText());
//		} else {
//			return Integer.parseInt(textCntLimit.getText());
//		}
	}
	
	/**
	 * 콘솔창을 보여줍니다.
	 */
	private void console() {
		TadpoleSimpleMessageDialog dialog = new TadpoleSimpleMessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				tableName + " query Console",  //$NON-NLS-1$
				sbConsoleMsg.toString());
		dialog.open();
	}
	
	/**
	 * default resuult page
	 * 
	 * @return
	 */
	public String getDefaultResultPage() {
		return defaultResultPage;
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
		this.tableName = moInput.getName();
		this.columnInfo = new HashMap<String, TableColumnDAO>();
		for (int i=0; i<moInput.getShowTableColumns().size(); i++) {
			TableColumnDAO tcDAO = (TableColumnDAO)moInput.getShowTableColumns().get(i);
			columnInfo.put(tcDAO.getField(), tcDAO);
		
//			if(!tcDAO.getType().equals("com.mongodb.BasicDBObject")) 
			strColumns += tcDAO.getField() + ", "; //$NON-NLS-1$
		}
		strColumns = StringUtils.chompLast(strColumns, ", "); //$NON-NLS-1$
		
		setPartName(userDB.getDisplay_name() + " [" + moInput.getName() + "]");		 //$NON-NLS-1$ //$NON-NLS-2$
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
