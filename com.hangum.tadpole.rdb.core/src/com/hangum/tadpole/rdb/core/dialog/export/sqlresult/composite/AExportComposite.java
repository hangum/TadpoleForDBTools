package com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.AExportDAO;

public abstract class AExportComposite extends Composite {
	protected Text textTargetName;
	protected Combo comboEncoding;


	public AExportComposite(Composite parent, int style) {
		super(parent, style);
	}
	
	public boolean isValidate() {
		if ( StringUtils.isEmpty( textTargetName.getText() ) ){
			MessageDialog.openWarning(getShell(), Messages.get().Warning, "파일명 또는 테이블명을 입력하십시오.");
			textTargetName.setFocus();
			return false;
		}
		if ( StringUtils.isEmpty( comboEncoding.getText() ) ){
			MessageDialog.openWarning(getShell(), Messages.get().Warning, "인코딩 형식을 선택하거나 입력하십시오.");
			comboEncoding.setFocus();
			return false;
		}
		return true;
	}
	
	public abstract AExportDAO getLastData();

	@Override
	protected void checkSubclass() {
	}


}
