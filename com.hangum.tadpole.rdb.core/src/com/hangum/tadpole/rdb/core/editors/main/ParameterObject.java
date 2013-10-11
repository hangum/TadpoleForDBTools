package com.hangum.tadpole.rdb.core.editors.main;

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
