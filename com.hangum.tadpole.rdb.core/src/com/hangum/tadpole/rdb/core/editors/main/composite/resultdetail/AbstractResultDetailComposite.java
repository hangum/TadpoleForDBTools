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
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.rdb.core.editors.main.composite.ResultMainComposite;
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
	
	protected Event eventTableSelect;
	
	/** result composite */
	protected ResultMainComposite rdbResultComposite;
	protected RequestQuery reqQuery;
	protected QueryExecuteResultDTO rsDAO;
	protected RequestResultDAO reqResultDAO;
	
	/** tail composite */
	protected ResultTailComposite compositeTail;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AbstractResultDetailComposite(Composite parent, int style, ResultMainComposite rdbResultComposite) {
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
	 * 
	 * @param reqQuery
	 * @param rsDAO 
	 * @param executingSQLDAO 
	 */
	public void printUI(RequestQuery reqQuery, QueryExecuteResultDTO rsDAO, RequestResultDAO reqResultDAO) {
		this.reqQuery = reqQuery;
		this.rsDAO = rsDAO;
		this.reqResultDAO = reqResultDAO;
	}
	
	public void endQuery() {
		eventTableSelect = null;
	}
	
	/**
	 * get rdb result composite
	 * @return
	 */
	public ResultMainComposite getRdbResultComposite() {
		return rdbResultComposite;
	}
	
	public ResultTailComposite getCompositeTail() {
		return compositeTail;
	}
	
	@Override
	protected void checkSubclass() {
	}

}
