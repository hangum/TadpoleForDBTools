/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.commons.libs.core.mails.dto;

/**
 * Email dao
 * 
 * @author hangum
 *
 */
public class EmailDTO {
	
	String to = "";
	
	String subject = "";
	String content = "";

	public EmailDTO() {
	}

	/**
	 * @return the to
	 */
	public final String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public final void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the subject
	 */
	public final String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public final void setSubject(String subject) {
		this.subject = "[TadpoleHUB] " + subject;
	}

	/**
	 * @return the content
	 */
	public final String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public final void setContent(String content) {
		this.content = content;
	}
	
	
}
