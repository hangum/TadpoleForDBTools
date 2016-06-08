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
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
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
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.TableColumnCreateAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.TableRelationAction;
import com.hangum.tadpole.rdb.core.extensionpoint.definition.ITableDecorationExtension;
import com.hangum.tadpole.rdb.core.extensionpoint.handler.TableDecorationContributionHandler;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.util.GenerateDDLScriptUtils;
import com.hangum.tadpole.rdb.core.viewers.object.ExplorerViewer;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.TableComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.columns.TableColumnComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.constraints.TadpoleConstraintComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.index.TadpoleIndexesComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.trigger.TadpoleTriggerComposite;
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

	private AbstractObjectAction creatAction_Table;
	private AbstractObjectAction renameAction_Table;
	private AbstractObjectAction tableRelationAction;
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
	private CTabFolder tabTableFolder;
	private TableColumnComposite 		tableColumnComposite;
	private TadpoleIndexesComposite 	indexComposite;
	private TadpoleConstraintComposite 	constraintsComposite;
	private TadpoleTriggerComposite 	triggerComposite;
	
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
				if (!is.isEmpty()) {
					
					Object objDAO = is.getFirstElement();
					TableDAO tableDao = (TableDAO) objDAO;
					if (selectTableName.equals(tableDao.getName())) return;
					
					String selTabName = (String)tabTableFolder.getSelection().getData(TAB_DATA_KEY);
					if(PublicTadpoleDefine.OBJECT_TYPE.INDEXES.name().equals(selTabName)){
						// 인덱스 탭이 선택된 상태에서 다른 테이블을 선택할 경우 선택된 테이블에 정의된 인덱스 목록을 표시한다.
						indexComposite.setTable(userDB, tableDao);
					}else if(PublicTadpoleDefine.OBJECT_TYPE.CONSTRAINTS.name().equals(selTabName)){
						// 테이블에 정의된 제약조건 목록을 표시한다.
						constraintsComposite.setTable(userDB, tableDao);
					}else if(PublicTadpoleDefine.OBJECT_TYPE.TRIGGERS.name().equals(selTabName)){
						// 테이블에 정의된 트리거 목록을 표시한다.
						triggerComposite.setTable(userDB, tableDao);
					}else {
						tableColumnComposite.refreshTableColumn(tableListViewer);
					}
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
				if(selectDB == DBDefine.ORACLE_DEFAULT | 
						selectDB == DBDefine.POSTGRE_DEFAULT |
						selectDB == DBDefine.MSSQL_DEFAULT |
						selectDB == DBDefine.TIBERO_DEFAULT) {
					
					if("".equals(table.getSchema_name()) || null == table.getSchema_name()) return table.getName();
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
		tbComment.setText(Messages.get().Comment);
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
		tableFilter = new TableFilter(userDB);
		tableListViewer.addFilter(tableFilter);
		
		createTableMenu();
		
		// columns
		tabTableFolder = new CTabFolder(sashForm, SWT.NONE);
		tabTableFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				if (userDB == null) return;
				CTabItem ct = (CTabItem)evt.item;
					
				IStructuredSelection is = (IStructuredSelection) tableListViewer.getSelection();
				if (!is.isEmpty()) {
					Object objDAO = is.getFirstElement();
					TableDAO tableDao = (TableDAO) objDAO;
					String strSelectItemText = ""+ct.getData(AbstractObjectComposite.TAB_DATA_KEY);
					if (strSelectItemText.equalsIgnoreCase(OBJECT_TYPE.INDEXES.name())) {
						indexComposite.setTable(userDB, tableDao);
					} else if (strSelectItemText.equalsIgnoreCase(OBJECT_TYPE.CONSTRAINTS.name())) {
						constraintsComposite.setTable(userDB, tableDao);
					} else if (strSelectItemText.equalsIgnoreCase(OBJECT_TYPE.TRIGGERS.name())) {
						triggerComposite.setTable(userDB, tableDao);
					}
					// filterText();
					
					// google analytic
					AnalyticCaller.track(ExplorerViewer.ID, strSelectItemText);
				}
			}
			
		});
		tabTableFolder.setBorderVisible(false);
		tabTableFolder.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());
		
		sashForm.setWeights(new int[] { 1, 1 });
		
		initUI();
	}
	
	/**
	 * initialize ui
	 */
	private void initUI() {
		createColumns();
		
		if(userDB != null) {
			if(userDB.getDBDefine() == DBDefine.ALTIBASE_DEFAULT ||
				userDB.getDBDefine() == DBDefine.CUBRID_DEFAULT ||
				userDB.getDBDefine() == DBDefine.SQLite_DEFAULT ||
				userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT
			) {
				createIndexes();
				createTrigger();
			} else if(userDB.getDBDefine() == DBDefine.HIVE_DEFAULT ||
					userDB.getDBDefine() == DBDefine.HIVE2_DEFAULT ||
					userDB.getDBDefine() == DBDefine.TAJO_DEFAULT
			) {
				// do not show them
			} else {
				createIndexes();
				createConstraints();
				createTrigger();	
			}
		}
		
		tabTableFolder.setSelection(0);
	}
	
