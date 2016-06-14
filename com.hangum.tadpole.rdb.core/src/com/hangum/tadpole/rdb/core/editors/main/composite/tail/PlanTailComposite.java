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
import com.hangum.tadpole.rdb.core.editors.main.composite.plandetail.GeneralPlanComposite;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;

/**
 * plan 의 결과 정보를보여주는 컴포짖
 * 
 * @author hangum
 *
 */
public class PlanTailComposite extends AbstractTailComposite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(PlanTailComposite.class);
	private GeneralPlanComposite abstractResultComp;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param compositeBtn 
	 * @param style
	 */
	public PlanTailComposite(Composite reqAbstractResult, Composite compositeBtn, int style) {
		super(compositeBtn, style);
		setLayout(new GridLayout(1, false));
		
		abstractResultComp = (GeneralPlanComposite)reqAbstractResult;
	}

	/**
	 * get query result dto
	 * @return
	 */
	public QueryExecuteResultDTO getRSDao() {
		return abstractResultComp.getRsDAO();
	}

	@Override
	public String getSQL() {
		if(abstractResultComp.getReqQuery() != null) return abstractResultComp.getReqQuery().getSql();
		else return "";
	}
	
	@Override
	public RequestQuery getRequestQuery() {
		return abstractResultComp.getReqQuery();
	}
}
