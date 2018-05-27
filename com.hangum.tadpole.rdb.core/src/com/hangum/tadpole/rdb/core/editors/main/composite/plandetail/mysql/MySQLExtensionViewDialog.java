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
package com.hangum.tadpole.rdb.core.editors.main.composite.plandetail.mysql;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;
import com.hangum.tadpole.engine.sql.util.tables.SQLResultSorter;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.engine.utils.RequestQuery;
import com.hangum.tadpole.rdb.core.editors.main.composite.direct.QueryResultLabelProvider;
import com.swtdesigner.SWTResourceManager;

/**
 * MySQL show profile and status variable Dialog
 * 
 * @author hangum
 *
 */
public class MySQLExtensionViewDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(MySQLExtensionViewDialog.class);
	
	/**
	 * key, value에 입력할 항목 중에 입력할 항목을 정의한다. 
	 * @author hangum
	 *
	 */
	public enum MYSQL_EXTENSION_VIEW {SHOW_PROFILLING, STATUS_VARIABLE, EXECUTE_PLAN};
	
	private RequestQuery reqQuery;
	private QueryExecuteResultDTO rsDAO;
	
	private TableViewer tvShowProfiller;
	private TableViewer tvShowStatus;
	private TableViewer tvExecutePlan;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param reqQuery
	 * @param showProfiles
	 * @param showStatus
	 */
	public MySQLExtensionViewDialog(Shell parentShell, RequestQuery reqQuery, QueryExecuteResultDTO rsDAO) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.reqQuery = reqQuery;
		this.rsDAO = rsDAO;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("SHOW PROFILE and Others Dialog");
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
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
		
		ScrolledComposite scCompositeBody = new ScrolledComposite(container, SWT.H_SCROLL | SWT.V_SCROLL);
		scCompositeBody.setLayout(new FillLayout());
		scCompositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		SashForm compositeBody = new SashForm(scCompositeBody, SWT.VERTICAL);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tvShowProfiller = createTitleTable(compositeBody, "SHOW PROFILE Result");
		tvShowStatus 	= createTitleTable(compositeBody, "Change Of STATUS VARIABLES");
		
		Object mapQueryPlan = rsDAO.getMapExtendResult().get(MYSQL_EXTENSION_VIEW.EXECUTE_PLAN.name());;
		if(mapQueryPlan != null) {
			tvExecutePlan 	= createTitleTable(compositeBody, "Execute Plan");
		}
		
		if(mapQueryPlan != null) {
			compositeBody.setWeights(new int[] {4, 4, 2});
		} else {
			compositeBody.setWeights(new int[] {5, 5});
		}
		
		showProfiller();
		showDiffStatus();
		if(mapQueryPlan != null) {
			showExecutePlan();
		}
		
		scCompositeBody.setContent(compositeBody);
		scCompositeBody.setMinSize(scCompositeBody.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scCompositeBody.setExpandHorizontal(true);
		scCompositeBody.setExpandVertical(true);
	    
		return container;
	}
	
	/** 
	 * profiller 정보를 출력한다.
	 */
	private void showProfiller() {
		QueryExecuteResultDTO showProfiles = (QueryExecuteResultDTO)rsDAO.getMapExtendResult().get(MYSQL_EXTENSION_VIEW.SHOW_PROFILLING.name());
		
		// table data를 생성한다.
		final TadpoleResultSet trs = showProfiles.getDataList();
		final SQLResultSorter sqlSorter = new SQLResultSorter(-999);
		
		TableUtil.createTableColumn(tvShowProfiller, showProfiles, sqlSorter);
		
		tvShowProfiller.setLabelProvider(new QueryResultLabelProvider(reqQuery.getMode(), showProfiles));
		tvShowProfiller.setContentProvider(ArrayContentProvider.getInstance());
		
		// 쿼리를 설정한 사용자가 설정 한 만큼 보여준다.
		tvShowProfiller.setInput(trs.getData());
		tvShowProfiller.setSorter(sqlSorter);
	
		// Pack the columns
		TableUtil.packTable(tvShowProfiller.getTable());
	}
	
	/**
	 * diff show status
	 */
	private void showDiffStatus() {
		QueryExecuteResultDTO showStatus = (QueryExecuteResultDTO)rsDAO.getMapExtendResult().get(MYSQL_EXTENSION_VIEW.STATUS_VARIABLE.name());
		
		// table data를 생성한다.
		final TadpoleResultSet trs = showStatus.getDataList();
		final SQLResultSorter sqlSorter = new SQLResultSorter(-999);
		
		TableUtil.createTableColumn(tvShowStatus, showStatus, sqlSorter);
		
		tvShowStatus.setLabelProvider(new QueryResultLabelProvider(reqQuery.getMode(), showStatus));
		tvShowStatus.setContentProvider(ArrayContentProvider.getInstance());
		
		// 쿼리를 설정한 사용자가 설정 한 만큼 보여준다.
		tvShowStatus.setInput(trs.getData());
		tvShowStatus.setSorter(sqlSorter);
	
		// Pack the columns
		TableUtil.packTable(tvShowStatus.getTable());
	}
	
	/**
	 * execute plan
	 */
	private void showExecutePlan() {
		QueryExecuteResultDTO showExecutePlan = (QueryExecuteResultDTO)rsDAO.getMapExtendResult().get(MYSQL_EXTENSION_VIEW.EXECUTE_PLAN.name());
		
		// table data를 생성한다.
		final TadpoleResultSet trs = showExecutePlan.getDataList();
		final SQLResultSorter sqlSorter = new SQLResultSorter(-999);
		
		TableUtil.createTableColumn(tvExecutePlan, showExecutePlan, sqlSorter);
		
		tvExecutePlan.setLabelProvider(new QueryResultLabelProvider(reqQuery.getMode(), showExecutePlan));
		tvExecutePlan.setContentProvider(ArrayContentProvider.getInstance());
		
		// 쿼리를 설정한 사용자가 설정 한 만큼 보여준다.
		tvExecutePlan.setInput(trs.getData());
		tvExecutePlan.setSorter(sqlSorter);
	
		// Pack the columns
		TableUtil.packTable(tvExecutePlan.getTable());
	}
	
	/**
	 * crate title table
	 * 
	 * @param bodyComposite
	 * @param tv
	 * @param title
	 */
	private TableViewer createTitleTable(Composite bodyComposite, String title) {
		Composite _composite = new Composite(bodyComposite, SWT.NONE);
		_composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBtn = new GridLayout(1, false);
		gl_compositeBtn.verticalSpacing = 2;
		gl_compositeBtn.horizontalSpacing = 2;
		gl_compositeBtn.marginWidth = 0;
		gl_compositeBtn.marginHeight = 2;
		_composite.setLayout(gl_compositeBtn);
		
		Label labelTitle = new Label(_composite, SWT.NONE);
		labelTitle.setFont(SWTResourceManager.getFont(".SF NS Text", 15, SWT.NONE));
		labelTitle.setText(title);
		
		TableViewer tv = new TableViewer(_composite, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tv.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		return tv;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, CommonMessages.get().Close, true);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(700, 550);
	}

}
