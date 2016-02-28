/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.sql.template;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.part.ViewPart;

import com.hangum.tadpole.engine.query.dao.system.TadpoleTemplateDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_SQLTemplate;

/**
 * SQL template view
 * 
 * @author hangum
 *
 */
public class SQLTemplateView extends ViewPart {
	private static Logger logger = Logger.getLogger(SQLTemplateView.class);
	public static final String ID = "com.hangum.tadpole.rdb.core.view.sql.template";
	private TreeViewer tvSQLTemplate;

	public SQLTemplateView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite compositeHead = new Composite(parent, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(compositeHead, SWT.FLAT | SWT.RIGHT);
		
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.setText("Refresh");
		
		ToolItem tltmAdd = new ToolItem(toolBar, SWT.NONE);
		tltmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SQLTemplateDialog dialog = new SQLTemplateDialog(getSite().getShell());
				dialog.open();
			}
		});
		tltmAdd.setText("Add");
		
		Composite compositeBody = new Composite(parent, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 0;
		gl_compositeBody.horizontalSpacing = 0;
		gl_compositeBody.marginHeight = 0;
		gl_compositeBody.marginWidth = 0;
		compositeBody.setLayout(gl_compositeBody);
		
		tvSQLTemplate = new TreeViewer(compositeBody, SWT.BORDER);
		Tree tree = tvSQLTemplate.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(tvSQLTemplate, SWT.NONE);
		TreeColumn trclmnUrl = treeViewerColumn.getColumn();
		trclmnUrl.setWidth(100);
		trclmnUrl.setText("Group name");
		
		TreeViewerColumn tvcName = new TreeViewerColumn(tvSQLTemplate, SWT.NONE);
		TreeColumn trclmnDBName = tvcName.getColumn();
		trclmnDBName.setWidth(100);
		trclmnDBName.setText("Name");
		
		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(tvSQLTemplate, SWT.NONE);
		TreeColumn trclmnName = treeViewerColumn_1.getColumn();
		trclmnName.setWidth(150);
		trclmnName.setText("SQL");
		
		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(tvSQLTemplate, SWT.NONE);
		TreeColumn trclmnDescription = treeViewerColumn_2.getColumn();
		trclmnDescription.setWidth(300);
		trclmnDescription.setText("Description");

		tvSQLTemplate.setContentProvider(new SQLTemplateContentprovider());
		tvSQLTemplate.setLabelProvider(new SQLTemplateLabelprovider());
		
//		Composite compositeTail = new Composite(parent, SWT.NONE);
//		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		compositeTail.setLayout(new GridLayout(1, false));
		
		initData();
	}
	
	/*
	 * init data
	 */
	private void initData() {
		try {
			List<TadpoleTemplateDAO> listSQLTemplate = TadpoleSystem_SQLTemplate.listSQLTemplate();
			tvSQLTemplate.setInput(listSQLTemplate);
			
		} catch(Exception e) {
			logger.error("list sql template", e);
		}
	}

	@Override
	public void setFocus() {
	}
}


/**
 * sql template content provider
 * @author hangum
 *
 */
class SQLTemplateContentprovider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		return false;
	}
	
}

/**
 * laver provider
 * @author hangum
 *
 */
class SQLTemplateLabelprovider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		TadpoleTemplateDAO dao = (TadpoleTemplateDAO)element;
		
		switch(columnIndex) {
		case 0: return dao.getGroup_name();
		case 1: return dao.getName();
		case 2: return dao.getContent();
		case 3: return dao.getDescription();
		}
		
		return "*** not set column ***";
	}
	
}