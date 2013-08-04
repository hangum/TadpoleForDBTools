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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.hangum.tadpole.mongodb.core.dialogs.resultview.FindOneDetailComposite;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;

/**
 * Server status (lock) information
 * 
 * @author hangum
 *
 */
public class LockComposite extends Composite {
	private CommandResult commandResult;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LockComposite(Composite parent, int style, CommandResult cr) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		setLayout(gridLayout);
		
		this.commandResult = cr;

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
		grpReplicaSet.setText("Locks Information");
		
		Composite compositeLocalLocks = new FindOneDetailComposite(grpReplicaSet, "Local Locks", (DBObject)commandResult.get("locks"), false);
		compositeLocalLocks.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeLocalLocks = new GridLayout(1, false);
		gl_compositeLocalLocks.verticalSpacing = 2;
		gl_compositeLocalLocks.horizontalSpacing = 2;
		gl_compositeLocalLocks.marginHeight = 2;
		gl_compositeLocalLocks.marginWidth = 2;
		compositeLocalLocks.setLayout(gl_compositeLocalLocks);
		
		Composite compositeGlobalLocks = new FindOneDetailComposite(grpReplicaSet, "Global Locks", (DBObject)commandResult.get("globalLock"), false);
		compositeGlobalLocks.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeGlobalLocks = new GridLayout(1, false);
		gl_compositeGlobalLocks.verticalSpacing = 2;
		gl_compositeGlobalLocks.horizontalSpacing = 2;
		gl_compositeGlobalLocks.marginHeight = 2;
		gl_compositeGlobalLocks.marginWidth = 2;
		compositeGlobalLocks.setLayout(gl_compositeGlobalLocks);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
