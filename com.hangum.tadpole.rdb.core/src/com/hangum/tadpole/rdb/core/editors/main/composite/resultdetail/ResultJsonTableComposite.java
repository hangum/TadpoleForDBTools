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
package com.hangum.tadpole.rdb.core.editors.main.composite.resultdetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.commons.util.JSONUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.editors.main.composite.ResultSetComposite;
import com.hangum.tadpole.rdb.core.editors.main.composite.tail.ResultTailComposite;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * Text result type 
 * 
 * @author hangum
 *
 */
public class ResultJsonTableComposite extends AbstractResultDetailComposite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(ResultJsonTableComposite.class);
	private TableViewer tvQueryResult;
	
//	private SQLResultFilter sqlFilter = new SQLResultFilter();
//	private SQLResultSorter sqlSorter;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ResultJsonTableComposite(Composite parent, int style, ResultSetComposite rdbResultComposite, List<QueryExecuteResultDTO> listRSDao) {
		super(parent, style, rdbResultComposite);
		setLayout(new GridLayout(1, false));
		GridData gd_compositeBody = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
		setLayoutData(gd_compositeBody);
		
		Composite compositeBody = new Composite(this, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 0;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);
		
		tvQueryResult = new TableViewer(compositeBody, /* SWT.VIRTUAL | */ SWT.MULTI | SWT.BORDER ); //| SWT.FULL_SELECTION
		final Table tableResult = tvQueryResult.getTable();
		GridData gd_tableResult = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_tableResult.heightHint = 90;
		tableResult.setLayoutData(gd_tableResult);
		
		tableResult.setLinesVisible(true);
		tableResult.setHeaderVisible(true);
		
		// bottom composite group
		Composite compositeBtn = new Composite(compositeBody, SWT.NONE);
		compositeBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		GridLayout gl_compositeBtn = new GridLayout(7, false);
		gl_compositeBtn.verticalSpacing = 2;
		gl_compositeBtn.horizontalSpacing = 2;
		gl_compositeBtn.marginWidth = 0;
		gl_compositeBtn.marginHeight = 2;
		compositeBtn.setLayout(gl_compositeBtn);
				
		compositeTail = new ResultTailComposite(rdbResultComposite.getUserDB(), this, compositeBtn, SWT.NONE, false);
		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 2;
		gl_compositeResult.horizontalSpacing = 2;
		gl_compositeResult.marginHeight = 0;
		gl_compositeResult.marginWidth = 2;
		compositeTail.setLayout(gl_compositeResult);
	}
	
	@Override
	public void printUI(RequestQuery reqQuery, List<QueryExecuteResultDTO> listRSDao) {
		if(listRSDao == null) return;
		if(listRSDao.size() == 0) return;
		super.printUI(reqQuery, listRSDao);
		
		makePrintData(listRSDao);
	}
	
	/**
	 * create column
	 * 
	 * @param headrArray
	 */
	private void createColumn(List<String> headrArray) {
		// 기존 column을 삭제한다.
		Table table = tvQueryResult.getTable();
		int columnCount = table.getColumnCount();
		for(int i=0; i<columnCount; i++) {
			table.getColumn(0).dispose();
		}
		
		if(headrArray == null | headrArray.isEmpty()) return;
		
		for (String strColumnName : headrArray) {
			final TableViewerColumn tv = new TableViewerColumn(tvQueryResult, SWT.RIGHT);
			final TableColumn tc = tv.getColumn();
			
			tc.setText(strColumnName);
			tc.setResizable(true);
			tc.setMoveable(true);
		}
	}
	
	/**
	 * 결과 데이터를 만든다.
	 * 
	 * @param listRSDao
	 * @return
	 */
	private void makePrintData(List<QueryExecuteResultDTO> listRSDao) {
		StringBuffer strBuff = new StringBuffer();
		for (QueryExecuteResultDTO queryExecuteResultDTO : listRSDao) {
			try {
				strBuff.append(
						queryExecuteResultDTO.getJsonString()
						);
			} catch(Exception e) {
				logger.error("Result json view error", e);
			}
		}
		
		List<Map<String, String>> flatJson = JSONUtil.getJsonToTable(strBuff.toString());
		List<String> headrArray = new ArrayList<String>(JSONUtil.collectHeaders(flatJson));
		
		createColumn(headrArray);
		tvQueryResult.setLabelProvider(new JSONQueryResultLabelProvider(headrArray, flatJson));
		tvQueryResult.setContentProvider(ArrayContentProvider.getInstance());
		tvQueryResult.setInput(flatJson);
		
		TableUtil.packTable(tvQueryResult.getTable());
	}

	@Override
	public void initUI() {
		if(this.isDisposed()) return;
		
		this.layout();
	}

	@Override
	public PublicTadpoleDefine.RESULT_COMP_TYPE getResultType() {
		return PublicTadpoleDefine.RESULT_COMP_TYPE.TABLEJSON;
	}
	
	@Override
	protected void checkSubclass() {
	}
}

/**
 * json result column label provider
 * 
 * @author hangum
 *
 */
class JSONQueryResultLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {
	List<String> headrArray;
	List<Map<String, String>> flatJson;
	
	/**
	 * header
	 * 
	 * @param header
	 * @param flatJson
	 */
	public JSONQueryResultLabelProvider(List<String> headrArray, List<Map<String, String>> flatJson) {
		this.headrArray = headrArray;
		this.flatJson = flatJson;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Map<String, String> rsResult = (Map<String, String>)element;
		
		Object obj = rsResult.get(headrArray.get(columnIndex));
		if(obj == null) {
			return GetPreferenceGeneral.getResultNull();
		} else {
			return StringUtils.abbreviate(obj.toString(), 0, GetPreferenceGeneral.getRDBShowInTheColumn());
		}
	}
	
}