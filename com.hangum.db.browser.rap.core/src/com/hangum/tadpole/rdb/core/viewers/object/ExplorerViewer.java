/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.ManagerListDTO;
import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.mongodb.core.editors.main.MongoDBEditorInput;
import com.hangum.tadpole.mongodb.core.editors.main.MongoDBTableEditor;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.GenerateSQLDeleteAction;
import com.hangum.tadpole.rdb.core.actions.object.GenerateSQLInsertAction;
import com.hangum.tadpole.rdb.core.actions.object.GenerateSQLSelectAction;
import com.hangum.tadpole.rdb.core.actions.object.GenerateSQLUpdateAction;
import com.hangum.tadpole.rdb.core.actions.object.GenerateSampleDataAction;
import com.hangum.tadpole.rdb.core.actions.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.ObjectDeleteAction;
import com.hangum.tadpole.rdb.core.actions.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.actions.object.mongodb.ObjectMongodbReIndexAction;
import com.hangum.tadpole.rdb.core.actions.object.mongodb.ObjectMongodbRenameAction;
import com.hangum.tadpole.rdb.core.editors.table.DBTableEditorInput;
import com.hangum.tadpole.rdb.core.editors.table.TableViewerEditPart;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.DefaultComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.TableComparator;
import com.hangum.tadpole.viewsupport.SelectionProviderMediator;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.Mongo;

/**
 * 선택된 db의 object를
 * 
 * @author hangumNote
 * 
 */
public class ExplorerViewer extends AbstraceExplorerViewer {

	public static String ID = "com.hangum.tadpole.rdb.core.view.object.explorer"; //$NON-NLS-1$
	private static Logger logger = Logger.getLogger(ExplorerViewer.class);

	// erd dnd
	private int DND_OPERATIONS = DND.DROP_COPY | DND.DROP_MOVE;

	private UserDBDAO userDB;
	private String selectTableName = ""; //$NON-NLS-1$

	private TabFolder tabFolderObject;
	private Text textSearch;

	// table
	private TableViewer tableListViewer;
	private List showTables;
	private ObjectComparator tableComparator;

	private TableViewer tableColumnViewer;
	private List showTableColumns;
	private TableFilter tableFilter;

	private ObjectCreatAction creatAction_Table;
	// private ObjectModifyAction modifyAction_Table;
	private ObjectDeleteAction deleteAction_Table;
	private ObjectRefreshAction refreshAction_Table;

	private GenerateSampleDataAction generateSampleData;

	private GenerateSQLSelectAction selectStmtAction;
	private GenerateSQLSelectAction insertStmtAction;
	private GenerateSQLSelectAction updateStmtAction;
	private GenerateSQLSelectAction deleteStmtAction;

	/** mongodb rename action */
	private ObjectMongodbRenameAction renameColAction;
	private ObjectMongodbReIndexAction reIndexColAction;

	// view
	private TableViewer viewListViewer;
	private ObjectComparator viewComparator;
	private List showViews;
	private TableViewer viewColumnViewer;
	private List showViewColumns;
	private RDBViewFilter viewFilter;

	private ObjectCreatAction creatAction_View;
	// private ObjectModifyAction modifyAction_Table;
	private ObjectDeleteAction deleteAction_View;
	private ObjectRefreshAction refreshAction_View;

	// index
	private TableViewer indexesListViewer;
	private ObjectComparator indexComparator;
	private List showIndex;
	private IndexesViewFilter indexFilter;

	private ObjectCreatAction creatAction_Index;
	// private ObjectModifyAction modifyAction_Index;
	private ObjectDeleteAction deleteAction_Index;
	private ObjectRefreshAction refreshAction_Index;

	// Procedure
	private TableViewer procedureListViewer;
	private ObjectComparator procedureComparator;
	private List showProcedure;
	private ProcedureFunctionViewFilter procedureFilter;

	private ObjectCreatAction creatAction_Procedure;
	// private ObjectModifyAction modifyAction_Procedure;
	private ObjectDeleteAction deleteAction_Procedure;
	private ObjectRefreshAction refreshAction_Procedure;

	// Function
	private TableViewer functionListViewer;
	private ObjectComparator fuctionComparator;
	private List showFunction;
	private ProcedureFunctionViewFilter functionFilter;

	private ObjectCreatAction creatAction_Function;
	// private ObjectModifyAction modifyAction_Function;
	private ObjectDeleteAction deleteAction_Function;
	private ObjectRefreshAction refreshAction_Function;

	// Trigger
	private TableViewer triggerListViewer;
	private ObjectComparator triggerComparator;
	private List showTrigger;
	private TriggerViewFilter triggerFilter;

	private ObjectCreatAction creatAction_Trigger;
	// private ObjectModifyAction modifyAction_Trigger;
	private ObjectDeleteAction deleteAction_Trigger;
	private ObjectRefreshAction refreshAction_Trigger;

	public ExplorerViewer() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.marginWidth = 1;
		gl_parent.verticalSpacing = 0;
		gl_parent.horizontalSpacing = 0;
		gl_parent.marginHeight = 0;
		parent.setLayout(gl_parent);

		Composite compositeSearch = new Composite(parent, SWT.NONE);
		compositeSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeSearch = new GridLayout(2, false);
		gl_compositeSearch.horizontalSpacing = 2;
		gl_compositeSearch.verticalSpacing = 2;
		gl_compositeSearch.marginHeight = 2;
		gl_compositeSearch.marginWidth = 2;
		compositeSearch.setLayout(gl_compositeSearch);

