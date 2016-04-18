/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.util.export;

import java.io.File;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;

/**
 * xml exporter
 * 
 * @author hangum
 *
 */
public class XMLExporter extends AbstractTDBExporter {

	/**
	 * make content
	 * 
	 * @param tableName
	 * @param rsDAO
	 * @return
	 * @throws Exception
	 */
	public static String makeContent(String tableName, QueryExecuteResultDTO rsDAO) throws Exception {
		return makeContent(tableName, rsDAO, -1);
	}
			
	/**
	 * make content
	 * 
	 * @param tableName
	 * @param rsDAO
	 * @return
	 */
	public static String makeContent(String tableName, QueryExecuteResultDTO rsDAO, int intLimitCnt) throws Exception {
		final StringWriter stWriter = new StringWriter();
		final List<Map<Integer, Object>> dataList = rsDAO.getDataList().getData();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    final Document doc = builder.newDocument();
	    final Element results = doc.createElement(tableName);
	    doc.appendChild(results);
						
	    Map<Integer, String> mapLabelName = rsDAO.getColumnLabelName();
	    for(int i=0; i<dataList.size(); i++) {
	    	Map<Integer, Object> mapColumns = dataList.get(i);
			Element row = doc.createElement("Row");
			results.appendChild(row);
			
			for(int j=1; j<mapColumns.size(); j++) {
				String columnName = mapLabelName.get(j);
				String strValue = mapColumns.get(j)==null?"":""+mapColumns.get(j);
				
				Element node = doc.createElement(columnName);
				node.appendChild(doc.createTextNode(strValue));
				row.appendChild(node);
			}
			
			if(i == intLimitCnt) break;
		}
		
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
	 * make content file
	 * 
	 * @param tableName
	 * @param rsDAO
	 * @return
	 * @throws Exception
	 */
	public static String makeContentFile(String tableName, QueryExecuteResultDTO rsDAO) throws Exception {
		String strTmpDir = PublicTadpoleDefine.TEMP_DIR + tableName + System.currentTimeMillis() + PublicTadpoleDefine.DIR_SEPARATOR;
		String strFile = tableName + ".xml";
		String strFullPath = strTmpDir + strFile;
		
		final StringWriter stWriter = new StringWriter();
		final List<Map<Integer, Object>> dataList = rsDAO.getDataList().getData();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    final Document doc = builder.newDocument();
	    final Element results = doc.createElement(tableName);
	    doc.appendChild(results);
						
	    Map<Integer, String> mapLabelName = rsDAO.getColumnLabelName();
	    for(int i=0; i<dataList.size(); i++) {
	    	Map<Integer, Object> mapColumns = dataList.get(i);
			Element row = doc.createElement("Row");
			results.appendChild(row);
			
			for(int j=1; j<mapColumns.size(); j++) {
				String columnName = mapLabelName.get(j);
				String strValue = mapColumns.get(j)==null?"":""+mapColumns.get(j);
				
				Element node = doc.createElement(columnName);
				node.appendChild(doc.createTextNode(strValue));
				row.appendChild(node);
			}
		}
		
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
		
		FileUtils.writeStringToFile(new File(strFullPath), stWriter.toString(), true);
		
		return strFullPath;
	}
}
