/*******************************************************************************
 * Copyright (c) 2015 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.manager.core.editor.restfulapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.hangum.tadpole.commons.dialogs.message.TadpoleSimpleMessageDialog;
import com.hangum.tadpole.commons.google.analytics.AnalyticCaller;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine;
import com.hangum.tadpole.commons.libs.core.define.PublicTadpoleDefine.RESOURCE_TYPE;
import com.hangum.tadpole.commons.util.Utils;
import com.hangum.tadpole.engine.define.DBDefine;
import com.hangum.tadpole.engine.initialize.TadpoleSystemInitializer;
import com.hangum.tadpole.engine.manager.TadpoleSQLManager;
import com.hangum.tadpole.engine.query.dao.ResourceManagerDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBDAO;
import com.hangum.tadpole.engine.query.dao.system.UserDBResourceDAO;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBQuery;
import com.hangum.tadpole.engine.query.sql.TadpoleSystem_UserDBResource;
import com.hangum.tadpole.engine.restful.RESTfulAPIUtils;
import com.hangum.tadpole.manager.core.Messages;
import com.hangum.tadpole.manager.core.dialogs.api.APIServiceDialog;
import com.hangum.tadpole.manager.core.dialogs.api.UserAPIServiceDialog;
import com.hangum.tadpole.manager.core.editor.restfulapi.dao.RESTFulAPIDAO;
import com.hangum.tadpole.rdb.core.Activator;
import com.hangum.tadpole.rdb.core.actions.connections.QueryEditorAction;
import com.hangum.tadpole.rdb.core.actions.erd.mongodb.MongoDBERDViewAction;
import com.hangum.tadpole.rdb.core.actions.erd.rdb.RDBERDViewAction;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.swtdesigner.ResourceManager;

/**
 * ResufulAPI Manager 
 * 
 * @author hangum
 *
 */
public class RESTFulAPIManagerEditor extends EditorPart {
	public static final String ID = "com.hangum.tadpole.manager.core.editor.restfulapi";
	private static final Logger logger = Logger.getLogger(RESTFulAPIManagerEditor.class);
	
	private TreeViewer tvAPIList;
	private List<RESTFulAPIDAO> listRestfulDao = new ArrayList<>();
	private UserDBDAO userDB = null;
	private ResourceManagerDAO rmDAO = null;
	
	//////////
	private ComboViewer comboShare;
	private Text textTitle;
	private Text textDescription;
	private Combo comboSupportAPI;
	private Text textAPIURL;
	private Text textAPIKey;
	
	private Text textQuery;

	public RESTFulAPIManagerEditor() {
	}
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite compositeHead = new Composite(parent, SWT.NONE);
		compositeHead.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeHead.setLayout(new GridLayout(1, false));
		
		ToolBar toolBar = new ToolBar(compositeHead, SWT.FLAT | SWT.RIGHT);
		
