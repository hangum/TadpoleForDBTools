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
package com.hangum.tadpole.mongodb.erd.core.editor;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.emf.ecore.xmi.util.XMLProcessor;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.MatchHeightAction;
import org.eclipse.gef.ui.actions.MatchWidthAction;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ToggleSnapToGeometryAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.xml.sax.InputSource;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.engine.sql.dialog.save.ResourceSaveDialog;
import com.hangum.tadpole.mongodb.erd.core.Messages;
import com.hangum.tadpole.mongodb.erd.core.actions.AutoLayoutAction;
import com.hangum.tadpole.mongodb.erd.core.actions.TableSelectionAction;
import com.hangum.tadpole.mongodb.erd.core.dnd.TableTransferDropTargetListener;
import com.hangum.tadpole.mongodb.erd.core.dnd.TableTransferFactory;
import com.hangum.tadpole.mongodb.erd.core.part.TadpoleEditPartFactory;
import com.hangum.tadpole.mongodb.erd.core.utils.TadpoleModelUtils;
import com.hangum.tadpole.mongodb.erd.stanalone.Activator;
import com.hangum.tadpole.mongodb.model.DB;
import com.hangum.tadpole.mongodb.model.MongodbFactory;
import com.hangum.tadpole.mongodb.model.MongodbPackage;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * Tadpole DB Hub ERD editor
 * 
 * @author hangum
 *
 */
