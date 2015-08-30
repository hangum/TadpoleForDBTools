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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.dialogs.message.dao.TadpoleMessageDAO;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.rdb.core.Messages;
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
		
		Button btnClear = new Button(this, SWT.NONE);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textMessage.setText(""); //$NON-NLS-1$
			}
		});
		btnClear.setText(Messages.MessageComposite_1);
	}

	/**
	 * table view refresh
	 * 
	 * @param tadpoleMessageDAO
	 */
	public void addAfterRefresh(TadpoleMessageDAO tadpoleMessageDAO) {
		String strNewMessage = String.format("==[ %s ]============\n", tadpoleMessageDAO.getDateExecute().toString()); //$NON-NLS-1$
		
		Throwable throwable = tadpoleMessageDAO.getThrowable();
		if(throwable == null) {
			strNewMessage += tadpoleMessageDAO.getStrMessage();
		} else {
			if (throwable instanceof SQLException) {
				SQLException sqlException = (SQLException)throwable;
				StringBuffer sbMsg = new StringBuffer();
				
				sbMsg.append(String.format("[SQL State : %s, Error Code: %s]", sqlException.getSQLState(), sqlException.getErrorCode())); //$NON-NLS-1$
				sbMsg.append(sqlException.getMessage());
				
				strNewMessage += sbMsg.toString();
			} else {
				strNewMessage += tadpoleMessageDAO.getStrMessage();
			}
		}
		
		// first show last error message
		final String strOldText = textMessage.getText();
		if("".equals(strOldText)) {
			textMessage.setText(strNewMessage);
			textMessage.setFocus();
			
			// Select the last error message.
			textMessage.setSelection(0, strNewMessage.length());
		} else {
			textMessage.setText(strOldText + PublicTadpoleDefine.LINE_SEPARATOR + PublicTadpoleDefine.LINE_SEPARATOR + strNewMessage);
			textMessage.setFocus();
			
			// Select the last error message.
			textMessage.setSelection(strOldText.length()+4, strOldText.length() + 4 + strNewMessage.length());
		}
	}
	
	@Override
	protected void checkSubclass() {
	}

}
