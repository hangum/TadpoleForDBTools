/*******************************************************************************
 * Copyright (c) 2012 - 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.application.start.api;

import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.StartupParameters;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.Text;

import com.google.gson.JsonArray;
import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpold.commons.libs.core.define.SystemDefine;
import com.hangum.tadpole.commons.dialogs.message.dao.SQLHistoryDAO;
import com.hangum.tadpole.commons.util.JSONUtil;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_ExecutedSQL;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.engine.sql.util.QueryUtils;

/**
 * API service dialog
 *
 *  test url	http://127.0.0.1:10081/tadpole?serviceID=10d3625a-eee1-409b-8a5f-16bb2a5f68d2
 *  			http://127.0.0.1:10081/tadpole?serviceID=10d3625a-eee1-409b-8a5f-16bb2a5f68d2&1=1&2=SQL
 *  
 *  			http://127.0.0.1:10081/tadpole?serviceID=c7f17960-cf68-46ac-9089-e429e711be39
 *
 * @author hangum
 * @version 1.6.1
 * @since 2015. 5. 19.
 *
 */
public class APIServiceDialog extends Dialog {
	private static final Logger logger = Logger.getLogger(APIServiceDialog.class);
	
	/** DOWNLOAD BUTTON ID */
	private int DOWNLOAD_BTN_ID = IDialogConstants.CLIENT_ID + 1;
	
	private StartupParameters serviceParameter;
	
	private Text textAPIName;
	private Text textArgument;
	private Text textResult;
	
	private Combo comboResultType;
	
	private Button btnAddHeader;
	private Text textDelimiter;
	
