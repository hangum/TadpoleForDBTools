package com.hangum.tadpole.monitoring.core.editors.monitoring;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ArrayContentProvider;
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

import com.hangum.tadpole.monitoring.core.cache.MonitoringCacheRepository;
import com.hangum.tadpole.session.manager.SessionManager;
import com.hangum.tadpole.sql.dao.system.monitoring.MonitoringIndexDAO;

/**
 * Monitorng main Editor
 * 
 * @author hangum
 *
 */
public class MonitoringMainEditor extends EditorPart {
	private static final Logger logger = Logger.getLogger(MonitoringMainEditor.class);

	public static final String ID = "com.hangum.tadpole.monitoring.core.editor.main";
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

		SashForm sashForm = new SashForm(parent, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite compositeError = new Composite(sashForm, SWT.NONE);
		compositeError.setLayout(new GridLayout(1, false));
		compositeError.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		ToolBar toolBarError = new ToolBar(compositeError, SWT.FLAT | SWT.RIGHT);

		ToolItem tltmStop = new ToolItem(toolBarError, SWT.NONE);
		tltmStop.setText("Stop");

		tvError = new TableViewer(compositeError, SWT.BORDER | SWT.FULL_SELECTION);
		Table tableError = tvError.getTable();
		tableError.setHeaderVisible(true);
		tableError.setLinesVisible(true);
		tableError.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tvError, SWT.NONE);
		TableColumn tblclmnDbName = tableViewerColumn.getColumn();
		tblclmnDbName.setWidth(150);
		tblclmnDbName.setText("DB Name");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tvError, SWT.NONE);
		TableColumn tblclmnTitle = tableViewerColumn_2.getColumn();
		tblclmnTitle.setWidth(120);
		tblclmnTitle.setText("Title");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tvError, SWT.NONE);
		TableColumn tblclmnResultData = tableViewerColumn_1.getColumn();
		tblclmnResultData.setWidth(80);
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

		sashForm.setWeights(new int[] { 1 });

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

				while(true) {
					final List<MonitoringIndexDAO> listMonitoringIndex = instance.get(email);
					
					display.asyncExec(new Runnable() {
						@Override
						public void run() {
							if (!tvError.getTable().isDisposed()) {
								tvError.setInput(listMonitoringIndex);
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
	public void setFocus() {
	}
}