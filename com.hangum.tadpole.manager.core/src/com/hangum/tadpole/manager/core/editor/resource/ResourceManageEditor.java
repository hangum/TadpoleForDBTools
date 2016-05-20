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
package com.hangum.tadpole.manager.core.editor.resource;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.ace.editor.core.widgets.TadpoleEditorWidget;
import com.hangum.tadpole.commons.dialogs.message.TadpoleSimpleMessageDialog;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.RESOURCE_TYPE;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.ResourceManagerDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.dao.system.userdb.ResourcesDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.engine.restful.RESTfulAPIUtils;
import com.hangum.tadpole.manager.core.Messages;
import com.hangum.tadpole.manager.core.dialogs.api.APIServiceDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.actions.connections.QueryEditorAction;
import com.hangum.tadpole.rdb.core.actions.erd.mongodb.MongoDBERDViewAction;
import com.hangum.tadpole.rdb.core.actions.erd.rdb.RDBERDViewAction;
import com.hangum.tadpole.rdb.core.dialog.resource.ResourceDetailDialog;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.ColumnHeaderCreator;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultTableColumnFilter;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.TableViewColumnDefine;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.hangum.tadpole.session.manager.SessionManager;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.swtdesigner.ResourceManager;

/**
 * Resource Manager
 * 
 * @author hangum
 * 
 */
public class ResourceManageEditor extends EditorPart {
	public final static String ID = "com.hangum.tadpole.manager.core.editor.resource.manager"; //$NON-NLS-1$
	private static final Logger logger = Logger.getLogger(ResourceManageEditor.class);
	
	private ToolItem tltmHistory;
	
	private Label lblDbname;
	private TableViewer tableViewer;
	private Text textFilter;
	private DefaultTableColumnFilter columnFilter;

	private UserDBDAO userDB;
	private TadpoleEditorWidget textQuery;
	private Text textTitle;
	private Button btnSave;
	private ComboViewer comboShare;
	
	private Text textDescription;
	private Combo comboSupportAPI;
	private Text textAPIURL;
	private Text textAPIKey;
	
	/** selected resource manager */
	private ResourceManagerDAO resourceManagerDao = null;

