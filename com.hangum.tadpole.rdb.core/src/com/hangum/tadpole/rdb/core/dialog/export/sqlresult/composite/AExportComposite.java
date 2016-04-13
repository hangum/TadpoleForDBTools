package com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite;

import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.AExportDAO;

public abstract class AExportComposite extends Composite {

	public AExportComposite(Composite parent, int style) {
		super(parent, style);
	}
	
	public boolean isValidate() {
		return false;
	}
	
	public abstract AExportDAO getLastData();

	@Override
	protected void checkSubclass() {
	}


}
