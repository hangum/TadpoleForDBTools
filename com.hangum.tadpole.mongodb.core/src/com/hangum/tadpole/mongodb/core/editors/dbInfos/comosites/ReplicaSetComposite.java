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
import com.mongodb.CommandResult;
import com.mongodb.DBObject;

/**
 * Replica Set Information
 * 
 * @author hangum
 *
 */
public class ReplicaSetComposite extends Composite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ReplicaSetComposite.class);

	private UserDBDAO userDB;
	private CommandResult commandResult;
	
	private FindOneDetailComposite compositeReplicaSet;
	private FindOneDetailComposite compositeReplicatOperation;
	private FindOneDetailComposite compositeReplicationNetQueue;
	
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ReplicaSetComposite(Composite parent, int style, UserDBDAO userDB, CommandResult cr) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		setLayout(gridLayout);
		
		this.userDB = userDB;
		this.commandResult = cr;
		
		Composite compositeToolbar = new Composite(this, SWT.NONE);
		compositeToolbar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeToolbar = new GridLayout(1, false);
		gl_compositeToolbar.verticalSpacing = 1;
		gl_compositeToolbar.horizontalSpacing = 1;
		gl_compositeToolbar.marginHeight = 1;
		gl_compositeToolbar.marginWidth = 1;
		compositeToolbar.setLayout(gl_compositeToolbar);
		
		ToolBar toolBar = new ToolBar(compositeToolbar, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.setImage(GlobalImageUtils.getRefresh());
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initData();
			}
		});
		tltmRefresh.setToolTipText(Messages.get().Refresh);

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
		grpReplicaSet.setText("Replica Set Information");
		
		compositeReplicaSet = new FindOneDetailComposite(grpReplicaSet, "Replica Set", (DBObject)commandResult.get("repl"), false);
		compositeReplicaSet.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeReplicaSet.setLayout(new GridLayout(1, false));
		
		compositeReplicatOperation = new FindOneDetailComposite(grpReplicaSet, "Replication operations", (DBObject)commandResult.get("opcountersRepl"), false);
		compositeReplicatOperation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeReplicatOperation.setLayout(new GridLayout(1, false));
		
		compositeReplicationNetQueue = new FindOneDetailComposite(grpReplicaSet, "Replication Network Queue", (DBObject)commandResult.get("replNetworkQueue"), false);
		compositeReplicationNetQueue.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeReplicationNetQueue.setLayout(new GridLayout(1, false));
	}
	
	/**
	 * data refresh
	 */
	private void initData() {
		try {
			commandResult = MongoDBQuery.serverStatusCommandResult(userDB);
		} catch (Exception e1) {
			logger.error("Get status command", e1);
		}
		
		compositeReplicaSet.refresh("Replica Set", (DBObject)commandResult.get("repl"), false);
		compositeReplicatOperation.refresh("Replication operations", (DBObject)commandResult.get("opcountersRepl"), false);
		compositeReplicationNetQueue.refresh("Replication Network Queue", (DBObject)commandResult.get("replNetworkQueue"), false);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
