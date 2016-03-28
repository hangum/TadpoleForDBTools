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
package com.hangum.tadpole.rdb.core.editors.objects.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
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
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine.EXECUTE_TYPE;
import com.hangum.tadpole.ace.editor.core.define.EditorDefine.QUERY_MODE;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.OBJECT_TYPE;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.resultset.ResultSetUtils;
import com.hangum.tadpole.engine.sql.util.sqlscripts.DDLScriptManager;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultContentProvider;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultFilter;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultSorter;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.msg.DirectChangeDialog;
import com.hangum.tadpole.rdb.core.dialog.msg.TDBErroDialog;
import com.hangum.tadpole.rdb.core.editors.main.execute.sub.ExecuteBatchSQL;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.hangum.tadpole.rdb.core.editors.main.utils.SQLTextUtil;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.sub.utils.TadpoleObjectQuery;
import com.hangum.tadpole.session.manager.SessionManager;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Table data direct editor
 * 
 * refer to https://github.com/hangum/TadpoleForDBTools/issues/469
 * 
 * @author hangum
 *
 */
public class TableDirectEditorComposite extends Composite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TableDirectEditorComposite.class);
		
	private TableDAO tableDao;
	private UserDBDAO userDB;
	private List<TableColumnDAO> columnList;
	/** pk key의 이름을 가지고 있습니다 */
	private Map<String, Boolean> primaryKEYListString = new HashMap<String, Boolean>();
	/** pk key의 이름 의 index를 가지고 있습니다 */
	private List<Integer> primaryKeyListIndex = new ArrayList<Integer>();
	/** pk key의 index별 이름 */
	private Map<Integer, String> primaryKEYIntStrList = new HashMap<Integer, String>();

	private Table tableResult;
	private TableViewer sqlResultTableViewer;
	private SQLResultFilter sqlFilter = new SQLResultFilter();
	private SQLResultSorter sqlSorter;
	
	/** query  HashMap -- table 컬럼의 정보 다음과 같습니다. <column index, Data> */
	private Map<Integer, String> mapColumns = null;
	/** query 의 결  -- table의 데이터는 다음과 같습니다. <column index, Data> */
	private List<Map<Integer, Object>> tableDataList = new ArrayList<Map<Integer, Object>>();
	/** 원본 데이터를 가지고 있습니다 */
	private List<Map<Integer, Object>> originalDataList = new ArrayList<Map<Integer, Object>>();
	/** 데이터의 데이터 타입 */
	private HashMap<Integer, String> tableDataTypeList = new HashMap<Integer, String>();
	
	private Text textFilter;
	
	private ToolBar toolBar;
	private ToolItem tltmRefresh;
	private ToolItem tltmSave;
	private ToolItem tltmDelete;
	private ToolItem tltmInsert;
	
	private Text textWhere;
	private Composite compositeTail;
	private Button btnDdlSourceView;
	private Label lblOrderBy;
	private Text textOrderBy;
	

	/**
	 * default composite
	 * 
	 * @param parent
	 * @param style
	 * @param userDB
	 * @param initTableNameStr
	 * @param columnList
	 * @param primaryKEYListString
	 */
	public TableDirectEditorComposite(Composite parent, int style, final UserDBDAO userDB, final TableDAO tableDao,  List<TableColumnDAO> columnList, Map<String, Boolean> primaryKEYListString) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		setLayout(gridLayout);
		
		// start initialize value
		this.userDB = userDB;
		this.tableDao = tableDao;
		this.columnList = columnList;
		this.primaryKEYListString = primaryKEYListString;
		// end initialize value
		
		Composite compositeBase = new Composite(this, SWT.NONE);
		compositeBase.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBase = new GridLayout(1, false);
		gl_compositeBase.verticalSpacing = 3;
		gl_compositeBase.horizontalSpacing = 3;
		gl_compositeBase.marginHeight = 3;
		gl_compositeBase.marginWidth = 3;
		compositeBase.setLayout(gl_compositeBase);
		
		toolBar = new ToolBar(compositeBase, SWT.NONE | SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.setImage(GlobalImageUtils.getRefresh());
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(tltmSave.getEnabled()) {
					if(!MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().TableDirectEditorComposite_1)) return;
				}
				
				refreshEditor();
			}
		});
		tltmRefresh.setToolTipText(Messages.get().Refresh);
		
		tltmSave = new ToolItem(toolBar, SWT.NONE);
		tltmSave.setImage(GlobalImageUtils.getSave());
		tltmSave.setEnabled(false);
		tltmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveTableData();
			}
		});
		tltmSave.setToolTipText(Messages.get().Save);
		
		tltmInsert = new ToolItem(toolBar, SWT.NONE);
		tltmInsert.setImage(GlobalImageUtils.getAdd());
		tltmInsert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				insertRow();
			}
		});
		tltmInsert.setToolTipText(Messages.get().Add);
		
		tltmDelete = new ToolItem(toolBar, SWT.NONE);
		tltmDelete.setImage(GlobalImageUtils.getDelete());
		tltmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				IStructuredSelection is = (IStructuredSelection)sqlResultTableViewer.getSelection();
				if(!is.isEmpty()) {
					deleteRow(is);
				}
				
			}
		});
		tltmDelete.setEnabled(false);
		tltmDelete.setToolTipText(Messages.get().Delete);
		
		Composite compositeBody = new Composite(compositeBase, SWT.NONE);
		GridLayout gl_compositeBody = new GridLayout(2, false);
		gl_compositeBody.horizontalSpacing = 3;
		gl_compositeBody.verticalSpacing = 3;
		gl_compositeBody.marginHeight = 3;
		gl_compositeBody.marginWidth = 3;
		compositeBody.setLayout(gl_compositeBody);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblWhere = new Label(compositeBody, SWT.NONE);
		lblWhere.setText(Messages.get().TableEditPart_lblWhere_text);
		
		textWhere = new Text(compositeBody, SWT.BORDER);
		textWhere.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) initBusiness();
			}
		});
		textWhere.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblOrderBy = new Label(compositeBody, SWT.NONE);
		lblOrderBy.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOrderBy.setText(Messages.get().TableDirectEditorComposite_lblOrderBy_text);
		
		textOrderBy = new Text(compositeBody, SWT.BORDER);
		textOrderBy.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) initBusiness();
			}
		});
		textOrderBy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel = new Label(compositeBody, SWT.NONE);
		lblNewLabel.setText(Messages.get().Filter);
		
		textFilter = new Text(compositeBody, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) setFilter();
			}
		});
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		sqlResultTableViewer = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		sqlResultTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				tltmDelete.setEnabled(true);
			}
		});
		tableResult = sqlResultTableViewer.getTable();
		tableResult.setHeaderVisible(true);
		tableResult.setLinesVisible(true);
		tableResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		
		// table markup-enable
		tableResult.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		sqlFilter.setTable(tableResult);
		
		compositeTail = new Composite(compositeBase, SWT.NONE);
		GridLayout gl_compositeTail = new GridLayout(1, false);
		gl_compositeTail.verticalSpacing = 2;
		gl_compositeTail.horizontalSpacing = 2;
		gl_compositeTail.marginHeight = 2;
		gl_compositeTail.marginWidth = 2;
		compositeTail.setLayout(gl_compositeTail);
		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnDdlSourceView = new Button(compositeTail, SWT.NONE);
		btnDdlSourceView.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					DDLScriptManager scriptManager = new DDLScriptManager(userDB, PublicTadpoleDefine.OBJECT_TYPE.TABLES);
					FindEditorAndWriteQueryUtil.run(userDB, scriptManager.getScript(tableDao), PublicTadpoleDefine.OBJECT_TYPE.TABLES);
				} catch(Exception ee) {
					MessageDialog.openError(null, Messages.get().Confirm, ee.getMessage());
				}
			}
		});
		btnDdlSourceView.setText(Messages.get().TableDirectEditorComposite_btnDdlSourceView_text);
		
		initBusiness();
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
	}
	
	/**
	 * 에디터에 처음 데이터 호출할때로 초기화한다.
	 */
	public void refreshEditor() {
		initBusiness();
		initButtonCtrl();
	}
	
	/**
	 * 테이블에 쿼리 하고, pk,fk 키의 값을 index로 매칭합니다.
	 */
	private void initBusiness() {
		
		String strWhere = StringUtils.trimToEmpty(textWhere.getText());
		String strOrder = StringUtils.trimToEmpty(textOrderBy.getText());
		
		try {
			primaryKeyListIndex.clear();
			// 쿼리 실행
			runSQLSelect(strWhere, strOrder);
			
			// 컬럼 중에 키 컬럼이 있는지 검사합니다.
			for(int i=0; i<mapColumns.size(); i++) {
				
				if(primaryKEYListString.get(mapColumns.get(i)) == null) continue;
				
				if(primaryKEYListString.get(mapColumns.get(i))) {
					primaryKeyListIndex.add(i);
					primaryKEYIntStrList.put(i, mapColumns.get(i));
				}
			}

			// 화면에 데이터를 보여준다.
			resultView();
			
		} catch(Exception e) {
			logger.error("Data moidfying..", e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().TableViewerEditPart_2, errStatus); //$NON-NLS-1$
			return;
		}
		
		// google analytic
		AnalyticCaller.track("TableDirectEditorComposite"); //$NON-NLS-1$
	}

	/**
	 * 테이블에 쿼리를 실행합니다.
	 * 
	 * 1) ResultSetMetaData를 사용하여 데이터 컬럼 항목을 저장합니다.
	 * 
	 * @param strWhere
	 * @param strOrderBy
	 */
	private void runSQLSelect(String strWhere, String strOrderBy) throws Exception {
		String requestQuery = "SELECT "; //$NON-NLS-1$
		
		if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT) {
			requestQuery += " rowid, "; //$NON-NLS-1$
		} else if(userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
			requestQuery += " ctid, "; //$NON-NLS-1$
		}
		List<TableColumnDAO> tmpTableColumns = TadpoleObjectQuery.getTableColumns(userDB, tableDao);
		for(int i=0 ; i<tmpTableColumns.size(); i++) {
			TableColumnDAO tabledao = tmpTableColumns.get(i);
			requestQuery += tabledao.getName();
			if(i < (tmpTableColumns.size()-1)) requestQuery += ","; //$NON-NLS-1$
		}
		
		requestQuery += " FROM " + SQLUtil.getTableName(userDB, tableDao);
		
		if(!"".equals( strWhere )) requestQuery += " where " + strWhere; //$NON-NLS-1$ //$NON-NLS-2$
		if(!"".equals( strOrderBy )) requestQuery += " order by " + strOrderBy; //$NON-NLS-1$ //$NON-NLS-2$
		if(logger.isDebugEnabled()) logger.debug("Last query is " + requestQuery);
					
		ResultSet rs = null;
		java.sql.Connection javaConn = null;
		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			PreparedStatement stmt = null;
			stmt = javaConn.prepareStatement(requestQuery);
			stmt.setMaxRows(GetPreferenceGeneral.getSelectLimitCount());
			
			rs = stmt.executeQuery();
			
			// table column의 정보
			ResultSetMetaData  rsm = rs.getMetaData();
			int columnCount = rsm.getColumnCount();
			for(int i=0; i<rsm.getColumnCount(); i++) {
//				if(logger.isDebugEnabled()) logger.debug(i + "[type]" + rsm.getColumnClassName(i+1) ); //$NON-NLS-1$
				tableDataTypeList.put(i, rsm.getColumnClassName(i+1));
			}
			
			// rs set의 데이터 정
			tableDataList = new ArrayList<Map<Integer, Object>>();
			originalDataList = new ArrayList<Map<Integer, Object>>();
			HashMap<Integer, Object> tmpRs = null;
			
			mapColumns = ResultSetUtils.getColumnName(rs);
			
			while(rs.next()) {
				tmpRs = new HashMap<Integer, Object>();
				
				/** column modify info */
				tmpRs.put(0, TbUtils.COLUMN_MOD_TYPE.NONE.toString());
				
				for(int i=1;i<columnCount+1; i++) {					
					try {
						String strValue = rs.getString(i) == null?"":rs.getString(i);
//						System.out.println("ogiginal: "+ strValue);
//						strValue = StringEscapeUtils.unescapeHtml(strValue);
//						System.out.println("unescapeHtml: "+ strValue);
//						strValue = StringEscapeUtils.unescapeXml(strValue);
//						System.out.println("unescapeXml: "+ strValue);
						strValue = StringEscapeUtils.escapeXml(strValue);
						
						tmpRs.put(i, strValue); //$NON-NLS-1$
					} catch(Exception e) {
						logger.error("ResutSet fetch error", e); //$NON-NLS-1$
						tmpRs.put(i, ""); //$NON-NLS-1$
					}
				}
				
				tableDataList.add(tmpRs);
				// 원본을 보존하여 update where 로 활용합니다.
				Map<Integer, Object> clondRs = (Map<Integer, Object>)tmpRs.clone();
				originalDataList.add(clondRs);
			}

		} finally {
			try { rs.close(); } catch(Exception e) {}
			try { javaConn.close(); } catch(Exception e){}
		}
	}
	
	/**
	 * 결과를 테이블에 출력합니다.
	 * 
	 */
	public void resultView() {
		// table data의 소
		sqlSorter = new SQLResultSorter(-999);
		
		createTableColumn(sqlResultTableViewer, mapColumns, sqlSorter);
		sqlResultTableViewer.setLabelProvider( new TableEditorLabelProvider(userDB) );
		sqlResultTableViewer.setContentProvider(new SQLResultContentProvider(tableDataList) );
		sqlResultTableViewer.setInput(tableDataList);
		sqlResultTableViewer.setSorter(sqlSorter);
		
		// 결과 후처리 
		tableResult.setToolTipText(tableDataList.size() + Messages.get().Rows);
		sqlFilter.setTable(tableResult);
		
		// Pack the columns
		TableUtil.packTable(tableResult);
	}
	
	/**
	 * 필터를 설정합니다.
	 */
	private void setFilter() {
		sqlFilter.setFilter(textFilter.getText());
		sqlResultTableViewer.addFilter( sqlFilter );
	}
	
	/**
	 * table의 Column을 생성한다.
	 */
	public void createTableColumn(final TableViewer tableViewer, final Map<Integer, String> mapColumns, final SQLResultSorter tableSorter) {
		// 기존 column을 삭제한다.
		Table table = tableViewer.getTable();
		int columnCount = table.getColumnCount();
		for(int i=0; i<columnCount; i++) {
			table.getColumn(0).dispose();
		}
		
		try {
			// column info
			final TableViewerColumn tableColumnInfo = new TableViewerColumn(tableViewer, SWT.LEFT);
			tableColumnInfo.getColumn().setText( Messages.get().DataStatus );
			tableColumnInfo.getColumn().setResizable(true);
			tableColumnInfo.getColumn().setMoveable(false);
			
			// 0 번째 컬럼은 데이터 수정 타입 NONE 
			// 오라클, PGSQL인 경우 1 번째 컬럼은 업데이트를 위해 ROWID, CID를 조회 하여서 보내주지 않도록 하였다. 
			int intColStartIndex = 1;
			if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT || userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
				intColStartIndex++;
			}
			
			// reset column 
			for(int i=intColStartIndex; i<mapColumns.size()+1; i++) {
				final int index = i;
				
				final TableViewerColumn tableColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
				tableColumn.getColumn().setText( mapColumns.get(index-1) );
				tableColumn.getColumn().setResizable(true);
				tableColumn.getColumn().setMoveable(false);
				
				tableColumn.getColumn().addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						tableSorter.setColumn(index);
						int dir = tableViewer.getTable().getSortDirection();
						if (tableViewer.getTable().getSortColumn() == tableColumn.getColumn()) {
							dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
						} else {
							dir = SWT.DOWN;
						}
						
						tableViewer.getTable().setSortDirection(dir);
						tableViewer.getTable().setSortColumn(tableColumn.getColumn());
						tableViewer.refresh();
					}
				});
				
				tableColumn.setEditingSupport(new TextViewerEditingSupport(this, index, tableViewer));
			}	// end for
			
		} catch(Exception e) { 
			logger.error(Messages.get().TableEditPart_8, e);
		}
	}

	/**
	 * save table data
	 */
	private void saveTableData() {
		String strQuery = getChangeQuery();
		if("".equals(strQuery)) return; //$NON-NLS-1$
		
		String strShowEditor = "";
		String[] querys = SQLTextUtil.delLineChar(strQuery).split(";"); //$NON-NLS-1$
		
		boolean isUpdateOrDelete = false;
		for(int i=0; i<querys.length; i++) {
			if(logger.isDebugEnabled()) logger.debug("exe query [" + querys[i] + "]"); //$NON-NLS-1$ //$NON-NLS-2$
			strShowEditor += querys[i] + ";" + PublicTadpoleDefine.LINE_SEPARATOR;
			
			if(StringUtils.startsWithIgnoreCase(querys[i], "update") ||  //$NON-NLS-1$
					StringUtils.startsWithIgnoreCase(querys[i], "delete")) isUpdateOrDelete = true; //$NON-NLS-1$
		}
		
		String changedSQL = "";
				
		int isUpdateed = IDialogConstants.CANCEL_ID;
		if(isUpdateOrDelete) {
			DBDefine selectDB = userDB.getDBDefine();
			if(selectDB == DBDefine.SQLite_DEFAULT || 
				selectDB == DBDefine.CUBRID_DEFAULT ||
				selectDB == DBDefine.MSSQL_DEFAULT ||
				selectDB == DBDefine.MSSQL_8_LE_DEFAULT) {
				
				DirectChangeDialog dialog = new DirectChangeDialog(getShell(), Messages.get().TableDirectEditorComposite_17, strShowEditor);
				isUpdateed = dialog.open();
				changedSQL = dialog.getSQL();
			} else {
				DirectChangeDialog dialog = new DirectChangeDialog(getShell(), Messages.get().TableDirectEditorComposite_19, strShowEditor);
				isUpdateed = dialog.open();
				changedSQL = dialog.getSQL();
			}
		} else {
			DirectChangeDialog dialog = new DirectChangeDialog(getShell(), Messages.get().TableDirectEditorComposite_19, strShowEditor);
			isUpdateed = dialog.open();
			changedSQL = dialog.getSQL();
		}
		
		if(isUpdateed == IDialogConstants.CANCEL_ID) return;
		if("".equals(changedSQL)) return; //$NON-NLS-1$
		
		querys = SQLTextUtil.delLineChar(changedSQL).split(";"); //$NON-NLS-1$
		try {
			RequestQuery reqQuery = new RequestQuery(userDB, changedSQL, OBJECT_TYPE.TABLES, QUERY_MODE.QUERY, EXECUTE_TYPE.ALL, true);
			ExecuteBatchSQL.runSQLExecuteBatch(Messages.get().MainEditor_21, Arrays.asList(querys), reqQuery, userDB, userDB.getRole_id(), 1000, SessionManager.getEMAIL());
			
			// 정상적으로 모든 결과 처리 완료.
			initBusiness();
			initButtonCtrl();
		} catch(Exception e) {
			TDBErroDialog dialog = new TDBErroDialog(getShell(), "Update Fail",  Messages.get().TableViewerEditPart_10 + e.getMessage());
			dialog.open(); 
		}	
	}
	
	/**
	 * 신규 데이터 추가
	 */
	private void insertRow() {
		HashMap<Integer, Object> tmpRs = new HashMap<Integer, Object>();
		
		/** column modify info */
		tmpRs.put(0, TbUtils.getColumnText(TbUtils.COLUMN_MOD_TYPE.INSERT));
		
		for(int i=1;i<mapColumns.size()+1; i++) {
			tmpRs.put(i, ""); //$NON-NLS-1$
		}
		tableDataList.add(tmpRs);
		
		setModifyButtonControl();
		
		sqlResultTableViewer.refresh(tableDataList);
	}
	
	/**
	 * 선택 로우 삭제 처리
	 * @param selObject
	 */
	public void deleteRow(IStructuredSelection is) {
		for(Object selObject : is.toArray()) {
			HashMap<Integer, String> data = (HashMap<Integer, String>)selObject;
			if(TbUtils.isInsert(data.get(0))) {
				tableDataList.remove(data);			
			} else {
				data.put(0, TbUtils.getColumnText(TbUtils.COLUMN_MOD_TYPE.DELETE) );
			}
		}
		
		sqlResultTableViewer.refresh(tableDataList);
		setModifyButtonControl();
	}
	
	/**
	 * 버튼을 초기화합니다.
	 */
	public void initButtonCtrl() {
		tltmDelete.setEnabled(false);
		tltmSave.setEnabled(false);
	}
	
	/**
	 * query수정되어 버튼을 조덜합니다. 
	 * 
	 * @param updateQuery
	 */
	public void setModifyButtonControl() {
		boolean isModifyed = false;
		
		for(int i=0; i<tableDataList.size(); i++) {
			Map<Integer, Object> tmpRs = tableDataList.get(i);
			if(TbUtils.isDelete( tmpRs.get(0).toString() ) || TbUtils.isUpdate( tmpRs.get(0).toString() ) || TbUtils.isInsert( tmpRs.get(0).toString() )) {
				isModifyed = true;
			}
		}
		tltmSave.setEnabled(isModifyed);
		
		// delete button enabled
		if(sqlResultTableViewer.getSelection().isEmpty()) tltmDelete.setEnabled(false);
		else tltmDelete.setEnabled(true);
	}
	
	/**
	 * 수정된 데이터의 쿼리를 리턴한다.
	 * @return
	 */
	public String getChangeQuery() {
		StringBuffer sbQuery = new StringBuffer();
		
		// 전체 데이터 건수를 돌면서...
		for(int i=0; i<tableDataList.size(); i++) {
			
			Map<Integer, Object> tmpRs = tableDataList.get(i);
			// 0번째 컬럼 중에 update, delete, insert 인 것을 찾는다.
			if(TbUtils.isDelete( tmpRs.get(0).toString() )) {
				sbQuery.append(makeDelete(i, tmpRs));
				
			} else if(TbUtils.isUpdate( tmpRs.get(0).toString() )) {
				sbQuery.append(makeUpdate(i, tmpRs));

			} else if(TbUtils.isInsert( tmpRs.get(0).toString() )) {
				sbQuery.append(makeInsert(tmpRs));
			}
			
		}
		
		return sbQuery.toString();
	}
	
	
	/**
	 * where 절 이하의 쿼리를 생성한다. 
	 * 
	 * @param data
	 * @param tmpRs
	 * 
	 * @return
	 */
	private String getWhereMake(int rowSeq, Map<Integer, Object> tmpRs) {
		String strWhere = ""; //$NON-NLS-1$
		
		// original data
		Map<Integer, Object> orgRs = originalDataList.get(rowSeq);
		
		/**
		 * oracle 은 rowid
		 * pgsql 은 cid 로 처리합니다.
		 * 
		 */
		if(userDB.getDBDefine() == DBDefine.ORACLE_DEFAULT) {
			strWhere = " rowid = '" + orgRs.get(1) + "'"; //$NON-NLS-1$ //$NON-NLS-2$
		} else if( userDB.getDBDefine() == DBDefine.POSTGRE_DEFAULT) {
			strWhere = " ctid = '" + orgRs.get(1) + "'"; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			if(!primaryKeyListIndex.isEmpty()) {
				for(int i=0; i<primaryKeyListIndex.size(); i++) {
					int keyIndex = primaryKeyListIndex.get(i);
					strWhere += primaryKEYIntStrList.get(keyIndex) + " = " + SQLUtil.makeQuote(TbUtils.getOriginalData(orgRs.get(keyIndex+1).toString())); //$NON-NLS-1$ //$NON-NLS-2$
					
					if(i < (primaryKeyListIndex.size()-1)) strWhere += " AND "; //$NON-NLS-1$
				}
			} else {
				for(int i=1; i<tmpRs.size(); i++) {
					strWhere += mapColumns.get(i-1) + " = " + SQLUtil.makeQuote(TbUtils.getOriginalData(orgRs.get(i).toString()) ); //$NON-NLS-1$ //$NON-NLS-2$
					
					if(i < (tmpRs.size()-1)) strWhere += " AND "; //$NON-NLS-1$
				}
				
			}
		}
		
		return strWhere;
	}
	
	/**
	 * delete 문장 생성
	 * 
	 * @param tmpRs
	 * @return
	 */
	private String makeDelete(int rowSeq, Map<Integer, Object> tmpRs) {
		String deleteStmt = "DELETE FROM " + tableDao.getSysName(); //$NON-NLS-1$
		deleteStmt += " WHERE  (" + getWhereMake(rowSeq, tmpRs) + ") "; //$NON-NLS-1$ //$NON-NLS-2$
		
		if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT || userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
			deleteStmt += "LIMIT 1"; //$NON-NLS-1$
		}
		
		return deleteStmt + PublicTadpoleDefine.SQL_DELIMITER ;
	}
	
	/**
	 * update 문장 생성.
	 * 
	 * @param tmpRs
	 * @return
	 */
	private String makeUpdate(int rowSeq, Map<Integer, Object> tmpRs) {
		String updateStmt = "UPDATE " + tableDao.getSysName() +   //$NON-NLS-1$ //$NON-NLS-2$
				" SET "; //$NON-NLS-1$
		
		// 수정된 컬럼의 값을 넣는다.
		// 0 번째 컬럼은 데이터 수정 유무이므로 .
		for(int i=1; i<tmpRs.size(); i++) {
			if(TbUtils.isModifyData( tmpRs.get(i).toString() )) {
				updateStmt += mapColumns.get(i-1) + " = " + SQLUtil.makeQuote(TbUtils.getOriginalData(tmpRs.get(i).toString())) + ", "; //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		updateStmt = StringUtils.chompLast(updateStmt, ", "); //$NON-NLS-1$
		
		updateStmt += " WHERE (" + getWhereMake(rowSeq, tmpRs) + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		
		if(userDB.getDBDefine() == DBDefine.MYSQL_DEFAULT || userDB.getDBDefine() == DBDefine.MARIADB_DEFAULT) {
			updateStmt += " LIMIT 1"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		return updateStmt + PublicTadpoleDefine.SQL_DELIMITER;
	}
	
	/**
	 * insert 문장 생성.
	 * 
	 * @param tmpRs
	 * @return
	 */
	private String makeInsert(Map<Integer, Object> tmpRs) {
		String insertStmt = "INSERT INTO " + tableDao.getSysName() + "("; //$NON-NLS-1$ //$NON-NLS-2$
		
		// 수정된 컬럼 리스트를 나열한다.
		boolean isModifyColumn = false;
		for(int i=1; i<tmpRs.size(); i++) {
			if(TbUtils.isModifyData( tmpRs.get(i).toString() )) {
				insertStmt += mapColumns.get(i-1) + ", "; //$NON-NLS-1$
				isModifyColumn = true;
			}
		}
		// 수정 된 컬럼이 없다. 즉 신규 추가되어 컬럼이 수정되지 않았다.
		if(!isModifyColumn) return ""; //$NON-NLS-1$
		
		insertStmt = StringUtils.chompLast(insertStmt, ", "); //$NON-NLS-1$
		insertStmt += ") VALUES ("; //$NON-NLS-1$
		
		// 수정된 값을 입력한다.
		for(int i=1; i<tmpRs.size(); i++) {
			if(TbUtils.isModifyData( tmpRs.get(i).toString() )) insertStmt += SQLUtil.makeQuote(TbUtils.getOriginalData(tmpRs.get(i).toString())) + ", "; //$NON-NLS-1$ //$NON-NLS-2$
		}
		insertStmt = StringUtils.chompLast(insertStmt, ", "); //$NON-NLS-1$
		insertStmt += ");"; //$NON-NLS-1$
		
		return insertStmt;
	}
	
	/**
	 * 현재 접속 디비
	 * @return
	 */
	public UserDBDAO getUserDB() {
		return userDB;
	}
	
	/**
	 * 컬럼 타입
	 * @return
	 */
	public HashMap<Integer, String> getTableDataTypeList() {
		return tableDataTypeList;
	}

	@Override
	protected void checkSubclass() {
	}
}
