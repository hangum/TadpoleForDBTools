/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.manager.core.dialogs.users;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.system.TadpoleUserDbRoleDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserRole;
import com.hangum.tadpole.manager.core.Messages;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerLabelProvider;

/**
 * 사용자를 디비 그룹에 추가하고, 사용자 역할을 설정합니다.
 * 
 * @author hangum
 *
 */
public class FindUserAndDBRoleDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(FindUserAndDBRoleDialog.class);
	
//	private UserDBDAO userDBDao;
//	private TreeViewer tvDBList;
	
	private Text textUserEMail;
	private TableViewer tableViewer;
	private TableViewer tableViewerTargetDB;
	private TableViewer tableViewerSelectUser;
	private List<UserDAO> listUserGroup = new ArrayList<UserDAO>();
	private List<UserDAO> listSelectUserGroup = new ArrayList<UserDAO>();
	private List<UserDBDAO> listUserDBs = new ArrayList<UserDBDAO>();
	
	private TadpoleUserDbRoleDAO tadpoleUserRoleDao;
	private Table tableUserRole;
	private Table tableDB;

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param tvDBList
	 */
	public FindUserAndDBRoleDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		//this.userDBDao = userDBDao; 
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().FindUserAndDBRoleDialog_0);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		
		SashForm sashForm = new SashForm(container, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		SashForm sashForm_1 = new SashForm(sashForm, SWT.NONE);
		
		Composite compositeUserSearch = new Composite(sashForm_1, SWT.NONE);
		compositeUserSearch.setLayout(new GridLayout(1, false));
		
		Label label_1 = new Label(compositeUserSearch, SWT.NONE);
		label_1.setText("사용자 검색");
		
		Composite compositeHead = new Composite(compositeUserSearch, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		compositeHead.setLayout(new GridLayout(3, false));
		
		Label lblEmail = new Label(compositeHead, SWT.NONE);
		lblEmail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEmail.setText(Messages.get().FindUserAndDBRoleDialog_1);
		
		textUserEMail = new Text(compositeHead, SWT.BORDER);
		textUserEMail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) {
					search();
				}
			}
		});
		textUserEMail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSearch = new Button(compositeHead, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
		});
		btnSearch.setText(CommonMessages.get().Search);
		
		Composite compositeBody = new Composite(compositeUserSearch, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));
		
		tableViewer = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection iss = (IStructuredSelection)tableViewer.getSelection();
				if(iss.isEmpty()) {
					MessageDialog.openWarning(getShell(), CommonMessages.get().Warning, Messages.get().PleaseSelectUser);
					return;
				}
				UserDAO userDAO = (UserDAO)iss.getFirstElement();
				
				listUserGroup.remove(userDAO);
				tableViewer.refresh();
				
				listSelectUserGroup.add(userDAO);
				tableViewerSelectUser.setInput(listSelectUserGroup);
				tableViewerSelectUser.refresh();
			}
		});
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createSearchColumns();
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new UserLabelProvider());
		
		textUserEMail.setFocus();
		
		Composite compositeDB = new Composite(sashForm_1, SWT.NONE);
		compositeDB.setLayout(new GridLayout(1, false));
		
		Label lblDb = new Label(compositeDB, SWT.NONE);
		lblDb.setText("권한부여 대상 DB");
		
		 tableViewerTargetDB = new TableViewer(compositeDB, SWT.BORDER | SWT.FULL_SELECTION);
		tableDB = tableViewerTargetDB.getTable();
		tableDB.setLinesVisible(true);
		tableDB.setHeaderVisible(true);
		tableDB.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sashForm_1.setWeights(new int[] {1, 1});
		
		createTargetDBColumns();
		
		tableViewerTargetDB.setContentProvider(new ArrayContentProvider());
		tableViewerTargetDB.setLabelProvider(new TargetDBLabelProvider());
		
		Composite compositeUser = new Composite(sashForm, SWT.BORDER);
		compositeUser.setLayout(new GridLayout(1, false));
		
		Label label_3 = new Label(compositeUser, SWT.NONE);
		label_3.setText("권한부여 대상자");
		
		tableViewerSelectUser = new TableViewer(compositeUser, SWT.BORDER | SWT.FULL_SELECTION);
		tableUserRole = tableViewerSelectUser.getTable();
		tableUserRole.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection iss = (IStructuredSelection)tableViewerSelectUser.getSelection();
				if(iss.isEmpty()) {
					MessageDialog.openWarning(getShell(), CommonMessages.get().Warning, Messages.get().PleaseSelectUser);
					return;
				}
				UserDAO userDAO = (UserDAO)iss.getFirstElement();
				if(!userDAO.isSelect()){
					listSelectUserGroup.remove(userDAO);
				}
				tableViewerSelectUser.refresh();
				
			}
		});
		tableUserRole.setLinesVisible(true);
		tableUserRole.setHeaderVisible(true);
		tableUserRole.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createSelectUserColumns();
		
		tableViewerSelectUser.setContentProvider(new ArrayContentProvider());
		tableViewerSelectUser.setLabelProvider(new SelectUserLabelProvider());

		sashForm.setWeights(new int[] {1, 1});
		initData();
		initUI();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}

	/**
	 * initialize UI
	 */
	private void initUI() {
		Calendar cal = Calendar.getInstance();
		
		cal.add(Calendar.DAY_OF_YEAR, 365 * 10);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		
		
		if(listUserDBs.size() <= 0 ) {
			MessageDialog.openWarning(getShell(), CommonMessages.get().Warning, Messages.get().PleaseSelectDB);
			return;
		}
		for (UserDBDAO userDBDao : listUserDBs ) {
		
			if(!userDBDao.isSelect()) continue;
			
			if(listSelectUserGroup.size() <= 0) {
				MessageDialog.openWarning(getShell(), CommonMessages.get().Warning, Messages.get().PleaseSelectUser);
				return;
			}
			for (UserDAO userDAO : listSelectUserGroup ){
				
				if("NONE".equals(userDAO.getRole_type())) { //$NON-NLS-1$
					MessageDialog.openWarning(getShell(), CommonMessages.get().Warning, Messages.get().FindUserAndDBRoleDialog_6);
					tableViewerSelectUser.getTable().setFocus();
					return;
				}
				
				// 사용자가 해당 디비에 추가 될수 있는지 검사합니다. 
				try {
					boolean isAddDBRole = TadpoleSystem_UserRole.isDBAddRole(userDBDao, userDAO);
					if(isAddDBRole) {
						if(!MessageDialog.openConfirm(getShell(), CommonMessages.get().Confirm, Messages.get().FindUserDialog_4)) return;
						
						Calendar calStart = Calendar.getInstance();
						calStart.set(userDAO.getService_start().getYear(), userDAO.getService_start().getMonth(), userDAO.getService_start().getDay(), 0, 0, 0);
	
						
						tadpoleUserRoleDao = TadpoleSystem_UserRole.insertTadpoleUserDBRole(userDAO.getSeq(), userDBDao.getSeq(), userDAO.getRole_type(), "*",  //$NON-NLS-1$
								new Timestamp(calStart.getTimeInMillis()), 
								userDAO.getService_end()
								);
						
						MessageDialog.openInformation(getShell(), CommonMessages.get().Confirm, Messages.get().FindUserAndDBRoleDialog_10);
						
					} else {
						MessageDialog.openWarning(getShell(), CommonMessages.get().Warning, Messages.get().FindUserAndDBRoleDialog_12);
					}
				} catch (Exception e) {
					logger.error(Messages.get().RoleType, e);
					MessageDialog.openError(getShell(),CommonMessages.get().Error, Messages.get().FindUserAndDBRoleDialog_15 + e.getMessage());
				}
			}
		} 
	}
	
	/**
	 * 검색.
	 */
	private void search() {
		String txtUserEmail = textUserEMail.getText();
		if("".equals(txtUserEmail)) return; //$NON-NLS-1$
		listUserGroup.clear();
		
		try {
			listUserGroup = TadpoleSystem_UserQuery.findUserList(txtUserEmail);
			
			tableViewer.setInput(listUserGroup);
			tableViewer.refresh();
		} catch(Exception e) {
			logger.error("search exception", e); //$NON-NLS-1$
			MessageDialog.openInformation(getShell(),CommonMessages.get().Error, e.getMessage());
		}
	}
	
	private void initData() {
		listUserDBs.clear();
		try {
			listUserDBs = TadpoleSystem_UserDBQuery.getCreateUserDB();
			
			
			tableViewerTargetDB.setInput(listUserDBs);
			tableViewerTargetDB.refresh();
			
		} catch (Exception e) {
			logger.error(Messages.get().DBListComposite_25, e);
		}
	}
	
	/**
	 * crate columns
	 */
	private void createSearchColumns() {
		String[] colNames = {CommonMessages.get().Name, CommonMessages.get().Email, Messages.get().CreateTime};
		int[] colSize = {150, 150, 120};
		
		for (int i=0; i<colSize.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			TableColumn tableColumn = tableViewerColumn.getColumn();
			tableColumn.setWidth(colSize[i]);
			tableColumn.setText(colNames[i]);
			
			if (i==0){
				tableViewerColumn.setEditingSupport(new UserSearchEditingSupport(tableViewer, i));
			}
		}
	}
	
	private void createSelectUserColumns() {
		String[] colNames = {CommonMessages.get().Name, CommonMessages.get().Email, Messages.get().CreateTime, "Role", "Start Date", "End Date"};
		int[] colSize = {150, 150, 120, 120, 200, 200};
		
		for (int i=0; i<colSize.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewerSelectUser, SWT.NONE);
			TableColumn tableColumn = tableViewerColumn.getColumn();
			tableColumn.setWidth(colSize[i]);
			tableColumn.setText(colNames[i]);
			
			
			tableViewerColumn.setEditingSupport(new SelectUserEditingSupport(tableViewerSelectUser, i));
			
		}
	}

	private void createTargetDBColumns() {
		String[] colNames = {CommonMessages.get().Name, "데이터베이스 정보", Messages.get().User};
		int[] colSize = {150, 150, 120};
		
		for (int i=0; i<colSize.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewerTargetDB, SWT.NONE);
			TableColumn tableColumn = tableViewerColumn.getColumn();
			tableColumn.setWidth(colSize[i]);
			tableColumn.setText(colNames[i]);
			
			if (i==0){
				tableViewerColumn.setEditingSupport(new TargetDBEditingSupport(tableViewerTargetDB, i));
			}
		}
	}

	public TadpoleUserDbRoleDAO getUserRoleDAO() {
		return tadpoleUserRoleDao;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, CommonMessages.get().Add, false);
		createButton(parent, IDialogConstants.CANCEL_ID, CommonMessages.get().Close, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(1022, 675);
	}
}

