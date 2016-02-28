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

import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.rdb.core.editors.main.composite.ResultMainComposite;
import com.hangum.tadpole.rdb.core.editors.main.composite.tail.PlanTailComposite;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;

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
	
	public AbstractPlanComposite(Composite parent, int style) {
		super(parent, style);
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

	@Override
	protected void checkSubclass() {
	}
}
