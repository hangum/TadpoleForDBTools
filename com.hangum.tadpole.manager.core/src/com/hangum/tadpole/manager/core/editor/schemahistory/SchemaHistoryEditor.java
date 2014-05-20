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
package com.hangum.tadpole.manager.core.editor.schemahistory;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.sql.dao.system.SchemaHistoryDAO;
import com.hangum.tadpole.sql.dao.system.SchemaHistoryDetailDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_SchemaHistory;
import com.hangum.tadpole.sql.query.TadpoleSystem_UserDBQuery;
import com.swtdesigner.ResourceManager;

/**
 * Schema History
 * 
 * @author hangum
 *
 */
public class SchemaHistoryEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(SchemaHistoryEditor.class);
	public static String ID = "com.hangum.tadpole.manager.core.editor.schemahistory";
	
	private Combo comboDisplayName;
	private Combo textWorkType;
	private Text textObjectID;
	private TableViewer tableViewer;
	private Combo textObjectType;
	
	private DateTime dateTimeStart;
	private DateTime dateTimeEnd;
	private Text textDateLeft;
	private Text textLeftSQL;
	private Text textDateRight;
	private Text textSQLRight;
	
	public SchemaHistoryEditor() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite compositeHead = new Composite(parent, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(6, false));
		
		Label lblDb = new Label(compositeHead, SWT.NONE);
		lblDb.setText("DB");
		
		comboDisplayName = new Combo(compositeHead, SWT.READ_ONLY);
		comboDisplayName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblWorkType = new Label(compositeHead, SWT.NONE);
		lblWorkType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblWorkType.setText("Work Type");
		
		textWorkType = new Combo(compositeHead, SWT.BORDER);
		textWorkType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textWorkType.add("");
		textWorkType.add("CREATE");
		textWorkType.add("ALTER");
		textWorkType.add("DROP");
		
		Label lblObjectType = new Label(compositeHead, SWT.NONE);
		lblObjectType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblObjectType.setText("Object Type");
		
		textObjectType = new Combo(compositeHead, SWT.BORDER);
		textObjectType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textObjectType.add("");
		textObjectType.add("TABLE");
		textObjectType.add("VIEW");
		textObjectType.add("INDEX");
		textObjectType.add("PROCEDURE");
		textObjectType.add("FUNCTION");
		textObjectType.add("TRIGGER");
		
		Label lblObjectId = new Label(compositeHead, SWT.NONE);
		lblObjectId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblObjectId.setText("Object Name");
		
		textObjectID = new Text(compositeHead, SWT.BORDER);
		textObjectID.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite = new Composite(compositeHead, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		GridLayout gl_composite = new GridLayout(4, false);
		gl_composite.verticalSpacing = 1;
		gl_composite.horizontalSpacing = 1;
		gl_composite.marginWidth = 1;
		gl_composite.marginHeight = 1;
		composite.setLayout(gl_composite);
		
		Label lblStart = new Label(composite, SWT.NONE);
		lblStart.setText("Date");
		
		dateTimeStart = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("~");
		
		dateTimeEnd = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		
		Button btnSearch = new Button(compositeHead, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
		});
		btnSearch.setImage(ResourceManager.getPluginImage("com.hangum.tadpole.manager.core", "resources/icons/search.png"));
		btnSearch.setText("Search");
		
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeList = new Composite(sashForm, SWT.NONE);
		compositeList.setLayout(new GridLayout(1, false));
		
		tableViewer = new TableViewer(compositeList, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Button btnCompare = new Button(compositeList, SWT.NONE);
		btnCompare.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				compare();
			}
		});
		btnCompare.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnCompare.setText("Compare");
		createTableColumn();
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new SchemaHistorLabelProvider());
		
		// result composite
		Composite compositeDetail = new Composite(sashForm, SWT.NONE);
		compositeDetail.setLayout(new GridLayout(1, false));
		
		SashForm sashFormDetail = new SashForm(compositeDetail, SWT.NONE);
		sashFormDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeLeft = new Composite(sashFormDetail, SWT.NONE);
		compositeLeft.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(compositeLeft, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Date");
		
		textDateLeft = new Text(compositeLeft, SWT.BORDER);
		textDateLeft.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		textLeftSQL = new Text(compositeLeft, SWT.BORDER | SWT.MULTI);
		textLeftSQL.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		Composite compositeRight = new Composite(sashFormDetail, SWT.NONE);
		compositeRight.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel_1 = new Label(compositeRight, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Date");
		
		textDateRight = new Text(compositeRight, SWT.BORDER);
		textDateRight.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		textSQLRight = new Text(compositeRight, SWT.BORDER | SWT.MULTI);
		textSQLRight.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		sashFormDetail.setWeights(new int[] {1, 1});
		
		sashForm.setWeights(new int[] {4, 6});

		initUI();
	}
	
	/**
	 * history compare
	 */
	private void compare() {
		IStructuredSelection ss = (IStructuredSelection)tableViewer.getSelection();
		if(!ss.isEmpty()) {
			Object[] objListSel = ss.toArray();
			
			try {
				for(int i=0; i<objListSel.length; i++) {
					if(i==2) break;
					
					SchemaHistoryDAO dao = (SchemaHistoryDAO)objListSel[i];
					String strSQL = getSQL(dao.getSeq());
					if(i==0) {
						textDateLeft.setText(dao.getCreate_date().toLocaleString());
						textLeftSQL.setText(strSQL);
					} else {
						textDateRight.setText(dao.getCreate_date().toLocaleString());
						textSQLRight.setText(strSQL);
					}
				}
			} catch(Exception e) {
				logger.error("Get detail sql", e);
			}
			if(objListSel.length == 1) {
				textDateRight.setText("");
				textSQLRight.setText("");
			}

		} else {
			textDateLeft.setText("");
			textLeftSQL.setText("");
			textDateRight.setText("");
			textSQLRight.setText("");
		}
	}
	
	/**
	 * detail sql
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	private String getSQL(int seq) throws Exception {
		String strSQL = "";
		List<SchemaHistoryDetailDAO> listDeatilDao = TadpoleSystem_SchemaHistory.getExecuteQueryHistoryDetail(seq);
		for(SchemaHistoryDetailDAO deatilDao : listDeatilDao) {
			strSQL += deatilDao.getSource();
		}
		
		return strSQL;
	}
	
	/**
	 * search
	 */
	private void search() {
		
		try {
			int dbSeq = ((UserDBDAO)comboDisplayName.getData(comboDisplayName.getText())).getSeq();
			String workType = textWorkType.getText();
			String objectType = textObjectType.getText();
			String objectId = textObjectID.getText();
			
			Calendar cal = Calendar.getInstance();
			cal.set(dateTimeStart.getYear(), dateTimeStart.getMonth(), dateTimeStart.getDay(), 0, 0, 0);
			long startTime = cal.getTimeInMillis();
			cal.set(dateTimeEnd.getYear(), dateTimeEnd.getMonth(), dateTimeEnd.getDay(), 23, 59, 59);
			long endTime = cal.getTimeInMillis();
			
			List<SchemaHistoryDAO> listSchemaHistory = TadpoleSystem_SchemaHistory.getExecuteQueryHistory(dbSeq, workType, objectType, objectId, startTime, endTime);
			tableViewer.setInput(listSchemaHistory);
			tableViewer.refresh();
		} catch(Exception e) {
			logger.debug("schema history", e);
		}
	}
	
	/**
	 * UI init
	 */
	private void initUI() {
		try {
			List<UserDBDAO> listUserDBDAO = TadpoleSystem_UserDBQuery.getUserDB();
			for (UserDBDAO userDBDAO : listUserDBDAO) {
				comboDisplayName.add(userDBDAO.getDisplay_name());
				comboDisplayName.setData(userDBDAO.getDisplay_name(), userDBDAO);
			}
			comboDisplayName.select(0);
			
			// Range of date
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, -7);
			dateTimeStart.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			
		} catch(Exception e) {
			logger.error("SchemaHistor view init fail", e);
		}
		
		search();
	}
	
	/**
	 * create table columns
	 */
	private void createTableColumn() {
		String[] names = {"Name", "Object Name", "Work Type", "Object Type", "Date"};
		int[] sizes = {120, 120, 100, 100, 200};
				
		for(int i=0; i<names.length; i++) {
			String name = names[i];
			int size = sizes[i];
			
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			TableColumn tblclmnEngine = tableViewerColumn.getColumn();
			tblclmnEngine.setWidth(size);
			tblclmnEngine.setText(name);
		}
	}

	@Override
	public void setFocus() {
	}
	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		SchemaHistoryEditorInput esqli = (SchemaHistoryEditorInput) input;
		setPartName(esqli.getName());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}

/**
 * 
 * @author hangum
 *
 */
class SchemaHistorLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		SchemaHistoryDAO dao = (SchemaHistoryDAO)element;
		
		switch(columnIndex) {
		case 0: return dao.getName();
		case 1: return dao.getObject_id();
		case 2: return dao.getWork_type();
		case 3: return dao.getObject_type();
		case 4: return dao.getCreate_date().toLocaleString();
		}
		
		return dao.toString();
	}
}