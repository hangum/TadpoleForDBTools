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
package com.hangum.tadpole.monitoring.core.editors.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.engine.query.dao.system.ScheduleMainDAO;
import com.hangum.tadpole.engine.query.dao.system.ScheduleResultDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_Schedule;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.monitoring.core.Messages;
import com.hangum.tadpole.monitoring.core.dialogs.schedule.AddScheduleDialog;
import com.hangum.tadpole.monitoring.core.manager.schedule.ScheduleManager;

/**
 * Tadpole Monitoring editor
 * 
 * @author hangum
 *
 */
public class ScheduleEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(ScheduleEditor.class);
	public static final String ID = "com.hangum.tadpole.monitoring.core.editor.schedule"; //$NON-NLS-1$
	
	private List<ScheduleMainDAO> listScheduleMain = new ArrayList<ScheduleMainDAO>();
	private TableViewer tableViewerList;
	private TableViewer tvResult;

	private ToolItem tltmModify;
	private ToolItem tltmDelete;
	
	public ScheduleEditor() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		Composite compositeHead = new Composite(parent, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(1, false));
		
		Label lblInfo = new Label(compositeHead, SWT.NONE);
		lblInfo.setText(Messages.get().ScheduleEditor_1);
		
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sashForm.setOrientation(SWT.HORIZONTAL);
		
		Group compositeList = new Group(sashForm, SWT.NONE);
		compositeList.setLayout(new GridLayout(1, false));
		compositeList.setText("Schedule List");
		
		ToolBar toolBar = new ToolBar(compositeList, SWT.FLAT | SWT.RIGHT);
		
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshSchedule();
			}
		});
		tltmRefresh.setText(Messages.get().ScheduleEditor_2);
		
		tltmModify = new ToolItem(toolBar, SWT.NONE);
		tltmModify.setEnabled(false);
		tltmModify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection iss = (IStructuredSelection)tableViewerList.getSelection();
				if(!iss.isEmpty()) {
					try {
						ScheduleMainDAO dao = (ScheduleMainDAO)iss.getFirstElement();
						UserDBDAO userDB = TadpoleSystem_UserDBQuery.getUserDBInstance(dao.getDb_seq());
						AddScheduleDialog dialog = new AddScheduleDialog(null, userDB, dao);
						dialog.open();
						
					} catch(Exception e1) {
						logger.error("modify schedule", e1);
					}
				}
			}
		});
		tltmModify.setText(Messages.get().ScheduleEditor_tltmModify_text);
		
		tltmDelete = new ToolItem(toolBar, SWT.NONE);
		tltmDelete.setEnabled(false);
		tltmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection iss = (IStructuredSelection)tableViewerList.getSelection();
				if(!iss.isEmpty()) {
					
					ScheduleMainDAO dao = (ScheduleMainDAO)iss.getFirstElement();
					
					if(!MessageDialog.openQuestion(null, Messages.get().Confirm, Messages.get().ScheduleEditor_4)) return;
					try {
						UserDBDAO userDB = TadpoleSystem_UserDBQuery.getUserDBInstance(dao.getDb_seq());
						ScheduleManager.getInstance().deleteJob(userDB, dao);
						
						TadpoleSystem_Schedule.deleteSchedule(dao.getSeq());
						
						refreshSchedule();
					} catch (Exception e1) {
						logger.error("delete schedule", e1); //$NON-NLS-1$
					}
				}
			}
		});
		tltmDelete.setText(Messages.get().ScheduleEditor_6);
		
		tableViewerList = new TableViewer(compositeList, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewerList.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection iss = (IStructuredSelection)event.getSelection();
				if(!iss.isEmpty()) {
					tltmDelete.setEnabled(true);
					tltmModify.setEnabled(true);
					
					ScheduleMainDAO dao = (ScheduleMainDAO)iss.getFirstElement();
					try {
						List<ScheduleResultDAO> listResult = TadpoleSystem_Schedule.getScheduleResult(dao.getSeq());
						tvResult.setInput(listResult);
					} catch (Exception e) {
						logger.error("get schedule result", e); //$NON-NLS-1$
					}
				}
			}
		});
		Table tableList = tableViewerList.getTable();
		tableList.setLinesVisible(true);
		tableList.setHeaderVisible(true);
		tableList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewerList, SWT.NONE);
		TableColumn tblclmnName = tableViewerColumn.getColumn();
		tblclmnName.setWidth(100);
		tblclmnName.setText(Messages.get().ScheduleEditor_8);
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewerList, SWT.NONE);
		TableColumn tblclmnDescription = tableViewerColumn_2.getColumn();
		tblclmnDescription.setWidth(100);
		tblclmnDescription.setText(Messages.get().ScheduleEditor_9);
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewerList, SWT.NONE);
		TableColumn tblclmnCreateDate = tableViewerColumn_1.getColumn();
		tblclmnCreateDate.setWidth(200);
		tblclmnCreateDate.setText(Messages.get().Description);
		
		Group compositeResult = new Group(sashForm, SWT.NONE);
		compositeResult.setLayout(new GridLayout(1, false));
		compositeResult.setText("Schedule Result");
		
		tvResult = new TableViewer(compositeResult, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tvResult.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tvResult, SWT.NONE);
		TableColumn tblclmnResult = tableViewerColumn_3.getColumn();
		tblclmnResult.setWidth(52);
		tblclmnResult.setText(Messages.get().ScheduleEditor_11);
		
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tvResult, SWT.NONE);
		TableColumn tblclmnMessage = tableViewerColumn_4.getColumn();
		tblclmnMessage.setWidth(240);
		tblclmnMessage.setText(Messages.get().ScheduleEditor_12);
		
		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(tvResult, SWT.NONE);
		TableColumn tblclmnDate = tableViewerColumn_5.getColumn();
		tblclmnDate.setWidth(140);
		tblclmnDate.setText(Messages.get().ScheduleEditor_13);
		
		tableViewerList.setContentProvider(ArrayContentProvider.getInstance());
		tableViewerList.setLabelProvider(new ScheduleLabelProvider());
		
		tvResult.setContentProvider(ArrayContentProvider.getInstance());
		tvResult.setLabelProvider(new ResultLabelProvider());
		
		initUI();

		sashForm.setWeights(new int[] {4, 6});
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
		
	}
	
	private void initUI() {
		refreshSchedule();
	}
	
	private void refreshSchedule() {
		try {
			listScheduleMain = TadpoleSystem_Schedule.findUserScheduleMain();
			tableViewerList.setInput(listScheduleMain);
			
			List<ScheduleResultDAO> listResult = new ArrayList<ScheduleResultDAO>();
			tvResult.setInput(listResult);
		} catch (Exception e) {
			logger.error("find schedule main", e); //$NON-NLS-1$
		}
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

		ScheduleEditorInput esqli = (ScheduleEditorInput) input;
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
	
	@Override
	public void setFocus() {
	}
}

/**
 * schedule label provider
 * @author hangum
 *
 */
class ScheduleLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		ScheduleMainDAO dao = (ScheduleMainDAO)element;
		
		switch(columnIndex) {
		case 0: return dao.getTitle();
		case 1: return dao.getCron_exp();
		case 2: return dao.getDescription();
		}
		return null;
	}
	
}

class ResultLabelProvider  extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		ScheduleResultDAO dao = (ScheduleResultDAO)element;
		
		switch(columnIndex) {
		case 0: return dao.getResult();
		case 1: return dao.getDescription();
		case 2: 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
			return sdf.format(dao.getCreate_time().getTime());
		}
		return null;
	}
	
}