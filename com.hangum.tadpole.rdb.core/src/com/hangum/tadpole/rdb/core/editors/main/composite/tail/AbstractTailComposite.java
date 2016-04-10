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
package com.hangum.tadpole.rdb.core.editors.main.composite.tail;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.commons.utils.zip.util.ZipUtils;
import com.hangum.tadpole.engine.sql.util.export.CSVExpoter;
import com.hangum.tadpole.engine.sql.util.export.HTMLExporter;
import com.hangum.tadpole.engine.sql.util.export.JsonExpoter;
import com.hangum.tadpole.engine.sql.util.export.SQLExporter;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.mongodb.core.dialogs.msg.TadpoleSQLDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.export.sqltoapplication.MakeResultToUpdateStatementDialog;
import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;

/**
 * abstract tail composite
 * 
 * @author hangum
 *
 */
public abstract class AbstractTailComposite extends Composite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(AbstractTailComposite.class);
	
	private Composite compositeParent;
	protected Composite compositeDownloadAMsg;
	protected Combo comboDownload;
	protected DownloadServiceHandler downloadServiceHandler;
	
	protected Label lblQueryResultStatus;
	protected Button btnPin;
	protected Button btnViewQuery;
	
	public AbstractTailComposite(Composite compositeBtn, int style) {
		super(compositeBtn, style);
		setLayout(new GridLayout(1, false));
		
		compositeParent = compositeBtn;
		compositeDownloadAMsg = new Composite(this, SWT.NONE);
		GridLayout gl_compositeDownloadAMsg = new GridLayout(7, false);
		gl_compositeDownloadAMsg.verticalSpacing = 2;
		gl_compositeDownloadAMsg.horizontalSpacing = 2;
		gl_compositeDownloadAMsg.marginHeight = 0;
		gl_compositeDownloadAMsg.marginWidth = 2;
		compositeDownloadAMsg.setLayout(gl_compositeDownloadAMsg);
		compositeDownloadAMsg.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		btnPin = new Button(compositeDownloadAMsg, SWT.TOGGLE);
		btnPin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strPin = btnPin.getToolTipText();
				if("Pin".equals(strPin)) {
					btnPin.setToolTipText("Unpin");
					btnPin.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
					layout();
				} else {
					Composite parentComposite = compositeParent.getParent().getParent();
					Composite resultMainComposite = parentComposite.getParent();
					parentComposite.dispose();
					
					resultMainComposite.layout();
				}
			}
		});
		btnPin.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/pin.png"));
		btnPin.setToolTipText("Pin");
		
		btnViewQuery = new Button(compositeDownloadAMsg, SWT.NONE);
		btnViewQuery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TadpoleSQLDialog dialog = new TadpoleSQLDialog(getShell(), Messages.get().ViewQuery, getSQL());
				dialog.open();
			}
		});
		btnViewQuery.setText(Messages.get().ViewQuery);

		comboDownload = new Combo(compositeDownloadAMsg, SWT.NONE | SWT.READ_ONLY);
		comboDownload.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false, 1, 1));
		comboDownload.add("CSV Comma(Add Header)"); //$NON-NLS-1$
		comboDownload.add("CSV Comma"); //$NON-NLS-1$
		
		comboDownload.add("CSV Tab(Add Header)"); //$NON-NLS-1$
		comboDownload.add("CSV Tab"); //$NON-NLS-1$
		
		comboDownload.add("HTML"); //$NON-NLS-1$
		comboDownload.add("JSON"); //$NON-NLS-1$
		comboDownload.add("INSERT statement"); //$NON-NLS-1$
		comboDownload.add("UPDATE statement"); //$NON-NLS-1$
		comboDownload.setVisibleItemCount(8);
		comboDownload.select(0);
		
		Button btnSQLResultDownload = new Button(compositeDownloadAMsg, SWT.NONE);
		btnSQLResultDownload.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false, 1, 1));
		btnSQLResultDownload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(getRSDao().getDataList() == null) return;
				
				if("CSV Comma(Add Header)".equals(comboDownload.getText())) { //$NON-NLS-1$
					exportResultCSVType(true, ',');
				} else if("CSV Comma".equals(comboDownload.getText())) { //$NON-NLS-1$
					exportResultCSVType(false, ',');
				} else if("CSV Tab(Add Header)".equals(comboDownload.getText())) { //$NON-NLS-1$
					exportResultCSVType(true, '\t');
				} else if("CSV Tab".equals(comboDownload.getText())) { //$NON-NLS-1$
					exportResultCSVType(false, '\t');
				} else if("HTML".equals(comboDownload.getText())) { //$NON-NLS-1$
					exportResultHTMLType();
				} else if("JSON".equals(comboDownload.getText())) { //$NON-NLS-1$
					exportResultJSONType();
				} else if("INSERT statement".equals(comboDownload.getText())) { //$NON-NLS-1$
					exportInsertStatement();
				} else if("UPDATE statement".equals(comboDownload.getText())) { //$NON-NLS-1$
					exportUpdateStatement();
				}
			}
			
		});
		btnSQLResultDownload.setText(Messages.get().Download);
		
		Label label = new Label(compositeDownloadAMsg, SWT.NONE);
		label.setText("");
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblQueryResultStatus = new Label(compositeDownloadAMsg, SWT.NONE);
		lblQueryResultStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblQueryResultStatus.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));

		registerServiceHandler();
	}
	
	public void execute(String strResultMsg) {
		this.layout();
		lblQueryResultStatus.setText(strResultMsg);
		lblQueryResultStatus.pack();
	}
	
	/**
	 * btn pin selection
	 * @return
	 */
	public boolean getBtnPinSelection() {
		if(btnPin.isDisposed()) return true;
		return btnPin.getSelection();
	}
	
	public abstract String getSQL();
	
	/**
	 * get query result dto
	 * @return
	 */
	public abstract QueryExecuteResultDTO getRSDao();
	
	/**
	 * export update statement
	 */
	protected void exportUpdateStatement() {
		MakeResultToUpdateStatementDialog dialog = new MakeResultToUpdateStatementDialog(null, findTableName(), getRSDao());
		if (dialog.open() == Window.OK) {
			try {
				downloadFile(dialog.getStrTableName(), SQLExporter.makeFileUpdateStatment(dialog.getStrTableName(), getRSDao(), dialog.getListWhereColumnName()));
			} catch(Exception ee) {
				logger.error("HTML type export error", ee); //$NON-NLS-1$
			}	
		}
	}
	
	/**
	 * export insert into statement
	 */
	protected void exportInsertStatement() {
		InputDialog inputDialog = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages.get().TableName, //$NON-NLS-1$
				Messages.get().PleaseTableName, findTableName(), new TableNameValidator());
		if (inputDialog.open() == Window.OK) {
			try {
				downloadFile(inputDialog.getValue(), SQLExporter.makeFileInsertStatment(inputDialog.getValue(), getRSDao()));
			} catch(Exception ee) {
				logger.error("HTML type export error", ee); //$NON-NLS-1$
			}	
		}
	}
	
	/**
	 * Export resultset csvMessages.get().ResultSetComposite_14
	 * @param seprator 
	 */
	protected void exportResultCSVType(boolean isAddHead, char seprator) {
		try {
			downloadFile(CSVExpoter.makeCSVFile(isAddHead, findTableName(), getRSDao(), seprator));
		} catch(Exception ee) {
			logger.error("HTML type export error", ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * export result of html
	 */
	protected void exportResultHTMLType() {
		try {
			downloadFile(HTMLExporter.makeContentFile(findTableName(), getRSDao()));
		} catch(Exception ee) {
			logger.error("HTML type export error", ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * export result of html
	 */
	protected void exportResultJSONType() {
		try {
			downloadFile(JsonExpoter.makeContentFile(findTableName(), getRSDao()));
		} catch(Exception ee) {
			logger.error("JSON type export error", ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * find table name
	 * @return
	 */
	protected String findTableName() {
		String strTableName = "TempTable"; //$NON-NLS-1$
		if(!getRSDao().getColumnTableName().isEmpty()) strTableName = getRSDao().getColumnTableName().get(1);
		if(strTableName == null | "".equals(strTableName)) strTableName = "TempTable"; //$NON-NLS-1$ //$NON-NLS-2$
		
		return strTableName;
	}

	/**
	 * download file
	 * @param strFileLocation
	 * @throws Exception
	 */
	protected void downloadFile(String fileName, String strFileLocation) throws Exception {
		String strZipFile = ZipUtils.pack(strFileLocation);
		byte[] bytesZip = FileUtils.readFileToByteArray(new File(strZipFile));
		
		_downloadExtFile(fileName +".zip", bytesZip); //$NON-NLS-1$
	}
	
	/**
	 * download file
	 * @param strFileLocation
	 * @throws Exception
	 */
	protected void downloadFile(String strFileLocation) throws Exception {
		downloadFile(findTableName(), strFileLocation); //$NON-NLS-1$
	}
	
	/** registery service handler */
	protected void registerServiceHandler() {
		downloadServiceHandler = new DownloadServiceHandler();
		RWT.getServiceManager().registerServiceHandler(downloadServiceHandler.getId(), downloadServiceHandler);
	}
	
	/** download service handler call */
	protected void unregisterServiceHandler() {
		RWT.getServiceManager().unregisterServiceHandler(downloadServiceHandler.getId());
		downloadServiceHandler = null;
	}
	
	/**
	 * download external file
	 * 
	 * @param fileName
	 * @param newContents
	 */
	protected void _downloadExtFile(String fileName, byte[] newContents) {
		downloadServiceHandler.setName(fileName);
		downloadServiceHandler.setByteContent(newContents);
		
		DownloadUtils.provideDownload(getShell(), downloadServiceHandler.getId());
	}
	
	@Override
	public void dispose() {
		unregisterServiceHandler();
		super.dispose();
	}

	@Override
	protected void checkSubclass() {
	}
}

/**
 * table name validattor
 */
class TableNameValidator implements IInputValidator {
	
	public TableNameValidator() {
	}

	public String isValid(String newText) {
		if(StringUtils.isEmpty(newText)) {
			return Messages.get().InputTableName;
		}	
	    return null;
	}
}
