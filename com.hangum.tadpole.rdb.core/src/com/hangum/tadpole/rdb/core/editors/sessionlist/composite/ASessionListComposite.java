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
package com.hangum.tadpole.rdb.core.editors.sessionlist.composite;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.hangum.tadpole.rdb.core.Activator;
//import com.hangum.tadpole.rdb.core.editors.sessionlist.SessionListLabelProvider;
import com.hangum.tadpole.rdb.core.editors.sessionlist.SessionListTableCompare;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.sql.dao.mysql.SessionListDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.swtdesigner.ResourceManager;

/**
 * abstract session list composite
 * 
 * @author hangum
 *
 */
public abstract class ASessionListComposite extends Composite {
	
	protected UserDBDAO userDB;
	protected TableViewer tableViewerSessionList;
	protected ObjectComparator comparator;
	protected Text textQuery;

	public ASessionListComposite(Composite parent, UserDBDAO userDB) {
		super(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		setLayout(gridLayout);
		
		this.userDB = userDB;
		
		Composite compositeHead = new Composite(this, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeHead.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(compositeHead, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.setToolTipText("Refresh");
		tltmRefresh.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/refresh.png")); //$NON-NLS-1$
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initSessionListData();
			}
		});
		
		final ToolItem tltmKillProcess = new ToolItem(toolBar, SWT.NONE);
		tltmKillProcess.setToolTipText("Kill Process");
		tltmKillProcess.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/stop_process.png")); //$NON-NLS-1$
		tltmKillProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				killProcess();
			}
		});
		tltmKillProcess.setEnabled(false);
		
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeBody = new Composite(sashForm, SWT.NONE);
		compositeBody.setLayout(new GridLayout(1, false));
		
		tableViewerSessionList = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewerSessionList.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if(tableViewerSessionList.getSelection().isEmpty()) return;
				
				tltmKillProcess.setEnabled(true);
				
				StructuredSelection ss = (StructuredSelection)tableViewerSessionList.getSelection();
				SessionListDAO sl = (SessionListDAO)ss.getFirstElement();
				if(null != sl.getInfo()) {
					textQuery.setText(sl.getInfo());
					textQuery.setFocus();
				} else {
					textQuery.setText("");
				}
			}
		});
		Table tableSessionList = tableViewerSessionList.getTable();
		tableSessionList.setHeaderVisible(true);
		tableSessionList.setLinesVisible(true);
		tableSessionList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group compositeQuery = new Group(sashForm, SWT.NONE);
		compositeQuery.setLayout(new GridLayout(1, false));
		compositeQuery.setText("Query");
		
		textQuery = new Text(compositeQuery, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		comparator = new SessionListTableCompare();
		tableViewerSessionList.setSorter(comparator);
		
//		createColumn();
		
		tableViewerSessionList.setContentProvider(new ArrayContentProvider());
//		tableViewerSessionList.setLabelProvider(new SessionListLabelProvider());
		
		sashForm.setWeights(new int[] {7, 3});
		
		// init data
		initUI();
	}
	
	/**
	 * init ui 
	 * 
	 * 1) set tableview labelprovider
	 * 2) initsessionListData
	 * 
	 */
	protected abstract void initUI();
	
	/**
	 * initialize session list
	 */
	protected abstract void initSessionListData();
	
	/**
	 * kill session
	 */
	protected abstract void killProcess();
	
	/**
	 * creatae column
	 */
	protected abstract void createColumn();

	public UserDBDAO getUserDB() {
		return userDB;
	}

	public void setUserDB(UserDBDAO userDB) {
		this.userDB = userDB;
	}

	public TableViewer getTableViewerSessionList() {
		return tableViewerSessionList;
	}

	public ObjectComparator getComparator() {
		return comparator;
	}

	public Text getTextQuery() {
		return textQuery;
	}

	
	
}
