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
package com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.composites.axisj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.utils.EditorDefine;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.SQLToLanguageConvert;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.application.SQLToAxisjConvert;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.composites.AbstractSQLToComposite;

/**
 * axisj composite
 * 
 * @author hangum
 *
 */
public class AxisjComposite extends AbstractSQLToComposite {
	private static final Logger logger = Logger.getLogger(AxisjComposite.class);
	
	private Text textConvert;
	private Text textVariable;
	private SQLToLanguageConvert slt;
	private Text textTheme;
	private Spinner spinnerFixedCol;
	private Button btnCheckFit2Width;
	private CCombo comboHeadAlign;
	private CCombo comboMergeCell;
	private CCombo comboHeight;
	private CCombo comboSort;
	private CCombo comboHeadTool;
	private CCombo comboViewMode;
	private TableViewer tableViewer;
	
	private CTabFolder tabFolder;
	private Browser browserPreview;
	private List<AxisjHeaderDAO> listAxisjHeader = new ArrayList<AxisjHeaderDAO>();

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AxisjComposite(Composite tabFolderObject, UserDBDAO userDB, String strTitle, final String sql, EditorDefine.SQL_TO_APPLICATION type) {
		super(tabFolderObject, SWT.NONE, userDB, sql, type);
		CTabItem tbtmTable = new CTabItem((CTabFolder)tabFolderObject, SWT.NONE);
		tbtmTable.setText(strTitle);
		tbtmTable.setData(type);
		
		Composite compositeBody = new Composite(tabFolderObject, SWT.NONE);
		tbtmTable.setControl(compositeBody);
		compositeBody.setLayout(new GridLayout(1, false));
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeTitle = new Composite(compositeBody, SWT.NONE);
		compositeTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeTitle.setLayout(new GridLayout(4, false));
		
		Label lblVariable = new Label(compositeTitle, SWT.NONE);
		lblVariable.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVariable.setText(Messages.get().Variable);
		
		slt = new SQLToLanguageConvert(userDB, type);
		textVariable = new Text(compositeTitle, SWT.BORDER);
		textVariable.setToolTipText("Target DIV ID");
		textVariable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textVariable.setText(slt.getDefaultVariable());
		
		Button btnConvertSQL = new Button(compositeTitle, SWT.NONE);
		btnConvertSQL.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sqlToStr();
				if(StringUtils.equalsIgnoreCase("Browser", tabFolder.getSelection().getText())){
					refreshBrowser();
				}
			}
		});
		btnConvertSQL.setText(String.format(Messages.get().SQLToStringDialog_btnNewButton_text, type));
		
		Button btnOriginalText = new Button(compositeTitle, SWT.NONE);
		btnOriginalText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textConvert.setText(sql);
				tabFolder.setSelection(0);	
			}
		});
		btnOriginalText.setText(Messages.get().SQLToStringDialog_4);
		
		Composite composite = new Composite(compositeBody, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(6, false));
		
		Label lblTheme = new Label(composite, SWT.NONE);
		lblTheme.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTheme.setText("Theme");
		
		textTheme = new Text(composite, SWT.BORDER);
		textTheme.setText("AXGrid");
		textTheme.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Fixed Column");
		
		spinnerFixedCol = new Spinner(composite, SWT.BORDER);
		
		Label lblFitToWidth = new Label(composite, SWT.NONE);
		lblFitToWidth.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFitToWidth.setText("Fit to Width");
		
		btnCheckFit2Width = new Button(composite, SWT.CHECK);
		btnCheckFit2Width.setSelection(true);
		
		Label lblHeaderAlign = new Label(composite, SWT.NONE);
		lblHeaderAlign.setAlignment(SWT.RIGHT);
		lblHeaderAlign.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHeaderAlign.setText("Header Align");
		
		comboHeadAlign = new CCombo(composite, SWT.BORDER);
		comboHeadAlign.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		comboHeadAlign.setItems(new String[] {"Left", "Center", "Right"});
		comboHeadAlign.setText("Center");
		comboHeadAlign.setEditable(false);
		
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Merge Cells");
		
		comboMergeCell = new CCombo(composite, SWT.BORDER);
		comboMergeCell.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		comboMergeCell.setItems(new String[] {"True", "False"});
		comboMergeCell.setText("False");
		
		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("Grid Height");
		
		comboHeight = new CCombo(composite, SWT.BORDER);
		comboHeight.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		comboHeight.setItems(new String[] {"Auto", "300", "500", "600", "800"});
		comboHeight.setText("500");
		
		Label lblSort = new Label(composite, SWT.NONE);
		lblSort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSort.setAlignment(SWT.RIGHT);
		lblSort.setText("Sort");
		
		comboSort = new CCombo(composite, SWT.BORDER);
		comboSort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		comboSort.setItems(new String[] {"True", "False"});
		comboSort.setText("True");
		comboSort.setEditable(false);
		
		Label lblNewLabel_3 = new Label(composite, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		lblNewLabel_3.setText("Head Tool");
		
		comboHeadTool = new CCombo(composite, SWT.BORDER);
		comboHeadTool.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		comboHeadTool.setItems(new String[] {"True", "False"});
		comboHeadTool.setText("True");
		comboHeadTool.setEditable(false);
		
		Label lblNewLabel_4 = new Label(composite, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setText("View Mode");
		
		comboViewMode = new CCombo(composite, SWT.BORDER);
		comboViewMode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		comboViewMode.setItems(new String[] {"Grid", "Icon", "Mobile"});
		comboViewMode.setText("Grid");
		comboViewMode.setEditable(false);
		
		SashForm sashForm = new SashForm(compositeBody, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tableViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION /* | SWT.VIRTUAL */);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		createTaleColumn();
		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new AxisJLabelProvider());
		tableViewer.setInput(listAxisjHeader);
		
		this.listAxisjHeader = SQLToAxisjConvert.initializeHead(listAxisjHeader, this.userDB, this.sql);
		tableViewer.refresh();
		
		tabFolder = new CTabFolder(sashForm, SWT.BORDER | SWT.BOTTOM);
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// selected preview 
				if(tabFolder.getSelectionIndex() == 1) {
					refreshBrowser();
				}
			}
		});
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		tabFolder.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());
		
		CTabItem tbtmHtml = new CTabItem(tabFolder, SWT.NONE);
		tbtmHtml.setText("HTML");
		
		textConvert = new Text(tabFolder, SWT.NONE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		tbtmHtml.setControl(textConvert);
		textConvert.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		CTabItem tabBrowser = new CTabItem(tabFolder, SWT.NONE);
		tabBrowser.setText("Browser");
		
		browserPreview = new Browser(tabFolder, SWT.NONE | SWT.H_SCROLL | SWT.V_SCROLL);
		tabBrowser.setControl(browserPreview);
		tabFolder.setSelection(0);		
		
		sqlToStr();
		sashForm.setWeights(new int[] {4, 5});
	}
	
	private void refreshBrowser(){
		try {
			String STR_PREVIEW_TEMPLATE = IOUtils.toString(AxisjConsts.class.getResource("AXISJ.PREVIEW.html"));
			
			STR_PREVIEW_TEMPLATE = StringUtils.replaceOnce(STR_PREVIEW_TEMPLATE, "_TDB_TEMPLATE_TITLE_", textVariable.getText());
			STR_PREVIEW_TEMPLATE = StringUtils.replaceOnce(STR_PREVIEW_TEMPLATE, "_AXISJ_JS_BLOCK_", textConvert.getText());
			
			browserPreview.setText(STR_PREVIEW_TEMPLATE);
		} catch (IOException e1) {
			logger.error("AXISJ preview exception", e1);
		}		
	}
	
	private void createTaleColumn() {

		for (int i = 0; i < AxisjConsts.sizes.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			TableColumn tblclmnColumnName = tableViewerColumn.getColumn();
			tblclmnColumnName.setWidth(AxisjConsts.sizes[i]);
			tblclmnColumnName.setText(AxisjConsts.names[i]);
			//tableViewerColumn.setLabelProvider(labelProvider);

			tableViewerColumn.setEditingSupport(new AxisjEditingSupport(tableViewer, i));
		}
	}

	/**
	 * sql to str
	 */
	private void sqlToStr() {
		StringBuffer sbStr = new StringBuffer();
		String[] sqls = parseSQL();
		
		if(StringUtils.isEmpty(textVariable.getText())){ 
			textVariable.setText(textVariable.getText());
		}
		
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("name", textVariable.getText());
		options.put("theme", textTheme.getText());
		if (StringUtils.isEmpty(spinnerFixedCol.getText())) {
			options.put("fixedColSeq", "0");
		}else{
			options.put("fixedColSeq", spinnerFixedCol.getText());
		}
		options.put("fitToWidth", btnCheckFit2Width.getSelection() ?"true":"false" );
		options.put("colHeadAlign", comboHeadAlign.getText().toLowerCase());
		if(StringUtils.containsIgnoreCase(comboMergeCell.getText(),"true") ){//전체셀 병합
			options.put("mergeCells", "true");
		}else if (StringUtils.containsIgnoreCase(comboMergeCell.getText(),"false") ){// 병합안함.
			options.put("mergeCells", "false");
		}else{//지정된 인덱스만
			try{
				String[] array = StringUtils.split(comboMergeCell.getText(), ',');				
				if (array.length > 0){
					StringBuffer sb = new StringBuffer();
					for (String idx : array) {
						sb.append(Integer.valueOf(idx) + ","); //컬럼 인덱스를 숫자로 입력했는지 확인.
					}
					options.put("mergeCells", "[" + StringUtils.removeEnd(sb.toString(), ",") +"]");
				}else{
					// 지정된 인덱스가 없을경우.
					options.put("mergeCells", "false");
				}
			}catch (Exception e){
				options.put("mergeCells", "false");
			}
		}
		if(StringUtils.containsIgnoreCase(comboHeight.getText(), "auto")) {
			options.put("height", "\"auto\"");
		}else{
			options.put("height", comboHeight.getText());
		}
		options.put("sort", comboSort.getText().toLowerCase());
		options.put("colHeadTool", comboHeadTool.getText().toLowerCase());
		options.put("viewMode", comboViewMode.getText().toLowerCase());
		
		for(int i=0; i < sqls.length; i++) {
			if("".equals(StringUtils.trimToEmpty(sqls[i]))) continue; //$NON-NLS-1$
			
			if(i ==0){
				sbStr.append( slt.sqlToString(sqls[i], options, listAxisjHeader));
			}else{
				options.put("name", textVariable.getText() + "_" + i);
				sbStr.append( slt.sqlToString(sqls[i], options, listAxisjHeader));
			}
			
			// 쿼리가 여러개일 경우 하나씩 한개를 준다.
			sbStr.append("\r\n"); //$NON-NLS-1$
		}
		
		textConvert.setText(sbStr.toString());
	}

}
