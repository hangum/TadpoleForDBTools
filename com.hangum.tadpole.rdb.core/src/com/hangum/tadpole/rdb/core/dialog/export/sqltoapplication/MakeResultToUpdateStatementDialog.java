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
package com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.StringHelper;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.rdb.core.Messages;
import org.eclipse.swt.widgets.Button;

/**
 * SQL 결과를 update statement로 만들어 주는 dialog
 * 
 * @author hangum
 *
 */
public class MakeResultToUpdateStatementDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(MakeResultToUpdateStatementDialog.class);
	private QueryExecuteResultDTO rsDAO;
	private Text textTableName;
	private Button[] btnUpdate;
	
	private String strTableName;
	private List<String> listWhereColumnName = new ArrayList<>();

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param strTableName 
	 * @param queryExecuteResultDTO 
	 */
	public MakeResultToUpdateStatementDialog(Shell parentShell, String strTableName, QueryExecuteResultDTO rsDAO) {
		super(parentShell);
		this.strTableName = strTableName;
		this.rsDAO = rsDAO;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		super.setShellStyle(SWT.SHELL_TRIM | SWT.BORDER | SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		newShell.setText(Messages.get().GenerateUpdateStatement);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
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
		
		Composite compositeTitle = new Composite(container, SWT.NONE);
		compositeTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeTitle.setLayout(new GridLayout(2, false));
		
		Label lblTableName = new Label(compositeTitle, SWT.NONE);
		lblTableName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTableName.setText(Messages.get().TableName);
		
		textTableName = new Text(compositeTitle, SWT.BORDER);
		textTableName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textTableName.setText(strTableName);
		
		Group grpColumninfo = new Group(container, SWT.NONE);
		grpColumninfo.setLayout(new GridLayout(3, false));
		grpColumninfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpColumninfo.setText(Messages.get().SelectWhereColumn);
		
		Map<Integer, String> mapColumnName = rsDAO.getColumnLabelName();
		btnUpdate = new Button[mapColumnName.size()-1];
		for(int i=1; i<mapColumnName.size(); i++) {
			btnUpdate[i-1] = new Button(grpColumninfo, SWT.CHECK);
			btnUpdate[i-1].setText(mapColumnName.get(i));
		}

		return container;
	}
	
	@Override
	protected void okPressed() {
		listWhereColumnName.clear();
		
		strTableName = StringUtils.trimToEmpty(textTableName.getText());
		for (int i=0; i<btnUpdate.length; i++) {
			Button button = btnUpdate[i];
			if(button.getSelection()) listWhereColumnName.add(button.getText());
		}
		
		if(StringUtils.isEmpty(strTableName)) {
			MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().InputTableName);
			return;
		} else if(listWhereColumnName.isEmpty()) {
			MessageDialog.openWarning(getShell(), Messages.get().Warning, Messages.get().PleaseSelectWhereColumn);
			return;
		}
		
		super.okPressed();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.get().OK, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().CANCEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 448);
	}
	
	public String getStrTableName() {
		return strTableName;
	}
	public List<String> getListWhereColumnName() {
		return listWhereColumnName;
	}

}