public class TadpoleMongoDBERDEditor extends GraphicalEditor {//WithFlyoutPalette {
	public static final String ID = "com.hangum.tadpole.mongodb.erd.core.editor"; //$NON-NLS-1$
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleMongoDBERDEditor.class);
	/** 사용자 seq */
	private final int user_seq = SessionManager.getUserSeq();
	
	/** first init data */
	private DB db;
	private UserDBDAO userDB;
	private UserDBResourceDAO userDBErd;
	/** 처음로드될때부터 모든 테이블 로드 인지 */
	private boolean isAllTable = false;
	
	/** auto layout action */
	private AutoLayoutAction autoLayoutAction;
	
	/** short key handler */
	private KeyHandler keyHandler;

	/** dnd */
	TableTransferFactory tableTransFactory = new TableTransferFactory();

	public TadpoleMongoDBERDEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}
	
	@Override
	protected void initializeGraphicalViewer() {
//		super.initializeGraphicalViewer();
		
		Job job = new Job("ERD Initialize") {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("ERD Initialize", IProgressMonitor.UNKNOWN);
		
				try {
					if(db == null) {
						// 모든 table 정보를 가져온다.
						if(isAllTable) {
							db = TadpoleModelUtils.INSTANCE.getDBAllTable(monitor, userDB);
						// 부분 테이블 정보를 처리한다.
						} else {
							MongodbFactory factory = MongodbFactory.eINSTANCE;
							db = factory.createDB();
						}
						
						db.setDbType(userDB.getDbms_type() + " (" + userDB.getDisplay_name() + ")");
					}
					
				} catch(Exception e) {
					logger.error("ERD Initialize excepiton", e);
					
					return new Status(Status.WARNING, Activator.PLUGIN_ID, e.getMessage());
				} finally {
					monitor.done();
				}
						
				/////////////////////////////////////////////////////////////////////////////////////////
				return Status.OK_STATUS;
			}
		};
		
		// job의 event를 처리해 줍니다.
		final TadpoleMongoDBERDEditor mongoEditor = this;
		job.addJobChangeListener(new JobChangeAdapter() {
			
			public void done(IJobChangeEvent event) {
				final IJobChangeEvent jobEvent = event; 
				getSite().getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if(!jobEvent.getResult().isOK()) {
							Exception e = new Exception(jobEvent.getResult().getException());
							Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
							ExceptionDetailsErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages.get().Error, Messages.get().TadpoleModelUtils_2, errStatus); //$NON-NLS-1$
							
							// 오류가 발생했을때는 기본 정보로 
							MongodbFactory factory = MongodbFactory.eINSTANCE;
							db = factory.createDB();
							db.setDbType(userDB.getDbms_type());
							db.setId(userDB.getUsers());
							db.setUrl(userDB.getUrl());
//						} else {
//							logger.debug("###change event object strat #####################################");
//							commandStackChanged(new EventObject(""));
//							logger.debug("###change event object end #####################################");
						}
						getGraphicalViewer().setContents(db);
						
						// dnd
						getGraphicalViewer().addDropTargetListener(new TableTransferDropTargetListener(mongoEditor, getGraphicalViewer(), userDB, db));
					}
					
				});	// end display.asyncExec
			}	// end done
			
		});	// end job0
		
		job.setName(userDB.getDisplay_name());
		job.setUser(true);
		job.schedule();		
	}	
	
	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new TadpoleEditPartFactory());
		
		// zoom menu
		zoomContribution(viewer);
		
		// layout action
		createDiagramAction(viewer);
		
		// context menu
		ContextMenuProvider provider = new TadpoleERDContextMenuProvider(viewer, getActionRegistry());
		viewer.setContextMenu(provider);

		// key handler
		configureKeyHandler();
		
		// grid and geometry
		configureGeometry();
		configureGrid();
	}
	
	/**
	 * configure key handler
	 */
	private void configureKeyHandler() {
		GraphicalViewer viewer = getGraphicalViewer();
		
		keyHandler = new KeyHandler();
//		keyHandler.put(KeyStroke.getPressed('a', 0x61, SWT.COMMAND),	getActionRegistry().getAction(ActionFactory.SELECT_ALL.getId()));
		keyHandler.put(KeyStroke.getPressed('s', 0x61, SWT.CTRL),		getActionRegistry().getAction(ActionFactory.SAVE.getId()));
		
		keyHandler.put(KeyStroke.getPressed('z', 0x7a, SWT.CTRL),			getActionRegistry().getAction(ActionFactory.UNDO.getId()));
		keyHandler.put(KeyStroke.getPressed('z', 0x7a, SWT.CTRL | SWT.SHIFT),getActionRegistry().getAction(ActionFactory.REDO.getId()));
		keyHandler.put(KeyStroke.getPressed('a', 0x61, SWT.CTRL),			getActionRegistry().getAction(ActionFactory.SELECT_ALL.getId()));
		
		keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), 				getActionRegistry().getAction(ActionFactory.DELETE.getId()));
		keyHandler.put(KeyStroke.getPressed('+', SWT.KEYPAD_ADD, 0), 		getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
		keyHandler.put(KeyStroke.getPressed('-', SWT.KEYPAD_SUBTRACT, 0), 	getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
		
		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.NONE), MouseWheelZoomHandler.SINGLETON);
		viewer.setKeyHandler(keyHandler);
	}
	
	/**
	 * configure grid
	 */
	private void configureGrid() {
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, true);
		viewer.setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, true);
		
		IAction action = new ToggleGridAction(viewer);
		getActionRegistry().registerAction(action);
	}

	/**
	 * configure geometry
	 */
	private void configureGeometry() {
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, true);
		
		IAction action = new ToggleSnapToGeometryAction(viewer);
		getActionRegistry().registerAction(action);
	}
	
	private void zoomContribution(GraphicalViewer viewer) {
		double[] zoomLevels;
		List<String> zoomContributions;
		
		ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
		viewer.setRootEditPart(rootEditPart);
		
		ZoomManager manager = rootEditPart.getZoomManager();
		getActionRegistry().registerAction(new ZoomInAction(manager));
		getActionRegistry().registerAction(new ZoomOutAction(manager));
		
		zoomLevels = new double[]{0.25, 0.5, 0.75, 1.0, 1.5, 2.0, 2.5, 3.0, 5.0, 10.0, 20.0};
		manager.setZoomLevels(zoomLevels);
		
		zoomContributions = new ArrayList<String>();
//		zoomContributions.add(ZoomManager.FIT_ALL);
//		zoomContributions.add(ZoomManager.FIT_HEIGHT);
//		zoomContributions.add(ZoomManager.FIT_WIDTH);
		manager.setZoomLevelContributions(zoomContributions);
	}

	private void createDiagramAction(GraphicalViewer viewer) {
		ActionRegistry registry = getActionRegistry();
		autoLayoutAction = new AutoLayoutAction(this, getGraphicalViewer());
		registry.registerAction(autoLayoutAction);
		getSelectionActions().add(autoLayoutAction.getId());
		
		IAction action = new TableSelectionAction(this, getGraphicalViewer());
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
	}
	
	@Override
	protected void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();
		
		IAction action = new MatchWidthAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new MatchHeightAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.LEFT);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.RIGHT);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.TOP);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.BOTTOM);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.CENTER);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.MIDDLE);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		loadDBRsource(input);
	}
	
	/**
	 * resource load
	 * 
	 * @param input
	 */
	private void loadDBRsource(IEditorInput input) {
		TadpoleMongoDBEditorInput erdInput = (TadpoleMongoDBEditorInput)input;
		userDB = erdInput.getUserDBDAO();
		isAllTable = erdInput.isAllTable();
		
		// 신규로드 인지 기존 파일 로드 인지 검사합니다.
		if(null != erdInput.getUserDBERD()) { 
			userDBErd = erdInput.getUserDBERD();
			
			// load resouce
			try {
				String xmlString = TadpoleSystem_UserDBResource.getResourceData(userDBErd);
				
				// 처음 로드 할때 ResourceSet에 instance가 등록 되어 있어야 합니다.
				/**
				 * <code>TadpolePackage.eNS_URI</code>
				 */
				ResourceSet resourceSet = new ResourceSetImpl();
				if(resourceSet.getPackageRegistry().get("http://com.hangum.tadpole.mongodb.model.ERDInfo") == null) {
					resourceSet.getPackageRegistry().put("http://com.hangum.tadpole.mongodb.model.ERDInfo", MongodbPackage.eINSTANCE.getClass());
				}
				
				// 
				XMLResourceImpl resource = new XMLResourceImpl();
				resource.setEncoding("UTF-8");
		        resource.load(new InputSource(new StringReader(xmlString)), null);
				db = (DB)resource.getContents().get(0);
				
			} catch(Exception e) {
				logger.error("Load ERD Resource", e); //$NON-NLS-1$
		        
		        Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().TadpoleEditor_0, errStatus); //$NON-NLS-1$
			}
			
			setPartName(isAllTable?"All " + userDBErd.getName():userDBErd.getName());
			setTitleToolTip(userDB.getDisplay_name());
		} else {
			setPartName(isAllTable?"All " + userDB.getDisplay_name():userDB.getDisplay_name());
			setTitleToolTip(userDB.getDisplay_name());
		}
		
		// google analytic
		AnalyticCaller.track(TadpoleMongoDBERDEditor.ID, userDB.getDbms_type());
	}
	
