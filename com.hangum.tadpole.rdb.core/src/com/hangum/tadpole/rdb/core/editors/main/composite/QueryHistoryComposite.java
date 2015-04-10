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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.dialogs.message.TadpoleMessageDialog;
import com.hangum.tadpole.commons.dialogs.message.dao.SQLHistoryDAO;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_ExecutedSQL;
import com.hangum.tadpole.rdb.core.Messages;
import com.swtdesigner.SWTResourceManager;

/**
 * query history composite
 * 
 * @author hangum
 *
 */
public class QueryHistoryComposite extends Composite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(QueryHistoryComposite.class);

	/** result composite */
	private ResultMainComposite rdbResultComposite;
	
	private Grid gridSQLHistory;
	private Text textHistoryFilter;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public QueryHistoryComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		gridSQLHistory = new Grid(this, SWT.V_SCROLL | SWT.BORDER);
		gridSQLHistory.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		gridSQLHistory.setLinesVisible(true);
		gridSQLHistory.setHeaderVisible(true);
		gridSQLHistory.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		gridSQLHistory.setToolTipText("");
		
		gridSQLHistory.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				GridItem[] gridItems = gridSQLHistory.getSelection();
				if(gridItems != null) {
					appendText(convHtmlToLine(gridItems[0].getText(2)));
				}
			}
		});
		
		createTableHistoryColumn();
		
		Composite compositeRecallBtn = new Composite(this, SWT.NONE);
		compositeRecallBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeRecallBtn = new GridLayout(8, false);
		gl_compositeRecallBtn.marginWidth = 1;
		gl_compositeRecallBtn.marginHeight = 0;
		compositeRecallBtn.setLayout(gl_compositeRecallBtn);
		
		final Button btnExportHistory = new Button(compositeRecallBtn, SWT.NONE);
		btnExportHistory.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnExportHistory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GridItem[] gridItems = gridSQLHistory.getSelection();
				if(gridItems != null) {
					appendText(convHtmlToLine(gridItems[0].getText(2)));
				} else {
					MessageDialog.openInformation(null, Messages.MainEditor_2, Messages.MainEditor_29);
				}
			}
		});
		btnExportHistory.setText(Messages.MainEditor_12);
		
		Button btnDetailView = new Button(compositeRecallBtn, SWT.NONE);
		btnDetailView.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GridItem[] gridItems = gridSQLHistory.getSelection();
				if(gridItems != null) {
					TadpoleMessageDialog dlg = new TadpoleMessageDialog(getShell(), Messages.MainEditor_11, 
							gridItems[0].getText(1), convHtmlToLine(gridItems[0].getText(2)) );
					dlg.open();
				} else {
					MessageDialog.openInformation(null, Messages.MainEditor_2, Messages.MainEditor_29);
				}
			}
		});
		btnDetailView.setText(Messages.MainEditor_btnDetailView_text);
		
