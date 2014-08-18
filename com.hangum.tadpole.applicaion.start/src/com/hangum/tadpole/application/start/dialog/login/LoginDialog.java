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
package com.hangum.tadpole.application.start.dialog.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.rap.rwt.RWT;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.googleauth.GoogleAuthManager;
import com.hangum.tadpole.application.start.BrowserActivator;
import com.hangum.tadpole.application.start.Messages;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.manager.core.dialogs.users.NewUserDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.system.UserDAO;
import com.hangum.tadpole.sql.query.TadpoleSystemInitializer;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserQuery;
import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;

/**
 * Tadpole DB Hub User login dialog.
 * support the localization : (http://wiki.eclipse.org/RAP/FAQ#How_to_switch_locale.2Flanguage_on_user_action.3F) 
 * 
 * @author hangum
 *
 */
public class LoginDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(LoginDialog.class);
	
	private int ID_NEW_USER		 = IDialogConstants.CLIENT_ID 	+ 1;
	private int ID_ADMIN_USER 	= IDialogConstants.CLIENT_ID 	+ 3;
	private int ID_MANAGER_USER = IDialogConstants.CLIENT_ID 	+ 4;
	
	private Text textEMail;
	private Text textPasswd;

	private TableViewer tableViewer;
	
	public LoginDialog(Shell shell) {
		super(shell);
	}
	
	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.LoginDialog_0);
		newShell.setImage(ResourceManager.getPluginImage(BrowserActivator.ID, "resources/Tadpole15-15.png"));
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
		
		Composite compositeLeftBtn = new Composite(container, SWT.NONE);
		compositeLeftBtn.setLayout(new GridLayout(1, false));
		
		Button button = new Button(compositeLeftBtn, SWT.NONE);
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		button.setImage(ResourceManager.getPluginImage(BrowserActivator.ID, "resources/TadpoleOverView.png"));
		
		Composite compositeLogin = new Composite(container, SWT.NONE);
		compositeLogin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeLogin.setLayout(new GridLayout(2, false));
		
		Label lblPleaseSignIn = new Label(compositeLogin, SWT.NONE);
		lblPleaseSignIn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblPleaseSignIn.setText(Messages.LoginDialog_lblPleaseSignIn_text);
		
		Label lblEmail = new Label(compositeLogin, SWT.NONE);
		lblEmail.setText(Messages.LoginDialog_1);
		
		textEMail = new Text(compositeLogin, SWT.BORDER);
		textEMail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(compositeLogin, SWT.NONE);
		lblPassword.setText(Messages.LoginDialog_4);
		
		textPasswd = new Text(compositeLogin, SWT.BORDER | SWT.PASSWORD);
		textPasswd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) {
					okPressed();
				}
			}
		});
		textPasswd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		new Label(compositeLogin, SWT.NONE);
		Button btnFindPassword = new Button(compositeLogin, SWT.PUSH);
		btnFindPassword.setText(Messages.LoginDialog_lblFindPassword);
		setButtonLayoutData(btnFindPassword);
		
		btnFindPassword.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				findPassword();
			}
		});
		
		Group compositeLetter = new Group(container, SWT.NONE);
		compositeLetter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		GridLayout gl_compositeLetter = new GridLayout(2, false);
		compositeLetter.setLayout(gl_compositeLetter);
		compositeLetter.setText(Messages.LoginDialog_grpShowInformation_text);
		
		Label lblSite = new Label(compositeLetter, SWT.NONE);
		lblSite.setText(Messages.LoginDialog_lblSite_text);
		
		Label lblNewLabel = new Label(compositeLetter, SWT.NONE);
		lblNewLabel.setText("<a href='" + Messages.LoginDialog_lblNewLabel_text_1 + "' target='_blank'>" + Messages.LoginDialog_lblNewLabel_text_1 + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		lblNewLabel.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		
		Label lblUserGuide = new Label(compositeLetter, SWT.NONE);
		lblUserGuide.setText(Messages.LoginDialog_lblUserGuide_text);
		
		Composite compositeUserGide = new Composite(compositeLetter, SWT.NONE);
		compositeUserGide.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeUserGide = new GridLayout(3, false);
		gl_compositeUserGide.verticalSpacing = 1;
		gl_compositeUserGide.horizontalSpacing = 1;
		gl_compositeUserGide.marginHeight = 1;
		gl_compositeUserGide.marginWidth = 1;
		compositeUserGide.setLayout(gl_compositeUserGide);
		
		Label lblUserKor = new Label(compositeUserGide, SWT.NONE);
		lblUserKor.setText("<a href='https://tadpoledbhub.atlassian.net/wiki/pages/viewpage.action?pageId=2621445' target='_blank'>(Korean)</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		lblUserKor.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		
		Label lblUserEng = new Label(compositeUserGide, SWT.NONE);
		lblUserEng.setSize(607, 14);
		lblUserEng.setText("<a href='https://github.com/hangum/TadpoleForDBTools/wiki/RDB-User-Guide-Eng' target='_blank'>(English)</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		lblUserEng.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		
		Label lblUserIndonesia = new Label(compositeUserGide, SWT.NONE);
		lblUserIndonesia.setSize(611, 14);
		lblUserIndonesia.setText("<a href='https://github.com/hangum/TadpoleForDBTools/wiki/RDB-User-Guide-ID' target='_blank'>(Indonesia)</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		lblUserIndonesia.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		
		Label lblIssues = new Label(compositeLetter, SWT.NONE);
		lblIssues.setText(Messages.LoginDialog_lblIssues_text);
		
		Label lblIssue = new Label(compositeLetter, SWT.NONE);
		lblIssue.setText("<a href='https://github.com/hangum/TadpoleForDBTools/issues' target='_blank'>https://github.com/hangum/TadpoleForDBTools/issues</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		lblIssue.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		
		Label lblContact = new Label(compositeLetter, SWT.NONE);
		lblContact.setText(Messages.LoginDialog_lblContact_text_1);
		
		Label lblContactUrl = new Label(compositeLetter, SWT.NONE);
		lblContactUrl.setText("<a href='mailto:adi.tadpole@gmail.com'>adi.tadpole@gmail.com</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		lblContactUrl.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		
		Group grpSponser = new Group(container, SWT.NONE);
		grpSponser.setLayout(new GridLayout(1, false));
		grpSponser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		grpSponser.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		grpSponser.setText(Messages.LoginDialog_grpSponser_text);
		
		tableViewer = new TableViewer(grpSponser, SWT.NONE | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnSeq = tableViewerColumn.getColumn();
		tblclmnSeq.setWidth(120);
		tblclmnSeq.setText(Messages.LoginDialog_tblclmnSeq_text);
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnName = tableViewerColumn_1.getColumn();
		tblclmnName.setWidth(100);
		tblclmnName.setText(Messages.LoginDialog_tblclmnName_text);

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new RegisteredDBLabelprovider());
		tableViewer.setInput(getInitData());

		textEMail.setFocus();
		AnalyticCaller.track("login");
		
		return compositeLogin;
	}
	
	/**
	 * registered database
	 * 
	 * @return
	 */
	private List getInitData() {
		List retList = new ArrayList();
		try {
			retList = TadpoleSystem_UserDBQuery.getRegisteredDB();
		} catch (Exception e) {
			logger.error("getRegistered db", e);
		}
		
		return retList;
	}

	private void newUser() {
		NewUserDialog newUser = new NewUserDialog(getParentShell(), PublicTadpoleDefine.YES_NO.YES);
		newUser.open();
	}
	
	private void findPassword() {
		FindPasswordDialog dlg = new FindPasswordDialog(getShell());
		dlg.open();
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		 if(buttonId == ID_NEW_USER) {
			newUser();
				
			return;
		 } else if(buttonId == IDialogConstants.OK_ID) {
			 okPressed();
			 
		 } else {
			String userId = "", password = ""; //$NON-NLS-1$ //$NON-NLS-2$
			
			if(buttonId == ID_ADMIN_USER) {
				userId = TadpoleSystemInitializer.ADMIN_EMAIL;
				password = TadpoleSystemInitializer.ADMIN_PASSWD;
				
			} else if(buttonId == ID_MANAGER_USER) {
				userId = TadpoleSystemInitializer.MANAGER_EMAIL;
				password = TadpoleSystemInitializer.MANAGER_PASSWD;
			}
			
			// 정상이면 session에 로그인 정보를 입력하고 
			try {
				// template code
				SessionManager.addSession(TadpoleSystem_UserQuery.login(userId, password));
				
			} catch (Exception e) {
				logger.error(Messages.LoginDialog_9, e);
				MessageDialog.openError(getParentShell(), Messages.LoginDialog_7, e.getMessage());
				return;
			}	
			
			super.okPressed();
		}
	}
	
	@Override
	protected void okPressed() {
		String strEmail = StringUtils.trimToEmpty(textEMail.getText());
		String strPass = StringUtils.trimToEmpty(textPasswd.getText());

		if(!validation(strEmail, strPass)) return;
		
		try {
			UserDAO userDao = TadpoleSystem_UserQuery.login(strEmail, strPass);
			if(PublicTadpoleDefine.YES_NO.YES.toString().equals(userDao.getUse_otp())) {
				OTPLoginDialog otpDialog = new OTPLoginDialog(getShell());
				otpDialog.open(); 

				if(!GoogleAuthManager.getInstance().isValidate(userDao.getOtp_secret(), otpDialog.getIntOTPCode())) {
					throw new Exception(Messages.LoginDialog_2);
				}
			} 
			
			SessionManager.addSession(userDao);
		} catch (Exception e) {
			logger.error("Login exception", e); //$NON-NLS-1$
			MessageDialog.openError(getParentShell(), Messages.LoginDialog_7, e.getMessage());
			
			textEMail.setFocus();
			return;
		}	
		
		super.okPressed();
	}
		
	@Override
	public boolean close() {
		//  로그인이 안되었을 경우 로그인 창이 남아 있도록...(https://github.com/hangum/TadpoleForDBTools/issues/31)
		if(!SessionManager.isLogin()) return false;
		
		return super.close();
	}

	/**
	 * validation
	 * 
	 * @param strEmail
	 * @param strPass
	 */
	private boolean validation(String strEmail, String strPass) {
		// validation
		if("".equals(strEmail)) { //$NON-NLS-1$
			MessageDialog.openError(getParentShell(), Messages.LoginDialog_7, Messages.LoginDialog_11);
			textEMail.setFocus();
			return false;
		} else if("".equals(strPass)) { //$NON-NLS-1$
			MessageDialog.openError(getParentShell(), Messages.LoginDialog_7, Messages.LoginDialog_14);
			textPasswd.setFocus();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
//		createButton(parent, ID_ADMIN_USER, "Admin", false);
		
		// -test 일 경우만 ..
		if(ApplicationArgumentUtils.isTestMode()) {
			createButton(parent, ID_MANAGER_USER, Messages.LoginDialog_12, false);
		}
		
		Button button = createButton(parent, ID_NEW_USER, Messages.LoginDialog_16, false);
		button.setText(Messages.LoginDialog_button_text);
		createButton(parent, IDialogConstants.OK_ID, Messages.LoginDialog_15, true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 500);
	}
}

class RegisteredDBLabelprovider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		Map<String, Object> retMap = (HashMap<String, Object>)element;
		
		switch(columnIndex) {
		case 0: 
			String dbmsType = ""+retMap.get("dbms_types");
			DBDefine dbType = DBDefine.getDBDefine(dbmsType);
			
			if(DBDefine.MYSQL_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/mysql-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.MARIADB_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/mariadb-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.ORACLE_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/oracle-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.SQLite_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/sqlite-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.MSSQL_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/mssql-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.CUBRID_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/cubrid-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.POSTGRE_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/postgresSQL-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.MONGODB_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/mongodb-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.HIVE_DEFAULT == dbType || DBDefine.HIVE2_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/hive-add.png"); //$NON-NLS-1$
			
			else if(DBDefine.TAJO_DEFAULT == dbType) 
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/tajo-add.jpg"); //$NON-NLS-1$
		}
		
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Map<String, Object> retMap = (HashMap<String, Object>)element;
		
		switch(columnIndex) {
		case 0: return ""+retMap.get("dbms_types");
		case 1: return ""+retMap.get("tot");
		}
		
		return "*** not set column ***";
	}
	
}