//	@Override
//	protected PaletteRoot getPaletteRoot() {
//		return null;
//	}
//	
//	@Override
//	public void doSaveAs() {
//		super.doSaveAs();
//	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		
		// 신규 저장이면 
		if(userDBErd == null) {
			
			// file 이름 dialog
			ResourceSaveDialog rsDialog = new ResourceSaveDialog(null, null, userDB, PublicTadpoleDefine.RESOURCE_TYPE.ERD, "");
			if (rsDialog.open() == Window.OK) {
				
				try {
					// erd 정보 디비저장
					userDBErd = TadpoleSystem_UserDBResource.saveResource(userDB, rsDialog.getRetResourceDao(), createResourceToString());
					userDBErd.setParent(userDB);
					
					// command stack 초기화
					getCommandStack().markSaveLocation();
					
					// title 수정
					setPartName(userDBErd.getName());

					// managerView tree refresh
					// 뒤에 시간을붙인것은 한번 저장한 db_seq는 업데이지 않는 오류를 방지하기위해...
					//
					PlatformUI.getPreferenceStore().setValue(PublicTadpoleDefine.SAVE_FILE, ""+userDBErd.getDb_seq() + ":" + System.currentTimeMillis()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					
				} catch (Exception e) {
					logger.error("new save error", e);
					
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().TadpoleEditor_3, errStatus); //$NON-NLS-1$
				}
			}
			
		// 기존 리소스를 가지고 있었으면 
		} else {
			
			try {
				TadpoleSystem_UserDBResource.updateResource(userDBErd, createResourceToString());
				getCommandStack().markSaveLocation();
			} catch(Exception e) {
				logger.error("Resource data", e);
				
				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().TadpoleEditor_1, errStatus); //$NON-NLS-1$
			}
		}
	}
	
	/**
	 * model to string
	 * 
	 * @return
	 * @throws Exception
	 */
	private String createResourceToString() throws Exception {
		XMLResourceImpl resource = new XMLResourceImpl();
        XMLProcessor processor = new XMLProcessor();
        resource.setEncoding("UTF-8");
        resource.getContents().add(db);
        return processor.saveToString(resource, null);
	}
	
	@Override
	public void commandStackChanged(EventObject event) {
		firePropertyChange(PROP_DIRTY);
		super.commandStackChanged(event);
	}

	@Override
	public Object getAdapter(Class type) {
		if(type == ZoomManager.class) { 
			return ((ScalableRootEditPart)getGraphicalViewer().getRootEditPart()).getZoomManager();
		}
 
//		if(type == IContentOutlinePage.class) {
//			return new OutlinePage();
//		}
		
		return super.getAdapter(type);
	}
	
	public UserDBResourceDAO getUserDBErd() {
		return userDBErd;
	}
	
	/**
	 * @return the userDB
	 */
	public UserDBDAO getUserDB() {
		return userDB;
	}

