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
package com.hangum.tadpole.rdb.core.dialog.dml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;

/**
 * DMLGenerae Statement Dialog
 * 
 * @author nilriri
 *
 */
public class SelectObjectDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(SelectObjectDialog.class);

	private Map<String, String> map = new HashMap<String, String>(); //$NON-NLS-1$
	
	private List<Map<String, String>> extendsInfoList = new ArrayList<Map<String, String>>();

	private UserDBDAO userDB;
	private TableViewer tableViewer;
	private Label lblTableName;
	private Map<String,String> object_map = new HashMap<String,String>();

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public SelectObjectDialog(Shell parentShell, UserDBDAO userDB, Map<String,String> object_map) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);

		this.userDB = userDB;
		// 상세보기를 지원하는 오브젝트 타입.
		this.object_map.put("object_type", "'TABLE','VIEW','SYNONYM'");
		this.object_map.put("schema_name", object_map.get("OBJECT_OWNER"));
		this.object_map.put("object_name", object_map.get("OBJECT_NAME"));

		initData();
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().SelectObject);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;

		Composite compositeBody = new Composite(container, SWT.NONE);
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 2;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite compositeTable = new Composite(compositeBody, SWT.NONE);
		compositeTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		compositeTable.setLayout(new GridLayout(1, false));

		lblTableName = new Label(compositeTable, SWT.NONE);
		lblTableName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTableName.setText(object_map.get("object_name"));

		tableViewer = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableViewerColumn tvOwner = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tcOwner = tvOwner.getColumn();
		tcOwner.setWidth(90);
		tcOwner.setText("Owner");

		TableViewerColumn tvObject = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tcObject = tvObject.getColumn();
		tcObject.setWidth(150);
		tcObject.setText("Object Name");

		TableViewerColumn tvType = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tcType = tvType.getColumn();
		tcType.setWidth(100);
		tcType.setText("Object Type");

		GridData gd_textQuery = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_textQuery.minimumHeight = 120;

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new ObjectSelectorLabelProvider());

		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection is = (IStructuredSelection) event.getSelection();
				map = (HashMap<String, String>) is.getFirstElement();
				okPressed();
			}
		});
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection is = (IStructuredSelection) event.getSelection();
				map = (HashMap<String, String>) is.getFirstElement();
			}
		});

		tableViewer.setInput(extendsInfoList);
		tableViewer.refresh();
		
		tableViewer.getTable().setSelection(0);
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		tableViewer.getTable().setFocus();
		
		return container;
	}
	
	/**
	 * selected object
	 */
	private void selectObject() {
		
	}

	/**
	 * 
	 * @return
	 */
	public int getObjectCount() {
		return extendsInfoList == null ? 0 : extendsInfoList.size();
	}
	
	public Map<String, String> getSelectObject() {
		return map == null ? new HashMap<String, String>() : map;
	}

	private void initData() {
		try {
			List<HashMap> Objects = TadpoleObjectQuery.getObjectInfo(userDB, object_map);
			for (HashMap map : Objects) {
				extendsInfoList.add(map);
			}
			/*
			Objects = TadpoleObjectQuery.getObjectInfo(userDB, "VIEW", object_name);
			for (HashMap map : Objects) {
				extendsInfoList.add(map);
			}
			*/

			if (extendsInfoList.size() >= 1) {
				map = extendsInfoList.get(0);
			}

		} catch (Exception e) {
			logger.error("initialize data", e);
		}

	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, SWT.CANCEL,  CommonMessages.get().Cancel, false).addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent event) {
				cancelPressed();
			}			
		});
		Button ok = createButton(parent, SWT.PUSH, CommonMessages.get().OK, false);

		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {

				if (map.isEmpty()) {
					MessageDialog.openInformation(getShell(), CommonMessages.get().Information, Messages.get().SelectSearchObject);
					return;
				} else {
					okPressed();
				}

			}
		});
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	public class ObjectSelectorLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			Map<String, String> map = (Map<String, String>) element;

			switch (columnIndex) {
			case 0:
				return map.get("OBJECT_OWNER");
			case 1:
				return map.get("OBJECT_NAME");
			case 2:
				return map.get("OBJECT_TYPE");
			}

			return "*** not set column value ***"; //$NON-NLS-1$
		}

	}
}
