package com.hangum.db.browser.rap.core.actions.object;

import org.apache.log4j.Logger;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.browser.rap.core.Messages;
import com.hangum.db.browser.rap.core.actions.connections.QueryEditorAction;
import com.hangum.db.commons.sql.TadpoleSQLManager;
import com.hangum.db.dao.mysql.TableColumnDAO;
import com.hangum.db.define.Define;
import com.hangum.db.define.Define.DB_ACTION;
import com.hangum.db.exception.dialog.ExceptionDetailsErrorDialog;
import com.ibatis.sqlmap.client.SqlMapClient;

public class GenerateSQLUpdateAction extends GenerateSQLSelectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateSQLUpdateAction.class);
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.GenerateSQLUpdateAction"; //$NON-NLS-1$

	public GenerateSQLUpdateAction(IWorkbenchWindow window, DB_ACTION actionType, String title) {
		super(window, actionType, title);
	}
	
	@Override
	public void run() {
		StringBuffer sbSQL = new StringBuffer();
		try {
			Object strTBName = sel.getFirstElement();
			
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			List<TableColumnDAO> showTableColumns = sqlClient.queryForList("tableColumnList", strTBName); //$NON-NLS-1$
			
			sbSQL.append(" UPDATE " + strTBName + " \r\n SET "); //$NON-NLS-1$ //$NON-NLS-2$
			for (int i=0; i<showTableColumns.size(); i++) {
				TableColumnDAO dao = showTableColumns.get(i);
				sbSQL.append(dao.getField());
				
				// 마지막 컬럼에는 ,를 않넣어주어야하니까 
				if(i < (showTableColumns.size()-1)) sbSQL.append("= ?,  ");  //$NON-NLS-1$
				else sbSQL.append("=? "); //$NON-NLS-1$
			}

			sbSQL.append("\r\n WHERE \r\n "); //$NON-NLS-1$
			int cnt = 0;
			for (int i=0; i<showTableColumns.size(); i++) {
				TableColumnDAO dao = showTableColumns.get(i);
				if(Define.isKEY(dao.getKey())) {
					if(cnt == 0) sbSQL.append("\t" + dao.getField() + " = ? \r\n"); //$NON-NLS-1$ //$NON-NLS-2$
					else sbSQL.append("\tAND " + dao.getField() + " = ? "); //$NON-NLS-1$ //$NON-NLS-2$
					cnt++;
				}				
			}
			sbSQL.append(" ; "); //$NON-NLS-1$
			
			//
			QueryEditorAction qea = new QueryEditorAction();
			qea.run(userDB, sbSQL.toString());
		} catch(Exception e) {
			logger.error("Generate SQL Statement Error", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.GenerateSQLUpdateAction_13, errStatus); //$NON-NLS-1$
		}
	}

}
