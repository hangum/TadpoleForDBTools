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
package com.hangum.tadpole.manager.core.editor.transaction.connection;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.util.ImageUtils;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.engine.transaction.TransactionDAO;
import com.hangum.tadpole.manager.core.Activator;

/**
 * Transaction Connection List
 * 
 * @author hangum
 *
 */
public class TransactionConnectionListEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(TransactionConnectionListEditor.class);
	public static final String ID = "com.hangum.tadpole.manager.core.editor.transaction.connection.db";

	private TableViewer tableViewer;
	private TransactioonTableComparator tableComparator;
	
	private ToolItem tltmCommit;
	private ToolItem tltmRollback;
	private Table tableCon;

	public TransactionConnectionListEditor() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 1;
		gl_parent.horizontalSpacing = 1;
		gl_parent.marginHeight = 1;
		gl_parent.marginWidth = 1;
		parent.setLayout(gl_parent);
		
		CTabFolder tabFolder = new CTabFolder(parent, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder.setBorderVisible(false);		
		tabFolder.setSelectionBackground(TadpoleWidgetUtils.getTabFolderBackgroundColor(), TadpoleWidgetUtils.getTabFolderPercents());
		
//		CTabItem tbtmConnectionPool = new CTabItem(tabFolder, SWT.NONE);
//		tbtmConnectionPool.setText("Connection Pool");
//		
//		Composite compositeConnectionPool = new Composite(tabFolder, SWT.NONE);
//		tbtmConnectionPool.setControl(compositeConnectionPool);
//		GridLayout gl_compositeConnectionPool = new GridLayout(1, false);
//		gl_compositeConnectionPool.verticalSpacing = 1;
//		gl_compositeConnectionPool.horizontalSpacing = 1;
//		gl_compositeConnectionPool.marginHeight = 1;
//		gl_compositeConnectionPool.marginWidth = 1;
//		compositeConnectionPool.setLayout(gl_compositeConnectionPool);
//		
//		Composite compositeCToolbar = new Composite(compositeConnectionPool, SWT.NONE);
//		compositeCToolbar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
//		GridLayout gl_compositeCToolbar = new GridLayout(1, false);
//		gl_compositeCToolbar.verticalSpacing = 1;
//		gl_compositeCToolbar.horizontalSpacing = 1;
//		gl_compositeCToolbar.marginHeight = 1;
//		gl_compositeCToolbar.marginWidth = 1;
//		compositeCToolbar.setLayout(gl_compositeCToolbar);
//		
//		ToolBar toolBar = new ToolBar(compositeCToolbar, SWT.FLAT | SWT.RIGHT);
//		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		
//		ToolItem tltmCRefresh = new ToolItem(toolBar, SWT.NONE);
//		tltmCRefresh.setText("Refresh");
//		
//		TableViewer tableViewerCon = new TableViewer(compositeConnectionPool, SWT.BORDER | SWT.FULL_SELECTION);
//		tableCon = tableViewerCon.getTable();
//		tableCon.setLinesVisible(true);
//		tableCon.setHeaderVisible(true);
//		tableCon.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		
		
		// transaction CTabItem widget create
		CTabItem tbtmTransactionConnection = new CTabItem(tabFolder, SWT.NONE);
		tbtmTransactionConnection.setText("Transaction Connection");
		
		Composite compositeTransactionConnection = new Composite(tabFolder, SWT.NONE);
		tbtmTransactionConnection.setControl(compositeTransactionConnection);
		GridLayout gl_compositeTransactionConnection = new GridLayout(1, false);
		gl_compositeTransactionConnection.verticalSpacing = 1;
		gl_compositeTransactionConnection.horizontalSpacing = 1;
		gl_compositeTransactionConnection.marginHeight = 1;
		gl_compositeTransactionConnection.marginWidth = 1;
		compositeTransactionConnection.setLayout(gl_compositeTransactionConnection);
		
		// create transaction composite
		createTransactionComposite(compositeTransactionConnection);
		
		// default folder selection
		tabFolder.setSelection(0);
	}
	
	/**
	 * create transaction composite
	 * 
	 * @param compositeTransactionConnection
	 */
	private void createTransactionComposite(Composite compositeTransactionConnection) {
		Composite compositeToolbar = new Composite(compositeTransactionConnection, SWT.NONE);
		compositeToolbar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeToolbar = new GridLayout(1, false);
		gl_compositeToolbar.verticalSpacing = 1;
		gl_compositeToolbar.horizontalSpacing = 1;
		gl_compositeToolbar.marginHeight = 1;
		gl_compositeToolbar.marginWidth = 1;
		compositeToolbar.setLayout(gl_compositeToolbar);
		
		ToolBar toolBar = new ToolBar(compositeToolbar, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmRefresh.setImage(ImageUtils.getRefresh());
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initTransactionUI();
			}
		});
		tltmRefresh.setToolTipText("Refresh");
		
		tltmCommit = new ToolItem(toolBar, SWT.NONE);
		tltmCommit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!tableViewer.getSelection().isEmpty()) {
					if(!MessageDialog.openConfirm(null, "Confirm", "Do you want DB Commit?")) return;
					
					executTransaction(true);
				}
			}
		});
		tltmCommit.setEnabled(false);
		tltmCommit.setText("Commit");
		
		tltmRollback = new ToolItem(toolBar, SWT.NONE);
		tltmRollback.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!tableViewer.getSelection().isEmpty()) {
					if(!MessageDialog.openConfirm(null, "Confirm", "Do you want DB Rollback?")) return;
					
					executTransaction(false);
				}
			}
		});
		tltmRollback.setEnabled(false);
		tltmRollback.setText("Rollback");
		
		Composite compositeBody = new Composite(compositeTransactionConnection, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 1;
		gl_compositeBody.horizontalSpacing = 1;
		gl_compositeBody.marginHeight = 1;
		gl_compositeBody.marginWidth = 1;
		compositeBody.setLayout(gl_compositeBody);
		
		tableViewer = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				transactionBtnInit(true);
			}
		});
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// sorter
		tableComparator = new TransactioonTableComparator();
		tableViewer.setSorter(tableComparator);
		tableComparator.setColumn(0);
		
		createColumns();
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new TransactionConnectionListLabelProvider());
		
		initTransactionUI();
	}
	
	/**
	 * execute transaction 
	 * 
	 * @param isCommit
	 */
	private void executTransaction(boolean isCommit) {
		IStructuredSelection iss = (IStructuredSelection)tableViewer.getSelection();
		TransactionDAO tdao = (TransactionDAO)iss.getFirstElement();
		
		try {
			if(isCommit) TadpoleSQLTransactionManager.commit(tdao.getUserId(), tdao.getUserDB());
			else TadpoleSQLTransactionManager.rollback(tdao.getUserId(), tdao.getUserDB());
			
			initTransactionUI();
		} catch (Exception e1) {
			logger.error("Transaction is commit " + isCommit + "[user db]" +  tdao.getUserDB(), e1);
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e1.getMessage(), e1); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), "Error", "Transaction fail", errStatus); //$NON-NLS-1$
		}
	}
	
	/**
	 * Initialize toolbar button 
	 * 
	 * @param isEnable
	 */
	private void transactionBtnInit(boolean isEnable) {
		tltmCommit.setEnabled(isEnable);
		tltmRollback.setEnabled(isEnable);
	}
	
	/**
	 * transaction dao list
	 */
	private void initTransactionUI() {
		transactionBtnInit(false);
		tableViewer.setInput(TadpoleSQLTransactionManager.getDbManager().values());
	}
	
	/**
	 * create columns
	 */
	private void createColumns() {
		String[] names = {"DB Type", "Display Name", "User", "Start Connectin"};
		int[] sizes = {80, 200, 200, 200};
				
		for(int i=0; i<names.length; i++) {
			String name = names[i];
			int size = sizes[i];
			
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			TableColumn tblclmnEngine = tableViewerColumn.getColumn();
			tblclmnEngine.setWidth(size);
			tblclmnEngine.setText(name);
			
			tblclmnEngine.addSelectionListener(getSelectionAdapter(tblclmnEngine, i));
		}
	}
	
	/**
	 * table sorter
	 * 
	 * @param comparator
	 * @param viewer
	 * @param column
	 * @param index
	 * @return
	 */
	private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
		SelectionAdapter adapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableComparator.setColumn(index);
				tableViewer.getTable().setSortDirection(tableComparator.getDirection());
				tableViewer.getTable().setSortColumn(column);
				tableViewer.refresh();
			}
		};
		
		return adapter;
	}

	@Override
	public void setFocus() {
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

		TransactionConnectionListEditorInput esqli = (TransactionConnectionListEditorInput) input;
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
}
