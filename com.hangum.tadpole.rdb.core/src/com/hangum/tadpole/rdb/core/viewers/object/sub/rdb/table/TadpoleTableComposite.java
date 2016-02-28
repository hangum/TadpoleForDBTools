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
import java.util.List;

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
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
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
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDropAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRenameAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.TableColumnAddAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.TableColumnDeleteAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.TableColumnModifyAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.TableColumnSelectionAction;
import com.hangum.tadpole.rdb.core.extensionpoint.definition.ITableDecorationExtension;
import com.hangum.tadpole.rdb.core.extensionpoint.handler.TableDecorationContributionHandler;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.util.GenerateDDLScriptUtils;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.TableColumnComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.TableComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;
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

	/** Define table decoration extension */
	private ITableDecorationExtension tableDecorationExtension;
	
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
	private AbstractObjectAction renameAction_Table;
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
	
	private AbstractObjectAction addTableColumnAction;
	
	// table column
	private AbstractObjectAction tableColumnSelectionAction;
	private AbstractObjectAction tableColumnDeleteAction;
	private AbstractObjectAction tableColumnModifyAction;
	
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
		tbtmTable.setText(Messages.get().TadpoleTableComposite_0);
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
//					fixed https://github.com/hangum/TadpoleForDBTools/issues/666
//						if (selectTableName.equals(tableDAO.getName())) {
//							String strSQL = GenerateDDLScriptUtils.genTableScript(userDB, tableDAO, showTableColumns);
//							if(StringUtils.isNotEmpty(strSQL)) {
//								FindEditorAndWriteQueryUtil.run(userDB, strSQL, PublicTadpoleDefine.OBJECT_TYPE.TABLES);
//							}
//						} else {
							List<TableColumnDAO> tmpTableColumns = TadpoleObjectQuery.getTableColumns(userDB, tableDAO);
							String strSQL = GenerateDDLScriptUtils.genTableScript(userDB, tableDAO, tmpTableColumns);
							if(StringUtils.isNotEmpty(strSQL)) {
								FindEditorAndWriteQueryUtil.run(userDB, strSQL, PublicTadpoleDefine.OBJECT_TYPE.TABLES);
							}
//						}
					} catch(Exception e) {
						logger.error("table columns", e); //$NON-NLS-1$
					}
				}
			}
		});
		tableListViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if(PublicTadpoleDefine.YES_NO.NO.name().equals(userDB.getIs_showtables())) return;
				
				IStructuredSelection is = (IStructuredSelection) tableListViewer.getSelection();
				Object objDAO = is.getFirstElement();

				if (objDAO != null) {
					TableDAO tableDao = (TableDAO) objDAO;
					if (selectTableName.equals(tableDao.getName())) return;
					refreshTableColumn();
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
		tbName.setMoveable(true);
		tbName.setText(Messages.get().TadpoleTableComposite_1);
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
					logger.error("extension point exception " + e.getMessage()); //$NON-NLS-1$
				}
				
				return baseImage;
			}
			
			@Override
			public String getText(Object element) {
				TableDAO table = (TableDAO) element;
				final DBDefine selectDB = getUserDB().getDBDefine();
				if(selectDB == DBDefine.ORACLE_DEFAULT || 
						selectDB == DBDefine.POSTGRE_DEFAULT ||
						selectDB == DBDefine.MSSQL_DEFAULT) {
					
					if("".equals(table.getSchema_name()) | null == table.getSchema_name()) return table.getName();
					return table.getSchema_name() + "."+ table.getName();
					
				} else {
					return table.getName();
				}
				
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
		tbComment.setMoveable(true);
		tbComment.setText(Messages.get().TadpoleTableComposite_2);
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
		tableColumnViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.Move);
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
		String[] name 		= {Messages.get().TadpoleTableComposite_4, Messages.get().TadpoleTableComposite_5, Messages.get().TadpoleTableComposite_6, Messages.get().TadpoleTableComposite_7, Messages.get().TadpoleTableComposite_8, Messages.get().TadpoleTableComposite_9, Messages.get().TadpoleTableComposite_10};
		int[] size 			= {120, 90, 100, 50, 50, 50, 50};
		
		// table column tooltip
		ColumnViewerToolTipSupport.enableFor(tableColumnViewer);
		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tableColumnViewer, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
			tableColumn.getColumn().addSelectionListener(getSelectionAdapter(tableColumn, i));
			tableColumn.getColumn().setMoveable(true);
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
	 * create Table menu
	 */
	private void createTableMenu() {
		creatAction_Table = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleTableComposite_11);
		renameAction_Table= new ObjectRenameAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleTableComposite_18);
		dropAction_Table = new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleTableComposite_12);
		refreshAction_Table = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleTableComposite_13);

		// generation sample data
		generateSampleData = new GenerateSampleDataAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleTableComposite_14);
		
		generateDMLAction = new GenerateSQLDMLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "DML"); //$NON-NLS-1$
		selectStmtAction = new GenerateSQLSelectAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Select"); //$NON-NLS-1$
		insertStmtAction = new GenerateSQLInsertAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Insert"); //$NON-NLS-1$
		updateStmtAction = new GenerateSQLUpdateAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Update"); //$NON-NLS-1$
		deleteStmtAction = new GenerateSQLDeleteAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Delete"); //$NON-NLS-1$
		
		addTableColumnAction = new TableColumnAddAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Add column"); //$NON-NLS-1$
		
		viewDDLAction = new GenerateViewDDLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleTableComposite_16);
		
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
								manager.add(renameAction_Table);
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
								manager.add(separator);
								manager.add(renameAction_Table);
								manager.add(dropAction_Table);
								manager.add(separator);
								if (userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT | userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
									manager.add(addTableColumnAction);
									manager.add(separator);
								}
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
	 * create table column menu
	 */
	private void createTableColumnMenu() {
		tableColumnDeleteAction = new TableColumnDeleteAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Table"); //$NON-NLS-1$
		tableColumnSelectionAction = new TableColumnSelectionAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Table"); //$NON-NLS-1$
		tableColumnModifyAction = new TableColumnModifyAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Table"); //$NON-NLS-1$
		
		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT | userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
					manager.add(tableColumnModifyAction);
					manager.add(tableColumnDeleteAction);
					manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
				}
				
				manager.add(tableColumnSelectionAction);
			}
		});

		tableColumnViewer.getTable().setMenu(menuMgr.createContextMenu(tableColumnViewer.getTable()));
		getSite().registerContextMenu(menuMgr, tableColumnViewer);
	}
	
	/**
	 * refresh table column
	 */
	public void refreshTableColumn() {
		// 테이블의 컬럼 목록을 출력합니다.
		try {
			IStructuredSelection is = (IStructuredSelection) tableListViewer.getSelection();
			Object objDAO = is.getFirstElement();

			if (objDAO != null) {
				TableDAO tableDao = (TableDAO) objDAO;

				selectTableName = tableDao.getName();
				showTableColumns = TadpoleObjectQuery.getTableColumns(userDB, tableDao);
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
			listTablesDAO.add(new TableDAO(Messages.get().TadpoleMongoDBCollectionComposite_4, "")); //$NON-NLS-1$
			
			tableListViewer.setInput(listTablesDAO);
			tableListViewer.refresh();
			
			TableUtil.packTable(tableListViewer.getTable());
			
			return;
		}
		
		final String strBeginMsg = Messages.get().TadpoleTableComposite_17;
		Job job = new Job(Messages.get().MainEditor_45) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(strBeginMsg, IProgressMonitor.UNKNOWN);
				
				try {
					listTablesDAO = TadpoleObjectQuery.getTableList(userDB);
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
							ExceptionDetailsErrorDialog.openError(display.getActiveShell(), Messages.get().TadpoleTableComposite_3, Messages.get().ExplorerViewer_86, errStatus);
						}
					}
				});	// end display.asyncExec
			}	// end done
			
		});	// end job
		
		job.setName(userDB.getDisplay_name());
		job.setUser(true);
		job.schedule();
	}
	
