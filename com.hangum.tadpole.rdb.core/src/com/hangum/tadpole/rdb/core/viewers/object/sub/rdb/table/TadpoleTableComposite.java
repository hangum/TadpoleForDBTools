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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table;

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
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.define.Define;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
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
import com.hangum.tadpole.rdb.core.editors.table.DBTableEditorInput;
import com.hangum.tadpole.rdb.core.editors.table.TableViewerEditPart;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer.CHANGE_TYPE;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.TableComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.system.permission.PermissionChecks;
import com.hangum.tadpole.util.tables.AutoResizeTableLayout;
import com.hangum.tadpole.util.tables.TableUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * RDB table composite
 * @author hangum
 *
 */
public class TadpoleTableComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleTableComposite.class);
	
	/** select table name */
	private String selectTableName = ""; //$NON-NLS-1$

	// table info
	private TableViewer tableListViewer;
	private List showTables;
	private ObjectComparator tableComparator;
	private TableFilter tableFilter;
	
	// column info
	private TableViewer tableColumnViewer;
	private List showTableColumns;
	
	private ObjectCreatAction creatAction_Table;
	private ObjectDeleteAction deleteAction_Table;
	private ObjectRefreshAction refreshAction_Table;

	private GenerateSampleDataAction generateSampleData;

	private GenerateSQLSelectAction selectStmtAction;
	private GenerateSQLSelectAction insertStmtAction;
	private GenerateSQLSelectAction updateStmtAction;
	private GenerateSQLSelectAction deleteStmtAction;
	
	/**
	 * Create the composite.
	 * 
	 * @param partSite
	 * @param parent
	 * @param userDB
	 */
	public TadpoleTableComposite(IWorkbenchPartSite partSite, final CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(partSite, tabFolderObject, userDB);
		
		createWidget(tabFolderObject);
	}
	
	private void createWidget(final CTabFolder tabFolderObject) {		
		CTabItem tbtmTable = new CTabItem(tabFolderObject, SWT.NONE);
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
		tableListViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection is = (IStructuredSelection) event.getSelection();

				if(PermissionChecks.isShow(strUserType, userDB)) {
					if (null != is) {
						TableDAO tableDAO = (TableDAO) is.getFirstElement();
	
						DBTableEditorInput mei = new DBTableEditorInput(tableDAO.getName(), userDB, showTableColumns);
						IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						try {
							page.openEditor(mei, TableViewerEditPart.ID);
						} catch (PartInitException e) {
							logger.error("Load the table data", e); //$NON-NLS-1$
	
							Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
							ExceptionDetailsErrorDialog.openError(tabFolderObject.getShell(), "Error", Messages.ExplorerViewer_39, errStatus); //$NON-NLS-1$
						}
					}
				}
			}
		});
		tableListViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				// 테이블의 컬럼 목록을 출력합니다.
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					Object tableDAO = is.getFirstElement();

					if (tableDAO != null) {
						TableDAO table = (TableDAO) tableDAO;

						if (selectTableName.equals(table.getName())) return;
						selectTableName = table.getName();

						SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
						Map<String, String> mapParam = new HashMap<String, String>();
						mapParam.put("db", userDB.getDb());
						mapParam.put("table", table.getName());

						showTableColumns = sqlClient.queryForList("tableColumnList", mapParam); //$NON-NLS-1$

					} else
						showTableColumns = null;

					tableColumnViewer.setInput(showTableColumns);
					tableColumnViewer.refresh();
					TableUtil.packTable(tableColumnViewer.getTable());

				} catch (Exception e) {
					logger.error("get table column", e); //$NON-NLS-1$

					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(tabFolderObject.getShell(), "Error", e.getMessage(), errStatus); //$NON-NLS-1$
				}
			}
		});

		Table tableTableList = tableListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);
		
		// sorter
		tableComparator = new TableComparator();
		tableListViewer.setSorter(tableComparator);
		
		// auto table layout
		AutoResizeTableLayout layoutColumnLayout = new AutoResizeTableLayout(tableListViewer.getTable());

		TableViewerColumn tvColName = new TableViewerColumn(tableListViewer, SWT.NONE);
		TableColumn tbName = tvColName.getColumn();
		tbName.setWidth(170);
		tbName.setText("Name"); //$NON-NLS-1$
		tbName.addSelectionListener(getSelectionAdapter(tableListViewer, tableComparator, tbName, 0));
		tvColName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				TableDAO table = (TableDAO) element;
				return table.getName();
			}
		});
		tvColName.setEditingSupport(new TableCommentEditorSupport(tableListViewer, userDB, 0));
		layoutColumnLayout.addColumnData(new ColumnWeightData(170));
		
		// table column tooltip
		ColumnViewerToolTipSupport.enableFor(tableListViewer);
		CellLabelProvider labelProvider = new CellLabelProvider() {

			public String getToolTipText(Object element) {
				TableDAO table = (TableDAO) element;
				return table.getComment();
			}

			public Point getToolTipShift(Object object) {
				return new Point(5, 5);
			}

			public int getToolTipDisplayDelayTime(Object object) {
				return 5000;
			}

			public int getToolTipTimeDisplayed(Object object) {
				return 1000;
			}

			public void update(ViewerCell cell) {
				TableDAO table = (TableDAO)cell.getElement();
				cell.setText(table.getComment());
			}
		};

		TableViewerColumn tvColComment = new TableViewerColumn(tableListViewer, SWT.NONE);
		TableColumn tbComment = tvColComment.getColumn();
		tbComment.setWidth(200);
		tbComment.setText("Comment"); //$NON-NLS-1$
		tbComment.addSelectionListener(getSelectionAdapter(tableListViewer, tableComparator, tbComment, 1));
