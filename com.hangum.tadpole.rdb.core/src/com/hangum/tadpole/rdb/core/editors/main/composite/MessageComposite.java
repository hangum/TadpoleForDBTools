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

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.dialogs.message.dao.TadpoleMessageDAO;
import com.swtdesigner.SWTResourceManager;

/**
 * Result Message Composite
 * 
 * @author hangum
 *
 */
public class MessageComposite extends Composite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(MessageComposite.class);
	private Text textMessage;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MessageComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		textMessage = new Text(this, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		textMessage.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		
		// text limit
		textMessage.setTextLimit(10000);
	}

	/**
	 * table view refresh
	 * 
	 * @param tadpoleMessageDAO
	 */
	public void addAfterRefresh(TadpoleMessageDAO tadpoleMessageDAO) {
		String strNewMessage = "=[" + tadpoleMessageDAO.getDateExecute().toString() + "]============\n";
		
		Throwable throwable = tadpoleMessageDAO.getThrowable();
		if(throwable == null) {
			strNewMessage += tadpoleMessageDAO.getStrMessage();
		} else {
			if (throwable instanceof SQLException) {
				SQLException sqlException = (SQLException)throwable;
				StringBuffer sbMsg = new StringBuffer();
				
				sbMsg.append("State : " + sqlException.getSQLState() + PublicTadpoleDefine.LINE_SEPARATOR);
				sbMsg.append("Error Code: " + sqlException.getErrorCode() + PublicTadpoleDefine.LINE_SEPARATOR);
				sbMsg.append("Message : " + sqlException.getMessage() + PublicTadpoleDefine.LINE_SEPARATOR);
				
				strNewMessage += sbMsg.toString();
			} else {
				strNewMessage += tadpoleMessageDAO.getStrMessage();
			}
		}
		
		String strOldText = textMessage.getText();
		textMessage.setText(strNewMessage+ PublicTadpoleDefine.LINE_SEPARATOR + strOldText);
		textMessage.setSelection(0, strNewMessage.length());
		
	}
	
	@Override
	protected void checkSubclass() {
	}

}
