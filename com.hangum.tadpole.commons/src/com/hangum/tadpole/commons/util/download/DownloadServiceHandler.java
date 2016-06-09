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
package com.hangum.tadpole.commons.util.download;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.service.ServiceHandler;

/**
 * 쿼리결과, 히스토리 다운로드 서비스
 * 
 * @author hangum
 * 
 */
public class DownloadServiceHandler implements ServiceHandler {
	public static final String ID = "com.hangum.db.browser.rap.core.editors.main.internals.DownloadServiceHandler";
	private static final Logger logger = Logger
			.getLogger(DownloadServiceHandler.class);
	private String name;
	private String contentType = "";
	private byte[] byteContent;

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		makeHtmlFile(response);
	}

	private void makeHtmlFile(HttpServletResponse resp) {
		try {
			resp.setContentType("application/octet-stream");
//			resp.setCharacterEncoding("UTF-8");
			resp.setContentLength(getByteContent().length);
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + getName() + "\";");
			resp.flushBuffer();
			resp.getOutputStream().write(getByteContent());
		} catch (Exception e) {
			logger.error("download make exception", e);
			throw new IllegalArgumentException("Download failed. Exception: " + e.getLocalizedMessage());
		}
	}

	public String getId() {
		return ID + hashCode();
	}

	public byte[] getByteContent() {
		return byteContent;
	}

	public void setByteContent(byte[] byteContent) {
		this.byteContent = byteContent;
	}

	public void setName(String name) {
		try {
			name = URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
