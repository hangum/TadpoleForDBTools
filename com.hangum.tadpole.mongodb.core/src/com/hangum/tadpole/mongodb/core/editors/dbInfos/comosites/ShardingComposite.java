/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.mongodb.core.editors.dbInfos.comosites;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.Messages;
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
		grpReplicaSet.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpReplicaSet.setText(Messages.ShardingComposite_0);
		
		try {
			CommandResult res = MongoDBQuery.getAdminMongoDB(userDB).command("listShards"); //$NON-NLS-1$
			shards = (BasicDBList)res.get("shards"); //$NON-NLS-1$
		} catch(Exception e) {
			logger.error("listShards", e); //$NON-NLS-1$
		}
	    
		Composite compositeLocalLocks = new FindOneDetailComposite(grpReplicaSet, Messages.ShardingComposite_4, shards, false);
		compositeLocalLocks.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeLocalLocks = new GridLayout(1, false);
		gl_compositeLocalLocks.verticalSpacing = 2;
		gl_compositeLocalLocks.horizontalSpacing = 2;
		gl_compositeLocalLocks.marginHeight = 2;
		gl_compositeLocalLocks.marginWidth = 2;
		compositeLocalLocks.setLayout(gl_compositeLocalLocks);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
