package com.hangum.tadpole.mongodb.core.dialogs.collection;

import org.apache.log4j.Logger;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.hangum.db.dao.system.UserDBDAO;
import com.hangum.db.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;

/**
 * 몽고디비 인덱스 생성
 * 
 * @author hangum
 *
 */
public class NewIndexDialog extends NewDocumentDialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(NewIndexDialog.class);

	public NewIndexDialog(Shell parentShell, UserDBDAO userDB, String collectionName) {
		super(parentShell, userDB, collectionName);
	}
	
	@Override
	protected void okPressed() {
		if("".equals(textContent.getText().trim())) { //$NON-NLS-1$
			
			textContent.setFocus();
			MessageDialog.openError(null, Messages.NewDocumentDialog_3, Messages.NewDocumentDialog_4);
			return;
		}

		try {
			MongoDBQuery.crateIndex(userDB, textName.getText().trim(), textContent.getText().trim());
		} catch (Exception e) {
			logger.error("mongodb create collection", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", "Create Collection Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
			
			return;
		}
		
		super.okPressed();
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("MongoDB Create Index Dialog"); //$NON-NLS-1$
	}
}
