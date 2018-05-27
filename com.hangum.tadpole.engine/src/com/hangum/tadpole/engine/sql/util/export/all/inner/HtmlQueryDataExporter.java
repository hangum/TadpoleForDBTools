/*******************************************************************************
 * Copyright (c) 2018 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.util.export.all.inner;

import java.io.File;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.hangum.tadpole.commons.libs.core.define.HTMLDefine;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.EXPORT_METHOD;

/**
 * html query data exporter
 * 
 * @author hangum
 *
 */
public class HtmlQueryDataExporter extends AQueryDataExporter {
	private static final Logger logger = Logger.getLogger(HtmlQueryDataExporter.class);
	private String encoding = "";
	private StringBuffer strHtmlData = new StringBuffer();
	/**
	 * listData array 
	 */
	private List<String[]> listResultData = new ArrayList<String[]>();
	
	public HtmlQueryDataExporter(String encoding) {
		this.encoding = encoding;
	}
	
	@Override
	public void init(String fileName, String fileExtension) throws Exception {
		super.init(fileName, fileExtension);
		
		exportDto.setExportMethod(EXPORT_METHOD.HTML);
	}

	@Override
	public void makeHead(int intColumnCnt, ResultSetMetaData rsm) throws Exception {
		StringBuffer sbHead = new StringBuffer(HTMLDefine.HTML_STYLE);
		sbHead.append("<table class='tg'>");
		for(int i=0; i<intColumnCnt; i++) {
			sbHead.append( HTMLDefine.makeTH(rsm.getColumnLabel(i+1)));
		}
		
		strHtmlData.append(sbHead);
	}

	@Override
	public void makeBody(int intRowCnt, String[] strArryData) throws Exception {
		listResultData.add(strArryData);
		
		if(((intRowCnt+1) % PER_DATA_SAVE_COUNT) == 0) {
			_makeHtmlData();
			
			if(logger.isDebugEnabled()) logger.debug("===processes =============>" + intRowCnt);
			try { Thread.sleep(THREAD_SLEEP_MILLIS); } catch(Exception e) {}
		}
	}

	@Override
	public void makeTail() throws Exception {
		if(!listResultData.isEmpty()) {
			_makeHtmlData();
		}
		
		FileUtils.writeStringToFile(new File(exportDto.getFileFullName()), "</table>", encoding, true);
	}
	
	/**
	 * make html body daa
	 * 
	 * @throws Exception
	 */
	private void _makeHtmlData() throws Exception {
		// body start
		for(int i=0; i<listResultData.size(); i++) {
			String[] arryData = listResultData.get(i);
			
			StringBuffer sbTmp = new StringBuffer();
			for(int j=0; j<arryData.length; j++) {
				String strValue = arryData[j]==null?"":""+arryData[j];
				sbTmp.append( HTMLDefine.makeTD(StringEscapeUtils.unescapeHtml(strValue)) ); //$NON-NLS-1$
			}
			strHtmlData.append(HTMLDefine.makeTR(sbTmp.toString()));
		}

		listResultData.clear();
		FileUtils.writeStringToFile(new File(exportDto.getFileFullName()), strHtmlData.toString(), encoding, true);
		strHtmlData.setLength(0);
	}

	@Override
	public void close() throws Exception {
	}
}
