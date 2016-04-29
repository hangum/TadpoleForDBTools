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
package com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.composites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.ace.editor.core.define.EditorDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.QueryUtils;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.AxisJLabelProvider;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.AxisjConsts;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.AxisjEditingSupport;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.SQLToLanguageConvert;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.application.SQLToAxisjConvert;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.application.SQLToJavaConvert;
import com.hangum.tadpole.rdb.core.ext.Activator;
import com.hangum.tadpole.rdb.core.ext.sampledata.GenType;
import com.hangum.tadpole.rdb.core.ext.sampledata.SampleDataConsts;
import com.hangum.tadpole.rdb.core.ext.sampledata.SampleDataEditingSupport;
import com.hangum.tadpole.rdb.core.ext.sampledata.SampleDataGenDAO;
import com.hangum.tadpole.rdb.core.ext.sampledata.SampleDataGenerateExecutor;
import com.swtdesigner.ResourceManager;

import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.custom.CCombo;

/**
 * axisj composite
 * 
 * @author hangum
 *
 */
public class AxisjComposite extends AbstractSQLToComposite {
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
			}
		});
		btnConvertSQL.setText(Messages.get().SQLToStringDialog_btnNewButton_text);
		
		Button btnOriginalText = new Button(compositeTitle, SWT.NONE);
		btnOriginalText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textConvert.setText(sql);
			}
		});
		btnOriginalText.setText(Messages.get().SQLToStringDialog_4);
		
		Composite composite = new Composite(compositeBody, SWT.NONE);
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
		

		Composite compositeHead = new Composite(compositeBody, SWT.NONE);
		GridData gd_compositeHead = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_compositeHead.minimumHeight = 200;
		compositeHead.setLayoutData(gd_compositeHead);
		compositeHead.setLayout(new GridLayout(1, false));

		tableViewer = new TableViewer(compositeHead, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		createTaleColumn();

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new AxisJLabelProvider());
		tableViewer.setInput(listAxisjHeader);

		this.listAxisjHeader = SQLToAxisjConvert.initializeHead(listAxisjHeader, this.userDB, this.sql);
		
		tableViewer.refresh();

		
		textConvert = new Text(compositeBody, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		textConvert.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		
		sqlToStr();
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
				options.put("name", options.get("name") + "_" + i);
				sbStr.append( slt.sqlToString(sqls[i], options, listAxisjHeader));
			}
			
			// 쿼리가 여러개일 경우 하나씩 한개를 준다.
			sbStr.append("\r\n"); //$NON-NLS-1$
		}
		
		textConvert.setText(sbStr.toString());
	}

}