//		tvColComment.setLabelProvider(new ColumnLabelProvider() {
//			@Override
//			public String getText(Object element) {
//				TableDAO table = (TableDAO) element;
//				return table.getComment();
//			}
//		});
		tvColComment.setLabelProvider(labelProvider);
		tvColComment.setEditingSupport(new TableCommentEditorSupport(tableListViewer, userDB, 1));
		layoutColumnLayout.addColumnData(new ColumnWeightData(200));
		
		tableListViewer.getTable().setLayout(layoutColumnLayout);

		tableListViewer.setContentProvider(new ArrayContentProvider());
		tableListViewer.setInput(showTables);

		// dnd 기능 추가
		Transfer[] transferTypes = new Transfer[]{TextTransfer.getInstance()};
		tableListViewer.addDragSupport(DND_OPERATIONS, transferTypes , new DragListener(userDB, tableListViewer));

		// filter
		tableFilter = new TableFilter();
		tableListViewer.addFilter(tableFilter);

		// columns
		tableColumnViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTableColumn = tableColumnViewer.getTable();
		tableTableColumn.setHeaderVisible(true);
		tableTableColumn.setLinesVisible(true);

		createTableColumne(tableColumnViewer);

		tableColumnViewer.setContentProvider(new ArrayContentProvider());
		tableColumnViewer.setLabelProvider(new TableColumnLabelprovider());
		createMenu();
		
		sashForm.setWeights(new int[] { 1, 1 });
	}
	
	/**
	 * create menu
	 */
	private void createMenu() {
		creatAction_Table = new ObjectCreatAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), Define.DB_ACTION.TABLES, "Table"); //$NON-NLS-1$
		deleteAction_Table = new ObjectDeleteAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), Define.DB_ACTION.TABLES, "Table"); //$NON-NLS-1$
		refreshAction_Table = new ObjectRefreshAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), Define.DB_ACTION.TABLES, "Table"); //$NON-NLS-1$

		// generation sample data
		generateSampleData = new GenerateSampleDataAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), Define.DB_ACTION.TABLES, "Table"); //$NON-NLS-1$

		selectStmtAction = new GenerateSQLSelectAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), Define.DB_ACTION.TABLES, "Select"); //$NON-NLS-1$
		insertStmtAction = new GenerateSQLInsertAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), Define.DB_ACTION.TABLES, "Insert"); //$NON-NLS-1$
		updateStmtAction = new GenerateSQLUpdateAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), Define.DB_ACTION.TABLES, "Update"); //$NON-NLS-1$
		deleteStmtAction = new GenerateSQLDeleteAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), Define.DB_ACTION.TABLES, "Delete"); //$NON-NLS-1$

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (userDB != null) {
					if(PermissionChecks.isShow(strUserType, userDB)) {
						manager.add(creatAction_Table);
						manager.add(deleteAction_Table);
					}					
					manager.add(refreshAction_Table);

					// 현재는 oracle db만 데이터 수정 모드..
					if (DBDefine.getDBDefine(userDB.getTypes()) == DBDefine.ORACLE_DEFAULT) {
						manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
						manager.add(generateSampleData);
					}

					manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
					manager.add(selectStmtAction);
					
					if(PermissionChecks.isShow(strUserType, userDB)) {
						manager.add(insertStmtAction);
						manager.add(updateStmtAction);
						manager.add(deleteStmtAction);
					}
				}
			}
		});

		org.eclipse.swt.widgets.Menu popupMenu = menuMgr.createContextMenu(tableListViewer.getTable());
		tableListViewer.getTable().setMenu(popupMenu);
		getSite().registerContextMenu(menuMgr, tableListViewer);
	}

	/**
	 * table 정보를 최신으로 리프레쉬합니다.
	 */
	public void refreshTable(final UserDBDAO selectUserDb, boolean boolRefresh) {
		if(!boolRefresh) if(selectUserDb == null) return;

		this.userDB = selectUserDb;

		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showTables = sqlClient.queryForList("tableList", userDB.getDb()); //$NON-NLS-1$
			
			tableListViewer.setInput(showTables);
			tableListViewer.refresh();

		} catch (Exception e) {
			logger.error("Table Referesh", e);

			if (showTables != null) showTables.clear();
			tableListViewer.setInput(showTables);
			tableListViewer.refresh();

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.ExplorerViewer_86, errStatus); //$NON-NLS-1$
		}
	}
	
	/**
	 * initialize action
	 */
	public void initAction() {
		creatAction_Table.setUserDB(getUserDB());
		deleteAction_Table.setUserDB(getUserDB());
		refreshAction_Table.setUserDB(getUserDB());

		generateSampleData.setUserDB(getUserDB());

		selectStmtAction.setUserDB(getUserDB());
		insertStmtAction.setUserDB(getUserDB());
		updateStmtAction.setUserDB(getUserDB());
		deleteStmtAction.setUserDB(getUserDB());
	}
	
	/**
	 * initialize filter text
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		tableFilter.setSearchText(textSearch);
		tableListViewer.refresh();
	}
	
	/**
	 * select table
	 * @param tableName
	 */
	public void selectTable(String tableName) {
		Table table = tableListViewer.getTable();
		for (int i = 0; i < table.getItemCount(); i++) {
			if (tableName.equals(table.getItem(i).getText(0))) {
				tableListViewer.setSelection(new StructuredSelection(tableListViewer.getElementAt(i)), true);
			}
		}
	}
	
	/**
	 * get tableviewer
	 * @return
	 */
	public TableViewer getTableListViewer() {
		return tableListViewer;
	}

	/**
	 * refresh tableviewer
	 * @param changeType
	 * @param changeTbName
	 */
	public void refreshTable(CHANGE_TYPE changeType, String changeTbName) {
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

	/**
	 * table search 
	 * 
	 * @param string
	 */
	public void setSearchText(String searchText) {
		tableFilter.setSearchText(searchText);		
	}

}
