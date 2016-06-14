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
package com.hangum.tadpole.rdb.core.viewers.object.sub.mongodb.collections;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.NumberFormatUtils;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.mongodb.CollectionFieldDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.engine.sql.util.tables.TreeUtil;
import com.hangum.tadpole.mongodb.core.editors.main.MongoDBEditorInput;
import com.hangum.tadpole.mongodb.core.editors.main.MongoDBTableEditor;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.mongodb.ObjectMongodbCollCompactAction;
import com.hangum.tadpole.rdb.core.actions.object.mongodb.ObjectMongodbCollFindAndModifyAction;
import com.hangum.tadpole.rdb.core.actions.object.mongodb.ObjectMongodbCollStatesAction;
import com.hangum.tadpole.rdb.core.actions.object.mongodb.ObjectMongodbCollValidateAction;
import com.hangum.tadpole.rdb.core.actions.object.mongodb.ObjectMongodbGroupAction;
import com.hangum.tadpole.rdb.core.actions.object.mongodb.ObjectMongodbMapReduceAction;
import com.hangum.tadpole.rdb.core.actions.object.mongodb.ObjectMongodbReIndexAction;
import com.hangum.tadpole.rdb.core.actions.object.mongodb.ObjectMongodbRenameAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateSQLInsertAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateSQLSelectAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDropAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.MongoDBCollectionComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.TableDragListener;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.TableFilter;
import com.swtdesigner.ResourceManager;

/**
 * Mongodb Collection composite
 * 
 * @author hangum
 *
 */
public class TadpoleMongoDBCollectionComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleMongoDBCollectionComposite.class);
	
	/** select table name */
	private String selectTableName = ""; //$NON-NLS-1$

	// table info
	private TableViewer tableListViewer;
	private List<TableDAO> showTables;
	private ObjectComparator tableComparator;
	private TableFilter tableFilter;
	
	// column info
	private TreeViewer treeColumnViewer;
	private List<CollectionFieldDAO> showTableColumns;
	
	private ObjectCreatAction 			creatAction_Table;
	private ObjectDropAction 			deleteAction_Table;
	private ObjectMongodbCollFindAndModifyAction collFindAndModifyAction;
	
	private ObjectRefreshAction 		refreshAction_Table;
	
	private GenerateSQLSelectAction 	insertStmtAction;
	private ObjectMongodbRenameAction 	renameColAction;
	private ObjectMongodbReIndexAction 	reIndexColAction;
	private ObjectMongodbMapReduceAction mapReduceAction;
	private ObjectMongodbGroupAction 	groupAction;
	private ObjectMongodbCollStatesAction		collStatsAction;
	private ObjectMongodbCollCompactAction		collCompactAction;
	
	private ObjectMongodbCollValidateAction		collValidateAction;
	
	
	public TadpoleMongoDBCollectionComposite(IWorkbenchPartSite partSite, final CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(partSite, tabFolderObject, userDB);
		
		createWidget(tabFolderObject);
	}
	
	private void createWidget(final CTabFolder tabFolderObject) {		
		CTabItem tbtmTable = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmTable.setText(Messages.get().Collections);
		tbtmTable.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.COLLECTIONS.name());
		
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
		tableListViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tableListViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				if(PublicTadpoleDefine.YES_NO.NO.name().equals(userDB.getIs_showtables())) return;
				
				IStructuredSelection is = (IStructuredSelection) event.getSelection();

				if (null != is) {
					TableDAO tableDAO = (TableDAO) is.getFirstElement();

					MongoDBEditorInput input = new MongoDBEditorInput(tableDAO.getName(), userDB, showTableColumns);
					IWorkbenchPage page = getSite().getWorkbenchWindow().getActivePage();
					try {
						page.openEditor(input, MongoDBTableEditor.ID);
					} catch (PartInitException e) {
						logger.error("Load the table data", e); //$NON-NLS-1$

						Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
						ExceptionDetailsErrorDialog.openError(tabFolderObject.getShell(), Messages.get().Error, Messages.get().ExplorerViewer_39, errStatus); //$NON-NLS-1$
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

						showTableColumns = MongoDBQuery.collectionColumn(userDB, selectTableName);

					} else
						showTableColumns = null;

					treeColumnViewer.setInput(showTableColumns);
					treeColumnViewer.refresh();
					
					TreeUtil.packTree(treeColumnViewer.getTree());

				} catch (Exception e) {
					logger.error("get table column", e); //$NON-NLS-1$

					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(tabFolderObject.getShell(), Messages.get().Error, e.getMessage(), errStatus); //$NON-NLS-1$
				}
			}
		});

		Table tableTableList = tableListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		// sorter
		tableComparator = new MongoDBCollectionComparator();
		tableListViewer.setSorter(tableComparator);
		tableComparator.setColumn(0);

		TableViewerColumn tvColName = new TableViewerColumn(tableListViewer, SWT.NONE);
		TableColumn tbName = tvColName.getColumn();
		tbName.setWidth(150);
		tbName.setText("Name"); //$NON-NLS-1$
		tbName.addSelectionListener(getSelectionAdapter(tableListViewer, tableComparator, tbName, 0));
		tvColName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(Object element) {
				return ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/objectExplorer/tables.png"); //$NON-NLS-1$
			}
			
			@Override
			public String getText(Object element) {
				TableDAO table = (TableDAO) element;
				return table.getName();
			}
		});