		Label lblNewLabel = new Label(compositeSearch, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText(Messages.ExplorerViewer_0);

		// filter를 설정합니다.
		textSearch = new Text(compositeSearch, SWT.BORDER);
		textSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				TabItem ti = tabFolderObject.getItem(tabFolderObject.getSelectionIndex());
				if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.TABLES.toString())) {
					tableFilter.setSearchText(textSearch.getText());
					tableListViewer.refresh();
				} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.VIEWS.toString())) {
					viewFilter.setSearchText(textSearch.getText());
					viewListViewer.refresh();
				} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.INDEXES.toString())) {
					indexFilter.setSearchText(textSearch.getText());
					indexesListViewer.refresh();
				} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.PROCEDURES.toString())) {
					procedureFilter.setSearchText(textSearch.getText());
					procedureListViewer.refresh();
				} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.FUNCTIONS.toString())) {
					functionFilter.setSearchText(textSearch.getText());
					functionListViewer.refresh();
				} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.TRIGGERS.toString())) {
					triggerFilter.setSearchText(textSearch.getText());
					triggerListViewer.refresh();
				}
			}
		});
		textSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Composite compositeBody = new Composite(parent, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);

		tabFolderObject = new TabFolder(compositeBody, SWT.NONE);
		tabFolderObject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				if (userDB == null) return;

				TabItem ti = (TabItem) evt.item;
				refershSelectTable(ti);
			}
		});
		tabFolderObject.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		createTable();

		createView();

		createIndexes();

		createProcedure();

		createFunction();

		createTrigger();

		// view의 set selection provider 설정
		StructuredViewer[] viewers = new StructuredViewer[] { tableListViewer, viewListViewer, indexesListViewer, procedureListViewer, functionListViewer, triggerListViewer };
		SelectionProviderMediator mediator = new SelectionProviderMediator(viewers, null);
		getViewSite().setSelectionProvider(mediator);

		// TabItem tbtmDatabases = new TabItem(tabFolder, SWT.NONE);
		// tbtmDatabases.setText("DataBases");
		//
		// TabItem tbtmUsers = new TabItem(tabFolder, SWT.NONE);
		// tbtmUsers.setText("Users");
