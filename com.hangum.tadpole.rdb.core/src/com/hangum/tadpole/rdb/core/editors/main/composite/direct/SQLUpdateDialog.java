///*******************************************************************************
// * Copyright (c) 2014 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.rdb.core.editors.main.composite.direct;
//
//import org.apache.log4j.Logger;
//import org.eclipse.jface.dialogs.Dialog;
//import org.eclipse.jface.dialogs.IDialogConstants;
//import org.eclipse.jface.dialogs.MessageDialog;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Text;
//
//import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
//import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
//import com.hangum.tadpole.commons.util.GlobalImageUtils;
//import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
//import com.hangum.tadpole.engine.utils.EditorDefine;
//import com.hangum.tadpole.engine.utils.RequestQuery;
//import com.hangum.tadpole.rdb.core.Messages;
//import com.hangum.tadpole.rdb.core.editors.main.execute.sub.ExecuteOtherSQL;
//import com.hangum.tadpole.session.manager.SessionManager;
//
///**
// * SQL update dialog
// * 
// * @author hangum
// *
// */
//public class SQLUpdateDialog extends Dialog {
//	private static final Logger logger = Logger.getLogger(SQLUpdateDialog.class);
//	
//	private UserDBDAO userDB;
//	private String strSQL = "";
//	private Text textSQL;
//
//	/**
//	 * Create the dialog.
//	 * 
//	 * @param parentShell
//	 * @param strSQL
//	 */
//	public SQLUpdateDialog(Shell parentShell, UserDBDAO userDB, String strSQL) {
//		super(parentShell);
//		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
//		
//		this.userDB = userDB;
//		this.strSQL = strSQL;
//	}
//	
//	@Override
//	protected void configureShell(Shell newShell) {
//		super.configureShell(newShell);
//		newShell.setText(Messages.get().SQLUpdateDialog_1);
//		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
//	}
//
//	/**
//	 * Create contents of the dialog.
//	 * @param parent
//	 */
//	@Override
//	protected Control createDialogArea(Composite parent) {
//		Composite container = (Composite) super.createDialogArea(parent);
//		GridLayout gridLayout = (GridLayout) container.getLayout();
//		gridLayout.verticalSpacing = 5;
//		gridLayout.horizontalSpacing = 5;
//		gridLayout.marginHeight = 5;
//		gridLayout.marginWidth = 5;
//		
//		Label lblPleaseCheckYour = new Label(container, SWT.NONE);
//		lblPleaseCheckYour.setText(Messages.get().SQLUpdateDialog_2);
//		
//		textSQL = new Text(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
//		textSQL.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		textSQL.setText(strSQL);
//
//		return container;
//	}
//	
//	/* (non-Javadoc)
//	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
//	 */
//	@Override
//	protected void okPressed() {
//		String strSQL = textSQL.getText();
//		
//		if("".equals(strSQL)) { //$NON-NLS-1$
//			MessageDialog.openWarning(getShell(), CommonMessages.get().Warning, Messages.get().SQLUpdateDialog_5);
//			return;
//		}
//		
//		RequestQuery reqQuery = new RequestQuery(userDB, strSQL, PublicTadpoleDefine.OBJECT_TYPE.TABLES, 
//					EditorDefine.QUERY_MODE.QUERY, EditorDefine.EXECUTE_TYPE.BLOCK, true);
//		try {
//			ExecuteOtherSQL.runPermissionSQLExecution(Messages.get().MainEditor_21, reqQuery, userDB, SessionManager.getRepresentRole(), SessionManager.getEMAIL());
//		} catch (Exception e) {
//			logger.error("SQL Execute error", e);
//			MessageDialog.openError(getShell(),CommonMessages.get().Error, Messages.get().SQLUpdateDialog_8 + e.getMessage());
//			return;
//		}
//		
//		super.okPressed();
//	}
//
//	/**
//	 * Create contents of the button bar.
//	 * @param parent
//	 */
//	@Override
//	protected void createButtonsForButtonBar(Composite parent) {
//		createButton(parent, IDialogConstants.OK_ID, Messages.get().Update, true);
//		createButton(parent, IDialogConstants.CANCEL_ID, CommonMessages.get().Close, false);
//	}
//
//	/**
//	 * Return the initial size of the dialog.
//	 */
//	@Override
//	protected Point getInitialSize() {
//		return new Point(650, 500);
//	}
//
//}
