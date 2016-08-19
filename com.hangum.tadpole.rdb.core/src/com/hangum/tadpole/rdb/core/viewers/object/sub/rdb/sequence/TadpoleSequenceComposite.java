/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     nilrir - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.viewers.object.sub.rdb.sequence;

import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.jface.viewers.IStructuredSelection;
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
import com.hangum.tadpole.commons.libs.core.message.CommonMessages;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.rdb.OracleSequenceDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.sql.util.SQLUtil;
import com.hangum.tadpole.engine.sql.util.tables.TableUtil;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.Messages;
import com.hangum.tadpole.rdb.core.actions.object.AbstractObjectAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.generate.GenerateViewDDLAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectCreatAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectDropAction;
import com.hangum.tadpole.rdb.core.actions.object.rdb.object.ObjectRefreshAction;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.ColumnHeaderCreator;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultLabelProvider;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.DefaultTableColumnFilter;
import com.hangum.tadpole.rdb.core.editors.dbinfos.composites.TableViewColumnDefine;
import com.hangum.tadpole.rdb.core.util.FindEditorAndWriteQueryUtil;
import com.hangum.tadpole.rdb.core.viewers.object.sub.AbstractObjectComposite;
import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * RDB sequence composite
 * 
 * @author nilriri
 * 
 */
