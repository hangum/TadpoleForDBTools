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
package com.hangum.tadpole.manager.core.editor.executedsql;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.sql.util.tables.AutoResizeTableLayout;
import com.hangum.tadpole.commons.sql.util.tables.SQLHistoryCreateColumn;
import com.hangum.tadpole.commons.sql.util.tables.SQLHistoryFilter;
import com.hangum.tadpole.commons.sql.util.tables.SQLHistoryLabelProvider;
import com.hangum.tadpole.commons.sql.util.tables.SQLHistorySorter;
import com.hangum.tadpole.dao.system.UserDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dao.system.ext.UserGroupAUserDAO;
import com.hangum.tadpole.dialogs.message.dao.SQLHistoryDAO;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.system.TadpoleSystem_ExecutedSQL;
import com.hangum.tadpole.system.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.system.TadpoleSystem_UserQuery;

/**
 * 실행한 쿼리.
 * 
 * 1. Name 콤보에 보여줄때는 사람을 기준으로 보여줄
 * 
 * @author hangum
 *
 */
public class ExecutedSQLEditor extends EditorPart {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ExecutedSQLEditor.class);

	public static String ID = "com.hangum.tadpole.manager.core.editor.manager.executed_sql";
	/** 마지막 검색시 사용하는 UserDBDAO */
	private UserDBDAO searchUserDBDAO = null;
	
	/** 제일 처음 설정 될때 사용하는 dao */
	private UserDAO userDAO;
	private UserDBDAO userDBDAO;
	
	private Combo comboUserName;
	private Combo comboDisplayName;
	
	private DateTime dateTimeSearch;
	private Text textMillis;
	private TableViewer tvList;
	
	private Button btnSearch;
	private Button btnShowQueryEditor;
	
	/** result list */
	private List<SQLHistoryDAO> listSQLHistory = new ArrayList<SQLHistoryDAO>();
	private Text textSearch;
	private SQLHistoryFilter filter;
	
	/**
	 * 
	 */
	public ExecutedSQLEditor() {
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
		
		Composite compositeHead = new Composite(parent, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeHead = new GridLayout(6, false);
		gl_compositeHead.verticalSpacing = 2;
		gl_compositeHead.marginHeight = 2;
		gl_compositeHead.horizontalSpacing = 2;
		gl_compositeHead.marginWidth = 2;
		compositeHead.setLayout(gl_compositeHead);
		
		Label lblOperationType = new Label(compositeHead, SWT.NONE);
		lblOperationType.setText("User Name");
		
		comboUserName = new Combo(compositeHead, SWT.READ_ONLY);
		comboUserName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblName = new Label(compositeHead, SWT.NONE);
		lblName.setText("DB Name");
		
		comboDisplayName = new Combo(compositeHead, SWT.READ_ONLY);
		comboDisplayName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblExecuteTime = new Label(compositeHead, SWT.NONE);
		lblExecuteTime.setText("Execute Time");
		
		dateTimeSearch = new DateTime(compositeHead, SWT.BORDER);
		
		Label lblExecuteMills = new Label(compositeHead, SWT.NONE);
		lblExecuteMills.setText("during execute");
		
		textMillis = new Text(compositeHead, SWT.BORDER);
		textMillis.setText("50");
		textMillis.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblmillis = new Label(compositeHead, SWT.NONE);
		lblmillis.setText("(millis)");
		
		btnSearch = new Button(compositeHead, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
		});
		btnSearch.setText("Search");
		
		Composite compositeSearch = new Composite(parent, SWT.NONE);
		compositeSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeSearch = new GridLayout(2, false);
		gl_compositeSearch.verticalSpacing = 1;
		gl_compositeSearch.horizontalSpacing = 1;
		gl_compositeSearch.marginWidth = 1;
		compositeSearch.setLayout(gl_compositeSearch);
		
		Label lblSearch = new Label(compositeSearch, SWT.NONE);
		lblSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSearch.setText("Search");
		
		textSearch = new Text(compositeSearch, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				filter.setSearchString(textSearch.getText());
				tvList.refresh();
			}
		});
		textSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite compositeBody = new Composite(parent, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);
		
		tvList = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		tvList.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				btnShowQueryEditor.setEnabled(true);
			}
		});
		tvList.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				showQueryEditor();
			}
		});
		Table table = tvList.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// auto column layout
		AutoResizeTableLayout layoutColumnLayout = new AutoResizeTableLayout(tvList.getTable());
		tvList.getTable().setLayout(layoutColumnLayout);
		
		SQLHistorySorter sorterHistory = new SQLHistorySorter();
		SQLHistoryCreateColumn.createTableHistoryColumn(tvList, sorterHistory, layoutColumnLayout);
		
		tvList.setLabelProvider(new SQLHistoryLabelProvider());
		tvList.setContentProvider(new ArrayContentProvider());
		tvList.setInput(listSQLHistory);
		tvList.setComparator(sorterHistory);
		
		filter = new SQLHistoryFilter();
		tvList.addFilter(filter);
		
		Composite compositeTail = new Composite(compositeBody, SWT.NONE);
		compositeTail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeTail = new GridLayout(1, false);
		gl_compositeTail.verticalSpacing = 2;
		gl_compositeTail.horizontalSpacing = 2;
		gl_compositeTail.marginHeight = 2;
		gl_compositeTail.marginWidth = 2;
		compositeTail.setLayout(gl_compositeTail);
		
		btnShowQueryEditor = new Button(compositeTail, SWT.NONE);
		btnShowQueryEditor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showQueryEditor();
			}
		});
		btnShowQueryEditor.setEnabled(false);
		btnShowQueryEditor.setText("Show Query Editor");
		
		initUIData();
		
		search();
	}
	
	/**
	 * show query editor
	 */
	private void showQueryEditor() {
		IStructuredSelection ss = (IStructuredSelection)tvList.getSelection();
		if(!ss.isEmpty()) {
			SQLHistoryDAO sqlHistoryDAO = (SQLHistoryDAO)ss.getFirstElement();
			
			try {
				FindEditorAndWriteQueryUtil.run(searchUserDBDAO, sqlHistoryDAO.getStrSQLText() + PublicTadpoleDefine.SQL_DILIMITER);
			} catch(Exception e) {
				logger.error("find editor and write query", e);
			}
		}
	}
	
	/**
	 * search 
	 */
	private void search() {
		listSQLHistory.clear();
		
		// Is user DB empty.
		if("".equals(comboUserName.getText()) || "".equals(comboDisplayName.getText())) return;
		
		int user_seq = (Integer)comboUserName.getData(comboUserName.getText());
		searchUserDBDAO = (UserDBDAO)comboDisplayName.getData(comboDisplayName.getText());
		
		int db_seq = searchUserDBDAO.getSeq();
		
		Calendar cal = Calendar.getInstance();
		cal.set(dateTimeSearch.getYear(), dateTimeSearch.getMonth(), dateTimeSearch.getDay(), 0, 0, 0);
		long startTime = cal.getTimeInMillis();
		int duringExecute = Integer.parseInt(textMillis.getText());
		
		try {
			listSQLHistory = TadpoleSystem_ExecutedSQL.getExecuteQueryHistoryDetail(user_seq, db_seq, startTime, duringExecute);
			tvList.setInput(listSQLHistory);
			tvList.refresh();
		} catch(Exception ee) {
			logger.error("Executed SQL History call", ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * 데이터를 초기 로드합니다.
	 */
	private void initUIData() {

		try {
			// user information
			List<UserGroupAUserDAO> listUserGroup =  TadpoleSystem_UserQuery.getUserListPermission(SessionManager.getGroupSeqs());
			for (UserGroupAUserDAO userGroupAUserDAO : listUserGroup) {
				String name = userGroupAUserDAO.getName() + " (" + userGroupAUserDAO.getEmail() + ")";
				comboUserName.add(name);
				comboUserName.setData(name, userGroupAUserDAO.getSeq());
			}
			if(userDAO == null) {
				comboUserName.select(0);
			} else {
				comboUserName.setText(userDAO.getName() + " (" + userDAO.getEmail() + ")");
			}

			// database name combo			
			List<UserDBDAO> listUserDBDAO = TadpoleSystem_UserDBQuery.getUserDB();
			for (UserDBDAO userDBDAO : listUserDBDAO) {
				comboDisplayName.add(userDBDAO.getDisplay_name());
				comboDisplayName.setData(userDBDAO.getDisplay_name(), userDBDAO);
			}
			if(userDBDAO == null) {
				comboDisplayName.select(0);
			} else {
				comboDisplayName.setText(userDBDAO.getDisplay_name());
			}
		} catch(Exception e) {
			logger.error("get db list", e);
		}
		
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
		
		ExecutedSQLEditorInput esqli = (ExecutedSQLEditorInput)input;
		setPartName(esqli.getName());
		
		this.userDAO = esqli.getUserDAO();
		this.userDBDAO = esqli.getUserDBDAO();
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
	public void setFocus() {
		btnSearch.setFocus();
	}

}
