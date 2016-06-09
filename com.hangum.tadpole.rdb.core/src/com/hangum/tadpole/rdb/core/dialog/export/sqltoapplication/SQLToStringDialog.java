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
package com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.composites.SQLToOthersComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.composites.axisj.AxisjComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.composites.realgrid.RealGridComposite;

/**
 * sql to application string 
 * 
 * @author hangum
 *
 */
public class SQLToStringDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(SQLToStringDialog.class);
	private UserDBDAO userDB;
	private String sql = ""; //$NON-NLS-1$

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param sql sql
	 */
	public SQLToStringDialog(Shell parentShell, UserDBDAO userDB, String sql) {
		super(parentShell);
		setShellStyle(SWT.RESIZE | SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		
		this.userDB = userDB;
		this.sql = sql;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().SQLToStringDialog_2);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 4;
		gridLayout.horizontalSpacing = 4;
		gridLayout.marginHeight = 4;
		gridLayout.marginWidth = 4;
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		CTabFolder tabFolder = new CTabFolder(compositeBody, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setBorderVisible(false);
		tabFolder.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());

		SQLToOthersComposite compositeText = new SQLToOthersComposite(tabFolder, userDB, "PHP", sql, EditorDefine.SQL_TO_APPLICATION.PHP);
		compositeText.setLayout(new GridLayout(1, false));
		
		SQLToOthersComposite compositeASP = new SQLToOthersComposite(tabFolder, userDB, "ASP", sql, EditorDefine.SQL_TO_APPLICATION.ASP);
		compositeASP.setLayout(new GridLayout(1, false));
		
		SQLToOthersComposite compositeJavaString = new SQLToOthersComposite(tabFolder, userDB, "Java StringBuffer", sql, EditorDefine.SQL_TO_APPLICATION.Java_StringBuffer);
		compositeJavaString.setLayout(new GridLayout(1, false));
		
		SQLToOthersComposite compositeMybatis = new SQLToOthersComposite(tabFolder, userDB, "MyBatis", sql, EditorDefine.SQL_TO_APPLICATION.MyBatis);
		compositeMybatis.setLayout(new GridLayout(1, false));
		
		AxisjComposite compositeAxisj = new AxisjComposite(tabFolder, userDB, "AXISJ", sql, EditorDefine.SQL_TO_APPLICATION.AXISJ);
		compositeAxisj.setLayout(new GridLayout(1, false));
		
		RealGridComposite compositeRealgrid = new RealGridComposite(tabFolder, userDB, "RealGrid", sql, EditorDefine.SQL_TO_APPLICATION.REAL_GRID);
		compositeRealgrid.setLayout(new GridLayout(1, false));

		tabFolder.setSelection(0);
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}


	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Close, true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(850, 750);
	}
}