//		tvColName.setEditingSupport(new TableCommentEditorSupport(tableListViewer, userDB, 0));

		TableViewerColumn tvColRows = new TableViewerColumn(tableListViewer, SWT.RIGHT);
		TableColumn tbComment = tvColRows.getColumn();
		tbComment.setWidth(200);
		tbComment.setText("Rows"); //$NON-NLS-1$
		tbComment.addSelectionListener(getSelectionAdapter(tableListViewer, tableComparator, tbComment, 1));
		tvColRows.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				TableDAO table = (TableDAO) element;
				return NumberFormatUtils.commaFormat(table.getRows());
			}
		});
//		tvColRows.setEditingSupport(new TableCommentEditorSupport(tableListViewer, userDB, 1));
		
		TableViewerColumn tvColSize = new TableViewerColumn(tableListViewer, SWT.RIGHT);
		TableColumn tbSize = tvColSize.getColumn();
		tbSize.setWidth(200);
		tbSize.setText("Size"); //$NON-NLS-1$
		tbSize.addSelectionListener(getSelectionAdapter(tableListViewer, tableComparator, tbComment, 1));
		tvColSize.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				TableDAO table = (TableDAO) element;
				return NumberFormatUtils.kbMbFormat(table.getSize());
			}
		});

		tableListViewer.setContentProvider(new ArrayContentProvider());
		tableListViewer.setInput(showTables);

		createMenu();

		// dnd 기능 추가
		Transfer[] transferTypes = new Transfer[]{TextTransfer.getInstance()};
		tableListViewer.addDragSupport(DND_OPERATIONS, transferTypes , new TableDragListener(userDB, tableListViewer));

		// filter
		tableFilter = new TableFilter(userDB);
		tableListViewer.addFilter(tableFilter);

		// columns
		treeColumnViewer = new TreeViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		Tree tableTableColumn = treeColumnViewer.getTree();
		tableTableColumn.setHeaderVisible(true);
		tableTableColumn.setLinesVisible(true);

		createTableMongoColumne();

		treeColumnViewer.setContentProvider(new MongoDBCollectionFieldsContentProvider());
		treeColumnViewer.setLabelProvider(new MongoDBCollectionFieldsLabelProvider());
		
		sashForm.setWeights(new int[] { 1, 1 });
	}
	
	/**
	 * mongodb collection column
	 * @param treeColumnViewer2
	 */
	private void createTableMongoColumne() {
		String[] columnName = {Messages.get().Field, Messages.get().TadpoleMongoDBCollectionComposite_2, Messages.get().TadpoleMongoDBCollectionComposite_3};
		int[] columnSize = {110, 100, 100};
		
		try {
			// reset column 
			for(int i=0; i<columnName.length; i++) {
				final TreeViewerColumn tableColumn = new TreeViewerColumn(treeColumnViewer, SWT.LEFT);
				tableColumn.getColumn().setText( columnName[i] );
				tableColumn.getColumn().setWidth( columnSize[i] );
				tableColumn.getColumn().setResizable(true);
				tableColumn.getColumn().setMoveable(false);
			}	// end for
			
		} catch(Exception e) { 
			logger.error("MongoDB Table Editor", e); //$NON-NLS-1$
		}	
	}

	/**
	 * create menu
	 */
	private void createMenu() {
		if(getUserDB() == null) return;
		
		creatAction_Table 	= new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleMongoDBCollectionComposite_5);
		deleteAction_Table 	= new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleMongoDBCollectionComposite_6);
		collFindAndModifyAction = new ObjectMongodbCollFindAndModifyAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleMongoDBCollectionComposite_7);
		
		refreshAction_Table = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().Refresh);
		insertStmtAction 	= new GenerateSQLInsertAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleMongoDBCollectionComposite_9);

		renameColAction 	= new ObjectMongodbRenameAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleMongoDBCollectionComposite_10);
		reIndexColAction 	= new ObjectMongodbReIndexAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleMongoDBCollectionComposite_11);
		mapReduceAction 	= new ObjectMongodbMapReduceAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleMongoDBCollectionComposite_12);
		groupAction			= new ObjectMongodbGroupAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleMongoDBCollectionComposite_13);
		
		collStatsAction 	= new ObjectMongodbCollStatesAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleMongoDBCollectionComposite_14);
		collCompactAction   = new ObjectMongodbCollCompactAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleMongoDBCollectionComposite_15);
		
		collValidateAction = new ObjectMongodbCollValidateAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.TABLES, Messages.get().TadpoleMongoDBCollectionComposite_16);
		
		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.add(creatAction_Table);
		if(PermissionChecker.isShow(getUserRoleType(), getUserDB())) {
			menuMgr.add(deleteAction_Table);
			menuMgr.add(collFindAndModifyAction);
			menuMgr.add(collValidateAction);
		}
		menuMgr.add(new Separator());
		menuMgr.add(refreshAction_Table);

		menuMgr.add(new Separator());
		menuMgr.add(insertStmtAction);
		menuMgr.add(collStatsAction);
		
		if(PermissionChecker.isShow(getUserRoleType(), getUserDB())) {
			menuMgr.add(new Separator());
			menuMgr.add(collCompactAction);
			menuMgr.add(renameColAction);
			menuMgr.add(reIndexColAction);
		}
		
		menuMgr.add(new Separator());
		menuMgr.add(mapReduceAction);
		menuMgr.add(groupAction);

		tableListViewer.getTable().setMenu(menuMgr.createContextMenu(tableListViewer.getTable()));
		getSite().registerContextMenu(menuMgr, tableListViewer);
	}

	@Override
	public void setSearchText(String searchText) {
		tableFilter.setSearchText(searchText);
	}

	public void initAction() {
		if(getUserDB() == null) return;
		
		creatAction_Table.setUserDB(getUserDB());
		deleteAction_Table.setUserDB(getUserDB());
		collFindAndModifyAction.setUserDB(getUserDB());
		collValidateAction.setUserDB(getUserDB());
		
		refreshAction_Table.setUserDB(getUserDB());
		insertStmtAction.setUserDB(getUserDB());
		
		collStatsAction.setUserDB(getUserDB());
		
		collCompactAction.setUserDB(getUserDB());
		renameColAction.setUserDB(getUserDB());
		reIndexColAction.setUserDB(getUserDB());
		mapReduceAction.setUserDB(getUserDB());
		groupAction.setUserDB(getUserDB());
	}

	public TableViewer getCollectionListViewer() {
		return tableListViewer;
	}

	/**
	 * refresh mongodb collection
	 * 
	 * @param selectUserDb
	 * @param boolRefresh
	 */
	public void refreshTable(final UserDBDAO selectUserDb, boolean boolRefresh) {
		if(!boolRefresh) if(selectUserDb == null) return;
		this.userDB = selectUserDb;
		
		// 테이블 등록시 테이블 목록 보이지 않는 옵션을 선택했는지.
		if(PublicTadpoleDefine.YES_NO.NO.name().equals(this.userDB.getIs_showtables())) {
			showTables.add(new TableDAO(Messages.get().TadpoleMongoDBCollectionComposite_4, ""));  //$NON-NLS-1$
			
			tableListViewer.setInput(showTables);
			tableListViewer.refresh();
			TableUtil.packTable(tableListViewer.getTable());
			return;
		}

		final String jobName = Messages.get().TadpoleTableComposite_17;
		Job job = new Job(Messages.get().MainEditor_45) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(jobName, IProgressMonitor.UNKNOWN);
				
				try {
					showTables= MongoDBQuery.listCollection(userDB);
					
				} catch(Exception e) {
					logger.error("Table Referesh", e); //$NON-NLS-1$
					
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
							TableUtil.packTable(tableListViewer.getTable());
						} else {
							if (showTables != null) showTables.clear();
							tableListViewer.setInput(showTables);
							tableListViewer.refresh();
							TableUtil.packTable(tableListViewer.getTable());

							Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, jobEvent.getResult().getMessage(), jobEvent.getResult().getException()); //$NON-NLS-1$
							ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().ExplorerViewer_86, errStatus); //$NON-NLS-1$
						}
					}
				});	// end display.asyncExec
			}	// end done
			
		});	// end job
		
		job.setName(userDB.getDisplay_name());
		job.setUser(true);
		job.schedule();
	}
	
	public TableViewer getTableListViewer() {
		return tableListViewer;
	}

	public void filter(String textSearch) {
		tableFilter.setSearchText(textSearch);
		tableListViewer.refresh();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		if(creatAction_Table != null) creatAction_Table.dispose();
		if(deleteAction_Table != null) deleteAction_Table.dispose();
		if(collFindAndModifyAction != null) collFindAndModifyAction.dispose();
		if(collValidateAction != null) collValidateAction.dispose();
		
		if(refreshAction_Table != null) refreshAction_Table.dispose();
		if(insertStmtAction != null) insertStmtAction.dispose();
		
		if(collStatsAction != null) collStatsAction.dispose();
		if(collCompactAction != null) collCompactAction.dispose();
		if(renameColAction != null) renameColAction.dispose();
		if(reIndexColAction != null) reIndexColAction.dispose();
		if(mapReduceAction != null) mapReduceAction.dispose();
		if(groupAction != null) groupAction.dispose();
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		// TODO Auto-generated method stub
		
	}
}
