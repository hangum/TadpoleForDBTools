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
package com.hangum.tadpole.mongodb.core.dialogs.collection.index;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.engine.query.dao.mongodb.CollectionFieldDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.mongodb.core.Activator;
import com.hangum.tadpole.mongodb.core.Messages;
import com.hangum.tadpole.mongodb.core.query.MongoDBQuery;

/**
 * 몽고디비 인덱스 생성
 * 
 * @author hangum
 *
 */
public class NewIndexDialog extends Dialog {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(NewIndexDialog.class);
	
	protected UserDBDAO userDB;
	protected String initCollectionName;
	
	private Combo comboColName;
	
	private CTabFolder tabFolder;
	private Text textIndexName;
	private Button btnUnique;
	
	private TreeViewer treeColumnViewer;
	private List<CollectionFieldDAO> listCollFields;

	public NewIndexDialog(Shell parentShell, UserDBDAO userDB, String initCollectionName) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.userDB = userDB;
		this.initCollectionName = initCollectionName;
	}
	
	public NewIndexDialog(Shell parentShell, UserDBDAO userDB) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.userDB = userDB;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 4;
		gridLayout.horizontalSpacing = 4;
		gridLayout.marginHeight = 4;
		gridLayout.marginWidth = 4;
		gridLayout.numColumns = 2;
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Collection Name");
		
		comboColName = new Combo(container, SWT.READ_ONLY);
		comboColName.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectCollection();
			}
		});
		comboColName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblIndexName = new Label(container, SWT.NONE);
		lblIndexName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblIndexName.setText("Index Name");
		
		textIndexName = new Text(container, SWT.BORDER);
		textIndexName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		tabFolder = new CTabFolder(container, SWT.NONE);
		tabFolder.setBorderVisible(false);		
		tabFolder.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));		
		
		CTabItem tbtmUi = new CTabItem(tabFolder, SWT.NONE);
		tbtmUi.setText("UI");
		
		Composite compositeUI = new Composite(tabFolder, SWT.NONE);
		tbtmUi.setControl(compositeUI);
		compositeUI.setLayout(new GridLayout(1, false));
		
		treeColumnViewer = new TreeViewer(compositeUI, SWT.NONE | SWT.FULL_SELECTION);
		Tree tree = treeColumnViewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createTableMongoColumne();
		
		treeColumnViewer.setContentProvider(new MongoDBCollectionFieldsContentProvider());
		treeColumnViewer.setLabelProvider(new MongoDBCollectionFieldsLabelProvider());
		
		btnUnique = new Button(compositeUI, SWT.CHECK);
		btnUnique.setText("Unique");
		
		initUI();
		
		comboColName.setFocus();

		return container;
	}
	
	/**
	 * UI를 초기화합니다.
	 */
	private void initUI() {
		tabFolder.setSelection(0);
		
		try {
			List<TableDAO> listCollFields = MongoDBQuery.listCollection(userDB);
			for (TableDAO tableDao : listCollFields) comboColName.add(tableDao.getName());
			
			if(null != initCollectionName) {
				comboColName.setText(initCollectionName);
				selectCollection(initCollectionName);
			} else {
				comboColName.select(0);
				selectCollection(comboColName.getText());
			}
		} catch (Exception e) {
			logger.error("get collection list", e); //$NON-NLS-1$

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(tabFolder.getShell(), Messages.get().Error, e.getMessage(), errStatus); //$NON-NLS-1$
		}
	}
	
	/**
	 * 선택된 컬랙선의 필드정보를 트리에 출력합니다.
	 */
	private void selectCollection(String colName) {
		try {
			listCollFields = MongoDBQuery.collectionColumn(userDB, colName);
		} catch (Exception e) {
			logger.error("get collection column", e); //$NON-NLS-1$

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(tabFolder.getShell(), Messages.get().Error, e.getMessage(), errStatus); //$NON-NLS-1$
		}
		treeColumnViewer.setInput(listCollFields);
		treeColumnViewer.refresh();
	}

	/**
	 * 선택된 컬랙선의 필드정보를 트리에 출력합니다.
	 */
	private void selectCollection() {
		selectCollection(comboColName.getText());
	}
	
	/**
	 * mongodb collection column
	 * @param treeColumnViewer2
	 */
	private void createTableMongoColumne() {
		String[] columnName = {"Field", "Type", "Key", "Select Index"};  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		int[] columnSize = {200, 170, 40, 100};
		
		try {
			// reset column 
			for(int i=0; i<columnName.length; i++) {
				final TreeViewerColumn tableColumn = new TreeViewerColumn(treeColumnViewer, SWT.LEFT);
				tableColumn.getColumn().setText( columnName[i] );
				tableColumn.getColumn().setWidth( columnSize[i] );
				tableColumn.getColumn().setResizable(true);
				tableColumn.getColumn().setMoveable(false);
				if(i == 3) tableColumn.setEditingSupport(new FieldIndexEditorSupport(treeColumnViewer, userDB));
			}	// end for
			
		} catch(Exception e) { 
			logger.error("MongoDB Table Editor", e); //$NON-NLS-1$
		}	
	}
	
	@Override
	protected void okPressed() {
		
		if("".equals(textIndexName.getText().trim())) { //$NON-NLS-1$				
			textIndexName.setFocus();
			MessageDialog.openWarning(null, Messages.get().Warning, "Please enter Index name");
			return;
		}
		
		List<String> listFullIndex = new ArrayList<String>();
		List<CollectionFieldDAO> listColFieldDao = (List)treeColumnViewer.getInput();
		for (CollectionFieldDAO dao : listColFieldDao) {
			if(!dao.getNewIndex().equals("")) {
				listFullIndex.add(getMakeIndexString(dao.getField(), dao.getNewIndex()));
			}
			
			if(!dao.getChildren().isEmpty()) {
				findSelectIndex(dao.getChildren(), dao.getField(), listFullIndex);
			}
		}
		String fullIndex = "";
		for (int i=0; i<listFullIndex.size(); i++) {
			if(i == listFullIndex.size()-1) {
				fullIndex += listFullIndex.get(i);
			} else {
				fullIndex += listFullIndex.get(i) + ",";
			}
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("[select index]" + fullIndex);
		}
		
		try {
			MongoDBQuery.crateIndex(userDB, comboColName.getText().trim(), textIndexName.getText().trim(), "{" + fullIndex + "}",  btnUnique.getSelection());
		} catch (Exception e) {
			logger.error("mongodb create index", e); //$NON-NLS-1$
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, "Create index Exception", errStatus); //$NON-NLS-1$ //$NON-NLS-2$
			
			return;
		}			
		
		super.okPressed();
	}
	
	/**
	 * child element에 index를 입력했는지 검사합니다.
	 * 
	 * @param listColFieldDao
	 * @param field
	 * @param listFullIndex
	 */
	private void findSelectIndex(List<CollectionFieldDAO> listColFieldDao, String field, List<String> listFullIndex) {
		for (CollectionFieldDAO dao : listColFieldDao) {
			if(!dao.getNewIndex().equals("")) {
				listFullIndex.add(getMakeIndexString(field + "." + dao.getField(), dao.getNewIndex()));
			}
			
			if(!dao.getChildren().isEmpty()) {
				findSelectIndex(dao.getChildren(), field + "." + dao.getField(), listFullIndex);
			}
		}	
	}

	/**
	 * index string을 만듭니다.
	 * @param name
	 * @param strIndex
	 * @return
	 */
	private String getMakeIndexString(String name, String strIndex) {
		String retValue = "";
		for (int i=0; i<FieldIndexEditorSupport.arryIndexKey.length; i++) {
			if(strIndex.equals(FieldIndexEditorSupport.arryIndexKey[i])) {
				
				// Geospatial 이면..
				if(FieldIndexEditorSupport.arryIndexKey[3] == FieldIndexEditorSupport.arryIndexKey[i]) {
					retValue = "'" +  name + "': '" +  FieldIndexEditorSupport.arryIndexValue[i] + "'";
				} else {
					retValue = "'" +  name + "': " +  FieldIndexEditorSupport.arryIndexValue[i];					
				}
			}
		}
		
		return retValue;
	}
	
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {		
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Add, true); //$NON-NLS-1$
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Cancel, false); //$NON-NLS-1$
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("MongoDB Create Index Dialog"); //$NON-NLS-1$
	}
	
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(550, 576);
	}
}
