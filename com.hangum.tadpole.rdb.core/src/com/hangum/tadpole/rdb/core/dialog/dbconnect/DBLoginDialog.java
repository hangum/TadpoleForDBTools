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
package com.hangum.tadpole.rdb.core.dialog.dbconnect;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.AbstractLoginComposite;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserDBQuery;
import com.swtdesigner.SWTResourceManager;

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
	
	/** test connection button id */
	public static final int TEST_CONNECTION_ID = IDialogConstants.CLIENT_ID + 1;
	
	/** add new connection button id */
	public static final int ADD_NEW_CONNECTION_ID = TEST_CONNECTION_ID + 1;
	
	/** main composite */
	private Composite container;
	
	/** group name */
	protected List<String> groupName;
	/** 초기 선택한 그룹 */
	private String selGroupName;
	
	private Combo comboDBList;
	private Composite compositeBody;

	private AbstractLoginComposite loginComposite;

	// 결과셋으로 사용할 logindb
	private UserDBDAO retuserDb;
	
	public DBLoginDialog(Shell paShell, String selGroupName) {
		super(paShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
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
		container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 3;
		gridLayout.horizontalSpacing = 3;
		gridLayout.marginHeight = 3;
		gridLayout.marginWidth = 3;
		
		SashForm sashFormContainer = new SashForm(container, SWT.VERTICAL);
		sashFormContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite compositeHead = new Composite(sashFormContainer, SWT.NONE);
		GridLayout gl_compositeHead = new GridLayout(2, false);
		gl_compositeHead.verticalSpacing = 3;
		gl_compositeHead.horizontalSpacing = 3;
		gl_compositeHead.marginHeight = 3;
		gl_compositeHead.marginWidth = 2;
		compositeHead.setLayout(gl_compositeHead);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		Label lblNewLabel = new Label(compositeHead, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText(Messages.DBLoginDialog_35);
		lblNewLabel.setFont(SWTResourceManager.getBoldFont(PlatformUI.getWorkbench().getDisplay().getSystemFont()));

		comboDBList = new Combo(compositeHead, SWT.DROP_DOWN | SWT.READ_ONLY);
		comboDBList.setBackground(SWTResourceManager.getColor(255, 250, 205));
		comboDBList.setVisibleItemCount(11);
		comboDBList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				initDBWidget(null);
			}
		});
		comboDBList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		// 초기데이터 추가
		for (DBDefine dbDefine : DBDefine.userDBValues()) {
			comboDBList.add(dbDefine.getDBToString());
			comboDBList.setData(dbDefine.getDBToString(), dbDefine);
		}
		
		// option에 default db가 존재하면..
		if(ApplicationArgumentUtils.isDefaultDB()) {
			try {
				String strDefaultDB = ApplicationArgumentUtils.getDefaultDB();
				comboDBList.setText(strDefaultDB);
				
				// 초기 값이 잘못되어 ui가 잘못 생성되는것을 방지하기위한 코드.
				if(-1 == comboDBList.getSelectionIndex()) comboDBList.select(0);;
			} catch(Exception e) {
				logger.error(Messages.DBLoginDialog_38, e);
			}
		} else {
			comboDBList.select(0);
		}
				
		// combo에서 선택된 디비의 콤포짖
		compositeBody = new Composite(compositeHead, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);
		
		// db groupData 
		try {
			groupName = TadpoleSystem_UserDBQuery.getUserGroup(SessionManager.getGroupSeqs());
		} catch (Exception e1) {
			logger.error("get group info", e1); //$NON-NLS-1$
		}
		
		createDBWidget(null);

		// history .....................................
		sashFormContainer.setWeights(new int[] {1});

		
		comboDBList.setFocus();
		
		return container;
	}
	
	/**
	 * db widget을 설정한다.
	 * @param userDB
	 */
	private void initDBWidget(UserDBDAO userDB) {
		if (loginComposite != null)loginComposite.dispose();

		createDBWidget(userDB);
		compositeBody.layout();
		container.layout();
		
		// google analytic
		AnalyticCaller.track("DBLoginDialog");
	}
	
	/**
	 * db widget을 생성한다.
	 */
	private void createDBWidget(UserDBDAO userDB) {
		
		DBDefine dbDefine = (DBDefine) comboDBList.getData(comboDBList.getText());
		loginComposite = DBConnectionUtils.getDBConnection(dbDefine, compositeBody, groupName, selGroupName, userDB);
	}

	@Override
	protected void okPressed() {
		if(!addDB()) return;
		
		super.okPressed();
	}
	
	/**
	 * add db
	 */
	private boolean addDB() {
		if (loginComposite.connection()) {
			this.retuserDb = loginComposite.getDBDTO();	
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		if(TEST_CONNECTION_ID == buttonId) {
			if(loginComposite.testConnection(true)) {
				MessageDialog.openInformation(null, "Confirm", Messages.DBLoginDialog_42); //$NON-NLS-1$
			}
		} else if(ADD_NEW_CONNECTION_ID == buttonId) {
			if(addDB()) {
				PlatformUI.getPreferenceStore().setValue(PublicTadpoleDefine.ADD_DB, ""+retuserDb.getSeq() + ":" + System.currentTimeMillis()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				
				MessageDialog.openInformation(null, "Confirm", Messages.DBLoginDialog_47); //$NON-NLS-1$
			}
		}
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
		createButton(parent, TEST_CONNECTION_ID, Messages.DBLoginDialog_43, false);
		
		createButton(parent, ADD_NEW_CONNECTION_ID, Messages.DBLoginDialog_45, false);
		createButton(parent, IDialogConstants.OK_ID, Messages.DBLoginDialog_44, true);
		
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.DBLoginDialog_7, false);
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
		return new Point(570, 540);
	}
}
