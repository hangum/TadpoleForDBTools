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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.part.ViewPart;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.widgets.TadpoleEditorWidget;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.system.SQLTemplateDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_SQLTemplate;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.session.manager.SessionManager;

/**
 * SQL template view
 * 
 * @author hangum
 *
 */
public class SQLTemplateView extends ViewPart {
	private static Logger logger = Logger.getLogger(SQLTemplateView.class);
	public static final String ID = "com.hangum.tadpole.rdb.core.view.sql.template";
	
	/**
	 * template type
	 */
	public enum SQL_TEMPLATE_TYPE {PUB, PRI};
	
	private TreeViewer tvSQLTemplate;
	private SQLTemplateFilter filter;
	
	private List<SQLTemplateGroupDAO> listGroup = new ArrayList<>();
	private SQLTemplateGroupDAO grpPublicDao;
	private SQLTemplateGroupDAO grpPrivateDao;
	
	private ToolItem tltmModify;
	private ToolItem tltmDelete;
	private Text textSearch;
	private TadpoleEditorWidget textSQL;

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
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initData();
			}
		});
		tltmRefresh.setImage(GlobalImageUtils.getRefresh());
		tltmRefresh.setToolTipText(Messages.get().Refresh);
		
		
		ToolItem tltmAdd = new ToolItem(toolBar, SWT.NONE);
		tltmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addTemplate(SQL_TEMPLATE_TYPE.PRI);
			}
		});
		tltmAdd.setImage(GlobalImageUtils.getAdd());
		tltmAdd.setToolTipText(Messages.get().Add);
		

		tltmModify = new ToolItem(toolBar, SWT.NONE);
		tltmModify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection ss = (StructuredSelection)tvSQLTemplate.getSelection();
				if(ss.getFirstElement() instanceof SQLTemplateDAO) {
					SQLTemplateDAO dao = (SQLTemplateDAO)ss.getFirstElement();
					SQLTemplateDialog dialog = new SQLTemplateDialog(getSite().getShell(), dao);
					if(Dialog.OK == dialog.open()) {
						tvSQLTemplate.refresh(dialog.getOldSqlTemplateDAO());
						textSQL.setText("");
					}
				}
			}
		});
		tltmModify.setImage(GlobalImageUtils.getModify());
		tltmModify.setToolTipText(Messages.get().Modified);
		tltmModify.setEnabled(false);
		
		tltmDelete = new ToolItem(toolBar, SWT.NONE);
		tltmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!MessageDialog.openConfirm(getSite().getShell(), Messages.get().Confirm, Messages.get().SQLTemplateView_del_equestion)) return;
				
				StructuredSelection ss = (StructuredSelection)tvSQLTemplate.getSelection();
				if(ss.getFirstElement() instanceof SQLTemplateDAO) {
					SQLTemplateDAO dao = (SQLTemplateDAO)ss.getFirstElement();
					try {
						TadpoleSystem_SQLTemplate.deleteSQLTemplate(dao);
						grpPrivateDao.getChildList().remove(dao);
						tvSQLTemplate.remove(dao);
						
						textSQL.setText("");
					} catch (Exception e1) {
						logger.error("Delete SQL template", e1);
					}
				}
			}
		});
		tltmDelete.setImage(GlobalImageUtils.getDelete());
		tltmDelete.setToolTipText(Messages.get().Delete);
		tltmDelete.setEnabled(false);
		
		// admin menu
		if(SessionManager.isSystemAdmin()) {
			new ToolItem(toolBar, SWT.SEPARATOR);
			ToolItem tltmAdminAdd = new ToolItem(toolBar, SWT.NONE);
			tltmAdminAdd.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					addTemplate(SQL_TEMPLATE_TYPE.PUB);
				}
			});
			tltmAdminAdd.setText(Messages.get().SQLTemplateView_Addpublictemplate);
			tltmAdminAdd.setToolTipText(Messages.get().SQLTemplateView_Addpublictemplate);
		}
		
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeBody = new Composite(sashForm, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 0;
		gl_compositeBody.horizontalSpacing = 0;
		gl_compositeBody.marginHeight = 0;
		gl_compositeBody.marginWidth = 0;
		compositeBody.setLayout(gl_compositeBody);
		
		textSearch = new Text(compositeBody, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				filterText();
			}
		});
		textSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		tvSQLTemplate = new TreeViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		tvSQLTemplate.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection ss = (StructuredSelection)event.getSelection();
				if(ss.getFirstElement() instanceof SQLTemplateDAO) {
					SQLTemplateDAO dao = (SQLTemplateDAO)ss.getFirstElement();
					if(SQL_TEMPLATE_TYPE.PUB.name().equals(dao.getCategory())) {
						if(SessionManager.isSystemAdmin()) {
							enableBtn(true);
						} else {
							enableBtn(false);
						}
					} else {
						enableBtn(true);
					}
					
					textSQL.setText(dao.getContent());
				} else {
					enableBtn(false);
					textSQL.setText("");
				}
			}
			
			private void enableBtn(boolean bool) {
				tltmModify.setEnabled(bool);
				tltmDelete.setEnabled(bool);
			}
		});
		tvSQLTemplate.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				StructuredSelection ss = (StructuredSelection)event.getSelection();
				if(ss.getFirstElement() instanceof SQLTemplateDAO) {
					SQLTemplateDAO dao = (SQLTemplateDAO)ss.getFirstElement();
					FindEditorAndWriteQueryUtil.run(dao.getContent());
				}
			}
		});
		Tree tree = tvSQLTemplate.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(tvSQLTemplate, SWT.NONE);
		TreeColumn trclmnUrl = treeViewerColumn.getColumn();
		trclmnUrl.setWidth(55);
		trclmnUrl.setText(Messages.get().GroupName);
		
		TreeViewerColumn tvcName = new TreeViewerColumn(tvSQLTemplate, SWT.NONE);
		TreeColumn trclmnDBName = tvcName.getColumn();
		trclmnDBName.setWidth(100);
		trclmnDBName.setText(Messages.get().Name);
		
		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(tvSQLTemplate, SWT.NONE);
		TreeColumn trclmnDescription = treeViewerColumn_2.getColumn();
		trclmnDescription.setWidth(100);
		trclmnDescription.setText(Messages.get().Description);
		
		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(tvSQLTemplate, SWT.NONE);
		TreeColumn trclmnName = treeViewerColumn_1.getColumn();
		trclmnName.setWidth(300);
		trclmnName.setText(Messages.get().SQL);
		
		Composite compositeSQL = new Composite(sashForm, SWT.NONE);
		GridLayout gl_compositeSQL = new GridLayout(1, false);
		gl_compositeSQL.verticalSpacing = 0;
		gl_compositeSQL.horizontalSpacing = 0;
		gl_compositeSQL.marginHeight = 0;
		gl_compositeSQL.marginWidth = 0;
		compositeSQL.setLayout(gl_compositeSQL);
		
		textSQL = new TadpoleEditorWidget(compositeSQL, SWT.BORDER, EditorDefine.EXT_DEFAULT, "", "");
		textSQL.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sashForm.setWeights(new int[] {7, 3});

		tvSQLTemplate.setContentProvider(new SQLTemplateContentprovider());
		tvSQLTemplate.setLabelProvider(new SQLTemplateLabelprovider());
		
