package com.hangum.db.browser.rap.exit;

import org.eclipse.rap.ui.branding.IExitConfirmation;

/**
 * exit conform 
 * 
 * @author hangum
 *
 */
public class ExitConfirm implements IExitConfirmation {

	@Override
	public boolean showExitConfirmation() {
		return true;
	}

	@Override
	public String getExitConfirmationText() {
		return "Do you really wanna leave the Tadpole for DB Tools?";
	}

}
