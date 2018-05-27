package com.hangum.tadpole.engine.query.dao.system;

import java.sql.Timestamp;

import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * UserLoginHistory table dao
 * 
 * succes_yn, fail_reason 컬럼 추가 - 17.1.16 로그인 실패 이력 확인
 * 
 * @author hangum
 *
 */
public class UserLoginHistoryDAO {
	
	int seq;
	int user_seq;
	String login_ip;
	Timestamp connet_time;
	Timestamp disconnect_time;
	
	String sqliteConnet_time;
	String sqliteDisconnect_time;
	
	String succes_yn = PublicTadpoleDefine.YES_NO.YES.name();
	String fail_reason;
	
	public UserLoginHistoryDAO() {
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
	 * @return the user_seq
	 */
	public int getUser_seq() {
		return user_seq;
	}

	/**
	 * @param user_seq the user_seq to set
	 */
	public void setUser_seq(int user_seq) {
		this.user_seq = user_seq;
	}

	/**
	 * @return the login_ip
	 */
	public String getLogin_ip() {
		return login_ip;
	}

	/**
	 * @param login_ip the login_ip to set
	 */
	public void setLogin_ip(String login_ip) {
		this.login_ip = login_ip;
	}

	/**
	 * @return the connet_time
	 */
	public Timestamp getConnet_time() {
		return connet_time;
	}

	/**
	 * @param connet_time the connet_time to set
	 */
	public void setConnet_time(Timestamp connet_time) {
		this.connet_time = connet_time;
	}

	/**
	 * @return the disconnect_time
	 */
	public Timestamp getDisconnect_time() {
		return disconnect_time;
	}

	/**
	 * @param disconnect_time the disconnect_time to set
	 */
	public void setDisconnect_time(Timestamp disconnect_time) {
		this.disconnect_time = disconnect_time;
	}

	/**
	 * @return the sqliteConnet_time
	 */
	public String getSqliteConnet_time() {
		return sqliteConnet_time;
	}

	/**
	 * @param sqliteConnet_time the sqliteConnet_time to set
	 */
	public void setSqliteConnet_time(String sqliteConnet_time) {
		this.sqliteConnet_time = sqliteConnet_time;
	}

	/**
	 * @return the sqliteDisconnect_time
	 */
	public String getSqliteDisconnect_time() {
		return sqliteDisconnect_time;
	}

	/**
	 * @param sqliteDisconnect_time the sqliteDisconnect_time to set
	 */
	public void setSqliteDisconnect_time(String sqliteDisconnect_time) {
		this.sqliteDisconnect_time = sqliteDisconnect_time;
	}

	/**
	 * @return the succes_yn
	 */
	public String getSucces_yn() {
		return succes_yn;
	}

	/**
	 * @param succes_yn the succes_yn to set
	 */
	public void setSucces_yn(String succes_yn) {
		this.succes_yn = succes_yn;
	}

	/**
	 * @return the fail_reason
	 */
	public String getFail_reason() {
		return fail_reason;
	}

	/**
	 * @param fail_reason the fail_reason to set
	 */
	public void setFail_reason(String fail_reason) {
		this.fail_reason = fail_reason;
	}

}
