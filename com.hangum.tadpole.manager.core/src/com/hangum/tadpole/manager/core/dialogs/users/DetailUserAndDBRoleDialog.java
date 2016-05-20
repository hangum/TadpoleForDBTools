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
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.system.TadpoleUserDbRoleDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserRole;
import com.hangum.tadpole.manager.core.Messages;

/**
 * Detail user role dialog
 * 
 * @author hangum
 *
 */
public class DetailUserAndDBRoleDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(DetailUserAndDBRoleDialog.class);
	
	private int BTN_ADD = IDialogConstants.CLIENT_ID + 1;
	
	private TadpoleUserDbRoleDAO userDBRole;
	
	private Text textEMail;
	
	private Combo comboRoleType;
	private DateTime dateTimeStart;
	private DateTime dateTimeEndDay;
	private DateTime dateTimeEndTime;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DetailUserAndDBRoleDialog(Shell parentShell, TadpoleUserDbRoleDAO userDBRole) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.userDBRole = userDBRole;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().DetailUserAndDBRoleDialog_0);
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
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite = new Composite(compositeBody, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(5, false));
		
		Label lblEmail = new Label(composite, SWT.NONE);
		lblEmail.setText(Messages.get().Email);
		
		textEMail = new Text(composite, SWT.BORDER);
		textEMail.setEditable(false);
		textEMail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
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

		return container;
	}

	/**
	 * initialize UI
	 */
	private void initUI() {
		textEMail.setText(userDBRole.getEmail());
		comboRoleType.setText(userDBRole.getRole_id());
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(userDBRole.getTerms_of_use_starttime().getTime());
		
		dateTimeStart.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		
		calendar.setTimeInMillis(userDBRole.getTerms_of_use_endtime().getTime());
		dateTimeEndDay.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		dateTimeEndTime.setTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 59);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		if("NONE".equals(comboRoleType.getText())) { //$NON-NLS-1$
			MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().DetailUserAndDBRoleDialog_8);
			comboRoleType.setFocus();
			return;
		}
		
		// 사용자가 해당 디비에 추가 될수 있는지 검사합니다. 
		try {
			if(!MessageDialog.openConfirm(getShell(), Messages.get().Confirm, Messages.get().FindUserDialog_4)) return;
			
			Calendar calStart = Calendar.getInstance();
			calStart.set(dateTimeStart.getYear(), dateTimeStart.getMonth(), dateTimeStart.getDay(), 0, 0, 0);

			Calendar calEnd = Calendar.getInstance();
			calEnd.set(dateTimeEndDay.getYear(), dateTimeEndDay.getMonth(), dateTimeEndDay.getDay(), dateTimeEndTime.getHours(), dateTimeEndTime.getMinutes(), 00);
			
			
			userDBRole.setRole_id(comboRoleType.getText());
			userDBRole.setTerms_of_use_starttime(new Timestamp(calStart.getTimeInMillis()));
			userDBRole.setTerms_of_use_endtime(new Timestamp(calEnd.getTimeInMillis()));
			TadpoleSystem_UserRole.updateTadpoleUserDBRole(userDBRole);
			
			MessageDialog.openInformation(getShell(), Messages.get().Confirm, Messages.get().DetailUserAndDBRoleDialog_11);
			
		} catch (Exception e) {
			logger.error("Is DB add role error.", e); //$NON-NLS-1$
			MessageDialog.openError(getShell(), Messages.get().Error, Messages.get().DetailUserAndDBRoleDialog_14 + e.getMessage());
			
			return;
		}
		
		super.okPressed();
	}
	

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Update, false);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Close, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 200);
	}

	/**
	 * @return the userDBRole
	 */
	public TadpoleUserDbRoleDAO getUserDBRole() {
		return userDBRole;
	}
}
