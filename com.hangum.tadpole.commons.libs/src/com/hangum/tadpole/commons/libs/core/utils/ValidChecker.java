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
package com.hangum.tadpole.commons.libs.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.commons.libs.core.Messages;

/**
 * email vilid utils
 * 
 * @author hangum
 *
 */
public class ValidChecker {
	

	/**
	 * 
	 * 
	 * @param passwd
	 * @return
	 */
	public static boolean isSimplePasswordChecker(String passwd) {
		if(StringUtils.length(passwd) < 5) {
			return false;
		}
		return true;
	}
	
	/**
	 * validate passwd
	 * 
	 * @param passwd
	 * @return
	 */
	public static boolean isPasswordChecker(String passwd) {
		Pattern p = Pattern.compile("^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z]).{8,}$"); //$NON-NLS-1$
		Matcher m = p.matcher(passwd);
		return m.matches();
	}
	

	/**
	 * email valid checker
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isValidEmailAddress(String email) {
		return EmailValidator.getInstance().isValid(email);
	}
	
	/**
	 * number checker util
	 * 
	 * @param text
	 * @param msg
	 * @return
	 */
	public static boolean checkNumberCtl(Text text, String msg) {
		if(!NumberUtils.isNumber(text.getText())) {
			MessageDialog.openWarning(null, Messages.get().Warning, msg + Messages.get().CheckNumberString);
			text.setFocus();
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * Text checker util
	 * 
	 * @param text
	 * @param msg
	 * @return
	 */
	public static boolean checkTextCtl(Text text, String msg) {
		if("".equals(StringUtils.trimToEmpty(text.getText()))) { //$NON-NLS-1$
			MessageDialog.openWarning(null, Messages.get().Warning, msg + Messages.get().CheckTextString);
			text.setFocus();
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * combo checker util
	 * 
	 * @param text
	 * @param msg
	 * @return
	 */
	public static boolean checkTextCtl(Combo text, String msg) {
		if("".equals(StringUtils.trimToEmpty(text.getText()))) { //$NON-NLS-1$
			MessageDialog.openWarning(null, Messages.get().Warning, msg + Messages.get().CheckTextString);
			text.setFocus();
			
			return false;
		}
		
		return true;
	}
	

	/**
	 * host, port에 ping
	 * 
	 * @param host
	 * @param port
	 * @return
	 * @deprecated 서버에따라 핑 서비스를 막아 놓는 경우도 있어, 막아 놓습니다.
	 */
	public static boolean isPing(String host, String port) throws NumberFormatException {
		
//		TODO system 네트웍 속도가 느릴경우(?) 핑이 늦게와서 좀 늘려... 방법이 없을까? - hangum
		int stats = PingTest.ping(host, Integer.parseInt(port), 2500);
		if(PingTest.SUCCESS == stats) {
			return true;
		} else {
			return false;
		}
	}
}
