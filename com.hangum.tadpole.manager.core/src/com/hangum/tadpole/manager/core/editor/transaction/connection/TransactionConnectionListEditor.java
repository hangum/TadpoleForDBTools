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

import java.util.List;

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
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.util.TadpoleWidgetUtils;
import com.hangum.tadpole.commons.util.GlobalImageUtils;
import com.hangum.tadpole.engine.manager.DBCPInfoDAO;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.manager.TadpoleSQLTransactionManager;
import com.hangum.tadpole.engine.manager.transaction.TransactionDAO;
import com.hangum.tadpole.manager.core.Activator;
import com.hangum.tadpole.manager.core.Messages;

/**
 * Transaction Connection List
 * 
 * @author hangum
 *
 */
public class TransactionConnectionListEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(TransactionConnectionListEditor.class);
	public static final String ID = "com.hangum.tadpole.manager.core.editor.transaction.connection.db"; //$NON-NLS-1$

	private TableViewer tvTransaction;
	private TransactioonTableComparator tableComparator;
	
	private ToolItem tltmCommit;
	private ToolItem tltmRollback;
	private TableViewer tvGeneral;

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

		// connection pool list
		CTabItem tbtmConnectionPool = new CTabItem(tabFolder, SWT.NONE);
		tbtmConnectionPool.setText(Messages.get().TransactionConnectionListEditor_0);
		
		Composite compositeConnectionPool = new Composite(tabFolder, SWT.NONE);
		tbtmConnectionPool.setControl(compositeConnectionPool);
		GridLayout gl_compositeConnectionPool = new GridLayout(1, false);
		gl_compositeConnectionPool.verticalSpacing = 1;
		gl_compositeConnectionPool.horizontalSpacing = 1;
		gl_compositeConnectionPool.marginHeight = 1;
		gl_compositeConnectionPool.marginWidth = 1;
		compositeConnectionPool.setLayout(gl_compositeConnectionPool);
		
		createConnectionPoolComposite(compositeConnectionPool);
		
		// transaction CTabItem widget create
		CTabItem tbtmTransactionConnection = new CTabItem(tabFolder, SWT.NONE);
		tbtmTransactionConnection.setText(Messages.get().TransactionConnectionListEditor_1);
		
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
		
		// google analytic
		AnalyticCaller.track(TransactionConnectionListEditor.ID);
	}
	
	/**
	 * connection pool composite
	 * 
	 * @param composte
	 */
	private void createConnectionPoolComposite(Composite compositeConnectionPool) {
		Composite compositeCToolbar = new Composite(compositeConnectionPool, SWT.NONE);
		compositeCToolbar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		GridLayout gl_compositeCToolbar = new GridLayout(1, false);
		gl_compositeCToolbar.verticalSpacing = 1;
		gl_compositeCToolbar.horizontalSpacing = 1;
		gl_compositeCToolbar.marginHeight = 1;
		gl_compositeCToolbar.marginWidth = 1;
		compositeCToolbar.setLayout(gl_compositeCToolbar);
		
		ToolBar toolBar = new ToolBar(compositeCToolbar, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		ToolItem tltmCRefresh = new ToolItem(toolBar, SWT.NONE);
		tltmCRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initGeneral();
			}
		});
		tltmCRefresh.setImage(GlobalImageUtils.getRefresh());
		tltmCRefresh.setToolTipText(Messages.get().Refresh);
		
		tvGeneral = new TableViewer(compositeConnectionPool, SWT.BORDER | SWT.FULL_SELECTION);
		Table tableCon = tvGeneral.getTable();
		tableCon.setLinesVisible(true);
		tableCon.setHeaderVisible(true);
		tableCon.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createGeneralColumns();
		
		tvGeneral.setContentProvider(new ArrayContentProvider());
		tvGeneral.setLabelProvider(new GeneralConnecionPoolLabelprovider());
		
		initGeneral();
	}
	
	/**
	 * 
	 */
	private void initGeneral() {
		List<DBCPInfoDAO> listDbcp = TadpoleSQLManager.getDBCPInfo();
		tvGeneral.setInput(listDbcp);
	}
	
	/**
	 * general table create columns
	 */
	private void createGeneralColumns() {
		String[] names = {Messages.get().TransactionConnectionListEditor_13, Messages.get().TransactionConnectionListEditor_14, 
				Messages.get().GeneralConnecionPoolLabelprovider_0, Messages.get().GeneralConnecionPoolLabelprovider_1, Messages.get().GeneralConnecionPoolLabelprovider_2, Messages.get().GeneralConnecionPoolLabelprovider_3};
		int[] sizes = {80, 200, 100, 100, 100, 100};
				
		for(int i=0; i<names.length; i++) {
			String name = names[i];
			int size = sizes[i];
			
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tvGeneral, SWT.NONE);
			TableColumn tblclmnEngine = tableViewerColumn.getColumn();
			tblclmnEngine.setWidth(size);
			tblclmnEngine.setText(name);
			
			tblclmnEngine.addSelectionListener(getSelectionAdapter(tblclmnEngine, i));
		}
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
		tltmRefresh.setImage(GlobalImageUtils.getRefresh());
		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initTransactionUI();
			}
		});
		tltmRefresh.setToolTipText(Messages.get().Refresh);
		
		tltmCommit = new ToolItem(toolBar, SWT.NONE);
		tltmCommit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!tvTransaction.getSelection().isEmpty()) {
					if(!MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().TransactionConnectionListEditor_5)) return;
					
					executTransaction(true);
				}
			}
		});
		tltmCommit.setEnabled(false);
		tltmCommit.setText(Messages.get().TransactionConnectionListEditor_6);
		
		tltmRollback = new ToolItem(toolBar, SWT.NONE);
		tltmRollback.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!tvTransaction.getSelection().isEmpty()) {
					if(!MessageDialog.openConfirm(null, Messages.get().Confirm, Messages.get().TransactionConnectionListEditor_8)) return;
					
					executTransaction(false);
				}
			}
		});
		tltmRollback.setEnabled(false);
		tltmRollback.setText(Messages.get().TransactionConnectionListEditor_9);
		
		Composite compositeBody = new Composite(compositeTransactionConnection, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBody = new GridLayout(1, false);
		gl_compositeBody.verticalSpacing = 1;
		gl_compositeBody.horizontalSpacing = 1;
		gl_compositeBody.marginHeight = 1;
		gl_compositeBody.marginWidth = 1;
		compositeBody.setLayout(gl_compositeBody);
		
		tvTransaction = new TableViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		tvTransaction.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				transactionBtnInit(true);
			}
		});
		Table table = tvTransaction.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// sorter
		tableComparator = new TransactioonTableComparator();
		tvTransaction.setSorter(tableComparator);
		tableComparator.setColumn(0);
		
		createTransactionColumns();
		
		tvTransaction.setContentProvider(new ArrayContentProvider());
		tvTransaction.setLabelProvider(new TransactionConnectionListLabelProvider());
		
		initTransactionUI();
	}
	
	/**
	 * execute transaction 
	 * 
	 * @param isCommit
	 */
	private void executTransaction(boolean isCommit) {
		IStructuredSelection iss = (IStructuredSelection)tvTransaction.getSelection();
		TransactionDAO tdao = (TransactionDAO)iss.getFirstElement();
		
		try {
			if(isCommit) TadpoleSQLTransactionManager.commit(tdao.getUserId(), tdao.getUserDB());
			else TadpoleSQLTransactionManager.rollback(tdao.getUserId(), tdao.getUserDB());
			
			initTransactionUI();
		} catch (Exception e1) {
			logger.error("Transaction is commit " + isCommit + "[user db]" +  tdao.getUserDB(), e1); //$NON-NLS-1$ //$NON-NLS-2$
			
			Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e1.getMessage(), e1); //$NON-NLS-1$
			ExceptionDetailsErrorDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().Refresh, errStatus); //$NON-NLS-1$
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
		tvTransaction.setInput(TadpoleSQLTransactionManager.getDbManager().values());
	}
	
	/**
	 * transaction table create columns
	 */
	private void createTransactionColumns() {
		String[] names = {Messages.get().TransactionConnectionListEditor_13, Messages.get().TransactionConnectionListEditor_14, Messages.get().User, Messages.get().TransactionConnectionListEditor_16};
		int[] sizes = {80, 200, 200, 200};
				
		for(int i=0; i<names.length; i++) {
			String name = names[i];
			int size = sizes[i];
			
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tvTransaction, SWT.NONE);
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
				tvTransaction.getTable().setSortDirection(tableComparator.getDirection());
				tvTransaction.getTable().setSortColumn(column);
				tvTransaction.refresh();
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
