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
package com.hangum.tadpole.rdb.core.dialog.dbconnect.dialog;

import java.util.ArrayList;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.commons.libs.core.utils.ValidChecker;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.query.dao.system.ExternalBrowserInfoDAO;
import com.hangum.tadpole.rdb.core.Messages;

/**
 * 브라우저 URL을 기록합니다.
 * 
 * https://github.com/hangum/TadpoleForDBTools/issues/356
 * @author hangum
 *
 */
public class ExtensionBrowserURLDialog extends Dialog {
	/**
	 * list default external dao
	 */
	private List<ExternalBrowserInfoDAO> listExterBroswer = new ArrayList<ExternalBrowserInfoDAO>();

	private Button btnEnable;
	private TableViewer tableViewer;
	
	private Combo comboUsed;
	private Text textName;
	private Text textURL;
	private Text textComment;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ExtensionBrowserURLDialog(Shell parentShell, List<ExternalBrowserInfoDAO> listExterBroswer) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.listExterBroswer = listExterBroswer;
	}
	
	@Override
	public void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().ExtensionBrowserURLDialog_0);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		btnEnable = new Button(container, SWT.CHECK);
		btnEnable.setSelection(true);
		btnEnable.setText(Messages.get().IsEnable);
		
		Group grpList = new Group(container, SWT.NONE);
		grpList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpList.setText(Messages.get().List);
		grpList.setLayout(new GridLayout(1, false));
		
		tableViewer = new TableViewer(grpList, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection iss = (IStructuredSelection)event.getSelection();
				Object selectObj = iss.getFirstElement();
				if(null != selectObj) {
					selectData((ExternalBrowserInfoDAO)selectObj);
				}
			}
		});
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnUsed = new TableColumn(table, SWT.NONE);
		tblclmnUsed.setWidth(70);
		tblclmnUsed.setText(Messages.get().IsUse);
		
		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(100);
		tblclmnName.setText(CommonMessages.get().Name);
		
		TableColumn tblclmnUrl = new TableColumn(table, SWT.NONE);
		tblclmnUrl.setWidth(200);
		tblclmnUrl.setText(Messages.get().URL);
		
		TableColumn tblclmnComment = new TableColumn(table, SWT.NONE);
		tblclmnComment.setWidth(100);
		tblclmnComment.setText(CommonMessages.get().Description);
		
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new ExtensionBrowserLableProvider());
		tableViewer.setInput(listExterBroswer);
		
		Group grpAdd = new Group(grpList, SWT.NONE);
		grpAdd.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		grpAdd.setText(CommonMessages.get().Add);
		grpAdd.setLayout(new GridLayout(2, false));
		
		Label lblUse = new Label(grpAdd, SWT.NONE);
		lblUse.setText(Messages.get().IsUse);
		
		comboUsed = new Combo(grpAdd, SWT.READ_ONLY);
		comboUsed.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboUsed.add("YES"); //$NON-NLS-1$
		comboUsed.add("NO"); //$NON-NLS-1$
		comboUsed.select(0);
		
		Label lblName = new Label(grpAdd, SWT.NONE);
		lblName.setText(CommonMessages.get().Name);
		
		textName = new Text(grpAdd, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblUrl = new Label(grpAdd, SWT.NONE);
		lblUrl.setText(Messages.get().URL);
		
		textURL = new Text(grpAdd, SWT.BORDER);
		textURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDescription = new Label(grpAdd, SWT.NONE);
		lblDescription.setText(CommonMessages.get().Description);
		
		textComment = new Text(grpAdd, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_textDescription.heightHint = 50;
		gd_textDescription.minimumHeight = 50;
		textComment.setLayoutData(gd_textDescription);
		new Label(grpAdd, SWT.NONE);
		
		Composite composite = new Composite(grpAdd, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		Button btnAdd = new Button(composite, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addExtensionBrowserData();
			}
		});
		btnAdd.setText(CommonMessages.get().Add);
		
		Button btnDelete = new Button(composite, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteExtensionBrowserData();
			}
		});
		btnDelete.setText(CommonMessages.get().Delete);

		return container;
	}
	
	/**
	 * table data를 삭제합니다.
	 */
	private void deleteExtensionBrowserData() {
		String strName = StringUtils.trimToEmpty(textName.getText());
		String strUrl = StringUtils.trimToEmpty(textURL.getText());
		
		// url데이터가 이미 존재하는지 검사합니다.
		List<ExternalBrowserInfoDAO> listCheckExterBroswer = (List)tableViewer.getInput();
		for (ExternalBrowserInfoDAO externalBrowserInfoDAO : listCheckExterBroswer) {
			if(strName.equals(externalBrowserInfoDAO.getName()) && strUrl.equals(externalBrowserInfoDAO.getUrl())) {

				listExterBroswer.remove(externalBrowserInfoDAO);
				tableViewer.refresh();
				return;
			}
		}
	}
	
	/**
	 * browser data를 추가합니다.
	 */
	private void addExtensionBrowserData() {
		String strName = StringUtils.trimToEmpty(textName.getText());
		String strUrl = StringUtils.trimToEmpty(textURL.getText());
		String strCmt = StringUtils.trimToEmpty(textComment.getText());
		
		if(!ValidChecker.checkTextCtl(textName, CommonMessages.get().Name)) return;
		if(!ValidChecker.checkTextCtl(textURL, Messages.get().URL)) return;
		
		// url데이터가 이미 존재하는지 검사합니다.
		List<ExternalBrowserInfoDAO> listCheckExterBroswer = (List)tableViewer.getInput();
		for (ExternalBrowserInfoDAO externalBrowserInfoDAO : listCheckExterBroswer) {
			if(strUrl.equals(externalBrowserInfoDAO.getUrl())) {
				MessageDialog.openWarning(null, CommonMessages.get().Warning, CommonMessages.get().Name); //$NON-NLS-1$
				return;
			}
		}
		
		// 신규데이터를 만든다.
		ExternalBrowserInfoDAO newDao = new ExternalBrowserInfoDAO();
		newDao.setIs_used(comboUsed.getText());
		newDao.setName(strName);
		newDao.setUrl(strUrl);
		newDao.setComment(strCmt);
		
		listExterBroswer.add(newDao);
		tableViewer.refresh();
	}
	
	/**
	 * select Obj
	 * @param externalDAo
	 */
	private void selectData(ExternalBrowserInfoDAO externalDAo) {
		comboUsed.setText(externalDAo.getIs_used());
		textName.setText(externalDAo.getName());
		textURL.setText(externalDAo.getUrl());
		textComment.setText(externalDAo.getComment());
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, CommonMessages.get().Save, true);
		createButton(parent, IDialogConstants.CANCEL_ID,  CommonMessages.get().Cancel, false);
	}
	
	@Override
	protected void okPressed() {
		isEnable = btnEnable.getSelection();
		
		super.okPressed();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 564);
	}
	
	private boolean isEnable = false;
	public boolean isEnable() {
		return isEnable;
	}
	
	public List<ExternalBrowserInfoDAO> getListExterBroswer() {
		return listExterBroswer;
	}

}

/**
 * extension browser URL label provider
 * 
 * @author hangum
 *
 */
class ExtensionBrowserLableProvider  extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		ExternalBrowserInfoDAO dao = (ExternalBrowserInfoDAO)element;
		
		switch(columnIndex) {
		case 0: return dao.getIs_used();
		case 1: return dao.getName();
		case 2: return dao.getUrl();
		case 3: return dao.getComment();
		}
		
		return "*** not set column ***"; //$NON-NLS-1$
	}
	
}