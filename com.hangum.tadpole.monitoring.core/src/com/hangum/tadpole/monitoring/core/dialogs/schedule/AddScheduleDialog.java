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
package com.hangum.tadpole.monitoring.core.dialogs.schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.quartz.CronExpression;

import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.ScheduleDAO;
import com.hangum.tadpole.engine.query.dao.system.ScheduleMainDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_Schedule;
import com.hangum.tadpole.monitoring.core.Messages;
import com.hangum.tadpole.monitoring.core.manager.schedule.ScheduleManager;

/**
 * Add Schedule dialogs
 * 
 * @author hangum
 *
 */
public class AddScheduleDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(AddScheduleDialog.class);
	
	private List<ScheduleDAO> listSchedule = new ArrayList<ScheduleDAO>();
	private UserDBDAO userDB;
	private Text textTitle;
	private Text textDescription;
	private Text textCronExp;
	private TableViewer tableViewer;
	private Text textViewSchedule;
	
	private ToolItem tltmModify;
	private ToolItem tltmDelete;
	
	private ScheduleMainDAO scheduleDao;

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	public AddScheduleDialog(Shell parentShell, UserDBDAO userDB) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);

		this.userDB = userDB;
	}
	
	/**
	 * modify data dialog
	 * 
	 * @param parentShell
	 * @param userDB
	 * @param dao
	 */
	public AddScheduleDialog(Shell parentShell, UserDBDAO userDB, ScheduleMainDAO dao) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);

		this.userDB = userDB;
		this.scheduleDao = dao;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Add Schedule"); //$NON-NLS-1$
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(final Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		
		Composite compositeHead = new Composite(container, SWT.BORDER);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(3, false));
		
		Label lblTitle = new Label(compositeHead, SWT.NONE);
		lblTitle.setText(Messages.get().Title);
		
		textTitle = new Text(compositeHead, SWT.BORDER);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeHead, SWT.NONE);
		
		Label lblDescription = new Label(compositeHead, SWT.NONE);
		lblDescription.setText(Messages.get().Description);
		
		textDescription = new Text(compositeHead, SWT.BORDER);
		textDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeHead, SWT.NONE);
		
		Label lblCronExpression = new Label(compositeHead, SWT.NONE);
		lblCronExpression.setText("<a href='http://www.cronmaker.com/' target='_blank'>Cron Expression</a>"); //$NON-NLS-1$
		lblCronExpression.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);

		
		textCronExp = new Text(compositeHead, SWT.BORDER);
		textCronExp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnViewSchedule = new Button(compositeHead, SWT.NONE);
		btnViewSchedule.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showExp();
			}
		});
		btnViewSchedule.setText(Messages.get().AddScheduleDialog_3);
		new Label(compositeHead, SWT.NONE);
		
		textViewSchedule = new Text(compositeHead, SWT.BORDER | SWT.MULTI);
		GridData gd_textViewSchedule = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_textViewSchedule.heightHint = 60;
		textViewSchedule.setLayoutData(gd_textViewSchedule);
		
		Composite compositeBody = new Composite(container, SWT.BORDER);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(compositeBody, SWT.FLAT | SWT.RIGHT);
		
		final ToolItem tltmAdd = new ToolItem(toolBar, SWT.NONE);
		tltmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AddSQLDialog dialog = new AddSQLDialog(parent.getShell(), listSchedule.size());
				if(Dialog.OK == dialog.open()) {
					listSchedule.add(dialog.getDao());
					
					tableViewer.refresh();
				}
			}
		});
		tltmAdd.setText(Messages.get().AddScheduleDialog_4);
		
		tltmModify = new ToolItem(toolBar, SWT.NONE);
		tltmModify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection iss = (IStructuredSelection)tableViewer.getSelection();
				if(!iss.isEmpty()) {
					ScheduleDAO dao = (ScheduleDAO)iss.getFirstElement();
					AddSQLDialog dialog = new AddSQLDialog(null, dao);
					if(Dialog.OK == dialog.open()) {
						tableViewer.refresh(dialog.getDao());						
					}
				}
			}
		});
		tltmModify.setEnabled(false);
		tltmModify.setText(Messages.get().AddScheduleDialog_5);
		
		tltmDelete = new ToolItem(toolBar, SWT.NONE);
		tltmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection iss = (IStructuredSelection)tableViewer.getSelection();
				if(!iss.isEmpty()) {
					if(!MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().AddScheduleDialog_7)) return;
		
					ScheduleDAO dao = (ScheduleDAO)iss.getFirstElement();
					listSchedule.remove(dao);
					tableViewer.refresh();
				}
			}
		});
		tltmDelete.setEnabled(false);
		tltmDelete.setText(Messages.get().AddScheduleDialog_8);
		
		tableViewer = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				tltmModify.setEnabled(true);
				tltmDelete.setEnabled(true);
			}
		});
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn.setEditingSupport(new SQLOrderEditingSupport(tableViewer));
		TableColumn tblclmnSeq = tableViewerColumn.getColumn();
		tblclmnSeq.setWidth(50);
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnTitle = tableViewerColumn_1.getColumn();
		tblclmnTitle.setWidth(100);
		tblclmnTitle.setText(Messages.get().AddScheduleDialog_9);
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnSql = tableViewerColumn_2.getColumn();
		tblclmnSql.setWidth(273);
		tblclmnSql.setText(Messages.get().SQL);
		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new AddScheduleLableProvider());
		tableViewer.setInput(listSchedule);
		
		initData();

		return container;
	}
	
	/**
	 * initialize data
	 */
	private void initData() {
		// 데이터가 수정이면. 
		if(scheduleDao != null) {
			this.textTitle.setText( scheduleDao.getTitle() );
			this.textDescription.setText( scheduleDao.getDescription() );
			this.textCronExp.setText(  scheduleDao.getCron_exp() );
			
			try {
				listSchedule = TadpoleSystem_Schedule.findSchedule(scheduleDao.getSeq());
				tableViewer.setInput(listSchedule);
			} catch (Exception e) {
				logger.error("get Schedule data", e);
			}
		}
		
		// google analytic
		AnalyticCaller.track(this.getClass().getName());
	}
	
	/**
	 * show cron expression
	 */
	private void showExp() {
		StringBuffer sbStr = new StringBuffer();
		try {
			CronExpression exp = new CronExpression(textCronExp.getText());
			java.util.Date showDate = new java.util.Date();
//			sbStr.append(showDate.toString() + PublicTadpoleDefine.LINE_SEPARATOR);
	        
	        for (int i=0; i<=5; i++) {
	          showDate = exp.getNextValidTimeAfter(showDate);
	          sbStr.append(convPretty(showDate) + PublicTadpoleDefine.LINE_SEPARATOR);
	          showDate = new java.util.Date(showDate.getTime() + 1000);
	        }
	        
	        textViewSchedule.setText(sbStr.toString());
		} catch (ParseException e) {
			MessageDialog.openError(null, Messages.get().Confirm, Messages.get().AddScheduleDialog_12);
			textCronExp.setFocus();
		}
	}
	
	private String convPretty(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
		return sdf.format(date);
	}
	
	@Override
	protected void okPressed() {
		String txtTitle = StringUtils.trim(textTitle.getText());
		String txtDescription = StringUtils.trim(textDescription.getText());
		String txtCronExp = StringUtils.trim(textCronExp.getText());
		
		if(StringUtils.isEmpty(txtTitle)) {
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().AddScheduleDialog_15);
			textTitle.setFocus();
			return;
		}
		
		if(!CronExpression.isValidExpression(txtCronExp)) {
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().AddScheduleDialog_17);
			textCronExp.setFocus();
			return;
		}
		
		if(listSchedule.size() == 0) {
			MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().AddScheduleDialog_19);
			return;
		}

		// 데이터 저장.
		if(scheduleDao == null) {
			try {
				if(!MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().AddScheduleDialog_21)) return;
				ScheduleMainDAO dao = TadpoleSystem_Schedule.addSchedule(userDB, txtTitle, txtDescription, txtCronExp, listSchedule);
				
				// cron manager 등록.
				Date nextJob = ScheduleManager.getInstance().newJob(userDB, dao);
				
				MessageDialog.openInformation(null, Messages.get().Confirm, Messages.get().AddScheduleDialog_23 + convPretty(nextJob));
				
			} catch (Exception e) {
				logger.error("save schedule", e); //$NON-NLS-1$
				MessageDialog.openError(null, Messages.get().Error, e.getMessage());
				return;
			}
		// 데이터 수정.
		} else {
			try {
				if(!MessageDialog.openConfirm(null, Messages.get().Confirm, "데이터를 수정하시겠습니까?")) return;
				
				// remove job
				ScheduleManager.getInstance().deleteJob(userDB, scheduleDao);
				
				scheduleDao.setTitle(txtTitle);
				scheduleDao.setDescription(txtDescription);
				scheduleDao.setCron_exp(txtCronExp);
				
				TadpoleSystem_Schedule.modifySchedule(userDB, scheduleDao, listSchedule);
				
				// cron manager 등록.
				Date nextJob = ScheduleManager.getInstance().newJob(userDB, scheduleDao);
				
				MessageDialog.openInformation(null, Messages.get().Confirm, Messages.get().AddScheduleDialog_23 + convPretty(nextJob));
				
			} catch (Exception e) {
				logger.error("save schedule", e); //$NON-NLS-1$
				MessageDialog.openError(null, Messages.get().Error, e.getMessage());
				return;
			}
		}

		super.okPressed();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().Confirm, false);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Cancel, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(518, 518);
	}
}


