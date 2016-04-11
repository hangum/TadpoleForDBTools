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
package com.hangum.tadpole.rdb.core.viewers.connections;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.ManagerListDTO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.dao.system.userdb.DBOtherDAO;
import com.hangum.tadpole.engine.query.dao.system.userdb.ResourcesDAO;
import com.hangum.tadpole.engine.query.dao.system.userdb.ResourcesDAO.DB_RESOURCE_TYPE;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.engine.security.TadpoleSecurityManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.connections.QueryEditorAction;
import com.hangum.tadpole.rdb.core.actions.erd.mongodb.MongoDBERDViewAction;
import com.hangum.tadpole.rdb.core.actions.erd.rdb.RDBERDViewAction;
import com.hangum.tadpole.rdb.core.actions.global.ConnectDatabaseAction;
import com.hangum.tadpole.rdb.core.dialog.commons.MapViewerDialog;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.main.MainEditorInput;
import com.hangum.tadpole.rdb.core.util.EditorUtils;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * connection manager 정보를 
 * 
 * @author hangum
 *
 */
public class ManagerViewer extends ViewPart {
	private static final Logger logger = Logger.getLogger(ManagerViewer.class);
	public static String ID = "com.hangum.tadpole.rdb.core.view.connection.manager"; //$NON-NLS-1$
	
	private Composite compositeMainComposite;
	private List<ManagerListDTO> treeDataList = new ArrayList<ManagerListDTO>();
	private Map<String, ManagerListDTO> mapTreeList = new HashMap<>();
	private TreeViewer managerTV;
	
	/** download servcie handler. */
	private DownloadServiceHandler downloadServiceHandler;
	
	public ManagerViewer() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		
		setPartName(Messages.get().ManagerViewer_0);
		
		compositeMainComposite = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 0;
		gl_composite.horizontalSpacing = 0;
		gl_composite.marginHeight = 0;
		gl_composite.marginWidth = 0;
		compositeMainComposite.setLayout(gl_composite);
		
