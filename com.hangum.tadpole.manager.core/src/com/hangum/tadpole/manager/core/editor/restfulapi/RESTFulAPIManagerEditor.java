/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.manager.core.editor.restfulapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.engine.query.dao.ResourceManagerDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.manager.core.editor.restfulapi.dao.RESTFulAPIDAO;

/**
 * ResufulAPI Manager 
 * 
 * @author hangum
 *
 */
public class RESTFulAPIManagerEditor extends EditorPart {
	public static final String ID = "com.hangum.tadpole.manager.core.editor.restfulapi";
	private static final Logger logger = Logger.getLogger(RESTFulAPIManagerEditor.class);
	
	private TreeViewer tvAPIList;
	private List<RESTFulAPIDAO> listRestfulDao = new ArrayList<>();

	public RESTFulAPIManagerEditor() {
	}
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite compositeHead = new Composite(parent, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(compositeHead, SWT.FLAT | SWT.RIGHT);
		
		ToolItem tltmRefrsh = new ToolItem(toolBar, SWT.NONE);
		tltmRefrsh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initUI();
			}
		});
		tltmRefrsh.setText("Refrsh");
		
		Composite compositeBody = new Composite(parent, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));
		
		tvAPIList = new TreeViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		Tree tree = tvAPIList.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(tvAPIList, SWT.NONE);
		TreeColumn trclmnUrl = treeViewerColumn.getColumn();
		trclmnUrl.setWidth(150);
		trclmnUrl.setText("URL");
		
		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(tvAPIList, SWT.NONE);
		TreeColumn trclmnName = treeViewerColumn_1.getColumn();
		trclmnName.setWidth(200);
		trclmnName.setText("NAME");
		
		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(tvAPIList, SWT.NONE);
		TreeColumn trclmnDescription = treeViewerColumn_2.getColumn();
		trclmnDescription.setWidth(300);
		trclmnDescription.setText("Description");
		
		tvAPIList.setContentProvider(new APIListContentProvider());
		tvAPIList.setLabelProvider(new APIListLabelProvider());
		
		initUI();

	}
	
	/**
	 * Initialize UI 
	 */
	private void initUI() {
		listRestfulDao.clear();
		
		try {
			List<ResourceManagerDAO> listAPIList = TadpoleSystem_UserDBResource.getRESTFulAPIList();
			Map<String, RESTFulAPIDAO> mapFirstApi = new HashMap<String, RESTFulAPIDAO>();
			
			// 첫 번째 인덱스 항목을 넣는다.
			for (ResourceManagerDAO resourceManagerDAO : listAPIList) {
				String strURL = StringUtils.removeStart(resourceManagerDAO.getRestapi_uri(), "/");
				
				// 트리의 마지막에 데이터를 넣어야 한다.
				String[] strArrySlash = StringUtils.split(strURL, '/');
				String strTreeKey = strArrySlash[0];
				if(!mapFirstApi.containsKey(strTreeKey)) {
					if(strArrySlash.length == 1) mapFirstApi.put(strTreeKey, new RESTFulAPIDAO(strTreeKey, resourceManagerDAO));
					else mapFirstApi.put(strTreeKey, new RESTFulAPIDAO(strTreeKey));
				}
			}
			
			
			// 두 번째 항목을 찾는다.
			for (ResourceManagerDAO resourceManagerDAO : listAPIList) {
				String strURL = StringUtils.removeStart(resourceManagerDAO.getRestapi_uri(), "/");
				
				// 트리의 마지막에 데이터를 넣어야 한다.
				String[] strArrySlash = StringUtils.split(strURL, '/');
				if(strArrySlash.length == 1) continue;
				
				String strTreeKey = strArrySlash[1];
				RESTFulAPIDAO rootDAO = mapFirstApi.get(strArrySlash[0]);						
				if(strArrySlash.length == 2) {
					List<RESTFulAPIDAO> listChildren = rootDAO.getListChildren();
					
					RESTFulAPIDAO existRestDAO = null;
					for (RESTFulAPIDAO restFulAPIDAO : listChildren) {
						if(strTreeKey.equals(restFulAPIDAO.getStrURL())) existRestDAO = restFulAPIDAO;
					}
					if(existRestDAO != null) listChildren.remove(existRestDAO);
					
					listChildren.add(new RESTFulAPIDAO(strTreeKey, resourceManagerDAO));
				} else {
					List<RESTFulAPIDAO> listChildren = rootDAO.getListChildren();
					boolean boolExist = false;
					for (RESTFulAPIDAO restFulAPIDAO : listChildren) {
						if(strTreeKey.equals(restFulAPIDAO.getStrURL())) boolExist = true;
					}
					
					if(!boolExist) {
						listChildren.add(new RESTFulAPIDAO(strTreeKey));	
					}
				}
			}
			
			// 세 번째 항목을 찾는다.
			for (ResourceManagerDAO resourceManagerDAO : listAPIList) {
				String strURL = StringUtils.removeStart(resourceManagerDAO.getRestapi_uri(), "/");
				
				// 트리의 마지막에 데이터를 넣어야 한다.
				String[] strArrySlash = StringUtils.split(strURL, '/');
					
				if(strArrySlash.length == 3) {
					String strTreeKey = strArrySlash[2];
					
					RESTFulAPIDAO rootDAO = mapFirstApi.get(strArrySlash[0]);
					String searchKey = strArrySlash[1];
					
					for (RESTFulAPIDAO restFulAPIDAO : rootDAO.getListChildren()) {
						if(searchKey.equals(restFulAPIDAO.getStrURL())) {
							restFulAPIDAO.getListChildren().add(new RESTFulAPIDAO(strTreeKey, resourceManagerDAO));
						}
					}
				}
			}

			listRestfulDao.addAll(mapFirstApi.values());
			tvAPIList.setInput(listRestfulDao);
			
		} catch (Exception e) {
			logger.error("RESTFulAPI List", e);
		}
		
		// google analytic
		AnalyticCaller.track(RESTFulAPIManagerEditor.ID);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		RESTFulAPIManagerEditorInput esqli = (RESTFulAPIManagerEditorInput) input;
		setPartName(esqli.getName());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {

	}

}

/**
 * content provider
 * 
 * @author hangum
 *
 */
class APIListContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		List<RESTFulAPIDAO> listRestfulDao = (List)inputElement;
		return listRestfulDao.toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		RESTFulAPIDAO restfulDao = (RESTFulAPIDAO)parentElement;
		
		return restfulDao.getListChildren().toArray();
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		RESTFulAPIDAO restfulDao = (RESTFulAPIDAO)element;
		
		return restfulDao.getListChildren().isEmpty()?false:true;
	}
	
}

/**
 * label provider
 * 
 * @author hangum
 *
 */
class APIListLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		RESTFulAPIDAO dao = (RESTFulAPIDAO)element;
		ResourceManagerDAO rmDAO = dao.getResourceManagerDao();

		if(columnIndex == 0) return "/" + dao.getStrURL();
		if(rmDAO != null) {
			switch(columnIndex) {
			case 1: return rmDAO.getName();
			case 2: return rmDAO.getDescription();
			}
		}
		
		return null;
	}
	
}