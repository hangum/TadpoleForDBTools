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
package com.hangum.tadpole.rdb.core.editors.main.composite.plan;

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
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.editors.main.composite.ResultMainComposite;
import com.hangum.tadpole.rdb.core.editors.main.composite.direct.SQLResultLabelProvider;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;

/**
 * General plan composite
 * 
 * @author hangum
 *
 */
public class GeneralPlanComposite extends Composite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(GeneralPlanComposite.class);
	private ResultMainComposite resultMainComposite;
	private TableViewer tvQueryPlan;
	private SQLResultSorter sqlSorter;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public GeneralPlanComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
//		Composite compositeHead = new Composite(this, SWT.NONE);
//		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		compositeHead.setLayout(new GridLayout(1, false));
		
		Composite compositeBody = new Composite(this, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));
		
		tvQueryPlan = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tvQueryPlan.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
//		Composite compositeTail = new Composite(this, SWT.NONE);
//		compositeTail.setLayout(new GridLayout(1, false));
//		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}

	@Override
	protected void checkSubclass() {
	}

	/**
	 * setting parent composite
	 * 
	 * @param resultMainComposite
	 */
	public void setRDBResultComposite(ResultMainComposite resultMainComposite) {
		this.resultMainComposite = resultMainComposite;
	}

	/**
	 * set query plan data
	 * 
	 * @param reqQuery
	 * @param rsDAO
	 */
	public void setQueryPlanData(RequestQuery reqQuery, QueryExecuteResultDTO rsDAO) {
		// table data를 생성한다.
		final TadpoleResultSet trs = rsDAO.getDataList();
		sqlSorter = new SQLResultSorter(-999);
		
		boolean isEditable = true;
		if("".equals(rsDAO.getColumnTableName().get(1))) isEditable = false; //$NON-NLS-1$
		SQLResultLabelProvider.createTableColumn(reqQuery, tvQueryPlan, rsDAO, sqlSorter, isEditable);
		
		tvQueryPlan.setLabelProvider(new SQLResultLabelProvider(reqQuery.getMode(), GetPreferenceGeneral.getISRDBNumberIsComma(), rsDAO));
		tvQueryPlan.setContentProvider(new ArrayContentProvider());
		
		// 쿼리를 설정한 사용자가 설정 한 만큼 보여준다.
		if(trs.getData().size() > GetPreferenceGeneral.getPageCount()) {
			tvQueryPlan.setInput(trs.getData().subList(0, GetPreferenceGeneral.getPageCount()));	
		} else {
			tvQueryPlan.setInput(trs.getData());
		}
		tvQueryPlan.setSorter(sqlSorter);
	
		// Pack the columns
		TableUtil.packTable(tvQueryPlan.getTable());
	}
}
