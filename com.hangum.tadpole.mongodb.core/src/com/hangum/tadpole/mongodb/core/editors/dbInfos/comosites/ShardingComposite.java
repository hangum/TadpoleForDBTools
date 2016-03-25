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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
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
	
	private FindOneDetailComposite compositeShardList;

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
		
		Composite composite = new Composite(compositeServerStatus, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 1;
		gl_composite.horizontalSpacing = 1;
		gl_composite.marginHeight = 1;
		gl_composite.marginWidth = 1;
		composite.setLayout(gl_composite);
		
		ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolBar.setBounds(0, 0, 87, 20);
		
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.setImage(GlobalImageUtils.getRefresh());
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initData();
			}
		});
		tltmRefresh.setToolTipText(Messages.get().Refresh);
		
		Group grpReplicaSet = new Group(compositeServerStatus, SWT.NONE);
		GridLayout gl_grpReplicaSet = new GridLayout(1, false);
		gl_grpReplicaSet.verticalSpacing = 0;
		gl_grpReplicaSet.horizontalSpacing = 0;
		gl_grpReplicaSet.marginHeight = 0;
		gl_grpReplicaSet.marginWidth = 0;
		grpReplicaSet.setLayout(gl_grpReplicaSet);
		grpReplicaSet.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpReplicaSet.setText(Messages.get().ShardingComposite_0);
		
		try {
			CommandResult res = MongoDBQuery.getAdminMongoDB(userDB).command("listShards"); //$NON-NLS-1$
			shards = (BasicDBList)res.get("shards"); //$NON-NLS-1$
		} catch(Exception e) {
			logger.error("listShards", e); //$NON-NLS-1$
		}
	    
		compositeShardList = new FindOneDetailComposite(grpReplicaSet, Messages.get().ShardingComposite_4, shards, false);
		compositeShardList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeLocalLocks = new GridLayout(1, false);
		gl_compositeLocalLocks.verticalSpacing = 2;
		gl_compositeLocalLocks.horizontalSpacing = 2;
		gl_compositeLocalLocks.marginHeight = 2;
		gl_compositeLocalLocks.marginWidth = 2;
		compositeShardList.setLayout(gl_compositeLocalLocks);
	}
	
	/**
	 * data refresh
	 */
	private void initData() {
		try {
			CommandResult res = MongoDBQuery.getAdminMongoDB(userDB).command("listShards"); //$NON-NLS-1$
			shards = (BasicDBList)res.get("shards"); //$NON-NLS-1$
		} catch(Exception e) {
			logger.error("listShards", e); //$NON-NLS-1$
		}
		
		compositeShardList.refresh(Messages.get().ShardingComposite_4, shards, false);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
