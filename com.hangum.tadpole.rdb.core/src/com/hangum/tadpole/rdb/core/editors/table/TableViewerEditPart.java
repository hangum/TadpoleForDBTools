///*******************************************************************************
// * Copyright (c) 2013 hangum.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the GNU Lesser Public License v2.1
// * which accompanies this distribution, and is available at
// * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
// * 
// * Contributors:
// *     hangum - initial API and implementation
// ******************************************************************************/
//package com.hangum.tadpole.rdb.core.editors.table;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
//import org.eclipse.core.runtime.IProgressMonitor;
//import org.eclipse.core.runtime.IStatus;
//import org.eclipse.core.runtime.Status;
//import org.eclipse.jface.dialogs.MessageDialog;
//import org.eclipse.jface.viewers.ISelectionChangedListener;
//import org.eclipse.jface.viewers.IStructuredSelection;
//import org.eclipse.jface.viewers.SelectionChangedEvent;
//import org.eclipse.jface.viewers.TableViewer;
//import org.eclipse.jface.viewers.TableViewerColumn;
//import org.eclipse.rap.rwt.RWT;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.KeyAdapter;
//import org.eclipse.swt.events.KeyEvent;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Table;
//import org.eclipse.swt.widgets.Text;
//import org.eclipse.swt.widgets.ToolBar;
//import org.eclipse.swt.widgets.ToolItem;
//import org.eclipse.ui.IEditorInput;
//import org.eclipse.ui.IEditorSite;
//import org.eclipse.ui.PartInitException;
//import org.eclipse.ui.part.EditorPart;
//
//import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
//import com.hangum.tadpole.commons.sql.TadpoleSQLManager;
//import com.hangum.tadpole.commons.sql.util.PartQueryUtil;
//import com.hangum.tadpole.commons.sql.util.SQLUtil;
//import com.hangum.tadpole.dao.mysql.TableColumnDAO;
//import com.hangum.tadpole.dao.system.UserDBDAO;
//import com.hangum.tadpole.define.DB_Define;
//import com.hangum.tadpole.exception.dialog.ExceptionDetailsErrorDialog;
//import com.hangum.tadpole.rdb.core.Activator;
//import com.hangum.tadpole.rdb.core.Messages;
//import com.hangum.tadpole.rdb.core.editors.main.SQLTextUtil;
//import com.hangum.tadpole.rdb.core.editors.objects.table.DBTableEditorInput;
//import com.hangum.tadpole.rdb.core.editors.objects.table.TbUtils;
//import com.hangum.tadpole.rdb.core.editors.objects.table.TextViewerEditingSupport;
//import com.hangum.tadpole.rdb.core.editors.objects.table.TbUtils.TABLE_MOD_TYPE;
//import com.hangum.tadpole.util.XMLUtils;
//import com.hangum.tadpole.util.tables.SQLResultContentProvider;
//import com.hangum.tadpole.util.tables.SQLResultFilter;
//import com.hangum.tadpole.util.tables.SQLResultLabelProvider;
//import com.hangum.tadpole.util.tables.SQLResultSorter;
//import com.hangum.tadpole.util.tables.TableUtil;
//import com.ibatis.sqlmap.client.SqlMapClient;
//
///**
// * <pre>
// * 
// * 		<title>테이블 데이터가 실제 테이블에 보여짐</title>
// * 
// * 		모든 데이터는 트렌잭션 처리를 합니다.
// * 		데이터 타입의 검사는 ResultSetMetaData의 getColumnType으로 검사합니다.
// * 
// * 	- 데이터 추가
// * 		사전 조건) foreign key가 존재 하지 않아야 한다.
// * 			1-1) 데이터 수정은 Date, 숫자 항목의 key type만 검사하여 입력한다.
// *
// * 	- 데이터 수정
// * 		사전 조건) primary key가 없다 면 읽기 모드만 가능
// *  		1-1) 데이터 수정은 Date, 숫자 항목의 key type만 검사하여 입력한다.
// *  
// *	- 데이터 삭제
// *		사전 조건) primary key가 존재해야한다.
// *
// * </pre>
// * 
// * @author hangum
// *
// */
//public class TableViewerEditPart extends EditorPart {
//
//	public static final String ID = "com.hangum.tadpole.rdb.core.editors.table.edit"; //$NON-NLS-1$
//	private static final Logger logger = Logger.getLogger(TableViewerEditPart.class);
//	
//	private boolean isDirty = false;
//	
//	/** 현재 테이블의 상태 */
//	private TABLE_MOD_TYPE modifyType = TABLE_MOD_TYPE.NONE;
//		
//	private String initTableNameStr;
//	private UserDBDAO userDB;
//	private List<TableColumnDAO> columnList;
//	/** pk key의 이름을 가지고 있습니다 */
//	private Map<String, Boolean> primaryKEYListString = new HashMap<String, Boolean>();
//	/** pk key의 이름 의 index를 가지고 있습니다 */
//	private List<Integer> primaryKeyListIndex = new ArrayList<Integer>();
//	/** pk key의 index별 이름 */
//	private Map<Integer, String> primaryKEYIntStrList = new HashMap<Integer, String>();
//
//	private Table tableResult;
//	private TableViewer sqlResultTableViewer;
//	private SQLResultFilter sqlFilter = new SQLResultFilter();
//	private SQLResultSorter sqlSorter;
//	
//	/** query  HashMap -- table 컬럼의 정보 다음과 같습니다. <column index, Data> */
//	private HashMap<Integer, String> mapColumns = null;
//	/** query 의 결  -- table의 데이터는 다음과 같습니다. <column index, Data> */
//	private List<HashMap<Integer, Object>> tableDataList = new ArrayList<HashMap<Integer, Object>>();
//	/** 원본 데이터를 가지고 있습니다 */
//	private List<HashMap<Integer, Object>> originalDataList = new ArrayList<HashMap<Integer, Object>>();
//	/** 데이터의 데이터 타입 */
//	private HashMap<Integer, String> tableDataTypeList = new HashMap<Integer, String>();
//	
//	private Text textFilter;
//	private Composite composite;
//	private ToolBar toolBar;
//	private ToolItem tltmSave;
//	private ToolItem tltmDelete;
//	private ToolItem tltmInsert;
//	private ToolItem tltmTablecomment;
//	
//	private Text textWhere;
//	
//	public TableViewerEditPart() {
//		super();
//	}
//
//	/**
//	 * Create contents of the editor part.
//	 * @param parent
//	 */
//	@Override
//	public void createPartControl(Composite parent) {
//		GridLayout gl_parent = new GridLayout(1, false);
//		gl_parent.marginHeight = 0;
//		parent.setLayout(gl_parent);
//		
//		composite = new Composite(parent, SWT.NONE);
//		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		composite.setLayout(new GridLayout(1, false));
//		
//		toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
//		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		
//		tltmSave = new ToolItem(toolBar, SWT.NONE);
//		tltmSave.setEnabled(false);
//		tltmSave.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				
//				saveTableData();
//				
//			}
//		});
//		tltmSave.setText(Messages.TableEditPart_0);
//		
//		tltmInsert = new ToolItem(toolBar, SWT.NONE);
//		tltmInsert.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				if(modifyType == TABLE_MOD_TYPE.EDITOR) insertRow();
//			}
//		});
//		tltmInsert.setText(Messages.TableEditPart_tltmInsert_text);
//		tltmInsert.setEnabled(false);
//		
//		tltmDelete = new ToolItem(toolBar, SWT.NONE);
//		tltmDelete.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				
//				IStructuredSelection is = (IStructuredSelection)sqlResultTableViewer.getSelection();
//				if(!is.isEmpty()) {
//					deleteRow(is.getFirstElement());					
//				}
//				
//			}
//		});
//		tltmDelete.setEnabled(false);
//		tltmDelete.setText(Messages.TableEditPart_1);
//		
//		tltmTablecomment = new ToolItem(toolBar, SWT.NONE);
//		tltmTablecomment.setText(TbUtils.NONE_MSG);
//		
//		Composite compositeBody = new Composite(parent, SWT.NONE);
//		compositeBody.setLayout(new GridLayout(2, false));
//		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		
//		Label lblWhere = new Label(compositeBody, SWT.NONE);
//		lblWhere.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//		lblWhere.setText(Messages.TableEditPart_lblWhere_text);
//		
//		textWhere = new Text(compositeBody, SWT.BORDER);
//		textWhere.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//				if(e.keyCode == SWT.Selection) changeWhere(textWhere.getText());
//			}
//		});
//		textWhere.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		
//		Label lblNewLabel = new Label(compositeBody, SWT.NONE);
//		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//		lblNewLabel.setText(Messages.TableEditPart_3);
//		
//		textFilter = new Text(compositeBody, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
//		textFilter.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyPressed(KeyEvent e) {
//				if(e.keyCode == SWT.Selection) setFilter();
//			}
//		});
//		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		
//		sqlResultTableViewer = new TableViewer(compositeBody, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
//		sqlResultTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
//			public void selectionChanged(SelectionChangedEvent event) {
//				if(primaryKeyListIndex.size() >= 1) tltmDelete.setEnabled(true);
//			}
//		});
//		tableResult = sqlResultTableViewer.getTable();
//		tableResult.setHeaderVisible(true);
//		tableResult.setLinesVisible(true);
//		tableResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
//		
//		// table markup-enable
//		tableResult.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
//		
//		sqlFilter.setTable(tableResult);
//		
//		initBusiness(StringUtils.trimToEmpty(textWhere.getText()));
//	}
//	
//	/**
//	 * 에디터에 처음 데이터 호출할때로 초기화한다.
//	 */
//	public void refreshEditor() {
//		initBusiness(StringUtils.trimToEmpty(textWhere.getText()));
//		initButtonCtrl();
//	}
//	
//	/**
//	 * where 조건 추가
//	 * 
//	 * @param where
//	 */
//	private void changeWhere(String where) {
//			
//		// 쿼리를 수정했으면...
//		if(!"".equals(getChangeQuery()) ) { //$NON-NLS-1$
//			if(MessageDialog.openConfirm(null, Messages.TableEditPart_5, Messages.TableViewerEditPart_1)) {
//				initBusiness(where);
//			}
//		} else {
//			initBusiness(where);
//		}
//	}
//	
//	/**
//	 * 테이블에 쿼리 하고, pk,fk 키의 값을 index로 매칭합니다.
//	 */
//	private void initBusiness(String whereSQL) {
//		try {
//			primaryKeyListIndex.clear();
//			// 쿼리 실행
//			runSQLSelect(whereSQL);
//			
//			// 컬럼 중에 키 컬럼이 있는지 검사합니다.
//			for(int i=0; i<mapColumns.size(); i++) {
//				
//				if(primaryKEYListString.get(mapColumns.get(i)) == null) continue;
//				
//				if(primaryKEYListString.get(mapColumns.get(i))) {
//					primaryKeyListIndex.add(i);
//					primaryKEYIntStrList.put(i, mapColumns.get(i));
//				}
//			}
//			// 인덱스가 존재하면  수정가능 모드
//			if(primaryKeyListIndex.size() >= 1) {
//				modifyType = TABLE_MOD_TYPE.EDITOR;
//				tltmTablecomment.setText(TbUtils.EDITOR_MSG);
//				tltmInsert.setEnabled(true);
//			}
//			
//			// 화면에 데이터를 보여준다.
//			resultView();
//			
//		} catch(Exception e) {
//			logger.error(Messages.TableEditPart_4, e);
//			
//			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
//			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", Messages.TableViewerEditPart_2, errStatus); //$NON-NLS-1$
//			return;
//		}
//		
//	}
//
//	/**
//	 * 테이블에 쿼리를 실행합니다.
//	 * 
//	 * 1) ResultSetMetaData를 사용하여 데이터 컬럼 항목을 저장합니다.
//	 * 
//	 * @param requestQuery
//	 * @param startResultPos
//	 * @param endResultPos
//	 */
//	private void runSQLSelect(String whereSQL) throws Exception {
//		String requestQuery = "SELECT * FROM " + initTableNameStr; //$NON-NLS-1$
//		if(!"".equals( whereSQL )) { //$NON-NLS-1$
//			requestQuery += " where " + whereSQL; //$NON-NLS-1$
//		}
//		
//		// 임시코드를 넣습니다.
//		//
//		//
//		//
//		//
////		if(DBDefine.MSSQL_DEFAULT != DBDefine.getDBDefine(userDB.getTypes())) {
//			requestQuery = PartQueryUtil.makeSelect(userDB, requestQuery, 0, 500);
////		} else {
////			requestQuery = requestQuery + " top 500";
////		}
//		
//		ResultSet rs = null;
//		java.sql.Connection javaConn = null;
//		
//		try {
//			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
//			javaConn = client.getDataSource().getConnection();
//			
//			PreparedStatement stmt = null;
//			stmt = javaConn.prepareStatement(requestQuery);
//			
//			rs = stmt.executeQuery();//Query( selText );
//			
//			// table column의 정보
//			ResultSetMetaData  rsm = rs.getMetaData();
//			int columnCount = rsm.getColumnCount();
//			for(int i=0; i<rsm.getColumnCount(); i++) {
////				if(logger.isDebugEnabled()) logger.debug(i + "[type]" + rsm.getColumnClassName(i+1) ); //$NON-NLS-1$
//				tableDataTypeList.put(i, rsm.getColumnClassName(i+1));
//			}
//			
//			// rs set의 데이터 정
//			tableDataList = new ArrayList<HashMap<Integer, Object>>();
//			originalDataList = new ArrayList<HashMap<Integer, Object>>();
//			HashMap<Integer, Object> tmpRs = null;
//			
//			mapColumns = SQLUtil.mataDataToMap(rs);
//			
//			while(rs.next()) {
//				tmpRs = new HashMap<Integer, Object>();
//				
//				/** column modify info */
//				tmpRs.put(0, TbUtils.COLUMN_MOD_TYPE.NONE.toString());
//				
//				for(int i=1;i<columnCount+1; i++) {					
//					try {
//						tmpRs.put(i, XMLUtils.xmlToString(rs.getString(i) == null?"":rs.getString(i)));
//					} catch(Exception e) {
//						logger.error("ResutSet fetch error", e);
//						tmpRs.put(i, "");
//					}
//				}
//				
//				tableDataList.add(tmpRs);
//				// 원본을 보존하여 update where 로 활용합니다.
//				HashMap<Integer, Object> clondRs = (HashMap<Integer, Object>)tmpRs.clone();
//				originalDataList.add(clondRs);
//			}
//
//		} finally {
//			try { rs.close(); } catch(Exception e) {}
//			try { javaConn.close(); } catch(Exception e){}
//		}
//	}
//	
//	/**
//	 * 결과를 테이블에 출력합니다.
//	 * 
//	 */
//	public void resultView() {
//		// table data의 소
//		sqlSorter = new SQLResultSorter(-999);
//		
//		createTableColumn(sqlResultTableViewer, mapColumns, sqlSorter);
//		sqlResultTableViewer.setLabelProvider( new SQLResultLabelProvider() );
//		sqlResultTableViewer.setContentProvider(new SQLResultContentProvider(tableDataList) );
//		sqlResultTableViewer.setInput(tableDataList);
//		sqlResultTableViewer.setSorter(sqlSorter);
//		
//		// 결과 후처리 
//		tableResult.setToolTipText(tableDataList.size() + Messages.MainEditor_33);
//		sqlFilter.setTable(tableResult);
//		
//		// Pack the columns
//		TableUtil.packTable(tableResult);
//	}
//	
//	/**
//	 * 필터를 설정합니다.
//	 */
//	private void setFilter() {
//		sqlFilter.setFilter(textFilter.getText());
//		sqlResultTableViewer.addFilter( sqlFilter );
//	}
//	
//	/**
//	 * table의 Column을 생성한다.
//	 */
//	public void createTableColumn(final TableViewer tableViewer, final HashMap<Integer, String> mapColumns, final SQLResultSorter tableSorter) {
//		// 기존 column을 삭제한다.
//		Table table = tableViewer.getTable();
//		int columnCount = table.getColumnCount();
//		for(int i=0; i<columnCount; i++) {
//			table.getColumn(0).dispose();
//		}
//		
//		try {
//			// column info
//			final TableViewerColumn tableColumnInfo = new TableViewerColumn(tableViewer, SWT.LEFT);
//			tableColumnInfo.getColumn().setText( Messages.TableViewerEditPart_0 );
//			tableColumnInfo.getColumn().setResizable(true);
//			tableColumnInfo.getColumn().setMoveable(false);
//			tableColumnInfo.getColumn().setWidth(100);
//			
//			// reset column 
//			for(int i=1; i<mapColumns.size()+1; i++) {
//				final int index = i;
//				
//				final TableViewerColumn tableColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
//				tableColumn.getColumn().setText( mapColumns.get(index-1) );
//				tableColumn.getColumn().setResizable(true);
//				tableColumn.getColumn().setMoveable(false);
//				
//				tableColumn.getColumn().addSelectionListener(new SelectionAdapter() {
//					@Override
//					public void widgetSelected(SelectionEvent e) {
//						tableSorter.setColumn(index);
//						int dir = tableViewer.getTable().getSortDirection();
//						if (tableViewer.getTable().getSortColumn() == tableColumn.getColumn()) {
//							dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
//						} else {
//							dir = SWT.DOWN;
//						}
//						
//						tableViewer.getTable().setSortDirection(dir);
//						tableViewer.getTable().setSortColumn(tableColumn.getColumn());
//						tableViewer.refresh();
//					}
//				});
//				
//				if(modifyType == TABLE_MOD_TYPE.EDITOR) tableColumn.setEditingSupport(new TextViewerEditingSupport(this, index, tableViewer));
//			}	// end for
//			
//		} catch(Exception e) { 
//			logger.error(Messages.TableEditPart_8, e);
//		}
//		
//	}
//
//	@Override
//	public void setFocus() {
//		sqlResultTableViewer.getTable().setFocus();
//	}
//
//	@Override
//	public void doSave(IProgressMonitor monitor) {
//		saveTableData();
//	}
//	
//	private void saveTableData() {
//		// save 기능을 수행합니다.
//		java.sql.Connection javaConn = null;
//		Statement stmt = null;
//		String lastExeQuery = ""; //$NON-NLS-1$
//		
//		try {
//			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
//			javaConn = client.getDataSource().getConnection();
//
//			// sqlite에서는 forward cursor만 지원하여 블럭 처리 
//			stmt = javaConn.createStatement();//ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//			javaConn.setAutoCommit(false);
//
//			String[] querys = SQLTextUtil.delLineChar(getChangeQuery()).split(";"); //$NON-NLS-1$
//			for(int i=0; i<querys.length; i++) {
//				
//			//	logger.info("exe query [" + querys[i] + "]"); //$NON-NLS-1$ //$NON-NLS-2$
//				
//				lastExeQuery = querys[i] ;
//				stmt.execute(querys[i] );
//			}
//			
//			javaConn.commit();
//			
//			// 정상적으로 모든 결과 처리 완료.
//			tltmSave.setEnabled(false);
//			initBusiness(textWhere.getText());
//			isDirty = false;
//			
//			isDirty = false;
//			firePropertyChange(PROP_DIRTY);
//			
//		} catch(Exception ee) {
//			try { if(javaConn != null) javaConn.rollback(); } catch(SQLException roE) {};
//			
//			logger.error(Messages.TableViewerEditPart_7, ee);
//			
//			// 에러 났으므로 사후 처리.
//			MessageDialog.openError(null, Messages.TableViewerEditPart_3, "Query [ " + lastExeQuery + Messages.TableViewerEditPart_10 + ee.getMessage() + Messages.TableViewerEditPart_11); //$NON-NLS-2$
//			
//		} finally {
//			// connection을 원래대로 돌려 놓는다.
//			try { javaConn.setAutoCommit(true); } catch(Exception ee) {}
//			
//			try {if(stmt != null) stmt.close();} catch(Exception ee) {}
//			try {if(javaConn != null) javaConn.close();} catch(Exception ee) {}
//		}
//	}
//
//	@Override
//	public void doSaveAs() {
//	}
//
//	@Override
//	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
//		setSite(site);
//		setInput(input);
//		
//		DBTableEditorInput qei = (DBTableEditorInput)input;
//		userDB = qei.getUserDB();
//		setPartName("[" + qei.getName() + "] Table Editor");		 //$NON-NLS-1$
//		
//		initTableNameStr = qei.getName();
//		columnList = qei.getShowTableColumns();
//		for (TableColumnDAO columnDAO : columnList) {
//			if(DB_Define.isPK(columnDAO.getKey())) {
//				primaryKEYListString.put(columnDAO.getField(), true);
//			} else {
//				primaryKEYListString.put(columnDAO.getField(), false);
//			}
//		}
//	}
//
//	@Override
//	public boolean isDirty() {
//		return isDirty;
//	}
//
//	@Override
//	public boolean isSaveAsAllowed() {
//		return false;
//	}
//	
//	/**
//	 * 신규 데이터 추가
//	 */
//	private void insertRow() {
//		HashMap<Integer, Object> tmpRs = new HashMap<Integer, Object>();
//		
//		/** column modify info */
//		tmpRs.put(0, TbUtils.getColumnText(TbUtils.COLUMN_MOD_TYPE.INSERT));
//		
//		for(int i=1;i<mapColumns.size()+1; i++) {
//			tmpRs.put(i, ""); //$NON-NLS-1$
//		}
//		tableDataList.add(tmpRs);
//		
//		setModifyButtonControl();
//		
//		sqlResultTableViewer.refresh(tableDataList);
//	}
//	
//	/**
//	 * 선택 로우 삭제 처리
//	 * @param selObject
//	 */
//	public void deleteRow(Object selObject) {
//		HashMap<Integer, String> data = (HashMap<Integer, String>)selObject;
//		if(TbUtils.isInsert(data.get(0))) {
//			tableDataList.remove(data);			
//		} else {
//			data.put(0, TbUtils.getColumnText(TbUtils.COLUMN_MOD_TYPE.DELETE) );
//		}
//		
//		setModifyButtonControl();
//		
//		sqlResultTableViewer.refresh(tableDataList);
//	}
//	
//	/**
//	 * 버튼을 초기화합니다.
//	 */
//	public void initButtonCtrl() {
//		tltmDelete.setEnabled(false);
//		tltmInsert.setEnabled(false);
//		
//		tltmSave.setEnabled(false);
//	}
//	
//	/**
//	 * query수정되어 버튼을 조덜합니다. 
//	 * 
//	 * @param updateQuery
//	 */
//	public void setModifyButtonControl() {
//		isDirty = true;
//		firePropertyChange(PROP_DIRTY);
//		tltmSave.setEnabled(true);
//	}
//	
//	/**
//	 * 수정된 데이터의 쿼리를 리턴한다.
//	 * @return
//	 */
//	public String getChangeQuery() {
//		StringBuffer sbQuery = new StringBuffer();
//		
//		// 전체 데이터 건수를 돌면서...
//		for(int i=0; i<tableDataList.size(); i++) {
//			
//			HashMap<Integer, Object> tmpRs = tableDataList.get(i);
//			// 0번째 컬럼 중에 update, delete, insert 인 것을 찾는다.
//			if(TbUtils.isDelete( tmpRs.get(0).toString() )) {
//				sbQuery.append(makeDelete(i, tmpRs));
//				
//			} else if(TbUtils.isUpdate( tmpRs.get(0).toString() )) {
//				sbQuery.append(makeUpdate(i, tmpRs));
//
//			} else if(TbUtils.isInsert( tmpRs.get(0).toString() )) {
//				sbQuery.append(makeInsert(tmpRs));
//			}
//			
//		}
//		
//		return sbQuery.toString();
//	}
//	
//	
//	/**
//	 * where 절 이하의 쿼리를 생성한다. 
//	 * 
//	 * @param data
//	 * @return
//	 */
//	private String getWhereMake(int rowSeq) {
//		String stmt = ""; //$NON-NLS-1$
//		
//		// original data
//		HashMap<Integer, Object> orgRs = originalDataList.get(rowSeq);
//		
//		for(int i=0; i<primaryKeyListIndex.size(); i++) {
//			int keyIndex = primaryKeyListIndex.get(i);
//			stmt += primaryKEYIntStrList.get(keyIndex) + " = '" + TbUtils.getOriginalData(orgRs.get(keyIndex+1).toString()) + "'"; //$NON-NLS-1$ //$NON-NLS-2$
//			
//			if(i < (primaryKeyListIndex.size()-1)) stmt += " AND "; //$NON-NLS-1$
//		}
//		
//		return stmt;
//	}
//	
//	/**
//	 * delete 문장 생성
//	 * 
//	 * @param tmpRs
//	 * @return
//	 */
//	private String makeDelete(int rowSeq, HashMap<Integer, Object> tmpRs) {
//		String deleteStmt = "DELETE FROM " + initTableNameStr + " \r\n WHERE "; //$NON-NLS-1$ //$NON-NLS-2$
//		deleteStmt += getWhereMake(rowSeq)  + PublicTadpoleDefine.SQL_DILIMITER; //$NON-NLS-1$
//		
//		return deleteStmt;
//	}
//	
//	/**
//	 * update 문장 생성
//	 * 
//	 * @param tmpRs
//	 * @return
//	 */
//	private String makeUpdate(int rowSeq, HashMap<Integer, Object> tmpRs) {
//		String updateStmt = "UPDATE " + initTableNameStr +   //$NON-NLS-1$ //$NON-NLS-2$
//				" SET "; //$NON-NLS-1$
//		
//		// 수정된 컬럼의 값을 넣는다.
//		// 0 번째 컬럼은 데이터 수정 유무이므로
//		for(int i=1; i<tmpRs.size(); i++) {
//			if(TbUtils.isModifyData( tmpRs.get(i).toString() )) {
//				updateStmt += mapColumns.get(i-1) + " = '" + TbUtils.getOriginalData(tmpRs.get(i).toString()) + "', "; //$NON-NLS-1$ //$NON-NLS-2$
//			}
//		}
//		updateStmt = StringUtils.chompLast(updateStmt, ", "); //$NON-NLS-1$
//		
//		updateStmt += " WHERE " + getWhereMake(rowSeq) + PublicTadpoleDefine.SQL_DILIMITER; //$NON-NLS-1$ //$NON-NLS-2$
//		return updateStmt;
//	}
//	
//	/**
//	 * insert 문장 생성
//	 * @param tmpRs
//	 * @return
//	 */
//	private String makeInsert(HashMap<Integer, Object> tmpRs) {
//		String insertStmt = "INSERT INTO " + initTableNameStr + "("; //$NON-NLS-1$ //$NON-NLS-2$
//		
//		// 수정된 컬럼 리스트를 나열한다.
//		boolean isModifyColumn = false;
//		for(int i=1; i<tmpRs.size(); i++) {
//			if(TbUtils.isModifyData( tmpRs.get(i).toString() )) {
//				insertStmt += mapColumns.get(i-1) + ", "; //$NON-NLS-1$
//				isModifyColumn = true;
//			}
//		}
//		// 수정 된 컬럼이 없다. 즉 신규 추가되어 컬럼이 수정되지 않았다.
//		if(!isModifyColumn) return ""; //$NON-NLS-1$
//		
//		insertStmt = StringUtils.chompLast(insertStmt, ", "); //$NON-NLS-1$
//		insertStmt += ") VALUES ("; //$NON-NLS-1$
//		
//		// 수정된 값을 입력한다.
//		for(int i=1; i<tmpRs.size(); i++) {
//			if(TbUtils.isModifyData( tmpRs.get(i).toString() )) insertStmt += "'" + TbUtils.getOriginalData(tmpRs.get(i).toString()) + "', "; //$NON-NLS-1$ //$NON-NLS-2$
//		}
//		insertStmt = StringUtils.chompLast(insertStmt, ", "); //$NON-NLS-1$
//		insertStmt += ");"; //$NON-NLS-1$
//		
//		return insertStmt;
//	}
//	
//	/**
//	 * 현재 접속 디비
//	 * @return
//	 */
//	public UserDBDAO getUserDB() {
//		return userDB;
//	}
//	
//	/**
//	 * 컬럼 타입
//	 * @return
//	 */
//	public HashMap<Integer, String> getTableDataTypeList() {
//		return tableDataTypeList;
//	}
//
//}
