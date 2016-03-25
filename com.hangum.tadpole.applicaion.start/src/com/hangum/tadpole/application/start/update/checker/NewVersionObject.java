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
package com.hangum.tadpole.application.start.update.checker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * new version object
 * 
 * @author hangum
 *
 */
public class NewVersionObject {
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("majorVer")
	@Expose
	private String majorVer;
	@SerializedName("subVer")
	@Expose
	private String subVer;
	@SerializedName("date")
	@Expose
	private String date;
	@SerializedName("download_url")
	@Expose
	private String downloadUrl;
	@SerializedName("info_url")
	@Expose
	private String infoUrl;

	public NewVersionObject() {
	}
	
	/**
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The majorVer
	 */
	public String getMajorVer() {
		return majorVer;
	}

	/**
	 * 
	 * @param majorVer
	 */
	public void setMajorVer(String majorVer) {
		this.majorVer = majorVer;
	}

	/**
	 * 
	 * @return The subVer
	 */
	public String getSubVer() {
		return subVer;
	}

	/**
	 * 
	 * @param subVer
	 */
	public void setSubVer(String subVer) {
		this.subVer = subVer;
	}

	/**
	 * 
	 * @return The date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * 
	 * @param date
	 *            The date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * 
	 * @return The downloadUrl
	 */
	public String getDownloadUrl() {
		return downloadUrl;
	}

	/**
	 * 
	 * @param downloadUrl
	 */
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	/**
	 * 
	 * @return The infoUrl
	 */
	public String getInfoUrl() {
		return infoUrl;
	}

	/**
	 * 
	 * @param infoUrl
	 */
	public void setInfoUrl(String infoUrl) {
		this.infoUrl = infoUrl;
	}
}
