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
package com.hangum.tadpole.rdb.core.dialog.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.msg.TDBErroDialog;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TableColumnObjectQuery;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;

/**
 * Alter table 
 * 
 * @author hangum
 *
 */
public class AbstractAlterDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(AbstractAlterDialog.class);
	protected UserDBDAO userDB;
	protected TableDAO tableDao;
	protected Text textTableName;
	protected Text textComment;
	
	protected List<AlterTableMetaDataDAO> listAlterTableColumns = new ArrayList<AlterTableMetaDataDAO>();
	protected TableViewer tableViewer;
	
	protected AlterTableExecutor executor;


	protected AbstractAlterDialog(Shell parentShell, final UserDBDAO userDB) {
		super(parentShell);
		setShellStyle(SWT.SHELL_TRIM | SWT.BORDER | SWT.MAX | SWT.RESIZE | SWT.TITLE);
		this.userDB = userDB;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Alter Table");
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}
	
	/**
	 * change table name
	 * @param tableDao2
	 * @param strNewname
	 */
	protected boolean changeTableName(TableDAO tableDAO, String strNewname) {
		if(!tableDao.getName().equals(strNewname)) {
			if(MessageDialog.openConfirm(null, "Confirm", "테이블 이름을 수정하시겠습니까?")) {
				try {
					RequestResultDAO req = TadpoleObjectQuery.renameTable(userDB, tableDAO, strNewname);
					boolean isChanged =  req.isDataChanged();

					tableDAO.setTab_name(strNewname);
					tableDAO.setSysName(SQLUtil.makeIdentifierName(userDB, strNewname));
					return true;
				} catch (Exception e) {
					logger.error("change table name", e);
					
					TDBErroDialog errDialog = new TDBErroDialog(null, Messages.get().ObjectDeleteAction_25, "테이블 이름을 수정하는 중에 "+ e.getMessage());
					errDialog.open();
				}
			}
		}
		
		return false;
	}

	/**
	 * change table comment
	 * 
	 * @param tableDAO
	 * @param strNewComment
	 * @return
	 */
	protected boolean changeTableComment(TableDAO tableDAO, String strNewComment) {
		if(!tableDao.getComment().equals(strNewComment)) {
			if(MessageDialog.openConfirm(null, "Confirm", "테이블 코멘트를 수정하시겠습니까?")) {
				try {
					tableDAO.setComment(strNewComment);
					
					TadpoleObjectQuery.updateComment(userDB, tableDAO);
					return true;
				} catch (Exception e) {
					logger.error("change table name", e);
					
					TDBErroDialog errDialog = new TDBErroDialog(null, Messages.get().ObjectDeleteAction_25, "테이블 코멘트를 수정하는 중에 "+ e.getMessage());
					errDialog.open();
				}
			}
		}
		
		return false;
	}
	
	/**
	 * delete column
	 * 
	 * @param columnDao
	 * @return
	 */
	protected boolean deleteColumn(AlterTableMetaDataDAO columnDao) {
		if(!MessageDialog.openConfirm(null, "Confirm", columnDao.getColumnName() + " 을 삭제하시겠습니까?")) return false;
		
		try {
			TableColumnObjectQuery.deleteColumn(userDB, tableDao.getSysName(), columnDao.getColumnName());
			return true;
		} catch (Exception e) {
			logger.error("Column delete", e);
			TDBErroDialog errDialog = new TDBErroDialog(null, Messages.get().ObjectDeleteAction_25, "Column 을 삭제하는 중에 "+ e.getMessage());
			errDialog.open();
		}
		
		return false;
	}
	
	/**
	 * add column
	 * 
	 * @param columnDao
	 * @return
	 */
	protected boolean addColumn(AlterTableMetaDataDAO columnDao) {
		try {
			TableColumnObjectQuery.addColumn(userDB, tableDao.getSysName(), columnDao);
			return true;
		} catch (Exception e) {
			logger.error("Column add", e);

			TDBErroDialog errDialog = new TDBErroDialog(null, Messages.get().ObjectDeleteAction_25, "Column 을 추가하는 중에 "+ e.getMessage());
			errDialog.open();
		}
		
		return false;
	}
}

/**
 * AlterTableInputValidator validator
 * @author hangum
 *
 */
class AlterTableInputValidator implements IInputValidator {
	public AlterTableInputValidator() {
		super();
	}
	
	@Override
	public String isValid(String newText) {
		int len = newText.length();
		if(len < 2) return "2자 이상입력하세요.";
				
		return null;
	}
}