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

import java.util.ArrayList;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.manager.core.Messages;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserQuery;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserRole;

/**
 * 사용자를 그룹에 추가하기 위한 다이얼로그.
 * 
 * @author hangum
 *
 */
public class FindUserDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(FindUserDialog.class);
	
	private int BTN_ADD = IDialogConstants.CLIENT_ID + 1;
	
	private Text textEMail;
	private TableViewer tableViewer;
	private List<UserDAO> listUserGroup = new ArrayList<UserDAO>();

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public FindUserDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Find User"); //$NON-NLS-1$
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
		lblEmail.setText("EMail"); //$NON-NLS-1$
		
		textEMail = new Text(compositeHead, SWT.BORDER);
		textEMail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) {
					search();
				}
			}
		});
		textEMail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSearch = new Button(compositeHead, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
		});
		btnSearch.setText("Search"); //$NON-NLS-1$
		
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
		
		textEMail.setFocus();

		return container;
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		
		if(buttonId == BTN_ADD) {
			IStructuredSelection iss = (IStructuredSelection)tableViewer.getSelection();
			if(iss.isEmpty()) return;
				
			if(MessageDialog.openConfirm(null, "Confirm", Messages.FindUserDialog_4)) { //$NON-NLS-1$
				UserDAO userDAO = (UserDAO)iss.getFirstElement();
				
				try {
					if(TadpoleSystem_UserRole.findGroupUserRole(SessionManager.getGroupSeq(), userDAO.getSeq())) {
						MessageDialog.openError(null, "Error", Messages.FindUserDialog_6); //$NON-NLS-1$
						return;
					} else {
						TadpoleSystem_UserRole.newUserRole(
									SessionManager.getGroupSeq(), 
									userDAO.getSeq(), 
									PublicTadpoleDefine.USER_TYPE.USER.toString(), 
									PublicTadpoleDefine.YES_NO.YES.toString(), 
									userDAO.getName()
								);
					}
				} catch(Exception e) {
					logger.error("Fine user role", e); //$NON-NLS-1$
				}
				
			}
		}
	}
	
	/**
	 * 검색.
	 */
	private void search() {
		listUserGroup.clear();
		
		String txtEmail = textEMail.getText();

		try {
			listUserGroup =  TadpoleSystem_UserQuery.findLikeUser(txtEmail);
			tableViewer.setInput(listUserGroup);
			tableViewer.refresh();
		} catch(Exception e) {
			logger.error("search exception", e); //$NON-NLS-1$
		}
	}
	
	/**
	 * crate columns
	 */
	private void createColumns() {
		String[] colNames = {"Email", "Name", "Create Time"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		int[] colSize = {130, 150, 120};
		
		for (int i=0; i<colSize.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			TableColumn tableColumn = tableViewerColumn.getColumn();
			tableColumn.setWidth(colSize[i]);
			tableColumn.setText(colNames[i]);
		}
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, BTN_ADD, "Add", false); //$NON-NLS-1$
		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false); //$NON-NLS-1$
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 400);
	}

}

/**
* 유저 정보 레이블 
* 
* @author hangum
*
*/
class UserLabelProvider extends LabelProvider implements ITableLabelProvider {
	private static final Logger logger = Logger.getLogger(UserLabelProvider.class);
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		UserDAO user = (UserDAO)element;

		switch(columnIndex) {
		case 0: return user.getEmail();
		case 1: return user.getName();
		case 2: return user.getCreate_time();
		}
		
		return "*** not set column ***"; //$NON-NLS-1$
	}
	
}