//		Button btnSetEditor = new Button(compositeRecallBtn, SWT.NONE);
//		btnSetEditor.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//		btnSetEditor.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				StringBuffer sbExportData = new StringBuffer();
//				
//				for(SQLHistoryDAO dao : listSQLHistory) {
//					sbExportData.append( dao.getStrSQLText() ).append(PublicTadpoleDefine.LINE_SEPARATOR); //$NON-NLS-1$
//				}
//				
////				downloadExtFile(getRdbResultComposite().getUserDB().getDisplay_name() + "_RecallSQLExport.txt", sbExportData.toString()); //$NON-NLS-1$
//			}
//		});
//		btnSetEditor.setText(Messages.MainEditor_17);
		
		// table clear
		Button btnHistoyClear = new Button(compositeRecallBtn, SWT.NONE);
		btnHistoyClear.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnHistoyClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearGrid();
			}
		});
		btnHistoyClear.setText(Messages.MainEditor_btnClear_text);
		
		Label labelDumyRecal = new Label(compositeRecallBtn, SWT.NONE);
		labelDumyRecal.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		textHistoryFilter = new Text(compositeRecallBtn, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		textHistoryFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.Selection) refreshSqlHistory();
			}
		});
		textHistoryFilter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		// refresh
		Button btnRefresh = new Button(compositeRecallBtn, SWT.NONE);
		btnRefresh.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshSqlHistory();
			}
		});
		btnRefresh.setText(Messages.MainEditor_24);
	}
	
	/**
	 * set parent composite
	 * 
	 * @param rdbResultComposite
	 */
	public void setRDBResultComposite(ResultMainComposite rdbResultComposite) {
		this.rdbResultComposite = rdbResultComposite;
	}
	public ResultMainComposite getRdbResultComposite() {
		return rdbResultComposite;
	}
	
	private void clearGrid() {
		GridItem[] gridItems = gridSQLHistory.getItems();
		for (GridItem gridItem : gridItems) {
			gridItem.dispose();
		}
	}
	
	/**
	 * 쿼리 후 실행결과를 히스토리화면과 프로파일에 저장합니다.
	 */
	public void afterQueryInit(SQLHistoryDAO sqlHistoryDAO) {
		try {
			TadpoleSystem_ExecutedSQL.saveExecuteSQUeryResource(getRdbResultComposite().getUserSeq(), 
							getRdbResultComposite().getUserDB(), 
							PublicTadpoleDefine.EXECUTE_SQL_TYPE.EDITOR, 
							sqlHistoryDAO);
		
			addRowData(sqlHistoryDAO);
		} catch(Exception e) {
			logger.error("save the user query", e); //$NON-NLS-1$
		}
	}
	
	private String convLineToHtml(String str) {
		if(str == null) return "";
//		str = StringUtils.replace(str, "\n", "<br/>");
		return StringUtils.replace(str, PublicTadpoleDefine.LINE_SEPARATOR, "<br/>");
	}
	private String convHtmlToLine(String str) {
		return StringUtils.replace(str, "<br/>", PublicTadpoleDefine.LINE_SEPARATOR);
	}
	
	/**
	 * 해당일에 실행했던 쿼리를 보여줍니다.
	 * 
	 * reference https://github.com/hangum/TadpoleForDBTools/issues/387
	 */
	public void findHistoryData() {
		try {
			List<SQLHistoryDAO> listSQLHistory  = TadpoleSystem_ExecutedSQL.getExecuteQueryHistory(
										getRdbResultComposite().getUserSeq(), 
										getRdbResultComposite().getUserDB().getSeq(), 
										textHistoryFilter.getText().trim());
			for (SQLHistoryDAO sqlHistoryDAO : listSQLHistory) {
				addRowData(sqlHistoryDAO);
			}
		} catch(Exception ee) {
			logger.error("Executed SQL History call", ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * add grid row
	 * 
	 * @param sqlHistoryDAO
	 */
	private void addRowData(SQLHistoryDAO sqlHistoryDAO) {
		GridItem item = new GridItem(gridSQLHistory, SWT.V_SCROLL | SWT.H_SCROLL);
		
		String strSQL = StringUtils.strip(sqlHistoryDAO.getStrSQLText());
		int intLine = StringUtils.countMatches(strSQL, "\n");
		if(intLine >= 1) {
			int height = (intLine+1) * 24;
			if(height > 100) item.setHeight(100); 
			else item.setHeight(height);
		}
		
		item.setText(0, ""+gridSQLHistory.getRootItemCount());
		item.setText(1, Utils.dateToStr(sqlHistoryDAO.getStartDateExecute()));
		item.setText(2, convLineToHtml(strSQL));
		item.setToolTipText(2, strSQL);
		
		item.setText(3, ""+( (sqlHistoryDAO.getEndDateExecute().getTime() - sqlHistoryDAO.getStartDateExecute().getTime()) / 1000f));
		item.setText(4, ""+sqlHistoryDAO.getRows());
		item.setText(5, sqlHistoryDAO.getResult());
		
		item.setText(6, convLineToHtml(sqlHistoryDAO.getMesssage()));
		item.setToolTipText(6, sqlHistoryDAO.getMesssage());
		
		if("F".equals(sqlHistoryDAO.getResult())) {
			item.setBackground(SWTResourceManager.getColor(240, 180, 167));
		}
	}
	
	/**
	 * refresh sql history table 
	 */
	private void refreshSqlHistory() {
		clearGrid();
		findHistoryData();
	}
	
	private void appendText(String cmd) {
		getRdbResultComposite().appendText(cmd);
	}

	@Override
	protected void checkSubclass() {
	}
	
	/**
	 * history column
	 * 
	 * @param tv
	 * @param sorterHistory
	 * @param layoutColumnLayout
	 */
	private void createTableHistoryColumn() {
		// time
		GridColumn tvcSeq = new GridColumn(gridSQLHistory, SWT.LEFT);
		tvcSeq.setWidth(50);
		tvcSeq.setText("#");
				
		// time
		GridColumn tvcDate = new GridColumn(gridSQLHistory, SWT.LEFT);
		tvcDate.setWidth(150);
		tvcDate.setText("Date");
		
		// sql
		GridColumn tvcSQL = new GridColumn(gridSQLHistory, SWT.LEFT);
		tvcSQL.setWidth(300);
		tvcSQL.setText("SQL");
		tvcSQL.setWordWrap(true);

		// duration
		GridColumn tvcDuration = new GridColumn(gridSQLHistory, SWT.RIGHT);
		tvcDuration.setWidth(60);
		tvcDuration.setText("Sec");
		
		// rows
		GridColumn tvcRows = new GridColumn(gridSQLHistory, SWT.RIGHT);
		tvcRows.setWidth(60);
		tvcRows.setText("Rows");
		
		// result
		GridColumn tvcResult = new GridColumn(gridSQLHistory, SWT.NONE);
		tvcResult.setWidth(90);
		tvcResult.setText("Result");

		GridColumn tvcMessage = new GridColumn(gridSQLHistory, SWT.NONE);
		tvcMessage.setWidth(250);
		tvcMessage.setText("Message");
	}
	
//	protected void calculateHeight() {
//		for (GridItem item : tvSQLHistory.getGrid().getItems()) {
//			GC gc = new GC(item.getDisplay());
//			GridColumn gridColumn = tvSQLHistory.getGrid().getColumn(1);
//			Point textBounds = gridColumn.getCellRenderer().computeSize(gc, gridColumn.getWidth(), SWT.DEFAULT, item);
//			gc.dispose();
//			item.setHeight(textBounds.y);
//		}
//	}
	
}
