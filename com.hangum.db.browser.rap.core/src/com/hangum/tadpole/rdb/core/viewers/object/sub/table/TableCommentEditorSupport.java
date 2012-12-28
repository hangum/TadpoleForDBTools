/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Jeong jaehong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object.sub.table;

import java.sql.PreparedStatement;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
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
		if(column == 1) return new TextCellEditor(viewer.getTable());
		else return null;
	}

	@Override
	protected boolean canEdit(Object element) {
		// TODO : ORACLE과 MSSQL일때만 처리한다.
		// 다른 DBMS들은 코멘트를 저장할 수 있는 테이블을 직접 만들어 주면 안될까?
		// Tadpole_Props 이런식으로...^^;
		
		if(column == 1) {
//			userDB = explorer.getUserDB();
			if (DBDefine.getDBDefine(userDB.getTypes()) == DBDefine.ORACLE_DEFAULT || DBDefine.getDBDefine(userDB.getTypes()) == DBDefine.MSSQL_DEFAULT) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
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
				ApplyComment(dao);
			}

			viewer.update(element, null);
		} catch (Exception e) {
			logger.error("setValue error ", e);
		}
		viewer.update(element, null);
	}

	private void ApplyComment(TableDAO dao) {
		// TODO : DBMS별 처리를 위해 별도의 Class로 분리해야 하지 않을까? 

		java.sql.Connection javaConn = null;
		PreparedStatement stmt = null;
		try {

			logger.debug("userDB is " + userDB.toString());

			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);

			javaConn = client.getDataSource().getConnection();

			StringBuffer query = new StringBuffer();

			if (DBDefine.getDBDefine(userDB.getTypes()) == DBDefine.ORACLE_DEFAULT) {
				query.append(" COMMENT ON TABLE ").append(dao.getName()).append(" IS '").append(dao.getComment()).append("'");

				stmt = javaConn.prepareStatement(query.toString());
				stmt.executeQuery();

			} else if (DBDefine.getDBDefine(userDB.getTypes()) == DBDefine.MSSQL_DEFAULT) {
				query.append(" exec sp_dropextendedproperty 'Caption' ").append(", 'user' ,").append(userDB.getUsers()).append(" ,'table' ").append(" , '").append(dao.getName()).append("'");
				stmt = javaConn.prepareStatement(query.toString());
				try {
					stmt.execute();
				} catch (Exception e) {
					logger.debug("query is " + query.toString());
					logger.error("Comment change error ", e);
				}

				query = new StringBuffer();
				query.append(" exec sp_addextendedproperty 'Caption', '").append(dao.getComment()).append("' ,'user' ,").append(userDB.getUsers()).append(" ,'table' ").append(" , '").append(dao.getName()).append("'");
				stmt = javaConn.prepareStatement(query.toString());
				stmt.execute();
			}

		} catch (Exception e) {
			logger.error("Comment change error ", e);
		} finally {
			try {
				stmt.close();
			} catch (Exception e) {
			}
			try {
				javaConn.close();
			} catch (Exception e) {
			}
		}
	}

}
