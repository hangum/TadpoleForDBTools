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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.dialogs.message.TadpoleSimpleMessageDialog;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.ManagerListDTO;
import com.hangum.tadpole.engine.query.dao.ResourceManagerDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.actions.connections.QueryEditorAction;
import com.hangum.tadpole.rdb.core.actions.erd.mongodb.MongoDBERDViewAction;
import com.hangum.tadpole.rdb.core.actions.erd.rdb.RDBERDViewAction;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.ColumnHeaderCreator;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultLabelProvider;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultTableColumnFilter;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.TableViewColumnDefine;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.main.MainEditorInput;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.swtdesigner.ResourceManager;

/**
 * Resource Manager
 * 
 * @author hangum
 * 
 */
public class ResourceManageEditor extends EditorPart {
	public final static String ID = "com.hangum.tadpole.manager.core.editor.resource.manager";
	private static final Logger logger = Logger.getLogger(ResourceManageEditor.class);

	private UserDBDAO userDB;
	private Text textQuery;
	private Text textTitle;
	private ComboViewer comboViewer;
	private Combo comboShare;
	private TableViewer tableViewer;
	private Table tableResource;
	private Text textFilter;
	private DefaultTableColumnFilter columnFilter;

	List<ManagerListDTO> treeList = new ArrayList<ManagerListDTO>();
	private TreeViewer treeViewer;
	private Text textDescription;
	private Combo comboSupportAPI;
	private Text textAPIKey;

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
		compositeToolbar.setLayout(new GridLayout(1, false));
		compositeToolbar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		ToolBar toolBar = new ToolBar(compositeToolbar, SWT.FLAT | SWT.RIGHT);
		GridData gd_toolBar = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_toolBar.widthHint = 267;
		toolBar.setLayoutData(gd_toolBar);

		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.setToolTipText("Refresh");
		tltmRefresh.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/refresh.png")); //$NON-NLS-1$
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comboViewer.getCombo().clearSelection();
				textTitle.setText("");
				textDescription.setText("");
				textQuery.setText("");

				initUI();
				//reLoadResource();
			}
		});

		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		columnFilter = new DefaultTableColumnFilter();

		SashForm sashForm_1 = new SashForm(sashForm, SWT.NONE);

		treeViewer = new TreeViewer(sashForm_1, SWT.BORDER);
		Tree treeDatabase = treeViewer.getTree();
		treeDatabase.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);

		Composite composite_1 = new Composite(sashForm_1, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));

		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, false));
		GridData gd_composite_2 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite_2.heightHint = 28;
		composite_2.setLayoutData(gd_composite_2);

		Label lblFilter = new Label(composite_2, SWT.NONE);
		lblFilter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilter.setText("Filter");

		textFilter = new Text(composite_2, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SEARCH | SWT.CANCEL);
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

		tableViewer = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		tableResource = tableViewer.getTable();
		tableResource.setHeaderVisible(true);
		tableResource.setLinesVisible(true);
		tableResource.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sashForm_1.setWeights(new int[] { 230, 359 });
		tableViewer.addFilter(columnFilter);

		Group grpQuery = new Group(sashForm, SWT.NONE);
		grpQuery.setText("Query");
		GridLayout gl_grpQuery = new GridLayout(1, false);
		gl_grpQuery.verticalSpacing = 1;
		gl_grpQuery.horizontalSpacing = 1;
		gl_grpQuery.marginHeight = 1;
		gl_grpQuery.marginWidth = 1;
		grpQuery.setLayout(gl_grpQuery);

		Composite composite = new Composite(grpQuery, SWT.NONE);
		GridLayout gl_composite = new GridLayout(6, false);
		gl_composite.marginHeight = 2;
		gl_composite.marginWidth = 2;
		gl_composite.verticalSpacing = 2;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText("Share");

		comboViewer = new ComboViewer(composite, SWT.NONE);
		comboShare = comboViewer.getCombo();
		comboShare.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboShare.setItems(new String[] { "PUBLIC", "PRIVATE" });

		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setText("Title");

		textTitle = new Text(composite, SWT.BORDER);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO : SAVE...공유 구분및 제목 변경.
				if (tableViewer.getSelection().isEmpty())
					return;

				StructuredSelection ss = (StructuredSelection) tableViewer.getSelection();
				ResourceManagerDAO dao = (ResourceManagerDAO) ss.getFirstElement();

				try {
					String share_type = comboShare.getText();
					share_type = (share_type == null || "".equals(share_type)) ? "PUBLIC" : share_type;
					dao.setShared_type(share_type);
					dao.setRes_title(textTitle.getText());
					dao.setDescription(textDescription.getText());
					dao.setRestapi_yesno(comboSupportAPI.getText());
					dao.setRestapi_key(textAPIKey.getText());
					
					TadpoleSystem_UserDBResource.updateResourceHeader(dao);
					//reLoadResource();
					addUserResouceData(dao);
					
					MessageDialog.openInformation(getSite().getShell(), "Confirm", "Save resource data.");
				} catch (Exception e1) {
					logger.error("save resource", e1);
					MessageDialog.openError(getSite().getShell(), "Error", "Rise exception.\n"+ e1.getMessage());
				}
			}
		});
		btnSave.setText("Save");

