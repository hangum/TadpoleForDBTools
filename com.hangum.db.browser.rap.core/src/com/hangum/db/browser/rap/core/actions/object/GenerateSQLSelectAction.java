package com.hangum.db.browser.rap.core.actions.object;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.db.browser.rap.core.Activator;
import com.hangum.db.browser.rap.core.Messages;
import com.hangum.db.browser.rap.core.actions.connections.QueryEditorAction;
import com.hangum.db.browser.rap.core.viewers.object.ExplorerViewer;
import com.hangum.db.commons.sql.TadpoleSQLManager;
import com.hangum.db.dao.mysql.TableColumnDAO;
import com.hangum.db.dao.mysql.TableDAO;
import com.hangum.db.define.Define;
import com.hangum.db.define.Define.DB_ACTION;
import com.hangum.db.exception.dialog.ExceptionDetailsErrorDialog;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * generate sql statement     
 * 
 * @author hangumNote
 *
 */
public class GenerateSQLSelectAction extends AbstractObjectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateSQLSelectAction.class);
	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.GenerateSQLSelectAction"; //$NON-NLS-1$
	
	public GenerateSQLSelectAction(IWorkbenchWindow window, DB_ACTION actionType, String title) {
		super(window, actionType);
	
		setId(ID + actionType.toString());
		setText(Messages.GenerateSQLSelectAction_1 + title);
		
		window.getSelectionService().addSelectionListener(this);
	}
	
	@Override
	public void run() {
		StringBuffer sbSQL = new StringBuffer();
		try {
			TableDAO tableDAO = (TableDAO)sel.getFirstElement();
			
			Map<String, String> parameter = new HashMap<String, String>();
			parameter.put("db", userDB.getDatabase());
			parameter.put("table", tableDAO.getName());
			
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			List<TableColumnDAO> showTableColumns = sqlClient.queryForList("tableColumnList", parameter); //$NON-NLS-1$
			
			sbSQL.append(" SELECT "); //$NON-NLS-1$
			for (int i=0; i<showTableColumns.size(); i++) {
				TableColumnDAO dao = showTableColumns.get(i);
				sbSQL.append(dao.getField());
				
				// 마지막 컬럼에는 ,를 않넣어주어야하니까 
				if(i < (showTableColumns.size()-1)) sbSQL.append(", ");  //$NON-NLS-1$
				else sbSQL.append(" "); //$NON-NLS-1$
			}
			sbSQL.append("\r\n FROM " + tableDAO.getName() + Define.SQL_DILIMITER); //$NON-NLS-1$ //$NON-NLS-2$
			
			//
			QueryEditorAction qea = new QueryEditorAction();
			qea.run(userDB, sbSQL.toString());
		} catch(Exception e) {
			logger.error(Messages.GenerateSQLSelectAction_8, e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.GenerateSQLSelectAction_0, errStatus); //$NON-NLS-1$
		}
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(ExplorerViewer.ID.equals( part.getSite().getId() )) {
			
			if(userDB != null) {
				if(selection instanceof IStructuredSelection && !selection.isEmpty()) {
										
					this.sel = (IStructuredSelection)selection;
					setEnabled(this.sel.size() > 0);
				} else setEnabled(false);
			}
			else setEnabled(false);
		}
	}
	

}
