/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.engine.sql.template;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;

/**
 * 템블릿을 정의합니다.
 * 
 * @author hangum
 *
 */
public abstract class AbstractDMLTemplate {
	/** tdb custom column */
	public static final String TDB_CUSTOME_COLUMN = PublicTadpoleDefine.SPECIAL_USER_DEFINE_HIDE_COLUMN + "__RNUM";

}
