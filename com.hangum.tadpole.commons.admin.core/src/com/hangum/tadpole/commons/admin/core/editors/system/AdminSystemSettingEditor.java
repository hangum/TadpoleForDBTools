/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.admin.core.editors.system;

import java.io.File;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.commons.admin.core.Activator;
import com.hangum.tadpole.commons.admin.core.Messages;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.system.UserInfoDataDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.preference.define.AdminPreferenceDefine;
import com.hangum.tadpole.preference.define.GetAdminPreference;
import com.hangum.tadpole.rdb.core.dialog.driver.JDBCDriverManageDialog;

/**
 * Admin System setting editor
 * 
 * @author hangum
 *
 */
public class AdminSystemSettingEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(AdminSystemSettingEditor.class);
	public static final String ID = "com.hangum.tadpole.admin.editor.admn.system.setting"; //$NON-NLS-1$
	private Combo comboNewUserPermit;
	private Text textResourceHome;
	
	private Combo comboIsAddDB;
	private Combo comboIsSharedDB;
	private Text textIntLimtCnt;
	private Text textDefaultUseDay;
	private Text textLog;

	public AdminSystemSettingEditor() {
		super();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		AdminSystemSettingEditorInput esqli = (AdminSystemSettingEditorInput) input;
		setPartName(esqli.getName());
	}
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite compositeHead = new Composite(parent, SWT.BORDER);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(compositeHead, SWT.FLAT | SWT.RIGHT);
		
		ToolItem tltmSave = new ToolItem(toolBar, SWT.NONE);
		tltmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveData();
			}
		});
		tltmSave.setToolTipText(Messages.get().Save);
		tltmSave.setImage(GlobalImageUtils.getSave());
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		ToolItem tltmJdbcDriverManage = new ToolItem(toolBar, SWT.NONE);
		tltmJdbcDriverManage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JDBCDriverManageDialog dialog = new JDBCDriverManageDialog(getSite().getShell());
				if(Dialog.OK ==  dialog.open()) {
					if(dialog.isUploaded()) {
						MessageDialog.openInformation(getSite().getShell(), Messages.get().Information, Messages.get().jdbcdriver);
					}
				}
			}
		});
		tltmJdbcDriverManage.setText(Messages.get().JDBCDriverManage);
		
		Composite compositeBody = new Composite(parent, SWT.NONE);
		compositeBody.setLayout(new GridLayout(2, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblLogDir = new Label(compositeBody, SWT.NONE);
		lblLogDir.setText("Log dir");
		
		textLog = new Text(compositeBody, SWT.BORDER);
		textLog.setEditable(false);
		textLog.setEnabled(false);
		textLog.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textLog.setText(new File(PublicTadpoleDefine.DEFAULT_LOG_FILE).getAbsolutePath());
		
		Label lblResourceHome = new Label(compositeBody, SWT.NONE);
		lblResourceHome.setText(Messages.get().ResourceHome);
		
		textResourceHome = new Text(compositeBody, SWT.BORDER);
		textResourceHome.setEnabled(false);
		textResourceHome.setEditable(false);
		textResourceHome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textResourceHome.setText(ApplicationArgumentUtils.getResourcesDir());
		
		Label lblNewLabel = new Label(compositeBody, SWT.NONE);
		lblNewLabel.setText(Messages.get().AdminSystemSettingEditor_2);
		
		comboNewUserPermit = new Combo(compositeBody, SWT.READ_ONLY);
		comboNewUserPermit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(PublicTadpoleDefine.YES_NO yesNo : PublicTadpoleDefine.YES_NO.values()) {
			comboNewUserPermit.add(yesNo.name());
		}
		new Label(compositeBody, SWT.NONE);
		
		Label labelHorizontal = new Label(compositeBody, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelHorizontal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeBody, SWT.NONE);
		
		Group grpSettingDefaultUser = new Group(compositeBody, SWT.NONE);
		grpSettingDefaultUser.setLayout(new GridLayout(4, false));
		grpSettingDefaultUser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpSettingDefaultUser.setText(Messages.get().SettingUser);
		
		Label lblNewLabel_1 = new Label(grpSettingDefaultUser, SWT.NONE);
		lblNewLabel_1.setText(Messages.get().IsAddDB);
		
		comboIsAddDB = new Combo(grpSettingDefaultUser, SWT.READ_ONLY);
		comboIsAddDB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(PublicTadpoleDefine.YES_NO yesNo : PublicTadpoleDefine.YES_NO.values()) {
			comboIsAddDB.add(yesNo.name());
		}
		
		Label lblIsSharedDb = new Label(grpSettingDefaultUser, SWT.NONE);
		lblIsSharedDb.setText(Messages.get().IsSharedDB);
		
		comboIsSharedDB = new Combo(grpSettingDefaultUser, SWT.READ_ONLY);
		comboIsSharedDB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(PublicTadpoleDefine.YES_NO yesNo : PublicTadpoleDefine.YES_NO.values()) {
			comboIsSharedDB.add(yesNo.name());
		}
		
		Label lblDefaultAddDb = new Label(grpSettingDefaultUser, SWT.NONE);
		lblDefaultAddDb.setText(Messages.get().DefaultAddDBCount);
		
		textIntLimtCnt = new Text(grpSettingDefaultUser, SWT.BORDER);
		textIntLimtCnt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDefaultUseDay = new Label(grpSettingDefaultUser, SWT.NONE);
		lblDefaultUseDay.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDefaultUseDay.setText(Messages.get().DefaultUseDay);
		
		textDefaultUseDay = new Text(grpSettingDefaultUser, SWT.BORDER);
		textDefaultUseDay.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	
		
//		Composite compositeTail = new Composite(parent, SWT.NONE);
//		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		compositeTail.setLayout(new GridLayout(1, false));
		
		initUI();
		
		AnalyticCaller.track(ID);
	}

	/**
	 * initialize UI
	 */
	private void initUI() {
		comboNewUserPermit.setText(GetAdminPreference.getNewUserPermit());
		
		comboIsAddDB.setText(GetAdminPreference.getIsAddDB());
		comboIsSharedDB.setText(GetAdminPreference.getIsSharedDB());
		textIntLimtCnt.setText(GetAdminPreference.getDefaultAddDBCnt());
		textDefaultUseDay.setText(GetAdminPreference.getServiceDurationDay());
	}
	
	/**
	 * save data
	 * 
	 */
	private void saveData() {
		
		if(!NumberUtils.isDigits(textIntLimtCnt.getText())) {
			MessageDialog.openError(null, Messages.get().Confirm, Messages.get().mustBeNumber);
			textIntLimtCnt.setFocus();
			return;
		} else if(!NumberUtils.isNumber(textDefaultUseDay.getText())) {
			MessageDialog.openError(null, Messages.get().Confirm, Messages.get().mustBeNumber);
			textDefaultUseDay.setFocus();
			return;
		}
		
		if(!MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().AdminSystemSettingEditor_4)) return;
		
		try {
			UserInfoDataDAO userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.NEW_USER_PERMIT, comboNewUserPermit.getText());
			GetAdminPreference.updateAdminData(AdminPreferenceDefine.NEW_USER_PERMIT, userInfoDao);
			
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.IS_ADD_DB, comboIsAddDB.getText());
			GetAdminPreference.updateAdminData(AdminPreferenceDefine.IS_ADD_DB, userInfoDao);
			
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.IS_SHARED_DB, comboIsSharedDB.getText());
			GetAdminPreference.updateAdminData(AdminPreferenceDefine.IS_SHARED_DB, userInfoDao);
			
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.DEFAULT_ADD_DB_CNT, textIntLimtCnt.getText());
			GetAdminPreference.updateAdminData(AdminPreferenceDefine.DEFAULT_ADD_DB_CNT, userInfoDao);
			
			userInfoDao = TadpoleSystem_UserInfoData.updateAdminValue(AdminPreferenceDefine.SERVICE_DURATION_DAY, textDefaultUseDay.getText());
			GetAdminPreference.updateAdminData(AdminPreferenceDefine.SERVICE_DURATION_DAY, userInfoDao);
		} catch (Exception e) {
			logger.error("save exception", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
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
	}
}
