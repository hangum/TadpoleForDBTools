package com.hangum.tadpole.monitoring.core.utils;

/**
 * monitoring define
 * 
 * @author hangum
 *
 */
public class MonitoringDefine {
	
	/**
	 * 
	 * @author hangum
	 *
	 */
	public static enum ACCESS_TYPE {
		SQL
//		,
//		PLSQL,
//		RestAPI
	};

	/**
	 * DEFINE MONITORING TYPE
	 * 
	 * @author hangum
	 *
	 */
	public static enum MONITORING_TYPE {
		CONNECTION,
		CPU,
		DISK,
		GENERAL_LOG,
		SLOW_QUERY,
		NETWORK_IN,
		NETWORK_OUT,
		PROCESS,
		SESSION_LIST,
		STATEMENT_STATUS
	};
	
	/**
	 * monitoring condition type
	 * 
	 * @author hangum
	 *
	 */
	public static enum CONDITION_TYPE {
		EQUALS,
		UNEQUAL,
		LEAST,
		GREATEST,
		NOT_CHECK,
		RISE_EXCEPTION
	}
	
	/**
	 * 후처리 타입
	 * 
	 * @author hangum
	 *
	 */
	public static enum AFTER_PROCESS_TYPE {
		EMAIL,
		KILL_AFTER_EMAIL
	}
	
}
