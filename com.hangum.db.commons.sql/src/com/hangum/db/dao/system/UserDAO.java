package com.hangum.db.dao.system;

/**
 * user 정보 정의
 * 
 * @author hangum
 *
 */
public class UserDAO {
	int seq;
	int group_seq;
	String email;
	String passwd;
	String name;
	String user_type;
	String approval_yn;
	String delYn;
	String create_time;
	
	public UserDAO() {
	}
	
	public UserDAO(int groupSeq, String email, String passwd, String name, String user_type, String approval_yn) {
		this.group_seq = groupSeq;
		this.email = email;
		this.passwd = passwd;
		this.name = name;
		this.user_type = user_type;
		this.approval_yn = approval_yn;
	}

	public UserDAO(String email, String passwd) {
		this.email = email;
		this.passwd = passwd;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public int getGroup_seq() {
		return group_seq;
	}

	public void setGroup_seq(int group_seq) {
		this.group_seq = group_seq;
	}

	public String getApproval_yn() {
		return approval_yn;
	}

	public void setApproval_yn(String approval_yn) {
		this.approval_yn = approval_yn;
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

}
