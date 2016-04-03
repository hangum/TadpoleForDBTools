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
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.JSONUtil;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.mongodb.DBCollection;

/**
 * 신규 collection을 추가한다.
 * 
 * @author hangum
 *
 */
public class NewCollectionDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(NewCollectionDialog.class);

	private UserDBDAO userDB;
	private Text textName;
	private Text textContent;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public NewCollectionDialog(Shell parentShell, UserDBDAO userDB) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		this.userDB = userDB;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Collection Dialog");
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
		gridLayout.numColumns = 2;
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText(Messages.get().NewCollectionDialog_0);
		
		textName = new Text(container, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDataStructure = new Label(container, SWT.NONE);
		lblDataStructure.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblDataStructure.setText(Messages.get().NewCollectionDialog_1);
		
		textContent = new Text(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textContent.setText(JSONUtil.getPretty(Messages.get().NewCollectionDialog_2));
		textContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		textContent.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			
				if(e.stateMask == 0 && e.keyCode == SWT.TAB) {
					textContent.insert(TadpoleWidgetUtils.TAB_CONETNT);
				}
			}
		});
		textContent.setData( RWT.CANCEL_KEYS, new String[] { "TAB" } );
		
		textName.setFocus();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
		return container;
	}
	
	@Override
	protected void okPressed() {
		if("".equals(textName.getText().trim())) { //$NON-NLS-1$
			
			textName.setFocus();			
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().NewCollectionDialog_5);
			return;
			
		} else if("".equals(textContent.getText().trim())) { //$NON-NLS-1$
			
			textContent.setFocus();
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().NewCollectionDialog_8);
			return;
		}
		
		try {
			// collection 이름이 중복 되어 있는지 검사합나다.
			DBCollection dbColl = MongoDBQuery.findCollection(userDB, textName.getText().trim());
			if(dbColl == null) {
				textName.setFocus();			
				MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().NewCollectionDialog_10);
				return;
			}
		} catch (Exception e) {
			logger.error("mongodb find collection", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "Create Collection Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
		}
			
		// 내용이 정상인지 검사합니다.
//		DBObject dbObject = (DBObject) JSON.parse(textContent.getText().trim());
//		logger.debug("[textContext json]" + dbObject); //$NON-NLS-1$
		try {
			MongoDBQuery.createCollection(userDB, textName.getText().trim(), textContent.getText().trim());
		} catch (Exception e) {
			logger.error("mongodb create collection", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "Create Collection Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
			
			return;
		}
		
		
		super.okPressed();
	}
	

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID,  Messages.get().OK, true); //$NON-NLS-1$
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Cancel, false);	 //$NON-NLS-1$
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 476);
	}

}
