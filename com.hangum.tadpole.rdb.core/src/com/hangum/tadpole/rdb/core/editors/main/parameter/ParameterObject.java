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
package com.hangum.tadpole.rdb.core.editors.main.parameter;

import java.util.ArrayList;
import java.util.List;

public class ParameterObject {

	private List<Object> _param = new ArrayList<Object>();

	public void setObject(Object obj) {
		this._param.add(obj);
	}

	public void setInt(int i) {
		setObject(Integer.valueOf(i));
	}

	public void setLong(long l) {
		setObject(Long.valueOf(l));
	}

	public void setDouble(double d) {
		setObject(Double.valueOf(d));
	}

	public Object[] getParameter() {
		if (this._param == null) {
			return null;
		}
		return this._param.toArray();
	}

}
