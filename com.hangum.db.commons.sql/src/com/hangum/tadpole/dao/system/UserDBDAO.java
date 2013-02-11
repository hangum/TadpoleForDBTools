/*******************************************************************************
\ * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.dao.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hangum.tadpole.dao.ManagerListDTO;

/**
 * 사용자 디비
 * 
 * @author hangum
 *
 */
public class UserDBDAO {
	protected int seq;
	protected int user_seq;
    
	protected String group_name = "";
	
	/** db type 
	* 000 :	운영 
	* 001 : 개발 
	* 002 :	기타  
	 */
	protected String operation_type = "001";
    
	protected String types;
	protected String url;
	protected String db;
	protected String display_name;
	protected String host;
	protected String port;
	protected String locale;
	protected String passwd;
	protected String users;
	protected Date create_time;
	protected String delYn;
	
	protected String ext1 = "";
	protected String ext2 = ""; 
	protected String ext3 = "";
	
	protected String is_profile = "";
    protected int profile_select_mill = -1;
    
    // 운영서버일 경우 DML 문 실행시 YES, NO 묻기
    protected String question_dml = "";
    
    protected ManagerListDTO parent;
    protected List<UserDBResourceDAO> listUserDBErd;
    
    /** 디비의 버전 정보 */
    protected String version;
    
    /** userdb를 그룹으로 표시 하고자 할때 사용합니다. 현재는 로그인창에서 디비 관리하면에서 사용. */
    protected List<UserDBDAO> listUserDBGroup = new ArrayList<UserDBDAO>();
    
    public UserDBDAO() {
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getUser_seq() {
		return user_seq;
	}

	public void setUser_seq(int user_seq) {
		this.user_seq = user_seq;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
    
	public ManagerListDTO getParent() {
		return parent;
	}
	
	public void setParent(ManagerListDTO parent) {
		this.parent = parent;
	}

	public List<UserDBResourceDAO> getListUserDBErd() {
		return listUserDBErd;
	}

	public void setListUserDBErd(List<UserDBResourceDAO> listUserDBErd) {
		this.listUserDBErd = listUserDBErd;
	}
	
	public String getDelYn() {
		return delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}
	
	public List<UserDBDAO> getListUserDBGroup() {
		return listUserDBGroup;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}
	
	public String getOperation_type() {
		return operation_type;
	}

	public void setOperation_type(String operation_type) {
		this.operation_type = operation_type;
	}

	public String getIs_profile() {
		return is_profile;
	}

	public void setIs_profile(String is_profile) {
		this.is_profile = is_profile;
	}

	public int getProfile_select_mill() {
		return profile_select_mill;
	}

	public void setProfile_select_mill(int profile_select_mill) {
		this.profile_select_mill = profile_select_mill;
	}

	public String getQuestion_dml() {
		return question_dml;
	}

	public void setQuestion_dml(String question_dml) {
		this.question_dml = question_dml;
	}

	@Override
	public String toString() {
		return "UserDBDAO [seq=" + seq + ", user_seq=" + user_seq
				+ ", group_name=" + group_name + ", operation_type="
				+ operation_type + ", types=" + types + ", url=" + url
				+ ", db=" + db + ", display_name=" + display_name + ", host="
				+ host + ", port=" + port + ", locale=" + locale + ", passwd="
				+ passwd + ", users=" + users + ", create_time=" + create_time
				+ ", delYn=" + delYn + ", ext1=" + ext1 + ", ext2=" + ext2
				+ ", ext3=" + ext3 + ", is_profile=" + is_profile
				+ ", profile_select_mill=" + profile_select_mill
				+ ", question_dml=" + question_dml + ", parent=" + parent
				+ ", listUserDBErd=" + listUserDBErd + ", version=" + version
				+ ", listUserDBGroup=" + listUserDBGroup + "]";
	}

}
