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
import com.hangum.tadpole.engine.sql.util.export.CSVExpoter;
import com.hangum.tadpole.engine.sql.util.export.HTMLExporter;
import com.hangum.tadpole.engine.sql.util.export.JsonExpoter;
import com.hangum.tadpole.engine.sql.util.export.SQLExporter;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.mongodb.core.dialogs.msg.TadpoleSQLDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.swtdesigner.ResourceManager;
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
	
	private AbstractResultDetailComposite abstractResultComp;
	
	private Composite compositeDownloadAMsg;
	private Combo comboDownload;
	private DownloadServiceHandler downloadServiceHandler;
	
	private Label lblQueryResultStatus;
	
	protected QueryExecuteResultDTO rsDAO;
	private Button btnPin;
	private Button btnViewQuery;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param compositeBtn 
	 * @param style
	 */
	public ResultTailComposite(Composite reqAbstractResult, Composite compositeBtn, int style) {
		super(compositeBtn, style);
		setLayout(new GridLayout(1, false));
		
		abstractResultComp = (AbstractResultDetailComposite)reqAbstractResult;

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
				if("Pin".equals(strPin)) btnPin.setToolTipText("Unpin");
				else btnPin.setToolTipText("Pin");
				
				layout();
			}
		});
		btnPin.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/pin.png"));
		btnPin.setToolTipText("Pin");
		
		btnViewQuery = new Button(compositeDownloadAMsg, SWT.NONE);
		btnViewQuery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TadpoleSQLDialog dialog = new TadpoleSQLDialog(getShell(), "View query", abstractResultComp.getReqQuery().getSql());
				dialog.open();
			}
		});
		btnViewQuery.setText("View query");

		comboDownload = new Combo(compositeDownloadAMsg, SWT.NONE | SWT.READ_ONLY);
		comboDownload.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false, 1, 1));
		comboDownload.add("CSV Comma"); //$NON-NLS-1$
		comboDownload.add("CSV Tab"); //$NON-NLS-1$
		comboDownload.add("HTML"); //$NON-NLS-1$
		comboDownload.add("JSON"); //$NON-NLS-1$
		comboDownload.add("INSERT statement"); //$NON-NLS-1$
//		comboDownload.add("UPDATE statement"); //$NON-NLS-1$
		comboDownload.select(0);
		
		Button btnSQLResultDownload = new Button(compositeDownloadAMsg, SWT.NONE);
		btnSQLResultDownload.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false, 1, 1));
		btnSQLResultDownload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(MessageDialog.openConfirm(getShell(), Messages.get().ResultSetComposite_4, Messages.get().ResultSetComposite_5)) {
					if("CSV Comma".equals(comboDownload.getText())) { //$NON-NLS-1$
						exportResultCSVType(',');
					} else if("CSV Tab".equals(comboDownload.getText())) { //$NON-NLS-1$
						exportResultCSVType('\t');
					} else if("HTML".equals(comboDownload.getText())) { //$NON-NLS-1$
						exportResultHTMLType();
					} else if("JSON".equals(comboDownload.getText())) { //$NON-NLS-1$
						exportResultJSONType();
					} else if("INSERT statement".equals(comboDownload.getText())) { //$NON-NLS-1$
						exportInsertStatement();
//					} else if("UPDATE statement".equals(comboDownload.getText())) { //$NON-NLS-1$
//						exportUpdateStatement();
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
	private void exportInsertStatement() {
		if(rsDAO == null) return;
	
		try {
			downloadFile(SQLExporter.makeFileInsertStatment(findTableName(), rsDAO));
		} catch(Exception ee) {
			logger.error("HTML type export error", ee); //$NON-NLS-1$
		}
	}
	
//	/**
//	 * export update into statement
//	 */
//	private void exportUpdateStatement() {
//		if(rsDAO == null) return;
//		
//		try {
//			downloadFile(SQLExporter.makeFileUpdateStatment(findTableName(), rsDAO));
//		} catch(Exception ee) {
//			logger.error("HTML type export error", ee); //$NON-NLS-1$
//		}
//	}
	
	/**
	 * Export resultset csvMessages.get().ResultSetComposite_14
	 * @param seprator 
	 */
	private void exportResultCSVType(char seprator) {
		if(rsDAO == null) return;
		
		try {
			downloadFile(CSVExpoter.makeCSVFile(findTableName(), rsDAO, seprator));
		} catch(Exception ee) {
			logger.error("HTML type export error", ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * export result of html
	 */
	private void exportResultHTMLType() {
		if(rsDAO == null) return;
		
		try {
			downloadFile(HTMLExporter.makeContentFile(findTableName(), rsDAO));
		} catch(Exception ee) {
			logger.error("HTML type export error", ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * export result of html
	 */
	private void exportResultJSONType() {
		if(rsDAO == null) return;
		
		try {
			downloadFile(JsonExpoter.makeContentFile(findTableName(), rsDAO));
		} catch(Exception ee) {
			logger.error("JSON type export error", ee); //$NON-NLS-1$
		}
	}
	
	/**
	 * find table name
	 * @return
	 */
	private String findTableName() {
		String strTableName = "TempTable"; //$NON-NLS-1$
		if(!rsDAO.getColumnTableName().isEmpty()) strTableName = rsDAO.getColumnTableName().get(1);
		if(strTableName == null | "".equals(strTableName)) strTableName = "TempTable"; //$NON-NLS-1$ //$NON-NLS-2$
		
		return strTableName;
	}

	/**
	 * download file
	 * @param strFileLocation
	 * @throws Exception
	 */
	private void downloadFile(String strFileLocation) throws Exception {
		String strZipFile = ZipUtils.pack(strFileLocation);
		byte[] bytesZip = FileUtils.readFileToByteArray(new File(strZipFile));
		
		downloadExtFile(findTableName() +".zip", bytesZip); //$NON-NLS-1$
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
	
	/**
	 * btn pin selection
	 * @return
	 */
	public boolean getBtnPinSelection() {
		return btnPin.getSelection();
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
