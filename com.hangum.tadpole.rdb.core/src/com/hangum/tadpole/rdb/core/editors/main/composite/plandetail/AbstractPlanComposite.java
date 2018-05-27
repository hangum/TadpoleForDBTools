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
package com.hangum.tadpole.rdb.core.editors.main.composite.plandetail;

import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.util.NumberFormatUtils;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.composite.ResultMainComposite;
import com.hangum.tadpole.rdb.core.editors.main.composite.tail.PlanTailComposite;

/**
 * Abstract plan composite
 * 
 * @author hangum
 *
 */
public abstract class AbstractPlanComposite extends Composite {
	
	/**
	 * result main composite
	 */
	protected ResultMainComposite resultMainComposite;
	/**
	 * set query plan data
	 * 
	 * @param reqQuery
	 * @param rsDAO
	 */
	protected RequestQuery reqQuery;
	protected QueryExecuteResultDTO rsDAO;
	
	/** tail composite */
	protected PlanTailComposite compositeTail;
	
	public AbstractPlanComposite(Composite parent, int style, QueryExecuteResultDTO rsDAO) {
		super(parent, style);
		this.rsDAO = rsDAO;
	}

	/**
	 * setting parent composite
	 * 
	 * @param resultMainComposite
	 */
	public void setRDBResultComposite(ResultMainComposite resultMainComposite) {
		this.resultMainComposite = resultMainComposite;
	}
	
	public ResultMainComposite getResultMainComposite() {
		return resultMainComposite;
	}
	
	public RequestQuery getReqQuery() {
		return reqQuery;
	}
	public QueryExecuteResultDTO getRsDAO() {
		return rsDAO;
	}
	
	public abstract void setQueryPlanData(RequestQuery reqQuery, QueryExecuteResultDTO rsDAO);

	/**
	 * 쿼리 결과 메시지를 출력합니다. 
	 * 
	 * @return
	 */
	public String getTailResultMsg() {
		if(getRsDAO() == null) return "";
		final TadpoleResultSet trs = getRsDAO().getDataList();
		final RequestResultDAO rsDAO = getReqQuery().getRequestResultDao();
		
		// 메시지를 출력합니다.
		float longExecuteTime = (rsDAO.getEndDateExecute().getTime() - rsDAO.getStartDateExecute().getTime()) / 1000f;
		String strResultMsg = ""; //$NON-NLS-1$
		if(trs.isEndOfRead()) {
			strResultMsg = String.format("%s %s (%s %s)", NumberFormatUtils.addComma(trs.getData().size()), Messages.get().Rows, longExecuteTime, Messages.get().Sec); //$NON-NLS-1$
		} else {
			if( (trs.getData().size() % GetPreferenceGeneral.getSelectLimitCount()) == 0) {
				// 데이터가 한계가 넘어 갔습니다.
				String strMsg = String.format(Messages.get().MainEditor_34, NumberFormatUtils.addComma(trs.getData().size()));
				strResultMsg = String.format("%s (%s %s)", strMsg, longExecuteTime, Messages.get().Sec); //$NON-NLS-1$
			} else { 
				strResultMsg = String.format("%s %s (%s %s)", NumberFormatUtils.addComma(trs.getData().size()), Messages.get().Rows, longExecuteTime, Messages.get().Sec); //$NON-NLS-1$
			}
		}
		
		return strResultMsg;
	}
	
	public PlanTailComposite getCompositeTail() {
		return compositeTail;
	}

	@Override
	protected void checkSubclass() {
	}
}
