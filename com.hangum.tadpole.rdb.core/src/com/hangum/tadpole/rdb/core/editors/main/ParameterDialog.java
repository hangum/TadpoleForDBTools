package com.hangum.tadpole.rdb.core.editors.main;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ParameterDialog extends Dialog {
	private Table table;	
	private UserDBDAO userDB;
	private int paramCount = 0;
	private List<Map<Integer, Object>> parameters;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ParameterDialog(Shell parentShell) {
		super(parentShell);
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public ParameterDialog(Shell parentShell, UserDBDAO userDB, String executeQuery) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);

		this.userDB = userDB;
		this.calcParamCount(executeQuery);
		makeParamCount();
	}



	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		TableColumnLayout tcl_composite = new TableColumnLayout();
		composite.setLayout(tcl_composite);
		
		TableViewer tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		CreateTableColumn(tableViewer, tcl_composite);

		//tableViewer.setContentProvider(new ParamContentProvider(parameters));
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ParamLabelProvider());
		tableViewer.setInput(parameters);
		
		tableViewer.refresh();
		
		return container;
	}

	private void CreateTableColumn(TableViewer tableViewer, TableColumnLayout tcl_composite) {
		TableViewerColumn tvcSeq = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcSeq = tvcSeq.getColumn();
		tcl_composite.setColumnData(tcSeq, new ColumnPixelData(50, true, true));
		tcSeq.setText("Seq");
		
		TableViewerColumn tvcName = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcName = tvcName.getColumn();
		tcl_composite.setColumnData(tcName, new ColumnPixelData(150, true, true));
		tcName.setText("Param Name");
		
		TableViewerColumn tvcType = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcType = tvcType.getColumn();
		tcl_composite.setColumnData(tcType, new ColumnPixelData(150, true, true));
		tcType.setText("Data Type");
		tvcType.setEditingSupport(new ParameterEditingSupport(tableViewer,2));
		
		TableViewerColumn tvcValue = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcValue = tvcValue.getColumn();
		tcl_composite.setColumnData(tcValue, new ColumnPixelData(300, true, true));
		tcValue.setText("Param Value");		
		tvcValue.setEditingSupport(new ParameterEditingSupport(tableViewer,3));
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		Button button = createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				parameters.clear();
			}
		});
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	public ParameterObject getParameterObject() {
		ParameterObject param = new ParameterObject();
		
		// TODO : 파라리터 타입 검증 및 처리방법 리팩토
		for(Map<Integer, Object> paramE: parameters){
			if(paramE.get(2).toString().equals("String")){
				param.setObject(String.valueOf(paramE.get(3)));
			}else if(paramE.get(2).toString().equals("Long")){
				param.setObject(Long.valueOf(paramE.get(3).toString()));
			}else if(paramE.get(2).toString().equals("Integer")){
				param.setObject(Integer.valueOf(paramE.get(3).toString()));
			}
		}
		
		return param;
		
	}
	
	protected void calcParamCount(String executeQuery){		
		
		java.sql.Connection javaConn = null;
		PreparedStatement stmt = null;		
		try {
			SqlMapClient client = TadpoleSQLManager.getInstance(userDB);
			javaConn = client.getDataSource().getConnection();	
			stmt = javaConn.prepareStatement(executeQuery);			
			paramCount = stmt.getParameterMetaData().getParameterCount();
				
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			try { stmt.close(); } catch(Exception e) {}
			try { javaConn.close(); } catch(Exception e){}
		}
	}
	
	protected  void makeParamCount(){		
		parameters = new ArrayList<Map<Integer, Object>>();
		for (int i=0; i<paramCount; i++){
			Map<Integer, Object> map = new HashMap<Integer, Object>();
			map.put(0, (i+1));
			map.put(1, "Param" + (i+1));
			map.put(2, "String");
			map.put(3,  "");
			parameters.add(map);
		}
	}
	
	public int getParamCount(){		
		return paramCount;
	}
}
