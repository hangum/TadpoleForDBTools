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
package com.hangum.tadpole.dao.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hangum.tadpole.dao.ManagerListDTO;
import com.hangum.tadpole.util.secret.EncryptiDecryptUtil;

/**
 * 사용자 디비

   ps)
   특이 password의 get메소드는 다음이어야 합니다. 
   이유는, 디비에 암호화된 패스워드가 들어 있기 때문에 그렇고..
   
 	public String getPasswd() {
		return EncryptiDecryptUtil.decryption(passwd);
	}

 * 
 * @author hangum
 *
 */
public class UserDBDAO {
	protected int seq;
	protected int user_seq;
    
	protected String group_name = "";
	
	protected String operation_type = "";
    
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
	protected String ext4 = "";
	protected String ext5 = "";
	protected String ext6 = "";
	protected String ext7 = "";
	protected String ext8 = "";
	protected String ext9 = "";
	protected String ext10 = "";
	
	protected String is_profile = "";
    protected int profile_select_mill = -1;
    
    protected String is_readOnlyConnect = "";
	protected String is_autocmmit = "";
	
	protected String is_table_filter = "";

	protected String table_filter_include = "";
	protected String table_filter_exclude = "";
    
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

	/**
	 * some special code
	 * @return
	 */
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

	
	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getExt5() {
		return ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}

	public String getExt6() {
		return ext6;
	}

	public void setExt6(String ext6) {
		this.ext6 = ext6;
	}

	public String getExt7() {
		return ext7;
	}

	public void setExt7(String ext7) {
		this.ext7 = ext7;
	}

	public String getExt8() {
		return ext8;
	}

	public void setExt8(String ext8) {
		this.ext8 = ext8;
	}

	public String getExt9() {
		return ext9;
	}

	public void setExt9(String ext9) {
		this.ext9 = ext9;
	}

	public String getExt10() {
		return ext10;
	}

	public void setExt10(String ext10) {
		this.ext10 = ext10;
	}


	public String getIs_readOnlyConnect() {
		return is_readOnlyConnect;
	}

	public void setIs_readOnlyConnect(String is_readOnlyConnect) {
		this.is_readOnlyConnect = is_readOnlyConnect;
	}

	public String getIs_autocmmit() {
		return is_autocmmit;
	}

	public void setIs_autocmmit(String is_autocmmit) {
		this.is_autocmmit = is_autocmmit;
	}

	public String getTable_filter_include() {
		return table_filter_include;
	}

	public void setTable_filter_include(String table_filter_include) {
		this.table_filter_include = table_filter_include;
	}

	public String getTable_filter_exclude() {
		return table_filter_exclude;
	}

	public void setTable_filter_exclude(String table_filter_exclude) {
		this.table_filter_exclude = table_filter_exclude;
	}

	public void setListUserDBGroup(List<UserDBDAO> listUserDBGroup) {
		this.listUserDBGroup = listUserDBGroup;
	}
	
	/**
	 * @return the is_table_filter
	 */
	public String getIs_table_filter() {
		return is_table_filter;
	}

	/**
	 * @param is_table_filter the is_table_filter to set
	 */
	public void setIs_table_filter(String is_table_filter) {
		this.is_table_filter = is_table_filter;
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
