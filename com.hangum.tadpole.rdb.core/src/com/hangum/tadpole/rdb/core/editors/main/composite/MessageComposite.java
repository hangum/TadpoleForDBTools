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
package com.hangum.tadpole.rdb.core.editors.main.composite;

import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.dialogs.message.dao.TadpoleMessageDAO;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.swtdesigner.SWTResourceManager;

/**
 * Result Message Composite
 * 
 * @author hangum
 *
 */
public class MessageComposite extends Composite {
	/** Logger for this class. */
	private static final Logger logger = Logger.getLogger(MessageComposite.class);
	private Text textMessage;
	Label lblGoogleSearch;
	private Composite compositeTail;
//	private String strSearchError = "";

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public MessageComposite(Composite parent, int style) {
		super(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 3;
		gridLayout.horizontalSpacing = 3;
		gridLayout.marginHeight = 3;
		gridLayout.marginWidth = 3;
		setLayout(gridLayout);
		
		textMessage = new Text(this, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		compositeTail = new Composite(this, SWT.NONE);
		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeTail.setLayout(new GridLayout(2, false));
		
		Label lblGoogleSearchTitle = new Label(compositeTail, SWT.NONE);
		lblGoogleSearchTitle.setText(Messages.get().MessageComposite_lblGoogleSearch_1_text);
		
		// text limit
//		textMessage.setTextLimit(1000);
//		Button btnClear = new Button(this, SWT.NONE);
//		btnClear.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				gotoGoogleSearch();
//			}
//		});
//		btnClear.setText(Messages.get().MessageComposite_1);
		lblGoogleSearch = new Label(compositeTail, SWT.NONE);
		lblGoogleSearch.setData( RWT.MARKUP_ENABLED, Boolean.TRUE );
	}
	
	/**
	 * refresh info message
	 * 
	 * @param userDB
	 * @param requestQuery
	 * @param msg
	 */
	public void addInfoAfterRefresh(UserDBDAO userDB, RequestQuery requestQuery, String msg) {
		textMessage.setBackground(SWTResourceManager.getColor(248, 248, 255));
		textMessage.setText(msg);
		lblGoogleSearch.setText("");
	}

	/**
	 * new error message
	 * 
	 * @param userDBDAO 
	 * @param requestQuery
	 * @param tadpoleMessageDAO
	 */
	public void addErrorAfterRefresh(UserDBDAO userDBDAO, RequestQuery requestQuery, TadpoleMessageDAO tadpoleMessageDAO) {
		String strNewMessage = " "; //$NON-NLS-1$
		String strSearchError = userDBDAO.getDbms_type() + " "; //$NON-NLS-1$

		Throwable throwable = tadpoleMessageDAO.getThrowable();
		if (throwable == null) {
			strNewMessage = Messages.get().SystemMessage;
			strNewMessage += tadpoleMessageDAO.getStrMessage();
			strSearchError = tadpoleMessageDAO.getStrMessage();
			
			textMessage.setBackground(SWTResourceManager.getColor(248, 248, 255));
		} else {
			strNewMessage = Messages.get().ErrorMessage;
			
			Throwable cause = throwable.getCause();
			if(throwable instanceof SQLException) {
				strNewMessage += sqlExceptionToMsg((SQLException)throwable, tadpoleMessageDAO);
				strSearchError += sqlExceptionToSearchMsg((SQLException)throwable, tadpoleMessageDAO);
			} else if (cause instanceof SQLException) {
				strNewMessage += sqlExceptionToMsg((SQLException)cause, tadpoleMessageDAO);
				strSearchError += sqlExceptionToSearchMsg((SQLException)cause, tadpoleMessageDAO);
			} else {
				strNewMessage += tadpoleMessageDAO.getStrMessage();
				strSearchError += tadpoleMessageDAO.getStrMessage();
			}
			
			// sqlite 는 상태,에러코드가 없다.--;;
			if(userDBDAO.getDBDefine() == DBDefine.SQLite_DEFAULT) {
				strSearchError = throwable.getMessage();
			}
			
			textMessage.setBackground(SWTResourceManager.getColor(255, 228, 225));
		}

//		// first show last error message
//		final String strOldText = textMessage.getText();
//		if ("".equals(strOldText)) { //$NON-NLS-1$
		textMessage.setText(strNewMessage);
		
		try {
			String strDeleteWhiteSpace = StringUtils.replace(strSearchError, "\"", "'");
			lblGoogleSearch.setText("<a href=\"http://www.google.com/search?q=" + strDeleteWhiteSpace + "\" target='_blank'>" + strDeleteWhiteSpace + "</a>");
			lblGoogleSearch.getParent().layout();
		} catch(Exception e) {
			logger.error("===" + strSearchError + "====");
			logger.error("parse", e);
		}
//		} else {
//			textMessage.setText(strNewMessage + PublicTadpoleDefine.LINE_SEPARATOR + PublicTadpoleDefine.LINE_SEPARATOR + strOldText);
//		}
//		textMessage.setSelection(0, strNewMessage.length());
//		textMessage.setFocus();
	}
	
	/**
	 * SQLException to pretty message
	 * 
	 * @param sqlException
	 * @param tadpoleMessageDAO
	 * @return
	 */
	private String sqlExceptionToSearchMsg(SQLException sqlException, TadpoleMessageDAO tadpoleMessageDAO) {
		String strNewMessage = "";
		try {
			StringBuffer sbMsg = new StringBuffer();
			sbMsg.append(String.format(" %s %s", sqlException.getSQLState(), sqlException.getErrorCode()));
//			sbMsg.append(String.format(" %s", sqlException.getMessage()));

			strNewMessage = sbMsg.toString();
		} catch(Exception e) {
			logger.error("SQLException to striing", e); //$NON-NLS-1$
			strNewMessage = tadpoleMessageDAO.getStrMessage();
		}
		
		return strNewMessage;
	}
	
	/**
	 * SQLException to pretty message
	 * 
	 * @param sqlException
	 * @param tadpoleMessageDAO
	 * @return
	 */
	private String sqlExceptionToMsg(SQLException sqlException, TadpoleMessageDAO tadpoleMessageDAO) {
		String strNewMessage = "";
		try {
			StringBuffer sbMsg = new StringBuffer();
			sbMsg.append(String.format(Messages.get().MessageComposite_5, sqlException.getSQLState(), sqlException.getErrorCode()));
			sbMsg.append(String.format(Messages.get().MessageComposite_4, sqlException.getMessage()));

			strNewMessage = sbMsg.toString();
		} catch(Exception e) {
			logger.error("SQLException to striing", e); //$NON-NLS-1$
			strNewMessage = tadpoleMessageDAO.getStrMessage();
		}
		
		return strNewMessage;
	}

	@Override
	protected void checkSubclass() {
	}

}
