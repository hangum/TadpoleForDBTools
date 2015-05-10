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
package com.hangum.tadpole.rdb.core.dialog.table;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

public class AlterTableExecutor {
	private UserDBDAO userDB;
	private List<AlterTableMetaDataDAO> listAlterTableColumns;
	private Shell parentShell;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AlterTableExecutor.class);

	public AlterTableExecutor(Shell parentShell, List<AlterTableMetaDataDAO> listAlterTableColumns, UserDBDAO userDB) {
		this.listAlterTableColumns = listAlterTableColumns;
		this.userDB = userDB;
		this.parentShell = parentShell;
	}

	public List<AlterTableMetaDataDAO> Initializing(String selectTable) {
		ResultSet rsDumy = null;
		// ResultSet rsCons = null;
		java.sql.Connection javaConn = null;
		PreparedStatement stmt = null;

		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();

			stmt = javaConn.prepareStatement("SELECT * FROM " + selectTable + " WHERE 1=0 "); //$NON-NLS-1$
			rsDumy = stmt.executeQuery();
			ResultSetMetaData rsm = rsDumy.getMetaData();
			DatabaseMetaData dmd = javaConn.getMetaData();
			
			ResultSet rs = dmd.getTypeInfo();
			while (rs.next()){
				String info="";
				for(int col = 1; col <= rs.getMetaData().getColumnCount(); col++){
					info  += rs.getMetaData().getColumnLabel(col) + "=" + rs.getString(col) + ", ";
				}
				logger.debug(info);
			}
			
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			List<HashMap<String,Integer>> conlist = sqlClient.queryForList("primarykeyListInTable", selectTable); //$NON-NLS-1$
			
			for (int i = 1; i <= rsm.getColumnCount(); i++) {
				AlterTableMetaDataDAO dao = new AlterTableMetaDataDAO();

				dao.setDbdef(userDB.getDBDefine());
				dao.setSeqNo(i);
				dao.setColumnId(i);
				dao.setColumnName(rsm.getColumnLabel(i));
				dao.setDataType(rsm.getColumnType(i));
				dao.setDataTypeName(rsm.getColumnTypeName(i));
				
				logger.debug(dao.toString());
				
				if (DataTypeDef.isCharType(rsm.getColumnType(i))){
					dao.setDataSize(rsm.getColumnDisplaySize(i));
					dao.setUseSize(true);
					dao.setUsePrecision(false);
				} else if (DataTypeDef.isNumericType(rsm.getColumnType(i))){
					dao.setDataPrecision(rsm.getPrecision(i));
					dao.setDataScale(rsm.getScale(i));
					dao.setUseSize(false);
					dao.setUsePrecision(true);
				} else {
					dao.setUseSize(false);
					dao.setUsePrecision(false);
				}
				
				// primary key
				if (conlist.size() > 0) {
					for (int k = 0; k < conlist.size(); k++) {
						HashMap cons = conlist.get(k);						
						if(dao.getDbdef() == DBDefine.SQLite_DEFAULT ) {
							/* cid, name, type, notnull, dflt_value, pk */
							if ("1".equals(cons.get("pk").toString())) {
								dao.setPrimaryKey(true);
							}
						}else{
							if (dao.getColumnName().equalsIgnoreCase(cons.get("column_name").toString())) { //$NON-NLS-1$
								dao.setPrimaryKey(true);
							}
						}
					}
				}
				dao.setDefaultValue("");
				dao.setNullable(1 != rsm.isNullable(i));

				listAlterTableColumns.add(dao);
			}

			return listAlterTableColumns;

		} catch (Exception e) {
			logger.error("SimpleDataGenerate select error", e);
			return listAlterTableColumns;
		} finally {
			try {
				stmt.close();
			} catch (Exception e) {
			}
			try {
				rsDumy.close();
			} catch (Exception e) {
			}
			try {
				javaConn.close();
			} catch (Exception e) {
			}
		}

	}

	public boolean AlterTableStart(String selectTable, int genCount) {

		java.sql.Connection javaConn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmtCount = null;
		ResultSet rsCount = null;

		Random oRandom = new Random();

		try {

			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);

			javaConn = client.getDataSource().getConnection();

			StringBuffer query = new StringBuffer(" INSERT INTO " + selectTable);
			StringBuffer cols = new StringBuffer();
			StringBuffer vars = new StringBuffer();

	
			MessageDialog.openInformation(parentShell, "Information", "Generation Compete.");

			return true;

		} catch (Exception e) {
			logger.error("SimpleDataGenerate select error", e);
			MessageDialog.openError(parentShell, "Error", e.getMessage());
			return false;
		} finally {
			try {
				stmt.close();
			} catch (Exception e) {
			}
			try {
				stmtCount.close();
			} catch (Exception e) {
			}
			try {
				rsCount.close();
			} catch (Exception e) {
			}
			try {
				javaConn.close();
			} catch (Exception e) {
			}
		}
	}

}
