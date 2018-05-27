package com.hangum.tadpole.application.initialize;

import org.eclipse.rap.rwt.RWT;

public class Messages {
	private static final String BUNDLE_NAME = "com.hangum.tadpole.application.initialize.messages"; //$NON-NLS-1$

	public String SystemAdminTermsPage_0;
	public String SystemAdminTermsPage_1;
	public String SystemAdminTermsPage_3;

	public String SystemAdminWizardUseTypePage_0;

	public String SystemAdminWizardUseType_1;
	public String SystemAdminWizardUseType_3;
	public String SystemAdminWizardUseType_4;
	public String SystemAdminWizardUseType_6;
	public String SystemAdminWizardUseType_7;

	public String SystemAdminWizardPage_1;
	public String SystemAdminWizardPage_2;
	
	public String SystemAdminWizardPage_3;
	public String SystemAdminWizardPage_35;
	public String SystemAdminWizardPage_37;
	public String SystemAdminWizardPage_39;
	public String SystemAdminWizardPage_6;

	public static Messages get() {
		return RWT.NLS.getISO8859_1Encoded(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
