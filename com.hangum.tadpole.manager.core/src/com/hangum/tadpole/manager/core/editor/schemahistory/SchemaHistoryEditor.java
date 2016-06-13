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
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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

import com.hangum.tadpole.ace.editor.core.widgets.TadpoleCompareWidget;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.SchemaHistoryDAO;
import com.hangum.tadpole.engine.query.dao.system.SchemaHistoryDetailDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_SchemaHistory;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.engine.utils.TimeZoneUtil;
import com.hangum.tadpole.manager.core.Messages;
import com.swtdesigner.ResourceManager;

/**
 * Schema History
 * 
 * @author hangum
 *
 */
public class SchemaHistoryEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(SchemaHistoryEditor.class);
	public static String ID = "com.hangum.tadpole.manager.core.editor.schemahistory"; //$NON-NLS-1$
	
	private Combo comboDisplayName;
	private Combo comboWorkType;
	private Text textObjectID;
	private TableViewer tableViewer;
	private Combo comboObjectType;
	
	private DateTime dateTimeStart;
	private DateTime dateTimeEnd;
	private TadpoleCompareWidget compareWidget;
	
	public SchemaHistoryEditor() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite compositeHead = new Composite(parent, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(6, false));
		
		Label lblDb = new Label(compositeHead, SWT.NONE);
		lblDb.setText(Messages.get().Database);
		
		comboDisplayName = new Combo(compositeHead, SWT.READ_ONLY);
		comboDisplayName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblWorkType = new Label(compositeHead, SWT.NONE);
		lblWorkType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblWorkType.setText(Messages.get().SchemaHistoryEditor_2);
		
		comboWorkType = new Combo(compositeHead, SWT.BORDER);
		comboWorkType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboWorkType.add(""); //$NON-NLS-1$
		comboWorkType.add("CREATE"); //$NON-NLS-1$
		comboWorkType.add("ALTER"); //$NON-NLS-1$
		comboWorkType.add("DROP"); //$NON-NLS-1$
		
		Label lblObjectType = new Label(compositeHead, SWT.NONE);
		lblObjectType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblObjectType.setText(Messages.get().SchemaHistoryEditor_7);
		
		comboObjectType = new Combo(compositeHead, SWT.BORDER);
		comboObjectType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboObjectType.setVisibleItemCount(7);
		
		comboObjectType.add(""); //$NON-NLS-1$
		comboObjectType.add("TABLE"); //$NON-NLS-1$
		comboObjectType.add("VIEW"); //$NON-NLS-1$
		comboObjectType.add("INDEX"); //$NON-NLS-1$
		comboObjectType.add("PROCEDURE"); //$NON-NLS-1$
		comboObjectType.add("FUNCTION"); //$NON-NLS-1$
		comboObjectType.add("TRIGGER"); //$NON-NLS-1$
		
		Label lblObjectId = new Label(compositeHead, SWT.NONE);
		lblObjectId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblObjectId.setText(Messages.get().SchemaHistoryEditor_15);
		
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
		lblStart.setText(Messages.get().Date);
		
		dateTimeStart = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("~"); //$NON-NLS-1$
		
		dateTimeEnd = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN);
		
		Button btnSearch = new Button(compositeHead, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
		});
		btnSearch.setImage(ResourceManager.getPluginImage("com.hangum.tadpole.manager.core", "resources/icons/search.png")); //$NON-NLS-1$ //$NON-NLS-2$
		btnSearch.setText(Messages.get().Search);
		
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeList = new Composite(sashForm, SWT.NONE);
		compositeList.setLayout(new GridLayout(1, false));
		
		tableViewer = new TableViewer(compositeList, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				try {
					IStructuredSelection ss = (IStructuredSelection)tableViewer.getSelection();
					if(!ss.isEmpty()) {
						SchemaHistoryDAO dao = (SchemaHistoryDAO)ss.getFirstElement();
						String strSQL = getSQL(dao.getSeq());
						compareWidget.changeDiff(strSQL, "");
							
					}
				} catch(Exception e) {
					logger.error("select change tree", e); //$NON-NLS-1$
				}
			}
		});
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
		btnCompare.setText(Messages.get().SchemaHistoryEditor_22);
		createTableColumn();
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new SchemaHistorLabelProvider());
		
		// result composite
		Composite compositeDetail = new Composite(sashForm, SWT.BORDER);
		compositeDetail.setLayout(new GridLayout(1, false));
		
		compareWidget = new TadpoleCompareWidget(compositeDetail, SWT.BORDER);
		compareWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
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
				String source = "", target = "";
				for(int i=0; i<objListSel.length; i++) {
					if(i==2) break;
					
					SchemaHistoryDAO dao = (SchemaHistoryDAO)objListSel[i];
					String strSQL = getSQL(dao.getSeq());
					if(i==0) {
						source = strSQL;
					} else {
						target = strSQL;
					}
				}
				
				compareWidget.changeDiff(source, target);
			} catch(Exception e) {
				logger.error("Get detail sql", e); //$NON-NLS-1$
			}

		} else {
			compareWidget.changeDiff("", "");
		}
	}
	
	/**
	 * detail sql
	 * @param seq
	 * @return
	 * @throws Exception
	 */
	private String getSQL(int seq) throws Exception {
		String strSQL = ""; //$NON-NLS-1$
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
			String workType = comboWorkType.getText();
			String objectType = comboObjectType.getText();
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
			logger.debug("schema history", e); //$NON-NLS-1$
		}
	}
	
	/**
	 * UI init
	 */
	private void initUI() {
		try {
			List<UserDBDAO> listUserDBDAO = TadpoleSystem_UserDBQuery.getSessionUserDB();
			for (UserDBDAO userDBDAO : listUserDBDAO) {
				if(userDBDAO.getDBDefine() != DBDefine.MONGODB_DEFAULT) {
					comboDisplayName.add(userDBDAO.getDisplay_name());
					comboDisplayName.setData(userDBDAO.getDisplay_name(), userDBDAO);
				}
			}
			comboDisplayName.select(0);
			
			// Range of date
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, -7);
			dateTimeStart.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			
		} catch(Exception e) {
			logger.error("SchemaHistor view init fail", e); //$NON-NLS-1$
		}
		
		search();
		
		// google analytic
		AnalyticCaller.track(SchemaHistoryEditor.ID);
	}
	
	/**
	 * create table columns
	 */
	private void createTableColumn() {
		String[] names = {Messages.get().SchemaHistoryEditor_35, Messages.get().SchemaHistoryEditor_36, Messages.get().SchemaHistoryEditor_37, Messages.get().SchemaHistoryEditor_38, Messages.get().Date};
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
		case 4: return TimeZoneUtil.dateToStr(dao.getCreate_date());
		}
		
		return dao.toString();
	}
}