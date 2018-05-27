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
package com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others.dao;

import java.util.ArrayList;
import java.util.List;

import com.hangum.tadpole.engine.query.dao.system.ExternalBrowserInfoDAO;

/**
 * Others Database Connection information dialog
 * 
 * @author hangum
 *
 */
public class OthersConnectionInfoDAO {
	 /** 히스토리허브에서 원본 위치(yes: tadpole, NO: 원본디비에 위치) */
	boolean isHistoryHubLocation = true;
	
	boolean isReadOnlyConnection = false;
	boolean isAutoCommit = false;
	boolean isShowTables = false;
	
	boolean isProfiling = false;
	boolean isDMLStatement = false;
	
//	boolean isTableFilter = false;
//	String strTableFilterInclude = "";
//	String strTableFilterExclude = "";
	
//	boolean isVisible = true;
	boolean isSummaryReport = false;
	
	boolean isMonitoring = false;
	
	/** 시스템 브라우저에서 사용 할 url을 기록합니다 */
	boolean isExterBrowser = false;
	List<ExternalBrowserInfoDAO> listExterBroswer = new ArrayList<ExternalBrowserInfoDAO>();

	public OthersConnectionInfoDAO() {
	}
	
	/**
	 * @return the isHistoryHubLocation
	 */
	public boolean isHistoryHubLocation() {
		return isHistoryHubLocation;
	}

	/**
	 * @param isHistoryHubLocation the isHistoryHubLocation to set
	 */
	public void setHistoryHubLocation(boolean isHistoryHubLocation) {
		this.isHistoryHubLocation = isHistoryHubLocation;
	}

	/**
	 * @return the isReadOnlyConnection
	 */
	public boolean isReadOnlyConnection() {
		return isReadOnlyConnection;
	}

	/**
	 * @param isReadOnlyConnection the isReadOnlyConnection to set
	 */
	public void setReadOnlyConnection(boolean isReadOnlyConnection) {
		this.isReadOnlyConnection = isReadOnlyConnection;
	}

	/**
	 * @return the isAutoCommit
	 */
	public boolean isAutoCommit() {
		return isAutoCommit;
	}

	/**
	 * @param isAutoCommit the isAutoCommit to set
	 */
	public void setAutoCommit(boolean isAutoCommit) {
		this.isAutoCommit = isAutoCommit;
	}

//	/**
//	 * @return the isTableFilter
//	 */
//	public boolean isTableFilter() {
//		return isTableFilter;
//	}
//
//	/**
//	 * @param isTableFilter the isTableFilter to set
//	 */
//	public void setTableFilter(boolean isTableFilter) {
//		this.isTableFilter = isTableFilter;
//	}
//
//	/**
//	 * @return the strTableFilterInclude
//	 */
//	public String getStrTableFilterInclude() {
//		return strTableFilterInclude;
//	}
//
//	/**
//	 * @param strTableFilterInclude the strTableFilterInclude to set
//	 */
//	public void setStrTableFilterInclude(String strTableFilterInclude) {
//		this.strTableFilterInclude = strTableFilterInclude;
//	}
//
//	/**
//	 * @return the strTableFilterExclude
//	 */
//	public String getStrTableFilterExclude() {
//		return strTableFilterExclude;
//	}
//
//	/**
//	 * @param strTableFilterExclude the strTableFilterExclude to set
//	 */
//	public void setStrTableFilterExclude(String strTableFilterExclude) {
//		this.strTableFilterExclude = strTableFilterExclude;
//	}

	/**
	 * @return the isProfiling
	 */
	public boolean isProfiling() {
		return isProfiling;
	}

	/**
	 * @param isProfiling the isProfiling to set
	 */
	public void setProfiling(boolean isProfiling) {
		this.isProfiling = isProfiling;
	}

	/**
	 * @return the isDMLStatement
	 */
	public boolean isDMLStatement() {
		return isDMLStatement;
	}

	/**
	 * @param isDMLStatement the isDMLStatement to set
	 */
	public void setDMLStatement(boolean isDMLStatement) {
		this.isDMLStatement = isDMLStatement;
	}

	/**
	 * @return the isShowTables
	 */
	public boolean isShowTables() {
		return isShowTables;
	}

	/**
	 * @param isShowTables the isShowTables to set
	 */
	public void setShowTables(boolean isShowTables) {
		this.isShowTables = isShowTables;
	}
	
	/**
	 * @return the isExterBrowser
	 */
	public boolean isExterBrowser() {
		return isExterBrowser;
	}

	/**
	 * @param isExterBrowser the isExterBrowser to set
	 */
	public void setExterBrowser(boolean isExterBrowser) {
		this.isExterBrowser = isExterBrowser;
	}

	/**
	 * @return the listExterBroswer
	 */
	public List<ExternalBrowserInfoDAO> getListExterBroswer() {
		return listExterBroswer;
	}

	/**
	 * @param listExterBroswer the listExterBroswer to set
	 */
	public void setListExterBroswer(List<ExternalBrowserInfoDAO> listExterBroswer) {
		this.listExterBroswer = listExterBroswer;
	}

//	public boolean isVisible() {
//		return isVisible;
//	}
//
//	public void setVisible(boolean isVisible) {
//		this.isVisible = isVisible;
//	}

	public boolean isSummaryReport() {
		return isSummaryReport;
	}

	public void setSummaryReport(boolean isSummaryReport) {
		this.isSummaryReport = isSummaryReport;
	}

	/**
	 * @return the isMonitoring
	 */
	public boolean isMonitoring() {
		return isMonitoring;
	}

	/**
	 * @param isMonitoring the isMonitoring to set
	 */
	public void setMonitoring(boolean isMonitoring) {
		this.isMonitoring = isMonitoring;
	}
	
}
