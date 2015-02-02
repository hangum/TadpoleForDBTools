package com.hangum.tadpole.monitoring.core.editors.monitoring;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.monitoring.core.dialogs.monitoring.ResultSetViewDialog;
import com.hangum.tadpole.monitoring.core.manager.cache.MonitoringCacheRepository;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringResultDAO;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * Monitoring main Editor
 * 
 * @author hangum
 *
 */
public class MonitoringMainEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(MonitoringMainEditor.class);

	public static final String ID = "com.hangum.tadpole.monitoring.core.editor.main";
	
	private boolean isThread = true;
	final ServerPushSession pushSession = new ServerPushSession();
	private TableViewer tvError;

	public MonitoringMainEditor() {
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

		MonitoringMainInput mmInput = (MonitoringMainInput) input;
		setPartName(mmInput.getName());
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
		parent.setLayout(new GridLayout(1, false));
		
		Composite compositeHead = new Composite(parent, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(compositeHead, SWT.FLAT | SWT.RIGHT);
		
		ToolItem tltmAddItem = new ToolItem(toolBar, SWT.NONE);
		tltmAddItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		tltmAddItem.setText("Add Item");

		SashForm sashFormBody = new SashForm(parent, SWT.VERTICAL);
		sashFormBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeChart = new Composite(sashFormBody, SWT.NONE);
		compositeChart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeChart.setLayout(new GridLayout(1, false));

		Composite compositeError = new Composite(sashFormBody, SWT.NONE);
		compositeError.setLayout(new GridLayout(1, false));
		compositeError.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		ToolBar toolBarError = new ToolBar(compositeError, SWT.FLAT | SWT.RIGHT);

		ToolItem tltmStop = new ToolItem(toolBarError, SWT.NONE);
		tltmStop.setText("Stop");

		tvError = new TableViewer(compositeError, SWT.BORDER | SWT.FULL_SELECTION);
		tvError.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection sel = (IStructuredSelection)event.getSelection();
				if(!sel.isEmpty()) {
					MonitoringResultDAO dao = (MonitoringResultDAO)sel.getFirstElement();
					ResultSetViewDialog dialog = new ResultSetViewDialog(null, dao);
					dialog.open();	
				}
			}
		});
		Table tableError = tvError.getTable();
		tableError.setHeaderVisible(true);
		tableError.setLinesVisible(true);
		tableError.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tvError, SWT.NONE);
		TableColumn tblclmnDbName = tableViewerColumn.getColumn();
		tblclmnDbName.setWidth(120);
		tblclmnDbName.setText("DB Name");
		
		TableViewerColumn tableViewerColumn_Err = new TableViewerColumn(tvError, SWT.NONE);
		TableColumn tblclmnErr = tableViewerColumn_Err.getColumn();
		tblclmnErr.setWidth(50);
		tblclmnErr.setText("is Error");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tvError, SWT.NONE);
		TableColumn tblclmnTitle = tableViewerColumn_2.getColumn();
		tblclmnTitle.setWidth(120);
		tblclmnTitle.setText("Title");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tvError, SWT.NONE);
		TableColumn tblclmnResultData = tableViewerColumn_1.getColumn();
		tblclmnResultData.setWidth(60);
		tblclmnResultData.setText("Value");

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tvError, SWT.NONE);
		TableColumn tblclmnCondition = tableViewerColumn_4.getColumn();
		tblclmnCondition.setWidth(100);
		tblclmnCondition.setText("Condition");

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tvError, SWT.NONE);
		TableColumn tblclmnResult = tableViewerColumn_3.getColumn();
		tblclmnResult.setWidth(500);
		tblclmnResult.setText("Result");

		tvError.setContentProvider(new ArrayContentProvider());
		tvError.setLabelProvider(new MonitoringErrorLabelprovider());

		sashFormBody.setWeights(new int[] { 7, 3 });

		callbackui();
	}

	private void callbackui() {
		pushSession.start();
		Thread thread = new Thread(startUIThread());
		thread.setDaemon(true);
		thread.start();
	}

	private Runnable startUIThread() {
		final String email = SessionManager.getEMAIL();
		final Display display = PlatformUI.getWorkbench().getDisplay();// tvError.getTable().getDisplay();
		final MonitoringCacheRepository instance = MonitoringCacheRepository.getInstance();

		Runnable bgRunnable = new Runnable() {
			@Override
			public void run() {

				while(isThread) {
					final List<MonitoringResultDAO> listMonitoringResult = instance.get(email);
					
					display.asyncExec(new Runnable() {
						@Override
						public void run() {
							if (!tvError.getTable().isDisposed()) {
								tvError.setInput(listMonitoringResult);
								tvError.refresh();
							}
						}
					});
					try{ Thread.sleep(2000); } catch(Exception e) {}
				}
			};
		};

		return bgRunnable;
	}
	
	@Override
	public void dispose() {
		super.dispose();

		isThread = false;
		pushSession.stop();
	}

	@Override
	public void setFocus() {
	}
}