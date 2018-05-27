/**
 * Copyright (c) 2017 Tadpole Hub.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * @author sun.han
 */
package com.hangum.tadpole.commons.libs.core.errors;

import java.util.logging.Level;
import com.hangum.tadpole.commons.libs.core.errors.TadpoleErrorMessages;


/**
 * Error/Exception Definitions of Tadpole DB Hub.
 */

/** 
 * java.lang.Object
 * java.util.logging.Level
 *
 * [Error Levels]
 * SEVERE: This is an error level for serious failures.
 * WARNING: This is an error level for potential problems.
 * INFO: This is an error level for informational messages.
 * CONFIG: This is an error level for static configuration messages.
 * FINE: This is to provide tracing information.
 * FINER: This is for fairly detailed tracing messages.
 * FINEST: This is for highly detailed tracing messages.
 */
public enum TadpoleErrors implements ErrorCodable {
	
	/* No Error */
	TDP_000("TDP-000", Level.INFO, TadpoleErrorMessages.get().NoError),
	
	/* Warning messages relating to user actions */
	TDP_010("TDP-010", Level.WARNING, TadpoleErrorMessages.get().EnterNumbersOnly),
	
	/* Validation */
	TDP_050("TDP-050", Level.WARNING, TadpoleErrorMessages.get().ItemIsEmpty),
	TDP_051("TDP-051", Level.WARNING, TadpoleErrorMessages.get().InvalidRange_GEAndLEWithItem),
	TDP_052("TDP-052", Level.WARNING, TadpoleErrorMessages.get().InvalidRange_GE),
	TDP_053("TDP-053", Level.WARNING, TadpoleErrorMessages.get().InvalidRange_G), 
	TDP_054("TDP-054", Level.WARNING, TadpoleErrorMessages.get().InvalidRange_LE),
	TDP_055("TDP-055", Level.WARNING, TadpoleErrorMessages.get().InvalidRange_L),
	
	/* License */
	TDP_100("TDP-100", Level.SEVERE, TadpoleErrorMessages.get().InvalidLicense),
	TDP_101("TDP-101", Level.SEVERE, TadpoleErrorMessages.get().LicenseExpired), 
	TDP_102("TDP-102", Level.INFO, TadpoleErrorMessages.get().LicenseWillExpire),
	TDP_103("TDP-103", Level.SEVERE, TadpoleErrorMessages.get().NoValidLicenseFound),
	
	/* Account */
	TDP_200("TDP-200", Level.WARNING, TadpoleErrorMessages.get().UserNotFound), 
	
	/* Password */
//	TDP_250("TDP-250", Level.WARNING, TadpoleErrorMessages.get().PasswordRule), 
	TDP_251("TDP-251", Level.WARNING, TadpoleErrorMessages.get().InvalidPassword),
	TDP_252("TDP-252", Level.WARNING, TadpoleErrorMessages.get().PasswordsDoNotMatch),
	
	/* Databases */
	TDP_300("TDP-300", Level.WARNING, TadpoleErrorMessages.get().UnsupportedDatabase),
	TDP_301("TDP-301", Level.WARNING, TadpoleErrorMessages.get().UnableToConnectToDatabase),
	TDP_302("TDP-302", Level.WARNING, TadpoleErrorMessages.get().NoPermissionToAddDatabase),
	TDP_303("TDP-303", Level.WARNING, TadpoleErrorMessages.get().NoPermissionToDeleteDatabase),
	TDP_304("TDP-304", Level.WARNING, TadpoleErrorMessages.get().NoPermissionToUpdateDatabase),
	TDP_305("TDP-305", Level.WARNING, TadpoleErrorMessages.get().ExceededMaxDatabases),
	TDP_306("TDP-306", Level.WARNING, TadpoleErrorMessages.get().NoJDBCDriverFound),
	TDP_307("TDP-307", Level.WARNING, TadpoleErrorMessages.get().FailedToLoadJDBCDriver),
	TDP_308("TDP-309", Level.WARNING, TadpoleErrorMessages.get().InvalidJDBCDriver),
	
	/* File Operation */
	TDP_400("TDP-400", Level.WARNING, TadpoleErrorMessages.get().FileNotFound),
	TDP_401("TDP-401", Level.WARNING, TadpoleErrorMessages.get().NoPermissionToUpdateFile),
	
	
	TDP_999("TDP-999", Level.INFO, TadpoleErrorMessages.get().UnexpectedError)
	; /* End of Definitions */
	
	private String code;
	private Level level;
	private String message;
	
	@Override 
	public String getCode() {
		return this.code;
	}
	
	@Override 
	public String getMessage(String...args) {
		return ErrorUtil.parseMessage(this.message, args);
	}
	
	@Override
	public Level getLevel() {
		return this.level;
	}
	
	TadpoleErrors(String code, Level level, String message) {
		this.code    = code;
		this.level   = level;
		this.message = message;
	}
}
