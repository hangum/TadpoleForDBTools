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
package com.hangum.tadpole.engine.sql.util;

import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hangum.tadpole.commons.util.JSONUtil;
import com.hangum.tadpole.commons.util.ResultSetUtil;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.ibatis.sqlmap.client.SqlMapClient;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * Query utils
 * 
 * @author hangum
 *
 */
public class QueryUtils {
	private static final Logger logger = Logger.getLogger(QueryUtils.class);
	
	/** SUPPORT RESULT TYPE */
	public static enum RESULT_TYPE {JSON, CSV, XML, HTML_TABLE};
	
	/**
	 * select문 이외의 쿼리를 실행합니다
	 * 
	 * @param reqQuery
	 * @exception
	 */
	private static Object runSQLOther(
			final UserDBDAO userDB,
			String strQuery, 
			final List<Object> listParam
	) throws SQLException, Exception 
	{
		
		// is tajo
		if(DBDefine.TAJO_DEFAULT == userDB.getDBDefine()) {
			logger.error("Not support TAJO.");
		} else { 
			
			java.sql.Connection javaConn = null;
			PreparedStatement prepareStatement = null;
			try {
				SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
				javaConn = client.getDataSource().getConnection();
				prepareStatement = javaConn.prepareStatement(strQuery);
				
				// TODO mysql일 경우 https://github.com/hangum/TadpoleForDBTools/issues/3 와 같은 문제가 있어 create table 테이블명 다음의 '(' 다음에 공백을 넣어주도록 합니다.
				if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT || userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
					final String checkSQL = strQuery.trim().toUpperCase();
					if(StringUtils.startsWithIgnoreCase(checkSQL, "CREATE TABLE")) { //$NON-NLS-1$
						strQuery = StringUtils.replaceOnce(strQuery, "(", " ("); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				
//				// hive는 executeUpdate()를 지원하지 않아서. 13.08.19-hangum
//				if(userDB.getDBDefine() == DBDefine.HIVE_DEFAULT | 
//					userDB.getDBDefine() == DBDefine.HIVE2_DEFAULT 
//				) { 
//					return prepareStatement.execute(strQuery);
//				} else {

				for(int i=0; i<listParam.size(); i++) {
					Object objParam = listParam.get(i);
					prepareStatement.setObject(i+1, objParam);
				}
				
				return prepareStatement.executeUpdate();
				
			} finally {
				try { prepareStatement.close();} catch(Exception e) {}
			}
		}  	// end which db
		
		return false;
	}
	
	/**
	 * execute query
	 * 
	 * @param userDB
	 * @param strQuery
	 * @param intStartCnt
	 * @param intSelectLimitCnt
	 * @return
	 * @throws Exception
	 */
	public static QueryExecuteResultDTO executeQuery(final UserDBDAO userDB, String strSQL, final int intStartCnt, final int intSelectLimitCnt, final String strNullValue) throws Exception {
		ResultSet resultSet = null;
		java.sql.Connection javaConn = null;
		Statement statement = null;
		
		strSQL = SQLUtil.makeExecutableSQL(userDB, strSQL);
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			statement = javaConn.createStatement();
			
			if(intStartCnt == 0) {
				statement.execute(strSQL);
				resultSet = statement.getResultSet();
			} else {
				strSQL = PartQueryUtil.makeSelect(userDB, strSQL, intStartCnt, intSelectLimitCnt);
				
				if(logger.isDebugEnabled()) logger.debug("part sql called : " + strSQL);
				statement.execute(strSQL);
				resultSet = statement.getResultSet();
			}
			return new QueryExecuteResultDTO(userDB, strSQL, false, resultSet, intSelectLimitCnt, intStartCnt, strNullValue);
			
		} catch(Exception e) {
			logger.error("execute query", e);
			throw e;
		} finally {
			if(statement != null) statement.close();
			if(resultSet != null) resultSet.close();
			if(javaConn != null) javaConn.close();
		}
		
	}
	