	public ResourceManageEditor() {
		super();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		ResourceManagerEditorInput esqli = (ResourceManagerEditorInput) input;
		setPartName(esqli.getName());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 1;
		gl_parent.marginHeight = 1;
		gl_parent.horizontalSpacing = 1;
		gl_parent.marginWidth = 1;
		parent.setLayout(gl_parent);

		Composite compositeToolbar = new Composite(parent, SWT.NONE);
		compositeToolbar.setLayout(new GridLayout(2, false));
		compositeToolbar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		ToolBar toolBar = new ToolBar(compositeToolbar, SWT.FLAT | SWT.RIGHT);
		GridData gd_toolBar = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_toolBar.widthHint = 267;
		toolBar.setLayoutData(gd_toolBar);

		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.setToolTipText(Messages.get().Refresh);
		tltmRefresh.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/refresh.png")); //$NON-NLS-1$
		
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comboShare.getCombo().clearSelection();
				textTitle.setText(""); //$NON-NLS-1$
				textDescription.setText(""); //$NON-NLS-1$
				textQuery.setText(""); //$NON-NLS-1$

				refreshResouceData();
			}
		});
		
		tltmHistory = new ToolItem(toolBar, SWT.NONE);
		tltmHistory.setToolTipText("History");
		tltmHistory.setImage(GlobalImageUtils.getHistory());
		tltmHistory.setEnabled(false);
		
		tltmHistory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showHistory();
			}
		});
		
		lblDbname = new Label(compositeToolbar, SWT.NONE);
		lblDbname.setText(""); //$NON-NLS-1$

		columnFilter = new DefaultTableColumnFilter();
		
		Composite compositeFilter = new Composite(parent, SWT.NONE);
		compositeFilter.setLayout(new GridLayout(2, false));
		GridData gd_composite_2 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite_2.heightHint = 28;
		compositeFilter.setLayoutData(gd_composite_2);

		Label lblFilter = new Label(compositeFilter, SWT.NONE);
		lblFilter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilter.setText(Messages.get().Filter);

		textFilter = new Text(compositeFilter, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SEARCH | SWT.CANCEL);
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.Selection) {
					columnFilter.setSearchString(textFilter.getText());
					tableViewer.refresh();
				}
			}
		});

		Composite compositeResourceList = new Composite(parent, SWT.NONE);
		GridData gd_compositeResourceList = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_compositeResourceList.heightHint = 140;
		compositeResourceList.setLayoutData(gd_compositeResourceList);
		compositeResourceList.setLayout(new GridLayout(1, false));

		tableViewer = new TableViewer(compositeResourceList, SWT.BORDER | SWT.FULL_SELECTION);
		Table tableResource = tableViewer.getTable();
		tableResource.setHeaderVisible(true);
		tableResource.setLinesVisible(true);
		tableResource.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableViewer.addFilter(columnFilter);
		
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				if (tableViewer.getSelection().isEmpty()) {
					tltmHistory.setEnabled(false);
					return;
				}
				tltmHistory.setEnabled(true);

				StructuredSelection ss = (StructuredSelection) tableViewer.getSelection();
				resourceManagerDao = (ResourceManagerDAO) ss.getFirstElement();

				try {
					SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
					List<String> result = sqlClient.queryForList("userDbResourceData", resourceManagerDao); //$NON-NLS-1$

					comboShare.getCombo().select("PUBLIC".equals(resourceManagerDao.getShared_type()) ? 0 : 1); //$NON-NLS-1$
					textTitle.setText(resourceManagerDao.getName());
					textDescription.setText(resourceManagerDao.getDescription());
					comboSupportAPI.setText(resourceManagerDao.getRestapi_yesno());
					textAPIURL.setText(resourceManagerDao.getRestapi_uri()==null?"":resourceManagerDao.getRestapi_uri()); //$NON-NLS-1$
					textAPIKey.setText(resourceManagerDao.getRestapi_key());
					textQuery.setText(""); //$NON-NLS-1$
					
					StringBuffer sbData = new StringBuffer();
					for (String data : result) {
						sbData.append(data);
					}
					textQuery.setText(sbData.toString());
					
					btnSave.setEnabled(resourceManagerDao.getUser_seq() == SessionManager.getUserSeq());

				} catch (Exception e) {
					logger.error("Resource detail", e); //$NON-NLS-1$
				}

			}
		});

		tableViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				if (tableViewer.getSelection().isEmpty())
					return;

				StructuredSelection ss = (StructuredSelection) tableViewer.getSelection();
				ResourceManagerDAO dao = (ResourceManagerDAO) ss.getFirstElement();
		
				// TODO : 기존 데이터베이스 목록에 리소스를 표시하기 위한  DAO를 사용하는 부분과 호환성을 위해 변환.리소스DAO를 하나로 통합할 필요 있음.
				UserDBResourceDAO ad = new UserDBResourceDAO();
				ad.setResource_seq((int) dao.getResource_seq());
				ad.setName(dao.getName());
				ad.setParent(userDB);
		
				// db object를 클릭하면 쿼리 창이 뜨도록하고.
				if (PublicTadpoleDefine.RESOURCE_TYPE.ERD.toString().equals(dao.getResource_types())) {
					if (userDB != null && DBDefine.MONGODB_DEFAULT == userDB.getDBDefine()) {
						 MongoDBERDViewAction ea = new MongoDBERDViewAction();
						 ea.run(ad);
					} else {
						 RDBERDViewAction ea = new RDBERDViewAction();
						 ea.run(ad);
					}
				} else if (PublicTadpoleDefine.RESOURCE_TYPE.SQL.toString().equals(dao.getResource_types())) {
					QueryEditorAction qea = new QueryEditorAction();
					qea.run(ad);
				}
			}
		});

		Group grpQuery = new Group(parent, SWT.NONE);
		grpQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpQuery.setText(Messages.get().DetailItem);
		GridLayout gl_grpQuery = new GridLayout(1, false);
		gl_grpQuery.verticalSpacing = 1;
		gl_grpQuery.horizontalSpacing = 1;
		gl_grpQuery.marginHeight = 1;
		gl_grpQuery.marginWidth = 1;
		grpQuery.setLayout(gl_grpQuery);

		Composite compositeDetail = new Composite(grpQuery, SWT.NONE);
		GridLayout gl_compositeDetail = new GridLayout(7, false);
		gl_compositeDetail.marginHeight = 2;
		gl_compositeDetail.marginWidth = 2;
		gl_compositeDetail.verticalSpacing = 2;
		compositeDetail.setLayout(gl_compositeDetail);
		compositeDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Label lblNewLabel = new Label(compositeDetail, SWT.NONE);
		lblNewLabel.setText(Messages.get().Share);

		comboShare = new ComboViewer(compositeDetail, SWT.NONE | SWT.READ_ONLY);
		Combo cShare = comboShare.getCombo();
		cShare.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cShare.setItems(new String[] { "PUBLIC", "PRIVATE" }); //$NON-NLS-1$ //$NON-NLS-2$

		Label lblNewLabel_1 = new Label(compositeDetail, SWT.NONE);
		lblNewLabel_1.setText(Messages.get().Title);

		textTitle = new Text(compositeDetail, SWT.BORDER);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		btnSave = new Button(compositeDetail, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!MessageDialog.openConfirm(getSite().getShell(), Messages.get().Confirm, Messages.get().ResourceManageEditor_18)) return;
				
				try {
					String share_type = comboShare.getCombo().getText();
					share_type = (share_type == null || "".equals(share_type)) ? "PUBLIC" : share_type; //$NON-NLS-1$ //$NON-NLS-2$
					resourceManagerDao.setShared_type(share_type);
					resourceManagerDao.setName(textTitle.getText());
					resourceManagerDao.setDescription(textDescription.getText());
					resourceManagerDao.setRestapi_yesno(comboSupportAPI.getText());
					resourceManagerDao.setRestapi_uri(textAPIURL.getText());
					
					if(comboSupportAPI.getText().equals(PublicTadpoleDefine.YES_NO.YES.name()) && "".equals(resourceManagerDao.getRestapi_key())) { //$NON-NLS-1$
						resourceManagerDao.setRestapi_key(Utils.getUniqueID());	
					}
					
					if(!isValid(resourceManagerDao)) return;
					
					TadpoleSystem_UserDBResource.updateResourceHeader(resourceManagerDao);
					tableViewer.refresh(resourceManagerDao, true);
					
					MessageDialog.openInformation(getSite().getShell(), Messages.get().Confirm, Messages.get().ResourceManageEditor_23);
				} catch (Exception e1) {
					logger.error("save resource", e1); //$NON-NLS-1$
					MessageDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().ResourceManageEditor_26+ e1.getMessage());
				}
			}
			
			/**
			 * is valid
			 * @return
			 */
			private boolean isValid(ResourceManagerDAO dao) {
				int len = StringUtils.trimToEmpty(textTitle.getText()).length();
				if(len < 3) {
					MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().ResourceManageEditor_27); //$NON-NLS-1$
					textTitle.setFocus();
					return false;
				}

				// sql type 
				if(dao.getResource_types().equals(RESOURCE_TYPE.SQL.name())) {
					if(PublicTadpoleDefine.YES_NO.YES.name().equals(comboSupportAPI.getText())) {
						String strAPIURI = textAPIURL.getText().trim();
						
						if(strAPIURI.equals("")) { //$NON-NLS-1$
							MessageDialog.openWarning(getSite().getShell(), Messages.get().Warning, Messages.get().ResourceManageEditor_30);
							textAPIURL.setFocus();
							return false;
						}
						
						// check valid url. url pattern is must be /{parent}/{child}
						if(!RESTfulAPIUtils.validateURL(textAPIURL.getText())) {
							MessageDialog.openWarning(getSite().getShell(), Messages.get().Warning, Messages.get().ResourceManageEditor_32);
							
							textAPIURL.setFocus();
							return false;
						}
					}
				}
				
				try {
					TadpoleSystem_UserDBResource.userDBResourceDupUpdate(userDB, dao);
				} catch (Exception ee) {
					logger.error("Resource validate", ee); //$NON-NLS-1$
					MessageDialog.openError(null, Messages.get().Error, ee.getMessage()); //$NON-NLS-1$
					return false;
				}
				
				return true;
			}
		});
		btnSave.setText(Messages.get().Save);

