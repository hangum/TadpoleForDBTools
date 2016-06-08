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

import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.rdb.core.editors.main.composite.resultdetail.AbstractResultDetailComposite;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;

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
	public ResultTailComposite(Composite reqAbstractResult, Composite compositeBtn, int style) {
		super(compositeBtn, style);
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
		return abstractResultComp.getReqQuery().getSql();
	}

	@Override
	public RequestQuery getRequestQuery() {
		return abstractResultComp.getReqQuery();
	}
}
