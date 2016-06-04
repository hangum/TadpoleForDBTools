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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.util.NumberFormatUtils;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.composite.ResultSetComposite;
import com.hangum.tadpole.rdb.core.editors.main.composite.tail.ResultTailComposite;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;

/**
 * abstract result detail composite
 * 
 * @author hangum
 *
 */
public abstract class AbstractResultDetailComposite extends Composite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(AbstractResultDetailComposite.class);
	public enum RESULT_COMP_TYPE {Table, Text, JSON};
	
	protected RequestQuery reqQuery;
	protected QueryExecuteResultDTO rsDAO;
	
	protected Event eventTableSelect;
	
	/** result composite */
	protected ResultSetComposite rdbResultComposite;
	
	/** tail composite */
	protected ResultTailComposite compositeTail;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AbstractResultDetailComposite(Composite parent, int style, ResultSetComposite rdbResultComposite) {
		super(parent, style);
		this.rdbResultComposite = rdbResultComposite;
	}
	
	/**
	 * initialize UI 
	 */
	public abstract void initUI();
	
	/** implements result type */
	public abstract RESULT_COMP_TYPE getResultType();
	
	/**
	 * print ui
	 * @param rsDAO 
	 * @param reqQuery 
	 * @param isMakePing 
	 */
	public void printUI(RequestQuery reqQuery, QueryExecuteResultDTO rsDAO, boolean isMakePing) {
		this.reqQuery = reqQuery;
		this.rsDAO = rsDAO;
		
		compositeTail.setBtnPint(isMakePing);
	}
	
	public void endQuery() {
		eventTableSelect = null;
	}
	
//	/**
//	 * get last data
//	 */
//	public void getLastData() {
//		// 데이터를 마지막 까지 가져왔는지 비교합니다. // 나머지 데이터를 가져온다.
//		final int intSelectLimitCnt = GetPreferenceGeneral.getSelectLimitCount();
//		final String strUserEmail 	= SessionManager.getEMAIL();
//		final int queryTimeOut 		= GetPreferenceGeneral.getQueryTimeOut();
//		
//		try {
//			final TadpoleResultSet oldTadpoleResultSet = getRsDAO().getDataList();
//			boolean isContinue = true;
//			while(isContinue) {
//				QueryExecuteResultDTO newRsDAO = getRdbResultComposite().runSelect(reqQuery, queryTimeOut, strUserEmail, intSelectLimitCnt, oldTadpoleResultSet.getData().size());
//				if(newRsDAO.getDataList().getData().isEmpty()) isContinue = false;
//				oldTadpoleResultSet.getData().addAll(newRsDAO.getDataList().getData());
//			}
//		
//		} catch(Exception e) {
//			logger.error("continue result set", e);
//		}
//
//	}
	
	/**
	 * 쿼리 결과 메시지를 출력합니다. 
	 * 
	 * @return
	 */
	public String getTailResultMsg() {
		if(getRsDAO() == null) return "";
		final TadpoleResultSet trs = getRsDAO().getDataList();
		
		// 메시지를 출력합니다.
		float longExecuteTime = (getReqResultDAO().getEndDateExecute().getTime() - getReqResultDAO().getStartDateExecute().getTime()) / 1000f;
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
	
	/**
	 * get rdb result composite
	 * @return
	 */
	public ResultSetComposite getRdbResultComposite() {
		return rdbResultComposite;
	}
	
	public ResultTailComposite getCompositeTail() {
		return compositeTail;
	}
	
	/**
	 * @return the reqQuery
	 */
	public RequestQuery getReqQuery() {
		return reqQuery;
	}

	/**
	 * @return the rsDAO
	 */
	public QueryExecuteResultDTO getRsDAO() {
		return rsDAO;
	}

	/**
	 * @return the reqResultDAO
	 */
	public RequestResultDAO getReqResultDAO() {
		return reqQuery.getResultDao();
	}

	@Override
	protected void checkSubclass() {
	}

}
