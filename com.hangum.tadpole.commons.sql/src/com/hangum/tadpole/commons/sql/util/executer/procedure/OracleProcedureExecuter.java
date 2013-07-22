package com.hangum.tadpole.commons.sql.util.executer.procedure;

import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.dao.rdb.InOutParameterDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * oracle procedure executer
 * 
 * @author hangum
 *
 */
public class OracleProcedureExecuter extends ProcedureExecutor {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(OracleProcedureExecuter.class);

	/**
	 * 
	 * @param procedureDAO
	 * @param userDB
	 */
	public OracleProcedureExecuter(ProcedureFunctionDAO procedureDAO, UserDBDAO userDB) {
		super(procedureDAO, userDB);
	}
	
	@Override
	public boolean exec(List<InOutParameterDAO> parameterList) {
		java.sql.Connection javaConn = null;
		java.sql.CallableStatement cstmt = null;

		try {
			if(listOutParamValues == null) getOutParameters();

			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			// make the script
			StringBuffer query = new StringBuffer("{call " + procedureDAO.getName() + "(");
			StringBuffer params = new StringBuffer();

			// in script
			for (int i = 0; i < listInParamValues.size(); i++) {
				if (i == 0) {
					params.append("?");
				} else {
					params.append(",?");
				}
			}

			query.append(params.toString() + ")}");
			cstmt = javaConn.prepareCall(query.toString());
			
			if(logger.isDebugEnabled()) {
				logger.debug("[make procedure query]" + query.toString());
			}
			
			// set the value
			for (InOutParameterDAO inOutParameterDAO : parameterList) {
				cstmt.setObject(inOutParameterDAO.getOrder(), inOutParameterDAO.getValue());
			}

			// Set the OUT Param
			for (int i = 0; i < listOutParamValues.size(); i++) {
				InOutParameterDAO dao = listInParamValues.get(i);

//				if (StringUtils.equalsIgnoreCase(dao.getType(), "OUT")) {
					cstmt.registerOutParameter(dao.getOrder(), Types.VARCHAR);
//				} else if (StringUtils.equalsIgnoreCase(dao.getType(), "IN OUT")) {
//					cstmt.setObject(dao.getOrder(), dao.getValue());
//					cstmt.registerOutParameter(dao.getOrder(), Types.VARCHAR);
//				} else {
//					cstmt.setObject(dao.getOrder(), dao.getValue());
//				}
			}

			logger.debug("Execute Procedure query is\t  " + query.toString());

			cstmt.execute();
			javaConn.commit();

			for (int i = 0; i < listOutParamValues.size(); i++) {
				InOutParameterDAO dao = listOutParamValues.get(i);

				if (StringUtils.contains(dao.getType(), "OUT")) {
					// dao.getType에 따라서 분리해야함.
					logger.debug("Execute Procedure result " + dao.getName() + "=" + cstmt.getString(dao.getOrder()));
				}
			}
			MessageDialog.openInformation(null, "Information", "Execute Compete.");

			return true;

		} catch (Exception e) {
			logger.error("ProcedureExecutor executing error", e);
			MessageDialog.openError(null, "Error", e.getMessage());
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
