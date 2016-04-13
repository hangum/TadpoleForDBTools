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
package com.hangum.tadpole.rdb.core.dialog.export.sqlresult;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.AExportComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.ExportHTMLComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.ExportJSONComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.ExportSQLComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.ExportTextComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.composite.ExportXMLComposite;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.dao.ExportTextDAO;

/**
 * Resultset to download
 * 
 * @author hangum
 *
 */
public class ResultSetDownloadDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(ResultSetDownloadDialog.class);
	
	private String strDefTableName;
	private QueryExecuteResultDTO queryExecuteResultDTO;
	
	private CTabFolder tabFolder;
	private AExportComposite compositeText;
	private AExportComposite compositeHTML;
	private AExportComposite compositeJSON;
	private AExportComposite compositeXML;
	private AExportComposite compositeSQL;
	
	// preview 
	private Text textPreview;
	
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param queryExecuteResultDTO 
	 * @param strDefTableName 
	 */
	public ResultSetDownloadDialog(Shell parentShell, String strDefTableName, QueryExecuteResultDTO queryExecuteResultDTO) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.strDefTableName = strDefTableName;
		this.queryExecuteResultDTO = queryExecuteResultDTO;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		newShell.setText("Export Dialog");
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
		
		SashForm sashForm = new SashForm(container, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tabFolder = new CTabFolder(sashForm, SWT.NONE);
		tabFolder.setBorderVisible(false);
		tabFolder.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());
		
		compositeText = new ExportTextComposite(tabFolder, SWT.NONE);
		compositeText.setLayout(new GridLayout(1, false));
		
		//--------------------------------------
		compositeHTML = new ExportHTMLComposite(tabFolder, SWT.NONE);
		compositeText.setLayout(new GridLayout(1, false));
		
		compositeJSON = new ExportJSONComposite(tabFolder, SWT.NONE);
		compositeText.setLayout(new GridLayout(1, false));
		
		compositeXML = new ExportXMLComposite(tabFolder, SWT.NONE);
		compositeText.setLayout(new GridLayout(1, false));
		
		compositeSQL = new ExportSQLComposite(tabFolder, SWT.NONE);
		compositeText.setLayout(new GridLayout(1, false));
		//--[tail]----------------------------------------------------------------------------------------
		Group groupPreview = new Group(sashForm, SWT.NONE);
		groupPreview.setText("Preview");
		groupPreview.setLayout(new GridLayout(1, false));
		
		textPreview = new Text(groupPreview, SWT.BORDER | SWT.MULTI);
		textPreview.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		//--[start]----------------------------------------------------------------------------------------
		sashForm.setWeights(new int[] {7,3});
		tabFolder.setSelection(0);
		//--[end]----------------------------------------------------------------------------------------
		
		return container;
	}
	
	@Override
	protected void okPressed() {
		String selectionTab = ""+tabFolder.getSelection().getData();
		if(logger.isDebugEnabled()) logger.debug("selection tab is " + selectionTab);
		
		if("text".equalsIgnoreCase(selectionTab)) {
			if(logger.isDebugEnabled()) logger.debug("text");
			
			if(compositeText.isValidate()) {
				ExportTextDAO dao = (ExportTextDAO)compositeText.getLastData();
			}
			
		}
		
		
//		super.okPressed();
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
		return new Point(500, 600);
	}
}