/**
 * schedule label provider
 * @author hangum
 *
 */
class AddScheduleLableProvider extends LabelProvider implements ITableLabelProvider {
	@Override
	public String getColumnText(Object element, int columnIndex) {
		ScheduleDAO dao = (ScheduleDAO)element;
		
		switch(columnIndex) {
		case 0: return ""+dao.getSend_seq(); //$NON-NLS-1$
		case 1: return dao.getName();
		case 2: return dao.getSql();
		}
		
		return "*** not set column ***"; //$NON-NLS-1$
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
	
}

/**
 * sql order editing support
 * 
 * @author hangum
 *
 */
class SQLOrderEditingSupport extends EditingSupport {
	private final TableViewer viewer;
	private final CellEditor editor;
	
	public SQLOrderEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
		this.editor = new TextCellEditor(viewer.getTable());
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		ScheduleDAO dao = (ScheduleDAO)element;
		return String.valueOf(dao.getSend_seq());
	}

	@Override
	protected void setValue(Object element, Object value) {
		ScheduleDAO dao = (ScheduleDAO)element;
		
		if(!NumberUtils.isNumber(value.toString())) {
			MessageDialog.openWarning(null, Messages.get().Warning, "Is not number value.");
			return;
		}
		
		
		dao.setSend_seq(NumberUtils.toInt(value.toString()));
		viewer.update(dao, null);
	}
	
}