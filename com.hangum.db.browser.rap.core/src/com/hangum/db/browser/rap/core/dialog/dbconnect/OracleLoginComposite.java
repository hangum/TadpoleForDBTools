package com.hangum.db.browser.rap.core.dialog.dbconnect;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.browser.rap.core.Messages;
import com.hangum.db.browser.rap.core.viewers.connections.ManagerViewer;
import com.hangum.db.commons.sql.TadpoleSQLManager;
import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.db.system.TadpoleSystem_UserDBQuery;
import com.hangum.db.util.ApplicationArgumentUtils;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * oracle login composite
 * 
 * @author hangum
 *
 */
public class OracleLoginComposite extends MySQLLoginComposite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8245123047846049939L;
	private static final Logger logger = Logger.getLogger(OracleLoginComposite.class);
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public OracleLoginComposite(Composite parent, int style) {
		super(DBDefine.ORACLE_DEFAULT, parent, style);
		setText(DBDefine.ORACLE_DEFAULT.getDBToString());
	}
	
	@Override
	public void init() {
		
		if(ApplicationArgumentUtils.isTestMode()) {
			textHost.setText(Messages.OracleLoginComposite_0);
			textUser.setText(Messages.OracleLoginComposite_1);
			textPassword.setText(Messages.OracleLoginComposite_2);
			textDatabase.setText(Messages.OracleLoginComposite_3);
			textPort.setText(Messages.OracleLoginComposite_4);
			
			textDisplayName.setText(Messages.OracleLoginComposite_5);
		}
	}
	
	@Override
	public boolean connection() {
		if(!isValidate()) return false;
		
		final String dbUrl = String.format(
				DBDefine.ORACLE_DEFAULT.getDB_URL_INFO(), 
				textHost.getText(), textPort.getText(), textDatabase.getText());

		userDB = new UserDBDAO();
		userDB.setType(DBDefine.ORACLE_DEFAULT.getDBToString());
		userDB.setUrl(dbUrl);
		userDB.setDatabase(textDatabase.getText());
		userDB.setDisplay_name(textDisplayName.getText());
		userDB.setHost(textHost.getText());
		userDB.setPasswd(textPassword.getText());
		userDB.setPort(textPort.getText());
		userDB.setLocale(comboLocale.getText().trim());
		userDB.setUser(textUser.getText());
		
		// 이미 연결한 것인지 검사한다.
		final ManagerViewer managerView = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
		if( !managerView.isAdd(DBDefine.ORACLE_DEFAULT, userDB) ) {
			MessageDialog.openError(null, Messages.DBLoginDialog_23, Messages.DBLoginDialog_24);
			
			return false;
		}

		// db가 정상적인지 채크해본다 
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			List showTables = sqlClient.queryForList("tableList", textDatabase.getText());
			
		} catch (Exception e) {
			logger.error(Messages.OracleLoginComposite_7, e);
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.OracleLoginComposite_10, errStatus); //$NON-NLS-1$
			
			return false;
		}
		
		// preference에 save합니다.
		if(btnSavePreference.getSelection())
			try {
				TadpoleSystem_UserDBQuery.newUserDB(userDB);
			} catch (Exception e) {
				logger.error("Oracle db info save", e);
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), "Error", Messages.OracleLoginComposite_11, errStatus); //$NON-NLS-1$
			}
		
		return true;
	}

}
