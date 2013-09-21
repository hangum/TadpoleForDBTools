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

import com.hangum.tadpole.cipher.core.manager.CipherManager;
import com.hangum.tadpole.dao.ManagerListDTO;
import com.hangum.tadpole.system.permission.PermissionChecker;

/**
 * <pre>
 * 사용자 디비
   
   1. 특이 url, db, host, port, user password의 get메소드는 다음이어야 합니다. 
   이유는, 디비에 암호화된 패스워드가 들어 있기 때문에 그렇고..
   
 	public String getPasswd() {
		return EncryptiDecryptUtil.decryption(passwd);
	}
   2. DB에 데이터를 넣기 위해서는 {@code UserDBOriginalDAO} 를 참고합니다.
	</pre>
 * 
 * @author hangum
 *
 */
public class UserDBDAO {
	protected int seq = -999;
	protected int user_seq;
	/** 외부 시스템 seq 현재는 amamzon rds seq*/
	protected int ext_seq = -999;
	protected int group_seq;
    
	protected String group_name = "";
	
	protected String operation_type = "";
    
	protected String dbms_types;
	protected String url;
	public String getShowUrl(String userType) {
		return PermissionChecker.isShow(userType)?getUrl():"jdbc:*************************";
	}
	
	protected String db;
	public String getShowDb(String userType) {
		return PermissionChecker.isShow(userType)?getDb():"********";
	}
	protected String display_name;
	protected String host;
	public String getShowHost(String userType) {
		return PermissionChecker.isShow(userType)?getHost():"***.***.***.***";
	}
	
	protected String port;
	public String getShowPort(String userType) {
		return PermissionChecker.isShow(userType)?getPort():"****";
	}
	
	protected String locale;
	protected String passwd;
	public String getShowPasswd(String userType) {
		return PermissionChecker.isShow(userType)?getPasswd():"********";
	}
	
	protected String users;
	public String getShowUsers(String userType) {
		return PermissionChecker.isShow(userType)?getUsers():"********";
	}
	
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
	protected String is_autocommit = "";
	protected String is_showtables = "";
	
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

	public int getExt_seq() {
		return ext_seq;
	}

	public void setExt_seq(int ext_seq) {
		this.ext_seq = ext_seq;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getUrl() {
		String urlDecrypt = url;
		try {
			urlDecrypt = CipherManager.getInstance().decryption(urlDecrypt);
		} catch(Exception e) {
			// ignore exception
		}
		
		return urlDecrypt;
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
		String hostDecrypt = host;
		try {
			hostDecrypt = CipherManager.getInstance().decryption(hostDecrypt);
		} catch(Exception e) {
			// ignore exception
		}
		
		return hostDecrypt;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		String portDecrypt = port;
		try {
			portDecrypt = CipherManager.getInstance().decryption(portDecrypt);
		} catch(Exception e) {
			// ignore exception
		}
		
		return portDecrypt;
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
		String passwdDecrypt = passwd;
		try {
			passwdDecrypt = CipherManager.getInstance().decryption(passwdDecrypt);
		} catch(Exception e) {
			// ignore exception
		}
		
		return passwdDecrypt;
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


	/**
	 * @return the dbms_types
	 */
	public String getDbms_types() {
		return dbms_types;
	}

	/**
	 * @param dbms_types the dbms_types to set
	 */
	public void setDbms_types(String dbms_types) {
		this.dbms_types = dbms_types;
	}

	public String getDb() {
		String dbDecrypt = db;
		try {
			dbDecrypt = CipherManager.getInstance().decryption(dbDecrypt);
		} catch(Exception e) {
			// ignore exception
		}
		
		return dbDecrypt;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getUsers() {
		String userDecrypt = users;
		try {
			userDecrypt = CipherManager.getInstance().decryption(userDecrypt);
		} catch(Exception e) {
			// ignore exception
		}
		
		return userDecrypt;
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

	/**
	 * @return the is_autocommit
	 */
	public String getIs_autocommit() {
		return is_autocommit;
	}

	/**
	 * @param is_autocommit the is_autocommit to set
	 */
	public void setIs_autocommit(String is_autocommit) {
		this.is_autocommit = is_autocommit;
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
	 * @return the is_showtables
	 */
	public String getIs_showtables() {
		return is_showtables;
	}

	/**
	 * @param is_showtables the is_showtables to set
	 */
	public void setIs_showtables(String is_showtables) {
		this.is_showtables = is_showtables;
	}

	/**
	 * @param is_table_filter the is_table_filter to set
	 */
	public void setIs_table_filter(String is_table_filter) {
		this.is_table_filter = is_table_filter;
	}
}
