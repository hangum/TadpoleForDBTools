/*******************************************************************************
 * Copyright (c) 2016 hangum.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     hangum - initial API and implementation
 ******************************************************************************/
package com.hangum.tadpole.rdb.core.editors.main.composite.plandetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.hangum.tadpole.engine.query.dao.mysql.InformationSchemaDAO;
import com.hangum.tadpole.engine.query.dao.mysql.TableDAO;
import com.hangum.tadpole.engine.sql.util.RDBTypeToJavaTypeUtils;
import com.hangum.tadpole.engine.sql.util.resultset.QueryExecuteResultDTO;
import com.hangum.tadpole.engine.sql.util.resultset.TadpoleResultSet;
import com.hangum.tadpole.rdb.core.dialog.dml.IndexInformationDialog;
import com.hangum.tadpole.rdb.core.dialog.dml.TableInformationDialog;
import com.hangum.tadpole.rdb.core.editors.main.composite.plandetail.oracle.OraclePlanDAO;
import com.hangum.tadpole.rdb.core.editors.main.composite.tail.PlanTailComposite;
import com.hangum.tadpole.rdb.core.editors.main.utils.RequestQuery;

/**
 * Oracle plan composite
 * 
 * @author hangum
 *
 */
public class OraclePlanComposite extends AbstractPlanComposite {
	/**  Logger for this class. */
	private static final Logger logger = Logger.getLogger(OraclePlanComposite.class);
	