/**
* 유저 정보 레이블 
* 
* @author hangum
*
*/
class UserLabelProvider extends LabelProvider implements ITableLabelProvider {
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		UserDAO dao = (UserDAO) element;

		switch (columnIndex) {
		case 0:
			if (dao.isSelect())
				return GlobalImageUtils.getCheck();
			else
				return GlobalImageUtils.getUnCheck();
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		UserDAO user = (UserDAO)element;

		switch(columnIndex) {
		case 0: return user.getName();
		case 1: return user.getEmail();
		case 2: return user.getCreate_time();
		}
		
		return "*** not set column ***"; //$NON-NLS-1$
	}
	
}

class SelectUserLabelProvider extends LabelProvider implements ITableLabelProvider {
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		UserDAO dao = (UserDAO) element;

		switch (columnIndex) {
		case 0:
			if (dao.isSelect())
				return GlobalImageUtils.getCheck();
			else
				return GlobalImageUtils.getUnCheck();
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		UserDAO user = (UserDAO)element;

		switch(columnIndex) {
		case 0: return user.getName();
		case 1: return user.getEmail();
		case 2: return user.getCreate_time();
		case 3: return user.getRole_type();
		case 4: return user.getService_start()+"";
		case 5: return user.getService_end()+"";
		}
		
		return "*** not set column ***"; //$NON-NLS-1$
	}
	
}

class TargetDBLabelProvider extends LabelProvider implements ITableLabelProvider {
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		UserDBDAO dao = (UserDBDAO) element;

		switch (columnIndex) {
		case 0:
			if (dao.isSelect())
				return GlobalImageUtils.getCheck();
			else
				return GlobalImageUtils.getUnCheck();
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		UserDBDAO user = (UserDBDAO)element;

		
		if(element instanceof UserDBDAO) {
			UserDBDAO userDB = (UserDBDAO)element;

			switch(columnIndex) {
				case 0: return ManagerLabelProvider.getDBText(userDB);
				case 1:
					// sqlite
					if("".equals(userDB.getHost())) return userDB.getUrl(); //$NON-NLS-1$
					return userDB.getHost() + " : "  + userDB.getPort(); //$NON-NLS-1$
				case 2: return userDB.getUsers();
			}

		} 
		
		
		
		
		return "*** not set column ***"; //$NON-NLS-1$
	}
	
}