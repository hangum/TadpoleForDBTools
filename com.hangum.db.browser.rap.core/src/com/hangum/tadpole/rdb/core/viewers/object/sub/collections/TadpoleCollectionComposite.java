package com.hangum.tadpole.rdb.core.viewers.object.sub.collections;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
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
import com.hangum.tadpole.mongodb.core.editors.main.MongoDBEditorInput;
import com.hangum.tadpole.mongodb.core.editors.main.MongoDBTableEditor;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.GenerateSQLInsertAction;
import com.hangum.tadpole.rdb.core.actions.object.GenerateSQLSelectAction;
import com.hangum.tadpole.rdb.core.actions.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.ObjectDeleteAction;
import com.hangum.tadpole.rdb.core.actions.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.actions.object.mongodb.ObjectMongodbReIndexAction;
import com.hangum.tadpole.rdb.core.actions.object.mongodb.ObjectMongodbRenameAction;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.TableComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.rdb.core.viewers.object.sub.table.DragListener;
import com.hangum.tadpole.rdb.core.viewers.object.sub.table.TableColumnLabelprovider;
import com.hangum.tadpole.rdb.core.viewers.object.sub.table.TableCommentEditorSupport;
import com.hangum.tadpole.rdb.core.viewers.object.sub.table.TableFilter;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Mongodb Collection composite
 * 
 * @author hangum
 *
 */
public class TadpoleCollectionComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleCollectionComposite.class);
	
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
	private GenerateSQLSelectAction insertStmtAction;
	private ObjectMongodbRenameAction renameColAction;
	private ObjectMongodbReIndexAction reIndexColAction;
	
	public TadpoleCollectionComposite(IWorkbenchPartSite partSite, final CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(partSite, tabFolderObject, userDB);
		
		createWidget(tabFolderObject);
	}
	
	private void createWidget(final CTabFolder tabFolderObject) {		
		CTabItem tbtmTable = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmTable.setText("Collection"); //$NON-NLS-1$
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

				if (null != is) {
					TableDAO tableDAO = (TableDAO) is.getFirstElement();

					MongoDBEditorInput input = new MongoDBEditorInput(tableDAO.getName(), userDB, showTableColumns);
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try {
						page.openEditor(input, MongoDBTableEditor.ID);
					} catch (PartInitException e) {
						logger.error("Load the table data", e); //$NON-NLS-1$

						Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
						ExceptionDetailsErrorDialog.openError(tabFolderObject.getShell(), "Error", Messages.ExplorerViewer_39, errStatus); //$NON-NLS-1$
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

						showTableColumns = MongoDBQuery.collectionColumn(userDB, selectTableName);

					} else
						showTableColumns = null;

					tableColumnViewer.setInput(showTableColumns);
					tableColumnViewer.refresh();

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
		tvColName.setEditingSupport(new TableCommentEditorSupport(tableListViewer, userDB, 0));

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
		tvColComment.setEditingSupport(new TableCommentEditorSupport(tableListViewer, userDB, 1));

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
		insertStmtAction = new GenerateSQLInsertAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), Define.DB_ACTION.TABLES, "Insert"); //$NON-NLS-1$

		renameColAction = new ObjectMongodbRenameAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), Define.DB_ACTION.TABLES, "Rename Collection");
		reIndexColAction = new ObjectMongodbReIndexAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), Define.DB_ACTION.TABLES, "ReIndex Collection");

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (userDB != null) {
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
		});

		org.eclipse.swt.widgets.Menu popupMenu = menuMgr.createContextMenu(tableListViewer.getTable());
		tableListViewer.getTable().setMenu(popupMenu);
		getSite().registerContextMenu(menuMgr, tableListViewer);
	}

	@Override
	public void setSearchText(String searchText) {
		tableFilter.setSearchText(searchText);
	}

	public void initAction() {
		creatAction_Table.setUserDB(getUserDB());
		deleteAction_Table.setUserDB(getUserDB());
		refreshAction_Table.setUserDB(getUserDB());
		insertStmtAction.setUserDB(getUserDB());
		renameColAction.setUserDB(getUserDB());
		reIndexColAction.setUserDB(getUserDB());
	}

	public TableViewer getCollectionListViewer() {
		return tableListViewer;
	}

	public void refreshTable(final UserDBDAO selectUserDb, boolean boolRefresh) {
		if(!boolRefresh) if(selectUserDb == null) return;

		this.userDB = selectUserDb;

		try {
			if (showTables != null) showTables.clear();
			else showTables = new ArrayList<TableDAO>();
			
			List<String> listCollection = MongoDBQuery.collectionList(userDB);
			for (String strColl : listCollection) {
				TableDAO dao = new TableDAO();
				dao.setName(strColl);

				showTables.add(dao);
			}

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
}