	/**
	 * execute DML
	 * 
	 * @param userDB
	 * @param strQuery
	 * @param listParam
	 * @param resultType
	 * @throws Exception
	 */
	public static String executeDML(final UserDBDAO userDB, final String strQuery, final List<Object> listParam, final String resultType) throws Exception {
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		Object effectObject = runSQLOther(userDB, strQuery, listParam);
		
		String strReturn = "";
		if(resultType.equals(RESULT_TYPE.CSV.name())) {
			final StringWriter stWriter = new StringWriter();
			CSVWriter csvWriter = new CSVWriter(stWriter, ',');
			
			String[] arryString = new String[2];
			arryString[0] = "effectrow";
			arryString[1] = String.valueOf(effectObject);
			csvWriter.writeNext(arryString);
			
			strReturn = stWriter.toString();
		} else if(resultType.equals(RESULT_TYPE.JSON.name())) {
			final JsonArray jsonArry = new JsonArray();
			JsonObject jsonObj = new JsonObject();
			jsonObj.addProperty("effectrow", String.valueOf(effectObject));
			jsonArry.add(jsonObj);
			
			strReturn = JSONUtil.getPretty(jsonArry.toString());
		} else {//if(resultType.equals(RESULT_TYPE.XML.name())) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    final Document doc = builder.newDocument();
		    final Element results = doc.createElement("Results");
		    doc.appendChild(results);
		    
		    Element row = doc.createElement("Row");
			results.appendChild(row);
			Element node = doc.createElement("effectrow");
			node.appendChild(doc.createTextNode(String.valueOf(effectObject)));
			row.appendChild(node);
			
			DOMSource domSource = new DOMSource(doc);
			TransformerFactory tf = TransformerFactory.newInstance();
			tf.setAttribute("indent-number", 4);
			
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			
			final StringWriter stWriter = new StringWriter();
			StreamResult sr = new StreamResult(stWriter);
			transformer.transform(domSource, sr);
			
			strReturn = stWriter.toString();
		}
		
		return strReturn;
	}
	
	/**
	 * query to csv
	 * 
	 * @param userDB
	 * @param strQuery
	 * @param isAddHead
	 * @param strDelimiter
	 * @return
	 * @throws Exception
	 */
	public static String selectToCSV(final UserDBDAO userDB, final String strQuery, final boolean isAddHead, final String strDelimiter) throws Exception {
		return selectToCSV(userDB, strQuery, new ArrayList(), isAddHead, strDelimiter);
	}
	