		ToolItem tltmRefrsh = new ToolItem(toolBar, SWT.NONE);
		tltmRefrsh.setToolTipText(Messages.get().Refresh);
		tltmRefrsh.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "resources/icons/refresh.png")); //$NON-NLS-1$
		tltmRefrsh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				initUI();
			}
		});
		
		ToolItem tltmAPIExecute = new ToolItem(toolBar, SWT.NONE);
		tltmAPIExecute.setToolTipText(Messages.get().RESTFulAPIManagerEditor_3);
		tltmAPIExecute.setImage(ResourceManager.getPluginImage(com.hangum.tadpole.manager.core.Activator.PLUGIN_ID, "resources/icons/restful_api.png")); //$NON-NLS-1$
		tltmAPIExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				UserAPIServiceDialog dialog = new UserAPIServiceDialog(getSite().getShell());
				dialog.open();
			}
		});
		
		Composite compositeBody = new Composite(parent, SWT.NONE);
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));
		
		tvAPIList = new TreeViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		tvAPIList.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if (tvAPIList.getSelection().isEmpty()) return;

				StructuredSelection ss = (StructuredSelection) tvAPIList.getSelection();
				RESTFulAPIDAO dao = (RESTFulAPIDAO) ss.getFirstElement();
				rmDAO = dao.getResourceManagerDao();
				
				if(rmDAO == null) return;
				
				try {
					userDB = TadpoleSystem_UserDBQuery.getUserDBInstance(Integer.parseInt(""+rmDAO.getDb_seq())); //$NON-NLS-1$
					
					SqlMapClient sqlClient = TadpoleSQLManager.getInstance(TadpoleSystemInitializer.getUserDB());
					List<String> result = sqlClient.queryForList("userDbResourceData", rmDAO); //$NON-NLS-1$

					comboShare.getCombo().select("PUBLIC".equals(rmDAO.getShared_type()) ? 0 : 1); //$NON-NLS-1$
					textTitle.setText(rmDAO.getName());
					textDescription.setText(rmDAO.getDescription());
					comboSupportAPI.setText(rmDAO.getRestapi_yesno());
					textAPIURL.setText(rmDAO.getRestapi_uri()==null?"":rmDAO.getRestapi_uri()); //$NON-NLS-1$
					textAPIKey.setText(rmDAO.getRestapi_key());
					textQuery.setText(""); //$NON-NLS-1$
					for (String data : result) {
						textQuery.append(data);
					}

				} catch (Exception e) {
					logger.error("Resource detail", e); //$NON-NLS-1$
				}
			}
		});
		tvAPIList.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				if (tvAPIList.getSelection().isEmpty()) return;

				StructuredSelection ss = (StructuredSelection) tvAPIList.getSelection();
				RESTFulAPIDAO dao = (RESTFulAPIDAO) ss.getFirstElement();
				ResourceManagerDAO rmDAO = dao.getResourceManagerDao();
				
				if(rmDAO == null) return;
				
				try {
					
					final UserDBDAO userDB = TadpoleSystem_UserDBQuery.getUserDBInstance(Integer.parseInt(""+rmDAO.getDb_seq())); //$NON-NLS-1$
					
					// TODO : 기존 데이터베이스 목록에 리소스를 표시하기 위한  DAO를 사용하는 부분과 호환성을 위해 변환.리소스DAO를 하나로 통합할 필요 있음.
					UserDBResourceDAO ad = new UserDBResourceDAO();
					ad.setResource_seq((int) rmDAO.getResource_seq());
					ad.setName(rmDAO.getName());
					ad.setParent(userDB);
			
					// db object를 클릭하면 쿼리 창이 뜨도록하고.
					if (PublicTadpoleDefine.RESOURCE_TYPE.ERD.toString().equals(rmDAO.getResource_types())) {
						if (userDB != null && DBDefine.MONGODB_DEFAULT == userDB.getDBDefine()) {
							 MongoDBERDViewAction ea = new MongoDBERDViewAction();
							 ea.run(ad);
						} else {
							 RDBERDViewAction ea = new RDBERDViewAction();
							 ea.run(ad);
						}
					} else if (PublicTadpoleDefine.RESOURCE_TYPE.SQL.toString().equals(rmDAO.getResource_types())) {
						QueryEditorAction qea = new QueryEditorAction();
						qea.run(ad);
					}
				} catch(Exception e) {
					logger.error("select api", e); //$NON-NLS-1$
				}
			}
		});
		Tree tree = tvAPIList.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(tvAPIList, SWT.NONE);
		TreeColumn trclmnUrl = treeViewerColumn.getColumn();
		trclmnUrl.setWidth(150);
		trclmnUrl.setText(Messages.get().URL);
		
		TreeViewerColumn tvcName = new TreeViewerColumn(tvAPIList, SWT.NONE);
		TreeColumn trclmnDBName = tvcName.getColumn();
		trclmnDBName.setWidth(150);
		trclmnDBName.setText(Messages.get().APIName);
		
		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(tvAPIList, SWT.NONE);
		TreeColumn trclmnName = treeViewerColumn_1.getColumn();
		trclmnName.setWidth(150);
		trclmnName.setText(Messages.get().DBName);
		
		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(tvAPIList, SWT.NONE);
		TreeColumn trclmnDescription = treeViewerColumn_2.getColumn();
		trclmnDescription.setWidth(300);
		trclmnDescription.setText(Messages.get().Description);
		
		tvAPIList.setContentProvider(new APIListContentProvider());
		tvAPIList.setLabelProvider(new APIListLabelProvider());
		
