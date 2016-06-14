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
package com.hangum.tadpole.rdb.core.editors.main.composite.tail;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.mongodb.core.dialogs.msg.TadpoleSQLDialog;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.dialog.export.sqlresult.ResultSetDownloadDialog;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;
import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;

/**
 * abstract tail composite
 * 
 * @author hangum
 *
 */
public abstract class AbstractTailComposite extends Composite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(AbstractTailComposite.class);
	private Composite compositeParent;
	protected Composite compositeDownloadAMsg;
	
	protected Label lblQueryResultStatus;
	protected Button btnPin;
	protected Button btnViewQuery;
	
	public AbstractTailComposite(Composite compositeBtn, int style) {
		super(compositeBtn, style);
		setLayout(new GridLayout(1, false));
		
		compositeParent = compositeBtn;
		compositeDownloadAMsg = new Composite(this, SWT.NONE);
		GridLayout gl_compositeDownloadAMsg = new GridLayout(7, false);
		gl_compositeDownloadAMsg.verticalSpacing = 2;
		gl_compositeDownloadAMsg.horizontalSpacing = 2;
		gl_compositeDownloadAMsg.marginHeight = 0;
		gl_compositeDownloadAMsg.marginWidth = 2;
		compositeDownloadAMsg.setLayout(gl_compositeDownloadAMsg);
		compositeDownloadAMsg.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		btnPin = new Button(compositeDownloadAMsg, SWT.TOGGLE);
		btnPin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strPin = btnPin.getToolTipText();
				if("Pin".equals(strPin)) {
					makePinBtn();
				} else {
					Composite parentComposite = compositeParent.getParent().getParent();
					Composite resultMainComposite = parentComposite.getParent();
					parentComposite.dispose();
					
					resultMainComposite.layout();
				}
			}
		});
		btnPin.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/editor/pin.png"));
		btnPin.setToolTipText("Pin");
		
		btnViewQuery = new Button(compositeDownloadAMsg, SWT.NONE);
		btnViewQuery.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String strSQL = getSQL();
				if(StringUtils.isNotEmpty(strSQL)) { 
					TadpoleSQLDialog dialog = new TadpoleSQLDialog(getShell(), Messages.get().ViewQuery, strSQL);
					dialog.open();
				}
			}
		});
		btnViewQuery.setText(Messages.get().ViewQuery);
		
		Button btnSQLResultDownload = new Button(compositeDownloadAMsg, SWT.NONE);
		btnSQLResultDownload.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false, 1, 1));
		btnSQLResultDownload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(getRSDao() == null || getRSDao().getDataList() == null) return;
				
				ResultSetDownloadDialog dialog = new ResultSetDownloadDialog(getShell(), getRequestQuery(), findTableName(), getRSDao());
				dialog.open();
			}
			
		});
		btnSQLResultDownload.setText(Messages.get().Download);
		
		Label label = new Label(compositeDownloadAMsg, SWT.NONE);
		label.setText("");
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblQueryResultStatus = new Label(compositeDownloadAMsg, SWT.NONE);
		lblQueryResultStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblQueryResultStatus.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
	}
	
	public void execute(String strResultMsg) {
		this.layout();
		lblQueryResultStatus.setText(strResultMsg);
		lblQueryResultStatus.pack();
	}
	
	public abstract RequestQuery getRequestQuery();
	
	private void makePinBtn() {
		btnPin.setToolTipText("Unpin");
		btnPin.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		btnPin.getParent().layout();
	}
	
	/**
	 * btn pin selection
	 * @return
	 */
	public boolean getBtnPinSelection() {
		if(btnPin.isDisposed()) return true;
		return btnPin.getSelection();
	}
	
	public void setBtnPint(boolean isSelect) {
		btnPin.setSelection(isSelect);
		if(isSelect) {
			makePinBtn();
		}
	}
	
	public abstract String getSQL();
	
	/**
	 * get query result dto
	 * @return
	 */
	public abstract QueryExecuteResultDTO getRSDao();
	/**
	 * find table name
	 * @return
	 */
	protected String findTableName() {
		String strTableName = "TempTable"; //$NON-NLS-1$
		if(!getRSDao().getColumnTableName().isEmpty()) strTableName = getRSDao().getColumnTableName().get(1);
		if(strTableName == null | "".equals(strTableName)) strTableName = "TempTable"; //$NON-NLS-1$ //$NON-NLS-2$
		
		return strTableName;
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	protected void checkSubclass() {
	}
}
