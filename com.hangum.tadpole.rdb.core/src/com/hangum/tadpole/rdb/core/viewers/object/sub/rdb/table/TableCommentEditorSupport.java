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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table;

import java.sql.PreparedStatement;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * table comment editor
 * 
 * @author hangum
 *
 */
public class TableCommentEditorSupport extends EditingSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6292003867430114514L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TableCommentEditorSupport.class);

	private final TableViewer viewer;
	private UserDBDAO userDB;
	private int column;

	/**
	 * 
	 * @param viewer
	 * @param explorer
	 */
	public TableCommentEditorSupport(TableViewer viewer, UserDBDAO userDB, int column) {
		super(viewer);
		
		this.viewer = viewer;
		this.userDB = userDB;
		this.column = column;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		if(column == 1) return new CommentCellEditor(column, viewer);
		else return null;
	}

	@Override
	protected boolean canEdit(Object element) {
		if(column == 1) {
			if (userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT || 
					userDB.getDBDefine() == DBDefine.TIBERO_DEFAULT ||
					userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT ||
					userDB.getDBDefine() == DBDefine.MSSQL_DEFAULT ||
					userDB.getDBDefine() == DBDefine.MSSQL_8_LE_DEFAULT ||
					userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT ||
					userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT
			) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	protected Object getValue(Object element) {
		try {
			TableDAO dao = (TableDAO) element;
			String comment = dao.getComment();
			return comment == null ? "" : comment;
		} catch (Exception e) {
			logger.error("getValue error ", e);
			return "";
		}
	}

	@Override
	protected void setValue(Object element, Object value) {
		String comment = "";
		try {
			TableDAO dao = (TableDAO) element;
			comment = (String) (value == null ? "" : value);

			// 기존 코멘트와 다를때만 db에 반영한다.
			if (!(comment.equals(dao.getComment()))) {
				dao.setComment(comment);
				applyComment(dao);
			}

			viewer.update(element, null);
		} catch (Exception e) {
			logger.error("setValue error ", e);
		}
	}

	/**
	 * DBMS별 처리를 위해 별도의 Class로 분리해야 하지 않을까?
	 * @param dao
	 */
	private void applyComment(TableDAO dao) {
		TadpoleObjectQuery.updateComment(userDB, dao);
	}

}
