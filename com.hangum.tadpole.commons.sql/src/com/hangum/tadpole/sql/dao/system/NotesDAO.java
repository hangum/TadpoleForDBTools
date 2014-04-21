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
import java.sql.Timestamp;

/**
 * notes
 * 
 * @author hangum
 *
 */
public class NotesDAO {
	int seq;
	String types;
	
	int sender_seq;
	String sendUserId;
	
	int receiver_seq;
	String receiveUserId;
	
	Timestamp sender_date;
	Timestamp receiver_date;
	String is_read;
	String is_system_read;
	String sender_delyn;
	String receiver_delyn;
	
	String delYn;
	String create_time;
	
	String title;
	String contents = "";
	
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

	public int getSender_seq() {
		return sender_seq;
	}

	public void setSender_seq(int sender_seq) {
		this.sender_seq = sender_seq;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public int getReceiver_seq() {
		return receiver_seq;
	}

	public void setReceiver_seq(int receiver_seq) {
		this.receiver_seq = receiver_seq;
	}

	public String getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	/**
	 * @return the sender_date
	 */
	public final Timestamp getSender_date() {
		return sender_date;
	}

	/**
	 * @param sender_date the sender_date to set
	 */
	public final void setSender_date(Timestamp sender_date) {
		this.sender_date = sender_date;
	}

	/**
	 * @return the receiver_date
	 */
	public final Timestamp getReceiver_date() {
		return receiver_date;
	}

	/**
	 * @param receiver_date the receiver_date to set
	 */
	public final void setReceiver_date(Timestamp receiver_date) {
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIs_system_read() {
		return is_system_read;
	}

	public void setIs_system_read(String is_system_read) {
		this.is_system_read = is_system_read;
	}

}
