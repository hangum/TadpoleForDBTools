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
package com.hangum.tadpole.manager.core.actions.global;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.manager.core.Activator;
import com.hangum.tadpole.manager.core.Messages;
import com.hangum.tadpole.manager.core.editor.transaction.connection.TransactionConnectionListEditor;
import com.hangum.tadpole.manager.core.editor.transaction.connection.TransactionConnectionListEditorInput;
import com.swtdesigner.ResourceManager;

/**
 * DB Connection manager action
 * 
 * @author hangum
 *
 */
public class TransactionConnectionManagerAction extends Action implements ISelectionListener, IWorkbenchAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TransactionConnectionManagerAction.class);
	private final static String ID = "com.hangum.db.browser.rap.core.actions.global.TransactionDBConnectionManagerAction"; //$NON-NLS-1$
	
	private final IWorkbenchWindow window;
	
	public TransactionConnectionManagerAction(IWorkbenchWindow window) {
		this.window = window;
		
		setId(ID);
		setText(Messages.get().TransactionConnectionManagerAction_0);
		setToolTipText(Messages.get().TransactionConnectionManagerAction_0);
		setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.PLUGIN_ID, "resources/icons/transaction_db_status.png"));
		setEnabled(true);
	}
	
	@Override
	public void run() {
		try {
			TransactionConnectionListEditorInput transactionInput = new TransactionConnectionListEditorInput();
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(transactionInput, TransactionConnectionListEditor.ID);
		} catch (PartInitException e) {
			logger.error("Connections Management editor", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "Connections Management editor", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
	}

	@Override
	public void dispose() {
	}

}