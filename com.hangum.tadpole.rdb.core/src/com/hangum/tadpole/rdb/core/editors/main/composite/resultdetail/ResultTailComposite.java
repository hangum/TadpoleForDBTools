/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main.composite.resultdetail;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
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

import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.commons.utils.zip.util.ZipUtils;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.export.CSVExpoterUtil;
import com.hangum.tadpole.engine.sql.util.export.HTMLExporter;
import com.hangum.tadpole.engine.sql.util.export.JsonExpoter;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.rdb.core.Messages;
import com.swtdesigner.SWTResourceManager;

/**
 * 결과 화면의 다운로드 부분과 결과 상태를  컴포짖
 * 
 * @author hangum
 *
 */
public class ResultTailComposite extends Composite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(ResultTailComposite.class);
	
	private Composite compositeDownloadAMsg;
	private Combo comboDownload;
	private DownloadServiceHandler downloadServiceHandler;
	
	private Label lblQueryResultStatus;
	
	protected QueryExecuteResultDTO rsDAO;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ResultTailComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		compositeDownloadAMsg = new Composite(this, SWT.NONE);
		GridLayout gl_compositeDownloadAMsg = new GridLayout(4, false);
		gl_compositeDownloadAMsg.verticalSpacing = 2;
		gl_compositeDownloadAMsg.horizontalSpacing = 2;
		gl_compositeDownloadAMsg.marginHeight = 0;
		gl_compositeDownloadAMsg.marginWidth = 2;
		compositeDownloadAMsg.setLayout(gl_compositeDownloadAMsg);
		compositeDownloadAMsg.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		comboDownload = new Combo(compositeDownloadAMsg, SWT.NONE | SWT.READ_ONLY);
		comboDownload.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false, 1, 1));
		comboDownload.add("CSV"); //$NON-NLS-1$
		comboDownload.add("HTML"); //$NON-NLS-1$
		comboDownload.add("JSON"); //$NON-NLS-1$
		comboDownload.add("INSERT INTO statement"); //$NON-NLS-1$
		comboDownload.select(0);
		
		Button btnSQLResultDownload = new Button(compositeDownloadAMsg, SWT.NONE);
		btnSQLResultDownload.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false, 1, 1));
		btnSQLResultDownload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(MessageDialog.openConfirm(getShell(), Messages.get().ResultSetComposite_4, Messages.get().ResultSetComposite_5)) {
					if("CSV".equals(comboDownload.getText())) { //$NON-NLS-1$
						exportResultCSVType();	
					} else if("HTML".equals(comboDownload.getText())) { //$NON-NLS-1$
						exportResultHTMLType();
					} else if("JSON".equals(comboDownload.getText())) { //$NON-NLS-1$
						exportResultJSONType();
					} else if("INSERT INTO statement".equals(comboDownload.getText())) { //$NON-NLS-1$
						exportInsertIntoStatement();
					}
				}
			}
			
		});
		btnSQLResultDownload.setText(Messages.get().ResultSetComposite_11);
		
		Label label = new Label(compositeDownloadAMsg, SWT.NONE);
		label.setText("");
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblQueryResultStatus = new Label(compositeDownloadAMsg, SWT.NONE);
		lblQueryResultStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblQueryResultStatus.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));

		registerServiceHandler();
	}
	
	public void execute(String strResultMsg, QueryExecuteResultDTO rsDAO) {
		this.rsDAO = rsDAO;
		
		this.layout();
		lblQueryResultStatus.setText(strResultMsg);
		lblQueryResultStatus.pack();
	}
	
	/**
	 * export insert into statement
	 */
	private void exportInsertIntoStatement() {
		if(rsDAO == null) return;
		
		try {
			String strTableName = "TempTable"; //$NON-NLS-1$
			if(!rsDAO.getColumnTableName().isEmpty()) strTableName = rsDAO.getColumnTableName().get(1);
			if(strTableName == null | "".equals(strTableName)) strTableName = "TempTable"; //$NON-NLS-1$ //$NON-NLS-2$
			
			String strPath = SQLUtil.makeFileInsertStatment(strTableName, rsDAO);
			String strZipFile = ZipUtils.pack(strPath);
			byte[] bytesZip = FileUtils.readFileToByteArray(new File(strZipFile));
			
			downloadExtFile(strTableName+"_SQL.zip", bytesZip); //$NON-NLS-1$
		} catch (Exception e) {
			logger.error("make insertinto", e); //$NON-NLS-1$
		}
	}
	
	/**
	 * Export resultset csvMessages.get().ResultSetComposite_14
	 * 
	 */
	private void exportResultCSVType() {
		try {
			String strTableName = "TempTable"; //$NON-NLS-1$
			if(!rsDAO.getColumnTableName().isEmpty()) strTableName = rsDAO.getColumnTableName().get(1);
			if(strTableName == null | "".equals(strTableName)) strTableName = "TempTable"; //$NON-NLS-1$ //$NON-NLS-2$
			
			String strPath = CSVExpoterUtil.makeCSVFile(strTableName, rsDAO);
			String strZipFile = ZipUtils.pack(strPath);
			byte[] bytesZip = FileUtils.readFileToByteArray(new File(strZipFile));
			
			downloadExtFile(strTableName +"_CSV.zip", bytesZip); //$NON-NLS-1$
		} catch(Exception ee) {
			logger.error("csv type export error", ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * export result of html
	 */
	private void exportResultHTMLType() {
		try {
			String strTableName = "TempTable"; //$NON-NLS-1$
			if(!rsDAO.getColumnTableName().isEmpty()) strTableName = rsDAO.getColumnTableName().get(1);
			if(strTableName == null | "".equals(strTableName)) strTableName = "TempTable"; //$NON-NLS-1$ //$NON-NLS-2$
			
			String strPath = HTMLExporter.makeContentFile(strTableName, rsDAO);
			String strZipFile = ZipUtils.pack(strPath);
			byte[] bytesZip = FileUtils.readFileToByteArray(new File(strZipFile));
			
			downloadExtFile(strTableName +"_HTML.zip", bytesZip); //$NON-NLS-1$
		} catch(Exception ee) {
			logger.error("csv type export error", ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * export result of html
	 */
	private void exportResultJSONType() {
		try {
			String strTableName = "TempTable"; //$NON-NLS-1$
			if(!rsDAO.getColumnTableName().isEmpty()) strTableName = rsDAO.getColumnTableName().get(1);
			if(strTableName == null | "".equals(strTableName)) strTableName = "TempTable"; //$NON-NLS-1$ //$NON-NLS-2$
			
			String strPath = JsonExpoter.makeContentFile(strTableName, rsDAO);
			String strZipFile = ZipUtils.pack(strPath);
			byte[] bytesZip = FileUtils.readFileToByteArray(new File(strZipFile));
			
			downloadExtFile(strTableName +"_HTML.zip", bytesZip); //$NON-NLS-1$
		} catch(Exception ee) {
			logger.error("csv type export error", ee); //$NON-NLS-1$
		}
	}
	
	/** registery service handler */
	private void registerServiceHandler() {
		downloadServiceHandler = new DownloadServiceHandler();
		RWT.getServiceManager().registerServiceHandler(downloadServiceHandler.getId(), downloadServiceHandler);
	}
	
	/** download service handler call */
	private void unregisterServiceHandler() {
		RWT.getServiceManager().unregisterServiceHandler(downloadServiceHandler.getId());
		downloadServiceHandler = null;
	}
	
	/**
	 * download external file
	 * 
	 * @param fileName
	 * @param newContents
	 */
	private void downloadExtFile(String fileName, byte[] newContents) {
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
