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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hangum.tadpole.commons.exception.dialog.ExceptionDetailsErrorDialog;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.mysql.ProcedureFunctionDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleSynonymColumnDAO;
import com.hangum.tadpole.engine.query.dao.rdb.OracleSynonymDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.sqlscripts.scripts.AbstractRDBDDLScript;
import com.hangum.tadpole.engine.sql.util.sqlscripts.scripts.OracleDDLScript;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateViewDDLAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDropAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectExecuteProcedureAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.ColumnHeaderCreator;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultLabelProvider;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultTableColumnFilter;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.TableViewColumnDefine;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.comparator.SynonymColumnComparator;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * RDB synonym composite
 * 
 * @author hangum
 * 
 */
public class TadpoleSynonymComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleSynonymComposite.class);

	private CTabItem tbtmSynonym;
	/** select synonym name */
	private String selectSynonymName = ""; //$NON-NLS-1$

	// table info
	private TableViewer synonymListViewer;
	private List<OracleSynonymDAO> showSynonyms = new ArrayList<OracleSynonymDAO>();
	private DefaultTableColumnFilter synonymFilter;

	// column info
	private TableViewer synonymColumnViewer;
	private List<OracleSynonymColumnDAO> showSynonymColumns;
	private SynonymColumnComparator synonymColumnComparator;

	private AbstractObjectAction dropAction_Synonym;
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
		tbtmSynonym = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmSynonym.setText(Messages.get().Synonym);
		tbtmSynonym.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.SYNONYM.name());

		Composite compositeTables = new Composite(tabFolderObject, SWT.NONE);
		tbtmSynonym.setControl(compositeTables);
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

						if (PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS.toString().startsWith(tableDAO.getObject_type())) {
							AbstractRDBDDLScript rdbScript = new OracleDDLScript(userDB, PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS);
							ProcedureFunctionDAO dao = new ProcedureFunctionDAO();
							dao.setName(tableDAO.getTable_name());

							FindEditorAndWriteQueryUtil.run(userDB, rdbScript.getFunctionScript(dao), PublicTadpoleDefine.OBJECT_TYPE.FUNCTIONS);
						}
					}
				} catch (Exception e) {
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
						selectSynonymName = synonym.getSynonym_name();

						SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
						Map<String, String> mapParam = new HashMap<String, String>();
						mapParam.put("db", userDB.getDb()); //$NON-NLS-1$
						mapParam.put("owner", synonym.getTable_owner()); //$NON-NLS-1$
						mapParam.put("table", synonym.getTable_name()); //$NON-NLS-1$

						showSynonymColumns = sqlClient.queryForList("synonymColumnList", mapParam); //$NON-NLS-1$

					} else {
						showSynonymColumns = null;
					}

					synonymColumnViewer.setInput(showSynonymColumns);
					synonymColumnViewer.refresh();

				} catch (Exception e) {
					logger.error("get table column", e); //$NON-NLS-1$

					Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e); //$NON-NLS-1$
					ExceptionDetailsErrorDialog.openError(tabFolderObject.getShell(), Messages.get().Error, e.getMessage(), errStatus); //$NON-NLS-1$
				}
			}
		});

		Table tableTableList = synonymListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		createSynonymMenu();
		createSysnonymListColumne();

		synonymListViewer.setInput(showSynonyms);
		synonymListViewer.refresh();

		synonymFilter = new DefaultTableColumnFilter();
		synonymListViewer.addFilter(synonymFilter);

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
		
		createSynonymDetailColumne();
		
		synonymColumnComparator = new SynonymColumnComparator();
		synonymColumnViewer.setSorter(synonymColumnComparator);
		synonymColumnComparator.setColumn(0);
		
		sashForm.setWeights(new int[] { 1, 1 });
	}

	private void createSysnonymListColumne() {
		TableViewColumnDefine[] tableColumnDef = new TableViewColumnDefine[] { //
		new TableViewColumnDefine("SYNONYM_NAME", Messages.get().TadpoleSynonymComposite_5, 100, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("TABLE_OWNER", Messages.get().TadpoleSynonymComposite_7, 60, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("OBJECT_TYPE", Messages.get().Type, 70, SWT.CENTER) // //$NON-NLS-1$
				, new TableViewColumnDefine("TABLE_NAME", Messages.get().TadpoleSynonymComposite_11, 100, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("COMMENTS", Messages.get().Comment, 100, SWT.LEFT) // //$NON-NLS-1$
		};

		ColumnHeaderCreator.createColumnHeader(synonymListViewer, tableColumnDef);

		synonymListViewer.setContentProvider(new ArrayContentProvider());
		synonymListViewer.setLabelProvider(new DefaultLabelProvider(synonymListViewer));

	}

	private void createSynonymDetailColumne() {
		TableViewColumnDefine[] tableColumnDef = new TableViewColumnDefine[] { //
		new TableViewColumnDefine("COLUMN_ID", Messages.get().SEQ, 50, SWT.RIGHT) // //$NON-NLS-1$
				, new TableViewColumnDefine("COLUMN_NAME", Messages.get().Name, 100, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("DATA_TYPE", Messages.get().DataType, 80, SWT.CENTER) // //$NON-NLS-1$
				, new TableViewColumnDefine("NULLABLE", Messages.get().TadpoleSynonymComposite_21, 60, SWT.CENTER) // //$NON-NLS-1$
				, new TableViewColumnDefine("KEY", Messages.get().TadpoleSynonymComposite_23, 50, SWT.CENTER) // //$NON-NLS-1$
				, new TableViewColumnDefine("COMMENTS", Messages.get().TadpoleSynonymComposite_25, 100, SWT.LEFT) // //$NON-NLS-1$
		};

		ColumnHeaderCreator.createColumnHeader(synonymColumnViewer, synonymColumnComparator, tableColumnDef);

		synonymColumnViewer.setContentProvider(new ArrayContentProvider());
		synonymColumnViewer.setLabelProvider(new DefaultLabelProvider(synonymColumnViewer));
	}

	/**
	 * create Table menu
	 */
	private void createSynonymMenu() {
		if(getUserDB() == null) return;
		
		dropAction_Synonym = new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.SYNONYM, Messages.get().DropSynonym); //$NON-NLS-1$
		refreshAction_Synonym = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.SYNONYM, Messages.get().Refresh); //$NON-NLS-1$
		executeAction = new ObjectExecuteProcedureAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.SYNONYM, Messages.get().Execute); //$NON-NLS-1$
		viewDDLAction = new GenerateViewDDLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.SYNONYM, Messages.get().ViewDDL); //$NON-NLS-1$

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu", "Synonym"); //$NON-NLS-1$ //$NON-NLS-2$
		if(!isDDLLock()) {
			menuMgr.add(dropAction_Synonym);
			menuMgr.add(new Separator());
		}
		menuMgr.add(refreshAction_Synonym);
		
		IStructuredSelection is = (IStructuredSelection) synonymListViewer.getSelection();
		if (!is.isEmpty()) {
			menuMgr.add(new Separator());
			OracleSynonymDAO synonymDAO = (OracleSynonymDAO) is.getFirstElement();
			if (synonymDAO.getObject_type().startsWith("PROCEDURE") || synonymDAO.getObject_type().startsWith("FUNCTION")) { //$NON-NLS-1$ //$NON-NLS-2$
				viewDDLAction.setEnabled(true);
				executeAction.setEnabled(true);
				menuMgr.add(viewDDLAction);
				menuMgr.add(executeAction);
			} else if (synonymDAO.getObject_type().startsWith("PACKAGE") || synonymDAO.getObject_type().startsWith("VIEW") //$NON-NLS-1$ //$NON-NLS-2$
					|| synonymDAO.getObject_type().startsWith("TABLE")) { //$NON-NLS-1$
				viewDDLAction.setEnabled(true);
				menuMgr.add(viewDDLAction);
			}
		}

		synonymListViewer.getTable().setMenu(menuMgr.createContextMenu(synonymListViewer.getTable()));
		getSite().registerContextMenu(menuMgr, synonymListViewer);
	}

	/**
	 * 정보를 최신으로 리프레쉬합니다.
	 * @param strObjectName 
	 */
	public void refreshSynonym(final UserDBDAO selectUserDb, final boolean boolRefresh, final String strObjectName) {
		if (!boolRefresh) if (selectUserDb == null) return;
		this.userDB = selectUserDb;

		Job job = new Job(Messages.get().MainEditor_45) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Connect database", IProgressMonitor.UNKNOWN); //$NON-NLS-1$

				try {
					showSynonyms = getSynonymList(userDB);
				} catch (Exception e) {
					logger.error("Synonym Referesh", e); //$NON-NLS-1$

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

							// select tabitem
							getTabFolderObject().setSelection(tbtmSynonym);
							
							selectDataOfTable(strObjectName);
						} else {
							if (showSynonyms != null)
								showSynonyms.clear();
							synonymListViewer.setInput(showSynonyms);
							synonymListViewer.refresh();
							TableUtil.packTable(synonymListViewer.getTable());

							Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, jobEvent.getResult().getMessage(), jobEvent.getResult()
									.getException()); //$NON-NLS-1$
							ExceptionDetailsErrorDialog.openError(null, Messages.get().Error, Messages.get().ExplorerViewer_86, errStatus); //$NON-NLS-1$
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
		if(getUserDB() == null) return; 
		
		dropAction_Synonym.setUserDB(getUserDB());
		refreshAction_Synonym.setUserDB(getUserDB());
		executeAction.setUserDB(getUserDB());
		viewDDLAction.setUserDB(getUserDB());
	}

	/**
	 * get synonymViewer
	 * 
	 * @return
	 */
	public TableViewer getTableviewer() {
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
		if(dropAction_Synonym != null) dropAction_Synonym.dispose();
		if(refreshAction_Synonym != null) refreshAction_Synonym.dispose();
		if(viewDDLAction != null) viewDDLAction.dispose();
		if(executeAction != null) executeAction.dispose();
	}

	@Override
	public void setSearchText(String searchText) {
		synonymFilter.setSearchString(searchText);
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		if("".equals(strObjectName) || strObjectName == null) return;
		
		getTableviewer().getTable().setFocus();
		
		// find select object and viewer select
		for(int i=0; i<showSynonymColumns.size(); i++) {
			OracleSynonymDAO tableDao = (OracleSynonymDAO)getTableviewer().getElementAt(i);
			if(StringUtils.equalsIgnoreCase(strObjectName, tableDao.getSynonym_name())) {
				getTableviewer().setSelection(new StructuredSelection(getTableviewer().getElementAt(i)), true);
				break;
			}
		}
	}
}
