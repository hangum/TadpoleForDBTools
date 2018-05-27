/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.query.dao.system.userdb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hangum.tadpole.commons.util.DateUtil;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;
import com.tadpole.common.define.core.define.PublicTadpoleDefine.OBJECT_TYPE;

/**
 * Define Tadpole DB Hub db.
 * 
 * @author hangum
 *
 */
public class TDBDBDAO extends ManagerListViewDAO {
	
	/** 디비 객체의 사영 여부 */
	protected boolean _isUseEnable = true;
	/** {@code _isUseEnable} 가 false 일 경우 시스템 메시지에서 보여줄 메시지를 기록한다. */
	protected String  _sysMessage = PublicTadpoleDefine.AUTH_CODE_DEFINE.VALID.name();
	
	/** schema list */
	protected List<String> schemas = new ArrayList<String>();
	
	/** This variable is schema assist */
	protected String schemaListSeparator = "";
	/** This variable is content assist */
	protected String tableListSeparator = null;
	/** This variable is content assit */
	protected String viewListSeparator = null;
	protected String functionLisstSeparator = null;
	
	/** 
	 * db object cache 
	 *
	 * {@code PublicTadpoleDefine#OBJECT_TYPE}, <SchemaName, Object> 
	 */
	protected Map<OBJECT_TYPE, Map<String, List<?>>> dbObjectCache= new HashMap<OBJECT_TYPE, Map<String, List<?>>>(); 
	
	// TadpoleUserDbRoleDAO start ======================================
	protected int role_seq;
	protected String role_id;
	protected String access_ip;
	protected String is_resource_download;
	// default value is 00:00
	protected Timestamp terms_of_use_starttime 	= new Timestamp(System.currentTimeMillis());

	// default value is 100 years after
	protected  Timestamp terms_of_use_endtime  	= new Timestamp(DateUtil.afterMonthToMillsMonth(24));
	// user db role description
	protected String description = "";
	
	public TDBDBDAO() {
	}
	
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
	
	/**
	 * @return the access_ip
	 */
	public String getAccess_ip() {
		return access_ip;
	}

	/**
	 * @param access_ip the access_ip to set
	 */
	public void setAccess_ip(String access_ip) {
		this.access_ip = access_ip;
	}

	/**
	 * @return the is_resource_download
	 */
	public String getIs_resource_download() {
		return is_resource_download;
	}
	
	/**
	 * @return the terms_of_use_starttime
	 */
	public Timestamp getTerms_of_use_starttime() {
		return terms_of_use_starttime;
	}

	/**
	 * @param terms_of_use_starttime the terms_of_use_starttime to set
	 */
	public void setTerms_of_use_starttime(Timestamp terms_of_use_starttime) {
		this.terms_of_use_starttime = terms_of_use_starttime;
	}

	/**
	 * @return the terms_of_use_endtime
	 */
	public Timestamp getTerms_of_use_endtime() {
		return terms_of_use_endtime;
	}

	/**
	 * @param terms_of_use_endtime the terms_of_use_endtime to set
	 */
	public void setTerms_of_use_endtime(Timestamp terms_of_use_endtime) {
		this.terms_of_use_endtime = terms_of_use_endtime;
	}

	/**
	 * @param is_resource_download the is_resource_download to set
	 */
	public void setIs_resource_download(String is_resource_download) {
		this.is_resource_download = is_resource_download;
	}	
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	// ====================================== end
	
	/**
	 * @return the schemaListSeparator
	 */
	public String getSchemaListSeparator() {
		return schemaListSeparator;
	}

	/**
	 * @param schemaListSeparator the schemaListSeparator to set
	 */
	public void setSchemaListSeparator(String schemaListSeparator) {
		this.schemaListSeparator = schemaListSeparator;
	}

	/**
	 * @return the viewListSeparator
	 */
	public String getViewListSeparator() {
		return viewListSeparator;
	}

	/**
	 * @param viewListSeparator the viewListSeparator to set
	 */
	public void setViewListSeparator(String viewListSeparator) {
		this.viewListSeparator = viewListSeparator;
	}

	/**
	 * @return the tableListSeparator
	 */
	public String getTableListSeparator() {
		return tableListSeparator;
	}

	/**
	 * @param tableListSeparator the tableListSeparator to set
	 */
	public void setTableListSeparator(String tableListSeparator) {
		this.tableListSeparator = tableListSeparator;
	}
	
	/**
	 * get db object
	 * 
	 * @param objecType
	 * @param schemaName
	 * @return
	 */
	public List<?> getDBObject(OBJECT_TYPE objecType, String schemaName) {
		Map<String, List<?>> mapDBObject = dbObjectCache.get(objecType);
		if(mapDBObject == null) {
			return new ArrayList();
		} else {
			List listObject = mapDBObject.get(schemaName);
			if(listObject == null) return new ArrayList();
			else return listObject;
		}
	}
	
	/**
	 * set db object
	 * 
	 * @param objecType
	 * @param schemaName
	 * @param objectList
	 */
	public void setDBObject(OBJECT_TYPE objecType, String schemaName, List objectList) {
		Map<String, List<?>> mapDBObject = dbObjectCache.get(objecType);
		if(mapDBObject == null) {
			Map<String, List<?>> map = new HashMap<String, List<?>>();
			map.put(schemaName, objectList);
		
			dbObjectCache.put(objecType, map);
		} else {
			mapDBObject.put(schemaName, objectList);
		}
	}

	/**
	 * @return the functionLisstSeparator
	 */
	public String getFunctionLisstSeparator() {
		return functionLisstSeparator;
	}

	/**
	 * @param functionLisstSeparator the functionLisstSeparator to set
	 */
	public void setFunctionLisstSeparator(String functionLisstSeparator) {
		this.functionLisstSeparator = functionLisstSeparator;
	}

	/**
	 * get schemas
	 * @return
	 */
	public List<String> getSchemas() {
		return schemas;
	}

	/**
	 * set schema 
	 * @param schemas
	 */
	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}

	/**
	 * list schema list
	 * @param strSchema
	 */
	public void addSchema(String strSchema) {
		this.schemas.add(strSchema);
	}

	/**
	 * @return the _isUseEnable
	 */
	public boolean is_isUseEnable() {
		return _isUseEnable;
	}

	/**
	 * @param _isUseEnable the _isUseEnable to set
	 */
	public void set_isUseEnable(boolean _isUseEnable) {
		this._isUseEnable = _isUseEnable;
	}

	/**
	 * @return the _sysMessage
	 */
	public String get_sysMessage() {
		return _sysMessage;
	}

	/**
	 * @param _sysMessage the _sysMessage to set
	 */
	public void set_sysMessage(String _sysMessage) {
		this._sysMessage = _sysMessage;
	}
	
	
}
