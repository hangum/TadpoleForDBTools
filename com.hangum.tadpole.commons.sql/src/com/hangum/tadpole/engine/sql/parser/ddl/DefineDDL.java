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
package com.hangum.tadpole.engine.sql.parser.ddl;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DDL_STATUS;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.QUERY_DDL_TYPE;

/**
 * define ddl
 * 
 * @author hangum
 *
 */
public enum DefineDDL {
	TABLE_CREATE("CREATE\\s+TABLE\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.TABLE, QUERY_DDL_STATUS.CREATE),
	TABLE_ALTER("ALTER\\s+TABLE\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.TABLE, QUERY_DDL_STATUS.ALTER),
	TABLE_DROP("DROP\\s+TABLE\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.TABLE, QUERY_DDL_STATUS.DROP),
	
	VIEW_CREATE("CREATE\\s+VIEW\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.VIEW, QUERY_DDL_STATUS.CREATE),
	VIEW_CREATE_REPLACE("CREATE\\s+OR\\s+REPLACE\\s+VIEW\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.VIEW, QUERY_DDL_STATUS.CREATE),
	VIEW_ALTER("ALTER\\s+VIEW\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.VIEW, QUERY_DDL_STATUS.ALTER),
	VIEW_DROP("DROP\\s+VIEW\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.VIEW, QUERY_DDL_STATUS.DROP),
	
	PROCEDURE_CREATE("CREATE\\s+PROCEDURE\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.PROCEDURE, QUERY_DDL_STATUS.CREATE),
	PROCEDURE_CREATE_REPLACE("CREATE\\s+OR\\s+REPLACE\\s+PROCEDURE\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.PROCEDURE, QUERY_DDL_STATUS.CREATE),
	PROCEDURE_ALTER("ALTER\\s+PROCEDURE\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.PROCEDURE, QUERY_DDL_STATUS.ALTER),
	PROCEDURE_DROP("DROP\\s+PROCEDURE\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.PROCEDURE, QUERY_DDL_STATUS.DROP),
	
	FUNCTION_CREATE("CREATE\\s+FUNCTION\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.FUNCTION, QUERY_DDL_STATUS.CREATE),
	FUNCTION_CREATE_REPLACE("CREATE\\s+OR\\s+REPLACE\\s+FUNCTION\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.FUNCTION, QUERY_DDL_STATUS.CREATE),
	FUNCTION_ALTER("ALTER\\s+FUNCTION\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.FUNCTION, QUERY_DDL_STATUS.ALTER),
	FUNCTION_DROP("DROP\\s+FUNCTION\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.FUNCTION, QUERY_DDL_STATUS.DROP),
	
	PACKEAGE_CREATE("CREATE\\s+PACKAGE\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.PACKAGE, QUERY_DDL_STATUS.CREATE),
	PACKEAGE_CREATE_REPLACE("CREATE\\s+OR\\s+REPLACE\\s+PACKAGE\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.PACKAGE, QUERY_DDL_STATUS.CREATE),
	PACKAGE_ALTER("ALTER\\s+PACKAGE\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.PACKAGE, QUERY_DDL_STATUS.ALTER),
	PACKAGE_DROP("DROP\\s+PACKAGE\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.PACKAGE, QUERY_DDL_STATUS.DROP),
	
	TRIGGER_CREATE("CREATE\\s+TRIGGER\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.TRIGGER, QUERY_DDL_STATUS.CREATE),
	TRIGGER_CREATE_REPLACE("TRIGGER\\s+OR\\s+REPLACE\\s+PACKAGE\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.TRIGGER, QUERY_DDL_STATUS.CREATE),
	TRIGGER_ALTER("ALTER\\s+TRIGGER\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.TRIGGER, QUERY_DDL_STATUS.ALTER),
	TRIGGER_DROP("DROP\\s+TRIGGER\\s+([A-Z0-9_\\.\"'`]+)", QUERY_DDL_TYPE.TRIGGER, QUERY_DDL_STATUS.DROP)
	;
	
	private String regExp;
	private QUERY_DDL_TYPE ddlType;
	private QUERY_DDL_STATUS ddlStatus;
	private DefineDDL(String regExp, QUERY_DDL_TYPE ddlType, QUERY_DDL_STATUS ddlStatus) {
		this.regExp = regExp;
		this.ddlType = ddlType;
		this.ddlStatus = ddlStatus;
	}
	/**
	 * @return the regExp
	 */
	public String getRegExp() {
		return regExp;
	}
	/**
	 * @param regExp the regExp to set
	 */
	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}
	/**
	 * @return the ddlType
	 */
	public QUERY_DDL_TYPE getDdlType() {
		return ddlType;
	}
	/**
	 * @param ddlType the ddlType to set
	 */
	public void setDdlType(QUERY_DDL_TYPE ddlType) {
		this.ddlType = ddlType;
	}
	/**
	 * @return the ddlStatus
	 */
	public QUERY_DDL_STATUS getDdlStatus() {
		return ddlStatus;
	}
	/**
	 * @param ddlStatus the ddlStatus to set
	 */
	public void setDdlStatus(QUERY_DDL_STATUS ddlStatus) {
		this.ddlStatus = ddlStatus;
	}
	
}