//
//		getViewSite().getActionBars().getStatusLineManager().setMessage(""); //$NON-NLS-1$

		// 왼쪽 트리에서 데이터 받았는지.
		getSite().getPage().addSelectionListener(ManagerViewer.ID, managementViewerListener);

		// erd table에 선택되었을때..
		PlatformUI.getPreferenceStore().addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {

				if (event.getProperty() == Define.SELECT_ERD_TABLE) {
					String tableName = event.getNewValue().toString();
					if (tabFolderObject.getSelectionIndex() != 0)
						tabFolderObject.setSelection(0);

					Table table = tableListViewer.getTable();
					for (int i = 0; i < table.getItemCount(); i++) {
						if (tableName.equals(table.getItem(i).getText(0))) {
							tableListViewer.setSelection(new StructuredSelection(tableListViewer.getElementAt(i)), true);
						}

					}
				} // end if(event.getProperty()

			} //
		}); // end property change
	}

	/**
	 * 현재 선택된 tab을 리프레쉬합니다.
	 * 
	 * @param ti
	 */
	private void refershSelectTable(TabItem ti) {
		if(userDB == null) return;
		
		if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.VIEWS.toString())) {
			refreshView();
		} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.INDEXES.toString())) {
			refreshIndexes();
		} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.PROCEDURES.toString())) {
			refreshProcedure();
		} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.FUNCTIONS.toString())) {
			refreshFunction();
		} else if (ti.getText().equalsIgnoreCase(Define.DB_ACTION.TRIGGERS.toString())) {
			refreshTrigger();
		}
	}

	/**
	 * Trigger 정의
	 */
	private void createTrigger() {
		TabItem tbtmTriggers = new TabItem(tabFolderObject, SWT.NONE);
		tbtmTriggers.setText("Triggers"); //$NON-NLS-1$

		Composite compositeIndexes = new Composite(tabFolderObject, SWT.NONE);
		tbtmTriggers.setControl(compositeIndexes);
		GridLayout gl_compositeIndexes = new GridLayout(1, false);
		gl_compositeIndexes.verticalSpacing = 2;
		gl_compositeIndexes.horizontalSpacing = 2;
		gl_compositeIndexes.marginHeight = 2;
		gl_compositeIndexes.marginWidth = 2;
		compositeIndexes.setLayout(gl_compositeIndexes);

		SashForm sashForm = new SashForm(compositeIndexes, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// procedure table viewer
		triggerListViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		triggerListViewer.setUseHashlookup(true);
		Table tableTableList = triggerListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		triggerComparator = new ObjectComparator();
		triggerListViewer.setSorter(triggerComparator);

		// Transfer[] transferTypes = new
		// Transfer[]{TextTransfer.getInstance()};
		// triggerListViewer.addDragSupport(DND_OPERATIONS, transferTypes , new
		// DragListener(triggerListViewer));

		createTriggerColumn(triggerListViewer, triggerComparator);

		triggerListViewer.setLabelProvider(new TriggerLabelProvicer());
		triggerListViewer.setContentProvider(new ArrayContentProvider());
		triggerListViewer.setInput(showTrigger);

		triggerFilter = new TriggerViewFilter();
		triggerListViewer.addFilter(triggerFilter);

		// getViewSite().setSelectionProvider(triggerListViewer);

		sashForm.setWeights(new int[] { 1 });

		// creat action
		creatAction_Trigger = new ObjectCreatAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.TRIGGERS, "Trigger"); //$NON-NLS-1$
		// modifyAction_Trigger = new ObjectModifyAction(
		// getSite().getWorkbenchWindow(), Define.DB_ACTION.TRIGGERS,
		// "Trigger");
		deleteAction_Trigger = new ObjectDeleteAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.TRIGGERS, "Trigger"); //$NON-NLS-1$
		refreshAction_Trigger = new ObjectRefreshAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.TRIGGERS, "Trigger"); //$NON-NLS-1$

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(creatAction_Trigger);
				// manager.add(modifyAction_Trigger);
				manager.add(deleteAction_Trigger);
				manager.add(refreshAction_Trigger);
			}
		});

		Menu popupMenu = menuMgr.createContextMenu(triggerListViewer.getTable());
		triggerListViewer.getTable().setMenu(popupMenu);
		getSite().registerContextMenu(menuMgr, triggerListViewer);
	}

	/**
	 * Procedure 정의
	 */
	private void createFunction() {
		TabItem tbtmFunctions = new TabItem(tabFolderObject, SWT.NONE);
		tbtmFunctions.setText("Functions");; //$NON-NLS-1$

		Composite compositeIndexes = new Composite(tabFolderObject, SWT.NONE);
		tbtmFunctions.setControl(compositeIndexes);
		GridLayout gl_compositeIndexes = new GridLayout(1, false);
		gl_compositeIndexes.verticalSpacing = 2;
		gl_compositeIndexes.horizontalSpacing = 2;
		gl_compositeIndexes.marginHeight = 2;
		gl_compositeIndexes.marginWidth = 2;
		compositeIndexes.setLayout(gl_compositeIndexes);

		SashForm sashForm = new SashForm(compositeIndexes, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// procedure table viewer
		functionListViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		functionListViewer.setUseHashlookup(true);
		Table tableTableList = functionListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		fuctionComparator = new ObjectComparator();
		functionListViewer.setSorter(fuctionComparator);

		// Transfer[] transferTypes = new
		// Transfer[]{TextTransfer.getInstance()};
		// functionListViewer.addDragSupport(DND_OPERATIONS, transferTypes , new
		// DragListener(functionListViewer));

		createProcedureFunctionColumn(functionListViewer, fuctionComparator);

		functionListViewer.setLabelProvider(new ProcedureFunctionLabelProvicer());
		functionListViewer.setContentProvider(new ArrayContentProvider());
		functionListViewer.setInput(showFunction);

		functionFilter = new ProcedureFunctionViewFilter();
		functionListViewer.addFilter(functionFilter);

		// getViewSite().setSelectionProvider(functionListViewer);

		sashForm.setWeights(new int[] { 1 });

		// creat action
		creatAction_Function = new ObjectCreatAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.FUNCTIONS, "Function"); //$NON-NLS-1$
		// modifyAction_Function = new ObjectModifyAction(
		// getSite().getWorkbenchWindow(), Define.DB_ACTION.FUNCTION,
		// "Function");
		deleteAction_Function = new ObjectDeleteAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.FUNCTIONS, "Function"); //$NON-NLS-1$
		refreshAction_Function = new ObjectRefreshAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.FUNCTIONS, "Function"); //$NON-NLS-1$

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(creatAction_Function);
				// manager.add(modifyAction_Function);
				manager.add(deleteAction_Function);
				manager.add(refreshAction_Function);
			}
		});

		Menu popupMenu = menuMgr.createContextMenu(functionListViewer.getTable());
		functionListViewer.getTable().setMenu(popupMenu);
		getSite().registerContextMenu(menuMgr, functionListViewer);
	}

	/**
	 * Procedure 정의
	 */
	private void createProcedure() {
		TabItem tbtmProcedures = new TabItem(tabFolderObject, SWT.NONE);
		tbtmProcedures.setText("Procedures"); //$NON-NLS-1$

		Composite compositeIndexes = new Composite(tabFolderObject, SWT.NONE);
		tbtmProcedures.setControl(compositeIndexes);
		GridLayout gl_compositeIndexes = new GridLayout(1, false);
		gl_compositeIndexes.verticalSpacing = 2;
		gl_compositeIndexes.horizontalSpacing = 2;
		gl_compositeIndexes.marginHeight = 2;
		gl_compositeIndexes.marginWidth = 2;
		compositeIndexes.setLayout(gl_compositeIndexes);

		SashForm sashForm = new SashForm(compositeIndexes, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// procedure table viewer
		procedureListViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		procedureListViewer.setUseHashlookup(true);
		Table tableTableList = procedureListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		procedureComparator = new ObjectComparator();
		procedureListViewer.setSorter(procedureComparator);

		// Transfer[] transferTypes = new
		// Transfer[]{TextTransfer.getInstance()};
		// procedureListViewer.addDragSupport(DND_OPERATIONS, transferTypes ,
		// new DragListener(procedureListViewer));

		createProcedureFunctionColumn(procedureListViewer, procedureComparator);

		procedureListViewer.setLabelProvider(new ProcedureFunctionLabelProvicer());
		procedureListViewer.setContentProvider(new ArrayContentProvider());
		procedureListViewer.setInput(showProcedure);

		procedureFilter = new ProcedureFunctionViewFilter();
		procedureListViewer.addFilter(procedureFilter);

		// getViewSite().setSelectionProvider(procedureListViewer);

		sashForm.setWeights(new int[] { 1 });

		// creat action
		creatAction_Procedure = new ObjectCreatAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.PROCEDURES, "Procedure"); //$NON-NLS-1$
		// modifyAction_Procedure = new ObjectModifyAction(
		// getSite().getWorkbenchWindow(), Define.DB_ACTION.PROCEDURES,
		// "Procedure");
		deleteAction_Procedure = new ObjectDeleteAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.PROCEDURES, "Procedure"); //$NON-NLS-1$
		refreshAction_Procedure = new ObjectRefreshAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.PROCEDURES, "Procedure"); //$NON-NLS-1$

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(creatAction_Procedure);
				// manager.add(modifyAction_Procedure);
				manager.add(deleteAction_Procedure);
				manager.add(refreshAction_Procedure);
			}
		});

		Menu popupMenu = menuMgr.createContextMenu(procedureListViewer.getTable());
		procedureListViewer.getTable().setMenu(popupMenu);
		getSite().registerContextMenu(menuMgr, procedureListViewer);
	}

	/**
	 * indexes 정의
	 */
	private void createIndexes() {
		TabItem tbtmIndex = new TabItem(tabFolderObject, SWT.NONE);
		tbtmIndex.setText("Indexes"); //$NON-NLS-1$

		Composite compositeIndexes = new Composite(tabFolderObject, SWT.NONE);
		tbtmIndex.setControl(compositeIndexes);
		GridLayout gl_compositeIndexes = new GridLayout(1, false);
		gl_compositeIndexes.verticalSpacing = 2;
		gl_compositeIndexes.horizontalSpacing = 2;
		gl_compositeIndexes.marginHeight = 2;
		gl_compositeIndexes.marginWidth = 2;
		compositeIndexes.setLayout(gl_compositeIndexes);

		SashForm sashForm = new SashForm(compositeIndexes, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// index table viewer
		indexesListViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		indexesListViewer.setUseHashlookup(true);
		Table tableTableList = indexesListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		// Transfer[] transferTypes = new
		// Transfer[]{TextTransfer.getInstance()};
		// indexesListViewer.addDragSupport(DND_OPERATIONS, transferTypes , new
		// DragListener(indexesListViewer));

		indexComparator = new DefaultComparator();
		indexesListViewer.setSorter(indexComparator);

		createIndexesColumn(indexesListViewer, indexComparator);

		indexesListViewer.setLabelProvider(new IndexesLabelProvicer());
		indexesListViewer.setContentProvider(new ArrayContentProvider());
		indexesListViewer.setInput(showIndex);

		indexFilter = new IndexesViewFilter();
		indexesListViewer.addFilter(indexFilter);

		// getViewSite().setSelectionProvider(indexesListViewer);

		sashForm.setWeights(new int[] { 1 });

		// creat action
		creatAction_Index = new ObjectCreatAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.INDEXES, "Index"); //$NON-NLS-1$
		// modifyAction_Index = new ObjectModifyAction(
		// getSite().getWorkbenchWindow(), Define.DB_ACTION.INDEXES, "Index");
		deleteAction_Index = new ObjectDeleteAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.INDEXES, "Index"); //$NON-NLS-1$
		refreshAction_Index = new ObjectRefreshAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.INDEXES, "Index"); //$NON-NLS-1$

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(creatAction_Index);
				// manager.add(modifyAction_Index);
				manager.add(deleteAction_Index);
				manager.add(refreshAction_Index);
			}
		});

		Menu popupMenu = menuMgr.createContextMenu(indexesListViewer.getTable());
		indexesListViewer.getTable().setMenu(popupMenu);
		getSite().registerContextMenu(menuMgr, indexesListViewer);
	}

	/**
	 * view 정의
	 */
	private void createView() {
		TabItem tbtmViews = new TabItem(tabFolderObject, SWT.NONE);
		tbtmViews.setText("Views"); //$NON-NLS-1$

		Composite compositeTables = new Composite(tabFolderObject, SWT.NONE);
		tbtmViews.setControl(compositeTables);
		GridLayout gl_compositeTables = new GridLayout(1, false);
		gl_compositeTables.verticalSpacing = 2;
		gl_compositeTables.horizontalSpacing = 2;
		gl_compositeTables.marginHeight = 2;
		gl_compositeTables.marginWidth = 2;
		compositeTables.setLayout(gl_compositeTables);

		SashForm sashForm = new SashForm(compositeTables, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		viewListViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		viewListViewer.setUseHashlookup(true);
		viewListViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				// 테이블의 컬럼 목록을 출력합니다.
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					if (is != null) {
						if (is.getFirstElement() != null) {
							String strTBName = is.getFirstElement().toString();
							Map<String, String> param = new HashMap<String, String>();
							param.put("db", userDB.getDb());
							param.put("table", strTBName);

							SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
							showViewColumns = sqlClient.queryForList("tableColumnList", param); //$NON-NLS-1$
						} else
							showViewColumns = null;

						viewColumnViewer.setInput(showViewColumns);
						viewColumnViewer.refresh();
					}

				} catch (Exception e) {
					logger.error("get table list", e); //$NON-NLS-1$
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.ExplorerViewer_29, errStatus); //$NON-NLS-1$
				}
			}
		});
		Table tableTableList = viewListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		// sorter
		viewComparator = new ObjectComparator();
		viewListViewer.setSorter(viewComparator);

		// Transfer[] transferTypes = new
		// Transfer[]{TextTransfer.getInstance()};
		// viewListViewer.addDragSupport(DND_OPERATIONS, transferTypes , new
		// DragListener(viewListViewer));

		TableViewerColumn tableViewerColumn = new TableViewerColumn(viewListViewer, SWT.NONE);
		TableColumn tblclmnTableName = tableViewerColumn.getColumn();
		tblclmnTableName.setWidth(200);
		tblclmnTableName.setText("Name"); //$NON-NLS-1$
		tblclmnTableName.addSelectionListener(getSelectionAdapter(viewListViewer, viewComparator, tblclmnTableName, 0));
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return element.toString();
			}
		});
		viewListViewer.setContentProvider(new ArrayContentProvider());
		viewListViewer.setInput(showViews);

		// columns
		viewColumnViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		viewColumnViewer.setUseHashlookup(true);
		Table tableTableColumn = viewColumnViewer.getTable();
		tableTableColumn.setHeaderVisible(true);
		tableTableColumn.setLinesVisible(true);

		createViewColumne(viewColumnViewer);

		// transferTypes = new Transfer[]{TextTransfer.getInstance()};
		// viewColumnViewer.addDragSupport(DND_OPERATIONS, transferTypes , new
		// DragListener(viewColumnViewer));

		viewColumnViewer.setContentProvider(new ArrayContentProvider());
		viewColumnViewer.setLabelProvider(new TableColumnLabelprovider());
		viewColumnViewer.setInput(showViewColumns);

		sashForm.setWeights(new int[] { 1, 1 });

		viewFilter = new RDBViewFilter();
		viewListViewer.addFilter(viewFilter);

		creatAction_View = new ObjectCreatAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.VIEWS, "View"); //$NON-NLS-1$
		// modifyActionView = new ObjectModifyAction(
		// getSite().getWorkbenchWindow(), Define.DB_ACTION.VIEW, "View");
		deleteAction_View = new ObjectDeleteAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.VIEWS, "View"); //$NON-NLS-1$
		refreshAction_View = new ObjectRefreshAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.VIEWS, "View"); //$NON-NLS-1$

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(creatAction_View);
				// manager.add(modifyActionView);
				manager.add(deleteAction_View);
				manager.add(refreshAction_View);
			}
		});

		Menu popupMenu = menuMgr.createContextMenu(viewListViewer.getTable());
		viewListViewer.getTable().setMenu(popupMenu);
		getSite().registerContextMenu(menuMgr, viewListViewer);
	}

	/**
	 * Table 정의
	 */
	private void createTable() {
		TabItem tbtmTable = new TabItem(tabFolderObject, SWT.NONE);
		tbtmTable.setText("Tables"); //$NON-NLS-1$

		Composite compositeTables = new Composite(tabFolderObject, SWT.NONE);
		tbtmTable.setControl(compositeTables);
		GridLayout gl_compositeTables = new GridLayout(1, false);
		gl_compositeTables.verticalSpacing = 2;
		gl_compositeTables.horizontalSpacing = 2;
		gl_compositeTables.marginHeight = 2;
		gl_compositeTables.marginWidth = 2;
		compositeTables.setLayout(gl_compositeTables);
		compositeTables.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		SashForm sashForm = new SashForm(compositeTables, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		tableListViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		tableListViewer.setUseHashlookup(true);
		tableListViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection is = (IStructuredSelection) event.getSelection();

				if (null != is) {
					TableDAO tableDAO = (TableDAO) is.getFirstElement();

					// is rdb
					if (DBDefine.getDBDefine(userDB.getTypes()) != DBDefine.MONGODB_DEFAULT) {

						DBTableEditorInput mei = new DBTableEditorInput(tableDAO.getName(), userDB, showTableColumns);
						IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						try {
							page.openEditor(mei, TableViewerEditPart.ID);
						} catch (PartInitException e) {
							logger.error("Load the table data", e); //$NON-NLS-1$

							Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
							ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.ExplorerViewer_39, errStatus); //$NON-NLS-1$
						}

						// is mongo db
					} else if (DBDefine.getDBDefine(userDB.getTypes()) == DBDefine.MONGODB_DEFAULT) {

						MongoDBEditorInput input = new MongoDBEditorInput(tableDAO.getName(), userDB, showTableColumns);
						IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						try {
							page.openEditor(input, MongoDBTableEditor.ID);
						} catch (PartInitException e) {
							logger.error("Load the table data", e); //$NON-NLS-1$

							Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
							ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.ExplorerViewer_39, errStatus); //$NON-NLS-1$
						}
					}

				} // if(null
			}
		});
		// tableListViewer.addSelectionChangedListener(new
		// ISelectionChangedListener() {
		tableListViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				// 테이블의 컬럼 목록을 출력합니다.
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					Object tableDAO = is.getFirstElement();

					if (tableDAO != null) {
						TableDAO table = (TableDAO) tableDAO;

						if (selectTableName.equals(table.getName()))
							return;
						selectTableName = table.getName();

						if (DBDefine.getDBDefine(userDB.getTypes()) != DBDefine.MONGODB_DEFAULT) {

							SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
							Map<String, String> mapParam = new HashMap<String, String>();
							mapParam.put("db", userDB.getDb());
							mapParam.put("table", table.getName());

							showTableColumns = sqlClient.queryForList("tableColumnList", mapParam); //$NON-NLS-1$

							// mongo db
						} else if (DBDefine.getDBDefine(userDB.getTypes()) == DBDefine.MONGODB_DEFAULT) {

							showTableColumns = MongoDBQuery.collectionColumn(userDB, selectTableName);
						}

					} else
						showTableColumns = null;

					tableColumnViewer.setInput(showTableColumns);
					tableColumnViewer.refresh();

				} catch (Exception e) {
					logger.error("get table column", e); //$NON-NLS-1$

					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", e.getMessage(), errStatus); //$NON-NLS-1$
				}
			}
		});

		Table tableTableList = tableListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		// sorter
		tableComparator = new TableComparator();
		tableListViewer.setSorter(tableComparator);

		TableViewerColumn tvColName = new TableViewerColumn(tableListViewer, SWT.NONE);
		TableColumn tbName = tvColName.getColumn();
		tbName.setWidth(150);
		tbName.setText("Name"); //$NON-NLS-1$
		tbName.addSelectionListener(getSelectionAdapter(tableListViewer, tableComparator, tbName, 0));
		tvColName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				TableDAO table = (TableDAO) element;
				return table.getName();
			}
		});
		tvColName.setEditingSupport(new TableCommentEditorSupport(tableListViewer, this, 0));

		TableViewerColumn tvColComment = new TableViewerColumn(tableListViewer, SWT.NONE);
		TableColumn tbComment = tvColComment.getColumn();
		tbComment.setWidth(400);
		tbComment.setText("Comment"); //$NON-NLS-1$
		tbComment.addSelectionListener(getSelectionAdapter(tableListViewer, tableComparator, tbComment, 1));
		tvColComment.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				TableDAO table = (TableDAO) element;
				return table.getComment();
			}
		});
		tvColComment.setEditingSupport(new TableCommentEditorSupport(tableListViewer, this, 1));

		tableListViewer.setContentProvider(new ArrayContentProvider());
		tableListViewer.setInput(showTables);

		// dnd 기능 추가
		Transfer[] transferTypes = new Transfer[]{TextTransfer.getInstance()};
		tableListViewer.addDragSupport(DND_OPERATIONS, transferTypes , new DragListener(tableListViewer));

		// filter
		tableFilter = new TableFilter();
		tableListViewer.addFilter(tableFilter);

		// columns
		tableColumnViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		tableColumnViewer.setUseHashlookup(true);
		Table tableTableColumn = tableColumnViewer.getTable();
		tableTableColumn.setHeaderVisible(true);
		tableTableColumn.setLinesVisible(true);

		createTableColumne(tableColumnViewer);

		// dnd 기능 추가
		// transferTypes = new Transfer[]{TextTransfer.getInstance()};
		// tableColumnViewer.addDragSupport(DND_OPERATIONS, transferTypes , new
		// DragListener(tableColumnViewer));

		tableColumnViewer.setContentProvider(new ArrayContentProvider());
		tableColumnViewer.setLabelProvider(new TableColumnLabelprovider());
		tableColumnViewer.setInput(showTableColumns);

		sashForm.setWeights(new int[] { 1, 1 });

		// action 설정
		creatAction_Table = new ObjectCreatAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.TABLES, "Table"); //$NON-NLS-1$
		// modifyAction = new ObjectModifyAction(
		// getSite().getWorkbenchWindow(), Define.DB_ACTION.TABLE, "Table");
		deleteAction_Table = new ObjectDeleteAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.TABLES, "Table"); //$NON-NLS-1$
		refreshAction_Table = new ObjectRefreshAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.TABLES, "Table"); //$NON-NLS-1$

		// generation sample data
		generateSampleData = new GenerateSampleDataAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.TABLES, "Table"); //$NON-NLS-1$

		selectStmtAction = new GenerateSQLSelectAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.TABLES, "Select"); //$NON-NLS-1$
		insertStmtAction = new GenerateSQLInsertAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.TABLES, "Insert"); //$NON-NLS-1$
		updateStmtAction = new GenerateSQLUpdateAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.TABLES, "Update"); //$NON-NLS-1$
		deleteStmtAction = new GenerateSQLDeleteAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.TABLES, "Delete"); //$NON-NLS-1$

		// mongodb 용 action설정
		renameColAction = new ObjectMongodbRenameAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.TABLES, "Rename Collection");
		reIndexColAction = new ObjectMongodbReIndexAction(getSite().getWorkbenchWindow(), Define.DB_ACTION.TABLES, "ReIndex Collection");

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (userDB != null) {
					if (DBDefine.getDBDefine(userDB.getTypes()) != DBDefine.MONGODB_DEFAULT) {
						manager.add(creatAction_Table);
						// manager.add(modifyAction);
						manager.add(deleteAction_Table);
						manager.add(refreshAction_Table);

						// 현재는 oracle db만 데이터 수정 모드..
						if (DBDefine.getDBDefine(userDB.getTypes()) == DBDefine.ORACLE_DEFAULT) {
							manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
							manager.add(generateSampleData);
						}

						manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

						manager.add(selectStmtAction);
						manager.add(insertStmtAction);
						manager.add(updateStmtAction);
						manager.add(deleteStmtAction);
						// is mongodb
					} else {
						manager.add(creatAction_Table);
						manager.add(deleteAction_Table);
						manager.add(refreshAction_Table);

						manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
						manager.add(insertStmtAction);
						manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
						manager.add(renameColAction);
						manager.add(reIndexColAction);
					}
				}
			}
		});

		Menu popupMenu = menuMgr.createContextMenu(tableListViewer.getTable());
		tableListViewer.getTable().setMenu(popupMenu);
		getSite().registerContextMenu(menuMgr, tableListViewer);
	}

	/**
	 * management의 tree가 선택되었을때
	 */
	private ISelectionListener managementViewerListener = new ISelectionListener() {
		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {

			if (selection instanceof IStructuredSelection) {

				IStructuredSelection is = (IStructuredSelection) selection;
				initObjectHead(is.getFirstElement());

			} // end selection
		} // end selectionchange
	};

	/**
	 * management의 head가 선택되었을때
	 * 
	 * @param element
	 */
	public void initObjectHead(Object element) {
		initSearch();

		if (element instanceof UserDBDAO || element instanceof UserDBResourceDAO) {
			UserDBDAO selectUserDb = null;
			if (element instanceof UserDBDAO) {
				selectUserDb = (UserDBDAO) element;
			} else if (element instanceof UserDBResourceDAO) {
				selectUserDb = ((UserDBResourceDAO) element).getParent();
			}

			// 기존 디비가 중복 선택되었으면 리프레쉬 하지 않는다.
			if (userDB != null) {
				if (userDB.getSeq() == selectUserDb.getSeq()) return;
			}
			userDB = selectUserDb;

			getViewSite().getActionBars().getStatusLineManager().setMessage(userDB.getDb());
			tabFolderObject.setSelection(0);

			refreshTable("DB"); //$NON-NLS-1$

			initObjectDetail(DBDefine.getDBDefine(userDB.getTypes()));

		} else if (element instanceof ManagerListDTO) {
			ManagerListDTO managerList = (ManagerListDTO) element;

			userDB = null;

			// table을 닫는다.
			if (showTables != null)
				showTables.clear();
			tableListViewer.setInput(showTables);
			tableListViewer.refresh();

			initObjectDetail(managerList.getName());//DbType());
			
		// Connection Manager의 모든 db가 삭제 되었을때 호출됨 
		} else {
			tabFolderObject.setSelection(0);
			userDB = null;

			// table을 닫는다.
			if (showTables != null) showTables.clear();
			tableListViewer.setInput(showTables);
			tableListViewer.refresh();
		}
	}
	
	/**
	 * init search
	 */
	private void initSearch() {
		textSearch.setText("");
		tableFilter.setSearchText("");
		viewFilter.setSearchText("");
		indexFilter.setSearchText("");
		procedureFilter.setSearchText("");
		functionFilter.setSearchText("");
		triggerFilter.setSearchText("");
	}

	/**
	 * 다른 디비가 선택되어 지면 초기화 되어야 할 object 목록
	 * 
	 * @param Connection Manager에서 선택된 object
	 */
	private void initObjectDetail(Object selObject) {//DBDefine dbDefie) {
		DBDefine dbDefine = null;
		if(selObject instanceof DBDefine) dbDefine = (DBDefine)selObject;
				
		// dbtype에 따른 object 뷰를 조절합니다.
		if (dbDefine == DBDefine.SQLite_DEFAULT) {
			// procedure, function 항목을 닫는다.
			for (TabItem tabItem : tabFolderObject.getItems()) {
				if (!tabItem.isDisposed()) {
					if ("Procedures".equals(tabItem.getText()))tabItem.dispose(); //$NON-NLS-1$
					else if ("Functions".equals(tabItem.getText()))tabItem.dispose(); //$NON-NLS-1$
				}
			}
		} else if (dbDefine == DBDefine.MONGODB_DEFAULT) {
			// table 항목 이외의 모든 항목을 닫는다.
			for (TabItem tabItem : tabFolderObject.getItems()) {
				if (!tabItem.isDisposed()) {
					if (!"Tables".equals(tabItem.getText()))tabItem.dispose(); //$NON-NLS-1$
				}
			}

		} else {

			boolean isViews = false;
			boolean isIndexes = false;

			boolean isProcedure = false;
			boolean isFunction = false;

			boolean isTriggers = false;

			TabItem[] tItems = tabFolderObject.getItems();
			for (TabItem tabItem : tItems) {

				if ("Views".equals(tabItem.getText()))isViews = true; //$NON-NLS-1$
				if ("Indexes".equals(tabItem.getText()))isIndexes = true; //$NON-NLS-1$

				if ("Procedures".equals(tabItem.getText()))isProcedure = true; //$NON-NLS-1$
				if ("Functions".equals(tabItem.getText()))isFunction = true; //$NON-NLS-1$

				if ("Triggers".equals(tabItem.getText()))isTriggers = true; //$NON-NLS-1$
			}

			if (!isViews) 	createView();
			if (!isIndexes) createIndexes();

			if (!isProcedure) createProcedure();
			if (!isFunction)  createFunction();

			if (!isTriggers) createTrigger();
		}

		//
		// table column viewer
		// if(showTableColumns != null) showTableColumns.clear();
		// tableColumnViewer.setInput(showTableColumns);
		// tableColumnViewer.refresh();
		// viewer안에 table viewer가 여러개 있으니 먹지 않는다. 음음
		// https://github.com/hangum/TadpoleForDBTools/issues/9
		Table table = tableColumnViewer.getTable();
		table.removeAll();

		// table comment edit
		// if(isCommentEdit(getUserDB())) tvColCmt.setEditingSupport(new
		// TableColumnEditor(this, tableListViewer));

		// table column comment edit
		// if(isCommentEdit(userDB)) tableColumn.setEditingSupport(new
		// TableColumnEditor(explorerViewer, tv));

		creatAction_Table.setUserDB(getUserDB());
		// modifyAction.setUserDB(getUserDB());
		deleteAction_Table.setUserDB(getUserDB());
		refreshAction_Table.setUserDB(getUserDB());

		generateSampleData.setUserDB(getUserDB());

		selectStmtAction.setUserDB(getUserDB());
		insertStmtAction.setUserDB(getUserDB());
		updateStmtAction.setUserDB(getUserDB());
		deleteStmtAction.setUserDB(getUserDB());

		renameColAction.setUserDB(getUserDB());
		reIndexColAction.setUserDB(getUserDB());

		// viewer
		if (showViews != null) showViews.clear();
		viewListViewer.setInput(showViews);
		viewListViewer.refresh();

		if (showViewColumns != null) showViewColumns.clear();
		viewColumnViewer.setInput(showViewColumns);
		viewColumnViewer.refresh();

		creatAction_View.setUserDB(getUserDB());
		// modifyAction_View.setUserDB(getUserDB());
		deleteAction_View.setUserDB(getUserDB());
		refreshAction_View.setUserDB(getUserDB());

		// index
		if (showIndex != null) showIndex.clear();
		indexesListViewer.setInput(showIndex);
		indexesListViewer.refresh();

		creatAction_Index.setUserDB(getUserDB());
		// modifyAction_Index.setUserDB(getUserDB());
		deleteAction_Index.setUserDB(getUserDB());
		refreshAction_Index.setUserDB(getUserDB());

		// procedure
		if (showProcedure != null) showProcedure.clear();
		procedureListViewer.setInput(showProcedure);
		procedureListViewer.refresh();

		creatAction_Procedure.setUserDB(getUserDB());
		// modifyAction_Procedure.setUserDB(getUserDB());
		deleteAction_Procedure.setUserDB(getUserDB());
		refreshAction_Procedure.setUserDB(getUserDB());

		// function
		if (showFunction != null) showFunction.clear();
		functionListViewer.setInput(showFunction);
		functionListViewer.refresh();

		creatAction_Function.setUserDB(getUserDB());
		// modifyAction_Function.setUserDB(getUserDB());
		deleteAction_Function.setUserDB(getUserDB());
		refreshAction_Function.setUserDB(getUserDB());

		// trigger
		if (showFunction != null) showFunction.clear();
		triggerListViewer.setInput(showTrigger);
		triggerListViewer.refresh();

		creatAction_Trigger.setUserDB(getUserDB());
		// modifyAction_Trigger.setUserDB(getUserDB());
		deleteAction_Trigger.setUserDB(getUserDB());
		refreshAction_Trigger.setUserDB(getUserDB());
	}

	/**
	 * view 정보를 최신으로 리프레쉬합니다.
	 */
	public void refreshView() {
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showViews = sqlClient.queryForList("viewList", userDB.getDb()); //$NON-NLS-1$

			viewListViewer.setInput(showViews);
			viewListViewer.refresh();

		} catch (Exception e) {
			logger.error("view refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.ExplorerViewer_61, errStatus); //$NON-NLS-1$
		}
	}

	/**
	 * index 정보를 최신으로 갱신 합니다.
	 */
	public void refreshIndexes() {
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showIndex = sqlClient.queryForList("indexList", userDB.getDb()); //$NON-NLS-1$

			indexesListViewer.setInput(showIndex);
			indexesListViewer.refresh();

		} catch (Exception e) {
			logger.error("index refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.ExplorerViewer_1, errStatus); //$NON-NLS-1$
		}
	}

	/**
	 * procedure 정보를 최신으로 갱신 합니다.
	 */
	public void refreshProcedure() {
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showProcedure = sqlClient.queryForList("procedureList", userDB.getDb()); //$NON-NLS-1$

			procedureListViewer.setInput(showProcedure);
			procedureListViewer.refresh();

		} catch (Exception e) {
			logger.error("showProcedure refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.ExplorerViewer_71, errStatus); //$NON-NLS-1$
		}
	}

	/**
	 * trigger 정보를 최신으로 갱신 합니다.
	 */
	public void refreshTrigger() {
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showTrigger = sqlClient.queryForList("triggerList", userDB.getDb()); //$NON-NLS-1$

			triggerListViewer.setInput(showTrigger);
			triggerListViewer.refresh();

		} catch (Exception e) {
			logger.error("showTrigger refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.ExplorerViewer_76, errStatus); //$NON-NLS-1$
		}
	}

	/**
	 * function 정보를 최신으로 갱신 합니다.
	 */
	public void refreshFunction() {
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showFunction = sqlClient.queryForList("functionList", userDB.getDb()); //$NON-NLS-1$

			functionListViewer.setInput(showFunction);
			functionListViewer.refresh();

		} catch (Exception e) {
			logger.error("showFunction refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.ExplorerViewer_81, errStatus); //$NON-NLS-1$
		}
	}

	/**
	 * table 정보를 최신으로 리프레쉬합니다.
	 */
	public void refreshTable(final String source) {

		try {
			if (DBDefine.getDBDefine(userDB.getTypes()) != DBDefine.MONGODB_DEFAULT) {

				SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
				showTables = sqlClient.queryForList("tableList", userDB.getDb()); //$NON-NLS-1$

				// mongo db
			} else if (DBDefine.getDBDefine(userDB.getTypes()) == DBDefine.MONGODB_DEFAULT) {

				Mongo mongo = new Mongo(new DBAddress(userDB.getUrl()));
				DB mongoDB = mongo.getDB(userDB.getDb());

				if (showTables != null) showTables.clear();
				else showTables = new ArrayList<TableDAO>();

				for (String col : mongoDB.getCollectionNames()) {
					TableDAO dao = new TableDAO();
					dao.setName(col);

					showTables.add(dao);
				}
			}

			tableListViewer.setInput(showTables);
			tableListViewer.refresh();

		} catch (Exception e) {
			logger.error(source + " Table Referesh", e);

			if (showTables != null) showTables.clear();
			tableListViewer.setInput(showTables);
			tableListViewer.refresh();

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			if ("DB".equals(source)) ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.ExplorerViewer_4, errStatus); //$NON-NLS-1$ //$NON-NLS-2$
			else  ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.ExplorerViewer_86, errStatus); //$NON-NLS-1$
		}

	}

	@Override
	public void setFocus() {
	}

	public UserDBDAO getUserDB() {
		return userDB;
	}

	/**
	 * 현재 오픈된페이지를 리프레쉬한다.
	 */
	public static enum CHANGE_TYPE {
		DEL, INS
	};

	public void refreshCurrentTab(UserDBDAO chgUserDB, CHANGE_TYPE changeType, String changeTbName) {

		if (this.userDB.getSeq() != chgUserDB.getSeq())
			return;
		if (tabFolderObject.getSelectionIndex() != 0)
			return;

		TableDAO dao = new TableDAO();
		dao.setName(changeTbName);
		if (CHANGE_TYPE.DEL == changeType) {
			showTables.remove(dao);
		} else if (CHANGE_TYPE.INS == changeType) {
			showTables.add(dao);
		}

		tableListViewer.setInput(showTables);
		tableListViewer.refresh(changeTbName, false, true);

	}
}
