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
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
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
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.ColumnHeaderCreator;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultLabelProvider;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultTableColumnFilter;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.TableViewColumnDefine;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.main.MainEditorInput;
import com.hangum.tadpole.sql.dao.ManagerListDTO;
import com.hangum.tadpole.sql.dao.ResourceManagerDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.session.manager.SessionManager;
import com.hangum.tadpole.sql.system.TadpoleSystemInitializer;
import com.hangum.tadpole.sql.system.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.sql.system.TadpoleSystem_UserDBResource;
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
	private TableViewer tableViewer;
	private Table tableResource;
	private Text textFilter;
	private DefaultTableColumnFilter columnFilter;

	List<ManagerListDTO> treeList = new ArrayList<ManagerListDTO>();
	private TreeViewer treeViewer;

	public ResourceManageEditor() {
		super();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
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

			}
		});

		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite compositeBody = new Composite(sashForm, SWT.NONE);
		compositeBody.setLayout(new GridLayout(2, false));

		treeViewer = new TreeViewer(compositeBody, SWT.BORDER);
		Tree treeDatabase = treeViewer.getTree();
		GridData gd_treeDatabase = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_treeDatabase.widthHint = 126;
		treeDatabase.setLayoutData(gd_treeDatabase);
		treeDatabase.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);

		Composite composite_1 = new Composite(compositeBody, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_composite_1.widthHint = 407;
		composite_1.setLayoutData(gd_composite_1);
		composite_1.setLayout(new GridLayout(1, false));

		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, false));
		GridData gd_composite_2 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite_2.heightHint = 28;
		composite_2.setLayoutData(gd_composite_2);

		Label lblFilter = new Label(composite_2, SWT.NONE);
		lblFilter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilter.setText("Filter");

		textFilter = new Text(composite_2, SWT.BORDER);
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		tableViewer = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		tableResource = tableViewer.getTable();
		tableResource.setHeaderVisible(true);
		tableResource.setLinesVisible(true);
		tableResource.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		createTableColumn();
		columnFilter = new DefaultTableColumnFilter();
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
		composite.setLayout(new GridLayout(6, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite.heightHint = 35;
		composite.setLayoutData(gd_composite);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Share");

		ComboViewer comboViewer = new ComboViewer(composite, SWT.NONE);
		Combo comboShare = comboViewer.getCombo();
		GridData gd_comboShare = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_comboShare.widthHint = 90;
		comboShare.setLayoutData(gd_comboShare);

		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Title");

		textTitle = new Text(composite, SWT.BORDER);
		GridData gd_textTitle = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textTitle.widthHint = 195;
		textTitle.setLayoutData(gd_textTitle);

		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.setText("Save");

		Button btnDelete = new Button(composite, SWT.NONE);
		btnDelete.setText("Delete");

		textQuery = new Text(grpQuery, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		sashForm.setWeights(new int[] { 7, 3 });

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
		new TableViewColumnDefine("RESOURCE_SEQ", "Resource ID", 50, SWT.RIGHT) //
				, new TableViewColumnDefine("RESOURCE_TYPES", "Resource Type", 60, SWT.CENTER) //
				, new TableViewColumnDefine("USER_NAME", "User Name", 90, SWT.CENTER) //
				, new TableViewColumnDefine("RES_TITLE", "Resource Title", 150, SWT.LEFT) //
				, new TableViewColumnDefine("SHARED_TYPE", "Shared type", 70, SWT.CENTER) //
				, new TableViewColumnDefine("CREATE_TIME", "Create time", 100, SWT.CENTER) //
				, new TableViewColumnDefine("DESCRIPTION", "Description", 250, SWT.LEFT) //
		};

		ColumnHeaderCreator.createColumnHeader(tableViewer, tableColumnDef);

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new DefaultLabelProvider(tableViewer));

	}

	public void initUI() {
		treeViewer.setContentProvider(new ResourceManagerContentProvider());
		treeViewer.setLabelProvider(new ResourceManagerLabelProvider());
		treeViewer.setInput(treeList);
		getSite().setSelectionProvider(treeViewer);

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection is = (IStructuredSelection) event.getSelection();
				if (is.getFirstElement() instanceof UserDBDAO) {
					userDB = (UserDBDAO) is.getFirstElement();
					addUserResouceData();
				}

				//
				// 아래 코드(managerTV.getControl().setFocus();)가 없으면, 오브젝트 탐색기의
				// event listener가 동작하지 않는다.
				// 이유는 글쎄 모르겠어.
				//
				treeViewer.getControl().setFocus();
			}
		});

		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {

				if (tableViewer.getSelection().isEmpty())
					return;

				StructuredSelection ss = (StructuredSelection) tableViewer.getSelection();
				ResourceManagerDAO dao = (ResourceManagerDAO) ss.getFirstElement();

				// db object를 클릭하면 쿼리 창이 뜨도록하고.
				if (PublicTadpoleDefine.RESOURCE_TYPE.ERD.toString().equals(dao.getResource_types())) {

					if (userDB != null && DBDefine.MONGODB_DEFAULT == DBDefine.getDBDefine(userDB)) {
						// MongoDBERDViewAction ea = new MongoDBERDViewAction();
						// ea.run(dao);
					} else {
						// RDBERDViewAction ea = new RDBERDViewAction();
						// ea.run(dao);
					}
					
					
					SqlMapClient sqlClient;
					try {
						sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
						List<String> result = sqlClient.queryForList("userDbResourceData", dao); //$NON-NLS-1$
						
						textQuery.setText("");
						for (String data:result){
							textQuery.append(data);
						}
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}else if (PublicTadpoleDefine.RESOURCE_TYPE.SQL.toString().equals(dao.getResource_types())) {
					SqlMapClient sqlClient;
					try {
						sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
						List<String> result = sqlClient.queryForList("userDbResourceData", dao); //$NON-NLS-1$
						
						textQuery.setText("");
						for (String data:result){
							textQuery.append(data);
						}
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	
				}

			}
		});

		try {
			List<String> groupNames = TadpoleSystem_UserDBQuery.getUserGroup(SessionManager.getGroupSeqs());
			for (String groupName : groupNames) {
				ManagerListDTO parent = new ManagerListDTO(groupName);
				treeList.add(parent);
			}

			List<UserDBDAO> userDBS = TadpoleSystem_UserDBQuery.getUserDB();
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
	public void addUserResouceData() {

		try {
			List<ResourceManagerDAO> listUserDBResources = TadpoleSystem_UserDBResource.userDbResource(userDB);

			tableViewer.setInput(listUserDBResources);
			tableViewer.refresh();

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
