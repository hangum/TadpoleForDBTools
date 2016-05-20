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
package com.hangum.tadpole.mongodb.core.dialogs.resultview;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.Messages;
import com.mongodb.DBObject;

/**
 * 결과 한개를 디테일하게 보는 다이얼로그
 * 
 * @author hangum
 *
 */
public class FindOneDetailDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FindOneDetailDialog.class);

	private UserDBDAO userDB;
	private String collectionName;
	private DBObject dbResultObject;
	
	private Text textColName;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public FindOneDetailDialog(Shell parentShell, UserDBDAO userDB, String collectionName, DBObject dbResultObject) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.userDB = userDB;
		this.collectionName = collectionName;
		this.dbResultObject = dbResultObject;				
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);

		newShell.setText("Collection Detail"); //$NON-NLS-1$
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
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		GridLayout gl_compositeHead = new GridLayout(2, false);
		gl_compositeHead.verticalSpacing = 2;
		gl_compositeHead.horizontalSpacing = 2;
		gl_compositeHead.marginHeight = 2;
		gl_compositeHead.marginWidth = 2;
		compositeHead.setLayout(gl_compositeHead);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblName = new Label(compositeHead, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name");
		
		textColName = new Text(compositeHead, SWT.BORDER);
		textColName.setEditable(false);
		textColName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite compositeBody = new FindOneDetailComposite(container, collectionName, dbResultObject);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		initData();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}
	
	private void initData() {
		textColName.setText(collectionName);
		textColName.setFocus();
	}
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,  Messages.get().Close, true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(521, 477);
	}

}
