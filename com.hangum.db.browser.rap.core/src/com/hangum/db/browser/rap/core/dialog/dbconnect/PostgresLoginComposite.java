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
 * postgres login composite
 * 
 * @author hangum
 *
 */
public class PostgresLoginComposite extends MySQLLoginComposite {

	private static final Logger logger = Logger.getLogger(PostgresLoginComposite.class);
	private static final DBDefine DB_DEFINE = DBDefine.POSTGRE_DEFAULT;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PostgresLoginComposite(Composite parent, int style) {
		super(DB_DEFINE, parent, style);
		setText(DB_DEFINE.getDBToString());
	}
	
	@Override
	public void init() {
		
		if(ApplicationArgumentUtils.isTestMode()) {
			textHost.setText("127.0.0.1");
			textUser.setText("tadpole");
			textPassword.setText("tadpole");
			textDatabase.setText("test");
			textPort.setText("5432");
			
			textDisplayName.setText("PostgreSQL 9.1");
		}
	}
	
	@Override
	public boolean connection() {
		if(!isValidate()) return false;
		
		final String dbUrl = String.format(
				DB_DEFINE.getDB_URL_INFO(), 
				textHost.getText(), textPort.getText(), textDatabase.getText());

		userDB = new UserDBDAO();
		userDB.setType(DB_DEFINE.getDBToString());
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
		if( !managerView.isAdd(DB_DEFINE, userDB) ) {
			MessageDialog.openError(null, Messages.DBLoginDialog_23, Messages.DBLoginDialog_24);
			
			return false;
		}

		// db가 정상적인지 채크해본다 
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			List showTables = sqlClient.queryForList("tableList", textDatabase.getText());
			
		} catch (Exception e) {
			logger.error("PostgreSQL DB Connection", e);
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getShell(), "Error", "PostgreSQL Connection Exception", errStatus);
			
			return false;
		}
		
		// preference에 save합니다.
		if(btnSavePreference.getSelection())
			try {
				TadpoleSystem_UserDBQuery.newUserDB(userDB);
			} catch (Exception e) {
				logger.error("PostgreSQL db preference save", e);
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getShell(), "Error", "PostgreSQL Connection Exception", errStatus); //$NON-NLS-1$
			}
		
		return true;
	}

}
