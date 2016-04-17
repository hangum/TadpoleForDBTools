package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.columns;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.table.TadpoleTableComposite;

public class AbstractTableComposite extends Composite {
	protected TadpoleTableComposite tableComposite;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AbstractTableComposite(TadpoleTableComposite tableComposite, Composite parent, int style) {
		super(parent, style);

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
