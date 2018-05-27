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
package com.hangum.tadpole.engine.restful;

import com.hangum.tadpole.engine.define.TDBResultCodeDefine;

public class RESTFulSQLExecuteException extends TadpoleException {

	public RESTFulSQLExecuteException(String message, Exception e) {
		super(TDBResultCodeDefine.INTERNAL_SERVER_ERROR, message, e);
	}
}
