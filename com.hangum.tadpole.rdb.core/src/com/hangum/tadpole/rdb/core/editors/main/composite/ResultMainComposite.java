/*******************************************************************************
 * Copyright (c) 2014 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main.composite;

import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.ace.editor.core.texteditor.function.EditorFunctionService;
import com.hangum.tadpole.commons.dialogs.message.dao.TadpoleMessageDAO;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.MainEditor;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.util.SQLUtil;

/**
 * RDB Result Composite
 * 
 * @author hangum
 *
 */
public class ResultMainComposite extends Composite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(ResultMainComposite.class);
	
	/** sql main editor */
	private MainEditor mainEditor;
	
	/** 사용자가 요청한 쿼리 */
	private RequestQuery reqQuery;
	
	/** query 결과 창 */
	private CTabFolder tabFolderResult;

	/** 쿼리 결과 페이지 로케이션 */
	private ResultSetComposite compositeResultSet;
	
	/** query history */
    private QueryHistoryComposite compositeQueryHistory;
	
	/** tadpole message */
	private MessageComposite compositeMessage;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ResultMainComposite(Composite ssahFormComposite, int style) {
		super(ssahFormComposite, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 1;
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		setLayout(gridLayout);
		
		Composite compositeResult = new Composite(this, SWT.NONE);
		compositeResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 1;
		gl_compositeResult.horizontalSpacing = 1;
		gl_compositeResult.marginHeight = 1;
		gl_compositeResult.marginWidth = 1;
		compositeResult.setLayout(gl_compositeResult);
		
		tabFolderResult = new CTabFolder(compositeResult, SWT.NONE);
		tabFolderResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolderResult.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());		
		
		// Set tab index
		tabFolderResult.setData(EditorDefine.RESULT_TAB.RESULT_SET.toString(), 		0);
		tabFolderResult.setData(EditorDefine.RESULT_TAB.SQL_RECALL.toString(), 		1);
		tabFolderResult.setData(EditorDefine.RESULT_TAB.TADPOLE_MESSAGE.toString(), 2);

		///////////////////// tab resultset //////////////////////////		
		compositeResultSet = new ResultSetComposite(tabFolderResult, SWT.NONE);
		compositeResultSet.setRDBResultComposite(this);
		compositeResultSet.setLayout(new GridLayout(1, false));
		
		CTabItem tbtmResult = new CTabItem(tabFolderResult, SWT.NONE);
		tbtmResult.setText(Messages.MainEditor_7);
		tbtmResult.setControl(compositeResultSet);

		///////////////////// tab query history //////////////////////////
		compositeQueryHistory = new QueryHistoryComposite(tabFolderResult, SWT.NONE);
		compositeQueryHistory.setRDBResultComposite(this);
		compositeQueryHistory.setLayout(new GridLayout(1, false));
		
		CTabItem tbtmNewItem = new CTabItem(tabFolderResult, SWT.NONE);
		tbtmNewItem.setText(Messages.MainEditor_10);
		tbtmNewItem.setControl(compositeQueryHistory);
		/////////////////// tab query history ///////////////////////
		
		///////////////////// tab Message //////////////////////////
		compositeMessage = new MessageComposite(tabFolderResult, SWT.NONE);
		compositeMessage.setLayout(new GridLayout(1, false));
		
		CTabItem tbtmMessage = new CTabItem(tabFolderResult, SWT.NONE);
		tbtmMessage.setText(Messages.MainEditor_0);
		tbtmMessage.setControl(compositeMessage);
		///////////////////// tab Message //////////////////////////		
	    
		tabFolderResult.setSelection(0);
	}
	
	/**
	 * main composite
	 */
	public void initMainComposite() {
		compositeQueryHistory.findHistoryData();
	}
	
	/**
	 * set main editor
	 * @param mainEditor
	 */
	public void setMainEditor(MainEditor mainEditor) {
		this.mainEditor = mainEditor;
	}
	
	/**
	 * execute command
	 * @param reqQuery
	 */
	public void executeCommand(final RequestQuery reqQuery) {
		if(logger.isDebugEnabled()) logger.debug("==> executeQuery user query is " + reqQuery.getOriginalSql());
		
		try {
			
			// 쿼리를 이미 실행 중이라면 무시합니다.
			if(compositeResultSet.getJobQueryManager() != null) {
				if(logger.isDebugEnabled()) logger.debug("===> job state is : " + compositeResultSet.getJobQueryManager().getState());
				
				if(Job.RUNNING == compositeResultSet.getJobQueryManager().getState()) {
					if(logger.isDebugEnabled()) logger.debug("================= return running query job ");
					return;
				}
			}
			
			// 요청쿼리가 없다면 무시합니다. 
			if("".equals(reqQuery.getSql())) return;
			this.reqQuery = reqQuery;
			
			// 실행해도 되는지 묻는다.
			if(PublicTadpoleDefine.YES_NO.YES.toString().equals(getUserDB().getQuestion_dml())) {
				boolean isDMLQuestion = false;
				if(reqQuery.getType() == EditorDefine.EXECUTE_TYPE.ALL) {						
					for (String strSQL : reqQuery.getOriginalSql().split(PublicTadpoleDefine.SQL_DILIMITER)) {							
						if(!SQLUtil.isStatement(strSQL)) {
							isDMLQuestion = true;
							break;
						}
					}
				} else {
					if(!SQLUtil.isStatement(reqQuery.getSql())) isDMLQuestion = true;
				}
			
				if(isDMLQuestion) if(!MessageDialog.openConfirm(null, "Confirm", Messages.MainEditor_56)) return; //$NON-NLS-1$
			}
			
			// tab을 처음으로 이동합니다. 
			resultFolderSel(EditorDefine.RESULT_TAB.RESULT_SET);
			
			// 실제 쿼리 실행.
			compositeResultSet.executeCommand(reqQuery);
		} catch(Exception e) {
			logger.error("execute query", e);
		} finally {
			// 에디터가 작업이 끝났음을 알립니다.
			browserEvaluate(EditorFunctionService.EXECUTE_DONE);
		}
	}
	
	/**
	 * tab을 선택합니다.
	 * 
	 * @param selectTab
	 */
	public void resultFolderSel(final EditorDefine.RESULT_TAB selectTab) {
		int index = (Integer)tabFolderResult.getData(selectTab.toString());
		
		if(tabFolderResult.getSelectionIndex() != index) {
			tabFolderResult.setSelection(index);
		}
	}
	
	public void appendTextAtPosition(String cmd) {
		mainEditor.appendTextAtPosition(cmd);
	}
	
	public UserDBDAO getUserDB() {
		return mainEditor.getUserDB();
	}
	
	/**
	 * 사용자 seq
	 * 
	 * @return
	 */
	public int getUserSeq() {
		return mainEditor.getUserSeq();
	}

	/**
	 * editor append text
	 * @param cmd
	 */
	public void appendText(String cmd) {
		mainEditor.appendText(cmd);
	}

	public void refreshMessageView(String msg) {
		compositeMessage.addAfterRefresh(new TadpoleMessageDAO(new Date(), msg));		
	}

	public IWorkbenchPartSite getSite() {
		return mainEditor.getSite();
	}
	
	public QueryHistoryComposite getCompositeQueryHistory() {
		return compositeQueryHistory;
	}

	public void setOrionTextFocus() {
		mainEditor.setOrionTextFocus();
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
	
	@Override
	protected void checkSubclass() {
	}

	public void browserEvaluate(String command) {
		mainEditor.browserEvaluate(command);
	}

}
