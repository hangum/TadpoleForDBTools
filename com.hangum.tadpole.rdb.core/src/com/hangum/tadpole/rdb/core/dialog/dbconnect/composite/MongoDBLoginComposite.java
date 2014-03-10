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
package com.hangum.tadpole.rdb.core.dialog.dbconnect.composite;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.ApplicationArgumentUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.PreConnectionInfoGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.OthersConnectionMongoDBGroup;
import com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.dao.OthersConnectionInfoDAO;
import com.hangum.tadpole.rdb.core.util.DBLocaleUtils;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.session.manager.SessionManager;
import com.hangum.tadpole.sql.template.DBOperationType;

/**
 * oracle login composite
 * 
 * @author hangum
 *
 */
public class MongoDBLoginComposite extends AbstractLoginComposite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8245123047846049939L;
//	private static final Logger logger = Logger.getLogger(MongoDBLoginComposite.class);
	
	protected Text textHost;
	protected Text textUser;
	protected Text textPassword;
	protected Text textDatabase;
	protected Text textPort;
	protected Combo comboLocale;
	
	private Text textReplicaSet;
	
	protected OthersConnectionMongoDBGroup othersConnectionInfo;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MongoDBLoginComposite(Composite parent, int style, List<String> listGroupName, String selGroupName, UserDBDAO userDB) {
		super("Sample MongoDB", DBDefine.MONGODB_DEFAULT, parent, style, listGroupName, selGroupName, userDB);
	}
	
	@Override
	public void crateComposite() {
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		setLayout(gridLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite compositeBody = new Composite(this, SWT.NONE);
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginWidth = 0;
		compositeBody.setLayout(gl_compositeBody);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		preDBInfo = new PreConnectionInfoGroup(compositeBody, SWT.NONE, listGroupName);
		preDBInfo.setText(Messages.MSSQLLoginComposite_preDBInfo_text);
		preDBInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		Group grpConnectionType = new Group(compositeBody, SWT.NONE);
		grpConnectionType.setLayout(new GridLayout(3, false));
		grpConnectionType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		grpConnectionType.setText(Messages.MSSQLLoginComposite_grpConnectionType_text);
		
		Label lblHost = new Label(grpConnectionType, SWT.NONE);
		lblHost.setText(Messages.DBLoginDialog_1);
		
		textHost = new Text(grpConnectionType, SWT.BORDER);
		textHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblNewLabelPort = new Label(grpConnectionType, SWT.NONE);
		lblNewLabelPort.setText(Messages.DBLoginDialog_5);
		
		textPort = new Text(grpConnectionType, SWT.BORDER);
		textPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnPing = new Button(grpConnectionType, SWT.NONE);
		btnPing.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String host 	= StringUtils.trimToEmpty(textHost.getText());
				String port 	= StringUtils.trimToEmpty(textPort.getText());
				
				if("".equals(host) || "".equals(port)) { //$NON-NLS-1$ //$NON-NLS-2$
					MessageDialog.openError(null, Messages.DBLoginDialog_10, Messages.DBLoginDialog_11);
					return;
				}
				
				try {
					if(isPing(host, port)) {
						MessageDialog.openInformation(null, Messages.DBLoginDialog_12, Messages.DBLoginDialog_13);
					} else {
						MessageDialog.openError(null, Messages.DBLoginDialog_14, Messages.DBLoginDialog_15);
					}
				} catch(NumberFormatException nfe) {
					MessageDialog.openError(null, Messages.MySQLLoginComposite_3, Messages.MySQLLoginComposite_4);
				}
			}
		});
		btnPing.setText(Messages.DBLoginDialog_btnPing_text);
		
		Label lblReplicaSet = new Label(grpConnectionType, SWT.NONE);
		lblReplicaSet.setText(Messages.MongoDBLoginComposite_lblReplicaSet_text);
		
		textReplicaSet = new Text(grpConnectionType, SWT.BORDER);
		textReplicaSet.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		new Label(grpConnectionType, SWT.NONE);
		
		Label lblExLocalhostlocalhost = new Label(grpConnectionType, SWT.NONE);
		lblExLocalhostlocalhost.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblExLocalhostlocalhost.setText(Messages.MongoDBLoginComposite_lblExLocalhostlocalhost_text);
		
		Label lblNewLabelDatabase = new Label(grpConnectionType, SWT.NONE);
		lblNewLabelDatabase.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		lblNewLabelDatabase.setText(Messages.DBLoginDialog_4);
		
		textDatabase = new Text(grpConnectionType, SWT.BORDER);
		textDatabase.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));		
		
		Label lblUser = new Label(grpConnectionType, SWT.NONE);
		lblUser.setText(Messages.DBLoginDialog_2);
		
		textUser = new Text(grpConnectionType, SWT.BORDER);
		textUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblPassword = new Label(grpConnectionType, SWT.NONE);
		lblPassword.setText(Messages.DBLoginDialog_3);
		
		textPassword = new Text(grpConnectionType, SWT.BORDER | SWT.PASSWORD);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblLocale = new Label(grpConnectionType, SWT.NONE);
		lblLocale.setText(Messages.MySQLLoginComposite_lblLocale_text);
		
		comboLocale = new Combo(grpConnectionType, SWT.READ_ONLY);
		comboLocale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			
		for(String val : DBLocaleUtils.getMySQLList()) comboLocale.add(val);
		comboLocale.setVisibleItemCount(12);
		comboLocale.select(0);

		othersConnectionInfo = new OthersConnectionMongoDBGroup(this, SWT.NONE, getSelectDB());
		othersConnectionInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		init();
	}
	
	@Override
	public void init() {

		if(oldUserDB != null) {
			
			selGroupName = oldUserDB.getGroup_name();
			preDBInfo.setTextDisplayName(oldUserDB.getDisplay_name());
			preDBInfo.getComboOperationType().setText( DBOperationType.valueOf(oldUserDB.getOperation_type()).getTypeName() );
			
			textHost.setText(oldUserDB.getHost());
			textPort.setText(oldUserDB.getPort());
			textDatabase.setText(oldUserDB.getDb());
			textUser.setText(oldUserDB.getUsers());
			textPassword.setText(oldUserDB.getPasswd());
			
			textReplicaSet.setText(oldUserDB.getExt1()==null?"":oldUserDB.getExt1());
		} else if(ApplicationArgumentUtils.isTestMode()) {

			preDBInfo.setTextDisplayName(getDisplayName()); //$NON-NLS-1$
			
			textHost.setText("127.0.0.1"); //$NON-NLS-1$
			textPort.setText("27017");			 //$NON-NLS-1$
			textDatabase.setText("test"); //$NON-NLS-1$
			textUser.setText(""); //$NON-NLS-1$
			textPassword.setText(""); //$NON-NLS-1$
			
		} else {
			textPort.setText("27017");			 //$NON-NLS-1$
		}
		
		Combo comboGroup = preDBInfo.getComboGroup();
		if(comboGroup.getItems().length == 0) {
			comboGroup.add(strOtherGroupName);
			comboGroup.select(0);
		} else {
			if("".equals(selGroupName)) { //$NON-NLS-1$
				comboGroup.setText(strOtherGroupName);
			} else {
				for(int i=0; i<comboGroup.getItemCount(); i++) {
					if(comboGroup.getItem(i).equals(selGroupName)) comboGroup.select(i);
				}
			}
		}
		
		//
		othersConnectionInfo.callBackUIInit(textHost.getText(), textPort.getText());
		
		textHost.setFocus();
	}
	
	@Override
	public boolean isValidateInput() {
		if(!checkTextCtl(preDBInfo.getComboGroup(), "Group")) return false; //$NON-NLS-1$
		if(!checkTextCtl(preDBInfo.getTextDisplayName(), "Display Name")) return false; //$NON-NLS-1$
		
		if(!checkTextCtl(textHost, "Host")) return false; //$NON-NLS-1$
		if(!checkTextCtl(textPort, "Port")) return false; //$NON-NLS-1$
//		if(!checkTextCtl(textDatabase, "Database")) return false; //$NON-NLS-1$		
		
		return true;
	}

	@Override
	public boolean makeUserDBDao() {
		if(!isValidateInput()) return false;
		
		final String dbUrl = String.format(
								getSelectDB().getDB_URL_INFO(), 
								StringUtils.trimToEmpty(textHost.getText()), 
								StringUtils.trimToEmpty(textPort.getText()), 
								StringUtils.trimToEmpty(textDatabase.getText())
							);

		userDB = new UserDBDAO();
		userDB.setDbms_types(getSelectDB().getDBToString());
		userDB.setUrl(dbUrl);
		userDB.setDb(textDatabase.getText());
		userDB.setGroup_seq(SessionManager.getGroupSeq());
		userDB.setGroup_name(StringUtils.trimToEmpty(preDBInfo.getComboGroup().getText()));
		userDB.setDisplay_name(StringUtils.trimToEmpty(preDBInfo.getTextDisplayName().getText()));
		userDB.setOperation_type( DBOperationType.getNameToType(preDBInfo.getComboOperationType().getText()).toString() );
		userDB.setHost(StringUtils.trimToEmpty(textHost.getText()));
		userDB.setPort(StringUtils.trimToEmpty(textPort.getText()));
		userDB.setUsers(StringUtils.trimToEmpty(textUser.getText()));
		userDB.setPasswd(StringUtils.trimToEmpty(textPassword.getText()));
		userDB.setLocale(StringUtils.trimToEmpty(comboLocale.getText()));
		userDB.setExt1(StringUtils.trimToEmpty(textReplicaSet.getText()));
		
		// others connection 정보를 입력합니다.
		OthersConnectionInfoDAO otherConnectionDAO =  othersConnectionInfo.getOthersConnectionInfo();
		userDB.setIs_readOnlyConnect(otherConnectionDAO.isReadOnlyConnection()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		userDB.setIs_autocommit(otherConnectionDAO.isAutoCommit()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		userDB.setIs_showtables(otherConnectionDAO.isShowTables()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		
		userDB.setIs_table_filter(otherConnectionDAO.isTableFilter()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		userDB.setTable_filter_include(otherConnectionDAO.getStrTableFilterInclude());
		userDB.setTable_filter_exclude(otherConnectionDAO.getStrTableFilterExclude());
		
		userDB.setIs_profile(otherConnectionDAO.isProfiling()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		userDB.setQuestion_dml(otherConnectionDAO.isDMLStatement()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		
		userDB.setIs_external_browser(otherConnectionDAO.isExterBrowser()?PublicTadpoleDefine.YES_NO.YES.toString():PublicTadpoleDefine.YES_NO.NO.toString());
		userDB.setListExternalBrowserdao(otherConnectionDAO.getListExterBroswer());
		
		return true;
	}

}
