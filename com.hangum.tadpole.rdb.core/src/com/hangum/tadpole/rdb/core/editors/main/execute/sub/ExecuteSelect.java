/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main.execute.sub;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;

/**
 * execute sql 
 * 
 * @author hangum
 *
 */
public class ExecuteSelect {
	private static final Logger logger = Logger.getLogger(ExecuteSelect.class);
	private static ExecuteSelect executeSelect = new ExecuteSelect();
	private ExecutorService execService = Executors.newCachedThreadPool();
	
	private ExecuteSelect() {
	}
	
	public static ExecuteSelect getInstance() {
		return executeSelect;
	}
	
	/**
	 * select문을 실행합니다.
	 * 
	 * @param requestQuery
	 */
	public ResultSet runSQLSelect(
			final Statement statement,
			final RequestQuery reqQuery
	) throws Exception {
		
		Future<ResultSet> queryFuture = execService.submit(new Callable<ResultSet>() {
			@Override
			public ResultSet call() throws Exception {
				statement.execute(reqQuery.getSql());
				return statement.getResultSet();
			}
		});
		
		return queryFuture.get();
	}
	
	/**
	 * shutdown
	 */
	public void shutdown() {
		execService.shutdown();
	}
}
