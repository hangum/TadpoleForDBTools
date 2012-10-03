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
package com.hangum.db.browser.rap.core.dialog.dbconnect;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.browser.rap.core.Messages;
import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.db.session.manager.SessionManager;
import com.hangum.db.system.TadpoleSystem_UserDBQuery;

/**
 * Login dialog
 * 
 * @author hangum
 * 
 */
public class DBLoginDialog extends Dialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1327678815994219469L;
	private static final Logger logger = Logger.getLogger(DBLoginDialog.class);
	
	/** group name */
	protected List<String> groupName;
	/** 초기 선택한 그룹 */
	private String selGroupName;
	
	private Combo comboDBList;
	private Composite compositeBody;

	private AbstractLoginComposite loginComposite;
	private Group grpLoginHistory;
	private TableViewer tableViewerLoginHistory;
	private LoginHistoryComparator comparator;

	// 결과셋으로 사용할 logindb
	private UserDBDAO retuserDb;
	
	// delete button id
	final int DELETE_BTN_ID = 99999;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	public DBLoginDialog(Shell parentShell) {
		super(parentShell);
	}
	
	public DBLoginDialog(Shell paShell, String selGroupName) {
		super(paShell);
		
		this.selGroupName = selGroupName;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Database Connection"); //$NON-NLS-1$
	}
	
	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 3;
		gridLayout.horizontalSpacing = 3;
		gridLayout.marginHeight = 3;
		gridLayout.marginWidth = 3;

		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayout(new GridLayout(2, false));
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		Label lblNewLabel = new Label(compositeHead, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setBounds(0, 0, 56, 15);
		lblNewLabel.setText(Messages.DBLoginDialog_0);

		comboDBList = new Combo(compositeHead, SWT.READ_ONLY);
		comboDBList.setVisibleItemCount(7);
		comboDBList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (loginComposite != null)loginComposite.dispose();
				grpLoginHistory.dispose();

				initDBWidget();
				createHistory(container);
				compositeBody.layout();
				container.layout();
			}
		});
		comboDBList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		// 초기데이터 추가
		for (DBDefine dbDefine : DBDefine.userDBValues()) {
			comboDBList.add(dbDefine.getDBToString());
			comboDBList.setData(dbDefine.getDBToString(), dbDefine);
		}
		comboDBList.select(3);

		// combo에서 선택된 디비의 콤포짖
		compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		// db groupData 
		try {
			groupName = TadpoleSystem_UserDBQuery.getUserGroup(SessionManager.getSeq());
		} catch (Exception e1) {
			logger.error("get group info", e1); //$NON-NLS-1$
		}
		
		initDBWidget();

		// history .....................................
		createHistory(container);

		return container;
	}
	
	/**
	 * 초기화면에서 로드될 widget
	 */
	private void initDBWidget() {
		
		DBDefine dbDefine = (DBDefine) comboDBList.getData(comboDBList.getText());
		if (dbDefine == DBDefine.MYSQL_DEFAULT) {
			loginComposite = new MySQLLoginComposite(DBDefine.MYSQL_DEFAULT, compositeBody, SWT.NONE, groupName, selGroupName);
		} else if (dbDefine == DBDefine.ORACLE_DEFAULT) {
			loginComposite = new OracleLoginComposite(compositeBody, SWT.NONE, groupName, selGroupName);
		} else if (dbDefine == DBDefine.SQLite_DEFAULT) {
			loginComposite = new SQLiteLoginComposite(compositeBody, SWT.NONE, groupName, selGroupName);
		} else if (dbDefine == DBDefine.MSSQL_DEFAULT) {
			loginComposite = new MSSQLLoginComposite(compositeBody, SWT.NONE, groupName, selGroupName);
		} else if (dbDefine == DBDefine.CUBRID_DEFAULT) {
			loginComposite = new CubridLoginComposite(compositeBody, SWT.NONE, groupName, selGroupName);
		} else if(dbDefine == DBDefine.POSTGRE_DEFAULT) {
			loginComposite = new PostgresLoginComposite(compositeBody, SWT.NONE, groupName, selGroupName);
		} else if(dbDefine == DBDefine.MONGODB_DEFAULT) {
			loginComposite = new MongoDBLoginComposite(compositeBody, SWT.NONE, groupName, selGroupName);
		}

	}

	/**
	 * login history
	 * 
	 * @param container
	 */
	private void createHistory(Composite container) {
		grpLoginHistory = new Group(container, SWT.NONE);
		grpLoginHistory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpLoginHistory.setText(Messages.DBLoginDialog_grpHistory_text);
		grpLoginHistory.setLayout(new GridLayout(1, false));

		tableViewerLoginHistory = new TableViewer(grpLoginHistory, SWT.BORDER | SWT.FULL_SELECTION);
//		tableViewerLoginHistory.addDoubleClickListener(new IDoubleClickListener() {
//			public void doubleClick(DoubleClickEvent event) {
//				IStructuredSelection ss = (IStructuredSelection) event.getSelection();
//				UserDBDAO userDb = (UserDBDAO) ss.getFirstElement();
//
//				selectDatabase(userDb);
//
//			}
//		});
		Table table = tableViewerLoginHistory.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableViewerColumn tvcGroupName = new TableViewerColumn(tableViewerLoginHistory, SWT.NONE);
		tvcGroupName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				UserDBDAO login = (UserDBDAO) element;
				return login.getGroup_name();
			}
		});
		TableColumn tblclmnGroupName = tvcGroupName.getColumn();
		tblclmnGroupName.addSelectionListener(getSelectionAdapter(tblclmnGroupName, 1));
		tblclmnGroupName.setWidth(110);
		tblclmnGroupName.setText(Messages.DBLoginDialog_36);

		TableViewerColumn tvcDBType = new TableViewerColumn(tableViewerLoginHistory, SWT.NONE);
		tvcDBType.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				UserDBDAO login = (UserDBDAO) element;
				return login.getTypes();//ComboDBList();
			}
		});
		TableColumn tblclmnDbType = tvcDBType.getColumn();
		tblclmnDbType.addSelectionListener(getSelectionAdapter(tblclmnDbType, 0));
		tblclmnDbType.setWidth(65);
		tblclmnDbType.setText(Messages.DBLoginDialog_tblclmnDbType_text);
		
		TableViewerColumn tvcDisplayName = new TableViewerColumn(tableViewerLoginHistory, SWT.NONE);
		tvcDisplayName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				UserDBDAO login = (UserDBDAO) element;
				return login.getDisplay_name();//TextDisplayName();
			}
		});
		TableColumn tblclmnDisplayName = tvcDisplayName.getColumn();
		tblclmnDisplayName.addSelectionListener(getSelectionAdapter(tblclmnDisplayName, 1));
		tblclmnDisplayName.setWidth(110);
		tblclmnDisplayName.setText(Messages.DBLoginDialog_tblclmnDisplayName_text);

		TableViewerColumn tvcInfo = new TableViewerColumn(tableViewerLoginHistory, SWT.NONE);
		tvcInfo.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				UserDBDAO login = (UserDBDAO) element;
				
				// 자신의 디비만 보이도록 수정
				if(login.getUser_seq() == SessionManager.getSeq()) {
					return login.getUrl();
				} else {
					return "*** not visible ***"; //$NON-NLS-1$
				}
			}
		});
		TableColumn tblclmnIfo = tvcInfo.getColumn();
		tblclmnIfo.addSelectionListener(getSelectionAdapter(tblclmnIfo, 2));
		tblclmnIfo.setWidth(300);
		tblclmnIfo.setText(Messages.DBLoginDialog_tblclmnIfo_text);

		tableViewerLoginHistory.setContentProvider(new ArrayContentProvider());
		try {
			tableViewerLoginHistory.setInput(TadpoleSystem_UserDBQuery.getUserDB());
		} catch (Exception e) {
			logger.error("select login history db", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.DBLoginDialog_22, errStatus); //$NON-NLS-1$
		}

		comparator = new LoginHistoryComparator();
		tableViewerLoginHistory.setSorter(comparator);
	}

	/**
	 * column adapter
	 * 
	 * @param tblclmnIfo
	 * @param i
	 * @return
	 */
	private SelectionListener getSelectionAdapter(final TableColumn column, final int index) {
		
		SelectionAdapter selectionAdapter = new SelectionAdapter() {		
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
				int dir = comparator.getDirection();
				tableViewerLoginHistory.getTable().setSortDirection(dir);
				tableViewerLoginHistory.getTable().setSortColumn(column);
				tableViewerLoginHistory.refresh();
			}
		};
		
		return selectionAdapter;
	}

	/**
	 * 
	 * @param userDb
	 */
	private void selectDatabase(UserDBDAO userDb) {
//		if (loginComposite.connectValite(userDb, userDb.getDb())) {
			this.retuserDb = userDb;

			super.okPressed();
//		}
	}

	@Override
	protected void okPressed() {
		if (!loginComposite.connection()) return;
		this.retuserDb = loginComposite.getDBDTO();
		
		super.okPressed();
	}

	public UserDBDAO getDTO() {
		return retuserDb;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, DELETE_BTN_ID, Messages.DBLoginDialog_button_text, false);
		createButton(parent, IDialogConstants.OK_ID, Messages.DBLoginDialog_6, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.DBLoginDialog_7, false);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		
		if(buttonId == DELETE_BTN_ID) {
			IStructuredSelection ss = (IStructuredSelection) tableViewerLoginHistory.getSelection();
			if(!ss.isEmpty()) {
				UserDBDAO userDb = (UserDBDAO) ss.getFirstElement();
				
				// 자신이 등록한 것만 삭제하도록
				if(SessionManager.getSeq() == userDb.getUser_seq()) {
					
					if(MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages.DBLoginDialog_9, Messages.DBLoginDialog_28)) {
						try {
							TadpoleSystem_UserDBQuery.removeUserDB(userDb.getSeq());
							tableViewerLoginHistory.setInput(TadpoleSystem_UserDBQuery.getUserDB());
						} catch(Exception e) {
							logger.error(Messages.DBLoginDialog_32, e);
							Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
							ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.DBLoginDialog_29, errStatus); //$NON-NLS-1$
						}						
					}	// end confirm
					
				}	// end if 자신의 등록한 것
			}	// end select
		}	// end delete button
	}
	
	/**
	 * group name
	 * 
	 * @return
	 */
	public List<String> getGroupName() {
		return groupName;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 598);
	}
}

class LoginHistoryComparator extends ViewerSorter  {
	private int propertyIndex;
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;

	public LoginHistoryComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	public int getDirection() {
		return direction == 1 ? SWT.DOWN : SWT.UP;
	}

	public void setColumn(int column) {
		if (column == this.propertyIndex) {
			direction = 1 - direction;
		} else {
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		UserDBDAO login1 = (UserDBDAO) e1;
		UserDBDAO login2 = (UserDBDAO) e2;
		int rc = 0;

		switch (propertyIndex) {
		case 0:
			rc = login1.getGroup_name().compareTo(login2.getGroup_name());
			break;
		case 1:
			rc = login1.getTypes().compareTo(login2.getTypes());
			break;
		case 2:
			rc = login1.getDisplay_name().compareTo(login2.getDisplay_name());
			break;
		case 3:
			rc = login1.getUrl().compareTo(login2.getUrl());
			break;
		default:
			rc = 0;
		}

		if (direction == DESCENDING)
			rc = -rc;

		return rc;
	}
}