	/**
	 * query to csv
	 * 
	 * @param userDB
	 * @param strQuery
	 * @param listParam
	 * @param isAddHead is true add head title
	 * @param strDelimiter if delimite is null default comma(,)
	 */
	@SuppressWarnings("deprecation")
	public static String selectToCSV(final UserDBDAO userDB, final String strQuery, final List<Object> listParam, final boolean isAddHead, final String strDelimiter) throws Exception {
		final StringWriter stWriter = new StringWriter();
		
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		QueryRunner qr = new QueryRunner(client.getDataSource());
		qr.query(strQuery, listParam.toArray(), new ResultSetHandler<Object>() {

			@Override
			public Object handle(ResultSet rs) throws SQLException {
				ResultSetMetaData metaData = rs.getMetaData();
				
				char strDel;
				if("".equals(strDelimiter)) {
					strDel = ',';
				} else if(StringUtils.equalsIgnoreCase("\t", strDelimiter)) {
					strDel = (char)9;
				} else {
					strDel = strDelimiter.charAt(0);
				}
				
				CSVWriter csvWriter = new CSVWriter(stWriter, strDel);
				if(isAddHead) {
					String[] arryString = new String[metaData.getColumnCount()];
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						arryString[i-1] = metaData.getColumnLabel(i);
					}
					csvWriter.writeNext(arryString);
				}
				
				while (rs.next()) {
					String[] arryString = new String[metaData.getColumnCount()];
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						arryString[i-1] = rs.getString(i);
					}
					csvWriter.writeNext(arryString);
				}
			
				return stWriter.toString();
			}
		});

		return stWriter.toString();
	}
	
	/**
	 * query to json
	 * 
	 * @param userDB
	 * @param strQuery
	 * @return
	 * @throws Exception
	 */
	public static JsonArray selectToJson(final UserDBDAO userDB, final String strQuery) throws Exception {
		return selectToJson(userDB, strQuery, new ArrayList());
	}
	
	/**
	 * query to json
	 * 
	 * @param userDB
	 * @param strQuery
	 * @param listParam
	 */
	@SuppressWarnings("deprecation")
	public static JsonArray selectToJson(final UserDBDAO userDB, final String strQuery, final List<Object> listParam) throws Exception {
		final JsonArray jsonArry = new JsonArray();
		
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		QueryRunner qr = new QueryRunner(client.getDataSource());
		qr.query(strQuery, listParam.toArray(), new ResultSetHandler<Object>() {

			@Override
			public Object handle(ResultSet rs) throws SQLException {
				ResultSetMetaData metaData = rs.getMetaData();

				while (rs.next()) {
					JsonObject jsonObj = new JsonObject();
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						String columnName = metaData.getColumnLabel(i);
						String value = rs.getString(i) == null ? "" : rs.getString(i);
						
						jsonObj.addProperty(columnName.toLowerCase(), value);
					}
					jsonArry.add(jsonObj);
				}

				return jsonArry;
			}
		});

		return jsonArry;
	}
	
	/**
	 * query to xml
	 * 
	 * @param userDB
	 * @param strQuery
	 * @return
	 * @throws Exception
	 */
	public static String selectToXML(final UserDBDAO userDB, final String strQuery) throws Exception {
		return selectToXML(userDB, strQuery, new ArrayList());
	}
	
	/**
	 * query to xml
	 * 
	 * @param userDB
	 * @param strQuery
	 * @param listParam
	 */
	@SuppressWarnings("deprecation")
	public static String selectToXML(final UserDBDAO userDB, final String strQuery, final List<Object> listParam) throws Exception {
		final StringWriter stWriter = new StringWriter();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    final Document doc = builder.newDocument();
	    final Element results = doc.createElement("Results");
	    doc.appendChild(results);
		
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		QueryRunner qr = new QueryRunner(client.getDataSource());
		qr.query(strQuery, listParam.toArray(), new ResultSetHandler<Object>() {

			@Override
			public Object handle(ResultSet rs) throws SQLException {
				ResultSetMetaData metaData = rs.getMetaData();
				
				while (rs.next()) {
					Element row = doc.createElement("Row");
					results.appendChild(row);
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						String columnName = metaData.getColumnName(i);
						Object value = rs.getObject(i) == null?"":rs.getObject(i);
						Element node = doc.createElement(columnName);
						node.appendChild(doc.createTextNode(value.toString()));
						row.appendChild(node);
					}
				}
				
				return stWriter.toString();
			}
		});
		
		DOMSource domSource = new DOMSource(doc);
		TransformerFactory tf = TransformerFactory.newInstance();
		tf.setAttribute("indent-number", 4);
		
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");//"ISO-8859-1");
		StreamResult sr = new StreamResult(stWriter);
		transformer.transform(domSource, sr);

		return stWriter.toString();
	}
	
	/**
	 * result to html_table
	 * 
	 * @param userDB
	 * @param strQuery
	 * @param listParam
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static String selectToHTML_TABLE(final UserDBDAO userDB, final String strQuery, final List<Object> listParam) throws Exception {
		
		SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
		QueryRunner qr = new QueryRunner(client.getDataSource());
		Object strHTMLTable = qr.query(strQuery, listParam.toArray(), new ResultSetHandler<Object>() {

			@Override
			public Object handle(ResultSet rs) throws SQLException {

				try {
					return ResultSetUtil.makeResultSetTOHTML(rs, 1000);
				} catch(Exception e) {
					return e.getMessage();
				}
			}
		});

		return strHTMLTable.toString();	
	}
}
