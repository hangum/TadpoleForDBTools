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
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.sysnonym;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpold.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateViewDDLAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDeleteAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectExecuteProcedureAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.ColumnHeaderCreator;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultLabelProvider;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultTableColumnFilter;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.TableViewColumnDefine;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.hangum.tadpole.sql.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.sql.dao.rdb.OracleSynonymColumnDAO;
import com.hangum.tadpole.sql.dao.rdb.OracleSynonymDAO;
import com.hangum.tadpole.sql.dao.system.UserDBDAO;
import com.hangum.tadpole.sql.util.sqlscripts.scripts.AbstractRDBDDLScript;
import com.hangum.tadpole.sql.util.sqlscripts.scripts.OracleDDLScript;
import com.hangum.tadpole.sql.util.tables.TableUtil;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * RDB table composite
 * 
 * @author hangum
 * 
 */
public class TadpoleSynonymComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleSynonymComposite.class);

	/** select synonym name */
	private String selectSynonymName = ""; //$NON-NLS-1$

	// table info
	private TableViewer synonymListViewer;
	private List<OracleSynonymDAO> showSynonyms = new ArrayList<OracleSynonymDAO>();
	private DefaultTableColumnFilter synonymFilter;

	// column info
	private TableViewer synonymColumnViewer;
	private List<OracleSynonymColumnDAO> showSynonymColumns;

	private AbstractObjectAction deleteAction_Synonym;
	private AbstractObjectAction refreshAction_Synonym;
	private GenerateViewDDLAction viewDDLAction;
	private ObjectExecuteProcedureAction executeAction;

	/**
	 * Create the composite.
	 * 
	 * @param partSite
	 * @param parent
	 * @param userDB
	 */
	public TadpoleSynonymComposite(IWorkbenchPartSite partSite, final CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(partSite, tabFolderObject, userDB);

		createWidget(tabFolderObject);
	}

	private void createWidget(final CTabFolder tabFolderObject) {
		CTabItem tbtmTable = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmTable.setText("Synonym"); //$NON-NLS-1$

		Composite compositeTables = new Composite(tabFolderObject, SWT.NONE);
		tbtmTable.setControl(compositeTables);
		GridLayout gl_compositeTables = new GridLayout(1, false);
		gl_compositeTables.verticalSpacing = 2;
		gl_compositeTables.horizontalSpacing = 2;
		gl_compositeTables.marginHeight = 2;
		gl_compositeTables.marginWidth = 2;
		compositeTables.setLayout(gl_compositeTables);
		compositeTables.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		SashForm sashForm = new SashForm(compositeTables, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// SWT.VIRTUAL 일 경우 FILTER를 적용하면 데이터가 보이지 않는 오류수정.
		synonymListViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
		synonymListViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					if (null != is) {
						OracleSynonymDAO tableDAO = (OracleSynonymDAO) is.getFirstElement();

						if (PublicTadpoleDefine.DB_ACTION.FUNCTIONS.toString().startsWith(tableDAO.getObject_type())) {
							AbstractRDBDDLScript rdbScript = new OracleDDLScript(userDB, PublicTadpoleDefine.DB_ACTION.FUNCTIONS);
							ProcedureFunctionDAO dao = new ProcedureFunctionDAO();
							dao.setName(tableDAO.getTable_name());

							FindEditorAndWriteQueryUtil.run(userDB, rdbScript.getFunctionScript(dao));
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		synonymListViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					Object synonymDAO = is.getFirstElement();

					if (synonymDAO != null) {
						OracleSynonymDAO synonym = (OracleSynonymDAO) synonymDAO;

						if (selectSynonymName.equals(synonym.getSynonym_name()))
							return;
						selectSynonymName = synonym.getSynonym_name();

						SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
						Map<String, String> mapParam = new HashMap<String, String>();
						mapParam.put("db", userDB.getDb());
						mapParam.put("owner", synonym.getTable_owner());
						mapParam.put("table", synonym.getTable_name());

						showSynonymColumns = sqlClient.queryForList("synonymColumnList", mapParam); //$NON-NLS-1$

					} else
						showSynonymColumns = null;

					synonymColumnViewer.setInput(showSynonymColumns);
					synonymColumnViewer.refresh();

				} catch (Exception e) {
					logger.error("get table column", e); //$NON-NLS-1$

					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(tabFolderObject.getShell(), "Error", e.getMessage(), errStatus); //$NON-NLS-1$
				}
			}
		});

		Table tableTableList = synonymListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		createSynonymMenu();

		// columns
		synonymColumnViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		synonymColumnViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection is = (IStructuredSelection) event.getSelection();

				if (null != is) {
					OracleSynonymColumnDAO synonymDAO = (OracleSynonymColumnDAO) is.getFirstElement();
					FindEditorAndWriteQueryUtil.runAtPosition(synonymDAO.getColumn_name());
				}
			}
		});
		Table tableTableColumn = synonymColumnViewer.getTable();
		tableTableColumn.setHeaderVisible(true);
		tableTableColumn.setLinesVisible(true);

		createSysnonymListColumne();
		createSynonymDetailColumne();

		synonymListViewer.setInput(showSynonyms);
		synonymListViewer.refresh();

		synonymFilter = new DefaultTableColumnFilter();
		synonymListViewer.addFilter(synonymFilter);

		sashForm.setWeights(new int[] { 1, 1 });
	}

	private void createSysnonymListColumne() {

		TableViewColumnDefine[] tableColumnDef = new TableViewColumnDefine[] { //
		new TableViewColumnDefine("SYNONYM_NAME", "Name", 100, SWT.LEFT) //
				, new TableViewColumnDefine("TABLE_OWNER", "Owner", 60, SWT.LEFT) //
				, new TableViewColumnDefine("OBJECT_TYPE", "Type", 70, SWT.CENTER) //
				, new TableViewColumnDefine("TABLE_NAME", "Object Name", 100, SWT.LEFT) //
				, new TableViewColumnDefine("COMMENTS", "Comments", 100, SWT.LEFT) //
		};

		ColumnHeaderCreator.createColumnHeader(synonymListViewer, tableColumnDef);

		synonymListViewer.setContentProvider(new ArrayContentProvider());
		synonymListViewer.setLabelProvider(new DefaultLabelProvider(synonymListViewer));

	}

	private void createSynonymDetailColumne() {
		TableViewColumnDefine[] tableColumnDef = new TableViewColumnDefine[] { //
		new TableViewColumnDefine("COLUMN_ID", "No", 50, SWT.RIGHT) //
				, new TableViewColumnDefine("COLUMN_NAME", "Name", 100, SWT.LEFT) //
				, new TableViewColumnDefine("DATA_TYPE", "Type", 80, SWT.CENTER) //
				, new TableViewColumnDefine("NULLABLE", "Null", 60, SWT.CENTER) //
				, new TableViewColumnDefine("KEY", "Key", 50, SWT.CENTER) //
				, new TableViewColumnDefine("COMMENTS", "Comments", 100, SWT.LEFT) //
		};

		ColumnHeaderCreator.createColumnHeader(synonymColumnViewer, tableColumnDef);

		synonymColumnViewer.setContentProvider(new ArrayContentProvider());
		synonymColumnViewer.setLabelProvider(new DefaultLabelProvider(synonymColumnViewer));

	}

	/**
	 * create Table menu
	 */
	private void createSynonymMenu() {
		deleteAction_Synonym = new ObjectDeleteAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.SYNONYM, "Synonym"); //$NON-NLS-1$
		refreshAction_Synonym = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.SYNONYM, "Synonym"); //$NON-NLS-1$
		executeAction = new ObjectExecuteProcedureAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.SYNONYM, "Synonym");
		viewDDLAction = new GenerateViewDDLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.DB_ACTION.SYNONYM, "View"); //$NON-NLS-1$

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu", "Synonym"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (userDB != null) {
					IStructuredSelection is = (IStructuredSelection) synonymListViewer.getSelection();

					manager.add(deleteAction_Synonym);
					manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
					manager.add(refreshAction_Synonym);

					if (!is.isEmpty()) {
						manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
						OracleSynonymDAO synonymDAO = (OracleSynonymDAO) is.getFirstElement();
						if (synonymDAO.getObject_type().startsWith("PROCEDURE") || synonymDAO.getObject_type().startsWith("FUNCTION")) {
							viewDDLAction.setEnabled(true);
							executeAction.setEnabled(true);
							manager.add(viewDDLAction);
							manager.add(executeAction);
						} else if (synonymDAO.getObject_type().startsWith("PACKAGE") || synonymDAO.getObject_type().startsWith("VIEW")
								|| synonymDAO.getObject_type().startsWith("TABLE")) {
							viewDDLAction.setEnabled(true);
							manager.add(viewDDLAction);
						}
					}

				}
			}
		});

		synonymListViewer.getTable().setMenu(menuMgr.createContextMenu(synonymListViewer.getTable()));
		getSite().registerContextMenu(menuMgr, synonymListViewer);
	}

	/**
	 * 정보를 최신으로 리프레쉬합니다.
	 */
	public void refreshSynonym(final UserDBDAO selectUserDb, boolean boolRefresh) {
		if (!boolRefresh)
			if (selectUserDb == null)
				return;
		this.userDB = selectUserDb;

		Job job = new Job(Messages.MainEditor_45) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Connect database", IProgressMonitor.UNKNOWN);

				try {
					showSynonyms = getSynonymList(userDB);
				} catch (Exception e) {
					logger.error("Synonym Referesh", e);

					return new Status(Status.WARNING, Activator.PLUGIN_ID, e.getMessage());
				} finally {
					monitor.done();
				}

				return Status.OK_STATUS;
			}
		};

		job.addJobChangeListener(new JobChangeAdapter() {

			public void done(IJobChangeEvent event) {
				final IJobChangeEvent jobEvent = event;

				getSite().getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (jobEvent.getResult().isOK()) {
							synonymListViewer.setInput(showSynonyms);
							synonymListViewer.refresh();
							TableUtil.packTable(synonymListViewer.getTable());

						} else {
							if (showSynonyms != null)
								showSynonyms.clear();
							synonymListViewer.setInput(showSynonyms);
							synonymListViewer.refresh();
							TableUtil.packTable(synonymListViewer.getTable());

							Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, jobEvent.getResult().getMessage(), jobEvent.getResult()
									.getException()); //$NON-NLS-1$
							ExceptionDetailsErrorDialog.openError(null, "Error", Messages.ExplorerViewer_86, errStatus); //$NON-NLS-1$
						}
					}
				}); // end display.asyncExec
			} // end done

		}); // end job

		job.setName(userDB.getDisplay_name());
		job.setUser(true);
		job.schedule();
	}

	/**
	 * 보여 주어야할 목록을 정의합니다.
	 * 
	 * @param userDB
	 * @return
	 * @throws Exception
	 */
	public static List<OracleSynonymDAO> getSynonymList(final UserDBDAO userDB) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("synonymList", userDB.getDb()); //$NON-NLS-1$
	}

	/**
	 * initialize action
	 */
	public void initAction() {
		deleteAction_Synonym.setUserDB(getUserDB());
		refreshAction_Synonym.setUserDB(getUserDB());
		executeAction.setUserDB(getUserDB());
		viewDDLAction.setUserDB(getUserDB());
	}

	/**
	 * get synonymViewer
	 * 
	 * @return
	 */
	public TableViewer getSynonymListViewer() {
		return synonymListViewer;
	}

	/**
	 * get synonym column viewer
	 * 
	 * @return
	 */
	public TableViewer getSynonymColumnViewer() {
		return synonymColumnViewer;
	}

	/**
	 * initialize filter text
	 * 
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		synonymFilter.setSearchString(textSearch);
		synonymListViewer.refresh();
	}

	@Override
	public void dispose() {
		super.dispose();
		deleteAction_Synonym.dispose();
		refreshAction_Synonym.dispose();
		viewDDLAction.dispose();
		executeAction.dispose();
	}

	@Override
	public void setSearchText(String searchText) {
		synonymFilter.setSearchString(searchText);
	}
}
