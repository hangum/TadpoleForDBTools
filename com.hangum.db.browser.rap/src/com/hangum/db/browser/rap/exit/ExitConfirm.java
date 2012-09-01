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
