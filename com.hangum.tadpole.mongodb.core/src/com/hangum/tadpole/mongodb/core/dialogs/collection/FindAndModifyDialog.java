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
package com.hangum.tadpole.mongodb.core.dialogs.collection;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.widgets.TadpoleEditorWidget;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.help.HelpDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.HelpUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.dialogs.resultview.FindOneDetailDialog;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.mongodb.core.utils.CollectionUtils;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * mongodb find and moidfy dialog
 * 
 * ref: http://docs.mongodb.org/manual/reference/command/findAndModify/
 * 
 * @author hangum
 *
 */
public class FindAndModifyDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(FindAndModifyDialog.class);
	
	private UserDBDAO userDB;
	private String collName;
	
	private TadpoleEditorWidget textQuery;
	private TadpoleEditorWidget textFields;
	private TadpoleEditorWidget textSort;
	private TadpoleEditorWidget textUpdate;
	
	private Button btnRemove;
	private Button btnReturnNew;
	private Button btnUpsert;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public FindAndModifyDialog(Shell parentShell, UserDBDAO userDB, String collName) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.userDB = userDB;
		this.collName = collName;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(collName + " - Find And Modify Dialog");
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
		
		SashForm compositeBody = new SashForm(container, SWT.VERTICAL);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);
		
		Composite compositeQuery = new Composite(compositeBody, SWT.NONE);
		compositeQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeQuery = new GridLayout(2, false);
		gl_compositeQuery.verticalSpacing = 2;
		gl_compositeQuery.horizontalSpacing = 2;
		gl_compositeQuery.marginHeight = 2;
		gl_compositeQuery.marginWidth = 2;
		compositeQuery.setLayout(gl_compositeQuery);
		
		Label lblNewLabel = new Label(compositeQuery, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 55;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("{Query}");
		
		String strAssist = CollectionUtils.getAssistList(userDB, collName);
		textQuery = new TadpoleEditorWidget(compositeQuery, SWT.BORDER, EditorDefine.EXT_JSON, "", strAssist);
		textQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeField = new Composite(compositeBody, SWT.NONE);
		compositeField.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeField = new GridLayout(2, false);
		gl_compositeField.verticalSpacing = 2;
		gl_compositeField.horizontalSpacing = 2;
		gl_compositeField.marginHeight = 2;
		gl_compositeField.marginWidth = 2;
		compositeField.setLayout(gl_compositeField);
		
		Label lblNewLabel_1 = new Label(compositeField, SWT.NONE);
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_1.widthHint = 55;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
		lblNewLabel_1.setText("{Field}");
		
		textFields = new TadpoleEditorWidget(compositeField, SWT.BORDER, EditorDefine.EXT_JSON, "", strAssist);
		textFields.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeSort = new Composite(compositeBody, SWT.NONE);
		compositeSort.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeSort = new GridLayout(2, false);
		gl_compositeSort.verticalSpacing = 2;
		gl_compositeSort.horizontalSpacing = 2;
		gl_compositeSort.marginHeight = 2;
		gl_compositeSort.marginWidth = 2;
		compositeSort.setLayout(gl_compositeSort);
		
		Label lblNewLabel_2 = new Label(compositeSort, SWT.NONE);
		GridData gd_lblNewLabel_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_2.widthHint = 55;
		lblNewLabel_2.setLayoutData(gd_lblNewLabel_2);
		lblNewLabel_2.setText("{Sort}");
		
		textSort = new TadpoleEditorWidget(compositeSort, SWT.BORDER, EditorDefine.EXT_JSON, "", strAssist);
		textSort.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeUpdate = new Composite(compositeBody, SWT.NONE);
		compositeUpdate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeUpdate = new GridLayout(2, false);
		gl_compositeUpdate.verticalSpacing = 2;
		gl_compositeUpdate.horizontalSpacing = 2;
		gl_compositeUpdate.marginHeight = 2;
		gl_compositeUpdate.marginWidth = 2;
		compositeUpdate.setLayout(gl_compositeUpdate);
		
		Label lblNewLabel_3 = new Label(compositeUpdate, SWT.NONE);
		GridData gd_lblNewLabel_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_3.widthHint = 55;
		lblNewLabel_3.setLayoutData(gd_lblNewLabel_3);
		lblNewLabel_3.setText("{Update}");
		
		textUpdate = new TadpoleEditorWidget(compositeUpdate, SWT.BORDER, EditorDefine.EXT_JSON, "", strAssist);
		textUpdate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeOthers = new Composite(container, SWT.NONE);
		compositeOthers.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeOthers.setLayout(new GridLayout(3, false));
		
		btnRemove = new Button(compositeOthers, SWT.CHECK);
		btnRemove.setSelection(true);
		btnRemove.setText("Remove");
		
		btnReturnNew = new Button(compositeOthers, SWT.CHECK);
		btnReturnNew.setSelection(false);
		btnReturnNew.setText("Return New");
		
		btnUpsert = new Button(compositeOthers, SWT.CHECK);
		btnUpsert.setSelection(false);
		btnUpsert.setText("Upsert");
		
		compositeBody.setWeights(new int[]{40, 20, 20, 20});
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}
	
	@Override
	protected void okPressed() {
		if(!MessageDialog.openConfirm(null, Messages.get().Confirm, "Are you want to execute?")) return;
		DBObject objQuery = null;
		DBObject objFields = null;
		DBObject objSort = null;
		DBObject objUpdate = null;
		
		try {
			objQuery = "".equals(textQuery.getText())?null:(DBObject)JSON.parse(textQuery.getText());
		} catch(Exception e) {
			textQuery.setFocus();
			MessageDialog.openError(null, Messages.get().Error, e.getMessage() + PublicTadpoleDefine.LINE_SEPARATOR + "{Query} is error.");
			return;
		}
		
		try {
			objFields = "".equals(textFields.getText())?null:(DBObject)JSON.parse(textFields.getText());
		} catch(Exception e) {
			textFields.setFocus();
			MessageDialog.openError(null, Messages.get().Error, e.getMessage() + PublicTadpoleDefine.LINE_SEPARATOR + "{Field} is error.");
			return;
		}
		
		try {
			objSort = "".equals(textSort.getText())?null:(DBObject)JSON.parse(textSort.getText());
		} catch(Exception e) {
			textSort.setFocus();
			MessageDialog.openError(null, Messages.get().Error, e.getMessage() + PublicTadpoleDefine.LINE_SEPARATOR + "{Sort} is error.");
			return;
		}
		
		try {
			objUpdate = "".equals(textUpdate.getText())?null:(DBObject)JSON.parse(textUpdate.getText());
		} catch(Exception e) {
			textUpdate.setFocus();
			MessageDialog.openError(null, Messages.get().Error, e.getMessage() + PublicTadpoleDefine.LINE_SEPARATOR + "{Update} is error.");
			return;
		}
		
		try {
			DBObject retDBObj = MongoDBQuery.findAndModify(userDB, collName, objQuery, objSort, objFields, btnRemove.getSelection(), objUpdate, btnReturnNew.getSelection(), btnUpsert.getSelection());
			if(null != retDBObj) {
				FindOneDetailDialog resultViewDialog = new FindOneDetailDialog(null, userDB, "Update Result Message", retDBObj);
				resultViewDialog.open();
			}
			else MessageDialog.openInformation(null, "Result", "Result message is null");
			
		} catch (Exception e) {
			logger.error("mongodb FindAndModify", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "FindAndModify Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		if(buttonId == IDialogConstants.HELP_ID) {
			HelpUtils.showHelp(HelpDefine.MONGODB_FINDANDMOIDFY);
		}
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.HELP_ID, "Help", false);
		createButton(parent, IDialogConstants.OK_ID, "Execute", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "CLOSE", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(746, 638);
	}
}
