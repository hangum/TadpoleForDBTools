/*******************************************************************************
 * Copyright (c) 2012 Cho Hyun Jong.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cho Hyun Jong - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.secret.util;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.util.secret.EncryptiDecryptUtil;

/**
 * 올챙이 엔진디비의 암호화 파일 
 * 
 * @author hangum
 *
 */
public class TadpoleEngineEncryptFile implements IEntryPoint {
	private static String defaultContent = "DB=CUBRID \r\n" + 
			"ip=127.0.0.1 \r\n" +
			"port=33000 \r\n" +
			"database=demodb \r\n" +
			"user=dba \r\n" +
			"password=";
	private Text txtOriginal;
	private Text textEncrypt;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public int createUI() {
		Display display = new Display ();
		Shell shellSecret = new Shell (display);
		shellSecret.setText("Tadpole cfg Encrypt");
		GridLayout gl_shellSecret = new GridLayout(1, false);
		gl_shellSecret.marginWidth = 2;
		gl_shellSecret.verticalSpacing = 2;
		gl_shellSecret.horizontalSpacing = 2;
		gl_shellSecret.marginHeight = 2;
		shellSecret.setLayout(gl_shellSecret);
		
		Group grpOriginal = new Group(shellSecret, SWT.NONE);
		GridLayout gl_grpOriginal = new GridLayout(1, false);
		gl_grpOriginal.verticalSpacing = 2;
		gl_grpOriginal.horizontalSpacing = 2;
		gl_grpOriginal.marginHeight = 2;
		gl_grpOriginal.marginWidth = 2;
		grpOriginal.setLayout(gl_grpOriginal);
		grpOriginal.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpOriginal.setText("Original");
		
		txtOriginal = new Text(grpOriginal, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		txtOriginal.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		txtOriginal.setText(defaultContent);
		
		Group grpEncrypt = new Group(grpOriginal, SWT.NONE);
		grpEncrypt.setText("Encrypt");
		grpEncrypt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_grpEncrypt = new GridLayout(1, false);
		gl_grpEncrypt.verticalSpacing = 2;
		gl_grpEncrypt.horizontalSpacing = 2;
		gl_grpEncrypt.marginHeight = 2;
		gl_grpEncrypt.marginWidth = 2;
		grpEncrypt.setLayout(gl_grpEncrypt);
		
		textEncrypt = new Text(grpEncrypt, SWT.BORDER | SWT.WRAP);
		textEncrypt.setEditable(false);
		textEncrypt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite = new Composite(shellSecret, SWT.NONE);
		GridLayout gl_composite = new GridLayout(3, false);
		gl_composite.verticalSpacing = 2;
		gl_composite.horizontalSpacing = 2;
		gl_composite.marginHeight = 2;
		gl_composite.marginWidth = 2;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnLoadDefault = new Button(composite, SWT.NONE);
		btnLoadDefault.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				txtOriginal.setText(defaultContent);
			}
		});
		btnLoadDefault.setText("Load Default");
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button bfnEncrypt = new Button(composite, SWT.NONE);
		bfnEncrypt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				encrypt();
			}
		});
		bfnEncrypt.setText("Encrypt");
		
		shellSecret.setSize(600, 400);
		shellSecret.open();
				
//		while (!shellSecret.isDisposed ()) {
//			if (!display.readAndDispatch ()) display.sleep ();
//		}
//		display.dispose ();
		
		return 0;
	}

	/**
	 * 텍스트를 암호화 합니다.
	 */
	private void encrypt() {
		try {
			String strEncrypt = EncryptiDecryptUtil.encryption(defaultContent);
			textEncrypt.setText(strEncrypt);
			
//			System.out.println(strEncrypt);			
//			String strDecrypt = EncryptiDecryptUtil.decryption(strEncrypt);
//			System.out.println(strDecrypt);
		} catch(Exception ee) {
			ee.printStackTrace();
		}
	}


}
