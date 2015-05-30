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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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

import au.com.bytecode.opencsv.CSVWriter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Query utils
 * 
 * @author hangum
 *
 */
public class QueryUtils {
	private static final Logger logger = Logger.getLogger(QueryUtils.class);
	public static enum RESULT_TYPE {JSON, CSV, XML};
	
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
						Object value = rs.getObject(i);
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
		transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		StreamResult sr = new StreamResult(stWriter);
		transformer.transform(domSource, sr);	

		return stWriter.toString();
	}

}
