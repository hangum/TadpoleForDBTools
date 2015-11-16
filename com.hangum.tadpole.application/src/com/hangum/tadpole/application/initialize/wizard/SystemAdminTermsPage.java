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
package com.hangum.tadpole.application.initialize.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.application.Messages;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;

/**
 * 사용자 약관 페이지.
 * 
 * @author hangum
 *
 */
public class SystemAdminTermsPage extends WizardPage {
	private Button btnAggree;

	/**
	 * Create the wizard.
	 */
	public SystemAdminTermsPage() {
		super("SystemInitializeWizard"); //$NON-NLS-1$
		setTitle(Messages.get().SystemAdminTermsPage_0); //$NON-NLS-1$
		setDescription(Messages.get().SystemAdminTermsPage_0); //$NON-NLS-1$
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		Text textTerms = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textTerms.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		textTerms.setText(Messages.get().SystemAdminTermsPage_1);
		
		btnAggree = new Button(container, SWT.CHECK);
		btnAggree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getWizard().getContainer().updateButtons();
			}
		});
		btnAggree.setText(Messages.get().SystemAdminTermsPage_3); //$NON-NLS-1$
		
		setPageComplete(false);
		
		AnalyticCaller.track("SystemAdminTermsPage"); //$NON-NLS-1$
	}
	
	@Override
	public boolean canFlipToNextPage() {
		if(btnAggree.getSelection()) return true;
		else return false;
	}
	
	public boolean getAggree() {
		return btnAggree.getSelection();
	}
}
