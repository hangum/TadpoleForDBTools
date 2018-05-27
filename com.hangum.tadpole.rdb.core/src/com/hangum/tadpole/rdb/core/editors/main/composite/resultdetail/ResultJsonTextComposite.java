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

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.ace.editor.core.widgets.TadpoleResultViewWidget;
import com.hangum.tadpole.commons.util.JSONUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.utils.EditorDefine;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.hangum.tadpole.rdb.core.editors.main.composite.ResultSetComposite;
import com.hangum.tadpole.rdb.core.editors.main.composite.tail.ResultTailComposite;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * Text result type 
 * 
 * @author hangum
 *
 */
public class ResultJsonTextComposite extends AbstractResultDetailComposite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(ResultJsonTextComposite.class);
	private TadpoleResultViewWidget textResult;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ResultJsonTextComposite(Composite parent, int style, ResultSetComposite rdbResultComposite, List<QueryExecuteResultDTO> listRSDao) {
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
		
		textResult = new TadpoleResultViewWidget(compositeBody, SWT.BORDER, EditorDefine.EXT_JSON, makePrintData(listRSDao), "");
		textResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
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
		
		textResult.setText(makePrintData(listRSDao));
	}
	
	/**
	 * 결과 데이터를 만든다.
	 * 
	 * @param listRSDao
	 * @return
	 */
	private String makePrintData(List<QueryExecuteResultDTO> listRSDao) {
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
		
		return JSONUtil.getPretty(strBuff.toString());
	}

	@Override
	public void initUI() {
		if(this.isDisposed()) return;
		
		this.layout();
	}

	@Override
	public PublicTadpoleDefine.RESULT_COMP_TYPE getResultType() {
		return PublicTadpoleDefine.RESULT_COMP_TYPE.TextJSON;
	}
	
	@Override
	protected void checkSubclass() {
	}
}