public class TadpoleSequenceComposite extends AbstractObjectComposite {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TadpoleSequenceComposite.class);

	private CTabItem tbtmSequence;

	// table info
	private TableViewer sequenceListViewer;
	private List<OracleSequenceDAO> showSequences = new ArrayList<OracleSequenceDAO>();
	private DefaultTableColumnFilter sequenceFilter;

	// column info
	private TableViewer sequenceColumnViewer;
	
	private ObjectCreatAction creatAction_Sequence;
	private AbstractObjectAction dropAction_Sequence;
	private AbstractObjectAction refreshAction_Sequence;
	private GenerateViewDDLAction viewDDLAction;

	/**
	 * Create the composite.
	 * 
	 * @param partSite
	 * @param parent
	 * @param userDB
	 */
	public TadpoleSequenceComposite(IWorkbenchPartSite partSite, final CTabFolder tabFolderObject, UserDBDAO userDB) {
		super(partSite, tabFolderObject, userDB);

		createWidget(tabFolderObject);
	}

	private void createWidget(final CTabFolder tabFolderObject) {
		tbtmSequence = new CTabItem(tabFolderObject, SWT.NONE);
		tbtmSequence.setText(Messages.get().Sequence);
		tbtmSequence.setData(TAB_DATA_KEY, PublicTadpoleDefine.OBJECT_TYPE.SEQUENCE.name());

		Composite compositeTables = new Composite(tabFolderObject, SWT.NONE);
		tbtmSequence.setControl(compositeTables);
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
		sequenceListViewer = new TableViewer(sashForm, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		sequenceListViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				try {
					IStructuredSelection is = (IStructuredSelection) event.getSelection();
					if (null != is) {
						OracleSequenceDAO sequenceDAO = (OracleSequenceDAO) is.getFirstElement();
						FindEditorAndWriteQueryUtil.run(userDB, "SELECT " + sequenceDAO.getFullName() + ".NEXTVAL FROM DUAL;" , PublicTadpoleDefine.OBJECT_TYPE.SEQUENCE);
					}
				} catch (Exception e) {
					logger.error("create sequence", e);
				}
			}
		});

		Table tableTableList = sequenceListViewer.getTable();
		tableTableList.setLinesVisible(true);
		tableTableList.setHeaderVisible(true);

		createSequenceMenu();
		createSequenceListColumns();

		sequenceListViewer.setInput(showSequences);
		sequenceListViewer.refresh();

		sequenceFilter = new DefaultTableColumnFilter();
		sequenceListViewer.addFilter(sequenceFilter);
	}

	//SEQUENCE_OWNER, SEQUENCE_NAME, MIN_VALUE, MAX_VALUE, INCREMENT_BY, CYCLE_FLAG, ORDER_FLAG, CACHE_SIZE, LAST_NUMBER
	private void createSequenceListColumns() {
		TableViewColumnDefine[] tableColumnDef = new TableViewColumnDefine[] { //
		new TableViewColumnDefine("SEQUENCE_NAME", CommonMessages.get().Name, 100, SWT.LEFT) // //$NON-NLS-1$
				, new TableViewColumnDefine("MIN_VALUE", "Min value", 80, SWT.RIGHT) // //$NON-NLS-1$
				//, new TableViewColumnDefine("MAX_VALUE", "Max value", 80, SWT.RIGHT) // //$NON-NLS-1$
				, new TableViewColumnDefine("INCREMENT_BY", "Increment by", 40, SWT.RIGHT) // //$NON-NLS-1$
				, new TableViewColumnDefine("CYCLE_FLAG", "Cycle flag", 40, SWT.CENTER) // //$NON-NLS-1$
				, new TableViewColumnDefine("ORDER_FLAG", "Order flag", 40, SWT.CENTER) // //$NON-NLS-1$
				, new TableViewColumnDefine("CACHE_SIZE", "Cache size", 40, SWT.RIGHT) // //$NON-NLS-1$
				, new TableViewColumnDefine("LAST_NUMBER", "Last number", 80, SWT.RIGHT) // //$NON-NLS-1$
		};

		ColumnHeaderCreator.createColumnHeader(sequenceListViewer, tableColumnDef);

		sequenceListViewer.setContentProvider(new ArrayContentProvider());
		sequenceListViewer.setLabelProvider(new DefaultLabelProvider(sequenceListViewer));

	}

	/**
	 * create Table menu
	 */
	private void createSequenceMenu() {
		if(getUserDB() == null) return;
		
		creatAction_Sequence = new ObjectCreatAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.SEQUENCE, Messages.get().SequenceCreated);
		dropAction_Sequence = new ObjectDropAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.SEQUENCE, Messages.get().SequenceDelete); //$NON-NLS-1$
		refreshAction_Sequence = new ObjectRefreshAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.SEQUENCE, CommonMessages.get().Refresh); //$NON-NLS-1$
		//executeAction = new ObjectExecuteProcedureAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.SEQUENCE, Messages.get().Execute); //$NON-NLS-1$
		viewDDLAction = new GenerateViewDDLAction(getSite().getWorkbenchWindow(), PublicTadpoleDefine.OBJECT_TYPE.SEQUENCE, Messages.get().ViewDDL); //$NON-NLS-1$

		// menu
		final MenuManager menuMgr = new MenuManager("#PopupMenu", "Sequence"); //$NON-NLS-1$ //$NON-NLS-2$
		if(!isDDLLock()) {
			menuMgr.add(creatAction_Sequence);
			menuMgr.add(dropAction_Sequence);
			menuMgr.add(new Separator());
		}
		menuMgr.add(refreshAction_Sequence);
		
		//IStructuredSelection is = (IStructuredSelection) sequenceListViewer.getSelection();
		//if (!is.isEmpty()) {
			menuMgr.add(new Separator());
			//OracleSequenceDAO sequenceDAO = (OracleSequenceDAO) is.getFirstElement();

			viewDDLAction.setEnabled(true);
			//executeAction.setEnabled(true);
			menuMgr.add(viewDDLAction);
			//menuMgr.add(executeAction);
		//}

		sequenceListViewer.getTable().setMenu(menuMgr.createContextMenu(sequenceListViewer.getTable()));
		getSite().registerContextMenu(menuMgr, sequenceListViewer);
	}

	/**
	 * 정보를 최신으로 리프레쉬합니다.
	 * @param strObjectName 
	 */
	public void refreshSequence(final UserDBDAO selectUserDb, final boolean boolRefresh, final String strObjectName) {
		if (!boolRefresh) if (selectUserDb == null) return;
		this.userDB = selectUserDb;

		Job job = new Job(Messages.get().MainEditor_45) {
			@Override
			public IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(MSG_DataIsBeginAcquired, IProgressMonitor.UNKNOWN); //$NON-NLS-1$

				try {
					showSequences = getSequenceList(userDB);
					
					for(OracleSequenceDAO dao : showSequences) {
						dao.setSysName(SQLUtil.makeIdentifierName(userDB, dao.getSequence_name() ));
					}
				} catch (Exception e) {
					logger.error("Sequence Referesh", e); //$NON-NLS-1$

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
							sequenceListViewer.setInput(showSequences);
							sequenceListViewer.refresh();
							TableUtil.packTable(sequenceListViewer.getTable());

							// select tabitem
							getTabFolderObject().setSelection(tbtmSequence);
							
							selectDataOfTable(strObjectName);
						} else {
							if (showSequences != null)
								showSequences.clear();
							sequenceListViewer.setInput(showSequences);
							sequenceListViewer.refresh();
							TableUtil.packTable(sequenceListViewer.getTable());

							Status errStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, jobEvent.getResult().getMessage(), jobEvent.getResult().getException()); //$NON-NLS-1$
							ExceptionDetailsErrorDialog.openError(null,CommonMessages.get().Error, Messages.get().ExplorerViewer_86, errStatus); //$NON-NLS-1$
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
	public static List<OracleSequenceDAO> getSequenceList(final UserDBDAO userDB) throws Exception {
		SqlMapClient sqlClient = TadpoleSQLManager.getInstance(userDB);
		return sqlClient.queryForList("sequenceList", userDB.getSchema()); //$NON-NLS-1$
	}

	/**
	 * initialize action
	 */
	public void initAction() {
		if(getUserDB() == null) return; 
		
		creatAction_Sequence.setUserDB(getUserDB());
		dropAction_Sequence.setUserDB(getUserDB());
		refreshAction_Sequence.setUserDB(getUserDB());
		//executeAction.setUserDB(getUserDB());
		viewDDLAction.setUserDB(getUserDB());
	}

	/**
	 * get sequenceViewer
	 * 
	 * @return
	 */
	public TableViewer getTableviewer() {
		return sequenceListViewer;
	}

	/**
	 * get sequence column viewer
	 * 
	 * @return
	 */
	public TableViewer getSequenceColumnViewer() {
		return sequenceColumnViewer;
	}

	/**
	 * initialize filter text
	 * 
	 * @param textSearch
	 */
	public void filter(String textSearch) {
		sequenceFilter.setSearchString(textSearch);
		sequenceListViewer.refresh();
	}

	@Override
	public void dispose() {
		super.dispose();	
		if(creatAction_Sequence != null) creatAction_Sequence.dispose();
		if(dropAction_Sequence != null) dropAction_Sequence.dispose();
		if(refreshAction_Sequence != null) refreshAction_Sequence.dispose();
		if(viewDDLAction != null) viewDDLAction.dispose();
		//if(executeAction != null) executeAction.dispose();
	}

	@Override
	public void setSearchText(String searchText) {
		sequenceFilter.setSearchString(searchText);
	}

	@Override
	public void selectDataOfTable(String strObjectName) {
		if("".equals(strObjectName) || strObjectName == null) return;
		
		getTableviewer().getTable().setFocus();
		
		// find select object and viewer select
		for(int i=0; i< this.showSequences.size(); i++) {
			OracleSequenceDAO sequenceDao = (OracleSequenceDAO)getTableviewer().getElementAt(i);
			if(StringUtils.equalsIgnoreCase(strObjectName, sequenceDao.getSequence_name())) {
				getTableviewer().setSelection(new StructuredSelection(getTableviewer().getElementAt(i)), true);
				break;
			}
		}
	}
}
