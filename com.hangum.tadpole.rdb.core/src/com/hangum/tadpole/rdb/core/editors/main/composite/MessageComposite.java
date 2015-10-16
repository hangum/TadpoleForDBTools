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

import com.hangum.tadpole.commons.dialogs.message.dao.TadpoleMessageDAO;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;

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

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public MessageComposite(Composite parent, int style) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(1, false));

		textMessage = new Text(this, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		    
		// text limit
//		textMessage.setTextLimit(1000);
//		Button btnClear = new Button(this, SWT.NONE);
//		btnClear.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				textMessage.setText(""); //$NON-NLS-1$
//			}
//		});
//		btnClear.setText(Messages.MessageComposite_1);
	}

	/**
	 * table view refresh
	 * 
	 * @param requestQuery
	 * @param tadpoleMessageDAO
	 */
	public void addAfterRefresh(RequestQuery requestQuery, TadpoleMessageDAO tadpoleMessageDAO) {
		String strNewMessage = ""; //$NON-NLS-1$

		Throwable throwable = tadpoleMessageDAO.getThrowable();
		if (throwable == null) {
			strNewMessage = Messages.MessageComposite_2;
			strNewMessage += tadpoleMessageDAO.getStrMessage();
		} else {
			strNewMessage = Messages.MessageComposite_3;
			
			Throwable cause = throwable.getCause();
			if (cause instanceof SQLException) {
				try {
					SQLException sqlException = (SQLException)cause;

					StringBuffer sbMsg = new StringBuffer();
					sbMsg.append(String.format(Messages.MessageComposite_5, sqlException.getSQLState(), sqlException.getErrorCode()));
					sbMsg.append(String.format(Messages.MessageComposite_4, sqlException.getMessage()));
	
					strNewMessage += sbMsg.toString();
				} catch(Exception e) {
					logger.error("SQLException to striing", e); //$NON-NLS-1$
					strNewMessage += tadpoleMessageDAO.getStrMessage();
				}
			} else {
				strNewMessage += tadpoleMessageDAO.getStrMessage();
			}
		}

//		// first show last error message
//		final String strOldText = textMessage.getText();
//		if ("".equals(strOldText)) { //$NON-NLS-1$
			textMessage.setText(strNewMessage);
//		} else {
//			textMessage.setText(strNewMessage + PublicTadpoleDefine.LINE_SEPARATOR + PublicTadpoleDefine.LINE_SEPARATOR + strOldText);
//		}
//		textMessage.setSelection(0, strNewMessage.length());
//		textMessage.setFocus();
	}

	@Override
	protected void checkSubclass() {
	}

}
