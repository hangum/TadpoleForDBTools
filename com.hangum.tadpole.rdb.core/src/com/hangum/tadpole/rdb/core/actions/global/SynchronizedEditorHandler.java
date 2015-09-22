/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.actions.global;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.State;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserInfoData;
import com.hangum.tadpole.mongodb.core.editors.main.MongoDBTableEditor;
import com.hangum.tadpole.mongodb.erd.core.editor.TadpoleMongoDBERDEditor;
import com.hangum.tadpole.preference.define.PreferenceDefine;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.hangum.tadpole.rdb.erd.core.editor.TadpoleRDBEditor;

/**
 * synchronized editor, viewer
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 5. 21.
 *
 */
public class SynchronizedEditorHandler extends AbstractHandler {
	private static final Logger logger = Logger.getLogger(SynchronizedEditorHandler.class);
	public static final String ID = "com.hangum.tadpole.rdb.core.command.synceditor";
	public static final String STATE_ID = "org.eclipse.ui.commands.toggleState";
	
	/**
	 * 현재 synceditor의 상태 정보를 가지고 온다.
	 * @return
	 */
	public static boolean isSynchronizedState() {
		ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);  
	    Command command = commandService.getCommand(ID);  
	    State state = command.getState(STATE_ID);
	    boolean boolStats = Boolean.parseBoolean(state.getValue().toString());
	    
	    return boolStats;
	}

	/**
	 * editor, connection manager synchronizer
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if(logger.isDebugEnabled()) logger.debug("Called Synchronized editor handler...................");
		
		ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);  
	    Command command = commandService.getCommand(ID);  
	    State state = command.getState(STATE_ID);
	    boolean boolStats = !Boolean.parseBoolean(state.getValue().toString());
	    state.setValue(boolStats);
	    
	    // save
	    try {
			TadpoleSystem_UserInfoData.updateUserInfoData(PreferenceDefine.SYNC_EIDOTR_STATS, Boolean.toString(boolStats));
		} catch (Exception e) {
			logger.error("update preference data", e);
		}
	    
	    selectConnectionViewer(boolStats);
		
		return null;
	}
	
	/**
	 * connection selection 
	 * 
	 * @param isSelection
	 */
	public void selectConnectionViewer(boolean isSelection) {
		if(isSelection) {
			IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			UserDBDAO selectionUserDB = null;
			
			// rdb
			if (activeEditor instanceof MainEditor) {
				MainEditor editor = (MainEditor) activeEditor;
				selectionUserDB = editor.getUserDB();
			// erd
			} else if(activeEditor instanceof TadpoleRDBEditor) {
				TadpoleRDBEditor editor = (TadpoleRDBEditor) activeEditor;
				selectionUserDB = editor.getUserDB();
			} else if(activeEditor instanceof MongoDBTableEditor) {
				MongoDBTableEditor editor = (MongoDBTableEditor) activeEditor;
				selectionUserDB = editor.getUserDB();
			} else if(activeEditor instanceof TadpoleMongoDBERDEditor) {
				TadpoleMongoDBERDEditor editor = (TadpoleMongoDBERDEditor) activeEditor;
				selectionUserDB = editor.getUserDB();
			}

			if(selectionUserDB != null) {
				ManagerViewer managerView =  (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
				IStructuredSelection iss = new StructuredSelection(selectionUserDB);
				managerView.getManagerTV().setSelection(iss);
			}
		}
	}

}
