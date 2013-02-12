/*******************************************************************************
 * Copyright (c) 2013 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.mongodb.core.editors.dbInfos.comosites;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.dialogs.resultview.FindOneDetailComposite;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.mongodb.BasicDBList;
import com.mongodb.CommandResult;

/**
 * MongoDB Shard info composite
 * 
 * @author hangum
 *
 */
public class ShardingComposite extends Composite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ShardingComposite.class);
	
	private UserDBDAO userDB;
	private BasicDBList shards;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ShardingComposite(Composite parent, int style, UserDBDAO userDB) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		setLayout(gridLayout);
		
		this.userDB = userDB;

		Composite compositeServerStatus = new Composite(this, SWT.NONE);
		compositeServerStatus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeServerStatus = new GridLayout(1, false);
		gl_compositeServerStatus.verticalSpacing = 1;
		gl_compositeServerStatus.horizontalSpacing = 1;
		gl_compositeServerStatus.marginHeight = 1;
		gl_compositeServerStatus.marginWidth = 1;
		compositeServerStatus.setLayout(gl_compositeServerStatus);
		
		Group grpReplicaSet = new Group(compositeServerStatus, SWT.NONE);
		GridLayout gl_grpReplicaSet = new GridLayout(1, false);
		gl_grpReplicaSet.verticalSpacing = 0;
		gl_grpReplicaSet.horizontalSpacing = 0;
		gl_grpReplicaSet.marginHeight = 0;
		gl_grpReplicaSet.marginWidth = 0;
		grpReplicaSet.setLayout(gl_grpReplicaSet);
		grpReplicaSet.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpReplicaSet.setText("Sharding Information");
		
		try {
			CommandResult res = MongoDBQuery.getAdminMongoDB(userDB).command("listShards");
			shards = (BasicDBList)res.get("shards");
		} catch(Exception e) {
			logger.error("listShards", e);
		}
	    
		Composite compositeLocalLocks = new FindOneDetailComposite(grpReplicaSet, "List Shards", shards, false);
		compositeLocalLocks.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeLocalLocks.setLayout(new GridLayout(1, false));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
