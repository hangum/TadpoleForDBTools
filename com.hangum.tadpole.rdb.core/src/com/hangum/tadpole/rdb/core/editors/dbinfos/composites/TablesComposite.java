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
package com.hangum.tadpole.rdb.core.editors.dbinfos.composites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
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
import com.hangum.tadpole.commons.util.NumberFormatUtils;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.engine.define.DBGroupDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.dbinfos.RDBDBInfosEditor;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Tables composite
 * 
 * @author hangum
 *
 */
public class TablesComposite extends DBInfosComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TablesComposite.class);
	
	private UserDBDAO userDB;
	private TableViewer tvTableInform;
	
	private TableInfoFilter tableFilter;
	private Text textFilter;
	
	/** download service handler. */
	private Composite compositeTail;
	private DownloadServiceHandler downloadServiceHandler;
	private List listTableInform = new ArrayList<>();

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TablesComposite(Composite parent, int style, UserDBDAO userDB) {
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
			public void keyReleased(KeyEvent e) {
				tableFilter.setSearchString(textFilter.getText());
				tvTableInform.refresh();
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
		
		tvTableInform = new TableViewer(this, /* SWT.VIRTUAL | */ SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tvTableInform.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createColumn();
		
		tvTableInform.setContentProvider(ArrayContentProvider.getInstance());
		tvTableInform.setLabelProvider(new TableInformLabelProvider(userDB));
		
		tableFilter = new TableInfoFilter();
		tvTableInform.addFilter(tableFilter);
		
		compositeTail = new Composite(this, SWT.NONE);
		compositeTail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		compositeTail.setLayout(new GridLayout(1, false));
		
		Button btnCsvExport = new Button(compositeTail, SWT.NONE);
		btnCsvExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				download();
			}
		});
		btnCsvExport.setBounds(0, 0, 94, 28);
		btnCsvExport.setText(Messages.get().TablesComposite_btnCsvExport_text);
		btnCsvExport.setEnabled("YES".equals(userDB.getIs_resource_download()));

