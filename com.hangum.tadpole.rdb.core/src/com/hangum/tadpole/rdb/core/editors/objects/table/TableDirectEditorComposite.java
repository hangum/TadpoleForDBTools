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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.util.XMLUtils;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.utils.SQLTextUtil;
import com.hangum.tadpole.rdb.core.editors.objects.table.TbUtils.TABLE_MOD_TYPE;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.sql.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.sql.dao.mysql.TableDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.util.PartQueryUtil;
import com.hangum.tadpole.sql.util.resultset.ResultSetUtils;
import com.hangum.tadpole.sql.util.sqlscripts.DDLScriptManager;
import com.hangum.tadpole.sql.util.tables.SQLResultContentProvider;
import com.hangum.tadpole.sql.util.tables.SQLResultFilter;
import com.hangum.tadpole.sql.util.tables.SQLResultLabelProvider;
import com.hangum.tadpole.sql.util.tables.SQLResultSorter;
import com.hangum.tadpole.sql.util.tables.TableUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * Table data direct editor
 * 
 * @author hangum
 *
 */
public class TableDirectEditorComposite extends Composite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TableDirectEditorComposite.class);
	
	/** 현재 테이블의 상태 */
	private TABLE_MOD_TYPE modifyType = TABLE_MOD_TYPE.NONE;
		
	private String initTableNameStr;
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
	private ToolItem tltmSave;
	private ToolItem tltmDelete;
	private ToolItem tltmInsert;
	private ToolItem tltmTablecomment;
	
	private Text textWhere;
	private Composite compositeTail;
	private Button btnDdlSourceView;
	private Label lblOrderBy;
	private Text textOrderBy;
	private Label lblLimit;
	private Composite compositeLimit;
	private Text textLimitStart;
	private Label lblEnd;
	private Text textLimitEnd;

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
	public TableDirectEditorComposite(Composite parent, int style, final UserDBDAO userDB, final String initTableNameStr,  List<TableColumnDAO> columnList, Map<String, Boolean> primaryKEYListString) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 2;
		gridLayout.horizontalSpacing = 2;
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		setLayout(gridLayout);
		
		// start initialize value
		this.userDB = userDB;
		this.initTableNameStr = initTableNameStr;
		this.columnList = columnList;
		this.primaryKEYListString = primaryKEYListString;
		// end initialize value
		
		Composite compositeBase = new Composite(this, SWT.NONE);
		compositeBase.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBase = new GridLayout(1, false);
		gl_compositeBase.verticalSpacing = 1;
		gl_compositeBase.horizontalSpacing = 1;
		gl_compositeBase.marginHeight = 1;
		gl_compositeBase.marginWidth = 1;
		compositeBase.setLayout(gl_compositeBase);
		
		toolBar = new ToolBar(compositeBase, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		tltmSave = new ToolItem(toolBar, SWT.NONE);
		tltmSave.setEnabled(false);
		tltmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveTableData();
			}
		});
		tltmSave.setText(Messages.TableEditPart_0);
		
		tltmInsert = new ToolItem(toolBar, SWT.NONE);
		tltmInsert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(modifyType == TABLE_MOD_TYPE.EDITOR) insertRow();
			}
		});
		tltmInsert.setText(Messages.TableEditPart_tltmInsert_text);
		tltmInsert.setEnabled(false);
		
		tltmDelete = new ToolItem(toolBar, SWT.NONE);
		tltmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				IStructuredSelection is = (IStructuredSelection)sqlResultTableViewer.getSelection();
				if(!is.isEmpty()) {
					deleteRow(is.getFirstElement());					
				}
				
			}
		});
		tltmDelete.setEnabled(false);
		tltmDelete.setText(Messages.TableEditPart_1);
		
		tltmTablecomment = new ToolItem(toolBar, SWT.NONE);
		tltmTablecomment.setText(TbUtils.NONE_MSG);
		
		Composite compositeBody = new Composite(compositeBase, SWT.NONE);
		GridLayout gl_compositeBody = new GridLayout(2, false);
		gl_compositeBody.horizontalSpacing = 1;
		gl_compositeBody.verticalSpacing = 1;
		gl_compositeBody.marginHeight = 1;
		gl_compositeBody.marginWidth = 1;
		compositeBody.setLayout(gl_compositeBody);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblWhere = new Label(compositeBody, SWT.NONE);
		lblWhere.setText(Messages.TableEditPart_lblWhere_text);
		
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
		lblOrderBy.setText(Messages.TableDirectEditorComposite_lblOrderBy_text);
		
		textOrderBy = new Text(compositeBody, SWT.BORDER);
		textOrderBy.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.keyCode == SWT.Selection) initBusiness();
			}
		});
		textOrderBy.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblLimit = new Label(compositeBody, SWT.NONE);
		lblLimit.setText(Messages.TableDirectEditorComposite_lblLimit_text);
		
		compositeLimit = new Composite(compositeBody, SWT.NONE);
		compositeLimit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeLimit = new GridLayout(4, false);
		gl_compositeLimit.verticalSpacing = 1;
		gl_compositeLimit.horizontalSpacing = 1;
		gl_compositeLimit.marginHeight = 1;
		gl_compositeLimit.marginWidth = 1;
		compositeLimit.setLayout(gl_compositeLimit);
		
		Label lblStart = new Label(compositeLimit, SWT.NONE);
		lblStart.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStart.setBounds(0, 0, 59, 14);
		lblStart.setText(Messages.TableDirectEditorComposite_lblStart_text);
		
		textLimitStart = new Text(compositeLimit, SWT.BORDER);
		textLimitStart.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) initBusiness();
			}
		});
		textLimitStart.setText(Messages.TableDirectEditorComposite_textStart_text);
		GridData gd_textLimitStart = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_textLimitStart.widthHint = 40;
		gd_textLimitStart.minimumWidth = 40;
		textLimitStart.setLayoutData(gd_textLimitStart);
		
		lblEnd = new Label(compositeLimit, SWT.NONE);
		lblEnd.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEnd.setText(Messages.TableDirectEditorComposite_lblEnd_text);
		
		textLimitEnd = new Text(compositeLimit, SWT.BORDER);
		textLimitEnd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) initBusiness();
			}
		});
		textLimitEnd.setText(Messages.TableDirectEditorComposite_text_text);
		GridData gd_textLimitEnd = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_textLimitEnd.minimumWidth = 40;
		gd_textLimitEnd.widthHint = 40;
		textLimitEnd.setLayoutData(gd_textLimitEnd);
		
		Label lblNewLabel = new Label(compositeBody, SWT.NONE);
		lblNewLabel.setText(Messages.TableEditPart_3);
		
		textFilter = new Text(compositeBody, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) setFilter();
			}
		});
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		sqlResultTableViewer = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		sqlResultTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if(primaryKeyListIndex.size() >= 1) tltmDelete.setEnabled(true);
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
					DDLScriptManager scriptManager = new DDLScriptManager(userDB, PublicTadpoleDefine.DB_ACTION.TABLES);
					FindEditorAndWriteQueryUtil.run(userDB, scriptManager.getScript(new TableDAO(initTableNameStr, "")));
				} catch(Exception ee) {
					MessageDialog.openError(null, "Confirm", ee.getMessage());
				}
			}
		});
		btnDdlSourceView.setText(Messages.TableDirectEditorComposite_btnDdlSourceView_text);
		
		initBusiness();
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
			// 인덱스가 존재하면  수정가능 모드
			if(primaryKeyListIndex.size() >= 1) {
				modifyType = TABLE_MOD_TYPE.EDITOR;
				tltmTablecomment.setText(TbUtils.EDITOR_MSG);
				tltmInsert.setEnabled(true);
			}
			
			// 화면에 데이터를 보여준다.
			resultView();
			
		} catch(Exception e) {
			logger.error(Messages.TableEditPart_4, e);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(null, "Error", Messages.TableViewerEditPart_2, errStatus); //$NON-NLS-1$
			return;
		}
		
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
		String requestQuery = "SELECT * FROM " + initTableNameStr; //$NON-NLS-1$
		if(!"".equals( strWhere )) requestQuery += " where " + strWhere; //$NON-NLS-1$
		if(!"".equals( strOrderBy )) requestQuery += " order by " + strOrderBy; //$NON-NLS-1$
		
		int intStart = 0, intEnd = 500;

		try {
			intStart = Integer.parseInt(StringUtils.trimToEmpty(textLimitStart.getText()));
		} catch(Exception e) {
			textLimitStart.setText(""+intStart);
		}
		try {
			intEnd = Integer.parseInt(StringUtils.trimToEmpty(textLimitEnd.getText()));
		} catch(Exception e) {
			textLimitEnd.setText(""+intEnd);
		}
		
		if((intEnd - intStart) > 500) {
			MessageDialog.openWarning(null, "Information", "Can not be more than 500.");
			textLimitStart.setFocus();
			return;
		}
		
		requestQuery = PartQueryUtil.makeSelect(userDB, requestQuery, intStart, intEnd);
		if(logger.isDebugEnabled()) logger.debug("[table information query]" + requestQuery);
		
		ResultSet rs = null;
		java.sql.Connection javaConn = null;
		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();
			
			PreparedStatement stmt = null;
			stmt = javaConn.prepareStatement(requestQuery);
			
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
						tmpRs.put(i, XMLUtils.xmlToString(rs.getString(i) == null?"":rs.getString(i)));
					} catch(Exception e) {
						logger.error("ResutSet fetch error", e);
						tmpRs.put(i, "");
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
		sqlResultTableViewer.setLabelProvider( new SQLResultLabelProvider() );
		sqlResultTableViewer.setContentProvider(new SQLResultContentProvider(tableDataList) );
		sqlResultTableViewer.setInput(tableDataList);
		sqlResultTableViewer.setSorter(sqlSorter);
		
		// 결과 후처리 
		tableResult.setToolTipText(tableDataList.size() + Messages.MainEditor_33);
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
			tableColumnInfo.getColumn().setText( Messages.TableViewerEditPart_0 );
			tableColumnInfo.getColumn().setResizable(true);
			tableColumnInfo.getColumn().setMoveable(false);
			tableColumnInfo.getColumn().setWidth(100);
			
			// reset column 
			for(int i=1; i<mapColumns.size()+1; i++) {
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
				
				if(modifyType == TABLE_MOD_TYPE.EDITOR) tableColumn.setEditingSupport(new TextViewerEditingSupport(this, index, tableViewer));
			}	// end for
			
		} catch(Exception e) { 
			logger.error(Messages.TableEditPart_8, e);
		}
		
	}

	/**
	 * save table data
	 */
	private void saveTableData() {
		// save 기능을 수행합니다.
		java.sql.Connection javaConn = null;
		Statement stmt = null;
		String lastExeQuery = ""; //$NON-NLS-1$
		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();

			// sqlite에서는 forward cursor만 지원하여 블럭 처리 
			stmt = javaConn.createStatement();//ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			javaConn.setAutoCommit(false);

			String[] querys = SQLTextUtil.delLineChar(getChangeQuery()).split(";"); //$NON-NLS-1$
			for(int i=0; i<querys.length; i++) {
				
			//	logger.info("exe query [" + querys[i] + "]"); //$NON-NLS-1$ //$NON-NLS-2$
				
				lastExeQuery = querys[i] ;
				stmt.execute(querys[i] );
			}
			
			javaConn.commit();
			
			// 정상적으로 모든 결과 처리 완료.
			tltmSave.setEnabled(false);
			initBusiness();
//			isDirty = false;
//			
//			isDirty = false;
//			firePropertyChange(PROP_DIRTY);
			
		} catch(Exception ee) {
			try { if(javaConn != null) javaConn.rollback(); } catch(SQLException roE) {};
			
			logger.error(Messages.TableViewerEditPart_7, ee);
			
			// 에러 났으므로 사후 처리.
			MessageDialog.openError(null, Messages.TableViewerEditPart_3, "Query [ " + lastExeQuery + Messages.TableViewerEditPart_10 + ee.getMessage() + Messages.TableViewerEditPart_11); //$NON-NLS-2$
			
		} finally {
			// connection을 원래대로 돌려 놓는다.
			try { javaConn.setAutoCommit(true); } catch(Exception ee) {}
			
			try {if(stmt != null) stmt.close();} catch(Exception ee) {}
			try {if(javaConn != null) javaConn.close();} catch(Exception ee) {}
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
	public void deleteRow(Object selObject) {
		HashMap<Integer, String> data = (HashMap<Integer, String>)selObject;
		if(TbUtils.isInsert(data.get(0))) {
			tableDataList.remove(data);			
		} else {
			data.put(0, TbUtils.getColumnText(TbUtils.COLUMN_MOD_TYPE.DELETE) );
		}
		
		setModifyButtonControl();
		
		sqlResultTableViewer.refresh(tableDataList);
	}
	
	/**
	 * 버튼을 초기화합니다.
	 */
	public void initButtonCtrl() {
		tltmDelete.setEnabled(false);
		tltmInsert.setEnabled(false);
		
		tltmSave.setEnabled(false);
	}
	
	/**
	 * query수정되어 버튼을 조덜합니다. 
	 * 
	 * @param updateQuery
	 */
	public void setModifyButtonControl() {
		tltmSave.setEnabled(true);
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
	 * @return
	 */
	private String getWhereMake(int rowSeq) {
		String stmt = ""; //$NON-NLS-1$
		
		// original data
		Map<Integer, Object> orgRs = originalDataList.get(rowSeq);
		
		for(int i=0; i<primaryKeyListIndex.size(); i++) {
			int keyIndex = primaryKeyListIndex.get(i);
			stmt += primaryKEYIntStrList.get(keyIndex) + " = '" + TbUtils.getOriginalData(orgRs.get(keyIndex+1).toString()) + "'"; //$NON-NLS-1$ //$NON-NLS-2$
			
			if(i < (primaryKeyListIndex.size()-1)) stmt += " AND "; //$NON-NLS-1$
		}
		
		return stmt;
	}
	
	/**
	 * delete 문장 생성
	 * 
	 * @param tmpRs
	 * @return
	 */
	private String makeDelete(int rowSeq, Map<Integer, Object> tmpRs) {
		String deleteStmt = "DELETE FROM " + initTableNameStr + " \r\n WHERE "; //$NON-NLS-1$ //$NON-NLS-2$
		deleteStmt += getWhereMake(rowSeq)  + PublicTadpoleDefine.SQL_DILIMITER; //$NON-NLS-1$
		
		return deleteStmt;
	}
	
	/**
	 * update 문장 생성.
	 * 
	 * @param tmpRs
	 * @return
	 */
	private String makeUpdate(int rowSeq, Map<Integer, Object> tmpRs) {
		String updateStmt = "UPDATE " + initTableNameStr +   //$NON-NLS-1$ //$NON-NLS-2$
				" SET "; //$NON-NLS-1$
		
		// 수정된 컬럼의 값을 넣는다.
		// 0 번째 컬럼은 데이터 수정 유무이므로 .
		for(int i=1; i<tmpRs.size(); i++) {
			if(TbUtils.isModifyData( tmpRs.get(i).toString() )) {
				updateStmt += mapColumns.get(i-1) + " = '" + TbUtils.getOriginalData(tmpRs.get(i).toString()) + "', "; //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		updateStmt = StringUtils.chompLast(updateStmt, ", "); //$NON-NLS-1$
		
		updateStmt += " WHERE " + getWhereMake(rowSeq) + PublicTadpoleDefine.SQL_DILIMITER; //$NON-NLS-1$ //$NON-NLS-2$
		return updateStmt;
	}
	
	/**
	 * insert 문장 생성.
	 * 
	 * @param tmpRs
	 * @return
	 */
	private String makeInsert(Map<Integer, Object> tmpRs) {
		String insertStmt = "INSERT INTO " + initTableNameStr + "("; //$NON-NLS-1$ //$NON-NLS-2$
		
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
			if(TbUtils.isModifyData( tmpRs.get(i).toString() )) insertStmt += "'" + TbUtils.getOriginalData(tmpRs.get(i).toString()) + "', "; //$NON-NLS-1$ //$NON-NLS-2$
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
