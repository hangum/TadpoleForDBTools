package com.hangum.db.log;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;

/**
 * eclipse log 정보를 log4j로그에 남길수 있도록 리스너 생성
 * 
 * @author hangum
 *
 */
public class LogListener implements ILogListener {

	private static final Logger logger = Logger.getLogger(LogListener.class.getName());
	
	public void logging(IStatus status, String plugin) {
		if (status.getSeverity() == IStatus.WARNING) {
			if (status.getException() == null) {
				logger.warn(status.getMessage());
			} else {
				logger.warn(status.getMessage() + status.getException());
			}
		} else if (status.getSeverity() == IStatus.ERROR) {
			if (status.getException() == null) {
				logger.error(status.getMessage());
			} else {
				logger.error(status.getMessage() + status.getException());
			}
		}
	}

}
