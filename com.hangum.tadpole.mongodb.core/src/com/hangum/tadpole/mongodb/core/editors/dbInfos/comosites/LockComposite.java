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
 * Server status (lock) information
 * 
 * @author hangum
 *
 */
public class LockComposite extends Composite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(LockComposite.class);

	private UserDBDAO userDB;
	private CommandResult commandResult;
	
	private FindOneDetailComposite compositeLocalLocks;
	private FindOneDetailComposite compositeGlobalLocks;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LockComposite(Composite parent, int style, UserDBDAO userDB, CommandResult cr) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		setLayout(gridLayout);
		
		this.userDB = userDB;
		this.commandResult = cr;

		Composite compositeServerStatus = new Composite(this, SWT.NONE);
		compositeServerStatus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeServerStatus = new GridLayout(1, false);
		gl_compositeServerStatus.verticalSpacing = 1;
		gl_compositeServerStatus.horizontalSpacing = 1;
		gl_compositeServerStatus.marginHeight = 1;
		gl_compositeServerStatus.marginWidth = 1;
		compositeServerStatus.setLayout(gl_compositeServerStatus);
		
		Composite compositeToolbar = new Composite(compositeServerStatus, SWT.NONE);
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
		
		Group grpReplicaSet = new Group(compositeServerStatus, SWT.NONE);
		GridLayout gl_grpReplicaSet = new GridLayout(1, false);
		gl_grpReplicaSet.verticalSpacing = 0;
		gl_grpReplicaSet.horizontalSpacing = 0;
		gl_grpReplicaSet.marginHeight = 0;
		gl_grpReplicaSet.marginWidth = 0;
		grpReplicaSet.setLayout(gl_grpReplicaSet);
		grpReplicaSet.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpReplicaSet.setText("Locks Information");
		
		compositeLocalLocks = new FindOneDetailComposite(grpReplicaSet, "Local Locks", (DBObject)commandResult.get("locks"), false);
		compositeLocalLocks.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeLocalLocks = new GridLayout(1, false);
		gl_compositeLocalLocks.verticalSpacing = 2;
		gl_compositeLocalLocks.horizontalSpacing = 2;
		gl_compositeLocalLocks.marginHeight = 2;
		gl_compositeLocalLocks.marginWidth = 2;
		compositeLocalLocks.setLayout(gl_compositeLocalLocks);
		
		compositeGlobalLocks = new FindOneDetailComposite(grpReplicaSet, "Global Locks", (DBObject)commandResult.get("globalLock"), false);
		compositeGlobalLocks.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeGlobalLocks = new GridLayout(1, false);
		gl_compositeGlobalLocks.verticalSpacing = 2;
		gl_compositeGlobalLocks.horizontalSpacing = 2;
		gl_compositeGlobalLocks.marginHeight = 2;
		gl_compositeGlobalLocks.marginWidth = 2;
		compositeGlobalLocks.setLayout(gl_compositeGlobalLocks);
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
		
		compositeLocalLocks.refresh("Local Locks", (DBObject)commandResult.get("locks"), false);
		compositeGlobalLocks.refresh("Global Locks", (DBObject)commandResult.get("globalLock"), false);
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
