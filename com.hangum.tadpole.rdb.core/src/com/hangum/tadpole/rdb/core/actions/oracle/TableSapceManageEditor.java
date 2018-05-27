/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     nilriri - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.actions.oracle;

import java.math.BigDecimal;
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
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
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

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.rdb.OracleTablespaceDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.executer.ExecuteDDLCommand;
import com.hangum.tadpole.engine.sql.util.executer.ExecuteDMLCommand;
import com.hangum.tadpole.engine.utils.RequestQueryUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.ColumnHeaderCreator;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultTableColumnFilter;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.TableViewColumnDefine;
import com.hangum.tadpole.rdb.core.viewers.connections.ManagerViewer;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;

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
	private TableViewer tableViewer_datafiles;
	private TableViewer tableViewer_property;
	private DefaultTableColumnFilter columnFilter;
	private List<OracleTablespaceDAO> showTablespaceList;

	private UserDBDAO userDB;
	private Table table;
	private Text textScript;
	private OracleTablespaceDAO tablespaceDao = null;
	private Text textDropScript;
	private Button btnIncludingContents;
	private Button btnAutoExtend;
	private Button btnCascadeConstraints;
	private Button btnMaximumSize;
	private Table table_datafiles;
	private Button btnDatafileName;
	private Text textDataFileName;
	private Text textDatafileSize;
	private Text textAutoExtendSize;
	private Text textMaximumSize;
	private Button btnReuse;

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
		tltmRefresh.setToolTipText(CommonMessages.get().Refresh);
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
		GridData gd_compositeTablespaceList = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_compositeTablespaceList.minimumHeight = 200;
		compositeTablespaceList.setLayoutData(gd_compositeTablespaceList);
		compositeTablespaceList.setLayout(new GridLayout(1, false));

		tableViewer = new TableViewer(compositeTablespaceList, SWT.BORDER | SWT.FULL_SELECTION);
		Table tableTablespace = tableViewer.getTable();
		tableTablespace.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableTablespace.setHeaderVisible(true);
		tableTablespace.setLinesVisible(true);
		tableViewer.addFilter(columnFilter);

		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				if (tableViewer.getSelection().isEmpty()) {
					return;
				}

				StructuredSelection ss = (StructuredSelection) tableViewer.getSelection();
				tablespaceDao = (OracleTablespaceDAO) ss.getFirstElement();

				List<HashMap<String, String>> datafileList = new ArrayList<HashMap<String, String>>();
				try {

					SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
					datafileList = (List<HashMap<String, String>>) sqlClient.queryForList("getTablespaceDataFileList", tablespaceDao.getTablespace_name()); //$NON-NLS-1$

				} catch (Exception e) {
					logger.error("Tablespace detail information loading...", e); //$NON-NLS-1$
				}

				textDropScript.setText(StringUtils.trimToEmpty(getDropScript()));
				makeCreateScript();

				tableViewer_datafiles.setInput(datafileList);
				tableViewer_datafiles.refresh();

				if (datafileList.size() > 0) {
					tableViewer_datafiles.setSelection(new StructuredSelection(tableViewer_datafiles.getElementAt(0)), true);

					refreshDatafileInformation();
				} else {
					// 테이블스페이스 선택을 변경하면 상세속성화면 초기화 한다.
					tableViewer_property.setInput(new ArrayList<Map<String, String>>());
					tableViewer_property.refresh();
				}
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

		SashForm sashForm_1 = new SashForm(sashForm, SWT.VERTICAL);

		Composite composite_2 = new Composite(sashForm_1, SWT.BORDER);
		GridLayout gl_composite_2 = new GridLayout(1, false);
		gl_composite_2.verticalSpacing = 0;
		gl_composite_2.horizontalSpacing = 0;
		gl_composite_2.marginHeight = 0;
		gl_composite_2.marginWidth = 0;
		composite_2.setLayout(gl_composite_2);

		tableViewer_datafiles = new TableViewer(composite_2, SWT.BORDER | SWT.FULL_SELECTION);
		table_datafiles = tableViewer_datafiles.getTable();
		table_datafiles.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table_datafiles.setLinesVisible(true);
		table_datafiles.setHeaderVisible(true);

		tableViewer_datafiles.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (tableViewer_datafiles.getSelection().isEmpty()) {
					return;
				}
				refreshDatafileInformation();
				makeCreateScript();
			}
		});

		Composite composite_6 = new Composite(composite_2, SWT.NONE);
		composite_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite_6 = new GridLayout(2, false);
		composite_6.setLayout(gl_composite_6);

		btnDatafileName = new Button(composite_6, SWT.CHECK);
		btnDatafileName.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				textDataFileName.setEnabled(btnDatafileName.getSelection());
				if (btnDatafileName.getSelection()) {
					StructuredSelection ss = (StructuredSelection) tableViewer_datafiles.getSelection();
					if (ss != null) {
						HashMap<String, Object> datafileMap = (HashMap<String, Object>) ss.getFirstElement();
						String fileName = (String) datafileMap.get("FILE_NAME");

						int cnt = tableViewer_datafiles.getTable().getItemCount() + 1;

						fileName = StringUtils.replaceOnce(fileName, ".", "_Copy" + cnt + ".");
						textDataFileName.setText(fileName);
					} else {
						textDataFileName.setFocus();
					}
				}
				makeAddDatafileScript();
			}
		});
		btnDatafileName.setText("Datafile Name");
		textDataFileName = new Text(composite_6, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		textDataFileName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
				makeAddDatafileScript();
			}
		});
		textDataFileName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textDataFileName.setText("Auto");
		textDataFileName.setEnabled(false);

		Label lblNewLabel_2 = new Label(composite_6, SWT.NONE);
		lblNewLabel_2.setText("Datafile Size(MB)");

		Composite composite_7 = new Composite(composite_6, SWT.NONE);
		GridLayout gl_composite_7 = new GridLayout(2, false);
		gl_composite_7.verticalSpacing = 0;
		gl_composite_7.horizontalSpacing = 0;
		gl_composite_7.marginWidth = 0;
		gl_composite_7.marginHeight = 0;
		composite_7.setLayout(gl_composite_7);

		textDatafileSize = new Text(composite_7, SWT.BORDER | SWT.RIGHT);
		textDatafileSize.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
				makeAddDatafileScript();
			}
		});
		GridData gd_textDatafileSize = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textDatafileSize.widthHint = 60;
		textDatafileSize.setLayoutData(gd_textDatafileSize);
		textDatafileSize.setText("5");

		btnReuse = new Button(composite_7, SWT.CHECK);
		btnReuse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				makeAddDatafileScript();
			}
		});
		btnReuse.setText("Reuse");

		Composite composite_5 = new Composite(composite_2, SWT.NONE);
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_5.setLayout(new GridLayout(4, false));

		btnAutoExtend = new Button(composite_5, SWT.CHECK);
		btnAutoExtend.setSelection(true);
		btnAutoExtend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textAutoExtendSize.setEnabled(btnAutoExtend.getSelection());
				btnMaximumSize.setEnabled(btnAutoExtend.getSelection());
				textMaximumSize.setEnabled(btnAutoExtend.getSelection());

				makeAddDatafileScript();
			}
		});
		btnAutoExtend.setText("Auto Extend");

		Label lblExtendSize = new Label(composite_5, SWT.NONE);
		lblExtendSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblExtendSize.setText("Extend Size(MB)");

		textAutoExtendSize = new Text(composite_5, SWT.BORDER | SWT.RIGHT);
		textAutoExtendSize.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
				makeAddDatafileScript();
			}
		});
		textAutoExtendSize.setText("5");
		textAutoExtendSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_5, SWT.NONE);

		btnMaximumSize = new Button(composite_5, SWT.CHECK);
		btnMaximumSize.setSelection(true);
		btnMaximumSize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textMaximumSize.setEnabled(!btnMaximumSize.getSelection());
				textMaximumSize.setText((1024 * 5)+""); //5GB
				textMaximumSize.setFocus();
				textMaximumSize.setSelection(0, textMaximumSize.getText().length());
				makeAddDatafileScript();
			}
		});
		btnMaximumSize.setText("Maximum Unlimited");

		Label lblMaximumSizemb = new Label(composite_5, SWT.NONE);
		lblMaximumSizemb.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMaximumSizemb.setText("Maximum Size(MB)");

		textMaximumSize = new Text(composite_5, SWT.BORDER | SWT.RIGHT);
		textMaximumSize.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent event) {
				makeAddDatafileScript();
			}
		});
		textMaximumSize.setEnabled(false);
		textMaximumSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnAddDatafile = new Button(composite_5, SWT.NONE);
		btnAddDatafile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				makeAddDatafileScript();
			}
		});
		btnAddDatafile.setText("Add Datafile");

		tableViewer_property = new TableViewer(sashForm_1, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer_property.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableViewerColumn tvPropertyName = new TableViewerColumn(tableViewer_property, SWT.NONE);
		TableColumn tcPropertyName = tvPropertyName.getColumn();
		tcPropertyName.setWidth(180);
		tcPropertyName.setText("Property");

		TableViewerColumn tvPropertyValue = new TableViewerColumn(tableViewer_property, SWT.NONE);
		TableColumn tcPropertyValue = tvPropertyValue.getColumn();
		tcPropertyValue.setWidth(300);
		tcPropertyValue.setText("Value");

		tableViewer_property.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer_property.setLabelProvider(new TablespaceExtInfoLabelProvider());
		sashForm_1.setWeights(new int[] { 1, 1 });

		Composite composite_1 = new Composite(sashForm, SWT.BORDER);
		composite_1.setLayout(new GridLayout(1, false));

		Label lblNewLabel_1 = new Label(composite_1, SWT.NONE);
		lblNewLabel_1.setText("Drop Tablespace");

		textDropScript = new Text(composite_1, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		textDropScript.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		GridData gd_textDropScript = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_textDropScript.minimumHeight = 20;
		textDropScript.setLayoutData(gd_textDropScript);

		Composite composite_4 = new Composite(composite_1, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		composite_4.setLayout(new GridLayout(3, false));

		btnIncludingContents = new Button(composite_4, SWT.CHECK);
		btnIncludingContents.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 데이터 파일을 함께 삭제한다.
				btnCascadeConstraints.setEnabled(btnIncludingContents.getSelection());
				textDropScript.setText(getDropScript());
			}
		});
		btnIncludingContents.setText("Including Contents?");

		btnCascadeConstraints = new Button(composite_4, SWT.CHECK);
		btnCascadeConstraints.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 제약조건을 함께 삭제한다.
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
				try {
					ExecuteDDLCommand.executSQL(RequestQueryUtil.simpleRequestQuery(userDB, textDropScript.getText().trim()));
					
					MessageDialog.openInformation(getSite().getShell(), CommonMessages.get().Information, Messages.get().WorkHasCompleted);
					refreshTablespaceList();
				} catch (Exception ex) {
					logger.error(ex);
					
					MessageDialog.openError(getSite().getShell(),CommonMessages.get().Error, Messages.get().RiseError + ex.getMessage());
				} //$NON-NLS-1$
			}
		});
		btnDrop.setSize(94, 28);
		btnDrop.setText("Drop Tablespace");

		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel.setText("Create Tablespace && Add Datafile");

		textScript = new Text(composite_1, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		textScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		Button btnExecute = new Button(composite_3, SWT.NONE);
		btnExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				//Excute script
				try {
					ExecuteDDLCommand.executSQL(RequestQueryUtil.simpleRequestQuery(userDB, textScript.getText().trim()));
					
					MessageDialog.openInformation(getSite().getShell(), CommonMessages.get().Information, Messages.get().WorkHasCompleted);
					refreshTablespaceList();
					refreshDatafileInformation();
				} catch (Exception ex) {
					logger.error(ex);
					
					MessageDialog.openError(getSite().getShell(),CommonMessages.get().Error, Messages.get().RiseError + ex.getMessage());
				} //$NON-NLS-1$
			}
		});
		btnExecute.setBounds(0, 0, 150, 28);
		btnExecute.setText("Execute Script");
		sashForm.setWeights(new int[] { 1, 1 });

		createTableColumn();
		createTableDataFileColumn();

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

	private void makeCreateScript() {
		StructuredSelection ss = (StructuredSelection) tableViewer.getSelection();
		tablespaceDao = (OracleTablespaceDAO) ss.getFirstElement();

		List<Map<String, String>> tablespaceScript = new ArrayList<Map<String, String>>();

		try {

			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			tablespaceScript = (List<Map<String, String>>) sqlClient.queryForList("getTablespaceScript", tablespaceDao.getTablespace_name()); //$NON-NLS-1$

		} catch (Exception e) {
			logger.error("Tablespace detail information loading...", e); //$NON-NLS-1$
		}

		String create_stmt = "";
		for (Map<String, String> line : tablespaceScript) {
			create_stmt += line.get("DDLSCRIPT") + "\n";
		}

		textScript.setText(StringUtils.trimToEmpty(create_stmt));
	}

	private void makeAddDatafileScript() {
		StringBuffer script = new StringBuffer();

		script.append("ALTER TABLESPACE " + tablespaceDao.getTablespace_name()).append("\n");
		script.append(" ADD DATAFILE \n");

		if (this.btnDatafileName.getSelection()) {
			script.append(" '").append(this.textDataFileName.getText().trim()).append("' \n");
		}

		script.append(" SIZE " + this.textDatafileSize.getText() + "M ");
		if (this.btnReuse.getSelection()) {
			script.append(" REUSE \n");
		}else{
			script.append(" \n");
		}		

		if (this.btnAutoExtend.getSelection()) {
			script.append(" AUTOEXTEND ON \n");
			script.append(" NEXT ").append(this.textAutoExtendSize.getText().trim()).append("M \n");
			if (this.btnMaximumSize.getSelection()) {
				script.append(" MAXSIZE UNLIMITED \n");

			} else {
				script.append(" MAXSIZE ").append(this.textMaximumSize.getText().trim()).append("M \n");
			}
		}
		textScript.setText(StringUtils.removeEnd(script.toString(), ";"));
	}

	private void refreshDatafileInformation() {
		StructuredSelection ss = (StructuredSelection) tableViewer_datafiles.getSelection();

		if (ss == null)
			return;

		HashMap<String, Object> datafileMap = (HashMap<String, Object>) ss.getFirstElement();

		List<HashMap<String, Object>> datafileInformationList = new ArrayList<HashMap<String, Object>>();
		List<Map<String, String>> datafilePropertyList = new ArrayList<Map<String, String>>();

		try {

			SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
			String file_id = ((BigDecimal) datafileMap.get("FILE_ID")).toString();

			datafileInformationList = (List<HashMap<String, Object>>) sqlClient.queryForList("getTablespaceDataFileInfomation", file_id); //$NON-NLS-1$

			for (Map<String, Object> informationMap : datafileInformationList) {
				for (String key : informationMap.keySet()) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("key", key);
					map.put("value", String.valueOf(informationMap.get(key)));
					datafilePropertyList.add(map);
				}
			}

		} catch (Exception e) {
			logger.error("Tablespace detail information loading...", e); //$NON-NLS-1$
		}
		textDropScript.setText(StringUtils.trimToEmpty(getDropScript()));
		tableViewer_property.setInput(datafilePropertyList);
		tableViewer_property.refresh();

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
			ExecuteDMLCommand.executeQuery(userDB, "SELECT 1 FROM SYS.DBA_DATA_FILES A,SYS.DBA_FREE_SPACE B,SYS.V_$TEMP_SPACE_HEADER C,SYS.V_$TEMP_EXTENT_POOL D,SYS.DBA_TEMP_FILES E,V$PARAMETER F WHERE 0=1", 0, 1);
		} catch (Exception e) {
			map_key = "getUserTablespaceList";
			//MessageDialog.openError(this.getSite().getShell(),CommonMessages.get().Error, "Tablespace 정보를 조회하기 위한 시스템 테이블 접근 권한이 부족합니다.\n" + e.getMessage() );
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
			ExceptionDetailsErrorDialog.openError(getSite().getShell(),CommonMessages.get().Error, e.getMessage(), errStatus); //$NON-NLS-1$
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

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new TableSpaceManagerLabelProvider(tableViewer));
	}

	private void createTableDataFileColumn() {

		TableViewColumnDefine[] tableColumnDef = new TableViewColumnDefine[] { new TableViewColumnDefine("FILE_NAME", "Datafile Name", 200, SWT.LEFT) //$NON-NLS-1$
				, new TableViewColumnDefine("FILE_ID", "File ID", 70, SWT.RIGHT) //$NON-NLS-1$
				, new TableViewColumnDefine("USAGE", "Usage", 100, SWT.RIGHT) //$NON-NLS-1$
				, new TableViewColumnDefine("TOTAL_SIZE", "Size", 100, SWT.RIGHT) //$NON-NLS-1$
				, new TableViewColumnDefine("USED", "Used", 80, SWT.RIGHT) //$NON-NLS-1$
				, new TableViewColumnDefine("FREE", "Free", 80, SWT.RIGHT) //$NON-NLS-1$
				, new TableViewColumnDefine("FRAG_INDEX", "Fragementation Index", 100, SWT.RIGHT) //$NON-NLS-1$
		};

		ColumnHeaderCreator.createColumnHeader(tableViewer_datafiles, tableColumnDef);

		tableViewer_datafiles.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer_datafiles.setLabelProvider(new TableSpaceManagerLabelProvider(tableViewer_datafiles));
	}

	@Override
	public void setFocus() {
		refreshTablespaceList();
	}

}
