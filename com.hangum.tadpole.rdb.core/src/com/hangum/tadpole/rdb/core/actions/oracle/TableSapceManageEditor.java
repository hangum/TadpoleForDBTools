/*******************************************************************************
 * Copyright (c) 2016 nilriri.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     nilriri - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.actions.oracle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.commons.dialogs.message.dao.RequestResultDAO;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.rdb.OracleTablespaceDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.ExecuteDDLCommand;
import com.hangum.tadpole.engine.sql.util.QueryUtils;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.ColumnHeaderCreator;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultTableColumnFilter;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.TableViewColumnDefine;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.swtdesigner.ResourceManager;

/**
 * 오라클 테이블 스페이즈 정보를 조회하고 관리한다.
 * 
 * @author nilriri
 * 
 */
public class TableSapceManageEditor extends EditorPart {
	public final static String ID = "com.hangum.tadpole.rdb.core.actions.oracle.tablespace"; //$NON-NLS-1$
	private static final Logger logger = Logger.getLogger(TableSapceManageEditor.class);

	private Label lblDbname;
	private TableViewer tableViewer;
	private TableViewer tableViewer_datafile;
	private DefaultTableColumnFilter columnFilter;
	private List<OracleTablespaceDAO> showTablespaceList;

	private UserDBDAO userDB;
	private Table table;
	private Text textScript;
	private OracleTablespaceDAO tablespaceDao = null;
	private Text textDropScript;
	private Button btnIncludingContents;
	private Button btnCascadeConstraints;

