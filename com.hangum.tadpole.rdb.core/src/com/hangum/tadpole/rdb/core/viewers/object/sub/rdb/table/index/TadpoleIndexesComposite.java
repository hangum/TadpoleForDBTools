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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.permission.PermissionChecker;
import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDropAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.DefaultComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.IndexColumnComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * RDB indexes composite
 * 
 * @author hangum
 *
 */
public class TadpoleIndexesComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleIndexesComposite.class);
	
	private CTabItem tbtmIndex;
	private TableDAO tableDao;
	private String selectIndexName = ""; //$NON-NLS-1$

	// index
	private TableViewer indexTableViewer;
	private ObjectComparator indexComparator;
	private List<InformationSchemaDAO> listIndexes = new ArrayList<InformationSchemaDAO>();
	private IndexesViewFilter indexFilter;

	// column info
	private TableViewer indexColumnViewer;
	private ObjectComparator indexColumnComparator;
	private List<InformationSchemaDAO> showIndexColumns = new ArrayList<InformationSchemaDAO>();

	private ObjectCreatAction creatAction_Index;
	private ObjectDropAction dropAction_Index;
	private ObjectRefreshAction refreshAction_Index;
//	private GenerateViewDDLAction viewDDLAction;

	/**
	 * indexes info
	 * 
	 * @param site
	 * @param tabFolderObject
	 * @param userDB
	 */
	public TadpoleIndexesComposite(IWorkbenchPartSite site, CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(site, tabFolderObject, userDB);
		createWidget(tabFolderObject);
	}
	
	private void createWidget(final CTabFolder tabFolderObject) {
		tbtmIndex = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmIndex.setText(Messages.get().Indexes);
		tbtmIndex.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.INDEXES.name());

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

		//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		indexTableViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
	
		indexTableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {

				// 인덱스 디테일한 정보를 확인할동안은 블럭으로 만들어 놓습니다.
				if (userDB.getDBDefine() == DBDefine.SQLite_DEFAULT 
						//userDB.getDBDefine() == DBDefine.CUBRID_DEFAULT 
						//userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT
				)  return;
				
				if(PublicTadpoleDefine.YES_NO.NO.name().equals(userDB.getIs_showtables())) return;
				
				// 테이블의 컬럼 목록을 출력합니다.
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					Object tableDAO = is.getFirstElement();

					if (tableDAO != null) {
						InformationSchemaDAO index = (InformationSchemaDAO) tableDAO;

						if (selectIndexName.equals(index.getINDEX_NAME())) return;
						selectIndexName = index.getINDEX_NAME();

						SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
						HashMap<String, String>paramMap = new HashMap<String, String>();
						paramMap.put("table_schema", index.getTABLE_SCHEMA()); //$NON-NLS-1$
						paramMap.put("table_name", index.getTABLE_NAME()); //$NON-NLS-1$
						paramMap.put("index_name", index.getINDEX_NAME()); //$NON-NLS-1$
						
						showIndexColumns = sqlClient.queryForList("indexDetailList", paramMap); //$NON-NLS-1$
					} else {
						showIndexColumns = new ArrayList<InformationSchemaDAO>();
					}

					indexColumnViewer.setInput(showIndexColumns);
					indexColumnViewer.refresh();
					TableUtil.packTable(indexColumnViewer.getTable());

				} catch (Exception e) {
					logger.error("get table column", e); //$NON-NLS-1$

					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(tabFolderObject.getShell(), Messages.get().Error, e.getMessage(), errStatus); //$NON-NLS-1$
				}
			}
		});		
		
		
		Table tableTableList = indexTableViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		indexComparator = new DefaultComparator();
		indexTableViewer.setSorter(indexComparator);

		createIndexesColumn(indexTableViewer, indexComparator);

		indexTableViewer.setLabelProvider(new IndexesLabelProvicer());
		indexTableViewer.setContentProvider(new ArrayContentProvider());

		indexFilter = new IndexesViewFilter();
		indexTableViewer.addFilter(indexFilter);
		
		// columns
		indexColumnViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTableColumn = indexColumnViewer.getTable();
		tableTableColumn.setHeaderVisible(true);
		tableTableColumn.setLinesVisible(true);
		
		indexColumnComparator = new IndexColumnComparator();
		indexColumnViewer.setSorter(indexColumnComparator);
		indexColumnComparator.setColumn(0);

		createIndexColumne(indexColumnViewer);

		indexColumnViewer.setContentProvider(new ArrayContentProvider());
		indexColumnViewer.setLabelProvider(new IndexColumnLabelprovider());
		
		createMenu();
		// index detail column
		
		sashForm.setWeights(new int[] { 1, 1 });

	}

	/**
	 * selection adapter
	 * 
	 * @param indexColumn
	 * @param index
	 * @return
	 */
	private SelectionAdapter getSelectionAdapter(final TableViewerColumn indexColumn, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				indexColumnComparator.setColumn(index);
				
				indexColumnViewer.getTable().setSortDirection(indexColumnComparator.getDirection());
				indexColumnViewer.getTable().setSortColumn(indexColumn.getColumn());
				
				indexColumnViewer.refresh();
			}
		};
		
		return selectionAdapter;
	}

	/**
	 * index column list
	 */
	protected void createIndexColumne(final TableViewer tv) {
		String[] name = {Messages.get().SEQ, Messages.get().Column, Messages.get().Order};
		int[] size = {60, 300, 50};

		for (int i=0; i<name.length; i++) {
			TableViewerColumn indexColumn = new TableViewerColumn(tv, SWT.LEFT);
			indexColumn.getColumn().setText(name[i]);
			indexColumn.getColumn().setWidth(size[i]);
			indexColumn.getColumn().addSelectionListener(getSelectionAdapter(indexColumn, i));
		}
	}
	/**
	 * create menu
	 * 
	 */
	private void createMenu() {
		if(getUserDB() == null) return;
		
		creatAction_Index = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.INDEXES, Messages.get().TadpoleIndexesComposite_1);
		dropAction_Index = new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.INDEXES, Messages.get().TadpoleIndexesComposite_2);
		refreshAction_Index = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.INDEXES, Messages.get().Refresh);
		
