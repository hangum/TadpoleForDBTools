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
package com.hangum.tadpole.rdb.core.dialog.dbconnect.sub.others;

import org.eclipse.swt.widgets.Composite;

import com.hangum.tadpole.engine.define.DBDefine;

/**
 * Others connection info
 * 
 * @author hangum
 *
 */
public class OthersConnectionRDBWithoutTunnelingGroup extends OthersConnectionGroup {
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public OthersConnectionRDBWithoutTunnelingGroup(Composite parent, int style, DBDefine selectDB) {
		super(parent, style, selectDB);
	}
	
	@Override
	public void initUI() {
//		btnSendMonitoring.setEnabled(false);
	}
	
}
