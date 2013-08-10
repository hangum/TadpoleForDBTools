/*******************************************************************************
 * Copyright (c) 2013 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.secret.util;

import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.cipher.core.manager.CipherManager;

/**
 * 올챙이 엔진디비의 암호화 파일
 * 
 * @author hangum
 * 
 */
public class BasicEntryPoint extends AbstractEntryPoint {
	private static String defaultContent = "DB=MYSQL \r\n"
						+ "ip=192.168.32.128 \r\n" 
						+ "port=3306 \r\n"
						+ "database=test \r\n" 
						+ "user=root \r\n" 
						+ "password=tadpole";
	private Text txtOriginal;
	private Text textEncrypt;

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	protected void createContents(final Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		final Composite composite2 = new Composite(parent, SWT.NONE);
		composite2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite2.setLayout(new GridLayout(1, false));

		Group grpOriginal = new Group(composite2, SWT.NONE);
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

		Composite composite = new Composite(composite2, SWT.NONE);
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

	}

	/**
	 * 텍스트를 암호화 합니다.
	 */
	private void encrypt() {
		try {
			String strEncrypt = CipherManager.getInstance().encryption(defaultContent);
			textEncrypt.setText(strEncrypt);

			// System.out.println(strEncrypt);
			// String strDecrypt = EncryptiDecryptUtil.decryption(strEncrypt);
			// System.out.println(strDecrypt);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

}
