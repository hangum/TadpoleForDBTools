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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.security.DBAccessCtlManager;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.tables.AutoResizeTableLayout;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.TableDataEditorAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateSQLDMLAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateSQLDeleteAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateSQLInsertAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateSQLSelectAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateSQLUpdateAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateSampleDataAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateViewDDLAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.AlterTableAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDropAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.TableColumnSelectionAction;
import com.hangum.tadpole.rdb.core.editors.main.utils.MakeContentAssistUtil;
import com.hangum.tadpole.rdb.core.extensionpoint.definition.ITableDecorationExtension;
import com.hangum.tadpole.rdb.core.extensionpoint.handler.TableDecorationContributionHandler;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.util.GenerateDDLScriptUtils;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.TableColumnComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.TableComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.TadpoleObjectQuery;
import com.hangum.tadpole.tajo.core.connections.TajoConnectionManager;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.swtdesigner.ResourceManager;

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
	private CTabItem tbtmTable;	
	/** selected table name */
	private String selectTableName = ""; //$NON-NLS-1$

	// table info
	private TableViewer tableListViewer;
	private List<TableDAO> listTablesDAO = new ArrayList<TableDAO>();
	private ObjectComparator tableComparator;
	private TableFilter tableFilter;
	
	// column info
	private TableViewer tableColumnViewer;
	private ObjectComparator tableColumnComparator;
	private List<TableColumnDAO> showTableColumns = new ArrayList<>();
	
	private AbstractObjectAction creatAction_Table;
	private AbstractObjectAction dropAction_Table;
	private AbstractObjectAction refreshAction_Table;

	private AbstractObjectAction generateSampleData;
	
	private AbstractObjectAction generateDMLAction;

	private AbstractObjectAction selectStmtAction;
	private AbstractObjectAction insertStmtAction;
	private AbstractObjectAction updateStmtAction;
	private AbstractObjectAction deleteStmtAction;
	
	private AbstractObjectAction viewDDLAction;
	private AbstractObjectAction tableDataEditorAction;
	
	/** table editor action */
	private AlterTableAction alterTableAction;
	
	// table column
	private TableColumnSelectionAction tableColumnSelectionAction;
	
	private ITableDecorationExtension tableDecorationExtension;
	
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
	
	public List<TableColumnDAO> getShowTableColumns() {
		return showTableColumns;
	}
	
	private void createWidget(final CTabFolder tabFolderObject) {		
		tbtmTable = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmTable.setText(Messages.TadpoleTableComposite_0);
		tbtmTable.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.TABLES.name());

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

		tableListViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tableListViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				if(PublicTadpoleDefine.YES_NO.NO.name().equals(userDB.getIs_showtables())) return;
				
				IStructuredSelection is = (IStructuredSelection) event.getSelection();
				if (null != is) {
					TableDAO tableDAO = (TableDAO) is.getFirstElement();
					
					try {
						if (selectTableName.equals(tableDAO.getName())) {
							String strSQL = GenerateDDLScriptUtils.genTableScript(userDB, tableDAO, showTableColumns);
							if(StringUtils.isNotEmpty(strSQL)) {
								FindEditorAndWriteQueryUtil.run(userDB, strSQL, PublicTadpoleDefine.OBJECT_TYPE.TABLES);
							}
						} else {
							List<TableColumnDAO> tmpTableColumns = TadpoleObjectQuery.makeShowTableColumns(userDB, tableDAO);
							String strSQL = GenerateDDLScriptUtils.genTableScript(userDB, tableDAO, tmpTableColumns);
							if(StringUtils.isNotEmpty(strSQL)) {
								FindEditorAndWriteQueryUtil.run(userDB, strSQL, PublicTadpoleDefine.OBJECT_TYPE.TABLES);
							}
						}
					} catch(Exception e) {
						logger.error("table columns", e); //$NON-NLS-1$
					}
				}
			}
		});
		tableListViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if(PublicTadpoleDefine.YES_NO.NO.name().equals(userDB.getIs_showtables())) return;
				
				// 테이블의 컬럼 목록을 출력합니다.
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					Object tableDAO = is.getFirstElement();

					if (tableDAO != null) {
						TableDAO table = (TableDAO) tableDAO;

						if (selectTableName.equals(table.getName())) return;
						selectTableName = table.getName();
						showTableColumns = TadpoleObjectQuery.makeShowTableColumns(userDB, table);
					} else {
						showTableColumns = new ArrayList<>();
						selectTableName = ""; //$NON-NLS-1$
					}
				} catch (Exception e) {
					logger.error("get table column", e); //$NON-NLS-1$
					
					// initialize table columns
					showTableColumns.clear();

					// show error message
					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(tabFolderObject.getShell(), "Error", e.getMessage(), errStatus); //$NON-NLS-1$
				} finally {
					tableColumnViewer.setInput(showTableColumns);
					tableColumnComparator = new TableColumnComparator();
					tableColumnViewer.setSorter(tableColumnComparator);
					tableColumnViewer.refresh();
					TableUtil.packTable(tableColumnViewer.getTable());
				}
			}
		});

		Table tableTableList = tableListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);
		
		// sorter
		tableComparator = new TableComparator();
		tableListViewer.setSorter(tableComparator);
		tableComparator.setColumn(0);
		
		// auto table layout
		AutoResizeTableLayout layoutColumnLayout = new AutoResizeTableLayout(tableListViewer.getTable());
		
		// table decoration extension start...
		TableDecorationContributionHandler decorationExtension = new TableDecorationContributionHandler();
		tableDecorationExtension = decorationExtension.evaluateCreateWidgetContribs(userDB);
		// table decoration extension end...

		TableViewerColumn tvColName = new TableViewerColumn(tableListViewer, SWT.NONE);
		TableColumn tbName = tvColName.getColumn();
		tbName.setWidth(170);
		tbName.setText(Messages.TadpoleTableComposite_1);
		tbName.addSelectionListener(getSelectionAdapter(tableListViewer, tableComparator, tbName, 0));
		tvColName.setLabelProvider(new ColumnLabelProvider() {
			
			@Override
			public Image getImage(Object element) {
				Image baseImage = ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/tables.png"); //$NON-NLS-1$
				
				try {
					if(tableDecorationExtension != null) {
						TableDAO table = (TableDAO) element;
						Image extensionImage = tableDecorationExtension.getTableImage(table.getName());
						
						if(extensionImage != null) {
							return ResourceManager.decorateImage(baseImage, extensionImage, ResourceManager.BOTTOM_RIGHT);
						}
					}
				} catch(Exception e) {
					logger.error("extension point exception", e); //$NON-NLS-1$
				}
				
				return baseImage;
			}
			
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
				return 100;
			}

			public int getToolTipTimeDisplayed(Object object) {
				return 5000;
			}

			public void update(ViewerCell cell) {
				TableDAO table = (TableDAO)cell.getElement();
				cell.setText(table.getComment());
			}
		};

		TableViewerColumn tvTableComment = new TableViewerColumn(tableListViewer, SWT.NONE);
		TableColumn tbComment = tvTableComment.getColumn();
		tbComment.setWidth(200);
		tbComment.setText(Messages.TadpoleTableComposite_2);
		tbComment.addSelectionListener(getSelectionAdapter(tableListViewer, tableComparator, tbComment, 1));
		tvTableComment.setLabelProvider(clpTable);
		tvTableComment.setEditingSupport(new TableCommentEditorSupport(tableListViewer, userDB, 1));
		layoutColumnLayout.addColumnData(new ColumnWeightData(200));
		
		tableListViewer.getTable().setLayout(layoutColumnLayout);
		tableListViewer.setContentProvider(new ArrayContentProvider());
		tableListViewer.setInput(listTablesDAO);
		
		// dnd 기능 추가
		Transfer[] transferTypes = new Transfer[]{TextTransfer.getInstance()};
		tableListViewer.addDragSupport(DND_OPERATIONS, transferTypes , new TableDragListener(userDB, tableListViewer));

		// filter
		tableFilter = new TableFilter();
		tableListViewer.addFilter(tableFilter);
		
		createTableMenu();

		// columns
		tableColumnViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tableColumnViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection is = (IStructuredSelection) event.getSelection();

				if (null != is) {
					TableColumnDAO tableDAO = (TableColumnDAO) is.getFirstElement();
					FindEditorAndWriteQueryUtil.runAtPosition(StringUtils.trim(tableDAO.getField()));
				}
			}
		});
		Table tableTableColumn = tableColumnViewer.getTable();
		tableTableColumn.setHeaderVisible(true);
		tableTableColumn.setLinesVisible(true);
		
		tableColumnComparator = new TableColumnComparator();
		tableColumnViewer.setSorter(tableColumnComparator);

		createTableColumne();

		tableColumnViewer.setContentProvider(new ArrayContentProvider());
		tableColumnViewer.setLabelProvider(new TableColumnLabelprovider(tableListViewer, tableDecorationExtension));
		
		createTableColumnMenu();
		
		sashForm.setWeights(new int[] { 1, 1 });
	}
	
	/**
	 * table table column
	 */
	protected void createTableColumne() {
		String[] name = {Messages.TadpoleTableComposite_4, Messages.TadpoleTableComposite_5, Messages.TadpoleTableComposite_6, Messages.TadpoleTableComposite_7, Messages.TadpoleTableComposite_8, Messages.TadpoleTableComposite_9, Messages.TadpoleTableComposite_10};
		int[] size = {120, 90, 100, 50, 50, 50, 50};
		
		// table column tooltip
		ColumnViewerToolTipSupport.enableFor(tableColumnViewer);
		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tableColumnViewer, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
			tableColumn.getColumn().addSelectionListener(getSelectionAdapter(tableColumn, i));
			tableColumn.setEditingSupport(new ColumnCommentEditorSupport(tableListViewer, tableColumnViewer, userDB, i));
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
	 * create table column menu
	 */
	private void createTableColumnMenu() {
		tableColumnSelectionAction = new TableColumnSelectionAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Table"); //$NON-NLS-1$
		
		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(tableColumnSelectionAction);
			}
		});

		tableColumnViewer.getTable().setMenu(menuMgr.createContextMenu(tableColumnViewer.getTable()));
		getSite().registerContextMenu(menuMgr, tableColumnViewer);
	}

	/**
	 * create Table menu
	 */
	private void createTableMenu() {
		creatAction_Table = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.TadpoleTableComposite_11);
		dropAction_Table = new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.TadpoleTableComposite_12);
		refreshAction_Table = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.TadpoleTableComposite_13);

		// generation sample data
		generateSampleData = new GenerateSampleDataAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.TadpoleTableComposite_14);
		
		generateDMLAction = new GenerateSQLDMLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "DML"); //$NON-NLS-1$
		selectStmtAction = new GenerateSQLSelectAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Select"); //$NON-NLS-1$
		insertStmtAction = new GenerateSQLInsertAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Insert"); //$NON-NLS-1$
		updateStmtAction = new GenerateSQLUpdateAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Update"); //$NON-NLS-1$
		deleteStmtAction = new GenerateSQLDeleteAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Delete"); //$NON-NLS-1$
		
		alterTableAction = new AlterTableAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.TadpoleTableComposite_15);
		
		viewDDLAction = new GenerateViewDDLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.TadpoleTableComposite_16);
		
		tableDataEditorAction = new TableDataEditorAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES);

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				Separator separator = new Separator(IWorkbenchActionConstants.MB_ADDITIONS);
				if (userDB != null) {
					// hive & tajo
					if(userDB.getDBDefine() == DBDefine.HIVE_DEFAULT || 
							userDB.getDBDefine() == DBDefine.HIVE2_DEFAULT || 
									userDB.getDBDefine() == DBDefine.TAJO_DEFAULT) {
						if(PermissionChecker.isShow(getUserRoleType(), userDB)) {
							
							if(!isDDLLock()) {
								manager.add(creatAction_Table);
								manager.add(dropAction_Table);
								manager.add(separator);
							}
						}	
						
						manager.add(refreshAction_Table);
						manager.add(separator);
						manager.add(selectStmtAction);
					// others rdb
					} else {
						if(PermissionChecker.isShow(getUserRoleType(), userDB)) {
							if(!isDDLLock()) {
								manager.add(creatAction_Table);
								manager.add(dropAction_Table);
								manager.add(separator);
							}
						}	
						
						manager.add(refreshAction_Table);
						manager.add(separator);
						
						// 현재는 oracle db만 데이터 수정 모드..
						if (userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT) {
							manager.add(generateSampleData);
							manager.add(separator);
						}
						
						manager.add(generateDMLAction);
						manager.add(separator);
						manager.add(selectStmtAction);
						
						if(PermissionChecker.isShow(getUserRoleType(), userDB)) {
							if(!isInsertLock()) manager.add(insertStmtAction);
							if(!isUpdateLock()) manager.add(updateStmtAction);
							if(!isDeleteLock()) manager.add(deleteStmtAction);
							
							manager.add(separator);
							manager.add(alterTableAction);
	
							manager.add(separator);
							manager.add(viewDDLAction);
							if(!(isInsertLock() | isUpdateLock() | isDeleteLock())) {
								manager.add(separator);
								manager.add(tableDataEditorAction);
							}
						}
					}	// if rdb
				}	// if hive and tajo
			}
		});

		tableListViewer.getTable().setMenu(menuMgr.createContextMenu(tableListViewer.getTable()));
		getSite().registerContextMenu(menuMgr, tableListViewer);
	}

	/**
	 * table 정보를 최신으로 리프레쉬합니다.
	 * 
	 * @param selectUserDb
	 * @param boolRefresh
	 * @param strObjectName
	 */
	public void refreshTable(final UserDBDAO selectUserDb, final boolean boolRefresh, final String strObjectName) {
		if(!boolRefresh) if(selectUserDb == null) return;
		this.userDB = selectUserDb;
		
		selectTableName = ""; //$NON-NLS-1$
		
		// 테이블 등록시 테이블 목록 보이지 않는 옵션을 선택했는지.
		if(PublicTadpoleDefine.YES_NO.NO.name().equals(this.userDB.getIs_showtables())) {
			listTablesDAO.clear();
			listTablesDAO.add(new TableDAO(Messages.TadpoleMongoDBCollectionComposite_4, "")); //$NON-NLS-1$
			
			tableListViewer.setInput(listTablesDAO);
			tableListViewer.refresh();
			
			TableUtil.packTable(tableListViewer.getTable());
			
			return;
		}
		
		Job job = new Job(Messages.MainEditor_45) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(Messages.TadpoleTableComposite_17, IProgressMonitor.UNKNOWN);
				
				try {
					listTablesDAO = getTableList(userDB);
				} catch(Exception e) {
					logger.error("Table Referesh", e); //$NON-NLS-1$
					
					return new Status(Status.WARNING, Activator.PLUGIN_ID, e.getMessage(), e);
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
				
				final Display display = getSite().getShell().getDisplay();
				display.asyncExec(new Runnable() {
					public void run() {
						if(jobEvent.getResult().isOK()) {
							tableListViewer.setInput(listTablesDAO);
							TableUtil.packTable(tableListViewer.getTable());
							// select tabitem
							getTabFolderObject().setSelection(tbtmTable);
							
							selectDataOfTable(strObjectName);
						} else {
							if (listTablesDAO != null) listTablesDAO.clear();
							tableListViewer.setInput(listTablesDAO);
							tableListViewer.refresh();
							TableUtil.packTable(tableListViewer.getTable());

							Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, jobEvent.getResult().getMessage(), jobEvent.getResult().getException()); //$NON-NLS-1$
							ExceptionDetailsErrorDialog.openError(display.getActiveShell(), Messages.TadpoleTableComposite_3, Messages.ExplorerViewer_86, errStatus);
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
	private List<TableDAO> getTableList(final UserDBDAO userDB) throws Exception {
		List<TableDAO> showTables = null;
				
		if(userDB.getDBDefine() == DBDefine.TAJO_DEFAULT) {
			showTables = new TajoConnectionManager().tableList(userDB);

			// sql keyword를 설정합니다.
			if(TadpoleSQLManager.getDbMetadata(userDB) == null) {
				TadpoleSQLManager.setMetaData(TadpoleSQLManager.getKey(userDB), userDB, TajoConnectionManager.getKeyworkd(userDB));
			}
			
		} else {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showTables = sqlClient.queryForList("tableList", userDB.getDb()); //$NON-NLS-1$			
		}
		
		/** filter 정보가 있으면 처리합니다. */
		return getTableAfterwork(showTables, userDB);
	}
	
	/**
	 * Table 정보 처리 후에 
	 * 
	 * @param showTables
	 * @param userDB
	 * @return
	 */
	private List<TableDAO> getTableAfterwork(List<TableDAO> showTables, final UserDBDAO userDB) {
		final StringBuffer strTablelist = new StringBuffer();
		/** filter 정보가 있으면 처리합니다. */
		showTables = DBAccessCtlManager.getInstance().getTableFilter(showTables, userDB);
		
		// 시스템에서 사용하는 용도록 수정합니다. '나 "를 붙이도록.
		for(TableDAO td : showTables) {
			td.setSysName(SQLUtil.makeIdentifierName(userDB, td.getName()));
			
			strTablelist.append(td.getSysName()).append("|"); //$NON-NLS-1$
		}
		
		// setting UserDBDAO 
		userDB.setListTable(showTables);

		// content assist util
		MakeContentAssistUtil contentAssistUtil = new MakeContentAssistUtil();
		contentAssistUtil.makeContentAssistUtil(userDB);
		
		return showTables;
	}
	
	/**
	 * @param userDB
	 * @param strObject
	 * @return
	 */
	public static TableDAO getTable(UserDBDAO userDB, String strObject) throws Exception {
		TableDAO tableDao = null;
		List<TableDAO> showTables = new ArrayList<TableDAO>();
		
		if(userDB.getDBDefine() == DBDefine.TAJO_DEFAULT) {
			List<TableDAO> tmpShowTables = new TajoConnectionManager().tableList(userDB);
			
			for(TableDAO dao : tmpShowTables) {
				if(dao.getName().equals(strObject)) {
					showTables.add(dao);
					break;
				}
			}
		} else if(userDB.getDBDefine() == DBDefine.HIVE_DEFAULT | userDB.getDBDefine() == DBDefine.HIVE2_DEFAULT) {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			List<TableDAO> tmpShowTables = sqlClient.queryForList("tableList", userDB.getDb()); //$NON-NLS-1$
			
			for(TableDAO dao : tmpShowTables) {
				if(dao.getName().equals(strObject)) {
					showTables.add(dao);
					break;
				}
			}
			
		} else {
			Map<String, Object> mapParam = new HashMap<String, Object>();
			mapParam.put("db", 	userDB.getDb()); //$NON-NLS-1$
			mapParam.put("name", 	strObject); //$NON-NLS-1$
			
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showTables = sqlClient.queryForList("table", mapParam); //$NON-NLS-1$			
		}
		
		/** filter 정보가 있으면 처리합니다. */
		showTables = DBAccessCtlManager.getInstance().getTableFilter(showTables, userDB);
		
		if(!showTables.isEmpty()) { 
			tableDao = showTables.get(0);
			tableDao.setSysName(SQLUtil.makeIdentifierName(userDB, tableDao.getName()));
			return tableDao;
		} else {
			return null;
		}
	}

	/**
	 * 디비 등록시 설정한 filter 정보를 적용한다.
	 * 
	 * @param userDB
	 * @param listDAO
	 */
//	public static List<TableDAO> filter(UserDBDAO userDB, List<TableDAO> listDAO) {
//		
//		if("YES".equals(userDB.getIs_table_filter())){
//			List<TableDAO> tmpShowTables = new ArrayList<TableDAO>();
//			String includeFilter = userDB.getTable_filter_include();
//			if("".equals(includeFilter)) {
//				tmpShowTables.addAll(listDAO);					
//			} else {
//				for (TableDAO tableDao : listDAO) {
//					String[] strArryFilters = StringUtils.split(userDB.getTable_filter_include(), ",");
//					for (String strFilter : strArryFilters) {
//						if(tableDao.getName().matches(strFilter)) {
//							tmpShowTables.add(tableDao);
//						}
//					}
//				}
//			}
//			
//			String excludeFilter = userDB.getTable_filter_exclude();
//			if(!"".equals(excludeFilter)) {
//				for (TableDAO tableDao : tmpShowTables) {
//					String[] strArryFilters = StringUtils.split(userDB.getTable_filter_exclude(), ",");
//					for (String strFilter : strArryFilters) {
//						if(tableDao.getName().matches(strFilter)) {
//							tmpShowTables.remove(tableDao);
//						}
//					}
//				}
//			}
//			
//			return tmpShowTables;
//		}
//		
//		return listDAO;
//	}
	
	/**
	 * initialize action
	 */
	public void initAction() {
		creatAction_Table.setUserDB(getUserDB());
		dropAction_Table.setUserDB(getUserDB());
		refreshAction_Table.setUserDB(getUserDB());

		generateSampleData.setUserDB(getUserDB());
		
		generateDMLAction.setUserDB(getUserDB());

		selectStmtAction.setUserDB(getUserDB());
		insertStmtAction.setUserDB(getUserDB());
		updateStmtAction.setUserDB(getUserDB());
		deleteStmtAction.setUserDB(getUserDB());
		
		alterTableAction.setUserDB(getUserDB());
		
		viewDDLAction.setUserDB(getUserDB());
		tableDataEditorAction.setUserDB(getUserDB());
		
		// table column
		tableColumnSelectionAction.setUserDB(getUserDB());
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
	 * get table column viewer
	 * @return
	 */
	public TableViewer getTableColumnViewer() {
		return tableColumnViewer;
	}

	/**
	 * table search 
	 * 
	 * @param string
	 */
	public void setSearchText(String searchText) {
		tableFilter.setSearchText(searchText);		
	}
	
	@Override
	public void dispose() {
		super.dispose();

		creatAction_Table.dispose();
		dropAction_Table.dispose();
		refreshAction_Table.dispose();
		generateSampleData.dispose();
		generateDMLAction.dispose();

		selectStmtAction.dispose();
		insertStmtAction.dispose();
		updateStmtAction.dispose();
		deleteStmtAction.dispose();
		
		alterTableAction.dispose();
		
		viewDDLAction.dispose();
		tableDataEditorAction.dispose();
		
		tableColumnSelectionAction.dispose();
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		if("".equals(strObjectName) || strObjectName == null) return;
		
		tableListViewer.getTable().setFocus();
		
		// find select object and viewer select
		for(int i=0; i<listTablesDAO.size(); i++) {
			TableDAO tableDao = (TableDAO)tableListViewer.getElementAt(i);
			if(StringUtils.equalsIgnoreCase(strObjectName, tableDao.getName())) {
				tableListViewer.setSelection(new StructuredSelection(tableListViewer.getElementAt(i)), true);
				break;
			}
		}
	}
}
