/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.AbstractExportDAO;

/**
 * Resultset export composite
 * 
 * @author hangum
 */
public abstract class AbstractExportComposite extends Composite {
	protected Text textTargetName;
	protected Combo comboEncoding;


	public AbstractExportComposite(Composite parent, int style) {
		super(parent, style);
	}
	
	public boolean isValidate() {
		if ( StringUtils.isEmpty( textTargetName.getText() ) ){
			MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().AbstractExportCompositeFileEmpty);
			textTargetName.setFocus();
			return false;
		}
		if ( StringUtils.isEmpty( comboEncoding.getText() ) ){
			MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().AbstractExportCompositeEncoding);
			comboEncoding.setFocus();
			return false;
		}
		return true;
	}
	
	public abstract AbstractExportDAO getLastData();

	@Override
	protected void checkSubclass() {
	}


}
