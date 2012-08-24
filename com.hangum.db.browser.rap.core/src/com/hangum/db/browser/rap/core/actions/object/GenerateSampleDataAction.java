package com.hangum.db.browser.rap.core.actions.object;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.hangum.db.browser.rap.core.viewers.object.ExplorerViewer;
import com.hangum.db.dao.mysql.TableDAO;
import com.hangum.db.define.Define;
import com.hangum.tadpole.rdb.core.ext.sampledata.SampleDataGenerateDialog;

/**
 * Object Explorer에서 사용하는 공통 action
 * 
 * @author hangumNote
 *
 */
public class GenerateSampleDataAction extends AbstractObjectAction {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateSampleDataAction.class);

	public final static String ID = "com.hangum.db.browser.rap.core.actions.object.generatesample.data";
	
	public GenerateSampleDataAction(IWorkbenchWindow window, Define.DB_ACTION actionType, String title) {
		super(window, actionType);
		setId(ID + actionType.toString());
		setText("Generate Sample data");
		
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void run() {
//		if(actionType == DB_ACTION.TABLES) {
//			
//			// others db
//			if(DBDefine.getDBDefine(userDB.getTypes()) != DBDefine.MONGODB_DEFAULT) {
//				CreateTableAction cta = new CreateTableAction();
//				cta.run(userDB, actionType);
//			// moongodb
//			} else if(DBDefine.getDBDefine(userDB.getTypes()) == DBDefine.MONGODB_DEFAULT) {
//				
//				NewCollectionDialog ncd = new NewCollectionDialog(Display.getCurrent().getActiveShell(), userDB);
//				if(Dialog.OK == ncd.open() ) {
//					refreshTable();
//				}
//				
//			}
//			
//		} else if(actionType == DB_ACTION.VIEWS) {
//			CreateViewAction cva = new CreateViewAction();
//			cva.run(userDB, actionType);
//		} else if(actionType == DB_ACTION.INDEXES) {
//			CreateIndexAction cia = new CreateIndexAction();
//			cia.run(userDB, actionType);
//		} else if(actionType == DB_ACTION.PROCEDURES) {
//			CreateProcedureAction cia = new CreateProcedureAction();
//			cia.run(userDB, actionType);
//		} else if(actionType == DB_ACTION.FUNCTIONS) {
//			CreateFunctionAction cia = new CreateFunctionAction();
//			cia.run(userDB, actionType);
//		} else if(actionType == DB_ACTION.TRIGGERS) {
//			CreateTriggerAction cia = new CreateTriggerAction();
//			cia.run(userDB, actionType);
//		}
		
		logger.debug("###################################");
		
		logger.debug("[] ======================> GenerationSample data");
		TableDAO tableDao = (TableDAO)sel.getFirstElement();
		
		SampleDataGenerateDialog dialog = new SampleDataGenerateDialog(window.getShell(), userDB, tableDao.getName());
		dialog.open();
		
		logger.debug("###################################");
	}
	

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(ExplorerViewer.ID.equals( part.getSite().getId() )) {
			
			if(userDB != null) {
				if(selection instanceof IStructuredSelection && !selection.isEmpty()) {
					this.sel = (IStructuredSelection)selection;
					setEnabled(this.sel.size() > 0);
				} else setEnabled(false);
			}
			else setEnabled(false);
		}
	}
	
}
