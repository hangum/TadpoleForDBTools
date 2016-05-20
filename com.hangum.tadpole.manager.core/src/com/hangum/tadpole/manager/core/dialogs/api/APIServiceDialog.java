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
package com.hangum.tadpole.manager.core.dialogs.api;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
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
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.commons.util.JSONUtil;
import com.hangum.tadpole.commons.util.download.DownloadServiceHandler;
import com.hangum.tadpole.commons.util.download.DownloadUtils;
import com.hangum.tadpole.engine.query.dao.ResourceManagerDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.restful.RESTfulAPIUtils;
import com.hangum.tadpole.engine.sql.paremeter.NamedParameterDAO;
import com.hangum.tadpole.engine.sql.paremeter.NamedParameterUtil;
import com.hangum.tadpole.engine.sql.util.QueryUtils;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.manager.core.Messages;

/**
 * Test API service dialog
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
	
	private UserDBDAO userDB;
	private String strSQL = ""; //$NON-NLS-1$
	private ResourceManagerDAO resourceManagerDao;
	
	private Text textAPIName;
	private Text textArgument;
	private Text textResult;
	
	private Combo comboResultType;
	
	private Button btnAddHeader;
	private Text textDelimiter;
	
	/** download servcie handler. */
	private DownloadServiceHandler downloadServiceHandler;
	private Text textApiURL;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public APIServiceDialog(Shell parentShell, UserDBDAO userDB, String strSQL, ResourceManagerDAO resourceManagerDao) {
		super(parentShell);
		setShellStyle(SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		
		this.userDB = userDB;
		this.strSQL = strSQL;
		this.resourceManagerDao = resourceManagerDao;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.get().APIServiceDialog_7);
		newShell.setImage(GlobalImageUtils.getTadpoleIcon());
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
		lblApiName.setText(Messages.get().APIName);
		
		textAPIName = new Text(compositeTitle, SWT.BORDER);
		textAPIName.setEditable(false);
		textAPIName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblApiUrl = new Label(compositeTitle, SWT.NONE);
		lblApiUrl.setText(Messages.get().APIURL);
		
		textApiURL = new Text(compositeTitle, SWT.BORDER);
		textApiURL.setEditable(false);
		textApiURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblArgument = new Label(compositeTitle, SWT.NONE);
		lblArgument.setText(Messages.get().Argument);
		
		textArgument = new Text(compositeTitle, SWT.BORDER);
		textArgument.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initData(textArgument.getText());
			}
		});
		textArgument.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblType = new Label(compositeTitle, SWT.NONE);
		lblType.setText(Messages.get().ResultType);
		
		comboResultType = new Combo(compositeTitle, SWT.READ_ONLY);
		comboResultType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initResultType();
			}
		});
		comboResultType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for (QueryUtils.RESULT_TYPE resultType : QueryUtils.RESULT_TYPE.values()) {
			comboResultType.add(resultType.name());
		}
		comboResultType.select(0);
		new Label(compositeTitle, SWT.NONE);
		
		Composite compositeDetailCSV = new Composite(compositeTitle, SWT.NONE);
		compositeDetailCSV.setLayout(new GridLayout(3, false));
		compositeDetailCSV.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnAddHeader = new Button(compositeDetailCSV, SWT.CHECK);
		btnAddHeader.setText(Messages.get().AddHeader);
		
		Label lblDelimiter = new Label(compositeDetailCSV, SWT.NONE);
		lblDelimiter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDelimiter.setText(Messages.get().APIServiceDialog_5);
		
		textDelimiter = new Text(compositeDetailCSV, SWT.BORDER);
		textDelimiter.setEditable(false);
		textDelimiter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpResultSet = new Group(container, SWT.NONE);
		grpResultSet.setLayout(new GridLayout(1, false));
		grpResultSet.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpResultSet.setText(Messages.get().Result);
		
		textResult = new Text(grpResultSet, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.CANCEL | SWT.MULTI);
		textResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		initUI();

		registerServiceHandler();
		
		AnalyticCaller.track("APIServiceDialog"); //$NON-NLS-1$
		
		return container;
	}
	
	/**
	 * initialize result type
	 */
	private void initResultType() {
		boolean isEnable = false;
		if(QueryUtils.RESULT_TYPE.CSV.name().equals(comboResultType.getText())) {
			isEnable = true;
		}
		
		btnAddHeader.setEnabled(isEnable);
		textDelimiter.setEditable(isEnable);
	}
	
	/**
	 * initialize UI
	 */
	private void initUI() {
		initResultType();
		textAPIName.setText(resourceManagerDao.getName());
		textApiURL.setText(resourceManagerDao.getRestapi_uri());
		
		textArgument.setText(RESTfulAPIUtils.getParameter(strSQL));
		btnAddHeader.setSelection(true);
		textDelimiter.setText(","); //$NON-NLS-1$
	}
	
	/**
	 * initialize ui
	 * 
	 * @param strArgument
	 */
	private void initData(String strArgument) {
		
		try {
			String strReturnResult = ""; //$NON-NLS-1$
			
			// velocity 로 if else 가 있는지 검사합니다. 
			String strSQLs = RESTfulAPIUtils.makeTemplateTOSQL("APIServiceDialog", strSQL, strArgument); //$NON-NLS-1$
			// 분리자 만큼 실행한다.
			for (String strTmpSQL : strSQLs.split(PublicTadpoleDefine.SQL_DELIMITER)) {
				if(StringUtils.trim(strTmpSQL).equals("")) continue;

				NamedParameterDAO dao = NamedParameterUtil.parseParameterUtils(userDB, strTmpSQL, strArgument);
				if(QueryUtils.RESULT_TYPE.JSON.name().equalsIgnoreCase(comboResultType.getText())) {
					strReturnResult += getSelect(userDB, dao.getStrSQL(), dao.getListParam()) + ","; //$NON-NLS-1$
				} else {
					strReturnResult += getSelect(userDB, dao.getStrSQL(), dao.getListParam());
				}
			}
			
			if(QueryUtils.RESULT_TYPE.JSON.name().equalsIgnoreCase(comboResultType.getText())) {
				strReturnResult = "[" + StringUtils.removeEnd(strReturnResult, ",") + "]";  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			
			textResult.setText(strReturnResult);
			
		} catch (Exception e) {
			logger.error("api exception", e); //$NON-NLS-1$
			
			MessageDialog.openError(getShell(), Messages.get().Error, Messages.get().APIServiceDialog_11 + "\n" + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		textArgument.setFocus();
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
		String strResult = ""; //$NON-NLS-1$
		
		if(SQLUtil.isStatement(strSQL)) {
			
			if(QueryUtils.RESULT_TYPE.JSON.name().equals(comboResultType.getText())) {
				JsonArray jsonArry = QueryUtils.selectToJson(userDB, strSQL, listParam);
				strResult = JSONUtil.getPretty(jsonArry.toString());
			} else if(QueryUtils.RESULT_TYPE.CSV.name().equals(comboResultType.getText())) {
				strResult = QueryUtils.selectToCSV(userDB, strSQL, listParam, btnAddHeader.getSelection(), textDelimiter.getText());
			} else if(QueryUtils.RESULT_TYPE.XML.name().equals(comboResultType.getText())) {
				strResult = QueryUtils.selectToXML(userDB, strSQL, listParam);
			} else {
				strResult = QueryUtils.selectToHTML_TABLE(userDB, strSQL, listParam);
			}
		} else {
			strResult = QueryUtils.executeDML(userDB, strSQL, listParam, comboResultType.getText());
		}
		
		return strResult;
	}

	/** download service handler call */
	private void unregisterServiceHandler() {
		RWT.getServiceManager().unregisterServiceHandler(downloadServiceHandler.getId());
		downloadServiceHandler = null;
	}
	
	@Override
	public boolean close() {
		try {
			unregisterServiceHandler();
		} catch(Exception e) {
			logger.error("unregisterServiceHandler", e); //$NON-NLS-1$
		}
		return super.close();
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
			downloadExtFile("TadpoleAPIServer.txt", strResult); //$NON-NLS-1$
		} else {
			super.buttonPressed(buttonId);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		initData(textArgument.getText());
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.get().Close, false);
		createButton(parent, DOWNLOAD_BTN_ID, Messages.get().Download, false);
		createButton(parent, IDialogConstants.OK_ID, Messages.get().RUN, true);
	}
	
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(650, 500);
	}
}
