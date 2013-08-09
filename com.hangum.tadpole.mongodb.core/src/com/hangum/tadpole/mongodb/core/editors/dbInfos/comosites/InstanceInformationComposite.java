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
import org.eclipse.rap.addons.d3chart.BarChart;
import org.eclipse.rap.addons.d3chart.ChartItem;
import org.eclipse.rap.addons.d3chart.ColorStream;
import org.eclipse.rap.addons.d3chart.Colors;
import org.eclipse.rap.addons.d3chart.PieChart;
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

import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.dialogs.resultview.FindOneDetailComposite;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.util.NumberFormatUtils;
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
		btnStop.setSelection(false);
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

		createMemeoryInformation(cmpMemory, commandResult);
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
		
		createConnectionChart(cmpConnections, commandResult);
		createCursorsChart(cmpConnections, commandResult);
		
		// show extra information
		Group grpExtraInfo = new Group(compositeServerStatus, SWT.NONE);
		GridLayout gl_grpExtraInfo = new GridLayout(1, false);
		gl_grpExtraInfo.verticalSpacing = 2;
		gl_grpExtraInfo.horizontalSpacing = 2;
		gl_grpExtraInfo.marginHeight = 2;
		gl_grpExtraInfo.marginWidth = 2;
		grpExtraInfo.setLayout(gl_grpExtraInfo);
		grpExtraInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
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
					    
					    try {
							Thread.sleep(1000);								
						} catch(Exception e){}	
				    } catch(Exception e) {
				    	logger.error("Job executing", e);
				    }
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
		String uptime 	= StringUtils.trimToEmpty((commandResult.getDouble("uptime")/1000) + " Sec");
		String uptimeMillis 	= StringUtils.trimToEmpty((commandResult.getDouble("uptimeMillis")/1000) + " Sec");
		String uptimeEstimate 	= StringUtils.trimToEmpty((commandResult.getDouble("uptimeEstimate")/1000) + " Sec");
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
	    int bits = (Integer)cursorConnections.get("bits");
	    int resident = (Integer)cursorConnections.get("resident");
	    int virtual = (Integer)cursorConnections.get("virtual");
	    
	    int mapped = (Integer)cursorConnections.get("mapped");
	    int mappedWithJournal = (Integer)cursorConnections.get("mappedWithJournal");
	    
	    float fBits 	= (float)bits / (float)virtual;
	    float fResident = (float)resident / (float)virtual;
	    float fVirtual 	= 0.8f;
	    float fMapped 	= (float)mapped / (float)virtual;
	    float fMappedWithJournal = (float)mappedWithJournal / (float)virtual;
	    
	    ChartItem itemAvailable = barChartMemory.getItems()[0];
	    itemAvailable.setText("In (" + NumberFormatUtils.kbMbFormat(bits) + ")");
	    itemAvailable.setValue(fBits);

	    ChartItem itemCurrent = barChartMemory.getItems()[1];
	    itemCurrent.setText("Out (" + NumberFormatUtils.kbMbFormat(resident) + ")");
	    itemCurrent.setValue(fResident);
	    
	    ChartItem itemNumRequests = barChartMemory.getItems()[2];
	    itemNumRequests.setText("Vitrual (" + NumberFormatUtils.commaFormat(virtual) + ")");
	    itemNumRequests.setValue(fVirtual);
	    
	    ChartItem itemMapped = barChartMemory.getItems()[3];
	    itemMapped.setText("Mapped (" + NumberFormatUtils.commaFormat(mapped) + ")");
	    itemMapped.setValue(fMapped);
	    
	    ChartItem itemMappedWithJournal = barChartMemory.getItems()[4];
	    itemMappedWithJournal.setText("Mapped With Journal (" + NumberFormatUtils.commaFormat(mappedWithJournal) + ")");
	    itemMappedWithJournal.setValue(fMappedWithJournal);
	}
	
	/**
	 * refresh network
	 * @param commandResult
	 */
	private void refreshNetwork(CommandResult commandResult) {
	    DBObject cursorConnections = (DBObject)commandResult.get("network");
	    int bytesIn = (Integer)cursorConnections.get("bytesIn");
	    int bytesOut = (Integer)cursorConnections.get("bytesOut");
	    int numRequests = (Integer)cursorConnections.get("numRequests");

	    float floatBI = 0f, floatBO = 0f, floatNf = 0f;
	    if(bytesIn < bytesOut) {
	    	floatBI = (float)bytesIn / (float)bytesOut;	    	
	    	floatBO = 0.8f;
	    	floatNf = (float)numRequests / (float)bytesOut;
	    } else {
	    	floatBI = 0.0f;	    	
	    	floatBO = (float)bytesOut / (float)bytesIn;
	    	floatNf = (float)numRequests / (float)bytesIn;
	    }

	    ChartItem itemAvailable = barChartNetwork.getItems()[0];
	    itemAvailable.setText("In (" + NumberFormatUtils.kbMbFormat(bytesIn) + ")");
	    itemAvailable.setValue(floatBI);

	    ChartItem itemCurrent = barChartNetwork.getItems()[1];
	    itemCurrent.setText("Out (" + NumberFormatUtils.kbMbFormat(bytesOut) + ")");
	    itemCurrent.setValue(floatBO);
	    
	    ChartItem itemNumRequests = barChartNetwork.getItems()[2];
	    itemNumRequests.setText("Requests (" + NumberFormatUtils.commaFormat(numRequests) + ")");
	    itemNumRequests.setValue(floatNf);
	}
	
	/**
	 * refresh connections
	 * 
	 * @param commandResult
	 */
	private void refreshConnections(CommandResult commandResult) {
		DBObject cursorConnections = (DBObject)commandResult.get("connections");
	    int current = (Integer)cursorConnections.get("current");
	    int available = (Integer)cursorConnections.get("available");
	    float floatCurrent = (float)current / (float)available;

	    ChartItem itemAvailable = barChartConnection.getItems()[0];
	    itemAvailable.setText("Available (" + NumberFormatUtils.commaFormat(available) + ")");
	    itemAvailable.setValue(0.80f);

	    ChartItem itemCurrent = barChartConnection.getItems()[1];
	    itemCurrent.setText("Current (" + NumberFormatUtils.commaFormat(current) + ")");
	    itemCurrent.setValue(floatCurrent);
	}
	
	/**
	 * refresh cursors
	 * 
	 * @param commandResult
	 */
	private void refreshCursors(CommandResult commandResult) {
		DBObject cursorCursors = (DBObject)commandResult.get("cursors");
		int totalOpen = (Integer)cursorCursors.get("totalOpen");
		int clientCursors_size = (Integer)cursorCursors.get("clientCursors_size");
		int timedOut = (Integer)cursorCursors.get("timedOut");
		
		ChartItem itemTotalOpen = pieChartCursors.getItems()[0];
	    itemTotalOpen.setText("Total Open (" + totalOpen + ")");
	    itemTotalOpen.setValue(totalOpen);

	    ChartItem itemClientCursors_size = pieChartCursors.getItems()[1];
	    itemClientCursors_size.setText("Client cursors size (" + clientCursors_size + ")");
	    itemClientCursors_size.setValue(clientCursors_size);
	    
	    ChartItem itemTimedOut = pieChartCursors.getItems()[1];
	    itemTimedOut.setText("Timed Out (" + timedOut + ")");
	    itemTimedOut.setValue(timedOut);
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
		
		ColorStream colors = Colors.cat10Colors(compositeMemory.getDisplay()).loop();
		
		barChartMemory = new BarChart(compositeMemory, SWT.NONE);
		barChartMemory.setLayout(new GridLayout(1, false));
		barChartMemory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	    barChartMemory.setBarWidth(25);

	    DBObject cursorConnections = (DBObject)commandResult.get("mem");
	    int bits = (Integer)cursorConnections.get("bits");
	    int resident = (Integer)cursorConnections.get("resident");
	    int virtual = (Integer)cursorConnections.get("virtual");
	    
	    int mapped = (Integer)cursorConnections.get("mapped");
	    int mappedWithJournal = (Integer)cursorConnections.get("mappedWithJournal");
	    
	    
	    float fBits 	= (float)bits / (float)virtual;
	    float fResident = (float)resident / (float)virtual;
	    float fVirtual 	= 0.8f;
	    float fMapped 	= (float)mapped / (float)virtual;
	    float fMappedWithJournal = (float)mappedWithJournal / (float)virtual;
	    
	    
	    ChartItem itemAvailable = new ChartItem(barChartMemory);
	    itemAvailable.setText("In (" + NumberFormatUtils.kbMbFormat(bits) + ")");
	    itemAvailable.setColor(colors.next());
	    itemAvailable.setValue(fBits);

	    ChartItem itemCurrent = new ChartItem(barChartMemory);
	    itemCurrent.setText("Out (" + NumberFormatUtils.kbMbFormat(resident) + ")");
	    itemCurrent.setColor(colors.next());
	    itemCurrent.setValue(fResident);
	    
	    ChartItem itemNumRequests = new ChartItem(barChartMemory);
	    itemNumRequests.setText("Vitrual (" + NumberFormatUtils.commaFormat(virtual) + ")");
	    itemNumRequests.setColor(colors.next());
	    itemNumRequests.setValue(fVirtual);
	    
	    ChartItem itemMapped = new ChartItem(barChartMemory);
	    itemMapped.setText("Mapped (" + NumberFormatUtils.commaFormat(mapped) + ")");
	    itemMapped.setColor(colors.next());
	    itemMapped.setValue(fMapped);
	    
	    ChartItem itemMappedWithJournal = new ChartItem(barChartMemory);
	    itemMappedWithJournal.setText("Mapped With Journal (" + NumberFormatUtils.commaFormat(mappedWithJournal) + ")");
	    itemMappedWithJournal.setColor(colors.next());
	    itemMappedWithJournal.setValue(fMappedWithJournal);
	}
	
	/**
	 * Show network information.
	 */
	private void createNetworkInformation(Composite cmpMemory, CommandResult commandResult) {
		Group compositeNetwork = new Group(cmpMemory, SWT.NONE);
		compositeNetwork.setLayout(new GridLayout(1, false));
		compositeNetwork.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeNetwork.setText("Network");
		
		ColorStream colors = Colors.cat10Colors(compositeNetwork.getDisplay()).loop();
		
		barChartNetwork = new BarChart(compositeNetwork, SWT.NONE);
		barChartNetwork.setLayout(new GridLayout(1, false));
		barChartNetwork.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	    barChartNetwork.setBarWidth(25);

	    DBObject cursorConnections = (DBObject)commandResult.get("network");
	    int bytesIn = (Integer)cursorConnections.get("bytesIn");
	    int bytesOut = (Integer)cursorConnections.get("bytesOut");
	    int numRequests = (Integer)cursorConnections.get("numRequests");

	    float floatBI = 0f, floatBO = 0f, floatNf = 0f;
	    if(bytesIn < bytesOut) {
	    	floatBI = (float)bytesIn / (float)bytesOut;	    	
	    	floatBO = 0.8f;
	    	floatNf = (float)numRequests / (float)bytesOut;
	    } else {
	    	floatBI = 0.0f;	    	
	    	floatBO = (float)bytesOut / (float)bytesIn;
	    	floatNf = (float)numRequests / (float)bytesIn;
	    }

	    ChartItem itemAvailable = new ChartItem(barChartNetwork);
	    itemAvailable.setText("In (" + NumberFormatUtils.kbMbFormat(bytesIn) + ")");
	    itemAvailable.setColor(colors.next());
	    itemAvailable.setValue(floatBI);

	    ChartItem itemCurrent = new ChartItem(barChartNetwork);
	    itemCurrent.setText("Out (" + NumberFormatUtils.kbMbFormat(bytesOut) + ")");
	    itemCurrent.setColor(colors.next());
	    itemCurrent.setValue(floatBO);
	    
	    ChartItem itemNumRequests = new ChartItem(barChartNetwork);
	    itemNumRequests.setText("Requests (" + NumberFormatUtils.commaFormat(numRequests) + ")");
	    itemNumRequests.setColor(colors.next());
	    itemNumRequests.setValue(floatNf);
	}
	
	/**
	 * create connection pie chart
	 */
	private void createConnectionChart(Composite cmpConnections, CommandResult commandResult) {
		Group compositeConnection = new Group(cmpConnections, SWT.NONE);
		compositeConnection.setLayout(new GridLayout(1, false));
		compositeConnection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeConnection.setText("Connections");
		
		ColorStream colors = Colors.cat10Colors(compositeConnection.getDisplay()).loop();
		
		barChartConnection = new BarChart(compositeConnection, SWT.NONE );
		GridLayout gl_grpConnectionInfo = new GridLayout(1, false);
		gl_grpConnectionInfo.verticalSpacing = 0;
		gl_grpConnectionInfo.horizontalSpacing = 0;
		gl_grpConnectionInfo.marginHeight = 0;
		gl_grpConnectionInfo.marginWidth = 0;
		barChartConnection.setLayout(gl_grpConnectionInfo);
		barChartConnection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	    barChartConnection.setBarWidth(25);

	    DBObject cursorConnections = (DBObject)commandResult.get("connections");
	    int current = (Integer)cursorConnections.get("current");
	    int available = (Integer)cursorConnections.get("available");
	    float floatCurrent = (float)current / (float)available;

	    ChartItem itemAvailable = new ChartItem(barChartConnection);
	    itemAvailable.setText("Available (" + NumberFormatUtils.commaFormat(available) + ")");
	    itemAvailable.setColor(colors.next());
	    itemAvailable.setValue(0.80f);

	    ChartItem itemCurrent = new ChartItem(barChartConnection);
	    itemCurrent.setText("Current (" + NumberFormatUtils.commaFormat(current) + ")");
	    itemCurrent.setColor(colors.next());
	    itemCurrent.setValue(floatCurrent);
	}
	
	/**
	 * Show connection Information
	 */
	private void createCursorsChart(Composite cmpConnections, CommandResult commandResult) {
		Group compositeCursor = new Group(cmpConnections, SWT.NONE);
		compositeCursor.setLayout(new GridLayout(1, false));
		compositeCursor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeCursor.setText("Cursors");
		
		ColorStream colors = Colors.cat10Colors(compositeCursor.getDisplay()).loop();
		
		pieChartCursors = new PieChart(compositeCursor, SWT.NONE);
		GridLayout gl_grpConnectionInfo = new GridLayout(1, false);
		gl_grpConnectionInfo.verticalSpacing = 0;
		gl_grpConnectionInfo.horizontalSpacing = 0;
		gl_grpConnectionInfo.marginHeight = 0;
		gl_grpConnectionInfo.marginWidth = 0;
		pieChartCursors.setLayout(gl_grpConnectionInfo);
		pieChartCursors.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		pieChartCursors.setInnerRadius(0.1f);
		
		DBObject cursorCursors = (DBObject)commandResult.get("cursors");
		int totalOpen = (Integer)cursorCursors.get("totalOpen");
		int clientCursors_size = (Integer)cursorCursors.get("clientCursors_size");
		int timedOut = (Integer)cursorCursors.get("timedOut");
		
		ChartItem itemTotalOpen = new ChartItem(pieChartCursors);
	    itemTotalOpen.setText("Total Open (" + totalOpen + ")");
	    itemTotalOpen.setColor(colors.next());
	    itemTotalOpen.setValue(totalOpen);

	    ChartItem itemClientCursors_size = new ChartItem(pieChartCursors);
	    itemClientCursors_size.setText("Client cursors size (" + clientCursors_size + ")");
	    itemClientCursors_size.setColor(colors.next());
	    itemClientCursors_size.setValue(clientCursors_size);
	    
	    ChartItem itemTimedOut = new ChartItem(pieChartCursors);
	    itemTimedOut.setText("Timed Out (" + timedOut + ")");
	    itemTimedOut.setColor(colors.next());
	    itemTimedOut.setValue(timedOut);
	    
//	    if(totalOpen == 0 && clientCursors_size == 0 && timedOut == 0) {
//	    	Label label = new Label(compositeCursor, SWT.NONE);
//	    	label.setText("Cursors value is all zero");
//	    }
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