//		Button btnDelete = new Button(composite, SWT.NONE);
//		btnDelete.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				if (tableViewer.getSelection().isEmpty()) return;
//
//				if(!MessageDialog.openConfirm(getSite().getShell(), "Confirm", "Do you wont to delete?")) return;
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

		Label lblDescription = new Label(composite, SWT.NONE);
		lblDescription.setText("Description");

		textDescription = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.CANCEL | SWT.MULTI);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1);
		gd_textDescription.heightHint = 44;
		textDescription.setLayoutData(gd_textDescription);
		
		Label lblSupportApi = new Label(composite, SWT.NONE);
		lblSupportApi.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSupportApi.setText("Support API");
		
		comboSupportAPI = new Combo(composite, SWT.READ_ONLY);
		comboSupportAPI.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if("YES".equals(comboSupportAPI.getText())) {
					if("".equals(textAPIKey.getText())) {
						textAPIKey.setText(Utils.getUniqueID());
					}
				}
			}
		});
		comboSupportAPI.add("YES");
		comboSupportAPI.add("NO");
		comboSupportAPI.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblApiKey = new Label(composite, SWT.NONE);
		lblApiKey.setText("API KEY");
		
		textAPIKey = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		textAPIKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Button btnShowUrl = new Button(composite, SWT.NONE);
		btnShowUrl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				String strURL = String.format("%s?%s=%s&1=FirstParameter&2=SecondParameter", 
						"http://yoururl.com", 
						PublicTadpoleDefine.SERVICE_KEY_NAME, textAPIKey.getText());

				TadpoleSimpleMessageDialog dialog = new TadpoleSimpleMessageDialog(getSite().getShell(), "API URI", strURL);
				dialog.open();
			}
		});
		btnShowUrl.setText("Show URL");
		new Label(composite, SWT.NONE);

		textQuery = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
		sashForm.setWeights(new int[] { 179, 242 });

		createTableColumn();

		initUI();

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	/**
	 * table column head를 생성합니다.
	 */
	private void createTableColumn() {

		TableViewColumnDefine[] tableColumnDef = new TableViewColumnDefine[] { //
		new TableViewColumnDefine("RESOURCE_SEQ", "ID", 50, SWT.RIGHT) //
				, new TableViewColumnDefine("RESOURCE_TYPES", "Type", 60, SWT.CENTER) //
				, new TableViewColumnDefine("USER_NAME", "User", 90, SWT.CENTER) //
				, new TableViewColumnDefine("RES_TITLE", "Subject", 150, SWT.LEFT) //
				, new TableViewColumnDefine("SHARED_TYPE", "Share", 70, SWT.CENTER) //
				, new TableViewColumnDefine("RESTAPI_YESNO", "Enable API", 100, SWT.CENTER) //
				, new TableViewColumnDefine("RESTAPI_KEY", "API Key", 200, SWT.CENTER) //
				, new TableViewColumnDefine("CREATE_TIME", "Create", 100, SWT.CENTER) //
				, new TableViewColumnDefine("DESCRIPTION", "Description", 250, SWT.LEFT) //
		};

		ColumnHeaderCreator.createColumnHeader(tableViewer, tableColumnDef);

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new DefaultLabelProvider(tableViewer));

	}

	private void initUI() {
		treeViewer.setContentProvider(new ResourceManagerContentProvider());
		treeViewer.setLabelProvider(new ResourceManagerLabelProvider());
		treeViewer.setInput(treeList);
		getSite().setSelectionProvider(treeViewer);

		treeViewer.getTree().clearAll(true);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection is = (IStructuredSelection) event.getSelection();
				if (is.getFirstElement() instanceof UserDBDAO) {
					userDB = (UserDBDAO) is.getFirstElement();
					addUserResouceData(null);
				}

				//
				// 아래 코드(managerTV.getControl().setFocus();)가 없으면, 오브젝트 탐색기의
				// event listener가 동작하지 않는다.
				// 이유는 글쎄 모르겠어.
				//
				treeViewer.getControl().setFocus();
			}
		});

		tableViewer.setInput(null);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {

				if (tableViewer.getSelection().isEmpty())
					return;

				StructuredSelection ss = (StructuredSelection) tableViewer.getSelection();
				ResourceManagerDAO dao = (ResourceManagerDAO) ss.getFirstElement();

				try {
					SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
					List<String> result = sqlClient.queryForList("userDbResourceData", dao); //$NON-NLS-1$

					comboShare.select("PUBLIC".equals(dao.getShared_type()) ? 0 : 1);
					textTitle.setText(dao.getRes_title());
					textDescription.setText(dao.getDescription());
					comboSupportAPI.setText(dao.getRestapi_yesno());
					textAPIKey.setText(dao.getRestapi_key()==null?"":dao.getRestapi_key());
					textQuery.setText("");
					for (String data : result) {
						textQuery.append(data);
					}

				} catch (Exception e) {
					logger.error("Resource detail", e);
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
		
				// 기존 데이터베이스 목록에 리소스를 표시하기 위한  DAO를 사용하는 부분과 호환성을 위해 변환.
				// TODO : 리소스DAO를 하나로 통합할 필요 있음.
				UserDBResourceDAO ad = new UserDBResourceDAO();
				ad.setResource_seq((int) dao.getResource_seq());
				ad.setName(dao.getRes_title());
				ad.setParent(userDB);
		
				// db object를 클릭하면 쿼리 창이 뜨도록하고.
				if (PublicTadpoleDefine.RESOURCE_TYPE.ERD.toString().equals(dao.getResource_types())) {
					if (userDB != null && DBDefine.MONGODB_DEFAULT == DBDefine.getDBDefine(userDB)) {
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
		reLoadResource();
		
		// google analytic
		AnalyticCaller.track(ResourceManageEditor.ID);
	}

	public void reLoadResource() {

		try {
			treeList.clear();
			List<UserDBDAO> userDBS = TadpoleSystem_UserDBQuery.getUserDB();
			// 그룹 이름을 생성합니다.
			List<String> groupNames = new ArrayList<String>();
			for (UserDBDAO userDBDAO : userDBS) {
				if(!groupNames.contains(userDBDAO.getGroup_name())) {
					groupNames.add(userDBDAO.getGroup_name());
				}
			}

			for (UserDBDAO userDBDAO : userDBS) {
				addUserDB(userDBDAO, false);
			}

		} catch (Exception e) {
			logger.error("initialize Managerview", e);

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", "Can't load database.", errStatus); //$NON-NLS-1$
		}

		treeViewer.refresh();
		treeViewer.expandToLevel(2);

	}

	/**
	 * tree에 user resource 항목을 추가합니다.
	 * 
	 * @param userDB
	 */
	public void addUserResouceData(ResourceManagerDAO dao) {

		try {
//			comboViewer.getCombo().clearSelection();
//			textTitle.setText("");
//			textDescription.setText("");
//			textQuery.setText("");
//			comboSupportAPI.select(0);
//			textAPIKey.setText("");

			List<ResourceManagerDAO> listUserDBResources = TadpoleSystem_UserDBResource.userDbResource(userDB);

			tableViewer.setInput(listUserDBResources);
			tableViewer.refresh(dao, true);

		} catch (Exception e) {
			logger.error("user_db_erd list", e); //$NON-NLS-1$

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", "Can't load resource...", errStatus); //$NON-NLS-1$
		}

	}

	/**
	 * tree에 새로운 항목 추가
	 * 
	 * @param userDB
	 * @param defaultOpen
	 *            default editor open
	 */
	public void addUserDB(UserDBDAO userDB, boolean defaultOpen) {
		for (ManagerListDTO dto : treeList) {
			if (dto.getName().equals(userDB.getGroup_name())) {
				dto.addLogin(userDB);

				if (defaultOpen) {
					selectAndOpenView(userDB);
					treeViewer.expandToLevel(userDB, 2);
				}
				return;
			} // end if(dto.getname()....
		} // end for

		// 신규 그룹이면...
		ManagerListDTO managerDto = new ManagerListDTO(userDB.getGroup_name());
		managerDto.addLogin(userDB);
		treeList.add(managerDto);

		if (defaultOpen) {
			selectAndOpenView(userDB);
			treeViewer.expandToLevel(userDB, 2);
		}
	}

	/**
	 * 트리를 갱신하고 쿼리 창을 엽니다.
	 * 
	 * @param dto
	 */
	public void selectAndOpenView(UserDBDAO dto) {
		treeViewer.refresh();
		treeViewer.setSelection(new StructuredSelection(dto), true);

		// mongodb 일경우 열지 않는다.
		if (DBDefine.getDBDefine(dto) != DBDefine.MONGODB_DEFAULT) {
			MainEditorInput mei = new MainEditorInput(dto);
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				page.openEditor(mei, MainEditor.ID);
			} catch (PartInitException e) {
				logger.error("main editor open", e); //$NON-NLS-1$

				Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
				ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", "Can't open resource. ", errStatus); //$NON-NLS-1$
			}
		}
	}

}