/////////////////////////////////////////////////////////////////////////////////////////////
		Group grpQuery = new Group(parent, SWT.NONE);
		grpQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpQuery.setText(Messages.get().DetailItem);
		GridLayout gl_grpQuery = new GridLayout(1, false);
		gl_grpQuery.verticalSpacing = 1;
		gl_grpQuery.horizontalSpacing = 1;
		gl_grpQuery.marginHeight = 1;
		gl_grpQuery.marginWidth = 1;
		grpQuery.setLayout(gl_grpQuery);

		Composite compositeDetail = new Composite(grpQuery, SWT.NONE | SWT.READ_ONLY);
		GridLayout gl_compositeDetail = new GridLayout(7, false);
		gl_compositeDetail.marginHeight = 2;
		gl_compositeDetail.marginWidth = 2;
		gl_compositeDetail.verticalSpacing = 2;
		compositeDetail.setLayout(gl_compositeDetail);
		compositeDetail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Label lblNewLabel = new Label(compositeDetail, SWT.NONE);
		lblNewLabel.setText(Messages.get().Share);

		comboShare = new ComboViewer(compositeDetail, SWT.NONE | SWT.READ_ONLY);
		Combo cShare = comboShare.getCombo();
		cShare.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cShare.setItems(new String[] { "PUBLIC", "PRIVATE" }); //$NON-NLS-1$ //$NON-NLS-2$

		Label lblNewLabel_1 = new Label(compositeDetail, SWT.NONE);
		lblNewLabel_1.setText(Messages.get().Title);

		textTitle = new Text(compositeDetail, SWT.BORDER);
		textTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		Button btnSave = new Button(compositeDetail, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(rmDAO == null) return;
				
				if(!MessageDialog.openConfirm(getSite().getShell(), Messages.get().Confirm, Messages.get().RESTFulAPIManagerEditor_22)) return;
				
				try {
					String share_type = comboShare.getCombo().getText();
					share_type = (share_type == null || "".equals(share_type)) ? "PUBLIC" : share_type; //$NON-NLS-1$ //$NON-NLS-2$
					rmDAO.setShared_type(share_type);
					rmDAO.setName(textTitle.getText());
					rmDAO.setDescription(textDescription.getText());
					rmDAO.setRestapi_yesno(comboSupportAPI.getText());
					rmDAO.setRestapi_uri(textAPIURL.getText());
					
					if(!isValid(rmDAO)) return;
					
					try {
						TadpoleSystem_UserDBResource.userDBResourceDupUpdate(userDB, rmDAO);
					} catch (Exception ee) {
						logger.error("Resource validate", ee); //$NON-NLS-1$
						MessageDialog.openError(null, Messages.get().Error, ee.getMessage()); //$NON-NLS-1$
						return;
					}
					
					if(comboSupportAPI.getText().equals(PublicTadpoleDefine.YES_NO.YES.name()) && "".equals(rmDAO.getRestapi_key())) { //$NON-NLS-1$
						rmDAO.setRestapi_key(Utils.getUniqueID());	
					}
					
					TadpoleSystem_UserDBResource.updateResourceHeader(rmDAO);
					tvAPIList.refresh(rmDAO, true);
					
					MessageDialog.openInformation(getSite().getShell(), Messages.get().Confirm, Messages.get().RESTFulAPIManagerEditor_27);
				} catch (Exception e1) {
					logger.error("save resource", e1); //$NON-NLS-1$
					MessageDialog.openError(getSite().getShell(), Messages.get().Error, Messages.get().RESTFulAPIManagerEditor_30+ e1.getMessage());
				}
			}
			
			/**
			 * is valid
			 * @return
			 */
			private boolean isValid(ResourceManagerDAO dao) {
				int len = StringUtils.trimToEmpty(textTitle.getText()).length();
				if(len < 3) {
					MessageDialog.openWarning(null, Messages.get().Warning, Messages.get().RESTFulAPIManagerEditor_31); //$NON-NLS-1$
					textTitle.setFocus();
					return false;
				}

				// sql type 
				if(dao.getResource_types().equals(RESOURCE_TYPE.SQL.name())) {
					if(PublicTadpoleDefine.YES_NO.YES.name().equals(comboSupportAPI.getText())) {
						String strAPIURI = textAPIURL.getText().trim();
						
						if(strAPIURI.equals("")) { //$NON-NLS-1$
							MessageDialog.openWarning(getSite().getShell(), Messages.get().Warning, Messages.get().RESTFulAPIManagerEditor_34);
							textAPIURL.setFocus();
							return false;
						}
						
						// check valid url. url pattern is must be /{parent}/{child}
						if(!RESTfulAPIUtils.validateURL(textAPIURL.getText())) {
							MessageDialog.openWarning(getSite().getShell(), Messages.get().Warning, Messages.get().RESTFulAPIManagerEditor_36);
							
							textAPIURL.setFocus();
							return false;
						}
					}
				}
				
				return true;
			}
		});
		btnSave.setText(Messages.get().Save);

