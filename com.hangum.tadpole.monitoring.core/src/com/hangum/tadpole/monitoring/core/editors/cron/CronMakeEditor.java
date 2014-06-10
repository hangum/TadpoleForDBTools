package com.hangum.tadpole.monitoring.core.editors.cron;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;

/**
 * Cron make 
 * 
 * @author hangum
 *
 */
public class CronMakeEditor extends EditorPart {
	private Text textSQL;
	private Text textCronExpress;
	private Text textExpress;
	private Text textCronName;
	private Text textEmail;
	private Table table;

	public CronMakeEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		
		Group grpJobList = new Group(sashForm, SWT.NONE);
		grpJobList.setText("Job List");
		grpJobList.setLayout(new GridLayout(1, false));
		
		TableViewer tableViewer = new TableViewer(grpJobList, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpGenerateCronExpress = new Group(sashForm, SWT.NONE);
		grpGenerateCronExpress.setText("Generate cron express");
		grpGenerateCronExpress.setLayout(new GridLayout(1, false));
		
		SashForm sashFormGenerateCron = new SashForm(grpGenerateCronExpress, SWT.VERTICAL);
		sashFormGenerateCron.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeGenCronHead = new Composite(sashFormGenerateCron, SWT.NONE);
		compositeGenCronHead.setLayout(new GridLayout(3, false));
		
		Label lblCronExpress = new Label(compositeGenCronHead, SWT.NONE);
		lblCronExpress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCronExpress.setText("Cron Express");
		
		textCronExpress = new Text(compositeGenCronHead, SWT.BORDER);
		textCronExpress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnGenerateExpress = new Button(compositeGenCronHead, SWT.NONE);
		btnGenerateExpress.setText("Generate express");
		
		Composite compositeGenCronBody = new Composite(sashFormGenerateCron, SWT.NONE);
		compositeGenCronBody.setLayout(new GridLayout(1, false));
		
		textExpress = new Text(compositeGenCronBody, SWT.BORDER);
		textExpress.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sashFormGenerateCron.setWeights(new int[] {2, 8});
		
		Group grpSql = new Group(sashForm, SWT.NONE);
		grpSql.setText("SQL");
		grpSql.setLayout(new GridLayout(2, false));
		
		Label lblCronName = new Label(grpSql, SWT.NONE);
		lblCronName.setText("Cron Name");
		
		textCronName = new Text(grpSql, SWT.BORDER);
		textCronName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDatabase = new Label(grpSql, SWT.NONE);
		lblDatabase.setText("DataBase");
		
		Combo comboDBName = new Combo(grpSql, SWT.NONE);
		comboDBName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSql = new Label(grpSql, SWT.NONE);
		lblSql.setText("SQL");
		new Label(grpSql, SWT.NONE);
		
		textSQL = new Text(grpSql, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		textSQL.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		Composite composite = new Composite(grpSql, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		composite.setLayout(new GridLayout(3, false));
		
		Button btnSendMail = new Button(composite, SWT.CHECK);
		btnSendMail.setText("Send Mail");
		
		textEmail = new Text(composite, SWT.BORDER);
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnMakeJob = new Button(composite, SWT.NONE);
		btnMakeJob.setText("Make Job");

		sashForm.setWeights(new int[] {3, 3, 4});

	}

	@Override
	public void setFocus() {

	}
}
