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
package com.hangum.tadpole.rdb.core.editors.main.composite.tail;

import org.apache.log4j.Logger;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.SQL_STATEMENT_TYPE;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.hangum.tadpole.rdb.core.editors.main.composite.resultdetail.AbstractResultDetailComposite;

/**
 * 결과 화면의 다운로드 부분과 결과 상태를  컴포짖
 * 
 * @author hangum
 *
 */
public class ResultTailComposite extends AbstractTailComposite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(ResultTailComposite.class);
	
	private AbstractResultDetailComposite abstractResultComp;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param compositeBtn 
	 * @param style
	 * @param isMakePing 
	 */
	public ResultTailComposite(UserDBDAO userDB, Composite reqAbstractResult, Composite compositeBtn, int style, boolean isViewDownloadBtn) {
		super(userDB, compositeBtn, style, isViewDownloadBtn);
		setLayout(new GridLayout(1, false));
		
		abstractResultComp = (AbstractResultDetailComposite)reqAbstractResult;
	}
	
	/**
	 * get query result dto
	 * @return
	 */
	@Override
	public QueryExecuteResultDTO getRSDao() {
		return abstractResultComp.getRsDAO();
	}
	
	@Override
	public String getSQL() {
		if(abstractResultComp.getReqQuery() != null) {
			RequestQuery requestQuery = abstractResultComp.getReqQuery();
			
			// prepared statement 일 경우는 인자도 넣어준다.
			StringBuffer sbParameter = new StringBuffer();
			if(requestQuery.getSqlStatementType() == SQL_STATEMENT_TYPE.PREPARED_STATEMENT) {
				sbParameter.append("/* Parameter is ");
				for (int i=0; i<requestQuery.getStatementParameter().length; i++) {
					Object objParam = requestQuery.getStatementParameter()[i];
					sbParameter.append(String.format("[ %d = %s ]", i, ""+objParam));
				}
				sbParameter.append("*/" + PublicTadpoleDefine.LINE_SEPARATOR);
				sbParameter.append(requestQuery.getSql());
				
				return sbParameter.toString();
			} else {
				return requestQuery.getSql();
			}
		}
		else return "";
	}

	@Override
	public RequestQuery getRequestQuery() {
		return abstractResultComp.getReqQuery();
	}
}