//		Button btnDelete = new Button(composite, SWT.NONE);
//		btnDelete.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				if (tableViewer.getSelection().isEmpty()) return;
//
//				if(!MessageDialog.openConfirm(getSite().getShell(), Messages.get().Confirm, "Do you wont to delete?")) return;
//				StructuredSelection ss = (StructuredSelection) tableViewer.getSelection();
//				ResourceManagerDAO dao = (ResourceManagerDAO) ss.getFirstElement();
//
//				// 기존에 사용하던 좌측 트리(디비목록)에 리소스를 표시하지 않을 경우에는 dao를 통일해서 하나만 쓰게 수정이
//				// 필요함.
//				UserDBResourceDAO userDBResource = new UserDBResourceDAO();
//				userDBResource.setResource_seq((int) dao.getResource_seq());
//				userDBResource.setName(dao.getRes_title());
//				userDBResource.setParent(userDB);
//
//				try {
//					TadpoleSystem_UserDBResource.delete(userDBResource);
//					addUserResouceData(null);
//				} catch (Exception e1) {
//					logger.error("Resource delete " + dao.toString(), e1);
//				}
//			}
//		});
//		btnDelete.setText("Delete");

		Label lblDescription = new Label(compositeDetail, SWT.NONE);
		lblDescription.setText(Messages.get().Description);

		textDescription = new Text(compositeDetail, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.CANCEL | SWT.MULTI);
		GridData gd_textDescription = new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1);
		gd_textDescription.heightHint = 44;
		textDescription.setLayoutData(gd_textDescription);
		
		Label lblSupportApi = new Label(compositeDetail, SWT.NONE);
		lblSupportApi.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSupportApi.setText(Messages.get().RESTFulAPIManagerEditor_39);
		
		comboSupportAPI = new Combo(compositeDetail, SWT.READ_ONLY);
		comboSupportAPI.add("YES"); //$NON-NLS-1$
		comboSupportAPI.add("NO"); //$NON-NLS-1$
		comboSupportAPI.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblApiURI = new Label(compositeDetail, SWT.NONE);
		lblApiURI.setText(Messages.get().APIURL);
		
		textAPIURL = new Text(compositeDetail, SWT.BORDER);
		textAPIURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Button btnShowUrl = new Button(compositeDetail, SWT.NONE);
		btnShowUrl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!"".equals(textAPIURL.getText())) { //$NON-NLS-1$
					String strURL = RESTfulAPIUtils.makeURL(textQuery.getText(), textAPIURL.getText());
					
					TadpoleSimpleMessageDialog dialog = new TadpoleSimpleMessageDialog(getSite().getShell(), Messages.get().RESTFulAPIManagerEditor_44, strURL);
					dialog.open();
				}
			}
		});
		btnShowUrl.setText(Messages.get().RESTFulAPIManagerEditor_45);
		new Label(compositeDetail, SWT.NONE);
		new Label(compositeDetail, SWT.NONE);
		
		Label lblApiKey = new Label(compositeDetail, SWT.NONE);
		lblApiKey.setText(Messages.get().RESTFulAPIManagerEditor_46);
		
		textAPIKey = new Text(compositeDetail, SWT.BORDER);
		textAPIKey.setEditable(false);
		textAPIKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Button btnApiExecute = new Button(compositeDetail, SWT.NONE);
		btnApiExecute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!"".equals(textAPIURL.getText())) { //$NON-NLS-1$
					if(userDB != null & rmDAO != null) {
						APIServiceDialog dialog = new APIServiceDialog(getSite().getShell(), userDB, textQuery.getText(), rmDAO);
						dialog.open();
					}
				}
			}
		});
		btnApiExecute.setText(Messages.get().RESTFulAPIManagerEditor_48);

		textQuery = new Text(compositeDetail, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textQuery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 7, 1));
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		initUI();

	}
	
	/**
	 * Initialize UI 
	 */
	private void initUI() {
		listRestfulDao.clear();
		
		try {
			List<ResourceManagerDAO> listAPIList = TadpoleSystem_UserDBResource.getRESTFulAPIList();
			Map<String, RESTFulAPIDAO> mapFirstApi = new HashMap<String, RESTFulAPIDAO>();
			
			// 첫 번째 인덱스 항목을 넣는다.
			for (ResourceManagerDAO resourceManagerDAO : listAPIList) {
				String strURL = StringUtils.removeStart(resourceManagerDAO.getRestapi_uri(), "/"); //$NON-NLS-1$
				
				// 트리의 마지막에 데이터를 넣어야 한다.
				String[] strArrySlash = StringUtils.split(strURL, '/');
				String strTreeKey = strArrySlash[0];
				if(!mapFirstApi.containsKey(strTreeKey)) {
					if(strArrySlash.length == 1) mapFirstApi.put(strTreeKey, new RESTFulAPIDAO(strTreeKey, resourceManagerDAO));
					else mapFirstApi.put(strTreeKey, new RESTFulAPIDAO(strTreeKey));
				}
			}
			
			// 두 번째 항목을 찾는다.
			for (ResourceManagerDAO resourceManagerDAO : listAPIList) {
				String strURL = StringUtils.removeStart(resourceManagerDAO.getRestapi_uri(), "/"); //$NON-NLS-1$
				
				// 트리의 마지막에 데이터를 넣어야 한다.
				String[] strArrySlash = StringUtils.split(strURL, '/');
				if(strArrySlash.length == 1) continue;
				
				String strTreeKey = strArrySlash[1];
				RESTFulAPIDAO rootDAO = mapFirstApi.get(strArrySlash[0]);						
				if(strArrySlash.length == 2) {
					List<RESTFulAPIDAO> listChildren = rootDAO.getListChildren();
					
					RESTFulAPIDAO existRestDAO = null;
					for (RESTFulAPIDAO restFulAPIDAO : listChildren) {
						if(strTreeKey.equals(restFulAPIDAO.getStrURL())) existRestDAO = restFulAPIDAO;
					}
					if(existRestDAO != null) listChildren.remove(existRestDAO);
					
					listChildren.add(new RESTFulAPIDAO(strTreeKey, resourceManagerDAO));
				} else {
					List<RESTFulAPIDAO> listChildren = rootDAO.getListChildren();
					boolean boolExist = false;
					for (RESTFulAPIDAO restFulAPIDAO : listChildren) {
						if(strTreeKey.equals(restFulAPIDAO.getStrURL())) boolExist = true;
					}
					
					if(!boolExist) {
						listChildren.add(new RESTFulAPIDAO(strTreeKey));	
					}
				}
			}
			
			// 세 번째 항목을 찾는다.
			for (ResourceManagerDAO resourceManagerDAO : listAPIList) {
				String strURL = StringUtils.removeStart(resourceManagerDAO.getRestapi_uri(), "/"); //$NON-NLS-1$
				
				// 트리의 마지막에 데이터를 넣어야 한다.
				String[] strArrySlash = StringUtils.split(strURL, '/');
					
				if(strArrySlash.length == 3) {
					String strTreeKey = strArrySlash[2];
					
					RESTFulAPIDAO rootDAO = mapFirstApi.get(strArrySlash[0]);
					String searchKey = strArrySlash[1];
					
					for (RESTFulAPIDAO restFulAPIDAO : rootDAO.getListChildren()) {
						if(searchKey.equals(restFulAPIDAO.getStrURL())) {
							restFulAPIDAO.getListChildren().add(new RESTFulAPIDAO(strTreeKey, resourceManagerDAO));
						}
					}
				}
			}

			listRestfulDao.addAll(mapFirstApi.values());
			tvAPIList.setInput(listRestfulDao);
			
		} catch (Exception e) {
			logger.error("RESTFulAPI List", e); //$NON-NLS-1$
		}
		
		// google analytic
		AnalyticCaller.track(RESTFulAPIManagerEditor.ID);
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

		RESTFulAPIManagerEditorInput esqli = (RESTFulAPIManagerEditorInput) input;
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
	public void setFocus() {

	}

}

/**
 * content provider
 * 
 * @author hangum
 *
 */
class APIListContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		List<RESTFulAPIDAO> listRestfulDao = (List)inputElement;
		return listRestfulDao.toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		RESTFulAPIDAO restfulDao = (RESTFulAPIDAO)parentElement;
		
		return restfulDao.getListChildren().toArray();
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		RESTFulAPIDAO restfulDao = (RESTFulAPIDAO)element;
		
		return restfulDao.getListChildren().isEmpty()?false:true;
	}
	
}

/**
 * label provider
 * 
 * @author hangum
 *
 */
class APIListLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		RESTFulAPIDAO dao = (RESTFulAPIDAO)element;
		ResourceManagerDAO rmDAO = dao.getResourceManagerDao();

		if(columnIndex == 0) return "/" + dao.getStrURL(); //$NON-NLS-1$
		if(rmDAO != null) {
			switch(columnIndex) {
			case 1: return rmDAO.getName();
			case 2: return rmDAO.getDisplay_name();
			case 3: return rmDAO.getDescription();
			}
		}
		
		return null;
	}
	
}