//	/**
//	 * outline page
//	 * @author hangum
//	 *
//	 */
//	protected class OutlinePage extends ContentOutlinePage {
//		private SashForm sash;
////		private ScrollableThumbnail thumbnail;
////		private DisposeListener disposeListener;
//		
//		public OutlinePage() {
//			super(new TreeViewer());
//		}
//		
//		@Override
//		public void createControl(Composite parent) {
//			sash = new SashForm(parent, SWT.VERTICAL);
//			
//			getViewer().createControl(sash);
//			getViewer().setEditDomain(getEditDomain());
//			getViewer().setEditPartFactory(new TadpoleTreeEditPartFactory());
//			getViewer().setContents(db);
//			getSelectionSynchronizer().addViewer(getViewer());
//
////			
//			// thumbnail 백그라운드가 검은색으로 나와서 주석으로 막습니다. ( http://hangumkj.blogspot.com/2012/02/rap-gef-port.html )
////			
////			Canvas canvas = new Canvas(sash, SWT.BORDER);
////			LightweightSystem lws = new LightweightSystem(canvas);
////			
////			thumbnail = new ScrollableThumbnail((Viewport)((ScalableRootEditPart)getGraphicalViewer().getRootEditPart()).getFigure());
////			thumbnail.setSource(((ScalableRootEditPart)getGraphicalViewer().getRootEditPart()).getLayer(LayerConstants.PRINTABLE_LAYERS));
////			lws.setContents(thumbnail);
////			
////			disposeListener = new DisposeListener() {
////				
////				@Override
////				public void widgetDisposed(DisposeEvent event) {
////					if(thumbnail != null) {
////						thumbnail.deactivate();
////						thumbnail = null;
////					}
////				}
////			};
////			getGraphicalViewer().getControl().addDisposeListener(disposeListener);
//			
//		}
//		
//		@Override
//		public void init(IPageSite pageSite) {
//			super.init(pageSite);
//			
//			IActionBars bars = getSite().getActionBars();
//			bars.setGlobalActionHandler(ActionFactory.UNDO.getId(), getActionRegistry().getAction(ActionFactory.UNDO.getId()));
//			bars.setGlobalActionHandler(ActionFactory.REDO.getId(), getActionRegistry().getAction(ActionFactory.REDO.getId()));
//			bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), getActionRegistry().getAction(ActionFactory.DELETE.getId()));
//			bars.updateActionBars();
//			
//			getViewer().setKeyHandler(keyHandler);
//		}
//		
//		@Override
//		public Control getControl() {
//			return sash;
//		}
//		
//		@Override
//		public void dispose() {
//			getSelectionSynchronizer().removeViewer(getViewer());
////			if(getGraphicalViewer().getControl() != null && !getGraphicalViewer().getControl().isDisposed())
////				getGraphicalViewer().getControl().removeDisposeListener(disposeListener);
//			
//			super.dispose();
//		}
//	}
}
