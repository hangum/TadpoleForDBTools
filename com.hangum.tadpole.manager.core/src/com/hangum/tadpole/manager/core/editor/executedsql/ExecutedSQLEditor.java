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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.rap.rwt.RWT;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.dialogs.message.dao.SQLHistoryDAO;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.dao.system.ext.UserGroupAUserDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_ExecutedSQL;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserQuery;
import com.hangum.tadpole.sql.util.tables.AutoResizeTableLayout;
import com.hangum.tadpole.sql.util.tables.SQLHistoryCreateColumn;
import com.hangum.tadpole.sql.util.tables.SQLHistoryFilter;
import com.hangum.tadpole.sql.util.tables.SQLHistoryLabelProvider;
import com.hangum.tadpole.sql.util.tables.SQLHistorySorter;
import com.swtdesigner.ResourceManager;

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

	private Button btnUserAllCheck;
	private Combo comboUserName;
	private Combo comboDisplayName;
	private Text textMillis;
	private TableViewer tvList;

	private Button btnSearch;
	private Button btnShowQueryEditor;

	private DateTime dateTimeStart;
	private DateTime dateTimeEnd;

	/** result list */
	private List<SQLHistoryDAO> listSQLHistory = new ArrayList<SQLHistoryDAO>();
	private Text textSearch;
	private SQLHistoryFilter filter;
	
	/** cache */
	private Map<Integer, UserDBDAO> userDBDaoCache = new HashMap<Integer, UserDBDAO>();
	
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
		parent.setLayout(gl_parent);

		Group compositeHead2 = new Group(parent, SWT.NONE);
		compositeHead2.setText("Search");
		compositeHead2.setLayout(new GridLayout(14, false));
		compositeHead2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

		Label lblUser = new Label(compositeHead2, SWT.NONE);
		lblUser.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		GridData gd_lblUser = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblUser.minimumWidth = 65;
		gd_lblUser.widthHint = 65;
		lblUser.setLayoutData(gd_lblUser);
		lblUser.setText("<b>User</b>");

		btnUserAllCheck = new Button(compositeHead2, SWT.CHECK);
		GridData gd_btnBtnuserallcheck = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnBtnuserallcheck.minimumWidth = 45;
		gd_btnBtnuserallcheck.widthHint = 45;
		btnUserAllCheck.setLayoutData(gd_btnBtnuserallcheck);
		btnUserAllCheck.setText("All");
		btnUserAllCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comboUserName.setEnabled(!btnUserAllCheck.getSelection());
			}
		});

		comboUserName = new Combo(compositeHead2, SWT.READ_ONLY);
		GridData gd_comboUserName = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_comboUserName.widthHint = 200;
		gd_comboUserName.minimumWidth = 200;
		comboUserName.setLayoutData(gd_comboUserName);

		Label lblBlank = new Label(compositeHead2, SWT.NONE);
		GridData gd_lblBlank = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblBlank.minimumWidth = 15;
		gd_lblBlank.widthHint = 15;
		lblBlank.setLayoutData(gd_lblBlank);

		Label lblDatabase = new Label(compositeHead2, SWT.NONE);
		GridData gd_lblDatabase = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblDatabase.widthHint = 65;
		gd_lblDatabase.minimumWidth = 65;
		lblDatabase.setLayoutData(gd_lblDatabase);
		lblDatabase.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		lblDatabase.setText("<b>Database</b>");

		final Button btnDbAllCheck = new Button(compositeHead2, SWT.CHECK);
		btnDbAllCheck.setSelection(true);
		GridData gd_btnBtndballcheck = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnBtndballcheck.minimumWidth = 45;
		gd_btnBtndballcheck.widthHint = 45;
		btnDbAllCheck.setLayoutData(gd_btnBtndballcheck);
		btnDbAllCheck.setText("All");
		btnDbAllCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comboDisplayName.setEnabled(!btnDbAllCheck.getSelection());
			}
		});

		comboDisplayName = new Combo(compositeHead2, SWT.READ_ONLY);
		GridData gd_comboDisplayName = new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1);
		gd_comboDisplayName.minimumWidth = 200;
		gd_comboDisplayName.widthHint = 200;
		comboDisplayName.setLayoutData(gd_comboDisplayName);

		Label lblDate = new Label(compositeHead2, SWT.NONE);
		lblDate.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblDate.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		lblDate.setText("<b>Search date</b>");

		dateTimeStart = new DateTime(compositeHead2, SWT.BORDER | SWT.DROP_DOWN);
		Label label = new Label(compositeHead2, SWT.NONE);
		label.setText("~");

		dateTimeEnd = new DateTime(compositeHead2, SWT.BORDER | SWT.DROP_DOWN);
		new Label(compositeHead2, SWT.NONE);

		Label lblDuring = new Label(compositeHead2, SWT.RIGHT);
		GridData gd_lblDuring = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1);
		gd_lblDuring.minimumWidth = 120;
		gd_lblDuring.widthHint = 120;
		lblDuring.setLayoutData(gd_lblDuring);
		lblDuring.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		lblDuring.setText("<b>During execute</b>");

		textMillis = new Text(compositeHead2, SWT.BORDER | SWT.CENTER);
		GridData gd_textMillis = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_textMillis.minimumWidth = 40;
		gd_textMillis.widthHint = 40;
		textMillis.setLayoutData(gd_textMillis);
		textMillis.setText("50");

		Label lblMilis = new Label(compositeHead2, SWT.NONE);
		lblMilis.setText("milliseconds");
		new Label(compositeHead2, SWT.NONE);
		new Label(compositeHead2, SWT.NONE);

		btnSearch = new Button(compositeHead2, SWT.NONE);
		btnSearch.setImage(ResourceManager.getPluginImage("com.hangum.tadpole.manager.core", "resources/icons/search.png"));
		GridData gd_btnSearch = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_btnSearch.minimumWidth = 120;
		gd_btnSearch.widthHint = 120;
		btnSearch.setLayoutData(gd_btnSearch);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
		});
		btnSearch.setText("Search");

		Composite compositeHead = new Composite(parent, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeHead = new GridLayout(1, false);
		gl_compositeHead.verticalSpacing = 2;
		gl_compositeHead.marginHeight = 2;
		gl_compositeHead.horizontalSpacing = 2;
		gl_compositeHead.marginWidth = 2;
		compositeHead.setLayout(gl_compositeHead);

		Composite compositeSearch = new Composite(parent, SWT.NONE);
		compositeSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeSearch = new GridLayout(1, false);
		gl_compositeSearch.marginHeight = 2;
		gl_compositeSearch.verticalSpacing = 1;
		gl_compositeSearch.horizontalSpacing = 1;
		gl_compositeSearch.marginWidth = 1;
		compositeSearch.setLayout(gl_compositeSearch);

		textSearch = new Text(compositeSearch, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SEARCH | SWT.CANCEL);
		textSearch.setMessage("Filter");
		textSearch.setToolTipText("After entering a value, press the Enter key.");
		textSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				filter.setSearchString(textSearch.getText());
				tvList.refresh();
			}
		});
		GridData gd_textSearch = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textSearch.heightHint = 20;
		textSearch.setLayoutData(gd_textSearch);

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
		SQLHistoryCreateColumn.createTableHistoryColumn(tvList, sorterHistory, layoutColumnLayout, true);

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
		GridData gd_btnShowQueryEditor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnShowQueryEditor.minimumHeight = 25;
		gd_btnShowQueryEditor.heightHint = 25;
		gd_btnShowQueryEditor.minimumWidth = 220;
		gd_btnShowQueryEditor.widthHint = 220;
		btnShowQueryEditor.setLayoutData(gd_btnShowQueryEditor);
		btnShowQueryEditor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showQueryEditor();
			}
		});
		btnShowQueryEditor.setEnabled(false);
		btnShowQueryEditor.setText("Show query in the Query editor");

		initUIData();
		initBehavior();

		search();
		
		// google analytic
		AnalyticCaller.track(ExecutedSQLEditor.ID);
	}

	/**
	 * Initial configuration setting of widgets
	 */
	private void initBehavior() {
//		if(PermissionChecker.isShow(SessionManager.getRepresentRole())) {
			btnUserAllCheck.setSelection(true);
			
			comboDisplayName.setEnabled(false);
			comboUserName.setEnabled(false);
//		} else {
//			btnUserAllCheck.setSelection(false);
//			btnUserAllCheck.setEnabled(false);
//			
//			comboDisplayName.setEnabled(false);
//			comboUserName.setEnabled(true);
//		}
	}

	/**
	 * show query editor
	 */
	private void showQueryEditor() {
		IStructuredSelection ss = (IStructuredSelection) tvList.getSelection();
		if (!ss.isEmpty()) {
			SQLHistoryDAO sqlHistoryDAO = (SQLHistoryDAO) ss.getFirstElement();

			try {
				UserDBDAO dbDao;
				if (null != searchUserDBDAO) {
					dbDao = searchUserDBDAO;
				} else {
					dbDao = userDBDaoCache.get(sqlHistoryDAO.getDbSeq());
					if (null == dbDao) {
						dbDao = TadpoleSystem_UserDBQuery.getUserDBInstance(sqlHistoryDAO.getDbSeq());
						userDBDaoCache.put(sqlHistoryDAO.getDbSeq(), dbDao);
					}
				}
				
				FindEditorAndWriteQueryUtil.run(dbDao, sqlHistoryDAO.getStrSQLText() + PublicTadpoleDefine.SQL_DELIMITER);
			} catch (Exception e) {
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
		if ("".equals(comboUserName.getText()) || "".equals(comboDisplayName.getText())) {
			MessageDialog.openWarning(getSite().getShell(), "Warning", "If do not have user or database, can't be viewed.");
			return;
		}

		// check all user
		int user_seq;
		if (comboUserName.getEnabled()) {
			user_seq = (Integer) comboUserName.getData(comboUserName.getText());
		} else {
			user_seq = -1;
		}

		// check all db
		int db_seq;
		if (comboDisplayName.getEnabled()) {
			searchUserDBDAO = (UserDBDAO) comboDisplayName.getData(comboDisplayName.getText());
			db_seq = searchUserDBDAO.getSeq();
		} else {
			searchUserDBDAO = null;
			db_seq = -1;
		}

		Calendar cal = Calendar.getInstance();
		cal.set(dateTimeStart.getYear(), dateTimeStart.getMonth(), dateTimeStart.getDay(), 0, 0, 0);
		long startTime = cal.getTimeInMillis();

		cal.set(dateTimeEnd.getYear(), dateTimeEnd.getMonth(), dateTimeEnd.getDay(), 23, 59, 59);
		long endTime = cal.getTimeInMillis();

		int duringExecute = Integer.parseInt(textMillis.getText());

		try {
			listSQLHistory = TadpoleSystem_ExecutedSQL.getExecuteQueryHistoryDetail(user_seq, db_seq, startTime, endTime, duringExecute);
			tvList.setInput(listSQLHistory);
			tvList.refresh();
		} catch (Exception ee) {
			logger.error("Executed SQL History call", ee); //$NON-NLS-1$
		}
		btnShowQueryEditor.setEnabled(false);
	}

	/**
	 * 데이터를 초기 로드합니다.
	 */
	private void initUIData() {

		try {
//			// 어드민 권한이면 모든 정보를 다 표현합니다.
//			if(PermissionChecker.isShow(SessionManager.getRepresentRole())) {
				List<UserGroupAUserDAO> listUserGroup = TadpoleSystem_UserQuery.getUserListPermission(SessionManager.getGroupSeqs());
				for (UserGroupAUserDAO userGroupAUserDAO : listUserGroup) {
					String name = userGroupAUserDAO.getName() + " (" + userGroupAUserDAO.getEmail() + ")";
					comboUserName.add(name);
					comboUserName.setData(name, userGroupAUserDAO.getSeq());
				}
//			// 개발자 권한일 경우에는 본인만 표시합니다.
//			} else {
//				String name = SessionManager.getName() + " (" + SessionManager.getEMAIL() + ")";
//				
//				comboUserName.add(name);
//				comboUserName.setData(name, SessionManager.getSeq());
//			}
			
			if (userDAO == null) {
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
			if (userDBDAO == null) {
				comboDisplayName.select(0);
			} else {
				comboDisplayName.setText(userDBDAO.getDisplay_name());
			}
		} catch (Exception e) {
			logger.error("get db list", e);
		}
		// Range of date
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -7);
		dateTimeStart.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
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

		ExecutedSQLEditorInput esqli = (ExecutedSQLEditorInput) input;
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
