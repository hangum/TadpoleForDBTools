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
package com.hangum.db.browser.rap.core.viewers.object;

import java.sql.PreparedStatement;
import java.util.Random;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import com.hangum.db.commons.sql.TadpoleSQLManager;
import com.hangum.db.commons.sql.define.DBDefine;
import com.hangum.db.dao.mysql.TableDAO;
import com.hangum.db.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * SampleDAtaEditingSupport
 * 
 * @author hangum
 */
public class TableCommentEditorSupport extends EditingSupport {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TableCommentEditorSupport.class);

	private final TableViewer viewer;
	private final UserDBDAO userDB;

	public TableCommentEditorSupport(TableViewer viewer, UserDBDAO userDB) {
		super(viewer);
		this.userDB = userDB;
		this.viewer = viewer;
		logger.debug("////////////////////////////////");
		try {
			logger.debug("userDB is " + userDB.toString());
		} catch (Exception e) {

		}
		logger.debug("////////////////////////////////");
	}

	@Override
	protected CellEditor getCellEditor(Object element) {

		return new TextCellEditor(viewer.getTable());
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {

		String comment = "";
		try {
			TableDAO dao = (TableDAO) element;
			comment = dao.getComment();
		} catch (Exception e) {

		}
		return comment;
	}

	@Override
	protected void setValue(Object element, Object value) {
		String comment = "";

		try {
			TableDAO dao = (TableDAO) element;

			comment = (String) value;
			dao.setComment(comment);

			ApplyComment(dao);

			viewer.update(element, null);
		} catch (Exception e) {
			viewer.update(element, null);
		}

	}

	private void ApplyComment(TableDAO dao) {

		java.sql.Connection javaConn = null;
		PreparedStatement stmt = null;

		Random oRandom = new Random();

		try {

			logger.debug("userDB is " + userDB.toString());

			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);

			javaConn = client.getDataSource().getConnection();

			StringBuffer query = new StringBuffer();

			if (DBDefine.ORACLE_DEFAULT.equals(this.userDB.getTypes())) {
				query.append(" COMMENT ON TABLE ").append(dao.getName()).append(" IS '").append(dao.getComment()).append("'");
			} else if (DBDefine.MSSQL_DEFAULT.equals(this.userDB.getTypes())) {
				query.append(" exec sp_addextendedproperty 'Caption', '").append(dao.getComment()).append("' ,'user' ").append(userDB.getUsers()).append(" ,'table' ").append(" , '").append(dao.getName()).append("'");
			}

			logger.debug("Table Comment SQL is " + query.toString());
			if (DBDefine.ORACLE_DEFAULT.equals(this.userDB.getTypes()) || DBDefine.MSSQL_DEFAULT.equals(this.userDB.getTypes())) {
				stmt = javaConn.prepareStatement(query.toString());
				stmt.executeQuery();
				javaConn.commit();
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
