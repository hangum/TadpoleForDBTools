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
package com.hangum.tadpole.sql.dao.system;

import java.sql.Date;

/**
 * notes
 * 
 * @author hangum
 *
 */
public class NotesDAO {
	int seq;
	String types;
	
	int send_user_seq;
	String sendUserId;
	
	int receive_user_seq;
	String receiveUserId;
	
	Date sender_date;
	Date receiver_date;
	String is_read;
	String sender_delyn;
	String receiver_delyn;
	
	String delYn;
	String create_time;
	
	String contents;
	
	public NotesDAO() {
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public int getSend_user_seq() {
		return send_user_seq;
	}

	public void setSend_user_seq(int send_user_seq) {
		this.send_user_seq = send_user_seq;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public int getReceive_user_seq() {
		return receive_user_seq;
	}

	public void setReceive_user_seq(int receive_user_seq) {
		this.receive_user_seq = receive_user_seq;
	}

	public String getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public Date getSender_date() {
		return sender_date;
	}

	public void setSender_date(Date sender_date) {
		this.sender_date = sender_date;
	}

	public Date getReceiver_date() {
		return receiver_date;
	}

	public void setReceiver_date(Date receiver_date) {
		this.receiver_date = receiver_date;
	}

	public String getIs_read() {
		return is_read;
	}

	public void setIs_read(String is_read) {
		this.is_read = is_read;
	}

	public String getSender_delyn() {
		return sender_delyn;
	}

	public void setSender_delyn(String sender_delyn) {
		this.sender_delyn = sender_delyn;
	}

	public String getReceiver_delyn() {
		return receiver_delyn;
	}

	public void setReceiver_delyn(String receiver_delyn) {
		this.receiver_delyn = receiver_delyn;
	}

	public String getDelYn() {
		return delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}


}