//		Composite compositeTail = new Composite(parent, SWT.NONE);
//		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		compositeTail.setLayout(new GridLayout(1, false));
		
		initData();
		
		filter = new SQLTemplateFilter();
		tvSQLTemplate.addFilter(filter);
		
		AnalyticCaller.track(SQLTemplateView.ID);
	}
	
	/**
	 * add template
	 * 
	 * @param type
	 */
	private void addTemplate(SQL_TEMPLATE_TYPE type) {
		SQLTemplateDialog dialog = new SQLTemplateDialog(getSite().getShell(), type);
		if(Dialog.OK == dialog.open()) {
			SQLTemplateDAO addDao = dialog.getSqlTemplateDAO();
			
			if(SQL_TEMPLATE_TYPE.PRI == type) {
				grpPrivateDao.getChildList().add(addDao);
			} else {
				grpPublicDao.getChildList().add(addDao);
			}
			
			tvSQLTemplate.refresh();
			tvSQLTemplate.setSelection(new StructuredSelection(addDao), true);
		}

	}
	
	/**
	 * filter text
	 */
	private void filterText() {
		filter.setSearchText(textSearch.getText());
		tvSQLTemplate.refresh();
	}
	
	/*
	 * init data
	 */
	private void initData() {
		listGroup.clear();
		try {
			grpPublicDao = new SQLTemplateGroupDAO();
			grpPublicDao.setName(Messages.get().SQLTemplateView_PUBLIC_Person);
			grpPublicDao.setChildList(TadpoleSystem_SQLTemplate.listPublicSQLTemplate());
			listGroup.add(grpPublicDao);

			grpPrivateDao = new SQLTemplateGroupDAO();
			grpPrivateDao.setName(Messages.get().SQLTemplateView_Person);
			grpPrivateDao.setChildList(TadpoleSystem_SQLTemplate.listPrivateSQLTemplate());
			listGroup.add(grpPrivateDao);
			
			tvSQLTemplate.setInput(listGroup);
			tvSQLTemplate.expandAll();
			
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
class SQLTemplateContentprovider extends ArrayContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof SQLTemplateGroupDAO) {
			SQLTemplateGroupDAO gropDao = (SQLTemplateGroupDAO)parentElement;
			return gropDao.childList.toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof SQLTemplateGroupDAO) {
			SQLTemplateGroupDAO gropDao = (SQLTemplateGroupDAO)element;
			return gropDao.childList.size() > 0;
		}
		
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
		if(element instanceof SQLTemplateGroupDAO) {
			SQLTemplateGroupDAO dao = (SQLTemplateGroupDAO)element;
			
			if(columnIndex == 0) {
				return dao.getName();
			} else {
				return "";
			}
		} else {
			SQLTemplateDAO dao = (SQLTemplateDAO)element;
			
			switch(columnIndex) {
			case 1: return dao.getName();
			case 2: return dao.getDescription();
			case 3: return dao.getContent();
			}
		}
		
		return "";
	}
	
}