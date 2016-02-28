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
package com.hangum.tadpole.engine.query.dao.system;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.hangum.tadpole.cipher.core.manager.CipherManager;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.system.accesscontrol.DBAccessControlDAO;
import com.hangum.tadpole.engine.query.dao.system.userdb.TDBDBDAO;

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
public class UserDBDAO extends TDBDBDAO implements Cloneable {	
	/** 
	 * pgsql, oracle, mssql은 인스턴스 아래에 schema가 있습니다. 
	 */
	protected String schema = "";
	
	// TadpoleUserDbRoleDAO start ======================================
	protected int role_seq;
	protected String role_id;
	
	/**
	 * @return the role_seq
	 */
	public int getRole_seq() {
		return role_seq;
	}

	/**
	 * @param role_seq the role_seq to set
	 */
	public void setRole_seq(int role_seq) {
		this.role_seq = role_seq;
	}

	/**
	 * @return the role_id
	 */
	public String getRole_id() {
		return role_id;
	}

	/**
	 * @param role_id the role_id to set
	 */
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
	// ====================================== end

	protected int seq = -999;
	protected int user_seq = -1;
	
	protected String group_name = "";
	protected String operation_type = "";
	protected String dbms_type;
	protected String url;
	protected String url_user_parameter = "";
	
	protected List<TadpoleUserDbRoleDAO> listChildren = new ArrayList<TadpoleUserDbRoleDAO>();
	
	public String getUrl(String userType) {
		return PermissionChecker.isShow(userType)?getUrl():"jdbc:********************";
	}
	
	protected String db;
	public String getDb(String userType) {
		return PermissionChecker.isShow(userType)?getDb():"********";
	}
	protected String display_name;
	protected String host;
	public String getHost(String userType) {
		return PermissionChecker.isShow(userType)?getHost():"***.***.***.***";
	}
	
	protected String port;
	public String getPort(String userType) {
		return PermissionChecker.isShow(userType)?getPort():"****";
	}
	
	protected String locale;
	protected String passwd;
	public String getPasswd(String userType) {
		return PermissionChecker.isShow(userType)?getPasswd():"********";
	}
	
	protected String users;
	public String getUsers(String userType) {
		return PermissionChecker.isShow(userType)?getUsers():"********";
	}
	
	protected Timestamp create_time;
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
	
	protected String is_external_browser = "";
	protected List<ExternalBrowserInfoDAO> listExternalBrowserdao = new ArrayList<ExternalBrowserInfoDAO>();
    
    // 운영서버일 경우 DML 문 실행시 YES, NO 묻기
    protected String question_dml = "";
    
    /** 디비의 버전 정보 */
    protected String version;
    
    /** 디비 정보를 커넥션 메이저에 보일 것인지 	- 2014.05.22 hangum */
    protected String is_visible 		= PublicTadpoleDefine.YES_NO.YES.name();
    /** 디비의 요약 정보를 보낼 것인지 			- 2014.05.22 hangum */
    protected String is_summary_report 	= PublicTadpoleDefine.YES_NO.YES.name();
    
    /** Is DB monitoring? */
    protected String is_monitoring 		= PublicTadpoleDefine.YES_NO.YES.name();
    
    /** is db lock? */
    protected String is_lock			= PublicTadpoleDefine.YES_NO.NO.name();
    
    /** db access control */
    protected DBAccessControlDAO dbAccessCtl = new DBAccessControlDAO();
    
    public UserDBDAO() {
	}
   
    public DBDefine getDBDefine() {
    	return DBDefine.getDBDefine(this);
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
		String urlDecrypt = url;
		try {
			urlDecrypt = CipherManager.getInstance().decryption(urlDecrypt);
		} catch(Exception e) {
			// ignore exception
		}
		
		return urlDecrypt;
	}
	
	/**
	 * @return the url_user_parameter
	 */
	public String getUrl_user_parameter() {
		return url_user_parameter;
	}

	/**
	 * @param url_user_parameter the url_user_parameter to set
	 */
	public void setUrl_user_parameter(String url_user_parameter) {
		this.url_user_parameter = url_user_parameter;
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

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
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
	public String getDbms_type() {
		return dbms_type;
	}

	/**
	 * @param dbms_types the dbms_types to set
	 */
	public void setDbms_type(String dbms_types) {
		this.dbms_type = dbms_types;
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
	 * @return the is_external_browser
	 */
	public String getIs_external_browser() {
		return is_external_browser;
	}

	/**
	 * @param is_external_browser the is_external_browser to set
	 */
	public void setIs_external_browser(String is_external_browser) {
		this.is_external_browser = is_external_browser;
	}

	/**
	 * @return the listExternalBrowserdao
	 */
	public List<ExternalBrowserInfoDAO> getListExternalBrowserdao() {
		return listExternalBrowserdao;
	}
	
	/**
	 * @return the is_monitoring
	 */
	public String getIs_monitoring() {
		return is_monitoring;
	}

	/**
	 * @param is_monitoring the is_monitoring to set
	 */
	public void setIs_monitoring(String is_monitoring) {
		this.is_monitoring = is_monitoring;
	}

	/**
	 * @param listExternalBrowserdao the listExternalBrowserdao to set
	 */
	public void setListExternalBrowserdao(
			List<ExternalBrowserInfoDAO> listExternalBrowserdao) {
		this.listExternalBrowserdao = listExternalBrowserdao;
	}

	public String getIs_visible() {
		return is_visible;
	}

	public void setIs_visible(String is_visible) {
		this.is_visible = is_visible;
	}

	public String getIs_summary_report() {
		return is_summary_report;
	}

	public void setIs_summary_report(String is_summary_report) {
		this.is_summary_report = is_summary_report;
	}
	
	public List<TadpoleUserDbRoleDAO> getListChildren() {
		return listChildren;
	}
	
	public void setListChildren(List<TadpoleUserDbRoleDAO> listChildren) {
		this.listChildren = listChildren;
	}

	/**
	 * @return the is_lock
	 */
	public String getIs_lock() {
		return is_lock;
	}

	/**
	 * @param is_lock the is_lock to set
	 */
	public void setIs_lock(String is_lock) {
		this.is_lock = is_lock;
	}

	/**
	 * @return the dbAccessCtl
	 */
	public DBAccessControlDAO getDbAccessCtl() {
		return dbAccessCtl;
	}

	/**
	 * @param dbAccessCtl the dbAccessCtl to set
	 */
	public void setDbAccessCtl(DBAccessControlDAO dbAccessCtl) {
		this.dbAccessCtl = dbAccessCtl;
	}
	
	/**
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * @param schema the schema to set
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof UserDBDAO) {
			UserDBDAO userDB = (UserDBDAO)obj;
			return userDB.getSeq() == getSeq();
		}
		
		return super.equals(obj);
	}

}
