/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.application.start.dialog.update;

/**
 * new version object 
 * 
 * @author hangum
 *
 */
public class NewVersionObject {
	String name;
	String majorVer;
	String subVer;
	String date;
	String download_url;
	String info_url;

	public NewVersionObject() {
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the majorVer
	 */
	public String getMajorVer() {
		return majorVer;
	}

	/**
	 * @param majorVer the majorVer to set
	 */
	public void setMajorVer(String majorVer) {
		this.majorVer = majorVer;
	}

	/**
	 * @return the subVer
	 */
	public String getSubVer() {
		return subVer;
	}

	/**
	 * @param subVer the subVer to set
	 */
	public void setSubVer(String subVer) {
		this.subVer = subVer;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the download_url
	 */
	public String getDownload_url() {
		return download_url;
	}

	/**
	 * @param download_url the download_url to set
	 */
	public void setDownload_url(String download_url) {
		this.download_url = download_url;
	}

	/**
	 * @return the info_url
	 */
	public String getInfo_url() {
		return info_url;
	}

	/**
	 * @param info_url the info_url to set
	 */
	public void setInfo_url(String info_url) {
		this.info_url = info_url;
	}
	
}