	private TreeViewer tvQueryPlan;
	private List<OraclePlanDAO> listOraclePlanDao = new ArrayList<>();

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public OraclePlanComposite(Composite parent, int style, QueryExecuteResultDTO rsDAO) {
		super(parent, style, rsDAO);
		setLayout(new GridLayout(1, false));
		Composite compositeBody = new Composite(this, SWT.NONE);
		GridLayout gl_compositeHead = new GridLayout(2, false);
		gl_compositeHead.verticalSpacing = 2;
		gl_compositeHead.horizontalSpacing = 2;
		gl_compositeHead.marginHeight = 0;
		gl_compositeHead.marginWidth = 2;
		compositeBody.setLayout(gl_compositeHead);
		
		compositeBody.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeBody.setLayout(new GridLayout(1, false));
		
		tvQueryPlan = new TreeViewer(compositeBody, SWT.BORDER | SWT.FULL_SELECTION);
		Tree tree = tvQueryPlan.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tree.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {
				if (event.keyCode == SWT.F4) {
					IStructuredSelection is = (IStructuredSelection) tvQueryPlan.getSelection();
					OraclePlanDAO selElement = (OraclePlanDAO)is.getFirstElement();				
					openInformationDialog(selElement);
				}
			}
		});
		
		tvQueryPlan.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {				
				IStructuredSelection is = (IStructuredSelection)event.getSelection();
				OraclePlanDAO selElement = (OraclePlanDAO)is.getFirstElement();				
				openInformationDialog(selElement);
			}
		});
		
		Composite compositeBtn = new Composite(compositeBody, SWT.NONE);
		compositeBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		GridLayout gl_compositeBtn = new GridLayout(4, false);
		gl_compositeBtn.verticalSpacing = 2;
		gl_compositeBtn.horizontalSpacing = 2;
		gl_compositeBtn.marginWidth = 0;
		gl_compositeBtn.marginHeight = 2;
		compositeBtn.setLayout(gl_compositeBtn);
		
		compositeTail = new PlanTailComposite(this, compositeBtn, SWT.NONE);
		compositeTail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		GridLayout gl_compositeResult = new GridLayout(1, false);
		gl_compositeResult.verticalSpacing = 2;
		gl_compositeResult.horizontalSpacing = 2;
		gl_compositeResult.marginHeight = 0;
		gl_compositeResult.marginWidth = 2;
		compositeTail.setLayout(gl_compositeResult);
	}

	/**
	 * 플랜뷰에서 F4를 누르거나 더블클릭했을때 관련 오브젝트의 상세 내역을 표시한다.
	 * @param selElement
	 */
	private void openInformationDialog(OraclePlanDAO selElement) {
		if (StringUtils.equalsIgnoreCase("TABLE", selElement.getObjectType())){
			
			TableDAO tableDao = new TableDAO();

			String temp = selElement.getName();				
			String object[] = StringUtils.split(temp,  '.');
			
			if (object.length > 1){
				tableDao.setSchema_name(object[0]);
				
				String obj = object[1];					
				String tbl[] = StringUtils.split(obj, '(');
				if (tbl.length > 1){
					tableDao.setSysName(tbl[0]);
					tableDao.setTable_name(tbl[0]);
				}else{
					tableDao.setSysName(obj);	
					tableDao.setTable_name(obj);
				}					
			}else{
				tableDao.setSysName(temp);	
				tableDao.setTable_name(temp);
			}				

			new TableInformationDialog(getShell(), false, getRsDAO().getUserDB(), tableDao).open();
		}else if (StringUtils.startsWithIgnoreCase(selElement.getObjectType(), "INDEX" )){
			
			InformationSchemaDAO indexDao = new InformationSchemaDAO();
			
			String temp = selElement.getName();				
			String object[] = StringUtils.split(temp,  '.');
			
			if (object.length > 1){
				indexDao.setTABLE_SCHEMA(object[0]);					
				
				String obj = object[1];					
				String tbl[] = StringUtils.split(obj, '(');
				if (tbl.length > 1){
					indexDao.setINDEX_NAME(tbl[0]);
				}else{
					indexDao.setINDEX_NAME(obj);	
				}					
			}else{
				indexDao.setINDEX_NAME(temp);
			}				
			
			new IndexInformationDialog(getShell(), false, getRsDAO().getUserDB(), indexDao).open();
		}
	}
	
	public void setQueryPlanData(RequestQuery reqQuery, QueryExecuteResultDTO rsDAO) {
		if(tvQueryPlan.getTree().getColumnCount() == 0) createTreeColumn(rsDAO);
		listOraclePlanDao.clear();
		
		this.reqQuery = reqQuery;
		this.rsDAO = rsDAO;
		
		// table data를 생성한다.
		final TadpoleResultSet trs = rsDAO.getDataList();
		List<Map<Integer, Object>> listObj = trs.getData();
		
		for (Map<Integer, Object> map : listObj) {
			OraclePlanDAO dao = new OraclePlanDAO();
			
			dao.setOperation(""+map.get(1));
			dao.setName(""+map.get(2));
			dao.setCost(""+map.get(3));
			dao.setRows(""+map.get(4));
			dao.setBytes(map.get(5)==null?"":""+map.get(5));
			dao.setPos(Integer.parseInt(map.get(6)==null?"0":""+map.get(6)));
			dao.setFilter(map.get(7)==null?"":""+map.get(7));
			dao.setAccess(map.get(8)==null?"":""+map.get(8));
			dao.setObjectType(map.get(9)==null?"":""+map.get(9));
			
			if(listOraclePlanDao.isEmpty()) {
				listOraclePlanDao.add(dao);
			} else {
				addChild(listOraclePlanDao, dao);
			}
		}
		
		tvQueryPlan.setLabelProvider(new OracleLabelProvider());
		tvQueryPlan.setContentProvider(new OraclePlanContentProvider());
		
		// 쿼리를 설정한 사용자가 설정 한 만큼 보여준다.
		tvQueryPlan.setInput(listOraclePlanDao);
		tvQueryPlan.expandAll();
		
		compositeTail.execute(getTailResultMsg());
	}
	
	private void addChild(List<OraclePlanDAO> list,  OraclePlanDAO dao){
		int size=0;
		for(OraclePlanDAO alOraclePlan : list) {
			if(alOraclePlan.getListChildren().size() > 0) {
				addChild(alOraclePlan.getListChildren(), dao);
				return;
			}			
			size++;			
			if (list.size() > size) continue;
			
			if((dao.getPos()-1) == alOraclePlan.getPos()) {
				alOraclePlan.getListChildren().add(dao);
				return;						
			}
		}
		list.add(dao);
		return;
	}

	/**
	 * crate tree column
	 * @param rsDAO
	 */
	private void createTreeColumn(QueryExecuteResultDTO rsDAO) {
		Map<Integer, String> mapColumn = rsDAO.getColumnLabelName();
		
		for (int i=1; i< mapColumn.size(); i++) {
			TreeViewerColumn treeViewerColumn = new TreeViewerColumn(tvQueryPlan, SWT.NONE);
			TreeColumn trclmnUrl = treeViewerColumn.getColumn();
			
			if( RDBTypeToJavaTypeUtils.isNumberType(rsDAO.getColumnType().get(i)) ) {
				trclmnUrl.setAlignment(SWT.RIGHT);
				trclmnUrl.setWidth(80);
			}else{
				trclmnUrl.setWidth(300);
			}
			
			trclmnUrl.setText(mapColumn.get(i));			
		}
	}
	
}
/**
 * content provider
 * 
 * @author hangum
 *
 */
class OraclePlanContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		List<OraclePlanDAO> listOraclePlanDao = (List)inputElement;
		return listOraclePlanDao.toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		OraclePlanDAO oraclePlanDao = (OraclePlanDAO)parentElement;
		
		return oraclePlanDao.getListChildren().toArray();
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		OraclePlanDAO oraclePlanDao = (OraclePlanDAO)element;
		
		return oraclePlanDao.getListChildren().isEmpty()?false:true;
	}
	
}

/**
 * label provider
 * 
 * @author hangum
 *
 */
class OracleLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		OraclePlanDAO dao = (OraclePlanDAO)element;

		switch(columnIndex) {
		case 0: return dao.getOperation();
		case 1: return dao.getName();
		case 2: return dao.getCost();
		case 3: return dao.getRows();
		case 4: return dao.getBytes();
		case 5: return ""+dao.getPos();
		case 6: return ""+dao.getAccess();
		case 7: return ""+dao.getFilter();
		case 8: return ""+dao.getObjectType();
		}
		
		return "*** not set column ***";
	}
	
}
