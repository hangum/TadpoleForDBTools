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
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.engine.sql.util.export.HTMLExporter;
import com.hangum.tadpole.rdb.core.editors.main.composite.ResultSetComposite;

/**
 * Text base composite
 * 
 * @author hangum
 *
 */
public class ResultTextComposite extends AbstractResultDetailComposite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(ResultTextComposite.class);
	private Browser browserResult;

	public ResultTextComposite(Composite parent, int style, ResultSetComposite rdbResultComposite) {
		super(parent, style, rdbResultComposite);
		setLayout(new GridLayout(1, false));
		
		Composite compositeContent = new Composite(this, SWT.NONE);
		compositeContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeContent.setLayout(new GridLayout(1, false));
		
		browserResult = new Browser(compositeContent, SWT.BORDER);
		browserResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
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
	public void printUI() {
		super.printUI();
		
		browserResult.setText(HTMLExporter.makeContent("", getRsDAO()));

		// 메시지를 출력합니다.
		compositeTail.execute(getTailResultMsg());
	}

	@Override
	public RESULT_COMP_TYPE getResultType() {
		return RESULT_COMP_TYPE.Text;
	}

}
