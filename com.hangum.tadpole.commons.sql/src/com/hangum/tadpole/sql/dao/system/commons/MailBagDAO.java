package com.hangum.tadpole.sql.dao.system.commons;

import java.sql.Timestamp;

/**
 * 보내야 하는 mail 
 * 
 * @author hangum
 *
 */
public class MailBagDAO {
	int seq;
	String receiver;
	String title;
	String content; 
	String is_send;
	String is_success;
	Timestamp create_date;
	Timestamp send_date;
	String delyn;
	
	public MailBagDAO() {
	}

	/**
	 * @return the seq
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}

	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the is_send
	 */
	public String getIs_send() {
		return is_send;
	}

	/**
	 * @param is_send the is_send to set
	 */
	public void setIs_send(String is_send) {
		this.is_send = is_send;
	}

	/**
	 * @return the is_success
	 */
	public String getIs_success() {
		return is_success;
	}

	/**
	 * @param is_success the is_success to set
	 */
	public void setIs_success(String is_success) {
		this.is_success = is_success;
	}

	/**
	 * @return the create_date
	 */
	public Timestamp getCreate_date() {
		return create_date;
	}

	/**
	 * @param create_date the create_date to set
	 */
	public void setCreate_date(Timestamp create_date) {
		this.create_date = create_date;
	}

	/**
	 * @return the send_date
	 */
	public Timestamp getSend_date() {
		return send_date;
	}

	/**
	 * @param send_date the send_date to set
	 */
	public void setSend_date(Timestamp send_date) {
		this.send_date = send_date;
	}

	/**
	 * @return the delyn
	 */
	public String getDelyn() {
		return delyn;
	}

	/**
	 * @param delyn the delyn to set
	 */
	public void setDelyn(String delyn) {
		this.delyn = delyn;
	}
	
}
