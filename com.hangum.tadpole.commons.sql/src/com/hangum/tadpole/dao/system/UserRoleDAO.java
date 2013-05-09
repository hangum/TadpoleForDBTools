package com.hangum.tadpole.dao.system;

/**
 * user_role table dao
 * 
 * @author hangum
 *
 */
public class UserRoleDAO {

	int seq;
	int group_seq;
	int user_seq;
	String role_type;
	String name;
	String approval_yn;
	String delyn;
	
	public UserRoleDAO() {
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
	 * @return the group_seq
	 */
	public int getGroup_seq() {
		return group_seq;
	}

	/**
	 * @param group_seq the group_seq to set
	 */
	public void setGroup_seq(int group_seq) {
		this.group_seq = group_seq;
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
	 * @return the role_type
	 */
	public String getRole_type() {
		return role_type;
	}

	/**
	 * @param role_type the role_type to set
	 */
	public void setRole_type(String role_type) {
		this.role_type = role_type;
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

	/**
	 * @return the approval_yn
	 */
	public String getApproval_yn() {
		return approval_yn;
	}

	/**
	 * @param approval_yn the approval_yn to set
	 */
	public void setApproval_yn(String approval_yn) {
		this.approval_yn = approval_yn;
	}
	
}
