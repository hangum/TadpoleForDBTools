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
package com.hangum.tadpole.rdb.core.dialog.procedure;

import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

public class ProcedureExecutor {
	private UserDBDAO userDB;
	private List<InOutParameterDAO> listParamValues;
	private String procedureName;
	private Shell parentShell;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ProcedureExecutor.class);

	public ProcedureExecutor(Shell parentShell, String procedureName, List<InOutParameterDAO> listParamValues, UserDBDAO userDB) {
		this.listParamValues = listParamValues;
		this.userDB = userDB;
		this.procedureName = procedureName;
		this.parentShell = parentShell;

	}

	public List<InOutParameterDAO> init() {

		SqlMapClient client;
		try {
			client = TadpoleSQLManager.getInstance(userDB);
			listParamValues = client.queryForList("getProcedureParamter", procedureName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listParamValues;
	}

	public boolean exec() {

		java.sql.Connection javaConn = null;
		java.sql.CallableStatement cstmt = null;

		try {

			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);

			javaConn = client.getDataSource().getConnection();

			StringBuffer query = new StringBuffer("{call " + procedureName + "(");
			StringBuffer params = new StringBuffer();

			for (int i = 0; i < listParamValues.size(); i++) {
				if (i == 0) {
					params.append("?");
				} else {
					params.append(",?");
				}
			}

			query.append(params.toString() + ")}");

			cstmt = javaConn.prepareCall(query.toString());

			for (int i = 0; i < listParamValues.size(); i++) {
				InOutParameterDAO dao = (InOutParameterDAO) listParamValues.get(i);

				if (StringUtils.equalsIgnoreCase(dao.getType(), "OUT")) {
					cstmt.registerOutParameter(dao.getOrder(), Types.VARCHAR);
				} else if (StringUtils.equalsIgnoreCase(dao.getType(), "IN OUT")) {
					cstmt.setObject(dao.getOrder(), dao.getValue());
					cstmt.registerOutParameter(dao.getOrder(), Types.VARCHAR);
				} else {
					cstmt.setObject(dao.getOrder(), dao.getValue());
				}
			}

			logger.debug("Execute Procedure query is " + query.toString());

			cstmt.execute();
			javaConn.commit();

			for (int i = 0; i < listParamValues.size(); i++) {
				InOutParameterDAO dao = (InOutParameterDAO) listParamValues.get(i);

				if (StringUtils.contains(dao.getType(), "OUT")) {
					// dao.getType에 따라서 분리해야함.
					logger.debug("Execute Procedure result " + dao.getName() + "=" + cstmt.getString(dao.getOrder()));
				}
			}

			MessageDialog.openInformation(parentShell, "Information", "Execute Compete.");

			return true;

		} catch (Exception e) {
			logger.error("ProcedureExecutor executing error", e);
			MessageDialog.openError(parentShell, "Error", e.getMessage());
			return false;
		} finally {
			try {
				cstmt.close();
			} catch (Exception e) {
			}
			try {
				javaConn.close();
			} catch (Exception e) {
			}
		}
	}

}
