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
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.system.TadpoleUserDbRoleDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserRole;
import com.hangum.tadpole.manager.core.Messages;

/**
 * 사용자를 디비 그룹에 추가하고, 사용자 역할을 설정합니다.
 * 
 * @author hangum
 *
 */
public class FindUserAndDBRoleDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(FindUserAndDBRoleDialog.class);
	
	private UserDBDAO userDBDao;
	
	private Text textUserEMail;
	private TableViewer tableViewer;
	private List<UserDAO> listUserGroup = new ArrayList<UserDAO>();
	
	private Combo comboRoleType;
	private DateTime dateTimeStart;
	private DateTime dateTimeEndDay;
	private DateTime dateTimeEndTime;
	
	private TadpoleUserDbRoleDAO tadpoleUserRoleDao;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public FindUserAndDBRoleDialog(Shell parentShell, UserDBDAO userDBDao) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.userDBDao = userDBDao;
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
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
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
		btnSearch.setText(Messages.get().Search);
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tableViewer = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		createColumns();
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new UserLabelProvider());
		
		Composite composite = new Composite(compositeBody, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(5, false));
		
		Label lblRoleType = new Label(composite, SWT.NONE);
		lblRoleType.setText(Messages.get().RoleType);
		
		comboRoleType = new Combo(composite, SWT.NONE | SWT.READ_ONLY);
		comboRoleType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		comboRoleType.add("NONE"); //$NON-NLS-1$
		comboRoleType.add(PublicTadpoleDefine.USER_ROLE_TYPE.ADMIN.toString());
		comboRoleType.add(PublicTadpoleDefine.USER_ROLE_TYPE.MANAGER.toString());
		comboRoleType.add(PublicTadpoleDefine.USER_ROLE_TYPE.USER.toString());
		comboRoleType.add(PublicTadpoleDefine.USER_ROLE_TYPE.GUEST.toString());
		comboRoleType.select(0);
		
		Label lblTermsUfUse = new Label(composite, SWT.NONE);
		lblTermsUfUse.setText(Messages.get().Term);
		
		dateTimeStart = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("~"); //$NON-NLS-1$
		
		dateTimeEndDay = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		
		dateTimeEndTime = new DateTime(composite, SWT.BORDER | SWT.TIME | SWT.SHORT);
		
		initUI();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
				
		textUserEMail.setFocus();

		return container;
	}

	/**
	 * initialize UI
	 */
	private void initUI() {
		Calendar cal = Calendar.getInstance();
		dateTimeStart.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		
		cal.add(Calendar.DAY_OF_YEAR, 365 * 10);
		dateTimeEndDay.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		
		dateTimeEndTime.setTime(23, 59, 59);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		IStructuredSelection iss = (IStructuredSelection)tableViewer.getSelection();
		if(iss.isEmpty()) return;
		UserDAO userDAO = (UserDAO)iss.getFirstElement();
		
		if("NONE".equals(comboRoleType.getText())) { //$NON-NLS-1$
			MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().FindUserAndDBRoleDialog_6);
			comboRoleType.setFocus();
			return;
		}
		
		// 사용자가 해당 디비에 추가 될수 있는지 검사합니다. 
		try {
			boolean isAddDBRole = TadpoleSystem_UserRole.isDBAddRole(userDBDao, userDAO);
			if(isAddDBRole) {
				if(!MessageDialog.openConfirm(getShell(), Messages.get().Confirm, Messages.get().FindUserDialog_4)) return;
				
				Calendar calStart = Calendar.getInstance();
				calStart.set(dateTimeStart.getYear(), dateTimeStart.getMonth(), dateTimeStart.getDay(), 0, 0, 0);

				Calendar calEnd = Calendar.getInstance();
				calEnd.set(dateTimeEndDay.getYear(), dateTimeEndDay.getMonth(), dateTimeEndDay.getDay(), dateTimeEndTime.getHours(), dateTimeEndTime.getMinutes(), 00);
				
				tadpoleUserRoleDao = TadpoleSystem_UserRole.insertTadpoleUserDBRole(userDAO.getSeq(), userDBDao.getSeq(), comboRoleType.getText(), "*",  //$NON-NLS-1$
						new Timestamp(calStart.getTimeInMillis()), 
						new Timestamp(calEnd.getTimeInMillis())
						);
				
				MessageDialog.openInformation(getShell(), Messages.get().Confirm, Messages.get().FindUserAndDBRoleDialog_10);
				
//				super.okPressed();
			} else {
				MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().FindUserAndDBRoleDialog_12);
			}
		} catch (Exception e) {
			logger.error(Messages.get().RoleType, e);
			MessageDialog.openError(getShell(), Messages.get().Error, Messages.get().FindUserAndDBRoleDialog_15 + e.getMessage());
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
			UserDAO userDAO = TadpoleSystem_UserQuery.findUser(txtUserEmail);
			if(userDAO != null) {
				listUserGroup.add(userDAO);
			}
			
			tableViewer.setInput(listUserGroup);
			tableViewer.refresh();
		} catch(Exception e) {
			logger.error("search exception", e); //$NON-NLS-1$
			MessageDialog.openInformation(getShell(), Messages.get().Error, e.getMessage());
		}
	}
	
	/**
	 * crate columns
	 */
	private void createColumns() {
		String[] colNames = {Messages.get().Name, Messages.get().Email, Messages.get().CreateTime};
		int[] colSize = {150, 150, 120};
		
		for (int i=0; i<colSize.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			TableColumn tableColumn = tableViewerColumn.getColumn();
			tableColumn.setWidth(colSize[i]);
			tableColumn.setText(colNames[i]);
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
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Add, false);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Close, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 450);
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