//	/**
//	 * 현재 선택된 tab을 리프레쉬합니다.
//	 * 
//	 * @param strSelectItemText TabItem text
//	 * @param strObjectName
//	 */
//	private void refershSelectObject(String strSelectItemText, String strObjectName) {
//
//		if (strSelectItemText.equalsIgnoreCase(OBJECT_TYPE.INDEXES.name())) {
//			refreshIndexes(true, strObjectName);
//		} else if (strSelectItemText.equalsIgnoreCase(OBJECT_TYPE.CONSTRAINTS.name())) {
//			refreshConstraints(true, strObjectName);
//		} else if (strSelectItemText.equalsIgnoreCase(OBJECT_TYPE.TRIGGERS.name())) {
//			refreshTrigger(true, strObjectName);
//
//		}
////		filterText();
//		
//		// google analytic
////		AnalyticCaller.track(ExplorerViewer.ID, strSelectItemText);
//	}
//	
	/**
	 * index 정보를 최신으로 갱신 합니다.
	 */
	public void refreshIndexes(boolean boolRefresh, String strObjectName) {
		indexComposite.refreshIndexes(getUserDB(), boolRefresh, strObjectName);
	}
	
	/**
	 * constraints 정보를 최신으로 갱신 합니다.
	 */
	public void refreshConstraints(boolean boolRefresh, String strObjectName) {
		constraintsComposite.refreshConstraints(getUserDB(), boolRefresh, strObjectName);
	}
	
	/**
	 * trigger 정보를 최신으로 갱신 합니다.
	 */
	public void refreshTrigger(boolean boolRefresh, String strObjectName) {
		triggerComposite.refreshTrigger(userDB, boolRefresh, strObjectName);
	}
	
	/**
	 * columm 정의
	 */
	private void createColumns() {
		tableColumnComposite = new TableColumnComposite(this, tabTableFolder, SWT.NONE);
		tableColumnComposite.setLayout(new GridLayout(1, false));
	}
	
	/**
	 * indexes 정의
	 */
	private void createIndexes() {
		indexComposite = new TadpoleIndexesComposite(getSite(), tabTableFolder, userDB);
		indexComposite.initAction();
	}
	
	/**
	 * indexes 정의
	 */
	private void createConstraints() {
		constraintsComposite = new TadpoleConstraintComposite(getSite(), tabTableFolder, userDB);
		constraintsComposite.initAction();
	}
	
	/**
	 * Trigger 정의
	 */
	private void createTrigger() {
		triggerComposite = new TadpoleTriggerComposite(getSite(), tabTableFolder, userDB);
		triggerComposite.initAction();
	}

	/**
	 * create Table menu
	 */
	private void createTableMenu() {
		if (getUserDB() == null) return;
		
		creatAction_Table = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleTableComposite_11);
		renameAction_Table= new ObjectRenameAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleTableComposite_18);
		tableRelationAction = new TableRelationAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleTableComposite_Relation);
		dropAction_Table = new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleTableComposite_12);
		refreshAction_Table = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().Refresh);

		// generation sample data
		generateSampleData = new GenerateSampleDataAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleTableComposite_14);
		
		generateDMLAction = new GenerateSQLDMLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "DML"); //$NON-NLS-1$
		selectStmtAction = new GenerateSQLSelectAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Select"); //$NON-NLS-1$
		insertStmtAction = new GenerateSQLInsertAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Insert"); //$NON-NLS-1$
		updateStmtAction = new GenerateSQLUpdateAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Update"); //$NON-NLS-1$
		deleteStmtAction = new GenerateSQLDeleteAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, "Delete"); //$NON-NLS-1$
		
		addTableColumnAction = new TableColumnCreateAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().AddColumn); //$NON-NLS-1$
		viewDDLAction = new GenerateViewDDLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().ViewDDL);
		tableDataEditorAction = new TableDataEditorAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES);
		
		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		if(getUserDB().getDBDefine() == DBDefine.HIVE_DEFAULT || 
				getUserDB().getDBDefine() == DBDefine.HIVE2_DEFAULT || 
						getUserDB().getDBDefine() == DBDefine.TAJO_DEFAULT) {
			if(PermissionChecker.isShow(getUserRoleType(), getUserDB())) {
				
				if(!isDDLLock()) {
					menuMgr.add(creatAction_Table);
					menuMgr.add(dropAction_Table);
					menuMgr.add(new Separator());
				}
			}	
			
			menuMgr.add(refreshAction_Table);
			menuMgr.add(new Separator());
			menuMgr.add(selectStmtAction);
		// others rdb
		} else {
			menuMgr.add(refreshAction_Table);
			menuMgr.add(new Separator());
			
			if(PermissionChecker.isShow(getUserRoleType(), getUserDB())) {
				if(!isDDLLock()) {
					menuMgr.add(creatAction_Table);
					menuMgr.add(new Separator());
					if (getUserDB().getDBDefine() != DBDefine.ALTIBASE_DEFAULT) { 
						menuMgr.add(renameAction_Table);
						
						if (getUserDB().getDBDefine() == DBDefine.MYSQL_DEFAULT ||
								getUserDB().getDBDefine() == DBDefine.MARIADB_DEFAULT) 
						{ 
							menuMgr.add(tableRelationAction);
						}
								
					}
					menuMgr.add(dropAction_Table);
					menuMgr.add(new Separator());
					if (getUserDB().getDBDefine() == DBDefine.MYSQL_DEFAULT || getUserDB().getDBDefine() == DBDefine.MARIADB_DEFAULT) {
						menuMgr.add(addTableColumnAction);
						menuMgr.add(new Separator());
					}
				}

				// 현재는 oracle db만 데이터 수정 모드..
				if (getUserDB().getDBDefine() == DBDefine.ORACLE_DEFAULT || getUserDB().getDBDefine() == DBDefine.TIBERO_DEFAULT) {
					menuMgr.add(generateSampleData);
					menuMgr.add(new Separator());
				}
			}	
			
			menuMgr.add(generateDMLAction);
			menuMgr.add(new Separator());
			menuMgr.add(selectStmtAction);
			
			if(PermissionChecker.isShow(getUserRoleType(), getUserDB())) {
				if(!isInsertLock()) menuMgr.add(insertStmtAction);
				if(!isUpdateLock()) menuMgr.add(updateStmtAction);
				if(!isDeleteLock()) menuMgr.add(deleteStmtAction);
				
				if (getUserDB().getDBDefine() != DBDefine.ALTIBASE_DEFAULT) { 
					menuMgr.add(new Separator());
					menuMgr.add(viewDDLAction);
				}
				if(!(isInsertLock() | isUpdateLock() | isDeleteLock())) {
					menuMgr.add(new Separator());
					menuMgr.add(tableDataEditorAction);
				}
			}
		}	// if rdb

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
							
							try {
								Throwable throwable = jobEvent.getResult().getException();
								Throwable cause = throwable.getCause().getCause();
								if(cause instanceof ClassNotFoundException) {
									// admin 이 드라이버를 업로드 해야한다.
									String msg = String.format(Messages.get().TadpoleTableComposite_driverMsg, userDB.getDbms_type(), throwable.getMessage());
									MessageDialog.openError(display.getActiveShell(), Messages.get().TadpoleTableComposite_Drivernotfound, msg);
									
								} else {
									Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, jobEvent.getResult().getMessage(), jobEvent.getResult().getException()); //$NON-NLS-1$
									ExceptionDetailsErrorDialog.openError(display.getActiveShell(), Messages.get().TadpoleTableComposite_3, Messages.get().ExplorerViewer_86, errStatus);
								}
							} catch(Exception e) {
								Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, jobEvent.getResult().getMessage(), jobEvent.getResult().getException()); //$NON-NLS-1$
								ExceptionDetailsErrorDialog.openError(display.getActiveShell(), Messages.get().TadpoleTableComposite_3, Messages.get().ExplorerViewer_86, errStatus);
							}
						}	// end else if
					}	// end run
				});	// end display.asyncExec
			}	// end done
			
		});	// end job
		
		job.setName(userDB.getDisplay_name());
		job.setUser(true);
		job.schedule();
	}
	
	/**
	 * initialize action
	 */
	public void initAction() {
		if(getUserDB() == null) return;
		
		creatAction_Table.setUserDB(getUserDB());
		renameAction_Table.setUserDB(getUserDB());
		tableRelationAction.setUserDB(getUserDB());
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
		
		tableColumnComposite.initAction();
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
		return tableColumnComposite.getTableColumnViewer();
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

		if(creatAction_Table != null) creatAction_Table.dispose();
		if(renameAction_Table != null) renameAction_Table.dispose();
		if(tableRelationAction != null) tableRelationAction.dispose();
		if(dropAction_Table != null) dropAction_Table.dispose();
		if(refreshAction_Table != null) refreshAction_Table.dispose();
		if(generateSampleData != null) generateSampleData.dispose();
		if(generateDMLAction != null) generateDMLAction.dispose();

		if(selectStmtAction != null) selectStmtAction.dispose();
		if(insertStmtAction != null) insertStmtAction.dispose();
		if(updateStmtAction != null) updateStmtAction.dispose();
		if(deleteStmtAction != null) deleteStmtAction.dispose();
		if(addTableColumnAction != null) addTableColumnAction.dispose();
		
		if(viewDDLAction != null) viewDDLAction.dispose();
		if(tableDataEditorAction != null) tableDataEditorAction.dispose();
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		if("".equals(strObjectName) || strObjectName == null) return;
		
		tableListViewer.getTable().setFocus();
		
		// find select object and viewer select
		for(int i=0; i<listTablesDAO.size(); i++) {
			TableDAO tableDao = (TableDAO)tableListViewer.getElementAt(i);
			if(tableDao != null && StringUtils.equalsIgnoreCase(strObjectName, tableDao.getName())) {
				tableListViewer.setSelection(new StructuredSelection(tableListViewer.getElementAt(i)), true);
				break;
			}
		}
	}
	
	/**
	 * select site
	 * @return
	 */
	public IWorkbenchPartSite getSite() {
		return site;
	}
	
	public ITableDecorationExtension getTableDecorationExtension() {
		return tableDecorationExtension;
	}

	public void refreshTableColumn() {
		tableColumnComposite.refreshTableColumn(getTableListViewer());
	}
	
	public void setSelectTableName(String tableName) {
		selectTableName = tableName;
	}

	public TadpoleIndexesComposite getIndexComposite() {
		return indexComposite;
	}
	
	public TadpoleConstraintComposite getConstraintsComposite() {
		return constraintsComposite;
	}

	public TadpoleTriggerComposite getTriggerComposite() {
		return triggerComposite;
	}
	
}