//		initUI();
		registerServiceHandler();
	}
	
	/**
	 * table column head를 생성합니다.
	 */
	private void createColumn() {
		if(DBGroupDefine.MYSQL_GROUP == userDB.getDBGroup()) {
			String[] name = {"Name", "Engine", "Rows", "Auto Increment", "collation", "Size(MB)", "Created", "Comment"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
			int[] size = {120, 70, 70, 100, 80, 80, 120, 220};
			int[] align = {SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT};
			
			createColumn(name, size, align);
		} else if(DBGroupDefine.ORACLE_GROUP == userDB.getDBGroup()) {
			String[] name = {"Table Name","Tablespace Name","Pct Free","Ini Trans","Logging","Num Rows","Blocks","Avg Row Len","Degree","Sample Size","Last Analyzed","Partitioned","Buffer Pool","Row Movement","Duration","Compression","Dropped","Read Only","Temporary","Max Extents","Iot Type","Initial Extent","Next Extent","Min Extents"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			int[] size = {120, 120, 90, 90, 52 , 90, 90, 90, 80 , 90, 120, 52 , 68 , 72 , 100, 72 , 52 , 52 , 40 , 90, 88 , 90, 90, 90};
			int[] align = {SWT.LEFT ,SWT.LEFT ,SWT.RIGHT ,SWT.RIGHT ,SWT.LEFT ,SWT.RIGHT ,SWT.RIGHT ,SWT.RIGHT ,SWT.RIGHT ,SWT.RIGHT ,SWT.LEFT ,SWT.LEFT ,SWT.LEFT ,SWT.LEFT ,SWT.LEFT ,SWT.LEFT ,SWT.LEFT ,SWT.LEFT ,SWT.LEFT ,SWT.RIGHT ,SWT.LEFT ,SWT.RIGHT ,SWT.RIGHT ,SWT.RIGHT};
			
			
			createColumn(name, size, align);
		} else if(DBGroupDefine.ALTIBASE_GROUP == userDB.getDBGroup()) {
			String[] name = {"Table Name", "Owner", "Rows", "Tablespace Name", "Character Set", "Size (MB)", "Created", "Comment"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
			int[] size = {200, 70, 70, 200, 100, 80, 120, 250};
			int[] align = {SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT};
			
			createColumn(name, size, align);
		} else {
			String[] name = {"Name", "comment", "Index", "Shared", "Primary Key", "Triggers", "Sub Class", "Rules", "Option"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
			int[] size = {120, 150, 60, 60, 100, 80, 80, 60, 60};
			int[] align = {SWT.LEFT, SWT.LEFT, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER};
			
			createColumn(name, size, align);
		}
	}
	
	/**
	 *  실제 컬럼의 헤더를 생성합니다.
	 * 
	 * @param name
	 * @param size
	 */
	private void createColumn(String[] name, int[] size, int[] align) {
		for (int i=0; i<name.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tvTableInform, align[i]);
			tableColumn.getColumn().setText(name[i]);
			tableColumn.getColumn().setWidth(size[i]);
		}
	}
	
	/**
	 *  initialize ui data
	 */
	public void initUI(boolean isRefresh) {
		if(isRefresh) {
			listTableInform.clear();
		} else {
			if(listTableInform.size() != 0) return;
		}
		
		try {
			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			if (DBGroupDefine.ORACLE_GROUP == userDB.getDBGroup()){
				HashMap<String, String>paramMap = new HashMap<String, String>();
				paramMap.put("schema_name", userDB.getSchema()); //$NON-NLS-1$
				listTableInform = sqlClient.queryForList("tableInformation", paramMap); //$NON-NLS-1$ //$NON-NLS-2$
			} else if (DBGroupDefine.MYSQL_GROUP == userDB.getDBGroup()){
				HashMap<String, String>paramMap = new HashMap<String, String>();
				paramMap.put("schema_name", userDB.getSchema()); //$NON-NLS-1$
				listTableInform = sqlClient.queryForList("tableInformation", paramMap); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				listTableInform = sqlClient.queryForList("tableInformation", userDB.getDb()); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			tvTableInform.setInput(listTableInform);
			tvTableInform.refresh();
		} catch (Exception e) {
			logger.error("Initialize session list", e); //$NON-NLS-1$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null,CommonMessages.get().Error, Messages.get().MainEditor_19, errStatus); //$NON-NLS-1$
		}
		
		// google analytic
		AnalyticCaller.track(RDBDBInfosEditor.ID, "TablesComposite"); //$NON-NLS-1$
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
		if(tvTableInform.getTable().getItemCount() == 0) return;
		if(!MessageDialog.openConfirm(null, CommonMessages.get().Confirm, CommonMessages.get().DoYouWantDownload)) return;
		
		try {
			byte[] strCVSContent = CSVUtils.tableToCSV(tvTableInform.getTable());//CSVUtils.makeData(listCsvData);
			downloadExtFile(userDB.getDisplay_name() + "_TableInformation.csv", strCVSContent); //$NON-NLS-1$
			
			MessageDialog.openInformation(null, CommonMessages.get().Information, CommonMessages.get().DownloadIsComplete);
		} catch (Exception e) {
			logger.error("Save CSV Data", e); //$NON-NLS-1$
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
	
	/** Register a service handler */
	private void registerServiceHandler() {
		downloadServiceHandler = new DownloadServiceHandler();
		RWT.getServiceManager().registerServiceHandler(downloadServiceHandler.getId(), downloadServiceHandler);
	}
	
	public void dispose() {
		unregisterServiceHandler();
		super.dispose();
	};
	
}

/**
 * mysql, mariadb label provider
 * @author hangum
 *
 */
class TableInformLabelProvider extends LabelProvider implements ITableLabelProvider {
	private UserDBDAO userDB;
	
	public TableInformLabelProvider(UserDBDAO userDB) {
		this.userDB  = userDB;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Map resultMap = (HashMap)element;
		
		if(DBGroupDefine.MYSQL_GROUP == userDB.getDBGroup()) {
			switch(columnIndex) {
			case 0: return ""+resultMap.get("TABLE_NAME"); //$NON-NLS-1$ //$NON-NLS-2$
			case 1: return ""+resultMap.get("ENGINE"); //$NON-NLS-1$ //$NON-NLS-2$
			case 2: return NumberFormatUtils.commaFormat(""+resultMap.get("TABLE_ROWS")); //$NON-NLS-1$ //$NON-NLS-2$
			case 3: return NumberFormatUtils.commaFormat(StringUtils.replace(""+resultMap.get("AUTO_INCREMENT"), "null", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			case 4: return ""+resultMap.get("TABLE_COLLATION"); //$NON-NLS-1$ //$NON-NLS-2$
			case 5: return ""+resultMap.get("SIZEOFMB"); //$NON-NLS-1$ //$NON-NLS-2$
			case 6: return ""+resultMap.get("CREATE_TIME"); //$NON-NLS-1$ //$NON-NLS-2$
			case 7: return ""+resultMap.get("TABLE_COMMENT"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else if(DBGroupDefine.ORACLE_GROUP == userDB.getDBGroup()) {
			switch(columnIndex) {
			case 0 : return "" + resultMap.get("TABLE_NAME"      ); //$NON-NLS-1$ //$NON-NLS-2$
			case 1 : return "" + resultMap.get("TABLESPACE_NAME" ); //$NON-NLS-1$ //$NON-NLS-2$
			case 2 : return NumberFormatUtils.commaFormat("" + resultMap.get("PCT_FREE"        )); //$NON-NLS-1$ //$NON-NLS-2$
			case 3 : return NumberFormatUtils.commaFormat("" + resultMap.get("INI_TRANS"       )); //$NON-NLS-1$ //$NON-NLS-2$
			case 4 : return "" + resultMap.get("LOGGING"         ); //$NON-NLS-1$ //$NON-NLS-2$
			case 5 : return NumberFormatUtils.commaFormat("" + resultMap.get("NUM_ROWS"        )); //$NON-NLS-1$ //$NON-NLS-2$
			case 6 : return NumberFormatUtils.commaFormat("" + resultMap.get("BLOCKS"          )); //$NON-NLS-1$ //$NON-NLS-2$
			case 7 : return NumberFormatUtils.commaFormat("" + resultMap.get("AVG_ROW_LEN"     )); //$NON-NLS-1$ //$NON-NLS-2$
			case 8 : return NumberFormatUtils.commaFormat("" + resultMap.get("DEGREE"          )); //$NON-NLS-1$ //$NON-NLS-2$
			case 9 : return NumberFormatUtils.commaFormat("" + resultMap.get("SAMPLE_SIZE"     )); //$NON-NLS-1$ //$NON-NLS-2$
			case 10: return "" + resultMap.get("LAST_ANALYZED"   ); //$NON-NLS-1$ //$NON-NLS-2$
			case 11: return "" + resultMap.get("PARTITIONED"     ); //$NON-NLS-1$ //$NON-NLS-2$
			case 12: return "" + resultMap.get("BUFFER_POOL"     ); //$NON-NLS-1$ //$NON-NLS-2$
			case 13: return "" + resultMap.get("ROW_MOVEMENT"    ); //$NON-NLS-1$ //$NON-NLS-2$
			case 14: return "" + resultMap.get("DURATION"        ); //$NON-NLS-1$ //$NON-NLS-2$
			case 15: return "" + resultMap.get("COMPRESSION"     ); //$NON-NLS-1$ //$NON-NLS-2$
			case 16: return "" + resultMap.get("DROPPED"         ); //$NON-NLS-1$ //$NON-NLS-2$
			case 17: return "" + resultMap.get("READ_ONLY"       ); //$NON-NLS-1$ //$NON-NLS-2$
			case 18: return "" + resultMap.get("TEMPORARY"       ); //$NON-NLS-1$ //$NON-NLS-2$
			case 19: return NumberFormatUtils.commaFormat("" + resultMap.get("MAX_EXTENTS"     )); //$NON-NLS-1$ //$NON-NLS-2$
			case 20: return "" + resultMap.get("IOT_TYPE"        ); //$NON-NLS-1$ //$NON-NLS-2$
			case 21: return NumberFormatUtils.commaFormat("" + resultMap.get("INITIAL_EXTENT"  )); //$NON-NLS-1$ //$NON-NLS-2$
			case 22: return NumberFormatUtils.commaFormat("" + resultMap.get("NEXT_EXTENT"     )); //$NON-NLS-1$ //$NON-NLS-2$
			case 23: return NumberFormatUtils.commaFormat("" + resultMap.get("MIN_EXTENTS"     )); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else if(DBGroupDefine.ALTIBASE_GROUP == userDB.getDBGroup()) {
			switch(columnIndex) {
				case 0: return ""+resultMap.get("TABLE_NAME"); //$NON-NLS-1$ //$NON-NLS-2$
				case 1: return ""+resultMap.get("OWNER"); //$NON-NLS-1$ //$NON-NLS-2$
				case 2: return NumberFormatUtils.commaFormat(""+resultMap.get("TABLE_ROWS")); //$NON-NLS-1$ //$NON-NLS-2$
				case 3: return NumberFormatUtils.commaFormat(StringUtils.replace(""+resultMap.get("TABLESPACE_NAME"), "null", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				case 4: return ""+resultMap.get("CHARACTER_SET"); //$NON-NLS-1$ //$NON-NLS-2$
				case 5: return ""+resultMap.get("SIZEOFMB"); //$NON-NLS-1$ //$NON-NLS-2$
				case 6: return ""+resultMap.get("CREATED"); //$NON-NLS-1$ //$NON-NLS-2$
				case 7: return ""+resultMap.get("TABLE_COMMENT"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			switch(columnIndex) {
		    case 0: return ""+resultMap.get("name"); //$NON-NLS-1$ //$NON-NLS-2$
			case 1: return StringUtils.replace(""+resultMap.get("comment"), "null", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			case 2: return "true".equals(""+resultMap.get("has_index")) ? "has" : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			case 3: return "true".equals(""+resultMap.get("is_shared")) ? "has" : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			case 4: return "true".equals(""+resultMap.get("has_pk")) ? "has" : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			case 5: return "true".equals(""+resultMap.get("has_triggers")) ? "has" : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			case 6: return "true".equals(""+resultMap.get("has_subclass")) ? "has" : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			case 7: return "true".equals(""+resultMap.get("has_rules")) ? "has" : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			case 8: return StringUtils.replace(""+resultMap.get("options"), "null", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
		}
		
		return "*** Invalid column index ***"; //$NON-NLS-1$
	}
	
}

/**
 * name filter
 * 
 * @author hangum
 *
 */
class TableInfoFilter extends ViewerFilter {
	String searchString;
	
	public void setSearchString(String s) {
		this.searchString = ".*" + s + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(searchString == null || searchString.length() == 0) {
			return true;
		}
		
		Map resultMap = (HashMap)element;
		String tbName = ""; //$NON-NLS-1$
		if(resultMap.get("TABLE_NAME") != null) tbName = ""+resultMap.get("TABLE_NAME"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		else tbName = ""+resultMap.get("name"); //$NON-NLS-1$ //$NON-NLS-2$
		
		if(tbName.matches(searchString)) return true;
		return false;
	}
	
}
