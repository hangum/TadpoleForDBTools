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
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
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
	private TableComparator tableComparator;
	
	private ToolItem tltmCommit;
	private ToolItem tltmRollback;

	public TransactionConnectionListEditor() {
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

	@Override
	public void createPartControl(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.verticalSpacing = 1;
		gl_parent.horizontalSpacing = 1;
		gl_parent.marginHeight = 1;
		gl_parent.marginWidth = 1;
		parent.setLayout(gl_parent);
		
		Composite compositeToolbar = new Composite(parent, SWT.NONE);
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
				initUI();
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
		
		Composite compositeBody = new Composite(parent, SWT.NONE);
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
		tableComparator = new TableComparator();
		tableViewer.setSorter(tableComparator);
		tableComparator.setColumn(0);
		
		createColumns();
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new TransactionConnectionListLabelProvider());
		
		initUI();
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
			
			initUI();
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
	private void initUI() {
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

}

/**
 * Transaction Connection Manager Labelprovider
 * @author hangum
 *
 */
class TransactionConnectionListLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		TransactionDAO dto = (TransactionDAO)element;

		switch(columnIndex) {
		case 0: return dto.getUserDB().getDbms_types();
		case 1: return dto.getUserDB().getDisplay_name();
		case 2: return dto.getUserId();
		case 3: return dto.getStartTransaction().toLocaleString();
		}
		
		return "*** not set column ***"; //$NON-NLS-1$
	}
	
}

/**
 * compartor
 * 
 * @author hangum
 *
 */
class TableComparator extends ViewerSorter  {
	protected int propertyIndex;
	protected static final int DESCENDING = 1;
	protected static final int ASCENDING = -1;
	protected int direction = DESCENDING;
	
	public TableComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	public int getDirection() {
		return direction == 1 ? SWT.DOWN : SWT.UP;
	}
	
	public void setColumn(int column) {
		if(column == this.propertyIndex) {
			direction = 1 - direction;
		} else {
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}
	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		TransactionDAO dao1 = (TransactionDAO) e1;
		TransactionDAO dao2 = (TransactionDAO) e2;
		
		int rc = ASCENDING;
		switch (this.propertyIndex) {
		case 0:
			rc = dao1.getUserDB().getDbms_types().toLowerCase().compareTo(dao2.getUserDB().getDbms_types().toLowerCase());
			break;
		case 1:
			rc = dao1.getUserDB().getDisplay_name().toLowerCase().compareTo(dao2.getUserDB().getDisplay_name().toLowerCase());
			break;
		case 2:
			rc = dao1.getUserId().toLowerCase().compareTo(dao2.getUserId().toLowerCase());
			break;
		case 3:
			rc = dao1.getStartTransaction().toLocaleString().compareTo(dao2.getStartTransaction().toLocaleString().toLowerCase());
			break;
		}
		
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}