//		Button btnDelete = new Button(composite, SWT.NONE);
//		btnDelete.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				if (tableViewer.getSelection().isEmpty()) return;
//
//				if(!MessageDialog.openConfirm(getSite().getShell(), Messages.get().Confirm, "Do you wont to delete?")) return;
//				StructuredSelection ss = (StructuredSelection) tableViewer.getSelection();
//				ResourceManagerDAO dao = (ResourceManagerDAO) ss.getFirstElement();
//
//				// 기존에 사용하던 좌측 트리(디비목록)에 리소스를 표시하지 않을 경우에는 dao를 통일해서 하나만 쓰게 수정이
//				// 필요함.
//				UserDBResourceDAO userDBResource = new UserDBResourceDAO();
//				userDBResource.setResource_seq((int) dao.getResource_seq());
//				userDBResource.setName(dao.getRes_title());
//				userDBResource.setParent(userDB);
//
//				try {
//					TadpoleSystem_UserDBResource.delete(userDBResource);
//					addUserResouceData(null);
//				} catch (Exception e1) {
//					logger.error("Resource delete " + dao.toString(), e1);
//				}
//			}
//		});
//		btnDelete.setText("Delete");

		Label lblDescription = new Label(compositeDetail, SWT.NONE);
		lblDescription.setText(Messages.get().Description);

		textDescription = new Text(compositeDetail, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.CANCEL | SWT.MULTI);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1);
		gd_textDescription.heightHint = 44;
		textDescription.setLayoutData(gd_textDescription);
		
		Label lblSupportApi = new Label(compositeDetail, SWT.NONE);
		lblSupportApi.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSupportApi.setText(Messages.get().ResourceManageEditor_35);
		
		comboSupportAPI = new Combo(compositeDetail, SWT.READ_ONLY);
		comboSupportAPI.add("YES"); //$NON-NLS-1$
		comboSupportAPI.add("NO"); //$NON-NLS-1$
		comboSupportAPI.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblApiURI = new Label(compositeDetail, SWT.NONE);
		lblApiURI.setText(Messages.get().ResourceManageEditor_38);
		
		textAPIURL = new Text(compositeDetail, SWT.BORDER);
		textAPIURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Button btnShowUrl = new Button(compositeDetail, SWT.NONE);
		btnShowUrl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if(!"".equals(textAPIURL.getText())) { //$NON-NLS-1$
					String strURL = RESTfulAPIUtils.makeURL(textQuery.getText(), textAPIURL.getText());
					
					TadpoleSimpleMessageDialog dialog = new TadpoleSimpleMessageDialog(getSite().getShell(), Messages.get().ResourceManageEditor_40, strURL);
					dialog.open();
				}
			}
		});
		btnShowUrl.setText(Messages.get().ResourceManageEditor_41);
		new Label(compositeDetail, SWT.NONE);
		new Label(compositeDetail, SWT.NONE);
		
		Label lblApiKey = new Label(compositeDetail, SWT.NONE);
		lblApiKey.setText(Messages.get().ResourceManageEditor_42);
		
		textAPIKey = new Text(compositeDetail, SWT.BORDER);
		textAPIKey.setEditable(false);
		textAPIKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Button btnApiExecute = new Button(compositeDetail, SWT.NONE);
		btnApiExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!"".equals(textAPIURL.getText())) { //$NON-NLS-1$
					APIServiceDialog dialog = new APIServiceDialog(getSite().getShell(), userDB, textQuery.getText(), resourceManagerDao);
					dialog.open();
				}
			}
		});
		btnApiExecute.setText(Messages.get().ResourceManageEditor_44);

		textQuery = new TadpoleEditorWidget(compositeDetail, SWT.BORDER, DBDefine.MYSQL_DEFAULT.getExt(), "", "");
		textQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 7, 1));

		createTableColumn();

		initUI();
		
		// Connection View에서 선택이벤트 받기.
		getSite().getPage().addSelectionListener(ManagerViewer.ID, managementViewerListener);
	}
	
	/**
	 * management의 tree가 선택되었을때
	 */
	private ISelectionListener managementViewerListener = new ISelectionListener() {

		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection is = (IStructuredSelection) selection;
				if(is.getFirstElement() instanceof UserDBDAO) {
					userDB = (UserDBDAO)is.getFirstElement();
					refreshResouceData();
				} else if(is.getFirstElement() instanceof UserDBResourceDAO) {
					UserDBResourceDAO dao = (UserDBResourceDAO)is.getFirstElement();
					userDB = dao.getParent();
					refreshResouceData();
				} else if(is.getFirstElement() instanceof ResourcesDAO) {
					ResourcesDAO dao = (ResourcesDAO)is.getFirstElement();
					userDB = dao.getUserDBDAO();
					refreshResouceData();
				}
			} // end selection			
		} // end selectionchange
	};

	@Override
	public void setFocus() {
	}

	/**
	 * initialize UI
	 */
	private void initUI() {
		final ManagerViewer managerView = (ManagerViewer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
		IStructuredSelection iss = (IStructuredSelection)managerView.getManagerTV().getSelection();
		if(iss.getFirstElement() instanceof UserDBDAO) {
			userDB = (UserDBDAO)iss.getFirstElement();
			
			refreshResouceData();
		}
		
		// google analytic
		AnalyticCaller.track(ResourceManageEditor.ID);
	}

	/**
	 * refresh resource 항목을 추가합니다.
	 * 
	 * @param dao
	 */
	public void refreshResouceData() {
		if(lblDbname.isDisposed()) return;
		
		lblDbname.setText(userDB.getDisplay_name());
		
		List<ResourceManagerDAO> listUserDBResources = new ArrayList<ResourceManagerDAO>();
		
		try {
			for (ResourceManagerDAO userDBResourceDAO : TadpoleSystem_UserDBResource.userDbResource(userDB)) {
				if(PublicTadpoleDefine.SHARED_TYPE.PUBLIC.toString().equals(userDBResourceDAO.getShared_type())) {
					listUserDBResources.add(userDBResourceDAO);
				// 리소스 중에서 개인 리소스만 넣도록 합니다.
				} else {
					if(SessionManager.getUserSeq() == userDBResourceDAO.getUser_seq()) {
						listUserDBResources.add(userDBResourceDAO);
					}
				}
			}

			tableViewer.setInput(listUserDBResources);
			tableViewer.refresh();

		} catch (Exception e) {
			logger.error("refresh list", e); //$NON-NLS-1$

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().ResourceManageEditor_45, errStatus); //$NON-NLS-1$
		}
	}
	
	@Override
	public void dispose() {
		if(managementViewerListener != null) managementViewerListener = null;
		
		super.dispose();
	}

	/**
	 * table column head를 생성합니다.
	 */
	private void createTableColumn() {

		TableViewColumnDefine[] tableColumnDef = new TableViewColumnDefine[] {
		new TableViewColumnDefine("RESOURCE_SEQ", Messages.get().ID, 50, SWT.RIGHT) //$NON-NLS-1$
				, new TableViewColumnDefine("RESOURCE_TYPES", Messages.get().Type, 60, SWT.CENTER) //$NON-NLS-1$
				, new TableViewColumnDefine("USER_NAME", Messages.get().User, 90, SWT.CENTER) //$NON-NLS-1$
				, new TableViewColumnDefine("RES_TITLE", Messages.get().Title, 150, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("RESTAPI_URI", Messages.get().APIURL, 150, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("SHARED_TYPE", Messages.get().Share, 70, SWT.CENTER) //$NON-NLS-1$
				, new TableViewColumnDefine("DESCRIPTION", Messages.get().Description, 250, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("CREATE_TIME", Messages.get().CreateDate, 120, SWT.LEFT) //$NON-NLS-1$
		};

		ColumnHeaderCreator.createColumnHeader(tableViewer, tableColumnDef);

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ResourceManagerLabelProvider(tableViewer));
	}
	
	/**
	 * show history button
	 */
	private void showHistory() {
		StructuredSelection ss = (StructuredSelection) tableViewer.getSelection();
		if(ss.isEmpty()) return;
		
		ResourceManagerDAO resourceManagerDao = (ResourceManagerDAO) ss.getFirstElement();
		
		ResourceDetailDialog dialog = new ResourceDetailDialog(getSite().getShell(), resourceManagerDao, null);
		dialog.open();
	}

}
