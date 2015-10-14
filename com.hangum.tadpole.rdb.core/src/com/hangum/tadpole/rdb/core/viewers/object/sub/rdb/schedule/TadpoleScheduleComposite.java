package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.schedule;


import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;

public class TadpoleScheduleComposite extends AbstractObjectComposite {
	
	TableViewer tvSchedule;
	
	/**
	 * procedure
	 * 
	 * @param site
	 * @param tabFolderObject
	 * @param userDB
	 */
	public TadpoleScheduleComposite(IWorkbenchPartSite site, CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(site, tabFolderObject, userDB);
		createWidget(tabFolderObject);
	}
	
	private void createWidget(final CTabFolder tabFolderObject) {
		CTabItem tbtmProcedures = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmProcedures.setText("Schedule");
		tbtmProcedures.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.SCHEDULE.name());

		Composite compositePackages = new Composite(tabFolderObject, SWT.NONE);
		tbtmProcedures.setControl(compositePackages);
		GridLayout gl_compositePackages = new GridLayout(1, false);
		gl_compositePackages.verticalSpacing = 2;
		gl_compositePackages.horizontalSpacing = 2;
		gl_compositePackages.marginHeight = 2;
		gl_compositePackages.marginWidth = 2;
		compositePackages.setLayout(gl_compositePackages);
		
		tvSchedule = new TableViewer(compositePackages, SWT.NONE);
		Table table = tvSchedule.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		crateSchedule(tvSchedule, null);
		
		
	}

	@Override
	public void setSearchText(String searchText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initAction() {
		// TODO Auto-generated method stub
		
	}

	public TableViewer getTableViewer() {
		return tvSchedule;
	}

	public void refreshSchedule(UserDBDAO userDB, boolean boolRefresh) {
		
	}

}
