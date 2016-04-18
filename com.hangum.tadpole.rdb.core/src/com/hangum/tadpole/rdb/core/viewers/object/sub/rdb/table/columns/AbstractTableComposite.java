/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.columns;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.TadpoleTableComposite;

/**
 * Abstract table composite
 * 
 * @author hangum
 *
 */
public abstract class AbstractTableComposite extends Composite {
	protected TadpoleTableComposite tableComposite;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AbstractTableComposite(TadpoleTableComposite tableComposite, Composite parent, int style) {
		super(parent, SWT.NONE);

		this.tableComposite = tableComposite;
	}
	
	public TadpoleTableComposite getTableComposite() {
		return tableComposite;
	}
	
	public IWorkbenchPartSite getSite() {
		return tableComposite.getSite();
	}

	/**
	 * userdb
	 * @return
	 */
	public UserDBDAO getUserDB() {
		return tableComposite.getUserDB();
	}

	@Override
	protected void checkSubclass() {
	}

}
