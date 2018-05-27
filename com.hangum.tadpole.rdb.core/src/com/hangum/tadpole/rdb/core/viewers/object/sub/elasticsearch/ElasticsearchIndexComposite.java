/*******************************************************************************
 * Copyright (c) 2018 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     nilrir - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object.sub.elasticsearch;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.query.dao.elasticsearch.ElastIndexDAO;
import com.hangum.tadpole.engine.query.dao.elasticsearch.ElasticColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.DefaultComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.tadpole.common.define.core.define.PublicTadpoleDefine;

/**
 * Elasticsearch index composite
 * 
 * @author hangum
 * 
 */
public class ElasticsearchIndexComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ElasticsearchIndexComposite.class);

	private CTabItem tbtmType;

	// table info
	private TableViewer indexListViewer;
	private ObjectComparator indexComparator;
	private List<ElastIndexDAO> showElasticsearch = new ArrayList<ElastIndexDAO>();
	private ElasticsearchFilter indexFilter;
	
	// column
	private TableViewer viewColumnViewer;

	private AbstractObjectAction refreshAction_Index;

	/**
	 * Create the composite.
	 * 
	 * @param partSite
	 * @param parent
	 * @param userDB
	 */
	public ElasticsearchIndexComposite(IWorkbenchPartSite partSite, final CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(partSite, tabFolderObject, userDB);

		createWidget(tabFolderObject);
	}

	private void createWidget(final CTabFolder tabFolderObject) {
		tbtmType = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmType.setText("Index");
		tbtmType.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.ELASTICSEARCH_INDEX.name());

		Composite compositeTables = new Composite(tabFolderObject, SWT.NONE);
		tbtmType.setControl(compositeTables);
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

		// SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		indexListViewer = new TableViewer(sashForm, /* SWT.VIRTUAL | */ SWT.BORDER | SWT.FULL_SELECTION);
		indexListViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				
				// 테이블의 컬럼 목록을 출력합니다.
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					Object indexDAO = is.getFirstElement();

					if (!is.isEmpty()) {
						ElastIndexDAO table = (ElastIndexDAO) indexDAO;
						List<ElasticColumnDAO> listColumnDao = new ArrayList();//ElasticSearchQuery.listIndexColumn(userDB, table.getIndex());
						
						viewColumnViewer.setInput(listColumnDao);
					}

				} catch (Exception e) {
					logger.error("get table column", e); //$NON-NLS-1$

					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(tabFolderObject.getShell(),CommonMessages.get().Error, e.getMessage(), errStatus); //$NON-NLS-1$
				}
			}
		});
		indexListViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					if (null != is) {
						ElastIndexDAO indexDAO = (ElastIndexDAO) is.getFirstElement();
						FindEditorAndWriteQueryUtil.run(userDB, String.format("GET /%s/_search;",  indexDAO.getIndex()), PublicTadpoleDefine.OBJECT_TYPE.ELASTICSEARCH_INDEX);
					}
				} catch (Exception e) {
					logger.error("create sequence", e);
				}
			}
		});

		Table tableTableList = indexListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		indexComparator = new DefaultComparator();
		indexListViewer.setSorter(indexComparator);
		indexComparator.setColumn(0);
		
		createListColumns();

		indexListViewer.setLabelProvider(new ElasticsearchLabelPrivider());
		indexListViewer.setContentProvider(ArrayContentProvider.getInstance());
		indexListViewer.setInput(showElasticsearch);
		indexListViewer.refresh();

		indexFilter = new ElasticsearchFilter(userDB);
		indexListViewer.addFilter(indexFilter);
		
		// column detail
		viewColumnViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		viewColumnViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection is = (IStructuredSelection) event.getSelection();

				if (null != is) {
					TableColumnDAO tableDAO = (TableColumnDAO) is.getFirstElement();
					FindEditorAndWriteQueryUtil.runAtPosition(StringUtils.trim(tableDAO.getField()));
				}
			}
		});
		Table tableTableColumn = viewColumnViewer.getTable();
		tableTableColumn.setHeaderVisible(true);
		tableTableColumn.setLinesVisible(true);
		
//		tableColumnComparator = new TableColumnComparator();
//		viewColumnViewer.setSorter(tableColumnComparator);

		createViewColumne();

		viewColumnViewer.setContentProvider(ArrayContentProvider.getInstance());
		viewColumnViewer.setLabelProvider(new ElasticsearchColumnLabelPrivider());
		
		// create menu
		createElasicsearchMenu();
	}
	
	/**
	 * view column
	 */
	protected void createViewColumne() {
		String[] name 		= {CommonMessages.get().Name, Messages.get().Type, Messages.get().Field};
		int[] size 			= {150, 120, 300};

		ColumnViewerToolTipSupport.enableFor(viewColumnViewer);
		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(viewColumnViewer, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
			tableColumn.getColumn().setMoveable(true);
		}
	}

	/**
	 * create table column
	 */
	private void createListColumns() {
		String[] name = {"health", "status", "index", "uuid", "pri", "rep", "docs.count", "docs.deleted", "store.size", "pri.store.size"};
		int[] size = {50, 50, 200, 150, 50, 50, 70, 70, 70, 70};

		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(indexListViewer, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
			tableColumn.getColumn().addSelectionListener(getSelectionAdapter(indexListViewer, indexComparator, tableColumn.getColumn(), i));
		}
	}

	/**
	 * create Table menu
	 */
	private void createElasicsearchMenu() {
		if(getUserDB() == null) return;
		
		refreshAction_Index = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.SEQUENCE, CommonMessages.get().Refresh); //$NON-NLS-1$

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu", "Sequence"); //$NON-NLS-1$ //$NON-NLS-2$
		menuMgr.add(refreshAction_Index);
//			
//		menuMgr.add(new Separator());

		indexListViewer.getTable().setMenu(menuMgr.createContextMenu(indexListViewer.getTable()));
		getSite().registerContextMenu(menuMgr, indexListViewer);
	}

	/**
	 * 정보를 최신으로 리프레쉬합니다.
	 * @param strObjectName 
	 */
	public void refreshIndex(final UserDBDAO selectUserDb, final boolean boolRefresh, final String strObjectName) {
		if (!boolRefresh) if (!showElasticsearch.isEmpty()) return;
		this.userDB = selectUserDb;
		
		try {
			indexListViewer.setInput(new ArrayList());
			
		} catch(Exception e) {
			logger.error("refresh elasticsearch index", e);
		}
	}

	/**
	 * initialize action
	 */
	public void initAction() {
		if(getUserDB() == null) return; 
		
		refreshAction_Index.setUserDB(getUserDB());
	}

	/**
	 * get sequenceViewer
	 * 
	 * @return
	 */
	public TableViewer getTableviewer() {
		return indexListViewer;
	}

	/**
	 * initialize filter text
	 * 
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		indexFilter.setSearchText(textSearch);
		indexListViewer.refresh();
		
		TableUtil.packTable(indexListViewer.getTable());
	}

	@Override
	public void dispose() {
		super.dispose();	
//		if(creatAction_Sequence != null) creatAction_Sequence.dispose();
//		if(dropAction_Sequence != null) dropAction_Sequence.dispose();
		if(refreshAction_Index != null) refreshAction_Index.dispose();
//		if(viewDDLAction != null) viewDDLAction.dispose();
		//if(executeAction != null) executeAction.dispose();
	}

	@Override
	public void setSearchText(String searchText) {
		indexFilter.setSearchText(searchText);
	}

	@Override
	public void selectDataOfTable(String strObjectName) {

	}

	public void clearList() {
		if(showElasticsearch != null) this.showElasticsearch.clear();	
	}
}
