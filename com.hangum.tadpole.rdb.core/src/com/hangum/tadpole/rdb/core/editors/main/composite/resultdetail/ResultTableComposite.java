/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main.composite.resultdetail;

import java.io.BufferedReader;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.commons.dialogs.message.TadpoleImageViewDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.mysql.TableColumnDAO;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultFilter;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultSorter;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.mongodb.core.dialogs.msg.TadpoleSimpleMessageDialog;
import com.hangum.tadpole.preference.get.GetPreferenceGeneral;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.global.OpenSingleRowDataDialogAction;
import com.hangum.tadpole.rdb.core.dialog.msg.TDBInfoDialog;
import com.hangum.tadpole.rdb.core.editors.main.composite.ResultSetComposite;
import com.hangum.tadpole.rdb.core.editors.main.composite.direct.SQLResultLabelProvider;
import com.hangum.tadpole.rdb.core.editors.main.composite.tail.ResultTailComposite;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.hangum.tadpole.rdb.core.extensionpoint.definition.IMainEditorExtension;
import com.hangum.tadpole.session.manager.SessionManager;
import com.swtdesigner.SWTResourceManager;

/**
 * Table result type
 * 
 * @author hangum
 *
 */
public class ResultTableComposite extends AbstractResultDetailComposite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(ResultTableComposite.class);
	
	public static int Composite_HEIGHT_SIZE = 220;
	public static int Composite_WIDTH_SIZE = 400;
	
	/** 이미 결과를 마지막까지 그렸는지 유무 */
	private boolean isLastReadData = false;
	
	private Text textFilter;
	
	private TableViewer tvQueryResult;
	
	private SQLResultFilter sqlFilter = new SQLResultFilter();
	private SQLResultSorter sqlSorter;
    
    private OpenSingleRowDataDialogAction openSingleRowDataAction;
	
    private Button btnResultToEditor;
    
    private Button btnDetailView;
	private Button btnColumnDetail;
	private Composite compositeHead;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 * @param rdbResultComposite
	 */
	public ResultTableComposite(Composite parent, int style, ResultSetComposite rdbResultComposite) {
		super(parent, style, rdbResultComposite);
		setLayout(new GridLayout(1, false));
		GridData gd_compositeBody = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
		gd_compositeBody.heightHint = Composite_HEIGHT_SIZE;
		gd_compositeBody.widthHint = Composite_WIDTH_SIZE;
		setLayoutData(gd_compositeBody);
		
		Composite compositeBody = new Composite(this, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 2;
		gl_compositeBody.horizontalSpacing = 2;
		gl_compositeBody.marginHeight = 0;
		gl_compositeBody.marginWidth = 2;
		compositeBody.setLayout(gl_compositeBody);
		
		compositeHead = new Composite(compositeBody, SWT.NONE);
		GridLayout gl_compositeHead = new GridLayout(2, false);
		gl_compositeHead.verticalSpacing = 2;
		gl_compositeHead.horizontalSpacing = 2;
		gl_compositeHead.marginHeight = 0;
		gl_compositeHead.marginWidth = 2;
		compositeHead.setLayout(gl_compositeHead);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFilter = new Label(compositeHead, SWT.NONE);
		lblFilter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilter.setText(Messages.get().Filter);
		
		textFilter = new Text(compositeHead,SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		textFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) setFilter();
			}
		});
		
	//  SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		tvQueryResult = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		final Table tableResult = tvQueryResult.getTable();
		GridData gd_tableResult = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_tableResult.heightHint = 90;
		tableResult.setLayoutData(gd_tableResult);

		final String PREFERENCE_USER_FONT = GetPreferenceGeneral.getRDBResultFont();
		if(!"".equals(PREFERENCE_USER_FONT)) { //$NON-NLS-1$
			try {
				String[] arryFontInfo = StringUtils.split(PREFERENCE_USER_FONT, "|"); //$NON-NLS-1$
				tableResult.setFont(SWTResourceManager.getFont(arryFontInfo[0], Integer.parseInt(arryFontInfo[1]), Integer.parseInt(arryFontInfo[2])));
			} catch(Exception e) {
				logger.error("Fail setting the user font", e); //$NON-NLS-1$
			}
		}
		tableResult.addListener(SWT.MouseDown, new Listener() {
		    public void handleEvent(final Event event) {
		    	TableItem[] selection = tableResult.getSelection();
		    	
				if (selection.length != 1) return;
				eventTableSelect = event;
				
				final TableItem item = tableResult.getSelection()[0];
				for (int i=0; i<tableResult.getColumnCount(); i++) {
					if (item.getBounds(i).contains(event.x, event.y)) {
						Map<Integer, Object> mapColumns = getRsDAO().getDataList().getData().get(tableResult.getSelectionIndex());
						// execute extension start =============================== 
						IMainEditorExtension[] extensions = getRdbResultComposite().getRdbResultComposite().getMainEditor().getMainEditorExtions();
						for (IMainEditorExtension iMainEditorExtension : extensions) {
							iMainEditorExtension.resultSetClick(i, mapColumns);
						}
						break;
					}
				}	// for column count
			}
		});
		
		tableResult.setLinesVisible(true);
		tableResult.setHeaderVisible(true);
		createResultMenu();
		
		sqlFilter.setTable(tableResult);
		
		// single column select start
		TableUtil.makeSelectSingleColumn(tvQueryResult);
	    // single column select end
		
		tableResult.getVerticalBar().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				calcTableData();
			}
		});
		tableResult.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				calcTableData();
			}
		});
		
		Composite compositeBtn = new Composite(compositeBody, SWT.NONE);
		compositeBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		GridLayout gl_compositeBtn = new GridLayout(4, false);
		gl_compositeBtn.verticalSpacing = 2;
		gl_compositeBtn.horizontalSpacing = 2;
		gl_compositeBtn.marginWidth = 0;
		gl_compositeBtn.marginHeight = 2;
		compositeBtn.setLayout(gl_compositeBtn);
		
		btnResultToEditor = new Button(compositeBtn, SWT.NONE);
		btnResultToEditor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableColumnDAO columnDao = selectColumnToEditor();
				if(columnDao == null) return;
				
				if(!"".equals(columnDao.getCol_value())) { //$NON-NLS-1$
					if(PublicTadpoleDefine.DEFINE_TABLE_COLUMN_BASE_ZERO.equals(columnDao.getName())) {
						appendTextAtPosition(""+columnDao.getCol_value()); //$NON-NLS-1$
					} else {
						if(RDBTypeToJavaTypeUtils.isNumberType(columnDao.getType())) {
							appendTextAtPosition(""+columnDao.getCol_value());
						} else {
							appendTextAtPosition(String.format(" '%s'", columnDao.getCol_value())); //$NON-NLS-1$
						}
					}
				}
			}
		});
		btnResultToEditor.setText(Messages.get().ResultSetComposite_2);
		
		btnDetailView = new Button(compositeBtn, SWT.NONE);
		GridData gd_btnDetailView = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnDetailView.widthHint = 80;
		btnDetailView.setLayoutData(gd_btnDetailView);
		btnDetailView.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openSingleRecordViewDialog();
			}
		});
		btnDetailView.setText(Messages.get().ResultSetComposite_0);
		
		btnColumnDetail = new Button(compositeBtn, SWT.NONE);
		btnColumnDetail.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openSinglColumViewDialog();
			}
		});
		btnColumnDetail.setText(Messages.get().ResultSetComposite_btnColumnDetail_text);
		
		compositeTail = new ResultTailComposite(this, compositeBtn, SWT.NONE);
		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 2;
		gl_compositeResult.horizontalSpacing = 2;
		gl_compositeResult.marginHeight = 0;
		gl_compositeResult.marginWidth = 2;
		compositeTail.setLayout(gl_compositeResult);
	}

	/**
	 * select table column to editor
	 */
	private TableColumnDAO selectColumnToEditor() {
		if(eventTableSelect == null) return null;
		final Table tableResult = tvQueryResult.getTable();
    	TableItem[] selection = tableResult.getSelection();
		if (selection.length != 1) return null;
		
		TableColumnDAO columnDao = new TableColumnDAO();
		TableItem item = tableResult.getSelection()[0];
		for (int i=0; i<tableResult.getColumnCount(); i++) {
			
			if (item.getBounds(i).contains(eventTableSelect.x, eventTableSelect.y)) {
				Map<Integer, Object> mapColumns = getRsDAO().getDataList().getData().get(tableResult.getSelectionIndex());
				// execute extension start =============================== 
				IMainEditorExtension[] extensions = getRdbResultComposite().getRdbResultComposite().getMainEditor().getMainEditorExtions();
				for (IMainEditorExtension iMainEditorExtension : extensions) {
					iMainEditorExtension.resultSetDoubleClick(i, mapColumns);
				}
				// execute extension stop ===============================
				
				// 첫번째 컬럼이면 전체 로우의 데이터를 상세하게 뿌려줍니
				if(i == 0) {
					columnDao.setName(PublicTadpoleDefine.DEFINE_TABLE_COLUMN_BASE_ZERO);
					columnDao.setType(PublicTadpoleDefine.DEFINE_TABLE_COLUMN_BASE_ZERO_TYPE);
					
					for (int j=1; j<tableResult.getColumnCount(); j++) {
						Object columnObject = mapColumns.get(j);
						boolean isNumberType = RDBTypeToJavaTypeUtils.isNumberType(getRsDAO().getColumnType().get(j));
						if(isNumberType) {
							String strText = ""; //$NON-NLS-1$
							
							// if select value is null can 
							if(columnObject == null) strText = "0"; //$NON-NLS-1$
							else strText = columnObject.toString();
							columnDao.setCol_value(columnDao.getCol_value() + strText + ", ");
						} else if("BLOB".equalsIgnoreCase(columnDao.getData_type())) { //$NON-NLS-1$
							// ignore blob type
						} else {
							String strText = ""; //$NON-NLS-1$
							
							// if select value is null can 
							if(columnObject == null) strText = ""; //$NON-NLS-1$
							else strText = columnObject.toString();
							columnDao.setCol_value(columnDao.getCol_value() + SQLUtil.makeQuote(strText) + ", ");
						}
					}
					columnDao.setCol_value(StringUtils.removeEnd(""+columnDao.getCol_value(), ", "));

					break;
				} else {
					
					//결과 그리드의 선택된 행에서 마우스 클릭된 셀에 연결된 컬럼 오브젝트를 조회한다.
					Object columnObject = mapColumns.get(i);
					
					Integer intType = getRsDAO().getColumnType().get(i);
					if(intType == null) intType = java.sql.Types.VARCHAR;
					String strType = RDBTypeToJavaTypeUtils.getRDBType(intType);
					
					columnDao.setName(getRsDAO().getColumnName().get(i));
					columnDao.setType(strType);
					
					if(columnObject != null) {
						// 해당컬럼 값이 널이 아니고 clob데이터 인지 확인한다.
						if (columnObject instanceof java.sql.Clob ){
							Clob cl = (Clob) columnObject;
	
							StringBuffer clobContent = new StringBuffer();
							String readBuffer = new String();
	
							// 버퍼를 이용하여 clob컬럼 자료를 읽어서 팝업 화면에 표시한다.
							BufferedReader bufferedReader;
							try {
								bufferedReader = new java.io.BufferedReader(cl.getCharacterStream());
								while ((readBuffer = bufferedReader.readLine())!= null) {
									clobContent.append(readBuffer);
								}
	
								columnDao.setCol_value(clobContent.toString());				
							} catch (Exception e) {
								logger.error("Clob column echeck", e); //$NON-NLS-1$
							}
						}else if (columnObject instanceof java.sql.Blob ){
							try {
								Blob blob = (Blob) columnObject;
								columnDao.setCol_value(blob.getBinaryStream());
	
							} catch (Exception e) {
								logger.error("Blob column echeck", e); //$NON-NLS-1$
							}
		
						}else if (columnObject instanceof byte[] ){// (columnObject.getClass().getCanonicalName().startsWith("byte[]")) ){
							byte[] b = (byte[])columnObject;
							StringBuffer str = new StringBuffer();
							try {
								for (byte buf : b){
									str.append(buf);
								}
								str.append("\n\nHex : " + new BigInteger(str.toString(), 2).toString(16)); //$NON-NLS-1$
								
								columnDao.setCol_value(str.toString());
							} catch (Exception e) {
								logger.error("Clob column echeck", e); //$NON-NLS-1$
							}
						}else{
							String strText = ""; //$NON-NLS-1$
							
							// if select value is null can 
							if(columnObject == null) strText = ""; //$NON-NLS-1$
							else strText = columnObject.toString();
							
							columnDao.setCol_value(strText);
						}
					} 	// end object null
				}	// end if first column
			
				break;
			}	// for column index
		}	// end for

		return columnDao;
	}
	
	/**
	 * Open Single recode view.
	 * Just view detail data.
	 */
	private void openSingleRecordViewDialog() {
		// selection sevice를 이용할 수 없어 중복되는 코드 생성이 필요해서 작성.
		openSingleRowDataAction.selectionChanged(getRsDAO(), tvQueryResult.getSelection());
		if (openSingleRowDataAction.isEnabled()) {
			openSingleRowDataAction.run();
		} else {
			MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().ResultSetComposite_8);
		}
	}
	
	/**
	 * column popup dialog
	 */
	private void openSinglColumViewDialog() {
		TableColumnDAO columnDao = selectColumnToEditor();
		if(columnDao == null) {
			MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().ResultSetComposite_6);
			return;
		}
			
		String strType = columnDao.getType();
		if("JSON".equalsIgnoreCase(strType)) { //$NON-NLS-1$
			TadpoleSimpleMessageDialog dialog = new TadpoleSimpleMessageDialog(getShell(), Messages.get().ResultSetComposite_16, ""+columnDao.getCol_value());
			dialog.open();
		} else if("BLOB".equalsIgnoreCase(strType)) { //$NON-NLS-1$
			logger.debug(columnDao.getCol_value());
			if (columnDao.getCol_value() instanceof String){
				TDBInfoDialog dialog = new TDBInfoDialog(getShell(), Messages.get().ResultSetComposite_16, ""+columnDao.getCol_value());
				dialog.open();
			}else{
				TadpoleImageViewDialog dlg = new TadpoleImageViewDialog(getShell(), Messages.get().ResultSetComposite_16, (InputStream)columnDao.getCol_value());
				dlg.open();
			}
		} else {
			TDBInfoDialog dialog = new TDBInfoDialog(getShell(), Messages.get().ResultSetComposite_16, ""+columnDao.getCol_value());
			dialog.open();
		}
	}

	/**
	 * tvQueryResult 테이블 뷰어에 메뉴 추가하기 
	 */
	private void createResultMenu() {
		openSingleRowDataAction = new OpenSingleRowDataDialogAction();
		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu", "ResultSet"); //$NON-NLS-1$ //$NON-NLS-2$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(openSingleRowDataAction);
			}
		});

		tvQueryResult.getTable().setMenu(menuMgr.createContextMenu(tvQueryResult.getTable()));

		// 
		// 본 Composite는 Editor에서 최초 생성되는데, 에디터가 site()에 등록되지 않은 상태에서
		// selection service에 접근할 수 없어서 임시로 selection 이벤트가 발생할때마다 
		// 직접 action selection 메소드를 호출하도록 수정함.
		// 또한, 쿼리 실행할 때 마다 rsDAO 값도 변경되므로, selectoin이 변경될때 마다 같이
		// 전달해 준다. 
		tvQueryResult.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				openSingleRowDataAction.selectionChanged(getRsDAO(), event.getSelection());
			}
		});
	}

	/**
	 * scroll data에 맞게 데이터를 출력합니다. 
	 */
	private void calcTableData() {
		if(getRsDAO() == null) return;
		if(isLastReadData) return;
		
		final Table tableResult = tvQueryResult.getTable();
		int tableRowCnt = tableResult.getBounds().height / tableResult.getItemHeight();
		
		// 만약에(테이블 위치 인덱스 + 테이블에 표시된로우 수 + 1) 보다 전체 아이템 수가 크면).
		if( (tableResult.getTopIndex() + tableRowCnt + 1) > tableResult.getItemCount()) { 
			final TadpoleResultSet oldTadpoleResultSet = getRsDAO().getDataList();

			if(logger.isDebugEnabled()) logger.debug("####11111###### [tableResult.getItemCount()]" + oldTadpoleResultSet.getData().size() +":"+tableResult.getItemCount() + ":" + GetPreferenceGeneral.getPageCount());
			if(oldTadpoleResultSet.getData().size() >= tableResult.getItemCount()) {
				if(logger.isDebugEnabled()) logger.debug("####2222222###### [tableResult.getItemCount()]" + oldTadpoleResultSet.getData().size() +":"+tableResult.getItemCount() + ":" + GetPreferenceGeneral.getPageCount());
				
				if(oldTadpoleResultSet.getData().size() >= (tableResult.getItemCount())) {
					// 나머지 데이터를 가져온다.
					final int intSelectLimitCnt = GetPreferenceGeneral.getSelectLimitCount();
					final String strUserEmail 	= SessionManager.getEMAIL();
					final int queryTimeOut 		= GetPreferenceGeneral.getQueryTimeOut();
					final String strNullValue   = GetPreferenceGeneral.getResultNull();
					
					try {
						QueryExecuteResultDTO newRsDAO = getRdbResultComposite().runSelect(reqQuery, queryTimeOut, strUserEmail, intSelectLimitCnt, oldTadpoleResultSet.getData().size(), strNullValue);
						if(newRsDAO.getDataList().getData().isEmpty()) isLastReadData = true;
						
						if(logger.isDebugEnabled()) logger.debug("==> old count is " + oldTadpoleResultSet.getData().size() );
						oldTadpoleResultSet.getData().addAll(newRsDAO.getDataList().getData());
					
						tvQueryResult.setInput(oldTadpoleResultSet.getData());
						compositeTail.execute(getTailResultMsg());
						tvQueryResult.getTable().setToolTipText(getTailResultMsg());
					} catch(Exception e) {
						logger.error("continue result set : " + e.getMessage());
					}
					
				} else {
					tvQueryResult.setInput(oldTadpoleResultSet.getData());
				}
			}
		}
	}
	
	/**
	 * 필터를 설정합니다.
	 */
	private void setFilter() {
		sqlFilter.setFilter(textFilter.getText());
		tvQueryResult.addFilter( sqlFilter );
	}
	
	private void appendTextAtPosition(String cmd) {
		getRdbResultComposite().getRdbResultComposite().appendTextAtPosition(cmd);
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void initUI() {
		if(this.isDisposed()) return;
		
		this.layout();
		
		this.sqlFilter.setFilter(""); //$NON-NLS-1$
		this.textFilter.setText(""); //$NON-NLS-1$
	}
	
	@Override
	public void printUI(RequestQuery reqQuery, QueryExecuteResultDTO rsDAO, boolean isMakePin) {
		isLastReadData = false;
		if(rsDAO == null) return;
		if(rsDAO.getDataList() == null) return;
		
		super.printUI(reqQuery, rsDAO, isMakePin);
		
		final TadpoleResultSet trs = rsDAO.getDataList();
		sqlSorter = new SQLResultSorter(-999);
		
		boolean isEditable = true;
		if("".equals(rsDAO.getColumnTableName().get(1))) isEditable = false; //$NON-NLS-1$
		SQLResultLabelProvider.createTableColumn(reqQuery, tvQueryResult, rsDAO, sqlSorter, isEditable);
		
		tvQueryResult.setLabelProvider(new SQLResultLabelProvider(reqQuery.getMode(), GetPreferenceGeneral.getISRDBNumberIsComma(), rsDAO));
		tvQueryResult.setContentProvider(new ArrayContentProvider());
		
		// 쿼리를 설정한 사용자가 설정 한 만큼 보여준다.
		if(trs.getData().size() > GetPreferenceGeneral.getPageCount()) {
			tvQueryResult.setInput(trs.getData().subList(0, GetPreferenceGeneral.getPageCount()));	
		} else {
			tvQueryResult.setInput(trs.getData());
		}
		tvQueryResult.setSorter(sqlSorter);
		
		// 메시지를 출력합니다.
		compositeTail.execute(getTailResultMsg());
		
		tvQueryResult.getTable().setToolTipText(getTailResultMsg());
		sqlFilter.setTable(tvQueryResult.getTable());
		
		// Pack the columns
		TableUtil.packTable(tvQueryResult.getTable());
		getRdbResultComposite().getRdbResultComposite().resultFolderSel(EditorDefine.RESULT_TAB.RESULT_SET);
	}

	@Override
	public RESULT_COMP_TYPE getResultType() {
		return RESULT_COMP_TYPE.Table;
	}
	
}
