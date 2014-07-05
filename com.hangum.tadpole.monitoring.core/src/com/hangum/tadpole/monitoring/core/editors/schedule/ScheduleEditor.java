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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Text;

/**
 * Tadpole Monitoring editor
 * 
 * @author hangum
 *
 */
public class ScheduleEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(ScheduleEditor.class);
	public static final String ID = "com.hangum.tadpole.monitoring.core.editor.schedule";
	private Table tableList;
	private Text textTitle;
	private Text textDescription;
	private Text textCronExp;
	private Table tableSQL;
	
	public ScheduleEditor() {
	}

	@Override
	public void createPartControl(Composite parent) {
		
		SashForm sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setOrientation(SWT.HORIZONTAL);
		
		Composite compositeList = new Composite(sashForm, SWT.NONE);
		compositeList.setLayout(new GridLayout(1, false));
		
		TableViewer tableViewerList = new TableViewer(compositeList, SWT.BORDER | SWT.FULL_SELECTION);
		tableList = tableViewerList.getTable();
		tableList.setLinesVisible(true);
		tableList.setHeaderVisible(true);
		tableList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewerList, SWT.NONE);
		TableColumn tblclmnName = tableViewerColumn.getColumn();
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewerList, SWT.NONE);
		TableColumn tblclmnDescription = tableViewerColumn_2.getColumn();
		tblclmnDescription.setWidth(100);
		tblclmnDescription.setText("Description");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewerList, SWT.NONE);
		TableColumn tblclmnCreateDate = tableViewerColumn_1.getColumn();
		tblclmnCreateDate.setWidth(100);
		tblclmnCreateDate.setText("Last Execute Time");
		
		Composite compositeListBtn = new Composite(compositeList, SWT.NONE);
		compositeListBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeListBtn.setLayout(new GridLayout(4, false));
		
		Button btnNew = new Button(compositeListBtn, SWT.NONE);
		btnNew.setText("New");
		
		Button btnDelete = new Button(compositeListBtn, SWT.NONE);
		btnDelete.setText("Delete");
		
		Button btnResult = new Button(compositeListBtn, SWT.NONE);
		btnResult.setText("Result");
		
		Button btnTemplate = new Button(compositeListBtn, SWT.NONE);
		btnTemplate.setText("Template");
		
		Composite compositeContent = new Composite(sashForm, SWT.NONE);
		compositeContent.setLayout(new GridLayout(1, false));
		
		Composite compositeCntHead = new Composite(compositeContent, SWT.NONE);
		compositeCntHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeCntHead.setLayout(new GridLayout(3, false));
		
		Label lblTitle = new Label(compositeCntHead, SWT.NONE);
		lblTitle.setText("Title");
		
		textTitle = new Text(compositeCntHead, SWT.BORDER);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeCntHead, SWT.NONE);
		
		Label lblDescription = new Label(compositeCntHead, SWT.NONE);
		lblDescription.setText("Description");
		
		textDescription = new Text(compositeCntHead, SWT.BORDER);
		textDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(compositeCntHead, SWT.NONE);
		
		Label lblCronExpression = new Label(compositeCntHead, SWT.NONE);
		lblCronExpression.setText("Cron Expression");
		
		textCronExp = new Text(compositeCntHead, SWT.BORDER);
		textCronExp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnShowSchedule = new Button(compositeCntHead, SWT.NONE);
		btnShowSchedule.setText("Schedule");
		
		TableViewer tableViewerSQL = new TableViewer(compositeContent, SWT.BORDER | SWT.FULL_SELECTION);
		tableSQL = tableViewerSQL.getTable();
		tableSQL.setLinesVisible(true);
		tableSQL.setHeaderVisible(true);
		tableSQL.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewerSQL, SWT.NONE);
		TableColumn tblclmnTitle = tableViewerColumn_3.getColumn();
		tblclmnTitle.setWidth(100);
		tblclmnTitle.setText("Title");
		
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewerSQL, SWT.NONE);
		TableColumn tblclmnSql = tableViewerColumn_4.getColumn();
		tblclmnSql.setWidth(330);
		tblclmnSql.setText("SQL");
		
		Composite compositeContentTail = new Composite(compositeContent, SWT.NONE);
		compositeContentTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeContentTail.setLayout(new GridLayout(4, false));
		
		Button btnAddSql = new Button(compositeContentTail, SWT.NONE);
		btnAddSql.setText("Add SQL");
		
		Button btnPreview = new Button(compositeContentTail, SWT.NONE);
		btnPreview.setText("Preview");
		
		Label lblNewLabel = new Label(compositeContentTail, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSave = new Button(compositeContentTail, SWT.NONE);
		btnSave.setText("Save");

		sashForm.setWeights(new int[] {4, 6});
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
