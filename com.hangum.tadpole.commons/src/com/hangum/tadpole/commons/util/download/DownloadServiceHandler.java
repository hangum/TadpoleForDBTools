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
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.service.ServiceHandler;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;

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
		OutputStream os = null;
		try {
			String contentType = getContentType().equals("") ? "text/html" : getContentType();

			// Set response headers
			resp.setContentType(contentType);
			resp.setContentLength(getByteContent().length);
			resp.setHeader("Content-Disposition", "attachment; filename=" + "\"" + getName() + "\"" + PublicTadpoleDefine.SQL_DELIMITER);
			resp.flushBuffer();
			// Copy documentation to responce's output stream.
			os = resp.getOutputStream();
			os.write(getByteContent());
			os.flush();
		} catch (Exception e) {
			logger.error("download make exception", e);
			throw new IllegalArgumentException("Download failed. Exception: " + e.getLocalizedMessage());
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				// Ignore
			}
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
