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
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.rap.addons.d3chart.BarChart;
import org.eclipse.rap.addons.d3chart.ChartItem;
import org.eclipse.rap.addons.d3chart.ColorStream;
import org.eclipse.rap.addons.d3chart.Colors;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.googleauth.GoogleAuthManager;
import com.hangum.tadpole.application.start.BrowserActivator;
import com.hangum.tadpole.application.start.Messages;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.RequestInfoUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserQuery;
import com.hangum.tadpole.manager.core.dialogs.users.NewUserDialog;
import com.hangum.tadpole.session.manager.SessionManager;
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
	
	private int ID_NEW_USER		 	= IDialogConstants.CLIENT_ID 	+ 1;
	private int ID_FINDPASSWORD 	= IDialogConstants.CLIENT_ID 	+ 2;

	/** database list */
	private List listDBMart = new ArrayList();
	
	private Text textEMail;
	private Text textPasswd;
	
	public LoginDialog(Shell shell) {
		super(shell);
	}
	
	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.LoginDialog_0);
		newShell.setImage(ResourceManager.getPluginImage(BrowserActivator.ID, "resources/Tadpole15-15.png")); //$NON-NLS-1$
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
		button.setImage(ResourceManager.getPluginImage(BrowserActivator.ID, "resources/TadpoleOverView.png")); //$NON-NLS-1$
		
		Composite compositeLogin = new Composite(container, SWT.NONE);
		compositeLogin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeLogin.setLayout(new GridLayout(2, false));
		
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

		// ---------------------  Registered database ----------------------------------------------------
		try {
			listDBMart = getDBMart();
			if(!listDBMart.isEmpty()) {
				Group grpSponser = new Group(container, SWT.NONE);
				GridLayout gl_grpSponser = new GridLayout(1, false);
				gl_grpSponser.verticalSpacing = 0;
				gl_grpSponser.horizontalSpacing = 0;
				gl_grpSponser.marginHeight = 0;
				gl_grpSponser.marginWidth = 0;
				grpSponser.setLayout(gl_grpSponser);
				grpSponser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
				grpSponser.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
				grpSponser.setText(Messages.LoginDialog_grpSponser_text);
				
				makeBarChart(grpSponser, listDBMart);
			}
		} catch(Exception e) {
			logger.error("get initdata", e); //$NON-NLS-1$
		}
		
		Group compositeLetter = new Group(container, SWT.NONE);
		compositeLetter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		GridLayout gl_compositeLetter = new GridLayout(2, false);
		compositeLetter.setLayout(gl_compositeLetter);
		compositeLetter.setText(Messages.LoginDialog_grpShowInformation_text);
		
		Label lblSite = new Label(compositeLetter, SWT.NONE);
		lblSite.setText(Messages.LoginDialog_lblSite_text);
		
		Label lblNewLabel = new Label(compositeLetter, SWT.NONE);
		lblNewLabel.setText("<a href='" + Messages.LoginDialog_lblNewLabel_text_1 + "' target='_blank'>" + Messages.LoginDialog_lblNewLabel_text_1 + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
		lblUserEng.setText("<a href='https://github.com/hangum/TadpoleForDBTools/wiki/RDB-User-Guide-Eng' target='_blank'>(English)</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		lblUserEng.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		
		Label lblUserIndonesia = new Label(compositeUserGide, SWT.NONE);
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
		
		textEMail.setFocus();
		AnalyticCaller.track("login"); //$NON-NLS-1$
		
		initUI();
		
		return compositeLogin;
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == ID_NEW_USER) {
			newUser();
		
		} else if(buttonId == ID_FINDPASSWORD) {
			findPassword();

		} else {
			okPressed();
		}
	}
	
	@Override
	protected void okPressed() {
		String strEmail = StringUtils.trimToEmpty(textEMail.getText());
		String strPass = StringUtils.trimToEmpty(textPasswd.getText());

		if(!validation(strEmail, strPass)) return;
		
		try {
			UserDAO userDao = TadpoleSystem_UserQuery.login(strEmail, strPass);
			
			// firsttime email confirm
			if(PublicTadpoleDefine.YES_NO.NO.name().equals(userDao.getIs_email_certification())) {
				InputDialog inputDialog=new InputDialog(getShell(), "Email Key Dialog", "Input email confirm KEY", "", null);
				if(inputDialog.open() == Window.OK) {
					if(!userDao.getEmail_key().equals(inputDialog.getValue())) {
						throw new Exception("Do not correct email confirm message. check your email.");
					} else {
						TadpoleSystem_UserQuery.updateEmailConfirm(strEmail);
					}
				} else {
					throw new Exception("Please input the email key.");
				}
			}
			
			if(PublicTadpoleDefine.YES_NO.NO.name().equals(userDao.getApproval_yn())) {
				MessageDialog.openError(getParentShell(), Messages.LoginDialog_7, Messages.LoginDialog_27);
				
				return;
			}
			
			if(PublicTadpoleDefine.YES_NO.YES.name().equals(userDao.getUse_otp())) {
				OTPLoginDialog otpDialog = new OTPLoginDialog(getShell());
				otpDialog.open(); 

				if(!GoogleAuthManager.getInstance().isValidate(userDao.getOtp_secret(), otpDialog.getIntOTPCode())) {
					throw new Exception(Messages.LoginDialog_2);
				}
			} 
			
			SessionManager.addSession(userDao);
			
			// save login_history
			TadpoleSystem_UserQuery.saveLoginHistory(userDao.getSeq());
		} catch (Exception e) {
			logger.error("Login exception. request email is " + strEmail, e); //$NON-NLS-1$
			MessageDialog.openError(getParentShell(), "Error", e.getMessage());
			
			textPasswd.setFocus();
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
		createButton(parent, ID_NEW_USER, Messages.LoginDialog_button_text_1, false);
		createButton(parent, ID_FINDPASSWORD, Messages.LoginDialog_lblFindPassword, false);
		createButton(parent, IDialogConstants.OK_ID, Messages.LoginDialog_15, true);
	}
	
	/**
	 * initialize ui
	 */
	private void initUI() {
		if(!RequestInfoUtils.isSupportBrowser()) {
			String errMsg = "User browser is  " + RequestInfoUtils.getUserBrowser() + ".\n" + Messages.UserInformationDialog_5 + "\n" + Messages.LoginDialog_lblNewLabel_text;
			MessageDialog.openError(getParentShell(), Messages.LoginDialog_7, errMsg);
		}
	}
	
	/**
	 * 데이터베이스 통계 bar chart를 생성합니다. 
	 * 
	 * @param composite
	 * @param listData
	 */
	private void makeBarChart(Composite compositeCursor, List listData) {
		try {
			ColorStream colors = Colors.cat20Colors(compositeCursor.getDisplay()).loop();
			
			BarChart barChart = new BarChart(compositeCursor, SWT.NONE);
			GridLayout gl_grpConnectionInfo = new GridLayout(1, true);
			gl_grpConnectionInfo.verticalSpacing = 0;
			gl_grpConnectionInfo.horizontalSpacing = 0;
			gl_grpConnectionInfo.marginHeight = 0;
			gl_grpConnectionInfo.marginWidth = 0;
			barChart.setLayout(gl_grpConnectionInfo);
			barChart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			barChart.setBarWidth(10);
			
			for(Object element : listData) {
				Map<String, Object> retMap = (HashMap<String, Object>)element;
				
				ChartItem item = new ChartItem(barChart);
			    item.setText(retMap.get("dbms_type") + " (" +  retMap.get("tot") + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			    item.setColor(colors.next());
			    
			    float floatVal = Float.parseFloat(""+retMap.get("tot")) / 300; //$NON-NLS-1$ //$NON-NLS-2$
			    item.setValue(floatVal);
			}
			
			barChart.layout();
			barChart.getParent().layout();
		} catch(Exception e) {
			logger.error("Get registered DB", e); //$NON-NLS-1$
		}
	}
	
	/**
	 * registered database
	 * 
	 * @return
	 */
	private List getDBMart() throws Exception {
		return TadpoleSystem_UserDBQuery.getRegisteredDB();
	}

	private void newUser() {
		NewUserDialog newUser = new NewUserDialog(getParentShell());
		newUser.open();
	}
	
	private void findPassword() {
		FindPasswordDialog dlg = new FindPasswordDialog(getShell());
		dlg.open();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		if(listDBMart.isEmpty()) {
			return new Point(540, 310);
		} else {
			return new Point(540, 470);
		}
	}
}