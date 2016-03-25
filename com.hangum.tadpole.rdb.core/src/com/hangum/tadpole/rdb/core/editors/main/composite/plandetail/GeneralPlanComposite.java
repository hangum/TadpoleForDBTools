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

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.util.NumberFormatUtils;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultSorter;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.composite.direct.SQLResultLabelProvider;
import com.hangum.tadpole.rdb.core.editors.main.composite.tail.PlanTailComposite;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;

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
	public GeneralPlanComposite(Composite parent, int style) {
		super(parent, style);
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
		
//		Composite compositeTail = new Composite(this, SWT.NONE);
//		compositeTail.setLayout(new GridLayout(1, false));
//		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
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

	public void setQueryPlanData(RequestQuery reqQuery, QueryExecuteResultDTO rsDAO) {
		this.reqQuery = reqQuery;
		this.rsDAO = rsDAO;
		
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
		
		compositeTail.execute(getTailResultMsg());
	
		// Pack the columns
		TableUtil.packTable(tvQueryPlan.getTable());
	}
	
	/**
	 * 쿼리 결과 메시지를 출력합니다. 
	 * 
	 * @return
	 */
	public String getTailResultMsg() {
		if(getRsDAO() == null) return "";
		final TadpoleResultSet trs = getRsDAO().getDataList();
		final RequestResultDAO rsDAO = getReqQuery().getResultDao();
		
		// 메시지를 출력합니다.
		float longExecuteTime = (rsDAO.getEndDateExecute().getTime() - rsDAO.getStartDateExecute().getTime()) / 1000f;
		String strResultMsg = ""; //$NON-NLS-1$
		if(trs.isEndOfRead()) {
			strResultMsg = String.format("%s %s (%s %s)", NumberFormatUtils.commaFormat(trs.getData().size()), Messages.get().Rows, longExecuteTime, Messages.get().Sec); //$NON-NLS-1$
		} else {
			if( (trs.getData().size() % GetPreferenceGeneral.getSelectLimitCount()) == 0) {
				// 데이터가 한계가 넘어 갔습니다.
				String strMsg = String.format(Messages.get().MainEditor_34, NumberFormatUtils.commaFormat(trs.getData().size()));
				strResultMsg = String.format("%s (%s %s)", strMsg, longExecuteTime, Messages.get().Sec); //$NON-NLS-1$
			} else { 
				strResultMsg = String.format("%s %s (%s %s)", NumberFormatUtils.commaFormat(trs.getData().size()), Messages.get().Rows, longExecuteTime, Messages.get().Sec); //$NON-NLS-1$
			}
		}
		
		return strResultMsg;
	}
	

	public PlanTailComposite getCompositeTail() {
		return compositeTail;
	}
}
