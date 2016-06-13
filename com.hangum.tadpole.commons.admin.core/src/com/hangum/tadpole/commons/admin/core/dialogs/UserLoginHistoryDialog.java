/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.admin.core.dialogs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.admin.core.Messages;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.dao.system.UserLoginHistoryDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.engine.utils.TimeZoneUtil;

/**
 * User login history dialog
 *
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 5. 30.
 *
 */
public class UserLoginHistoryDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(UserLoginHistoryDialog.class);
	private UserDAO userDao;
	
	private Text textEmail;
	private DateTime dateTimeStart;
	private DateTime dateTimeEnd;
	private TableViewer tvHistory;
	
	private List<UserLoginHistoryDAO> listLoginHistory = new ArrayList<>();

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param userDao 
	 */
	public UserLoginHistoryDialog(Shell parentShell, UserDAO userDao) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.userDao = userDao;
	}
	
	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().UserLoginHistoryDialog_0);
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
		compositeHead.setLayout(new GridLayout(5, false));
		
		Label lblEmail = new Label(compositeHead, SWT.NONE);
		lblEmail.setText(Messages.get().email);
		
		textEmail = new Text(compositeHead, SWT.BORDER);
		textEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) search();
			}
		});
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		Button btnSearch = new Button(compositeHead, SWT.NONE);
		btnSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
		});
		btnSearch.setText(Messages.get().Search);
		
		Label lblDate = new Label(compositeHead, SWT.NONE);
		lblDate.setText("로그인 시간");
						
		dateTimeStart = new DateTime(compositeHead, SWT.BORDER | SWT.DROP_DOWN);
		Label label = new Label(compositeHead, SWT.NONE);
		label.setText("~"); //$NON-NLS-1$
								
		dateTimeEnd = new DateTime(compositeHead, SWT.BORDER | SWT.DROP_DOWN);
		new Label(compositeHead, SWT.NONE);
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));
		
		tvHistory = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tvHistory.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tvHistory, SWT.NONE);
		TableColumn tblclmnIp = tableViewerColumn.getColumn();
		tblclmnIp.setWidth(100);
		tblclmnIp.setText(Messages.get().IP);
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tvHistory, SWT.NONE);
		TableColumn tblclmnConnectionTime = tableViewerColumn_1.getColumn();
		tblclmnConnectionTime.setWidth(200);
		tblclmnConnectionTime.setText(Messages.get().UserLoginHistoryDialog_4);
		
//		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tvHistory, SWT.NONE);
//		TableColumn tblclmnLogoutTime = tableViewerColumn_2.getColumn();
//		tblclmnLogoutTime.setWidth(200);
//		tblclmnLogoutTime.setText(Messages.get().UserLoginHistoryDialog_5);
		
		tvHistory.setContentProvider(new ArrayContentProvider());
		tvHistory.setLabelProvider(new LoginHistoryLabelProvider());
		
		initUI();
		
		textEmail.setFocus();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}
	
	/** 
	 * initialize ui
	 */
	private void initUI() {
		// Range of date
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -7);
		dateTimeStart.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		
		textEmail.setText(userDao.getEmail());
	}
	
	private void search() {
		String strEmail = textEmail.getText();
		if("".equals(strEmail)) { //$NON-NLS-1$
			listLoginHistory.clear();
			tvHistory.setInput(listLoginHistory);
			
			MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().UserLoginHistoryDialog_8);
			return;
		}
		
		try {
			Calendar cal = Calendar.getInstance();
			cal.set(dateTimeStart.getYear(), dateTimeStart.getMonth(), dateTimeStart.getDay(), 0, 0, 0);
			long startTime = cal.getTimeInMillis();
			cal.set(dateTimeEnd.getYear(), dateTimeEnd.getMonth(), dateTimeEnd.getDay(), 23, 59, 59);
			long endTime = cal.getTimeInMillis();
			
			listLoginHistory = TadpoleSystem_UserQuery.getLoginHistory(strEmail, startTime, endTime);
			tvHistory.setInput(listLoginHistory);
		} catch (Exception e) {
			logger.error("find login history", e); //$NON-NLS-1$
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Close, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 400);
	}

}

class LoginHistoryLabelProvider  extends LabelProvider implements ITableLabelProvider {
	private static final Logger logger = Logger.getLogger(LoginHistoryLabelProvider.class);

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		UserLoginHistoryDAO dao = (UserLoginHistoryDAO)element;
		
		switch(columnIndex) {
		case 0 : return dao.getLogin_ip();
		case 1 : return dao.getConnet_time() == null?dao.getSqliteConnet_time():TimeZoneUtil.dateToStr(dao.getConnet_time());
		case 2 : return dao.getDisconnect_time() == null?"":TimeZoneUtil.dateToStr(dao.getDisconnect_time()); //$NON-NLS-1$
		}
		
		return "*** not column setting ***"; //$NON-NLS-1$
	}
	
}