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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.commons.sql.define.DBDefine;
import com.hangum.tadpole.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.dao.mysql.TableDAO;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.GenerateSQLDMLAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.GenerateSQLDeleteAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.GenerateSQLInsertAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.GenerateSQLSelectAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.GenerateSQLUpdateAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.GenerateSampleDataAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.GenerateViewDDLAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.ObjectDeleteAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.TableDataEditorAction;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.TableColumnComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.TableComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.system.permission.PermissionChecker;
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
	private List<TableDAO> showTables = new ArrayList<TableDAO>();
	private ObjectComparator tableComparator;
	private TableFilter tableFilter;
	
	// column info
	private TableViewer tableColumnViewer;
	private ObjectComparator tableColumnComparator;
	private List showTableColumns;
	
	private AbstractObjectAction creatAction_Table;
	private AbstractObjectAction deleteAction_Table;
	private AbstractObjectAction refreshAction_Table;

	private AbstractObjectAction generateSampleData;
	
	private GenerateSQLDMLAction selectDMLAction;

	private GenerateSQLSelectAction selectStmtAction;
	private GenerateSQLSelectAction insertStmtAction;
	private GenerateSQLSelectAction updateStmtAction;
	private GenerateSQLSelectAction deleteStmtAction;
	
	private AbstractObjectAction viewDDLAction;
	
	private AbstractObjectAction tableDataEditorAction;
	
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

		//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		tableListViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);// SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		tableListViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				if(PublicTadpoleDefine.YES_NO.NO.toString().equals(userDB.getIs_showtables())) return;
				
				IStructuredSelection is = (IStructuredSelection) event.getSelection();

				if(PermissionChecker.isShow(getUserRoleType(), userDB)) {
					if (null != is) {
						try {
							TableDAO tableDAO = (TableDAO) is.getFirstElement();
							StringBuffer sbSQL = new StringBuffer();
		
							Map<String, String> parameter = new HashMap<String, String>();
							parameter.put("db", userDB.getDb());
							parameter.put("table", tableDAO.getName());
							
							SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
							List<TableColumnDAO> showTableColumns = sqlClient.queryForList("tableColumnList", parameter); //$NON-NLS-1$
							
							sbSQL.append(" SELECT "); //$NON-NLS-1$
							for (int i=0; i<showTableColumns.size(); i++) {
								TableColumnDAO dao = showTableColumns.get(i);
								sbSQL.append(dao.getField());
								
								// 마지막 컬럼에는 ,를 않넣어주어야하니까 
								if(i < (showTableColumns.size()-1)) sbSQL.append(", ");  //$NON-NLS-1$
								else sbSQL.append(" "); //$NON-NLS-1$
							}
							sbSQL.append(PublicTadpoleDefine.LINE_SEPARATOR + " FROM " + tableDAO.getName() + PublicTadpoleDefine.SQL_DILIMITER); //$NON-NLS-1$ //$NON-NLS-2$
							
							//
							FindEditorAndWriteQueryUtil.run(userDB, sbSQL.toString());
						} catch(Exception e) {
							logger.error(Messages.GenerateSQLSelectAction_8, e);
							
							Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
							ExceptionDetailsErrorDialog.openError(null, "Error", Messages.GenerateSQLSelectAction_0, errStatus); //$NON-NLS-1$
						}
					}
				}
			}
		});
		tableListViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if(PublicTadpoleDefine.YES_NO.NO.toString().equals(userDB.getIs_showtables())) return;
				
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
		CellLabelProvider clpTable = new CellLabelProvider() {

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

		TableViewerColumn tvTableComment = new TableViewerColumn(tableListViewer, SWT.NONE);
		TableColumn tbComment = tvTableComment.getColumn();
		tbComment.setWidth(200);
		tbComment.setText("Comment"); //$NON-NLS-1$
		tbComment.addSelectionListener(getSelectionAdapter(tableListViewer, tableComparator, tbComment, 1));
		tvTableComment.setLabelProvider(clpTable);
		tvTableComment.setEditingSupport(new TableCommentEditorSupport(tableListViewer, userDB, 1));
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
		tableColumnViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTableColumn = tableColumnViewer.getTable();
		tableTableColumn.setHeaderVisible(true);
		tableTableColumn.setLinesVisible(true);
		
		tableColumnComparator = new TableColumnComparator();
		tableColumnViewer.setSorter(tableColumnComparator);

		createTableColumne(tableColumnViewer);

		tableColumnViewer.setContentProvider(new ArrayContentProvider());
		tableColumnViewer.setLabelProvider(new TableColumnLabelprovider());
		createMenu();
		
		sashForm.setWeights(new int[] { 1, 1 });
	}
	
	/**
	 * table table column
	 */
	protected void createTableColumne(final TableViewer tv) {
		String[] name = {"Field", "Type", "Key", "Comment", "Null", "Default", "Extra"};
		int[] size = {120, 90, 50, 100, 50, 50, 50};
		
		// table column tooltip
		ColumnViewerToolTipSupport.enableFor(tv);
		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tv, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
			tableColumn.getColumn().addSelectionListener(getSelectionAdapter(tableColumn, i));
			tableColumn.setEditingSupport(new ColumnCommentEditorSupport(tableListViewer, tv, userDB, i));
		}
	}
	/**
	 * selection adapter
	 * 
	 * @param tableColumn
	 * @param i
	 * @return
	 */
	private SelectionAdapter getSelectionAdapter(final TableViewerColumn tableColumn, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableColumnComparator.setColumn(index);
				
				tableColumnViewer.getTable().setSortDirection(tableColumnComparator.getDirection());
				tableColumnViewer.getTable().setSortColumn(tableColumn.getColumn());
				
				tableColumnViewer.refresh();
			}
		};
		
		return selectionAdapter;
	}

	/**
	 * create menu
	 */
	private void createMenu() {
		creatAction_Table = new ObjectCreatAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.TABLES, "Table"); //$NON-NLS-1$
		deleteAction_Table = new ObjectDeleteAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.TABLES, "Table"); //$NON-NLS-1$
		refreshAction_Table = new ObjectRefreshAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.TABLES, "Table"); //$NON-NLS-1$

		// generation sample data
		generateSampleData = new GenerateSampleDataAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.TABLES, "Table"); //$NON-NLS-1$
		
		selectDMLAction = new GenerateSQLDMLAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.TABLES, "DML"); //$NON-NLS-1$

		selectStmtAction = new GenerateSQLSelectAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.TABLES, "Select"); //$NON-NLS-1$
		insertStmtAction = new GenerateSQLInsertAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.TABLES, "Insert"); //$NON-NLS-1$
		updateStmtAction = new GenerateSQLUpdateAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.TABLES, "Update"); //$NON-NLS-1$
		deleteStmtAction = new GenerateSQLDeleteAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.TABLES, "Delete"); //$NON-NLS-1$
		
		viewDDLAction = new GenerateViewDDLAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.TABLES, "View"); //$NON-NLS-1$
		
		tableDataEditorAction = new TableDataEditorAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.TABLES);

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (userDB != null) {
					if(PermissionChecker.isShow(getUserRoleType(), userDB)) {
						manager.add(creatAction_Table);
						manager.add(deleteAction_Table);
					}					
					manager.add(refreshAction_Table);

					// 현재는 oracle db만 데이터 수정 모드..
					if (DBDefine.getDBDefine(userDB.getDbms_types()) == DBDefine.ORACLE_DEFAULT) {
						manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
						manager.add(generateSampleData);
					}
					
					manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
					manager.add(selectDMLAction);

					manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
					manager.add(selectStmtAction);
					
					if(PermissionChecker.isShow(getUserRoleType(), userDB)) {
						manager.add(insertStmtAction);
						
						if(DBDefine.getDBDefine(userDB) != DBDefine.HIVE_DEFAULT) {
							manager.add(updateStmtAction);
							manager.add(deleteStmtAction);

							manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
							manager.add(viewDDLAction);
							manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
							manager.add(tableDataEditorAction);
						}
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
		
		// 테이블 등록시 테이블 목록 보이지 않는 옵션을 선택했는지.
		if(PublicTadpoleDefine.YES_NO.NO.toString().equals(this.userDB.getIs_showtables())) {
			showTables.add(new TableDAO(Messages.TadpoleMongoDBCollectionComposite_4, ""));
			
			tableListViewer.setInput(showTables);
			tableListViewer.refresh();
			
			return;
		}
		
		Job job = new Job(Messages.MainEditor_45) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Connect database", IProgressMonitor.UNKNOWN);
				
				try {
//					SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
//					showTables = sqlClient.queryForList("tableList", userDB.getDb()); //$NON-NLS-1$
//					
//					/** filter 정보가 있으면 처리합니다. */
//					filter();
					showTables = getTableList(userDB);
					
				} catch(Exception e) {
					logger.error("Table Referesh", e);
					
					return new Status(Status.WARNING, Activator.PLUGIN_ID, e.getMessage());
				} finally {
					monitor.done();
				}
				
				/////////////////////////////////////////////////////////////////////////////////////////
				return Status.OK_STATUS;
			}
		};
		
		// job의 event를 처리해 줍니다.
		job.addJobChangeListener(new JobChangeAdapter() {
			
			public void done(IJobChangeEvent event) {
				final IJobChangeEvent jobEvent = event; 
				
				getSite().getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if(jobEvent.getResult().isOK()) {
							tableListViewer.setInput(showTables);
							tableListViewer.refresh();
						} else {
							if (showTables != null) showTables.clear();
							tableListViewer.setInput(showTables);
							tableListViewer.refresh();
							
							Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, jobEvent.getResult().getMessage(), jobEvent.getResult().getException()); //$NON-NLS-1$
							ExceptionDetailsErrorDialog.openError(null, "Error", Messages.ExplorerViewer_86, errStatus); //$NON-NLS-1$
						}
					}
				});	// end display.asyncExec
			}	// end done
			
		});	// end job
		
		job.setName(userDB.getDisplay_name());
		job.setUser(true);
		job.schedule();
	}
	
	/**
	 * 보여 주어야할 테이블 목록을 정의합니다.
	 *
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	public static List<TableDAO> getTableList(final UserDBDAO userDB) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		List<TableDAO> showTables = sqlClient.queryForList("tableList", userDB.getDb()); //$NON-NLS-1$
		
		/** filter 정보가 있으면 처리합니다. */
		return filter(userDB, showTables);
	}
	
	/**
	 * 디비 등록시 설정한 filter 정보를 적용한다.
	 * 
	 * @param userDB
	 * @param listDAO
	 */
	public static List<TableDAO> filter(UserDBDAO userDB, List<TableDAO> listDAO) {
		
		if("YES".equals(userDB.getIs_table_filter())){
			List<TableDAO> tmpShowTables = new ArrayList<TableDAO>();
			String includeFilter = userDB.getTable_filter_include();
			if("".equals(includeFilter)) {
				tmpShowTables.addAll(listDAO);					
			} else {
				for (TableDAO tableDao : listDAO) {
					String[] strArryFilters = StringUtils.split(userDB.getTable_filter_include(), ",");
					for (String strFilter : strArryFilters) {
						if(tableDao.getName().matches(strFilter)) {
							tmpShowTables.add(tableDao);
						}
					}
				}
			}
			
			String excludeFilter = userDB.getTable_filter_exclude();
			if(!"".equals(excludeFilter)) {
				for (TableDAO tableDao : tmpShowTables) {
					String[] strArryFilters = StringUtils.split(userDB.getTable_filter_exclude(), ",");
					for (String strFilter : strArryFilters) {
						if(tableDao.getName().matches(strFilter)) {
							tmpShowTables.remove(tableDao);
						}
					}
				}
			}
			
			return tmpShowTables;
		}
		
		return listDAO;
	}
	
	/**
	 * initialize action
	 */
	public void initAction() {
		creatAction_Table.setUserDB(getUserDB());
		deleteAction_Table.setUserDB(getUserDB());
		refreshAction_Table.setUserDB(getUserDB());

		generateSampleData.setUserDB(getUserDB());
		
		selectDMLAction.setUserDB(getUserDB());

		selectStmtAction.setUserDB(getUserDB());
		insertStmtAction.setUserDB(getUserDB());
		updateStmtAction.setUserDB(getUserDB());
		deleteStmtAction.setUserDB(getUserDB());
		
		viewDDLAction.setUserDB(getUserDB());
		tableDataEditorAction.setUserDB(getUserDB());
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
	 * table search 
	 * 
	 * @param string
	 */
	public void setSearchText(String searchText) {
		tableFilter.setSearchText(searchText);		
	}

}