//		viewDDLAction = new GenerateViewDDLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.INDEXES, Messages.get().TadpoleIndexesComposite_7);

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		if(PermissionChecker.isShow(getUserRoleType(), getUserDB())) {
			if(!isDDLLock()) {
				menuMgr.add(creatAction_Index);
				menuMgr.add(dropAction_Index);
				menuMgr.add(new Separator());
			}
			menuMgr.add(refreshAction_Index);
//			menuMgr.add(new Separator());
//			menuMgr.add(viewDDLAction);
		}

		indexTableViewer.getTable().setMenu(menuMgr.createContextMenu(indexTableViewer.getTable()));
		getSite().registerContextMenu(menuMgr, indexTableViewer);
	}

	/**
	 * init action
	 */
	public void initAction() {
		if (listIndexes != null) listIndexes.clear();
		indexTableViewer.setInput(listIndexes);
		indexTableViewer.refresh();

		if(getUserDB() == null) return;
		creatAction_Index.setUserDB(getUserDB());
		dropAction_Index.setUserDB(getUserDB());
		refreshAction_Index.setUserDB(getUserDB());
//		viewDDLAction.setUserDB(getUserDB());
	}
	
	/**
	 * refresh index
	 * 
	 * @param userDB
	 * @param tableDao
	 */
	public void setTable(UserDBDAO userDB, TableDAO tableDao) {
		this.userDB = userDB;
		this.tableDao = tableDao;
		
		refreshIndexes(userDB, true, "");
		
		showIndexColumns.clear();
		indexColumnViewer.setInput(showIndexColumns);
		indexColumnViewer.refresh();
	}
	
	/**
	 * index 정보를 최신으로 갱신 합니다.
	 * @param strObjectName 
	 */
	public void refreshIndexes(final UserDBDAO userDB, boolean boolRefresh, String strObjectName) {
		if(!boolRefresh) if(listIndexes != null) return;
		
		this.userDB = userDB;
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			HashMap<String, String> map = new HashMap<String, String>();
			 if(userDB.getDBDefine() == DBDefine.ALTIBASE_DEFAULT) {
				 map.put("user_name", 	StringUtils.substringBefore(tableDao.getName(), ".")); //$NON-NLS-1$
				 map.put("table_name", 	StringUtils.substringAfter(tableDao.getName(), ".")); //$NON-NLS-1$
			 } else {
				 map.put("table_schema", userDB.getDb());
				 map.put("table_name", tableDao.getName());	 
			 }
			listIndexes = sqlClient.queryForList("indexList", map); //$NON-NLS-1$

			indexTableViewer.setInput(listIndexes);
			indexTableViewer.refresh();
			
			TableUtil.packTable(indexTableViewer.getTable());

			// select tabitem
			getTabFolderObject().setSelection(tbtmIndex);
			
			selectDataOfTable(strObjectName);
		} catch (Exception e) {
			logger.error("index refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().ExplorerViewer_1, errStatus); //$NON-NLS-1$
		}
	}

	/**
	 * filter
	 * 
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		indexFilter.setSearchText(textSearch);
		indexTableViewer.refresh();		
	}
	
	/**
	 * table viewer
	 * @return
	 */
	public TableViewer getTableViewer() {
		return indexTableViewer;
	}

	@Override
	public void setSearchText(String searchText) {
		indexFilter.setSearchText(searchText);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		if(creatAction_Index != null) creatAction_Index.dispose();
		if(dropAction_Index != null) dropAction_Index.dispose();
		if(refreshAction_Index != null) refreshAction_Index.dispose();
//		if(viewDDLAction != null) viewDDLAction.dispose();
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		if("".equals(strObjectName) || strObjectName == null) return;
		
		getTableViewer().getTable().setFocus();
		
		// find select object and viewer select
		for(int i=0; i<listIndexes.size(); i++) {
			InformationSchemaDAO tableDao = (InformationSchemaDAO)getTableViewer().getElementAt(i);
			if(StringUtils.equalsIgnoreCase(strObjectName, tableDao.getINDEX_NAME())) {
				getTableViewer().setSelection(new StructuredSelection(getTableViewer().getElementAt(i)), true);
				break;
			}
		}		
	}
	
}
