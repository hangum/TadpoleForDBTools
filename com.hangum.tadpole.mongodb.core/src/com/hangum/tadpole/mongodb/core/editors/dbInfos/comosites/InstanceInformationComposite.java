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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.rap.addons.chart.basic.BarChart;
import org.eclipse.rap.addons.chart.basic.DataItem;
import org.eclipse.rap.addons.chart.basic.PieChart;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.util.ColorsSWTUtils;
import com.hangum.tadpole.commons.util.DateUtil;
import com.hangum.tadpole.commons.util.ENumberUtils;
import com.hangum.tadpole.commons.util.NumberFormatUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.dialogs.resultview.FindOneDetailComposite;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;

/**
 * Server Status (Instance Information) composite
 * 
 * @author hangum
 *
 */
public class InstanceInformationComposite extends Composite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(InstanceInformationComposite.class);
	
	/** server push session */
	final ServerPushSession spsInstance = new ServerPushSession();
	private boolean isUIThreadRunning = false;
	
	/** main composite */
	private Composite compositeServerStatus;
	private Button btnStart;
	private Button btnStop;
	
	/** userDB data */
	private UserDBDAO userDB;
	
	/** System information */
	private Text textHost;
	private Text textVersion;
	private Text textProcess;
	private Text textPID;
	private Text textUptime;
	private Text textUptimeMillis;
	private Text textUpTimeEstimate;
	private Text textLocalTime;
	
	private BarChart barChartMemory;
	private BarChart barChartNetwork;
	
	private BarChart barChartConnection;
	private PieChart pieChartCursors;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public InstanceInformationComposite(Composite parent, int style, UserDBDAO userDB, CommandResult commandResult) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		setLayout(gridLayout);
		
		this.userDB = userDB;
		
		compositeServerStatus = new Composite(this, SWT.NONE);
		compositeServerStatus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeServerStatus = new GridLayout(1, false);
		gl_compositeServerStatus.verticalSpacing = 2;
		gl_compositeServerStatus.horizontalSpacing = 2;
		gl_compositeServerStatus.marginHeight = 2;
		gl_compositeServerStatus.marginWidth = 2;
		compositeServerStatus.setLayout(gl_compositeServerStatus);
		
		// monitoring start, stop
		Composite compositeHead = new Composite(compositeServerStatus, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeHead = new GridLayout(3, false);
		gl_compositeHead.verticalSpacing = 2;
		gl_compositeHead.horizontalSpacing = 2;
		gl_compositeHead.marginHeight = 2;
		gl_compositeHead.marginWidth = 2;
		compositeHead.setLayout(gl_compositeHead);
		
		Label lblMonitoring = new Label(compositeHead, SWT.NONE);
		lblMonitoring.setText("Monitoring");
		
		btnStart = new Button(compositeHead, SWT.NONE);
		btnStart.setBounds(0, 0, 94, 28);
		btnStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				startInstanceMon();
			}
		});
		btnStart.setText("Start");
		
		btnStop = new Button(compositeHead, SWT.NONE);
		btnStop.setEnabled(false);
		btnStop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				stopInstanceMon();
			}
		});
		btnStop.setText("Stop");
		
		// create information
		createInstanceInformation();
		
		Composite cmpMemory = new Composite(compositeServerStatus, SWT.NONE);
		GridLayout gl_grpMemory = new GridLayout(2, false);
		gl_grpMemory.verticalSpacing = 2;
		gl_grpMemory.horizontalSpacing = 2;
		gl_grpMemory.marginHeight = 2;
		gl_grpMemory.marginWidth = 2;
		cmpMemory.setLayout(gl_grpMemory);
		cmpMemory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		createConnectionChart(cmpMemory, commandResult);
		createNetworkInformation(cmpMemory, commandResult);

		// show connection, cursor
		Composite cmpConnections = new Composite(compositeServerStatus, SWT.NONE);
		GridLayout gl_grpConnections = new GridLayout(2, false);
		gl_grpConnections.verticalSpacing = 1;
		gl_grpConnections.horizontalSpacing = 1;
		gl_grpConnections.marginHeight = 1;
		gl_grpConnections.marginWidth = 1;
		cmpConnections.setLayout(gl_grpConnections);
		cmpConnections.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createMemeoryInformation(cmpConnections, commandResult);
		createCursorsChart(cmpConnections, commandResult);
		
		// show extra information
		Group grpExtraInfo = new Group(compositeServerStatus, SWT.NONE);
		GridLayout gl_grpExtraInfo = new GridLayout(1, false);
		gl_grpExtraInfo.verticalSpacing = 2;
		gl_grpExtraInfo.horizontalSpacing = 2;
		gl_grpExtraInfo.marginHeight = 2;
		gl_grpExtraInfo.marginWidth = 2;
		grpExtraInfo.setLayout(gl_grpExtraInfo);
		grpExtraInfo.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		grpExtraInfo.setText("Extra Information");
		
		Composite compositeExtraInfo = new FindOneDetailComposite(grpExtraInfo, "Extra Information", (DBObject)commandResult.get("extra_info"), false);
		compositeExtraInfo.setLayout(new GridLayout(1, false));
		compositeExtraInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	
	/**
	 * start instance monitoring
	 */
	private void startInstanceMon() {
		if(!isUIThreadRunning) {
			spsInstance.start();
			Thread bgThread = new Thread( startUIThread() );
			bgThread.setDaemon( true );
			bgThread.start();
			
			btnStart.setEnabled(false);
			btnStop.setEnabled(true);
		}
	}
	
	/**
	 * start runnable
	 * 
	 * @return
	 */
	private Runnable startUIThread() {
		isUIThreadRunning = true;
		
		Runnable bgRunnable = new Runnable() {
			public void run() {
		    
				while(isUIThreadRunning) {
				    try {
				    	final CommandResult commandResult = MongoDBQuery.serverStatusCommandResult(userDB);
				    
					    final Display display = compositeServerStatus.getDisplay();
					    display.asyncExec( new Runnable() {
					    	public void run() {
					    		initMongoDBInfoData(commandResult);
					    		refreshMemoryData(commandResult);
					    		refreshNetwork(commandResult);
					    		refreshConnections(commandResult);
					    		refreshCursors(commandResult);
					    	}
					    } );
					    
				    } catch(Exception e) {
				    	logger.error("Job executing", e);
				    }
				    
				    try {
						Thread.sleep(2000);								
					} catch(Exception e){}	
				}	// end while
			}	// end run
		};
		
		return bgRunnable;
	}
	
	/**
	 * stop instance monitoring
	 */
	private void stopInstanceMon() {
		isUIThreadRunning = false;
		spsInstance.stop();
		
		btnStart.setEnabled(true);
		btnStop.setEnabled(false);
	}
	
	/**
	 * Show Mongodb System Information
	 * 
	 * @param commandResult
	 */
	public void initMongoDBInfoData(CommandResult commandResult) {
		String strHost 	= StringUtils.trimToEmpty(commandResult.getString("host"));
		String version 	= StringUtils.trimToEmpty(commandResult.getString("version"));
		String process 	= StringUtils.trimToEmpty(commandResult.getString("process"));
		String pid 		= StringUtils.trimToEmpty(commandResult.getString("pid"));
		String uptime 	= StringUtils.trimToEmpty(commandResult.getString("uptime"));
		
		String uptimeMillis 	= StringUtils.trimToEmpty(DateUtil.getHoureMinSecString(ENumberUtils.toInt(commandResult.getString("uptimeMillis"))));
		String uptimeEstimate 	= StringUtils.trimToEmpty(commandResult.getString("uptimeEstimate"));
		String localTime 		= StringUtils.trimToEmpty(commandResult.getString("localTime"));
		
		textHost.setText(strHost);
		textVersion.setText(version);
		textProcess.setText(process);
		textPID.setText(pid);
		textUptime.setText(uptime);
		textUptimeMillis.setText(uptimeMillis);
		textUpTimeEstimate.setText(uptimeEstimate);
		textLocalTime.setText(localTime);
	}
	
	/**
	 * refresh memory
	 * 
	 * @param commandResult
	 */
	private void refreshMemoryData(CommandResult commandResult) {
		DBObject cursorConnections = (DBObject)commandResult.get("mem");
	    int bits 		= cursorConnections==null?0:ENumberUtils.toInt(cursorConnections.get("bits"));
	    int resident 	= cursorConnections==null?0:ENumberUtils.toInt(cursorConnections.get("resident"));
	    int virtual 	= cursorConnections==null?0:ENumberUtils.toInt(cursorConnections.get("virtual"));
	    
	    int mapped 		= cursorConnections==null?0:ENumberUtils.toInt(cursorConnections.get("mapped"));
	    int mappedWithJournal = cursorConnections==null?0:ENumberUtils.toInt(cursorConnections.get("mappedWithJournal"));
	    
	    float fBits 	= virtual==0?0:(float)bits / (float)virtual;
	    float fResident = virtual==0?0:(float)resident / (float)virtual;
	    float fVirtual 	= 0.8f;
	    float fMapped 	= virtual==0?0:(float)mapped / (float)virtual;
	    float fMappedWithJournal = virtual==0?0:(float)mappedWithJournal / (float)virtual;
	    
	    DataItem[] dataItems = new DataItem[] {
	    	      new DataItem( fBits, 				"In (" + NumberFormatUtils.kbMbFormat(bits) + ")", 			ColorsSWTUtils.CAT10_COLORS[ 0 ] ),
	    	      new DataItem( fResident, 			"Out (" + NumberFormatUtils.kbMbFormat(resident) + ")", 	ColorsSWTUtils.CAT10_COLORS[ 1 ] ),
	    	      new DataItem( fVirtual, 			"Vitrual (" + NumberFormatUtils.addComma(virtual) + ")", ColorsSWTUtils.CAT10_COLORS[ 2 ] ),
	    	      new DataItem( fMapped, 			"Mapped (" + NumberFormatUtils.addComma(mapped) + ")", 	ColorsSWTUtils.CAT10_COLORS[ 3 ] ),
	    	      new DataItem( fMappedWithJournal, "Mapped With Journal (" + NumberFormatUtils.addComma(mappedWithJournal) + ")", ColorsSWTUtils.CAT10_COLORS[ 4 ] )
	    	    };
	
		barChartMemory.setItems(dataItems);
	}
	
	/**
	 * refresh network
	 * @param commandResult
	 */
	private void refreshNetwork(CommandResult commandResult) {
	    DBObject cursorConnections = (DBObject)commandResult.get("network");
	    int bytesIn 	= cursorConnections==null?0:ENumberUtils.toInt(cursorConnections.get("bytesIn"));
	    int bytesOut 	= cursorConnections==null?0:ENumberUtils.toInt(cursorConnections.get("bytesOut"));
	    int numRequests = cursorConnections==null?0:ENumberUtils.toInt(cursorConnections.get("numRequests"));

	    float floatBI = 0f, floatBO = 0f, floatNf = 0f;
	    if(bytesIn < bytesOut) {
	    	floatBI = (float)bytesIn / (float)bytesOut;	    	
	    	floatBO = 0.8f;
	    	floatNf = (float)numRequests / (float)bytesOut;
	    } else if(bytesIn ==0 || bytesOut == 0){
	    	floatBI = 0.0f;	    	
	    	floatBO = 0.0f;
	    	floatNf = 0.0f;
	    } else {
	    	floatBI = 0.0f;	    	
	    	floatBO = (float)bytesOut / (float)bytesIn;
	    	floatNf = (float)numRequests / (float)bytesIn;
	    }
	    
	    DataItem[] dataItems = new DataItem[] {
	    	      new DataItem( floatBI, "In (" + NumberFormatUtils.kbMbFormat(bytesIn) + ")", 			ColorsSWTUtils.CAT10_COLORS[ 0 ] ),
	    	      new DataItem( floatBO, "Out (" + NumberFormatUtils.kbMbFormat(bytesOut) + ")", 		ColorsSWTUtils.CAT10_COLORS[ 1 ] ),
	    	      new DataItem( floatNf, "Requests (" + NumberFormatUtils.addComma(numRequests) + ")", ColorsSWTUtils.CAT10_COLORS[ 2 ] ),
	    	    };
	
	    barChartNetwork.setItems(dataItems);
	}
	
	/**
	 * refresh connections
	 * 
	 * @param commandResult
	 */
	private void refreshConnections(CommandResult commandResult) {
		DBObject cursorConnections = (DBObject)commandResult.get("connections");
	    int current 		= cursorConnections==null?0:ENumberUtils.toInt(cursorConnections.get("current"));
	    int available 		= cursorConnections==null?0:ENumberUtils.toInt(cursorConnections.get("available"));
	    float floatCurrent 	= available==0?0:(float)current / (float)available;
	    
	    DataItem[] dataItems = new DataItem[] {
	    	      new DataItem( 0.80f, 		 "Available (" + NumberFormatUtils.addComma(available) + ")", ColorsSWTUtils.CAT10_COLORS[ 0 ] ),
	    	      new DataItem( floatCurrent, "Available (" + NumberFormatUtils.addComma(available) + ")", ColorsSWTUtils.CAT10_COLORS[ 1 ] ),
	    	    };
	    
	    barChartConnection.setItems(dataItems);
	}
	
	/**
	 * refresh cursors
	 * 
	 * @param commandResult
	 */
	private void refreshCursors(CommandResult commandResult) {
		DBObject cursorCursors = (DBObject)commandResult.get("cursors");
		int totalOpen 			= cursorCursors==null?0:ENumberUtils.toInt(cursorCursors.get("totalOpen"));
		int clientCursors_size 	= cursorCursors==null?0:ENumberUtils.toInt(cursorCursors.get("clientCursors_size"));
		int timedOut 			= cursorCursors==null?0:ENumberUtils.toInt(cursorCursors.get("timedOut"));
		
		 DataItem[] dataItems = new DataItem[] {
	    	      new DataItem( totalOpen, "Total Open (" + totalOpen + ")", ColorsSWTUtils.CAT10_COLORS[ 0 ] ),
	    	      new DataItem( clientCursors_size, "Client cursors size (" + clientCursors_size + ")", ColorsSWTUtils.CAT10_COLORS[ 1 ] ),
	    	      new DataItem( timedOut, "Timed Out (" + timedOut + ")", ColorsSWTUtils.CAT10_COLORS[ 2 ] ),
	    	    };
		 pieChartCursors.setItems(dataItems);
	}
	
	/**
	 * Show instance information
	 */
	private void createInstanceInformation() {
		Group groupInstance = new Group(compositeServerStatus, SWT.NONE);
		GridLayout gl_compositeInstance = new GridLayout(4, false);
		gl_compositeInstance.verticalSpacing = 2;
		gl_compositeInstance.horizontalSpacing = 2;
		gl_compositeInstance.marginHeight = 2;
		gl_compositeInstance.marginWidth = 2;
		groupInstance.setLayout(gl_compositeInstance);
		groupInstance.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		groupInstance.setText("DB Instance Information");
		
		Label lblHost = new Label(groupInstance, SWT.NONE);
		lblHost.setText("Host");
		
		textHost = new Text(groupInstance, SWT.BORDER | SWT.READ_ONLY);
		textHost.setEditable(false);
		textHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblVersion = new Label(groupInstance, SWT.NONE);
		lblVersion.setText("Version");
		
		textVersion = new Text(groupInstance, SWT.BORDER | SWT.READ_ONLY);
		textVersion.setEditable(false);
		textVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblProcess = new Label(groupInstance, SWT.NONE);
		lblProcess.setText("Process");
		
		textProcess = new Text(groupInstance, SWT.BORDER | SWT.READ_ONLY);
		textProcess.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPid = new Label(groupInstance, SWT.NONE);
		lblPid.setText("PID");
		
		textPID = new Text(groupInstance, SWT.BORDER | SWT.READ_ONLY);
		textPID.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblUptime = new Label(groupInstance, SWT.NONE);
		lblUptime.setText("Uptime");
		
		textUptime = new Text(groupInstance, SWT.BORDER | SWT.READ_ONLY);
		textUptime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblUptimemillis = new Label(groupInstance, SWT.NONE);
		lblUptimemillis.setText("UptimeMillis");
		
		textUptimeMillis = new Text(groupInstance, SWT.BORDER | SWT.READ_ONLY);
		textUptimeMillis.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblUptimeEstimate = new Label(groupInstance, SWT.NONE);
		lblUptimeEstimate.setText("Uptime Estimate");
		
		textUpTimeEstimate = new Text(groupInstance, SWT.BORDER | SWT.READ_ONLY);
		textUpTimeEstimate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblLocaltime = new Label(groupInstance, SWT.NONE);
		lblLocaltime.setText("LocalTime");
		
		textLocalTime = new Text(groupInstance, SWT.BORDER | SWT.READ_ONLY);
		textLocalTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}
	
	/**
	 * Show memory information
	 */
	private void createMemeoryInformation(Composite cmpMemory, CommandResult commandResult) {
		Group compositeMemory = new Group(cmpMemory, SWT.NONE);
		compositeMemory.setLayout(new GridLayout(1, false));
		compositeMemory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeMemory.setText("Memory");
		
		barChartMemory = new BarChart(compositeMemory, SWT.NONE);
		barChartMemory.setLayout(new GridLayout(1, false));
		barChartMemory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

	    DBObject cursorConnections = (DBObject)commandResult.get("mem");
	    int bits 		= cursorConnections == null?0:ENumberUtils.toInt(cursorConnections.get("bits"));
	    int resident 	= cursorConnections == null?0:ENumberUtils.toInt(cursorConnections.get("resident"));
	    int virtual 	= cursorConnections == null?0:ENumberUtils.toInt(cursorConnections.get("virtual"));
	    
	    int mapped = cursorConnections == null?0:(Integer)cursorConnections.get("mapped");
	    int mappedWithJournal =0;
	    try {
	    	mappedWithJournal = cursorConnections == null?0:(Integer)cursorConnections.get("mappedWithJournal");
	    } catch(Exception e) {}
	    
	    float fBits 	= virtual==0?0:(float)bits / (float)virtual;
	    float fResident = virtual==0?0:(float)resident / (float)virtual;
	    float fVirtual 	= 0.8f;
	    float fMapped 	= virtual==0?0:(float)mapped / (float)virtual;
	    float fMappedWithJournal = virtual==0?0:(float)mappedWithJournal / (float)virtual;
	    
	    DataItem[] dataItems = new DataItem[] {
	    	      new DataItem( fBits, 				"In (" + NumberFormatUtils.kbMbFormat(bits) + ")", 			ColorsSWTUtils.CAT10_COLORS[ 0 ] ),
	    	      new DataItem( fResident, 			"Out (" + NumberFormatUtils.kbMbFormat(resident) + ")", 	ColorsSWTUtils.CAT10_COLORS[ 1 ] ),
	    	      new DataItem( fVirtual, 			"Vitrual (" + NumberFormatUtils.addComma(virtual) + ")", ColorsSWTUtils.CAT10_COLORS[ 2 ] ),
	    	      new DataItem( fMapped, 			"Mapped (" + NumberFormatUtils.addComma(mapped) + ")", 	ColorsSWTUtils.CAT10_COLORS[ 3 ] ),
	    	      new DataItem( fMappedWithJournal, "Mapped With Journal (" + NumberFormatUtils.addComma(mappedWithJournal) + ")", ColorsSWTUtils.CAT10_COLORS[ 4 ] )
	    	    };
	    barChartMemory.setItems(dataItems);
	}
	
	/**
	 * Show network information.
	 */
	private void createNetworkInformation(Composite cmpMemory, CommandResult commandResult) {
		Group compositeNetwork = new Group(cmpMemory, SWT.NONE);
		compositeNetwork.setLayout(new GridLayout(1, false));
		compositeNetwork.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeNetwork.setText("Network");
		
		barChartNetwork = new BarChart(compositeNetwork, SWT.NONE);
		barChartNetwork.setLayout(new GridLayout(1, false));
		barChartNetwork.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	    
	    try {
		    DBObject cursorConnections = (DBObject)commandResult.get("network");
		    int bytesIn 	= cursorConnections == null?0:ENumberUtils.toInt(cursorConnections.get("bytesIn"));
		    int bytesOut 	= cursorConnections == null?0:ENumberUtils.toInt(cursorConnections.get("bytesOut"));
		    int numRequests = cursorConnections == null?0:ENumberUtils.toInt(cursorConnections.get("numRequests"));
	
		    float floatBI = 0f, floatBO = 0f, floatNf = 0f;
		    if(bytesIn < bytesOut) {
		    	floatBI = (float)bytesIn / (float)bytesOut;	    	
		    	floatBO = 0.8f;
		    	floatNf = (float)numRequests / (float)bytesOut;
		    } else if(bytesIn == 0) {
		    	floatBI = 0.0f;	    	
		    	floatBO = 0.0f;
		    	floatNf = 0.0f;
		    } else {
		    	floatBI = 0.0f;	    	
		    	floatBO = (float)bytesOut / (float)bytesIn;
		    	floatNf = (float)numRequests / (float)bytesIn;
		    }

		    DataItem[] dataItems = new DataItem[] {
		    	      new DataItem( floatBI, "In (" + NumberFormatUtils.kbMbFormat(bytesIn) + ")", 			ColorsSWTUtils.CAT10_COLORS[ 0 ] ),
		    	      new DataItem( floatBO, "Out (" + NumberFormatUtils.kbMbFormat(bytesOut) + ")", 		ColorsSWTUtils.CAT10_COLORS[ 1 ] ),
		    	      new DataItem( floatNf, "Requests (" + NumberFormatUtils.addComma(numRequests) + ")", ColorsSWTUtils.CAT10_COLORS[ 2 ] ),
		    	    };
		
		    barChartNetwork.setItems(dataItems);
	    } catch(Exception e) {
	    	logger.error("Network information", e);
	    }
	}
	
	/**
	 * create connection pie chart
	 */
	private void createConnectionChart(Composite cmpConnections, CommandResult commandResult) {
		if(logger.isDebugEnabled()) logger.debug("=============start create newtrok Information================================================");
		
		Group compositeConnection = new Group(cmpConnections, SWT.NONE);
		compositeConnection.setLayout(new GridLayout(1, false));
		compositeConnection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeConnection.setText("Connections");
		
		barChartConnection = new BarChart(compositeConnection, SWT.NONE );
		GridLayout gl_grpConnectionInfo = new GridLayout(1, false);
		gl_grpConnectionInfo.verticalSpacing = 0;
		gl_grpConnectionInfo.horizontalSpacing = 0;
		gl_grpConnectionInfo.marginHeight = 0;
		gl_grpConnectionInfo.marginWidth = 0;
		barChartConnection.setLayout(gl_grpConnectionInfo);
		barChartConnection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

	    int current 	= 0;
	    int available 	= 0;
	    float floatCurrent = 0f;
	    
	    try {
		    // nullPointExcepiton check - https://github.com/hangum/TadpoleForDBTools/issues/361 
		    DBObject cursorConnections = (DBObject)commandResult.get("connections");
		    if(cursorConnections != null) {
		    	current 	= ENumberUtils.toInt(cursorConnections.get("current"));
			    available 	= ENumberUtils.toInt(cursorConnections.get("available"));
			    floatCurrent = (float)current / (float)available;	
		    }
	    } catch(Exception e) {
	    	logger.error("Crate Connection chart", e);
	    }
	    
	    if(logger.isDebugEnabled()) logger.debug("=============start create newtrok Information [end]================================================");
	}
	
	/**
	 * Show connection Information
	 */
	private void createCursorsChart(Composite cmpConnections, CommandResult commandResult) {
		Group compositeCursor = new Group(cmpConnections, SWT.NONE);
		compositeCursor.setLayout(new GridLayout(1, false));
		compositeCursor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeCursor.setText("Cursors");

		pieChartCursors = new PieChart(compositeCursor, SWT.NONE);
		GridLayout gl_grpConnectionInfo = new GridLayout(1, false);
		gl_grpConnectionInfo.verticalSpacing = 0;
		gl_grpConnectionInfo.horizontalSpacing = 0;
		gl_grpConnectionInfo.marginHeight = 0;
		gl_grpConnectionInfo.marginWidth = 0;
		pieChartCursors.setLayout(gl_grpConnectionInfo);
		pieChartCursors.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		pieChartCursors.setInnerRadius(0.1f);
		
		DBObject cursorCursors = (DBObject)commandResult.get("cursors");
		int totalOpen 			= cursorCursors == null?0:ENumberUtils.toInt(cursorCursors.get("totalOpen"));
		int clientCursors_size 	= cursorCursors == null?0:ENumberUtils.toInt(cursorCursors.get("clientCursors_size"));
		int timedOut 			= cursorCursors == null?0:ENumberUtils.toInt(cursorCursors.get("timedOut"));
		
		DataItem[] dataItems = new DataItem[] {
	    	      new DataItem( totalOpen, "Total Open (" + totalOpen + ")", ColorsSWTUtils.CAT10_COLORS[ 0 ] ),
	    	      new DataItem( clientCursors_size, "Client cursors size (" + clientCursors_size + ")", ColorsSWTUtils.CAT10_COLORS[ 1 ] ),
	    	      new DataItem( timedOut, "Timed Out (" + timedOut + ")", ColorsSWTUtils.CAT10_COLORS[ 2 ] ),
	    	    };
		 pieChartCursors.setItems(dataItems);
	}
	
	@Override
	public void dispose() {
		isUIThreadRunning = false;
		super.dispose();
	}

	@Override
	protected void checkSubclass() {
	}
}
