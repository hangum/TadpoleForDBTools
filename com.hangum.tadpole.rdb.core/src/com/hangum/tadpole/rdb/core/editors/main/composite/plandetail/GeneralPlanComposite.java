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
package com.hangum.tadpole.rdb.core.editors.main.composite.plandetail;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultSorter;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.editors.main.composite.direct.QueryResultLabelProvider;
import com.hangum.tadpole.rdb.core.editors.main.composite.tail.PlanTailComposite;

/**
 * General plan composite
 * 
 * @author hangum
 *
 */
public class GeneralPlanComposite extends AbstractPlanComposite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(GeneralPlanComposite.class);
	
	private TableViewer tvQueryPlan;
	private SQLResultSorter sqlSorter;
	

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public GeneralPlanComposite(Composite parent, int style, QueryExecuteResultDTO rsDAO) {
		super(parent, style, rsDAO);
		setLayout(new GridLayout(1, false));
		Composite compositeBody = new Composite(this, SWT.NONE);
		GridLayout gl_compositeHead = new GridLayout(2, false);
		gl_compositeHead.verticalSpacing = 2;
		gl_compositeHead.horizontalSpacing = 2;
		gl_compositeHead.marginHeight = 0;
		gl_compositeHead.marginWidth = 2;
		compositeBody.setLayout(gl_compositeHead);
		
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));
		
		tvQueryPlan = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tvQueryPlan.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeBtn = new Composite(compositeBody, SWT.NONE);
		compositeBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		GridLayout gl_compositeBtn = new GridLayout(4, false);
		gl_compositeBtn.verticalSpacing = 2;
		gl_compositeBtn.horizontalSpacing = 2;
		gl_compositeBtn.marginWidth = 0;
		gl_compositeBtn.marginHeight = 2;
		compositeBtn.setLayout(gl_compositeBtn);
		
		compositeTail = new PlanTailComposite(this, compositeBtn, SWT.NONE);
		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 2;
		gl_compositeResult.horizontalSpacing = 2;
		gl_compositeResult.marginHeight = 0;
		gl_compositeResult.marginWidth = 2;
		compositeTail.setLayout(gl_compositeResult);
	}

	/**
	 * 
	 */
	public void setQueryPlanData(RequestQuery reqQuery, QueryExecuteResultDTO rsDAO) {
		this.reqQuery = reqQuery;
		this.rsDAO = rsDAO;
		
		// table data를 생성한다.
		final TadpoleResultSet trs = rsDAO.getDataList();
		sqlSorter = new SQLResultSorter(-999);
		
		boolean isEditable = true;
		if("".equals(rsDAO.getColumnTableName().get(1))) isEditable = false; //$NON-NLS-1$
		TableUtil.createTableColumn(tvQueryPlan, rsDAO, sqlSorter);
		
		tvQueryPlan.setLabelProvider(new QueryResultLabelProvider(reqQuery.getMode(), rsDAO));
		tvQueryPlan.setContentProvider(ArrayContentProvider.getInstance());
		
		// 쿼리를 설정한 사용자가 설정 한 만큼 보여준다.
		if(trs.getData().size() > GetPreferenceGeneral.getPageCount()) {
			tvQueryPlan.setInput(trs.getData().subList(0, GetPreferenceGeneral.getPageCount()));	
		} else {
			tvQueryPlan.setInput(trs.getData());
		}
		tvQueryPlan.setSorter(sqlSorter);
		
		compositeTail.execute(getTailResultMsg());
	
		// Pack the columns
		TableUtil.packTable(tvQueryPlan.getTable());
	}
	
}
