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

import org.apache.log4j.Logger;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.manager.core.editor.restfulapi.RESTFulAPIManagerEditorInput;
import com.hangum.tadpole.manager.core.editor.restfulapi.manager.APIHubManagerEditor;
//import com.hangum.tadpole.notes.core.views.list.NoteListViewPart;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.rdb.core.viewers.sql.template.SQLTemplateView;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * Configures the perspective layout. This class is contributed through the
 * plugin.xml.
 */
public class Perspective implements IPerspectiveFactory {
	public static final String ID = "com.hangum.tadpole.application.start.perspective";
	private static final Logger logger = Logger.getLogger(Perspective.class);
	
	public final static String DEFAULT = "default";
	public final static String APIUSER = "api";
	
	public void createInitialLayout(IPageLayout layout) {
		final String strRepresentRole = SessionManager.getRepresentRole();
		if(strRepresentRole.equals(PublicTadpoleDefine.USER_ROLE_TYPE.API_USER.name())) {
			apiUserPerspective(layout);
		} else {
			defaultPerspective(layout);
		}
	
//		get의 outline을 보일것인지? 현재는 썸네일이 블렉으로 보여주어서 블럭을 해 놓습니다. 
//		layout.addStandaloneView(IPageLayout.ID_OUTLINE, true, IPageLayout.LEFT, 0.3f, layout.getEditorArea());
	}
	
	/**
	 * api user perspective
	 * 
	 * @param layout
	 */
	private void apiUserPerspective(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		layout.setFixed(true);
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();	
		RESTFulAPIManagerEditorInput input = new RESTFulAPIManagerEditorInput();
		
		try {
			page.openEditor(input, APIHubManagerEditor.ID, false);
		} catch(Exception e) {
			logger.error(e);
		}
	}

	/**
	 * default perspective
	 * 
	 * @param layout
	 */
	private void defaultPerspective(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);

		IFolderLayout leftFolder = layout.createFolder("id" + ManagerViewer.ID, IPageLayout.LEFT, 0.26f, editorArea);
		leftFolder.addView(ManagerViewer.ID);
//		leftFolder.addView(HelpViewPart.ID);

		IFolderLayout leftUnderFolder = layout.createFolder("id" + ExplorerViewer.ID, IPageLayout.BOTTOM, 0.26f, "id" + ManagerViewer.ID);
		leftUnderFolder.addView(ExplorerViewer.ID);
		leftUnderFolder.addView(SQLTemplateView.ID);

		// viewer closealbe false
//		layout.getViewLayout(HelpViewPart.ID).setCloseable(false);
		layout.getViewLayout(SQLTemplateView.ID).setCloseable(false);
		layout.getViewLayout(ManagerViewer.ID).setCloseable(false);
		layout.getViewLayout(ExplorerViewer.ID).setCloseable(false);
	}

//	/**
//	 * open editor
//	 * 
//	 * @param id
//	 */
//	private void openEditor(String id) {
//		IEditorInput input = null;
//		
//		boolean isAdmin = PermissionChecker.isUserAdmin(SessionManager.getRepresentRole());
//    	if(isAdmin) {
//			if (id.equals(DBMgmtEditor.ID)) {
//				input = new DBMgntEditorInput(PublicTadpoleDefine.USER_ROLE_TYPE.USER);
//			} else if (id.equals(SQLAuditEditor.ID)) {
//				input = new SQLAuditEditorInput();
//			}
//    	} else {
//    		if (id.equals(DBMgmtEditor.ID)) {
//				input = new DBMgntEditorInput(PublicTadpoleDefine.USER_ROLE_TYPE.USER);
//			} else if (id.equals(SQLAuditEditor.ID)) {
//				input = new SQLAuditEditorInput();
//			}
//    	}
//    	
//		try {
//			// Check duplicated editor.
//			IEditorReference[] editorRefs = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
//			for (IEditorReference ref : editorRefs) {
//				if (id.equals(ref.getId())) {
//					input = ref.getEditorInput();
//					break;
//				}
//			}
//			if (input != null) {
//				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, id);
//			}
//		} catch (PartInitException e) {
//			logger.error("Open editor", e); //$NON-NLS-1$
//
//			Status errStatus = new Status(IStatus.ERROR, BrowserActivator.APPLICTION_ID, e.getMessage(), e); //$NON-NLS-1$
//			ExceptionDetailsErrorDialog.openError(null,CommonMessages.get().Error, "Open editor", errStatus); //$NON-NLS-1$
//		}
//	}

}
