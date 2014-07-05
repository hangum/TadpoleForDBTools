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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
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

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.sql.dao.system.ScheduleDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.query.TadpoleSystem_Schedule;

/**
 * Add Schedule dialogs
 * 
 * @author hangum
 *
 */
public class AddScheduleDialog extends Dialog {
	private List<ScheduleDAO> listSchedule = new ArrayList<ScheduleDAO>();
	private UserDBDAO userDB;
	private Text textTitle;
	private Text textDescription;
	private Text textCronExp;
	private TableViewer tableViewer;
	private Text textViewSchedule;
	
	private ToolItem tltmModify;
	private ToolItem tltmDelete;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AddScheduleDialog(Shell parentShell, UserDBDAO userDB) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);

		this.userDB = userDB;
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
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		gridLayout.marginHeight = 5;
		gridLayout.marginWidth = 5;
		
		Composite compositeHead = new Composite(container, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(3, false));
		
		Label lblTitle = new Label(compositeHead, SWT.NONE);
		lblTitle.setText("Title");
		
		textTitle = new Text(compositeHead, SWT.BORDER);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeHead, SWT.NONE);
		
		Label lblDescription = new Label(compositeHead, SWT.NONE);
		lblDescription.setText("Description");
		
		textDescription = new Text(compositeHead, SWT.BORDER);
		textDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeHead, SWT.NONE);
		
		Label lblCronExpression = new Label(compositeHead, SWT.NONE);
		lblCronExpression.setText("<a href='http://www.cronmaker.com/' target='_blank'>Cron Expression</a>");
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
		btnViewSchedule.setText("View Schedule");
		new Label(compositeHead, SWT.NONE);
		
		textViewSchedule = new Text(compositeHead, SWT.BORDER | SWT.MULTI);
		GridData gd_textViewSchedule = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_textViewSchedule.heightHint = 60;
		textViewSchedule.setLayoutData(gd_textViewSchedule);
		
		Composite compositeBody = new Composite(container, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(compositeBody, SWT.FLAT | SWT.RIGHT);
		
		ToolItem tltmAdd = new ToolItem(toolBar, SWT.NONE);
		tltmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AddSQLDialog dialog = new AddSQLDialog(null, listSchedule.size());
				if(Dialog.OK == dialog.open()) {
					listSchedule.add(dialog.getDao());
					
					tableViewer.refresh();
				}
			}
		});
		tltmAdd.setText("Add");
		
		tltmModify = new ToolItem(toolBar, SWT.NONE);
		tltmModify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection iss = (IStructuredSelection)tableViewer.getSelection();
				if(!iss.isEmpty()) {
					ScheduleDAO dao = (ScheduleDAO)iss.getFirstElement();
					AddSQLDialog dialog = new AddSQLDialog(null, dao);
					if(Dialog.OK == dialog.open()) {
						tableViewer.refresh();						
					}
				}
			}
		});
		tltmModify.setEnabled(false);
		tltmModify.setText("Modify");
		
		tltmDelete = new ToolItem(toolBar, SWT.NONE);
		tltmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection iss = (IStructuredSelection)tableViewer.getSelection();
				if(!iss.isEmpty()) {
					if(MessageDialog.openConfirm(null, "Confirm", "Are you delete?")) return;
		
					ScheduleDAO dao = (ScheduleDAO)iss.getFirstElement();
					listSchedule.remove(dao);
					tableViewer.refresh();
				}
			}
		});
		tltmDelete.setEnabled(false);
		tltmDelete.setText("Delete");
		
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
		TableColumn tblclmnSeq = tableViewerColumn.getColumn();
		tblclmnSeq.setWidth(50);
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnTitle = tableViewerColumn_1.getColumn();
		tblclmnTitle.setWidth(100);
		tblclmnTitle.setText("Title");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnSql = tableViewerColumn_2.getColumn();
		tblclmnSql.setWidth(273);
		tblclmnSql.setText("SQL");
		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new AddScheduleLableProvider());
		tableViewer.setInput(listSchedule);

		return container;
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
			MessageDialog.openError(null, "Confirm", "Check Cron Expression.");
			textCronExp.setFocus();
		}
	}
	
	private String convPretty(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	@Override
	protected void okPressed() {
		String txtTitle = StringUtils.trim(textTitle.getText());
		String txtDescription = StringUtils.trim(textDescription.getText());
		String txtCronExp = StringUtils.trim(textCronExp.getText());
		
		if(StringUtils.isEmpty(txtTitle)) {
			MessageDialog.openError(null, "Error", "Title is empty. Check user text.");
			textTitle.setFocus();
			return;
		}
		
		if(!CronExpression.isValidExpression(txtCronExp)) {
			MessageDialog.openError(null, "Error", "Cron Expression is not valid. Check your a text.");
			textCronExp.setFocus();
			return;
		}
		
		if(listSchedule.size() == 0) {
			MessageDialog.openError(null, "Error", "Please add SQL.");
			return;
		}
		
		TadpoleSystem_Schedule.addSchedule(userDB, txtTitle, txtDescription, txtCronExp, listSchedule);

		super.okPressed();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", false);
		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(518, 518);
	}
}


class AddScheduleLableProvider extends LabelProvider implements ITableLabelProvider {
	@Override
	public String getColumnText(Object element, int columnIndex) {
		ScheduleDAO dao = (ScheduleDAO)element;
		
		switch(columnIndex) {
		case 0: return ""+dao.getSend_seq();
		case 1: return dao.getName();
		case 2: return dao.getSql();
		}
		
		return "*** not set column ***";
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
	
}