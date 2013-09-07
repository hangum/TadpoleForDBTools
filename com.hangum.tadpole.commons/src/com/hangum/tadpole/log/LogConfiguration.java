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
package com.hangum.tadpole.log;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.hangum.tadpole.util.ApplicationArgumentUtils;

/**
 * Set log file configuration.
 * 
 * @author hangum
 *
 */
public class LogConfiguration {
	String defaultFileName 	= "./logs/tadpole.log";
	String filePattern 		= "%d %-5p [%t] %-17c{2} (%13F:%L) %3x - %m%n";
	String defaultPattern 	= "%p - %C{1} : %M %m %n";
	
	public static Logger logger = Logger.getRootLogger();
	
	private static LogConfiguration logConfig;
	RollingFileAppender fileAppender = null;
	Appender consoleAppender = null;
	
	Level level 			= Level.DEBUG;
	String isDevelopment	= "";
	
	private LogConfiguration(){}
	
	public synchronized static LogConfiguration getInstance() {
		if( logConfig == null) {
			logConfig = new LogConfiguration();
			try { logConfig.init();}catch(Exception e){}
		}
		
		return logConfig;
	}
	
	public void init() throws Exception {
		
		fileAppender = new RollingFileAppender(new PatternLayout(filePattern), defaultFileName, true);
		fileAppender.setMaxBackupIndex(10);
		fileAppender.setMaxFileSize("10MB");
		
		consoleAppender = new ConsoleAppender(new PatternLayout(defaultPattern) );

		if(ApplicationArgumentUtils.isDebugMode()) this.level = Level.DEBUG;
		else this.level = Level.INFO;
		
		logger = Logger.getRootLogger();
		logger.setLevel(this.level);
		logger.addAppender(fileAppender);
		logger.addAppender(consoleAppender);
//		logger.setPriority(priority);
	}
	
	public void closeAppend() {
		if(consoleAppender != null) consoleAppender.close();
	}
	
	public RollingFileAppender getFileAppender() {
		return fileAppender;
	}

	public void setFileAppender(RollingFileAppender fileAppender) {
		this.fileAppender = fileAppender;
	}
	
	public void setLevel(String level) {
		this.level = Level.toLevel(level);
		logger.setLevel(this.level);
	}
	
	@SuppressWarnings("unchecked")
	public static Logger getLogger(final Class cls) {
		return Logger.getLogger(cls);
	}
	
	public String getIsDevelopment() {
		return isDevelopment;
	}

	public void setIsDevelopment(String isDevelopment) {
		this.isDevelopment = isDevelopment;
	}
}
