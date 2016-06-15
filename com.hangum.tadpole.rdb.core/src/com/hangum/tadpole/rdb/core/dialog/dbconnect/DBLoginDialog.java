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

import org.apache.commons.lang.ClassUtils;
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

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.composite.AbstractLoginComposite;
import com.hangum.tadpole.rdb.core.dialog.driver.JDBCDriverManageDialog;
import com.hangum.tadpole.session.manager.SessionManager;
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
	protected List<String> listGroupName;
	/** 초기 선택한 그룹 */
	private String selGroupName;
	
	private Combo comboDBList;
	private Composite compositeBody;

	private AbstractLoginComposite loginComposite;

	// 결과셋으로 사용할 logindb
	private UserDBDAO retuserDb;
	
	public DBLoginDialog(Shell paShell, String selGroupName) {
		super(paShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		this.selGroupName = selGroupName;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().DBLoginDialog_9);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
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
		lblNewLabel.setText(Messages.get().Database);

		comboDBList = new Combo(compositeHead, SWT.DROP_DOWN | SWT.READ_ONLY);
		comboDBList.setForeground(SWTResourceManager.getColor(0, 0, 204));
		comboDBList.setVisibleItemCount(13);
		comboDBList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				initDBWidget();
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
				logger.error("find default db", e);
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
			listGroupName = TadpoleSystem_UserDBQuery.getUserGroupName();
			
		} catch (Exception e1) {
			logger.error("get group info", e1); //$NON-NLS-1$
		}
		
		createDBWidget(null);

		// history .....................................
		sashFormContainer.setWeights(new int[] {1});

//		comboDBList.setFocus();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		return container;
	}
	
	/**
	 * db widget을 설정한다.
	 */
	private void initDBWidget() {
		if(loginComposite != null) loginComposite.dispose();
		
		if(!ApplicationArgumentUtils.isOnlineServer()) {
			DBDefine dbDefine = (DBDefine)comboDBList.getData(comboDBList.getText());
			if(dbDefine == DBDefine.ALTIBASE_DEFAULT |
					dbDefine == DBDefine.CUBRID_DEFAULT |
					dbDefine == DBDefine.MYSQL_DEFAULT |
					dbDefine == DBDefine.MARIADB_DEFAULT |
					dbDefine == DBDefine.MSSQL_DEFAULT |
					dbDefine == DBDefine.ORACLE_DEFAULT |
					dbDefine == DBDefine.SQLite_DEFAULT |
					dbDefine == DBDefine.TIBERO_DEFAULT |
					dbDefine == DBDefine.POSTGRE_DEFAULT
			) {
				try {
					ClassUtils.getClass(dbDefine.getDriverClass());
				} catch (ClassNotFoundException e) {
		
					if(MessageDialog.openConfirm(null, Messages.get().DriverNotFound, Messages.get().DriverNotFoundMSG)) {
						JDBCDriverManageDialog dialog = new JDBCDriverManageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
						if(Dialog.OK ==  dialog.open()) {
							if(dialog.isUploaded()) {
								MessageDialog.openInformation(null, Messages.get().Information, Messages.get().jdbcdriver);
							}
						}		
					}
				}
			}
		}

		createDBWidget(null);
		compositeBody.layout();
		container.layout();
		
		// google analytic
		AnalyticCaller.track("DBLoginDialog"); //$NON-NLS-1$
	}
	
	/**
	 * db widget을 생성한다.
	 */
	private void createDBWidget(UserDBDAO userDB) {
		DBDefine dbDefine = (DBDefine) comboDBList.getData(comboDBList.getText());
		loginComposite = DBConnectionUtils.getDBConnection(dbDefine, compositeBody, listGroupName, selGroupName, userDB);
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
		// 사용자가데이터베이스를 추가할 수 있는 한계까지.
		int limitDBCount = SessionManager.getLimitAddDBCnt();
		try {
			if(limitDBCount <= TadpoleSystem_UserDBQuery.getCreateUserDB().size()) {
				MessageDialog.openInformation(null, Messages.get().Information, Messages.get().DBLoginDialog_AddDBOverMsg);
				return false;
			}
		} catch(Exception e) {
			logger.error("count userr db list", e);
		}
		
		if (loginComposite.saveDBData()) {
			this.retuserDb = loginComposite.getDBDTO();
			if(PublicTadpoleDefine.YES_NO.YES.name().equals(this.retuserDb.getIs_lock())) {
				SessionManager.setUnlokDB(retuserDb);
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		if(TEST_CONNECTION_ID == buttonId) {
			if(loginComposite.testConnection(true)) {
				MessageDialog.openInformation(null, Messages.get().Confirm, Messages.get().DBLoginDialog_42); //$NON-NLS-1$
			}
		} else if(ADD_NEW_CONNECTION_ID == buttonId) {
			if(addDB()) {
				PlatformUI.getPreferenceStore().setValue(PublicTadpoleDefine.ADD_DB, ""+retuserDb.getSeq() + ":" + System.currentTimeMillis()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				
				MessageDialog.openInformation(null, Messages.get().Confirm, Messages.get().DBLoginDialog_47); //$NON-NLS-1$
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
		createButton(parent, TEST_CONNECTION_ID, Messages.get().DBLoginDialog_43, false);
		
		createButton(parent, ADD_NEW_CONNECTION_ID, Messages.get().DBLoginDialog_45, false);
		createButton(parent, IDialogConstants.OK_ID, Messages.get().DBLoginDialog_44, true);
		
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Close, false);
	}
	/**
	 * group name
	 * 
	 * @return
	 */
	public List<String> getGroupName() {
		return listGroupName;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(530, 500);
	}
}
