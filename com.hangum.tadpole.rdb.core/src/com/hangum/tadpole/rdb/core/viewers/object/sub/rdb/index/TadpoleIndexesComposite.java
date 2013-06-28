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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.index;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
import com.hangum.tadpole.dao.system.UserDBDAO;
import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.rdb.GenerateViewDDLAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.ObjectDeleteAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.DefaultComparator;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.system.permission.PermissionChecker;
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
	
	// index
	private TableViewer tableViewer;
	private ObjectComparator indexComparator;
	private List listIndexes;
	private IndexesViewFilter indexFilter;

	private ObjectCreatAction creatAction_Index;
	private ObjectDeleteAction deleteAction_Index;
	private ObjectRefreshAction refreshAction_Index;
	
	private GenerateViewDDLAction viewDDLAction;

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
		CTabItem tbtmIndex = new CTabItem(tabFolderObject, SWT.NONE);
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

		//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		tableViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTableList = tableViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		indexComparator = new DefaultComparator();
		tableViewer.setSorter(indexComparator);

		createIndexesColumn(tableViewer, indexComparator);

		tableViewer.setLabelProvider(new IndexesLabelProvicer());
		tableViewer.setContentProvider(new ArrayContentProvider());
//		tableViewer.setInput(listIndexes);

		indexFilter = new IndexesViewFilter();
		tableViewer.addFilter(indexFilter);

		createMenu();
		// index detail column
		

		sashForm.setWeights(new int[] { 1 });

	}

	/**
	 * create menu
	 * 
	 */
	private void createMenu() {
		creatAction_Index = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.INDEXES, "Index"); //$NON-NLS-1$
		deleteAction_Index = new ObjectDeleteAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.INDEXES, "Index"); //$NON-NLS-1$
		refreshAction_Index = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.INDEXES, "Index"); //$NON-NLS-1$
		
		viewDDLAction = new GenerateViewDDLAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.INDEXES, "View"); //$NON-NLS-1$

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if(PermissionChecker.isShow(getUserRoleType(), userDB)) {
					manager.add(creatAction_Index);
					manager.add(deleteAction_Index);
					
					manager.add(refreshAction_Index);
					
					manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
					manager.add(viewDDLAction);
				}
			}
		});

		Menu popupMenu = menuMgr.createContextMenu(tableViewer.getTable());
		tableViewer.getTable().setMenu(popupMenu);
		getSite().registerContextMenu(menuMgr, tableViewer);

	}

	/**
	 * init action
	 */
	public void initAction() {
		if (listIndexes != null) listIndexes.clear();
		tableViewer.setInput(listIndexes);
		tableViewer.refresh();

		creatAction_Index.setUserDB(getUserDB());
		deleteAction_Index.setUserDB(getUserDB());
		refreshAction_Index.setUserDB(getUserDB());
		
		viewDDLAction.setUserDB(getUserDB());
	}
	
	/**
	 * index 정보를 최신으로 갱신 합니다.
	 */
	public void refreshIndexes(final UserDBDAO userDB, boolean boolRefresh) {
		if(!boolRefresh) if(listIndexes != null) return;
		
		this.userDB = userDB;
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			listIndexes = sqlClient.queryForList("indexList", userDB.getDb()); //$NON-NLS-1$

			tableViewer.setInput(listIndexes);
			tableViewer.refresh();

		} catch (Exception e) {
			logger.error("index refresh", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.ExplorerViewer_1, errStatus); //$NON-NLS-1$
		}
	}

	/**
	 * filter
	 * 
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		indexFilter.setSearchText(textSearch);
		tableViewer.refresh();		
	}
	
	/**
	 * table viewer
	 * @return
	 */
	public TableViewer getTableViewer() {
		return tableViewer;
	}

	@Override
	public void setSearchText(String searchText) {
		indexFilter.setSearchText(searchText);
	}
}