//	/**
//	 * 디비 등록시 설정한 filter 정보를 적용한다.
//	 * 
//	 * @param userDB
//	 * @param listDAO
//	 */
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
		renameAction_Table.setUserDB(getUserDB());
		dropAction_Table.setUserDB(getUserDB());
		refreshAction_Table.setUserDB(getUserDB());

		generateSampleData.setUserDB(getUserDB());
		
		generateDMLAction.setUserDB(getUserDB());

		selectStmtAction.setUserDB(getUserDB());
		insertStmtAction.setUserDB(getUserDB());
		updateStmtAction.setUserDB(getUserDB());
		deleteStmtAction.setUserDB(getUserDB());
		
		addTableColumnAction.setUserDB(getUserDB());
		
		viewDDLAction.setUserDB(getUserDB());
		tableDataEditorAction.setUserDB(getUserDB());
		
		// table column
		tableColumnSelectionAction.setUserDB(getUserDB());
		tableColumnDeleteAction.setUserDB(getUserDB());
		tableColumnModifyAction.setUserDB(getUserDB());
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
		renameAction_Table.dispose();
		dropAction_Table.dispose();
		refreshAction_Table.dispose();
		generateSampleData.dispose();
		generateDMLAction.dispose();

		selectStmtAction.dispose();
		insertStmtAction.dispose();
		updateStmtAction.dispose();
		deleteStmtAction.dispose();
		addTableColumnAction.dispose();
		
		viewDDLAction.dispose();
		tableDataEditorAction.dispose();
		
		tableColumnSelectionAction.dispose();
		tableColumnDeleteAction.dispose();
		tableColumnModifyAction.dispose();
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		if("".equals(strObjectName) || strObjectName == null) return;
		
		tableListViewer.getTable().setFocus();
		
		// find select object and viewer select
		for(int i=0; i<listTablesDAO.size(); i++) {
			TableDAO tableDao = (TableDAO)tableListViewer.getElementAt(i);
			if(tableDao != null & StringUtils.equalsIgnoreCase(strObjectName, tableDao.getName())) {
				tableListViewer.setSelection(new StructuredSelection(tableListViewer.getElementAt(i)), true);
				break;
			}
		}
	}
}
