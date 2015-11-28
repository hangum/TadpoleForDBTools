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
package com.hangum.tadpole.rdb.core.editors.main.composite.resultdetail;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.widgets.TadpoleEditorWidget;
import com.hangum.tadpole.engine.sql.util.export.JsonExpoter;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.rdb.core.editors.main.composite.ResultSetComposite;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;

/**
 * json base composite
 * 
 * @author hangum
 *
 */
public class ResultJsonComposite extends AbstractResultDetailComposite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(ResultJsonComposite.class);
	
	private TadpoleEditorWidget textJson;

	public ResultJsonComposite(Composite parent, int style, ResultSetComposite rdbResultComposite, String defaultValue) {
		super(parent, style, rdbResultComposite);
		setLayout(new GridLayout(1, false));
		
		Composite compositeContent = new Composite(this, SWT.NONE);
		compositeContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeContent.setLayout(new GridLayout(1, false));
		
		textJson = new TadpoleEditorWidget(compositeContent, SWT.BORDER, EditorDefine.EXT_JSON, defaultValue, ""); //$NON-NLS-1$
		textJson.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeBtn = new Composite(this, SWT.NONE);
		compositeBtn.setLayout(new GridLayout(1, false));
		compositeBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		compositeTail = new ResultTailComposite(this, compositeBtn, SWT.NONE);
		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 2;
		gl_compositeResult.horizontalSpacing = 2;
		gl_compositeResult.marginHeight = 0;
		gl_compositeResult.marginWidth = 2;
		compositeTail.setLayout(gl_compositeResult);
	}

	@Override
	public void initUI() {
		this.layout();
	}

	@Override
	public void printUI(RequestQuery reqQuery, QueryExecuteResultDTO rsDAO) {
		super.printUI(reqQuery, rsDAO);
		
		textJson.setText(JsonExpoter.makeContent("", getRsDAO()));

		// 메시지를 출력합니다.
		compositeTail.execute(getTailResultMsg());
	}

	@Override
	public RESULT_COMP_TYPE getResultType() {
		return RESULT_COMP_TYPE.JSON;
	}

}
