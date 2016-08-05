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
package com.hangum.tadpole.rdb.core.dialog.dml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;
import org.eclipse.swt.custom.SashForm;

/**
 * DMLGenerae Statement Dialog
 * 
 * @author nilriri
 *
 */
public class TableInformationDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(TableInformationDialog.class);
	private boolean isEditorAdd = false;

	private UserDBDAO userDB;
	private TableDAO tableDAO;
	private TableViewer tableViewer;
	private TableViewer tableViewer_ext;
	private Text textTBNameCmt;
	private Label lblTableName;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public TableInformationDialog(Shell parentShell, boolean isEditorAdd,
			UserDBDAO userDB, TableDAO tableDAO) {
		super(parentShell);
		setBlockOnOpen(isEditorAdd);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);

		this.isEditorAdd = isEditorAdd;
		this.userDB = userDB;
		this.tableDAO = tableDAO;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(tableDAO.getName() + " " + Messages.get().Information);
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
		compositeTable.setLayout(new GridLayout(2, false));

		lblTableName = new Label(compositeTable, SWT.NONE);
		lblTableName.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblTableName.setText(SQLUtil.getTableName(userDB, tableDAO));

		textTBNameCmt = new Text(compositeTable, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_textTBNameCmt = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textTBNameCmt.heightHint = 33;
		textTBNameCmt.setLayoutData(gd_textTBNameCmt);
		textTBNameCmt.setText(tableDAO.getComment());
		
		SashForm sashFormData = new SashForm(compositeBody, SWT.VERTICAL);
		sashFormData.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tableViewer = new TableViewer(sashFormData, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		TableViewerColumn tvColumnName = new TableViewerColumn(tableViewer,	SWT.NONE);
		TableColumn tcColumnName = tvColumnName.getColumn();
		tcColumnName.setWidth(150);
		tcColumnName.setText(Messages.get().ColumnName);
		// tvColumnName.setEditingSupport(new
		// DMLColumnEditingSupport(tableViewer, 0, this));

		TableViewerColumn tvColumnDataType = new TableViewerColumn(tableViewer,	SWT.LEFT);
		TableColumn tcDataType = tvColumnDataType.getColumn();
		tcDataType.setWidth(100);
		tcDataType.setText(Messages.get().DataType);
				
		TableViewerColumn tvColumnKey = new TableViewerColumn(tableViewer, SWT.CENTER);
		TableColumn tcKey = tvColumnKey.getColumn();
		tcKey.setWidth(50);
		tcKey.setText(Messages.get().Key);
		
		TableViewerColumn tvColumnCmt = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tcCmt = tvColumnCmt.getColumn();
		tcCmt.setWidth(300);
		tcCmt.setText(Messages.get().Description);
		
		tableViewer_ext = new TableViewer(sashFormData, SWT.BORDER | SWT.FULL_SELECTION);
		Table table_ext = tableViewer_ext.getTable();
		table_ext.setLinesVisible(true);
		table_ext.setHeaderVisible(true);
		
		TableViewerColumn tvPropertyName = new TableViewerColumn(tableViewer_ext, SWT.NONE);
		TableColumn tcPropertyName = tvPropertyName.getColumn();
		tcPropertyName.setWidth(180);
		tcPropertyName.setText("Property");
				
		TableViewerColumn tvPropertyValue = new TableViewerColumn(tableViewer_ext, SWT.NONE);
		TableColumn tcPropertyValue = tvPropertyValue.getColumn();
		tcPropertyValue.setWidth(300);
		tcPropertyValue.setText("Value");
		
		//DefaultViewerSorter sorterMessage = new DefaultViewerSorter();
		
		tableViewer_ext.setContentProvider(new ArrayContentProvider());
		tableViewer_ext.setLabelProvider(new TableStatisticsLabelProvider());

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new TableInformationLabelProvider());
		sashFormData.setWeights(new int[] {6, 4});

		initData();
		//tableViewer_ext.setComparator(new BasicViewerSorter());
		initExtendedData();

		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}

	private void initData() {
		try {
			List<TableColumnDAO> showTableColumns;

			//조회된 내용이 없고 스키마 정보가 없으면
			if (StringUtils.isEmpty(tableDAO.getSchema_name()) ){//&& showTableColumns.size() <= 0) {
				Map<String,String> paramMap = new HashMap<String,String>();
				paramMap.put("OBJECT_NAME", tableDAO.getName());
				SelectObjectDialog dialog = new SelectObjectDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), userDB, paramMap);

				if (dialog.getSelectObject().isEmpty() && dialog.getObjectCount() > 1) {
					//이름으로 검색한 결과가 1개이상이면 선택화면을 띄운다.
					dialog.open();
//				} else if (dialog.getObjectCount() <= 0) {
					//해당 오브젝트를 찾을 수 없습니다.
//					MessageDialog.openInformation(null , Messages.get().Information, Messages.get().NotFountObject);
				}
				Map<String, String> map = dialog.getSelectObject();
				tableDAO.setSchema_name(map.get("OBJECT_OWNER"));
				tableDAO.setTable_name(map.get("OBJECT_NAME"));
				tableDAO.setTab_name(map.get("OBJECT_NAME"));
//				this.lblTableName.setText(tableDAO.getSchema_name() + "." + tableDAO.getName());
				showTableColumns = TadpoleObjectQuery.getTableColumns(userDB, tableDAO);
			}else{
				showTableColumns = TadpoleObjectQuery.getTableColumns(userDB, tableDAO);
			}
			
			List<ExtendTableColumnDAO> newTableColumns = new ArrayList<ExtendTableColumnDAO>();

			ExtendTableColumnDAO newTableDAO;
			textTBNameCmt.setText(tableDAO.getComment());

			for (TableColumnDAO tableColumnDAO : showTableColumns) {
				String strSysName = SQLUtil.makeIdentifierName(userDB, tableColumnDAO.getField());
				newTableDAO = new ExtendTableColumnDAO(tableColumnDAO.getField(), tableColumnDAO.getType(),	tableColumnDAO.getKey(), lblTableName.getText());
				newTableDAO.setSysName(strSysName);
				newTableDAO.setComment(tableColumnDAO.getComment());

				newTableColumns.add(newTableDAO);
			}

			tableViewer.setInput(newTableColumns);

			tableViewer.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initExtendedData() {
		try {
			Map<String, String> sizeInfoMap = new HashMap<String,String>();
			Map<String, String> statInfoMap = new HashMap<String,String>();
			Map<String, String> statViewInfoMap = new HashMap<String,String>();

			try{
				sizeInfoMap = (Map<String, String>) TadpoleObjectQuery.getTableSizeInfo(userDB, tableDAO);
			}catch(Exception e){
				logger.error(e);
			}
			try{
				statInfoMap = (Map<String, String>) TadpoleObjectQuery.getStatisticsInfo(userDB, tableDAO);
			}catch(Exception e){
				logger.error(e);
			}
			try{
				statViewInfoMap = (Map<String, String>) TadpoleObjectQuery.getViewStatisticsInfo(userDB, tableDAO);
			}catch(Exception e){
				logger.error(e);
			}

			List<Map<String, String>> extendsInfoList = new ArrayList<Map<String, String>>();

			if(sizeInfoMap != null){
				for (String key : sizeInfoMap.keySet()) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("key", key);
					map.put("value", String.valueOf(sizeInfoMap.get(key)));				
					extendsInfoList.add(map);
				}
			}

			if(statInfoMap != null){
				for (String key : statInfoMap.keySet()) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("key", key);
					map.put("value", String.valueOf(statInfoMap.get(key)));
					extendsInfoList.add(map);
				}
			}

			if(statViewInfoMap != null){
				for (String key : statViewInfoMap.keySet()) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("key", key);
					map.put("value", String.valueOf(statViewInfoMap.get(key)));
					extendsInfoList.add(map);
				}
			}

			tableViewer_ext.setInput(extendsInfoList);
			tableViewer_ext.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if (isEditorAdd) {
			createButton(parent, IDialogConstants.OK_ID, Messages.get().GenerateStatmentDMLDialog_2, false);
		}
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Close, false);

	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 600);
	}
}
