package com.hangum.tadpole.monitoring.core.utils;

import org.eclipse.swt.SWT;


/**
 * monitoring define
 * 
 * @author hangum
 *
 */
public class MonitoringDefine {
	
	/**
	 * monitoring status
	 * 
	 * @author hangum
	 *
	 */
	public static enum MONITORING_STATUS {
		CLEAN("CLEAN", SWT.COLOR_DARK_GREEN),
		WARRING("WARRING", SWT.COLOR_DARK_GRAY),
		CRITICAL("CRITICAL", SWT.COLOR_RED);
		
		String name;
		int color;
		
		private MONITORING_STATUS(String name, int color) {
			this.name = name;
			this.color = color;
		}
		
		public String getName() {
			return name;
		}
		
		public int getColor() {
			return color;
		} 
	
	}
	
	/**
	 * template type
	 * 
	 * @author hangum
	 *
	 */
	public static enum TEMPLATE_TYPE {
		BASIC,
		INTERMEDIATE,
		ADVANCE
	}
	
	/** 
	 * define kpi type 
	 */
	public static enum KPI_TYPE {
		SECURITY,
		PERFORMANCE,
		SYSTEM,
		OTHERS
	}
	
	/**
	 * Define access type
	 * 
	 * @author hangum
	 *
	 */
	public static enum ACCESS_TYPE {
		SQL
//		AGENT,
//		PLSQL,
//		RestAPI,
//		CLASS
	};

	/**
	 * DEFINE MONITORING TYPE
	 * 
	 * @author hangum
	 *
	 */
	public static enum MONITORING_TYPE {
		LIVE,
		CONNECTION,
		CPU,
		DISK,
		GENERAL_LOG,
		NETWORK_IN,
		NETWORK_OUT,
		PROCESS,
		SESSION_LIST,
		STATEMENT_STATUS,
		SLAVE_STATUS,
		SLOW_QUERY
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
		KILL_AFTER_EMAIL,
		PUSH,
		PUSH_EMAIL
	}
	
}
