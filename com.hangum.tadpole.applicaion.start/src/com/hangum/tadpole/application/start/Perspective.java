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
package com.hangum.tadpole.application.start;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hamgum.tadpole.help.core.views.HelpViewPart;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.manager.core.editor.auth.UserManagementEditor;
import com.hangum.tadpole.manager.core.editor.auth.UserManagementEditorInput;
import com.hangum.tadpole.manager.core.editor.executedsql.ExecutedSQLEditor;
import com.hangum.tadpole.manager.core.editor.executedsql.ExecutedSQLEditorInput;
import com.hangum.tadpole.notes.core.views.list.NoteListViewPart;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * Configures the perspective layout. This class is contributed through the
 * plugin.xml.
 */
public class Perspective implements IPerspectiveFactory {
	public static final String ID = "com.hangum.tadpole.application.start.perspective";
	
	public final static String ADMIN = "admin";
	public final static String MANAGER = "manager";
	public final static String DEFAULT = "default";
	
	private static final Logger logger = Logger.getLogger(Perspective.class);

	public void createInitialLayout(IPageLayout layout) {
		boolean errFlag = false;
		try {
			String pesp = SessionManager.getPerspective();
			Method method = getClass().getMethod(pesp + "Perspective", IPageLayout.class);
			method.invoke(this, layout);
		} catch (SecurityException e) {
			errFlag = true;
			logger.error(e);
		} catch (NoSuchMethodException e) {
			errFlag = true;
			logger.error(e);
		} catch (IllegalArgumentException e) {
			errFlag = true;
			logger.error(e);
		} catch (IllegalAccessException e) {
			errFlag = true;
			logger.error(e);
		} catch (InvocationTargetException e) {
			errFlag = true;
			logger.error(e);
		} finally {
			if (errFlag) {
				defaultPerspective(layout);
			}
		}
	}

	public void adminPerspective(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);

		IFolderLayout leftFolder = layout.createFolder("id" + ManagerViewer.ID, IPageLayout.LEFT, 0.30f, editorArea);
		leftFolder.addView(ManagerViewer.ID);

		IFolderLayout leftUnderFolder = layout.createFolder("id" + ExplorerViewer.ID, IPageLayout.BOTTOM, 0.30f, "id" + ManagerViewer.ID);
		leftUnderFolder.addView(ExplorerViewer.ID);
		leftUnderFolder.addView(NoteListViewPart.ID);
		
		IFolderLayout rightFolder = layout.createFolder("id" + HelpViewPart.ID, IPageLayout.RIGHT, 0.80f, editorArea);
		rightFolder.addView(HelpViewPart.ID);

		// viewer closealbe false
		layout.getViewLayout(HelpViewPart.ID).setCloseable(false);
		layout.getViewLayout(ManagerViewer.ID).setCloseable(false);
		layout.getViewLayout(ExplorerViewer.ID).setCloseable(false);
		layout.getViewLayout(NoteListViewPart.ID).setCloseable(false);
		openEditor(UserManagementEditor.ID);
	}

	public void managerPerspective(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);

		IFolderLayout leftFolder = layout.createFolder("id" + ManagerViewer.ID, IPageLayout.LEFT, 0.30f, editorArea);
		leftFolder.addView(ManagerViewer.ID);

		IFolderLayout leftUnderFolder = layout.createFolder("id" + ExplorerViewer.ID, IPageLayout.BOTTOM, 0.30f, "id" + ManagerViewer.ID);
		leftUnderFolder.addView(ExplorerViewer.ID);

		leftUnderFolder.addView(NoteListViewPart.ID);

		IFolderLayout rightFolder = layout.createFolder("id" + HelpViewPart.ID, IPageLayout.RIGHT, 0.80f, editorArea);
		rightFolder.addView(HelpViewPart.ID);

		// viewer closealbe false
		layout.getViewLayout(HelpViewPart.ID).setCloseable(false);
		layout.getViewLayout(ManagerViewer.ID).setCloseable(false);
		layout.getViewLayout(ExplorerViewer.ID).setCloseable(false);
		layout.getViewLayout(NoteListViewPart.ID).setCloseable(false);
		
		openEditor(ExecutedSQLEditor.ID);
	}

	public void defaultPerspective(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);

		IFolderLayout leftFolder = layout.createFolder("id" + ManagerViewer.ID, IPageLayout.LEFT, 0.30f, editorArea);
		leftFolder.addView(ManagerViewer.ID);
		leftFolder.addView(HelpViewPart.ID);

		IFolderLayout leftUnderFolder = layout.createFolder("id" + ExplorerViewer.ID, IPageLayout.BOTTOM, 0.30f, "id" + ManagerViewer.ID);
		leftUnderFolder.addView(ExplorerViewer.ID);
		leftUnderFolder.addView(NoteListViewPart.ID);

		// viewer closealbe false
		layout.getViewLayout(HelpViewPart.ID).setCloseable(false);
		layout.getViewLayout(ManagerViewer.ID).setCloseable(false);
		layout.getViewLayout(ExplorerViewer.ID).setCloseable(false);
		layout.getViewLayout(NoteListViewPart.ID).setCloseable(false);
	}

	private void openEditor(String id) {
		IEditorInput input = null;
		if (id.equals(UserManagementEditor.ID)) {
			input = new UserManagementEditorInput();
		} else if (id.equals(ExecutedSQLEditor.ID)) {
			input = new ExecutedSQLEditorInput();
		}
		try {
			// Check duplicated editor.
			IEditorReference[] editorRefs = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
			for (IEditorReference ref : editorRefs) {
				if (id.equals(ref.getId())) {
					input = ref.getEditorInput();
					break;
				}
			}
			if (input != null) {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, id);
			}
		} catch (PartInitException e) {
			logger.error("Open editor", e); //$NON-NLS-1$

			Status errStatus = new Status(IStatus.ERROR, BrowserActivator.ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", "Open editor", errStatus); //$NON-NLS-1$
		}
	}

}
