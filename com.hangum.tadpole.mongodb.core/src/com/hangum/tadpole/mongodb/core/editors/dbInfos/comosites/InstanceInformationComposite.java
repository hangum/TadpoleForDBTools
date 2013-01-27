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

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.mongodb.core.dialogs.resultview.FindOneDetailComposite;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;

/**
 * Server Status (Instance Information) composite
 * 
 * @author hangum
 *
 */
public class InstanceInformationComposite extends Composite {
	private CommandResult commandResult;
	
	private Text textHost;
	private Text textVersion;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public InstanceInformationComposite(Composite parent, int style, CommandResult cr) {
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
		
		Composite compositeInstance = new Composite(compositeServerStatus, SWT.NONE);
		compositeInstance.setLayout(new GridLayout(2, false));
		compositeInstance.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Label lblHost = new Label(compositeInstance, SWT.NONE);
		lblHost.setText("Host");
		
		textHost = new Text(compositeInstance, SWT.BORDER);
		textHost.setEditable(false);
		textHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblVersion = new Label(compositeInstance, SWT.NONE);
		lblVersion.setText("Version");
		
		textVersion = new Text(compositeInstance, SWT.BORDER);
		textVersion.setEditable(false);
		textVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpMemory = new Group(compositeServerStatus, SWT.NONE);
		grpMemory.setLayout(new GridLayout(2, false));
		grpMemory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpMemory.setText("System Information");
		
		Composite compositeMemoryUse = new FindOneDetailComposite(grpMemory, "Memory", (DBObject)commandResult.get("mem"), false);
		compositeMemoryUse.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeMemoryUse.setLayout(new GridLayout(1, false));
		
		Composite compositeNetwork = new FindOneDetailComposite(grpMemory, "Network", (DBObject)commandResult.get("network"), false);
		compositeNetwork.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeNetwork.setLayout(new GridLayout(1, false));
		
		Group grpConnections = new Group(compositeServerStatus, SWT.NONE);
		grpConnections.setLayout(new GridLayout(2, false));
		grpConnections.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		grpConnections.setText("Connections");
		
		Composite compositeConnection = new FindOneDetailComposite(grpConnections, "Connections", (DBObject)commandResult.get("connections"), false);
		compositeConnection.setLayout(new GridLayout(1, false));
		compositeConnection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeCursor = new FindOneDetailComposite(grpConnections, "Cursors", (DBObject)commandResult.get("cursors"), false);
		compositeCursor.setLayout(new GridLayout(1, false));
		compositeCursor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpExtraInfo = new Group(compositeServerStatus, SWT.NONE);
		grpExtraInfo.setLayout(new GridLayout(1, false));
		grpExtraInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpExtraInfo.setText("Extra Info");
		
		Composite compositeExtraInfo = new FindOneDetailComposite(grpExtraInfo, "ExtraInfo", (DBObject)commandResult.get("extra_info"), false);
		compositeExtraInfo.setLayout(new GridLayout(1, false));
		compositeExtraInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
	}
	
	public void initData() {
		String strHost 	= StringUtils.trimToEmpty(commandResult.getString("host"));
		String version 	= StringUtils.trimToEmpty(commandResult.getString("version"));
//		String process 	= commandResult.getString("process");
//		String pid 		= commandResult.getString("pid");
//		String uptime 	= commandResult.getString("uptime");
//		String uptimeMillis = commandResult.getString("uptimeMillis");
//		String uptimeEstimate 	= commandResult.getString("uptimeEstimate");
//		String localTime 		= commandResult.getString("localTime");
		
		textHost.setText(strHost);
		textVersion.setText(version);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
