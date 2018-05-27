/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 *     nilriri - column information.
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.dbinfos.composites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.csv.CSVUtils;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.rdb.RDBInfomationforColumnDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.dbinfos.RDBDBInfosEditor;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Tables composite
 * 
 * @author nilriri
 * 
 */
public class ColumnsComposite extends DBInfosComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ColumnsComposite.class);

	private UserDBDAO userDB;
	private TableViewer tvColumnInform;

	private DefaultTableColumnFilter columnFilter;
	private Text textFilter;
	
	/** download service handler. */
	private Composite compositeTail;
	private DownloadServiceHandler downloadServiceHandler;
	
	private List<RDBInfomationforColumnDAO> listTableInform = new ArrayList<RDBInfomationforColumnDAO>();

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ColumnsComposite(Composite parent, int style, UserDBDAO userDB) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		this.userDB = userDB;

		Composite compositeHead = new Composite(this, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(3, false));

		Label lblNewLabel = new Label(compositeHead, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText(CommonMessages.get().Filter);

		textFilter = new Text(compositeHead, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.Selection) {
					columnFilter.setSearchString(textFilter.getText());
					tvColumnInform.refresh();
				}
			}
		});

		Button btnRefresh = new Button(compositeHead, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initUI(true);
			}
		});
		btnRefresh.setText(CommonMessages.get().Refresh);

		tvColumnInform = new TableViewer(this, /* SWT.VIRTUAL | */ SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tvColumnInform.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		compositeTail = new Composite(this, SWT.NONE);
		compositeTail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		compositeTail.setLayout(new GridLayout(1, false));
		
		Button btnDownload = new Button(compositeTail, SWT.NONE);
		btnDownload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				download();
			}
		});
		btnDownload.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnDownload.setBounds(0, 0, 94, 28);
		btnDownload.setText(Messages.get().ColumnsComposite_btnDownload_text);
		btnDownload.setEnabled("YES".equals(userDB.getIs_resource_download()));

		createTableColumn();

		columnFilter = new DefaultTableColumnFilter();
		tvColumnInform.addFilter(columnFilter);

		registerServiceHandler();
	}
	
	/** download service handler call */
	private void unregisterServiceHandler() {
		RWT.getServiceManager().unregisterServiceHandler(downloadServiceHandler.getId());
		downloadServiceHandler = null;
	}
	
	/**
	 * download
	 */
	private void download() {
		if(tvColumnInform.getTable().getItemCount() == 0) return;
		if(!MessageDialog.openConfirm(null, CommonMessages.get().Confirm, CommonMessages.get().DoYouWantDownload)) return;
		
		try {
			byte[] strCVSContent = CSVUtils.tableToCSV(tvColumnInform.getTable());//CSVUtils.makeData(listCsvData);
			downloadExtFile(userDB.getDisplay_name() + "_ColumnInformation.csv", strCVSContent); //$NON-NLS-1$
			
			MessageDialog.openInformation(null, CommonMessages.get().Information, CommonMessages.get().DownloadIsComplete);
		} catch (Exception e) {
			logger.error("An error occurred while writing into a CSV file.", e); //$NON-NLS-1$
		}		
	}

	/**
	 * download external file
	 * 
	 * @param fileName
	 * @param newContents
	 */
	public void downloadExtFile(String fileName, byte[] newContents) {
		downloadServiceHandler.setName(fileName);
		downloadServiceHandler.setByteContent(newContents);
		
		DownloadUtils.provideDownload(compositeTail, downloadServiceHandler.getId());
	}
	
	/** registery service handler */
	private void registerServiceHandler() {
		downloadServiceHandler = new DownloadServiceHandler();
		RWT.getServiceManager().registerServiceHandler(downloadServiceHandler.getId(), downloadServiceHandler);
	}
	
	public void dispose() {
		unregisterServiceHandler();
		super.dispose();
	};

	/**
	 * table column head를 생성합니다.
	 */
	private void createTableColumn() {

		TableViewColumnDefine [] tableColumnDef = new TableViewColumnDefine [] {};
		if (userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT || userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {

			tableColumnDef = new TableViewColumnDefine [] { //
			new TableViewColumnDefine ("TABLE_NAME", "Table Name", 100, SWT.LEFT, true) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("TABLE_COMMENT", "Table Comment", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("COLUMN_NAME", "Column Name", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("NULLABLE", "Nullable", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("DATA_TYPE", "Data Type", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("DATA_DEFAULT", "Data Default", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("COLUMN_COMMENT", "Column Comment", 100, SWT.LEFT) // //$NON-NLS-2$
					, new TableViewColumnDefine ("DATA_TYPE_MOD", "Data Type Mod", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("CHAR_USED", "Char Used", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("HISTOGRAM", "Histogram", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("NUM_DISTINCT", "Num Distinct", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("NUM_NULLS", "Num Nulls", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("DENSITY", "Density", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("LAST_ANALYZED", "Last Analyzed", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
			};

		} else if (userDB.getDBDefine() == DBDefine.CUBRID_DEFAULT) {

			tableColumnDef = new TableViewColumnDefine [] { //
			new TableViewColumnDefine ("TABLE_NAME", "Table Name", 100, SWT.LEFT, true) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("TABLE_COMMENT", "Table Comment", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("COLUMN_NAME", "Column Name", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("COLUMN_COMMENT", "Column Comment", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("NULLABLE", "Nullable", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("DATA_TYPE", "Data Type", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("DATA_DEFAULT", "Data Default", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("PARTITIONED", "Paritioned", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("DATA_PRECISION", "Data Precision", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("DATA_SCALE", "Data Scale", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("NUM_DISTINCT", "Num Distinct", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("NUM_NULLS", "Num Nulls", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("DENSITY", "Density", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("LAST_ANALYZED", "Last Analyzed", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
			};

		} else if (userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT || userDB.getDBDefine() == DBDefine.TIBERO_DEFAULT || userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {

			tableColumnDef = new TableViewColumnDefine [] { //
			new TableViewColumnDefine ("TABLE_NAME", "Table Name", 100, SWT.LEFT, true) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("TABLE_COMMENT", "Table Comment", 150, SWT.LEFT) // //$NON-NLS-2$
					, new TableViewColumnDefine ("COLUMN_NAME", "Column Name", 120, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("COLUMN_COMMENT", "Column Comment", 150, SWT.LEFT).assignEditingSupport(new DBInfoCommentEditorSupport(tvColumnInform, userDB, 3) )  // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("NULLABLE", "Nullable", 60, SWT.CENTER) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("DATA_TYPE", "Data Type", 120, SWT.LEFT) // //$NON-NLS-2$
					, new TableViewColumnDefine ("DATA_DEFAULT", "Data Default", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("DATA_TYPE_MOD", "Data Type Mod", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("CHAR_USED", "Char Used", 60, SWT.CENTER) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("HISTOGRAM", "Histogram", 60, SWT.CENTER) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("NUM_DISTINCT", "Num Distinct", 100, SWT.RIGHT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("NUM_NULLS", "Num Nulls", 100, SWT.RIGHT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("DENSITY", "Density", 100, SWT.RIGHT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("LAST_ANALYZED", "Last Analyzed", 120, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
			};
		} else if (DBDefine.getDBDefine(userDB) == DBDefine.ALTIBASE_DEFAULT) {

			tableColumnDef = new TableViewColumnDefine [] { //
					  new TableViewColumnDefine ("TABLE_NAME", "Table Name", 180, SWT.LEFT, true) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("TABLE_COMMENT", "Table Comment", 150, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("COLUMN_NAME", "Column Name", 150, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("COLUMN_COMMENT", "Column Comment", 150, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("NULLABLE", "Nullable", 60, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("PK", "Key", 100, SWT.LEFT)
					, new TableViewColumnDefine ("DATA_TYPE", "Data Type", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("DATA_DEFAULT", "Default", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
			};
		} else {
			tableColumnDef = new TableViewColumnDefine [] { //
			new TableViewColumnDefine ("TABLE_NAME", "Table Name", 100, SWT.LEFT, true) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("TABLE_COMMENT", "Table Comment", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("COLUMN_NAME", "Column Name", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("COLUMN_COMMENT", "Column Comment", 100, SWT.LEFT).assignEditingSupport(new DBInfoCommentEditorSupport(tvColumnInform, userDB, 3) ) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("NULLABLE", "Nullable", 60, SWT.CENTER) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("PK", "PK", 60, SWT.CENTER) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("DATA_TYPE", "Data Type", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("DATA_DEFAULT", "Data Default", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("DATA_TYPE_MOD", "Data Type Mod", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("CHAR_USED", "Char Used", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("HISTOGRAM", "Histogram", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("NUM_DISTINCT", "Num Distinct", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("NUM_NULLS", "Num Nulls", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("DENSITY", "Density", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
					, new TableViewColumnDefine ("LAST_ANALYZED", "Last Analyzed", 100, SWT.LEFT) // //$NON-NLS-1$ //$NON-NLS-2$
			};
		}

		ColumnHeaderCreator.createColumnHeader(tvColumnInform, tableColumnDef);

		tvColumnInform.setContentProvider(ArrayContentProvider.getInstance());
		tvColumnInform.setLabelProvider(new DefaultLabelProvider(tvColumnInform));

	}


	/**
	 * 
	 */
	public void initUI(boolean isRefresh) {
		if(isRefresh) {
			listTableInform.clear();
		} else {
			if(listTableInform.size() != 0) return;
		}
		
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			
			if (userDB.getDBDefine() == DBDefine.SQLite_DEFAULT) {

				List<HashMap<String, String>> sqliteTableList = sqlClient.queryForList("tableInformation", userDB.getDb()); //$NON-NLS-1$ //$NON-NLS-2$
				
				listTableInform = new ArrayList<RDBInfomationforColumnDAO>();
				for (HashMap<String, String> table : sqliteTableList) {

					List<HashMap<String, String>> sqliteColumnList = sqlClient.queryForList("columnInformation", table.get("name")); //$NON-NLS-1$ //$NON-NLS-2$

					for (HashMap<String, String> sqliteMap : sqliteColumnList) {//
						RDBInfomationforColumnDAO dao = new RDBInfomationforColumnDAO(table.get("name"), ""// //$NON-NLS-1$ //$NON-NLS-2$
								, sqliteMap.get("name"), ""// //$NON-NLS-1$ //$NON-NLS-2$
								, sqliteMap.get("type")// //$NON-NLS-1$
								, ("0".equals(String.valueOf(sqliteMap.get("notnull"))) ? "No" : "")// //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
								, String.valueOf(sqliteMap.get("dflt_value") == null ? "" : sqliteMap.get("dflt_value"))// //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								, ("1".equals(String.valueOf(sqliteMap.get("pk"))) ? "PK" : "") // //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						);
						listTableInform.add(dao);
					}
				}

			} else if (userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT | userDB.getDBDefine() == DBDefine.TIBERO_DEFAULT){
				HashMap<String, String>paramMap = new HashMap<String, String>();
				paramMap.put("schema_name", userDB.getSchema()); //$NON-NLS-1$
				try{
					listTableInform = sqlClient.queryForList("columnInformation", paramMap); //$NON-NLS-1$ //$NON-NLS-2$
				}catch(Exception e){
					// 권한이 없을경우 해당 유저(스키마)에서 접근가능한 컬럼 목록만 조회한다.
					listTableInform = sqlClient.queryForList("userColumnInformation", paramMap); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else if (userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT | userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT){
				HashMap<String, String>paramMap = new HashMap<String, String>();
				paramMap.put("schema_name", userDB.getSchema()); //$NON-NLS-1$
				listTableInform = sqlClient.queryForList("columnInformation", paramMap); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				listTableInform = sqlClient.queryForList("columnInformation", userDB.getDb()); //$NON-NLS-1$ //$NON-NLS-2$
			}
			tvColumnInform.setInput(listTableInform);
			tvColumnInform.refresh();
			
			// google analytic
			AnalyticCaller.track(RDBDBInfosEditor.ID, "ColumnsComposite"); //$NON-NLS-1$
		} catch (Exception e) {
			logger.error("initialize column summary", e); //$NON-NLS-1$

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null,CommonMessages.get().Error, Messages.get().MainEditor_19, errStatus); //$NON-NLS-1$
		}
	}

}