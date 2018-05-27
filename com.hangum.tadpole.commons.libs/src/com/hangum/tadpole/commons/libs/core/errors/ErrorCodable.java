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

public interface ErrorCodable {
	String getCode();
	Level  getLevel();
	String getMessage(String...args);
}
