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
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.libs.core.utils.NullSafeComparator;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.TadpoleViewrFilter;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.ObjectComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;

/**
 * DMLGenerae Statement Dialog
 * 
 * @author nilriri
 *
 */
public class TableInformationDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(TableInformationDialog.class);
	private boolean isEditorAdd = false;
	private InformationComparator informationComparator;
	private InformationFilter informationFilter;

	private UserDBDAO userDB;
	private TableDAO tableDAO;
	private TableViewer informationTableViewer;
	private TableViewer tableViewer_ext;
	private Text textTBNameCmt;
	private Label lblTableName;
	private Text textFilter;

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
		newShell.setText(tableDAO.getFullName() + " " + CommonMessages.get().Information);
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
		
		Composite compositeFilter = new Composite(compositeBody, SWT.NONE);
		compositeFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeFilter.setLayout(new GridLayout(2, false));
		
		Label lblFilter = new Label(compositeFilter, SWT.NONE);
		lblFilter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilter.setText(CommonMessages.get().Filter);
		
		textFilter = new Text(compositeFilter, SWT.BORDER);
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				filterText();
			}
		});
		
		SashForm sashFormData = new SashForm(compositeBody, SWT.VERTICAL);
		sashFormData.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		informationTableViewer = new TableViewer(sashFormData, SWT.BORDER | SWT.FULL_SELECTION);
		informationTableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection is = (IStructuredSelection) event.getSelection();

				if (!is.isEmpty()) {
					ExtendTableColumnDAO tableDAO = (ExtendTableColumnDAO) is.getFirstElement();
					if(GetPreferenceGeneral.getAddComma()) {
						FindEditorAndWriteQueryUtil.runAtPosition(String.format("%s, ", tableDAO.getName()));
					} else {
						FindEditorAndWriteQueryUtil.runAtPosition(String.format("%s ", tableDAO.getName()));
					}
				}
			}
		});
		Table table = informationTableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		// sorter
		informationComparator = new InformationComparator();
		informationTableViewer.setSorter(informationComparator);

		createInformationColumn();
		
		informationTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		informationTableViewer.setLabelProvider(new TableInformationLabelProvider());
		
		// add filter
		informationFilter = new InformationFilter();
		informationTableViewer.addFilter(informationFilter);
		
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
		
		tableViewer_ext.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer_ext.setLabelProvider(new TableStatisticsLabelProvider());

		sashFormData.setWeights(new int[] {6, 4});

		initData();
		//tableViewer_ext.setComparator(new BasicViewerSorter());
		initExtendedData();

		// google analytic
		AnalyticCaller.track(this.getClass().getName());

		return container;
	}
	
	/**
	 *  컬럼을 생성합니다.
	 */
	private void createInformationColumn() {
		
		String[] name 		= {Messages.get().ColumnName, Messages.get().DataType, Messages.get().Key, CommonMessages.get().Description};
		int[] size 			= {150, 100, 50, 300};
		
		// table column tooltip
		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(informationTableViewer, SWT.LEFT);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
			tableColumn.getColumn().addSelectionListener(getSelectionAdapter(tableColumn, i));
			tableColumn.getColumn().setMoveable(true);
		}
	}
	
	/**
	 * filter text
	 */
	private void filterText() {
		informationFilter.setSearchText(textFilter.getText());
		informationTableViewer.refresh();
	}

	/**
	 * column select event
	 * 
	 * @param column
	 * @param index
	 * @return
	 */
	private SelectionAdapter getSelectionAdapter(final TableViewerColumn column, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				informationComparator.setColumn(index);
				
				informationTableViewer.getTable().setSortDirection(informationComparator.getDirection());
				informationTableViewer.getTable().setSortColumn(column.getColumn());
				informationTableViewer.refresh();
			}
		};
		return selectionAdapter;
	}

	private void initData() {
		try {
			List<TableColumnDAO> showTableColumns = new ArrayList<>();

			// 현재 allObject로 조회하는 것은 
			// oracle, mysql, maria, mssql, tibero 이면 모든 오브젝트를 조회하여 보여주도록하고 나머지는 
			if(userDB.getDBGroup() == DBGroupDefine.MSSQL_GROUP || 
					userDB.getDBGroup() == DBGroupDefine.MYSQL_GROUP ||
					userDB.getDBGroup() == DBGroupDefine.ORACLE_GROUP
			) { 
				
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
	//					MessageDialog.openInformation(null , CommonMessages.get().Information, String.format(Messages.get().NotFountObject,tableDAO.getName()));
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
			} else {
				showTableColumns = TadpoleObjectQuery.getTableColumns(userDB, tableDAO);
			}
			
			if(showTableColumns.isEmpty()) {
				MessageDialog.openWarning(getShell(), CommonMessages.get().Warning, Messages.get().CantnotFoundTable);
				super.close();
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

			informationTableViewer.setInput(newTableColumns);
		} catch (Exception e) {
			logger.error("find table object", e);
		}

	}

	private void initExtendedData() {
		if(userDB.getDBGroup() != DBGroupDefine.ORACLE_GROUP) return;
			
		try {
			Map<String, String> sizeInfoMap = new HashMap<String,String>();
			Map<String, String> statInfoMap = new HashMap<String,String>();
			Map<String, String> statViewInfoMap = new HashMap<String,String>();

			sizeInfoMap = (Map<String, String>) TadpoleObjectQuery.getTableSizeInfo(userDB, tableDAO);
			statInfoMap = (Map<String, String>) TadpoleObjectQuery.getStatisticsInfo(userDB, tableDAO);
			statViewInfoMap = (Map<String, String>) TadpoleObjectQuery.getViewStatisticsInfo(userDB, tableDAO);

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
		if (isEditorAdd) {
			createButton(parent, IDialogConstants.OK_ID, Messages.get().GenerateStatmentDMLDialog_2, false);
		}
		createButton(parent, IDialogConstants.CANCEL_ID, CommonMessages.get().Close, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 600);
	}
}
/*
	table information comparator
 */
class InformationComparator extends ObjectComparator {
	public InformationComparator() {
		this.propertyIndex = -1;
		direction = ASCENDING;
	}
	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		ExtendTableColumnDAO tableDAO1 = (ExtendTableColumnDAO) e1;
		ExtendTableColumnDAO tableDAO2 = (ExtendTableColumnDAO) e2;
		
		int rc = DESCENDING;
		switch (this.propertyIndex) {
		case 0:
			rc = NullSafeComparator.compare(tableDAO1.getName(), tableDAO2.getName());
			break;
		case 1:
			rc = NullSafeComparator.compare(tableDAO1.getType(), tableDAO2.getType());
			break;
		case 2:
			rc = NullSafeComparator.compare(tableDAO1.getKey(), tableDAO2.getKey());
			break;
		case 3:
			rc = NullSafeComparator.compare(tableDAO1.getComment(), tableDAO2.getComment());
			break;
		}
		
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}

/**
 * information filter
 * 
 * @author hangum
 *
 */
class InformationFilter extends TadpoleViewrFilter {

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchString == null || searchString.length() == 0) return true;
		
		searchString = StringUtils.lowerCase(searchString);
		
		ExtendTableColumnDAO dao = (ExtendTableColumnDAO)element;
		if(StringUtils.lowerCase((""+dao.getName())).matches(searchString)) 		return true;
		else if(StringUtils.lowerCase((""+dao.getType())).matches(searchString)) 		return true;
		else if(StringUtils.lowerCase((""+dao.getKey())).matches(searchString)) 	return true;
		else if(StringUtils.lowerCase((""+dao.getComment())).matches(searchString)) 	return true;
		else return false;
	}

} 