		managerTV = new TreeViewer(compositeMainComposite, SWT.NONE);
		managerTV.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection is = (IStructuredSelection)event.getSelection();
				Object objSelect = is.getFirstElement();
				if(objSelect instanceof UserDBDAO) {
					final UserDBDAO userDB = (UserDBDAO)objSelect;
					if(!TadpoleSecurityManager.getInstance().ifLockOpenDialog(userDB)) return;

					addManagerResouceData(userDB, false);
					
					// Rice lock icode change event
					managerTV.refresh(userDB, true);
					
					AnalyticCaller.track(ManagerViewer.ID, userDB.getDbms_type());
					managerTV.getControl().setFocus();
				} else if(objSelect instanceof ManagerListDTO) {
					ManagerListDTO managerDTO = (ManagerListDTO)objSelect;
					if(managerDTO.getManagerList().isEmpty()) {
						try {
							List<UserDBDAO> userDBS = TadpoleSystem_UserDBQuery.getUserGroupDB(managerDTO.getName());
							for (UserDBDAO userDBDAO : userDBS) {
								managerDTO.addLogin(userDBDAO);
							}
							
							managerTV.refresh(managerDTO, false);
							managerTV.expandToLevel(managerDTO, 2);
						} catch(Exception e) {
							logger.error("get manager list", e);
						}
					}
				} else if(objSelect instanceof DBOtherDAO) {
					DBOtherDAO dao = (DBOtherDAO)objSelect;
					
				}
			}	// select change event 
		});
		managerTV.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				
				IStructuredSelection is = (IStructuredSelection)event.getSelection();
				Object selElement = is.getFirstElement();
				
				// db object를 클릭하면 쿼리 창이 뜨도록하고.
				if(selElement instanceof UserDBDAO) {
					final UserDBDAO userDB= (UserDBDAO)selElement;
					if(!TadpoleSecurityManager.getInstance().ifLockOpenDialog(userDB)) return;
					
					QueryEditorAction qea = new QueryEditorAction();
					qea.run(userDB);
				// erd를 클릭하면 erd가 오픈되도록 수정.
				} else if(selElement instanceof UserDBResourceDAO) {
					final UserDBResourceDAO dao = (UserDBResourceDAO)selElement;
					
					if( PublicTadpoleDefine.RESOURCE_TYPE.ERD.toString().equals(dao.getResource_types())) {
						UserDBDAO userDB = dao.getParent();
						
						if(userDB != null && DBDefine.MONGODB_DEFAULT == userDB.getDBDefine()) {							
							MongoDBERDViewAction ea = new MongoDBERDViewAction();
							ea.run(dao);
						} else {
							RDBERDViewAction ea = new RDBERDViewAction();
							ea.run(dao);
						}
					} else {
						QueryEditorAction qea = new QueryEditorAction();
						qea.run(dao);
					}
				// manager
				} else if (selElement instanceof ManagerListDTO) {
					if("YES".equals(SessionManager.getIsRegistDB())) {
						ConnectDatabaseAction cda = new ConnectDatabaseAction(getSite().getWorkbenchWindow());
						cda.runConnectionDialog(is);
					}
				} else if (selElement instanceof DBOtherDAO) {
					final DBOtherDAO dao = (DBOtherDAO)selElement;
					MapViewerDialog dialog = new MapViewerDialog(getSite().getWorkbenchWindow().getShell(), dao.getParent().getName(), dao);
					dialog.open();
				}
			}
		});
		Tree tree = managerTV.getTree();
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tree.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
		
		managerTV.setContentProvider(new ManagerContentProvider());
		managerTV.setLabelProvider(new ManagerLabelProvider());
		managerTV.setInput(treeDataList);		
		getSite().setSelectionProvider(managerTV);
		
		createPopupMenu();
		
		registerServiceHandler();
		init();
		
		// db에 erd가 추가되었을 경우 
		PlatformUI.getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty() ==  PublicTadpoleDefine.SAVE_FILE) {
					addResource(Integer.parseInt( event.getNewValue().toString().split(":")[0] )); //$NON-NLS-1$
				} else if (event.getProperty() ==  PublicTadpoleDefine.ADD_DB) {
					init();
				}
			}
		});
	}
	
	/**
	 * 트리 데이터 초기화
	 */
	public void init() {
		treeDataList.clear();
		mapTreeList.clear();
	
		try {
			for (String strGroupName : TadpoleSystem_UserDBQuery.getUserGroupName()) {
				ManagerListDTO managerDTO = new ManagerListDTO(strGroupName);
				
				for (UserDBDAO userDBDAO : TadpoleSystem_UserDBQuery.getUserGroupDB(managerDTO.getName())) {
					managerDTO.addLogin(userDBDAO);
				}
				
				treeDataList.add(managerDTO);
			}	// end last end

			managerTV.refresh();
			managerTV.expandToLevel(2);
			
		} catch (Exception e) {
			logger.error("initialize Managerview", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().ManagerViewer_4, errStatus); //$NON-NLS-1$
		}
		
		managerTV.refresh();
		AnalyticCaller.track(ManagerViewer.ID);
	}

	/**
	 * popup 화면을 오픈합니다.
	 */
	private void createPopupMenu() {
		MenuManager menuMgr = new MenuManager();
		menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		
		Menu popupMenu = menuMgr.createContextMenu(managerTV.getTree());
		managerTV.getTree().setMenu(popupMenu);
		getSite().registerContextMenu(menuMgr, managerTV);
	}

	@Override
	public void setFocus() {
	}
	
	/**
	 * 모든 트리 목록을 리턴
	 * 
	 * @return
	 */
	public List<ManagerListDTO> getAllTreeList() {
		return treeDataList;
	}
	
	/**
	 * tree에 새로운 항목 추가
	 * 
	 * @param userDB
	 * @param defaultOpen default editor open
	 */
	public void addUserDB(UserDBDAO userDB, boolean defaultOpen) {
		
		for(ManagerListDTO dto: treeDataList) {
			if(dto.getName().equals(userDB.getGroup_name())) {
				dto.addLogin(userDB);
				
				selectAndOpenView(dto, userDB, defaultOpen);
				return;
			}	// end if(dto.getname()....		
		}	// end for
		
		// 신규 그룹이면...
		ManagerListDTO managerDto = new ManagerListDTO(userDB.getGroup_name());
		managerDto.addLogin(userDB);
		treeDataList.add(managerDto);	
		selectAndOpenView(managerDto, userDB, defaultOpen);
	}
	
	/**
	 * tree에 user resource 항목을 추가합니다.
	 * 
	 * @param userDB
	 */ 
	public void addManagerResouceData(UserDBDAO userDB, boolean isReload) {
		if(userDB.getListResource().isEmpty() || isReload) {
			// user_resource_data 목록을 추가해 줍니다.
			try {
				List<UserDBResourceDAO> listUserDBResources = TadpoleSystem_UserDBResource.userDbResourceTree(userDB);
				if(!listUserDBResources.isEmpty()) {
					ResourcesDAO resourcesDAO = new ResourcesDAO(userDB);
					resourcesDAO.setType(DB_RESOURCE_TYPE.USER_RESOURCE);
					resourcesDAO.setName(Messages.get().ManagerViewer_Resources);
					resourcesDAO.setListResource(listUserDBResources);
					userDB.getListResource().add(resourcesDAO);
				}
				
				// pgsql, oracle, mssql 은 스키마를 추가한다.
				if(userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
					PostgresqlConnectionEXT.connectionext(userDB);
				}
				managerTV.refresh(userDB, true);
				managerTV.expandToLevel(userDB, 1);
				
			} catch (Exception e) {
				logger.error("user_db_erd list", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().ManagerViewer_4, errStatus); //$NON-NLS-1$
			}
		}
	}
	
	/**
	 * 사용자 리소스가 추가되었을때
	 * 
	 * @param userDBErd
	 */
	public void addResource(int dbSeq) {
		for(ManagerListDTO dto: treeDataList) {
			
			for(UserDBDAO userDB : dto.getManagerList()) {
				if(userDB.getSeq() == dbSeq) {
					userDB.getListResource().clear();
					addManagerResouceData(userDB, true);
					
					return;
				}	// if(userDB.getSeq() == dbSeq) {
				
			}	// for(UserDBDAO
				
		}
	}
	
	/**
	 * change resource
	 * 
	 * @param originalResourceDB
	 */
	public void refreshResource(UserDBResourceDAO originalResourceDB) {
		managerTV.refresh(originalResourceDB);
	}
	
	public void deleteResource(UserDBResourceDAO userDBResource) {
		UserDBDAO userDB = userDBResource.getParent();
		IEditorReference iEditorReference = null;
		
		// 열린화면 검색
		if(userDBResource.getResource_types().equals(PublicTadpoleDefine.RESOURCE_TYPE.SQL.toString())) {
			iEditorReference = EditorUtils.findSQLEditor(userDBResource);
		} else if(userDBResource.getResource_types().equals(PublicTadpoleDefine.RESOURCE_TYPE.ERD.toString())) {
			iEditorReference = EditorUtils.findERDEditor(userDBResource);
		}
		
		// 열린 화면 닫기
		if(iEditorReference != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(iEditorReference.getEditor(false), true);		
		}
		
		// 삭제
		userDBResource.getParent().findResource(DB_RESOURCE_TYPE.USER_RESOURCE).getListResource().remove(userDBResource);
		managerTV.refresh(userDB);
	}
	
	/**
	 * 트리를 갱신하고 쿼리 창을 엽니다.
	 * @param managerDto 
	 * @param userDB
	 * @param defaultOpen 
	 */
	public void selectAndOpenView(ManagerListDTO managerDto, UserDBDAO userDB, boolean defaultOpen) {
		managerTV.refresh();
		managerTV.expandToLevel(managerDto, 2);
		managerTV.setSelection(new StructuredSelection(userDB), true);
		
		if(!defaultOpen) return;
		// mongodb 일경우 열지 않는다.
		if(userDB.getDBDefine() != DBDefine.MONGODB_DEFAULT) {
			MainEditorInput mei = new MainEditorInput(userDB);		
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				page.openEditor(mei, MainEditor.ID);
			} catch (PartInitException e) {
				logger.error("main editor open", e); //$NON-NLS-1$
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().ManagerViewer_10, errStatus); //$NON-NLS-1$
			}
		}
	}
	
	/** registery service handler */
	private void registerServiceHandler() {
		downloadServiceHandler = new DownloadServiceHandler();
		RWT.getServiceManager().registerServiceHandler(downloadServiceHandler.getId(), downloadServiceHandler);
	}
	
	/**
	 * SQLite file download
	 */
	public void download(final UserDBDAO userDB) {
		try {
			String strFileLoc = StringUtils.difference(StringUtils.remove(userDB.getDBDefine().getDB_URL_INFO(), "%s"), userDB.getUrl());
			File dbFile = new File(strFileLoc);
			
			byte[] arrayData = FileUtils.readFileToByteArray(dbFile);
			
			downloadServiceHandler.setContentType("");
			downloadServiceHandler.setName(dbFile.getName()); //$NON-NLS-1$
			downloadServiceHandler.setByteContent(arrayData);
			DownloadUtils.provideDownload(compositeMainComposite, downloadServiceHandler.getId());
		} catch(Exception e) {
			logger.error("SQLite file Download exception", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "DB Download Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * @return the managerTV
	 */
	public TreeViewer getManagerTV() {
		return managerTV;
	}
}

