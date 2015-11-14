/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     nilriri - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table;

import java.sql.PreparedStatement;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * column comment editor
 * 
 * @author nilriri
 *
 */
public class ColumnCommentEditorSupport extends EditingSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6292003867430114514L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ColumnCommentEditorSupport.class);

	private final TableViewer tableviewer;
	private final TableViewer viewer;
	private UserDBDAO userDB;
	private int column;

	/**
	 * 
	 * @param viewer
	 * @param explorer
	 */
	public ColumnCommentEditorSupport(TableViewer tableviewer, TableViewer viewer, UserDBDAO userDB, int column) {
		super(viewer);
		
		this.tableviewer = tableviewer;
		this.viewer = viewer;
		this.userDB = userDB;
		this.column = column;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		if(column == 3) return new CommentCellEditor(column, viewer);
		else return null;
	}

	@Override
	protected boolean canEdit(Object element) {
		if(column == 3) {
			if (userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT || 
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
			TableColumnDAO dao = (TableColumnDAO) element;
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
			TableColumnDAO dao = (TableColumnDAO) element;
			comment = (String) (value == null ? "" : value);
			if(logger.isDebugEnabled()) logger.debug("dao column name is " + dao.getField());
			
			// 기존 코멘트와 다를때만 db에 반영한다.
			if (!(comment.equals(dao.getComment()))) {
				applyComment(dao);
				dao.setComment(comment);
			}

			viewer.update(element, null);
		} catch (Exception e) {
			logger.error("setValue error ", e);
		}
		viewer.update(element, null);
	}

	private void applyComment(TableColumnDAO columnDAO) {
		// TODO : DBMS별 처리를 위해 별도의 Class로 분리해야 하지 않을까? 

		java.sql.Connection javaConn = null;
		PreparedStatement stmt = null;
		try {

			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			IStructuredSelection is = (IStructuredSelection) tableviewer.getSelection();
			TableDAO tableDAO = (TableDAO)is.getFirstElement();
			StringBuffer query = new StringBuffer();

			if (userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT || userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
				query.append(" COMMENT ON COLUMN ").append(tableDAO.getSysName()+".").append(columnDAO.getField()).append(" IS '").append(columnDAO.getComment()).append("'");
				stmt = javaConn.prepareStatement(query.toString());
				
				try{
					stmt.execute();
				}catch(Exception e){
					//  org.postgresql.util.PSQLException: No results were returned by the query.
				}

			} else if (userDB.getDBDefine() == DBDefine.MSSQL_8_LE_DEFAULT) {
				query.append(" exec sp_dropextendedproperty 'MS_Description' ").append(", 'user' ,").append(userDB.getUsers());
				query.append(",'table' , '").append(tableDAO.getSysName()).append("'");
				query.append(",'column' , '").append(columnDAO.getSysName()).append("'");
				stmt = javaConn.prepareStatement(query.toString());
				stmt.execute();

				query = new StringBuffer();
				query.append(" exec sp_addextendedproperty 'MS_Description', '").append(columnDAO.getComment()).append("' ,'user' ,").append(userDB.getUsers());
				query.append(",'table' , '").append(tableDAO.getSysName()).append("'");
				query.append(",'column', '").append(columnDAO.getSysName()).append("'");
				stmt = javaConn.prepareStatement(query.toString());
				stmt.execute();

			} else if (userDB.getDBDefine() == DBDefine.MSSQL_DEFAULT ) {
				query.append(" exec sp_dropextendedproperty 'MS_Description' ").append(", 'schema' , " + tableDAO.getSchema_name());
				query.append(",'table' , '").append(tableDAO.getTable_name()).append("'");
				query.append(",'column' , '").append(columnDAO.getSysName()).append("'");
				stmt = javaConn.prepareStatement(query.toString());
				stmt.execute();

				query = new StringBuffer();
				query.append(" exec sp_addextendedproperty 'MS_Description', '").append(columnDAO.getComment()).append("' ,'schema' , " + tableDAO.getSchema_name());
				query.append(",'table' , '").append(tableDAO.getTable_name()).append("'");
				query.append(",'column', '").append(columnDAO.getSysName()).append("'");
				stmt = javaConn.prepareStatement(query.toString());
				stmt.execute();

			} else if (userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT || userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {

				query.append(" ALTER TABLE  ").append(tableDAO.getSysName()+" CHANGE ")
					.append(columnDAO.getField() + " " + columnDAO.getField() + " ")
					.append( columnDAO.getType() + " " + ("NO".equals(columnDAO.getNull())?"NOT NULL":"NULL"));
				
				if(columnDAO.getDefault() == null) {
					if(!"".equals(columnDAO.getExtra())) query.append(" " + columnDAO.getExtra() + " " ); 
				} else if("CURRENT_TIMESTAMP".equals(columnDAO.getDefault())) {
					query.append(" DEFAULT " + columnDAO.getDefault());
				} else {
					query.append(" DEFAULT '" + columnDAO.getDefault() + "'" );
				}
				
				query.append(" COMMENT '").append(SQLUtil.makeQuote(columnDAO.getComment())).append("'");
				if(logger.isDebugEnabled()) logger.debug(query);
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
