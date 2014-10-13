package com.hangum.tadpole.rdb.core.dialog.table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.ext.sampledata.SampleDataConsts;
import com.hangum.tadpole.rdb.core.ext.sampledata.SampleDataEditingSupport;
import com.hangum.tadpole.sql.dao.mysql.TableDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.swtdesigner.ResourceManager;

/**
 * Table 수정 다이얼로그.
 * 
 * @author hangum
 *
 */

/** table view sample data */

public class AlterTableDialog extends Dialog {
	private List<AlterTableMetaDataDAO> listAlterTableColumns = new ArrayList<AlterTableMetaDataDAO>();

	TableViewer tableViewer;
	
	private UserDBDAO userDB;
	private TableDAO tableDao;
	private Text textSchemaName;
	private Text textTableName;
	private Table table;

	private AlterTableExecutor executor;
	
	private String schemaName;
	private String tableName;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	public AlterTableDialog(Shell parentShell, final UserDBDAO userDB) {
		super(parentShell);
		setShellStyle(SWT.SHELL_TRIM | SWT.BORDER);
		
		this.userDB = userDB;
	}

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AlterTableDialog(Shell parentShell, final UserDBDAO userDB, TableDAO tableDao) {
		super(parentShell);
		
		this.userDB = userDB;
		this.tableDao = tableDao;
		this.schemaName = "".equals(this.tableDao.getSchema_name()) ? userDB.getUsers() : this.tableDao.getSchema_name();
		this.tableName = "".equals(this.tableDao.getTable_name()) ? this.tableDao.getName() : this.tableDao.getTable_name();
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.makeColumnsEqualWidth = true;
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_composite.widthHint = 403;
		composite.setLayoutData(gd_composite);
		
		Label lblSchemeName = new Label(composite, SWT.NONE);
		lblSchemeName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSchemeName.setText("Schema Name");
		
		textSchemaName = new Text(composite, SWT.BORDER);
		textSchemaName.setText(this.schemaName);
		textSchemaName.setEditable(false);
		textSchemaName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTableName = new Label(composite, SWT.NONE);
		lblTableName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTableName.setText("Table Name");
		
		textTableName = new Text(composite, SWT.BORDER);
		textTableName.setText(this.tableName);
		textTableName.setEditable(false);
		textTableName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabItem tbtmColumns = new TabItem(tabFolder, SWT.NONE);
		tbtmColumns.setText("Columns");
		
	    tableViewer = new TableViewer(tabFolder, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbtmColumns.setControl(table);
		
		executor = new AlterTableExecutor(this.getParentShell(), listAlterTableColumns, userDB);
		this.listAlterTableColumns = executor.Initializing(this.tableName);

		createTaleColumn();
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new AlterTableLabelProvider());

		tableViewer.setInput(listAlterTableColumns);
		
		tableViewer.refresh();		
		
		TabItem tbtmConstraints = new TabItem(tabFolder, SWT.NONE);
		tbtmConstraints.setText("Constraints");
		
		Composite composite_1 = new Composite(container, SWT.NONE);
		GridLayout gl_composite_1 = new GridLayout(2, false);
		gl_composite_1.marginWidth = 2;
		gl_composite_1.marginHeight = 2;
		gl_composite_1.horizontalSpacing = 2;
		gl_composite_1.verticalSpacing = 2;
		composite_1.setLayout(gl_composite_1);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Button btnAddColumn = new Button(composite_1, SWT.NONE);
		btnAddColumn.setText("Add Column");
		
		Button btnDeleteColumn = new Button(composite_1, SWT.NONE);
		btnDeleteColumn.setText("Delete Column");

		return container;
	}

	/**
	 * create column
	 */
	private void createTaleColumn() {

		for (int i = 0; i < AlterTableConsts.sizes.length; i++) {
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			TableColumn tblclmnColumnName = tableViewerColumn.getColumn();
			tblclmnColumnName.setWidth(AlterTableConsts.sizes[i]);
			tblclmnColumnName.setText(AlterTableConsts.names[i]);

			tableViewerColumn.setEditingSupport(new AlterTableEditingSupport(tableViewer, i));
		}
	}


	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", false);
		createButton(parent, IDialogConstants.CANCEL_ID, "CANCEL", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(700, 600);
	}
}