	public TableSapceManageEditor() {
		super();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		TableSpaceManagerEditorInput esqli = (TableSpaceManagerEditorInput) input;
		setPartName(esqli.getName());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 1;
		gl_parent.marginHeight = 1;
		gl_parent.horizontalSpacing = 1;
		gl_parent.marginWidth = 1;
		parent.setLayout(gl_parent);

		Composite compositeToolbar = new Composite(parent, SWT.NONE);
		compositeToolbar.setLayout(new GridLayout(2, false));
		compositeToolbar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		ToolBar toolBar = new ToolBar(compositeToolbar, SWT.FLAT | SWT.RIGHT);
		GridData gd_toolBar = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_toolBar.widthHint = 267;
		toolBar.setLayoutData(gd_toolBar);

		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.setToolTipText(Messages.get().Refresh);
		tltmRefresh.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/refresh.png")); //$NON-NLS-1$

		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshTablespaceList();
			}
		});

		lblDbname = new Label(compositeToolbar, SWT.NONE);
		lblDbname.setText(""); //$NON-NLS-1$

		columnFilter = new DefaultTableColumnFilter();

		Composite compositeTablespaceList = new Composite(parent, SWT.NONE);
		GridData gd_compositeTablespaceList = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_compositeTablespaceList.heightHint = 140;
		compositeTablespaceList.setLayoutData(gd_compositeTablespaceList);
		compositeTablespaceList.setLayout(new GridLayout(1, false));

		tableViewer = new TableViewer(compositeTablespaceList, SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTablespace = tableViewer.getTable();
		tableTablespace.setHeaderVisible(true);
		tableTablespace.setLinesVisible(true);
		tableTablespace.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableViewer.addFilter(columnFilter);

		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				if (tableViewer.getSelection().isEmpty()) {
					return;
				}

				StructuredSelection ss = (StructuredSelection) tableViewer.getSelection();
				tablespaceDao = (OracleTablespaceDAO) ss.getFirstElement();

				List<Map<String, String>> datafileMapList = new ArrayList<Map<String, String>>();
				String tablespaceScript = "";

				try {

					SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
					List<HashMap> datafileList = (List<HashMap>) sqlClient.queryForList("getTablespaceDataFileList", tablespaceDao.getTablespace_name()); //$NON-NLS-1$
					tablespaceScript = (String) sqlClient.queryForObject("getTablespaceScript", tablespaceDao.getTablespace_name()); //$NON-NLS-1$

					for (Map<String, String> datafileMap : datafileList) {
						for (String key : datafileMap.keySet()) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("key", key);
							map.put("value", String.valueOf(datafileMap.get(key)));
							datafileMapList.add(map);
						}
					}

				} catch (Exception e) {
					logger.error("Tablespace detail information loading...", e); //$NON-NLS-1$
				}
				textScript.setText(StringUtils.trimToEmpty(tablespaceScript));
				textDropScript.setText(StringUtils.trimToEmpty(getDropScript()));
				tableViewer_datafile.setInput(datafileMapList);
				tableViewer_datafile.refresh();
			}
		});

		Group grpQuery = new Group(parent, SWT.NONE);
		grpQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpQuery.setText("Detail Information");
		GridLayout gl_grpQuery = new GridLayout(1, false);
		gl_grpQuery.verticalSpacing = 1;
		gl_grpQuery.horizontalSpacing = 1;
		gl_grpQuery.marginHeight = 1;
		gl_grpQuery.marginWidth = 1;
		grpQuery.setLayout(gl_grpQuery);

		Composite composite = new Composite(grpQuery, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm sashForm = new SashForm(composite, SWT.NONE);

		Composite composite_2 = new Composite(sashForm, SWT.NONE);
		composite_2.setLayout(new GridLayout(1, false));

		tableViewer_datafile = new TableViewer(composite_2, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer_datafile.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableViewerColumn tvPropertyName = new TableViewerColumn(tableViewer_datafile, SWT.NONE);
		TableColumn tcPropertyName = tvPropertyName.getColumn();
		tcPropertyName.setWidth(180);
		tcPropertyName.setText("Property");

		TableViewerColumn tvPropertyValue = new TableViewerColumn(tableViewer_datafile, SWT.NONE);
		TableColumn tcPropertyValue = tvPropertyValue.getColumn();
		tcPropertyValue.setWidth(300);
		tcPropertyValue.setText("Value");

		tableViewer_datafile.setContentProvider(new ArrayContentProvider());
		tableViewer_datafile.setLabelProvider(new TablespaceExtInfoLabelProvider());

		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));

		Label lblNewLabel_1 = new Label(composite_1, SWT.NONE);
		lblNewLabel_1.setText("Drop Tablespace");

		textDropScript = new Text(composite_1, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_textDropScript = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_textDropScript.heightHint = 50;
		gd_textDropScript.minimumHeight = 20;
		textDropScript.setLayoutData(gd_textDropScript);

		Composite composite_4 = new Composite(composite_1, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		composite_4.setLayout(new GridLayout(3, false));

		btnIncludingContents = new Button(composite_4, SWT.CHECK);
		btnIncludingContents.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCascadeConstraints.setEnabled(btnIncludingContents.getSelection());
				textDropScript.setText(getDropScript());
			}
		});
		btnIncludingContents.setText("Including Contents?");

		btnCascadeConstraints = new Button(composite_4, SWT.CHECK);
		btnCascadeConstraints.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textDropScript.setText(getDropScript());
			}
		});
		btnCascadeConstraints.setEnabled(false);
		btnCascadeConstraints.setText("Cascade Constraints?");

		Button btnDrop = new Button(composite_4, SWT.NONE);
		btnDrop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				//Excute script
				RequestResultDAO reqReResultDAO = new RequestResultDAO();
				try {
					ExecuteDDLCommand.executSQL(userDB, reqReResultDAO, textDropScript.getText().trim());
				} catch (Exception ex) {
					logger.error(ex);
				} //$NON-NLS-1$
				if (PublicTadpoleDefine.SUCCESS_FAIL.F.name().equals(reqReResultDAO.getResult())) {
					MessageDialog.openError(getSite().getShell(), Messages.get().Error, "오류가 발생했습니다.\n" + reqReResultDAO.getMesssage() + reqReResultDAO.getException().getMessage());
				} else {
					MessageDialog.openInformation(getSite().getShell(), Messages.get().Information, "작업이 완료 되었습니다.");
					refreshTablespaceList();
				}

			}
		});
		btnDrop.setSize(94, 28);
		btnDrop.setText("Drop Tablespace");

		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel.setText("Create Tablespace");

		textScript = new Text(composite_1, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		textScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		Button btnExecute = new Button(composite_3, SWT.NONE);
		btnExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				//Excute script
				RequestResultDAO reqReResultDAO = new RequestResultDAO();
				try {
					ExecuteDDLCommand.executSQL(userDB, reqReResultDAO, textScript.getText().trim());
				} catch (Exception ex) {
					logger.error(ex);
				} //$NON-NLS-1$
				if (PublicTadpoleDefine.SUCCESS_FAIL.F.name().equals(reqReResultDAO.getResult())) {
					MessageDialog.openError(getSite().getShell(), Messages.get().Error, "오류가 발생했습니다.\n" + reqReResultDAO.getMesssage() + reqReResultDAO.getException().getMessage());
				} else {
					MessageDialog.openInformation(getSite().getShell(), Messages.get().Information, "작업이 완료 되었습니다.");
					refreshTablespaceList();
				}
			}
		});
		btnExecute.setBounds(0, 0, 150, 28);
		btnExecute.setText("Create Tablespace");
		sashForm.setWeights(new int[] { 1, 1 });

		createTableColumn();

		initUI();

	}

	/**
	 * initialize UI
	 */
	private void initUI() {
		final ManagerViewer managerView = (ManagerViewer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ManagerViewer.ID);
		IStructuredSelection iss = (IStructuredSelection) managerView.getManagerTV().getSelection();
		if (iss.getFirstElement() instanceof UserDBDAO) {
			userDB = (UserDBDAO) iss.getFirstElement();

			refreshTablespaceList();
		}

		// google analytic
		AnalyticCaller.track(TableSapceManageEditor.ID);
	}

	private String getDropScript() {

		String drop_stmt = "";
		try {
			drop_stmt += "DROP TABLESPACE " + tablespaceDao.getTablespace_name();
			if (btnIncludingContents.getSelection()) {
				drop_stmt += " INCLUDING CONTENTS AND DATAFILES ";
				if (btnCascadeConstraints.getSelection()) {
					drop_stmt += " CASCADE CONSTRAINTS";
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return drop_stmt;
	}

	public void refreshTablespaceList() {
		if (lblDbname.isDisposed())
			return;
		if (userDB == null)
			return;

		textScript.setText(StringUtils.EMPTY);
		textDropScript.setText(StringUtils.EMPTY);
		lblDbname.setText(userDB.getDisplay_name());
		String map_key = "getTablespaceList";

		try {
			QueryUtils.executeQuery(userDB, "SELECT 1 FROM SYS.DBA_DATA_FILES A,SYS.DBA_FREE_SPACE B,SYS.V_$TEMP_SPACE_HEADER C,SYS.V_$TEMP_EXTENT_POOL D,SYS.DBA_TEMP_FILES E,V$PARAMETER F WHERE 0=1", 0, 1);
		} catch (Exception e) {
			map_key = "getUserTablespaceList";
			//MessageDialog.openError(this.getSite().getShell(), Messages.get().Error, "Tablespace 정보를 조회하기 위한 시스템 테이블 접근 권한이 부족합니다.\n" + e.getMessage() );
			//showTablespaceList = new ArrayList<OracleTablespaceDAO>();
			//return;
		}

		try {

			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			showTablespaceList = (List<OracleTablespaceDAO>) sqlClient.queryForList(map_key); //$NON-NLS-1$

			tableViewer.setInput(showTablespaceList);
			tableViewer.refresh();

		} catch (Exception e) {
			logger.error("refresh list", e); //$NON-NLS-1$

			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, e.getMessage(), errStatus); //$NON-NLS-1$
		}
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * table column head를 생성합니다.
	 */
	private void createTableColumn() {
		TableViewColumnDefine[] tableColumnDef = new TableViewColumnDefine[] { new TableViewColumnDefine("TABLESPACE_NAME", "Name", 200, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("STATUS", "Status", 70, SWT.CENTER) //$NON-NLS-1$
				, new TableViewColumnDefine("CONTENTS", "Contents", 100, SWT.CENTER) //$NON-NLS-1$
				, new TableViewColumnDefine("EXTENT_MANAGEMENT", "Extent", 100, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("BIGFILE", "Big file", 60, SWT.CENTER) //$NON-NLS-1$
				, new TableViewColumnDefine("MB_SIZE", "Size", 60, SWT.RIGHT) //$NON-NLS-1$
				, new TableViewColumnDefine("MB_FREE", "Free", 60, SWT.RIGHT) //$NON-NLS-1$
				, new TableViewColumnDefine("MB_USED", "Used", 60, SWT.RIGHT) //$NON-NLS-1$
				, new TableViewColumnDefine("PCT_FREE", "Free %", 70, SWT.RIGHT) //$NON-NLS-1$
				, new TableViewColumnDefine("PCT_USED", "Used %", 70, SWT.RIGHT) //$NON-NLS-1$
				, new TableViewColumnDefine("MB_MAX", "Max Size", 70, SWT.RIGHT) //$NON-NLS-1$
		};

		ColumnHeaderCreator.createColumnHeader(tableViewer, tableColumnDef);

		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new TableSpaceManagerLabelProvider(tableViewer));
	}

	@Override
	public void setFocus() {
		refreshTablespaceList();
	}

}