	/** download servcie handler. */
	private DownloadServiceHandler downloadServiceHandler;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public APIServiceDialog(Shell parentShell, StartupParameters serviceParameter) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE);
		
		this.serviceParameter = serviceParameter;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(SystemDefine.NAME + " API Dialog"); //$NON-NLS-1$
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Composite compositeTitle = new Composite(container, SWT.NONE);
		compositeTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeTitle.setLayout(new GridLayout(2, false));
		
		Label lblApiName = new Label(compositeTitle, SWT.NONE);
		lblApiName.setText("API NAME");
		
		textAPIName = new Text(compositeTitle, SWT.BORDER);
		textAPIName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblArgument = new Label(compositeTitle, SWT.NONE);
		lblArgument.setText("Argument");
		
		textArgument = new Text(compositeTitle, SWT.BORDER);
		textArgument.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblType = new Label(compositeTitle, SWT.NONE);
		lblType.setText("Result Type");
		
		comboResultType = new Combo(compositeTitle, SWT.READ_ONLY);
		comboResultType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isEnable = false;
				if(QueryUtils.RESULT_TYPE.CSV.name().equals(comboResultType.getText())) {
					isEnable = true;
				}
				
				btnAddHeader.setEnabled(isEnable);
				textDelimiter.setEnabled(isEnable);
			}
		});
		comboResultType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for (QueryUtils.RESULT_TYPE resultType : QueryUtils.RESULT_TYPE.values()) {
			comboResultType.add(resultType.name());
		}
		comboResultType.select(1);
		new Label(compositeTitle, SWT.NONE);
		
		Composite compositeDetailCSV = new Composite(compositeTitle, SWT.NONE);
		compositeDetailCSV.setLayout(new GridLayout(3, false));
		compositeDetailCSV.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnAddHeader = new Button(compositeDetailCSV, SWT.CHECK);
		btnAddHeader.setText("Add Header");
		
		Label lblDelimiter = new Label(compositeDetailCSV, SWT.NONE);
		lblDelimiter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDelimiter.setText("Delimiter");
		
		textDelimiter = new Text(compositeDetailCSV, SWT.BORDER);
		textDelimiter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpResultSet = new Group(container, SWT.NONE);
		grpResultSet.setLayout(new GridLayout(1, false));
		grpResultSet.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpResultSet.setText("Result Set");
		
		textResult = new Text(grpResultSet, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.CANCEL | SWT.MULTI);
		textResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		initUI();
		initData();

		registerServiceHandler();
		
		return container;
	}
	
	private void initUI() {
		Collection<String> collString = serviceParameter.getParameterNames();
		String strAPIKey = serviceParameter.getParameter(PublicTadpoleDefine.SERVICE_KEY_NAME);
		
		String strArgument = "";
		for (String strArgKey : collString) {
			if(!strArgKey.equals(PublicTadpoleDefine.SERVICE_KEY_NAME)) {
				strArgument += String.format("%s=%s&", strArgKey, serviceParameter.getParameter(strArgKey));
			}
		}
		
		textAPIName.setText(strAPIKey);
		textArgument.setText(strArgument);
		btnAddHeader.setSelection(true);
		textDelimiter.setText(",");
	}
	
	/**
	 * initialize ui
	 */
	private void initData() {
		
		Timestamp timstampStart = new Timestamp(System.currentTimeMillis());
		UserDBDAO userDB = null;
		
		try {
			UserDBResourceDAO userDBResourceDao = TadpoleSystem_UserDBResource.findAPIKey(textAPIName.getText());
			if(userDBResourceDao == null) {
				MessageDialog.openInformation(getShell(), "Confirm", "Not found apikey. please check out.");
			} else {
				
				String strSQL = TadpoleSystem_UserDBResource.getResourceData(userDBResourceDao);
				if(logger.isDebugEnabled()) logger.debug(userDBResourceDao.getName() + ", " + strSQL);
				
				// find db
				userDB = TadpoleSystem_UserDBQuery.getUserDBInstance(userDBResourceDao.getDb_seq());
				List<Object> listParam = makeListParameter();
				
				String strResultType = getSelect(userDB, strSQL, listParam);
				textResult.setText(strResultType);
				
				// save called history
				saveHistoryData(userDB, timstampStart, textAPIName.getText(), textArgument.getText(), PublicTadpoleDefine.SUCCESS_FAIL.S.name(), "");
				
			}
			
		} catch (Exception e) {
			logger.error("find api", e);
			
			saveHistoryData(userDB, timstampStart, textAPIName.getText(), textArgument.getText(), PublicTadpoleDefine.SUCCESS_FAIL.F.name(), e.getMessage());
			
			MessageDialog.openError(getShell(), "Error", "Rise exception. please check you argument and others.\n");
		}
	}
	
	/**
	 * save api history
	 * 
	 * @param userDB
	 * @param timstampStart
	 * @param strApiname
	 * @param strApiArgument
	 * @param strResult
	 * @param strErrorMsg
	 */
	private void saveHistoryData(final UserDBDAO userDB, Timestamp timstampStart, String strApiname, String strApiArgument, String strResult, String strErrorMsg) {
		SQLHistoryDAO sqlHistoryDAO = new SQLHistoryDAO();
		sqlHistoryDAO.setDbSeq(userDB.getSeq());
		sqlHistoryDAO.setStartDateExecute(timstampStart);
		sqlHistoryDAO.setEndDateExecute(new Timestamp(System.currentTimeMillis()));
		sqlHistoryDAO.setResult(strResult);
		sqlHistoryDAO.setMesssage(strErrorMsg);
		sqlHistoryDAO.setStrSQLText(strApiname + "&" + strApiArgument);
		
		sqlHistoryDAO.setIpAddress(RWT.getRequest().getRemoteAddr());
		
		try {
			TadpoleSystem_ExecutedSQL.saveExecuteSQUeryResource(-1, 
					userDB, 
					PublicTadpoleDefine.EXECUTE_SQL_TYPE.API, 
					sqlHistoryDAO);
		} catch(Exception e) {
			logger.error("save history", e);
		}
	}
	
	/**
	 * called sql
	 * 
	 * @param userDB
	 * @param strSQL
	 * @param listParam
	 * @return
	 * @throws Exception
	 */
	private String getSelect(final UserDBDAO userDB, String strSQL, List<Object> listParam) throws Exception {
		String strResult = "";
		if(QueryUtils.RESULT_TYPE.JSON.name().equals(comboResultType.getText())) {
			JsonArray jsonArry = QueryUtils.selectToJson(userDB, strSQL, listParam);
			strResult = JSONUtil.getPretty(jsonArry.toString());
		} else if(QueryUtils.RESULT_TYPE.CSV.name().equals(comboResultType.getText())) {
			strResult = QueryUtils.selectToCSV(userDB, strSQL, listParam, btnAddHeader.getSelection(), textDelimiter.getText());
		} else {
			strResult = QueryUtils.selectToXML(userDB, strSQL, listParam);
		}
		
		return strResult;
	}
	
	/**
	 * make parameter list
	 * 
	 * @return
	 * @throws Exception
	 */
	private List<Object> makeListParameter() throws Exception {
		List<Object> listParam = new ArrayList<Object>();
		String strArgement = textArgument.getText();
		
		// TODO check this code
		List<NameValuePair> params = URLEncodedUtils.parse(new URI("http://dumy.com?" + strArgement), "UTF-8");
		Map<String, String> mapParam = new HashMap<String, String>();
		for (NameValuePair nameValuePair : params) {
			mapParam.put(nameValuePair.getName(), nameValuePair.getValue());
		}

		// assume this count... no way i'll argument is over 100..... --;;
		for(int i=1; i<100; i++) {
			if(mapParam.containsKey(String.valueOf(i))) {
				listParam.add(mapParam.get(""+i));
			} else {
				break;
			}
		}

		return listParam;
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
	public void downloadExtFile(String fileName, String newContents) {
		downloadServiceHandler.setName(fileName);
		downloadServiceHandler.setByteContent(newContents.getBytes());
		
		DownloadUtils.provideDownload(textDelimiter.getParent(), downloadServiceHandler.getId());
	}
	
	/** registery service handler */
	private void registerServiceHandler() {
		downloadServiceHandler = new DownloadServiceHandler();
		RWT.getServiceManager().registerServiceHandler(downloadServiceHandler.getId(), downloadServiceHandler);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		// download 
		if(buttonId == DOWNLOAD_BTN_ID) {
			String strResult = textResult.getText();
			downloadExtFile("TadpoleAPIServer.txt", strResult);
		} else {
			super.buttonPressed(buttonId);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		initData();
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, DOWNLOAD_BTN_ID, "Download", false);
		createButton(parent, IDialogConstants.OK_ID, "RUN", true);
	}
	
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(650, 500);
	}
}
